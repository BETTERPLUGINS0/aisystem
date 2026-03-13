package org.terraform.structure.catacombs;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.structure.room.CubeRoom;
import org.terraform.utils.SphereBuilder;

public class CatacombsStairwayPopulator extends CatacombsStandardPopulator {
   public CatacombsStairwayPopulator(Random rand, boolean forceSpawn, boolean unique) {
      super(rand, forceSpawn, unique);
   }

   public void populate(@NotNull PopulatorDataAbstract data, @NotNull CubeRoom room) {
      super.populate(data, room);
      SimpleBlock center = room.getCenterSimpleBlock(data).getUp();
      (new SphereBuilder(new Random(), center.getDown(), new Material[]{Material.CAVE_AIR})).setRadius(3.0F).setHardReplace(true).build();
      super.spawnHangingChains(data, room);
   }
}
