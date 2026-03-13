package org.terraform.biome.river;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.V_1_19;

public class DarkForestRiverHandler extends BiomeHandler {
   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.RIVER;
   }

   @NotNull
   public Material[] getSurfaceCrust(Random rand) {
      return new Material[]{Material.DIRT, Material.PODZOL, Material.STONE, Material.STONE};
   }

   public void populateSmallItems(@NotNull TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY < TerraformGenerator.seaLevel) {
         if (surfaceY >= TerraformGenerator.seaLevel - 2) {
            data.setType(rawX, surfaceY, rawZ, Material.COARSE_DIRT);
         } else if (surfaceY >= TerraformGenerator.seaLevel - 4 && random.nextBoolean()) {
            data.setType(rawX, surfaceY, rawZ, Material.COARSE_DIRT);
         }

         if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
            RiverHandler.riverVegetation(world, random, data, rawX, surfaceY, rawZ);
            if (GenUtils.chance(random, TConfig.c.BIOME_CLAY_DEPOSIT_CHANCE_OUT_OF_THOUSAND, 1000)) {
               BlockUtils.generateClayDeposit(rawX, surfaceY, rawZ, data, random);
            }

            if (GenUtils.chance(random, 1, 1000)) {
               BlockUtils.replaceCircularPatch(random.nextInt(9999), 2.0F, new SimpleBlock(data, rawX, surfaceY, rawZ), Material.MAGMA_BLOCK);
            }

         }
      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data) {
      GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 7, 0.6F);
      SimpleLocation[] bigTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 24);
      SimpleLocation[] var6 = bigTrees;
      int var7 = bigTrees.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         SimpleLocation sLoc = var6[var8];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            if (treeY < TerraformGenerator.seaLevel) {
               TreeDB.spawnBreathingRoots(tw, new SimpleBlock(data, sLoc), V_1_19.MANGROVE_ROOTS);
               FractalTypes.Tree.SWAMP_TOP.build(tw, new SimpleBlock(data, sLoc), (t) -> {
                  t.setCheckGradient(false);
               });
            }
         }
      }

   }
}
