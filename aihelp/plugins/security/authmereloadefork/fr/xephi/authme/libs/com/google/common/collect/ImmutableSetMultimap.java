package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.MoreObjects;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent.LazyInit;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import fr.xephi.authme.libs.com.google.j2objc.annotations.Weak;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   serializable = true,
   emulated = true
)
public class ImmutableSetMultimap<K, V> extends ImmutableMultimap<K, V> implements SetMultimap<K, V> {
   private final transient ImmutableSet<V> emptySet;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableSetMultimap<V, K> inverse;
   @LazyInit
   @CheckForNull
   @RetainedWith
   private transient ImmutableSet<Entry<K, V>> entries;
   @GwtIncompatible
   private static final long serialVersionUID = 0L;

   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends V> valueFunction) {
      return CollectCollectors.toImmutableSetMultimap(keyFunction, valueFunction);
   }

   public static <T, K, V> Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(Function<? super T, ? extends K> keyFunction, Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
      return CollectCollectors.flatteningToImmutableSetMultimap(keyFunction, valuesFunction);
   }

   public static <K, V> ImmutableSetMultimap<K, V> of() {
      return EmptyImmutableSetMultimap.INSTANCE;
   }

   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1) {
      ImmutableSetMultimap.Builder<K, V> builder = builder();
      builder.put(k1, v1);
      return builder.build();
   }

   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2) {
      ImmutableSetMultimap.Builder<K, V> builder = builder();
      builder.put(k1, v1);
      builder.put(k2, v2);
      return builder.build();
   }

   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
      ImmutableSetMultimap.Builder<K, V> builder = builder();
      builder.put(k1, v1);
      builder.put(k2, v2);
      builder.put(k3, v3);
      return builder.build();
   }

   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
      ImmutableSetMultimap.Builder<K, V> builder = builder();
      builder.put(k1, v1);
      builder.put(k2, v2);
      builder.put(k3, v3);
      builder.put(k4, v4);
      return builder.build();
   }

   public static <K, V> ImmutableSetMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
      ImmutableSetMultimap.Builder<K, V> builder = builder();
      builder.put(k1, v1);
      builder.put(k2, v2);
      builder.put(k3, v3);
      builder.put(k4, v4);
      builder.put(k5, v5);
      return builder.build();
   }

   public static <K, V> ImmutableSetMultimap.Builder<K, V> builder() {
      return new ImmutableSetMultimap.Builder();
   }

   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
      return copyOf(multimap, (Comparator)null);
   }

   private static <K, V> ImmutableSetMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap, @CheckForNull Comparator<? super V> valueComparator) {
      Preconditions.checkNotNull(multimap);
      if (multimap.isEmpty() && valueComparator == null) {
         return of();
      } else {
         if (multimap instanceof ImmutableSetMultimap) {
            ImmutableSetMultimap<K, V> kvMultimap = (ImmutableSetMultimap)multimap;
            if (!kvMultimap.isPartialView()) {
               return kvMultimap;
            }
         }

         return fromMapEntries(multimap.asMap().entrySet(), valueComparator);
      }
   }

   @Beta
   public static <K, V> ImmutableSetMultimap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
      return (new ImmutableSetMultimap.Builder()).putAll(entries).build();
   }

   static <K, V> ImmutableSetMultimap<K, V> fromMapEntries(Collection<? extends Entry<? extends K, ? extends Collection<? extends V>>> mapEntries, @CheckForNull Comparator<? super V> valueComparator) {
      if (mapEntries.isEmpty()) {
         return of();
      } else {
         ImmutableMap.Builder<K, ImmutableSet<V>> builder = new ImmutableMap.Builder(mapEntries.size());
         int size = 0;
         Iterator var4 = mapEntries.iterator();

         while(var4.hasNext()) {
            Entry<? extends K, ? extends Collection<? extends V>> entry = (Entry)var4.next();
            K key = entry.getKey();
            Collection<? extends V> values = (Collection)entry.getValue();
            ImmutableSet<V> set = valueSet(valueComparator, values);
            if (!set.isEmpty()) {
               builder.put(key, set);
               size += set.size();
            }
         }

         return new ImmutableSetMultimap(builder.buildOrThrow(), size, valueComparator);
      }
   }

   ImmutableSetMultimap(ImmutableMap<K, ImmutableSet<V>> map, int size, @CheckForNull Comparator<? super V> valueComparator) {
      super(map, size);
      this.emptySet = emptySet(valueComparator);
   }

   public ImmutableSet<V> get(K key) {
      ImmutableSet<V> set = (ImmutableSet)this.map.get(key);
      return (ImmutableSet)MoreObjects.firstNonNull(set, this.emptySet);
   }

   public ImmutableSetMultimap<V, K> inverse() {
      ImmutableSetMultimap<V, K> result = this.inverse;
      return result == null ? (this.inverse = this.invert()) : result;
   }

   private ImmutableSetMultimap<V, K> invert() {
      ImmutableSetMultimap.Builder<V, K> builder = builder();
      UnmodifiableIterator var2 = this.entries().iterator();

      while(var2.hasNext()) {
         Entry<K, V> entry = (Entry)var2.next();
         builder.put(entry.getValue(), entry.getKey());
      }

      ImmutableSetMultimap<V, K> invertedMultimap = builder.build();
      invertedMultimap.inverse = this;
      return invertedMultimap;
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final ImmutableSet<V> removeAll(@CheckForNull Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final ImmutableSet<V> replaceValues(K key, Iterable<? extends V> values) {
      throw new UnsupportedOperationException();
   }

   public ImmutableSet<Entry<K, V>> entries() {
      ImmutableSet<Entry<K, V>> result = this.entries;
      return result == null ? (this.entries = new ImmutableSetMultimap.EntrySet(this)) : result;
   }

   private static <V> ImmutableSet<V> valueSet(@CheckForNull Comparator<? super V> valueComparator, Collection<? extends V> values) {
      return (ImmutableSet)(valueComparator == null ? ImmutableSet.copyOf(values) : ImmutableSortedSet.copyOf(valueComparator, values));
   }

   private static <V> ImmutableSet<V> emptySet(@CheckForNull Comparator<? super V> valueComparator) {
      return (ImmutableSet)(valueComparator == null ? ImmutableSet.of() : ImmutableSortedSet.emptySet(valueComparator));
   }

   private static <V> ImmutableSet.Builder<V> valuesBuilder(@CheckForNull Comparator<? super V> valueComparator) {
      return (ImmutableSet.Builder)(valueComparator == null ? new ImmutableSet.Builder() : new ImmutableSortedSet.Builder(valueComparator));
   }

   @GwtIncompatible
   private void writeObject(ObjectOutputStream stream) throws IOException {
      stream.defaultWriteObject();
      stream.writeObject(this.valueComparator());
      Serialization.writeMultimap(this, stream);
   }

   @CheckForNull
   Comparator<? super V> valueComparator() {
      return this.emptySet instanceof ImmutableSortedSet ? ((ImmutableSortedSet)this.emptySet).comparator() : null;
   }

   @GwtIncompatible
   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
      stream.defaultReadObject();
      Comparator<Object> valueComparator = (Comparator)stream.readObject();
      int keyCount = stream.readInt();
      if (keyCount < 0) {
         throw new InvalidObjectException((new StringBuilder(29)).append("Invalid key count ").append(keyCount).toString());
      } else {
         ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
         int tmpSize = 0;

         for(int i = 0; i < keyCount; ++i) {
            Object key = stream.readObject();
            int valueCount = stream.readInt();
            if (valueCount <= 0) {
               throw new InvalidObjectException((new StringBuilder(31)).append("Invalid value count ").append(valueCount).toString());
            }

            ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);

            for(int j = 0; j < valueCount; ++j) {
               valuesBuilder.add(stream.readObject());
            }

            ImmutableSet<Object> valueSet = valuesBuilder.build();
            if (valueSet.size() != valueCount) {
               String var11 = String.valueOf(key);
               throw new InvalidObjectException((new StringBuilder(40 + String.valueOf(var11).length())).append("Duplicate key-value pairs exist for key ").append(var11).toString());
            }

            builder.put(key, valueSet);
            tmpSize += valueCount;
         }

         ImmutableMap tmpMap;
         try {
            tmpMap = builder.buildOrThrow();
         } catch (IllegalArgumentException var12) {
            throw (InvalidObjectException)(new InvalidObjectException(var12.getMessage())).initCause(var12);
         }

         ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, tmpMap);
         ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, tmpSize);
         ImmutableSetMultimap.SetFieldSettersHolder.EMPTY_SET_FIELD_SETTER.set(this, emptySet(valueComparator));
      }
   }

   @GwtIncompatible
   private static final class SetFieldSettersHolder {
      static final Serialization.FieldSetter<ImmutableSetMultimap> EMPTY_SET_FIELD_SETTER = Serialization.getFieldSetter(ImmutableSetMultimap.class, "emptySet");
   }

   private static final class EntrySet<K, V> extends ImmutableSet<Entry<K, V>> {
      @Weak
      private final transient ImmutableSetMultimap<K, V> multimap;

      EntrySet(ImmutableSetMultimap<K, V> multimap) {
         this.multimap = multimap;
      }

      public boolean contains(@CheckForNull Object object) {
         if (object instanceof Entry) {
            Entry<?, ?> entry = (Entry)object;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
         } else {
            return false;
         }
      }

      public int size() {
         return this.multimap.size();
      }

      public UnmodifiableIterator<Entry<K, V>> iterator() {
         return this.multimap.entryIterator();
      }

      boolean isPartialView() {
         return false;
      }
   }

   public static final class Builder<K, V> extends ImmutableMultimap.Builder<K, V> {
      Collection<V> newMutableValueCollection() {
         return Platform.preservesInsertionOrderOnAddsSet();
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> put(K key, V value) {
         super.put(key, value);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
         super.put(entry);
         return this;
      }

      @CanIgnoreReturnValue
      @Beta
      public ImmutableSetMultimap.Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
         super.putAll(entries);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> putAll(K key, Iterable<? extends V> values) {
         super.putAll(key, values);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> putAll(K key, V... values) {
         return this.putAll(key, (Iterable)Arrays.asList(values));
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
         Iterator var2 = multimap.asMap().entrySet().iterator();

         while(var2.hasNext()) {
            Entry<? extends K, ? extends Collection<? extends V>> entry = (Entry)var2.next();
            this.putAll(entry.getKey(), (Iterable)entry.getValue());
         }

         return this;
      }

      @CanIgnoreReturnValue
      ImmutableSetMultimap.Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
         super.combine(other);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
         super.orderKeysBy(keyComparator);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableSetMultimap.Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
         super.orderValuesBy(valueComparator);
         return this;
      }

      public ImmutableSetMultimap<K, V> build() {
         Collection<Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
         if (this.keyComparator != null) {
            mapEntries = Ordering.from(this.keyComparator).onKeys().immutableSortedCopy((Iterable)mapEntries);
         }

         return ImmutableSetMultimap.fromMapEntries((Collection)mapEntries, this.valueComparator);
      }
   }
}
