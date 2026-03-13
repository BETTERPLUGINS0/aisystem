package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

public class WhiteNoise implements NoiseGenerator {
   private final FastNoise n;

   public WhiteNoise(long seed) {
      this.n = new FastNoise((new RNG(var1)).imax());
   }

   public boolean isStatic() {
      return true;
   }

   public boolean isNoScale() {
      return true;
   }

   private double f(double m) {
      return var1 % 8192.0D * 1024.0D;
   }

   public double noise(double x) {
      return (double)this.n.GetWhiteNoise(this.f(var1), 0.0D) / 2.0D + 0.5D;
   }

   public double noise(double x, double z) {
      return (double)this.n.GetWhiteNoise(this.f(var1), this.f(var3)) / 2.0D + 0.5D;
   }

   public double noise(double x, double y, double z) {
      return (double)this.n.GetWhiteNoise(this.f(var1), this.f(var3), this.f(var5)) / 2.0D + 0.5D;
   }
}
