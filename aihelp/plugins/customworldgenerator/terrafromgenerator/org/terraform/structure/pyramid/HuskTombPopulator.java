package org.terraform.structure.pyramid;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class HuskTombPopulator extends RoomPopulatorAbstract {
   public HuskTombPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Iterator var3 = room.getFourWalls(data, 0).entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = (Wall)entry.getKey();

         for(int i = 0; i < (Integer)entry.getValue(); ++i) {
            if (w.isSolid()) {
               if (w.getType().toString().contains("SAND")) {
                  w.Pillar(room.getHeight() - 1, this.rand, new Material[]{Material.SANDSTONE, Material.SANDSTONE_SLAB, Material.CUT_SANDSTONE, Material.CHISELED_SANDSTONE});
               } else {
                  w.Pillar(room.getHeight() - 1, this.rand, new Material[]{Material.ANDESITE, Material.ANDESITE, Material.ANDESITE, Material.ANDESITE_SLAB, Material.STONE_BRICKS, Material.STONE_BRICK_SLAB, Material.CRACKED_STONE_BRICKS, Material.STONE_BRICKS});
               }

               if (GenUtils.chance(this.rand, 1, 50) && i != 0 && i != (Integer)entry.getValue() - 1) {
                  Directional chest = (Directional)Bukkit.createBlockData(Material.CHEST);
                  chest.setFacing(w.getDirection());
                  w.getFront().setBlockData(chest);
                  data.lootTableChest(w.getFront().getX(), w.getFront().getY(), w.getFront().getZ(), TerraLootTable.SIMPLE_DUNGEON);
               }
            }

            w = w.getLeft();
         }
      }

      data.setSpawner(room.getX(), room.getY() + 1, room.getZ(), EntityType.HUSK);

      for(int i = 0; i < GenUtils.randInt(3, 10); ++i) {
         int[] loc = room.randomCoords(this.rand, 1);
         if (data.getType(loc[0], room.getY() + room.getHeight() + 1, loc[2]) == Material.SAND) {
            data.setType(loc[0], room.getY() + room.getHeight() + 1, loc[2], Material.SANDSTONE);
         }

         BlockUtils.dropDownBlock(new SimpleBlock(data, loc[0], room.getY() + room.getHeight(), loc[2]));
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 5 && room.getWidthZ() >= 5 && room.getWidthX() < 13 && room.getWidthZ() < 13;
   }
}
