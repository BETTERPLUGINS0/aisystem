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

public class MiniRoomNetworkPopulator extends MonumentRoomPopulator {
   public MiniRoomNetworkPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() <= 13;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 4).entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = (Wall)entry.getKey();
         int l = (Integer)entry.getValue();

         for(int i = 0; i < l + 4; ++i) {
            w.RPillar(room.getHeight() - 1, this.rand, this.design.tileSet());
            if (i % 2 == 0) {
               w.setType(Material.SEA_LANTERN);
            }

            if (i == l / 2) {
               w.getUp(2).setType(Material.WATER);
            }

            if (i == l + 2) {
               w.getUp(2).setType(Material.WATER);
            }

            w = w.getLeft();
         }
      }

      Wall center = new Wall(new SimpleBlock(data, room.getX(), room.getY(), room.getZ()), BlockFace.NORTH);
      this.tetrapod(center);
   }

   public void tetrapod(@NotNull Wall w) {
      for(int width = 0; width < 3; ++width) {
         if (width % 2 == 1) {
            w.getLeft(width).RPillar(5, this.rand, this.design.tileSet());
            w.getRight(width).RPillar(5, this.rand, this.design.tileSet());
         } else {
            w.getLeft(width).getUp(2).setType(this.design.mat(this.rand));
            w.getRight(width).getUp(2).setType(this.design.mat(this.rand));
         }
      }

      w.getLeft().getRear().getUp(2).setType(Material.SEA_LANTERN);
      w.getLeft().getFront().getUp(2).setType(Material.SEA_LANTERN);
      w.getRight().getRear().getUp(2).setType(Material.SEA_LANTERN);
      w.getRight().getFront().getUp(2).setType(Material.SEA_LANTERN);
   }
}
