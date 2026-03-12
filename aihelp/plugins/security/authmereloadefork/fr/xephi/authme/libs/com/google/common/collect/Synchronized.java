package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.annotations.VisibleForTesting;
import fr.xephi.authme.libs.com.google.common.base.Function;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
final class Synchronized {
   private Synchronized() {
   }

   private static <E> Collection<E> collection(Collection<E> collection, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedCollection(collection, mutex);
   }

   @VisibleForTesting
   static <E> Set<E> set(Set<E> set, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedSet(set, mutex);
   }

   private static <E> SortedSet<E> sortedSet(SortedSet<E> set, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedSortedSet(set, mutex);
   }

   private static <E> List<E> list(List<E> list, @CheckForNull Object mutex) {
      return (List)(list instanceof RandomAccess ? new Synchronized.SynchronizedRandomAccessList(list, mutex) : new Synchronized.SynchronizedList(list, mutex));
   }

   static <E> Multiset<E> multiset(Multiset<E> multiset, @CheckForNull Object mutex) {
      return (Multiset)(!(multiset instanceof Synchronized.SynchronizedMultiset) && !(multiset instanceof ImmutableMultiset) ? new Synchronized.SynchronizedMultiset(multiset, mutex) : multiset);
   }

   static <K, V> Multimap<K, V> multimap(Multimap<K, V> multimap, @CheckForNull Object mutex) {
      return (Multimap)(!(multimap instanceof Synchronized.SynchronizedMultimap) && !(multimap instanceof BaseImmutableMultimap) ? new Synchronized.SynchronizedMultimap(multimap, mutex) : multimap);
   }

   static <K, V> ListMultimap<K, V> listMultimap(ListMultimap<K, V> multimap, @CheckForNull Object mutex) {
      return (ListMultimap)(!(multimap instanceof Synchronized.SynchronizedListMultimap) && !(multimap instanceof BaseImmutableMultimap) ? new Synchronized.SynchronizedListMultimap(multimap, mutex) : multimap);
   }

   static <K, V> SetMultimap<K, V> setMultimap(SetMultimap<K, V> multimap, @CheckForNull Object mutex) {
      return (SetMultimap)(!(multimap instanceof Synchronized.SynchronizedSetMultimap) && !(multimap instanceof BaseImmutableMultimap) ? new Synchronized.SynchronizedSetMultimap(multimap, mutex) : multimap);
   }

   static <K, V> SortedSetMultimap<K, V> sortedSetMultimap(SortedSetMultimap<K, V> multimap, @CheckForNull Object mutex) {
      return (SortedSetMultimap)(multimap instanceof Synchronized.SynchronizedSortedSetMultimap ? multimap : new Synchronized.SynchronizedSortedSetMultimap(multimap, mutex));
   }

   private static <E> Collection<E> typePreservingCollection(Collection<E> collection, @CheckForNull Object mutex) {
      if (collection instanceof SortedSet) {
         return sortedSet((SortedSet)collection, mutex);
      } else if (collection instanceof Set) {
         return set((Set)collection, mutex);
      } else {
         return (Collection)(collection instanceof List ? list((List)collection, mutex) : collection(collection, mutex));
      }
   }

   private static <E> Set<E> typePreservingSet(Set<E> set, @CheckForNull Object mutex) {
      return (Set)(set instanceof SortedSet ? sortedSet((SortedSet)set, mutex) : set(set, mutex));
   }

   @VisibleForTesting
   static <K, V> Map<K, V> map(Map<K, V> map, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedMap(map, mutex);
   }

   static <K, V> SortedMap<K, V> sortedMap(SortedMap<K, V> sortedMap, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedSortedMap(sortedMap, mutex);
   }

   static <K, V> BiMap<K, V> biMap(BiMap<K, V> bimap, @CheckForNull Object mutex) {
      return (BiMap)(!(bimap instanceof Synchronized.SynchronizedBiMap) && !(bimap instanceof ImmutableBiMap) ? new Synchronized.SynchronizedBiMap(bimap, mutex, (BiMap)null) : bimap);
   }

   @GwtIncompatible
   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedNavigableSet(navigableSet, mutex);
   }

   @GwtIncompatible
   static <E> NavigableSet<E> navigableSet(NavigableSet<E> navigableSet) {
      return navigableSet(navigableSet, (Object)null);
   }

   @GwtIncompatible
   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap) {
      return navigableMap(navigableMap, (Object)null);
   }

   @GwtIncompatible
   static <K, V> NavigableMap<K, V> navigableMap(NavigableMap<K, V> navigableMap, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedNavigableMap(navigableMap, mutex);
   }

   @CheckForNull
   @GwtIncompatible
   private static <K, V> Entry<K, V> nullableSynchronizedEntry(@CheckForNull Entry<K, V> entry, @CheckForNull Object mutex) {
      return entry == null ? null : new Synchronized.SynchronizedEntry(entry, mutex);
   }

   static <E> Queue<E> queue(Queue<E> queue, @CheckForNull Object mutex) {
      return (Queue)(queue instanceof Synchronized.SynchronizedQueue ? queue : new Synchronized.SynchronizedQueue(queue, mutex));
   }

   static <E> Deque<E> deque(Deque<E> deque, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedDeque(deque, mutex);
   }

   static <R, C, V> Table<R, C, V> table(Table<R, C, V> table, @CheckForNull Object mutex) {
      return new Synchronized.SynchronizedTable(table, mutex);
   }

   private static final class SynchronizedTable<R, C, V> extends Synchronized.SynchronizedObject implements Table<R, C, V> {
      SynchronizedTable(Table<R, C, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      Table<R, C, V> delegate() {
         return (Table)super.delegate();
      }

      public boolean contains(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
         synchronized(this.mutex) {
            return this.delegate().contains(rowKey, columnKey);
         }
      }

      public boolean containsRow(@CheckForNull Object rowKey) {
         synchronized(this.mutex) {
            return this.delegate().containsRow(rowKey);
         }
      }

      public boolean containsColumn(@CheckForNull Object columnKey) {
         synchronized(this.mutex) {
            return this.delegate().containsColumn(columnKey);
         }
      }

      public boolean containsValue(@CheckForNull Object value) {
         synchronized(this.mutex) {
            return this.delegate().containsValue(value);
         }
      }

      @CheckForNull
      public V get(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
         synchronized(this.mutex) {
            return this.delegate().get(rowKey, columnKey);
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.delegate().isEmpty();
         }
      }

      public int size() {
         synchronized(this.mutex) {
            return this.delegate().size();
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.delegate().clear();
         }
      }

      @CheckForNull
      public V put(R rowKey, C columnKey, V value) {
         synchronized(this.mutex) {
            return this.delegate().put(rowKey, columnKey, value);
         }
      }

      public void putAll(Table<? extends R, ? extends C, ? extends V> table) {
         synchronized(this.mutex) {
            this.delegate().putAll(table);
         }
      }

      @CheckForNull
      public V remove(@CheckForNull Object rowKey, @CheckForNull Object columnKey) {
         synchronized(this.mutex) {
            return this.delegate().remove(rowKey, columnKey);
         }
      }

      public Map<C, V> row(R rowKey) {
         synchronized(this.mutex) {
            return Synchronized.map(this.delegate().row(rowKey), this.mutex);
         }
      }

      public Map<R, V> column(C columnKey) {
         synchronized(this.mutex) {
            return Synchronized.map(this.delegate().column(columnKey), this.mutex);
         }
      }

      public Set<Table.Cell<R, C, V>> cellSet() {
         synchronized(this.mutex) {
            return Synchronized.set(this.delegate().cellSet(), this.mutex);
         }
      }

      public Set<R> rowKeySet() {
         synchronized(this.mutex) {
            return Synchronized.set(this.delegate().rowKeySet(), this.mutex);
         }
      }

      public Set<C> columnKeySet() {
         synchronized(this.mutex) {
            return Synchronized.set(this.delegate().columnKeySet(), this.mutex);
         }
      }

      public Collection<V> values() {
         synchronized(this.mutex) {
            return Synchronized.collection(this.delegate().values(), this.mutex);
         }
      }

      public Map<R, Map<C, V>> rowMap() {
         synchronized(this.mutex) {
            return Synchronized.map(Maps.transformValues(this.delegate().rowMap(), new Function<Map<C, V>, Map<C, V>>() {
               public Map<C, V> apply(Map<C, V> t) {
                  return Synchronized.map(t, SynchronizedTable.this.mutex);
               }
            }), this.mutex);
         }
      }

      public Map<C, Map<R, V>> columnMap() {
         synchronized(this.mutex) {
            return Synchronized.map(Maps.transformValues(this.delegate().columnMap(), new Function<Map<R, V>, Map<R, V>>() {
               public Map<R, V> apply(Map<R, V> t) {
                  return Synchronized.map(t, SynchronizedTable.this.mutex);
               }
            }), this.mutex);
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }

      public boolean equals(@CheckForNull Object obj) {
         if (this == obj) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.delegate().equals(obj);
            }
         }
      }
   }

   private static final class SynchronizedDeque<E> extends Synchronized.SynchronizedQueue<E> implements Deque<E> {
      private static final long serialVersionUID = 0L;

      SynchronizedDeque(Deque<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      Deque<E> delegate() {
         return (Deque)super.delegate();
      }

      public void addFirst(E e) {
         synchronized(this.mutex) {
            this.delegate().addFirst(e);
         }
      }

      public void addLast(E e) {
         synchronized(this.mutex) {
            this.delegate().addLast(e);
         }
      }

      public boolean offerFirst(E e) {
         synchronized(this.mutex) {
            return this.delegate().offerFirst(e);
         }
      }

      public boolean offerLast(E e) {
         synchronized(this.mutex) {
            return this.delegate().offerLast(e);
         }
      }

      public E removeFirst() {
         synchronized(this.mutex) {
            return this.delegate().removeFirst();
         }
      }

      public E removeLast() {
         synchronized(this.mutex) {
            return this.delegate().removeLast();
         }
      }

      @CheckForNull
      public E pollFirst() {
         synchronized(this.mutex) {
            return this.delegate().pollFirst();
         }
      }

      @CheckForNull
      public E pollLast() {
         synchronized(this.mutex) {
            return this.delegate().pollLast();
         }
      }

      public E getFirst() {
         synchronized(this.mutex) {
            return this.delegate().getFirst();
         }
      }

      public E getLast() {
         synchronized(this.mutex) {
            return this.delegate().getLast();
         }
      }

      @CheckForNull
      public E peekFirst() {
         synchronized(this.mutex) {
            return this.delegate().peekFirst();
         }
      }

      @CheckForNull
      public E peekLast() {
         synchronized(this.mutex) {
            return this.delegate().peekLast();
         }
      }

      public boolean removeFirstOccurrence(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().removeFirstOccurrence(o);
         }
      }

      public boolean removeLastOccurrence(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().removeLastOccurrence(o);
         }
      }

      public void push(E e) {
         synchronized(this.mutex) {
            this.delegate().push(e);
         }
      }

      public E pop() {
         synchronized(this.mutex) {
            return this.delegate().pop();
         }
      }

      public Iterator<E> descendingIterator() {
         synchronized(this.mutex) {
            return this.delegate().descendingIterator();
         }
      }
   }

   private static class SynchronizedQueue<E> extends Synchronized.SynchronizedCollection<E> implements Queue<E> {
      private static final long serialVersionUID = 0L;

      SynchronizedQueue(Queue<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex, null);
      }

      Queue<E> delegate() {
         return (Queue)super.delegate();
      }

      public E element() {
         synchronized(this.mutex) {
            return this.delegate().element();
         }
      }

      public boolean offer(E e) {
         synchronized(this.mutex) {
            return this.delegate().offer(e);
         }
      }

      @CheckForNull
      public E peek() {
         synchronized(this.mutex) {
            return this.delegate().peek();
         }
      }

      @CheckForNull
      public E poll() {
         synchronized(this.mutex) {
            return this.delegate().poll();
         }
      }

      public E remove() {
         synchronized(this.mutex) {
            return this.delegate().remove();
         }
      }
   }

   @GwtIncompatible
   private static class SynchronizedEntry<K, V> extends Synchronized.SynchronizedObject implements Entry<K, V> {
      private static final long serialVersionUID = 0L;

      SynchronizedEntry(Entry<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      Entry<K, V> delegate() {
         return (Entry)super.delegate();
      }

      public boolean equals(@CheckForNull Object obj) {
         synchronized(this.mutex) {
            return this.delegate().equals(obj);
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }

      public K getKey() {
         synchronized(this.mutex) {
            return this.delegate().getKey();
         }
      }

      public V getValue() {
         synchronized(this.mutex) {
            return this.delegate().getValue();
         }
      }

      public V setValue(V value) {
         synchronized(this.mutex) {
            return this.delegate().setValue(value);
         }
      }
   }

   @GwtIncompatible
   @VisibleForTesting
   static class SynchronizedNavigableMap<K, V> extends Synchronized.SynchronizedSortedMap<K, V> implements NavigableMap<K, V> {
      @CheckForNull
      transient NavigableSet<K> descendingKeySet;
      @CheckForNull
      transient NavigableMap<K, V> descendingMap;
      @CheckForNull
      transient NavigableSet<K> navigableKeySet;
      private static final long serialVersionUID = 0L;

      SynchronizedNavigableMap(NavigableMap<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      NavigableMap<K, V> delegate() {
         return (NavigableMap)super.delegate();
      }

      @CheckForNull
      public Entry<K, V> ceilingEntry(K key) {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().ceilingEntry(key), this.mutex);
         }
      }

      @CheckForNull
      public K ceilingKey(K key) {
         synchronized(this.mutex) {
            return this.delegate().ceilingKey(key);
         }
      }

      public NavigableSet<K> descendingKeySet() {
         synchronized(this.mutex) {
            return this.descendingKeySet == null ? (this.descendingKeySet = Synchronized.navigableSet(this.delegate().descendingKeySet(), this.mutex)) : this.descendingKeySet;
         }
      }

      public NavigableMap<K, V> descendingMap() {
         synchronized(this.mutex) {
            return this.descendingMap == null ? (this.descendingMap = Synchronized.navigableMap(this.delegate().descendingMap(), this.mutex)) : this.descendingMap;
         }
      }

      @CheckForNull
      public Entry<K, V> firstEntry() {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().firstEntry(), this.mutex);
         }
      }

      @CheckForNull
      public Entry<K, V> floorEntry(K key) {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().floorEntry(key), this.mutex);
         }
      }

      @CheckForNull
      public K floorKey(K key) {
         synchronized(this.mutex) {
            return this.delegate().floorKey(key);
         }
      }

      public NavigableMap<K, V> headMap(K toKey, boolean inclusive) {
         synchronized(this.mutex) {
            return Synchronized.navigableMap(this.delegate().headMap(toKey, inclusive), this.mutex);
         }
      }

      public SortedMap<K, V> headMap(K toKey) {
         return this.headMap(toKey, false);
      }

      @CheckForNull
      public Entry<K, V> higherEntry(K key) {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().higherEntry(key), this.mutex);
         }
      }

      @CheckForNull
      public K higherKey(K key) {
         synchronized(this.mutex) {
            return this.delegate().higherKey(key);
         }
      }

      @CheckForNull
      public Entry<K, V> lastEntry() {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().lastEntry(), this.mutex);
         }
      }

      @CheckForNull
      public Entry<K, V> lowerEntry(K key) {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().lowerEntry(key), this.mutex);
         }
      }

      @CheckForNull
      public K lowerKey(K key) {
         synchronized(this.mutex) {
            return this.delegate().lowerKey(key);
         }
      }

      public Set<K> keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet<K> navigableKeySet() {
         synchronized(this.mutex) {
            return this.navigableKeySet == null ? (this.navigableKeySet = Synchronized.navigableSet(this.delegate().navigableKeySet(), this.mutex)) : this.navigableKeySet;
         }
      }

      @CheckForNull
      public Entry<K, V> pollFirstEntry() {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().pollFirstEntry(), this.mutex);
         }
      }

      @CheckForNull
      public Entry<K, V> pollLastEntry() {
         synchronized(this.mutex) {
            return Synchronized.nullableSynchronizedEntry(this.delegate().pollLastEntry(), this.mutex);
         }
      }

      public NavigableMap<K, V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
         synchronized(this.mutex) {
            return Synchronized.navigableMap(this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive), this.mutex);
         }
      }

      public SortedMap<K, V> subMap(K fromKey, K toKey) {
         return this.subMap(fromKey, true, toKey, false);
      }

      public NavigableMap<K, V> tailMap(K fromKey, boolean inclusive) {
         synchronized(this.mutex) {
            return Synchronized.navigableMap(this.delegate().tailMap(fromKey, inclusive), this.mutex);
         }
      }

      public SortedMap<K, V> tailMap(K fromKey) {
         return this.tailMap(fromKey, true);
      }
   }

   @GwtIncompatible
   @VisibleForTesting
   static class SynchronizedNavigableSet<E> extends Synchronized.SynchronizedSortedSet<E> implements NavigableSet<E> {
      @CheckForNull
      transient NavigableSet<E> descendingSet;
      private static final long serialVersionUID = 0L;

      SynchronizedNavigableSet(NavigableSet<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      NavigableSet<E> delegate() {
         return (NavigableSet)super.delegate();
      }

      @CheckForNull
      public E ceiling(E e) {
         synchronized(this.mutex) {
            return this.delegate().ceiling(e);
         }
      }

      public Iterator<E> descendingIterator() {
         return this.delegate().descendingIterator();
      }

      public NavigableSet<E> descendingSet() {
         synchronized(this.mutex) {
            if (this.descendingSet == null) {
               NavigableSet<E> dS = Synchronized.navigableSet(this.delegate().descendingSet(), this.mutex);
               this.descendingSet = dS;
               return dS;
            } else {
               return this.descendingSet;
            }
         }
      }

      @CheckForNull
      public E floor(E e) {
         synchronized(this.mutex) {
            return this.delegate().floor(e);
         }
      }

      public NavigableSet<E> headSet(E toElement, boolean inclusive) {
         synchronized(this.mutex) {
            return Synchronized.navigableSet(this.delegate().headSet(toElement, inclusive), this.mutex);
         }
      }

      public SortedSet<E> headSet(E toElement) {
         return this.headSet(toElement, false);
      }

      @CheckForNull
      public E higher(E e) {
         synchronized(this.mutex) {
            return this.delegate().higher(e);
         }
      }

      @CheckForNull
      public E lower(E e) {
         synchronized(this.mutex) {
            return this.delegate().lower(e);
         }
      }

      @CheckForNull
      public E pollFirst() {
         synchronized(this.mutex) {
            return this.delegate().pollFirst();
         }
      }

      @CheckForNull
      public E pollLast() {
         synchronized(this.mutex) {
            return this.delegate().pollLast();
         }
      }

      public NavigableSet<E> subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive) {
         synchronized(this.mutex) {
            return Synchronized.navigableSet(this.delegate().subSet(fromElement, fromInclusive, toElement, toInclusive), this.mutex);
         }
      }

      public SortedSet<E> subSet(E fromElement, E toElement) {
         return this.subSet(fromElement, true, toElement, false);
      }

      public NavigableSet<E> tailSet(E fromElement, boolean inclusive) {
         synchronized(this.mutex) {
            return Synchronized.navigableSet(this.delegate().tailSet(fromElement, inclusive), this.mutex);
         }
      }

      public SortedSet<E> tailSet(E fromElement) {
         return this.tailSet(fromElement, true);
      }
   }

   private static class SynchronizedAsMapValues<V> extends Synchronized.SynchronizedCollection<Collection<V>> {
      private static final long serialVersionUID = 0L;

      SynchronizedAsMapValues(Collection<Collection<V>> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex, null);
      }

      public Iterator<Collection<V>> iterator() {
         return new TransformedIterator<Collection<V>, Collection<V>>(super.iterator()) {
            Collection<V> transform(Collection<V> from) {
               return Synchronized.typePreservingCollection(from, SynchronizedAsMapValues.this.mutex);
            }
         };
      }
   }

   private static class SynchronizedAsMap<K, V> extends Synchronized.SynchronizedMap<K, Collection<V>> {
      @CheckForNull
      transient Set<Entry<K, Collection<V>>> asMapEntrySet;
      @CheckForNull
      transient Collection<Collection<V>> asMapValues;
      private static final long serialVersionUID = 0L;

      SynchronizedAsMap(Map<K, Collection<V>> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      @CheckForNull
      public Collection<V> get(@CheckForNull Object key) {
         synchronized(this.mutex) {
            Collection<V> collection = (Collection)super.get(key);
            return collection == null ? null : Synchronized.typePreservingCollection(collection, this.mutex);
         }
      }

      public Set<Entry<K, Collection<V>>> entrySet() {
         synchronized(this.mutex) {
            if (this.asMapEntrySet == null) {
               this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries(this.delegate().entrySet(), this.mutex);
            }

            return this.asMapEntrySet;
         }
      }

      public Collection<Collection<V>> values() {
         synchronized(this.mutex) {
            if (this.asMapValues == null) {
               this.asMapValues = new Synchronized.SynchronizedAsMapValues(this.delegate().values(), this.mutex);
            }

            return this.asMapValues;
         }
      }

      public boolean containsValue(@CheckForNull Object o) {
         return this.values().contains(o);
      }
   }

   @VisibleForTesting
   static class SynchronizedBiMap<K, V> extends Synchronized.SynchronizedMap<K, V> implements BiMap<K, V>, Serializable {
      @CheckForNull
      private transient Set<V> valueSet;
      @CheckForNull
      @RetainedWith
      private transient BiMap<V, K> inverse;
      private static final long serialVersionUID = 0L;

      private SynchronizedBiMap(BiMap<K, V> delegate, @CheckForNull Object mutex, @CheckForNull BiMap<V, K> inverse) {
         super(delegate, mutex);
         this.inverse = inverse;
      }

      BiMap<K, V> delegate() {
         return (BiMap)super.delegate();
      }

      public Set<V> values() {
         synchronized(this.mutex) {
            if (this.valueSet == null) {
               this.valueSet = Synchronized.set(this.delegate().values(), this.mutex);
            }

            return this.valueSet;
         }
      }

      @CheckForNull
      public V forcePut(K key, V value) {
         synchronized(this.mutex) {
            return this.delegate().forcePut(key, value);
         }
      }

      public BiMap<V, K> inverse() {
         synchronized(this.mutex) {
            if (this.inverse == null) {
               this.inverse = new Synchronized.SynchronizedBiMap(this.delegate().inverse(), this.mutex, this);
            }

            return this.inverse;
         }
      }

      // $FF: synthetic method
      SynchronizedBiMap(BiMap x0, Object x1, BiMap x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   static class SynchronizedSortedMap<K, V> extends Synchronized.SynchronizedMap<K, V> implements SortedMap<K, V> {
      private static final long serialVersionUID = 0L;

      SynchronizedSortedMap(SortedMap<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      SortedMap<K, V> delegate() {
         return (SortedMap)super.delegate();
      }

      @CheckForNull
      public Comparator<? super K> comparator() {
         synchronized(this.mutex) {
            return this.delegate().comparator();
         }
      }

      public K firstKey() {
         synchronized(this.mutex) {
            return this.delegate().firstKey();
         }
      }

      public SortedMap<K, V> headMap(K toKey) {
         synchronized(this.mutex) {
            return Synchronized.sortedMap(this.delegate().headMap(toKey), this.mutex);
         }
      }

      public K lastKey() {
         synchronized(this.mutex) {
            return this.delegate().lastKey();
         }
      }

      public SortedMap<K, V> subMap(K fromKey, K toKey) {
         synchronized(this.mutex) {
            return Synchronized.sortedMap(this.delegate().subMap(fromKey, toKey), this.mutex);
         }
      }

      public SortedMap<K, V> tailMap(K fromKey) {
         synchronized(this.mutex) {
            return Synchronized.sortedMap(this.delegate().tailMap(fromKey), this.mutex);
         }
      }
   }

   private static class SynchronizedMap<K, V> extends Synchronized.SynchronizedObject implements Map<K, V> {
      @CheckForNull
      transient Set<K> keySet;
      @CheckForNull
      transient Collection<V> values;
      @CheckForNull
      transient Set<Entry<K, V>> entrySet;
      private static final long serialVersionUID = 0L;

      SynchronizedMap(Map<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      Map<K, V> delegate() {
         return (Map)super.delegate();
      }

      public void clear() {
         synchronized(this.mutex) {
            this.delegate().clear();
         }
      }

      public boolean containsKey(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().containsKey(key);
         }
      }

      public boolean containsValue(@CheckForNull Object value) {
         synchronized(this.mutex) {
            return this.delegate().containsValue(value);
         }
      }

      public Set<Entry<K, V>> entrySet() {
         synchronized(this.mutex) {
            if (this.entrySet == null) {
               this.entrySet = Synchronized.set(this.delegate().entrySet(), this.mutex);
            }

            return this.entrySet;
         }
      }

      public void forEach(BiConsumer<? super K, ? super V> action) {
         synchronized(this.mutex) {
            this.delegate().forEach(action);
         }
      }

      @CheckForNull
      public V get(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().get(key);
         }
      }

      @CheckForNull
      public V getOrDefault(@CheckForNull Object key, @CheckForNull V defaultValue) {
         synchronized(this.mutex) {
            return this.delegate().getOrDefault(key, defaultValue);
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.delegate().isEmpty();
         }
      }

      public Set<K> keySet() {
         synchronized(this.mutex) {
            if (this.keySet == null) {
               this.keySet = Synchronized.set(this.delegate().keySet(), this.mutex);
            }

            return this.keySet;
         }
      }

      @CheckForNull
      public V put(K key, V value) {
         synchronized(this.mutex) {
            return this.delegate().put(key, value);
         }
      }

      @CheckForNull
      public V putIfAbsent(K key, V value) {
         synchronized(this.mutex) {
            return this.delegate().putIfAbsent(key, value);
         }
      }

      public boolean replace(K key, V oldValue, V newValue) {
         synchronized(this.mutex) {
            return this.delegate().replace(key, oldValue, newValue);
         }
      }

      @CheckForNull
      public V replace(K key, V value) {
         synchronized(this.mutex) {
            return this.delegate().replace(key, value);
         }
      }

      public V computeIfAbsent(K key, java.util.function.Function<? super K, ? extends V> mappingFunction) {
         synchronized(this.mutex) {
            return this.delegate().computeIfAbsent(key, mappingFunction);
         }
      }

      public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
         synchronized(this.mutex) {
            return this.delegate().computeIfPresent(key, remappingFunction);
         }
      }

      public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
         synchronized(this.mutex) {
            return this.delegate().compute(key, remappingFunction);
         }
      }

      public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
         synchronized(this.mutex) {
            return this.delegate().merge(key, value, remappingFunction);
         }
      }

      public void putAll(Map<? extends K, ? extends V> map) {
         synchronized(this.mutex) {
            this.delegate().putAll(map);
         }
      }

      public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
         synchronized(this.mutex) {
            this.delegate().replaceAll(function);
         }
      }

      @CheckForNull
      public V remove(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().remove(key);
         }
      }

      public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
         synchronized(this.mutex) {
            return this.delegate().remove(key, value);
         }
      }

      public int size() {
         synchronized(this.mutex) {
            return this.delegate().size();
         }
      }

      public Collection<V> values() {
         synchronized(this.mutex) {
            if (this.values == null) {
               this.values = Synchronized.collection(this.delegate().values(), this.mutex);
            }

            return this.values;
         }
      }

      public boolean equals(@CheckForNull Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.delegate().equals(o);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }
   }

   private static class SynchronizedAsMapEntries<K, V> extends Synchronized.SynchronizedSet<Entry<K, Collection<V>>> {
      private static final long serialVersionUID = 0L;

      SynchronizedAsMapEntries(Set<Entry<K, Collection<V>>> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      public Iterator<Entry<K, Collection<V>>> iterator() {
         return new TransformedIterator<Entry<K, Collection<V>>, Entry<K, Collection<V>>>(super.iterator()) {
            Entry<K, Collection<V>> transform(final Entry<K, Collection<V>> entry) {
               return new ForwardingMapEntry<K, Collection<V>>() {
                  protected Entry<K, Collection<V>> delegate() {
                     return entry;
                  }

                  public Collection<V> getValue() {
                     return Synchronized.typePreservingCollection((Collection)entry.getValue(), SynchronizedAsMapEntries.this.mutex);
                  }
               };
            }
         };
      }

      public Object[] toArray() {
         synchronized(this.mutex) {
            Object[] result = ObjectArrays.toArrayImpl(this.delegate());
            return result;
         }
      }

      public <T> T[] toArray(T[] array) {
         synchronized(this.mutex) {
            return ObjectArrays.toArrayImpl(this.delegate(), array);
         }
      }

      public boolean contains(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return Maps.containsEntryImpl(this.delegate(), o);
         }
      }

      public boolean containsAll(Collection<?> c) {
         synchronized(this.mutex) {
            return Collections2.containsAllImpl(this.delegate(), c);
         }
      }

      public boolean equals(@CheckForNull Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return Sets.equalsImpl(this.delegate(), o);
            }
         }
      }

      public boolean remove(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return Maps.removeEntryImpl(this.delegate(), o);
         }
      }

      public boolean removeAll(Collection<?> c) {
         synchronized(this.mutex) {
            return Iterators.removeAll(this.delegate().iterator(), c);
         }
      }

      public boolean retainAll(Collection<?> c) {
         synchronized(this.mutex) {
            return Iterators.retainAll(this.delegate().iterator(), c);
         }
      }
   }

   private static class SynchronizedSortedSetMultimap<K, V> extends Synchronized.SynchronizedSetMultimap<K, V> implements SortedSetMultimap<K, V> {
      private static final long serialVersionUID = 0L;

      SynchronizedSortedSetMultimap(SortedSetMultimap<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      SortedSetMultimap<K, V> delegate() {
         return (SortedSetMultimap)super.delegate();
      }

      public SortedSet<V> get(K key) {
         synchronized(this.mutex) {
            return Synchronized.sortedSet(this.delegate().get(key), this.mutex);
         }
      }

      public SortedSet<V> removeAll(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().removeAll(key);
         }
      }

      public SortedSet<V> replaceValues(K key, Iterable<? extends V> values) {
         synchronized(this.mutex) {
            return this.delegate().replaceValues(key, values);
         }
      }

      @CheckForNull
      public Comparator<? super V> valueComparator() {
         synchronized(this.mutex) {
            return this.delegate().valueComparator();
         }
      }
   }

   private static class SynchronizedSetMultimap<K, V> extends Synchronized.SynchronizedMultimap<K, V> implements SetMultimap<K, V> {
      @CheckForNull
      transient Set<Entry<K, V>> entrySet;
      private static final long serialVersionUID = 0L;

      SynchronizedSetMultimap(SetMultimap<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      SetMultimap<K, V> delegate() {
         return (SetMultimap)super.delegate();
      }

      public Set<V> get(K key) {
         synchronized(this.mutex) {
            return Synchronized.set(this.delegate().get(key), this.mutex);
         }
      }

      public Set<V> removeAll(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().removeAll(key);
         }
      }

      public Set<V> replaceValues(K key, Iterable<? extends V> values) {
         synchronized(this.mutex) {
            return this.delegate().replaceValues(key, values);
         }
      }

      public Set<Entry<K, V>> entries() {
         synchronized(this.mutex) {
            if (this.entrySet == null) {
               this.entrySet = Synchronized.set(this.delegate().entries(), this.mutex);
            }

            return this.entrySet;
         }
      }
   }

   private static class SynchronizedListMultimap<K, V> extends Synchronized.SynchronizedMultimap<K, V> implements ListMultimap<K, V> {
      private static final long serialVersionUID = 0L;

      SynchronizedListMultimap(ListMultimap<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      ListMultimap<K, V> delegate() {
         return (ListMultimap)super.delegate();
      }

      public List<V> get(K key) {
         synchronized(this.mutex) {
            return Synchronized.list(this.delegate().get(key), this.mutex);
         }
      }

      public List<V> removeAll(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().removeAll(key);
         }
      }

      public List<V> replaceValues(K key, Iterable<? extends V> values) {
         synchronized(this.mutex) {
            return this.delegate().replaceValues(key, values);
         }
      }
   }

   private static class SynchronizedMultimap<K, V> extends Synchronized.SynchronizedObject implements Multimap<K, V> {
      @CheckForNull
      transient Set<K> keySet;
      @CheckForNull
      transient Collection<V> valuesCollection;
      @CheckForNull
      transient Collection<Entry<K, V>> entries;
      @CheckForNull
      transient Map<K, Collection<V>> asMap;
      @CheckForNull
      transient Multiset<K> keys;
      private static final long serialVersionUID = 0L;

      Multimap<K, V> delegate() {
         return (Multimap)super.delegate();
      }

      SynchronizedMultimap(Multimap<K, V> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      public int size() {
         synchronized(this.mutex) {
            return this.delegate().size();
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.delegate().isEmpty();
         }
      }

      public boolean containsKey(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().containsKey(key);
         }
      }

      public boolean containsValue(@CheckForNull Object value) {
         synchronized(this.mutex) {
            return this.delegate().containsValue(value);
         }
      }

      public boolean containsEntry(@CheckForNull Object key, @CheckForNull Object value) {
         synchronized(this.mutex) {
            return this.delegate().containsEntry(key, value);
         }
      }

      public Collection<V> get(K key) {
         synchronized(this.mutex) {
            return Synchronized.typePreservingCollection(this.delegate().get(key), this.mutex);
         }
      }

      public boolean put(K key, V value) {
         synchronized(this.mutex) {
            return this.delegate().put(key, value);
         }
      }

      public boolean putAll(K key, Iterable<? extends V> values) {
         synchronized(this.mutex) {
            return this.delegate().putAll(key, values);
         }
      }

      public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
         synchronized(this.mutex) {
            return this.delegate().putAll(multimap);
         }
      }

      public Collection<V> replaceValues(K key, Iterable<? extends V> values) {
         synchronized(this.mutex) {
            return this.delegate().replaceValues(key, values);
         }
      }

      public boolean remove(@CheckForNull Object key, @CheckForNull Object value) {
         synchronized(this.mutex) {
            return this.delegate().remove(key, value);
         }
      }

      public Collection<V> removeAll(@CheckForNull Object key) {
         synchronized(this.mutex) {
            return this.delegate().removeAll(key);
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.delegate().clear();
         }
      }

      public Set<K> keySet() {
         synchronized(this.mutex) {
            if (this.keySet == null) {
               this.keySet = Synchronized.typePreservingSet(this.delegate().keySet(), this.mutex);
            }

            return this.keySet;
         }
      }

      public Collection<V> values() {
         synchronized(this.mutex) {
            if (this.valuesCollection == null) {
               this.valuesCollection = Synchronized.collection(this.delegate().values(), this.mutex);
            }

            return this.valuesCollection;
         }
      }

      public Collection<Entry<K, V>> entries() {
         synchronized(this.mutex) {
            if (this.entries == null) {
               this.entries = Synchronized.typePreservingCollection(this.delegate().entries(), this.mutex);
            }

            return this.entries;
         }
      }

      public void forEach(BiConsumer<? super K, ? super V> action) {
         synchronized(this.mutex) {
            this.delegate().forEach(action);
         }
      }

      public Map<K, Collection<V>> asMap() {
         synchronized(this.mutex) {
            if (this.asMap == null) {
               this.asMap = new Synchronized.SynchronizedAsMap(this.delegate().asMap(), this.mutex);
            }

            return this.asMap;
         }
      }

      public Multiset<K> keys() {
         synchronized(this.mutex) {
            if (this.keys == null) {
               this.keys = Synchronized.multiset(this.delegate().keys(), this.mutex);
            }

            return this.keys;
         }
      }

      public boolean equals(@CheckForNull Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.delegate().equals(o);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }
   }

   private static class SynchronizedMultiset<E> extends Synchronized.SynchronizedCollection<E> implements Multiset<E> {
      @CheckForNull
      transient Set<E> elementSet;
      @CheckForNull
      transient Set<Multiset.Entry<E>> entrySet;
      private static final long serialVersionUID = 0L;

      SynchronizedMultiset(Multiset<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex, null);
      }

      Multiset<E> delegate() {
         return (Multiset)super.delegate();
      }

      public int count(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().count(o);
         }
      }

      public int add(E e, int n) {
         synchronized(this.mutex) {
            return this.delegate().add(e, n);
         }
      }

      public int remove(@CheckForNull Object o, int n) {
         synchronized(this.mutex) {
            return this.delegate().remove(o, n);
         }
      }

      public int setCount(E element, int count) {
         synchronized(this.mutex) {
            return this.delegate().setCount(element, count);
         }
      }

      public boolean setCount(E element, int oldCount, int newCount) {
         synchronized(this.mutex) {
            return this.delegate().setCount(element, oldCount, newCount);
         }
      }

      public Set<E> elementSet() {
         synchronized(this.mutex) {
            if (this.elementSet == null) {
               this.elementSet = Synchronized.typePreservingSet(this.delegate().elementSet(), this.mutex);
            }

            return this.elementSet;
         }
      }

      public Set<Multiset.Entry<E>> entrySet() {
         synchronized(this.mutex) {
            if (this.entrySet == null) {
               this.entrySet = Synchronized.typePreservingSet(this.delegate().entrySet(), this.mutex);
            }

            return this.entrySet;
         }
      }

      public boolean equals(@CheckForNull Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.delegate().equals(o);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }
   }

   private static class SynchronizedRandomAccessList<E> extends Synchronized.SynchronizedList<E> implements RandomAccess {
      private static final long serialVersionUID = 0L;

      SynchronizedRandomAccessList(List<E> list, @CheckForNull Object mutex) {
         super(list, mutex);
      }
   }

   private static class SynchronizedList<E> extends Synchronized.SynchronizedCollection<E> implements List<E> {
      private static final long serialVersionUID = 0L;

      SynchronizedList(List<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex, null);
      }

      List<E> delegate() {
         return (List)super.delegate();
      }

      public void add(int index, E element) {
         synchronized(this.mutex) {
            this.delegate().add(index, element);
         }
      }

      public boolean addAll(int index, Collection<? extends E> c) {
         synchronized(this.mutex) {
            return this.delegate().addAll(index, c);
         }
      }

      public E get(int index) {
         synchronized(this.mutex) {
            return this.delegate().get(index);
         }
      }

      public int indexOf(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().indexOf(o);
         }
      }

      public int lastIndexOf(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().lastIndexOf(o);
         }
      }

      public ListIterator<E> listIterator() {
         return this.delegate().listIterator();
      }

      public ListIterator<E> listIterator(int index) {
         return this.delegate().listIterator(index);
      }

      public E remove(int index) {
         synchronized(this.mutex) {
            return this.delegate().remove(index);
         }
      }

      public E set(int index, E element) {
         synchronized(this.mutex) {
            return this.delegate().set(index, element);
         }
      }

      public void replaceAll(UnaryOperator<E> operator) {
         synchronized(this.mutex) {
            this.delegate().replaceAll(operator);
         }
      }

      public void sort(Comparator<? super E> c) {
         synchronized(this.mutex) {
            this.delegate().sort(c);
         }
      }

      public List<E> subList(int fromIndex, int toIndex) {
         synchronized(this.mutex) {
            return Synchronized.list(this.delegate().subList(fromIndex, toIndex), this.mutex);
         }
      }

      public boolean equals(@CheckForNull Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.delegate().equals(o);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }
   }

   static class SynchronizedSortedSet<E> extends Synchronized.SynchronizedSet<E> implements SortedSet<E> {
      private static final long serialVersionUID = 0L;

      SynchronizedSortedSet(SortedSet<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      SortedSet<E> delegate() {
         return (SortedSet)super.delegate();
      }

      @CheckForNull
      public Comparator<? super E> comparator() {
         synchronized(this.mutex) {
            return this.delegate().comparator();
         }
      }

      public SortedSet<E> subSet(E fromElement, E toElement) {
         synchronized(this.mutex) {
            return Synchronized.sortedSet(this.delegate().subSet(fromElement, toElement), this.mutex);
         }
      }

      public SortedSet<E> headSet(E toElement) {
         synchronized(this.mutex) {
            return Synchronized.sortedSet(this.delegate().headSet(toElement), this.mutex);
         }
      }

      public SortedSet<E> tailSet(E fromElement) {
         synchronized(this.mutex) {
            return Synchronized.sortedSet(this.delegate().tailSet(fromElement), this.mutex);
         }
      }

      public E first() {
         synchronized(this.mutex) {
            return this.delegate().first();
         }
      }

      public E last() {
         synchronized(this.mutex) {
            return this.delegate().last();
         }
      }
   }

   static class SynchronizedSet<E> extends Synchronized.SynchronizedCollection<E> implements Set<E> {
      private static final long serialVersionUID = 0L;

      SynchronizedSet(Set<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex, null);
      }

      Set<E> delegate() {
         return (Set)super.delegate();
      }

      public boolean equals(@CheckForNull Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.mutex) {
               return this.delegate().equals(o);
            }
         }
      }

      public int hashCode() {
         synchronized(this.mutex) {
            return this.delegate().hashCode();
         }
      }
   }

   @VisibleForTesting
   static class SynchronizedCollection<E> extends Synchronized.SynchronizedObject implements Collection<E> {
      private static final long serialVersionUID = 0L;

      private SynchronizedCollection(Collection<E> delegate, @CheckForNull Object mutex) {
         super(delegate, mutex);
      }

      Collection<E> delegate() {
         return (Collection)super.delegate();
      }

      public boolean add(E e) {
         synchronized(this.mutex) {
            return this.delegate().add(e);
         }
      }

      public boolean addAll(Collection<? extends E> c) {
         synchronized(this.mutex) {
            return this.delegate().addAll(c);
         }
      }

      public void clear() {
         synchronized(this.mutex) {
            this.delegate().clear();
         }
      }

      public boolean contains(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().contains(o);
         }
      }

      public boolean containsAll(Collection<?> c) {
         synchronized(this.mutex) {
            return this.delegate().containsAll(c);
         }
      }

      public boolean isEmpty() {
         synchronized(this.mutex) {
            return this.delegate().isEmpty();
         }
      }

      public Iterator<E> iterator() {
         return this.delegate().iterator();
      }

      public Spliterator<E> spliterator() {
         synchronized(this.mutex) {
            return this.delegate().spliterator();
         }
      }

      public Stream<E> stream() {
         synchronized(this.mutex) {
            return this.delegate().stream();
         }
      }

      public Stream<E> parallelStream() {
         synchronized(this.mutex) {
            return this.delegate().parallelStream();
         }
      }

      public void forEach(Consumer<? super E> action) {
         synchronized(this.mutex) {
            this.delegate().forEach(action);
         }
      }

      public boolean remove(@CheckForNull Object o) {
         synchronized(this.mutex) {
            return this.delegate().remove(o);
         }
      }

      public boolean removeAll(Collection<?> c) {
         synchronized(this.mutex) {
            return this.delegate().removeAll(c);
         }
      }

      public boolean retainAll(Collection<?> c) {
         synchronized(this.mutex) {
            return this.delegate().retainAll(c);
         }
      }

      public boolean removeIf(Predicate<? super E> filter) {
         synchronized(this.mutex) {
            return this.delegate().removeIf(filter);
         }
      }

      public int size() {
         synchronized(this.mutex) {
            return this.delegate().size();
         }
      }

      public Object[] toArray() {
         synchronized(this.mutex) {
            return this.delegate().toArray();
         }
      }

      public <T> T[] toArray(T[] a) {
         synchronized(this.mutex) {
            return this.delegate().toArray(a);
         }
      }

      // $FF: synthetic method
      SynchronizedCollection(Collection x0, Object x1, Object x2) {
         this(x0, x1);
      }
   }

   static class SynchronizedObject implements Serializable {
      final Object delegate;
      final Object mutex;
      @GwtIncompatible
      private static final long serialVersionUID = 0L;

      SynchronizedObject(Object delegate, @CheckForNull Object mutex) {
         this.delegate = Preconditions.checkNotNull(delegate);
         this.mutex = mutex == null ? this : mutex;
      }

      Object delegate() {
         return this.delegate;
      }

      public String toString() {
         synchronized(this.mutex) {
            return this.delegate.toString();
         }
      }

      @GwtIncompatible
      private void writeObject(ObjectOutputStream stream) throws IOException {
         synchronized(this.mutex) {
            stream.defaultWriteObject();
         }
      }
   }
}
