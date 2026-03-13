package org.terraform.structure.warmoceanruins;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.GenUtils;

public class WarmOceanRuinsPopulator extends SingleMegaChunkStructurePopulator {
   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(352341322L, chunkX, chunkZ);
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 65732), (int)(TConfig.c.STRUCTURES_WARMOCEANRUINS_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return biome != BiomeBank.DEEP_WARM_OCEAN && biome != BiomeBank.WARM_OCEAN && biome != BiomeBank.DEEP_LUKEWARM_OCEAN && biome != BiomeBank.CORAL_REEF_OCEAN && biome != BiomeBank.COLD_OCEAN && biome != BiomeBank.DEEP_COLD_OCEAN && biome != BiomeBank.FROZEN_OCEAN && biome != BiomeBank.DEEP_FROZEN_OCEAN ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         int x = coords[0];
         int z = coords[1];
         int y = GenUtils.getHighestGround(data, x, z);
         this.spawnWarmOceanRuins(tw, this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), data, x, y, z);
      }
   }

   public void spawnWarmOceanRuins(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int numRooms = 10;
      int range = 80;
      Random hashedRand = tw.getHashedRand((long)x, y, z);
      RoomLayoutGenerator gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y, z, range);
      gen.setRoomMaxX(15);
      gen.setRoomMaxZ(15);
      gen.setRoomMinX(10);
      gen.setRoomMinZ(10);
      gen.setRoomMaxHeight(15);
      gen.setCarveRooms(true);
      gen.setCarveRoomsMultiplier(0.0F, 0.0F, 0.0F);
      gen.forceAddRoom(25, 25, 25);
      gen.registerRoomPopulator(new WarmOceanDomeHutRoom(random, false, false));
      gen.registerRoomPopulator(new WarmOceanAltarRoom(random, false, false));
      gen.registerRoomPopulator(new WarmOceanWellRoom(random, false, false));
      gen.registerRoomPopulator(new WarmOceanLargeArcRoom(random, false, false));
      gen.calculateRoomPlacement();
      gen.fillRoomsOnly(data, tw, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.CRACKED_STONE_BRICKS);
   }

   public int getChunkBufferDistance() {
      return 0;
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && (BiomeBank.isBiomeEnabled(BiomeBank.DEEP_WARM_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.WARM_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.DEEP_LUKEWARM_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.CORAL_REEF_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.COLD_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.DEEP_COLD_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.FROZEN_OCEAN) || BiomeBank.isBiomeEnabled(BiomeBank.DEEP_FROZEN_OCEAN)) && TConfig.c.STRUCTURES_WARMOCEANRUINS_ENABLED;
   }
}
