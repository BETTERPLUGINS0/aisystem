package org.terraform.structure.room.jigsaw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class JigsawBuilder {
   protected final int widthX;
   protected final int widthZ;
   @NotNull
   protected final int[] lowerBounds = new int[2];
   @NotNull
   protected final int[] upperBounds = new int[2];
   protected final int maxDepth = 5;
   protected final SimpleBlock core;
   @NotNull
   protected final Stack<JigsawStructurePiece> traverseStack = new Stack();
   @NotNull
   protected final HashMap<SimpleLocation, JigsawStructurePiece> pieces = new HashMap();
   @NotNull
   protected final ArrayList<JigsawStructurePiece> overlapperPieces = new ArrayList();
   protected int chanceToAddNewPiece = 60;
   protected int minimumPieces = 0;
   protected int pieceWidth = 5;
   @Nullable
   protected JigsawStructurePiece center;
   protected Wall entranceBlock;
   protected JigsawStructurePiece[] pieceRegistry;
   protected BlockFace entranceDir;
   protected boolean hasPlacedEntrance = false;
   int traversalIndex = 0;

   public JigsawBuilder(int widthX, int widthZ, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      this.widthX = widthX;
      this.widthZ = widthZ;
      this.core = new SimpleBlock(data, x, y, z);
      this.lowerBounds[0] = x - widthX / 2;
      this.lowerBounds[1] = z - widthZ / 2;
      this.upperBounds[0] = x + widthX / 2;
      this.upperBounds[1] = z + widthZ / 2;
   }

   public void forceEntranceDirection(BlockFace face) {
      this.entranceDir = face;
   }

   public BlockFace getEntranceDirection() {
      return this.entranceDir;
   }

   @Nullable
   public JigsawStructurePiece getFirstPiece(@NotNull Random random) {
      return this.getPiece(this.pieceRegistry, JigsawType.STANDARD, random).getInstance(random, 0);
   }

   public void generate(@NotNull Random random) {
      this.center = this.getFirstPiece(random);
      this.center.getRoom().setX(this.core.getX());
      this.center.getRoom().setY(this.core.getY());
      this.center.getRoom().setZ(this.core.getZ());
      this.pieces.put(new SimpleLocation(this.core.getX(), this.core.getY(), this.core.getZ()), this.center);
      this.traverseStack.push(this.center);

      while(!this.areAllPiecesCovered() && this.traverseAndPopulatePieces(random)) {
      }

      ArrayList<SimpleLocation> problemCells = new ArrayList();
      HashMap<SimpleLocation, Integer> map = new HashMap();
      Iterator var4 = this.overlapperPieces.iterator();

      while(var4.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var4.next();
         if (map.containsKey(piece.getRoom().getSimpleLocation())) {
            map.put(piece.getRoom().getSimpleLocation(), (Integer)map.get(piece.getRoom().getSimpleLocation()) + 1);
         } else {
            map.put(piece.getRoom().getSimpleLocation(), 1);
         }

         if ((Integer)map.get(piece.getRoom().getSimpleLocation()) >= 4) {
            problemCells.add(piece.getRoom().getSimpleLocation());
            TerraformGeneratorPlugin.logger.info("Found problem piece. Attempting to replace with room.");
         }
      }

      if (!problemCells.isEmpty()) {
         this.overlapperPieces.removeIf((piecex) -> {
            return problemCells.contains(piecex.getRoom().getSimpleLocation());
         });
         var4 = problemCells.iterator();

         while(var4.hasNext()) {
            SimpleLocation loc = (SimpleLocation)var4.next();
            JigsawStructurePiece toAdd = this.getPiece(this.pieceRegistry, JigsawType.STANDARD, random).getInstance(random, 0);
            toAdd.getRoom().setX(loc.getX());
            toAdd.getRoom().setY(loc.getY());
            toAdd.getRoom().setZ(loc.getZ());
            toAdd.setPopulated(BlockFace.NORTH);
            toAdd.setPopulated(BlockFace.SOUTH);
            toAdd.setPopulated(BlockFace.EAST);
            toAdd.setPopulated(BlockFace.WEST);
            this.pieces.put(loc, toAdd);
            TerraformGeneratorPlugin.logger.info("Patched problem piece with new room.");
         }
      }

   }

   public boolean traverseAndPopulatePieces(@NotNull Random random) {
      if (this.traverseStack.isEmpty()) {
         TerraformGeneratorPlugin.logger.info("Jigsaw stack size empty!");
         return false;
      } else {
         JigsawStructurePiece current = (JigsawStructurePiece)this.traverseStack.peek();
         ++this.traversalIndex;
         if (this.traversalIndex > 200) {
            TerraformGeneratorPlugin.logger.error("Infinite loop detected! Breaking.");
            return false;
         } else {
            if (current.hasUnpopulatedDirections()) {
               BlockFace dir = current.getNextUnpopulatedBlockFace();
               int toAddX = current.getRoom().getX() + this.pieceWidth * dir.getModX();
               int toAddY = current.getRoom().getY() + this.pieceWidth * dir.getModY();
               int toAddZ = current.getRoom().getZ() + this.pieceWidth * dir.getModZ();
               SimpleLocation newLoc = new SimpleLocation(toAddX, toAddY, toAddZ);
               if (!this.pieces.containsKey(newLoc)) {
                  JigsawStructurePiece toAdd;
                  if (dir == BlockFace.UP) {
                     toAdd = this.getRelativePiece(current, JigsawType.UPPERCONNECTOR, random).getInstance(random, current.getDepth() + 1);
                     toAdd.setRotation(BlockUtils.getDirectBlockFace(random));
                     toAdd.setElevation(current.getElevation() + 1);
                  } else if (current.getDepth() >= 5) {
                     toAdd = this.getRelativePiece(current, JigsawType.END, random).getInstance(random, current.getDepth() + 1);
                     toAdd.setRotation(dir);
                  } else if (toAddX - this.pieceWidth / 2 >= this.lowerBounds[0] && toAddX + this.pieceWidth / 2 <= this.upperBounds[0] && toAddZ - this.pieceWidth / 2 >= this.lowerBounds[1] && toAddZ + this.pieceWidth / 2 <= this.upperBounds[1]) {
                     if (this.pieces.size() > this.minimumPieces && !GenUtils.chance(random, this.chanceToAddNewPiece, 100)) {
                        toAdd = this.getRelativePiece(current, JigsawType.END, random).getInstance(random, current.getDepth() + 1);
                        toAdd.setRotation(dir);
                     } else {
                        toAdd = this.getRelativePiece(current, JigsawType.STANDARD, random).getInstance(random, current.getDepth() + 1);
                     }
                  } else {
                     toAdd = this.getRelativePiece(current, JigsawType.END, random).getInstance(random, current.getDepth() + 1);
                     toAdd.setRotation(dir);
                  }

                  toAdd.getRoom().setX(toAddX);
                  toAdd.getRoom().setY(toAddY);
                  toAdd.getRoom().setZ(toAddZ);
                  current.setPopulated(dir);
                  toAdd.setPopulated(dir.getOppositeFace());
                  if (toAdd.getType() == JigsawType.END) {
                     this.overlapperPieces.add(toAdd);
                  }

                  if (toAdd.getType() != JigsawType.END) {
                     this.pieces.put(newLoc, toAdd);
                     this.traverseStack.push(toAdd);
                  }
               } else {
                  JigsawStructurePiece other = (JigsawStructurePiece)this.pieces.get(newLoc);
                  current.setPopulated(dir);
                  other.setPopulated(dir.getOppositeFace());
               }
            } else {
               this.traverseStack.pop();
            }

            return true;
         }
      }
   }

   public boolean areAllPiecesCovered() {
      Iterator var1 = this.pieces.values().iterator();

      JigsawStructurePiece piece;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         piece = (JigsawStructurePiece)var1.next();
      } while(!piece.hasUnpopulatedDirections());

      return false;
   }

   public SimpleBlock getCore() {
      return this.core;
   }

   @NotNull
   public HashMap<SimpleLocation, JigsawStructurePiece> getPieces() {
      return this.pieces;
   }

   public void build(@NotNull Random random) {
      Iterator var2 = this.pieces.values().iterator();

      JigsawStructurePiece entrance;
      while(var2.hasNext()) {
         entrance = (JigsawStructurePiece)var2.next();
         entrance.getRoom().purgeRoomContents(this.core.getPopData(), 0);
         entrance.build(this.core.getPopData(), random);
      }

      ArrayList<JigsawStructurePiece> toRemove = new ArrayList();
      entrance = null;
      Collections.shuffle(this.overlapperPieces);
      Iterator it = this.overlapperPieces.iterator();

      while(true) {
         JigsawStructurePiece piece;
         while(it.hasNext()) {
            piece = (JigsawStructurePiece)it.next();
            SimpleLocation pieceLoc = new SimpleLocation(piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
            if (this.pieces.containsKey(pieceLoc)) {
               toRemove.add(piece);
            } else {
               if (!this.hasPlacedEntrance && (this.entranceDir == null || piece.getRotation() == this.entranceDir) && this.canPlaceEntrance(pieceLoc)) {
                  this.hasPlacedEntrance = true;
                  entrance = this.getPiece(this.pieceRegistry, JigsawType.ENTRANCE, random).getInstance(random, piece.getDepth());
                  entrance.getRoom().setX(piece.getRoom().getX());
                  entrance.getRoom().setY(piece.getRoom().getY());
                  entrance.getRoom().setZ(piece.getRoom().getZ());
                  entrance.setRotation(piece.getRotation());
                  this.entranceBlock = new Wall(new SimpleBlock(this.core.getPopData(), piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ()), piece.getRotation());
                  piece = entrance;
               }

               JigsawStructurePiece host = this.getAdjacentPiece(pieceLoc, piece.getRotation().getOppositeFace());
               if (host != null) {
                  host.getWalledFaces().add(piece.getRotation());
               }

               piece.build(this.core.getPopData(), random);
            }
         }

         it = this.overlapperPieces.iterator();

         while(it.hasNext()) {
            piece = (JigsawStructurePiece)it.next();
            if (toRemove.contains(piece)) {
               it.remove();
            } else if (piece.getRoom().getSimpleLocation().equals(entrance.getRoom().getSimpleLocation()) && piece.getRotation() == entrance.getRotation()) {
               it.remove();
            }
         }

         this.overlapperPieces.add(entrance);
         return;
      }
   }

   public boolean canPlaceEntrance(SimpleLocation pieceLoc) {
      return this.countOverlappingPiecesAtLocation(pieceLoc) != 4;
   }

   public int countOverlappingPiecesAtLocation(SimpleLocation loc) {
      int count = 0;
      Iterator var3 = this.overlapperPieces.iterator();

      while(var3.hasNext()) {
         JigsawStructurePiece wall = (JigsawStructurePiece)var3.next();
         if (wall.getRoom().getSimpleLocation().equals(loc)) {
            ++count;
         }
      }

      return count;
   }

   public JigsawStructurePiece getAdjacentPiece(@NotNull SimpleLocation loc, @NotNull BlockFace face) {
      SimpleLocation other = new SimpleLocation(loc.getX() + face.getModX() * this.pieceWidth, loc.getY() + face.getModY() * this.pieceWidth, loc.getZ() + face.getModZ() * this.pieceWidth);
      return (JigsawStructurePiece)this.pieces.get(other);
   }

   @Nullable
   public JigsawStructurePiece getAdjacentWall(@NotNull SimpleLocation loc, @NotNull BlockFace face) {
      SimpleLocation other = new SimpleLocation(loc.getX() + face.getModX() * this.pieceWidth, loc.getY() + face.getModY() * this.pieceWidth, loc.getZ() + face.getModZ() * this.pieceWidth);
      Iterator var4 = this.overlapperPieces.iterator();

      JigsawStructurePiece wall;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         wall = (JigsawStructurePiece)var4.next();
      } while(wall.getRotation() != face || !wall.getRoom().getSimpleLocation().equals(other));

      return wall;
   }

   @Nullable
   public JigsawStructurePiece getPiece(JigsawStructurePiece[] registry, JigsawType type, @NotNull Random rand) {
      ArrayList<JigsawStructurePiece> validPieces = new ArrayList();
      JigsawStructurePiece[] var5 = this.pieceRegistry;
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         JigsawStructurePiece piece = var5[var7];
         boolean dontPlace = false;
         if (piece.getType() == type) {
            if (piece.isUnique()) {
               Iterator var10 = this.pieces.values().iterator();

               while(var10.hasNext()) {
                  JigsawStructurePiece present = (JigsawStructurePiece)var10.next();
                  if (present.getClass().equals(piece.getClass())) {
                     dontPlace = true;
                     break;
                  }
               }
            }

            if (!dontPlace) {
               validPieces.add(piece);
            }
         }
      }

      if (validPieces.isEmpty()) {
         TerraformGeneratorPlugin.logger.error("Tried to query jigsaw type that doesn't exist: " + String.valueOf(type));
         return null;
      } else {
         return (JigsawStructurePiece)validPieces.get(rand.nextInt(validPieces.size()));
      }
   }

   @Nullable
   public JigsawStructurePiece getRelativePiece(@Nullable JigsawStructurePiece current, JigsawType type, @NotNull Random random) {
      return current != null && (current.getAllowedPieces() == null || current.getAllowedPieces().length != 0) ? this.getPiece(current.getAllowedPieces(), type, random) : this.getPiece(this.pieceRegistry, type, random);
   }

   public int getPieceWidth() {
      return this.pieceWidth;
   }

   public Wall getEntranceBlock() {
      return this.entranceBlock;
   }

   @NotNull
   public ArrayList<JigsawStructurePiece> getOverlapperPieces() {
      return this.overlapperPieces;
   }
}
