package com.volmit.iris.util.noise;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.IrisImageMap;

public class ImageNoise implements NoiseGenerator {
   private final IrisImageMap expression;
   private final IrisData data;

   public ImageNoise(IrisData data, IrisImageMap expression) {
      this.data = var1;
      this.expression = var2;
   }

   public double noise(double x) {
      return this.noise(var1, 0.0D);
   }

   public double noise(double x, double z) {
      return this.expression.getNoise(this.data, (int)var1, (int)var3);
   }

   public double noise(double x, double y, double z) {
      return this.noise(var1, var5 + var3);
   }
}
