package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class ArgumentParseException extends CommandParseException {
   private final Throwable cause;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public ArgumentParseException(@NonNull final Throwable throwable, @NonNull final Object commandSender, @NonNull final List<CommandComponent<?>> currentChain) {
      super(commandSender, currentChain);
      this.cause = throwable;
   }

   @NonNull
   public synchronized Throwable getCause() {
      return this.cause;
   }
}
