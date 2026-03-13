package com.volmit.iris.util.uniques.features;

import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.uniques.UFeature;
import com.volmit.iris.util.uniques.UFeatureMeta;
import com.volmit.iris.util.uniques.UImage;
import java.awt.Color;
import java.util.function.Consumer;

public class UFInterpolator implements UFeature {
   public void render(UImage image, RNG rng, double t, Consumer<Double> progressor, UFeatureMeta meta) {
      UImage var7 = var1.copy();
      CNG var8 = this.generator("interpolator_radius", var2, 1.0D, 33004L, var6);
      NoiseProvider var9 = (var1x, var3x) -> {
         int var5 = Math.abs((int)var1x % var7.getWidth());
         int var6 = Math.abs((int)var3x % var7.getHeight());
         Color var7x = var7.get(var5, var6);
         float[] var8 = new float[3];
         Color.RGBtoHSB(var7x.getRed(), var7x.getGreen(), var7x.getGreen(), var8);
         return (double)var8[0];
      };
      NoiseProvider var10 = (var1x, var3x) -> {
         int var5 = Math.abs((int)var1x % var7.getWidth());
         int var6 = Math.abs((int)var3x % var7.getHeight());
         Color var7x = var7.get(var5, var6);
         float[] var8 = new float[3];
         Color.RGBtoHSB(var7x.getRed(), var7x.getGreen(), var7x.getGreen(), var8);
         return (double)var8[1];
      };
      NoiseProvider var11 = (var1x, var3x) -> {
         int var5 = Math.abs((int)var1x % var7.getWidth());
         int var6 = Math.abs((int)var3x % var7.getHeight());
         Color var7x = var7.get(var5, var6);
         float[] var8 = new float[3];
         Color.RGBtoHSB(var7x.getRed(), var7x.getGreen(), var7x.getGreen(), var8);
         return (double)var8[2];
      };
      InterpolationMethod var12 = this.interpolator(var2);
      int var13 = Math.min(var1.getWidth(), var1.getHeight());
      double var14 = (double)Math.max(4, var8.fit(var13 / 256, var13 / 4, var3 * var2.d(0.03D, 1.25D), var3 * var2.d(0.01D, 2.225D)));

      for(int var16 = 0; var16 < var1.getWidth(); ++var16) {
         for(int var17 = 0; var17 < var1.getHeight(); ++var17) {
            var1.set(var16, var17, Color.getHSBColor((float)Math.max(Math.min(1.0D, IrisInterpolation.getNoise(var12, var16, var17, var14, var9)), 0.0D), (float)Math.max(Math.min(1.0D, IrisInterpolation.getNoise(var12, var16, var17, var14, var10)), 0.0D), (float)Math.max(Math.min(1.0D, IrisInterpolation.getNoise(var12, var16, var17, var14, var11)), 0.0D)));
         }

         var5.accept((double)var16 / (double)var1.getWidth());
      }

   }
}
