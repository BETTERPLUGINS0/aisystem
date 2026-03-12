package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.BigListIterator;
import ac.grim.grimac.shaded.fastutil.SafeMath;

public interface DoubleBigListIterator extends DoubleBidirectionalIterator, BigListIterator<Double> {
   default void set(double k) {
      throw new UnsupportedOperationException();
   }

   default void add(double k) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default void set(Double k) {
      this.set(k);
   }

   /** @deprecated */
   @Deprecated
   default void add(Double k) {
      this.add(k);
   }

   default long skip(long n) {
      long i = n;

      while(i-- != 0L && this.hasNext()) {
         this.nextDouble();
      }

      return n - i - 1L;
   }

   default long back(long n) {
      long i = n;

      while(i-- != 0L && this.hasPrevious()) {
         this.previousDouble();
      }

      return n - i - 1L;
   }

   default int skip(int n) {
      return SafeMath.safeLongToInt(this.skip((long)n));
   }
}
