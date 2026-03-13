package com.volmit.iris.util.noise;

public class FlatNoise implements NoiseGenerator {
   public FlatNoise(long seed) {
   }

   public double noise(double x) {
      return 1.0D;
   }

   public double noise(double x, double z) {
      return 1.0D;
   }

   public double noise(double x, double y, double z) {
      return 1.0D;
   }
}
