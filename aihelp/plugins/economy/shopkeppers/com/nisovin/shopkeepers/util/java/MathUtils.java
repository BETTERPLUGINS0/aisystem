package com.nisovin.shopkeepers.util.java;

import java.util.concurrent.ThreadLocalRandom;

public final class MathUtils {
   public static final double EPSILON = 1.0E-5D;
   public static final float FLOAT_EPSILON = 1.0E-5F;

   public static boolean fuzzyEquals(double a, double b) {
      return fuzzyEquals(a, b, 1.0E-5D);
   }

   public static boolean fuzzyEquals(double a, double b, double tolerance) {
      return Double.compare(a, b) == 0 || Math.abs(a - b) <= tolerance;
   }

   public static boolean fuzzyEquals(float a, float b) {
      return fuzzyEquals(a, b, 1.0E-5F);
   }

   public static boolean fuzzyEquals(float a, float b, float tolerance) {
      return Float.compare(a, b) == 0 || Math.abs(a - b) <= tolerance;
   }

   public static int randomIntInRange(int min, int max) {
      return ThreadLocalRandom.current().nextInt(min, max);
   }

   public static float randomFloatInRange(float min, float max) {
      Validate.isTrue(min < max, "max must be greater than min");
      float value = min + ThreadLocalRandom.current().nextFloat() * (max - min);
      if (value >= max) {
         value = Float.intBitsToFloat(Float.floatToIntBits(max) - 1);
      }

      return value;
   }

   public static int addSaturated(int x, int y) {
      int result = x + y;
      if (((x ^ result) & (y ^ result)) < 0) {
         return result < 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
      } else {
         return result;
      }
   }

   public static int clamp(int value, int min, int max) {
      if (value <= min) {
         return min;
      } else {
         return value >= max ? max : value;
      }
   }

   public static int middle(int start, int end) {
      return start + (end - 1 - start) / 2;
   }

   public static double average(long[] values) {
      long total = 0L;
      long[] var3 = values;
      int var4 = values.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         long value = var3[var5];
         total += value;
      }

      return (double)total / (double)values.length;
   }

   public static double average(long[] values, long ignore) {
      long total = 0L;
      int ignored = 0;
      long[] var6 = values;
      int var7 = values.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         long value = var6[var8];
         if (value == ignore) {
            ++ignored;
         } else {
            total += value;
         }
      }

      int elementCount = values.length - ignored;
      if (elementCount == 0) {
         return 0.0D;
      } else {
         return (double)total / (double)elementCount;
      }
   }

   public static long max(long[] values) {
      if (values.length == 0) {
         return 0L;
      } else {
         long max = Long.MIN_VALUE;
         long[] var3 = values;
         int var4 = values.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            long value = var3[var5];
            if (value > max) {
               max = value;
            }
         }

         return max;
      }
   }

   public static long max(long[] values, long ignore) {
      long max = Long.MIN_VALUE;
      int ignored = 0;
      long[] var6 = values;
      int var7 = values.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         long value = var6[var8];
         if (value == ignore) {
            ++ignored;
         } else if (value > max) {
            max = value;
         }
      }

      if (values.length - ignored == 0) {
         return 0L;
      } else {
         return max;
      }
   }

   public static int rangeModulo(int value, int min, int max) {
      Validate.isTrue(min <= max, "min > max");
      int range = max - min + 1;
      int modulo = (value - min) % range;
      if (modulo < 0) {
         modulo += range;
      }

      return min + modulo;
   }

   private MathUtils() {
   }
}
