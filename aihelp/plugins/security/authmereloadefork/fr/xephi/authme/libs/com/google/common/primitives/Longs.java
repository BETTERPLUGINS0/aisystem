package fr.xephi.authme.libs.com.google.common.primitives;

import fr.xephi.authme.libs.com.google.common.annotations.Beta;
import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.base.Converter;
import fr.xephi.authme.libs.com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;
import java.util.Spliterators;
import java.util.Spliterator.OfLong;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Longs {
   public static final int BYTES = 8;
   public static final long MAX_POWER_OF_TWO = 4611686018427387904L;

   private Longs() {
   }

   public static int hashCode(long value) {
      return (int)(value ^ value >>> 32);
   }

   public static int compare(long a, long b) {
      return a < b ? -1 : (a > b ? 1 : 0);
   }

   public static boolean contains(long[] array, long target) {
      long[] var3 = array;
      int var4 = array.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         long value = var3[var5];
         if (value == target) {
            return true;
         }
      }

      return false;
   }

   public static int indexOf(long[] array, long target) {
      return indexOf(array, target, 0, array.length);
   }

   private static int indexOf(long[] array, long target, int start, int end) {
      for(int i = start; i < end; ++i) {
         if (array[i] == target) {
            return i;
         }
      }

      return -1;
   }

   public static int indexOf(long[] array, long[] target) {
      Preconditions.checkNotNull(array, "array");
      Preconditions.checkNotNull(target, "target");
      if (target.length == 0) {
         return 0;
      } else {
         label28:
         for(int i = 0; i < array.length - target.length + 1; ++i) {
            for(int j = 0; j < target.length; ++j) {
               if (array[i + j] != target[j]) {
                  continue label28;
               }
            }

            return i;
         }

         return -1;
      }
   }

   public static int lastIndexOf(long[] array, long target) {
      return lastIndexOf(array, target, 0, array.length);
   }

   private static int lastIndexOf(long[] array, long target, int start, int end) {
      for(int i = end - 1; i >= start; --i) {
         if (array[i] == target) {
            return i;
         }
      }

      return -1;
   }

   public static long min(long... array) {
      Preconditions.checkArgument(array.length > 0);
      long min = array[0];

      for(int i = 1; i < array.length; ++i) {
         if (array[i] < min) {
            min = array[i];
         }
      }

      return min;
   }

   public static long max(long... array) {
      Preconditions.checkArgument(array.length > 0);
      long max = array[0];

      for(int i = 1; i < array.length; ++i) {
         if (array[i] > max) {
            max = array[i];
         }
      }

      return max;
   }

   @Beta
   public static long constrainToRange(long value, long min, long max) {
      Preconditions.checkArgument(min <= max, "min (%s) must be less than or equal to max (%s)", min, max);
      return Math.min(Math.max(value, min), max);
   }

   public static long[] concat(long[]... arrays) {
      int length = 0;
      long[][] var2 = arrays;
      int pos = arrays.length;

      for(int var4 = 0; var4 < pos; ++var4) {
         long[] array = var2[var4];
         length += array.length;
      }

      long[] result = new long[length];
      pos = 0;
      long[][] var9 = arrays;
      int var10 = arrays.length;

      for(int var6 = 0; var6 < var10; ++var6) {
         long[] array = var9[var6];
         System.arraycopy(array, 0, result, pos, array.length);
         pos += array.length;
      }

      return result;
   }

   public static byte[] toByteArray(long value) {
      byte[] result = new byte[8];

      for(int i = 7; i >= 0; --i) {
         result[i] = (byte)((int)(value & 255L));
         value >>= 8;
      }

      return result;
   }

   public static long fromByteArray(byte[] bytes) {
      Preconditions.checkArgument(bytes.length >= 8, "array too small: %s < %s", (int)bytes.length, (int)8);
      return fromBytes(bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], bytes[6], bytes[7]);
   }

   public static long fromBytes(byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7, byte b8) {
      return ((long)b1 & 255L) << 56 | ((long)b2 & 255L) << 48 | ((long)b3 & 255L) << 40 | ((long)b4 & 255L) << 32 | ((long)b5 & 255L) << 24 | ((long)b6 & 255L) << 16 | ((long)b7 & 255L) << 8 | (long)b8 & 255L;
   }

   @CheckForNull
   @Beta
   public static Long tryParse(String string) {
      return tryParse(string, 10);
   }

   @CheckForNull
   @Beta
   public static Long tryParse(String string, int radix) {
      if (((String)Preconditions.checkNotNull(string)).isEmpty()) {
         return null;
      } else if (radix >= 2 && radix <= 36) {
         boolean negative = string.charAt(0) == '-';
         int index = negative ? 1 : 0;
         if (index == string.length()) {
            return null;
         } else {
            int digit = Longs.AsciiDigits.digit(string.charAt(index++));
            if (digit >= 0 && digit < radix) {
               long accum = (long)(-digit);

               for(long cap = Long.MIN_VALUE / (long)radix; index < string.length(); accum -= (long)digit) {
                  digit = Longs.AsciiDigits.digit(string.charAt(index++));
                  if (digit < 0 || digit >= radix || accum < cap) {
                     return null;
                  }

                  accum *= (long)radix;
                  if (accum < Long.MIN_VALUE + (long)digit) {
                     return null;
                  }
               }

               if (negative) {
                  return accum;
               } else if (accum == Long.MIN_VALUE) {
                  return null;
               } else {
                  return -accum;
               }
            } else {
               return null;
            }
         }
      } else {
         throw new IllegalArgumentException((new StringBuilder(65)).append("radix must be between MIN_RADIX and MAX_RADIX but was ").append(radix).toString());
      }
   }

   @Beta
   public static Converter<String, Long> stringConverter() {
      return Longs.LongConverter.INSTANCE;
   }

   public static long[] ensureCapacity(long[] array, int minLength, int padding) {
      Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
      Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
      return array.length < minLength ? Arrays.copyOf(array, minLength + padding) : array;
   }

   public static String join(String separator, long... array) {
      Preconditions.checkNotNull(separator);
      if (array.length == 0) {
         return "";
      } else {
         StringBuilder builder = new StringBuilder(array.length * 10);
         builder.append(array[0]);

         for(int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
         }

         return builder.toString();
      }
   }

   public static Comparator<long[]> lexicographicalComparator() {
      return Longs.LexicographicalComparator.INSTANCE;
   }

   public static void sortDescending(long[] array) {
      Preconditions.checkNotNull(array);
      sortDescending(array, 0, array.length);
   }

   public static void sortDescending(long[] array, int fromIndex, int toIndex) {
      Preconditions.checkNotNull(array);
      Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
      Arrays.sort(array, fromIndex, toIndex);
      reverse(array, fromIndex, toIndex);
   }

   public static void reverse(long[] array) {
      Preconditions.checkNotNull(array);
      reverse(array, 0, array.length);
   }

   public static void reverse(long[] array, int fromIndex, int toIndex) {
      Preconditions.checkNotNull(array);
      Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
      int i = fromIndex;

      for(int j = toIndex - 1; i < j; --j) {
         long tmp = array[i];
         array[i] = array[j];
         array[j] = tmp;
         ++i;
      }

   }

   public static long[] toArray(Collection<? extends Number> collection) {
      if (collection instanceof Longs.LongArrayAsList) {
         return ((Longs.LongArrayAsList)collection).toLongArray();
      } else {
         Object[] boxedArray = collection.toArray();
         int len = boxedArray.length;
         long[] array = new long[len];

         for(int i = 0; i < len; ++i) {
            array[i] = ((Number)Preconditions.checkNotNull(boxedArray[i])).longValue();
         }

         return array;
      }
   }

   public static List<Long> asList(long... backingArray) {
      return (List)(backingArray.length == 0 ? Collections.emptyList() : new Longs.LongArrayAsList(backingArray));
   }

   @GwtCompatible
   private static class LongArrayAsList extends AbstractList<Long> implements RandomAccess, Serializable {
      final long[] array;
      final int start;
      final int end;
      private static final long serialVersionUID = 0L;

      LongArrayAsList(long[] array) {
         this(array, 0, array.length);
      }

      LongArrayAsList(long[] array, int start, int end) {
         this.array = array;
         this.start = start;
         this.end = end;
      }

      public int size() {
         return this.end - this.start;
      }

      public boolean isEmpty() {
         return false;
      }

      public Long get(int index) {
         Preconditions.checkElementIndex(index, this.size());
         return this.array[this.start + index];
      }

      public OfLong spliterator() {
         return Spliterators.spliterator(this.array, this.start, this.end, 0);
      }

      public boolean contains(@CheckForNull Object target) {
         return target instanceof Long && Longs.indexOf(this.array, (Long)target, this.start, this.end) != -1;
      }

      public int indexOf(@CheckForNull Object target) {
         if (target instanceof Long) {
            int i = Longs.indexOf(this.array, (Long)target, this.start, this.end);
            if (i >= 0) {
               return i - this.start;
            }
         }

         return -1;
      }

      public int lastIndexOf(@CheckForNull Object target) {
         if (target instanceof Long) {
            int i = Longs.lastIndexOf(this.array, (Long)target, this.start, this.end);
            if (i >= 0) {
               return i - this.start;
            }
         }

         return -1;
      }

      public Long set(int index, Long element) {
         Preconditions.checkElementIndex(index, this.size());
         long oldValue = this.array[this.start + index];
         this.array[this.start + index] = (Long)Preconditions.checkNotNull(element);
         return oldValue;
      }

      public List<Long> subList(int fromIndex, int toIndex) {
         int size = this.size();
         Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
         return (List)(fromIndex == toIndex ? Collections.emptyList() : new Longs.LongArrayAsList(this.array, this.start + fromIndex, this.start + toIndex));
      }

      public boolean equals(@CheckForNull Object object) {
         if (object == this) {
            return true;
         } else if (object instanceof Longs.LongArrayAsList) {
            Longs.LongArrayAsList that = (Longs.LongArrayAsList)object;
            int size = this.size();
            if (that.size() != size) {
               return false;
            } else {
               for(int i = 0; i < size; ++i) {
                  if (this.array[this.start + i] != that.array[that.start + i]) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return super.equals(object);
         }
      }

      public int hashCode() {
         int result = 1;

         for(int i = this.start; i < this.end; ++i) {
            result = 31 * result + Longs.hashCode(this.array[i]);
         }

         return result;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder(this.size() * 10);
         builder.append('[').append(this.array[this.start]);

         for(int i = this.start + 1; i < this.end; ++i) {
            builder.append(", ").append(this.array[i]);
         }

         return builder.append(']').toString();
      }

      long[] toLongArray() {
         return Arrays.copyOfRange(this.array, this.start, this.end);
      }
   }

   private static enum LexicographicalComparator implements Comparator<long[]> {
      INSTANCE;

      public int compare(long[] left, long[] right) {
         int minLength = Math.min(left.length, right.length);

         for(int i = 0; i < minLength; ++i) {
            int result = Longs.compare(left[i], right[i]);
            if (result != 0) {
               return result;
            }
         }

         return left.length - right.length;
      }

      public String toString() {
         return "Longs.lexicographicalComparator()";
      }

      // $FF: synthetic method
      private static Longs.LexicographicalComparator[] $values() {
         return new Longs.LexicographicalComparator[]{INSTANCE};
      }
   }

   private static final class LongConverter extends Converter<String, Long> implements Serializable {
      static final Longs.LongConverter INSTANCE = new Longs.LongConverter();
      private static final long serialVersionUID = 1L;

      protected Long doForward(String value) {
         return Long.decode(value);
      }

      protected String doBackward(Long value) {
         return value.toString();
      }

      public String toString() {
         return "Longs.stringConverter()";
      }

      private Object readResolve() {
         return INSTANCE;
      }
   }

   static final class AsciiDigits {
      private static final byte[] asciiDigits;

      private AsciiDigits() {
      }

      static int digit(char c) {
         return c < 128 ? asciiDigits[c] : -1;
      }

      static {
         byte[] result = new byte[128];
         Arrays.fill(result, (byte)-1);

         int i;
         for(i = 0; i < 10; ++i) {
            result[48 + i] = (byte)i;
         }

         for(i = 0; i < 26; ++i) {
            result[65 + i] = (byte)(10 + i);
            result[97 + i] = (byte)(10 + i);
         }

         asciiDigits = result;
      }
   }
}
