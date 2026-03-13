package org.terraform.structure.pillager.mansion.secondfloor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.structure.pillager.mansion.MansionJigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.utils.BlockUtils;

public class MansionSecondFloorHandler {
   @NotNull
   public final HashMap<SimpleLocation, JigsawStructurePiece> secondFloorPieces = new HashMap();
   @NotNull
   public final ArrayList<JigsawStructurePiece> secondFloorOverlapperPieces = new ArrayList();
   private final MansionJigsawBuilder builder;
   private Random random;

   public MansionSecondFloorHandler(MansionJigsawBuilder builder) {
      this.builder = builder;
      this.random = new Random();
   }

   public void decorateAwkwardCorners() {
      Iterator var1 = this.secondFloorPieces.values().iterator();

      while(var1.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var1.next();
         SimpleBlock core = new SimpleBlock(this.builder.getCore().getPopData(), piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
         Wall target;
         if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
            target = new Wall(core.getRelative(-5, 1, -5));
            this.decorateAwkwardCorner(target, this.random, BlockFace.NORTH, BlockFace.WEST);
         }

         if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
            target = new Wall(core.getRelative(5, 1, -5));
            this.decorateAwkwardCorner(target, this.random, BlockFace.NORTH, BlockFace.EAST);
         }

         if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
            target = new Wall(core.getRelative(-5, 1, 5));
            this.decorateAwkwardCorner(target, this.random, BlockFace.SOUTH, BlockFace.WEST);
         }

         if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
            target = new Wall(core.getRelative(5, 1, 5));
            this.decorateAwkwardCorner(target, this.random, BlockFace.SOUTH, BlockFace.EAST);
         }
      }

   }

   public void decorateAwkwardCorner(@NotNull Wall target, Random random, BlockFace one, BlockFace two) {
      target.Pillar(7, new Material[]{Material.DARK_OAK_LOG});
   }

   public void populateSecondFloorRoomLayout() {
      Iterator var1 = this.builder.getPieces().values().iterator();

      while(var1.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var1.next();
         JigsawStructurePiece newPiece = (new MansionStandardSecondFloorPiece(this.builder, 9, 7, 9, JigsawType.STANDARD, BlockUtils.directBlockFaces)).getInstance(new Random(), 0);
         newPiece.getRoom().setX(piece.getRoom().getX());
         newPiece.getRoom().setY(piece.getRoom().getY() + 7 + 1);
         newPiece.getRoom().setZ(piece.getRoom().getZ());
         ArrayList<BlockFace> faces = new ArrayList();
         Iterator var5 = piece.getWalledFaces().iterator();

         while(var5.hasNext()) {
            BlockFace face = (BlockFace)var5.next();
            JigsawStructurePiece newWall = (new MansionSecondFloorWallPiece(this.builder, 9, 7, 9, JigsawType.STANDARD, BlockUtils.directBlockFaces)).getInstance(new Random(), 0);
            newWall.getRoom().setX(piece.getRoom().getX() + face.getModX() * 9);
            newWall.getRoom().setY(piece.getRoom().getY() + 7 + 1);
            newWall.getRoom().setZ(piece.getRoom().getZ() + face.getModZ() * 9);
            newWall.setRotation(face);
            this.secondFloorOverlapperPieces.add(newWall);
            faces.add(face);
         }

         newPiece.setWalledFaces(faces);
         this.secondFloorPieces.put(newPiece.getRoom().getSimpleLocation(), newPiece);
      }

   }

   public void buildSecondFloor(Random random) {
      Iterator var2 = this.secondFloorPieces.values().iterator();

      while(var2.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var2.next();
         int[] lowerCorner = piece.getRoom().getLowerCorner(0);
         int[] upperCorner = piece.getRoom().getUpperCorner(0);
         int lowestY = piece.getRoom().getY() + 1;
         int upperY = piece.getRoom().getY() + piece.getRoom().getHeight();

         for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               for(int y = lowestY; y <= upperY; ++y) {
                  this.builder.getCore().getPopData().setType(x, y, z, Material.AIR);
               }
            }
         }

         piece.build(this.builder.getCore().getPopData(), random);
      }

      ArrayList<JigsawStructurePiece> toRemove = new ArrayList();
      Collections.shuffle(this.secondFloorOverlapperPieces);
      Iterator var12 = this.secondFloorOverlapperPieces.iterator();

      while(var12.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var12.next();
         SimpleLocation pieceLoc = new SimpleLocation(piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
         if (this.secondFloorPieces.containsKey(pieceLoc)) {
            toRemove.add(piece);
         } else {
            JigsawStructurePiece host = this.builder.getAdjacentPiece(pieceLoc, piece.getRotation().getOppositeFace());
            if (host != null) {
               host.getWalledFaces().add(piece.getRotation());
            }

            piece.build(this.builder.getCore().getPopData(), random);
         }
      }

      ArrayList var10000 = this.secondFloorOverlapperPieces;
      Objects.requireNonNull(toRemove);
      var10000.removeIf(toRemove::contains);
   }

   public void setRandom(Random random) {
      this.random = random;
   }
}
