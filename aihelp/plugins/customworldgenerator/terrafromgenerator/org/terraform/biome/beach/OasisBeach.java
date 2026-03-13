package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.TreeDB;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class OasisBeach {
   public static final double oasisThreshold;
   private static final float oasisFrequency;

   public static float getOasisNoise(TerraformWorld world, int x, int z) {
      FastNoise lushRiversNoise = NoiseCacheHandler.getNoise(world, NoiseCacheHandler.NoiseCacheEntry.BIOME_DESERT_LUSH_RIVER, (w) -> {
         FastNoise n = new FastNoise((int)((double)w.getSeed() * 0.4D));
         n.SetNoiseType(FastNoise.NoiseType.Cubic);
         n.SetFrequency(oasisFrequency);
         return n;
      });
      return lushRiversNoise.GetNoise((float)x, (float)z);
   }

   private static boolean isOasisBeach(TerraformWorld tw, int x, int z, BiomeBank targetBiome) {
      double lushRiverNoiseValue = (double)getOasisNoise(tw, x, z);
      double riverDepth = HeightMap.getRawRiverDepth(tw, x, z);
      BiomeBank biome = BiomeBank.calculateHeightIndependentBiome(tw, x, z);
      return lushRiverNoiseValue > oasisThreshold && riverDepth > 0.0D && biome == targetBiome;
   }

   public static void generateOasisBeach(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int z, BiomeBank targetBiome) {
      if (isOasisBeach(tw, x, z, targetBiome)) {
         int y = GenUtils.getHighestGround(data, x, z);
         int aboveSea = y - TerraformGenerator.seaLevel;
         boolean isGrass = false;
         if (1.0D - (double)aboveSea / 8.0D > random.nextDouble() && y >= TerraformGenerator.seaLevel) {
            data.setType(x, y, z, Material.GRASS_BLOCK);
            isGrass = true;
         }

         if (y == TerraformGenerator.seaLevel && random.nextInt(3) == 0) {
            PlantBuilder.SUGAR_CANE.build(random, data, x, y + 1, z, 1, 4);
         } else if (y >= TerraformGenerator.seaLevel) {
            if (random.nextInt(8) == 0) {
               TreeDB.spawnCoconutTree(tw, data, x, y, z);
            } else if (random.nextInt(5) == 0) {
               createBush(random, data, x, y, z, GenUtils.randDouble(random, 1.7D, 3.0D), GenUtils.randDouble(random, 2.0D, 2.8D), GenUtils.randDouble(random, 1.7D, 3.0D), Material.JUNGLE_LEAVES, Material.JUNGLE_LOG, 0.7D);
            } else if (isGrass) {
               PlantBuilder.build(random, data, x, y + 1, z, PlantBuilder.GRASS, PlantBuilder.GRASS, PlantBuilder.GRASS, PlantBuilder.FERN);
            }
         }

      }
   }

   public static void createBush(@NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z, double xRadius, double yRadius, double zRadius, Material leaves, Material stem, double density) {
      if (TConfig.arePlantsEnabled()) {
         int xr;
         for(xr = y; (double)xr < (double)y + yRadius / 2.0D; ++xr) {
            data.setType(x, xr, z, stem);
         }

         for(xr = (int)(-Math.ceil(xRadius)); (double)xr < Math.ceil(xRadius); ++xr) {
            for(int yr = (int)(-Math.ceil(yRadius)); (double)yr < Math.ceil(yRadius); ++yr) {
               for(int zr = (int)(-Math.ceil(zRadius)); (double)zr < Math.ceil(zRadius); ++zr) {
                  double distToCenter = Math.sqrt((double)(xr * xr) / (xRadius * xRadius) + (double)(yr * yr) / (yRadius * yRadius) + (double)(zr * zr) / (zRadius * zRadius));
                  if (distToCenter < 1.0D && random.nextDouble() < 1.0D - distToCenter + density) {
                     data.lsetType(x + xr, y + yr, z + zr, leaves);
                  }
               }
            }
         }

      }
   }

   static {
      oasisThreshold = (2.0D - TConfig.c.BIOME_OASIS_COMMONNESS) * 0.31D;
      oasisFrequency = TConfig.c.BIOME_OASIS_FREQUENCY;
   }
}
