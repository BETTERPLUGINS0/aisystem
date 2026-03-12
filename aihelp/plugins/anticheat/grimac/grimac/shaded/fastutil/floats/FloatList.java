package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.SafeMath;
import ac.grim.grimac.shaded.fastutil.Size64;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.DoubleUnaryOperator;
import java.util.function.UnaryOperator;

public interface FloatList extends List<Float>, Comparable<List<? extends Float>>, FloatCollection {
   FloatListIterator iterator();

   default FloatSpliterator spliterator() {
      return (FloatSpliterator)(this instanceof RandomAccess ? new AbstractFloatList.IndexBasedSpliterator(this, 0) : FloatSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 16720));
   }

   FloatListIterator listIterator();

   FloatListIterator listIterator(int var1);

   FloatList subList(int var1, int var2);

   void size(int var1);

   void getElements(int var1, float[] var2, int var3, int var4);

   void removeElements(int var1, int var2);

   void addElements(int var1, float[] var2);

   void addElements(int var1, float[] var2, int var3, int var4);

   default void setElements(float[] a) {
      this.setElements(0, a);
   }

   default void setElements(int index, float[] a) {
      this.setElements(index, a, 0, a.length);
   }

   default void setElements(int index, float[] a, int offset, int length) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
      } else if (index > this.size()) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
      } else {
         FloatArrays.ensureOffsetLength(a, offset, length);
         if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
         } else {
            FloatListIterator iter = this.listIterator(index);
            int i = 0;

            while(i < length) {
               iter.nextFloat();
               iter.set(a[offset + i++]);
            }

         }
      }
   }

   boolean add(float var1);

   void add(int var1, float var2);

   /** @deprecated */
   @Deprecated
   default void add(int index, Float key) {
      this.add(index, key);
   }

   boolean addAll(int var1, FloatCollection var2);

   float set(int var1, float var2);

   default void replaceAll(FloatUnaryOperator operator) {
      FloatListIterator iter = this.listIterator();

      while(iter.hasNext()) {
         iter.set(operator.apply(iter.nextFloat()));
      }

   }

   default void replaceAll(DoubleUnaryOperator operator) {
      this.replaceAll(operator instanceof FloatUnaryOperator ? (FloatUnaryOperator)operator : (x) -> {
         return SafeMath.safeDoubleToFloat(operator.applyAsDouble((double)x));
      });
   }

   /** @deprecated */
   @Deprecated
   default void replaceAll(UnaryOperator<Float> operator) {
      Objects.requireNonNull(operator);
      FloatUnaryOperator var10001;
      if (operator instanceof FloatUnaryOperator) {
         var10001 = (FloatUnaryOperator)operator;
      } else {
         Objects.requireNonNull(operator);
         var10001 = operator::apply;
      }

      this.replaceAll(var10001);
   }

   float getFloat(int var1);

   int indexOf(float var1);

   int lastIndexOf(float var1);

   /** @deprecated */
   @Deprecated
   default boolean contains(Object key) {
      return FloatCollection.super.contains(key);
   }

   /** @deprecated */
   @Deprecated
   default Float get(int index) {
      return this.getFloat(index);
   }

   /** @deprecated */
   @Deprecated
   default int indexOf(Object o) {
      return this.indexOf((Float)o);
   }

   /** @deprecated */
   @Deprecated
   default int lastIndexOf(Object o) {
      return this.lastIndexOf((Float)o);
   }

   /** @deprecated */
   @Deprecated
   default boolean add(Float k) {
      return this.add(k);
   }

   float removeFloat(int var1);

   /** @deprecated */
   @Deprecated
   default boolean remove(Object key) {
      return FloatCollection.super.remove(key);
   }

   /** @deprecated */
   @Deprecated
   default Float remove(int index) {
      return this.removeFloat(index);
   }

   /** @deprecated */
   @Deprecated
   default Float set(int index, Float k) {
      return this.set(index, k);
   }

   default boolean addAll(int index, FloatList l) {
      return this.addAll(index, (FloatCollection)l);
   }

   default boolean addAll(FloatList l) {
      return this.addAll(this.size(), l);
   }

   static FloatList of() {
      return FloatImmutableList.of();
   }

   static FloatList of(float e) {
      return FloatLists.singleton(e);
   }

   static FloatList of(float e0, float e1) {
      return FloatImmutableList.of(e0, e1);
   }

   static FloatList of(float e0, float e1, float e2) {
      return FloatImmutableList.of(e0, e1, e2);
   }

   static FloatList of(float... a) {
      switch(a.length) {
      case 0:
         return of();
      case 1:
         return of(a[0]);
      default:
         return FloatImmutableList.of(a);
      }
   }

   /** @deprecated */
   @Deprecated
   default void sort(Comparator<? super Float> comparator) {
      this.sort(FloatComparators.asFloatComparator(comparator));
   }

   default void sort(FloatComparator comparator) {
      float[] elements = this.toFloatArray();
      if (comparator == null) {
         FloatArrays.stableSort(elements);
      } else {
         FloatArrays.stableSort(elements, comparator);
      }

      this.setElements(elements);
   }

   /** @deprecated */
   @Deprecated
   default void unstableSort(Comparator<? super Float> comparator) {
      this.unstableSort(FloatComparators.asFloatComparator(comparator));
   }

   default void unstableSort(FloatComparator comparator) {
      float[] elements = this.toFloatArray();
      if (comparator == null) {
         FloatArrays.unstableSort(elements);
      } else {
         FloatArrays.unstableSort(elements, comparator);
      }

      this.setElements(elements);
   }
}
