package org.terraform.structure.village.plains.forge;

import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.SlabBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageForgeWallPiece extends PlainsVillageForgePiece {
   public PlainsVillageForgeWallPiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, validDirs);
   }

   public void build(PopulatorDataAbstract data, Random rand) {
   }

   public void postBuildDecoration(@NotNull Random rand, @NotNull PopulatorDataAbstract data) {
      SimpleEntry entry;
      Wall w;
      int i;
      if (this.getWallType() == PlainsVillageForgeWallPiece.PlainsVillageForgeWallType.SOLID) {
         entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
         w = ((Wall)entry.getKey()).getDown();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            w.getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            w.RPillar(5, rand, new Material[]{Material.COBBLESTONE, Material.ANDESITE, Material.STONE});
            if (i == 2) {
               w.getUp(2).setType(Material.AIR);
               (new SlabBuilder(new Material[]{Material.COBBLESTONE_SLAB, Material.ANDESITE_SLAB, Material.STONE_SLAB, Material.STONE_BRICK_SLAB})).setType(Type.TOP).apply(w.getUp(3).getFront());
               (new StairBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.ANDESITE_STAIRS, Material.STONE_STAIRS, Material.STONE_BRICK_STAIRS})).setFacing(w.getDirection().getOppositeFace()).apply(w.getUp().getFront());
               w.getFront().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            } else if (i == 1 || i == 3) {
               w.getUp(3).getFront().setType(new Material[]{Material.COBBLESTONE_SLAB, Material.ANDESITE_SLAB, Material.STONE_SLAB, Material.STONE_BRICK_SLAB});
               w.getUp(2).getFront().setType(new Material[]{Material.COBBLESTONE_WALL, Material.STONE_BRICK_WALL, Material.ANDESITE_WALL});
               if (i == 1) {
                  (new StairBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.ANDESITE_STAIRS, Material.STONE_STAIRS, Material.STONE_BRICK_STAIRS})).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getUp(2)).setFacing(BlockUtils.getLeft(w.getDirection())).setHalf(Half.TOP).apply(w.getUp().getFront());
               } else {
                  (new StairBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.ANDESITE_STAIRS, Material.STONE_STAIRS, Material.STONE_BRICK_STAIRS})).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getUp(2)).setFacing(BlockUtils.getRight(w.getDirection())).setHalf(Half.TOP).apply(w.getUp().getFront());
               }
            }

            w = w.getLeft();
         }
      } else {
         entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
         w = (Wall)entry.getKey();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            w.getDown(2).downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            if (i == 2) {
               w.getDown().Pillar(2, rand, new Material[]{this.plainsVillagePopulator.woodLog});
               w.getUp().setType(new Material[]{Material.STONE_SLAB, Material.COBBLESTONE_SLAB, Material.ANDESITE_SLAB});
            } else {
               w.get().lsetType(this.plainsVillagePopulator.woodFence);
               w.CorrectMultipleFacing(1);
               (new OrientableBuilder(this.plainsVillagePopulator.woodLog)).setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getLeft(w.getDirection()))).apply(w.getDown());
            }

            w = w.getLeft();
         }
      }

   }

   public static enum PlainsVillageForgeWallType {
      SOLID,
      FENCE;

      // $FF: synthetic method
      private static PlainsVillageForgeWallPiece.PlainsVillageForgeWallType[] $values() {
         return new PlainsVillageForgeWallPiece.PlainsVillageForgeWallType[]{SOLID, FENCE};
      }
   }
}
