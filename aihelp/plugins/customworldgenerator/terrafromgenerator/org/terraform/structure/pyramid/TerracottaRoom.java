package org.terraform.structure.pyramid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;

public class TerracottaRoom extends RoomPopulatorAbstract {
   public TerracottaRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      ArrayList<Wall> entrances = new ArrayList();
      Iterator var4 = room.getFourWalls(data, 1).entrySet().iterator();

      int i;
      while(var4.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var4.next();
         Wall w = (Wall)entry.getKey();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i != 0 && i != (Integer)entry.getValue() - 1) {
               if (w.getRear().isSolid()) {
                  if (i != 1 && i != (Integer)entry.getValue() - 2) {
                     if (w.getRear().getLeft().isSolid() && w.getRear().getRight().isSolid()) {
                        if (i % 3 == 0) {
                           w.Pillar(room.getHeight(), true, this.rand, new Material[]{Material.BLUE_TERRACOTTA, Material.YELLOW_TERRACOTTA});
                        } else {
                           w.Pillar(room.getHeight(), true, this.rand, new Material[]{Material.YELLOW_TERRACOTTA, Material.BLUE_TERRACOTTA});
                        }
                     } else {
                        w.Pillar(room.getHeight(), this.rand, new Material[]{Material.CHISELED_SANDSTONE});
                     }
                  }
               } else {
                  entrances.add(w.clone());
                  w.getUp(3).Pillar(room.getHeight() - 3, this.rand, new Material[]{Material.CHISELED_SANDSTONE});
               }
            }

            w = w.getLeft();
         }
      }

      var4 = entrances.iterator();

      while(var4.hasNext()) {
         Wall w = (Wall)var4.next();
         w.Pillar(room.getHeight() - 1, this.rand, new Material[]{Material.AIR});
      }

      int[] lowerCorner = room.getLowerCorner(2);
      int[] upperCorner = room.getUpperCorner(2);

      for(int x = lowerCorner[0]; x <= upperCorner[0]; x += 2) {
         for(i = lowerCorner[1]; i <= upperCorner[1]; i += 2) {
            BlockUtils.horizontalGlazedTerracotta(data, x, room.getY(), i, Material.YELLOW_GLAZED_TERRACOTTA);
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() % 2 == 1 && room.getWidthZ() % 2 == 1;
   }
}
