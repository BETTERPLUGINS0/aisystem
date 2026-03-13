package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.GenUtils;

public class IcyBeachHandler extends BiomeHandler {
   private static void makeIceSheet(int x, int y, int z, @NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int length = GenUtils.randInt(6, 16);
      int nx = x;

      for(int nz = z; length > 0; y = GenUtils.getTransformedHeight(data.getTerraformWorld(), nx, nz)) {
         --length;
         if (data.getType(nx, y, nz).isSolid() && data.getType(nx, y + 1, nz) == Material.AIR) {
            data.setType(nx, y, nz, Material.ICE);
         }

         switch(random.nextInt(5)) {
         case 0:
            ++nx;
         case 1:
         default:
            break;
         case 2:
            ++nz;
            break;
         case 3:
            --nx;
            break;
         case 4:
            --nz;
         }
      }

   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SNOWY_BEACH;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{GenUtils.weightedRandomMaterial(rand, Material.STONE, 35, Material.GRAVEL, 5, Material.COBBLESTONE, 10), GenUtils.weightedRandomMaterial(rand, Material.STONE, 35, Material.GRAVEL, 5, Material.COBBLESTONE, 10), (Material)GenUtils.randChoice(rand, Material.STONE, Material.COBBLESTONE, Material.GRAVEL), (Material)GenUtils.randChoice(rand, Material.STONE, Material.COBBLESTONE, Material.GRAVEL)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (GenUtils.chance(random, 7, 100)) {
         makeIceSheet(rawX, surfaceY, rawZ, data, random);
      }
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
