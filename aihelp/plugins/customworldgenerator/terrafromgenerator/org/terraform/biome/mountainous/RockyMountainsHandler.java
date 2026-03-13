package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.PhysicsUpdaterPopulator;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;

public class RockyMountainsHandler extends AbstractMountainHandler {
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

   public static void placeWaterFall(@NotNull TerraformWorld tw, int seed, @NotNull SimpleBlock base) {
      float radius = 4.0F;
      FastNoise noise = new FastNoise(seed);
      noise.SetNoiseType(FastNoise.NoiseType.Simplex);
      noise.SetFrequency(0.09F);

      for(float x = -radius; x <= radius; ++x) {
         for(float y = -radius / 2.0F; y <= radius / 2.0F; ++y) {
            for(float z = -radius; z <= radius; ++z) {
               SimpleBlock rel = base.getRelative(Math.round(x), Math.round(y), Math.round(z));
               double equationResult = Math.pow((double)x, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)y, 2.0D) / Math.pow((double)radius, 2.0D) + Math.pow((double)z, 2.0D) / Math.pow((double)radius, 2.0D);
               if (equationResult <= 1.0D + 0.7D * (double)noise.GetNoise((float)rel.getX(), (float)rel.getY(), (float)rel.getZ())) {
                  if (y > 0.0F) {
                     rel.setType(Material.AIR);
                  } else if (rel.isSolid()) {
                     rel.setType(Material.WATER);
                     PhysicsUpdaterPopulator.pushChange(tw.getName(), new SimpleLocation(rel.getX(), rel.getY(), rel.getZ()));
                  }
               }
            }
         }
      }

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
      return new Material[]{(Material)GenUtils.randChoice(rand, Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.COBBLESTONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.COBBLESTONE, Material.STONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel) {
         if (GenUtils.chance(random, 1, 25)) {
            dirtStack(data, random, rawX, surfaceY, rawZ);

            for(int nx = -2; nx <= 2; ++nx) {
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

   public boolean checkWaterfallSpace(@NotNull SimpleBlock b) {
      if (b.getY() < TerraformGenerator.seaLevel + 15) {
         return false;
      } else {
         for(int i = 0; i < 5; ++i) {
            if (!b.getRelative(0, -i, 0).isSolid()) {
               return false;
            }
         }

         return BlockUtils.isExposedToNonSolid(b.getDown(4));
      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data) {
      int surfaceY;
      for(int rawX = data.getChunkX() * 16; rawX < data.getChunkX() * 16 + 16; ++rawX) {
         for(int rawZ = data.getChunkZ() * 16; rawZ < data.getChunkZ() * 16 + 16; ++rawZ) {
            surfaceY = GenUtils.getTransformedHeight(data.getTerraformWorld(), rawX, rawZ);
            if (HeightMap.getTrueHeightGradient(data, rawX, rawZ, 3) > 1.5D && HeightMap.CORE.getHeight(tw, rawX, rawZ) - HeightMap.getRawRiverDepth(tw, rawX, rawZ) < (double)TerraformGenerator.seaLevel) {
               SimpleBlock block = new SimpleBlock(data, rawX, surfaceY, rawZ);
               if (this.checkWaterfallSpace(block) && GenUtils.chance(tw.getHashedRand((long)rawX, surfaceY, rawZ), 1, 30)) {
                  block = block.getDown(4);
                  placeWaterFall(tw, rawX + 11 * rawZ + 31 * surfaceY, block);
                  break;
               }
            }
         }
      }

      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 14);
      SimpleLocation[] var11 = trees;
      surfaceY = trees.length;

      for(int var12 = 0; var12 < surfaceY; ++var12) {
         SimpleLocation sLoc = var11[var12];
         if (HeightMap.getTrueHeightGradient(data, sLoc.getX(), sLoc.getZ(), 3) < 1.4D) {
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.NORMAL_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ROCKY_BEACH;
   }
}
