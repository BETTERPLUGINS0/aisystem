package fr.xephi.authme.libs.com.google.common.graph;

import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.util.Map;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
final class MapRetrievalCache<K, V> extends MapIteratorCache<K, V> {
   @CheckForNull
   private transient volatile MapRetrievalCache.CacheEntry<K, V> cacheEntry1;
   @CheckForNull
   private transient volatile MapRetrievalCache.CacheEntry<K, V> cacheEntry2;

   MapRetrievalCache(Map<K, V> backingMap) {
      super(backingMap);
   }

   @CheckForNull
   V get(Object key) {
      Preconditions.checkNotNull(key);
      V value = this.getIfCached(key);
      if (value != null) {
         return value;
      } else {
         value = this.getWithoutCaching(key);
         if (value != null) {
            this.addToCache(key, value);
         }

         return value;
      }
   }

   @CheckForNull
   V getIfCached(@CheckForNull Object key) {
      V value = super.getIfCached(key);
      if (value != null) {
         return value;
      } else {
         MapRetrievalCache.CacheEntry<K, V> entry = this.cacheEntry1;
         if (entry != null && entry.key == key) {
            return entry.value;
         } else {
            entry = this.cacheEntry2;
            if (entry != null && entry.key == key) {
               this.addToCache(entry);
               return entry.value;
            } else {
               return null;
            }
         }
      }
   }

   void clearCache() {
      super.clearCache();
      this.cacheEntry1 = null;
      this.cacheEntry2 = null;
   }

   private void addToCache(K key, V value) {
      this.addToCache(new MapRetrievalCache.CacheEntry(key, value));
   }

   private void addToCache(MapRetrievalCache.CacheEntry<K, V> entry) {
      this.cacheEntry2 = this.cacheEntry1;
      this.cacheEntry1 = entry;
   }

   private static final class CacheEntry<K, V> {
      final K key;
      final V value;

      CacheEntry(K key, V value) {
         this.key = key;
         this.value = value;
      }
   }
}
