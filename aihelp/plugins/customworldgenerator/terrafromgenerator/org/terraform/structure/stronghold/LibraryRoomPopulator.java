package org.terraform.structure.stronghold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class LibraryRoomPopulator extends RoomPopulatorAbstract {
   public LibraryRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] upperBounds = room.getUpperCorner();
      int[] lowerBounds = room.getLowerCorner();
      HashMap<Wall, Integer> walls = room.getFourWalls(data, 1);
      Iterator var6 = walls.entrySet().iterator();

      Wall wall;
      int l;
      Wall pWall;
      int i;
      while(var6.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var6.next();
         wall = ((Wall)entry.getKey()).clone();
         int other = 0;

         for(l = 0; l < (Integer)entry.getValue(); ++l) {
            if (wall.getRear().get().getType().toString().endsWith("STONE_BRICKS")) {
               if (other <= 2) {
                  wall.LPillar(room.getHeight(), this.rand, new Material[]{Material.BOOKSHELF});
                  ++other;
               } else {
                  other = 0;
                  wall.LPillar(room.getHeight(), this.rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
                  wall.getFront().LPillar(room.getHeight(), this.rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
               }
            } else {
               pWall = wall.getUp();

               for(i = 0; i < room.getHeight(); ++i) {
                  pWall = pWall.getUp();
                  if (pWall.getRear().get().getType().toString().endsWith("STONE_BRICKS")) {
                     break;
                  }
               }

               pWall.setType(Material.CHISELED_STONE_BRICKS);
               pWall.getUp().LPillar(room.getHeight(), this.rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            }

            wall = wall.getLeft();
         }
      }

      int pHeight = room.getHeight() / 2;
      Iterator var18 = walls.entrySet().iterator();

      Entry entry;
      Wall wall;
      SimpleBlock core;
      while(var18.hasNext()) {
         entry = (Entry)var18.next();
         wall = ((Wall)entry.getKey()).clone();

         for(l = 0; l < (Integer)entry.getValue(); ++l) {
            pWall = wall.getFront().getRelative(0, pHeight - 1, 0);
            if (pWall.get().getType().toString().contains("COBBLE")) {
               for(i = 0; i < 3; ++i) {
                  pWall = pWall.getFront();
                  core = pWall.get();
                  if (core.lsetType(Material.OAK_LOG)) {
                     Orientable o = (Orientable)Bukkit.createBlockData(Material.OAK_LOG);
                     o.setAxis(BlockUtils.getAxisFromBlockFace(wall.getDirection()));
                     core.setBlockData(o);
                  }
               }
            } else {
               if (GenUtils.chance(this.rand, 5, 100)) {
                  SimpleBlock cBlock = pWall.getUp().get();
                  cBlock.setType(Material.CHEST);
                  Chest chest = (Chest)Bukkit.createBlockData(Material.CHEST);
                  chest.setFacing(pWall.getDirection());
                  cBlock.setBlockData(chest);
                  data.lootTableChest(cBlock.getX(), cBlock.getY(), cBlock.getZ(), TerraLootTable.STRONGHOLD_LIBRARY);
               }

               for(i = 0; i < 3; ++i) {
                  core = pWall.get();
                  if (!core.lsetType(Material.OAK_SLAB)) {
                     pWall = pWall.getFront();
                  } else {
                     Slab s = (Slab)Bukkit.createBlockData(Material.OAK_SLAB);
                     s.setType(Type.TOP);
                     core.setBlockData(s);
                     pWall = pWall.getFront();
                  }
               }
            }

            wall = wall.getLeft();
         }
      }

      walls.clear();
      walls = room.getFourWalls(data, 4);
      var18 = walls.entrySet().iterator();

      while(var18.hasNext()) {
         entry = (Entry)var18.next();
         wall = ((Wall)entry.getKey()).clone().getRelative(0, pHeight, 0);

         for(l = 0; l < (Integer)entry.getValue(); ++l) {
            wall.setType(Material.OAK_FENCE);
            BlockUtils.correctSurroundingMultifacingData(wall.get());
            if (GenUtils.chance(this.rand, 1, 10)) {
               wall.getUp().setType(Material.TORCH);
            }

            wall = wall.getLeft();
         }
      }

      ArrayList<Wall> stairWalls = new ArrayList();
      wall = new Wall(new SimpleBlock(data, lowerBounds[0] + 5, room.getY() + pHeight, upperBounds[1] - 5), BlockFace.NORTH);
      wall = new Wall(new SimpleBlock(data, upperBounds[0] - 5, room.getY() + pHeight, lowerBounds[1] + 5), BlockFace.SOUTH);
      Wall stairWallThree = new Wall(new SimpleBlock(data, lowerBounds[0] + 5, room.getY() + pHeight, lowerBounds[1] + 5), BlockFace.EAST);
      pWall = new Wall(new SimpleBlock(data, upperBounds[0] - 5, room.getY() + pHeight, upperBounds[1] - 5), BlockFace.WEST);
      stairWalls.add(wall);
      stairWalls.add(wall);
      stairWalls.add(stairWallThree);
      stairWalls.add(pWall);
      Collections.shuffle(stairWalls, this.rand);
      i = GenUtils.randInt(this.rand, 1, 4);

      for(int s = 0; s < i; ++s) {
         Wall stairWall = (Wall)stairWalls.get(s);
         stairWall.getRight().getUp().setType(Material.AIR);
         stairWall.getFront().getRight().getUp().setType(Material.AIR);

         while(stairWall.get().getY() > room.getY()) {
            stairWall.setType(Material.OAK_STAIRS);
            Stairs d = (Stairs)Bukkit.createBlockData(Material.OAK_STAIRS);
            d.setFacing(BlockUtils.getAdjacentFaces(stairWall.getDirection())[1]);
            stairWall.get().setBlockData(d);
            stairWall.getFront().setType(Material.OAK_STAIRS);
            stairWall.getFront().get().setBlockData(d);
            stairWall = stairWall.getLeft().getDown();
         }
      }

      core = new SimpleBlock(data, room.getX(), room.getY() + 1, room.getZ());

      for(int nx = -1; nx <= 1; ++nx) {
         for(int nz = -1; nz <= 1; ++nz) {
            for(int ny = 0; ny < room.getHeight() - 1; ++ny) {
               if (ny != pHeight && ny != 0 && ny != room.getHeight() - 2) {
                  core.getRelative(nx, ny, nz).setType(Material.IRON_BARS);
                  BlockUtils.correctSurroundingMultifacingData(core.getRelative(nx, ny, nz));
               } else {
                  core.getRelative(nx, ny, nz).setType(Material.CHISELED_STONE_BRICKS);
               }
            }
         }
      }

      BlockUtils.spawnPillar(this.rand, data, room.getX(), room.getY() + 1, room.getZ(), Material.BOOKSHELF, room.getHeight() - 2, room.getHeight() - 2);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.isHuge();
   }
}
