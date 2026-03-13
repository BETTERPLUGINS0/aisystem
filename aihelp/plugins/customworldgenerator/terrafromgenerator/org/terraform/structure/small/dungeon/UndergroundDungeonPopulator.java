package org.terraform.structure.small.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class UndergroundDungeonPopulator extends SmallDungeonPopulator {
   private static void dropDownBlock(@NotNull SimpleBlock block, @NotNull Material fluid) {
      if (block.isSolid()) {
         Material type = block.getType();
         block.setType(fluid);
         int depth = 0;

         while(!block.isSolid()) {
            block = block.getDown();
            ++depth;
            if (depth > 50) {
               return;
            }
         }

         block.getUp().setType(type);
      }

   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
      int[] spawnCoords = new int[]{data.getChunkX() * 16, data.getChunkZ() * 16};
      int[][] allCoords = this.getCoordsFromMegaChunk(tw, mc);
      int[][] var6 = allCoords;
      int z = allCoords.length;

      for(int var8 = 0; var8 < z; ++var8) {
         int[] coords = var6[var8];
         if (coords[0] >> 4 == data.getChunkX() && coords[1] >> 4 == data.getChunkZ()) {
            spawnCoords = coords;
            break;
         }
      }

      int x = spawnCoords[0];
      z = spawnCoords[1];
      Random rand = this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ());
      int y = HeightMap.getBlockHeight(tw, x, z) - GenUtils.randInt(rand, 15, 50);
      if (y < 10) {
         y = 10;
      }

      while(!data.getType(x, y, z).isSolid()) {
         --y;
      }

      this.spawnDungeonRoom(x, y, z, tw, rand, data);
   }

   public void spawnDungeonRoom(int x, int y, int z, TerraformWorld tw, @NotNull Random rand, @NotNull PopulatorDataAbstract data) {
      TerraformGeneratorPlugin.logger.info("Spawning Underground Dungeon at " + x + "," + y + "," + z);
      CubeRoom room = new CubeRoom(GenUtils.randOddInt(rand, 9, 15), GenUtils.randOddInt(rand, 9, 15), GenUtils.randInt(rand, 5, 7), x, y, z);
      boolean isWet = false;
      Material fluid = Material.CAVE_AIR;
      SimpleBlock center = room.getCenterSimpleBlock(data);
      if (BlockUtils.isWet(center.getUp())) {
         fluid = Material.WATER;
         isWet = true;
      }

      room.fillRoom(data, -1, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE}, fluid);
      Iterator var11 = room.getFourWalls(data, 0).entrySet().iterator();

      int i;
      int h;
      while(var11.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var11.next();
         Wall w = ((Wall)entry.getKey()).getUp();

         for(i = (Integer)entry.getValue(); i >= 0; w = w.getLeft()) {
            if (i % 2 != 0 && i != (Integer)entry.getValue()) {
               w.CAPillar(room.getHeight() - 3, rand, new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
               if (isWet) {
                  w.waterlog(room.getHeight() - 3);
               }
            }

            for(h = 0; h < room.getHeight() - 3; ++h) {
               BlockUtils.correctSurroundingMultifacingData(w.getRelative(0, h, 0).get());
            }

            --i;
         }
      }

      int nx;
      int i;
      for(nx = 0; nx < GenUtils.randInt(rand, 0, 3); ++nx) {
         int[] coords = room.randomCoords(rand);
         i = coords[0];
         i = coords[1];
         h = coords[2];
         BlockUtils.replaceSphere(rand.nextInt(992), (float)GenUtils.randInt(rand, 1, 3), new SimpleBlock(data, i, i, h), true, fluid);
      }

      int nz;
      for(nx = -room.getWidthX() / 2; nx < room.getWidthX() / 2; ++nx) {
         for(nz = -room.getWidthZ() / 2; nz < room.getWidthZ() / 2; ++nz) {
            i = room.getHeight();
            if (!GenUtils.chance(10, 13)) {
               dropDownBlock(new SimpleBlock(data, x + nx, y + i, z + nz), fluid);
            }
         }
      }

      for(nx = -room.getWidthX() / 2; nx < room.getWidthX() / 2; ++nx) {
         for(nz = -room.getWidthZ() / 2; nz < room.getWidthZ() / 2; ++nz) {
            i = room.getHeight() - 1;
            if (!GenUtils.chance(9, 10)) {
               for(i = 0; i < GenUtils.randInt(rand, 1, room.getHeight() - 3); ++i) {
                  data.setType(x + nx, y + i, z + nz, (Material)GenUtils.randChoice((Object[])(Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL)));
                  BlockUtils.correctSurroundingMultifacingData(new SimpleBlock(data, x + nx, y + i, z + nz));
               }
            }
         }
      }

      for(nx = -room.getWidthX() / 2; nx < room.getWidthX() / 2; ++nx) {
         for(nz = -room.getWidthZ() / 2; nz < room.getWidthZ() / 2; ++nz) {
            if (!GenUtils.chance(9, 10)) {
               for(i = 0; i < GenUtils.randInt(rand, 1, room.getHeight() - 3); ++i) {
                  Wall w = new Wall(new SimpleBlock(data, x + nx, y + 1, z + nz), BlockFace.NORTH);
                  w.LPillar(room.getHeight() - 2, rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
                  BlockUtils.correctSurroundingMultifacingData(w.get());
               }
            }
         }
      }

      EntityType var10000;
      switch(rand.nextInt(3)) {
      case 0:
         var10000 = EntityType.ZOMBIE;
         break;
      case 1:
         var10000 = EntityType.SKELETON;
         break;
      case 2:
         var10000 = EntityType.SPIDER;
         break;
      default:
         var10000 = null;
      }

      EntityType type = var10000;
      if (isWet) {
         type = EntityType.DROWNED;
      }

      data.setSpawner(x, y + 1, z, type);
      ArrayList<Entry<Wall, Integer>> entries = new ArrayList();
      HashMap<Wall, Integer> walls = room.getFourWalls(data, 1);
      Iterator var28 = walls.entrySet().iterator();

      Entry entry;
      while(var28.hasNext()) {
         entry = (Entry)var28.next();
         if (rand.nextBoolean()) {
            entries.add(entry);
         }
      }

      var28 = entries.iterator();

      while(var28.hasNext()) {
         entry = (Entry)var28.next();
         Wall w = (Wall)entry.getKey();
         int length = (Integer)entry.getValue();

         for(int chest = GenUtils.randInt(1, length - 1); length >= 0; w = w.getLeft()) {
            if (length == chest) {
               Directional dir = (Directional)Bukkit.createBlockData(Material.CHEST);
               dir.setFacing(w.getDirection());
               if (isWet && dir instanceof Waterlogged) {
                  ((Waterlogged)dir).setWaterlogged(true);
               }

               w.setBlockData(dir);
               data.lootTableChest(w.get().getX(), w.get().getY(), w.get().getZ(), TerraLootTable.SIMPLE_DUNGEON);
            }

            --length;
         }
      }

   }
}
