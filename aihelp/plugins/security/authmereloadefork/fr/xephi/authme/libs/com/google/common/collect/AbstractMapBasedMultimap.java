package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class AbstractMapBasedMultimap<K, V> extends AbstractMultimap<K, V> implements Serializable {
   private transient Map<K, Collection<V>> map;
   private transient int totalSize;
   private static final long serialVersionUID = 2447537837011683357L;

   protected AbstractMapBasedMultimap(Map<K, Collection<V>> map) {
      Preconditions.checkArgument(map.isEmpty());
      this.map = map;
   }

   final void setMap(Map<K, Collection<V>> map) {
      this.map = map;
      this.totalSize = 0;

      Collection values;
      for(Iterator var2 = map.values().iterator(); var2.hasNext(); this.totalSize += values.size()) {
         values = (Collection)var2.next();
         Preconditions.checkArgument(!values.isEmpty());
      }

   }

   Collection<V> createUnmodifiableEmptyCollection() {
      return this.unmodifiableCollectionSubclass(this.createCollection());
   }

   abstract Collection<V> createCollection();

   Collection<V> createCollection(@ParametricNullness K key) {
      return this.createCollection();
   }

   Map<K, Collection<V>> backingMap() {
      return this.map;
   }

   public int size() {
      return this.totalSize;
   }

   public boolean containsKey(@CheckForNull Object key) {
      return this.map.containsKey(key);
   }

   public boolean put(@ParametricNullness K key, @ParametricNullness V value) {
      Collection<V> collection = (Collection)this.map.get(key);
      if (collection == null) {
         collection = this.createCollection(key);
         if (collection.add(value)) {
            ++this.totalSize;
            this.map.put(key, collection);
            return true;
         } else {
            throw new AssertionError("New Collection violated the Collection spec");
         }
      } else if (collection.add(value)) {
         ++this.totalSize;
         return true;
      } else {
         return false;
      }
   }

   private Collection<V> getOrCreateCollection(@ParametricNullness K key) {
      Collection<V> collection = (Collection)this.map.get(key);
      if (collection == null) {
         collection = this.createCollection(key);
         this.map.put(key, collection);
      }

      return collection;
   }

   public Collection<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
      Iterator<? extends V> iterator = values.iterator();
      if (!iterator.hasNext()) {
         return this.removeAll(key);
      } else {
         Collection<V> collection = this.getOrCreateCollection(key);
         Collection<V> oldValues = this.createCollection();
         oldValues.addAll(collection);
         this.totalSize -= collection.size();
         collection.clear();

         while(iterator.hasNext()) {
            if (collection.add(iterator.next())) {
               ++this.totalSize;
            }
         }

         return this.unmodifiableCollectionSubclass(oldValues);
      }
   }

   public Collection<V> removeAll(@CheckForNull Object key) {
      Collection<V> collection = (Collection)this.map.remove(key);
      if (collection == null) {
         return this.createUnmodifiableEmptyCollection();
      } else {
         Collection<V> output = this.createCollection();
         output.addAll(collection);
         this.totalSize -= collection.size();
         collection.clear();
         return this.unmodifiableCollectionSubclass(output);
      }
   }

   <E> Collection<E> unmodifiableCollectionSubclass(Collection<E> collection) {
      return Collections.unmodifiableCollection(collection);
   }

   public void clear() {
      Iterator var1 = this.map.values().iterator();

      while(var1.hasNext()) {
         Collection<V> collection = (Collection)var1.next();
         collection.clear();
      }

      this.map.clear();
      this.totalSize = 0;
   }

   public Collection<V> get(@ParametricNullness K key) {
      Collection<V> collection = (Collection)this.map.get(key);
      if (collection == null) {
         collection = this.createCollection(key);
      }

      return this.wrapCollection(key, collection);
   }

   Collection<V> wrapCollection(@ParametricNullness K key, Collection<V> collection) {
      return new AbstractMapBasedMultimap.WrappedCollection(key, collection, (AbstractMapBasedMultimap.WrappedCollection)null);
   }

   final List<V> wrapList(@ParametricNullness K key, List<V> list, @CheckForNull AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
      return (List)(list instanceof RandomAccess ? new AbstractMapBasedMultimap.RandomAccessWrappedList(this, key, list, ancestor) : new AbstractMapBasedMultimap.WrappedList(key, list, ancestor));
   }

   private static <E> Iterator<E> iteratorOrListIterator(Collection<E> collection) {
      return (Iterator)(collection instanceof List ? ((List)collection).listIterator() : collection.iterator());
   }

   Set<K> createKeySet() {
      return new AbstractMapBasedMultimap.KeySet(this.map);
   }

   final Set<K> createMaybeNavigableKeySet() {
      if (this.map instanceof NavigableMap) {
         return new AbstractMapBasedMultimap.NavigableKeySet((NavigableMap)this.map);
      } else {
         return (Set)(this.map instanceof SortedMap ? new AbstractMapBasedMultimap.SortedKeySet((SortedMap)this.map) : new AbstractMapBasedMultimap.KeySet(this.map));
      }
   }

   private void removeValuesForKey(@CheckForNull Object key) {
      Collection<V> collection = (Collection)Maps.safeRemove(this.map, key);
      if (collection != null) {
         int count = collection.size();
         collection.clear();
         this.totalSize -= count;
      }

   }

   public Collection<V> values() {
      return super.values();
   }

   Collection<V> createValues() {
      return new AbstractMultimap.Values();
   }

   Iterator<V> valueIterator() {
      return new AbstractMapBasedMultimap<K, V>.Itr<V>(this) {
         @ParametricNullness
         V output(@ParametricNullness K key, @ParametricNullness V value) {
            return value;
         }
      };
   }

   Spliterator<V> valueSpliterator() {
      return CollectSpliterators.flatMap(this.map.values().spliterator(), Collection::spliterator, 64, (long)this.size());
   }

   Multiset<K> createKeys() {
      return new Multimaps.Keys(this);
   }

   public Collection<Entry<K, V>> entries() {
      return super.entries();
   }

   Collection<Entry<K, V>> createEntries() {
      return (Collection)(this instanceof SetMultimap ? new AbstractMultimap.EntrySet() : new AbstractMultimap.Entries());
   }

   Iterator<Entry<K, V>> entryIterator() {
      return new AbstractMapBasedMultimap<K, V>.Itr<Entry<K, V>>(this) {
         Entry<K, V> output(@ParametricNullness K key, @ParametricNullness V value) {
            return Maps.immutableEntry(key, value);
         }
      };
   }

   Spliterator<Entry<K, V>> entrySpliterator() {
      return CollectSpliterators.flatMap(this.map.entrySet().spliterator(), (keyToValueCollectionEntry) -> {
         K key = keyToValueCollectionEntry.getKey();
         Collection<V> valueCollection = (Collection)keyToValueCollectionEntry.getValue();
         return CollectSpliterators.map(valueCollection.spliterator(), (value) -> {
            return Maps.immutableEntry(key, value);
         });
      }, 64, (long)this.size());
   }

   public void forEach(BiConsumer<? super K, ? super V> action) {
      Preconditions.checkNotNull(action);
      this.map.forEach((key, valueCollection) -> {
         valueCollection.forEach((value) -> {
            action.accept(key, value);
         });
      });
   }

   Map<K, Collection<V>> createAsMap() {
      return new AbstractMapBasedMultimap.AsMap(this.map);
   }

   final Map<K, Collection<V>> createMaybeNavigableAsMap() {
      if (this.map instanceof NavigableMap) {
         return new AbstractMapBasedMultimap.NavigableAsMap((NavigableMap)this.map);
      } else {
         return (Map)(this.map instanceof SortedMap ? new AbstractMapBasedMultimap.SortedAsMap((SortedMap)this.map) : new AbstractMapBasedMultimap.AsMap(this.map));
      }
   }

   class NavigableAsMap extends AbstractMapBasedMultimap<K, V>.SortedAsMap implements NavigableMap<K, Collection<V>> {
      NavigableAsMap(NavigableMap<K, Collection<V>> submap) {
         super(submap);
      }

      NavigableMap<K, Collection<V>> sortedMap() {
         return (NavigableMap)super.sortedMap();
      }

      @CheckForNull
      public Entry<K, Collection<V>> lowerEntry(@ParametricNullness K key) {
         Entry<K, Collection<V>> entry = this.sortedMap().lowerEntry(key);
         return entry == null ? null : this.wrapEntry(entry);
      }

      @CheckForNull
      public K lowerKey(@ParametricNullness K key) {
         return this.sortedMap().lowerKey(key);
      }

      @CheckForNull
      public Entry<K, Collection<V>> floorEntry(@ParametricNullness K key) {
         Entry<K, Collection<V>> entry = this.sortedMap().floorEntry(key);
         return entry == null ? null : this.wrapEntry(entry);
      }

      @CheckForNull
      public K floorKey(@ParametricNullness K key) {
         return this.sortedMap().floorKey(key);
      }

      @CheckForNull
      public Entry<K, Collection<V>> ceilingEntry(@ParametricNullness K key) {
         Entry<K, Collection<V>> entry = this.sortedMap().ceilingEntry(key);
         return entry == null ? null : this.wrapEntry(entry);
      }

      @CheckForNull
      public K ceilingKey(@ParametricNullness K key) {
         return this.sortedMap().ceilingKey(key);
      }

      @CheckForNull
      public Entry<K, Collection<V>> higherEntry(@ParametricNullness K key) {
         Entry<K, Collection<V>> entry = this.sortedMap().higherEntry(key);
         return entry == null ? null : this.wrapEntry(entry);
      }

      @CheckForNull
      public K higherKey(@ParametricNullness K key) {
         return this.sortedMap().higherKey(key);
      }

      @CheckForNull
      public Entry<K, Collection<V>> firstEntry() {
         Entry<K, Collection<V>> entry = this.sortedMap().firstEntry();
         return entry == null ? null : this.wrapEntry(entry);
      }

      @CheckForNull
      public Entry<K, Collection<V>> lastEntry() {
         Entry<K, Collection<V>> entry = this.sortedMap().lastEntry();
         return entry == null ? null : this.wrapEntry(entry);
      }

      @CheckForNull
      public Entry<K, Collection<V>> pollFirstEntry() {
         return this.pollAsMapEntry(this.entrySet().iterator());
      }

      @CheckForNull
      public Entry<K, Collection<V>> pollLastEntry() {
         return this.pollAsMapEntry(this.descendingMap().entrySet().iterator());
      }

      @CheckForNull
      Entry<K, Collection<V>> pollAsMapEntry(Iterator<Entry<K, Collection<V>>> entryIterator) {
         if (!entryIterator.hasNext()) {
            return null;
         } else {
            Entry<K, Collection<V>> entry = (Entry)entryIterator.next();
            Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll((Collection)entry.getValue());
            entryIterator.remove();
            return Maps.immutableEntry(entry.getKey(), AbstractMapBasedMultimap.this.unmodifiableCollectionSubclass(output));
         }
      }

      public NavigableMap<K, Collection<V>> descendingMap() {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().descendingMap());
      }

      public NavigableSet<K> keySet() {
         return (NavigableSet)super.keySet();
      }

      NavigableSet<K> createKeySet() {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap());
      }

      public NavigableSet<K> navigableKeySet() {
         return this.keySet();
      }

      public NavigableSet<K> descendingKeySet() {
         return this.descendingMap().navigableKeySet();
      }

      public NavigableMap<K, Collection<V>> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
         return this.subMap(fromKey, true, toKey, false);
      }

      public NavigableMap<K, Collection<V>> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().subMap(fromKey, fromInclusive, toKey, toInclusive));
      }

      public NavigableMap<K, Collection<V>> headMap(@ParametricNullness K toKey) {
         return this.headMap(toKey, false);
      }

      public NavigableMap<K, Collection<V>> headMap(@ParametricNullness K toKey, boolean inclusive) {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().headMap(toKey, inclusive));
      }

      public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness K fromKey) {
         return this.tailMap(fromKey, true);
      }

      public NavigableMap<K, Collection<V>> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
         return AbstractMapBasedMultimap.this.new NavigableAsMap(this.sortedMap().tailMap(fromKey, inclusive));
      }
   }

   private class SortedAsMap extends AbstractMapBasedMultimap<K, V>.AsMap implements SortedMap<K, Collection<V>> {
      @CheckForNull
      SortedSet<K> sortedKeySet;

      SortedAsMap(SortedMap<K, Collection<V>> submap) {
         super(submap);
      }

      SortedMap<K, Collection<V>> sortedMap() {
         return (SortedMap)this.submap;
      }

      @CheckForNull
      public Comparator<? super K> comparator() {
         return this.sortedMap().comparator();
      }

      @ParametricNullness
      public K firstKey() {
         return this.sortedMap().firstKey();
      }

      @ParametricNullness
      public K lastKey() {
         return this.sortedMap().lastKey();
      }

      public SortedMap<K, Collection<V>> headMap(@ParametricNullness K toKey) {
         return AbstractMapBasedMultimap.this.new SortedAsMap(this.sortedMap().headMap(toKey));
      }

      public SortedMap<K, Collection<V>> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
         return AbstractMapBasedMultimap.this.new SortedAsMap(this.sortedMap().subMap(fromKey, toKey));
      }

      public SortedMap<K, Collection<V>> tailMap(@ParametricNullness K fromKey) {
         return AbstractMapBasedMultimap.this.new SortedAsMap(this.sortedMap().tailMap(fromKey));
      }

      public SortedSet<K> keySet() {
         SortedSet<K> result = this.sortedKeySet;
         return result == null ? (this.sortedKeySet = this.createKeySet()) : result;
      }

      SortedSet<K> createKeySet() {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap());
      }
   }

   private class AsMap extends Maps.ViewCachingAbstractMap<K, Collection<V>> {
      final transient Map<K, Collection<V>> submap;

      AsMap(Map<K, Collection<V>> submap) {
         this.submap = submap;
      }

      protected Set<Entry<K, Collection<V>>> createEntrySet() {
         return new AbstractMapBasedMultimap.AsMap.AsMapEntries();
      }

      public boolean containsKey(@CheckForNull Object key) {
         return Maps.safeContainsKey(this.submap, key);
      }

      @CheckForNull
      public Collection<V> get(@CheckForNull Object key) {
         Collection<V> collection = (Collection)Maps.safeGet(this.submap, key);
         return collection == null ? null : AbstractMapBasedMultimap.this.wrapCollection(key, collection);
      }

      public Set<K> keySet() {
         return AbstractMapBasedMultimap.this.keySet();
      }

      public int size() {
         return this.submap.size();
      }

      @CheckForNull
      public Collection<V> remove(@CheckForNull Object key) {
         Collection<V> collection = (Collection)this.submap.remove(key);
         if (collection == null) {
            return null;
         } else {
            Collection<V> output = AbstractMapBasedMultimap.this.createCollection();
            output.addAll(collection);
            AbstractMapBasedMultimap.this.totalSize = collection.size();
            collection.clear();
            return output;
         }
      }

      public boolean equals(@CheckForNull Object object) {
         return this == object || this.submap.equals(object);
      }

      public int hashCode() {
         return this.submap.hashCode();
      }

      public String toString() {
         return this.submap.toString();
      }

      public void clear() {
         if (this.submap == AbstractMapBasedMultimap.this.map) {
            AbstractMapBasedMultimap.this.clear();
         } else {
            Iterators.clear(new AbstractMapBasedMultimap.AsMap.AsMapIterator());
         }

      }

      Entry<K, Collection<V>> wrapEntry(Entry<K, Collection<V>> entry) {
         K key = entry.getKey();
         return Maps.immutableEntry(key, AbstractMapBasedMultimap.this.wrapCollection(key, (Collection)entry.getValue()));
      }

      class AsMapIterator implements Iterator<Entry<K, Collection<V>>> {
         final Iterator<Entry<K, Collection<V>>> delegateIterator;
         @CheckForNull
         Collection<V> collection;

         AsMapIterator() {
            this.delegateIterator = AsMap.this.submap.entrySet().iterator();
         }

         public boolean hasNext() {
            return this.delegateIterator.hasNext();
         }

         public Entry<K, Collection<V>> next() {
            Entry<K, Collection<V>> entry = (Entry)this.delegateIterator.next();
            this.collection = (Collection)entry.getValue();
            return AsMap.this.wrapEntry(entry);
         }

         public void remove() {
            Preconditions.checkState(this.collection != null, "no calls to next() since the last call to remove()");
            this.delegateIterator.remove();
            AbstractMapBasedMultimap.this.totalSize = this.collection.size();
            this.collection.clear();
            this.collection = null;
         }
      }

      class AsMapEntries extends Maps.EntrySet<K, Collection<V>> {
         Map<K, Collection<V>> map() {
            return AsMap.this;
         }

         public Iterator<Entry<K, Collection<V>>> iterator() {
            return AsMap.this.new AsMapIterator();
         }

         public Spliterator<Entry<K, Collection<V>>> spliterator() {
            return CollectSpliterators.map(AsMap.this.submap.entrySet().spliterator(), AsMap.this::wrapEntry);
         }

         public boolean contains(@CheckForNull Object o) {
            return Collections2.safeContains(AsMap.this.submap.entrySet(), o);
         }

         public boolean remove(@CheckForNull Object o) {
            if (!this.contains(o)) {
               return false;
            } else {
               Entry<?, ?> entry = (Entry)Objects.requireNonNull((Entry)o);
               AbstractMapBasedMultimap.this.removeValuesForKey(entry.getKey());
               return true;
            }
         }
      }
   }

   private abstract class Itr<T> implements Iterator<T> {
      final Iterator<Entry<K, Collection<V>>> keyIterator;
      @CheckForNull
      K key;
      @CheckForNull
      Collection<V> collection;
      Iterator<V> valueIterator;

      Itr() {
         this.keyIterator = AbstractMapBasedMultimap.this.map.entrySet().iterator();
         this.key = null;
         this.collection = null;
         this.valueIterator = Iterators.emptyModifiableIterator();
      }

      abstract T output(@ParametricNullness K var1, @ParametricNullness V var2);

      public boolean hasNext() {
         return this.keyIterator.hasNext() || this.valueIterator.hasNext();
      }

      public T next() {
         if (!this.valueIterator.hasNext()) {
            Entry<K, Collection<V>> mapEntry = (Entry)this.keyIterator.next();
            this.key = mapEntry.getKey();
            this.collection = (Collection)mapEntry.getValue();
            this.valueIterator = this.collection.iterator();
         }

         return this.output(NullnessCasts.uncheckedCastNullableTToT(this.key), this.valueIterator.next());
      }

      public void remove() {
         this.valueIterator.remove();
         if (((Collection)Objects.requireNonNull(this.collection)).isEmpty()) {
            this.keyIterator.remove();
         }

         AbstractMapBasedMultimap.this.totalSize--;
      }
   }

   class NavigableKeySet extends AbstractMapBasedMultimap<K, V>.SortedKeySet implements NavigableSet<K> {
      NavigableKeySet(NavigableMap<K, Collection<V>> subMap) {
         super(subMap);
      }

      NavigableMap<K, Collection<V>> sortedMap() {
         return (NavigableMap)super.sortedMap();
      }

      @CheckForNull
      public K lower(@ParametricNullness K k) {
         return this.sortedMap().lowerKey(k);
      }

      @CheckForNull
      public K floor(@ParametricNullness K k) {
         return this.sortedMap().floorKey(k);
      }

      @CheckForNull
      public K ceiling(@ParametricNullness K k) {
         return this.sortedMap().ceilingKey(k);
      }

      @CheckForNull
      public K higher(@ParametricNullness K k) {
         return this.sortedMap().higherKey(k);
      }

      @CheckForNull
      public K pollFirst() {
         return Iterators.pollNext(this.iterator());
      }

      @CheckForNull
      public K pollLast() {
         return Iterators.pollNext(this.descendingIterator());
      }

      public NavigableSet<K> descendingSet() {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().descendingMap());
      }

      public Iterator<K> descendingIterator() {
         return this.descendingSet().iterator();
      }

      public NavigableSet<K> headSet(@ParametricNullness K toElement) {
         return this.headSet(toElement, false);
      }

      public NavigableSet<K> headSet(@ParametricNullness K toElement, boolean inclusive) {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().headMap(toElement, inclusive));
      }

      public NavigableSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
         return this.subSet(fromElement, true, toElement, false);
      }

      public NavigableSet<K> subSet(@ParametricNullness K fromElement, boolean fromInclusive, @ParametricNullness K toElement, boolean toInclusive) {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().subMap(fromElement, fromInclusive, toElement, toInclusive));
      }

      public NavigableSet<K> tailSet(@ParametricNullness K fromElement) {
         return this.tailSet(fromElement, true);
      }

      public NavigableSet<K> tailSet(@ParametricNullness K fromElement, boolean inclusive) {
         return AbstractMapBasedMultimap.this.new NavigableKeySet(this.sortedMap().tailMap(fromElement, inclusive));
      }
   }

   private class SortedKeySet extends AbstractMapBasedMultimap<K, V>.KeySet implements SortedSet<K> {
      SortedKeySet(SortedMap<K, Collection<V>> subMap) {
         super(subMap);
      }

      SortedMap<K, Collection<V>> sortedMap() {
         return (SortedMap)super.map();
      }

      @CheckForNull
      public Comparator<? super K> comparator() {
         return this.sortedMap().comparator();
      }

      @ParametricNullness
      public K first() {
         return this.sortedMap().firstKey();
      }

      public SortedSet<K> headSet(@ParametricNullness K toElement) {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap().headMap(toElement));
      }

      @ParametricNullness
      public K last() {
         return this.sortedMap().lastKey();
      }

      public SortedSet<K> subSet(@ParametricNullness K fromElement, @ParametricNullness K toElement) {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap().subMap(fromElement, toElement));
      }

      public SortedSet<K> tailSet(@ParametricNullness K fromElement) {
         return AbstractMapBasedMultimap.this.new SortedKeySet(this.sortedMap().tailMap(fromElement));
      }
   }

   private class KeySet extends Maps.KeySet<K, Collection<V>> {
      KeySet(Map<K, Collection<V>> subMap) {
         super(subMap);
      }

      public Iterator<K> iterator() {
         final Iterator<Entry<K, Collection<V>>> entryIterator = this.map().entrySet().iterator();
         return new Iterator<K>() {
            @CheckForNull
            Entry<K, Collection<V>> entry;

            public boolean hasNext() {
               return entryIterator.hasNext();
            }

            @ParametricNullness
            public K next() {
               this.entry = (Entry)entryIterator.next();
               return this.entry.getKey();
            }

            public void remove() {
               Preconditions.checkState(this.entry != null, "no calls to next() since the last call to remove()");
               Collection<V> collection = (Collection)this.entry.getValue();
               entryIterator.remove();
               AbstractMapBasedMultimap.this.totalSize = collection.size();
               collection.clear();
               this.entry = null;
            }
         };
      }

      public Spliterator<K> spliterator() {
         return this.map().keySet().spliterator();
      }

      public boolean remove(@CheckForNull Object key) {
         int count = 0;
         Collection<V> collection = (Collection)this.map().remove(key);
         if (collection != null) {
            count = collection.size();
            collection.clear();
            AbstractMapBasedMultimap.this.totalSize = count;
         }

         return count > 0;
      }

      public void clear() {
         Iterators.clear(this.iterator());
      }

      public boolean containsAll(Collection<?> c) {
         return this.map().keySet().containsAll(c);
      }

      public boolean equals(@CheckForNull Object object) {
         return this == object || this.map().keySet().equals(object);
      }

      public int hashCode() {
         return this.map().keySet().hashCode();
      }
   }

   private class RandomAccessWrappedList extends AbstractMapBasedMultimap<K, V>.WrappedList implements RandomAccess {
      RandomAccessWrappedList(@ParametricNullness AbstractMapBasedMultimap var1, Object key, @CheckForNull List delegate, AbstractMapBasedMultimap.WrappedCollection ancestor) {
         super(key, delegate, ancestor);
      }
   }

   class WrappedList extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements List<V> {
      WrappedList(@ParametricNullness K key, List<V> delegate, @CheckForNull AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
         super(key, delegate, ancestor);
      }

      List<V> getListDelegate() {
         return (List)this.getDelegate();
      }

      public boolean addAll(int index, Collection<? extends V> c) {
         if (c.isEmpty()) {
            return false;
         } else {
            int oldSize = this.size();
            boolean changed = this.getListDelegate().addAll(index, c);
            if (changed) {
               int newSize = this.getDelegate().size();
               AbstractMapBasedMultimap.this.totalSize = newSize - oldSize;
               if (oldSize == 0) {
                  this.addToMap();
               }
            }

            return changed;
         }
      }

      @ParametricNullness
      public V get(int index) {
         this.refreshIfEmpty();
         return this.getListDelegate().get(index);
      }

      @ParametricNullness
      public V set(int index, @ParametricNullness V element) {
         this.refreshIfEmpty();
         return this.getListDelegate().set(index, element);
      }

      public void add(int index, @ParametricNullness V element) {
         this.refreshIfEmpty();
         boolean wasEmpty = this.getDelegate().isEmpty();
         this.getListDelegate().add(index, element);
         AbstractMapBasedMultimap.this.totalSize++;
         if (wasEmpty) {
            this.addToMap();
         }

      }

      @ParametricNullness
      public V remove(int index) {
         this.refreshIfEmpty();
         V value = this.getListDelegate().remove(index);
         AbstractMapBasedMultimap.this.totalSize--;
         this.removeIfEmpty();
         return value;
      }

      public int indexOf(@CheckForNull Object o) {
         this.refreshIfEmpty();
         return this.getListDelegate().indexOf(o);
      }

      public int lastIndexOf(@CheckForNull Object o) {
         this.refreshIfEmpty();
         return this.getListDelegate().lastIndexOf(o);
      }

      public ListIterator<V> listIterator() {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedList.WrappedListIterator();
      }

      public ListIterator<V> listIterator(int index) {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedList.WrappedListIterator(index);
      }

      public List<V> subList(int fromIndex, int toIndex) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.wrapList(this.getKey(), this.getListDelegate().subList(fromIndex, toIndex), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      private class WrappedListIterator extends AbstractMapBasedMultimap<K, V>.WrappedCollection.WrappedIterator implements ListIterator<V> {
         WrappedListIterator() {
            super();
         }

         public WrappedListIterator(int index) {
            super(WrappedList.this.getListDelegate().listIterator(index));
         }

         private ListIterator<V> getDelegateListIterator() {
            return (ListIterator)this.getDelegateIterator();
         }

         public boolean hasPrevious() {
            return this.getDelegateListIterator().hasPrevious();
         }

         @ParametricNullness
         public V previous() {
            return this.getDelegateListIterator().previous();
         }

         public int nextIndex() {
            return this.getDelegateListIterator().nextIndex();
         }

         public int previousIndex() {
            return this.getDelegateListIterator().previousIndex();
         }

         public void set(@ParametricNullness V value) {
            this.getDelegateListIterator().set(value);
         }

         public void add(@ParametricNullness V value) {
            boolean wasEmpty = WrappedList.this.isEmpty();
            this.getDelegateListIterator().add(value);
            AbstractMapBasedMultimap.this.totalSize++;
            if (wasEmpty) {
               WrappedList.this.addToMap();
            }

         }
      }
   }

   class WrappedNavigableSet extends AbstractMapBasedMultimap<K, V>.WrappedSortedSet implements NavigableSet<V> {
      WrappedNavigableSet(@ParametricNullness K key, NavigableSet<V> delegate, @CheckForNull AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
         super(key, delegate, ancestor);
      }

      NavigableSet<V> getSortedSetDelegate() {
         return (NavigableSet)super.getSortedSetDelegate();
      }

      @CheckForNull
      public V lower(@ParametricNullness V v) {
         return this.getSortedSetDelegate().lower(v);
      }

      @CheckForNull
      public V floor(@ParametricNullness V v) {
         return this.getSortedSetDelegate().floor(v);
      }

      @CheckForNull
      public V ceiling(@ParametricNullness V v) {
         return this.getSortedSetDelegate().ceiling(v);
      }

      @CheckForNull
      public V higher(@ParametricNullness V v) {
         return this.getSortedSetDelegate().higher(v);
      }

      @CheckForNull
      public V pollFirst() {
         return Iterators.pollNext(this.iterator());
      }

      @CheckForNull
      public V pollLast() {
         return Iterators.pollNext(this.descendingIterator());
      }

      private NavigableSet<V> wrap(NavigableSet<V> wrapped) {
         return AbstractMapBasedMultimap.this.new WrappedNavigableSet(this.key, wrapped, (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      public NavigableSet<V> descendingSet() {
         return this.wrap(this.getSortedSetDelegate().descendingSet());
      }

      public Iterator<V> descendingIterator() {
         return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator(this.getSortedSetDelegate().descendingIterator());
      }

      public NavigableSet<V> subSet(@ParametricNullness V fromElement, boolean fromInclusive, @ParametricNullness V toElement, boolean toInclusive) {
         return this.wrap(this.getSortedSetDelegate().subSet(fromElement, fromInclusive, toElement, toInclusive));
      }

      public NavigableSet<V> headSet(@ParametricNullness V toElement, boolean inclusive) {
         return this.wrap(this.getSortedSetDelegate().headSet(toElement, inclusive));
      }

      public NavigableSet<V> tailSet(@ParametricNullness V fromElement, boolean inclusive) {
         return this.wrap(this.getSortedSetDelegate().tailSet(fromElement, inclusive));
      }
   }

   class WrappedSortedSet extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements SortedSet<V> {
      WrappedSortedSet(@ParametricNullness K key, SortedSet<V> delegate, @CheckForNull AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
         super(key, delegate, ancestor);
      }

      SortedSet<V> getSortedSetDelegate() {
         return (SortedSet)this.getDelegate();
      }

      @CheckForNull
      public Comparator<? super V> comparator() {
         return this.getSortedSetDelegate().comparator();
      }

      @ParametricNullness
      public V first() {
         this.refreshIfEmpty();
         return this.getSortedSetDelegate().first();
      }

      @ParametricNullness
      public V last() {
         this.refreshIfEmpty();
         return this.getSortedSetDelegate().last();
      }

      public SortedSet<V> headSet(@ParametricNullness V toElement) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().headSet(toElement), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      public SortedSet<V> subSet(@ParametricNullness V fromElement, @ParametricNullness V toElement) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().subSet(fromElement, toElement), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }

      public SortedSet<V> tailSet(@ParametricNullness V fromElement) {
         this.refreshIfEmpty();
         return AbstractMapBasedMultimap.this.new WrappedSortedSet(this.getKey(), this.getSortedSetDelegate().tailSet(fromElement), (AbstractMapBasedMultimap.WrappedCollection)(this.getAncestor() == null ? this : this.getAncestor()));
      }
   }

   class WrappedSet extends AbstractMapBasedMultimap<K, V>.WrappedCollection implements Set<V> {
      WrappedSet(@ParametricNullness K key, Set<V> delegate) {
         super(key, delegate, (AbstractMapBasedMultimap.WrappedCollection)null);
      }

      public boolean removeAll(Collection<?> c) {
         if (c.isEmpty()) {
            return false;
         } else {
            int oldSize = this.size();
            boolean changed = Sets.removeAllImpl((Set)this.delegate, c);
            if (changed) {
               int newSize = this.delegate.size();
               AbstractMapBasedMultimap.this.totalSize = newSize - oldSize;
               this.removeIfEmpty();
            }

            return changed;
         }
      }
   }

   class WrappedCollection extends AbstractCollection<V> {
      @ParametricNullness
      final K key;
      Collection<V> delegate;
      @CheckForNull
      final AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor;
      @CheckForNull
      final Collection<V> ancestorDelegate;

      WrappedCollection(@ParametricNullness K key, Collection<V> delegate, @CheckForNull AbstractMapBasedMultimap<K, V>.WrappedCollection ancestor) {
         this.key = key;
         this.delegate = delegate;
         this.ancestor = ancestor;
         this.ancestorDelegate = ancestor == null ? null : ancestor.getDelegate();
      }

      void refreshIfEmpty() {
         if (this.ancestor != null) {
            this.ancestor.refreshIfEmpty();
            if (this.ancestor.getDelegate() != this.ancestorDelegate) {
               throw new ConcurrentModificationException();
            }
         } else if (this.delegate.isEmpty()) {
            Collection<V> newDelegate = (Collection)AbstractMapBasedMultimap.this.map.get(this.key);
            if (newDelegate != null) {
               this.delegate = newDelegate;
            }
         }

      }

      void removeIfEmpty() {
         if (this.ancestor != null) {
            this.ancestor.removeIfEmpty();
         } else if (this.delegate.isEmpty()) {
            AbstractMapBasedMultimap.this.map.remove(this.key);
         }

      }

      @ParametricNullness
      K getKey() {
         return this.key;
      }

      void addToMap() {
         if (this.ancestor != null) {
            this.ancestor.addToMap();
         } else {
            AbstractMapBasedMultimap.this.map.put(this.key, this.delegate);
         }

      }

      public int size() {
         this.refreshIfEmpty();
         return this.delegate.size();
      }

      public boolean equals(@CheckForNull Object object) {
         if (object == this) {
            return true;
         } else {
            this.refreshIfEmpty();
            return this.delegate.equals(object);
         }
      }

      public int hashCode() {
         this.refreshIfEmpty();
         return this.delegate.hashCode();
      }

      public String toString() {
         this.refreshIfEmpty();
         return this.delegate.toString();
      }

      Collection<V> getDelegate() {
         return this.delegate;
      }

      public Iterator<V> iterator() {
         this.refreshIfEmpty();
         return new AbstractMapBasedMultimap.WrappedCollection.WrappedIterator();
      }

      public Spliterator<V> spliterator() {
         this.refreshIfEmpty();
         return this.delegate.spliterator();
      }

      public boolean add(@ParametricNullness V value) {
         this.refreshIfEmpty();
         boolean wasEmpty = this.delegate.isEmpty();
         boolean changed = this.delegate.add(value);
         if (changed) {
            AbstractMapBasedMultimap.this.totalSize++;
            if (wasEmpty) {
               this.addToMap();
            }
         }

         return changed;
      }

      @CheckForNull
      AbstractMapBasedMultimap<K, V>.WrappedCollection getAncestor() {
         return this.ancestor;
      }

      public boolean addAll(Collection<? extends V> collection) {
         if (collection.isEmpty()) {
            return false;
         } else {
            int oldSize = this.size();
            boolean changed = this.delegate.addAll(collection);
            if (changed) {
               int newSize = this.delegate.size();
               AbstractMapBasedMultimap.this.totalSize = newSize - oldSize;
               if (oldSize == 0) {
                  this.addToMap();
               }
            }

            return changed;
         }
      }

      public boolean contains(@CheckForNull Object o) {
         this.refreshIfEmpty();
         return this.delegate.contains(o);
      }

      public boolean containsAll(Collection<?> c) {
         this.refreshIfEmpty();
         return this.delegate.containsAll(c);
      }

      public void clear() {
         int oldSize = this.size();
         if (oldSize != 0) {
            this.delegate.clear();
            AbstractMapBasedMultimap.this.totalSize = oldSize;
            this.removeIfEmpty();
         }
      }

      public boolean remove(@CheckForNull Object o) {
         this.refreshIfEmpty();
         boolean changed = this.delegate.remove(o);
         if (changed) {
            AbstractMapBasedMultimap.this.totalSize--;
            this.removeIfEmpty();
         }

         return changed;
      }

      public boolean removeAll(Collection<?> c) {
         if (c.isEmpty()) {
            return false;
         } else {
            int oldSize = this.size();
            boolean changed = this.delegate.removeAll(c);
            if (changed) {
               int newSize = this.delegate.size();
               AbstractMapBasedMultimap.this.totalSize = newSize - oldSize;
               this.removeIfEmpty();
            }

            return changed;
         }
      }

      public boolean retainAll(Collection<?> c) {
         Preconditions.checkNotNull(c);
         int oldSize = this.size();
         boolean changed = this.delegate.retainAll(c);
         if (changed) {
            int newSize = this.delegate.size();
            AbstractMapBasedMultimap.this.totalSize = newSize - oldSize;
            this.removeIfEmpty();
         }

         return changed;
      }

      class WrappedIterator implements Iterator<V> {
         final Iterator<V> delegateIterator;
         final Collection<V> originalDelegate;

         WrappedIterator() {
            this.originalDelegate = WrappedCollection.this.delegate;
            this.delegateIterator = AbstractMapBasedMultimap.iteratorOrListIterator(WrappedCollection.this.delegate);
         }

         WrappedIterator(Iterator<V> delegateIterator) {
            this.originalDelegate = WrappedCollection.this.delegate;
            this.delegateIterator = delegateIterator;
         }

         void validateIterator() {
            WrappedCollection.this.refreshIfEmpty();
            if (WrappedCollection.this.delegate != this.originalDelegate) {
               throw new ConcurrentModificationException();
            }
         }

         public boolean hasNext() {
            this.validateIterator();
            return this.delegateIterator.hasNext();
         }

         @ParametricNullness
         public V next() {
            this.validateIterator();
            return this.delegateIterator.next();
         }

         public void remove() {
            this.delegateIterator.remove();
            AbstractMapBasedMultimap.this.totalSize--;
            WrappedCollection.this.removeIfEmpty();
         }

         Iterator<V> getDelegateIterator() {
            this.validateIterator();
            return this.delegateIterator;
         }
      }
   }
}
