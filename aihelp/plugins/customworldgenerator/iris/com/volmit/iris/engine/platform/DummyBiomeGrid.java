package com.volmit.iris.engine.platform;

import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.BiomeGrid;
import org.jetbrains.annotations.NotNull;

public class DummyBiomeGrid implements BiomeGrid {
   @NotNull
   public Biome getBiome(int x, int z) {
      return null;
   }

   @NotNull
   public Biome getBiome(int x, int y, int z) {
      return null;
   }

   public void setBiome(int x, int z, @NotNull Biome bio) {
   }

   public void setBiome(int x, int y, int z, @NotNull Biome bio) {
   }
}
