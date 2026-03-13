package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
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

public class SavannaHandler extends BiomeHandler {
   private static void makeYellowPatch(int x, int y, int z, @NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int length = GenUtils.randInt(6, 16);
      int nx = x;

      for(int nz = z; length-- > 0; y = GenUtils.getHighestGround(data, nx, nz)) {
         if (BlockUtils.isDirtLike(data.getType(nx, y, nz)) && data.getType(nx, y + 1, nz) == Material.AIR) {
            data.setType(nx, y, nz, Material.DIRT_PATH);
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
      return Biome.SAVANNA;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{(Material)GenUtils.randChoice(rand, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.COARSE_DIRT), Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (GenUtils.chance(random, 1, 128)) {
         makeYellowPatch(rawX, surfaceY, rawZ, data, random);
      }

      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && !data.getType(rawX, surfaceY + 1, rawZ).isSolid() && GenUtils.chance(random, TConfig.c.BIOME_SAVANNA_TALLGRASSCHANCE, 10000)) {
         PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      boolean spawnedLargeSavannaTree = false;
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 112, 0.6F);
      SimpleLocation[] poffs;
      int var7;
      int var8;
      SimpleLocation sLoc;
      int treeY;
      if (TConfig.c.TREES_SAVANNA_BIG_ENABLED) {
         poffs = trees;
         var7 = trees.length;

         for(var8 = 0; var8 < var7; ++var8) {
            sLoc = poffs[var8];
            treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.SAVANNA_BIG)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
               spawnedLargeSavannaTree = true;
            }
         }
      }

      if (!spawnedLargeSavannaTree) {
         trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 32);
         poffs = trees;
         var7 = trees.length;

         for(var8 = 0; var8 < var7; ++var8) {
            sLoc = poffs[var8];
            treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.SAVANNA_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

      if (TConfig.arePlantsEnabled()) {
         poffs = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 31);
         SimpleLocation[] var16 = poffs;
         var8 = poffs.length;

         for(int var17 = 0; var17 < var8; ++var17) {
            SimpleLocation sLoc = var16[var17];
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ())) && !data.getType(sLoc.getX(), sLoc.getY() + 1, sLoc.getZ()).isSolid()) {
               SimpleBlock base = new SimpleBlock(data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
               int rX = GenUtils.randInt(random, 2, 4);
               int rY = GenUtils.randInt(random, 2, 4);
               int rZ = GenUtils.randInt(random, 2, 4);
               BlockUtils.replaceSphere(random.nextInt(999), (float)rX, (float)rY, (float)rZ, base, false, Material.ACACIA_LEAVES);
            }
         }
      }

   }
}
