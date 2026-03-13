package org.terraform.structure.village.plains.forge;

import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.StairwayBuilder;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class PlainsVillageForgeEntrancePiece extends PlainsVillageForgePiece {
   public PlainsVillageForgeEntrancePiece(PlainsVillagePopulator plainsVillagePopulator, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(plainsVillagePopulator, widthX, height, widthZ, type, validDirs);
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w = w.getLeft();
      }

      Wall core = new Wall(new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY() + 1, this.getRoom().getZ()), this.getRotation());
      core = core.getRear(2);
      if (core.getFront().isSolid()) {
         (new StairwayBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setAngled(true).setStopAtWater(true).setStairwayDirection(BlockFace.UP).build(core.getFront(3));
         core.getFront().Pillar(2, rand, new Material[]{Material.AIR});
         core.getFront(2).Pillar(3, rand, new Material[]{Material.AIR});
      } else {
         (new StairwayBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setAngled(true).setStopAtWater(true).build(core.getFront().getDown());
      }

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
            w = w.getLeft();
         }

         Wall core = new Wall(new SimpleBlock(data, this.getRoom().getX(), this.getRoom().getY() + 1, this.getRoom().getZ()), this.getRotation());
         core = core.getRear(2);
         core.getDown().setType(Material.CHISELED_STONE_BRICKS);
         BlockUtils.placeDoor(data, this.plainsVillagePopulator.woodDoor, core.getX(), core.getY(), core.getZ(), core.getDirection().getOppositeFace());
         core.getUp(2).getFront().setType(Material.STONE_BRICK_SLAB);
         core.getUp(2).setType(Material.CHISELED_STONE_BRICKS);
         core.getLeft().Pillar(2, rand, new Material[]{Material.CHISELED_STONE_BRICKS});
         core.getRight().Pillar(2, rand, new Material[]{Material.CHISELED_STONE_BRICKS});
         core.getLeft().getFront().setType(Material.STONE_BRICK_WALL);
         core.getRight().getFront().setType(Material.STONE_BRICK_WALL);
         core.getLeft().getFront().getDown().setType(Material.STONE_BRICKS);
         core.getRight().getFront().getDown().setType(Material.STONE_BRICKS);
         (new StairBuilder(Material.STONE_BRICK_STAIRS)).setFacing(BlockUtils.getLeft(core.getDirection())).apply(core.getRight().getFront().getUp()).setFacing(BlockUtils.getRight(core.getDirection())).apply(core.getLeft().getFront().getUp()).setHalf(Half.TOP).apply(core.getRight().getUp(2)).setFacing(BlockUtils.getLeft(core.getDirection())).apply(core.getLeft().getUp(2));
      } else {
         entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
         w = (Wall)entry.getKey();

         for(i = 0; i < (Integer)entry.getValue(); ++i) {
            w.getDown(2).downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
            if (i == 2) {
               w.getDown().setType(Material.CHISELED_STONE_BRICKS);
            } else if (i != 1 && i != 3) {
               w.get().lsetType(this.plainsVillagePopulator.woodFence);
               w.CorrectMultipleFacing(1);
               (new OrientableBuilder(this.plainsVillagePopulator.woodLog)).setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getLeft(w.getDirection()))).apply(w.getDown());
            } else {
               w.getDown().Pillar(2, rand, new Material[]{this.plainsVillagePopulator.woodLog});
               w.getUp().setType(new Material[]{Material.STONE_SLAB, Material.COBBLESTONE_SLAB, Material.ANDESITE_SLAB});
            }

            w = w.getLeft();
         }
      }

   }
}
