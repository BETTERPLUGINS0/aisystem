package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.io.Serializable;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;

@DoNotMock("Use ImmutableMap.of or another implementation")
@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
   static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableSet<Entry<K, V>> entrySet;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableSet<K> keySet;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableCollection<V> values;
   @LazyInit
   @CheckForNull
   private transient ImmutableSetMultimap<K, V> multimapView;

   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
   }

   public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction, BinaryOperator<V> mergeFunction) {
      return CollectCollectors.toImmutableMap(keyFunction, valueFunction, mergeFunction);
   }

   public static <K, V> ImmutableMap<K, V> of() {
      return RegularImmutableMap.EMPTY;
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1) {
      return ImmutableBiMap.of(k1, v1);
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8), entryOf(k9, v9));
   }

   public static <K, V> ImmutableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
      return RegularImmutableMap.fromEntries(entryOf(k1, v1), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5), entryOf(k6, v6), entryOf(k7, v7), entryOf(k8, v8), entryOf(k9, v9), entryOf(k10, v10));
   }

   @SafeVarargs
   public static <K, V> ImmutableMap<K, V> ofEntries(Entry<? extends K, ? extends V>... entries) {
      return RegularImmutableMap.fromEntries(entries);
   }

   static <K, V> Entry<K, V> entryOf(K key, V value) {
      return new ImmutableMapEntry(key, value);
   }

   public static <K, V> ImmutableMap.Builder<K, V> builder() {
      return new ImmutableMap.Builder();
   }

   @Beta
   public static <K, V> ImmutableMap.Builder<K, V> builderWithExpectedSize(int expectedSize) {
      CollectPreconditions.checkNonnegative(expectedSize, "expectedSize");
      return new ImmutableMap.Builder(expectedSize);
   }

   static void checkNoConflict(boolean safe, String conflictDescription, Object entry1, Object entry2) {
      if (!safe) {
         throw conflictException(conflictDescription, entry1, entry2);
      }
   }

   static IllegalArgumentException conflictException(String conflictDescription, Object entry1, Object entry2) {
      String var3 = String.valueOf(entry1);
      String var4 = String.valueOf(entry2);
      return new IllegalArgumentException((new StringBuilder(34 + String.valueOf(conflictDescription).length() + String.valueOf(var3).length() + String.valueOf(var4).length())).append("Multiple entries with same ").append(conflictDescription).append(": ").append(var3).append(" and ").append(var4).toString());
   }

   public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
      ImmutableMap kvMap;
      if (map instanceof ImmutableMap && !(map instanceof SortedMap)) {
         kvMap = (ImmutableMap)map;
         if (!kvMap.isPartialView()) {
            return kvMap;
         }
      } else if (map instanceof EnumMap) {
         kvMap = copyOfEnumMap((EnumMap)map);
         return kvMap;
      }

      return copyOf((Iterable)map.entrySet());
   }

   @Beta
   public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
      Entry<K, V>[] entryArray = (Entry[])Iterables.toArray(entries, (Object[])EMPTY_ENTRY_ARRAY);
      switch(entryArray.length) {
      case 0:
         return of();
      case 1:
         Entry<K, V> onlyEntry = (Entry)Objects.requireNonNull(entryArray[0]);
         return of(onlyEntry.getKey(), onlyEntry.getValue());
      default:
         return RegularImmutableMap.fromEntries(entryArray);
      }
   }

   private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> original) {
      EnumMap<K, V> copy = new EnumMap(original);
      Iterator var2 = copy.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<K, V> entry = (Entry)var2.next();
         CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
      }

      return ImmutableEnumMap.asImmutable(copy);
   }

   ImmutableMap() {
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V put(K k, V v) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V putIfAbsent(K key, V value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean replace(K key, V oldValue, V newValue) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V replace(K key, V value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void putAll(Map<? extends K, ? extends V> map) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CheckForNull
   @DoNotCall("Always throws UnsupportedOperationException")
   public final V remove(@CheckForNull Object o) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.get(key) != null;
   }

   public boolean containsValue(@CheckForNull Object value) {
      return this.values().contains(value);
   }

   @CheckForNull
   public abstract V get(@CheckForNull Object var1);

   @CheckForNull
   public final V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
      V result = this.get(key);
      return result != null ? result : defaultValue;
   }

   public ImmutableSet<Entry<K, V>> entrySet() {
      ImmutableSet<Entry<K, V>> result = this.entrySet;
      return result == null ? (this.entrySet = this.createEntrySet()) : result;
   }

   abstract ImmutableSet<Entry<K, V>> createEntrySet();

   public ImmutableSet<K> keySet() {
      ImmutableSet<K> result = this.keySet;
      return result == null ? (this.keySet = this.createKeySet()) : result;
   }

   abstract ImmutableSet<K> createKeySet();

   UnmodifiableIterator<K> keyIterator() {
      final UnmodifiableIterator<Entry<K, V>> entryIterator = this.entrySet().iterator();
      return new UnmodifiableIterator<K>(this) {
         public boolean hasNext() {
            return entryIterator.hasNext();
         }

         public K next() {
            return ((Entry)entryIterator.next()).getKey();
         }
      };
   }

   Spliterator<K> keySpliterator() {
      return CollectSpliterators.map(this.entrySet().spliterator(), Entry::getKey);
   }

   public ImmutableCollection<V> values() {
      ImmutableCollection<V> result = this.values;
      return result == null ? (this.values = this.createValues()) : result;
   }

   abstract ImmutableCollection<V> createValues();

   public ImmutableSetMultimap<K, V> asMultimap() {
      if (this.isEmpty()) {
         return ImmutableSetMultimap.of();
      } else {
         ImmutableSetMultimap<K, V> result = this.multimapView;
         return result == null ? (this.multimapView = new ImmutableSetMultimap(new ImmutableMap.MapViewOfValuesAsSingletonSets(), this.size(), (Comparator)null)) : result;
      }
   }

   public boolean equals(@CheckForNull Object object) {
      return Maps.equalsImpl(this, object);
   }

   abstract boolean isPartialView();

   public int hashCode() {
      return Sets.hashCodeImpl(this.entrySet());
   }

   boolean isHashCodeFast() {
      return false;
   }

   public String toString() {
      return Maps.toStringImpl(this);
   }

   Object writeReplace() {
      return new ImmutableMap.SerializedForm(this);
   }

   static class SerializedForm<K, V> implements Serializable {
      private static final boolean USE_LEGACY_SERIALIZATION = true;
      private final Object keys;
      private final Object values;
      private static final long serialVersionUID = 0L;

      SerializedForm(ImmutableMap<K, V> map) {
         Object[] keys = new Object[map.size()];
         Object[] values = new Object[map.size()];
         int i = 0;

         for(UnmodifiableIterator var5 = map.entrySet().iterator(); var5.hasNext(); ++i) {
            Entry<? extends Object, ? extends Object> entry = (Entry)var5.next();
            keys[i] = entry.getKey();
            values[i] = entry.getValue();
         }

         this.keys = keys;
         this.values = values;
      }

      final Object readResolve() {
         if (!(this.keys instanceof ImmutableSet)) {
            return this.legacyReadResolve();
         } else {
            ImmutableSet<K> keySet = (ImmutableSet)this.keys;
            ImmutableCollection<V> values = (ImmutableCollection)this.values;
            ImmutableMap.Builder<K, V> builder = this.makeBuilder(keySet.size());
            UnmodifiableIterator<K> keyIter = keySet.iterator();
            UnmodifiableIterator valueIter = values.iterator();

            while(keyIter.hasNext()) {
               builder.put(keyIter.next(), valueIter.next());
            }

            return builder.buildOrThrow();
         }
      }

      final Object legacyReadResolve() {
         K[] keys = (Object[])this.keys;
         V[] values = (Object[])this.values;
         ImmutableMap.Builder<K, V> builder = this.makeBuilder(keys.length);

         for(int i = 0; i < keys.length; ++i) {
            builder.put(keys[i], values[i]);
         }

         return builder.buildOrThrow();
      }

      ImmutableMap.Builder<K, V> makeBuilder(int size) {
         return new ImmutableMap.Builder(size);
      }
   }

   private final class MapViewOfValuesAsSingletonSets extends ImmutableMap.IteratorBasedImmutableMap<K, ImmutableSet<V>> {
      private MapViewOfValuesAsSingletonSets() {
      }

      public int size() {
         return ImmutableMap.this.size();
      }

      ImmutableSet<K> createKeySet() {
         return ImmutableMap.this.keySet();
      }

      public boolean containsKey(@CheckForNull Object key) {
         return ImmutableMap.this.containsKey(key);
      }

      @CheckForNull
      public ImmutableSet<V> get(@CheckForNull Object key) {
         V outerValue = ImmutableMap.this.get(key);
         return outerValue == null ? null : ImmutableSet.of(outerValue);
      }

      boolean isPartialView() {
         return ImmutableMap.this.isPartialView();
      }

      public int hashCode() {
         return ImmutableMap.this.hashCode();
      }

      boolean isHashCodeFast() {
         return ImmutableMap.this.isHashCodeFast();
      }

      UnmodifiableIterator<Entry<K, ImmutableSet<V>>> entryIterator() {
         final Iterator<Entry<K, V>> backingIterator = ImmutableMap.this.entrySet().iterator();
         return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>(this) {
            public boolean hasNext() {
               return backingIterator.hasNext();
            }

            public Entry<K, ImmutableSet<V>> next() {
               final Entry<K, V> backingEntry = (Entry)backingIterator.next();
               return new AbstractMapEntry<K, ImmutableSet<V>>(this) {
                  public K getKey() {
                     return backingEntry.getKey();
                  }

                  public ImmutableSet<V> getValue() {
                     return ImmutableSet.of(backingEntry.getValue());
                  }
               };
            }
         };
      }

      // $FF: synthetic method
      MapViewOfValuesAsSingletonSets(Object x1) {
         this();
      }
   }

   abstract static class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
      abstract UnmodifiableIterator<Entry<K, V>> entryIterator();

      Spliterator<Entry<K, V>> entrySpliterator() {
         return Spliterators.spliterator(this.entryIterator(), (long)this.size(), 1297);
      }

      ImmutableSet<K> createKeySet() {
         return new ImmutableMapKeySet(this);
      }

      ImmutableSet<Entry<K, V>> createEntrySet() {
         class EntrySetImpl extends ImmutableMapEntrySet<K, V> {
            ImmutableMap<K, V> map() {
               return IteratorBasedImmutableMap.this;
            }

            public UnmodifiableIterator<Entry<K, V>> iterator() {
               return IteratorBasedImmutableMap.this.entryIterator();
            }
         }

         return new EntrySetImpl();
      }

      ImmutableCollection<V> createValues() {
         return new ImmutableMapValues(this);
      }
   }

   @DoNotMock
   public static class Builder<K, V> {
      @CheckForNull
      Comparator<? super V> valueComparator;
      Entry<K, V>[] entries;
      int size;
      boolean entriesUsed;

      public Builder() {
         this(4);
      }

      Builder(int initialCapacity) {
         this.entries = new Entry[initialCapacity];
         this.size = 0;
         this.entriesUsed = false;
      }

      private void ensureCapacity(int minCapacity) {
         if (minCapacity > this.entries.length) {
            this.entries = (Entry[])Arrays.copyOf(this.entries, ImmutableCollection.Builder.expandedCapacity(this.entries.length, minCapacity));
            this.entriesUsed = false;
         }

      }

      @CanIgnoreReturnValue
      public ImmutableMap.Builder<K, V> put(K key, V value) {
         this.ensureCapacity(this.size + 1);
         Entry<K, V> entry = ImmutableMap.entryOf(key, value);
         this.entries[this.size++] = entry;
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMap.Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
         return this.put(entry.getKey(), entry.getValue());
      }

      @CanIgnoreReturnValue
      public ImmutableMap.Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
         return this.putAll((Iterable)map.entrySet());
      }

      @CanIgnoreReturnValue
      @Beta
      public ImmutableMap.Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
         if (entries instanceof Collection) {
            this.ensureCapacity(this.size + ((Collection)entries).size());
         }

         Iterator var2 = entries.iterator();

         while(var2.hasNext()) {
            Entry<? extends K, ? extends V> entry = (Entry)var2.next();
            this.put(entry);
         }

         return this;
      }

      @CanIgnoreReturnValue
      @Beta
      public ImmutableMap.Builder<K, V> orderEntriesByValue(Comparator<? super V> valueComparator) {
         Preconditions.checkState(this.valueComparator == null, "valueComparator was already set");
         this.valueComparator = (Comparator)Preconditions.checkNotNull(valueComparator, "valueComparator");
         return this;
      }

      @CanIgnoreReturnValue
      ImmutableMap.Builder<K, V> combine(ImmutableMap.Builder<K, V> other) {
         Preconditions.checkNotNull(other);
         this.ensureCapacity(this.size + other.size);
         System.arraycopy(other.entries, 0, this.entries, this.size, other.size);
         this.size += other.size;
         return this;
      }

      private ImmutableMap<K, V> build(boolean throwIfDuplicateKeys) {
         switch(this.size) {
         case 0:
            return ImmutableMap.of();
         case 1:
            Entry<K, V> onlyEntry = (Entry)Objects.requireNonNull(this.entries[0]);
            return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
         default:
            int localSize = this.size;
            Entry[] localEntries;
            if (this.valueComparator == null) {
               localEntries = this.entries;
            } else {
               if (this.entriesUsed) {
                  this.entries = (Entry[])Arrays.copyOf(this.entries, this.size);
               }

               localEntries = this.entries;
               if (!throwIfDuplicateKeys) {
                  localEntries = lastEntryForEachKey(localEntries, this.size);
                  localSize = localEntries.length;
               }

               Arrays.sort(localEntries, 0, localSize, Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
            }

            this.entriesUsed = true;
            return RegularImmutableMap.fromEntryArray(localSize, localEntries, throwIfDuplicateKeys);
         }
      }

      public ImmutableMap<K, V> build() {
         return this.buildOrThrow();
      }

      public ImmutableMap<K, V> buildOrThrow() {
         return this.build(true);
      }

      public ImmutableMap<K, V> buildKeepingLast() {
         return this.build(false);
      }

      @VisibleForTesting
      ImmutableMap<K, V> buildJdkBacked() {
         Preconditions.checkState(this.valueComparator == null, "buildJdkBacked is only for testing; can't use valueComparator");
         switch(this.size) {
         case 0:
            return ImmutableMap.of();
         case 1:
            Entry<K, V> onlyEntry = (Entry)Objects.requireNonNull(this.entries[0]);
            return ImmutableMap.of(onlyEntry.getKey(), onlyEntry.getValue());
         default:
            this.entriesUsed = true;
            return JdkBackedImmutableMap.create(this.size, this.entries, true);
         }
      }

      private static <K, V> Entry<K, V>[] lastEntryForEachKey(Entry<K, V>[] entries, int size) {
         Set<K> seen = new HashSet();
         BitSet dups = new BitSet();

         for(int i = size - 1; i >= 0; --i) {
            if (!seen.add(entries[i].getKey())) {
               dups.set(i);
            }
         }

         if (dups.isEmpty()) {
            return entries;
         } else {
            Entry<K, V>[] newEntries = new Entry[size - dups.cardinality()];
            int inI = 0;

            for(int var6 = 0; inI < size; ++inI) {
               if (!dups.get(inI)) {
                  newEntries[var6++] = entries[inI];
               }
            }

            return newEntries;
         }
      }
   }
}
