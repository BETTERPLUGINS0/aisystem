package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface DoubleConsumer extends Consumer<Double>, java.util.function.DoubleConsumer {
   /** @deprecated */
   @Deprecated
   default void accept(Double t) {
      this.accept(t);
   }

   default DoubleConsumer andThen(java.util.function.DoubleConsumer after) {
      Objects.requireNonNull(after);
      return (t) -> {
         this.accept(t);
         after.accept(t);
      };
   }

   default DoubleConsumer andThen(DoubleConsumer after) {
      return this.andThen((java.util.function.DoubleConsumer)after);
   }

   /** @deprecated */
   @Deprecated
   default Consumer<Double> andThen(Consumer<? super Double> after) {
      return super.andThen(after);
   }
}
