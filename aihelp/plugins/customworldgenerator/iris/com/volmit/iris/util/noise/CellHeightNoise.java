package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;

public class CellHeightNoise implements NoiseGenerator {
   private final FastNoiseDouble n;

   public CellHeightNoise(long seed) {
      this.n = new FastNoiseDouble((new RNG(var1)).lmax());
      this.n.setNoiseType(FastNoiseDouble.NoiseType.Cellular);
      this.n.setCellularReturnType(FastNoiseDouble.CellularReturnType.Distance2Sub);
      this.n.setCellularDistanceFunction(FastNoiseDouble.CellularDistanceFunction.Natural);
   }

   private double filter(double noise) {
      return (Double)M.clip(1.0D - (var1 / 2.0D + 0.5D), 0.0D, 1.0D);
   }

   public double noise(double x) {
      return this.filter(this.n.GetCellular(var1, 0.0D));
   }

   public double noise(double x, double z) {
      return this.filter(this.n.GetCellular(var1, var3));
   }

   public double noise(double x, double y, double z) {
      return this.filter(this.n.GetCellular(var1, var3, var5));
   }
}
