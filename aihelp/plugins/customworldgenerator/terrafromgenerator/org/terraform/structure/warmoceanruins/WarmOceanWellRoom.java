package org.terraform.structure.warmoceanruins;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.structure.room.CubeRoom;
import org.terraform.structure.small.DesertWellPopulator;

public class WarmOceanWellRoom extends WarmOceanBaseRoom {
   public WarmOceanWellRoom(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      (new DesertWellPopulator()).spawnDesertWell(data.getTerraformWorld(), this.rand, data, room.getX(), room.getY() - 1, room.getZ(), false);
   }

   public boolean canPopulate(@NotNull CubeRoom room) {
      return room.getWidthX() < 25;
   }
}
