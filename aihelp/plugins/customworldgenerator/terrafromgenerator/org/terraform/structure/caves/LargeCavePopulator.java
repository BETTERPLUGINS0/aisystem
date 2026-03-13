package org.terraform.structure.caves;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.JigsawState;
import org.terraform.structure.JigsawStructurePopulator;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class LargeCavePopulator extends JigsawStructurePopulator {
   @NotNull
   public JigsawState calculateRoomPopulators(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      JigsawState state = new JigsawState();
      int[] spawnCoords = mc.getCenterBiomeSectionBlockCoords();
      int x = spawnCoords[0];
      int z = spawnCoords[1];
      Random rand = tw.getHashedRand((long)x, z, 79810139);
      FastNoise noise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.STRUCTURE_LARGECAVE_CARVER, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 8726L));
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.04F);
         return n;
      });
      int minY = GenUtils.randInt(rand, -5, -32);
      int highest = GenUtils.getTransformedHeight(tw, x, z) - minY;
      int rX = GenUtils.randInt(rand, 30, 50);
      int rY = (highest - 20) / 2;
      int rZ = GenUtils.randInt(rand, 30, 50);
      int y = rY + minY + 6;
      GenericLargeCavePopulator cavePopulator = (GenericLargeCavePopulator)Objects.requireNonNull((GenericLargeCavePopulator)GenUtils.choice(rand, new GenericLargeCavePopulator[]{new MushroomCavePopulator(tw.getHashedRand((long)x, 13729804, z), false, false), new GenericLargeCavePopulator(tw.getHashedRand((long)x, 13729804, z), false, false), new LargeLushCavePopulator(tw.getHashedRand((long)x, 13729804, z), false, false)}));
      RoomLayoutGenerator gen = new RoomLayoutGenerator(new Random(), RoomLayout.RANDOM_BRUTEFORCE, 10, x, y, z, 150);
      gen.setGenPaths(false);
      gen.roomCarver = new LargeCaveRoomCarver((Material)GenUtils.randChoice(rand, Material.LAVA, Material.WATER));
      SimpleLocation center = new SimpleLocation(x, y, z);
      TerraformGeneratorPlugin.logger.info("Large Cave at " + String.valueOf(center) + " has water level > " + minY + " with populator " + cavePopulator.getClass().getSimpleName());
      HashMap<SimpleChunkLocation, LargeCaveRoomPiece> chunkToRoom = new HashMap();
      ArrayDeque<SimpleLocation> queue = new ArrayDeque();
      HashSet<SimpleLocation> seen = new HashSet();
      int actualMinY = y;
      seen.add(center);
      queue.add(center);

      while(!queue.isEmpty()) {
         SimpleLocation v = (SimpleLocation)queue.remove();
         LargeCaveRoomPiece caveRoom = (LargeCaveRoomPiece)chunkToRoom.computeIfAbsent(new SimpleChunkLocation(tw.getName(), GenUtils.getTripleChunk(v.getX() >> 4), GenUtils.getTripleChunk(v.getZ() >> 4)), (loc) -> {
            LargeCaveRoomPiece newRoom = new LargeCaveRoomPiece(41, 41, 15, GenUtils.getTripleChunk(v.getX() >> 4) * 16 + 7, y, GenUtils.getTripleChunk(v.getZ() >> 4) * 16 + 7);
            newRoom.setRoomPopulator(cavePopulator);
            gen.getRooms().add(newRoom);
            return newRoom;
         });
         actualMinY = Math.min(actualMinY, v.getY());
         boolean nextToBoundary = false;
         BlockFace[] var25 = BlockUtils.sixBlockFaces;
         int var26 = var25.length;

         for(int var27 = 0; var27 < var26; ++var27) {
            BlockFace face = var25[var27];
            SimpleLocation neighbour = v.getRelative(face);
            if (!seen.contains(neighbour)) {
               if (neighbour.getY() + 3 >= GenUtils.getTransformedHeight(tw, neighbour.getX(), neighbour.getZ())) {
                  nextToBoundary = true;
               } else {
                  double equationResult = Math.pow((double)(neighbour.getX() - center.getX()), 2.0D) / Math.pow((double)rX, 2.0D) + Math.pow((double)(neighbour.getY() - center.getY()), 2.0D) / Math.pow((double)rY, 2.0D) + Math.pow((double)(neighbour.getZ() - center.getZ()), 2.0D) / Math.pow((double)rZ, 2.0D);
                  double n = 0.7D * (double)noise.GetNoise((float)neighbour.getX(), (float)neighbour.getY(), (float)neighbour.getZ());
                  if (equationResult > 1.0D + Math.max(0.0D, n)) {
                     nextToBoundary = true;
                  } else {
                     seen.add(neighbour);
                     queue.add(neighbour);
                  }
               }
            }
         }

         caveRoom.toCarve.set(v);
         caveRoom.boundaries.set(v, nextToBoundary);
         if (!nextToBoundary && caveRoom.startingLoc == null) {
            caveRoom.startingLoc = v;
         }
      }

      ((LargeCaveRoomCarver)gen.roomCarver).waterLevel = actualMinY + GenUtils.randInt(rand, 4, 7);
      gen.getRooms().forEach((room) -> {
         ((LargeCaveRoomPiece)room).waterLevel = ((LargeCaveRoomCarver)gen.roomCarver).waterLevel;
      });
      state.roomPopulatorStates.add(gen);
      return state;
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12345), (int)(TConfig.c.STRUCTURES_LARGECAVE_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return biome.getType() == BiomeType.DEEP_OCEANIC ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(123912L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areCavesEnabled() && TConfig.c.STRUCTURES_LARGECAVE_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 0;
   }

   public int getCaveClusterBufferDistance() {
      return 3;
   }
}
