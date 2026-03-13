package org.terraform.structure.village.plains.temple;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.config.TConfig;
import org.terraform.structure.room.jigsaw.JigsawBuilder;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageTempleJigsawBuilder extends JigsawBuilder {
   final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageTempleJigsawBuilder(PlainsVillagePopulator plainsVillagePopulator, int widthX, int widthZ, @NotNull PopulatorDataAbstract data, int x, int y, int z) {
      super(widthX, widthZ, data, x, y, z);
      this.plainsVillagePopulator = plainsVillagePopulator;
      this.pieceRegistry = new JigsawStructurePiece[]{new PlainsVillageTempleLoungePiece(plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageTempleRelicPiece(plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, true, BlockUtils.directBlockFaces), new PlainsVillageTempleLootPiece(plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, BlockUtils.directBlockFaces), new PlainsVillageTempleWallPiece(5, 3, 5, JigsawType.END, BlockUtils.directBlockFaces), new PlainsVillageTempleEntrancePiece(plainsVillagePopulator, 5, 3, 5, JigsawType.ENTRANCE, BlockUtils.directBlockFaces)};
      this.chanceToAddNewPiece = 50;
   }

   protected static boolean hasAdjacentWall(@NotNull JigsawStructurePiece piece, @NotNull BlockFace face, @NotNull ArrayList<JigsawStructurePiece> overlapperPieces) {
      Iterator var3 = overlapperPieces.iterator();

      JigsawStructurePiece other;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         other = (JigsawStructurePiece)var3.next();
      } while(!other.getRoom().getSimpleLocation().equals(piece.getRoom().getSimpleLocation().getRelative(face, 5)) || other.getRotation() != piece.getRotation());

      return true;
   }

   protected static boolean hasAdjacentInwardWall(@NotNull JigsawStructurePiece piece, @NotNull BlockFace face, @NotNull ArrayList<JigsawStructurePiece> overlapperPieces) {
      Iterator var3 = overlapperPieces.iterator();

      JigsawStructurePiece other;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         other = (JigsawStructurePiece)var3.next();
      } while(!other.getRoom().getSimpleLocation().equals(piece.getRoom().getSimpleLocation()) || other.getRotation() != face.getOppositeFace());

      return true;
   }

   @NotNull
   public JigsawStructurePiece getFirstPiece(@NotNull Random random) {
      return new PlainsVillageTempleClericAltarPiece(this.plainsVillagePopulator, 5, 3, 5, JigsawType.STANDARD, true, this, BlockUtils.directBlockFaces);
   }

   public void build(@NotNull Random random) {
      if (TConfig.areStructuresEnabled()) {
         super.build(random);
         Iterator var2 = this.pieces.values().iterator();

         while(var2.hasNext()) {
            JigsawStructurePiece piece = (JigsawStructurePiece)var2.next();
            SimpleBlock core = new SimpleBlock(this.core.getPopData(), piece.getRoom().getX(), piece.getRoom().getY(), piece.getRoom().getZ());
            Wall target;
            if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
               target = new Wall(core.getRelative(-3, 0, -3));
               this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.WEST);
            }

            if (piece.getWalledFaces().contains(BlockFace.NORTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
               target = new Wall(core.getRelative(3, 0, -3));
               this.decorateAwkwardCorner(target, random, BlockFace.NORTH, BlockFace.EAST);
            }

            if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.WEST)) {
               target = new Wall(core.getRelative(-3, 0, 3));
               this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.WEST);
            }

            if (piece.getWalledFaces().contains(BlockFace.SOUTH) && piece.getWalledFaces().contains(BlockFace.EAST)) {
               target = new Wall(core.getRelative(3, 0, 3));
               this.decorateAwkwardCorner(target, random, BlockFace.SOUTH, BlockFace.EAST);
            }
         }

         int randIndex = random.nextInt(this.pieces.size());
         int i = 0;
         Iterator var12 = this.pieces.values().iterator();

         JigsawStructurePiece wallPiece;
         while(var12.hasNext()) {
            wallPiece = (JigsawStructurePiece)var12.next();
            if (i == randIndex) {
               ((PlainsVillageTempleStandardPiece)wallPiece).setTower(true);
            }
         }

         var12 = this.overlapperPieces.iterator();

         while(var12.hasNext()) {
            wallPiece = (JigsawStructurePiece)var12.next();
            PlainsVillageTempleRoofHandler.handleTempleRoof(this.plainsVillagePopulator, this.core.getPopData(), wallPiece, this.overlapperPieces);
         }

         var12 = this.overlapperPieces.iterator();

         while(var12.hasNext()) {
            wallPiece = (JigsawStructurePiece)var12.next();
            BlockFace[] var6 = BlockUtils.getAdjacentFaces(wallPiece.getRotation());
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               BlockFace face = var6[var8];
               if (hasAdjacentWall(wallPiece, face, this.overlapperPieces)) {
                  PlainsVillageTempleWallPiece.setLargeWindow(this.core.getPopData(), wallPiece.getRotation(), wallPiece.getRoom(), face);
               }
            }
         }

         PlainsVillageTempleRoofHandler.placeCeilingTerracotta(this.core.getPopData(), this.pieces.values());
         var12 = this.pieces.values().iterator();

         while(var12.hasNext()) {
            wallPiece = (JigsawStructurePiece)var12.next();
            wallPiece.postBuildDecoration(random, this.core.getPopData());
         }

      }
   }

   public void decorateAwkwardCorner(@NotNull Wall target, @NotNull Random random, @NotNull BlockFace one, @NotNull BlockFace two) {
      Material[] cobblestone = new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE};
      Material[] stoneBricks = new Material[]{Material.STONE_BRICKS, Material.STONE_BRICKS, Material.STONE_BRICKS, Material.CRACKED_STONE_BRICKS};
      target.Pillar(5, random, BlockUtils.stoneBricks);
      target.getDown().downUntilSolid(random, cobblestone);
      target = target.getUp();
      target.getRelative(one).Pillar(3, random, stoneBricks);
      (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(one.getOppositeFace()).apply(target.getRelative(one).getUp(3));
      target.getRelative(two).Pillar(3, random, stoneBricks);
      (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(two.getOppositeFace()).apply(target.getRelative(two).getUp(3));
      target = target.getDown();
      target.getRelative(one).downUntilSolid(random, cobblestone);
      target.getRelative(two).downUntilSolid(random, cobblestone);
   }
}
