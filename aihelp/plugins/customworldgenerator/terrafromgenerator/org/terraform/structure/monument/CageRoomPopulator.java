package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.GenUtils;

public class CageRoomPopulator extends MonumentRoomPopulator {
   public CageRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 9;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      if (!GenUtils.chance(this.rand, 3, 5)) {
         Iterator var3 = room.getFourWalls(data, 0).entrySet().iterator();

         int i;
         while(var3.hasNext()) {
            Entry<Wall, Integer> entry = (Entry)var3.next();
            Wall w = ((Wall)entry.getKey()).getUp(7);
            int length = (Integer)entry.getValue();

            for(i = 0; i < length; ++i) {
               if (i % 2 == 0) {
                  Waterlogged wall = (Waterlogged)Bukkit.createBlockData(Material.PRISMARINE_WALL);
                  wall.setWaterlogged(w.get().getY() <= TerraformGenerator.seaLevel);

                  for(int j = 0; j < room.getHeight() - 9; ++j) {
                     w.getRelative(0, j, 0).setBlockData(wall);
                  }
               } else {
                  w.Pillar(room.getHeight() - 9, this.rand, new Material[]{Material.DARK_PRISMARINE_SLAB, Material.PRISMARINE_SLAB, Material.PRISMARINE_BRICK_SLAB});
                  Stairs s = (Stairs)Bukkit.createBlockData(this.design.stairs());
                  s.setFacing(w.getDirection());
                  s.setWaterlogged(w.get().getY() <= TerraformGenerator.seaLevel);
                  w.setBlockData(s);
                  s = (Stairs)s.clone();
                  s.setHalf(Half.TOP);
                  w.getRelative(0, room.getHeight() - 9, 0).setBlockData(s);
               }

               w = w.getLeft();
            }
         }

         int[][] var10 = room.getAllCorners();
         int var11 = var10.length;

         for(int var12 = 0; var12 < var11; ++var12) {
            int[] corner = var10[var12];

            for(i = 0; i < room.getHeight(); ++i) {
               if (data.getType(corner[0], i + room.getY(), corner[1]).isSolid()) {
                  data.setType(corner[0], i + room.getY(), corner[1], Material.DARK_PRISMARINE);
               }
            }
         }

      }
   }
}
