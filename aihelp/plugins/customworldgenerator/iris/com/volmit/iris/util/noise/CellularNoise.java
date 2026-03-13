package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

public class CellularNoise implements NoiseGenerator {
   private final FastNoise n;

   public CellularNoise(long seed) {
      this.n = new FastNoise((new RNG(var1)).imax());
      this.n.SetNoiseType(FastNoise.NoiseType.Cellular);
      this.n.SetCellularReturnType(FastNoise.CellularReturnType.CellValue);
      this.n.SetCellularDistanceFunction(FastNoise.CellularDistanceFunction.Natural);
   }

   public double noise(double x) {
      return (double)this.n.GetCellular((float)var1, 0.0F) / 2.0D + 0.5D;
   }

   public double noise(double x, double z) {
      return (double)this.n.GetCellular((float)var1, (float)var3) / 2.0D + 0.5D;
   }

   public double noise(double x, double y, double z) {
      return (double)this.n.GetCellular((float)var1, (float)var3, (float)var5) / 2.0D + 0.5D;
   }
}
