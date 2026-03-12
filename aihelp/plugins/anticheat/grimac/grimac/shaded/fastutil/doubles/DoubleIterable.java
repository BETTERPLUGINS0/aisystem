package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.Objects;
import java.util.function.Consumer;

public interface DoubleIterable extends Iterable<Double> {
   DoubleIterator iterator();

   default DoubleIterator doubleIterator() {
      return this.iterator();
   }

   default DoubleSpliterator spliterator() {
      return DoubleSpliterators.asSpliteratorUnknownSize(this.iterator(), 0);
   }

   default DoubleSpliterator doubleSpliterator() {
      return this.spliterator();
   }

   default void forEach(java.util.function.DoubleConsumer action) {
      Objects.requireNonNull(action);
      this.iterator().forEachRemaining((java.util.function.DoubleConsumer)action);
   }

   default void forEach(DoubleConsumer action) {
      this.forEach((java.util.function.DoubleConsumer)action);
   }

   /** @deprecated */
   @Deprecated
   default void forEach(Consumer<? super Double> action) {
      Objects.requireNonNull(action);
      java.util.function.DoubleConsumer var10001;
      if (action instanceof java.util.function.DoubleConsumer) {
         var10001 = (java.util.function.DoubleConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEach(var10001);
   }
}
