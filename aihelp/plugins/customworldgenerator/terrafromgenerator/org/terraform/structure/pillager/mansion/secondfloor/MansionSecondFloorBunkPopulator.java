package org.terraform.structure.pillager.mansion.secondfloor;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.schematic.TerraSchematic;
import org.terraform.structure.pillager.mansion.MansionInternalWallState;
import org.terraform.structure.pillager.mansion.MansionRoomPopulator;
import org.terraform.structure.pillager.mansion.MansionRoomSchematicParser;
import org.terraform.structure.pillager.mansion.MansionRoomSize;
import org.terraform.structure.room.CubeRoom;

public class MansionSecondFloorBunkPopulator extends MansionRoomPopulator {
   private static final int roomWidthX = 15;
   private static final int roomWidthZ = 6;

   public MansionSecondFloorBunkPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
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
            schema = TerraSchematic.load("mansion/mansion-bunk", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         } else if (randomFace == BlockFace.SOUTH) {
            target = new SimpleBlock(data, lowerBounds[0] + 15, this.getRoom().getY(), lowerBounds[1] + 6);
            schema = TerraSchematic.load("mansion/mansion-bunk", target);
            schema.parser = new MansionRoomSchematicParser(random, data);
            schema.setFace(randomFace);
            schema.apply();
         }
      } catch (FileNotFoundException var7) {
         TerraformGeneratorPlugin.logger.stackTrace(var7);
      }

   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(2, 1);
   }
}
