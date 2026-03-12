package fr.xephi.authme.libs.com.google.common.cache;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMap;
import fr.xephi.authme.libs.com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class AbstractCache<K, V> implements Cache<K, V> {
   protected AbstractCache() {
   }

   public V get(K key, Callable<? extends V> valueLoader) throws ExecutionException {
      throw new UnsupportedOperationException();
   }

   public ImmutableMap<K, V> getAllPresent(Iterable<? extends Object> keys) {
      Map<K, V> result = Maps.newLinkedHashMap();
      Iterator var3 = keys.iterator();

      while(var3.hasNext()) {
         Object key = var3.next();
         if (!result.containsKey(key)) {
            V value = this.getIfPresent(key);
            if (value != null) {
               result.put(key, value);
            }
         }
      }

      return ImmutableMap.copyOf((Map)result);
   }

   public void put(K key, V value) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map<? extends K, ? extends V> m) {
      Iterator var2 = m.entrySet().iterator();

      while(var2.hasNext()) {
         Entry<? extends K, ? extends V> entry = (Entry)var2.next();
         this.put(entry.getKey(), entry.getValue());
      }

   }

   public void cleanUp() {
   }

   public long size() {
      throw new UnsupportedOperationException();
   }

   public void invalidate(Object key) {
      throw new UnsupportedOperationException();
   }

   public void invalidateAll(Iterable<? extends Object> keys) {
      Iterator var2 = keys.iterator();

      while(var2.hasNext()) {
         Object key = var2.next();
         this.invalidate(key);
      }

   }

   public void invalidateAll() {
      throw new UnsupportedOperationException();
   }

   public CacheStats stats() {
      throw new UnsupportedOperationException();
   }

   public ConcurrentMap<K, V> asMap() {
      throw new UnsupportedOperationException();
   }

   public static final class SimpleStatsCounter implements AbstractCache.StatsCounter {
      private final LongAddable hitCount = LongAddables.create();
      private final LongAddable missCount = LongAddables.create();
      private final LongAddable loadSuccessCount = LongAddables.create();
      private final LongAddable loadExceptionCount = LongAddables.create();
      private final LongAddable totalLoadTime = LongAddables.create();
      private final LongAddable evictionCount = LongAddables.create();

      public void recordHits(int count) {
         this.hitCount.add((long)count);
      }

      public void recordMisses(int count) {
         this.missCount.add((long)count);
      }

      public void recordLoadSuccess(long loadTime) {
         this.loadSuccessCount.increment();
         this.totalLoadTime.add(loadTime);
      }

      public void recordLoadException(long loadTime) {
         this.loadExceptionCount.increment();
         this.totalLoadTime.add(loadTime);
      }

      public void recordEviction() {
         this.evictionCount.increment();
      }

      public CacheStats snapshot() {
         return new CacheStats(negativeToMaxValue(this.hitCount.sum()), negativeToMaxValue(this.missCount.sum()), negativeToMaxValue(this.loadSuccessCount.sum()), negativeToMaxValue(this.loadExceptionCount.sum()), negativeToMaxValue(this.totalLoadTime.sum()), negativeToMaxValue(this.evictionCount.sum()));
      }

      private static long negativeToMaxValue(long value) {
         return value >= 0L ? value : Long.MAX_VALUE;
      }

      public void incrementBy(AbstractCache.StatsCounter other) {
         CacheStats otherStats = other.snapshot();
         this.hitCount.add(otherStats.hitCount());
         this.missCount.add(otherStats.missCount());
         this.loadSuccessCount.add(otherStats.loadSuccessCount());
         this.loadExceptionCount.add(otherStats.loadExceptionCount());
         this.totalLoadTime.add(otherStats.totalLoadTime());
         this.evictionCount.add(otherStats.evictionCount());
      }
   }

   public interface StatsCounter {
      void recordHits(int var1);

      void recordMisses(int var1);

      void recordLoadSuccess(long var1);

      void recordLoadException(long var1);

      void recordEviction();

      CacheStats snapshot();
   }
}
