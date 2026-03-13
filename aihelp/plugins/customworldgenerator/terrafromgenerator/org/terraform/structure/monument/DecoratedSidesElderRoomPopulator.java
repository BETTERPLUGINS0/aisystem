package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;

public class DecoratedSidesElderRoomPopulator extends MonumentRoomPopulator {
   public DecoratedSidesElderRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 2).entrySet().iterator();

      Entry walls;
      Wall w;
      int length;
      int j;
      while(var3.hasNext()) {
         walls = (Entry)var3.next();
         w = ((Wall)walls.getKey()).getRelative(0, room.getHeight() - 2, 0);
         length = (Integer)walls.getValue();

         for(j = 0; j < length; ++j) {
            Stairs stair = (Stairs)Bukkit.createBlockData(this.design.stairs());
            stair.setFacing(w.getDirection().getOppositeFace());
            stair.setWaterlogged(true);
            stair.setHalf(Half.TOP);
            w.setBlockData(stair);
            w = w.getLeft();
         }
      }

      var3 = room.getFourWalls(data, 1).entrySet().iterator();

      while(var3.hasNext()) {
         walls = (Entry)var3.next();
         w = (Wall)walls.getKey();
         length = (Integer)walls.getValue();

         for(j = 0; j < length; ++j) {
            if (!w.getRear().isSolid()) {
               Wall wall = w.getUp(4);
               wall.LPillar(room.getHeight() - 4, true, this.rand, new Material[]{Material.SEA_LANTERN, Material.DARK_PRISMARINE});
            } else {
               if (j % 2 == 0) {
                  w.LPillar(room.getHeight() - 1, this.rand, new Material[]{Material.PRISMARINE_BRICKS});
               } else {
                  w.LPillar(room.getHeight() - 1, this.rand, new Material[]{Material.PRISMARINE});
                  w.getUp(3).Pillar(4, this.rand, new Material[]{Material.SEA_LANTERN});
               }

               w.setType(Material.DARK_PRISMARINE);
               w.getRelative(0, room.getHeight() - 2, 0).setType(Material.DARK_PRISMARINE);
            }

            w = w.getLeft();
         }
      }

      data.addEntity(room.getX(), room.getY() + room.getHeight() / 2, room.getZ(), EntityType.ELDER_GUARDIAN);
      Waterlogged wall = (Waterlogged)Bukkit.createBlockData(Material.PRISMARINE_WALL);
      wall.setWaterlogged(true);
      int[][] var10 = room.getAllCorners(2);
      int var11 = var10.length;

      for(length = 0; length < var11; ++length) {
         int[] corner = var10[length];
         data.setType(corner[0], room.getY() + room.getHeight() - 1, corner[1], Material.SEA_LANTERN);
         data.setBlockData(corner[0], room.getY() + room.getHeight() - 2, corner[1], wall);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 7;
   }
}
