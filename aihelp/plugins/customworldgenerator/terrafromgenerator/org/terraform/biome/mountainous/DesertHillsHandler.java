package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeSection;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class DesertHillsHandler extends AbstractMountainHandler {
   public boolean isOcean() {
      return false;
   }

   protected double getPeakMultiplier(@NotNull BiomeSection section, @NotNull Random sectionRandom) {
      return GenUtils.randDouble(sectionRandom, 1.1D, 1.3D);
   }

   @NotNull
   public Biome getBiome() {
      return Biome.DESERT;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.SAND, Material.SAND, (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND), (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND), (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND), Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE, (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND, Material.STONE), (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      FastNoise duneNoise = NoiseCacheHandler.getNoise(world, NoiseCacheHandler.NoiseCacheEntry.BIOME_DESERT_DUNENOISE, (tw) -> {
         FastNoise n = new FastNoise((int)tw.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.CubicFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.03F);
         return n;
      });

      for(int y = surfaceY; (double)y > HeightMap.CORE.getHeight(world, rawX, rawZ); --y) {
         if (duneNoise.GetNoise((float)rawX, (float)y, (float)rawZ) > 0.0F) {
            if (data.getType(rawX, y, rawZ) != Material.SAND && data.getType(rawX, y, rawZ) != Material.RED_SAND) {
               if ((data.getType(rawX, y, rawZ) == Material.SANDSTONE || data.getType(rawX, y, rawZ) == Material.RED_SANDSTONE) && TConfig.c.BIOME_DESERT_MOUNTAINS_YELLOW_CONCRETE) {
                  data.setType(rawX, y, rawZ, Material.YELLOW_CONCRETE);
               }
            } else if (TConfig.c.BIOME_DESERT_MOUNTAINS_YELLOW_CONCRETE_POWDER) {
               data.setType(rawX, y, rawZ, Material.YELLOW_CONCRETE_POWDER);
            }
         }
      }

   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
