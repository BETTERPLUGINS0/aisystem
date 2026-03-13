package org.terraform.structure.monument;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;

public class LanternPillarRoomPopulator extends MonumentRoomPopulator {
   public LanternPillarRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, design, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      int y = room.getY() + room.getHeight() - 1;

      int x;
      for(int i = 0; i < 5; ++i) {
         int[] upperBounds = room.getUpperCorner(1);
         int[] lowerBounds = room.getLowerCorner(1);

         int z;
         for(x = lowerBounds[0]; x <= upperBounds[0]; ++x) {
            for(z = lowerBounds[1]; z <= upperBounds[1]; ++z) {
               data.setType(x, y - i, z, this.design.mat(this.rand));
            }
         }

         upperBounds = room.getUpperCorner(6 - i);
         lowerBounds = room.getLowerCorner(6 - i);

         for(x = lowerBounds[0]; x <= upperBounds[0]; ++x) {
            for(z = lowerBounds[1]; z <= upperBounds[1]; ++z) {
               data.setType(x, y - i, z, Material.WATER);
            }
         }
      }

      Wall w = new Wall(new SimpleBlock(data, room.getX(), room.getY() + 1, room.getZ()), BlockFace.NORTH);
      w.LPillar(room.getHeight(), this.rand, new Material[]{Material.SEA_LANTERN});
      BlockFace[] var11 = BlockUtils.xzDiagonalPlaneBlockFaces;
      int nz = var11.length;

      BlockFace face;
      for(x = 0; x < nz; ++x) {
         face = var11[x];
         w.getRelative(face).LPillar(room.getHeight(), true, this.rand, new Material[]{Material.DARK_PRISMARINE, Material.PRISMARINE_WALL});
      }

      var11 = BlockUtils.directBlockFaces;
      nz = var11.length;

      for(x = 0; x < nz; ++x) {
         face = var11[x];
         w.getRelative(face).LPillar(room.getHeight(), true, this.rand, new Material[]{Material.PRISMARINE_WALL, Material.WATER});

         for(int i = 0; i < room.getHeight() - 2; ++i) {
            BlockUtils.correctSurroundingMultifacingData(w.getRelative(face).getRelative(0, i, 0).get());
         }
      }

      for(int nx = -2; nx <= 2; ++nx) {
         for(nz = -2; nz <= 2; ++nz) {
            w.getRelative(nx, 0, nz).setType(this.design.mat(this.rand));
         }
      }

   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getHeight() > 12;
   }
}
