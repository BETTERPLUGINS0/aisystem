package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.STABLE
)
public class CommandExecutionException extends IllegalArgumentException {
   private final CommandContext<?> commandContext;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public CommandExecutionException(@NonNull final Throwable cause) {
      this(cause, (CommandContext)null);
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public CommandExecutionException(@NonNull final Throwable cause, @Nullable final CommandContext<?> commandContext) {
      super(cause);
      this.commandContext = commandContext;
   }

   @API(
      status = Status.STABLE
   )
   @Nullable
   public CommandContext<?> context() {
      return this.commandContext;
   }
}
