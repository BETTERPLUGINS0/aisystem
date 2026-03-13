package org.terraform.structure.monument;

import java.util.Iterator;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Stairs;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.RoomPopulatorAbstract;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MonumentRoomPopulator extends RoomPopulatorAbstract {
   final MonumentDesign design;

   public MonumentRoomPopulator(Random rand, MonumentDesign design, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
      this.design = design;
   }

   protected static void setThickPillar(@NotNull Random rand, @NotNull MonumentDesign design, @NotNull SimpleBlock base) {
      Wall w = new Wall(base, BlockFace.NORTH);
      w.downUntilSolid(rand, new Material[]{Material.PRISMARINE});
      BlockFace[] var4 = BlockUtils.directBlockFaces;
      int var5 = var4.length;

      int var6;
      BlockFace face;
      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         switch(design) {
         case DARK_LIGHTLESS:
            w.getRelative(face).downUntilSolid(rand, new Material[]{Material.PRISMARINE_BRICKS});
            break;
         case DARK_PRISMARINE_CORNERS:
         case PRISMARINE_LANTERNS:
            w.getRelative(face).downUntilSolid(rand, new Material[]{Material.PRISMARINE_BRICKS, Material.SEA_LANTERN});
         }
      }

      var4 = BlockUtils.xzDiagonalPlaneBlockFaces;
      var5 = var4.length;

      for(var6 = 0; var6 < var5; ++var6) {
         face = var4[var6];
         switch(design) {
         case DARK_LIGHTLESS:
         case DARK_PRISMARINE_CORNERS:
            w.getRelative(face).downUntilSolid(rand, new Material[]{Material.DARK_PRISMARINE});
            break;
         case PRISMARINE_LANTERNS:
            w.getRelative(face).downUntilSolid(rand, new Material[]{Material.PRISMARINE_BRICKS});
         }
      }

   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      int[] upperBounds = room.getUpperCorner();
      int[] lowerBounds = room.getLowerCorner();

      int i;
      int x;
      int z;
      for(i = lowerBounds[0] + 1; i <= upperBounds[0] - 1; ++i) {
         for(x = lowerBounds[1] + 1; x <= upperBounds[1] - 1; ++x) {
            for(z = room.getY() + 1; z < room.getY() + room.getHeight(); ++z) {
               data.setType(i, z, x, Material.WATER);
            }
         }
      }

      if (room.getHeight() >= 7) {
         int[][] var12 = room.getAllCorners();
         x = var12.length;

         int[] corner;
         int length;
         for(z = 0; z < x; ++z) {
            corner = var12[z];

            for(length = 1; length < room.getHeight(); ++length) {
               if (data.getType(corner[0], length + room.getY(), corner[1]).isSolid()) {
                  data.setType(corner[0], length + room.getY(), corner[1], Material.DARK_PRISMARINE);
               }
            }
         }

         Iterator var13 = room.getFourWalls(data, 0).entrySet().iterator();

         int length;
         while(var13.hasNext()) {
            Entry<Wall, Integer> walls = (Entry)var13.next();
            Wall w = ((Wall)walls.getKey()).getRelative(0, room.getHeight() - 1, 0);
            length = (Integer)walls.getValue();

            for(length = 0; length < length; ++length) {
               if (!w.getUp().isSolid()) {
                  Stairs stair = (Stairs)Bukkit.createBlockData(this.design.stairs());
                  stair.setFacing(w.getDirection());
                  if (w.get().getY() < TerraformGenerator.seaLevel) {
                     stair.setWaterlogged(true);
                  }

                  w.setBlockData(stair);
               }

               if (length == length / 2 && room.getHeight() >= 16 && room.getWidthX() >= 10 && room.getWidthZ() >= 10) {
                  MonumentWallPattern.values()[this.rand.nextInt(MonumentWallPattern.values().length)].apply(w.getDown(4));
               }

               w = w.getLeft();
            }
         }

         var12 = room.getAllCorners();
         x = var12.length;

         for(z = 0; z < x; ++z) {
            corner = var12[z];
            if (!data.getType(corner[0], room.getY() + room.getHeight() + 1, corner[1]).isSolid()) {
               data.setType(corner[0], room.getY() + room.getHeight(), corner[1], Material.SEA_LANTERN);
            }
         }

         if (!data.getType(room.getX(), room.getY() + room.getHeight() + 1, room.getZ()).isSolid()) {
            i = GenUtils.randInt(1, 3);
            int j;
            if (i == 1) {
               int[][] var15 = room.getAllCorners(1);
               z = var15.length;

               for(length = 0; length < z; ++length) {
                  int[] pos = var15[length];
                  j = pos[0];
                  int z = pos[1];
                  this.design.spire(new Wall(new SimpleBlock(data, j, room.getY() + room.getHeight() + 1, z), BlockFace.NORTH), this.rand);
               }

               MonumentPopulator.arch(new Wall(new SimpleBlock(data, room.getX(), room.getY() + room.getHeight(), room.getZ()), BlockFace.NORTH), this.design, this.rand, (room.getWidthX() - 4) / 2, 6);
               MonumentPopulator.arch(new Wall(new SimpleBlock(data, room.getX(), room.getY() + room.getHeight(), room.getZ()), BlockFace.EAST), this.design, this.rand, (room.getWidthX() - 4) / 2, 6);
            } else {
               Iterator var16;
               Wall w;
               Entry walls;
               if (i == 2) {
                  var16 = room.getFourWalls(data, 1).entrySet().iterator();

                  while(var16.hasNext()) {
                     walls = (Entry)var16.next();
                     w = ((Wall)walls.getKey()).getRelative(0, room.getHeight(), 0);
                     length = (Integer)walls.getValue();

                     for(j = 0; j < length; ++j) {
                        if (j % 2 == 0) {
                           w.RPillar(4, this.rand, new Material[]{Material.PRISMARINE_WALL});
                        }

                        w.getUp(4).setType(this.design.slab());
                        w = w.getLeft();
                     }
                  }

                  for(x = lowerBounds[0] + 2; x <= upperBounds[0] - 2; ++x) {
                     for(z = lowerBounds[1] + 2; z <= upperBounds[1] - 2; ++z) {
                        data.setType(x, room.getY() + room.getHeight() + 5, z, this.design.mat(this.rand));
                     }
                  }
               } else if (i == 3) {
                  var16 = room.getFourWalls(data, 1).entrySet().iterator();

                  while(var16.hasNext()) {
                     walls = (Entry)var16.next();
                     w = ((Wall)walls.getKey()).getRelative(0, room.getHeight(), 0);
                     length = (Integer)walls.getValue();

                     for(j = 0; j < length; ++j) {
                        if (j % 2 == 0) {
                           w.setType(this.design.mat(this.rand));
                           w.getUp().setType(Material.PRISMARINE_WALL);
                           w.getUp(2).setType(this.design.slab());
                        } else {
                           w.setType(this.design.slab());
                        }

                        w = w.getLeft();
                     }
                  }

                  this.design.spawnLargeLight(data, room.getX(), room.getY() + room.getHeight() + 1, room.getZ());
               }
            }
         }

         setThickPillar(this.rand, this.design, new SimpleBlock(data, lowerBounds[0] + 1, room.getY() - 1, lowerBounds[1] + 1));
         setThickPillar(this.rand, this.design, new SimpleBlock(data, upperBounds[0] - 1, room.getY() - 1, lowerBounds[1]));
         setThickPillar(this.rand, this.design, new SimpleBlock(data, upperBounds[0] - 1, room.getY() - 1, upperBounds[1] - 1));
         setThickPillar(this.rand, this.design, new SimpleBlock(data, lowerBounds[0], room.getY() - 1, upperBounds[1] - 1));
      }
   }

   public boolean canPopulate(CubeRoom room) {
      return true;
   }
}
