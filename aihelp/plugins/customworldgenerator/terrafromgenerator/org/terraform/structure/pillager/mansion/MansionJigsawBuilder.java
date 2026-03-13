package org.terraform.structure.pillager.mansion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.structure.pillager.mansion.ground.MansionEntrancePiece;
import org.terraform.structure.pillager.mansion.ground.MansionGrandStairwayPopulator;
import org.terraform.structure.pillager.mansion.ground.MansionGroundRoomPiece;
import org.terraform.structure.pillager.mansion.ground.MansionGroundWallPiece;
import org.terraform.structure.pillager.mansion.ground.MansionStandardGroundRoomPiece;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorGrandStairwayPopulator;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorHandler;
import org.terraform.structure.pillager.mansion.secondfloor.MansionSecondFloorWallPiece;
import org.terraform.structure.pillager.mansion.secondfloor.MansionTowerStairwayPopulator;
import org.terraform.structure.pillager.mansion.tower.MansionTowerPieceHandler;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.room.jigsaw.JigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionJigsawBuilder extends JigsawBuilder {
   public static final int roomHeight = 7;
   public static final int groundFloorRoomWidth = 9;
   @NotNull
   private final ArrayList<SimpleLocation> roofedLocations = new ArrayList();
   private final MansionTowerPieceHandler towerPieceHandler;
   private final MansionSecondFloorHandler secondFloorHandler;

   public MansionJigsawBuilder(int widthX, int widthZ, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      super(widthX, widthZ, data, x, y, z);
      this.towerPieceHandler = new MansionTowerPieceHandler(this, data);
      this.secondFloorHandler = new MansionSecondFloorHandler(this);
      this.pieceWidth = 9;
      this.pieceRegistry = new JigsawStructurePiece[]{new MansionGroundRoomPiece(9, 7, 9, JigsawType.STANDARD, BlockUtils.directBlockFaces), new MansionGroundWallPiece(this, 9, 7, 9, JigsawType.END, BlockUtils.directBlockFaces), new MansionEntrancePiece(this, 9, 7, 9, JigsawType.ENTRANCE, BlockUtils.directBlockFaces)};
      this.chanceToAddNewPiece = 90;
      this.minimumPieces = 15;
   }

   public JigsawStructurePiece getFirstPiece(@NotNull Random random) {
      return new MansionGroundRoomPiece(9, 7, 9, JigsawType.STANDARD, BlockUtils.directBlockFaces);
   }

   public void build(@NotNull Random random) {
      Iterator var2 = this.pieces.values().iterator();

      JigsawStructurePiece piece;
      while(var2.hasNext()) {
         piece = (JigsawStructurePiece)var2.next();
         ((MansionStandardGroundRoomPiece)piece).purgeMinimalArea(this.core.getPopData());
      }

      super.build(random);
      var2 = this.pieces.values().iterator();

      while(true) {
         SimpleBlock core;
         Wall target;
         do {
            do {
               if (!var2.hasNext()) {
                  var2 = this.overlapperPieces.iterator();

                  while(var2.hasNext()) {
                     piece = (JigsawStructurePiece)var2.next();
                     piece.postBuildDecoration(random, this.core.getPopData());
                  }

                  var2 = this.pieces.values().iterator();

                  while(var2.hasNext()) {
                     piece = (JigsawStructurePiece)var2.next();
                     piece.postBuildDecoration(random, this.core.getPopData());
                  }

                  var2 = this.pieces.values().iterator();

                  while(var2.hasNext()) {
                     piece = (JigsawStructurePiece)var2.next();
                     MansionStandardGroundRoomPiece mansionPiece = (MansionStandardGroundRoomPiece)piece;
                     mansionPiece.thirdStageDecoration(random, this.core.getPopData());
                  }

                  this.secondFloorHandler.setRandom(random);
                  this.secondFloorHandler.populateSecondFloorRoomLayout();
                  this.secondFloorHandler.buildSecondFloor(random);
                  this.secondFloorHandler.decorateAwkwardCorners();
                  int[][] bounds = MansionRoofHandler.getLargestRectangle(this);
                  int[] lowerBounds = new int[]{bounds[0][0], bounds[0][1]};
                  int[] upperBounds = new int[]{bounds[1][0], bounds[1][1]};
                  if (MansionRoofHandler.getDominantAxis(lowerBounds, upperBounds) == Axis.X) {
                     lowerBounds[0] -= 7;
                     upperBounds[0] += 7;
                     lowerBounds[1] -= 4;
                     upperBounds[1] += 4;
                  } else {
                     lowerBounds[1] -= 7;
                     upperBounds[1] += 7;
                     lowerBounds[0] -= 4;
                     upperBounds[0] += 4;
                  }

                  Iterator var13 = this.secondFloorHandler.secondFloorOverlapperPieces.iterator();

                  JigsawStructurePiece piece;
                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     if (piece instanceof MansionSecondFloorWallPiece) {
                        ((MansionSecondFloorWallPiece)piece).buildIndividualRoofs(random, this.core.getPopData(), lowerBounds, upperBounds);
                     }
                  }

                  MansionRoofHandler.placeTentRoof(random, this, bounds);
                  var13 = this.secondFloorHandler.secondFloorOverlapperPieces.iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     piece.postBuildDecoration(random, this.core.getPopData());
                  }

                  var13 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     piece.postBuildDecoration(random, this.core.getPopData());
                  }

                  var13 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     if (!this.getRoofedLocations().contains(piece.getRoom().getSimpleLocation())) {
                        SimpleLocation loc = piece.getRoom().getSimpleLocation().getUp(13);
                        if (this.core.getPopData().getType(loc.getX(), loc.getY(), loc.getZ()) != Material.COBBLESTONE_SLAB) {
                           int towerHeight = this.towerPieceHandler.registerTowerPiece(random, piece);
                           ((MansionStandardRoomPiece)piece).setRoomPopulator(new MansionTowerStairwayPopulator(piece.getRoom(), ((MansionStandardRoomPiece)piece).internalWalls, towerHeight));
                        }
                     }
                  }

                  this.towerPieceHandler.setupWalls();
                  this.towerPieceHandler.buildPieces(random);
                  this.towerPieceHandler.buildOverlapperPieces(random);
                  var13 = this.towerPieceHandler.overlapperPieces.iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     piece.postBuildDecoration(random, this.core.getPopData());
                  }

                  var13 = this.towerPieceHandler.pieces.values().iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     piece.postBuildDecoration(random, this.core.getPopData());
                  }

                  this.towerPieceHandler.buildRoofs(MansionRoofHandler.getDominantBlockFace(lowerBounds, upperBounds), random);
                  var13 = this.pieces.values().iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     ((MansionStandardRoomPiece)piece).setupInternalAttributes(this.core.getPopData(), this.getPieces());
                  }

                  MansionMazeAlgoUtil.setupPathways(this.pieces.values(), random);
                  MansionMazeAlgoUtil.knockdownRandomWalls(this.pieces.values(), random);
                  MansionCompoundRoomDistributor.distributeRooms(this.pieces.values(), random, true);
                  var13 = this.pieces.values().iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     ((MansionStandardRoomPiece)piece).buildWalls(random, this.core.getPopData());
                  }

                  var13 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var13.hasNext()) {
                     piece = (JigsawStructurePiece)var13.next();
                     ((MansionStandardRoomPiece)piece).setupInternalAttributes(this.core.getPopData(), this.secondFloorHandler.secondFloorPieces);
                  }

                  MansionMazeAlgoUtil.setupPathways(this.secondFloorHandler.secondFloorPieces.values(), random);
                  MansionMazeAlgoUtil.knockdownRandomWalls(this.secondFloorHandler.secondFloorPieces.values(), random);
                  Iterator var16 = this.pieces.values().iterator();

                  JigsawStructurePiece piece;
                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     if (((MansionStandardRoomPiece)piece).getRoomPopulator() instanceof MansionGrandStairwayPopulator && ((MansionStandardRoomPiece)piece).isPopulating()) {
                        MansionStandardRoomPiece secondFloorStairwayCenter = (MansionStandardRoomPiece)this.secondFloorHandler.secondFloorPieces.get(piece.getRoom().getSimpleLocation().getRelative(0, 8, 0));
                        MansionRoomPopulator secondFloorGrandStairwayPopulator = (new MansionSecondFloorGrandStairwayPopulator((CubeRoom)null, (HashMap)null)).getInstance(secondFloorStairwayCenter.getRoom(), secondFloorStairwayCenter.internalWalls);
                        if (!MansionCompoundRoomDistributor.canRoomSizeFitWithCenter(secondFloorStairwayCenter, this.secondFloorHandler.secondFloorPieces.values(), new MansionRoomSize(3, 3), secondFloorGrandStairwayPopulator, true)) {
                           TerraformGeneratorPlugin.logger.info("[!] Failed to allocate second floor grand stairway space!");
                        }

                        secondFloorStairwayCenter.setRoomPopulator(secondFloorGrandStairwayPopulator);
                     }
                  }

                  MansionCompoundRoomDistributor.distributeRooms(this.secondFloorHandler.secondFloorPieces.values(), random, false);
                  var16 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).buildWalls(random, this.core.getPopData());
                  }

                  var16 = this.pieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).decorateInternalRoom(random, this.core.getPopData());
                  }

                  var16 = this.pieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).decorateWalls(random, this.core.getPopData());
                  }

                  var16 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).decorateInternalRoom(random, this.core.getPopData());
                     MansionRoofHandler.atticDecorations(random, this.core.getPopData(), piece);
                  }

                  var16 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).decorateWalls(random, this.core.getPopData());
                  }

                  MansionStandardRoomPiece.spawnedGuards = 0;
                  var16 = this.pieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).spawnGuards(random, this.core.getPopData());
                  }

                  var16 = this.secondFloorHandler.secondFloorPieces.values().iterator();

                  while(var16.hasNext()) {
                     piece = (JigsawStructurePiece)var16.next();
                     ((MansionStandardRoomPiece)piece).spawnGuards(random, this.core.getPopData());
                  }

                  TerraformGeneratorPlugin.logger.info("Mansion spawned " + MansionStandardRoomPiece.spawnedGuards + " vindicators and evokers");
                  return;
               }

               piece = (JigsawStructurePiece)var2.next();
               core = new SimpleBlock(this.core.getPopData(), piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
               if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
                  target = new Wall(core.getRelative(-5, 1, -5));
                  this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.WEST, this.areOtherWallsOverlapping(piece, BlockFace.NORTH) || this.areOtherWallsOverlapping(piece, BlockFace.WEST));
               }

               if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
                  target = new Wall(core.getRelative(5, 1, -5));
                  this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.EAST, this.areOtherWallsOverlapping(piece, BlockFace.NORTH) || this.areOtherWallsOverlapping(piece, BlockFace.EAST));
               }

               if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
                  target = new Wall(core.getRelative(-5, 1, 5));
                  this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.WEST, this.areOtherWallsOverlapping(piece, BlockFace.SOUTH) || this.areOtherWallsOverlapping(piece, BlockFace.WEST));
               }
            } while(!piece.getWalledFaces().contains(BlockFace.SOUTH));
         } while(!piece.getWalledFaces().contains(BlockFace.EAST));

         target = new Wall(core.getRelative(5, 1, 5));
         this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.EAST, this.areOtherWallsOverlapping(piece, BlockFace.SOUTH) || this.areOtherWallsOverlapping(piece, BlockFace.EAST));
      }
   }

   private boolean areOtherWallsOverlapping(@NotNull JigsawStructurePiece piece, @NotNull BlockFace face) {
      SimpleLocation other = new SimpleLocation(piece.getRoom().getSimpleLocation().getX() + face.getModX() * this.pieceWidth, piece.getRoom().getSimpleLocation().getY() + face.getModY() * this.pieceWidth, piece.getRoom().getSimpleLocation().getZ() + face.getModZ() * this.pieceWidth);
      Iterator var4 = this.overlapperPieces.iterator();

      JigsawStructurePiece wall;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         wall = (JigsawStructurePiece)var4.next();
      } while(!wall.getRoom().getSimpleLocation().equals(other) || wall.getRotation() != face.getOppositeFace());

      return true;
   }

   public void decorateAwkwardCorner(@NotNull Wall target, @NotNull Random random, @NotNull BlockFace one, @NotNull BlockFace two, boolean isSinkIn) {
      if (!isSinkIn) {
         Wall largePillar = target.getRelative(one, 4).getRelative(two, 4);
         largePillar.Pillar(7, new Material[]{Material.STONE_BRICKS});
         largePillar.getDown().downUntilSolid(new Random(), new Material[]{Material.COBBLESTONE});
         largePillar.getRelative(one).downUntilSolid(new Random(), new Material[]{Material.COBBLESTONE});
         largePillar.getRelative(two).downUntilSolid(new Random(), new Material[]{Material.COBBLESTONE});
         largePillar.getRelative(one).getUp().Pillar(5, new Material[]{Material.COBBLESTONE_WALL});
         largePillar.getRelative(one).getUp().CorrectMultipleFacing(5);
         largePillar.getRelative(two).getUp().Pillar(5, new Material[]{Material.COBBLESTONE_WALL});
         largePillar.getRelative(two).getUp().CorrectMultipleFacing(5);
         largePillar.getRelative(one).getRelative(0, 6, 0).Pillar(3, new Material[]{Material.COBBLESTONE});
         largePillar.getRelative(two).getRelative(0, 6, 0).Pillar(3, new Material[]{Material.COBBLESTONE});
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(one).setHalf(Half.TOP).apply(largePillar.getRelative(0, 5, 0).getRelative(one.getOppositeFace())).apply(largePillar.getRelative(0, 6, 0).getRelative(one.getOppositeFace())).apply(largePillar.getRelative(0, 6, 0).getRelative(one.getOppositeFace(), 2));
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(two).setHalf(Half.TOP).apply(largePillar.getRelative(0, 5, 0).getRelative(two.getOppositeFace())).apply(largePillar.getRelative(0, 6, 0).getRelative(two.getOppositeFace())).apply(largePillar.getRelative(0, 6, 0).getRelative(two.getOppositeFace(), 2));
      }

      target.Pillar(7, new Material[]{Material.POLISHED_ANDESITE});
      target.getUp(2).setType(Material.STONE_BRICK_WALL);
      target.getUp(3).setType(Material.POLISHED_DIORITE);
      target.getUp(4).setType(Material.STONE_BRICK_WALL);
      target.getUp(2).CorrectMultipleFacing(3);
      target.getDown().downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(one.getOppositeFace()).apply(target.getRelative(one));
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(two.getOppositeFace()).apply(target.getRelative(two));
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(two.getOppositeFace()).apply(target.getRelative(two).getRelative(one)).correct();
      (new SlabBuilder(Material.COBBLESTONE_SLAB)).lapply(target.getRelative(one).getRelative(BlockUtils.getRight(one))).lapply(target.getRelative(one).getRelative(BlockUtils.getLeft(one)));
      (new SlabBuilder(Material.COBBLESTONE_SLAB)).lapply(target.getRelative(two).getRelative(BlockUtils.getRight(two))).lapply(target.getRelative(two).getRelative(BlockUtils.getLeft(two)));
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(one.getOppositeFace()).setHalf(Half.TOP).apply(target.getUp(6).getRelative(one));
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(two.getOppositeFace()).setHalf(Half.TOP).apply(target.getUp(6).getRelative(two));
      (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(two.getOppositeFace()).setHalf(Half.TOP).apply(target.getUp(6).getRelative(two).getRelative(one)).correct();
      (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.TOP).lapply(target.getUp(6).getRelative(one).getRelative(BlockUtils.getRight(one))).lapply(target.getUp(6).getRelative(one).getRelative(BlockUtils.getLeft(one)));
      (new SlabBuilder(Material.COBBLESTONE_SLAB)).setType(Type.TOP).lapply(target.getUp(6).getRelative(two).getRelative(BlockUtils.getRight(two))).lapply(target.getUp(6).getRelative(two).getRelative(BlockUtils.getLeft(two)));
   }

   public boolean canPlaceEntrance(SimpleLocation pieceLoc) {
      return this.countOverlappingPiecesAtLocation(pieceLoc) != 4 && this.countOverlappingPiecesAtLocation(pieceLoc) != 2;
   }

   @NotNull
   public ArrayList<SimpleLocation> getRoofedLocations() {
      return this.roofedLocations;
   }

   public MansionTowerPieceHandler getTowerPieceHandler() {
      return this.towerPieceHandler;
   }
}
