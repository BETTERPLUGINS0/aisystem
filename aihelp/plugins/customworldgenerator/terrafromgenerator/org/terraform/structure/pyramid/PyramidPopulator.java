package org.terraform.structure.pyramid;

import java.util.Random;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.MazeSpawner;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class PyramidPopulator extends SingleMegaChunkStructurePopulator {
   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return biome != BiomeBank.DESERT ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 163456), (int)(TConfig.c.STRUCTURES_PYRAMID_SPAWNRATIO * 10000.0D), 10000);
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         int[] coords = (new MegaChunk(data.getChunkX(), data.getChunkZ())).getCenterBiomeSectionBlockCoords();
         int x = coords[0];
         int z = coords[1];
         int y = HeightMap.getBlockHeight(tw, x, z);

         try {
            this.spawnPyramid(tw, tw.getHashedRand(x, y, z, 1211222L), data, x, y, z);
         } catch (Throwable var8) {
            TerraformGeneratorPlugin.logger.stackTrace(var8);
         }

      }
   }

   public void spawnPyramid(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      y -= 10;
      TerraformGeneratorPlugin.logger.info("Spawning Pyramid at: " + x + "," + z);
      int numRooms = 1000;
      int range = 70;
      if (y >= TerraformGenerator.seaLevel + 3) {
         this.spawnSandBase(tw, data, x, y, z);
      } else {
         this.spawnSandBase(tw, data, x, TerraformGenerator.seaLevel + 3, z);
         y = TerraformGenerator.seaLevel - 7;
      }

      this.spawnPyramidBase(data, x, y, z);
      Random hashedRand = tw.getHashedRand((long)x, y, z);
      RoomLayoutGenerator level0 = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y - 8, z, range);
      int entranceRoomHeight = 4 + GenUtils.getHighestGround(data, x, z + 5 + range / 2) - (y - 8);
      CubeRoom entranceRoom = new CubeRoom(9, 9, entranceRoomHeight, x, y - 8, z + 5 + range / 2);
      MainEntrancePopulator entrancePopulator = new MainEntrancePopulator(hashedRand, false, false, BlockFace.NORTH);
      entranceRoom.setRoomPopulator(entrancePopulator);
      level0.getRooms().add(entranceRoom);
      level0.registerRoomPopulator(new HuskTombPopulator(random, false, true));
      level0.registerRoomPopulator(new SilverfishNestPopulator(random, false, false));
      level0.registerRoomPopulator(new CursedChamber(random, false, false));
      level0.registerRoomPopulator(new CryptRoom(random, false, false));
      level0.registerRoomPopulator(new GuardianChamberPopulator(random, false, false));
      level0.registerRoomPopulator(new TrapChestChamberPopulator(random, false, false));
      level0.setPathPopulator(new PyramidDungeonPathPopulator(tw.getHashedRand(x, y - 8, z, 2233L)));
      int range = range - 20;
      RoomLayoutGenerator level1 = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y, z, range + 10);
      level1.setMazePathGenerator(new MazeSpawner());
      level1.setRoomMinX(5);
      level1.setRoomMaxX(6);
      level1.setRoomMinZ(5);
      level1.setRoomMaxZ(6);
      level1.setNumRooms(15);
      level1.setPathPopulator(new PyramidPathPopulator(tw.getHashedRand(x, y, z, 2233L)));
      level1.registerRoomPopulator(new MazeLevelMonsterRoom(random, false, false));
      CubeRoom room = level1.forceAddRoom(GenUtils.randInt(6, 12), GenUtils.randInt(6, 12), GenUtils.randInt(level1.getRoomMinHeight(), level1.getRoomMaxHeight()));
      room.setRoomPopulator(new HuskTombPopulator(random, true, true));
      CubeRoom placeholder = new CubeRoom(20, 20, 15, x, y, z);
      level1.getRooms().add(placeholder);

      CubeRoom stairwayTop;
      for(int i = 0; i < 4; ++i) {
         CubeRoom stairway;
         for(stairway = level0.forceAddRoom(5, 5, 10); stairway.centralDistanceSquared(level1.getCenter()) > Math.pow((double)level1.getRange() / 2.0D, 2.0D); stairway = level0.forceAddRoom(5, 5, 10)) {
            level0.getRooms().remove(stairway);
         }

         stairway.setRoomPopulator(new PyramidStairwayRoomPopulator(random, false, false));
         stairwayTop = new CubeRoom(5, 5, 5, stairway.getX(), y, stairway.getZ());
         stairwayTop.setRoomPopulator(new PyramidStairwayTopPopulator(random, false, false));
         level1.getRooms().add(stairwayTop);
      }

      range -= 10;
      RoomLayoutGenerator level2 = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y + 8, z, range);
      level2.setRoomMaxX(10);
      level2.setRoomMinX(7);
      level2.setRoomMaxZ(10);
      level2.setRoomMinZ(7);
      level2.setRoomMaxHeight(6);
      level2.registerRoomPopulator(new TerracottaRoom(random, false, false));
      level2.registerRoomPopulator(new GenericAntechamber(random, false, false));
      level2.registerRoomPopulator(new WarAntechamber(random, false, false));
      level2.registerRoomPopulator(new TreasureAntechamber(random, false, false));
      level2.registerRoomPopulator(new EnchantmentAntechamber(random, false, true));
      level2.setPathPopulator(new PyramidPathPopulator(tw.getHashedRand(x, y + 8, z, 2253L)));
      MazeSpawner mazeSpawner = new MazeSpawner();
      mazeSpawner.setMazePeriod(5);
      level2.setMazePathGenerator(mazeSpawner);
      stairwayTop = new CubeRoom(20, 20, 20, x, y + 8, z);
      stairwayTop.setRoomPopulator(new ElderGuardianChamber(tw.getHashedRand(x, y + 8, z, 1121L), true, true));
      level2.getRooms().add(stairwayTop);

      for(int i = 0; i < 3; ++i) {
         CubeRoom stairway;
         for(stairway = level1.forceAddRoom(5, 5, 10); stairway.centralDistanceSquared(level2.getCenter()) > Math.pow((double)level2.getRange() / 2.0D, 2.0D); stairway = level1.forceAddRoom(5, 5, 10)) {
            level1.getRooms().remove(stairway);
         }

         stairway.setRoomPopulator(new PyramidStairwayRoomPopulator(random, false, false));
         CubeRoom stairwayTop = new CubeRoom(5, 5, 5, stairway.getX(), y + 8, stairway.getZ());
         stairwayTop.setRoomPopulator(new PyramidStairwayTopPopulator(random, false, false));
         level2.getRooms().add(stairwayTop);
      }

      level1.getRooms().remove(placeholder);
      level0.calculateRoomPlacement(false);
      level0.fill(data, tw, Material.SANDSTONE, Material.CUT_SANDSTONE);
      Set<Material> toReplace = Set.of(Material.SANDSTONE, Material.CUT_SANDSTONE);

      for(int nx = -50; nx <= 50; ++nx) {
         for(int nz = -50; nz <= 50; ++nz) {
            if (toReplace.contains(data.getType(x + nx, y - 8, z + nz))) {
               data.setType(x + nx, y - 8, z + nz, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.STONE, Material.STONE, Material.COBBLESTONE, Material.ANDESITE)));
            }

            if (random.nextBoolean()) {
               if (toReplace.contains(data.getType(x + nx, y - 7, z + nz))) {
                  data.setType(x + nx, y - 7, z + nz, GenUtils.weightedRandomMaterial(random, Material.STONE, 9, Material.INFESTED_STONE, 5, Material.COBBLESTONE, 3, Material.ANDESITE, 3));
               }

               if (random.nextBoolean() && toReplace.contains(data.getType(x + nx, y - 6, z + nz))) {
                  data.setType(x + nx, y - 6, z + nz, GenUtils.weightedRandomMaterial(random, Material.STONE, 9, Material.INFESTED_STONE, 5, Material.COBBLESTONE, 3, Material.ANDESITE, 3));
               }
            }
         }
      }

      level1.calculateRoomPlacement(false);
      level1.fill(data, tw, Material.SANDSTONE, Material.CUT_SANDSTONE);
      level2.calculateRoomPlacement(false);
      level2.fill(data, tw, Material.SANDSTONE, Material.CUT_SANDSTONE);
   }

   public void spawnSandBase(TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int squareRadius = 45;
      FastNoise noiseGenerator = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_PYRAMID_BASEELEVATOR, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.PerlinFractal);
         n.SetFrequency(0.007F);
         n.SetFractalOctaves(6);
         return n;
      });
      FastNoise vertNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_PYRAMID_BASEFUZZER, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.PerlinFractal);
         n.SetFrequency(0.01F);
         n.SetFractalOctaves(8);
         return n;
      });

      for(int nx = x - squareRadius; nx <= x + squareRadius; ++nx) {
         for(int nz = z - squareRadius; nz <= z + squareRadius; ++nz) {
            int height = GenUtils.getHighestGround(data, nx, nz);
            Material mat = data.getType(nx, height, nz);
            int original = height;
            int raiseDone = 0;
            int noise = Math.round(noiseGenerator.GetNoise((float)nx, (float)nz) * 5.0F);
            int newHeight = y + noise - 1;
            if (newHeight < y - 1) {
               newHeight = y - 1;
            }

            for(; height < newHeight; ++height) {
               ++raiseDone;
               if (!data.getType(nx, height + 1, nz).isSolid() || data.getType(nx, height + 1, nz) == Material.CACTUS) {
                  data.setType(nx, height + 1, nz, mat);
               }
            }

            if (raiseDone > 0) {
               int XdistanceFromCenter = (int)((float)Math.abs(nx - x) + Math.abs(vertNoise.GetNoise((float)(nx - 80), (float)(nz - 80)) * 25.0F));
               int ZdistanceFromCenter = (int)((float)Math.abs(nz - z) + Math.abs(vertNoise.GetNoise((float)(nx - 80), (float)(nz - 80)) * 25.0F));
               if (XdistanceFromCenter > squareRadius - 10 || ZdistanceFromCenter > squareRadius - 10) {
                  int dist = Math.max(XdistanceFromCenter, ZdistanceFromCenter);
                  float comp = (float)original + (float)raiseDone * (((float)squareRadius - 5.0F - (float)dist) / 5.0F) + Math.abs(vertNoise.GetNoise((float)nx, (float)nz) * 30.0F);
                  if (comp < (float)original) {
                     comp = (float)original;
                  }

                  for(; (float)height > comp; --height) {
                     if (data.getType(nx, height, nz) == mat) {
                        if (height > TerraformGenerator.seaLevel) {
                           data.setType(nx, height, nz, Material.AIR);
                        } else {
                           data.setType(nx, height, nz, Material.WATER);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public void spawnPyramidBase(@NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int height;
      int radius;
      for(int height = 0; height < 40; ++height) {
         height = 40 - height;

         for(radius = -height; radius <= height; ++radius) {
            for(int nz = -height; nz <= height; ++nz) {
               data.setType(x + radius, y + height, z + nz, (Material)GenUtils.randChoice((Object[])(Material.SANDSTONE, Material.SMOOTH_SANDSTONE)));
               if (Math.abs(radius) == height && Math.abs(nz) == height) {
                  if (!data.getType(x + radius, y + height + 1, z + nz).isSolid()) {
                     data.setType(x + radius, y + height + 1, z + nz, Material.SANDSTONE_WALL);
                  }

                  if (height == 38) {
                     data.setType(x + radius, y + 38 + 2, z + nz, Material.CAMPFIRE);
                  }
               } else if (GenUtils.chance(1, 20)) {
                  BlockFace dir = null;
                  if (radius == -height) {
                     dir = BlockFace.EAST;
                  } else if (radius == height) {
                     dir = BlockFace.WEST;
                  } else if (nz == -height) {
                     dir = BlockFace.SOUTH;
                  } else if (nz == height) {
                     dir = BlockFace.NORTH;
                  }

                  if (dir != null) {
                     Stairs s = (Stairs)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])(Material.SANDSTONE_STAIRS, Material.SMOOTH_SANDSTONE_STAIRS)));
                     s.setFacing(dir);
                     data.setBlockData(x + radius, y + height, z + nz, s);
                  }
               }
            }
         }
      }

      data.setType(x, y + 40, z, Material.GOLD_BLOCK);
      data.setType(x, y + 41, z, Material.SANDSTONE_WALL);
      int elevation = 14;

      for(height = elevation; height <= elevation + 16; ++height) {
         radius = 40 - height;
         int[] var20 = new int[]{-radius, 0, radius};
         int var22 = var20.length;

         for(int var21 = 0; var21 < var22; ++var21) {
            int nx = var20[var21];
            int[] var12 = new int[]{-radius, 0, radius};
            int var13 = var12.length;

            for(int var14 = 0; var14 < var13; ++var14) {
               int nz = var12[var14];
               int carveLength = height - elevation;
               if (carveLength > 8) {
                  carveLength = 16 - carveLength;
               }

               if (nx == 0 || nz == 0) {
                  Wall w = null;
                  if (nx == -radius) {
                     w = new Wall(new SimpleBlock(data, x + nx, y + height, z + nz), BlockFace.WEST);
                  } else if (nx == radius) {
                     w = new Wall(new SimpleBlock(data, x + nx, y + height, z + nz), BlockFace.EAST);
                  } else if (nz == -radius) {
                     w = new Wall(new SimpleBlock(data, x + nx, y + height, z + nz), BlockFace.NORTH);
                  } else if (nz == radius) {
                     w = new Wall(new SimpleBlock(data, x + nx, y + height, z + nz), BlockFace.SOUTH);
                  }

                  if (w != null) {
                     for(int i = 0; i <= carveLength; ++i) {
                        if (carveLength == 0) {
                           if (height == elevation) {
                              w.getFront().setType(Material.SANDSTONE_WALL);
                           } else if (height == elevation + 16) {
                              w.getRear().getUp(2).setType(Material.SANDSTONE_WALL);
                           }
                        }

                        w.getLeft(i).setType(Material.AIR);
                        w.getLeft(i).getRear().setType(Material.CUT_RED_SANDSTONE);
                        w.getRight(i).setType(Material.AIR);
                        w.getRight(i).getRear().setType(Material.CUT_RED_SANDSTONE);
                        if (i == carveLength) {
                           w.getRight(i + 1).getUp().setType(Material.SANDSTONE_WALL);
                           w.getLeft(i + 1).getUp().setType(Material.SANDSTONE_WALL);
                        }
                     }
                  }
               }
            }
         }
      }

   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(72917299L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && BiomeBank.isBiomeEnabled(BiomeBank.DESERT) && TConfig.c.STRUCTURES_PYRAMID_ENABLED;
   }
}
