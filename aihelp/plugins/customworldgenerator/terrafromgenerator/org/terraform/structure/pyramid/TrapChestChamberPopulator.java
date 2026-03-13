package org.terraform.structure.pyramid;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;

public class TrapChestChamberPopulator extends RoomPopulatorAbstract {
   public TrapChestChamberPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] lowerCorner = room.getLowerCorner(1);
      int[] upperCorner = room.getUpperCorner(1);

      int nx;
      for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
         for(nx = lowerCorner[1]; nx <= upperCorner[1]; ++nx) {
            data.setType(x, room.getY() + 1, nx, Material.AIR);
         }
      }

      SimpleBlock center = new SimpleBlock(data, room.getX(), room.getY(), room.getZ());
      center.setType(Material.BLUE_TERRACOTTA);
      BlockFace[] var11 = BlockUtils.xzDiagonalPlaneBlockFaces;
      int nz = var11.length;

      int var8;
      BlockFace face;
      for(var8 = 0; var8 < nz; ++var8) {
         face = var11[var8];
         center.getRelative(face).setType(Material.ORANGE_TERRACOTTA);
         (new Wall(center.getRelative(face).getRelative(face).getUp())).Pillar(room.getHeight(), this.rand, new Material[]{Material.CUT_SANDSTONE});
      }

      var11 = BlockUtils.directBlockFaces;
      nz = var11.length;

      for(var8 = 0; var8 < nz; ++var8) {
         face = var11[var8];
         center.getRelative(face).getRelative(face).setType(Material.ORANGE_TERRACOTTA);
      }

      center.getUp().setType(Material.TRAPPED_CHEST);
      data.lootTableChest(center.getX(), center.getY() + 1, center.getZ(), TerraLootTable.DESERT_PYRAMID);
      center = center.getDown();

      for(nx = -1; nx <= 1; ++nx) {
         for(nz = -1; nz <= 1; ++nz) {
            data.setType(nx + center.getX(), center.getY(), center.getZ() + nz, Material.REDSTONE_WIRE);
            data.setType(nx + center.getX(), center.getY() - 1, center.getZ() + nz, Material.TNT);
         }
      }

      center.getDown().setType(Material.STONE);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() >= 5 && room.getWidthZ() >= 5;
   }
}
