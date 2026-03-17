package advancedplugins.pm2.cv.util.math;

import me.PM2.infinitevehicles.math.util.FastMath;

public final class CachedMathUtil {
   private static final float[] COSINE_CACHE = new float[361];
   private static final float[] SINE_CACHE = new float[361];

   public static float cos(int x) {
      return COSINE_CACHE[FastMath.abs(var0) % 360];
   }

   public static float sin(int x) {
      float var1 = SINE_CACHE[FastMath.abs(var0) % 360];
      return var0 < 0 ? -var1 : var1;
   }

   static {
      for(int var0 = 0; var0 <= 360; ++var0) {
         COSINE_CACHE[var0] = (float)FastMath.cos(Math.toRadians((double)var0));
         SINE_CACHE[var0] = (float)FastMath.sin(Math.toRadians((double)var0));
      }

   }
}
