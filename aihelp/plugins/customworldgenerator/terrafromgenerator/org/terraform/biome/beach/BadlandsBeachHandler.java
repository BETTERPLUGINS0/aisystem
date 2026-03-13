package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.GenUtils;

public class BadlandsBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BEACH;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.RED_SAND, Material.RED_SAND, (Material)GenUtils.randChoice(rand, Material.RED_SAND, Material.RED_SANDSTONE), (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, Random random, int rawX, int surfaceY, int rawZ, PopulatorDataAbstract data) {
   }

   public BiomeHandler getTransformHandler() {
      return BiomeBank.BADLANDS.getHandler();
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
