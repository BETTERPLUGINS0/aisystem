package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Size64;
import java.util.Collection;
import java.util.Set;

public interface DoubleSet extends DoubleCollection, Set<Double> {
   DoubleIterator iterator();

   default DoubleSpliterator spliterator() {
      return DoubleSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 321);
   }

   boolean remove(double var1);

   /** @deprecated */
   @Deprecated
   default boolean remove(Object o) {
      return DoubleCollection.super.remove(o);
   }

   /** @deprecated */
   @Deprecated
   default boolean add(Double o) {
      return DoubleCollection.super.add(o);
   }

   /** @deprecated */
   @Deprecated
   default boolean contains(Object o) {
      return DoubleCollection.super.contains(o);
   }

   /** @deprecated */
   @Deprecated
   default boolean rem(double k) {
      return this.remove(k);
   }

   static DoubleSet of() {
      return DoubleSets.UNMODIFIABLE_EMPTY_SET;
   }

   static DoubleSet of(double e) {
      return DoubleSets.singleton(e);
   }

   static DoubleSet of(double e0, double e1) {
      DoubleArraySet innerSet = new DoubleArraySet(2);
      innerSet.add(e0);
      if (!innerSet.add(e1)) {
         throw new IllegalArgumentException("Duplicate element: " + e1);
      } else {
         return DoubleSets.unmodifiable(innerSet);
      }
   }

   static DoubleSet of(double e0, double e1, double e2) {
      DoubleArraySet innerSet = new DoubleArraySet(3);
      innerSet.add(e0);
      if (!innerSet.add(e1)) {
         throw new IllegalArgumentException("Duplicate element: " + e1);
      } else if (!innerSet.add(e2)) {
         throw new IllegalArgumentException("Duplicate element: " + e2);
      } else {
         return DoubleSets.unmodifiable(innerSet);
      }
   }

   static DoubleSet of(double... a) {
      switch(a.length) {
      case 0:
         return of();
      case 1:
         return of(a[0]);
      case 2:
         return of(a[0], a[1]);
      case 3:
         return of(a[0], a[1], a[2]);
      default:
         DoubleSet innerSet = a.length <= 4 ? new DoubleArraySet(a.length) : new DoubleOpenHashSet(a.length);
         double[] var2 = a;
         int var3 = a.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            double element = var2[var4];
            if (!((DoubleSet)innerSet).add(element)) {
               throw new IllegalArgumentException("Duplicate element: " + element);
            }
         }

         return DoubleSets.unmodifiable((DoubleSet)innerSet);
      }
   }
}
