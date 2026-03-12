package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Size64;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface DoubleCollection extends Collection<Double>, DoubleIterable {
   DoubleIterator iterator();

   default DoubleIterator doubleIterator() {
      return this.iterator();
   }

   default DoubleSpliterator spliterator() {
      return DoubleSpliterators.asSpliterator(this.iterator(), Size64.sizeOf((Collection)this), 320);
   }

   default DoubleSpliterator doubleSpliterator() {
      return this.spliterator();
   }

   boolean add(double var1);

   boolean contains(double var1);

   boolean rem(double var1);

   /** @deprecated */
   @Deprecated
   default boolean add(Double key) {
      return this.add(key);
   }

   /** @deprecated */
   @Deprecated
   default boolean contains(Object key) {
      return key == null ? false : this.contains((Double)key);
   }

   /** @deprecated */
   @Deprecated
   default boolean remove(Object key) {
      return key == null ? false : this.rem((Double)key);
   }

   double[] toDoubleArray();

   /** @deprecated */
   @Deprecated
   default double[] toDoubleArray(double[] a) {
      return this.toArray(a);
   }

   double[] toArray(double[] var1);

   boolean addAll(DoubleCollection var1);

   boolean containsAll(DoubleCollection var1);

   boolean removeAll(DoubleCollection var1);

   /** @deprecated */
   @Deprecated
   default boolean removeIf(Predicate<? super Double> filter) {
      return this.removeIf(filter instanceof java.util.function.DoublePredicate ? (java.util.function.DoublePredicate)filter : (key) -> {
         return filter.test(key);
      });
   }

   default boolean removeIf(java.util.function.DoublePredicate filter) {
      Objects.requireNonNull(filter);
      boolean removed = false;
      DoubleIterator each = this.iterator();

      while(each.hasNext()) {
         if (filter.test(each.nextDouble())) {
            each.remove();
            removed = true;
         }
      }

      return removed;
   }

   default boolean removeIf(DoublePredicate filter) {
      return this.removeIf((java.util.function.DoublePredicate)filter);
   }

   boolean retainAll(DoubleCollection var1);

   /** @deprecated */
   @Deprecated
   default Stream<Double> stream() {
      return super.stream();
   }

   default DoubleStream doubleStream() {
      return StreamSupport.doubleStream(this.doubleSpliterator(), false);
   }

   /** @deprecated */
   @Deprecated
   default Stream<Double> parallelStream() {
      return super.parallelStream();
   }

   default DoubleStream doubleParallelStream() {
      return StreamSupport.doubleStream(this.doubleSpliterator(), true);
   }
}
