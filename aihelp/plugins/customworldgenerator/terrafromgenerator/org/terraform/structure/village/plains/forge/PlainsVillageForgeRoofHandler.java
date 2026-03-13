package org.terraform.structure.village.plains.forge;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;

public class PlainsVillageForgeRoofHandler {
   private static final int pieceWidth = 5;

   public static void placeRoof(@NotNull PlainsVillagePopulator plainsVillagePopulator, @NotNull SimpleBlock core, @NotNull ArrayList<SimpleLocation> rectangleLocations) {
      SimpleLocation lowerBound = null;
      SimpleLocation upperBound = null;
      Material roofCornerMaterial = (Material)GenUtils.randChoice((Object[])(Material.STONE_BRICKS, Material.COBBLESTONE));
      Material roofSlabCornerMaterial = Material.STONE_BRICK_SLAB;
      if (roofCornerMaterial == Material.COBBLESTONE) {
         roofSlabCornerMaterial = Material.COBBLESTONE_SLAB;
      }

      Iterator var7 = rectangleLocations.iterator();

      while(var7.hasNext()) {
         SimpleLocation sLoc = (SimpleLocation)var7.next();
         if (lowerBound == null) {
            lowerBound = sLoc;
         }

         if (upperBound == null) {
            upperBound = sLoc;
         }

         if (lowerBound.getX() >= sLoc.getX() && lowerBound.getZ() >= sLoc.getZ()) {
            lowerBound = sLoc;
         }

         if (upperBound.getX() <= sLoc.getX() && upperBound.getZ() <= sLoc.getZ()) {
            upperBound = sLoc;
         }
      }

      lowerBound = lowerBound.getRelative(-4, 0, -4);
      upperBound = upperBound.getRelative(4, 0, 4);
      Axis roofAxis = Axis.X;
      if (upperBound.getZ() - lowerBound.getZ() > upperBound.getX() - lowerBound.getX()) {
         roofAxis = Axis.Z;
      }

      for(int x = lowerBound.getX(); x <= upperBound.getX(); ++x) {
         for(int z = lowerBound.getZ(); z <= upperBound.getZ(); ++z) {
            double percent;
            if (roofAxis == Axis.X) {
               percent = ((double)x - (double)lowerBound.getX()) / ((double)upperBound.getX() - (double)lowerBound.getX());
            } else {
               percent = ((double)z - (double)lowerBound.getZ()) / ((double)upperBound.getZ() - (double)lowerBound.getZ());
            }

            int height = (int)(-12.0D * Math.pow(percent - 0.5D, 2.0D) + 3.0D);
            SimpleBlock target = new SimpleBlock(core.getPopData(), x, core.getY() + 4 + (int)((double)height / 2.0D), z);
            if (height % 2 != 1) {
               if (x != lowerBound.getX() && x != upperBound.getX() && z != lowerBound.getZ() && z != upperBound.getZ()) {
                  (new SlabBuilder(plainsVillagePopulator.woodSlab)).apply(target);
               } else {
                  (new SlabBuilder(roofSlabCornerMaterial)).apply(target);
               }
            } else {
               if (x != lowerBound.getX() && x != upperBound.getX() && z != lowerBound.getZ() && z != upperBound.getZ()) {
                  target.setType(plainsVillagePopulator.woodPlank);
               } else {
                  target.setType(roofCornerMaterial);
                  Wall adj = new Wall(target, BlockUtils.getBlockFaceFromAxis(roofAxis));
                  (new SlabBuilder(roofSlabCornerMaterial)).setType(Type.TOP).lapply(adj.getFront()).lapply(adj.getRear());
               }

               if (height == 3) {
                  (new OrientableBuilder(plainsVillagePopulator.woodLog)).setAxis(BlockUtils.getPerpendicularHorizontalPlaneAxis(roofAxis)).apply(target);
                  if ((roofAxis == Axis.X && (z == lowerBound.getZ() + 1 || z == upperBound.getZ() - 1) || roofAxis == Axis.Z && (x == lowerBound.getX() + 1 || x == upperBound.getX() - 1)) && (BlockUtils.isStoneLike(target.getDown(2).getType()) || target.getDown(2).getType() == plainsVillagePopulator.woodDoor)) {
                     (new Wall(target.getDown())).downUntilSolid(new Random(), new Material[]{Material.STONE, Material.COBBLESTONE, Material.ANDESITE});
                  }
               }
            }
         }
      }

   }

   @NotNull
   public static ArrayList<SimpleLocation> identifyRectangle(@NotNull HashMap<SimpleLocation, JigsawStructurePiece> pieces) {
      ArrayList<SimpleLocation> rectangleList = new ArrayList();
      SimpleLocation cornerLoc = null;
      Iterator var3 = pieces.keySet().iterator();
      if (var3.hasNext()) {
         SimpleLocation loc = (SimpleLocation)var3.next();
         cornerLoc = loc;
      }

      BlockFace sideToMove = BlockUtils.getDirectBlockFace(new Random());

      JigsawStructurePiece target;
      for(target = getAdjacentPiece(pieces, cornerLoc, sideToMove); target != null; target = getAdjacentPiece(pieces, cornerLoc, sideToMove)) {
         cornerLoc = target.getRoom().getSimpleLocation();
      }

      for(target = (JigsawStructurePiece)pieces.get(cornerLoc); target != null; target = getAdjacentPiece(pieces, cornerLoc, sideToMove.getOppositeFace())) {
         cornerLoc = target.getRoom().getSimpleLocation();
         rectangleList.add(target.getRoom().getSimpleLocation());
      }

      sideToMove = BlockUtils.getTurnBlockFace(new Random(), sideToMove);
      int shortestLength = 99;
      Iterator var6 = rectangleList.iterator();

      while(var6.hasNext()) {
         SimpleLocation pLoc = (SimpleLocation)var6.next();
         JigsawStructurePiece piece = (JigsawStructurePiece)pieces.get(pLoc);
         int expansionLength = 0;

         for(piece = getAdjacentPiece(pieces, piece.getRoom().getSimpleLocation(), sideToMove); piece != null; piece = getAdjacentPiece(pieces, piece.getRoom().getSimpleLocation(), sideToMove)) {
            ++expansionLength;
         }

         if (expansionLength < shortestLength) {
            shortestLength = expansionLength;
         }

         if (expansionLength == 0) {
            break;
         }
      }

      Collection<SimpleLocation> toAdd = new ArrayList();
      Iterator var14 = rectangleList.iterator();

      while(var14.hasNext()) {
         SimpleLocation pLoc = (SimpleLocation)var14.next();
         JigsawStructurePiece piece = (JigsawStructurePiece)pieces.get(pLoc);
         piece = getAdjacentPiece(pieces, piece.getRoom().getSimpleLocation(), sideToMove);

         for(int i = 0; i < shortestLength; ++i) {
            toAdd.add(piece.getRoom().getSimpleLocation());
            piece = getAdjacentPiece(pieces, piece.getRoom().getSimpleLocation(), sideToMove);
         }
      }

      rectangleList.addAll(toAdd);
      return rectangleList;
   }

   private static JigsawStructurePiece getAdjacentPiece(@NotNull HashMap<SimpleLocation, JigsawStructurePiece> pieces, @NotNull SimpleLocation loc, @NotNull BlockFace face) {
      SimpleLocation other = new SimpleLocation(loc.getX() + face.getModX() * 5, loc.getY() + face.getModY() * 5, loc.getZ() + face.getModZ() * 5);
      return (JigsawStructurePiece)pieces.get(other);
   }
}
