package com.volmit.iris.util.cache;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.function.Function2;

public class WorldCache2D<T> {
   private final ConcurrentLinkedHashMap<Long, ChunkCache2D<T>> chunks;
   private final Function2<Integer, Integer, T> resolver;

   public WorldCache2D(Function2<Integer, Integer, T> resolver, int size) {
      this.resolver = var1;
      this.chunks = (new Builder()).initialCapacity(var2).maximumWeightedCapacity((long)var2).concurrencyLevel(Math.max(32, Runtime.getRuntime().availableProcessors() * 4)).build();
   }

   public T get(int x, int z) {
      ChunkCache2D var3 = (ChunkCache2D)this.chunks.computeIfAbsent(Cache.key(var1 >> 4, var2 >> 4), (var0) -> {
         return new ChunkCache2D();
      });
      return var3.get(var1, var2, this.resolver);
   }

   public long getSize() {
      return (long)this.chunks.size() * 256L;
   }

   public long getMaxSize() {
      return this.chunks.capacity() * 256L;
   }
}
