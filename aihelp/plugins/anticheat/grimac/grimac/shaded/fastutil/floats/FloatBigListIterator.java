package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.BigListIterator;
import ac.grim.grimac.shaded.fastutil.SafeMath;

public interface FloatBigListIterator extends FloatBidirectionalIterator, BigListIterator<Float> {
   default void set(float k) {
      throw new UnsupportedOperationException();
   }

   default void add(float k) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default void set(Float k) {
      this.set(k);
   }

   /** @deprecated */
   @Deprecated
   default void add(Float k) {
      this.add(k);
   }

   default long skip(long n) {
      long i = n;

      while(i-- != 0L && this.hasNext()) {
         this.nextFloat();
      }

      return n - i - 1L;
   }

   default long back(long n) {
      long i = n;

      while(i-- != 0L && this.hasPrevious()) {
         this.previousFloat();
      }

      return n - i - 1L;
   }

   default int skip(int n) {
      return SafeMath.safeLongToInt(this.skip((long)n));
   }
}
