package org.terraform.structure.village.plains.forge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;

public class PlainsVillageForgeJigsawBuilder extends JigsawBuilder {
   private final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageForgeJigsawBuilder(PlainsVillagePopulator plainsVillagePopulator, int widthX, int widthZ, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      super(widthX, widthZ, data, x, y, z);
      this.plainsVillagePopulator = plainsVillagePopulator;
      this.pieceRegistry = new JigsawStructurePiece[]{new PlainsVillageForgeWeaponSmithPiece(plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageForgeMasonPiece(plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageForgeWallPiece(plainsVillagePopulator, 5, 3, 5, JigsawType.END, BlockUtils.directBlockFaces), new PlainsVillageForgeEntrancePiece(plainsVillagePopulator, 5, 3, 5, JigsawType.ENTRANCE, BlockUtils.directBlockFaces)};
      this.chanceToAddNewPiece = 50;
   }

   @NotNull
   public JigsawStructurePiece getFirstPiece(@NotNull Random random) {
      return new PlainsVillageForgeChimneyPiece(this.plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces);
   }

   public void build(@NotNull Random random) {
      super.build(random);
      ArrayList<SimpleLocation> rectanglePieces = PlainsVillageForgeRoofHandler.identifyRectangle(this.pieces);
      ArrayList<JigsawStructurePiece> builtWalls = new ArrayList();
      Iterator var4 = this.pieces.values().iterator();

      JigsawStructurePiece piece;
      while(var4.hasNext()) {
         piece = (JigsawStructurePiece)var4.next();
         PlainsVillageForgeWallPiece.PlainsVillageForgeWallType wallType = PlainsVillageForgeWallPiece.PlainsVillageForgeWallType.FENCE;
         if (rectanglePieces.contains(piece.getRoom().getSimpleLocation())) {
            wallType = PlainsVillageForgeWallPiece.PlainsVillageForgeWallType.SOLID;
         }

         ((PlainsVillageForgePiece)piece).setWallType(wallType);
         BlockFace[] var7 = BlockUtils.directBlockFaces;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            BlockFace face = var7[var9];
            JigsawStructurePiece wall = this.getAdjacentWall(piece.getRoom().getSimpleLocation(), face);
            if (wall != null && !builtWalls.contains(wall)) {
               builtWalls.add(wall);
               ((PlainsVillageForgePiece)wall).setWallType(wallType);
            }
         }
      }

      var4 = this.pieces.values().iterator();

      while(var4.hasNext()) {
         piece = (JigsawStructurePiece)var4.next();
         SimpleBlock core = new SimpleBlock(this.core.getPopData(), piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
         PlainsVillageForgeWallPiece.PlainsVillageForgeWallType type = PlainsVillageForgeWallPiece.PlainsVillageForgeWallType.FENCE;
         if (rectanglePieces.contains(piece.getRoom().getSimpleLocation())) {
            type = PlainsVillageForgeWallPiece.PlainsVillageForgeWallType.SOLID;
         }

         Wall target;
         if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
            target = new Wall(core.getRelative(-3, 0, -3));
            this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.WEST, type);
         }

         if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
            target = new Wall(core.getRelative(3, 0, -3));
            this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.EAST, type);
         }

         if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
            target = new Wall(core.getRelative(-3, 0, 3));
            this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.WEST, type);
         }

         if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
            target = new Wall(core.getRelative(3, 0, 3));
            this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.EAST, type);
         }
      }

      PlainsVillageForgeRoofHandler.placeRoof(this.plainsVillagePopulator, this.core, rectanglePieces);
      var4 = this.overlapperPieces.iterator();

      while(var4.hasNext()) {
         piece = (JigsawStructurePiece)var4.next();
         piece.postBuildDecoration(random, this.core.getPopData());
      }

      var4 = this.pieces.values().iterator();

      while(var4.hasNext()) {
         piece = (JigsawStructurePiece)var4.next();
         piece.postBuildDecoration(random, this.core.getPopData());
      }

   }

   public void decorateAwkwardCorner(@NotNull Wall target, @NotNull Random random, BlockFace one, BlockFace two, PlainsVillageForgeWallPiece.PlainsVillageForgeWallType wallType) {
      if (wallType == PlainsVillageForgeWallPiece.PlainsVillageForgeWallType.SOLID) {
         target.Pillar(4, random, new Material[]{this.plainsVillagePopulator.woodLog});
         target.getDown().downUntilSolid(random, new Material[]{this.plainsVillagePopulator.woodLog});
         target.getUp();
      } else {
         target.Pillar(2, random, new Material[]{this.plainsVillagePopulator.woodLog});
         target.getUp(2).setType(new Material[]{Material.STONE_SLAB, Material.COBBLESTONE_SLAB, Material.ANDESITE_SLAB});
         target.getDown().downUntilSolid(random, new Material[]{this.plainsVillagePopulator.woodLog});
      }

   }
}
