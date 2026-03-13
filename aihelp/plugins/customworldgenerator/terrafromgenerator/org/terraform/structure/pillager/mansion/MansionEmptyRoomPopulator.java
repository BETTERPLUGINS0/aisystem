package org.terraform.structure.pillager.mansion;

import java.util.HashMap;
import java.util.Random;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;

public class MansionEmptyRoomPopulator extends MansionRoomPopulator {
   public MansionEmptyRoomPopulator(CubeRoom room, HashMap<BlockFace, MansionInternalWallState> internalWalls) {
      super(room, internalWalls);
   }

   public void decorateRoom(PopulatorDataAbstract data, Random random) {
   }

   @NotNull
   public MansionRoomSize getSize() {
      return new MansionRoomSize(1, 1);
   }
}
