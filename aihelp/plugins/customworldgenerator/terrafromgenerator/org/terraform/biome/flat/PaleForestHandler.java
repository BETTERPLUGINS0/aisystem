package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.NewFractalTreeBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_21_4;

public class PaleForestHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return V_1_21_4.PALE_GARDEN;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && GenUtils.chance(random, 1, 10)) {
         if (data.getType(rawX, surfaceY + 1, rawZ) != Material.AIR) {
            return;
         }

         PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
         if (random.nextInt(3) != 0) {
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
         } else if (random.nextInt(2) != 0) {
            PlantBuilder.CLOSED_EYEBLOSSOM.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public NewFractalTreeBuilder paleTreeMutator(NewFractalTreeBuilder b) {
      b.getFractalLeaves().setMaterial(V_1_21_4.PALE_OAK_LEAVES);
      b.getFractalLeaves().setPaleMossVines(0.2F, 3);
      b.setBranchMaterial(V_1_21_4.PALE_OAK_LOG);
      b.setRootMaterial(V_1_21_4.PALE_OAK_WOOD);
      return b;
   }

   private void placeHeart(PopulatorDataAbstract data, int x, int y, int z) {
      data.setBlockData(x, y, z, V_1_21_4.CREAKING_HEART);
      data.setType(x + 1, y, z, V_1_21_4.PALE_OAK_LOG);
      data.setType(x - 1, y, z, V_1_21_4.PALE_OAK_LOG);
      data.setType(x, y, z + 1, V_1_21_4.PALE_OAK_LOG);
      data.setType(x, y, z - 1, V_1_21_4.PALE_OAK_LOG);
      data.setType(x, y - 1, z, V_1_21_4.PALE_OAK_LOG);
      data.setType(x, y + 1, z, V_1_21_4.PALE_OAK_LOG);
      TerraformGeneratorPlugin.logger.info("Spawning Creaking Heart at " + x + "," + y + "," + z);
   }

   private void treeMossCircle(SimpleBlock core) {
      Random random = core.getPopData().getTerraformWorld().getHashedRand((long)core.getX(), core.getZ(), 1);
      core.setType(V_1_21_4.PALE_MOSS);
      if (random.nextInt(4) == 0) {
         core.getUp().lsetType(V_1_21_4.PALE_MOSS_CARPET);
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] bigTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 20);
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 10);
      SimpleLocation[] var6 = bigTrees;
      int var7 = bigTrees.length;

      int var8;
      SimpleLocation sLoc;
      int treeY;
      for(var8 = 0; var8 < var7; ++var8) {
         sLoc = var6[var8];
         treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ())) && TConfig.c.TREES_PALE_FOREST_BIG_ENABLED) {
            BlockUtils.lambdaCircularPatch(tw.getHashedRand((long)sLoc.getX(), sLoc.getY(), sLoc.getZ()).nextInt(9999), 7.0F, new SimpleBlock(data, sLoc), this::treeMossCircle);
            if (FractalTypes.Tree.DARK_OAK_BIG_TOP.build(tw, new SimpleBlock(data, sLoc), this::paleTreeMutator) && random.nextDouble() <= TConfig.c.BIOME_PALE_FOREST_CREAKING_CHANCE) {
               this.placeHeart(data, sLoc.getX(), treeY + GenUtils.randInt(random, 2, 5), sLoc.getZ());
            }
         }
      }

      var6 = trees;
      var7 = trees.length;

      for(var8 = 0; var8 < var7; ++var8) {
         sLoc = var6[var8];
         treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            BlockUtils.lambdaCircularPatch(tw.getHashedRand((long)sLoc.getX(), sLoc.getY(), sLoc.getZ()).nextInt(9999), 4.0F, new SimpleBlock(data, sLoc), this::treeMossCircle);
            if (FractalTypes.Tree.DARK_OAK_SMALL.build(tw, new SimpleBlock(data, sLoc), this::paleTreeMutator) && random.nextDouble() <= TConfig.c.BIOME_PALE_FOREST_CREAKING_CHANCE) {
               this.placeHeart(data, sLoc.getX(), treeY + GenUtils.randInt(random, 1, 3), sLoc.getZ());
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.DARK_FOREST_BEACH;
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.DARK_FOREST_RIVER;
   }
}
