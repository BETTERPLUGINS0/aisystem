package com.volmit.iris.util.noise;

public class CachedNoise implements NoiseGenerator {
   private final CachedNoiseMap n;

   public CachedNoise(NoiseGenerator generator, int size) {
      this.n = new CachedNoiseMap(var2, var1);
   }

   public double noise(double x) {
      return this.n.get((int)Math.round(var1), 0);
   }

   public double noise(double x, double z) {
      return this.n.get((int)Math.round(var1), (int)Math.round(var3));
   }

   public double noise(double x, double y, double z) {
      return this.n.get((int)Math.round(var1), (int)Math.round(var5));
   }
}
