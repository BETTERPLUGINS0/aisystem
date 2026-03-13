package org.terraform.structure.monument;

import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.Wall;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;
import org.terraform.utils.GenUtils;

public class MonumentPathPopulator extends PathPopulatorAbstract {
   final Random rand;
   final MonumentDesign design;
   private boolean light = true;

   public MonumentPathPopulator(MonumentDesign design, Random rand) {
      this.rand = rand;
      this.design = design;
   }

   public void populate(@NotNull PathPopulatorData ppd) {
      Wall w = new Wall(ppd.base, ppd.dir);
      w.getLeft().RPillar(5, this.rand, new Material[]{Material.WATER});
      w.RPillar(5, this.rand, new Material[]{Material.WATER});
      w.getRight().RPillar(5, this.rand, new Material[]{Material.WATER});

      for(int i = 0; i <= 1; ++i) {
         if (w.getLeft(i).getType() != Material.SEA_LANTERN) {
            w.getLeft(i).setType(Material.PRISMARINE);
         }

         if (w.getRight(i).getType() != Material.SEA_LANTERN) {
            w.getRight(i).setType(Material.PRISMARINE);
         }
      }

      if (this.light) {
         w.setType(Material.SEA_LANTERN);
      }

      this.light = !this.light;
      if (GenUtils.chance(this.rand, 1, 20)) {
         w.RPillar(5, this.rand, GenUtils.mergeArr(this.design.tileSet, new Material[]{Material.SEA_LANTERN}));
      }

      if (GenUtils.chance(this.rand, 1, 50)) {
         MonumentRoomPopulator.setThickPillar(this.rand, this.design, w.get().getDown());
      }

      if (GenUtils.chance(this.rand, 1, 50) && w.getUp(6).isSolid() && !w.getUp(7).isSolid()) {
         this.design.spire(w.getUp(7), this.rand);
      }

   }
}
