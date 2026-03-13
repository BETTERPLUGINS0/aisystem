package org.terraform.structure.pyramid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class CursedChamber extends RoomPopulatorAbstract {
   public CursedChamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(3);
      int[] upperCorner = room.getUpperCorner(3);
      Iterator var5 = room.getFourWalls(data, 1).entrySet().iterator();

      int nx;
      while(var5.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var5.next();
         Wall w = (Wall)entry.getKey();

         for(nx = 0; nx < (Integer)entry.getValue(); ++nx) {
            for(int h = 0; h < room.getHeight() - 2; ++h) {
               if (w.getRelative(0, h, 0).getRear().isSolid() && GenUtils.chance(this.rand, 1, 5)) {
                  Directional head = (Directional)Bukkit.createBlockData(Material.SKELETON_WALL_SKULL);
                  head.setFacing(w.getDirection());
                  w.getRelative(0, h, 0).setBlockData(head);
               }
            }

            w = w.getLeft();
         }
      }

      ArrayList<Integer> availableX = new ArrayList();
      ArrayList<Integer> availableZ = new ArrayList();
      if (room.getWidthX() > 10) {
         availableX.add(lowerCorner[0]);
         availableX.add(upperCorner[0]);
      } else {
         availableX.add(room.getX());
      }

      if (room.getWidthZ() > 10) {
         availableZ.add(lowerCorner[1]);
         availableZ.add(upperCorner[1]);
      } else {
         availableZ.add(room.getZ());
      }

      Iterator var13 = availableX.iterator();

      while(var13.hasNext()) {
         nx = (Integer)var13.next();
         Iterator var16 = availableZ.iterator();

         while(var16.hasNext()) {
            int nz = (Integer)var16.next();
            this.spawnSkullPillar(new Wall(new SimpleBlock(data, nx, room.getY() + 1, nz)), room);
         }
      }

      int i;
      for(i = 0; i < GenUtils.randInt(3, 10); ++i) {
         int[] loc = room.randomCoords(this.rand, 1);
         if (data.getType(loc[0], room.getY() + room.getHeight() + 1, loc[2]) == Material.SAND) {
            data.setType(loc[0], room.getY() + room.getHeight() + 1, loc[2], Material.SANDSTONE);
         }

         BlockUtils.dropDownBlock(new SimpleBlock(data, loc[0], room.getY() + room.getHeight(), loc[2]));
      }

      for(i = 0; i < GenUtils.randInt(this.rand, 1, 4); ++i) {
         data.addEntity(room.getX() - room.getWidthX() / 2 + 1, room.getY() + 1, room.getZ(), EntityType.SKELETON);
      }

   }

   public void spawnSkullPillar(@NotNull Wall w, @NotNull CubeRoom room) {
      w.LPillar(room.getHeight() - 1, this.rand, new Material[]{Material.SANDSTONE, Material.CHISELED_SANDSTONE});
      BlockFace[] var3 = BlockUtils.directBlockFaces;
      int nx = var3.length;

      for(int var5 = 0; var5 < nx; ++var5) {
         BlockFace face = var3[var5];
         Stairs stair = (Stairs)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])(Material.SANDSTONE_STAIRS, Material.STONE_STAIRS, Material.COBBLESTONE_STAIRS)));
         stair.setFacing(face.getOppositeFace());
         w.getRelative(face).setBlockData(stair);
         stair = (Stairs)Bukkit.createBlockData((Material)GenUtils.randChoice((Object[])(Material.SANDSTONE_STAIRS, Material.STONE_STAIRS, Material.COBBLESTONE_STAIRS)));
         stair.setFacing(face.getOppositeFace());
         stair.setHalf(Half.TOP);
         w.getRelative(face).getRelative(0, room.getHeight() - 2, 0).setBlockData(stair);
      }

      int nz;
      for(nz = 1; nz < room.getHeight() - 3; ++nz) {
         Wall target = w.getRelative(0, nz, 0);
         BlockFace[] var12 = BlockUtils.directBlockFaces;
         int var13 = var12.length;

         for(int var14 = 0; var14 < var13; ++var14) {
            BlockFace face = var12[var14];
            if (!GenUtils.chance(4, 5)) {
               Directional head = (Directional)Bukkit.createBlockData(Material.SKELETON_WALL_SKULL);
               head.setFacing(face);
               target.getRelative(face).setBlockData(head);
            }
         }
      }

      for(nz = -2; nz <= 2; ++nz) {
         for(nx = -2; nx <= 2; ++nx) {
            w.getRelative(nx, room.getHeight() - 1, nz).setType((Material)GenUtils.randChoice((Object[])(Material.ANDESITE, Material.COBBLESTONE, Material.SANDSTONE)));
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() > 6 && room.getWidthZ() > 6;
   }
}
