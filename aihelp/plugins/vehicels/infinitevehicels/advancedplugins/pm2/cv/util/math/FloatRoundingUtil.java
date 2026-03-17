package advancedplugins.pm2.cv.util.math;

public final class FloatRoundingUtil {
   public static final float FLOAT_ROUNDING_ERROR = 1.0E-6F;
   private static final int BIG_ENOUGH_INT = 16384;
   private static final double BIG_ENOUGH_FLOOR = 16384.0D;
   private static final double CEIL = 0.9999999D;
   private static final double BIG_ENOUGH_ROUND = 16384.5D;

   public static int floor(float value) {
      return (int)((double)var0 + 16384.0D) - 16384;
   }

   public static int floorPositive(float value) {
      return (int)var0;
   }

   public static int ceil(float value) {
      return 16384 - (int)(16384.0D - (double)var0);
   }

   public static int ceilPositive(float value) {
      return (int)((double)var0 + 0.9999999D);
   }

   public static int round(float value) {
      return (int)((double)var0 + 16384.5D) - 16384;
   }

   public static int roundPositive(float value) {
      return (int)(var0 + 0.5F);
   }

   public static boolean isZero(double value) {
      return Math.abs(var0) <= 9.999999974752427E-7D;
   }

   public static boolean isZero(float value, float tolerance) {
      return Math.abs(var0) <= var1;
   }

   public static boolean isEqual(float a, float b) {
      return Math.abs(var0 - var1) <= 1.0E-6F;
   }

   public static boolean isEqual(float a, float b, float tolerance) {
      return Math.abs(var0 - var1) <= var2;
   }
}
