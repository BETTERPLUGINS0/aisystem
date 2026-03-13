package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;

public class PerlinNoise implements NoiseGenerator, OctaveNoise {
   private final FastNoiseDouble n;
   private int octaves;

   public PerlinNoise(long seed) {
      this.n = new FastNoiseDouble((new RNG(var1)).lmax());
      this.octaves = 1;
   }

   public double f(double v) {
      return var1 / 2.0D + 0.5D;
   }

   public double noise(double x) {
      if (this.octaves <= 1) {
         return this.f(this.n.GetPerlin(var1, 0.0D));
      } else {
         double var3 = 1.0D;
         double var5 = 0.0D;
         double var7 = 0.0D;

         for(int var9 = 0; var9 < this.octaves; ++var9) {
            var7 += this.n.GetPerlin(var1 * (var3 == 1.0D ? var3++ : (var3 *= 2.0D)), 0.0D) * var3;
            var5 += var3;
         }

         return this.f(var7 / var5);
      }
   }

   public double noise(double x, double z) {
      if (this.octaves <= 1) {
         return this.f(this.n.GetPerlin(var1, var3));
      } else {
         double var5 = 1.0D;
         double var7 = 0.0D;
         double var9 = 0.0D;

         for(int var11 = 0; var11 < this.octaves; ++var11) {
            var5 = var5 == 1.0D ? var5 + 1.0D : var5 * 2.0D;
            var9 += this.n.GetPerlin(var1 * var5, var3 * var5) * var5;
            var7 += var5;
         }

         return this.f(var9 / var7);
      }
   }

   public double noise(double x, double y, double z) {
      if (this.octaves <= 1) {
         return this.f(this.n.GetPerlin(var1, var3, var5));
      } else {
         double var7 = 1.0D;
         double var9 = 0.0D;
         double var11 = 0.0D;

         for(int var13 = 0; var13 < this.octaves; ++var13) {
            var7 = var7 == 1.0D ? var7 + 1.0D : var7 * 2.0D;
            var11 += this.n.GetPerlin(var1 * var7, var3 * var7, var5 * var7) * var7;
            var9 += var7;
         }

         return this.f(var11 / var9);
      }
   }

   public void setOctaves(int o) {
      this.octaves = var1;
   }

   public NoiseGenerator hermite() {
      this.n.m_longerp = FastNoiseDouble.Longerp.Hermite;
      return this;
   }

   public NoiseGenerator quad() {
      this.n.m_longerp = FastNoiseDouble.Longerp.Qulongic;
      return this;
   }
}
