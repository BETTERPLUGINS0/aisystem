package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public class CherryGroveHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.PLAINS;
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
      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && GenUtils.chance(random, 2, 10)) {
         if (GenUtils.chance(random, 8, 10)) {
            if (Version.VERSION.isAtLeast(Version.v1_20) && TConfig.arePlantsEnabled() && GenUtils.chance(random, 6, 10)) {
               data.setBlockData(rawX, surfaceY + 1, rawZ, V_1_20.getPinkPetalData(GenUtils.randInt(1, 4)));
            } else {
               PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            }
         } else if (GenUtils.chance(random, 7, 10)) {
            PlantBuilder.ALLIUM.build(data, rawX, surfaceY + 1, rawZ);
         } else {
            PlantBuilder.PEONY.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 20);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (tw.getBiomeBank(sLoc.getX(), sLoc.getZ()) == BiomeBank.CHERRY_GROVE && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            int rX;
            switch(random.nextInt(20)) {
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
               (new SphereBuilder(random, new SimpleBlock(data, sLoc), new Material[]{Material.COBBLESTONE, Material.STONE, Material.STONE, Material.STONE, Material.MOSSY_COBBLESTONE})).setRadius((float)GenUtils.randInt(random, 3, 5)).setRY((float)GenUtils.randInt(random, 6, 10)).build();
               continue;
            default:
               if (random.nextBoolean()) {
                  (new FractalTreeBuilder(FractalTypes.Tree.CHERRY_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
               } else {
                  (new FractalTreeBuilder(FractalTypes.Tree.CHERRY_THICK)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
               }

               if (Version.VERSION.isAtLeast(Version.v1_20)) {
                  continue;
               }

               rX = sLoc.getX() - 6;
            }

            while(rX <= sLoc.getX() + 6) {
               for(int rZ = sLoc.getZ() - 6; rZ <= sLoc.getZ() + 6; ++rZ) {
                  Wall ceil = (new Wall(new SimpleBlock(data, rX, sLoc.getY(), rZ))).findCeiling(15);
                  if (ceil != null && GenUtils.chance(random, 1, 30) && ceil.getType() == Material.DARK_OAK_LEAVES) {
                     PlantBuilder.SPORE_BLOSSOM.build(ceil.getDown());
                  }
               }

               ++rX;
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.CHERRY_GROVE_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.CHERRY_GROVE_RIVER;
   }
}
