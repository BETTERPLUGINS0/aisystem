package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.Size64;
import java.util.Collection;
import java.util.Set;

public interface FloatSet extends FloatCollection, Set<Float> {
   FloatIterator iterator();

   default FloatSpliterator spliterator() {
      return FloatSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 321);
   }

   boolean remove(float var1);

   /** @deprecated */
   @Deprecated
   default boolean remove(Object o) {
      return FloatCollection.super.remove(o);
   }

   /** @deprecated */
   @Deprecated
   default boolean add(Float o) {
      return FloatCollection.super.add(o);
   }

   /** @deprecated */
   @Deprecated
   default boolean contains(Object o) {
      return FloatCollection.super.contains(o);
   }

   /** @deprecated */
   @Deprecated
   default boolean rem(float k) {
      return this.remove(k);
   }

   static FloatSet of() {
      return FloatSets.UNMODIFIABLE_EMPTY_SET;
   }

   static FloatSet of(float e) {
      return FloatSets.singleton(e);
   }

   static FloatSet of(float e0, float e1) {
      FloatArraySet innerSet = new FloatArraySet(2);
      innerSet.add(e0);
      if (!innerSet.add(e1)) {
         throw new IllegalArgumentException("Duplicate element: " + e1);
      } else {
         return FloatSets.unmodifiable(innerSet);
      }
   }

   static FloatSet of(float e0, float e1, float e2) {
      FloatArraySet innerSet = new FloatArraySet(3);
      innerSet.add(e0);
      if (!innerSet.add(e1)) {
         throw new IllegalArgumentException("Duplicate element: " + e1);
      } else if (!innerSet.add(e2)) {
         throw new IllegalArgumentException("Duplicate element: " + e2);
      } else {
         return FloatSets.unmodifiable(innerSet);
      }
   }

   static FloatSet of(float... a) {
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
         FloatSet innerSet = a.length <= 4 ? new FloatArraySet(a.length) : new FloatOpenHashSet(a.length);
         float[] var2 = a;
         int var3 = a.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            float element = var2[var4];
            if (!((FloatSet)innerSet).add(element)) {
               throw new IllegalArgumentException("Duplicate element: " + element);
            }
         }

         return FloatSets.unmodifiable((FloatSet)innerSet);
      }
   }
}
