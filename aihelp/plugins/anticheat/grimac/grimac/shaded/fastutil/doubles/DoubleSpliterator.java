package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.Objects;
import java.util.Spliterator.OfDouble;
import java.util.function.Consumer;

public interface DoubleSpliterator extends OfDouble {
   /** @deprecated */
   @Deprecated
   default boolean tryAdvance(Consumer<? super Double> action) {
      java.util.function.DoubleConsumer var10001;
      if (action instanceof java.util.function.DoubleConsumer) {
         var10001 = (java.util.function.DoubleConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      return this.tryAdvance((java.util.function.DoubleConsumer)var10001);
   }

   default boolean tryAdvance(DoubleConsumer action) {
      return this.tryAdvance((java.util.function.DoubleConsumer)action);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Double> action) {
      java.util.function.DoubleConsumer var10001;
      if (action instanceof java.util.function.DoubleConsumer) {
         var10001 = (java.util.function.DoubleConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining((java.util.function.DoubleConsumer)var10001);
   }

   default void forEachRemaining(DoubleConsumer action) {
      this.forEachRemaining((java.util.function.DoubleConsumer)action);
   }

   default long skip(long n) {
      if (n < 0L) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         long i = n;

         while(i-- != 0L && this.tryAdvance((unused) -> {
         })) {
         }

         return n - i - 1L;
      }
   }

   DoubleSpliterator trySplit();

   default DoubleComparator getComparator() {
      throw new IllegalStateException();
   }
}
