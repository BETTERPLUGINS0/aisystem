package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

public final class MathUtil {
   public static final float EPSILON = 1.0E-5F;
   public static final float F_PI = 3.1415927F;

   private MathUtil() {
   }

   public static float square(float x) {
      return x * x;
   }

   public static float cube(float x) {
      return x * x * x;
   }

   public static float lerp(float t, float a, float b) {
      return t * (b - a) + a;
   }

   public static double lerp(double t, double a, double b) {
      return t * (b - a) + a;
   }

   public static int lerp(float t, int a, int b) {
      return floor(t * (float)(b - a)) + a;
   }

   public static int clamp(int value, int min, int max) {
      return value < min ? min : Math.min(value, max);
   }

   public static double clamp(double value, double min, double max) {
      return value < min ? min : Math.min(value, max);
   }

   public static float clamp(float value, float min, float max) {
      return value < min ? min : Math.min(value, max);
   }

   public static int floor(double value) {
      int temp = (int)value;
      return value < (double)temp ? temp - 1 : temp;
   }

   public static int floor(float value) {
      int temp = (int)value;
      return value < (float)temp ? temp - 1 : temp;
   }

   public static long ceilLong(double value) {
      long temp = (long)value;
      return value > (double)temp ? temp + 1L : temp;
   }

   public static double absMax(double a, double b) {
      return Math.max(Math.abs(a), Math.abs(b));
   }
}
