package org.terraform.biome.flat;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeBlender;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.beach.OasisBeach;
import org.terraform.biome.mountainous.BadlandsCanyonHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;
import org.terraform.utils.version.V_1_21_5;
import org.terraform.utils.version.Version;

public class BadlandsHandler extends BiomeHandler {
   static final int sandRadius;
   static final int plateauHeight;
   static final float plateauFrequency;
   static final double plateauThreshold;
   static final double plateauCommonness;
   private static BiomeBlender riversBlender;
   private static BiomeBlender plateauBlender;

   @NotNull
   private static BiomeBlender getRiversBlender(TerraformWorld tw) {
      if (riversBlender == null) {
         riversBlender = (new BiomeBlender(tw, true, false)).setGridBlendingFactor(0.45D);
      }

      return riversBlender;
   }

   @NotNull
   private static BiomeBlender getPlateauBlender(TerraformWorld tw) {
      if (plateauBlender == null) {
         plateauBlender = (new BiomeBlender(tw, true, true)).setRiverThreshold(10);
      }

      return plateauBlender;
   }

   @NotNull
   public static FastNoise getPlateauNoise(TerraformWorld tw) {
      return NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_BADLANDS_PLATEAUNOISE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 7509L));
         n.SetNoiseType(FastNoise.NoiseType.CubicFractal);
         n.SetFractalOctaves(2);
         n.SetFrequency(plateauFrequency);
         return n;
      });
   }

   static int getPlateauHeight(TerraformWorld tw, int x, int z) {
      double rawValue = Math.max(0.0D, (double)getPlateauNoise(tw).GetNoise((float)x, (float)z) + plateauCommonness);
      double noiseValue = rawValue * getPlateauBlender(tw).getEdgeFactor(BiomeBank.BADLANDS, x, z) * (1.0D - (double)((int)(rawValue / plateauThreshold)) * 0.1D);
      double graduated = noiseValue / plateauThreshold;
      double platformHeight = (double)((int)graduated * plateauHeight) + 10.0D * Math.pow(graduated - (double)((int)graduated) - 0.5D - 0.1D, 7.0D) * (double)plateauHeight;
      return (int)Math.round(platformHeight);
   }

   @NotNull
   public BiomeBank getRiverType() {
      return BiomeBank.BADLANDS_RIVER;
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BADLANDS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.RED_SAND, Material.RED_SAND, (Material)GenUtils.randChoice(rand, Material.RED_SAND, Material.RED_SANDSTONE), (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.STONE)};
   }

   public void populateSmallItems(@NotNull TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      this.generatePlateaus(world, rawX, surfaceY, rawZ, data);
      OasisBeach.generateOasisBeach(world, random, data, rawX, rawZ, BiomeBank.BADLANDS);
      if (HeightMap.getNoiseGradient(world, rawX, rawZ, 3) >= 1.5D && GenUtils.chance(random, 49, 50)) {
         BadlandsCanyonHandler.oneUnit(random, data, rawX, rawZ, true);
      } else {
         Material base = data.getType(rawX, surfaceY, rawZ);
         if (base == Material.SAND || base == Material.RED_SAND) {
            if (GenUtils.chance(random, 1, 200)) {
               BlockFace[] var8 = BlockUtils.directBlockFaces;
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  BlockFace face = var8[var10];
                  if (data.getType(rawX + face.getModX(), surfaceY + 1, rawZ + face.getModZ()) != Material.AIR) {
                     return;
                  }
               }

               if (HeightMap.getBlockHeight(world, rawX, rawZ) + 5 < surfaceY) {
                  return;
               }

               if (GenUtils.chance(1, 50)) {
                  this.spawnDeadTree(data, rawX, surfaceY, rawZ);
               } else if (GenUtils.chance(1, 30)) {
                  PlantBuilder.FIREFLY_BUSH.build(data, rawX, surfaceY + 1, rawZ);
               } else {
                  int cactusHeight = PlantBuilder.CACTUS.build(random, data, rawX, surfaceY + 1, rawZ, 2, 5);
                  if (Version.VERSION.isAtLeast(Version.v1_21_5) && GenUtils.chance(random, 1, 10)) {
                     data.setType(rawX, surfaceY + 1 + cactusHeight, rawZ, V_1_21_5.CACTUS_FLOWER);
                  }
               }
            } else if (GenUtils.chance(random, 1, 80) && surfaceY > TerraformGenerator.seaLevel) {
               PlantBuilder.build(new SimpleBlock(data, rawX, surfaceY + 1, rawZ), PlantBuilder.DEAD_BUSH, PlantBuilder.SHORT_DRY_GRASS, PlantBuilder.TALL_DRY_GRASS);
            }
         }

      }
   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(ChunkCache cache, @NotNull TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      BiomeBlender blender = getRiversBlender(tw);
      FastNoise wallNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_BADLANDS_WALLNOISE, (world) -> {
         FastNoise n = new FastNoise((int)(tw.getWorld().getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.07F);
         n.SetFractalOctaves(2);
         return n;
      });
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
      if (HeightMap.getRawRiverDepth(tw, rawX, rawZ) > 0.0D) {
         double riverlessHeight = HeightMap.getRiverlessHeight(tw, rawX, rawZ) - 2.0D;
         double edgeFactor = blender.getEdgeFactor(BiomeBank.BADLANDS, rawX, rawZ);
         double bottomEdgeFactor = Math.min(2.0D * edgeFactor, 1.0D);
         double topEdgeFactor = Math.max(2.0D * edgeFactor - 1.0D, 0.0D);
         double maxDiff = riverlessHeight - (double)TerraformGenerator.seaLevel;
         double heightAboveSea = preciseHeight - 2.0D - (double)TerraformGenerator.seaLevel;
         double riverFactor = heightAboveSea / maxDiff;
         if (riverFactor > 0.0D && heightAboveSea > 0.0D) {
            int buildHeight = (int)Math.round(bottomEdgeFactor * (Math.min(1.0D, 4.0D * Math.pow(riverFactor, 4.0D)) * maxDiff + (double)wallNoise.GetNoise((float)rawX, (float)rawZ) * 1.5D));

            for(int i = buildHeight; i >= 0; --i) {
               int lowerHeight = Math.min(TerraformGenerator.seaLevel + i, (int)Math.round(riverlessHeight));
               chunk.setBlock(x, lowerHeight, z, BlockUtils.getTerracotta(lowerHeight));
            }

            double threshold = 0.4D + (1.0D - topEdgeFactor) * 0.6D;
            if (riverFactor > threshold) {
               int upperBuildHeight = (int)Math.round(1.0D * (Math.min(1.0D, 50.0D * Math.pow(riverFactor - threshold, 2.5D)) * maxDiff + (double)wallNoise.GetNoise((float)rawX, (float)rawZ) * 1.5D));
               if (topEdgeFactor == 0.0D) {
                  return;
               }

               for(int i = 0; i <= upperBuildHeight; ++i) {
                  int upperHeight = (int)riverlessHeight - i;
                  chunk.setBlock(x, upperHeight, z, BlockUtils.getTerracotta(upperHeight));
               }
            }

            if (riverFactor > threshold + 0.12D) {
               chunk.setBlock(x, (int)riverlessHeight + 1, z, Material.RED_SAND);
            }
         }
      }

   }

   void generatePlateaus(@NotNull TerraformWorld tw, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      FastNoise detailsNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_BADLANDS_WALLNOISE, (world) -> {
         FastNoise n = new FastNoise((int)(tw.getSeed() * 7509L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.08F);
         return n;
      });
      double rawValue = Math.max(0.0D, (double)getPlateauNoise(tw).GetNoise((float)rawX, (float)rawZ) + plateauCommonness);
      double noiseValue = rawValue * getPlateauBlender(tw).getEdgeFactor(BiomeBank.BADLANDS, rawX, rawZ) * (1.0D - (double)((int)(rawValue / plateauThreshold)) * 0.05D);
      double graduated = noiseValue / plateauThreshold;
      double platformHeight = (double)((int)graduated * plateauHeight) + 10.0D * Math.pow(graduated - (double)((int)graduated) - 0.5D - 0.1D, 7.0D) * (double)plateauHeight;
      boolean placeSand = false;

      int level;
      for(level = 1; level <= (int)Math.round(platformHeight); ++level) {
         placeSand = true;
         Material material;
         if ((int)graduated * plateauHeight == level) {
            material = Material.RED_SAND;
         } else if ((int)graduated * plateauHeight == level + 1) {
            material = (Material)GenUtils.randChoice((Object[])(Material.RED_SAND, Material.RED_SAND, BlockUtils.getTerracotta(surfaceY + level)));
         } else if ((int)graduated * plateauHeight == level + 2) {
            material = (Material)GenUtils.randChoice((Object[])(Material.RED_SAND, BlockUtils.getTerracotta(surfaceY + level), BlockUtils.getTerracotta(surfaceY + level)));
         } else {
            material = BlockUtils.getTerracotta(surfaceY + level);
         }

         data.setType(rawX, surfaceY + level, rawZ, material);
      }

      if (placeSand && !(graduated - (double)((int)graduated) > 0.2D)) {
         level = ((int)graduated - 1) * plateauHeight;

         for(int sx = rawX - sandRadius; sx <= rawX + sandRadius; ++sx) {
            for(int sz = rawZ - sandRadius; sz <= rawZ + sandRadius; ++sz) {
               double distance = Math.sqrt(Math.pow((double)(sx - rawX), 2.0D) + Math.pow((double)(sz - rawZ), 2.0D));
               if (distance < (double)sandRadius && ((int)graduated == 1 || getPlateauHeight(tw, sx, sz) == plateauHeight)) {
                  int sandHeight = (int)Math.round((double)plateauHeight * 0.55D * Math.pow(1.0D - distance / (double)sandRadius, 1.7D) + (double)detailsNoise.GetNoise((float)sx, (float)sz));

                  for(int y = 1 + level; y <= sandHeight + level; ++y) {
                     if (data.getType(sx, HeightMap.getBlockHeight(tw, sx, sz) + y, sz) == Material.AIR) {
                        data.setType(sx, HeightMap.getBlockHeight(tw, sx, sz) + y, sz, Material.RED_SAND);
                     }
                  }
               }
            }
         }

      }
   }

   void spawnDeadTree(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      if (TConfig.areTreesEnabled()) {
         int height = GenUtils.randInt(5, 7);
         int branches = GenUtils.randInt(1, height == 5 ? 2 : 3);

         for(int i = 1; i <= height; ++i) {
            data.setType(x, y + i, z, Material.DARK_OAK_WOOD);
         }

         ArrayList<Integer> usedBranchHorizontals = new ArrayList();
         ArrayList<Integer> usedBranchVerticals = new ArrayList();

         for(int i = 0; i < branches; ++i) {
            int bHeight = GenUtils.randInt(2, height - 1);
            int bDirection = GenUtils.randInt(1, 4);
            if (!usedBranchHorizontals.contains(bDirection) && !usedBranchVerticals.contains(bHeight)) {
               int bx = x;
               int bz = z;
               switch(bDirection) {
               case 1:
                  bz = z + 1;
                  break;
               case 2:
                  bx = x + 1;
                  break;
               case 3:
                  bz = z - 1;
                  break;
               default:
                  bx = x - 1;
               }

               data.setType(bx, y + bHeight, bz, Material.DARK_OAK_WOOD);
               usedBranchHorizontals.add(bDirection);
               usedBranchVerticals.add(bHeight);
            } else {
               --i;
            }
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      for(int x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
         for(int z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
            int highest = GenUtils.getTrueHighestBlock(data, x, z);
            BiomeBank currentBiome = tw.getBiomeBank(x, z);
            if (currentBiome == BiomeBank.BADLANDS || currentBiome == BiomeBank.BADLANDS_BEACH || currentBiome == BiomeBank.BADLANDS_CANYON) {
               if (HeightMap.getNoiseGradient(tw, x, z, 3) >= 1.5D && GenUtils.chance(random, 49, 50)) {
                  BadlandsCanyonHandler.oneUnit(random, data, x, z, true);
               } else {
                  Material base = data.getType(x, highest, z);
                  if ((base == Material.SAND || base == Material.RED_SAND) && GenUtils.chance(random, 1, 200)) {
                     boolean canSpawn = true;
                     BlockFace[] var10 = BlockUtils.directBlockFaces;
                     int var11 = var10.length;

                     for(int var12 = 0; var12 < var11; ++var12) {
                        BlockFace face = var10[var12];
                        if (data.getType(x + face.getModX(), highest + 1, z + face.getModZ()) != Material.AIR) {
                           canSpawn = false;
                        }
                     }

                     if (GenUtils.getHighestGround(data, x, z) + 5 < highest) {
                        canSpawn = false;
                     }

                     if (canSpawn && GenUtils.chance(1, 50)) {
                        this.spawnDeadTree(data, x, highest, z);
                     }
                  }
               }
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.BADLANDS_BEACH;
   }

   static {
      sandRadius = TConfig.c.BIOME_BADLANDS_PLATEAU_SAND_RADIUS;
      plateauHeight = TConfig.c.BIOME_BADLANDS_PLATEAU_HEIGHT;
      plateauFrequency = TConfig.c.BIOME_BADLANDS_PLATEAU_FREQUENCY;
      plateauThreshold = TConfig.c.BIOME_BADLANDS_PLATEAU_THRESHOLD;
      plateauCommonness = TConfig.c.BIOME_BADLANDS_PLATEAU_COMMONNESS;
   }
}
