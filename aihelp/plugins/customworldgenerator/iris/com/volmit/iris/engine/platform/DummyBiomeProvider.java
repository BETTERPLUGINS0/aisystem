package com.volmit.iris.engine.platform;

import com.volmit.iris.core.nms.INMS;
import java.util.List;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

public class DummyBiomeProvider extends BiomeProvider {
   private final List<Biome> ALL = INMS.get().getBiomes();

   @NotNull
   public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
      return Biome.PLAINS;
   }

   @NotNull
   public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
      return this.ALL;
   }
}
