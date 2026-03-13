package org.terraform.structure.mineshaft;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.HeightMap;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.structure.JigsawState;
import org.terraform.structure.JigsawStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.structure.room.carver.CaveRoomCarver;
import org.terraform.structure.room.path.CavePathWriter;
import org.terraform.structure.room.path.PathState;
import org.terraform.utils.GenUtils;

public class MineshaftPopulator extends JigsawStructurePopulator {
   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else if (biome.getType() == BiomeType.DEEP_OCEANIC) {
         return false;
      } else {
         return biome == BiomeBank.BADLANDS_CANYON ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12222), (int)(TConfig.c.STRUCTURES_MINESHAFT_SPAWNRATIO * 10000.0D), 10000);
   }

   @NotNull
   public JigsawState calculateRoomPopulators(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      return this.calculateRoomPopulators(tw, mc, false);
   }

   @NotNull
   public JigsawState calculateRoomPopulators(@NotNull TerraformWorld tw, @NotNull MegaChunk mc, boolean badlandsMineshaft) {
      JigsawState state = new JigsawState();
      int[] coords = mc.getCenterBiomeSectionBlockCoords();
      int x = coords[0];
      int z = coords[1];
      int y;
      if (!badlandsMineshaft) {
         y = GenUtils.randInt(TConfig.c.STRUCTURES_MINESHAFT_MIN_Y, TConfig.c.STRUCTURES_MINESHAFT_MAX_Y);
         if (y < TerraformGeneratorPlugin.injector.getMinY()) {
            y = TerraformGeneratorPlugin.injector.getMinY() + 15;
         }
      } else {
         y = (int)(HeightMap.CORE.getHeight(tw, x, z) - (double)BadlandsMinePopulator.shaftDepth);
      }

      Random hashedRand = tw.getHashedRand((long)mc.getX(), mc.getZ(), 179821643);
      boolean doubleLevel = hashedRand.nextBoolean();
      RoomLayoutGenerator gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, 10, x, y, z, 150);
      Random pathRand = tw.getHashedRand(x, y, z, 2L);
      gen.setPathPopulator((PathPopulatorAbstract)(badlandsMineshaft ? new BadlandsMineshaftPathPopulator(pathRand) : new MineshaftPathPopulator(pathRand)));
      gen.setRoomMaxX(15);
      gen.setRoomMaxZ(15);
      gen.setRoomMinX(13);
      gen.setRoomMinZ(13);
      gen.registerRoomPopulator(new SmeltingHallPopulator(tw.getHashedRand((long)mc.getX(), mc.getZ(), 198034143), false, false));
      gen.registerRoomPopulator(new CaveSpiderDenPopulator(tw.getHashedRand((long)mc.getX(), mc.getZ(), 1829731), false, false));
      if (doubleLevel) {
         gen.registerRoomPopulator(new ShaftRoomPopulator(tw.getHashedRand((long)mc.getX(), mc.getZ(), 213098), true, false));
      }

      if (badlandsMineshaft) {
         CubeRoom brokenShaft = new CubeRoom(15, 15, 7, gen.getCentX(), gen.getCentY(), gen.getCentZ());
         brokenShaft.setRoomPopulator(new BrokenShaftPopulator(hashedRand, true, false));
         gen.getRooms().add(brokenShaft);
      }

      gen.wallMaterials = new Material[]{Material.CAVE_AIR};
      gen.roomCarver = new CaveRoomCarver();
      gen.calculateRoomPlacement();
      PathState ps = gen.getOrCalculatePathState(tw);
      ps.writer = new CavePathWriter(0.0F, 1.0F, 0.0F, 0, 1, 0);
      gen.calculateRoomPopulators(tw);
      state.roomPopulatorStates.add(gen);
      if (doubleLevel) {
         RoomLayoutGenerator secondGen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, 10, x, y + 15, z, 150);
         pathRand = tw.getHashedRand(x, y + 15, z, 2L);
         secondGen.setPathPopulator((PathPopulatorAbstract)(badlandsMineshaft ? new BadlandsMineshaftPathPopulator(pathRand) : new MineshaftPathPopulator(pathRand)));
         secondGen.setRoomMaxX(15);
         secondGen.setRoomMaxZ(15);
         secondGen.setRoomMinX(13);
         secondGen.setRoomMinZ(13);
         Iterator var15 = gen.getRooms().iterator();

         while(var15.hasNext()) {
            CubeRoom room = (CubeRoom)var15.next();
            if (room.getPop() instanceof ShaftRoomPopulator) {
               CubeRoom topShaft = new CubeRoom(room.getWidthX(), room.getHeight(), room.getWidthZ(), room.getX(), room.getY() + 15, room.getZ());
               topShaft.setRoomPopulator(new ShaftTopPopulator(hashedRand, true, false));
               secondGen.getRooms().add(topShaft);
            }
         }

         secondGen.registerRoomPopulator(new SmeltingHallPopulator(tw.getHashedRand((long)mc.getX(), mc.getZ(), 9870312), false, false));
         secondGen.registerRoomPopulator(new CaveSpiderDenPopulator(tw.getHashedRand((long)mc.getX(), mc.getZ(), 46783129), false, false));
         secondGen.wallMaterials = new Material[]{Material.CAVE_AIR};
         secondGen.roomCarver = new CaveRoomCarver();
         secondGen.calculateRoomPlacement();
         ps = secondGen.getOrCalculatePathState(tw);
         ps.writer = new CavePathWriter(0.0F, 1.0F, 0.0F, 0, 1, 0);
         secondGen.calculateRoomPopulators(tw);
         state.roomPopulatorStates.add(secondGen);
      }

      return state;
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(3929202L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_MINESHAFT_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 0;
   }
}
