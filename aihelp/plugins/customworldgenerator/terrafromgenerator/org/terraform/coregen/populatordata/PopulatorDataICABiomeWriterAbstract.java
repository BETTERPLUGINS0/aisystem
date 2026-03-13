package org.terraform.coregen.populatordata;

import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.custombiomes.CustomBiomeType;

public abstract class PopulatorDataICABiomeWriterAbstract extends PopulatorDataICAAbstract {
   public abstract void setBiome(int var1, int var2, int var3, CustomBiomeType var4, Biome var5);

   public void setBiome(int rawX, int rawY, int rawZ, @NotNull BiomeBank biomebank) {
      this.setBiome(rawX, rawY, rawZ, biomebank.getHandler().getCustomBiome(), biomebank.getHandler().getBiome());
   }

   public abstract void setBiome(int var1, int var2, int var3, Biome var4);
}
