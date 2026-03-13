package org.terraform.cave;

import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class NoiseCaveRegistry {
   private final TerraformWorld tw;
   @NotNull
   private final NoiseCaveAbstract[] noiseCaveCarvers;
   @NotNull
   private final NoiseCaveAbstract[] generateCaveCarvers;

   public NoiseCaveRegistry(TerraformWorld tw) {
      this.tw = tw;
      this.noiseCaveCarvers = new NoiseCaveAbstract[]{new CheeseCave()};
      this.generateCaveCarvers = new NoiseCaveAbstract[]{new NoiseRavine()};
   }

   public boolean canNoiseCarve(int x, int y, int z, double height, ChunkCache cache) {
      if (!TConfig.areCavesEnabled()) {
         return false;
      } else {
         float filterHeight = this.yBarrier(this.tw, x, y, z, (float)height, 10.0F, 5.0F, cache);
         float filterGround = this.yBarrier(this.tw, x, y, z, (float)TerraformGeneratorPlugin.injector.getMinY(), 20.0F, 5.0F, cache);
         float filter = filterHeight * filterGround;
         NoiseCaveAbstract[] var10 = this.noiseCaveCarvers;
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            NoiseCaveAbstract carver = var10[var12];
            if (carver.canCarve(this.tw, x, y, z, height, filter)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean canGenerateCarve(int x, int y, int z, double height, ChunkCache cache) {
      if (!TConfig.areCavesEnabled()) {
         return false;
      } else {
         float filterSea = this.yBarrier(this.tw, x, (int)height, z, (float)TerraformGenerator.seaLevel, 5.0F, 1.0F, cache);
         NoiseCaveAbstract[] var8 = this.generateCaveCarvers;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            NoiseCaveAbstract carver = var8[var10];
            if (carver.canCarve(this.tw, x, y, z, height, filterSea)) {
               return true;
            }
         }

         return false;
      }
   }

   public float yBarrier(@NotNull TerraformWorld tw, int x, int y, int z, float v, float barrier, float limit, ChunkCache cache) {
      FastNoise boundaryNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.CAVE_YBARRIER_NOISE, (world) -> {
         FastNoise n = new FastNoise((int)tw.getSeed() * 5);
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.01F);
         return n;
      });
      float boundaryNoiseVal = cache.getYBarrierNoise(x & 15, z & 15);
      if (boundaryNoiseVal == ChunkCache.CHUNKCACHE_INVAL) {
         boundaryNoiseVal = 3.0F * boundaryNoise.GetNoise((float)x, (float)z);
         cache.cacheYBarrierNoise(x & 15, z & 15, boundaryNoiseVal);
      }

      barrier += boundaryNoiseVal;
      if (Math.abs((float)y - v) <= limit) {
         return 0.0F;
      } else {
         float abs = Math.abs((float)y - v);
         return abs < barrier + limit ? (abs - limit) / barrier : 1.0F;
      }
   }
}
