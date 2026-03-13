package com.volmit.iris.util.data;

import java.util.Arrays;

public class HeightMap {
   private final int[] height = new int[256];

   public HeightMap() {
      Arrays.fill(this.height, 0);
   }

   public void setHeight(int x, int z, int h) {
      this.height[var1 * 16 + var2] = var3;
   }

   public int getHeight(int x, int z) {
      return this.height[var1 * 16 + var2];
   }
}
