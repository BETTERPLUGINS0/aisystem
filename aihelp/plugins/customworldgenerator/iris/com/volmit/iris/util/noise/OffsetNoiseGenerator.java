package com.volmit.iris.util.noise;

import com.volmit.iris.util.math.RNG;
import org.jetbrains.annotations.NotNull;

public class OffsetNoiseGenerator implements NoiseGenerator {
   private final NoiseGenerator base;
   private final double ox;
   private final double oz;

   public OffsetNoiseGenerator(NoiseGenerator base, long seed) {
      this.base = var1;
      RNG var4 = new RNG(var2);
      this.ox = (double)var4.nextInt(-32768, 32767);
      this.oz = (double)var4.nextInt(-32768, 32767);
   }

   public double noise(double x) {
      return this.base.noise(var1 + this.ox);
   }

   public double noise(double x, double z) {
      return this.base.noise(var1 + this.ox, var3 + this.oz);
   }

   public double noise(double x, double y, double z) {
      return this.base.noise(var1 + this.ox, var3, var5 + this.oz);
   }

   public boolean isNoScale() {
      return this.base.isNoScale();
   }

   public boolean isStatic() {
      return this.base.isStatic();
   }

   @NotNull
   public NoiseGenerator getBase() {
      return this.base;
   }
}
