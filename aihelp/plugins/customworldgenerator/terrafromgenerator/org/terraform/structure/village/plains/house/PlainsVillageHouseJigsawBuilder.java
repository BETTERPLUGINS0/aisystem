package org.terraform.structure.village.plains.house;

import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;

public class PlainsVillageHouseJigsawBuilder extends JigsawBuilder {
   final PlainsVillageHouseVariant var;
   final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageHouseJigsawBuilder(PlainsVillagePopulator plainsVillagePopulator, int widthX, int widthZ, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      super(widthX, widthZ, data, x, y, z);
      this.plainsVillagePopulator = plainsVillagePopulator;
      this.var = PlainsVillageHouseVariant.roll(new Random());
      this.pieceRegistry = new JigsawStructurePiece[]{new PlainsVillageBedroomPiece(plainsVillagePopulator, this.var, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageKitchenPiece(plainsVillagePopulator, this.var, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageLibraryPiece(plainsVillagePopulator, this.var, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageWallPiece(plainsVillagePopulator, this.var, 5, 3, 5, JigsawType.END, BlockUtils.directBlockFaces), new PlainsVillageEntrancePiece(plainsVillagePopulator, this.var, 5, 3, 5, JigsawType.ENTRANCE, BlockUtils.directBlockFaces)};
      this.chanceToAddNewPiece = 30;
   }

   @NotNull
   public JigsawStructurePiece getFirstPiece(@NotNull Random random) {
      return new PlainsVillageBedroomPiece(this.plainsVillagePopulator, this.var, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces);
   }

   public void build(@NotNull Random random) {
      super.build(random);
      Iterator var2 = this.pieces.values().iterator();

      JigsawStructurePiece piece;
      while(var2.hasNext()) {
         piece = (JigsawStructurePiece)var2.next();
         SimpleBlock core = new SimpleBlock(this.core.getPopData(), piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
         Material[] fenceType = new Material[]{this.plainsVillagePopulator.woodFence};
         Material cornerType = this.plainsVillagePopulator.woodLog;
         if (this.var == PlainsVillageHouseVariant.COBBLESTONE) {
            fenceType = new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL};
         } else if (this.var == PlainsVillageHouseVariant.CLAY) {
            fenceType = new Material[]{Material.STONE_BRICK_WALL, Material.MOSSY_STONE_BRICK_WALL};
            cornerType = this.plainsVillagePopulator.woodStrippedLog;
         }

         Wall target;
         if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
            target = new Wall(core.getRelative(-3, 0, -3));
            this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.WEST, cornerType, fenceType);
         }

         if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
            target = new Wall(core.getRelative(3, 0, -3));
            this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.EAST, cornerType, fenceType);
         }

         if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
            target = new Wall(core.getRelative(-3, 0, 3));
            this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.WEST, cornerType, fenceType);
         }

         if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
            target = new Wall(core.getRelative(3, 0, 3));
            this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.EAST, cornerType, fenceType);
         }
      }

      if (!PlainsVillageRoofHandler.isRectangle(this)) {
         PlainsVillageRoofHandler.placeStandardRoof(this.plainsVillagePopulator, this);
      } else {
         PlainsVillageRoofHandler.placeTentRoof(this.plainsVillagePopulator, random, this);
      }

      var2 = this.pieces.values().iterator();

      while(var2.hasNext()) {
         piece = (JigsawStructurePiece)var2.next();
         piece.postBuildDecoration(random, this.core.getPopData());
      }

   }

   public void decorateAwkwardCorner(@NotNull Wall target, @NotNull Random random, @NotNull BlockFace one, @NotNull BlockFace two, Material cornerType, Material[] fenceType) {
      target.Pillar(4, random, new Material[]{cornerType});
      target.getDown().downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
      target = target.getUp();
      target.getRelative(one).Pillar(3, random, fenceType);
      target.getRelative(two).Pillar(3, random, fenceType);
      target.getRelative(one).CorrectMultipleFacing(3);
      target.getRelative(two).CorrectMultipleFacing(3);
      target = target.getDown();
      target.getRelative(one).downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
      target.getRelative(two).downUntilSolid(random, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
   }

   public PlainsVillageHouseVariant getVariant() {
      return this.var;
   }
}
