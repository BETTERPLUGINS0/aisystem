package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.Objects;
import java.util.PrimitiveIterator.OfDouble;
import java.util.function.Consumer;

public interface DoubleIterator extends OfDouble {
   double nextDouble();

   /** @deprecated */
   @Deprecated
   default Double next() {
      return this.nextDouble();
   }

   default void forEachRemaining(DoubleConsumer action) {
      this.forEachRemaining((java.util.function.DoubleConsumer)action);
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

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextDouble();
         }

         return n - i - 1;
      }
   }
}
