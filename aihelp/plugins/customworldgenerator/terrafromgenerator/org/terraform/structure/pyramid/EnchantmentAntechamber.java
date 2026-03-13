package org.terraform.structure.pyramid;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;

public class EnchantmentAntechamber extends Antechamber {
   public EnchantmentAntechamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      data.setType(room.getX(), room.getY() + 1, room.getZ(), Material.ENCHANTING_TABLE);
      SimpleBlock core = new SimpleBlock(data, room.getX(), room.getY() + 1, room.getZ());
      Map<Wall, Integer> tableWalls = Map.of(new Wall(core.getRelative(2, 0, -2), BlockFace.SOUTH), 5, new Wall(core.getRelative(-2, 0, 2), BlockFace.NORTH), 5, new Wall(core.getRelative(2, 0, 2), BlockFace.WEST), 5, new Wall(core.getRelative(-2, 0, -2), BlockFace.EAST), 5);
      Iterator var5 = tableWalls.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var5.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i % 2 == 0) {
               int h = 1;
               if (i == 2) {
                  h = 2;
               }

               w.LPillar(h, this.rand, new Material[]{Material.BOOKSHELF});
               w.getRelative(0, room.getHeight() - 2, 0).downLPillar(this.rand, h, new Material[]{Material.BOOKSHELF});
               w.RPillar(room.getHeight(), this.rand, new Material[]{Material.SANDSTONE_WALL});
            } else {
               Directional decor = (Directional)Bukkit.createBlockData(Material.LECTERN);
               decor.setFacing(w.getDirection());
               w.setBlockData(decor);
            }

            w = w.getLeft();
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() > 7 && room.getWidthZ() > 7;
   }
}
