package org.terraform.coregen;

import java.util.HashMap;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.CoordPair;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;
import org.terraform.utils.datastructs.ConcurrentLRUCache;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public enum HeightMap {
   RIVER {
      public double getHeight(TerraformWorld tw, int x, int z) {
         FastNoise noise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.HEIGHTMAP_RIVER, (world) -> {
            FastNoise n = new FastNoise((int)world.getSeed());
            n.SetNoiseType(FastNoise.NoiseType.PerlinFractal);
            n.SetFrequency(TConfig.c.HEIGHT_MAP_RIVER_FREQUENCY);
            n.SetFractalOctaves(5);
            return n;
         });
         return (double)(15.0F - 200.0F * Math.abs(noise.GetNoise((float)x, (float)z)));
      }
   },
   CORE {
      public double getHeight(TerraformWorld tw, int x, int z) {
         FastNoise noise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.HEIGHTMAP_CORE, (world) -> {
            FastNoise n = new FastNoise((int)world.getSeed());
            n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
            n.SetFractalOctaves(2);
            n.SetFrequency(TConfig.c.HEIGHT_MAP_CORE_FREQUENCY);
            return n;
         });
         double height = (double)(10.0F * noise.GetNoise((float)x, (float)z) + 7.0F + (float)TerraformGenerator.seaLevel);
         if (height > (double)(TerraformGenerator.seaLevel + 10)) {
            height = (height - (double)TerraformGenerator.seaLevel - 10.0D) * 0.1D + (double)TerraformGenerator.seaLevel + 10.0D;
         }

         return height;
      }
   },
   ATTRITION {
      public double getHeight(TerraformWorld tw, int x, int z) {
         FastNoise perlin = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.HEIGHTMAP_ATTRITION, (world) -> {
            FastNoise n = new FastNoise((int)world.getSeed() + 113);
            n.SetNoiseType(FastNoise.NoiseType.PerlinFractal);
            n.SetFractalOctaves(4);
            n.SetFrequency(0.02F);
            return n;
         });
         double height = (double)(perlin.GetNoise((float)x, (float)z) * 2.0F * 7.0F);
         return Math.max(0.0D, height);
      }
   };

   public static final int defaultSeaLevel = 62;
   public static final float heightAmplifier = TConfig.c.HEIGHT_MAP_LAND_HEIGHT_AMPLIFIER;
   public static final int MASK_RADIUS = 5;
   public static final int MASK_DIAMETER = 11;
   public static final int MASK_VOLUME = 121;
   private static final int upscaleSize = 3;
   public static int spawnFlatRadiusSquared = -324534;
   private static final ConcurrentLRUCache<BiomeSection, SectionBlurCache> BLUR_CACHE = new ConcurrentLRUCache("BLUR_CACHE", 64, (sect) -> {
      SectionBlurCache newCache = new SectionBlurCache(sect, new float[BiomeSection.sectionWidth + 11][BiomeSection.sectionWidth + 11], new float[BiomeSection.sectionWidth + 11][BiomeSection.sectionWidth + 11]);
      newCache.fillCache();
      return newCache;
   });

   public static double getNoiseGradient(TerraformWorld tw, int x, int z, int radius) {
      double totalChangeInGradient = 0.0D;
      int count = 0;
      double centerNoise = (double)getBlockHeight(tw, x, z);

      for(int nx = -radius; nx <= radius; ++nx) {
         for(int nz = -radius; nz <= radius; ++nz) {
            if (nx != 0 || nz != 0) {
               totalChangeInGradient += Math.abs((double)getBlockHeight(tw, x + nx, z + nz) - centerNoise);
               ++count;
            }
         }
      }

      return totalChangeInGradient / (double)count;
   }

   public static double getTrueHeightGradient(PopulatorDataAbstract data, int x, int z, int radius) {
      double totalChangeInGradient = 0.0D;
      int count = 0;
      double centerNoise = (double)GenUtils.getHighestGround(data, x, z);

      for(int nx = -radius; nx <= radius; ++nx) {
         for(int nz = -radius; nz <= radius; ++nz) {
            if (nx != 0 || nz != 0) {
               totalChangeInGradient += Math.abs((double)GenUtils.getHighestGround(data, x + nx, z + nz) - centerNoise);
               ++count;
            }
         }
      }

      return totalChangeInGradient / (double)count;
   }

   public static double getRawRiverDepth(TerraformWorld tw, int x, int z) {
      if (Math.pow((double)x, 2.0D) + Math.pow((double)z, 2.0D) < (double)spawnFlatRadiusSquared) {
         return 0.0D;
      } else {
         double depth = RIVER.getHeight(tw, x, z);
         return Math.max(0.0D, depth);
      }
   }

   public static double getPreciseHeight(TerraformWorld tw, int x, int z) {
      ChunkCache cache = TerraformGenerator.getCache(tw, x >> 4, z >> 4);
      double cachedValue = cache.getHeightMapHeight(x, z);
      if (cachedValue != (double)ChunkCache.CHUNKCACHE_INVAL) {
         return cachedValue;
      } else {
         double height = getRiverlessHeight(tw, x, z);
         double depth = getRawRiverDepth(tw, x, z);
         if (height - depth >= (double)(TerraformGenerator.seaLevel - 15)) {
            height -= depth;
         } else if (height > (double)(TerraformGenerator.seaLevel - 15) && height - depth < (double)(TerraformGenerator.seaLevel - 15)) {
            height = (double)(TerraformGenerator.seaLevel - 15);
         }

         if (heightAmplifier != 1.0F && height > (double)TerraformGenerator.seaLevel) {
            height += (double)heightAmplifier * (height - (double)TerraformGenerator.seaLevel);
         }

         cache.cacheHeightMap(x, z, height);
         return height;
      }
   }

   static float getDominantBiomeHeight(TerraformWorld tw, int x, int z, HashMap<CoordPair, Float> dominantBiomeHeights) {
      CoordPair key = new CoordPair(x, z);
      Float h = (Float)dominantBiomeHeights.get(key);
      if (h == null) {
         if (x % 3 != 0 && z % 3 != 0) {
            h = getDominantBiomeHeight(tw, x - x % 3, z - z % 3, dominantBiomeHeights);
         } else {
            h = (float)BiomeBank.calculateHeightIndependentBiome(tw, x, z).getHandler().calculateHeight(tw, x, z);
            if (Math.pow((double)x, 2.0D) + Math.pow((double)z, 2.0D) < (double)spawnFlatRadiusSquared) {
               h = (float)CORE.getHeight(tw, x, z);
            }
         }

         dominantBiomeHeights.put(key, h);
      }

      return h;
   }

   public static double getRiverlessHeight(TerraformWorld tw, int x, int z) {
      BiomeSection sect = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      double coreHeight = (double)((SectionBlurCache)BLUR_CACHE.get(sect)).getBlurredHeight(x, z);
      coreHeight += ATTRITION.getHeight(tw, x, z);
      return coreHeight;
   }

   public static int getBlockHeight(TerraformWorld tw, int x, int z) {
      return (int)getPreciseHeight(tw, x, z);
   }

   public abstract double getHeight(TerraformWorld var1, int var2, int var3);

   // $FF: synthetic method
   private static HeightMap[] $values() {
      return new HeightMap[]{RIVER, CORE, ATTRITION};
   }
}
