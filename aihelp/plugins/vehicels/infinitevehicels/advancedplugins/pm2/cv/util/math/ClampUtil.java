package advancedplugins.pm2.cv.util.math;

public final class ClampUtil {
   public static int clamp(int value, int min, int max) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static long clamp(long value, long min, long max) {
      return Math.max(var2, Math.min(var4, var0));
   }

   public static float clamp(float value, float min, float max) {
      return Math.max(var1, Math.min(var2, var0));
   }

   public static double clamp(double value, double min, double max) {
      return Math.max(var2, Math.min(var4, var0));
   }
}
