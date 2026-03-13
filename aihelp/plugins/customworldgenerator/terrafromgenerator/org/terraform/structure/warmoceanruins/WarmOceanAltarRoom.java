package org.terraform.structure.warmoceanruins;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;

public class WarmOceanAltarRoom extends WarmOceanBaseRoom {
   public WarmOceanAltarRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      Iterator var3 = room.getFourWalls(data, 3).entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = ((Wall)entry.getKey()).getGround();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            int h;
            if (i % 2 == 0 && this.rand.nextBoolean()) {
               h = 2 + this.rand.nextInt(3);
               w.getUp().Pillar(h, new Material[]{Material.CUT_SANDSTONE});
               w.getUp(h + 1).setType(new Material[]{Material.POLISHED_DIORITE, Material.POLISHED_ANDESITE, Material.POLISHED_GRANITE});
            } else if (i % 2 == 1 && this.rand.nextBoolean()) {
               h = 2 + this.rand.nextInt(3);
               w.getUp().setType(new Material[]{Material.CUT_SANDSTONE_SLAB, Material.SMOOTH_SANDSTONE});
            }

            if (i > 0 && i < (Integer)entry.getValue() - 1 && GenUtils.chance(this.rand, 1, 9)) {
               (new ChestBuilder(Material.CHEST)).setFacing(w.getDirection()).setLootTable(TerraLootTable.UNDERWATER_RUIN_SMALL).setWaterlogged(w.getUp().getY() <= TConfig.c.HEIGHT_MAP_SEA_LEVEL).apply(w.getFront().getRight().getUp());
            }

            w = w.getLeft().getGround();
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() < 25;
   }
}
