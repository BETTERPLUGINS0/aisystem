package org.terraform.structure.room.carver;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.GenUtils;

public class StandardRoomCarver extends RoomCarver {
   final int tile;
   final Material fillMat;

   public StandardRoomCarver(int tile, Material fillMat) {
      this.tile = tile;
      this.fillMat = fillMat;
   }

   public void carveRoom(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room, @NotNull Material... mat) {
      int tileIndex = 0;
      int nx;
      int ny;
      int nz;
      if (mat[0] != Material.BARRIER) {
         for(nx = room.getX() - room.getWidthX() / 2; nx <= room.getX() + room.getWidthX() / 2; ++nx) {
            for(ny = room.getY(); ny <= room.getY() + room.getHeight(); ++ny) {
               for(nz = room.getZ() - room.getWidthZ() / 2; nz <= room.getZ() + room.getWidthZ() / 2; ++nz) {
                  if (data.getType(nx, ny, nz) != Material.CAVE_AIR) {
                     if (this.tile == -1) {
                        data.setType(nx, ny, nz, (Material)GenUtils.randChoice((Object[])mat));
                     } else {
                        data.setType(nx, ny, nz, mat[Math.abs(nz + room.getWidthZ() / 2 + ny + nx + room.getWidthX() / 2 - tileIndex) % mat.length]);
                        ++tileIndex;
                        if (tileIndex == 2) {
                           tileIndex = 0;
                        }
                     }
                  }
               }
            }
         }
      }

      for(nx = room.getX() - room.getWidthX() / 2 + 1; nx <= room.getX() + room.getWidthX() / 2 - 1; ++nx) {
         for(ny = room.getY() + 1; ny <= room.getY() + room.getHeight() - 1; ++ny) {
            for(nz = room.getZ() - room.getWidthZ() / 2 + 1; nz <= room.getZ() + room.getWidthZ() / 2 - 1; ++nz) {
               data.setType(nx, ny, nz, this.fillMat);
            }
         }
      }

   }
}
