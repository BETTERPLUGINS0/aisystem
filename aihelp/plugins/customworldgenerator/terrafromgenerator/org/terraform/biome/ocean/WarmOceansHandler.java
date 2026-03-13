package org.terraform.biome.ocean;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class WarmOceansHandler extends AbstractOceanHandler {
   public WarmOceansHandler(BiomeType oceanType) {
      super(oceanType);
   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.WARM_OCEAN;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRAVEL, Material.GRAVEL, (Material)GenUtils.randChoice(rand, Material.STONE, Material.GRAVEL, Material.STONE), (Material)GenUtils.randChoice(rand, Material.STONE), (Material)GenUtils.randChoice(rand, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel - 2) {
         data.setType(rawX, surfaceY, rawZ, Material.SAND);
      } else if (surfaceY >= TerraformGenerator.seaLevel - 4 && random.nextBoolean()) {
         data.setType(rawX, surfaceY, rawZ, Material.SAND);
      }

      if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
         if (GenUtils.chance(random, 10, 100)) {
            data.setType(rawX, surfaceY + 1, rawZ, Material.SEAGRASS);
            if (random.nextBoolean() && surfaceY < TerraformGenerator.seaLevel - 3) {
               BlockUtils.setDoublePlant(data, rawX, surfaceY + 1, rawZ, Material.TALL_SEAGRASS);
            }
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] rocks = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 25, 0.4F);
      SimpleLocation[] var5 = rocks;
      int var6 = rocks.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int rockY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(rockY);
            if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRAVEL) {
               BlockUtils.replaceSphere(random.nextInt(9987), (float)GenUtils.randDouble(random, 3.0D, 7.0D), (float)GenUtils.randDouble(random, 2.0D, 4.0D), (float)GenUtils.randDouble(random, 3.0D, 7.0D), new SimpleBlock(data, sLoc), true, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.GRANITE, Material.ANDESITE, Material.DIORITE)));
            }
         }
      }

   }
}
