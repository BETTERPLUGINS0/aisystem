package org.terraform.structure.trailruins;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.utils.GenUtils;

public class TrailRuinsPopulator extends SingleMegaChunkStructurePopulator {
   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return biome != BiomeBank.TAIGA && biome != BiomeBank.SNOWY_TAIGA && biome != BiomeBank.JUNGLE ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   public void spawnTrailRuins(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      int numRooms = 10;
      int range = 40;
      Random hashedRand = tw.getHashedRand((long)x, y, z);
      RoomLayoutGenerator gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y, z, range);
      gen.setPathPopulator(new TrailRuinsPathPopulator(hashedRand));
      gen.setRoomMaxX(10);
      gen.setRoomMaxZ(10);
      gen.setRoomMinX(6);
      gen.setRoomMinZ(6);
      gen.setRoomMaxHeight(15);
      gen.setCarveRooms(true);
      gen.setCarveRoomsMultiplier(0.0F, 0.0F, 0.0F);
      CubeRoom towerRoom = new CubeRoom(7, 7, 7, x, y, z);
      towerRoom.setRoomPopulator(new TrailRuinsTowerRoom(random, false, false));
      gen.getRooms().add(towerRoom);
      gen.registerRoomPopulator(new TrailRuinsTowerRoom(random, false, false));
      gen.registerRoomPopulator(new TrailRuinsHutRoom(random, false, false));
      gen.calculateRoomPlacement();
      gen.carvePathsOnly(data, tw, Material.BARRIER);
      gen.populatePathsOnly();
      gen.fillRoomsOnly(data, tw, Material.STONE_BRICKS);
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         int x = coords[0];
         int z = coords[1];
         int y = GenUtils.getHighestGround(data, x, z) - GenUtils.randInt(this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), 10, 15);
         this.spawnTrailRuins(tw, this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), data, x, y, z);
      }
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(217842323L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && (BiomeBank.isBiomeEnabled(BiomeBank.TAIGA) || BiomeBank.isBiomeEnabled(BiomeBank.SNOWY_TAIGA) || BiomeBank.isBiomeEnabled(BiomeBank.JUNGLE)) && TConfig.c.STRUCTURES_TRAILRUINS_ENABLED;
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 34122), (int)(TConfig.c.STRUCTURES_TRAILRUINS_SPAWNRATIO * 10000.0D), 10000);
   }
}
