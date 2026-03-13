package com.volmit.iris.util.data;

import com.volmit.iris.engine.object.IrisBiome;

public class BiomeMap {
   private final IrisBiome[] height = new IrisBiome[256];

   public void setBiome(int x, int z, IrisBiome h) {
      this.height[var1 * 16 + var2] = var3;
   }

   public IrisBiome getBiome(int x, int z) {
      return this.height[var1 * 16 + var2];
   }
}
