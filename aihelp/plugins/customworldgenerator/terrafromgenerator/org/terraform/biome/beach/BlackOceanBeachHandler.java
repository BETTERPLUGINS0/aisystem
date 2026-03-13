package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CoralGenerator;
import org.terraform.utils.GenUtils;

public class BlackOceanBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.STONY_SHORE;
   }

   @NotNull
   public Material[] getSurfaceCrust(Random rand) {
      return new Material[]{Material.STONE};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel - 2) {
         data.setType(rawX, surfaceY, rawZ, Material.GRAVEL);
      } else if (surfaceY >= TerraformGenerator.seaLevel - 4 && random.nextBoolean()) {
         data.setType(rawX, surfaceY, rawZ, Material.GRAVEL);
      }

      if (surfaceY <= TerraformGenerator.seaLevel) {
         if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
            if (GenUtils.chance(random, 1, 80)) {
               CoralGenerator.generateKelpGrowth(data, rawX, surfaceY + 1, rawZ);
            }

         }
      }
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
