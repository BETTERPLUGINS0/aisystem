package org.terraform.structure.pillager.mansion;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.Map.Entry;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MansionMazeAlgoUtil {
   @Nullable
   private static MansionStandardRoomPiece getStartingPiece(@NotNull Collection<JigsawStructurePiece> pieces) {
      Iterator var1 = pieces.iterator();
      if (var1.hasNext()) {
         JigsawStructurePiece p = (JigsawStructurePiece)var1.next();
         return (MansionStandardRoomPiece)p;
      } else {
         return null;
      }
   }

   public static void setupPathways(@NotNull Collection<JigsawStructurePiece> pieces, @NotNull Random rand) {
      int n = pieces.size();
      Stack<MansionStandardRoomPiece> cellStack = new Stack();
      MansionStandardRoomPiece currentCell = getStartingPiece(pieces);
      int nv = 1;

      while(nv < n) {
         Map<BlockFace, MansionStandardRoomPiece> neighbours = getValidNeighbours(pieces, currentCell);
         if (neighbours.isEmpty()) {
            if (cellStack.isEmpty()) {
               break;
            }

            currentCell = (MansionStandardRoomPiece)cellStack.pop();
         } else {
            Entry<BlockFace, MansionStandardRoomPiece> entry = (Entry)neighbours.entrySet().toArray()[rand.nextInt(neighbours.size())];
            if (currentCell.internalWalls.get(entry.getKey()) == MansionInternalWallState.SOLID) {
               currentCell.internalWalls.put((BlockFace)entry.getKey(), MansionInternalWallState.ROOM_ENTRANCE);
               MansionStandardRoomPiece otherPiece = (MansionStandardRoomPiece)currentCell.adjacentPieces.get(entry.getKey());
               if (otherPiece.internalWalls.containsKey(((BlockFace)entry.getKey()).getOppositeFace())) {
                  otherPiece.internalWalls.put(((BlockFace)entry.getKey()).getOppositeFace(), MansionInternalWallState.ROOM_ENTRANCE);
               }
            }

            cellStack.push(currentCell);
            currentCell = (MansionStandardRoomPiece)entry.getValue();
            ++nv;
         }
      }

   }

   public static void knockdownRandomWalls(@NotNull Collection<JigsawStructurePiece> pieces, @NotNull Random rand) {
      Iterator var2 = pieces.iterator();

      while(var2.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var2.next();
         MansionStandardRoomPiece spiece = (MansionStandardRoomPiece)piece;
         Iterator var5 = spiece.getShuffledInternalWalls().iterator();

         while(var5.hasNext()) {
            BlockFace face = (BlockFace)var5.next();
            if (spiece.internalWalls.get(face) != MansionInternalWallState.WINDOW && spiece.internalWalls.get(face) != MansionInternalWallState.EXIT && GenUtils.chance(rand, 1, 10)) {
               ((MansionStandardRoomPiece)spiece.adjacentPieces.get(face)).internalWalls.put(face.getOppositeFace(), MansionInternalWallState.ROOM_ENTRANCE);
               spiece.internalWalls.put(face, MansionInternalWallState.ROOM_ENTRANCE);
            }
         }
      }

   }

   @NotNull
   private static Map<BlockFace, MansionStandardRoomPiece> getValidNeighbours(Collection<JigsawStructurePiece> pieces, @NotNull MansionStandardRoomPiece piece) {
      Map<BlockFace, MansionStandardRoomPiece> neighbours = new EnumMap(BlockFace.class);
      BlockFace[] var3 = BlockUtils.directBlockFaces;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         BlockFace face = var3[var5];
         if (piece.adjacentPieces.containsKey(face)) {
            MansionStandardRoomPiece neighbour = (MansionStandardRoomPiece)piece.adjacentPieces.get(face);
            if (neighbour != null && neighbour.areInternalWallsFullyBlocked()) {
               neighbours.put(face, neighbour);
            }
         }
      }

      return neighbours;
   }
}
