package org.terraform.biome.ocean;

import org.terraform.biome.BiomeHandler;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.HeightMap;
import org.terraform.data.TerraformWorld;

public abstract class AbstractOceanHandler extends BiomeHandler {
   protected final BiomeType oceanType;

   public AbstractOceanHandler(BiomeType oceanType) {
      this.oceanType = oceanType;
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      double height = HeightMap.CORE.getHeight(tw, x, z) - 25.0D;
      if (this.oceanType == BiomeType.DEEP_OCEANIC) {
         height -= 20.0D;
      }

      if (height <= 0.0D) {
         height = 3.0D;
      }

      return height;
   }
}
