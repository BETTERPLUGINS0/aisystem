package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.BidirectionalIterator;

public interface ObjectBidirectionalIterator<K> extends ObjectIterator<K>, BidirectionalIterator<K> {
   default int back(int n) {
      int i = n;

      while(i-- != 0 && this.hasPrevious()) {
         this.previous();
      }

      return n - i - 1;
   }

   default int skip(int n) {
      return ObjectIterator.super.skip(n);
   }
}
