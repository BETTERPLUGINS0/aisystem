package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MainEntrancePopulator extends RoomPopulatorAbstract {
   private final BlockFace entranceFace;

   public MainEntrancePopulator(Random rand, boolean forceSpawn, boolean unique, BlockFace entranceFace) {
      super(rand, forceSpawn, unique);
      this.entranceFace = entranceFace;
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int entranceHeightOffsetFromBase = room.getHeight() - 5;
      int[] upperRoomCorner = room.getUpperCorner();
      int[] lowerRoomCorner = room.getLowerCorner();

      int i;
      for(int h = 0; h <= 6; ++h) {
         int[] upperCorner = room.getUpperCorner(-(6 - h));
         int[] lowerCorner = room.getLowerCorner(-(6 - h));

         for(i = lowerCorner[0]; i <= upperCorner[0]; ++i) {
            for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               if ((i <= lowerRoomCorner[0] || i >= upperRoomCorner[0] || z <= lowerRoomCorner[1] || z >= upperRoomCorner[1]) && h != 6) {
                  if (h == 2) {
                     data.setType(i, room.getY() + entranceHeightOffsetFromBase + 2, z, Material.CHISELED_RED_SANDSTONE);
                  } else {
                     data.setType(i, room.getY() + entranceHeightOffsetFromBase + h, z, (Material)GenUtils.randChoice((Object[])(Material.SANDSTONE, Material.SMOOTH_SANDSTONE)));
                  }

                  if (h == 0) {
                     BlockUtils.setDownUntilSolid(i, room.getY() + entranceHeightOffsetFromBase - 1, z, data, Material.SANDSTONE);
                  }

                  if ((i == lowerCorner[0] + 1 || i == upperCorner[0] - 1) && (z == lowerCorner[1] + 1 || z == upperCorner[1] - 1) && data.getType(i, room.getY() + entranceHeightOffsetFromBase + h + 1, z) == Material.AIR) {
                     data.setType(i, room.getY() + entranceHeightOffsetFromBase + h + 1, z, Material.SANDSTONE_WALL);
                  }
               }
            }
         }
      }

      SimpleBlock b = new SimpleBlock(data, room.getX(), room.getY() + room.getHeight(), room.getZ());

      int nx;
      int h;
      for(nx = -1; nx <= 1; ++nx) {
         for(h = -1; h <= 1; ++h) {
            b.getRelative(nx, 0, h).setType(Material.ORANGE_TERRACOTTA);
            b.getRelative(nx, 1, h).setType(Material.CUT_RED_SANDSTONE);
         }
      }

      for(nx = -2; nx <= 2; nx += 2) {
         for(h = -2; h <= 2; h += 2) {
            b.getRelative(nx, 0, h).setType(Material.ORANGE_TERRACOTTA);
         }
      }

      Wall w = new Wall(new SimpleBlock(data, room.getX(), room.getY() + entranceHeightOffsetFromBase + 1, room.getZ()), this.entranceFace.getOppositeFace());
      w = w.getFront(3);

      for(h = 0; h <= 6; ++h) {
         w = w.getFront(1);
         w.Pillar(4, this.rand, new Material[]{Material.AIR});
         w.getLeft().Pillar(3, this.rand, new Material[]{Material.AIR});
         w.getRight().Pillar(3, this.rand, new Material[]{Material.AIR});
      }

      for(h = entranceHeightOffsetFromBase; h > 0; --h) {
         w = new Wall(new SimpleBlock(data, room.getX(), room.getY() + h, room.getZ()), BlockUtils.rotateFace(this.entranceFace.getOppositeFace(), entranceHeightOffsetFromBase - h));
         if (h > 3) {
            for(i = 1; i <= 3; ++i) {
               w.getFront(i).setType(Material.CUT_SANDSTONE);
            }
         } else {
            w.getFront().downUntilSolid(new Random(), new Material[]{Material.CUT_SANDSTONE});
         }
      }

      data.setType(room.getX(), room.getY(), room.getZ(), Material.AIR);
      data.setType(room.getX(), room.getY() + room.getHeight(), room.getZ(), Material.LAVA);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 5 && room.getWidthZ() >= 5;
   }
}
