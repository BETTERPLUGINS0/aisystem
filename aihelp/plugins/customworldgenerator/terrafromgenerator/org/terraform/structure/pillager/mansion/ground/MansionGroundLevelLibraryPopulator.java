package org.terraform.structure.pillager.mansion.ground;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Lantern;
import org.bukkit.block.data.type.Stairs.Shape;
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

public class MansionGroundLevelLibraryPopulator extends MansionRoomPopulator {
   private static final int roomWidth = 15;

   public MansionGroundLevelLibraryPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
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
            schema = TerraSchematic.load("mansion/mansion-library", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1] + 15);
            schema = TerraSchematic.load("mansion/mansion-library", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.EAST) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-library", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.WEST) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1] + 15);
            schema = TerraSchematic.load("mansion/mansion-library", target);
            schema.setFace(randomFace);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   public void decorateWall(@NotNull Random rand, @NotNull Wall w) {
      w.getRear().Pillar(4, new Material[]{Material.BOOKSHELF});
      w.getLeft().getRear().Pillar(3, new Material[]{Material.BOOKSHELF});
      w.getRight().getRear().Pillar(3, new Material[]{Material.BOOKSHELF});
      w.getLeft(2).getRear().Pillar(4, new Material[]{Material.DARK_OAK_LOG});
      w.getRight(2).getRear().Pillar(4, new Material[]{Material.DARK_OAK_LOG});
      (new OrientableBuilder(Material.DARK_OAK_LOG)).setAxis(BlockUtils.getAxisFromBlockFace(w.getDirection())).apply(w.getRear().getUp(5));
      w.getUp(6).downPillar(rand, 2, new Material[]{Material.CHAIN});
      Lantern lantern = (Lantern)Bukkit.createBlockData(Material.LANTERN);
      lantern.setHanging(true);
      w.getUp(4).setBlockData(lantern);
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getRear().getUp(3).getRight()).apply(w.getRear().getUp(3).getRight(3)).apply(w.getRear().getUp(4).getRight(2)).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRear().getUp(3).getLeft()).apply(w.getRear().getUp(3).getLeft(3)).apply(w.getRear().getUp(4).getLeft(2));
      (new StairBuilder(Material.DARK_OAK_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w.getRear().getUp(4).getLeft()).apply(w.getRear().getUp(4).getRight());
   }

   public void decorateWindow(@NotNull Random rand, @NotNull Wall w) {
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(w.getDirection().getOppositeFace()).apply(w).setFacing(BlockUtils.getLeft(w.getDirection())).setShape(Shape.INNER_RIGHT).apply(w.getLeft()).setFacing(BlockUtils.getRight(w.getDirection())).setShape(Shape.INNER_LEFT).apply(w.getRight());
      if (rand.nextBoolean()) {
         w.getLeft().setType(Material.BOOKSHELF);
         w.getLeft().getUp().setType(Material.LANTERN);
      } else if (rand.nextBoolean()) {
         w.getRight().setType(Material.BOOKSHELF);
         w.getRight().getUp().setType(Material.LANTERN);
      }

   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(2, 2);
   }
}
