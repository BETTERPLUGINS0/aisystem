package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class SparseJungleHandler extends JungleHandler {
   @NotNull
   public Biome getBiome() {
      return Biome.SPARSE_JUNGLE;
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (BlockUtils.isDirtLike(data.getType(rawX, surfaceY, rawZ)) && BlockUtils.isAir(data.getType(rawX, surfaceY + 1, rawZ)) && GenUtils.chance(2, 7)) {
         if (random.nextBoolean()) {
            GenUtils.weightedRandomSmallItem(random, PlantBuilder.GRASS, 5, BlockUtils.pickFlower(), 1).build(data, rawX, surfaceY + 1, rawZ);
         } else if (BlockUtils.isAir(data.getType(rawX, surfaceY + 2, rawZ))) {
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] bigTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 35);
      SimpleLocation[] trees;
      int x;
      int z;
      if (TConfig.c.TREES_JUNGLE_BIG_ENABLED) {
         trees = bigTrees;
         x = bigTrees.length;

         for(z = 0; z < x; ++z) {
            SimpleLocation sLoc = trees[z];
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.JUNGLE_BIG)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

      trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 15);
      SimpleLocation[] var11 = trees;
      z = trees.length;

      int y;
      for(y = 0; y < z; ++y) {
         SimpleLocation sLoc = var11[y];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            if (GenUtils.chance(random, 1000 - TConfig.c.BIOME_JUNGLE_STATUE_CHANCE, 1000)) {
               TreeDB.spawnSmallJungleTree(false, tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            } else {
               this.spawnStatue(random, data, sLoc);
            }
         }
      }

      for(x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
         for(z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
            y = GenUtils.getHighestGround(data, x, z);
            if (GenUtils.chance(1, 95)) {
               createBush(data, 0.0F, x, y, z);
            }

            if (data.getBiome(x, z) == this.getBiome() && BlockUtils.isDirtLike(data.getType(x, y, z)) && data.getType(x, y + 1, z) == Material.JUNGLE_WOOD && BlockUtils.isAir(data.getType(x, y + 2, z)) && GenUtils.chance(2, 9)) {
               PlantBuilder.build(data, x, y + 2, z, PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
            }
         }
      }

   }
}
