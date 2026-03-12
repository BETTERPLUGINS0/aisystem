package ac.grim.grimac.shaded.incendo.cloud.internal;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface CommandRegistrationHandler<C> {
   @NonNull
   static <C> CommandRegistrationHandler<C> nullCommandRegistrationHandler() {
      return new CommandRegistrationHandler.NullCommandRegistrationHandler();
   }

   boolean registerCommand(@NonNull Command<C> command);

   @API(
      status = Status.STABLE
   )
   default void unregisterRootCommand(@NonNull final CommandComponent<C> rootCommand) {
   }

   @API(
      status = Status.INTERNAL,
      consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
   )
   public static final class NullCommandRegistrationHandler<C> implements CommandRegistrationHandler<C> {
      private NullCommandRegistrationHandler() {
      }

      public boolean registerCommand(@NonNull final Command<C> command) {
         return true;
      }

      public void unregisterRootCommand(@NonNull final CommandComponent<C> rootCommand) {
      }

      // $FF: synthetic method
      NullCommandRegistrationHandler(Object x0) {
         this();
      }
   }
}
