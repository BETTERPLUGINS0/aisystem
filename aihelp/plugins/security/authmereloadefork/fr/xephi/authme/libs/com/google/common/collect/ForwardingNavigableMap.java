package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public abstract class ForwardingNavigableMap<K, V> extends ForwardingSortedMap<K, V> implements NavigableMap<K, V> {
   protected ForwardingNavigableMap() {
   }

   protected abstract NavigableMap<K, V> delegate();

   @CheckForNull
   public Entry<K, V> lowerEntry(@ParametricNullness K key) {
      return this.delegate().lowerEntry(key);
   }

   @CheckForNull
   protected Entry<K, V> standardLowerEntry(@ParametricNullness K key) {
      return this.headMap(key, false).lastEntry();
   }

   @CheckForNull
   public K lowerKey(@ParametricNullness K key) {
      return this.delegate().lowerKey(key);
   }

   @CheckForNull
   protected K standardLowerKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.lowerEntry(key));
   }

   @CheckForNull
   public Entry<K, V> floorEntry(@ParametricNullness K key) {
      return this.delegate().floorEntry(key);
   }

   @CheckForNull
   protected Entry<K, V> standardFloorEntry(@ParametricNullness K key) {
      return this.headMap(key, true).lastEntry();
   }

   @CheckForNull
   public K floorKey(@ParametricNullness K key) {
      return this.delegate().floorKey(key);
   }

   @CheckForNull
   protected K standardFloorKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.floorEntry(key));
   }

   @CheckForNull
   public Entry<K, V> ceilingEntry(@ParametricNullness K key) {
      return this.delegate().ceilingEntry(key);
   }

   @CheckForNull
   protected Entry<K, V> standardCeilingEntry(@ParametricNullness K key) {
      return this.tailMap(key, true).firstEntry();
   }

   @CheckForNull
   public K ceilingKey(@ParametricNullness K key) {
      return this.delegate().ceilingKey(key);
   }

   @CheckForNull
   protected K standardCeilingKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.ceilingEntry(key));
   }

   @CheckForNull
   public Entry<K, V> higherEntry(@ParametricNullness K key) {
      return this.delegate().higherEntry(key);
   }

   @CheckForNull
   protected Entry<K, V> standardHigherEntry(@ParametricNullness K key) {
      return this.tailMap(key, false).firstEntry();
   }

   @CheckForNull
   public K higherKey(@ParametricNullness K key) {
      return this.delegate().higherKey(key);
   }

   @CheckForNull
   protected K standardHigherKey(@ParametricNullness K key) {
      return Maps.keyOrNull(this.higherEntry(key));
   }

   @CheckForNull
   public Entry<K, V> firstEntry() {
      return this.delegate().firstEntry();
   }

   @CheckForNull
   protected Entry<K, V> standardFirstEntry() {
      return (Entry)Iterables.getFirst(this.entrySet(), (Object)null);
   }

   protected K standardFirstKey() {
      Entry<K, V> entry = this.firstEntry();
      if (entry == null) {
         throw new NoSuchElementException();
      } else {
         return entry.getKey();
      }
   }

   @CheckForNull
   public Entry<K, V> lastEntry() {
      return this.delegate().lastEntry();
   }

   @CheckForNull
   protected Entry<K, V> standardLastEntry() {
      return (Entry)Iterables.getFirst(this.descendingMap().entrySet(), (Object)null);
   }

   protected K standardLastKey() {
      Entry<K, V> entry = this.lastEntry();
      if (entry == null) {
         throw new NoSuchElementException();
      } else {
         return entry.getKey();
      }
   }

   @CheckForNull
   public Entry<K, V> pollFirstEntry() {
      return this.delegate().pollFirstEntry();
   }

   @CheckForNull
   protected Entry<K, V> standardPollFirstEntry() {
      return (Entry)Iterators.pollNext(this.entrySet().iterator());
   }

   @CheckForNull
   public Entry<K, V> pollLastEntry() {
      return this.delegate().pollLastEntry();
   }

   @CheckForNull
   protected Entry<K, V> standardPollLastEntry() {
      return (Entry)Iterators.pollNext(this.descendingMap().entrySet().iterator());
   }

   public NavigableMap<K, V> descendingMap() {
      return this.delegate().descendingMap();
   }

   public NavigableSet<K> navigableKeySet() {
      return this.delegate().navigableKeySet();
   }

   public NavigableSet<K> descendingKeySet() {
      return this.delegate().descendingKeySet();
   }

   @Beta
   protected NavigableSet<K> standardDescendingKeySet() {
      return this.descendingMap().navigableKeySet();
   }

   protected SortedMap<K, V> standardSubMap(@ParametricNullness K fromKey, @ParametricNullness K toKey) {
      return this.subMap(fromKey, true, toKey, false);
   }

   public NavigableMap<K, V> subMap(@ParametricNullness K fromKey, boolean fromInclusive, @ParametricNullness K toKey, boolean toInclusive) {
      return this.delegate().subMap(fromKey, fromInclusive, toKey, toInclusive);
   }

   public NavigableMap<K, V> headMap(@ParametricNullness K toKey, boolean inclusive) {
      return this.delegate().headMap(toKey, inclusive);
   }

   public NavigableMap<K, V> tailMap(@ParametricNullness K fromKey, boolean inclusive) {
      return this.delegate().tailMap(fromKey, inclusive);
   }

   protected SortedMap<K, V> standardHeadMap(@ParametricNullness K toKey) {
      return this.headMap(toKey, false);
   }

   protected SortedMap<K, V> standardTailMap(@ParametricNullness K fromKey) {
      return this.tailMap(fromKey, true);
   }

   @Beta
   protected class StandardNavigableKeySet extends Maps.NavigableKeySet<K, V> {
      public StandardNavigableKeySet(ForwardingNavigableMap this$0) {
         super(this$0);
      }
   }

   @Beta
   protected class StandardDescendingMap extends Maps.DescendingMap<K, V> {
      public StandardDescendingMap() {
      }

      NavigableMap<K, V> forward() {
         return ForwardingNavigableMap.this;
      }

      public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
         this.forward().replaceAll(function);
      }

      protected Iterator<Entry<K, V>> entryIterator() {
         return new Iterator<Entry<K, V>>() {
            @CheckForNull
            private Entry<K, V> toRemove = null;
            @CheckForNull
            private Entry<K, V> nextOrNull = StandardDescendingMap.this.forward().lastEntry();

            public boolean hasNext() {
               return this.nextOrNull != null;
            }

            public Entry<K, V> next() {
               if (this.nextOrNull == null) {
                  throw new NoSuchElementException();
               } else {
                  Entry var1;
                  try {
                     var1 = this.nextOrNull;
                  } finally {
                     this.toRemove = this.nextOrNull;
                     this.nextOrNull = StandardDescendingMap.this.forward().lowerEntry(this.nextOrNull.getKey());
                  }

                  return var1;
               }
            }

            public void remove() {
               if (this.toRemove == null) {
                  throw new IllegalStateException("no calls to next() since the last call to remove()");
               } else {
                  StandardDescendingMap.this.forward().remove(this.toRemove.getKey());
                  this.toRemove = null;
               }
            }
         };
      }
   }
}
