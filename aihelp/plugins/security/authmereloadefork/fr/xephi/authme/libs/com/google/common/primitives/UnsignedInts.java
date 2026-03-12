package fr.xephi.authme.libs.com.google.common.primitives;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import fr.xephi.authme.libs.com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Comparator;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
public final class UnsignedInts {
   static final long INT_MASK = 4294967295L;

   private UnsignedInts() {
   }

   static int flip(int value) {
      return value ^ Integer.MIN_VALUE;
   }

   public static int compare(int a, int b) {
      return Ints.compare(flip(a), flip(b));
   }

   public static long toLong(int value) {
      return (long)value & 4294967295L;
   }

   public static int checkedCast(long value) {
      Preconditions.checkArgument(value >> 32 == 0L, "out of range: %s", value);
      return (int)value;
   }

   public static int saturatedCast(long value) {
      if (value <= 0L) {
         return 0;
      } else {
         return value >= 4294967296L ? -1 : (int)value;
      }
   }

   public static int min(int... array) {
      Preconditions.checkArgument(array.length > 0);
      int min = flip(array[0]);

      for(int i = 1; i < array.length; ++i) {
         int next = flip(array[i]);
         if (next < min) {
            min = next;
         }
      }

      return flip(min);
   }

   public static int max(int... array) {
      Preconditions.checkArgument(array.length > 0);
      int max = flip(array[0]);

      for(int i = 1; i < array.length; ++i) {
         int next = flip(array[i]);
         if (next > max) {
            max = next;
         }
      }

      return flip(max);
   }

   public static String join(String separator, int... array) {
      Preconditions.checkNotNull(separator);
      if (array.length == 0) {
         return "";
      } else {
         StringBuilder builder = new StringBuilder(array.length * 5);
         builder.append(toString(array[0]));

         for(int i = 1; i < array.length; ++i) {
            builder.append(separator).append(toString(array[i]));
         }

         return builder.toString();
      }
   }

   public static Comparator<int[]> lexicographicalComparator() {
      return UnsignedInts.LexicographicalComparator.INSTANCE;
   }

   public static void sort(int[] array) {
      Preconditions.checkNotNull(array);
      sort(array, 0, array.length);
   }

   public static void sort(int[] array, int fromIndex, int toIndex) {
      Preconditions.checkNotNull(array);
      Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);

      int i;
      for(i = fromIndex; i < toIndex; ++i) {
         array[i] = flip(array[i]);
      }

      Arrays.sort(array, fromIndex, toIndex);

      for(i = fromIndex; i < toIndex; ++i) {
         array[i] = flip(array[i]);
      }

   }

   public static void sortDescending(int[] array) {
      Preconditions.checkNotNull(array);
      sortDescending(array, 0, array.length);
   }

   public static void sortDescending(int[] array, int fromIndex, int toIndex) {
      Preconditions.checkNotNull(array);
      Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);

      int i;
      for(i = fromIndex; i < toIndex; ++i) {
         array[i] ^= Integer.MAX_VALUE;
      }

      Arrays.sort(array, fromIndex, toIndex);

      for(i = fromIndex; i < toIndex; ++i) {
         array[i] ^= Integer.MAX_VALUE;
      }

   }

   public static int divide(int dividend, int divisor) {
      return (int)(toLong(dividend) / toLong(divisor));
   }

   public static int remainder(int dividend, int divisor) {
      return (int)(toLong(dividend) % toLong(divisor));
   }

   @CanIgnoreReturnValue
   public static int decode(String stringValue) {
      ParseRequest request = ParseRequest.fromString(stringValue);

      try {
         return parseUnsignedInt(request.rawValue, request.radix);
      } catch (NumberFormatException var4) {
         NumberFormatException var10000 = new NumberFormatException;
         String var10003 = String.valueOf(stringValue);
         String var10002;
         if (var10003.length() != 0) {
            var10002 = "Error parsing value: ".concat(var10003);
         } else {
            String var10004 = new String;
            var10002 = var10004;
            var10004.<init>("Error parsing value: ");
         }

         var10000.<init>(var10002);
         NumberFormatException decodeException = var10000;
         decodeException.initCause(var4);
         throw decodeException;
      }
   }

   @CanIgnoreReturnValue
   public static int parseUnsignedInt(String s) {
      return parseUnsignedInt(s, 10);
   }

   @CanIgnoreReturnValue
   public static int parseUnsignedInt(String string, int radix) {
      Preconditions.checkNotNull(string);
      long result = Long.parseLong(string, radix);
      if ((result & 4294967295L) != result) {
         throw new NumberFormatException((new StringBuilder(69 + String.valueOf(string).length())).append("Input ").append(string).append(" in base ").append(radix).append(" is not in the range of an unsigned integer").toString());
      } else {
         return (int)result;
      }
   }

   public static String toString(int x) {
      return toString(x, 10);
   }

   public static String toString(int x, int radix) {
      long asLong = (long)x & 4294967295L;
      return Long.toString(asLong, radix);
   }

   static enum LexicographicalComparator implements Comparator<int[]> {
      INSTANCE;

      public int compare(int[] left, int[] right) {
         int minLength = Math.min(left.length, right.length);

         for(int i = 0; i < minLength; ++i) {
            if (left[i] != right[i]) {
               return UnsignedInts.compare(left[i], right[i]);
            }
         }

         return left.length - right.length;
      }

      public String toString() {
         return "UnsignedInts.lexicographicalComparator()";
      }

      // $FF: synthetic method
      private static UnsignedInts.LexicographicalComparator[] $values() {
         return new UnsignedInts.LexicographicalComparator[]{INSTANCE};
      }
   }
}
