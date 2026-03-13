package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.GenUtils;

public class DarkForestBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BEACH;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.COARSE_DIRT, Material.COARSE_DIRT, (Material)GenUtils.randChoice(rand, Material.STONE, Material.COARSE_DIRT, Material.COARSE_DIRT, Material.COARSE_DIRT, Material.COARSE_DIRT, Material.COARSE_DIRT), Material.STONE, Material.STONE};
   }

   public void populateSmallItems(TerraformWorld world, Random random, int rawX, int surfaceY, int rawZ, PopulatorDataAbstract data) {
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
