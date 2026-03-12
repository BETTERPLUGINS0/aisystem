package ac.grim.grimac.utils.math;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Iterator;
import java.util.List;
import lombok.Generated;

public final class GrimMath {
   public static final double MINIMUM_DIVISOR = Math.pow(0.20000000298023224D, 3.0D) * 8.0D * 0.15D - 0.001D;
   private static final float DEGREES_TO_RADIANS = 0.017453292F;
   private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
   public static final int PACKED_HORIZONTAL_LENGTH = 1 + log2(smallestEncompassingPowerOfTwo(30000000));
   public static final int PACKED_Y_LENGTH;
   private static final long PACKED_X_MASK;
   private static final long PACKED_Y_MASK;
   private static final long PACKED_Z_MASK;
   private static final int Z_OFFSET;
   private static final int X_OFFSET;

   @Contract(
      pure = true
   )
   public static double gcd(double a, double b) {
      if (a == 0.0D) {
         return 0.0D;
      } else {
         double temp;
         if (a < b) {
            temp = a;
            a = b;
            b = temp;
         }

         while(b > MINIMUM_DIVISOR) {
            temp = a - Math.floor(a / b) * b;
            a = b;
            b = temp;
         }

         return a;
      }
   }

   @Contract(
      pure = true
   )
   public static double calculateSD(@NotNull List<Double> numbers) {
      double sum = 0.0D;
      double standardDeviation = 0.0D;

      double rotation;
      for(Iterator var5 = numbers.iterator(); var5.hasNext(); sum += rotation) {
         rotation = (Double)var5.next();
      }

      double mean = sum / (double)numbers.size();

      double num;
      for(Iterator var7 = numbers.iterator(); var7.hasNext(); standardDeviation += Math.pow(num - mean, 2.0D)) {
         num = (Double)var7.next();
      }

      return Math.sqrt(standardDeviation / (double)numbers.size());
   }

   @Contract(
      pure = true
   )
   public static int floor(double d) {
      return (int)Math.floor(d);
   }

   @Contract(
      pure = true
   )
   public static int ceil(double d) {
      return (int)Math.ceil(d);
   }

   @Contract(
      pure = true
   )
   public static int mojangFloor(double num) {
      int floor = (int)num;
      return (double)floor == num ? floor : floor - (int)(Double.doubleToRawLongBits(num) >>> 63);
   }

   @Contract(
      pure = true
   )
   public static int mojangCeil(double num) {
      int floor = (int)num;
      return (double)floor == num ? floor : floor + (int)(~Double.doubleToRawLongBits(num) >>> 63);
   }

   @Contract(
      pure = true
   )
   public static double clamp(double num, double min, double max) {
      return num < min ? min : Math.min(num, max);
   }

   @Contract(
      pure = true
   )
   public static int clamp(int num, int min, int max) {
      return num < min ? min : Math.min(num, max);
   }

   @Contract(
      pure = true
   )
   public static float clamp(float num, float min, float max) {
      return num < min ? min : Math.min(num, max);
   }

   @Contract(
      pure = true
   )
   public static double lerp(double lerpAmount, double start, double end) {
      return start + lerpAmount * (end - start);
   }

   @Contract(
      pure = true
   )
   public static double frac(double p_14186_) {
      return p_14186_ - (double)lfloor(p_14186_);
   }

   @Contract(
      pure = true
   )
   public static long lfloor(double p_14135_) {
      long i = (long)p_14135_;
      return p_14135_ < (double)i ? i - 1L : i;
   }

   @Contract(
      pure = true
   )
   public static int sign(double x) {
      return x == 0.0D ? 0 : (x > 0.0D ? 1 : -1);
   }

   @Contract(
      pure = true
   )
   public static float square(float value) {
      return value * value;
   }

   @Contract(
      pure = true
   )
   public static float sqrt(float value) {
      return (float)Math.sqrt((double)value);
   }

   public static double distanceToHorizontalCollision(double position) {
      return Math.min(Math.abs(position % 0.0015625D), Math.abs(Math.abs(position % 0.0015625D) - 0.0015625D));
   }

   @Contract(
      pure = true
   )
   public static boolean betweenRange(double value, double min, double max) {
      return value > min && value < max;
   }

   @Contract(
      pure = true
   )
   public static boolean inRange(double value, double min, double max) {
      return value >= min && value <= max;
   }

   @Contract(
      pure = true
   )
   public static boolean inRange(int value, int min, int max) {
      return value >= min && value <= max;
   }

   @Contract(
      pure = true
   )
   public static boolean isNearlySame(double a, double b, double epoch) {
      return Math.abs(a - b) < epoch;
   }

   @Contract(
      pure = true
   )
   public static long hashCode(double x, int y, double z) {
      long l = (long)(x * 3129871.0D) ^ (long)z * 116129781L ^ (long)y;
      l = l * l * 42317861L + l * 11L;
      return l >> 16;
   }

   @Contract(
      pure = true
   )
   public static float radians(float degrees) {
      return degrees * 0.017453292F;
   }

   @Contract(
      pure = true
   )
   public static long asLong(Vector3i vector) {
      return asLong(vector.getX(), vector.getY(), vector.getZ());
   }

   @Contract(
      pure = true
   )
   public static long asLong(int x, int y, int z) {
      return ((long)x & PACKED_X_MASK) << X_OFFSET | (long)y & PACKED_Y_MASK | ((long)z & PACKED_Z_MASK) << Z_OFFSET;
   }

   public static int log2(int value) {
      return ceillog2(value) - (isPowerOfTwo(value) ? 0 : 1);
   }

   public static int ceillog2(int value) {
      value = isPowerOfTwo(value) ? value : smallestEncompassingPowerOfTwo(value);
      return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)value * 125613361L >> 27) & 31];
   }

   @Contract(
      pure = true
   )
   public static boolean isPowerOfTwo(int value) {
      return value != 0 && (value & value - 1) == 0;
   }

   @Contract(
      pure = true
   )
   public static int smallestEncompassingPowerOfTwo(int value) {
      int output = value - 1;
      output |= output >> 1;
      output |= output >> 2;
      output |= output >> 4;
      output |= output >> 8;
      output |= output >> 16;
      return output + 1;
   }

   @Contract(
      pure = true
   )
   public static boolean equal(double first, double second) {
      return Math.abs(second - first) < 9.999999747378752E-6D;
   }

   @Contract(
      pure = true
   )
   public static double square(double num) {
      return num * num;
   }

   @Generated
   private GrimMath() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      PACKED_Y_LENGTH = 64 - 2 * PACKED_HORIZONTAL_LENGTH;
      PACKED_X_MASK = (1L << PACKED_HORIZONTAL_LENGTH) - 1L;
      PACKED_Y_MASK = (1L << PACKED_Y_LENGTH) - 1L;
      PACKED_Z_MASK = (1L << PACKED_HORIZONTAL_LENGTH) - 1L;
      Z_OFFSET = PACKED_Y_LENGTH;
      X_OFFSET = PACKED_Y_LENGTH + PACKED_HORIZONTAL_LENGTH;
   }
}
