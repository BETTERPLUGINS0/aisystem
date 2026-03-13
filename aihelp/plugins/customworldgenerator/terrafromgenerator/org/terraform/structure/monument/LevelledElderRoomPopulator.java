package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;

public class LevelledElderRoomPopulator extends LevelledRoomPopulator {
   public LevelledElderRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 12;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 1).entrySet().iterator();

      Entry walls;
      Wall w;
      int length;
      int j;
      while(var3.hasNext()) {
         walls = (Entry)var3.next();
         w = ((Wall)walls.getKey()).getUp(4);
         length = (Integer)walls.getValue();

         for(j = 0; j < length; ++j) {
            if (j % 2 == 0) {
               w.LPillar(room.getHeight() - 1, this.rand, new Material[]{Material.PRISMARINE_BRICKS});
            } else {
               w.LPillar(room.getHeight() - 1, this.rand, new Material[]{Material.PRISMARINE});
               w.getUp(3).Pillar(4, this.rand, new Material[]{Material.SEA_LANTERN});
            }

            w.setType(Material.DARK_PRISMARINE);
            w = w.getLeft();
         }
      }

      var3 = room.getFourWalls(data, 2).entrySet().iterator();

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

      data.addEntity(room.getX() + 3, room.getY() + 8, room.getZ() - 3, EntityType.ELDER_GUARDIAN);
   }
}
