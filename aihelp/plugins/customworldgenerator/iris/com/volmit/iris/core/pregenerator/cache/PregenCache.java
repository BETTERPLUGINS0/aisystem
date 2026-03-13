package com.volmit.iris.core.pregenerator.cache;

import com.volmit.iris.util.documentation.ChunkCoordinates;
import com.volmit.iris.util.documentation.RegionCoordinates;
import java.io.File;

public interface PregenCache {
   PregenCache EMPTY = new PregenCache() {
      public boolean isThreadSafe() {
         return true;
      }

      public boolean isChunkCached(int x, int z) {
         return false;
      }

      public boolean isRegionCached(int x, int z) {
         return false;
      }

      public void cacheChunk(int x, int z) {
      }

      public void cacheRegion(int x, int z) {
      }

      public void write() {
      }

      public void trim(long unloadDuration) {
      }
   };

   default boolean isThreadSafe() {
      return false;
   }

   @ChunkCoordinates
   boolean isChunkCached(int x, int z);

   @RegionCoordinates
   boolean isRegionCached(int x, int z);

   @ChunkCoordinates
   void cacheChunk(int x, int z);

   @RegionCoordinates
   void cacheRegion(int x, int z);

   void write();

   void trim(long unloadDuration);

   static PregenCache create(File directory) {
      return (PregenCache)(directory == null ? EMPTY : new PregenCacheImpl(directory, 16));
   }

   default PregenCache sync() {
      return (PregenCache)(this.isThreadSafe() ? this : new SynchronizedCache(this));
   }
}
