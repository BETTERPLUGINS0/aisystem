package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.GenUtils;

public class RockBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.STONY_SHORE;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{GenUtils.weightedRandomMaterial(rand, Material.STONE, 5, Material.GRAVEL, 35, Material.COBBLESTONE, 10), GenUtils.weightedRandomMaterial(rand, Material.STONE, 5, Material.GRAVEL, 35, Material.COBBLESTONE, 10), (Material)GenUtils.randChoice(rand, Material.STONE, Material.COBBLESTONE, Material.GRAVEL), (Material)GenUtils.randChoice(rand, Material.STONE, Material.COBBLESTONE, Material.GRAVEL)};
   }

   public void populateSmallItems(TerraformWorld world, Random random, int rawX, int surfaceY, int rawZ, PopulatorDataAbstract data) {
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
