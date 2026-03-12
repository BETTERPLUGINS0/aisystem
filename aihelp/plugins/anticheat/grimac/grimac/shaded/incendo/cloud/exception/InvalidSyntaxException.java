package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class InvalidSyntaxException extends CommandParseException {
   private final String correctSyntax;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public InvalidSyntaxException(@NonNull final String correctSyntax, @NonNull final Object commandSender, @NonNull final List<CommandComponent<?>> currentChain) {
      super(commandSender, currentChain);
      this.correctSyntax = correctSyntax;
   }

   @NonNull
   public String correctSyntax() {
      return this.correctSyntax;
   }

   public final String getMessage() {
      return String.format("Invalid command syntax. Correct syntax is: %s", this.correctSyntax);
   }
}
