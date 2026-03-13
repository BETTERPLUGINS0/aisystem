package com.volmit.iris.engine.object;

import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.engine.framework.Engine;
import com.volmit.iris.util.collection.KMap;
import lombok.Generated;

public class IrisEngineData extends IrisSpawnerCooldowns {
   private IrisEngineStatistics statistics = new IrisEngineStatistics();
   private KMap<Long, IrisSpawnerCooldowns> chunks = new KMap();
   private Long seed = null;

   public void removeChunk(int x, int z) {
      this.chunks.remove(Cache.key(var1, var2));
   }

   public IrisSpawnerCooldowns getChunk(int x, int z) {
      return (IrisSpawnerCooldowns)this.chunks.computeIfAbsent(Cache.key(var1, var2), (var0) -> {
         return new IrisSpawnerCooldowns();
      });
   }

   public void cleanup(Engine engine) {
      super.cleanup(var1);
      this.chunks.values().removeIf((var1x) -> {
         var1x.cleanup(var1);
         return var1x.isEmpty();
      });
   }

   @Generated
   public IrisEngineStatistics getStatistics() {
      return this.statistics;
   }

   @Generated
   public KMap<Long, IrisSpawnerCooldowns> getChunks() {
      return this.chunks;
   }

   @Generated
   public Long getSeed() {
      return this.seed;
   }

   @Generated
   public void setStatistics(final IrisEngineStatistics statistics) {
      this.statistics = var1;
   }

   @Generated
   public void setChunks(final KMap<Long, IrisSpawnerCooldowns> chunks) {
      this.chunks = var1;
   }

   @Generated
   public void setSeed(final Long seed) {
      this.seed = var1;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getStatistics());
      return "IrisEngineData(statistics=" + var10000 + ", chunks=" + String.valueOf(this.getChunks()) + ", seed=" + this.getSeed() + ")";
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisEngineData)) {
         return false;
      } else {
         IrisEngineData var2 = (IrisEngineData)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            label49: {
               Long var3 = this.getSeed();
               Long var4 = var2.getSeed();
               if (var3 == null) {
                  if (var4 == null) {
                     break label49;
                  }
               } else if (var3.equals(var4)) {
                  break label49;
               }

               return false;
            }

            IrisEngineStatistics var5 = this.getStatistics();
            IrisEngineStatistics var6 = var2.getStatistics();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            KMap var7 = this.getChunks();
            KMap var8 = var2.getChunks();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisEngineData;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      int var2 = super.hashCode();
      Long var3 = this.getSeed();
      var2 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisEngineStatistics var4 = this.getStatistics();
      var2 = var2 * 59 + (var4 == null ? 43 : var4.hashCode());
      KMap var5 = this.getChunks();
      var2 = var2 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var2;
   }
}
