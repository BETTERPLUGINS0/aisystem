package org.terraform.biome.beach;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class SandyBeachHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BEACH;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.SAND, Material.SAND, (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.SAND, Material.SAND, Material.SAND, Material.SAND, Material.SAND), (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.SANDSTONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      boolean hasSugarcane = GenUtils.chance(random, 1, 100);
      Material base = data.getType(rawX, surfaceY, rawZ);
      if (base == Material.SAND || base == Material.GRASS_BLOCK) {
         ++surfaceY;
         if (hasSugarcane) {
            boolean hasWater = data.getType(rawX + 1, surfaceY - 1, rawZ) == Material.WATER;
            if (data.getType(rawX - 1, surfaceY - 1, rawZ) == Material.WATER) {
               hasWater = true;
            }

            if (data.getType(rawX, surfaceY - 1, rawZ + 1) == Material.WATER) {
               hasWater = true;
            }

            if (data.getType(rawX, surfaceY - 1, rawZ - 1) == Material.WATER) {
               hasWater = true;
            }

            if (hasWater) {
               PlantBuilder.SUGAR_CANE.build(random, data, rawX, surfaceY, rawZ, 3, 7);
            }
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] coconutTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 20);
      SimpleLocation[] var5 = coconutTrees;
      int var6 = coconutTrees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && (BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ())) || data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.SAND)) {
            TreeDB.spawnCoconutTree(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
         }
      }

   }
}
