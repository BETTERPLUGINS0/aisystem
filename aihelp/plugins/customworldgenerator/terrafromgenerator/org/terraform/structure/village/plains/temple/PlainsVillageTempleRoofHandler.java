package org.terraform.structure.village.plains.temple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Lantern;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.DirectionalBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageTempleRoofHandler {
   public static void handleTempleRoof(@NotNull PlainsVillagePopulator plainsVillagePopulator, @NotNull PopulatorDataAbstract data, @NotNull JigsawStructurePiece piece, @NotNull ArrayList<JigsawStructurePiece> wallPieces) {
      Wall base = new Wall(new SimpleBlock(data, piece.getRoom().getX(), piece.getRoom().getY() + 5, piece.getRoom().getZ()), piece.getRotation());
      BlockFace[] var5 = BlockUtils.getAdjacentFaces(piece.getRotation());
      int var6 = var5.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         BlockFace face = var5[var7];
         byte multiplier;
         if (PlainsVillageTempleJigsawBuilder.hasAdjacentWall(piece, face, wallPieces)) {
            multiplier = 0;
         } else if (PlainsVillageTempleJigsawBuilder.hasAdjacentInwardWall(piece, face, wallPieces)) {
            multiplier = 1;
         } else {
            multiplier = -1;
         }

         for(int height = 0; height < 3; ++height) {
            for(int horDepth = 0; horDepth < 3 + height * multiplier; ++horDepth) {
               Wall w = base.getRelative(face, horDepth);
               (new StairBuilder(Material.COBBLESTONE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getRear(height + 2).getRelative(0, height * 2, 0)).correct();
               Wall pillar;
               Lantern l;
               if (multiplier == -1 && horDepth == 2 + height * multiplier) {
                  pillar = w.getRelative(face).getRear(height + 2).getRelative(0, height * 2, 0);
                  pillar.Pillar(3, new Random(), new Material[]{Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
                  pillar.setType(Material.CHISELED_STONE_BRICKS);
                  pillar = w.getRear(height + 3).getRelative(0, (height + 1) * 2, 0);
                  pillar.getDown(2).setType(plainsVillagePopulator.woodLog);
                  l = (Lantern)Bukkit.createBlockData(Material.LANTERN);
                  l.setHanging(true);
                  pillar.getDown(3).setBlockData(l);
               } else if (multiplier == 1 && horDepth == 2 + height * multiplier) {
                  pillar = w.getRelative(face, 1).getRear(height + 3).getRelative(0, (height + 1) * 2, 0);
                  pillar.getDown().get().lsetType(plainsVillagePopulator.woodLog);
                  pillar.getDown(2).setType(plainsVillagePopulator.woodLog);
                  l = (Lantern)Bukkit.createBlockData(Material.LANTERN);
                  l.setHanging(true);
                  pillar.getDown(3).setBlockData(l);
               }

               if (height != 2) {
                  (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(w.getDirection()).setHalf(Half.TOP).lapply(w.getRear(height + 3).getRelative(0, height * 2, 0)).correct();
               } else {
                  (new OrientableBuilder(plainsVillagePopulator.woodLog)).setAxis(BlockUtils.getAxisFromBlockFace(face)).lapply(w.getRear(height + 3).getRelative(0, height * 2, 0));
               }

               w.getRelative(0, height * 2 + 1, 0).getRear(height + 3).setType(Material.POLISHED_ANDESITE);
            }
         }
      }

   }

   public static void placeCeilingTerracotta(@NotNull PopulatorDataAbstract data, @NotNull Collection<JigsawStructurePiece> structurePieces) {
      Material glazedTerracotta = BlockUtils.GLAZED_TERRACOTTA[(new Random()).nextInt(BlockUtils.GLAZED_TERRACOTTA.length)];
      Iterator var3 = structurePieces.iterator();

      while(var3.hasNext()) {
         JigsawStructurePiece piece = (JigsawStructurePiece)var3.next();
         int[] lowerCorner = piece.getRoom().getLowerCorner();
         int[] upperCorner = piece.getRoom().getUpperCorner();

         for(int x = lowerCorner[0]; x <= upperCorner[0]; ++x) {
            for(int z = lowerCorner[1]; z <= upperCorner[1]; ++z) {
               SimpleBlock b = new SimpleBlock(data, x, piece.getRoom().getY() + 1, z);

               int i;
               for(i = 0; i < 9 && !b.isSolid(); ++i) {
                  b = b.getUp();
               }

               if (i == 9 && !b.isSolid()) {
                  placeGlazedTerracotta(b, glazedTerracotta);
                  b.getUp().setType(Material.POLISHED_ANDESITE);
               }
            }
         }
      }

   }

   private static void placeGlazedTerracotta(@NotNull SimpleBlock target, @NotNull Material glazedTerracotta) {
      BlockFace dir;
      if (target.getX() % 2 == 0) {
         if (target.getZ() % 2 == 0) {
            dir = BlockFace.SOUTH;
         } else {
            dir = BlockFace.WEST;
         }
      } else if (target.getZ() % 2 == 0) {
         dir = BlockFace.EAST;
      } else {
         dir = BlockFace.NORTH;
      }

      (new DirectionalBuilder(glazedTerracotta)).setFacing(dir).apply(target);
   }
}
