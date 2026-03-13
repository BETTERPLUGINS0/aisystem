package org.terraform.structure.village.plains.house;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;

public class PlainsVillageRoofHandler {
   public static boolean isRectangle(@NotNull PlainsVillageHouseJigsawBuilder builder) {
      int[] lowestCoords = null;
      int[] highestCoords = null;
      int y = 0;
      Iterator var4 = builder.getPieces().values().iterator();

      while(var4.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var4.next();
         if (lowestCoords == null) {
            y = piece.getRoom().getY();
            lowestCoords = new int[]{piece.getRoom().getX(), piece.getRoom().getZ()};
         }

         if (highestCoords == null) {
            highestCoords = new int[]{piece.getRoom().getX(), piece.getRoom().getZ()};
         }

         if (piece.getRoom().getX() < lowestCoords[0]) {
            lowestCoords[0] = piece.getRoom().getX();
         }

         if (piece.getRoom().getZ() < lowestCoords[1]) {
            lowestCoords[1] = piece.getRoom().getZ();
         }

         if (piece.getRoom().getX() > highestCoords[0]) {
            highestCoords[0] = piece.getRoom().getX();
         }

         if (piece.getRoom().getZ() > highestCoords[1]) {
            highestCoords[1] = piece.getRoom().getZ();
         }
      }

      int count = 0;

      for(int x = lowestCoords[0]; x <= highestCoords[0]; x += builder.getPieceWidth()) {
         for(int z = lowestCoords[1]; z <= highestCoords[1]; z += builder.getPieceWidth()) {
            if (!builder.getPieces().containsKey(new SimpleLocation(x, y, z))) {
               return false;
            }

            ++count;
         }
      }

      return count == builder.getPieces().size();
   }

   public static void placeTentRoof(@NotNull PlainsVillagePopulator plainsVillagePopulator, @NotNull Random rand, @NotNull PlainsVillageHouseJigsawBuilder builder) {
      PopulatorDataAbstract data = builder.getCore().getPopData();
      int[] lowestCoords = null;
      int[] highestCoords = null;
      int y = 0;
      Iterator var8 = builder.getPieces().values().iterator();

      while(var8.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var8.next();
         if (lowestCoords == null) {
            y = piece.getRoom().getY();
            lowestCoords = new int[]{piece.getRoom().getX(), piece.getRoom().getZ()};
         }

         if (highestCoords == null) {
            highestCoords = new int[]{piece.getRoom().getX(), piece.getRoom().getZ()};
         }

         if (piece.getRoom().getX() < lowestCoords[0]) {
            lowestCoords[0] = piece.getRoom().getX();
         }

         if (piece.getRoom().getZ() < lowestCoords[1]) {
            lowestCoords[1] = piece.getRoom().getZ();
         }

         if (piece.getRoom().getX() > highestCoords[0]) {
            highestCoords[0] = piece.getRoom().getX();
         }

         if (piece.getRoom().getZ() > highestCoords[1]) {
            highestCoords[1] = piece.getRoom().getZ();
         }
      }

      Axis superiorAxis;
      if (highestCoords[0] - lowestCoords[0] > highestCoords[1] - lowestCoords[1]) {
         superiorAxis = Axis.X;
      } else if (highestCoords[0] - lowestCoords[0] < highestCoords[1] - lowestCoords[1]) {
         superiorAxis = Axis.Z;
      } else {
         superiorAxis = (new Axis[]{Axis.X, Axis.Z})[rand.nextInt(1)];
      }

      lowestCoords[0] -= 3;
      lowestCoords[1] -= 3;
      highestCoords[0] += 3;
      highestCoords[1] += 3;
      int breadth;
      Wall w;
      int length;
      if (superiorAxis == Axis.X) {
         length = highestCoords[0] - lowestCoords[0] + 5;
         breadth = highestCoords[1] - lowestCoords[1] + 3;
         w = new Wall(new SimpleBlock(data, highestCoords[0] + 2, y + 4, lowestCoords[1] - 1), BlockFace.WEST);
      } else {
         length = highestCoords[1] - lowestCoords[1] + 5;
         breadth = highestCoords[0] - lowestCoords[0] + 3;
         w = new Wall(new SimpleBlock(data, lowestCoords[0] - 1, y + 4, lowestCoords[1] - 2), BlockFace.SOUTH);
      }

      for(int i = 0; i < length; ++i) {
         Wall target = w;

         for(int right = 0; right < breadth; ++right) {
            if (i == 2 || i == length - 3) {
               Material bottom = getLowestMaterial(target);
               target.downUntilSolid(new Random(), new Material[]{bottom});
            }

            if (right != 0 && right != breadth - 1) {
               if (i == 0) {
                  (new TrapdoorBuilder(plainsVillagePopulator.woodTrapdoor)).setHalf(Half.TOP).setOpen(true).setFacing(target.getDirection().getOppositeFace()).apply(target.getDown());
               } else if (i == length - 1) {
                  (new TrapdoorBuilder(plainsVillagePopulator.woodTrapdoor)).setHalf(Half.TOP).setOpen(true).setFacing(target.getDirection()).apply(target.getDown());
               } else {
                  (new OrientableBuilder(plainsVillagePopulator.woodLog)).setAxis(superiorAxis).apply(target.getDown().get());
               }
            }

            Material[] stairType = new Material[]{plainsVillagePopulator.woodStairs};
            Material[] slabType = new Material[]{Material.COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB};
            if (right == 0 || right == breadth - 1 || i == 0 || i == length - 1) {
               stairType = new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS};
            }

            if (breadth % 2 == 1) {
               if (right > breadth / 2) {
                  (new StairBuilder(stairType)).setFacing(BlockUtils.getLeft(target.getDirection())).apply(target);
                  target = target.getRight().getDown();
               } else if (right < breadth / 2) {
                  (new StairBuilder(stairType)).setFacing(BlockUtils.getRight(target.getDirection())).apply(target);
                  target = target.getRight().getUp();
               } else {
                  target.setType(slabType);
                  target = target.getRight().getDown();
               }
            } else if (right == breadth / 2 - 1) {
               (new StairBuilder(stairType)).setFacing(BlockUtils.getRight(target.getDirection())).apply(target);
               target = target.getRight();
            } else if (right >= breadth / 2) {
               (new StairBuilder(stairType)).setFacing(BlockUtils.getLeft(target.getDirection())).apply(target);
               target = target.getRight().getDown();
            } else if (right < breadth / 2) {
               (new StairBuilder(stairType)).setFacing(BlockUtils.getRight(target.getDirection())).apply(target);
               target = target.getRight().getUp();
            }
         }

         w = w.getFront();
      }

   }

   @Nullable
   private static Material getLowestMaterial(@NotNull Wall w) {
      Wall other = w.findFloor(10);
      return other != null ? other.getType() : null;
   }

   public static void placeStandardRoof(@NotNull PlainsVillagePopulator plainsVillagePopulator, @NotNull PlainsVillageHouseJigsawBuilder builder) {
      PopulatorDataAbstract data = builder.getCore().getPopData();
      Material[] solidMat = new Material[]{plainsVillagePopulator.woodPlank};
      Material[] stairMat = new Material[]{plainsVillagePopulator.woodStairs};
      if (builder.getVariant() == PlainsVillageHouseVariant.CLAY) {
         solidMat = new Material[]{Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE};
         stairMat = new Material[]{Material.COBBLESTONE_STAIRS, Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS};
      }

      Iterator var5 = builder.getPieces().values().iterator();

      JigsawStructurePiece piece;
      int depth;
      int[] lowerCorner;
      int[] upperCorner;
      int x;
      int z;
      while(var5.hasNext()) {
         piece = (JigsawStructurePiece)var5.next();

         for(depth = -2; depth <= 0; ++depth) {
            lowerCorner = piece.getRoom().getLowerCorner(depth);
            upperCorner = piece.getRoom().getUpperCorner(depth);

            for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
               for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
                  data.setType(x, piece.getRoom().getY() + piece.getRoom().getHeight() + 3 + depth, z, (Material)GenUtils.randChoice((Object[])solidMat));
               }
            }
         }
      }

      var5 = builder.getPieces().values().iterator();

      while(var5.hasNext()) {
         piece = (JigsawStructurePiece)var5.next();

         for(depth = -2; depth <= 0; ++depth) {
            lowerCorner = piece.getRoom().getLowerCorner(depth);
            upperCorner = piece.getRoom().getUpperCorner(depth);

            for(x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
               for(z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
                  SimpleBlock target = new SimpleBlock(data, x, piece.getRoom().getY() + piece.getRoom().getHeight() + 3 + depth, z);
                  if (target.getType() == Material.COBBLESTONE || target.getType() == plainsVillagePopulator.woodPlank || target.getType() == Material.MOSSY_COBBLESTONE) {
                     BlockFace[] var13 = BlockUtils.directBlockFaces;
                     int var14 = var13.length;

                     for(int var15 = 0; var15 < var14; ++var15) {
                        BlockFace face = var13[var15];
                        if (!target.getRelative(face).isSolid()) {
                           (new StairBuilder(stairMat)).setFacing(face.getOppositeFace()).apply(target);
                           BlockUtils.correctSurroundingStairData(target);
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

   }
}
