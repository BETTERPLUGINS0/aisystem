package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Size64;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

public interface DoubleList extends List<Double>, Comparable<List<? extends Double>>, DoubleCollection {
   DoubleListIterator iterator();

   default DoubleSpliterator spliterator() {
      return (DoubleSpliterator)(this instanceof RandomAccess ? new AbstractDoubleList.IndexBasedSpliterator(this, 0) : DoubleSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 16720));
   }

   DoubleListIterator listIterator();

   DoubleListIterator listIterator(int var1);

   DoubleList subList(int var1, int var2);

   void size(int var1);

   void getElements(int var1, double[] var2, int var3, int var4);

   void removeElements(int var1, int var2);

   void addElements(int var1, double[] var2);

   void addElements(int var1, double[] var2, int var3, int var4);

   default void setElements(double[] a) {
      this.setElements(0, a);
   }

   default void setElements(int index, double[] a) {
      this.setElements(index, a, 0, a.length);
   }

   default void setElements(int index, double[] a, int offset, int length) {
      if (index < 0) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
      } else if (index > this.size()) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + this.size() + ")");
      } else {
         DoubleArrays.ensureOffsetLength(a, offset, length);
         if (index + length > this.size()) {
            throw new IndexOutOfBoundsException("End index (" + (index + length) + ") is greater than list size (" + this.size() + ")");
         } else {
            DoubleListIterator iter = this.listIterator(index);
            int i = 0;

            while(i < length) {
               iter.nextDouble();
               iter.set(a[offset + i++]);
            }

         }
      }
   }

   boolean add(double var1);

   void add(int var1, double var2);

   /** @deprecated */
   @Deprecated
   default void add(int index, Double key) {
      this.add(index, key);
   }

   boolean addAll(int var1, DoubleCollection var2);

   double set(int var1, double var2);

   default void replaceAll(java.util.function.DoubleUnaryOperator operator) {
      DoubleListIterator iter = this.listIterator();

      while(iter.hasNext()) {
         iter.set(operator.applyAsDouble(iter.nextDouble()));
      }

   }

   default void replaceAll(DoubleUnaryOperator operator) {
      this.replaceAll((java.util.function.DoubleUnaryOperator)operator);
   }

   /** @deprecated */
   @Deprecated
   default void replaceAll(UnaryOperator<Double> operator) {
      Objects.requireNonNull(operator);
      java.util.function.DoubleUnaryOperator var10001;
      if (operator instanceof java.util.function.DoubleUnaryOperator) {
         var10001 = (java.util.function.DoubleUnaryOperator)operator;
      } else {
         Objects.requireNonNull(operator);
         var10001 = operator::apply;
      }

      this.replaceAll(var10001);
   }

   double getDouble(int var1);

   int indexOf(double var1);

   int lastIndexOf(double var1);

   /** @deprecated */
   @Deprecated
   default boolean contains(Object key) {
      return DoubleCollection.super.contains(key);
   }

   /** @deprecated */
   @Deprecated
   default Double get(int index) {
      return this.getDouble(index);
   }

   /** @deprecated */
   @Deprecated
   default int indexOf(Object o) {
      return this.indexOf((Double)o);
   }

   /** @deprecated */
   @Deprecated
   default int lastIndexOf(Object o) {
      return this.lastIndexOf((Double)o);
   }

   /** @deprecated */
   @Deprecated
   default boolean add(Double k) {
      return this.add(k);
   }

   double removeDouble(int var1);

   /** @deprecated */
   @Deprecated
   default boolean remove(Object key) {
      return DoubleCollection.super.remove(key);
   }

   /** @deprecated */
   @Deprecated
   default Double remove(int index) {
      return this.removeDouble(index);
   }

   /** @deprecated */
   @Deprecated
   default Double set(int index, Double k) {
      return this.set(index, k);
   }

   default boolean addAll(int index, DoubleList l) {
      return this.addAll(index, (DoubleCollection)l);
   }

   default boolean addAll(DoubleList l) {
      return this.addAll(this.size(), l);
   }

   static DoubleList of() {
      return DoubleImmutableList.of();
   }

   static DoubleList of(double e) {
      return DoubleLists.singleton(e);
   }

   static DoubleList of(double e0, double e1) {
      return DoubleImmutableList.of(e0, e1);
   }

   static DoubleList of(double e0, double e1, double e2) {
      return DoubleImmutableList.of(e0, e1, e2);
   }

   static DoubleList of(double... a) {
      switch(a.length) {
      case 0:
         return of();
      case 1:
         return of(a[0]);
      default:
         return DoubleImmutableList.of(a);
      }
   }

   /** @deprecated */
   @Deprecated
   default void sort(Comparator<? super Double> comparator) {
      this.sort(DoubleComparators.asDoubleComparator(comparator));
   }

   default void sort(DoubleComparator comparator) {
      double[] elements = this.toDoubleArray();
      if (comparator == null) {
         DoubleArrays.stableSort(elements);
      } else {
         DoubleArrays.stableSort(elements, comparator);
      }

      this.setElements(elements);
   }

   /** @deprecated */
   @Deprecated
   default void unstableSort(Comparator<? super Double> comparator) {
      this.unstableSort(DoubleComparators.asDoubleComparator(comparator));
   }

   default void unstableSort(DoubleComparator comparator) {
      double[] elements = this.toDoubleArray();
      if (comparator == null) {
         DoubleArrays.unstableSort(elements);
      } else {
         DoubleArrays.unstableSort(elements, comparator);
      }

      this.setElements(elements);
   }
}
