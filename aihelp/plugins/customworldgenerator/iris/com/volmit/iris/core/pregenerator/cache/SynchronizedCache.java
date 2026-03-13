package com.volmit.iris.core.pregenerator.cache;

record SynchronizedCache(PregenCache cache) implements PregenCache {
   SynchronizedCache(PregenCache cache) {
      this.cache = var1;
   }

   public boolean isThreadSafe() {
      return true;
   }

   public boolean isChunkCached(int x, int z) {
      synchronized(this.cache) {
         return this.cache.isChunkCached(var1, var2);
      }
   }

   public boolean isRegionCached(int x, int z) {
      synchronized(this.cache) {
         return this.cache.isRegionCached(var1, var2);
      }
   }

   public void cacheChunk(int x, int z) {
      synchronized(this.cache) {
         this.cache.cacheChunk(var1, var2);
      }
   }

   public void cacheRegion(int x, int z) {
      synchronized(this.cache) {
         this.cache.cacheRegion(var1, var2);
      }
   }

   public void write() {
      synchronized(this.cache) {
         this.cache.write();
      }
   }

   public void trim(long unloadDuration) {
      synchronized(this.cache) {
         this.cache.trim(var1);
      }
   }

   public PregenCache cache() {
      return this.cache;
   }
}
