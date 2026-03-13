package com.volmit.iris.util.uniques.features;

import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.uniques.UFeature;
import com.volmit.iris.util.uniques.UFeatureMeta;
import com.volmit.iris.util.uniques.UImage;
import java.util.function.Consumer;

public class UFWarpedDisc implements UFeature {
   public void render(UImage image, RNG rng, double t, Consumer<Double> progressor, UFeatureMeta meta) {
      double var7 = (double)Math.min(var1.getWidth(), var1.getHeight()) / 3.0D;
      CNG var9 = this.generator("x_warp", var2, 0.6D, 1001L, var6);
      CNG var10 = this.generator("y_warp", var2, 0.6D, 1002L, var6);
      CNG var11 = this.generator("color_hue", var2, var2.d(0.25D, 2.5D), 1003L, var6);
      CNG var12 = this.generator("color_sat", var2, var2.d(0.25D, 2.5D), 1004L, var6);
      CNG var13 = this.generator("color_bri", var2, var2.d(0.25D, 2.5D), 1005L, var6);
      double var14 = var2.d(0.75D, 11.25D);
      int var16 = var1.getWidth() / 2;
      int var17 = var1.getHeight() / 2;

      for(int var18 = (int)((double)var16 - var7); (double)var18 < (double)var16 + var7; ++var18) {
         for(int var19 = (int)((double)var17 - var7); (double)var19 < (double)var17 + var7; ++var19) {
            if (var1.isInBounds(var18, var19) && Math.pow((double)(var16 - var18), 2.0D) + Math.pow((double)(var17 - var19), 2.0D) <= var7 * var7) {
               var1.set(Math.round((float)(var18 + var9.fit(-var7 / 2.0D, var7 / 2.0D, (double)var18 + var3, (double)(-var19) + var3))), Math.round((float)(var19 + var10.fit(-var7 / 2.0D, var7 / 2.0D, (double)var19 + var3, (double)(-var18) + var3))), this.color(var11, var12, var13, (double)var18, (double)var19, var14 * var3));
            }
         }

         var5.accept((double)var18 / ((double)var16 + var7));
      }

   }
}
