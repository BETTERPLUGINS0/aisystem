package ac.grim.grimac.shaded.fastutil.floats;

import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public interface FloatIterator extends PrimitiveIterator<Float, FloatConsumer> {
   float nextFloat();

   /** @deprecated */
   @Deprecated
   default Float next() {
      return this.nextFloat();
   }

   default void forEachRemaining(FloatConsumer action) {
      Objects.requireNonNull(action);

      while(this.hasNext()) {
         action.accept(this.nextFloat());
      }

   }

   default void forEachRemaining(DoubleConsumer action) {
      Objects.requireNonNull(action);
      FloatConsumer var10001;
      if (action instanceof FloatConsumer) {
         var10001 = (FloatConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining(var10001);
   }

   /** @deprecated */
   @Deprecated
   default void forEachRemaining(Consumer<? super Float> action) {
      FloatConsumer var10001;
      if (action instanceof FloatConsumer) {
         var10001 = (FloatConsumer)action;
      } else {
         Objects.requireNonNull(action);
         var10001 = action::accept;
      }

      this.forEachRemaining(var10001);
   }

   default int skip(int n) {
      if (n < 0) {
         throw new IllegalArgumentException("Argument must be nonnegative: " + n);
      } else {
         int i = n;

         while(i-- != 0 && this.hasNext()) {
            this.nextFloat();
         }

         return n - i - 1;
      }
   }
}
