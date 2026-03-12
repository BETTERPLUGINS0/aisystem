package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.ListIterator;

public interface DoubleListIterator extends DoubleBidirectionalIterator, ListIterator<Double> {
   default void set(double k) {
      throw new UnsupportedOperationException();
   }

   default void add(double k) {
      throw new UnsupportedOperationException();
   }

   default void remove() {
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

   /** @deprecated */
   @Deprecated
   default Double next() {
      return DoubleBidirectionalIterator.super.next();
   }

   /** @deprecated */
   @Deprecated
   default Double previous() {
      return DoubleBidirectionalIterator.super.previous();
   }
}
