package org.terraform.structure.catacombs;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.HeightMap;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.JigsawState;
import org.terraform.structure.JigsawStructurePopulator;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.structure.room.carver.StandardRoomCarver;
import org.terraform.structure.room.path.CavePathWriter;
import org.terraform.structure.room.path.PathState;
import org.terraform.utils.GenUtils;

public class CatacombsPopulator extends JigsawStructurePopulator {
   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         if (biome.getType() == BiomeType.DEEP_OCEANIC) {
            return false;
         } else if (biome == BiomeBank.BADLANDS_CANYON) {
            return false;
         } else if (biome != BiomeBank.PLAINS && biome != BiomeBank.FOREST && biome != BiomeBank.SAVANNA && biome != BiomeBank.TAIGA && biome != BiomeBank.SCARLET_FOREST && biome != BiomeBank.CHERRY_GROVE) {
            int height = HeightMap.getBlockHeight(tw, coords[0], coords[1]);
            return height < TConfig.c.STRUCTURES_CATACOMBS_MAX_Y + 15 ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
         } else {
            return false;
         }
      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 17261), (int)(TConfig.c.STRUCTURES_CATACOMBS_SPAWNRATIO * 10000.0D), 10000);
   }

   @NotNull
   public JigsawState calculateRoomPopulators(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      JigsawState state = new JigsawState();
      int[] coords = mc.getCenterBiomeSectionBlockCoords();
      int x = coords[0];
      int z = coords[1];
      int minY = TConfig.c.STRUCTURES_CATACOMBS_MIN_Y;
      int y = GenUtils.randInt(minY, TConfig.c.STRUCTURES_CATACOMBS_MAX_Y);
      int numRooms = 10;
      int range = 50;
      Random random = tw.getHashedRand(x, y, z, 1928374L);
      Random hashedRand = tw.getHashedRand((long)x, y, z);
      boolean canGoDeeper = this.canGoDeeper(tw, y, hashedRand);
      RoomLayoutGenerator gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y, z, range);
      gen.setPathPopulator(new CatacombsPathPopulator(tw.getHashedRand(x, y, z, 2L)));
      gen.setRoomMaxX(10);
      gen.setRoomMaxZ(10);
      gen.setRoomMinX(7);
      gen.setRoomMinZ(7);
      gen.setRoomMinHeight(7);
      gen.setRoomMaxHeight(10);
      gen.registerRoomPopulator(new CatacombsStandardPopulator(random, false, false));
      gen.registerRoomPopulator(new CatacombsSkeletonDungeonPopulator(random, false, false));
      gen.registerRoomPopulator(new CatacombsPillarRoomPopulator(random, false, false));
      gen.registerRoomPopulator(new CatacombsCasketRoomPopulator(random, false, false));
      if (canGoDeeper) {
         gen.registerRoomPopulator(new CatacombsStairwayPopulator(random, true, false));
         gen.registerRoomPopulator(new CatacombsDripstoneCavern(random, true, false));
      }

      gen.roomCarver = new StandardRoomCarver(-1, Material.CAVE_AIR);
      gen.calculateRoomPlacement();
      PathState ps = gen.getOrCalculatePathState(tw);
      ps.writer = new CavePathWriter(0.0F, 0.0F, 0.0F, 0, 2, 0);
      gen.calculateRoomPopulators(tw);
      state.roomPopulatorStates.add(gen);
      byte catacombLevels = 1;

      while(canGoDeeper && catacombLevels < TConfig.c.STRUCTURES_CATACOMBS_MAX_LEVELS) {
         y -= 15;
         hashedRand = tw.getHashedRand((long)x, y, z);
         canGoDeeper = this.canGoDeeper(tw, y, hashedRand);
         RoomLayoutGenerator previousGen = gen;
         gen = new RoomLayoutGenerator(hashedRand, RoomLayout.RANDOM_BRUTEFORCE, numRooms, x, y, z, range);
         gen.setPathPopulator(new CatacombsPathPopulator(tw.getHashedRand(x, y, z, 2L)));
         int stairways = 0;
         Iterator var19 = previousGen.getRooms().iterator();

         while(var19.hasNext()) {
            CubeRoom room = (CubeRoom)var19.next();
            CubeRoom stairwayBase;
            if (room.getPop() instanceof CatacombsStairwayPopulator) {
               stairwayBase = new CubeRoom(room.getWidthX(), room.getHeight(), room.getWidthZ(), room.getX(), room.getY() - 15, room.getZ());
               stairwayBase.setRoomPopulator(new CatacombsStairwayBasePopulator(hashedRand, true, false));
               gen.getRooms().add(stairwayBase);
               ++stairways;
            } else if (room.getPop() instanceof CatacombsDripstoneCavern) {
               stairwayBase = new CubeRoom(room.getWidthX(), room.getHeight(), room.getWidthZ(), room.getX(), room.getY() - 15, room.getZ());
               stairwayBase.setRoomPopulator(new CatacombsDripstoneBasinPopulator(hashedRand, true, false));
               gen.getRooms().add(stairwayBase);
               ++stairways;
            }
         }

         gen.setRoomMaxX(10);
         gen.setRoomMaxZ(10);
         gen.setRoomMinX(7);
         gen.setRoomMinZ(7);
         gen.setRoomMinHeight(7);
         gen.setRoomMaxHeight(10);
         gen.registerRoomPopulator(new CatacombsStandardPopulator(random, false, false));
         gen.registerRoomPopulator(new CatacombsSkeletonDungeonPopulator(random, false, false));
         gen.registerRoomPopulator(new CatacombsPillarRoomPopulator(random, false, false));
         gen.registerRoomPopulator(new CatacombsCasketRoomPopulator(random, false, false));
         if (canGoDeeper) {
            gen.registerRoomPopulator(new CatacombsStairwayPopulator(random, true, false));
            gen.registerRoomPopulator(new CatacombsDripstoneCavern(random, true, false));
         }

         if (stairways <= 0) {
            break;
         }

         gen.roomCarver = new StandardRoomCarver(-1, Material.CAVE_AIR);
         gen.calculateRoomPlacement();
         ps = gen.getOrCalculatePathState(tw);
         ps.writer = new CavePathWriter(0.0F, 0.0F, 0.0F, 0, 2, 0);
         gen.calculateRoomPopulators(tw);
         state.roomPopulatorStates.add(gen);
      }

      return state;
   }

   private boolean canGoDeeper(@NotNull TerraformWorld tw, int y, @NotNull Random random) {
      return y > tw.minY + 10 && GenUtils.chance(random, (int)(TConfig.c.STRUCTURES_CATACOMBS_SIZEROLLCHANCE * 10000.0D), 10000);
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(91829209L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_CATACOMBS_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 0;
   }
}
