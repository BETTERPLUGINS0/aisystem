package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.STABLE
)
public final class InvalidCommandSenderException extends CommandParseException {
   private final Set<Type> requiredSenderTypes;
   @Nullable
   private final Command<?> command;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public InvalidCommandSenderException(@NonNull final Object commandSender, @NonNull final Type requiredSenderTypes, @NonNull final List<CommandComponent<?>> currentChain, @Nullable final Command<?> command) {
      this(commandSender, (Set)(new HashSet(Collections.singletonList(requiredSenderTypes))), currentChain, command);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public InvalidCommandSenderException(@NonNull final Object commandSender, @NonNull final Set<Type> requiredSenderTypes, @NonNull final List<CommandComponent<?>> currentChain, @Nullable final Command<?> command) {
      super(commandSender, currentChain);
      this.requiredSenderTypes = Collections.unmodifiableSet(requiredSenderTypes);
      this.command = command;
   }

   @NonNull
   public Set<Type> requiredSenderTypes() {
      return this.requiredSenderTypes;
   }

   public String getMessage() {
      return this.requiredSenderTypes.size() == 1 ? String.format("%s is not allowed to execute that command. Must be of type %s", this.commandSender().getClass().getSimpleName(), TypeUtils.simpleName((Type)this.requiredSenderTypes.iterator().next())) : String.format("%s is not allowed to execute that command. Must be one of %s", this.commandSender().getClass().getSimpleName(), this.requiredSenderTypes.stream().map(TypeUtils::simpleName).collect(Collectors.joining(", ")));
   }

   @API(
      status = Status.STABLE
   )
   @Nullable
   public Command<?> command() {
      return this.command;
   }
}
