package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

public class GlobNoise implements NoiseGenerator {
   private final FastNoiseDouble n;

   public GlobNoise(long seed) {
      this.n = new FastNoiseDouble((new RNG(var1)).lmax());
      this.n.setNoiseType(FastNoiseDouble.NoiseType.Cellular);
      this.n.setCellularReturnType(FastNoiseDouble.CellularReturnType.Distance2Div);
      this.n.setCellularDistanceFunction(FastNoiseDouble.CellularDistanceFunction.Natural);
   }

   private double f(double n) {
      return var1 + 1.0D;
   }

   public double noise(double x) {
      return this.f(this.n.GetCellular(var1, 0.0D));
   }

   public double noise(double x, double z) {
      return this.f(this.n.GetCellular(var1, var3));
   }

   public double noise(double x, double y, double z) {
      return this.f(this.n.GetCellular(var1, var3, var5));
   }
}
