package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.common.collect.UnmodifiableIterator;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
class MapIteratorCache<K, V> {
   private final Map<K, V> backingMap;
   @CheckForNull
   private transient volatile Entry<K, V> cacheEntry;

   MapIteratorCache(Map<K, V> backingMap) {
      this.backingMap = (Map)Preconditions.checkNotNull(backingMap);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   final V put(K key, V value) {
      Preconditions.checkNotNull(key);
      Preconditions.checkNotNull(value);
      this.clearCache();
      return this.backingMap.put(key, value);
   }

   @CheckForNull
   @CanIgnoreReturnValue
   final V remove(Object key) {
      Preconditions.checkNotNull(key);
      this.clearCache();
      return this.backingMap.remove(key);
   }

   final void clear() {
      this.clearCache();
      this.backingMap.clear();
   }

   @CheckForNull
   V get(Object key) {
      Preconditions.checkNotNull(key);
      V value = this.getIfCached(key);
      return value == null ? this.getWithoutCaching(key) : value;
   }

   @CheckForNull
   final V getWithoutCaching(Object key) {
      Preconditions.checkNotNull(key);
      return this.backingMap.get(key);
   }

   final boolean containsKey(@CheckForNull Object key) {
      return this.getIfCached(key) != null || this.backingMap.containsKey(key);
   }

   final Set<K> unmodifiableKeySet() {
      return new AbstractSet<K>() {
         public UnmodifiableIterator<K> iterator() {
            final Iterator<Entry<K, V>> entryIterator = MapIteratorCache.this.backingMap.entrySet().iterator();
            return new UnmodifiableIterator<K>() {
               public boolean hasNext() {
                  return entryIterator.hasNext();
               }

               public K next() {
                  Entry<K, V> entry = (Entry)entryIterator.next();
                  MapIteratorCache.this.cacheEntry = entry;
                  return entry.getKey();
               }
            };
         }

         public int size() {
            return MapIteratorCache.this.backingMap.size();
         }

         public boolean contains(@CheckForNull Object key) {
            return MapIteratorCache.this.containsKey(key);
         }
      };
   }

   @CheckForNull
   V getIfCached(@CheckForNull Object key) {
      Entry<K, V> entry = this.cacheEntry;
      return entry != null && entry.getKey() == key ? entry.getValue() : null;
   }

   void clearCache() {
      this.cacheEntry = null;
   }
}
