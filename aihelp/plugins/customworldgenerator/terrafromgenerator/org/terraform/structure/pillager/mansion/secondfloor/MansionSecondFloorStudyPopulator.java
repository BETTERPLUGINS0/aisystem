package org.terraform.structure.pillager.mansion.secondfloor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Lantern;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSchematicParser;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.blockdata.OrientableBuilder;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionSecondFloorStudyPopulator extends MansionRoomPopulator {
   private static final int roomWidth = 15;

   public MansionSecondFloorStudyPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(@NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int[] lowerBounds = this.getRoom().getLowerCorner(1);
      BlockFace randomFace = BlockUtils.getDirectBlockFace(random);

      try {
         SimpleBlock target;
         TerraSchematic schema;
         if (randomFace == BlockFace.NORTH) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-study", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1] + 15);
            schema = TerraSchematic.load("mansion/mansion-study", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.EAST) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-study", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.WEST) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1] + 15);
            schema = TerraSchematic.load("mansion/mansion-study", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   public void decorateEntrance(Random rand, Wall w) {
      w = w.getRear();
      w.getLeft(2).Pillar(5, new Material[]{Material.DARK_OAK_PLANKS});
      w.getRight(2).Pillar(5, new Material[]{Material.DARK_OAK_PLANKS});
      w.getLeft(2).Pillar(3, new Material[]{Material.DARK_OAK_LOG});
      w.getRight(2).Pillar(3, new Material[]{Material.DARK_OAK_LOG});
      w.getLeft().getUp(5).downPillar(2, new Material[]{Material.DARK_OAK_PLANKS});
      w.getRight().getUp(5).downPillar(2, new Material[]{Material.DARK_OAK_PLANKS});
      w.getUp(5).downPillar(2, new Material[]{Material.DARK_OAK_PLANKS});
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(w.getDirection()).setHalf(Half.TOP).apply(w.getUp(4)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getUp(3).getLeft()).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getUp(3).getRight());
   }

   public void decorateWall(@NotNull Random rand, @NotNull Wall w) {
      w.getRear().Pillar(4, new Material[]{Material.BOOKSHELF});
      w.getLeft().getRear().Pillar(3, new Material[]{Material.BOOKSHELF});
      w.getRight().getRear().Pillar(3, new Material[]{Material.BOOKSHELF});
      w.getLeft(2).getRear().Pillar(4, new Material[]{Material.DARK_OAK_LOG});
      w.getRight(2).getRear().Pillar(4, new Material[]{Material.DARK_OAK_LOG});
      (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getRear().getUp(5));
      w.getUp(5).downPillar(rand, 2, new Material[]{Material.CHAIN});
      Lantern lantern = (Lantern)Bukkit.createBlockData(Material.LANTERN);
      lantern.setHanging(true);
      w.getUp(3).setBlockData(lantern);
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getRear().getUp(3).getRight()).apply(w.getRear().getUp(3).getRight(3)).apply(w.getRear().getUp(4).getRight(2)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRear().getUp(3).getLeft()).apply(w.getRear().getUp(3).getLeft(3)).apply(w.getRear().getUp(4).getLeft(2));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getRear().getUp(4).getLeft()).apply(w.getRear().getUp(4).getRight());
   }

   public void decorateWindow(Random rand, @NotNull Wall w) {
      this.clearRoguePillar(w);
   }

   public void decorateExit(Random rand, @NotNull Wall w) {
      this.clearRoguePillar(w);
   }

   public void clearRoguePillar(@NotNull Wall base) {
      Entry<Wall, Integer> entry = this.getRoom().getWall(base.get().getPopData(), base.getDirection().getOppositeFace(), 0);
      Wall w = (Wall)entry.getKey();

      for(int i = 0; i < (Integer)entry.getValue(); ++i) {
         if (!w.isSolid()) {
            w.setType(Material.RED_CARPET);
         }

         if (w.getFront().getType() == Material.POLISHED_ANDESITE) {
            w.getFront().Pillar(6, new Material[]{Material.AIR});
            w.getFront().setType(Material.RED_CARPET);
            Wall target = w.getFront().getUp(5);
            BlockFace[] var6 = BlockUtils.directBlockFaces;
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               BlockFace face = var6[var8];
               if (target.getRelative(face).getType() == Material.DARK_OAK_STAIRS || target.getRelative(face).getType() == Material.DARK_OAK_SLAB) {
                  target.getRelative(face).setType(Material.AIR);
                  target.getDown().getRelative(face).setType(Material.AIR);
                  target.getRelative(face, 2).setType(Material.AIR);
               }
            }
         }

         w = w.getLeft();
      }

   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(2, 2);
   }
}
