package ac.grim.grimac.shaded.incendo.cloud;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContextFactory;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.handling.ExceptionController;
import ac.grim.grimac.shaded.incendo.cloud.execution.CommandExecutor;
import ac.grim.grimac.shaded.incendo.cloud.execution.CommandResult;
import ac.grim.grimac.shaded.incendo.cloud.execution.ExecutionCoordinator;
import ac.grim.grimac.shaded.incendo.cloud.services.State;
import ac.grim.grimac.shaded.incendo.cloud.util.CompletableFutures;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

final class StandardCommandExecutor<C> implements CommandExecutor<C> {
   private final CommandManager<C> commandManager;
   private final ExecutionCoordinator<C> executionCoordinator;
   private final CommandContextFactory<C> commandContextFactory;

   StandardCommandExecutor(@NonNull final CommandManager<C> commandManager, @NonNull final ExecutionCoordinator<C> executionCoordinator, @NonNull final CommandContextFactory<C> commandContextFactory) {
      this.commandManager = commandManager;
      this.executionCoordinator = executionCoordinator;
      this.commandContextFactory = commandContextFactory;
   }

   @NonNull
   public CompletableFuture<CommandResult<C>> executeCommand(@NonNull final C commandSender, @NonNull final String input, @NonNull final Consumer<CommandContext<C>> contextConsumer) {
      CommandContext<C> context = this.commandContextFactory.create(false, commandSender);
      contextConsumer.accept(context);
      CommandInput commandInput = CommandInput.of(input);
      return this.executeCommand(context, commandInput).whenComplete((result, throwable) -> {
         if (throwable != null) {
            try {
               this.commandManager.exceptionController().handleException(context, ExceptionController.unwrapCompletionException(throwable));
            } catch (RuntimeException var5) {
               throw var5;
            } catch (Throwable var6) {
               throw new CompletionException(var6);
            }
         }
      });
   }

   @NonNull
   private CompletableFuture<CommandResult<C>> executeCommand(@NonNull final CommandContext<C> context, @NonNull final CommandInput commandInput) {
      context.store((String)"__raw_input__", commandInput.copy());

      try {
         if (this.commandManager.preprocessContext(context, commandInput) == State.ACCEPTED) {
            return this.executionCoordinator().coordinateExecution(this.commandManager.commandTree(), context, commandInput);
         }
      } catch (Exception var4) {
         return CompletableFutures.failedFuture(var4);
      }

      return CompletableFuture.completedFuture((Object)null);
   }

   @NonNull
   public ExecutionCoordinator<C> executionCoordinator() {
      return this.executionCoordinator;
   }
}
