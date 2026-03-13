package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.flat.JungleHandler;
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
import org.terraform.tree.TreeDB;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.CylinderBuilder;
import org.terraform.utils.GenUtils;
import org.terraform.utils.SphereBuilder;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class ForestedMountainsHandler extends AbstractMountainHandler {
   private static void dirtStack(@NotNull PopulatorDataAbstract data, @NotNull Random rand, int x, int y, int z) {
      data.setType(x, y, z, Material.GRASS_BLOCK);
      if (GenUtils.chance(rand, 1, 10)) {
         PlantBuilder.GRASS.build(data, x, y + 1, z);
      }

      int depth = GenUtils.randInt(rand, 3, 7);

      for(int i = 1; i < depth && BlockUtils.isStoneLike(data.getType(x, y - i, z)); ++i) {
         data.setType(x, y - i, z, Material.DIRT);
         if (BlockUtils.isExposedToNonSolid(new SimpleBlock(data, x, y - i, z))) {
            ++depth;
         }
      }

   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.JUNGLE;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{(Material)GenUtils.randChoice(rand, Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.COBBLESTONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      int nx;
      if (rawX % 3 == 0 && rawZ % 3 == 0 && HeightMap.CORE.getHeight(tw, rawX, rawZ) - HeightMap.getRawRiverDepth(tw, rawX, rawZ) < (double)(TerraformGenerator.seaLevel - 4)) {
         (new SphereBuilder(random, new SimpleBlock(data, rawX, TerraformGenerator.seaLevel, rawZ), new Material[]{Material.AIR})).setRadius(5.0F).setStaticWaterLevel(TerraformGenerator.seaLevel).setHardReplace(true).build();
         if (GenUtils.chance(random, 1, 30)) {
            nx = TerraformGenerator.seaLevel + (surfaceY - TerraformGenerator.seaLevel) / 2 + 4;
            (new CylinderBuilder(random, new SimpleBlock(data, rawX, nx, rawZ), new Material[]{Material.AIR})).setRadius(5.0F).setRY((float)(surfaceY - TerraformGenerator.seaLevel) / 2.0F + 2.0F).setHardReplace(true).build();
         }
      }

      if (surfaceY >= TerraformGenerator.seaLevel) {
         if (GenUtils.chance(random, 1, 25)) {
            dirtStack(data, random, rawX, surfaceY, rawZ);

            for(nx = -2; nx <= 2; ++nx) {
               for(int nz = -2; nz <= 2; ++nz) {
                  if (!GenUtils.chance(random, 1, 5)) {
                     surfaceY = GenUtils.getHighestGround(data, rawX + nx, rawZ + nz);
                     if (surfaceY >= TerraformGenerator.seaLevel) {
                        dirtStack(data, random, rawX + nx, surfaceY, rawZ + nz);
                     }
                  }
               }
            }
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      FastNoise groundWoodNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_JUNGLE_GROUNDWOOD, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 12L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.07F);
         return n;
      });
      FastNoise groundLeavesNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_JUNGLE_GROUNDLEAVES, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.07F);
         return n;
      });
      SimpleLocation[] bigTrees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 20);
      SimpleLocation[] trees;
      int x;
      int z;
      int distanceToSeaOrMountain;
      if (TConfig.c.TREES_JUNGLE_BIG_ENABLED) {
         trees = bigTrees;
         x = bigTrees.length;

         for(z = 0; z < x; ++z) {
            SimpleLocation sLoc = trees[z];
            distanceToSeaOrMountain = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(distanceToSeaOrMountain);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.JUNGLE_BIG)).skipGradientCheck().build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

      trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 9);
      SimpleLocation[] var14 = trees;
      z = trees.length;

      int y;
      for(y = 0; y < z; ++y) {
         SimpleLocation sLoc = var14[y];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            TreeDB.spawnSmallJungleTree(true, tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
         }
      }

      for(x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
         for(z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
            y = GenUtils.getHighestGround(data, x, z);
            distanceToSeaOrMountain = Math.min(y - TerraformGenerator.seaLevel, 80 - y);
            if (distanceToSeaOrMountain > 0) {
               float leavesNoiseValue = groundLeavesNoise.GetNoise((float)x, (float)z);
               float groundWoodNoiseValue = groundWoodNoise.GetNoise((float)x, (float)z);
               if (distanceToSeaOrMountain <= 4) {
                  leavesNoiseValue -= -0.25F * (float)distanceToSeaOrMountain + 1.0F;
                  groundWoodNoiseValue -= -0.25F * (float)distanceToSeaOrMountain + 1.0F;
               }

               if ((double)leavesNoiseValue > -0.12D && Math.random() > 0.85D) {
                  JungleHandler.createBush(data, leavesNoiseValue, x, y, z);
               } else if (GenUtils.chance(random, 1, 10) && HeightMap.getTrueHeightGradient(data, x, z, 2) > 2.0D) {
                  JungleHandler.createBush(data, 0.0F, x, y, z);
               }

               if ((double)groundWoodNoiseValue > 0.3D) {
                  data.lsetType(x, y + 1, z, Material.JUNGLE_WOOD);
               }
            }

            if (data.getBiome(x, z) == this.getBiome() && BlockUtils.isDirtLike(data.getType(x, y, z)) && data.getType(x, y + 1, z) == Material.JUNGLE_WOOD && BlockUtils.isAir(data.getType(x, y + 2, z)) && GenUtils.chance(2, 9)) {
               PlantBuilder.build(data, x, y + 2, z, PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
            }
         }
      }

   }
}
