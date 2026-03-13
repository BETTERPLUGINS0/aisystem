package org.terraform.structure.pyramid;

import java.util.Random;
import org.terraform.structure.room.PathPopulatorAbstract;
import org.terraform.structure.room.PathPopulatorData;

public class PyramidPathPopulator extends PathPopulatorAbstract {
   private final Random rand;
   private final int height;

   public PyramidPathPopulator(Random rand) {
      this.rand = rand;
      this.height = 3;
   }

   public PyramidPathPopulator(Random rand, int height) {
      this.rand = rand;
      this.height = height;
   }

   public void populate(PathPopulatorData ppd) {
   }

   public int getPathWidth() {
      return 1;
   }

   public int getPathHeight() {
      return this.height;
   }
}
