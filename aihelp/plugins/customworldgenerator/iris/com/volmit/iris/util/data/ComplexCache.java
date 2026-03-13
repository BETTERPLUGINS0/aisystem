package com.volmit.iris.util.data;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.collection.KMap;

public class ComplexCache<T> {
   private final KMap<Long, ChunkCache<T>> chunks = new KMap();

   public boolean has(int x, int z) {
      return this.chunks.containsKey(Cache.key(var1, var2));
   }

   public void invalidate(int x, int z) {
      this.chunks.remove(Cache.key(var1, var2));
   }

   public ChunkCache<T> chunk(int x, int z) {
      return (ChunkCache)this.chunks.computeIfAbsent(Cache.key(var1, var2), (var0) -> {
         return new ChunkCache();
      });
   }
}
