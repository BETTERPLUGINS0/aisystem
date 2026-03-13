package org.terraform.structure.mineshaft;

import java.util.EnumSet;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class ShaftRoomPopulator extends RoomPopulatorAbstract {
   public ShaftRoomPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      for(int i = 8; i < 20; ++i) {
         BlockUtils.carveCaveAir((new Random()).nextInt(777123), (float)(room.getWidthX() - 4) / 2.0F, 5.0F, (float)(room.getWidthZ() - 4) / 2.0F, new SimpleBlock(data, room.getX(), room.getY() + i, room.getZ()), false, EnumSet.of(Material.BARRIER));
      }

      int[] lowerCorner = room.getLowerCorner(3);
      int[] upperCorner = room.getUpperCorner(3);
      int y = room.getY();

      int z;
      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
            SimpleBlock b = new SimpleBlock(data, x, y, z);
            if (b.getType() == Material.CAVE_AIR || b.getType() == Material.OAK_PLANKS || b.getType() == Material.OAK_SLAB || b.getType() == Material.GRAVEL) {
               b.setType((Material)GenUtils.randChoice((Object[])(Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS, Material.MOSSY_STONE_BRICKS, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE, Material.CAVE_AIR)));
            }
         }
      }

      int[][] var14 = room.getAllCorners(3);
      z = var14.length;

      for(int var15 = 0; var15 < z; ++var15) {
         int[] corner = var14[var15];
         int x = corner[0];
         int z = corner[1];
         Wall w = new Wall(new SimpleBlock(data, x, room.getY() + 1, z));
         w.getDown().downUntilSolid(this.rand, new Material[]{Material.OAK_LOG});
      }

   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
