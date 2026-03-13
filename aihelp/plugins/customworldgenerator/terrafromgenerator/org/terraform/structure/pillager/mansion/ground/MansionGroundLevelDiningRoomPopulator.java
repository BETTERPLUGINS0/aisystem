package org.terraform.structure.pillager.mansion.ground;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
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
import org.terraform.utils.PaintingUtils;
import org.terraform.utils.blockdata.OrientableBuilder;

public class MansionGroundLevelDiningRoomPopulator extends MansionRoomPopulator {
   private static final int roomWidthX = 15;
   private static final int roomWidthZ = 6;

   public MansionGroundLevelDiningRoomPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(@NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int[] lowerBounds = this.getRoom().getLowerCorner(1);
      BlockFace randomFace = (new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH})[random.nextInt(2)];

      try {
         SimpleBlock target;
         TerraSchematic schema;
         if (randomFace == BlockFace.NORTH) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-diningroom", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1] + 6);
            schema = TerraSchematic.load("mansion/mansion-diningroom", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   public void decorateExit(Random rand, @NotNull Wall w) {
      OrientableBuilder builder = new OrientableBuilder(Material.DARK_OAK_LOG);
      builder.setAxis(BlockUtils.getAxisFromBlockFace(BlockUtils.getLeft(w.getDirection())));

      for(int i = 0; i <= 4; ++i) {
         builder.lapply(w.getUp(6).getLeft(i));
         builder.lapply(w.getUp(6).getRight(i));
      }

   }

   public void decorateWindow(@NotNull Random rand, @NotNull Wall w) {
      this.decorateExit(rand, w);
      w = w.getUp(6).getRight(4);

      for(int i = 0; i <= 8; ++i) {
         if (w.getFront().getType() == Material.POLISHED_ANDESITE_STAIRS) {
            w.downPillar(rand, 7, new Material[]{Material.DARK_OAK_LOG});
            w.getRear().downPillar(rand, 7, new Material[]{Material.DARK_OAK_PLANKS});
         }

         w = w.getLeft();
      }

   }

   public void decorateWall(@NotNull Random rand, @NotNull Wall w) {
      PaintingUtils.placePainting(w.getUp(2).get(), w.getDirection(), PaintingUtils.getArtFromDimensions(rand, 4, 4));
      w = w.getUp(6).getRight(4);

      for(int i = 0; i <= 8; ++i) {
         if (w.getType() == Material.POLISHED_ANDESITE_STAIRS) {
            w.getRear().downPillar(rand, 7, new Material[]{Material.DARK_OAK_LOG});
         }

         w = w.getLeft();
      }

   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(2, 1);
   }
}
