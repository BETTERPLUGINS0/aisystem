package org.terraform.structure;

public abstract class VanillaStructurePopulator extends SingleMegaChunkStructurePopulator {
   public final String structureRegistryKey;

   public VanillaStructurePopulator(String structureKey) {
      this.structureRegistryKey = structureKey;
   }
}
