package org.terraform.structure.villagehouse.animalfarm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeClimate;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomLayout;
import org.terraform.structure.room.RoomLayoutGenerator;
import org.terraform.structure.villagehouse.VillageHousePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.WoodUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.version.V_1_20;
import org.terraform.utils.version.Version;

public class AnimalFarmPopulator extends VillageHousePopulator {
   private static final EntityType[] farmAnimals;

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(425332L, chunkX, chunkZ);
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
      int[] coords = mc.getCenterBiomeSectionBlockCoords();
      int x = coords[0];
      int z = coords[1];
      int height = GenUtils.getHighestGround(data, x, z);
      this.spawnAnimalFarm(tw, data, x, height + 1, z);
   }

   public void spawnAnimalFarm(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      try {
         Random random = this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ());
         BiomeBank biome = tw.getBiomeBank(x, z);
         BlockFace dir = BlockUtils.getDirectBlockFace(random);
         TerraSchematic animalFarm = TerraSchematic.load("animalfarm", new SimpleBlock(data, x, y, z));
         animalFarm.parser = new AnimalFarmSchematicParser(biome, random, data);
         animalFarm.setFace(dir);
         animalFarm.apply();
         TerraformGeneratorPlugin.logger.info("Spawning animal farm at " + x + "," + y + "," + z + " with rotation of " + String.valueOf(animalFarm.getFace()));
         data.addEntity(x, y + 1, z, EntityType.VILLAGER);
         data.addEntity(x, y + 1, z, EntityType.VILLAGER);
         data.addEntity(x, y + 1, z, EntityType.CAT);
         int nx;
         int nz;
         if (dir != BlockFace.EAST && dir != BlockFace.WEST) {
            for(nx = -10; nx <= 10; ++nx) {
               for(nz = -5; nz <= 5; ++nz) {
                  if (data.getType(x + nx, y - 1, z + nz).isSolid()) {
                     BlockUtils.setDownUntilSolid(x + nx, y - 2, z + nz, data, WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG));
                  }
               }
            }
         } else {
            for(nx = -5; nx <= 5; ++nx) {
               for(nz = -10; nz <= 10; ++nz) {
                  if (data.getType(x + nx, y - 1, z + nz).isSolid()) {
                     BlockUtils.setDownUntilSolid(x + nx, y - 2, z + nz, data, WoodUtils.getWoodForBiome(biome, WoodUtils.WoodType.LOG));
                  }
               }
            }
         }

         this.createSurroundingFences(tw, biome, random, data, x, y, z);
      } catch (Throwable var12) {
         TerraformGeneratorPlugin.logger.error("Something went wrong trying to place farmhouse at " + x + "," + y + "," + z + "!");
         TerraformGeneratorPlugin.logger.stackTrace(var12);
      }

   }

   private void createSurroundingFences(@NotNull TerraformWorld tw, @NotNull BiomeBank biome, @NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      RoomLayoutGenerator gen = new RoomLayoutGenerator(random, RoomLayout.RANDOM_BRUTEFORCE, 50, x, y, z, 75);
      gen.setPathPopulator(new AnimalFarmPathPopulator(gen, tw.getHashedRand(x, y, z, 1234L)));
      gen.setRoomMaxX(17);
      gen.setRoomMaxZ(17);
      gen.setRoomMaxHeight(1);
      gen.getRooms().add(new CubeRoom(20, 20, 30, x, y, z));
      gen.calculateRoomPlacement();
      FastNoise fieldNoise = new FastNoise(tw.getHashedRand(x, y, z, 23L).nextInt(225));
      fieldNoise.SetNoiseType(FastNoise.NoiseType.Simplex);
      fieldNoise.SetFrequency(0.05F);
      FastNoise radiusNoise = new FastNoise(tw.getHashedRand(x, y, z, 23L).nextInt(225));
      radiusNoise.SetNoiseType(FastNoise.NoiseType.Cubic);
      radiusNoise.SetFrequency(0.09F);

      int nx;
      for(nx = -50; nx <= 50; ++nx) {
         for(int nz = -50; nz <= 50; ++nz) {
            int height = GenUtils.getHighestGround(data, x + nx, z + nz);
            if (BlockUtils.isDirtLike(data.getType(x + nx, height, z + nz)) && data.getType(x + nx, height + 1, z + nz) == Material.AIR) {
               double noise = (double)fieldNoise.GetNoise((float)(nx + x), (float)(nz + z));
               double dist = Math.pow((double)nx, 2.0D) + Math.pow((double)nz, 2.0D);
               double multiplier = Math.pow(1.0D / (dist - 2500.0D) + 1.0D, 255.0D);
               if (multiplier < 0.0D || dist > 2500.0D + (double)radiusNoise.GetNoise((float)nx, (float)nz) * 500.0D) {
                  multiplier = 0.0D;
               }

               noise = Math.abs(noise * multiplier);
               if (GenUtils.chance(random, 2500.0D - dist > 0.0D ? (int)(2500.0D - dist) : 0, 2500)) {
                  if (0.1D < noise && noise < 0.2D) {
                     data.setType(nx + x, height, nz + z, (Material)GenUtils.randChoice(random, Material.CHISELED_STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS));
                  } else if (noise <= 0.1D && GenUtils.chance(random, (int)(100.0D * Math.pow(multiplier, 3.0D)), 100)) {
                     data.setType(nx + x, height, nz + z, (Material)GenUtils.randChoice(random, Material.COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB));
                  }
               }
            }
         }
      }

      nx = 0;
      Iterator var22 = gen.getRooms().iterator();

      while(true) {
         CubeRoom room;
         do {
            do {
               if (!var22.hasNext()) {
                  return;
               }

               room = (CubeRoom)var22.next();
            } while(room.getWidthX() == 20 && room.getWidthZ() == 20);
         } while(GenUtils.getHighestGround(data, room.getX(), room.getZ()) < TerraformGenerator.seaLevel);

         int animalCount;
         int nx;
         for(int t = 0; t <= 360; t += 5) {
            animalCount = room.getX() + (int)((double)room.getWidthX() / 2.0D * Math.cos(Math.toRadians((double)t)));
            int ePZ = room.getZ() + (int)((double)room.getWidthZ() / 2.0D * Math.sin(Math.toRadians((double)t)));
            nx = GenUtils.getHighestGround(data, animalCount, ePZ);
            data.setType(animalCount, nx + 1, ePZ, Material.SPRUCE_FENCE);
            if (GenUtils.chance(random, 1, 30)) {
               data.setType(animalCount, nx + 1, ePZ, Material.CHISELED_STONE_BRICKS);
               data.setType(animalCount, nx + 2, ePZ, (Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL)));
               data.setType(animalCount, nx + 3, ePZ, (Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL)));
               data.setType(animalCount, nx + 4, ePZ, (Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL)));
               data.setType(animalCount, nx + 5, ePZ, Material.CAMPFIRE);
            }

            BlockUtils.correctSurroundingMultifacingData(new SimpleBlock(data, animalCount, nx + 1, ePZ));
         }

         HashMap<Wall, Integer> walls = room.getFourWalls(data, 0);
         Iterator var26 = walls.entrySet().iterator();

         int highest;
         int nz;
         while(var26.hasNext()) {
            Entry<Wall, Integer> entry = (Entry)var26.next();
            Wall w = (Wall)entry.getKey();
            nz = (Integer)entry.getValue();

            for(highest = 0; highest < nz; ++highest) {
               w = w.getLeft();
               if (GenUtils.chance(random, 1, 50)) {
                  SimpleBlock rear = w.getRear().get();
                  int highest = GenUtils.getHighestGround(data, rear.getX(), rear.getZ());
                  data.setType(rear.getX(), highest + 1, rear.getZ(), (Material)GenUtils.randChoice((Object[])(Material.CAULDRON, Material.SMOKER, Material.LOOM)));
               }
            }
         }

         animalCount = GenUtils.randInt(3, 7);
         EntityType animal = farmAnimals[random.nextInt(farmAnimals.length)];
         if (Version.VERSION.isAtLeast(Version.v1_20) && nx == 0 && biome.getClimate() == BiomeClimate.HOT_BARREN) {
            animal = V_1_20.CAMEL;
            animalCount = 2;
         }

         for(nx = 0; nx < animalCount; ++nx) {
            int[] coords = room.randomCoords(random, 2);
            highest = GenUtils.getHighestGround(data, coords[0], coords[2]);
            data.addEntity(coords[0], highest + 1, coords[2], animal);
         }

         for(nx = room.getLowerCorner()[0] + 2; nx <= room.getUpperCorner()[0] - 2; ++nx) {
            for(nz = room.getLowerCorner()[1] + 2; nz <= room.getUpperCorner()[1] - 2; ++nz) {
               highest = GenUtils.getHighestGround(data, nx, nz);
               if (data.getType(nx, highest, nz) == Material.CHISELED_STONE_BRICKS) {
                  --highest;
               }

               if (Math.pow((double)((float)(nx - room.getX()) / ((float)room.getWidthX() / 2.0F)), 2.0D) + Math.pow((double)((float)(nz - room.getZ()) / ((float)room.getWidthZ() / 2.0F)), 2.0D) <= 1.0D) {
                  data.setType(nx, highest, nz, (Material)GenUtils.randChoice(random, Material.GRASS_BLOCK, Material.PODZOL, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.GRASS_BLOCK, Material.DIRT_PATH, Material.GRASS_BLOCK, Material.COARSE_DIRT));
               }

               if (GenUtils.chance(random, 1, 60)) {
                  BlockUtils.replaceUpperSphere(nx + 7 * nz + 289, 1.1F, 2.0F, 1.1F, new SimpleBlock(data, nx, highest + 1, nz), false, Material.HAY_BLOCK);
               }
            }
         }

         ++nx;
      }
   }

   static {
      farmAnimals = new EntityType[]{EntityType.PIG, EntityType.SHEEP, EntityType.COW, EntityType.HORSE, EntityType.CHICKEN};
   }
}
