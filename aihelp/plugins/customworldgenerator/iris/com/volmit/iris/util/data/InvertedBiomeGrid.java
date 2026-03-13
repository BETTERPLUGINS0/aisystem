package com.volmit.iris.util.data;

import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;

public class InvertedBiomeGrid implements BiomeGrid {
   private final BiomeGrid grid;

   public InvertedBiomeGrid(BiomeGrid real) {
      this.grid = var1;
   }

   public Biome getBiome(int arg0, int arg1) {
      return this.grid.getBiome(var1, var2);
   }

   public Biome getBiome(int arg0, int arg1, int arg2) {
      return this.grid.getBiome(var1, 255 - var2, var3);
   }

   public void setBiome(int arg0, int arg1, Biome arg2) {
      this.grid.setBiome(var1, var2, var3);
   }

   public void setBiome(int arg0, int arg1, int arg2, Biome arg3) {
      this.grid.setBiome(var1, 255 - var2, var3, var4);
   }
}
