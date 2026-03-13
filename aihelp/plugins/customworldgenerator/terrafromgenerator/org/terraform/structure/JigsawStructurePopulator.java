package org.terraform.structure;

import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;

public abstract class JigsawStructurePopulator extends SingleMegaChunkStructurePopulator {
   public abstract JigsawState calculateRoomPopulators(TerraformWorld var1, MegaChunk var2);

   public void populate(TerraformWorld world, PopulatorDataAbstract data) {
   }
}
