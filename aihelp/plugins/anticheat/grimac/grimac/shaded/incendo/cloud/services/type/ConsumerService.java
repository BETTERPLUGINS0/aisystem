package ac.grim.grimac.shaded.incendo.cloud.services.type;

import ac.grim.grimac.shaded.incendo.cloud.services.State;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
public interface ConsumerService<Context> extends SideEffectService<Context>, Consumer<Context> {
   static void interrupt() throws ConsumerService.PipeBurst {
      throw new ConsumerService.PipeBurst();
   }

   @NonNull
   default State handle(@NonNull final Context context) {
      try {
         this.accept(context);
      } catch (ConsumerService.PipeBurst var3) {
         return State.ACCEPTED;
      }

      return State.REJECTED;
   }

   void accept(@NonNull Context context);

   public static final class PipeBurst extends RuntimeException {
      private PipeBurst() {
      }

      public synchronized Throwable fillInStackTrace() {
         return this;
      }

      public synchronized Throwable initCause(final Throwable cause) {
         return this;
      }

      // $FF: synthetic method
      PipeBurst(Object x0) {
         this();
      }
   }
}
