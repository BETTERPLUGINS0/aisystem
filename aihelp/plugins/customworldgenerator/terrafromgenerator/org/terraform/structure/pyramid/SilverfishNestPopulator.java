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
import org.terraform.utils.GenUtils;

public class SilverfishNestPopulator extends RoomPopulatorAbstract {
   public SilverfishNestPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      Iterator var3 = room.getFourWalls(data, 0).entrySet().iterator();

      int i;
      while(var3.hasNext()) {
         Entry<Wall, Integer> entry = (Entry)var3.next();
         Wall w = (Wall)entry.getKey();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            if (w.isSolid()) {
               w.Pillar(room.getHeight() - 1, this.rand, new Material[]{Material.ANDESITE, Material.ANDESITE, Material.ANDESITE, Material.ANDESITE_SLAB, Material.STONE_BRICKS, Material.STONE_BRICK_SLAB, Material.CRACKED_STONE_BRICKS, Material.SANDSTONE, Material.STONE_BRICKS});
               if (i > 1 && i < (Integer)entry.getValue() - 2) {
                  w.getFront().Pillar(room.getHeight() - 1, this.rand, new Material[]{Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.AIR, Material.STONE, Material.INFESTED_STONE, Material.INFESTED_STONE_BRICKS, Material.STONE_BRICKS, Material.ANDESITE});
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

      int[] lowerCorner = room.getLowerCorner(3);
      int[] upperCorner = room.getUpperCorner(3);

      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(i = lowerCorner[1]; i <= upperCorner[1]; ++i) {
            Wall w = new Wall(new SimpleBlock(data, x, room.getY() + room.getHeight() - 1, i));
            w.downLPillar(this.rand, GenUtils.randInt(0, 4), new Material[]{Material.STONE, Material.ANDESITE, Material.INFESTED_STONE});
         }
      }

      if (room.getWidthX() >= 10 && room.getWidthZ() >= 10) {
         data.setSpawner(room.getX(), room.getY() + 1, room.getZ(), EntityType.SILVERFISH);
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() < 13 && room.getWidthZ() < 13;
   }
}
