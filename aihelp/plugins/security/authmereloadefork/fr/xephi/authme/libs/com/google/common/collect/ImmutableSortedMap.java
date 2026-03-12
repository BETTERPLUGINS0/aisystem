package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
public final class ImmutableSortedMap<K, V> extends ImmutableSortedMapFauxverideShim<K, V> implements NavigableMap<K, V> {
   private static final Comparator<Comparable> NATURAL_ORDER = Ordering.natural();
   private static final ImmutableSortedMap<Comparable, Object> NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());
   private final transient RegularImmutableSortedSet<K> keySet;
   private final transient ImmutableList<V> valueList;
   @CheckForNull
   private transient ImmutableSortedMap<K, V> descendingMap;
   private static final long serialVersionUID = 0L;

   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
   }

   public static <T, K, V> Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(Comparator<? super K> comparator, Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction, mergeFunction);
   }

   static <K, V> ImmutableSortedMap<K, V> emptyMap(Comparator<? super K> comparator) {
      return Ordering.natural().equals(comparator) ? of() : new ImmutableSortedMap(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
   }

   public static <K, V> ImmutableSortedMap<K, V> of() {
      return NATURAL_EMPTY_MAP;
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1) {
      return of(Ordering.natural(), k1, v1);
   }

   private static <K, V> ImmutableSortedMap<K, V> of(Comparator<? super K> comparator, K k1, V v1) {
      return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.of(k1), (Comparator)Preconditions.checkNotNull(comparator)), ImmutableList.of(v1));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8), entryOf(k9, v9));
   }

   public static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
      return fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8), entryOf(k9, v9), entryOf(k10, v10));
   }

   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
      Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
      return copyOfInternal(map, naturalOrder);
   }

   public static <K, V> ImmutableSortedMap<K, V> copyOf(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
      return copyOfInternal(map, (Comparator)Preconditions.checkNotNull(comparator));
   }

   @Beta
   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
      Ordering<K> naturalOrder = (Ordering)NATURAL_ORDER;
      return copyOf((Iterable)entries, naturalOrder);
   }

   @Beta
   public static <K, V> ImmutableSortedMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries, Comparator<? super K> comparator) {
      return fromEntries((Comparator)Preconditions.checkNotNull(comparator), false, entries);
   }

   public static <K, V> ImmutableSortedMap<K, V> copyOfSorted(SortedMap<K, ? extends V> map) {
      Comparator<? super K> comparator = map.comparator();
      if (comparator == null) {
         comparator = NATURAL_ORDER;
      }

      if (map instanceof ImmutableSortedMap) {
         ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
         if (!kvMap.isPartialView()) {
            return kvMap;
         }
      }

      return fromEntries(comparator, true, map.entrySet());
   }

   private static <K, V> ImmutableSortedMap<K, V> copyOfInternal(Map<? extends K, ? extends V> map, Comparator<? super K> comparator) {
      boolean sameComparator = false;
      if (map instanceof SortedMap) {
         SortedMap<?, ?> sortedMap = (SortedMap)map;
         Comparator<?> comparator2 = sortedMap.comparator();
         sameComparator = comparator2 == null ? comparator == NATURAL_ORDER : comparator.equals(comparator2);
      }

      if (sameComparator && map instanceof ImmutableSortedMap) {
         ImmutableSortedMap<K, V> kvMap = (ImmutableSortedMap)map;
         if (!kvMap.isPartialView()) {
            return kvMap;
         }
      }

      return fromEntries(comparator, sameComparator, map.entrySet());
   }

   private static <K extends Comparable<? super K>, V> ImmutableSortedMap<K, V> fromEntries(Entry<K, V>... entries) {
      return fromEntries(Ordering.natural(), false, entries, entries.length);
   }

   private static <K, V> ImmutableSortedMap<K, V> fromEntries(Comparator<? super K> comparator, boolean sameComparator, Iterable<? extends Entry<? extends K, ? extends V>> entries) {
      Entry<K, V>[] entryArray = (Entry[])Iterables.toArray(entries, (Object[])EMPTY_ENTRY_ARRAY);
      return fromEntries(comparator, sameComparator, entryArray, entryArray.length);
   }

   private static <K, V> ImmutableSortedMap<K, V> fromEntries(final Comparator<? super K> comparator, boolean sameComparator, Entry<K, V>[] entryArray, int size) {
      switch(size) {
      case 0:
         return emptyMap(comparator);
      case 1:
         Entry<K, V> onlyEntry = (Entry)Objects.requireNonNull(entryArray[0]);
         return of(comparator, onlyEntry.getKey(), onlyEntry.getValue());
      default:
         Object[] keys = new Object[size];
         Object[] values = new Object[size];
         if (sameComparator) {
            for(int i = 0; i < size; ++i) {
               Entry<K, V> entry = (Entry)Objects.requireNonNull(entryArray[i]);
               Object key = entry.getKey();
               Object value = entry.getValue();
               CollectPreconditions.checkEntryNotNull(key, value);
               keys[i] = key;
               values[i] = value;
            }
         } else {
            Arrays.sort(entryArray, 0, size, new Comparator<Entry<K, V>>() {
               public int compare(@CheckForNull Entry<K, V> e1, @CheckForNull Entry<K, V> e2) {
                  Objects.requireNonNull(e1);
                  Objects.requireNonNull(e2);
                  return comparator.compare(e1.getKey(), e2.getKey());
               }
            });
            Entry<K, V> firstEntry = (Entry)Objects.requireNonNull(entryArray[0]);
            K prevKey = firstEntry.getKey();
            keys[0] = prevKey;
            values[0] = firstEntry.getValue();
            CollectPreconditions.checkEntryNotNull(keys[0], values[0]);

            for(int i = 1; i < size; ++i) {
               Entry<K, V> prevEntry = (Entry)Objects.requireNonNull(entryArray[i - 1]);
               Entry<K, V> entry = (Entry)Objects.requireNonNull(entryArray[i]);
               K key = entry.getKey();
               V value = entry.getValue();
               CollectPreconditions.checkEntryNotNull(key, value);
               keys[i] = key;
               values[i] = value;
               checkNoConflict(comparator.compare(prevKey, key) != 0, "key", prevEntry, entry);
               prevKey = key;
            }
         }

         return new ImmutableSortedMap(new RegularImmutableSortedSet(new RegularImmutableList(keys), comparator), new RegularImmutableList(values));
      }
   }

   public static <K extends Comparable<?>, V> ImmutableSortedMap.Builder<K, V> naturalOrder() {
      return new ImmutableSortedMap.Builder(Ordering.natural());
   }

   public static <K, V> ImmutableSortedMap.Builder<K, V> orderedBy(Comparator<K> comparator) {
      return new ImmutableSortedMap.Builder(comparator);
   }

   public static <K extends Comparable<?>, V> ImmutableSortedMap.Builder<K, V> reverseOrder() {
      return new ImmutableSortedMap.Builder(Ordering.natural().reverse());
   }

   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList) {
      this(keySet, valueList, (ImmutableSortedMap)null);
   }

   ImmutableSortedMap(RegularImmutableSortedSet<K> keySet, ImmutableList<V> valueList, @CheckForNull ImmutableSortedMap<K, V> descendingMap) {
      this.keySet = keySet;
      this.valueList = valueList;
      this.descendingMap = descendingMap;
   }

   public int size() {
      return this.valueList.size();
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      Preconditions.checkNotNull(action);
      ImmutableList<K> keyList = this.keySet.asList();

      for(int i = 0; i < this.size(); ++i) {
         action.accept(keyList.get(i), this.valueList.get(i));
      }

   }

   @CheckForNull
   public V get(@CheckForNull Object key) {
      int index = this.keySet.indexOf(key);
      return index == -1 ? null : this.valueList.get(index);
   }

   boolean isPartialView() {
      return this.keySet.isPartialView() || this.valueList.isPartialView();
   }

   public ImmutableSet<Entry<K, V>> entrySet() {
      return super.entrySet();
   }

   ImmutableSet<Entry<K, V>> createEntrySet() {
      class EntrySet extends ImmutableMapEntrySet<K, V> {
         public UnmodifiableIterator<Entry<K, V>> iterator() {
            return this.asList().iterator();
         }

         public Spliterator<Entry<K, V>> spliterator() {
            return this.asList().spliterator();
         }

         public void forEach(Consumer<? super Entry<K, V>> action) {
            this.asList().forEach(action);
         }

         ImmutableList<Entry<K, V>> createAsList() {
            return new ImmutableAsList<Entry<K, V>>() {
               public Entry<K, V> get(int index) {
                  return new SimpleImmutableEntry(ImmutableSortedMap.this.keySet.asList().get(index), ImmutableSortedMap.this.valueList.get(index));
               }

               public Spliterator<Entry<K, V>> spliterator() {
                  return CollectSpliterators.indexed(this.size(), 1297, this::get);
               }

               ImmutableCollection<Entry<K, V>> delegateCollection() {
                  return EntrySet.this;
               }
            };
         }

         ImmutableMap<K, V> map() {
            return ImmutableSortedMap.this;
         }
      }

      return (ImmutableSet)(this.isEmpty() ? ImmutableSet.of() : new EntrySet());
   }

   public ImmutableSortedSet<K> keySet() {
      return this.keySet;
   }

   ImmutableSet<K> createKeySet() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection<V> values() {
      return this.valueList;
   }

   ImmutableCollection<V> createValues() {
      throw new AssertionError("should never be called");
   }

   public Comparator<? super K> comparator() {
      return this.keySet().comparator();
   }

   public K firstKey() {
      return this.keySet().first();
   }

   public K lastKey() {
      return this.keySet().last();
   }

   private ImmutableSortedMap<K, V> getSubMap(int fromIndex, int toIndex) {
      if (fromIndex == 0 && toIndex == this.size()) {
         return this;
      } else {
         return fromIndex == toIndex ? emptyMap(this.comparator()) : new ImmutableSortedMap(this.keySet.getSubSet(fromIndex, toIndex), this.valueList.subList(fromIndex, toIndex));
      }
   }

   public ImmutableSortedMap<K, V> headMap(K toKey) {
      return this.headMap(toKey, false);
   }

   public ImmutableSortedMap<K, V> headMap(K toKey, boolean inclusive) {
      return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(toKey), inclusive));
   }

   public ImmutableSortedMap<K, V> subMap(K fromKey, K toKey) {
      return this.subMap(fromKey, true, toKey, false);
   }

   public ImmutableSortedMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
      Preconditions.checkNotNull(fromKey);
      Preconditions.checkNotNull(toKey);
      Preconditions.checkArgument(this.comparator().compare(fromKey, toKey) <= 0, "expected fromKey <= toKey but %s > %s", fromKey, toKey);
      return this.headMap(toKey, toInclusive).tailMap(fromKey, fromInclusive);
   }

   public ImmutableSortedMap<K, V> tailMap(K fromKey) {
      return this.tailMap(fromKey, true);
   }

   public ImmutableSortedMap<K, V> tailMap(K fromKey, boolean inclusive) {
      return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(fromKey), inclusive), this.size());
   }

   @CheckForNull
   public Entry<K, V> lowerEntry(K key) {
      return this.headMap(key, false).lastEntry();
   }

   @CheckForNull
   public K lowerKey(K key) {
      return Maps.keyOrNull(this.lowerEntry(key));
   }

   @CheckForNull
   public Entry<K, V> floorEntry(K key) {
      return this.headMap(key, true).lastEntry();
   }

   @CheckForNull
   public K floorKey(K key) {
      return Maps.keyOrNull(this.floorEntry(key));
   }

   @CheckForNull
   public Entry<K, V> ceilingEntry(K key) {
      return this.tailMap(key, true).firstEntry();
   }

   @CheckForNull
   public K ceilingKey(K key) {
      return Maps.keyOrNull(this.ceilingEntry(key));
   }

   @CheckForNull
   public Entry<K, V> higherEntry(K key) {
      return this.tailMap(key, false).firstEntry();
   }

   @CheckForNull
   public K higherKey(K key) {
      return Maps.keyOrNull(this.higherEntry(key));
   }

   @CheckForNull
   public Entry<K, V> firstEntry() {
      return this.isEmpty() ? null : (Entry)this.entrySet().asList().get(0);
   }

   @CheckForNull
   public Entry<K, V> lastEntry() {
      return this.isEmpty() ? null : (Entry)this.entrySet().asList().get(this.size() - 1);
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final Entry<K, V> pollFirstEntry() {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final Entry<K, V> pollLastEntry() {
      throw new UnsupportedOperationException();
   }

   public ImmutableSortedMap<K, V> descendingMap() {
      ImmutableSortedMap<K, V> result = this.descendingMap;
      if (result == null) {
         return this.isEmpty() ? emptyMap(Ordering.from(this.comparator()).reverse()) : new ImmutableSortedMap((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
      } else {
         return result;
      }
   }

   public ImmutableSortedSet<K> navigableKeySet() {
      return this.keySet;
   }

   public ImmutableSortedSet<K> descendingKeySet() {
      return this.keySet.descendingSet();
   }

   Object writeReplace() {
      return new ImmutableSortedMap.SerializedForm(this);
   }

   private static class SerializedForm<K, V> extends ImmutableMap.SerializedForm<K, V> {
      private final Comparator<? super K> comparator;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableSortedMap<K, V> sortedMap) {
         super(sortedMap);
         this.comparator = sortedMap.comparator();
      }

      ImmutableSortedMap.Builder<K, V> makeBuilder(int size) {
         return new ImmutableSortedMap.Builder(this.comparator);
      }
   }

   public static class Builder<K, V> extends ImmutableMap.Builder<K, V> {
      private final Comparator<? super K> comparator;

      public Builder(Comparator<? super K> comparator) {
         this.comparator = (Comparator)Preconditions.checkNotNull(comparator);
      }

      @CanIgnoreReturnValue
      public ImmutableSortedMap.Builder<K, V> put(K key, V value) {
         super.put(key, value);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSortedMap.Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
         super.put(entry);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSortedMap.Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
         super.putAll(map);
         return this;
      }

      @CanIgnoreReturnValue
      @Beta
      public ImmutableSortedMap.Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
         super.putAll(entries);
         return this;
      }

      /** @deprecated */
      @Deprecated
      @CanIgnoreReturnValue
      @Beta
      @DoNotCall("Always throws UnsupportedOperationException")
      public final ImmutableSortedMap.Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
         throw new UnsupportedOperationException("Not available on ImmutableSortedMap.Builder");
      }

      ImmutableSortedMap.Builder<K, V> combine(ImmutableMap.Builder<K, V> other) {
         super.combine(other);
         return this;
      }

      public ImmutableSortedMap<K, V> build() {
         return this.buildOrThrow();
      }

      public ImmutableSortedMap<K, V> buildOrThrow() {
         switch(this.size) {
         case 0:
            return ImmutableSortedMap.emptyMap(this.comparator);
         case 1:
            Entry<K, V> onlyEntry = (Entry)Objects.requireNonNull(this.entries[0]);
            return ImmutableSortedMap.of(this.comparator, onlyEntry.getKey(), onlyEntry.getValue());
         default:
            return ImmutableSortedMap.fromEntries(this.comparator, false, this.entries, this.size);
         }
      }

      /** @deprecated */
      @Deprecated
      @DoNotCall
      public final ImmutableSortedMap<K, V> buildKeepingLast() {
         throw new UnsupportedOperationException("ImmutableSortedMap.Builder does not yet implement buildKeepingLast()");
      }
   }
}
