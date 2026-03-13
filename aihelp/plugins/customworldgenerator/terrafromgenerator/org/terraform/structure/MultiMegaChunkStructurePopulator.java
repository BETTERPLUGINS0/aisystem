package org.terraform.structure;

import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;

public abstract class MultiMegaChunkStructurePopulator extends StructurePopulator {
   public abstract boolean canSpawn(TerraformWorld var1, int var2, int var3);

   public abstract int[][] getCoordsFromMegaChunk(TerraformWorld var1, MegaChunk var2);

   public abstract int[] getNearestFeature(TerraformWorld var1, int var2, int var3);
}
