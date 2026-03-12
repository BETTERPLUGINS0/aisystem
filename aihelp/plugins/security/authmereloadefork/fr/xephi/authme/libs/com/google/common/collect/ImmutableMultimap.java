package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotCall;
import fr.xephi.authme.libs.com.google.errorprone.annotations.DoNotMock;
import fr.xephi.authme.libs.com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
public abstract class ImmutableMultimap<K, V> extends BaseImmutableMultimap<K, V> implements Serializable {
   final transient ImmutableMap<K, ? extends ImmutableCollection<V>> map;
   final transient int size;
   private static final long serialVersionUID = 0L;

   public static <K, V> ImmutableMultimap<K, V> of() {
      return ImmutableListMultimap.of();
   }

   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1) {
      return ImmutableListMultimap.of(k1, v1);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2) {
      return ImmutableListMultimap.of(k1, v1, k2, v2);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
      return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
      return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4);
   }

   public static <K, V> ImmutableMultimap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
      return ImmutableListMultimap.of(k1, v1, k2, v2, k3, v3, k4, v4, k5, v5);
   }

   public static <K, V> ImmutableMultimap.Builder<K, V> builder() {
      return new ImmutableMultimap.Builder();
   }

   public static <K, V> ImmutableMultimap<K, V> copyOf(Multimap<? extends K, ? extends V> multimap) {
      if (multimap instanceof ImmutableMultimap) {
         ImmutableMultimap<K, V> kvMultimap = (ImmutableMultimap)multimap;
         if (!kvMultimap.isPartialView()) {
            return kvMultimap;
         }
      }

      return ImmutableListMultimap.copyOf(multimap);
   }

   @Beta
   public static <K, V> ImmutableMultimap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
      return ImmutableListMultimap.copyOf(entries);
   }

   ImmutableMultimap(ImmutableMap<K, ? extends ImmutableCollection<V>> map, int size) {
      this.map = map;
      this.size = size;
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public ImmutableCollection<V> removeAll(@CheckForNull Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public ImmutableCollection<V> replaceValues(K key, Iterable<? extends V> values) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @DoNotCall("Always throws UnsupportedOperationException")
   public final void clear() {
      throw new UnsupportedOperationException();
   }

   public abstract ImmutableCollection<V> get(K var1);

   public abstract ImmutableMultimap<V, K> inverse();

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean put(K key, V value) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean putAll(K key, Iterable<? extends V> values) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean putAll(Multimap<? extends K, ? extends V> multimap) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   @CanIgnoreReturnValue
   @DoNotCall("Always throws UnsupportedOperationException")
   public final boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
      throw new UnsupportedOperationException();
   }

   boolean isPartialView() {
      return this.map.isPartialView();
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.map.containsKey(key);
   }

   public boolean containsValue(@CheckForNull Object value) {
      return value != null && super.containsValue(value);
   }

   public int size() {
      return this.size;
   }

   public ImmutableSet<K> keySet() {
      return this.map.keySet();
   }

   Set<K> createKeySet() {
      throw new AssertionError("unreachable");
   }

   public ImmutableMap<K, Collection<V>> asMap() {
      return this.map;
   }

   Map<K, Collection<V>> createAsMap() {
      throw new AssertionError("should never be called");
   }

   public ImmutableCollection<Entry<K, V>> entries() {
      return (ImmutableCollection)super.entries();
   }

   ImmutableCollection<Entry<K, V>> createEntries() {
      return new ImmutableMultimap.EntryCollection(this);
   }

   UnmodifiableIterator<Entry<K, V>> entryIterator() {
      return new UnmodifiableIterator<Entry<K, V>>() {
         final Iterator<? extends Entry<K, ? extends ImmutableCollection<V>>> asMapItr;
         @CheckForNull
         K currentKey;
         Iterator<V> valueItr;

         {
            this.asMapItr = ImmutableMultimap.this.map.entrySet().iterator();
            this.currentKey = null;
            this.valueItr = Iterators.emptyIterator();
         }

         public boolean hasNext() {
            return this.valueItr.hasNext() || this.asMapItr.hasNext();
         }

         public Entry<K, V> next() {
            if (!this.valueItr.hasNext()) {
               Entry<K, ? extends ImmutableCollection<V>> entry = (Entry)this.asMapItr.next();
               this.currentKey = entry.getKey();
               this.valueItr = ((ImmutableCollection)entry.getValue()).iterator();
            }

            return Maps.immutableEntry(Objects.requireNonNull(this.currentKey), this.valueItr.next());
         }
      };
   }

   Spliterator<Entry<K, V>> entrySpliterator() {
      return CollectSpliterators.flatMap(this.asMap().entrySet().spliterator(), (keyToValueCollectionEntry) -> {
         K key = keyToValueCollectionEntry.getKey();
         Collection<V> valueCollection = (Collection)keyToValueCollectionEntry.getValue();
         return CollectSpliterators.map(valueCollection.spliterator(), (value) -> {
            return Maps.immutableEntry(key, value);
         });
      }, 64 | (this instanceof SetMultimap ? 1 : 0), (long)this.size());
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      Preconditions.checkNotNull(action);
      this.asMap().forEach((key, valueCollection) -> {
         valueCollection.forEach((value) -> {
            action.accept(key, value);
         });
      });
   }

   public ImmutableMultiset<K> keys() {
      return (ImmutableMultiset)super.keys();
   }

   ImmutableMultiset<K> createKeys() {
      return new ImmutableMultimap.Keys();
   }

   public ImmutableCollection<V> values() {
      return (ImmutableCollection)super.values();
   }

   ImmutableCollection<V> createValues() {
      return new ImmutableMultimap.Values(this);
   }

   UnmodifiableIterator<V> valueIterator() {
      return new UnmodifiableIterator<V>() {
         Iterator<? extends ImmutableCollection<V>> valueCollectionItr;
         Iterator<V> valueItr;

         {
            this.valueCollectionItr = ImmutableMultimap.this.map.values().iterator();
            this.valueItr = Iterators.emptyIterator();
         }

         public boolean hasNext() {
            return this.valueItr.hasNext() || this.valueCollectionItr.hasNext();
         }

         public V next() {
            if (!this.valueItr.hasNext()) {
               this.valueItr = ((ImmutableCollection)this.valueCollectionItr.next()).iterator();
            }

            return this.valueItr.next();
         }
      };
   }

   private static final class Values<K, V> extends ImmutableCollection<V> {
      @Weak
      private final transient ImmutableMultimap<K, V> multimap;
      private static final long serialVersionUID = 0L;

      Values(ImmutableMultimap<K, V> multimap) {
         this.multimap = multimap;
      }

      public boolean contains(@CheckForNull Object object) {
         return this.multimap.containsValue(object);
      }

      public UnmodifiableIterator<V> iterator() {
         return this.multimap.valueIterator();
      }

      @GwtIncompatible
      int copyIntoArray(Object[] dst, int offset) {
         ImmutableCollection valueCollection;
         for(UnmodifiableIterator var3 = this.multimap.map.values().iterator(); var3.hasNext(); offset = valueCollection.copyIntoArray(dst, offset)) {
            valueCollection = (ImmutableCollection)var3.next();
         }

         return offset;
      }

      public int size() {
         return this.multimap.size();
      }

      boolean isPartialView() {
         return true;
      }
   }

   @GwtIncompatible
   private static final class KeysSerializedForm implements Serializable {
      final ImmutableMultimap<?, ?> multimap;

      KeysSerializedForm(ImmutableMultimap<?, ?> multimap) {
         this.multimap = multimap;
      }

      Object readResolve() {
         return this.multimap.keys();
      }
   }

   class Keys extends ImmutableMultiset<K> {
      public boolean contains(@CheckForNull Object object) {
         return ImmutableMultimap.this.containsKey(object);
      }

      public int count(@CheckForNull Object element) {
         Collection<V> values = (Collection)ImmutableMultimap.this.map.get(element);
         return values == null ? 0 : values.size();
      }

      public ImmutableSet<K> elementSet() {
         return ImmutableMultimap.this.keySet();
      }

      public int size() {
         return ImmutableMultimap.this.size();
      }

      Multiset.Entry<K> getEntry(int index) {
         Entry<K, ? extends Collection<V>> entry = (Entry)ImmutableMultimap.this.map.entrySet().asList().get(index);
         return Multisets.immutableEntry(entry.getKey(), ((Collection)entry.getValue()).size());
      }

      boolean isPartialView() {
         return true;
      }

      @GwtIncompatible
      Object writeReplace() {
         return new ImmutableMultimap.KeysSerializedForm(ImmutableMultimap.this);
      }
   }

   private static class EntryCollection<K, V> extends ImmutableCollection<Entry<K, V>> {
      @Weak
      final ImmutableMultimap<K, V> multimap;
      private static final long serialVersionUID = 0L;

      EntryCollection(ImmutableMultimap<K, V> multimap) {
         this.multimap = multimap;
      }

      public UnmodifiableIterator<Entry<K, V>> iterator() {
         return this.multimap.entryIterator();
      }

      boolean isPartialView() {
         return this.multimap.isPartialView();
      }

      public int size() {
         return this.multimap.size();
      }

      public boolean contains(@CheckForNull Object object) {
         if (object instanceof Entry) {
            Entry<?, ?> entry = (Entry)object;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
         } else {
            return false;
         }
      }
   }

   @GwtIncompatible
   static class FieldSettersHolder {
      static final Serialization.FieldSetter<ImmutableMultimap> MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
      static final Serialization.FieldSetter<ImmutableMultimap> SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
   }

   @DoNotMock
   public static class Builder<K, V> {
      final Map<K, Collection<V>> builderMap = Platform.preservesInsertionOrderOnPutsMap();
      @CheckForNull
      Comparator<? super K> keyComparator;
      @CheckForNull
      Comparator<? super V> valueComparator;

      Collection<V> newMutableValueCollection() {
         return new ArrayList();
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> put(K key, V value) {
         CollectPreconditions.checkEntryNotNull(key, value);
         Collection<V> valueCollection = (Collection)this.builderMap.get(key);
         if (valueCollection == null) {
            this.builderMap.put(key, valueCollection = this.newMutableValueCollection());
         }

         valueCollection.add(value);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
         return this.put(entry.getKey(), entry.getValue());
      }

      @CanIgnoreReturnValue
      @Beta
      public ImmutableMultimap.Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> entries) {
         Iterator var2 = entries.iterator();

         while(var2.hasNext()) {
            Entry<? extends K, ? extends V> entry = (Entry)var2.next();
            this.put(entry);
         }

         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> putAll(K key, Iterable<? extends V> values) {
         if (key == null) {
            NullPointerException var10000 = new NullPointerException;
            String var10003 = String.valueOf(Iterables.toString(values));
            String var10002;
            if (var10003.length() != 0) {
               var10002 = "null key in entry: null=".concat(var10003);
            } else {
               String var10004 = new String;
               var10002 = var10004;
               var10004.<init>("null key in entry: null=");
            }

            var10000.<init>(var10002);
            throw var10000;
         } else {
            Collection<V> valueCollection = (Collection)this.builderMap.get(key);
            Iterator valuesItr;
            Object value;
            if (valueCollection != null) {
               valuesItr = values.iterator();

               while(valuesItr.hasNext()) {
                  value = valuesItr.next();
                  CollectPreconditions.checkEntryNotNull(key, value);
                  valueCollection.add(value);
               }

               return this;
            } else {
               valuesItr = values.iterator();
               if (!valuesItr.hasNext()) {
                  return this;
               } else {
                  valueCollection = this.newMutableValueCollection();

                  while(valuesItr.hasNext()) {
                     value = valuesItr.next();
                     CollectPreconditions.checkEntryNotNull(key, value);
                     valueCollection.add(value);
                  }

                  this.builderMap.put(key, valueCollection);
                  return this;
               }
            }
         }
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> putAll(K key, V... values) {
         return this.putAll(key, (Iterable)Arrays.asList(values));
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> putAll(Multimap<? extends K, ? extends V> multimap) {
         Iterator var2 = multimap.asMap().entrySet().iterator();

         while(var2.hasNext()) {
            Entry<? extends K, ? extends Collection<? extends V>> entry = (Entry)var2.next();
            this.putAll(entry.getKey(), (Iterable)entry.getValue());
         }

         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> orderKeysBy(Comparator<? super K> keyComparator) {
         this.keyComparator = (Comparator)Preconditions.checkNotNull(keyComparator);
         return this;
      }

      @CanIgnoreReturnValue
      public ImmutableMultimap.Builder<K, V> orderValuesBy(Comparator<? super V> valueComparator) {
         this.valueComparator = (Comparator)Preconditions.checkNotNull(valueComparator);
         return this;
      }

      @CanIgnoreReturnValue
      ImmutableMultimap.Builder<K, V> combine(ImmutableMultimap.Builder<K, V> other) {
         Iterator var2 = other.builderMap.entrySet().iterator();

         while(var2.hasNext()) {
            Entry<K, Collection<V>> entry = (Entry)var2.next();
            this.putAll(entry.getKey(), (Iterable)entry.getValue());
         }

         return this;
      }

      public ImmutableMultimap<K, V> build() {
         Collection<Entry<K, Collection<V>>> mapEntries = this.builderMap.entrySet();
         if (this.keyComparator != null) {
            mapEntries = Ordering.from(this.keyComparator).onKeys().immutableSortedCopy((Iterable)mapEntries);
         }

         return ImmutableListMultimap.fromMapEntries((Collection)mapEntries, this.valueComparator);
      }
   }
}
