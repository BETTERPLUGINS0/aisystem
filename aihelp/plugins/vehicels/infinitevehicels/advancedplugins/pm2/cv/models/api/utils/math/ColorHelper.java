package advancedplugins.pm2.cv.models.api.utils.math;

public class ColorHelper {
   public static int getAlpha(int var0) {
      return var0 >>> 24;
   }

   public static int getRed(int var0) {
      return var0 >> 16 & 255;
   }

   public static int getGreen(int var0) {
      return var0 >> 8 & 255;
   }

   public static int getBlue(int var0) {
      return var0 & 255;
   }

   public static int getArgb(int var0, int var1, int var2, int var3) {
      return var0 << 24 | var1 << 16 | var2 << 8 | var3;
   }

   public static ColorHelper.HSL toHSL(int var0) {
      return toHSL(getAlpha(var0), getRed(var0), getGreen(var0), getBlue(var0));
   }

   public static ColorHelper.HSL toHSL(int var0, int var1, int var2, int var3) {
      ColorHelper.HSL var4 = new ColorHelper.HSL();
      var4.alpha = var0;
      float var5 = (float)var1 / 255.0F;
      float var6 = (float)var2 / 255.0F;
      float var7 = (float)var3 / 255.0F;
      float var8 = Math.max(var5, Math.max(var6, var7));
      float var9 = Math.min(var5, Math.min(var6, var7));
      float var10 = var8 - var9;
      var4.lightness = (var8 + var9) * 0.5F;
      if ((double)Math.abs(var10) <= 1.0E-5D) {
         var4.hue = 0.0F;
         var4.saturation = 0.0F;
      } else {
         if (var8 != var5) {
            if (var8 == var6) {
               var4.hue = 60.0F * ((var7 - var5) / var10 + 2.0F);
            } else if (var8 == var7) {
               var4.hue = 60.0F * ((var5 - var6) / var10 + 4.0F);
            }
         } else {
            float var11;
            for(var11 = (var6 - var7) / var10; var11 < 0.0F; var11 += 6.0F) {
            }

            var4.hue = 60.0F * (var11 % 6.0F);
         }

         var4.saturation = var10 / (1.0F - Math.abs(2.0F * var4.lightness - 1.0F));
      }

      var4.sanitize();
      return var4;
   }

   public static int fromHSL(ColorHelper.HSL var0) {
      var0.sanitize();
      float var1 = (1.0F - Math.abs(2.0F * var0.lightness - 1.0F)) * var0.saturation;
      float var2 = var1 * (1.0F - Math.abs(var0.hue / 60.0F % 2.0F - 1.0F));
      float var3 = var0.lightness - var1 * 0.5F;
      float var4 = 0.0F;
      float var5 = 0.0F;
      float var6 = 0.0F;
      switch((int)var0.hue / 60 % 6) {
      case 0:
         var4 = var1;
         var5 = var2;
         break;
      case 1:
         var4 = var2;
         var5 = var1;
         break;
      case 2:
         var5 = var1;
         var6 = var2;
         break;
      case 3:
         var5 = var2;
         var6 = var1;
         break;
      case 4:
         var4 = var2;
         var6 = var1;
         break;
      case 5:
         var4 = var1;
         var6 = var2;
      }

      return getArgb(var0.alpha, MathUtils.floor((double)((var4 + var3) * 255.0F)), MathUtils.floor((double)((var5 + var3) * 255.0F)), MathUtils.floor((double)((var6 + var3) * 255.0F)));
   }

   public static int mixColor(int var0, int var1) {
      return getArgb(getAlpha(var0) * getAlpha(var1) / 255, getRed(var0) * getRed(var1) / 255, getGreen(var0) * getGreen(var1) / 255, getBlue(var0) * getBlue(var1) / 255);
   }

   public static int lerpColor(int var0, int var1, float var2) {
      int var3 = getAlpha(var0);
      int var4 = getRed(var0);
      int var5 = getGreen(var0);
      int var6 = getBlue(var0);
      int var7 = getAlpha(var1);
      int var8 = getRed(var1);
      int var9 = getGreen(var1);
      int var10 = getBlue(var1);
      int var11 = MathUtils.floor(Math.sqrt(MathUtils.lerp((double)(var3 * var3), (double)(var7 * var7), (double)var2)));
      int var12 = MathUtils.floor(Math.sqrt(MathUtils.lerp((double)(var4 * var4), (double)(var8 * var8), (double)var2)));
      int var13 = MathUtils.floor(Math.sqrt(MathUtils.lerp((double)(var5 * var5), (double)(var9 * var9), (double)var2)));
      int var14 = MathUtils.floor(Math.sqrt(MathUtils.lerp((double)(var6 * var6), (double)(var10 * var10), (double)var2)));
      return getArgb(var11, var12, var13, var14);
   }

   public static class HSL {
      public int alpha;
      public float hue;
      public float saturation;
      public float lightness;

      public HSL() {
      }

      public HSL(int var1, float var2, float var3, float var4) {
         this.alpha = var1;
         this.hue = var2;
         this.saturation = var3;
         this.lightness = var4;
      }

      public void sanitize() {
         while(this.hue < 0.0F) {
            this.hue += 360.0F;
         }

         this.saturation = Math.max(Math.min(this.saturation, 1.0F), 0.0F);
         this.lightness = Math.max(Math.min(this.lightness, 1.0F), 0.0F);
      }
   }
}
