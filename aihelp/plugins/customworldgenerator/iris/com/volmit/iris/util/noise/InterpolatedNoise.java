package com.volmit.iris.util.noise;

import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import java.util.Objects;

public class InterpolatedNoise implements NoiseGenerator {
   private final InterpolationMethod method;
   private final NoiseProvider p;

   public InterpolatedNoise(long seed, NoiseType type, InterpolationMethod method) {
      this.method = var4;
      NoiseGenerator var5 = var3.create(var1);
      Objects.requireNonNull(var5);
      this.p = var5::noise;
   }

   public double noise(double x) {
      return this.noise(var1, 0.0D);
   }

   public double noise(double x, double z) {
      return IrisInterpolation.getNoise(this.method, (int)var1, (int)var3, 32.0D, this.p);
   }

   public double noise(double x, double y, double z) {
      return var5 == 0.0D ? this.noise(var1, var3) : IrisInterpolation.getNoise(this.method, (int)var1, (int)var5, 32.0D, this.p);
   }
}
