package org.terraform.structure;

import java.util.Random;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;

public abstract class StructurePopulator {
   public abstract boolean isEnabled();

   public abstract void populate(TerraformWorld var1, PopulatorDataAbstract var2);

   public abstract Random getHashedRandom(TerraformWorld var1, int var2, int var3);

   public int getChunkBufferDistance() {
      return 3;
   }

   public int getCaveClusterBufferDistance() {
      return 0;
   }
}
