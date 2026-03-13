package org.terraform.biome.custombiomes;

import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.TerraformWorld;

public abstract class CustomBiomeSupportedBiomeGrid {
   public void setBiome(@NotNull TerraformWorld tw, int x, int z, CustomBiomeType bio, Biome fallback) {
      for(int y = tw.minY; y < tw.maxY; y += 4) {
         this.setBiome(x, y, z, bio, fallback);
      }

   }

   public abstract void setBiome(int var1, int var2, int var3, CustomBiomeType var4, Biome var5);
}
