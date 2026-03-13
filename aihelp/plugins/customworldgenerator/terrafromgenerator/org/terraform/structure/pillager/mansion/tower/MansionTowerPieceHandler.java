package org.terraform.structure.pillager.mansion.tower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleLocation;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class MansionTowerPieceHandler {
   public static final int towerPieceWidth = 7;
   @NotNull
   public final HashMap<SimpleLocation, JigsawStructurePiece> pieces = new HashMap();
   @NotNull
   public final ArrayList<JigsawStructurePiece> overlapperPieces = new ArrayList();
   private final MansionJigsawBuilder builder;
   private final PopulatorDataAbstract data;

   public MansionTowerPieceHandler(MansionJigsawBuilder builder, PopulatorDataAbstract data) {
      this.builder = builder;
      this.data = data;
   }

   public int registerTowerPiece(@NotNull Random rand, @NotNull JigsawStructurePiece piece) {
      int height = GenUtils.randInt(rand, 1, 2);

      for(int i = 1; i <= height; ++i) {
         JigsawStructurePiece newPiece;
         if (i == 1) {
            newPiece = (new MansionBaseTowerPiece(this.builder, 7, 7, 7, JigsawType.STANDARD, BlockUtils.directBlockFaces)).getInstance(new Random(), 0);
         } else {
            newPiece = (new MansionStandardTowerPiece(this.builder, 7, 7, 7, JigsawType.STANDARD, BlockUtils.directBlockFaces)).getInstance(new Random(), 0);
         }

         if (i == height) {
            ((MansionStandardTowerPiece)newPiece).setHighestPieceInTower(true);
         }

         newPiece.getRoom().setX(piece.getRoom().getX());
         newPiece.getRoom().setY(piece.getRoom().getY() + i * 7);
         newPiece.getRoom().setZ(piece.getRoom().getZ());
         this.pieces.put(newPiece.getRoom().getSimpleLocation(), newPiece);
      }

      return height;
   }

   public void setupWalls() {
      Iterator var1 = this.pieces.values().iterator();

      while(var1.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var1.next();
         ArrayList<BlockFace> faces = new ArrayList();
         BlockFace[] var4 = BlockUtils.directBlockFaces;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            BlockFace face = var4[var6];
            if (!this.pieces.containsKey(piece.getRoom().getSimpleLocation().getRelative(face, 7))) {
               JigsawStructurePiece newWall;
               if (piece instanceof MansionBaseTowerPiece) {
                  newWall = (new MansionBaseTowerWallPiece(this.builder, 7, 7, 7, JigsawType.STANDARD, BlockUtils.directBlockFaces)).getInstance(new Random(), 0);
               } else {
                  newWall = (new MansionLookoutTowerWallPiece(this.builder, 7, 7, 7, JigsawType.STANDARD, BlockUtils.directBlockFaces)).getInstance(new Random(), 0);
               }

               newWall.getRoom().setX(piece.getRoom().getX() + face.getModX() * 7);
               newWall.getRoom().setY(piece.getRoom().getY());
               newWall.getRoom().setZ(piece.getRoom().getZ() + face.getModZ() * 7);
               newWall.setRotation(face);
               this.overlapperPieces.add(newWall);
               faces.add(face);
            }
         }

         piece.setWalledFaces(faces);
      }

   }

   public void buildPieces(Random rand) {
      JigsawStructurePiece piece;
      for(Iterator var2 = this.pieces.values().iterator(); var2.hasNext(); piece.build(this.builder.getCore().getPopData(), rand)) {
         piece = (JigsawStructurePiece)var2.next();
         int[] lowerCorner = piece.getRoom().getLowerCorner(0);
         int[] upperCorner = piece.getRoom().getUpperCorner(0);
         int lowestY = piece.getRoom().getY() + 1;
         int upperY = piece.getRoom().getY() + piece.getRoom().getHeight();

         for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               for(int y = lowestY; y < upperY; ++y) {
                  this.builder.getCore().getPopData().setType(x, y, z, Material.AIR);
               }
            }
         }

         if (piece instanceof MansionStandardTowerPiece) {
            ((MansionStandardTowerPiece)piece).decorateAwkwardCorners(rand);
         }
      }

   }

   public void buildRoofs(BlockFace roofFacing, Random rand) {
      Iterator var3 = this.pieces.values().iterator();

      while(var3.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var3.next();
         if (((MansionStandardTowerPiece)piece).isHighestPieceInTower()) {
            ((MansionStandardTowerPiece)piece).placeTentRoof(this.data, roofFacing, rand);
         }
      }

   }

   public void buildOverlapperPieces(Random rand) {
      Iterator var2 = this.overlapperPieces.iterator();

      while(var2.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var2.next();
         piece.build(this.builder.getCore().getPopData(), rand);
      }

   }
}
