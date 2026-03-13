package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.GenUtils;

public class CherryGroveBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BEACH;
   }

   @NotNull
   public CustomBiomeType getCustomBiome() {
      return CustomBiomeType.CHERRY_GROVE;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      boolean hasSugarcane = GenUtils.chance(random, 1, 100);
      Material base = data.getType(rawX, surfaceY, rawZ);
      if (base == Material.GRASS_BLOCK && data.getType(rawX, surfaceY + 1, rawZ) == Material.WATER) {
         data.setType(rawX, surfaceY, rawZ, Material.DIRT);
      }

      if (base == Material.SAND || base == Material.GRASS_BLOCK) {
         ++surfaceY;
         if (hasSugarcane) {
            boolean hasWater = data.getType(rawX + 1, surfaceY - 1, rawZ) == Material.WATER;
            if (data.getType(rawX - 1, surfaceY - 1, rawZ) == Material.WATER) {
               hasWater = true;
            }

            if (data.getType(rawX, surfaceY - 1, rawZ + 1) == Material.WATER) {
               hasWater = true;
            }

            if (data.getType(rawX, surfaceY - 1, rawZ - 1) == Material.WATER) {
               hasWater = true;
            }

            if (hasWater) {
               PlantBuilder.SUGAR_CANE.build(random, data, rawX, surfaceY, rawZ, 3, 7);
            }
         }

      }
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
