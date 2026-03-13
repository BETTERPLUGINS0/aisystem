package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;

public class HollowPillarRoomPopulator extends CageRoomPopulator {
   public HollowPillarRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      CubeRoom cage = new CubeRoom(room.getWidthX() - 8, room.getWidthZ() - 8, room.getHeight() - 9, room.getX(), room.getY() + room.getHeight() / 2 - (room.getHeight() - 9) / 2, room.getZ());
      int[][] var4 = cage.getAllCorners();
      int var5 = var4.length;

      int var6;
      int[] corner;
      for(var6 = 0; var6 < var5; ++var6) {
         corner = var4[var6];
         Wall w = new Wall(new SimpleBlock(data, corner[0], room.getY() + 1, corner[1]), BlockFace.NORTH);
         w.LPillar(room.getHeight() - 1, this.rand, this.design.tileSet());
      }

      Iterator var10 = cage.getFourWalls(data, 0).entrySet().iterator();

      int x;
      while(var10.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var10.next();
         Wall w = (Wall)entry.getKey();
         int length = (Integer)entry.getValue();

         for(x = 0; x < length; ++x) {
            w.getDown().setType(this.design.mat(this.rand));
            w.getRelative(0, cage.getHeight() - 1, 0).setType(this.design.mat(this.rand));
            w = w.getLeft();
         }
      }

      var4 = cage.getAllCorners();
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         corner = var4[var6];
         x = corner[0];
         int z = corner[1];
         data.setType(x, cage.getY(), z, Material.SEA_LANTERN);
         data.setType(x, cage.getY() + cage.getHeight(), z, Material.SEA_LANTERN);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 13;
   }
}
