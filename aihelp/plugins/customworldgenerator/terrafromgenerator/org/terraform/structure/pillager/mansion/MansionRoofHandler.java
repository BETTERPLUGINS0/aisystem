package org.terraform.structure.pillager.mansion;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;
import org.terraform.utils.version.V_1_19;
import org.terraform.utils.version.Version;

public class MansionRoofHandler {
   public static int[][] getLargestRectangle(@NotNull MansionJigsawBuilder builder) {
      int[] lowestCoords = null;
      int[] highestCoords = null;
      Iterator var3 = builder.getPieces().values().iterator();

      while(var3.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var3.next();
         if (lowestCoords == null) {
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

      int previousNotInRect = getNumberOfPiecesNotInRectangle(builder, lowestCoords, highestCoords);
      int i = 0;

      int y;
      for(int stall = 0; previousNotInRect != 0; ++i) {
         y = 0;
         switch(i % 4) {
         case 0:
            lowestCoords[0] += 9;
            y = getNumberOfPiecesNotInRectangle(builder, lowestCoords, highestCoords);
            if (y == previousNotInRect) {
               ++stall;
               if (stall < 4) {
                  lowestCoords[0] -= 9;
               } else {
                  stall = 0;
               }
            }
            break;
         case 1:
            lowestCoords[1] += 9;
            y = getNumberOfPiecesNotInRectangle(builder, lowestCoords, highestCoords);
            if (y == previousNotInRect) {
               ++stall;
               if (stall < 4) {
                  lowestCoords[1] -= 9;
               } else {
                  stall = 0;
               }
            }
            break;
         case 2:
            highestCoords[0] -= 9;
            y = getNumberOfPiecesNotInRectangle(builder, lowestCoords, highestCoords);
            if (y == previousNotInRect) {
               ++stall;
               if (stall < 4) {
                  highestCoords[0] += 9;
               } else {
                  stall = 0;
               }
            }
            break;
         case 3:
            highestCoords[1] -= 9;
            y = getNumberOfPiecesNotInRectangle(builder, lowestCoords, highestCoords);
            if (y == previousNotInRect) {
               ++stall;
               if (stall < 4) {
                  highestCoords[1] += 9;
               } else {
                  stall = 0;
               }
            }
         }

         previousNotInRect = y;
      }

      y = builder.getCore().getY();

      for(int x = lowestCoords[0]; x <= highestCoords[0]; x += builder.getPieceWidth()) {
         for(int z = lowestCoords[1]; z <= highestCoords[1]; z += builder.getPieceWidth()) {
            if (builder.getPieces().containsKey(new SimpleLocation(x, y, z))) {
               builder.getRoofedLocations().add(new SimpleLocation(x, y + 7 + 1, z));
            }
         }
      }

      return new int[][]{lowestCoords, highestCoords};
   }

   private static int getNumberOfPiecesNotInRectangle(@NotNull MansionJigsawBuilder builder, @NotNull int[] lowestCoords, @NotNull int[] highestCoords) {
      int y = builder.getCore().getY();
      int notInRect = 0;

      for(int x = lowestCoords[0]; x <= highestCoords[0]; x += builder.getPieceWidth()) {
         for(int z = lowestCoords[1]; z <= highestCoords[1]; z += builder.getPieceWidth()) {
            if (!builder.getPieces().containsKey(new SimpleLocation(x, y, z))) {
               ++notInRect;
            }
         }
      }

      return notInRect;
   }

   @NotNull
   public static Axis getDominantAxis(@NotNull int[] lowestCoords, @NotNull int[] highestCoords) {
      Axis superiorAxis;
      if (highestCoords[0] - lowestCoords[0] > highestCoords[1] - lowestCoords[1]) {
         superiorAxis = Axis.X;
      } else if (highestCoords[0] - lowestCoords[0] < highestCoords[1] - lowestCoords[1]) {
         superiorAxis = Axis.Z;
      } else {
         superiorAxis = Axis.X;
      }

      return superiorAxis;
   }

   @NotNull
   public static BlockFace getDominantBlockFace(@NotNull int[] lowestCoords, @NotNull int[] highestCoords) {
      BlockFace superiorAxis;
      if (highestCoords[0] - lowestCoords[0] > highestCoords[1] - lowestCoords[1]) {
         superiorAxis = BlockFace.WEST;
      } else if (highestCoords[0] - lowestCoords[0] < highestCoords[1] - lowestCoords[1]) {
         superiorAxis = BlockFace.NORTH;
      } else {
         superiorAxis = BlockFace.WEST;
      }

      return superiorAxis;
   }

   public static void placeTentRoof(Random rand, @NotNull MansionJigsawBuilder builder, int[][] bounds) {
      PopulatorDataAbstract data = builder.getCore().getPopData();
      int highestY = true;
      int[] lowestCoords = bounds[0];
      int[] highestCoords = bounds[1];
      int y = builder.getCore().getY() + 14 + 4;
      Axis superiorAxis = getDominantAxis(lowestCoords, highestCoords);
      lowestCoords[0] -= 5;
      lowestCoords[1] -= 5;
      highestCoords[0] += 5;
      highestCoords[1] += 5;
      Wall w;
      int length;
      int breadth;
      if (superiorAxis == Axis.X) {
         length = highestCoords[0] - lowestCoords[0] + 5;
         breadth = highestCoords[1] - lowestCoords[1] + 3;
         w = new Wall(new SimpleBlock(data, highestCoords[0] + 2, y - 1, lowestCoords[1] - 1), BlockFace.WEST);
      } else {
         length = highestCoords[1] - lowestCoords[1] + 5;
         breadth = highestCoords[0] - lowestCoords[0] + 3;
         w = new Wall(new SimpleBlock(data, lowestCoords[0] - 1, y - 1, lowestCoords[1] - 2), BlockFace.SOUTH);
      }

      for(int i = 0; i < length; ++i) {
         Wall target = w;
         boolean ascendBlock = false;

         for(int right = 0; right < breadth - 1; ++right) {
            if (right != 0 && right != breadth - 1) {
               if (i != 0 && i != length - 1) {
                  if (target.getDown(2).getType() != Material.DARK_OAK_PLANKS) {
                     (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(superiorAxis).apply(target.getDown().get());
                  }

                  if (i != 2 && i != length - 3) {
                     if (i != 1 && i != length - 2) {
                        target.getDown(2).downPillar(new Random(), target.getY() - y + 1, new Material[]{Material.AIR});
                     }
                  } else {
                     Wall bottom = target.getAtY(builder.getCore().getY() + 14 + 2);
                     if (BlockUtils.isAir(bottom.getType()) || Tag.STAIRS.isTagged(bottom.getType()) || Tag.SLABS.isTagged(bottom.getType())) {
                        bottom.setType(Material.DARK_OAK_PLANKS);
                     }

                     target.getDown(2).downPillar(new Random(), target.getY() - bottom.getY() - 2, new Material[]{bottom.getType()});
                  }
               } else {
                  (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setHalf(Half.TOP).setOpen(true).setFacing(i == 0 ? target.getDirection().getOppositeFace() : target.getDirection()).lapply(target.getDown());
               }
            }

            Material slabType = Material.DARK_OAK_SLAB;
            if (right == 0 || right == breadth - 2 || i == 0 || i == length - 1) {
               slabType = Material.COBBLESTONE_SLAB;
            }

            int highestY;
            if (breadth % 2 == 1) {
               if (right > breadth / 2) {
                  attemptReplaceSlab(slabType, target, ascendBlock ? Type.BOTTOM : Type.DOUBLE);
                  if (ascendBlock) {
                     target = target.getRight().getDown();
                     ascendBlock = false;
                  } else {
                     target = target.getRight();
                     ascendBlock = true;
                  }
               } else if (right < breadth / 2) {
                  attemptReplaceSlab(slabType, target, ascendBlock ? Type.DOUBLE : Type.BOTTOM);
                  if (ascendBlock) {
                     target = target.getRight().getUp();
                     ascendBlock = false;
                  } else {
                     target = target.getRight();
                     ascendBlock = true;
                  }
               } else {
                  highestY = target.getY();
                  target.setType(slabType);
                  if (ascendBlock) {
                     target = target.getRight().getDown();
                     ascendBlock = false;
                  } else {
                     target = target.getRight();
                     ascendBlock = true;
                  }
               }
            } else if (right == breadth / 2 - 1) {
               highestY = target.getY();
               target.setType(Material.DARK_OAK_PLANKS);
               if (slabType == Material.COBBLESTONE_SLAB) {
                  target.setType(Material.COBBLESTONE);
               }

               target = target.getRight();
            } else if (right >= breadth / 2) {
               attemptReplaceSlab(slabType, target, ascendBlock ? Type.BOTTOM : Type.DOUBLE);
               if (ascendBlock) {
                  target = target.getRight().getDown();
                  ascendBlock = false;
               } else {
                  target = target.getRight();
                  ascendBlock = true;
               }
            } else if (right < breadth / 2) {
               attemptReplaceSlab(slabType, target, ascendBlock ? Type.DOUBLE : Type.BOTTOM);
               if (ascendBlock) {
                  target = target.getRight().getUp();
                  ascendBlock = false;
               } else {
                  target = target.getRight();
                  ascendBlock = true;
               }
            }
         }

         w = w.getFront();
      }

   }

   private static void attemptReplaceSlab(@NotNull Material slabType, @NotNull Wall w, @NotNull Type type) {
      if (!w.isSolid()) {
         if (w.findCeiling(5) != null) {
            return;
         }

         (new SlabBuilder(slabType)).setType(type).lapply(w);
      } else if (Tag.STAIRS.isTagged(w.getType()) || Tag.SLABS.isTagged(w.getType())) {
         w.setType(Material.DARK_OAK_PLANKS);
      }

   }

   public static void atticDecorations(@NotNull Random rand, @NotNull PopulatorDataAbstract data, @NotNull JigsawStructurePiece piece) {
      SimpleBlock core = piece.getRoom().getCenterSimpleBlock(data).getUp(8);
      int chainLength;
      if (!core.isSolid()) {
         Wall ceiling = (new Wall(core)).getUp().findCeiling(15);
         if (ceiling == null) {
            return;
         }

         if (ceiling.getType().toString().contains("DARK_OAK")) {
            ceiling = ceiling.getDown();
            chainLength = ceiling.getY() - core.getY() - 2 - rand.nextInt(3);
            if (chainLength < 0) {
               chainLength = 0;
            }

            ceiling.downPillar(chainLength, new Material[]{Material.CHAIN});
            Lantern lantern = (Lantern)Bukkit.createBlockData(Material.LANTERN);
            lantern.setHanging(true);
            ceiling.getDown(chainLength).setBlockData(lantern);
            if (ceiling.getY() - core.getY() > 5) {
               if (GenUtils.chance(rand, 1, 2)) {
                  data.addEntity(core.getX(), core.getY(), core.getZ(), EntityType.SPIDER);
               }

               if (GenUtils.chance(rand, 1, 2)) {
                  data.addEntity(core.getX(), core.getY(), core.getZ(), EntityType.CAVE_SPIDER);
               }

               if (Version.VERSION.isAtLeast(Version.v1_19_4) && rand.nextBoolean()) {
                  for(int i = 0; i < 1 + rand.nextInt(3); ++i) {
                     data.addEntity(core.getX(), core.getY(), core.getZ(), V_1_19.ALLAY);
                  }
               }
            }
         }
      }

      int[][] var14 = piece.getRoom().getAllCorners(2);
      chainLength = var14.length;

      for(int var15 = 0; var15 < chainLength; ++var15) {
         int[] loc = var14[var15];
         SimpleBlock target = new SimpleBlock(data, loc[0], core.getY(), loc[1]);
         Wall ceiling = (new Wall(target)).findCeiling(15);
         if (ceiling != null && ceiling.getY() > target.getY() + 1) {
            ceiling.getDown().downUntilSolid(new Random(), new Material[]{Material.DARK_OAK_LOG});
            BlockFace[] var10 = BlockUtils.directBlockFaces;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               BlockFace face = var10[var12];
               if (GenUtils.chance(rand, 1, 8)) {
                  ceiling.getDown().getRelative(face).get().lsetType(Material.COBWEB);
               }
            }

            if (GenUtils.chance(rand, 1, 20)) {
               BlockFace f = BlockUtils.getDirectBlockFace(rand);
               if (!target.getRelative(f).isSolid()) {
                  (new ChestBuilder(Material.CHEST)).setFacing(f).setLootTable(TerraLootTable.WOODLAND_MANSION).apply(target.getRelative(f));
               }
            }
         }
      }

   }
}
