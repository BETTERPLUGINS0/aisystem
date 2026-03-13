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
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class ScarletForestHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.FOREST;
   }

   @NotNull
   public CustomBiomeType getCustomBiome() {
      return CustomBiomeType.SCARLET_FOREST;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && GenUtils.chance(random, 1, 10)) {
         if (GenUtils.chance(random, 6, 10)) {
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            if (random.nextBoolean()) {
               PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
            }
         } else if (GenUtils.chance(random, 7, 10)) {
            PlantBuilder.POPPY.build(data, rawX, surfaceY + 1, rawZ);
         } else {
            PlantBuilder.ROSE_BUSH.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 16);
      SimpleLocation[] smalltrees = trees;
      int var6 = trees.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = smalltrees[var7];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (tw.getBiomeBank(sLoc.getX(), sLoc.getZ()) == BiomeBank.SCARLET_FOREST && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            if (TConfig.c.TREES_SCARLET_BIG_ENABLED) {
               (new FractalTreeBuilder(FractalTypes.Tree.SCARLET_BIG)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            } else {
               (new FractalTreeBuilder(FractalTypes.Tree.SCARLET_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }

            BlockUtils.lambdaCircularPatch(random.nextInt(132798), 7.0F, new SimpleBlock(data, sLoc), (b) -> {
               if (BlockUtils.isDirtLike(b.getType())) {
                  b.setType(Material.PODZOL);
               }

            });
         }
      }

      smalltrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 7);
      SimpleLocation[] var11 = smalltrees;
      var7 = smalltrees.length;

      for(int var12 = 0; var12 < var7; ++var12) {
         SimpleLocation sLoc = var11[var12];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            (new FractalTreeBuilder(FractalTypes.Tree.SCARLET_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.SCARLET_FOREST_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.SCARLET_FOREST_RIVER;
   }
}
