package org.terraform.structure.mineshaft;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Chest;
import org.bukkit.block.data.type.Furnace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.GenUtils;

public class SmeltingHallPopulator extends RoomPopulatorAbstract {
   public SmeltingHallPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(3);
      int[] upperCorner = room.getUpperCorner(3);
      int y = room.getY();

      int z;
      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            SimpleBlock b = new SimpleBlock(data, x, y, z);
            if (b.getType() == Material.CAVE_AIR || b.getType() == Material.OAK_PLANKS || b.getType() == Material.OAK_SLAB || b.getType() == Material.GRAVEL) {
               b.setType((Material)GenUtils.randChoice((Object[])(Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE, Material.CAVE_AIR)));
               if (TConfig.areDecorationsEnabled() && GenUtils.chance(this.rand, 1, 150)) {
                  b.getUp().setType(Material.COBBLESTONE);
                  b.getUp(2).setType(Material.LANTERN);
               }
            }
         }
      }

      int[][] var14 = room.getAllCorners(3);
      z = var14.length;

      int l;
      int i;
      int type;
      for(type = 0; type < z; ++type) {
         int[] corner = var14[type];
         l = corner[0];
         i = corner[1];
         Wall w = new Wall(new SimpleBlock(data, l, room.getY() + 1, i), BlockFace.NORTH);
         if (w.findCeiling(50) != null) {
            if (TConfig.areDecorationsEnabled()) {
               w.LPillar(50, this.rand, new Material[]{Material.IRON_BARS});
            }
         } else {
            w.getDown().downUntilSolid(this.rand, new Material[]{Material.OAK_LOG});
         }
      }

      Iterator var15 = room.getFourWalls(data, 4).entrySet().iterator();

      while(true) {
         Entry walls;
         do {
            if (!var15.hasNext()) {
               return;
            }

            walls = (Entry)var15.next();
            type = this.rand.nextInt(3);
         } while(type == 0);

         Wall w = (Wall)walls.getKey();
         l = (Integer)walls.getValue();

         for(i = 0; i < l; ++i) {
            if (TConfig.areDecorationsEnabled() && w.getType() == Material.CAVE_AIR) {
               if (type == 1) {
                  Furnace furnace = (Furnace)Bukkit.createBlockData(Material.FURNACE);
                  furnace.setFacing(w.getDirection());

                  for(int ny = 0; ny < room.getHeight() / 3; ++ny) {
                     w.getRelative(0, ny, 0).setBlockData(furnace);
                  }
               } else if (GenUtils.chance(this.rand, 1, 5)) {
                  Chest chest = (Chest)Bukkit.createBlockData(Material.CHEST);
                  chest.setFacing(w.getDirection());
                  w.setBlockData(chest);
                  data.lootTableChest(w.getX(), w.getY(), w.getZ(), TerraLootTable.ABANDONED_MINESHAFT);
               }
            }

            w = w.getLeft();
         }
      }
   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
