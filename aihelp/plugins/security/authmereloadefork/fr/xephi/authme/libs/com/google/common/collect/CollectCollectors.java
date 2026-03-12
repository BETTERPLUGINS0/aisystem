package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Collector.Characteristics;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class CollectCollectors {
   private static final Collector<Object, ?, ImmutableList<Object>> TO_IMMUTABLE_LIST = Collector.of(ImmutableList::builder, ImmutableList.Builder::add, ImmutableList.Builder::combine, ImmutableList.Builder::build);
   private static final Collector<Object, ?, ImmutableSet<Object>> TO_IMMUTABLE_SET = Collector.of(ImmutableSet::builder, ImmutableSet.Builder::add, ImmutableSet.Builder::combine, ImmutableSet.Builder::build);
   @GwtIncompatible
   private static final Collector<Range<Comparable<?>>, ?, ImmutableRangeSet<Comparable<?>>> TO_IMMUTABLE_RANGE_SET = Collector.of(ImmutableRangeSet::builder, ImmutableRangeSet.Builder::add, ImmutableRangeSet.Builder::combine, ImmutableRangeSet.Builder::build);

   static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
      return TO_IMMUTABLE_LIST;
   }

   static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
      return TO_IMMUTABLE_SET;
   }

   static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(Comparator<? super E> comparator) {
      Preconditions.checkNotNull(comparator);
      return Collector.of(() -> {
         return new ImmutableSortedSet.Builder(comparator);
      }, ImmutableSortedSet.Builder::add, ImmutableSortedSet.Builder::combine, ImmutableSortedSet.Builder::build);
   }

   static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
      return CollectCollectors.EnumSetAccumulator.TO_IMMUTABLE_ENUM_SET;
   }

   @GwtIncompatible
   static <E extends Comparable<? super E>> Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
      return TO_IMMUTABLE_RANGE_SET;
   }

   static <T, E> Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(Function<? super T, ? extends E> elementFunction, ToIntFunction<? super T> countFunction) {
      Preconditions.checkNotNull(elementFunction);
      Preconditions.checkNotNull(countFunction);
      return Collector.of(LinkedHashMultiset::create, (multiset, t) -> {
         multiset.add(Preconditions.checkNotNull(elementFunction.apply(t)), countFunction.applyAsInt(t));
      }, (multiset1, multiset2) -> {
         multiset1.addAll(multiset2);
         return multiset1;
      }, (multiset) -> {
         return ImmutableMultiset.copyFromEntries(multiset.entrySet());
      });
   }

   static <T, E, M extends Multiset<E>> Collector<T, ?, M> toMultiset(Function<? super T, E> elementFunction, ToIntFunction<? super T> countFunction, Supplier<M> multisetSupplier) {
      Preconditions.checkNotNull(elementFunction);
      Preconditions.checkNotNull(countFunction);
      Preconditions.checkNotNull(multisetSupplier);
      return Collector.of(multisetSupplier, (ms, t) -> {
         ms.add(elementFunction.apply(t), countFunction.applyAsInt(t));
      }, (ms1, ms2) -> {
         ms1.addAll(ms2);
         return ms1;
      });
   }

   static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(ImmutableMap.Builder::new, (builder, input) -> {
         builder.put(keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableMap.Builder::combine, ImmutableMap.Builder::build);
   }

   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      Preconditions.checkNotNull(mergeFunction);
      return Collectors.collectingAndThen(Collectors.toMap(keyFunction, valueFunction, mergeFunction, LinkedHashMap::new), ImmutableMap::copyOf);
   }

   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(comparator);
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(() -> {
         return new ImmutableSortedMap.Builder(comparator);
      }, (builder, input) -> {
         builder.put(keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableSortedMap.Builder::combine, ImmutableSortedMap.Builder::build, Characteristics.UNORDERED);
   }

   static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      Preconditions.checkNotNull(comparator);
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      Preconditions.checkNotNull(mergeFunction);
      return Collectors.collectingAndThen(Collectors.toMap(keyFunction, valueFunction, mergeFunction, () -> {
         return new TreeMap(comparator);
      }), ImmutableSortedMap::copyOfSorted);
   }

   static <T, K, V> Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(ImmutableBiMap.Builder::new, (builder, input) -> {
         builder.put(keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableBiMap.Builder::combine, ImmutableBiMap.Builder::build);
   }

   static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(() -> {
         return new CollectCollectors.EnumMapAccumulator((v1, v2) -> {
            String var2 = String.valueOf(v1);
            String var3 = String.valueOf(v2);
            throw new IllegalArgumentException((new StringBuilder(27 + String.valueOf(var2).length() + String.valueOf(var3).length())).append("Multiple values for key: ").append(var2).append(", ").append(var3).toString());
         });
      }, (accum, t) -> {
         K key = (Enum)keyFunction.apply(t);
         V newValue = valueFunction.apply(t);
         accum.put((Enum)Preconditions.checkNotNull(key, "Null key for input %s", (Object)t), Preconditions.checkNotNull(newValue, "Null value for input %s", t));
      }, CollectCollectors.EnumMapAccumulator::combine, CollectCollectors.EnumMapAccumulator::toImmutableMap, Characteristics.UNORDERED);
   }

   static <T, K extends Enum<K>, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      Preconditions.checkNotNull(mergeFunction);
      return Collector.of(() -> {
         return new CollectCollectors.EnumMapAccumulator(mergeFunction);
      }, (accum, t) -> {
         K key = (Enum)keyFunction.apply(t);
         V newValue = valueFunction.apply(t);
         accum.put((Enum)Preconditions.checkNotNull(key, "Null key for input %s", (Object)t), Preconditions.checkNotNull(newValue, "Null value for input %s", t));
      }, CollectCollectors.EnumMapAccumulator::combine, CollectCollectors.EnumMapAccumulator::toImmutableMap);
   }

   @GwtIncompatible
   static <T, K extends Comparable<? super K>, V> Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(Function<? super T, Range<K>> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      return Collector.of(ImmutableRangeMap::builder, (builder, input) -> {
         builder.put((Range)keyFunction.apply(input), valueFunction.apply(input));
      }, ImmutableRangeMap.Builder::combine, ImmutableRangeMap.Builder::build);
   }

   static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction, "keyFunction");
      Preconditions.checkNotNull(valueFunction, "valueFunction");
      return Collector.of(ImmutableListMultimap::builder, (builder, t) -> {
         builder.put(keyFunction.apply(t), valueFunction.apply(t));
      }, ImmutableListMultimap.Builder::combine, ImmutableListMultimap.Builder::build);
   }

   static <T, K, V> Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valuesFunction);
      Function var10000 = (input) -> {
         return Preconditions.checkNotNull(keyFunction.apply(input));
      };
      Function var10001 = (input) -> {
         return ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull);
      };
      MultimapBuilder.ListMultimapBuilder var10002 = MultimapBuilder.linkedHashKeys().arrayListValues();
      Objects.requireNonNull(var10002);
      return Collectors.collectingAndThen(flatteningToMultimap(var10000, var10001, var10002::build), ImmutableListMultimap::copyOf);
   }

   static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      Preconditions.checkNotNull(keyFunction, "keyFunction");
      Preconditions.checkNotNull(valueFunction, "valueFunction");
      return Collector.of(ImmutableSetMultimap::builder, (builder, t) -> {
         builder.put(keyFunction.apply(t), valueFunction.apply(t));
      }, ImmutableSetMultimap.Builder::combine, ImmutableSetMultimap.Builder::build);
   }

   static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valuesFunction);
      Function var10000 = (input) -> {
         return Preconditions.checkNotNull(keyFunction.apply(input));
      };
      Function var10001 = (input) -> {
         return ((Stream)valuesFunction.apply(input)).peek(Preconditions::checkNotNull);
      };
      MultimapBuilder.SetMultimapBuilder var10002 = MultimapBuilder.linkedHashKeys().linkedHashSetValues();
      Objects.requireNonNull(var10002);
      return Collectors.collectingAndThen(flatteningToMultimap(var10000, var10001, var10002::build), ImmutableSetMultimap::copyOf);
   }

   static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> toMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, Supplier<M> multimapSupplier) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      Preconditions.checkNotNull(multimapSupplier);
      return Collector.of(multimapSupplier, (multimap, input) -> {
         multimap.put(keyFunction.apply(input), valueFunction.apply(input));
      }, (multimap1, multimap2) -> {
         multimap1.putAll(multimap2);
         return multimap1;
      });
   }

   static <T, K, V, M extends Multimap<K, V>> Collector<T, ?, M> flatteningToMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valueFunction, Supplier<M> multimapSupplier) {
      Preconditions.checkNotNull(keyFunction);
      Preconditions.checkNotNull(valueFunction);
      Preconditions.checkNotNull(multimapSupplier);
      return Collector.of(multimapSupplier, (multimap, input) -> {
         K key = keyFunction.apply(input);
         Collection<V> valuesForKey = multimap.get(key);
         Stream var10000 = (Stream)valueFunction.apply(input);
         Objects.requireNonNull(valuesForKey);
         var10000.forEachOrdered(valuesForKey::add);
      }, (multimap1, multimap2) -> {
         multimap1.putAll(multimap2);
         return multimap1;
      });
   }

   private static class EnumMapAccumulator<K extends Enum<K>, V> {
      private final BinaryOperator<V> mergeFunction;
      @CheckForNull
      private EnumMap<K, V> map = null;

      EnumMapAccumulator(BinaryOperator<V> mergeFunction) {
         this.mergeFunction = mergeFunction;
      }

      void put(K key, V value) {
         if (this.map == null) {
            this.map = new EnumMap(key.getDeclaringClass());
         }

         this.map.merge(key, value, this.mergeFunction);
      }

      CollectCollectors.EnumMapAccumulator<K, V> combine(CollectCollectors.EnumMapAccumulator<K, V> other) {
         if (this.map == null) {
            return other;
         } else if (other.map == null) {
            return this;
         } else {
            other.map.forEach(this::put);
            return this;
         }
      }

      ImmutableMap<K, V> toImmutableMap() {
         return this.map == null ? ImmutableMap.of() : ImmutableEnumMap.asImmutable(this.map);
      }
   }

   private static final class EnumSetAccumulator<E extends Enum<E>> {
      static final Collector<Enum<?>, ?, ImmutableSet<? extends Enum<?>>> TO_IMMUTABLE_ENUM_SET;
      @CheckForNull
      private EnumSet<E> set;

      void add(E e) {
         if (this.set == null) {
            this.set = EnumSet.of(e);
         } else {
            this.set.add(e);
         }

      }

      CollectCollectors.EnumSetAccumulator<E> combine(CollectCollectors.EnumSetAccumulator<E> other) {
         if (this.set == null) {
            return other;
         } else if (other.set == null) {
            return this;
         } else {
            this.set.addAll(other.set);
            return this;
         }
      }

      ImmutableSet<E> toImmutableSet() {
         return this.set == null ? ImmutableSet.of() : ImmutableEnumSet.asImmutable(this.set);
      }

      static {
         TO_IMMUTABLE_ENUM_SET = Collector.of(CollectCollectors.EnumSetAccumulator::new, CollectCollectors.EnumSetAccumulator::add, CollectCollectors.EnumSetAccumulator::combine, CollectCollectors.EnumSetAccumulator::toImmutableSet, Characteristics.UNORDERED);
      }
   }
}
