package com.volmit.iris.util.uniques.features;

import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.noise.CNG;
import com.volmit.iris.util.uniques.UFeature;
import com.volmit.iris.util.uniques.UFeatureMeta;
import com.volmit.iris.util.uniques.UImage;
import java.util.function.Consumer;

public class UFWarpedBackground implements UFeature {
   public void render(UImage image, RNG rng, double t, Consumer<Double> progressor, UFeatureMeta meta) {
      CNG var7 = this.generator("color_hue", var2, var2.d(0.001D, var2.d(2.0D, 5.0D)), var2.i(0, 3), var2.i(0, 3), 31007L, var6);
      CNG var8 = this.generator("color_sat", var2, var2.d(0.001D, var2.d(2.0D, 5.0D)), var2.i(0, 2), var2.i(0, 2), 33004L, var6);
      CNG var9 = this.generator("color_bri", var2, var2.d(0.001D, var2.d(2.0D, 5.0D)), var2.i(0, 1), var2.i(0, 1), 32005L, var6).patch(0.145D);
      double var10 = var2.d(0.15D, 0.55D);

      for(int var12 = 0; var12 < var1.getWidth(); ++var12) {
         for(int var13 = 0; var13 < var1.getHeight(); ++var13) {
            var1.set(var12, var13, this.color(var7, var8, var9, (double)var12, (double)var13, var10 * var3));
         }

         var5.accept((double)var12 / (double)var1.getWidth());
      }

   }
}
