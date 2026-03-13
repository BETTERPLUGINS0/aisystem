package com.volmit.iris.core.pregenerator.methods;

import com.volmit.iris.Iris;
import com.volmit.iris.core.pregenerator.PregenListener;
import com.volmit.iris.core.pregenerator.PregeneratorMethod;
import com.volmit.iris.core.pregenerator.cache.PregenCache;
import com.volmit.iris.core.service.GlobalCacheSVC;
import com.volmit.iris.util.mantle.Mantle;
import lombok.Generated;

public class CachedPregenMethod implements PregeneratorMethod {
   private final PregeneratorMethod method;
   private final PregenCache cache;

   public CachedPregenMethod(PregeneratorMethod method, String worldName) {
      this.method = var1;
      PregenCache var3 = ((GlobalCacheSVC)Iris.service(GlobalCacheSVC.class)).get(var2);
      if (var3 == null) {
         Iris.debug("Could not find existing cache for " + var2 + " creating fallback");
         var3 = GlobalCacheSVC.createDefault(var2);
      }

      this.cache = var3;
   }

   public void init() {
      this.method.init();
   }

   public void close() {
      this.method.close();
      this.cache.write();
   }

   public void save() {
      this.method.save();
      this.cache.write();
   }

   public boolean supportsRegions(int x, int z, PregenListener listener) {
      return this.cache.isRegionCached(var1, var2) || this.method.supportsRegions(var1, var2, var3);
   }

   public String getMethod(int x, int z) {
      return this.method.getMethod(var1, var2);
   }

   public void generateRegion(int x, int z, PregenListener listener) {
      if (!this.cache.isRegionCached(var1, var2)) {
         this.method.generateRegion(var1, var2, var3);
         this.cache.cacheRegion(var1, var2);
      } else {
         var3.onRegionGenerated(var1, var2);
         int var4 = var1 << 5;
         int var5 = var2 << 5;

         for(int var6 = 0; var6 < 32; ++var6) {
            for(int var7 = 0; var7 < 32; ++var7) {
               var3.onChunkGenerated(var4 + var6, var5 + var7, true);
               var3.onChunkCleaned(var4 + var6, var5 + var7);
            }
         }

      }
   }

   public void generateChunk(int x, int z, PregenListener listener) {
      if (this.cache.isChunkCached(var1, var2)) {
         var3.onChunkGenerated(var1, var2, true);
         var3.onChunkCleaned(var1, var2);
      } else {
         this.method.generateChunk(var1, var2, var3);
         this.cache.cacheChunk(var1, var2);
      }
   }

   public Mantle getMantle() {
      return this.method.getMantle();
   }

   @Generated
   public CachedPregenMethod(final PregeneratorMethod method, final PregenCache cache) {
      this.method = var1;
      this.cache = var2;
   }
}
