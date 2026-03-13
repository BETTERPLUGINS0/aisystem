package org.terraform.structure.trailruins;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.Wall;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;

public class TrailRuinsPathPopulator extends PathPopulatorAbstract {
   public TrailRuinsPathPopulator(Random rand) {
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      for(int i = -1; i <= 1; ++i) {
         Wall core = (new Wall(ppd.base, ppd.dir)).getLeft(i);
         if (core.getDown(2).isSolid()) {
            core.setType(new Material[]{Material.STONE, Material.COBBLESTONE, Material.AIR});
            core.getDown().setType(new Material[]{Material.STONE, Material.COBBLESTONE});
         }
      }

   }
}
