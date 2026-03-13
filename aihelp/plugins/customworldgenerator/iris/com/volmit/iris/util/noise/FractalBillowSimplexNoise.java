package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

public class FractalBillowSimplexNoise implements NoiseGenerator, OctaveNoise {
   private final FastNoiseDouble n;

   public FractalBillowSimplexNoise(long seed) {
      this.n = new FastNoiseDouble((new RNG(var1)).lmax());
      this.n.setFractalOctaves(1L);
      this.n.setFractalType(FastNoiseDouble.FractalType.Billow);
   }

   public double f(double v) {
      return var1 / 2.0D + 0.5D;
   }

   public double noise(double x) {
      return this.f(this.n.GetSimplexFractal(var1, 0.0D));
   }

   public double noise(double x, double z) {
      return this.f(this.n.GetSimplexFractal(var1, var3));
   }

   public double noise(double x, double y, double z) {
      return this.f(this.n.GetSimplexFractal(var1, var3, var5));
   }

   public void setOctaves(int o) {
      this.n.setFractalOctaves((long)var1);
   }
}
