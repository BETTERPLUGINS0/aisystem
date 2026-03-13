package org.terraform.biome.river;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class CherryGroveRiverHandler extends BiomeHandler {
   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.RIVER;
   }

   @NotNull
   public CustomBiomeType getCustomBiome() {
      return CustomBiomeType.CHERRY_GROVE;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(@NotNull TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY < TerraformGenerator.seaLevel) {
         if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
            RiverHandler.riverVegetation(world, random, data, rawX, surfaceY, rawZ);
            if (GenUtils.chance(random, TConfig.c.BIOME_CLAY_DEPOSIT_CHANCE_OUT_OF_THOUSAND, 1000)) {
               BlockUtils.generateClayDeposit(rawX, surfaceY, rawZ, data, random);
            }

         }
      }
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
