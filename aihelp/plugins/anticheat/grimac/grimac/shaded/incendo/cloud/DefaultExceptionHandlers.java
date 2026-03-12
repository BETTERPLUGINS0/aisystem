package ac.grim.grimac.shaded.incendo.cloud;

import ac.grim.grimac.shaded.incendo.cloud.caption.Caption;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.caption.StandardCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.exception.ArgumentParseException;
import ac.grim.grimac.shaded.incendo.cloud.exception.CommandExecutionException;
import ac.grim.grimac.shaded.incendo.cloud.exception.InvalidCommandSenderException;
import ac.grim.grimac.shaded.incendo.cloud.exception.InvalidSyntaxException;
import ac.grim.grimac.shaded.incendo.cloud.exception.NoPermissionException;
import ac.grim.grimac.shaded.incendo.cloud.exception.NoSuchCommandException;
import ac.grim.grimac.shaded.incendo.cloud.exception.handling.ExceptionContext;
import ac.grim.grimac.shaded.incendo.cloud.exception.handling.ExceptionController;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Pair;
import ac.grim.grimac.shaded.incendo.cloud.type.tuple.Triplet;
import ac.grim.grimac.shaded.incendo.cloud.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
final class DefaultExceptionHandlers<C> {
   private final Consumer<Triplet<CommandContext<C>, Caption, List<CaptionVariable>>> messageSender;
   private final Consumer<Pair<String, Throwable>> logger;
   private final ExceptionController<C> exceptionController;

   DefaultExceptionHandlers(@NonNull final Consumer<Triplet<CommandContext<C>, Caption, List<CaptionVariable>>> messageSender, @NonNull final Consumer<Pair<String, Throwable>> logger, @NonNull final ExceptionController<C> exceptionController) {
      this.messageSender = (Consumer)Objects.requireNonNull(messageSender, "messageSender");
      this.logger = (Consumer)Objects.requireNonNull(logger, "logger");
      this.exceptionController = (ExceptionController)Objects.requireNonNull(exceptionController, "exceptionController");
   }

   void register() {
      this.exceptionController.registerHandler(Throwable.class, (context) -> {
         this.sendMessage(context, StandardCaptionKeys.EXCEPTION_UNEXPECTED);
         this.log("An unhandled exception was thrown during command execution", context.exception());
      });
      this.exceptionController.registerHandler(CommandExecutionException.class, (context) -> {
         this.sendMessage(context, StandardCaptionKeys.EXCEPTION_UNEXPECTED);
         this.log("Exception executing command handler", ((CommandExecutionException)context.exception()).getCause());
      });
      this.exceptionController.registerHandler(ArgumentParseException.class, (context) -> {
         this.sendMessage(context, StandardCaptionKeys.EXCEPTION_INVALID_ARGUMENT, CaptionVariable.of("cause", ((ArgumentParseException)context.exception()).getCause().getMessage()));
      });
      this.exceptionController.registerHandler(NoSuchCommandException.class, (context) -> {
         this.sendMessage(context, StandardCaptionKeys.EXCEPTION_NO_SUCH_COMMAND, CaptionVariable.of("command", ((NoSuchCommandException)context.exception()).suppliedCommand()));
      });
      this.exceptionController.registerHandler(NoPermissionException.class, (context) -> {
         this.sendMessage(context, StandardCaptionKeys.EXCEPTION_NO_PERMISSION, CaptionVariable.of("permission", ((NoPermissionException)context.exception()).permissionResult().permission().permissionString()));
      });
      this.exceptionController.registerHandler(InvalidCommandSenderException.class, (context) -> {
         boolean multiple = ((InvalidCommandSenderException)context.exception()).requiredSenderTypes().size() != 1;
         String expected = multiple ? (String)((InvalidCommandSenderException)context.exception()).requiredSenderTypes().stream().map(TypeUtils::simpleName).collect(Collectors.joining(", ")) : TypeUtils.simpleName((Type)((InvalidCommandSenderException)context.exception()).requiredSenderTypes().iterator().next());
         this.sendMessage(context, multiple ? StandardCaptionKeys.EXCEPTION_INVALID_SENDER_LIST : StandardCaptionKeys.EXCEPTION_INVALID_SENDER, CaptionVariable.of("actual", context.context().sender().getClass().getSimpleName()), CaptionVariable.of("expected", expected));
      });
      this.exceptionController.registerHandler(InvalidSyntaxException.class, (context) -> {
         this.sendMessage(context, StandardCaptionKeys.EXCEPTION_INVALID_SYNTAX, CaptionVariable.of("syntax", ((InvalidSyntaxException)context.exception()).correctSyntax()));
      });
   }

   private void sendMessage(@NonNull final ExceptionContext<C, ?> context, @NonNull final Caption caption, @NonNull final CaptionVariable... variables) {
      this.sendMessage(context.context(), caption, variables);
   }

   private void sendMessage(@NonNull final CommandContext<C> context, @NonNull final Caption caption, @NonNull final CaptionVariable... variables) {
      this.messageSender.accept(Triplet.of(context, caption, Arrays.asList(variables)));
   }

   private void log(@NonNull final String message, @NonNull final Throwable exception) {
      this.logger.accept(Pair.of(message, exception));
   }
}
