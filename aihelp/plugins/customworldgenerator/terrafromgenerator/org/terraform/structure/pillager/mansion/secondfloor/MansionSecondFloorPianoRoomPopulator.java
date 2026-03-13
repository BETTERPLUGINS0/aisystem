package org.terraform.structure.pillager.mansion.secondfloor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.Wall;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSchematicParser;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.PaintingUtils;
import org.terraform.utils.blockdata.StairBuilder;

public class MansionSecondFloorPianoRoomPopulator extends MansionRoomPopulator {
   private static final int roomWidthX = 6;
   private static final int roomWidthZ = 15;

   public MansionSecondFloorPianoRoomPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(@NotNull PopulatorDataAbstract data, @NotNull Random random) {
      int[] lowerBounds = this.getRoom().getLowerCorner(1);
      BlockFace randomFace = (new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH})[random.nextInt(2)];
      TLogger var10000 = TerraformGeneratorPlugin.logger;
      String var10001 = String.valueOf(this.getRoom().getSimpleLocation());
      var10000.info("Piano at " + var10001 + " picking face: " + String.valueOf(randomFace));

      try {
         SimpleBlock target;
         TerraSchematic schema;
         if (randomFace == BlockFace.NORTH) {
            target = new SimpleBlock(data, lowerBounds[0], this.getRoom().getY(), lowerBounds[1]);
            schema = TerraSchematic.load("mansion/mansion-piano", target);
            schema.setFace(randomFace);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 6, this.getRoom().getY(), lowerBounds[1] + 15);
            schema = TerraSchematic.load("mansion/mansion-piano", target);
            schema.setFace(randomFace);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   public void decorateWindow(Random rand, @NotNull Wall w) {
      w.setType(Material.DARK_OAK_LOG);
      BlockUtils.pickPottedPlant().build(w.getUp());
      (new StairBuilder(Material.POLISHED_ANDESITE_STAIRS)).setFacing(BlockUtils.getLeft(w.getDirection())).apply(w.getLeft()).setFacing(BlockUtils.getRight(w.getDirection())).apply(w.getRight());
   }

   public void decorateWall(@NotNull Random rand, @NotNull Wall w) {
      PaintingUtils.placePainting(w.getUp(2).getLeft().get(), w.getDirection(), PaintingUtils.getArtFromDimensions(rand, 1, 2));
      PaintingUtils.placePainting(w.getUp(2).getRight().get(), w.getDirection(), PaintingUtils.getArtFromDimensions(rand, 1, 2));
      w.getRear().Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      w.getLeft(2).getRear().Pillar(6, new Material[]{Material.DARK_OAK_LOG});
      w.getRight(2).getRear().Pillar(6, new Material[]{Material.DARK_OAK_LOG});
   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(1, 2);
   }
}
