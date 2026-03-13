package org.terraform.structure.pillager.mansion.secondfloor;

import java.util.Iterator;
import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;
import org.terraform.utils.blockdata.TrapdoorBuilder;

public class MansionSecondFloorWallPiece extends JigsawStructurePiece {
   private final MansionJigsawBuilder builder;
   public boolean isTentRoofFace = false;

   public MansionSecondFloorWallPiece(MansionJigsawBuilder builder, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
      this.builder = builder;
   }

   public void buildIndividualRoofs(Random random, @NotNull PopulatorDataAbstract data, int[] lowerBound, int[] upperBound) {
      if (this.builder.countOverlappingPiecesAtLocation(this.getRoom().getSimpleLocation().getRelative(0, -8, 0)) < 3) {
         BlockFace walledFace = this.getRotation();
         Wall w = new Wall((new SimpleBlock(data, this.getRoom().getSimpleLocation())).getRelative(walledFace.getOppositeFace(), 3).getUp(7), walledFace);
         int maxDepth = -1;
         BlockFace roofOuterFace = null;
         if (lowerBound[0] <= w.getX() && lowerBound[1] <= w.getZ() && upperBound[0] >= w.getX() && upperBound[1] >= w.getZ()) {
            this.builder.getRoofedLocations().add(this.getRoom().getSimpleLocation().getRelative(this.getRotation().getOppositeFace(), 9));
         } else {
            if (w.getX() >= lowerBound[0] && w.getX() <= upperBound[0]) {
               if (w.getZ() > upperBound[1]) {
                  roofOuterFace = BlockFace.SOUTH;
                  maxDepth = w.getZ() - upperBound[1];
               } else {
                  roofOuterFace = BlockFace.NORTH;
                  maxDepth = lowerBound[1] - w.getZ();
               }
            } else if (w.getZ() >= lowerBound[1] && w.getZ() <= upperBound[1]) {
               if (w.getX() > upperBound[0]) {
                  roofOuterFace = BlockFace.EAST;
                  maxDepth = w.getX() - upperBound[0];
               } else {
                  roofOuterFace = BlockFace.WEST;
                  maxDepth = lowerBound[0] - w.getX();
               }
            }

            maxDepth *= 2;
            if (maxDepth < 6) {
               maxDepth *= 2;
            }

            if (roofOuterFace != null && walledFace == roofOuterFace) {
               this.isTentRoofFace = true;
               this.builder.getRoofedLocations().add(this.getRoom().getSimpleLocation().getRelative(this.getRotation().getOppositeFace(), 9));
               BlockFace[] var9 = BlockUtils.getAdjacentFaces(walledFace);
               int var10 = var9.length;

               for(int var11 = 0; var11 < var10; ++var11) {
                  BlockFace side = var9[var11];

                  for(int i = 0; i < 6; ++i) {
                     int position = 6 - i;
                     Wall roofPiece = w.getRelative(0, position, 0).getRelative(side, i);
                     if (i == 0) {
                        roofPiece.setType(Material.COBBLESTONE_SLAB);
                        (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).lapply(roofPiece.getDown());
                     } else {
                        StairBuilder builder = (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(side.getOppositeFace()).lapply(roofPiece);
                        if (BlockUtils.isAir(roofPiece.getDown().getType()) || roofPiece.getDown().getType() == Material.COBBLESTONE_STAIRS) {
                           builder.setFacing(side).setHalf(Half.TOP).apply(roofPiece.getDown());
                        }

                        if (i == 5 && (roofPiece.getDown().getRelative(side).getType() == Material.COBBLESTONE_STAIRS || roofPiece.getDown().getRelative(side.getOppositeFace()).getType() == Material.COBBLESTONE_STAIRS)) {
                           roofPiece.getDown().setType(Material.AIR);
                           roofPiece.getDown().getRelative(side).setType(Material.AIR);
                           roofPiece.getDown().getRelative(side.getOppositeFace()).setType(Material.AIR);
                        }
                     }

                     for(int depth = 1; depth < maxDepth; ++depth) {
                        if (i == 0) {
                           roofPiece.getRear(depth).setType(Material.COBBLESTONE_SLAB);
                           (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).lapply(roofPiece.getRear(depth).getDown());
                        } else {
                           (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(side.getOppositeFace()).lapply(roofPiece.getRear(depth));
                        }
                     }
                  }
               }

            }
         }
      }
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getUp().Pillar(this.getRoom().getHeight(), rand, new Material[]{Material.DARK_OAK_PLANKS});
         w = w.getLeft();
      }

   }

   public void postBuildDecoration(@NotNull Random rand, @NotNull PopulatorDataAbstract data) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      MansionSecondFloorWallPiece.MansionSecondFloorWallType var10000;
      switch(rand.nextInt(3)) {
      case 0:
         var10000 = MansionSecondFloorWallPiece.MansionSecondFloorWallType.LARGE_WINDOW;
         break;
      case 1:
         var10000 = MansionSecondFloorWallPiece.MansionSecondFloorWallType.THIN_WINDOWS;
         break;
      case 2:
         var10000 = MansionSecondFloorWallPiece.MansionSecondFloorWallType.BALCONY;
         break;
      default:
         var10000 = null;
      }

      MansionSecondFloorWallPiece.MansionSecondFloorWallType type = var10000;
      if (type == MansionSecondFloorWallPiece.MansionSecondFloorWallType.BALCONY) {
         int overlappers = 0;
         Iterator var6 = this.builder.getOverlapperPieces().iterator();

         while(var6.hasNext()) {
            JigsawStructurePiece otherPiece = (JigsawStructurePiece)var6.next();
            int[] center = otherPiece.getRoom().getCenter();
            if (center[0] == this.getRoom().getCenter()[0] && center[2] == this.getRoom().getCenter()[2]) {
               ++overlappers;
            }
         }

         if (overlappers > 1) {
            type = MansionSecondFloorWallPiece.MansionSecondFloorWallType.LARGE_WINDOW;
         }
      }

      Wall w = ((Wall)entry.getKey()).getDown();

      int i;
      for(i = 0; i < (Integer)entry.getValue(); ++i) {
         switch(type.ordinal()) {
         case 0:
            if (i == 2 || i == (Integer)entry.getValue() - 3) {
               w.getUp().Pillar(this.getRoom().getHeight(), new Random(), new Material[]{Material.DARK_OAK_LOG});
               (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setHalf(Half.TOP).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp(2).getFront());
               w.getUp(3).getFront().setType(Material.STONE_BRICK_WALL);
               w.getUp(4).getFront().setType(Material.STONE_BRICK_WALL);
               w.getUp(5).getFront().setType(Material.COBBLESTONE_SLAB);
            }

            if (i % 2 == 1) {
               w.getUp(2).Pillar(4, new Random(), new Material[]{Material.LIGHT_GRAY_STAINED_GLASS_PANE});
               w.getUp(2).CorrectMultipleFacing(4);
            }

            if (i == (Integer)entry.getValue() / 2) {
               this.spawnWallSupportingPillar(w.getFront().getUp(), this.getRoom().getHeight());
            }
            break;
         case 1:
            if (i == 1 || i == (Integer)entry.getValue() - 2) {
               w.getUp().Pillar(this.getRoom().getHeight(), new Random(), new Material[]{Material.DARK_OAK_LOG});
            }

            if (i == 3 || i == 4 || i == 5) {
               w.getUp(2).Pillar(4, new Random(), new Material[]{Material.LIGHT_GRAY_STAINED_GLASS_PANE});
               w.getUp(2).CorrectMultipleFacing(4);
               (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getUp().getFront());
               (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setFacing(w.getDirection()).setOpen(true).apply(w.getUp().getFront(2));
               (new TrapdoorBuilder(Material.DARK_OAK_TRAPDOOR)).setFacing(w.getDirection()).apply(w.getUp().getFront().getUp());
            }

            if (i == 2 || i == (Integer)entry.getValue() - 3) {
               w.getFront().getUp().setType(Material.COBBLESTONE);
               w.getFront().getUp(2).Pillar(3, new Random(), new Material[]{Material.STONE_BRICK_WALL});
               w.getFront().getUp(2).CorrectMultipleFacing(3);
               (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getFront().getUp(5));
            }

            if (i == 4) {
               this.spawnWindowOverhang(w.getFront().getUp(6));
            }
            break;
         case 2:
            if (i == 4) {
               w.getUp().getLeft().Pillar(3, new Material[]{Material.AIR});
               w.getUp().getRight().Pillar(3, new Material[]{Material.AIR});
               w.getUp().Pillar(4, new Material[]{Material.AIR});
               (new StairBuilder(Material.DARK_OAK_STAIRS)).setHalf(Half.TOP).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp(5)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getLeft().getUp(4)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRight().getUp(4));
               w.getUp(6).getFront().setType(Material.COBBLESTONE_SLAB);
               (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.TOP).apply(w.getUp(5).getFront().getLeft()).apply(w.getUp(5).getFront().getRight());
               (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getUp(6)).apply(w.getRight().getUp(5)).apply(w.getLeft().getUp(5));
               w.getUp().getRight(2).getFront().setType(Material.POLISHED_ANDESITE);
               w.getUp(2).getRight(2).getFront().Pillar(2, new Material[]{Material.STONE_BRICK_WALL});
               w.getUp(2).getRight(2).getFront().CorrectMultipleFacing(2);
               w.getUp().getLeft(2).getFront().setType(Material.POLISHED_ANDESITE);
               w.getUp(2).getLeft(2).getFront().Pillar(2, new Material[]{Material.STONE_BRICK_WALL});
               w.getUp(2).getLeft(2).getFront().CorrectMultipleFacing(2);
               (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp().getRight(2).getFront(2)).apply(w.getRight(2).getFront().getUp(4)).apply(w.getUp().getLeft(2).getFront(2)).apply(w.getLeft(2).getFront().getUp(4)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getUp().getRight(3).getFront()).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getUp().getLeft(3).getFront());
            } else if (i == 1 || i == (Integer)entry.getValue() - 2) {
               w.getUp().Pillar(this.getRoom().getHeight(), new Material[]{Material.DARK_OAK_LOG});
               (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setHalf(Half.TOP).setFacing(w.getDirection().getOppositeFace()).apply(w.getFront().getUp(5));
               w.getFront().getUp(6).setType(Material.LANTERN);
            }
         }

         w = w.getLeft();
      }

      entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      w = ((Wall)entry.getKey()).getDown();

      for(i = 0; i < (Integer)entry.getValue(); ++i) {
         Wall target = w.getRelative(0, this.getRoom().getHeight(), 0);
         if (target.getUp().getType() == Material.DARK_OAK_LOG) {
            target.getUp().setType(Material.AIR);
         }

         if (target.findCeiling(10) != null) {
            int spawnedHeight = target.getUp().LPillar(10, new Random(), new Material[]{target.getType()});
            if (!this.isTentRoofFace && spawnedHeight == 0 && target.getUp().getFront().isAir() && Tag.STAIRS.isTagged(target.getUp().getType())) {
               StairBuilder builder = (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(target.getDirection().getOppositeFace()).lapply(target.getFront());

               for(int depth = 1; depth <= 2; ++depth) {
                  if (Tag.STAIRS.isTagged(target.getLeft(depth).getUp().getType()) && target.getLeft(depth).getUp().getFront().isAir()) {
                     builder.lapply(target.getLeft(depth).getFront());
                  }

                  if (Tag.STAIRS.isTagged(target.getRight(depth).getUp().getType()) && target.getRight(depth).getUp().getFront().isAir()) {
                     builder.lapply(target.getRight(depth).getFront());
                  }
               }
            }
         }

         w = w.getLeft();
      }

   }

   private void spawnWallSupportingPillar(@NotNull Wall w, int height) {
      w.Pillar(height, new Random(), new Material[]{Material.POLISHED_ANDESITE});
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getFront());
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRelative(BlockUtils.getLeft(w.getDirection())));
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getRelative(BlockUtils.getRight(w.getDirection())));
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getRelative(0, height - 1, 0));
      w.getUp(2).setType(Material.STONE_BRICK_WALL);
      w.getUp(3).setType(Material.POLISHED_DIORITE);
      w.getUp(4).setType(Material.STONE_BRICK_WALL);
      w.getUp(2).CorrectMultipleFacing(3);
   }

   private void spawnWindowOverhang(@NotNull Wall w) {
      (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getRight(w.getDirection()))).apply(w).apply(w.getLeft()).apply(w.getRight());
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setHalf(Half.TOP).setFacing(w.getDirection().getOppositeFace()).apply(w.getFront());
      (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.TOP).apply(w.getFront().getLeft()).apply(w.getFront().getRight());
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setHalf(Half.TOP).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getDown().getLeft()).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getDown().getRight());
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getRight(2)).apply(w.getRight().getUp());
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getLeft(2)).apply(w.getLeft().getUp());
      (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getUp());
   }

   private static enum MansionSecondFloorWallType {
      THIN_WINDOWS,
      LARGE_WINDOW,
      BALCONY;

      // $FF: synthetic method
      private static MansionSecondFloorWallPiece.MansionSecondFloorWallType[] $values() {
         return new MansionSecondFloorWallPiece.MansionSecondFloorWallType[]{THIN_WINDOWS, LARGE_WINDOW, BALCONY};
      }
   }
}
