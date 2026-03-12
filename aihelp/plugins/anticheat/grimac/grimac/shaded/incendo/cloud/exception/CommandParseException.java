package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import java.util.Collections;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public class CommandParseException extends IllegalArgumentException {
   private final Object commandSender;
   private final List<CommandComponent<?>> currentChain;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   protected CommandParseException(@NonNull final Object commandSender, @NonNull final List<CommandComponent<?>> currentChain) {
      this.commandSender = commandSender;
      this.currentChain = currentChain;
   }

   @NonNull
   public Object commandSender() {
      return this.commandSender;
   }

   @NonNull
   public List<CommandComponent<?>> currentChain() {
      return Collections.unmodifiableList(this.currentChain);
   }
}
