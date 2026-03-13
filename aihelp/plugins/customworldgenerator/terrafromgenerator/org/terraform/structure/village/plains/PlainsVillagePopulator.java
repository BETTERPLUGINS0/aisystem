package org.terraform.structure.village.plains;

import java.util.Locale;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.village.VillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;

public class PlainsVillagePopulator extends VillagePopulator {
   @NotNull
   public Material woodSlab;
   @NotNull
   public Material woodPlank;
   @NotNull
   public Material woodLog;
   @NotNull
   public Material woodStrippedLog;
   @NotNull
   public Material woodFence;
   @NotNull
   public Material woodButton;
   @NotNull
   public Material woodTrapdoor;
   @NotNull
   public Material woodDoor;
   @NotNull
   public Material woodStairs;
   @NotNull
   public Material woodLeaves;
   @NotNull
   public Material woodPressurePlate;
   @NotNull
   public String wood;

   public PlainsVillagePopulator() {
      this.woodSlab = Material.OAK_SLAB;
      this.woodPlank = Material.OAK_PLANKS;
      this.woodLog = Material.OAK_LOG;
      this.woodStrippedLog = Material.STRIPPED_OAK_LOG;
      this.woodFence = Material.OAK_FENCE;
      this.woodButton = Material.OAK_BUTTON;
      this.woodTrapdoor = Material.OAK_TRAPDOOR;
      this.woodDoor = Material.OAK_DOOR;
      this.woodStairs = Material.OAK_STAIRS;
      this.woodLeaves = Material.OAK_LEAVES;
      this.woodPressurePlate = Material.OAK_PRESSURE_PLATE;
      this.wood = "oak_";
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
      int[] coords = mc.getCenterBiomeSectionBlockCoords();
      int x = coords[0];
      int z = coords[1];
      this.spawnPlainsVillage(tw, this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ()), data, x, 50, z);
   }

   private void ensureFarmHouseEntrance(@NotNull Random rand, @NotNull DirectionalCubeRoom room, @NotNull PopulatorDataAbstract data) {
      int frontSpaceGuarantee = 11;
      Wall w = (new Wall((new SimpleBlock(data, room.getX(), room.getY(), room.getZ())).getGround(), room.getDirection())).getUp(4);
      int elevation = GenUtils.randInt(rand, 2, 4);

      int max;
      for(max = 30; max > 0 && !this.isFrontSpaceClear(w, frontSpaceGuarantee); --max) {
         switch(rand.nextInt(3)) {
         case 0:
            w = w.getFront().getGround().getRelative(0, elevation, 0);
            break;
         case 1:
            w = new Wall(w.get(), BlockUtils.getTurnBlockFace(rand, w.getDirection()));
            break;
         case 2:
            elevation += 2;
         }
      }

      for(Wall temp = w.getGround().getUp(elevation); BlockUtils.isWet(temp) || BlockUtils.isWet(temp.getDown()); ++elevation) {
         temp = temp.getUp();
      }

      if (max == 0) {
         TerraformGeneratorPlugin.logger.info("Village at " + String.valueOf(w.get().toVector()) + " may have a weird spawn.");
      }

      room.setX(w.getX());
      room.setY(w.getY());
      room.setZ(w.getZ());
      room.setDirection(w.getDirection());
      ((PlainsVillageTownhallPopulator)room.getPop()).setElevation(elevation);
   }

   private boolean isFrontSpaceClear(@NotNull Wall w, int space) {
      for(int i = 0; i < space; ++i) {
         if (w.getFront(i).isSolid()) {
            return false;
         }
      }

      return true;
   }

   public void spawnPlainsVillage(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      BlockFace pathStart = BlockUtils.getDirectBlockFace(random);
      TerraformGeneratorPlugin.logger.info("Spawning plains village at " + x + "," + y + "," + z);
      DirectionalCubeRoom townHall = new DirectionalCubeRoom(pathStart, 24, 24, 24, x, y, z);
      PlainsVillageTownhallPopulator townHallPop = new PlainsVillageTownhallPopulator(tw, random, false, false);
      townHall.setRoomPopulator(townHallPop);
      this.ensureFarmHouseEntrance(random, townHall, data);
      pathStart = townHall.getDirection();
      BiomeBank biome = tw.getBiomeBank(townHall.getX(), townHall.getZ());
      this.woodSlab = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.SLAB);
      this.woodPlank = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.PLANKS);
      this.woodLog = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG);
      this.woodStairs = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.STAIRS);
      this.woodFence = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.FENCE);
      this.woodStrippedLog = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.STRIPPED_LOG);
      this.woodButton = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.BUTTON);
      this.woodTrapdoor = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.TRAPDOOR);
      this.woodPressurePlate = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.PRESSURE_PLATE);
      this.woodDoor = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.DOOR);
      this.woodLeaves = WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LEAVES);
      this.wood = this.woodLeaves.toString().toLowerCase(Locale.ENGLISH).replace("leaves", "");
      PlainsPathRecursiveSpawner spawner = new PlainsPathRecursiveSpawner(new SimpleBlock(data, townHall.getX() + pathStart.getModX() * 13, y, townHall.getZ() + pathStart.getModZ() * 13), 100, BlockUtils.getAdjacentFaces(pathStart));
      spawner.forceRegisterRoom(townHall);
      spawner.setVillageDensity(0.7D);
      spawner.setPathPop(new PlainsVillagePathPopulator(tw, spawner.getRooms().values(), random));
      spawner.registerRoomPopulator(new PlainsVillageStandardHousePopulator(this, random, false, false));
      spawner.registerRoomPopulator(new PlainsVillageTemplePopulator(this, random, false, true));
      spawner.registerRoomPopulator(new PlainsVillageForgePopulator(this, random, false, true));
      spawner.registerRoomPopulator(new PlainsVillageCropFarmPopulator(this, random, false, false));
      spawner.registerRoomPopulator(new PlainsVillageAnimalPenPopulator(this, random, false, false));
      spawner.registerRoomPopulator(new PlainsVillageWellPopulator(this, random, false, false));
      spawner.registerRoomPopulator(new PlainsVillageFountainPopulator(this, random, false, true));
      spawner.registerRoomPopulator(new PlainsVillagePondPopulator(random, false, false));
      spawner.generate(random);
      spawner.build(random);
   }
}
