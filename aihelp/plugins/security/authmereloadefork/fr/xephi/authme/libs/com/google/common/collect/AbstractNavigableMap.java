package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
abstract class AbstractNavigableMap<K, V> extends Maps.IteratorBasedAbstractMap<K, V> implements NavigableMap<K, V> {
   @CheckForNull
   public abstract V get(@CheckForNull Object var1);

   @CheckForNull
   public Entry<K, V> firstEntry() {
      return (Entry)Iterators.getNext(this.entryIterator(), (Object)null);
   }

   @CheckForNull
   public Entry<K, V> lastEntry() {
      return (Entry)Iterators.getNext(this.descendingEntryIterator(), (Object)null);
   }

   @CheckForNull
   public Entry<K, V> pollFirstEntry() {
      return (Entry)Iterators.pollNext(this.entryIterator());
   }

   @CheckForNull
   public Entry<K, V> pollLastEntry() {
      return (Entry)Iterators.pollNext(this.descendingEntryIterator());
   }

   @ParametricNullness
   public K firstKey() {
      Entry<K, V> entry = this.firstEntry();
      if (entry == null) {
         throw new NoSuchElementException();
      } else {
         return entry.getKey();
      }
   }

   @ParametricNullness
   public K lastKey() {
      Entry<K, V> entry = this.lastEntry();
      if (entry == null) {
         throw new NoSuchElementException();
      } else {
         return entry.getKey();
      }
   }

   @CheckForNull
   public Entry<K, V> lowerEntry(@ParametricNullness K key) {
      return this.headMap(key, false).lastEntry();
   }

   @CheckForNull
   public Entry<K, V> floorEntry(@ParametricNullness K key) {
      return this.headMap(key, true).lastEntry();
   }

   @CheckForNull
   public Entry<K, V> ceilingEntry(@ParametricNullness K key) {
      return this.tailMap(key, true).firstEntry();
   }

   @CheckForNull
   public Entry<K, V> higherEntry(@ParametricNullness K key) {
      return this.tailMap(key, false).firstEntry();
   }

   @CheckForNull
   public K lowerKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.lowerEntry(key));
   }

   @CheckForNull
   public K floorKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.floorEntry(key));
   }

   @CheckForNull
   public K ceilingKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.ceilingEntry(key));
   }

   @CheckForNull
   public K higherKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.higherEntry(key));
   }

   abstract Iterator<Entry<K, V>> descendingEntryIterator();

   public SortedMap<K, V> subMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
      return this.subMap(fromKey, true, toKey, false);
   }

   public SortedMap<K, V> headMap(@ParametricNullness K toKey) {
      return this.headMap(toKey, false);
   }

   public SortedMap<K, V> tailMap(@ParametricNullness K fromKey) {
      return this.tailMap(fromKey, true);
   }

   public NavigableSet<K> navigableKeySet() {
      return new Maps.NavigableKeySet(this);
   }

   public Set<K> keySet() {
      return this.navigableKeySet();
   }

   public NavigableSet<K> descendingKeySet() {
      return this.descendingMap().navigableKeySet();
   }

   public NavigableMap<K, V> descendingMap() {
      return new AbstractNavigableMap.DescendingMap();
   }

   private final class DescendingMap extends Maps.DescendingMap<K, V> {
      private DescendingMap() {
      }

      NavigableMap<K, V> forward() {
         return AbstractNavigableMap.this;
      }

      Iterator<Entry<K, V>> entryIterator() {
         return AbstractNavigableMap.this.descendingEntryIterator();
      }

      // $FF: synthetic method
      DescendingMap(Object x1) {
         this();
      }
   }
}
