package org.terraform.structure.stronghold;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.CoordPair;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.GenUtils;
import org.terraform.utils.MazeSpawner;

public class StrongholdPopulator extends SingleMegaChunkStructurePopulator {
   private static boolean debugSpawnMessage = false;
   private static final HashMap<TerraformWorld, CoordPair[]> POSITIONS = new HashMap();

   private static CoordPair randomCircleCoords(@NotNull Random rand, int radius) {
      double angle = rand.nextDouble() * 3.141592653589793D * 2.0D;
      int x = (int)(Math.cos(angle) * (double)radius);
      int z = (int)(Math.sin(angle) * (double)radius);
      return new CoordPair(x, z);
   }

   public CoordPair[] strongholdPositions(@NotNull TerraformWorld tw) {
      if (!POSITIONS.containsKey(tw)) {
         CoordPair[] positions = new CoordPair[128];
         int pos = 0;
         int radius = 1408;
         Random rand = tw.getHashedRand(1L, 1, 1);

         int i;
         for(i = 0; i < 3; ++i) {
            CoordPair coords = randomCircleCoords(rand, radius);
            if (!debugSpawnMessage) {
               TerraformGeneratorPlugin.logger.info("Will spawn stronghold at: " + String.valueOf(coords));
               debugSpawnMessage = true;
            }

            positions[pos++] = coords;
         }

         int radius = radius + 3072;

         for(i = 0; i < 6; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         radius += 3072;

         for(i = 0; i < 10; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         radius += 3072;

         for(i = 0; i < 15; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         radius += 3072;

         for(i = 0; i < 21; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         radius += 3072;

         for(i = 0; i < 28; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         radius += 3072;

         for(i = 0; i < 36; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         radius += 3072;

         for(i = 0; i < 9; ++i) {
            positions[pos++] = randomCircleCoords(rand, radius);
         }

         POSITIONS.put(tw, positions);
      }

      return (CoordPair[])POSITIONS.get(tw);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         CoordPair[] positions = this.strongholdPositions(tw);
         CoordPair[] var6 = positions;
         int var7 = positions.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            CoordPair pos = var6[var8];
            if (pos.x() >> 4 == chunkX && pos.z() >> 4 == chunkZ) {
               return true;
            }
         }

         return false;
      }
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         CoordPair[] positions = this.strongholdPositions(tw);

         for(int x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
            for(int z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
               CoordPair[] var6 = positions;
               int var7 = positions.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  CoordPair pos = var6[var8];
                  if (pos.x() == x && pos.z() == z) {
                     int y = GenUtils.randInt(TConfig.c.STRUCTURES_STRONGHOLD_MIN_Y, TConfig.c.STRUCTURES_STRONGHOLD_MAX_Y);
                     if (y + 18 > GenUtils.getHighestGround(data, x, z) && y > TConfig.c.STRUCTURES_STRONGHOLD_FAILSAFE_Y) {
                        y = TConfig.c.STRUCTURES_STRONGHOLD_FAILSAFE_Y;
                     }

                     this.spawnStronghold(tw, this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), data, x, y, z);
                     break;
                  }
               }
            }
         }

      }
   }

   public void spawnStronghold(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int numRooms = 70;
      int range = 100;
      Random hashedRand = tw.getHashedRand((long)x, y, z);
      RoomLayoutGenerator gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y, z, range);
      MazeSpawner mazeSpawner = new MazeSpawner();
      mazeSpawner.setMazePeriod(10);
      mazeSpawner.setMazePathWidth(3);
      mazeSpawner.setWidth(range + 20);
      mazeSpawner.setMazeHeight(4);
      mazeSpawner.setCovered(true);
      gen.setMazePathGenerator(mazeSpawner);
      gen.setPathPopulator(new StrongholdPathPopulator(tw.getHashedRand(x, y, z, 2L)));
      gen.setRoomMaxX(30);
      gen.setRoomMaxZ(30);
      gen.setRoomMaxHeight(15);
      gen.forceAddRoom(25, 25, 15);
      CubeRoom stairwayOne = gen.forceAddRoom(5, 5, 18);

      assert stairwayOne != null;

      stairwayOne.setRoomPopulator(new StairwayRoomPopulator(random, false, false));
      gen.registerRoomPopulator(new PortalRoomPopulator(random, true, true));
      gen.registerRoomPopulator(new LibraryRoomPopulator(random, false, false));
      gen.registerRoomPopulator(new NetherPortalRoomPopulator(random, false, true));
      gen.registerRoomPopulator(new PrisonRoomPopulator(random, false, false));
      gen.registerRoomPopulator(new SilverfishDenPopulator(random, false, false));
      gen.registerRoomPopulator(new SupplyRoomPopulator(random, false, false));
      gen.registerRoomPopulator(new TrapChestRoomPopulator(random, false, false));
      gen.registerRoomPopulator(new HallwayPopulator(random, false, false));
      gen.calculateRoomPlacement();
      gen.fill(data, tw, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS);
      gen.reset();
      mazeSpawner = new MazeSpawner();
      mazeSpawner.setMazePeriod(10);
      mazeSpawner.setMazePathWidth(3);
      mazeSpawner.setCovered(true);
      mazeSpawner.setMazeHeight(4);
      mazeSpawner.setWidth(range + 20);
      gen.setMazePathGenerator(mazeSpawner);
      y += 18;
      gen.setCentY(y);
      gen.setRand(tw.getHashedRand((long)x, y, z));
      gen.setPathPopulator(new StrongholdPathPopulator(tw.getHashedRand(x, y, z, 2L)));
      CubeRoom stairwayTwo = new CubeRoom(5, 5, 5, stairwayOne.getX(), y, stairwayOne.getZ());
      stairwayTwo.setRoomPopulator(new StairwayTopPopulator(random, false, false));
      gen.getRooms().add(stairwayTwo);
      gen.calculateRoomPlacement();
      gen.fill(data, tw, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS);
   }

   public int[] getNearestFeature(@NotNull TerraformWorld tw, int rawX, int rawZ) {
      double minDistanceSquared = Double.MAX_VALUE;
      CoordPair min = null;
      CoordPair[] var7 = this.strongholdPositions(tw);
      int var8 = var7.length;

      for(int var9 = 0; var9 < var8; ++var9) {
         CoordPair pos = var7[var9];
         double distSqr = Math.pow((double)(pos.x() - rawX), 2.0D) + Math.pow((double)(pos.z() - rawZ), 2.0D);
         if (min == null) {
            minDistanceSquared = distSqr;
            min = pos;
         } else if (distSqr < minDistanceSquared) {
            minDistanceSquared = distSqr;
            min = pos;
         }
      }

      assert min != null;

      return new int[]{min.x(), min.z()};
   }

   public CoordPair getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      CoordPair[] positions = this.strongholdPositions(tw);
      CoordPair[] var4 = positions;
      int var5 = positions.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         CoordPair pos = var4[var6];
         if (mc.containsXZBlockCoords(pos.x(), pos.z())) {
            return pos;
         }
      }

      return null;
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(1999222L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_STRONGHOLD_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 0;
   }
}
