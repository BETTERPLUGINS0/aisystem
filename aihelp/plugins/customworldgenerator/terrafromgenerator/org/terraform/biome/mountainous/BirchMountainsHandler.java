package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
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
import org.terraform.utils.version.V_1_21_5;

public class BirchMountainsHandler extends AbstractMountainHandler {
   protected double getPeakMultiplier(@NotNull BiomeSection section, @NotNull Random sectionRandom) {
      return GenUtils.randDouble(sectionRandom, 1.1D, 1.3D);
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.JAGGED_PEAKS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      this.setRock((new SimpleBlock(data, rawX, 0, rawZ)).getGround());
      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && GenUtils.chance(random, 1, 10)) {
         if (data.getType(rawX, surfaceY + 1, rawZ) != Material.AIR) {
            return;
         }

         switch(random.nextInt(5)) {
         case 0:
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 1:
            PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 2:
            BlockUtils.pickFlower().build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 3:
            PlantBuilder.BUSH.build(data, rawX, surfaceY + 1, rawZ);
            break;
         case 4:
            V_1_21_5.wildflowers(random, data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   private void setRock(@NotNull SimpleBlock target) {
      if (HeightMap.getTrueHeightGradient(target.getPopData(), target.getX(), target.getZ(), 3) > TConfig.c.MISC_TREES_GRADIENT_LIMIT) {
         Material rock = Material.ANDESITE;
         if (HeightMap.getTrueHeightGradient(target.getPopData(), target.getX(), target.getZ(), 3) > TConfig.c.MISC_TREES_GRADIENT_LIMIT * 2.0D) {
            rock = Material.DIORITE;
         }

         while(BlockUtils.isExposedToNonSolid(target)) {
            target.setType(rock);
            target = target.getDown();
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 7);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (TConfig.c.TREES_BIRCH_BIG_ENABLED && GenUtils.chance(random, 1, 20)) {
               (new FractalTreeBuilder(FractalTypes.Tree.BIRCH_BIG)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            } else {
               (new FractalTreeBuilder(FractalTypes.Tree.BIRCH_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ROCKY_BEACH;
   }

   public double calculateHeight(@NotNull TerraformWorld tw, int x, int z) {
      double coreRawHeight = HeightMap.CORE.getHeight(tw, x, z);
      double height = super.calculateHeight(tw, x, z);
      double riverDepth = HeightMap.getRawRiverDepth(tw, x, z);
      if (coreRawHeight - riverDepth <= (double)(TerraformGenerator.seaLevel - 4)) {
         double makeup = 0.0D;
         if (coreRawHeight - riverDepth > (double)(TerraformGenerator.seaLevel - 10)) {
            makeup = coreRawHeight - riverDepth - (double)(TerraformGenerator.seaLevel - 10);
         }

         height = coreRawHeight - makeup;
      }

      return height;
   }
}
