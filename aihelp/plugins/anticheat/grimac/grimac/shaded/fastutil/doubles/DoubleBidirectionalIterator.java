package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.objects.ObjectBidirectionalIterator;

public interface DoubleBidirectionalIterator extends DoubleIterator, ObjectBidirectionalIterator<Double> {
   double previousDouble();

   /** @deprecated */
   @Deprecated
   default Double previous() {
      return this.previousDouble();
   }

   default int back(int n) {
      int i = n;

      while(i-- != 0 && this.hasPrevious()) {
         this.previousDouble();
      }

      return n - i - 1;
   }

   default int skip(int n) {
      return DoubleIterator.super.skip(n);
   }
}
