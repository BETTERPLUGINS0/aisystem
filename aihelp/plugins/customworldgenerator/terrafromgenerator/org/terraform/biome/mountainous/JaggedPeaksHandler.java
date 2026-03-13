package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class JaggedPeaksHandler extends AbstractMountainHandler {
   private static void stoneStack(Material stoneType, @NotNull PopulatorDataAbstract data, @NotNull Random rand, int x, int y, int z) {
      data.setType(x, y, z, stoneType);
      int depth = GenUtils.randInt(rand, 3, 7);

      for(int i = 1; i < depth && BlockUtils.isStoneLike(data.getType(x, y - i, z)); ++i) {
         data.setType(x, y - i, z, stoneType);
         if (BlockUtils.isExposedToNonSolid(new SimpleBlock(data, x, y - i, z))) {
            ++depth;
         }
      }

   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SNOWY_SLOPES;
   }

   @NotNull
   public Material[] getSurfaceCrust(Random rand) {
      return new Material[]{Material.STONE};
   }

   public double calculateHeight(@NotNull TerraformWorld tw, int x, int z) {
      double height = super.calculateHeight(tw, x, z);
      FastNoise jaggedPeaksNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_JAGGED_PEAKSNOISE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(6);
         n.SetFrequency(0.03F);
         return n;
      });
      double noise = (double)jaggedPeaksNoise.GetNoise((float)x, (float)z);
      if (noise > 0.0D) {
         height += noise * 50.0D;
      }

      return height * 1.03D;
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel) {
         this.correctDirt(new SimpleBlock(data, rawX, surfaceY, rawZ));
         if (GenUtils.chance(random, 1, 25)) {
            Material stoneType = (Material)GenUtils.randChoice((Object[])(Material.ANDESITE, Material.DIORITE));
            stoneStack(stoneType, data, random, rawX, surfaceY, rawZ);

            for(int nx = -2; nx <= 2; ++nx) {
               for(int nz = -2; nz <= 2; ++nz) {
                  if (!GenUtils.chance(random, 1, 5)) {
                     int stoneY = GenUtils.getHighestGround(data, rawX + nx, rawZ + nz);
                     if (stoneY >= TerraformGenerator.seaLevel) {
                        stoneStack(stoneType, data, random, rawX + nx, stoneY, rawZ + nz);
                     }
                  }
               }
            }
         }

         double gradient = HeightMap.getTrueHeightGradient(data, rawX, rawZ, 3);
         if (gradient < 1.4D) {
            if (surfaceY < TerraformGenerator.seaLevel) {
               return;
            }

            if (gradient < 1.2D) {
               data.setType(rawX, surfaceY, rawZ, Material.POWDER_SNOW);
               data.setType(rawX, surfaceY + 1, rawZ, Material.AIR);
            } else {
               data.setType(rawX, surfaceY, rawZ, Material.SNOW_BLOCK);
            }
         }

      }
   }

   private void correctDirt(@NotNull SimpleBlock start) {
      for(int depth = 0; depth < 5; ++depth) {
         BlockFace[] var3 = BlockUtils.directBlockFaces;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockFace face = var3[var5];
            if (start.getRelative(face).getType() == Material.STONE) {
               start.setType(Material.STONE);
               break;
            }
         }

         start = start.getDown();
      }

   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ICY_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.FROZEN_RIVER;
   }
}
