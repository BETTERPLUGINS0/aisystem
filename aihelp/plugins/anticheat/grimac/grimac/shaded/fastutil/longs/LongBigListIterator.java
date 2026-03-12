package ac.grim.grimac.shaded.fastutil.longs;

import ac.grim.grimac.shaded.fastutil.BigListIterator;
import ac.grim.grimac.shaded.fastutil.SafeMath;

public interface LongBigListIterator extends LongBidirectionalIterator, BigListIterator<Long> {
   default void set(long k) {
      throw new UnsupportedOperationException();
   }

   default void add(long k) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default void set(Long k) {
      this.set(k);
   }

   /** @deprecated */
   @Deprecated
   default void add(Long k) {
      this.add(k);
   }

   default long skip(long n) {
      long i = n;

      while(i-- != 0L && this.hasNext()) {
         this.nextLong();
      }

      return n - i - 1L;
   }

   default long back(long n) {
      long i = n;

      while(i-- != 0L && this.hasPrevious()) {
         this.previousLong();
      }

      return n - i - 1L;
   }

   default int skip(int n) {
      return SafeMath.safeLongToInt(this.skip((long)n));
   }
}
