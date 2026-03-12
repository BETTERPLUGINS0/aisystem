package ac.grim.grimac.shaded.incendo.cloud.exception;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import java.util.Iterator;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class NoSuchCommandException extends CommandParseException {
   private final String suppliedCommand;

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public NoSuchCommandException(@NonNull final Object commandSender, @NonNull final List<CommandComponent<?>> currentChain, @NonNull final String command) {
      super(commandSender, currentChain);
      this.suppliedCommand = command;
   }

   public String getMessage() {
      StringBuilder builder = new StringBuilder();
      Iterator var2 = this.currentChain().iterator();

      while(var2.hasNext()) {
         CommandComponent<?> commandComponent = (CommandComponent)var2.next();
         if (commandComponent != null) {
            builder.append(" ").append(commandComponent.name());
         }
      }

      return String.format("Unrecognized command input '%s' following chain%s", this.suppliedCommand, builder.toString());
   }

   @NonNull
   public String suppliedCommand() {
      return this.suppliedCommand;
   }

   public synchronized Throwable fillInStackTrace() {
      return this;
   }

   public synchronized Throwable initCause(final Throwable cause) {
      return this;
   }
}
