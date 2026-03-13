package org.terraform.structure;

import org.terraform.biome.BiomeBank;
import org.terraform.data.TerraformWorld;

public abstract class SingleMegaChunkStructurePopulator extends StructurePopulator {
   public abstract boolean canSpawn(TerraformWorld var1, int var2, int var3, BiomeBank var4);
}
