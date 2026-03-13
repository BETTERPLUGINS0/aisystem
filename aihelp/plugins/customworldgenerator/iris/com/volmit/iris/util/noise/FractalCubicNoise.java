package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

public class FractalCubicNoise implements NoiseGenerator {
   private final FastNoiseDouble n;

   public FractalCubicNoise(long seed) {
      this.n = new FastNoiseDouble((new RNG(var1)).lmax());
      this.n.setFractalType(FastNoiseDouble.FractalType.Billow);
   }

   private double f(double n) {
      return var1 / 2.0D + 0.5D;
   }

   public double noise(double x) {
      return this.f(this.n.GetCubicFractal(var1, 0.0D));
   }

   public double noise(double x, double z) {
      return this.f(this.n.GetCubicFractal(var1, var3));
   }

   public double noise(double x, double y, double z) {
      return this.f(this.n.GetCubicFractal(var1, var3, var5));
   }
}
