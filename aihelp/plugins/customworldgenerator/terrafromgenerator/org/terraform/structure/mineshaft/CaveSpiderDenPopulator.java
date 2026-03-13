package org.terraform.structure.mineshaft;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class CaveSpiderDenPopulator extends RoomPopulatorAbstract {
   public CaveSpiderDenPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(3);
      int[] upperCorner = room.getUpperCorner(3);
      SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
      if (!BlockUtils.isWet(center) && GenUtils.getHighestGround(data, center.getX(), center.getZ()) > center.getY()) {
         int y = room.getY();

         int x;
         int z;
         SimpleBlock b;
         for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               b = new SimpleBlock(data, x, y, z);
               if (b.getType() == Material.CAVE_AIR) {
                  b.setType((Material)GenUtils.randChoice((Object[])(Material.OAK_PLANKS, Material.OAK_SLAB, Material.OAK_PLANKS, Material.OAK_SLAB, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE_SLAB)));
               }
            }
         }

         for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               b = new SimpleBlock(data, x, y, z);

               int limit;
               for(limit = 10; limit > 0 && b.getType() != Material.CAVE_AIR; --limit) {
                  b = b.getUp();
               }

               if (limit >= 0) {
                  if (x == room.getX() && z == room.getZ()) {
                     data.setSpawner(x, b.getY(), z, EntityType.CAVE_SPIDER);
                  } else {
                     Wall w = new Wall(b, BlockFace.NORTH);
                     w.LPillar(GenUtils.randInt(0, 2), this.rand, new Material[]{Material.COBWEB});
                  }
               }
            }
         }

      }
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return !room.isBig();
   }
}
