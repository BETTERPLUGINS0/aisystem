package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.stats.CacheStats;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import org.checkerframework.checker.nullness.qual.Nullable;

interface LocalManualCache<K, V> extends Cache<K, V> {
   LocalCache<K, V> cache();

   default long estimatedSize() {
      return this.cache().estimatedSize();
   }

   default void cleanUp() {
      this.cache().cleanUp();
   }

   @Nullable
   default V getIfPresent(Object key) {
      return this.cache().getIfPresent(key, true);
   }

   @Nullable
   default V get(K key, Function<? super K, ? extends V> mappingFunction) {
      return this.cache().computeIfAbsent(key, mappingFunction);
   }

   default Map<K, V> getAllPresent(Iterable<?> keys) {
      return this.cache().getAllPresent(keys);
   }

   default Map<K, V> getAll(Iterable<? extends K> keys, Function<Iterable<? extends K>, Map<K, V>> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      Set<K> keysToLoad = new LinkedHashSet();
      Map<K, V> found = this.cache().getAllPresent(keys);
      Map<K, V> result = new LinkedHashMap(found.size());

      Object key;
      Object value;
      for(Iterator var6 = keys.iterator(); var6.hasNext(); result.put(key, value)) {
         key = var6.next();
         value = found.get(key);
         if (value == null) {
            keysToLoad.add(key);
         }
      }

      if (keysToLoad.isEmpty()) {
         return found;
      } else {
         this.bulkLoad(keysToLoad, result, mappingFunction);
         return Collections.unmodifiableMap(result);
      }
   }

   default void bulkLoad(Set<K> keysToLoad, Map<K, V> result, Function<Iterable<? extends K>, Map<K, V>> mappingFunction) {
      boolean success = false;
      long startTime = this.cache().statsTicker().read();
      boolean var17 = false;

      try {
         var17 = true;
         Map<K, V> loaded = (Map)mappingFunction.apply(keysToLoad);
         loaded.forEach((keyx, valuex) -> {
            this.cache().put(keyx, valuex, false);
         });
         Iterator var8 = keysToLoad.iterator();

         while(true) {
            if (!var8.hasNext()) {
               success = !loaded.isEmpty();
               var17 = false;
               break;
            }

            K key = var8.next();
            V value = loaded.get(key);
            if (value == null) {
               result.remove(key);
            } else {
               result.put(key, value);
            }
         }
      } catch (RuntimeException var18) {
         throw var18;
      } catch (Exception var19) {
         throw new CompletionException(var19);
      } finally {
         if (var17) {
            long loadTime = this.cache().statsTicker().read() - startTime;
            if (success) {
               this.cache().statsCounter().recordLoadSuccess(loadTime);
            } else {
               this.cache().statsCounter().recordLoadFailure(loadTime);
            }

         }
      }

      long loadTime = this.cache().statsTicker().read() - startTime;
      if (success) {
         this.cache().statsCounter().recordLoadSuccess(loadTime);
      } else {
         this.cache().statsCounter().recordLoadFailure(loadTime);
      }

   }

   default void put(K key, V value) {
      this.cache().put(key, value);
   }

   default void putAll(Map<? extends K, ? extends V> map) {
      this.cache().putAll(map);
   }

   default void invalidate(Object key) {
      this.cache().remove(key);
   }

   default void invalidateAll(Iterable<?> keys) {
      this.cache().invalidateAll(keys);
   }

   default void invalidateAll() {
      this.cache().clear();
   }

   default CacheStats stats() {
      return this.cache().statsCounter().snapshot();
   }

   default ConcurrentMap<K, V> asMap() {
      return this.cache();
   }
}
