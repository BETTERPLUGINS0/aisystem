package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Comparators {
   private Comparators() {
   }

   public static <T, S extends T> Comparator<Iterable<S>> lexicographical(Comparator<T> comparator) {
      return new LexicographicalOrdering((Comparator)Preconditions.checkNotNull(comparator));
   }

   public static <T> boolean isInOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
      Preconditions.checkNotNull(comparator);
      Iterator<? extends T> it = iterable.iterator();
      Object next;
      if (it.hasNext()) {
         for(Object prev = it.next(); it.hasNext(); prev = next) {
            next = it.next();
            if (comparator.compare(prev, next) > 0) {
               return false;
            }
         }
      }

      return true;
   }

   public static <T> boolean isInStrictOrder(Iterable<? extends T> iterable, Comparator<T> comparator) {
      Preconditions.checkNotNull(comparator);
      Iterator<? extends T> it = iterable.iterator();
      Object next;
      if (it.hasNext()) {
         for(Object prev = it.next(); it.hasNext(); prev = next) {
            next = it.next();
            if (comparator.compare(prev, next) >= 0) {
               return false;
            }
         }
      }

      return true;
   }

   public static <T> Collector<T, ?, List<T>> least(int k, Comparator<? super T> comparator) {
      CollectPreconditions.checkNonnegative(k, "k");
      Preconditions.checkNotNull(comparator);
      return Collector.of(() -> {
         return TopKSelector.least(k, comparator);
      }, TopKSelector::offer, TopKSelector::combine, TopKSelector::topK, Characteristics.UNORDERED);
   }

   public static <T> Collector<T, ?, List<T>> greatest(int k, Comparator<? super T> comparator) {
      return least(k, comparator.reversed());
   }

   public static <T> Comparator<Optional<T>> emptiesFirst(Comparator<? super T> valueComparator) {
      Preconditions.checkNotNull(valueComparator);
      return Comparator.comparing((o) -> {
         return o.orElse((Object)null);
      }, Comparator.nullsFirst(valueComparator));
   }

   public static <T> Comparator<Optional<T>> emptiesLast(Comparator<? super T> valueComparator) {
      Preconditions.checkNotNull(valueComparator);
      return Comparator.comparing((o) -> {
         return o.orElse((Object)null);
      }, Comparator.nullsLast(valueComparator));
   }

   public static <T extends Comparable<? super T>> T min(T a, T b) {
      return a.compareTo(b) <= 0 ? a : b;
   }

   @ParametricNullness
   public static <T> T min(@ParametricNullness T a, @ParametricNullness T b, Comparator<T> comparator) {
      return comparator.compare(a, b) <= 0 ? a : b;
   }

   public static <T extends Comparable<? super T>> T max(T a, T b) {
      return a.compareTo(b) >= 0 ? a : b;
   }

   @ParametricNullness
   public static <T> T max(@ParametricNullness T a, @ParametricNullness T b, Comparator<T> comparator) {
      return comparator.compare(a, b) >= 0 ? a : b;
   }
}
