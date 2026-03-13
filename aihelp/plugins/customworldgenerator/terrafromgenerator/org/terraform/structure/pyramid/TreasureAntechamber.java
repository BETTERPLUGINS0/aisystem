package org.terraform.structure.pyramid;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.type.Chest;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.GenUtils;

public class TreasureAntechamber extends Antechamber {
   public TreasureAntechamber(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 1).entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            if (i != 0 && i != (Integer)entry.getValue() - 1 && w.getRear().isSolid() && !w.isSolid() && GenUtils.chance(this.rand, 1, 4)) {
               Chest chest = (Chest)Bukkit.createBlockData(Material.CHEST);
               chest.setFacing(w.getDirection());
               w.setBlockData(chest);
               data.lootTableChest(w.getX(), w.getY(), w.getZ(), TerraLootTable.DESERT_PYRAMID);
            }

            w = w.getLeft();
         }
      }

   }
}
