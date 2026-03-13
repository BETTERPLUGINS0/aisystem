package com.volmit.iris.util.uniques.features;

import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.uniques.UFeature;
import com.volmit.iris.util.uniques.UFeatureMeta;
import com.volmit.iris.util.uniques.UImage;
import java.util.function.Consumer;

public class UFWarpedLines implements UFeature {
   public void render(UImage image, RNG rng, double t, Consumer<Double> progressor, UFeatureMeta meta) {
      for(int var7 = 1; var7 < 5; ++var7) {
         CNG var8 = this.generator("x_warp_" + var7, var2, 0.6D, (long)(1001 * var7), var6);
         CNG var9 = this.generator("y_warp_" + var7, var2, 0.6D, (long)(1002 * var7), var6);
         CNG var10 = this.generator("x_clip_" + var7, var2, var2.d(0.035D, 0.6D), (long)(77001 * var7), var6);
         CNG var11 = this.generator("y_clip" + var7, var2, var2.d(0.035D, 0.6D), 77002L, var6);
         CNG var12 = this.generator("color_hue_" + var7, var2, var2.d(0.25D, 2.5D), (long)(1003 * var7), var6);
         CNG var13 = this.generator("color_sat_" + var7, var2, var2.d(0.25D, 2.5D), (long)(1004 * var7), var6);
         CNG var14 = this.generator("color_bri_" + var7, var2, var2.d(0.25D, 2.5D), (long)(1005 * var7), var6);
         double var15 = var2.d(0.75D, 11.25D + (double)(var7 * 4));
         double var17 = var2.d(7.75D, 16.25D + (double)(var7 * 5));
         double var19 = var2.d(0.15D, 0.55D + (double)var7 * 0.645D);
         double var21 = var2.d(64.0D, (double)(186 + var7 * 8));
         double var23 = (double)var1.getWidth() / var2.d(3.0D, 9.0D);
         double var25 = (double)var1.getHeight() / var2.d(3.0D, 9.0D);
         boolean var27 = var2.nextBoolean();
         double var28 = var2.d(0.35D, 0.66D);
         double var30 = var2.d(0.35D, 0.66D);

         for(int var32 = 0; var32 < var1.getWidth(); var32 = (int)((double)var32 + (var27 ? (double)var1.getWidth() / var21 : 1.0D))) {
            for(int var33 = 0; var33 < var1.getHeight(); var33 = (int)((double)var33 + (!var27 ? (double)var1.getHeight() / var21 : 1.0D))) {
               if (var10.fitDouble(0.0D, 1.0D, (double)var32, (double)(-var33), var3 * var19) > var28 && var11.fitDouble(0.0D, 1.0D, (double)(-var33), (double)var32, var3 * var19) > var30) {
                  var1.set(Math.round((float)(var32 + var8.fit(-var23, var23, (double)var32, (double)var33, var3 * var17))), Math.round((float)(var33 + var9.fit(-var25, var25, (double)(-var33), (double)var32, var3 * var17))), this.color(var12, var13, var14, (double)var32, (double)var33, var3 * var15));
               }
            }
         }
      }

   }
}
