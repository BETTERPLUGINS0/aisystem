package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.objects.ObjectBidirectionalIterator;

public interface FloatBidirectionalIterator extends FloatIterator, ObjectBidirectionalIterator<Float> {
   float previousFloat();

   /** @deprecated */
   @Deprecated
   default Float previous() {
      return this.previousFloat();
   }

   default int back(int n) {
      int i = n;

      while(i-- != 0 && this.hasPrevious()) {
         this.previousFloat();
      }

      return n - i - 1;
   }

   default int skip(int n) {
      return FloatIterator.super.skip(n);
   }
}
