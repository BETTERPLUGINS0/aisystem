package com.volmit.iris.util.uniques.features;

import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.uniques.UFeature;
import com.volmit.iris.util.uniques.UFeatureMeta;
import com.volmit.iris.util.uniques.UImage;
import java.util.function.Consumer;

public class UFWarpedDots implements UFeature {
   public void render(UImage image, RNG rng, double t, Consumer<Double> progressor, UFeatureMeta meta) {
      CNG var7 = this.generator("x_pos", var2, 4.0D, 2000L, var6);
      CNG var8 = this.generator("y_pos", var2, 4.0D, 2001L, var6);
      double var9 = var2.d(0.75D, 11.25D);

      for(int var11 = 1; var11 <= 8; ++var11) {
         CNG var12 = this.generator("x_warp_" + var11, var2, 2.0D, (long)(2006 + var11), var6);
         CNG var13 = this.generator("y_warp_" + var11, var2, 2.0D, (long)(2007 + var11), var6);
         CNG var14 = this.generator("color_hue_" + var11, var2, var2.d(0.55D, 3.5D), (long)(2003 + var11), var6);
         CNG var15 = this.generator("color_sat_" + var11, var2, var2.d(0.55D, 3.5D), (long)(2004 + var11), var6);
         CNG var16 = this.generator("color_bri_" + var11, var2, var2.d(0.55D, 3.5D), (long)(2005 + var11), var6);
         int var17 = var7.fit(0, var1.getWidth(), (double)(var11 * 128), (double)(var11 * 5855));
         int var18 = var8.fit(0, var1.getHeight(), (double)(var11 * 128), (double)(var11 * 5855));
         this.color(var14, var15, var16, (double)var17, (double)var18, var3);
         double var20 = (double)Math.max(var7.fit(var1.getWidth() / 10, var1.getWidth() / 6, (double)var17, (double)var18), var8.fit(var1.getHeight() / 10, var1.getHeight() / 6, (double)var17, (double)var18));

         for(int var22 = (int)((double)var17 - var20); (double)var22 < (double)var17 + var20; ++var22) {
            for(int var23 = (int)((double)var18 - var20); (double)var23 < (double)var18 + var20; ++var23) {
               if (var1.isInBounds(var22, var23) && Math.pow((double)(var17 - var22), 2.0D) + Math.pow((double)(var18 - var23), 2.0D) <= var20 * var20) {
                  var1.set(Math.round((float)(var22 + var12.fit(-var20 / 2.0D, var20 / 2.0D, (double)var22 + var3, (double)(-var23) + var3))), Math.round((float)(var23 + var13.fit(-var20 / 2.0D, var20 / 2.0D, (double)var23 + var3, (double)(-var22) + var3))), this.color(var14, var15, var16, (double)var22, (double)var23, var9 * var3));
               }
            }
         }

         var5.accept((double)var11 / 32.0D);
      }

   }
}
