package org.terraform.structure.village.plains.house;

import java.util.Random;
import java.util.AbstractMap.SimpleEntry;
import org.bukkit.Axis;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Slab.Type;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.Wall;
import org.terraform.structure.room.jigsaw.JigsawStructurePiece;
import org.terraform.structure.room.jigsaw.JigsawType;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.StairwayBuilder;
import org.terraform.utils.blockdata.SlabBuilder;

public class PlainsVillageEntrancePiece extends JigsawStructurePiece {
   final PlainsVillageHouseVariant var;
   final PlainsVillagePopulator plainsVillagePopulator;

   public PlainsVillageEntrancePiece(PlainsVillagePopulator plainsVillagePopulator, PlainsVillageHouseVariant var, int widthX, int height, int widthZ, JigsawType type, BlockFace[] validDirs) {
      super(widthX, height, widthZ, type, validDirs);
      this.var = var;
      this.plainsVillagePopulator = plainsVillagePopulator;
   }

   public void build(@NotNull PopulatorDataAbstract data, @NotNull Random rand) {
      SimpleEntry<Wall, Integer> entry = this.getRoom().getWall(data, this.getRotation().getOppositeFace(), 0);
      Wall w = ((Wall)entry.getKey()).getDown();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         w.getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.Pillar(2, rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         if (this.var == PlainsVillageHouseVariant.CLAY) {
            w.getUp(2).Pillar(2, rand, new Material[]{Material.WHITE_TERRACOTTA});
         } else {
            w.getUp(2).Pillar(2, rand, new Material[]{this.plainsVillagePopulator.woodPlank});
         }

         w = w.getLeft();
      }

      w = w.getRight(3).getUp();
      BlockUtils.placeDoor(data, this.plainsVillagePopulator.woodDoor, w.getX(), w.getY(), w.getZ(), w.getDirection().getOppositeFace());
      if (w.getFront().isSolid()) {
         (new StairwayBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setAngled(true).setStopAtWater(true).setStairwayDirection(BlockFace.UP).build(w.getFront(4));
         w.getFront().Pillar(2, new Random(), new Material[]{Material.AIR});
         w.getFront(2).Pillar(2, new Random(), new Material[]{Material.AIR});
         w.getFront(3).Pillar(3, new Random(), new Material[]{Material.AIR});
      } else {
         (new StairwayBuilder(new Material[]{Material.COBBLESTONE_STAIRS, Material.MOSSY_COBBLESTONE_STAIRS})).setAngled(true).setStopAtWater(true).build(w.getFront().getDown());
      }

      Orientable log;
      if (this.var == PlainsVillageHouseVariant.COBBLESTONE) {
         w = w.getFront();
         w.getLeft().Pillar(2, rand, new Material[]{this.plainsVillagePopulator.woodLog});
         w.getRight().Pillar(2, rand, new Material[]{this.plainsVillagePopulator.woodLog});
         w.getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getLeft().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getRight().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         log = (Orientable)Bukkit.createBlockData(this.plainsVillagePopulator.woodLog);
         if (w.getDirection().getModZ() != 0) {
            log.setAxis(Axis.X);
         } else {
            log.setAxis(Axis.Z);
         }

         w.getUp(2).setBlockData(log);
         w.getRight().getUp(2).setBlockData(log);
         w.getLeft().getUp(2).setBlockData(log);
         w = w.getFront();
         w.getLeft().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getRight().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         (new SlabBuilder(new Material[]{Material.COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB})).setType(Type.TOP).apply(w.getUp(2));
         (new SlabBuilder(new Material[]{Material.COBBLESTONE_SLAB, Material.MOSSY_COBBLESTONE_SLAB})).setType(Type.BOTTOM).apply(w.getUp(2).getLeft()).apply(w.getUp(2).getRight());
         w.getLeft().Pillar(2, rand, new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getRight().Pillar(2, rand, new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getLeft().CorrectMultipleFacing(2);
         w.getRight().CorrectMultipleFacing(2);
      } else if (this.var == PlainsVillageHouseVariant.CLAY) {
         w.getLeft().getUp().setType(this.plainsVillagePopulator.woodLog);
         w.getRight().getUp().setType(this.plainsVillagePopulator.woodLog);
         w = w.getFront();
         w.getLeft().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getRight().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         log = (Orientable)Bukkit.createBlockData(this.plainsVillagePopulator.woodLog);
         if (w.getDirection().getModZ() != 0) {
            log.setAxis(Axis.Z);
         } else {
            log.setAxis(Axis.X);
         }

         w.getLeft().setType(this.plainsVillagePopulator.woodLog);
         w.getLeft().getUp().setType(new Material[]{Material.STONE_BRICK_WALL, Material.MOSSY_STONE_BRICK_WALL});
         w.getLeft().getUp(2).setBlockData(log);
         w.getLeft().getUp().CorrectMultipleFacing(1);
         w.getRight().setType(this.plainsVillagePopulator.woodLog);
         w.getRight().getUp().setType(new Material[]{Material.STONE_BRICK_WALL, Material.MOSSY_STONE_BRICK_WALL});
         w.getRight().getUp(2).setBlockData(log);
         w.getRight().getUp().CorrectMultipleFacing(1);
         w.getUp(2).setType(BlockUtils.stoneBrickSlabs);
         w = w.getFront();
         w.getLeft().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getRight().getDown().downUntilSolid(rand, new Material[]{Material.COBBLESTONE, Material.MOSSY_COBBLESTONE});
         w.getLeft().setType(new Material[]{Material.STONE_BRICK_WALL, Material.MOSSY_STONE_BRICK_WALL});
         w.getLeft().CorrectMultipleFacing(1);
         w.getRight().setType(new Material[]{Material.STONE_BRICK_WALL, Material.MOSSY_STONE_BRICK_WALL});
         w.getRight().CorrectMultipleFacing(1);
      } else if (this.var == PlainsVillageHouseVariant.WOODEN) {
         log = (Orientable)Bukkit.createBlockData(this.plainsVillagePopulator.woodLog);
         if (w.getDirection().getModZ() != 0) {
            log.setAxis(Axis.Z);
         } else {
            log.setAxis(Axis.X);
         }

         w = w.getFront();
         w.getLeft().getDown().downUntilSolid(rand, new Material[]{this.plainsVillagePopulator.woodLog});
         w.getRight().getDown().downUntilSolid(rand, new Material[]{this.plainsVillagePopulator.woodLog});
         w.getUp(2).setBlockData(log);
         w.getLeft().setBlockData(log);
         w.getLeft().getUp().setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getLeft().getUp(2).setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getLeft().getUp().CorrectMultipleFacing(2);
         w.getRight().setBlockData(log);
         w.getRight().getUp().setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getRight().getUp(2).setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getRight().getUp().CorrectMultipleFacing(2);
         w = w.getFront();
         w.getLeft().getDown().downUntilSolid(rand, new Material[]{this.plainsVillagePopulator.woodLog});
         w.getRight().getDown().downUntilSolid(rand, new Material[]{this.plainsVillagePopulator.woodLog});
         w.getLeft().setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getLeft().CorrectMultipleFacing(1);
         w.getRight().setType(new Material[]{Material.COBBLESTONE_WALL, Material.MOSSY_COBBLESTONE_WALL});
         w.getRight().CorrectMultipleFacing(1);
      }

   }
}
