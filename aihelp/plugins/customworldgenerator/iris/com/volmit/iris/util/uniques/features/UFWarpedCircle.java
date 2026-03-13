package com.volmit.iris.util.uniques.features;

import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.uniques.UFeature;
import com.volmit.iris.util.uniques.UFeatureMeta;
import com.volmit.iris.util.uniques.UImage;
import java.util.function.Consumer;

public class UFWarpedCircle implements UFeature {
   public void render(UImage image, RNG rng, double t, Consumer<Double> progressor, UFeatureMeta meta) {
      double var7 = (double)Math.min(var1.getWidth(), var1.getHeight()) / 2.5D;
      CNG var17 = this.generator("x_warp", var2, 0.6D, 1001L, var6);
      CNG var18 = this.generator("y_warp", var2, 0.6D, 1002L, var6);
      CNG var19 = this.generator("color_hue", var2, var2.d(0.25D, 2.5D), 1003L, var6);
      CNG var20 = this.generator("color_sat", var2, var2.d(0.25D, 2.5D), 1004L, var6);
      CNG var21 = this.generator("color_bri", var2, var2.d(0.25D, 2.5D), 1005L, var6);
      double var22 = var2.d(0.75D, 11.25D);
      double var24 = var2.d(7.75D, 16.25D);
      int var26 = var1.getWidth() / 2;
      int var27 = var1.getHeight() / 2;

      for(int var28 = 0; var28 < 256; ++var28) {
         var7 -= (double)Math.min(var1.getWidth(), var1.getHeight()) / 300.0D;
         if (var7 <= 0.0D) {
            return;
         }

         for(double var9 = 0.0D; var9 < 360.0D; var9 += 0.1D) {
            double var13 = var7 * Math.cos(var9 * 3.141592653589793D / 180.0D);
            double var15 = var7 * Math.sin(var9 * 3.141592653589793D / 180.0D);
            var1.set((int)Math.round((double)var26 + var13 + (double)var17.fit(-var7 / 2.0D, var7 / 2.0D, var13 + var3 + (double)(var28 * 8), -var15 + var3 + (double)(var28 * 8))), (int)Math.round((double)var27 + var15 + (double)var18.fit(-var7 / 2.0D, var7 / 2.0D, var15 + var3 + (double)(var28 * 8), -var13 + var3 + (double)(var28 * 8))), this.color(var19, var20, var21, var13, var15, var3 * var22 + (double)var28 * var24));
         }

         var5.accept((double)var28 / 256.0D);
      }

   }
}
