package org.terraform.structure.ancientcity;

import java.util.HashSet;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.EmptyPathWriter;
import org.terraform.structure.JigsawState;
import org.terraform.structure.JigsawStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.structure.room.path.CavePathWriter;
import org.terraform.structure.room.path.PathState;
import org.terraform.utils.GenUtils;
import org.terraform.utils.version.Version;

public class AncientCityPopulator extends JigsawStructurePopulator {
   public static final int RADIUS = 50;

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else if (biome.getType() != BiomeType.MOUNTAINOUS && biome.getType() != BiomeType.HIGH_MOUNTAINOUS) {
         return false;
      } else {
         return !Version.VERSION.isAtLeast(Version.v1_19_4) ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 123122), (int)(TConfig.c.STRUCTURES_ANCIENTCITY_SPAWNRATIO * 10000.0D), 10000);
   }

   @NotNull
   public JigsawState calculateRoomPopulators(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      JigsawState state = new JigsawState();
      int[] coords = mc.getCenterBiomeSectionBlockCoords();
      int x = coords[0];
      int z = coords[1];
      int minY = TConfig.c.STRUCTURES_ANCIENTCITY_MIN_Y;
      int y = GenUtils.randInt(minY, TConfig.c.STRUCTURES_ANCIENTCITY_MAX_Y);
      Random random = tw.getHashedRand(x, y, z, 23412222L);
      TerraformGeneratorPlugin.logger.info("Spawning ancient city at: " + x + "," + y + "," + z);
      RoomLayoutGenerator carverGen = new RoomLayoutGenerator(GenUtils.RANDOMIZER, RoomLayout.RANDOM_BRUTEFORCE, 0, x, y, z, 50);

      for(int nx = ((x - 50 >> 4) - 1 << 4) + 7; nx <= ((x + 50 >> 4) + 1 << 4) + 7; nx += 16) {
         for(int nz = ((z - 50 >> 4) - 1 << 4) + 7; nz <= ((z + 50 >> 4) + 1 << 4) + 7; nz += 16) {
            carverGen.getRooms().add(new CubeRoom(1, 1, 5, nx, y, nz));
         }
      }

      carverGen.roomCarver = new AncientCityBFSCarver(new SimpleLocation(x, y, z));
      PathState ps = carverGen.getOrCalculatePathState(tw);
      ps.writer = new EmptyPathWriter();
      state.roomPopulatorStates.add(carverGen);
      HashSet<SimpleLocation> occupied = new HashSet();
      Random hashedRand = tw.getHashedRand((long)x, y, z);
      RoomLayoutGenerator gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, 40, x, y, z, 120);
      gen.setPathPopulator(new AncientCityPathPopulator(tw.getHashedRand(x, y, z, 2L), gen, occupied));
      gen.setRoomMaxX(26);
      gen.setRoomMaxZ(26);
      gen.setRoomMinHeight(14);
      gen.setRoomMinHeight(20);
      gen.setRoomMinX(16);
      gen.setRoomMinZ(16);
      gen.registerRoomPopulator(new AncientCityRuinsPlatform(tw, occupied, gen, random, false, false));
      gen.registerRoomPopulator(new AncientCitySchematicPlatform(tw, occupied, gen, random, false, false));
      gen.registerRoomPopulator(new AncientCityAltarPopulator(tw, occupied, gen, random, false, false));
      gen.registerRoomPopulator(new AncientCityLargePillarRoomPopulator(tw, occupied, gen, random, false, false));
      SimpleChunkLocation centChunk = new SimpleChunkLocation(tw.getName(), x, y, z);
      int centX = (centChunk.getX() << 4) + 8;
      int centZ = (centChunk.getZ() << 4) + 8;
      CubeRoom room = new CubeRoom(46, 46, 40, centX, y, centZ);
      room.setRoomPopulator(new AncientCityCenterPlatformPopulator(tw, occupied, gen, random, true, true));
      gen.getRooms().add(room);
      gen.calculateRoomPlacement();
      ps = gen.getOrCalculatePathState(tw);
      ps.writer = new CavePathWriter(2.0F, 2.0F, 2.0F, 0, 0, 0);
      gen.calculateRoomPopulators(tw);
      state.roomPopulatorStates.add(gen);
      return state;
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(318377L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_ANCIENTCITY_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 0;
   }

   public int getCaveClusterBufferDistance() {
      return 3;
   }
}
