package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableInt;

public class ArrayUtils {
   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
   public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];
   public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];
   public static final char[] EMPTY_CHAR_ARRAY = new char[0];
   public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];
   public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
   public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];
   public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];
   public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];
   public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
   public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];
   public static final int[] EMPTY_INT_ARRAY = new int[0];
   public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];
   public static final long[] EMPTY_LONG_ARRAY = new long[0];
   public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];
   public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];
   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
   public static final short[] EMPTY_SHORT_ARRAY = new short[0];
   public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];
   public static final String[] EMPTY_STRING_ARRAY = new String[0];
   public static final Throwable[] EMPTY_THROWABLE_ARRAY = new Throwable[0];
   public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];
   public static final int INDEX_NOT_FOUND = -1;

   public static boolean[] add(boolean[] var0, boolean var1) {
      boolean[] var2 = (boolean[])((boolean[])copyArrayGrow1(var0, Boolean.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static boolean[] add(boolean[] var0, int var1, boolean var2) {
      return (boolean[])((boolean[])add(var0, var1, var2, Boolean.TYPE));
   }

   public static byte[] add(byte[] var0, byte var1) {
      byte[] var2 = (byte[])((byte[])copyArrayGrow1(var0, Byte.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static byte[] add(byte[] var0, int var1, byte var2) {
      return (byte[])((byte[])add(var0, var1, var2, Byte.TYPE));
   }

   public static char[] add(char[] var0, char var1) {
      char[] var2 = (char[])((char[])copyArrayGrow1(var0, Character.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static char[] add(char[] var0, int var1, char var2) {
      return (char[])((char[])add(var0, var1, var2, Character.TYPE));
   }

   public static double[] add(double[] var0, double var1) {
      double[] var3 = (double[])((double[])copyArrayGrow1(var0, Double.TYPE));
      var3[var3.length - 1] = var1;
      return var3;
   }

   /** @deprecated */
   @Deprecated
   public static double[] add(double[] var0, int var1, double var2) {
      return (double[])((double[])add(var0, var1, var2, Double.TYPE));
   }

   public static float[] add(float[] var0, float var1) {
      float[] var2 = (float[])((float[])copyArrayGrow1(var0, Float.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static float[] add(float[] var0, int var1, float var2) {
      return (float[])((float[])add(var0, var1, var2, Float.TYPE));
   }

   public static int[] add(int[] var0, int var1) {
      int[] var2 = (int[])((int[])copyArrayGrow1(var0, Integer.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static int[] add(int[] var0, int var1, int var2) {
      return (int[])((int[])add(var0, var1, var2, Integer.TYPE));
   }

   /** @deprecated */
   @Deprecated
   public static long[] add(long[] var0, int var1, long var2) {
      return (long[])((long[])add(var0, var1, var2, Long.TYPE));
   }

   public static long[] add(long[] var0, long var1) {
      long[] var3 = (long[])((long[])copyArrayGrow1(var0, Long.TYPE));
      var3[var3.length - 1] = var1;
      return var3;
   }

   private static Object add(Object var0, int var1, Object var2, Class<?> var3) {
      if (var0 == null) {
         if (var1 != 0) {
            throw new IndexOutOfBoundsException("Index: " + var1 + ", Length: 0");
         } else {
            Object var6 = Array.newInstance(var3, 1);
            Array.set(var6, 0, var2);
            return var6;
         }
      } else {
         int var4 = Array.getLength(var0);
         if (var1 <= var4 && var1 >= 0) {
            Object var5 = Array.newInstance(var3, var4 + 1);
            System.arraycopy(var0, 0, var5, 0, var1);
            Array.set(var5, var1, var2);
            if (var1 < var4) {
               System.arraycopy(var0, var1, var5, var1 + 1, var4 - var1);
            }

            return var5;
         } else {
            throw new IndexOutOfBoundsException("Index: " + var1 + ", Length: " + var4);
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public static short[] add(short[] var0, int var1, short var2) {
      return (short[])((short[])add(var0, var1, var2, Short.TYPE));
   }

   public static short[] add(short[] var0, short var1) {
      short[] var2 = (short[])((short[])copyArrayGrow1(var0, Short.TYPE));
      var2[var2.length - 1] = var1;
      return var2;
   }

   /** @deprecated */
   @Deprecated
   public static <T> T[] add(T[] var0, int var1, T var2) {
      Class var3 = null;
      if (var0 != null) {
         var3 = var0.getClass().getComponentType();
      } else {
         if (var2 == null) {
            throw new IllegalArgumentException("Array and element cannot both be null");
         }

         var3 = var2.getClass();
      }

      Object[] var4 = (Object[])((Object[])add(var0, var1, var2, var3));
      return var4;
   }

   public static <T> T[] add(T[] var0, T var1) {
      Class var2;
      if (var0 != null) {
         var2 = var0.getClass().getComponentType();
      } else {
         if (var1 == null) {
            throw new IllegalArgumentException("Arguments cannot both be null");
         }

         var2 = var1.getClass();
      }

      Object[] var3 = (Object[])((Object[])copyArrayGrow1(var0, var2));
      var3[var3.length - 1] = var1;
      return var3;
   }

   public static boolean[] addAll(boolean[] var0, boolean... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         boolean[] var2 = new boolean[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static byte[] addAll(byte[] var0, byte... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         byte[] var2 = new byte[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static char[] addAll(char[] var0, char... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         char[] var2 = new char[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static double[] addAll(double[] var0, double... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         double[] var2 = new double[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static float[] addAll(float[] var0, float... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         float[] var2 = new float[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static int[] addAll(int[] var0, int... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         int[] var2 = new int[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static long[] addAll(long[] var0, long... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         long[] var2 = new long[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static short[] addAll(short[] var0, short... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         short[] var2 = new short[var0.length + var1.length];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         System.arraycopy(var1, 0, var2, var0.length, var1.length);
         return var2;
      }
   }

   public static <T> T[] addAll(T[] var0, T... var1) {
      if (var0 == null) {
         return clone(var1);
      } else if (var1 == null) {
         return clone(var0);
      } else {
         Class var2 = var0.getClass().getComponentType();
         Object[] var3 = (Object[])((Object[])Array.newInstance(var2, var0.length + var1.length));
         System.arraycopy(var0, 0, var3, 0, var0.length);

         try {
            System.arraycopy(var1, 0, var3, var0.length, var1.length);
            return var3;
         } catch (ArrayStoreException var6) {
            Class var5 = var1.getClass().getComponentType();
            if (!var2.isAssignableFrom(var5)) {
               throw new IllegalArgumentException("Cannot store " + var5.getName() + " in an array of " + var2.getName(), var6);
            } else {
               throw var6;
            }
         }
      }
   }

   public static boolean[] addFirst(boolean[] var0, boolean var1) {
      return var0 == null ? add(var0, var1) : insert(0, (boolean[])var0, (boolean[])(var1));
   }

   public static byte[] addFirst(byte[] var0, byte var1) {
      return var0 == null ? add(var0, var1) : insert(0, (byte[])var0, (byte[])(var1));
   }

   public static char[] addFirst(char[] var0, char var1) {
      return var0 == null ? add(var0, var1) : insert(0, (char[])var0, (char[])(var1));
   }

   public static double[] addFirst(double[] var0, double var1) {
      return var0 == null ? add(var0, var1) : insert(0, (double[])var0, (double[])(var1));
   }

   public static float[] addFirst(float[] var0, float var1) {
      return var0 == null ? add(var0, var1) : insert(0, (float[])var0, (float[])(var1));
   }

   public static int[] addFirst(int[] var0, int var1) {
      return var0 == null ? add(var0, var1) : insert(0, (int[])var0, (int[])(var1));
   }

   public static long[] addFirst(long[] var0, long var1) {
      return var0 == null ? add(var0, var1) : insert(0, (long[])var0, (long[])(var1));
   }

   public static short[] addFirst(short[] var0, short var1) {
      return var0 == null ? add(var0, var1) : insert(0, (short[])var0, (short[])(var1));
   }

   public static <T> T[] addFirst(T[] var0, T var1) {
      return var0 == null ? add(var0, var1) : insert(0, (Object[])var0, (Object[])(var1));
   }

   public static boolean[] clone(boolean[] var0) {
      return var0 == null ? null : (boolean[])var0.clone();
   }

   public static byte[] clone(byte[] var0) {
      return var0 == null ? null : (byte[])var0.clone();
   }

   public static char[] clone(char[] var0) {
      return var0 == null ? null : (char[])var0.clone();
   }

   public static double[] clone(double[] var0) {
      return var0 == null ? null : (double[])var0.clone();
   }

   public static float[] clone(float[] var0) {
      return var0 == null ? null : (float[])var0.clone();
   }

   public static int[] clone(int[] var0) {
      return var0 == null ? null : (int[])var0.clone();
   }

   public static long[] clone(long[] var0) {
      return var0 == null ? null : (long[])var0.clone();
   }

   public static short[] clone(short[] var0) {
      return var0 == null ? null : (short[])var0.clone();
   }

   public static <T> T[] clone(T[] var0) {
      return var0 == null ? null : (Object[])var0.clone();
   }

   public static boolean contains(boolean[] var0, boolean var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(byte[] var0, byte var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(char[] var0, char var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(double[] var0, double var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(double[] var0, double var1, double var3) {
      return indexOf(var0, var1, 0, var3) != -1;
   }

   public static boolean contains(float[] var0, float var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(int[] var0, int var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(long[] var0, long var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(Object[] var0, Object var1) {
      return indexOf(var0, var1) != -1;
   }

   public static boolean contains(short[] var0, short var1) {
      return indexOf(var0, var1) != -1;
   }

   private static Object copyArrayGrow1(Object var0, Class<?> var1) {
      if (var0 != null) {
         int var2 = Array.getLength(var0);
         Object var3 = Array.newInstance(var0.getClass().getComponentType(), var2 + 1);
         System.arraycopy(var0, 0, var3, 0, var2);
         return var3;
      } else {
         return Array.newInstance(var1, 1);
      }
   }

   public static <T> T get(T[] var0, int var1) {
      return get(var0, var1, (Object)null);
   }

   public static <T> T get(T[] var0, int var1, T var2) {
      return isArrayIndexValid(var0, var1) ? var0[var1] : var2;
   }

   public static int getLength(Object var0) {
      return var0 == null ? 0 : Array.getLength(var0);
   }

   public static int hashCode(Object var0) {
      return (new HashCodeBuilder()).append(var0).toHashCode();
   }

   public static BitSet indexesOf(boolean[] var0, boolean var1) {
      return indexesOf(var0, var1, 0);
   }

   public static BitSet indexesOf(boolean[] var0, boolean var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static BitSet indexesOf(byte[] var0, byte var1) {
      return indexesOf((byte[])var0, (byte)var1, 0);
   }

   public static BitSet indexesOf(byte[] var0, byte var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static BitSet indexesOf(char[] var0, char var1) {
      return indexesOf((char[])var0, (char)var1, 0);
   }

   public static BitSet indexesOf(char[] var0, char var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static BitSet indexesOf(double[] var0, double var1) {
      return indexesOf(var0, var1, 0);
   }

   public static BitSet indexesOf(double[] var0, double var1, double var3) {
      return indexesOf(var0, var1, 0, var3);
   }

   public static BitSet indexesOf(double[] var0, double var1, int var3) {
      BitSet var4 = new BitSet();
      if (var0 == null) {
         return var4;
      } else {
         while(var3 < var0.length) {
            var3 = indexOf(var0, var1, var3);
            if (var3 == -1) {
               break;
            }

            var4.set(var3);
            ++var3;
         }

         return var4;
      }
   }

   public static BitSet indexesOf(double[] var0, double var1, int var3, double var4) {
      BitSet var6 = new BitSet();
      if (var0 == null) {
         return var6;
      } else {
         while(var3 < var0.length) {
            var3 = indexOf(var0, var1, var3, var4);
            if (var3 == -1) {
               break;
            }

            var6.set(var3);
            ++var3;
         }

         return var6;
      }
   }

   public static BitSet indexesOf(float[] var0, float var1) {
      return indexesOf(var0, var1, 0);
   }

   public static BitSet indexesOf(float[] var0, float var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static BitSet indexesOf(int[] var0, int var1) {
      return indexesOf((int[])var0, (int)var1, 0);
   }

   public static BitSet indexesOf(int[] var0, int var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static BitSet indexesOf(long[] var0, long var1) {
      return indexesOf(var0, var1, 0);
   }

   public static BitSet indexesOf(long[] var0, long var1, int var3) {
      BitSet var4 = new BitSet();
      if (var0 == null) {
         return var4;
      } else {
         while(var3 < var0.length) {
            var3 = indexOf(var0, var1, var3);
            if (var3 == -1) {
               break;
            }

            var4.set(var3);
            ++var3;
         }

         return var4;
      }
   }

   public static BitSet indexesOf(Object[] var0, Object var1) {
      return indexesOf(var0, var1, 0);
   }

   public static BitSet indexesOf(Object[] var0, Object var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static BitSet indexesOf(short[] var0, short var1) {
      return indexesOf((short[])var0, (short)var1, 0);
   }

   public static BitSet indexesOf(short[] var0, short var1, int var2) {
      BitSet var3 = new BitSet();
      if (var0 == null) {
         return var3;
      } else {
         while(var2 < var0.length) {
            var2 = indexOf(var0, var1, var2);
            if (var2 == -1) {
               break;
            }

            var3.set(var2);
            ++var2;
         }

         return var3;
      }
   }

   public static int indexOf(boolean[] var0, boolean var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(boolean[] var0, boolean var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int indexOf(byte[] var0, byte var1) {
      return indexOf((byte[])var0, (byte)var1, 0);
   }

   public static int indexOf(byte[] var0, byte var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int indexOf(char[] var0, char var1) {
      return indexOf((char[])var0, (char)var1, 0);
   }

   public static int indexOf(char[] var0, char var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int indexOf(double[] var0, double var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(double[] var0, double var1, double var3) {
      return indexOf(var0, var1, 0, var3);
   }

   public static int indexOf(double[] var0, double var1, int var3) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         if (var3 < 0) {
            var3 = 0;
         }

         boolean var4 = Double.isNaN(var1);

         for(int var5 = var3; var5 < var0.length; ++var5) {
            double var6 = var0[var5];
            if (var1 == var6 || var4 && Double.isNaN(var6)) {
               return var5;
            }
         }

         return -1;
      }
   }

   public static int indexOf(double[] var0, double var1, int var3, double var4) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         if (var3 < 0) {
            var3 = 0;
         }

         double var6 = var1 - var4;
         double var8 = var1 + var4;

         for(int var10 = var3; var10 < var0.length; ++var10) {
            if (var0[var10] >= var6 && var0[var10] <= var8) {
               return var10;
            }
         }

         return -1;
      }
   }

   public static int indexOf(float[] var0, float var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(float[] var0, float var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         boolean var3 = Float.isNaN(var1);

         for(int var4 = var2; var4 < var0.length; ++var4) {
            float var5 = var0[var4];
            if (var1 == var5 || var3 && Float.isNaN(var5)) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int indexOf(int[] var0, int var1) {
      return indexOf((int[])var0, (int)var1, 0);
   }

   public static int indexOf(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int indexOf(long[] var0, long var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(long[] var0, long var1, int var3) {
      if (var0 == null) {
         return -1;
      } else {
         if (var3 < 0) {
            var3 = 0;
         }

         for(int var4 = var3; var4 < var0.length; ++var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int indexOf(Object[] var0, Object var1) {
      return indexOf(var0, var1, 0);
   }

   public static int indexOf(Object[] var0, Object var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         int var3;
         if (var1 == null) {
            for(var3 = var2; var3 < var0.length; ++var3) {
               if (var0[var3] == null) {
                  return var3;
               }
            }
         } else {
            for(var3 = var2; var3 < var0.length; ++var3) {
               if (var1.equals(var0[var3])) {
                  return var3;
               }
            }
         }

         return -1;
      }
   }

   public static int indexOf(short[] var0, short var1) {
      return indexOf((short[])var0, (short)var1, 0);
   }

   public static int indexOf(short[] var0, short var1, int var2) {
      if (var0 == null) {
         return -1;
      } else {
         if (var2 < 0) {
            var2 = 0;
         }

         for(int var3 = var2; var3 < var0.length; ++var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean[] insert(int var0, boolean[] var1, boolean... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         boolean[] var3 = new boolean[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static byte[] insert(int var0, byte[] var1, byte... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         byte[] var3 = new byte[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static char[] insert(int var0, char[] var1, char... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         char[] var3 = new char[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static double[] insert(int var0, double[] var1, double... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         double[] var3 = new double[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static float[] insert(int var0, float[] var1, float... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         float[] var3 = new float[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static int[] insert(int var0, int[] var1, int... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         int[] var3 = new int[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static long[] insert(int var0, long[] var1, long... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         long[] var3 = new long[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static short[] insert(int var0, short[] var1, short... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         short[] var3 = new short[var1.length + var2.length];
         System.arraycopy(var2, 0, var3, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var3, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var3, var0 + var2.length, var1.length - var0);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   @SafeVarargs
   public static <T> T[] insert(int var0, T[] var1, T... var2) {
      if (var1 == null) {
         return null;
      } else if (isEmpty(var2)) {
         return clone(var1);
      } else if (var0 >= 0 && var0 <= var1.length) {
         Class var3 = var1.getClass().getComponentType();
         Object[] var4 = (Object[])((Object[])Array.newInstance(var3, var1.length + var2.length));
         System.arraycopy(var2, 0, var4, var0, var2.length);
         if (var0 > 0) {
            System.arraycopy(var1, 0, var4, 0, var0);
         }

         if (var0 < var1.length) {
            System.arraycopy(var1, var0, var4, var0 + var2.length, var1.length - var0);
         }

         return var4;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var0 + ", Length: " + var1.length);
      }
   }

   public static <T> boolean isArrayIndexValid(T[] var0, int var1) {
      return var1 >= 0 && getLength(var0) > var1;
   }

   public static boolean isEmpty(boolean[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(byte[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(char[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(double[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(float[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(int[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(long[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(Object[] var0) {
      return getLength(var0) == 0;
   }

   public static boolean isEmpty(short[] var0) {
      return getLength(var0) == 0;
   }

   /** @deprecated */
   @Deprecated
   public static boolean isEquals(Object var0, Object var1) {
      return (new EqualsBuilder()).append(var0, var1).isEquals();
   }

   public static boolean isNotEmpty(boolean[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(byte[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(char[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(double[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(float[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(int[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(long[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isNotEmpty(short[] var0) {
      return !isEmpty(var0);
   }

   public static <T> boolean isNotEmpty(T[] var0) {
      return !isEmpty(var0);
   }

   public static boolean isSameLength(boolean[] var0, boolean[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(byte[] var0, byte[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(char[] var0, char[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(double[] var0, double[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(float[] var0, float[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(int[] var0, int[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(long[] var0, long[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(Object var0, Object var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(Object[] var0, Object[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameLength(short[] var0, short[] var1) {
      return getLength(var0) == getLength(var1);
   }

   public static boolean isSameType(Object var0, Object var1) {
      if (var0 != null && var1 != null) {
         return var0.getClass().getName().equals(var1.getClass().getName());
      } else {
         throw new IllegalArgumentException("The Array must not be null");
      }
   }

   public static boolean isSorted(boolean[] var0) {
      if (var0 != null && var0.length >= 2) {
         boolean var1 = var0[0];
         int var2 = var0.length;

         for(int var3 = 1; var3 < var2; ++var3) {
            boolean var4 = var0[var3];
            if (BooleanUtils.compare(var1, var4) > 0) {
               return false;
            }

            var1 = var4;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(byte[] var0) {
      if (var0 != null && var0.length >= 2) {
         byte var1 = var0[0];
         int var2 = var0.length;

         for(int var3 = 1; var3 < var2; ++var3) {
            byte var4 = var0[var3];
            if (NumberUtils.compare(var1, var4) > 0) {
               return false;
            }

            var1 = var4;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(char[] var0) {
      if (var0 != null && var0.length >= 2) {
         char var1 = var0[0];
         int var2 = var0.length;

         for(int var3 = 1; var3 < var2; ++var3) {
            char var4 = var0[var3];
            if (CharUtils.compare(var1, var4) > 0) {
               return false;
            }

            var1 = var4;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(double[] var0) {
      if (var0 != null && var0.length >= 2) {
         double var1 = var0[0];
         int var3 = var0.length;

         for(int var4 = 1; var4 < var3; ++var4) {
            double var5 = var0[var4];
            if (Double.compare(var1, var5) > 0) {
               return false;
            }

            var1 = var5;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(float[] var0) {
      if (var0 != null && var0.length >= 2) {
         float var1 = var0[0];
         int var2 = var0.length;

         for(int var3 = 1; var3 < var2; ++var3) {
            float var4 = var0[var3];
            if (Float.compare(var1, var4) > 0) {
               return false;
            }

            var1 = var4;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(int[] var0) {
      if (var0 != null && var0.length >= 2) {
         int var1 = var0[0];
         int var2 = var0.length;

         for(int var3 = 1; var3 < var2; ++var3) {
            int var4 = var0[var3];
            if (NumberUtils.compare(var1, var4) > 0) {
               return false;
            }

            var1 = var4;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(long[] var0) {
      if (var0 != null && var0.length >= 2) {
         long var1 = var0[0];
         int var3 = var0.length;

         for(int var4 = 1; var4 < var3; ++var4) {
            long var5 = var0[var4];
            if (NumberUtils.compare(var1, var5) > 0) {
               return false;
            }

            var1 = var5;
         }

         return true;
      } else {
         return true;
      }
   }

   public static boolean isSorted(short[] var0) {
      if (var0 != null && var0.length >= 2) {
         short var1 = var0[0];
         int var2 = var0.length;

         for(int var3 = 1; var3 < var2; ++var3) {
            short var4 = var0[var3];
            if (NumberUtils.compare(var1, var4) > 0) {
               return false;
            }

            var1 = var4;
         }

         return true;
      } else {
         return true;
      }
   }

   public static <T extends Comparable<? super T>> boolean isSorted(T[] var0) {
      return isSorted(var0, Comparable::compareTo);
   }

   public static <T> boolean isSorted(T[] var0, Comparator<T> var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Comparator should not be null.");
      } else if (var0 != null && var0.length >= 2) {
         Object var2 = var0[0];
         int var3 = var0.length;

         for(int var4 = 1; var4 < var3; ++var4) {
            Object var5 = var0[var4];
            if (var1.compare(var2, var5) > 0) {
               return false;
            }

            var2 = var5;
         }

         return true;
      } else {
         return true;
      }
   }

   public static int lastIndexOf(boolean[] var0, boolean var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(boolean[] var0, boolean var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(byte[] var0, byte var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(byte[] var0, byte var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(char[] var0, char var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(char[] var0, char var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(double[] var0, double var1, double var3) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE, var3);
   }

   public static int lastIndexOf(double[] var0, double var1, int var3) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         if (var3 >= var0.length) {
            var3 = var0.length - 1;
         }

         for(int var4 = var3; var4 >= 0; --var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(double[] var0, double var1, int var3, double var4) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         if (var3 >= var0.length) {
            var3 = var0.length - 1;
         }

         double var6 = var1 - var4;
         double var8 = var1 + var4;

         for(int var10 = var3; var10 >= 0; --var10) {
            if (var0[var10] >= var6 && var0[var10] <= var8) {
               return var10;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(float[] var0, float var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(float[] var0, float var1, int var2) {
      if (isEmpty(var0)) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(int[] var0, int var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(long[] var0, long var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(long[] var0, long var1, int var3) {
      if (var0 == null) {
         return -1;
      } else if (var3 < 0) {
         return -1;
      } else {
         if (var3 >= var0.length) {
            var3 = var0.length - 1;
         }

         for(int var4 = var3; var4 >= 0; --var4) {
            if (var1 == var0[var4]) {
               return var4;
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(Object[] var0, Object var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(Object[] var0, Object var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         int var3;
         if (var1 == null) {
            for(var3 = var2; var3 >= 0; --var3) {
               if (var0[var3] == null) {
                  return var3;
               }
            }
         } else if (var0.getClass().getComponentType().isInstance(var1)) {
            for(var3 = var2; var3 >= 0; --var3) {
               if (var1.equals(var0[var3])) {
                  return var3;
               }
            }
         }

         return -1;
      }
   }

   public static int lastIndexOf(short[] var0, short var1) {
      return lastIndexOf(var0, var1, Integer.MAX_VALUE);
   }

   public static int lastIndexOf(short[] var0, short var1, int var2) {
      if (var0 == null) {
         return -1;
      } else if (var2 < 0) {
         return -1;
      } else {
         if (var2 >= var0.length) {
            var2 = var0.length - 1;
         }

         for(int var3 = var2; var3 >= 0; --var3) {
            if (var1 == var0[var3]) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static boolean[] nullToEmpty(boolean[] var0) {
      return isEmpty(var0) ? EMPTY_BOOLEAN_ARRAY : var0;
   }

   public static Boolean[] nullToEmpty(Boolean[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_BOOLEAN_OBJECT_ARRAY : var0;
   }

   public static byte[] nullToEmpty(byte[] var0) {
      return isEmpty(var0) ? EMPTY_BYTE_ARRAY : var0;
   }

   public static Byte[] nullToEmpty(Byte[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_BYTE_OBJECT_ARRAY : var0;
   }

   public static char[] nullToEmpty(char[] var0) {
      return isEmpty(var0) ? EMPTY_CHAR_ARRAY : var0;
   }

   public static Character[] nullToEmpty(Character[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_CHARACTER_OBJECT_ARRAY : var0;
   }

   public static Class<?>[] nullToEmpty(Class<?>[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_CLASS_ARRAY : var0;
   }

   public static double[] nullToEmpty(double[] var0) {
      return isEmpty(var0) ? EMPTY_DOUBLE_ARRAY : var0;
   }

   public static Double[] nullToEmpty(Double[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_DOUBLE_OBJECT_ARRAY : var0;
   }

   public static float[] nullToEmpty(float[] var0) {
      return isEmpty(var0) ? EMPTY_FLOAT_ARRAY : var0;
   }

   public static Float[] nullToEmpty(Float[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_FLOAT_OBJECT_ARRAY : var0;
   }

   public static int[] nullToEmpty(int[] var0) {
      return isEmpty(var0) ? EMPTY_INT_ARRAY : var0;
   }

   public static Integer[] nullToEmpty(Integer[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_INTEGER_OBJECT_ARRAY : var0;
   }

   public static long[] nullToEmpty(long[] var0) {
      return isEmpty(var0) ? EMPTY_LONG_ARRAY : var0;
   }

   public static Long[] nullToEmpty(Long[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_LONG_OBJECT_ARRAY : var0;
   }

   public static Object[] nullToEmpty(Object[] var0) {
      return isEmpty(var0) ? EMPTY_OBJECT_ARRAY : var0;
   }

   public static short[] nullToEmpty(short[] var0) {
      return isEmpty(var0) ? EMPTY_SHORT_ARRAY : var0;
   }

   public static Short[] nullToEmpty(Short[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_SHORT_OBJECT_ARRAY : var0;
   }

   public static String[] nullToEmpty(String[] var0) {
      return isEmpty((Object[])var0) ? EMPTY_STRING_ARRAY : var0;
   }

   public static <T> T[] nullToEmpty(T[] var0, Class<T[]> var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The type must not be null");
      } else {
         return var0 == null ? (Object[])var1.cast(Array.newInstance(var1.getComponentType(), 0)) : var0;
      }
   }

   public static boolean[] remove(boolean[] var0, int var1) {
      return (boolean[])((boolean[])remove((Object)var0, var1));
   }

   public static byte[] remove(byte[] var0, int var1) {
      return (byte[])((byte[])remove((Object)var0, var1));
   }

   public static char[] remove(char[] var0, int var1) {
      return (char[])((char[])remove((Object)var0, var1));
   }

   public static double[] remove(double[] var0, int var1) {
      return (double[])((double[])remove((Object)var0, var1));
   }

   public static float[] remove(float[] var0, int var1) {
      return (float[])((float[])remove((Object)var0, var1));
   }

   public static int[] remove(int[] var0, int var1) {
      return (int[])((int[])remove((Object)var0, var1));
   }

   public static long[] remove(long[] var0, int var1) {
      return (long[])((long[])remove((Object)var0, var1));
   }

   private static Object remove(Object var0, int var1) {
      int var2 = getLength(var0);
      if (var1 >= 0 && var1 < var2) {
         Object var3 = Array.newInstance(var0.getClass().getComponentType(), var2 - 1);
         System.arraycopy(var0, 0, var3, 0, var1);
         if (var1 < var2 - 1) {
            System.arraycopy(var0, var1 + 1, var3, var1, var2 - var1 - 1);
         }

         return var3;
      } else {
         throw new IndexOutOfBoundsException("Index: " + var1 + ", Length: " + var2);
      }
   }

   public static short[] remove(short[] var0, int var1) {
      return (short[])((short[])remove((Object)var0, var1));
   }

   public static <T> T[] remove(T[] var0, int var1) {
      return (Object[])((Object[])remove((Object)var0, var1));
   }

   public static boolean[] removeAll(boolean[] var0, int... var1) {
      return (boolean[])((boolean[])removeAll((Object)var0, (int[])var1));
   }

   public static byte[] removeAll(byte[] var0, int... var1) {
      return (byte[])((byte[])removeAll((Object)var0, (int[])var1));
   }

   public static char[] removeAll(char[] var0, int... var1) {
      return (char[])((char[])removeAll((Object)var0, (int[])var1));
   }

   public static double[] removeAll(double[] var0, int... var1) {
      return (double[])((double[])removeAll((Object)var0, (int[])var1));
   }

   public static float[] removeAll(float[] var0, int... var1) {
      return (float[])((float[])removeAll((Object)var0, (int[])var1));
   }

   public static int[] removeAll(int[] var0, int... var1) {
      return (int[])((int[])removeAll((Object)var0, (int[])var1));
   }

   public static long[] removeAll(long[] var0, int... var1) {
      return (long[])((long[])removeAll((Object)var0, (int[])var1));
   }

   static Object removeAll(Object var0, BitSet var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = getLength(var0);
         int var3 = var1.cardinality();
         Object var4 = Array.newInstance(var0.getClass().getComponentType(), var2 - var3);
         int var5 = 0;

         int var6;
         int var7;
         int var8;
         for(var6 = 0; (var8 = var1.nextSetBit(var5)) != -1; var5 = var1.nextClearBit(var8)) {
            var7 = var8 - var5;
            if (var7 > 0) {
               System.arraycopy(var0, var5, var4, var6, var7);
               var6 += var7;
            }
         }

         var7 = var2 - var5;
         if (var7 > 0) {
            System.arraycopy(var0, var5, var4, var6, var7);
         }

         return var4;
      }
   }

   static Object removeAll(Object var0, int... var1) {
      int var2 = getLength(var0);
      int var3 = 0;
      int[] var4 = ArraySorter.sort(clone(var1));
      int var6;
      int var7;
      if (isNotEmpty(var4)) {
         int var5 = var4.length;
         var6 = var2;

         while(true) {
            --var5;
            if (var5 < 0) {
               break;
            }

            var7 = var4[var5];
            if (var7 < 0 || var7 >= var2) {
               throw new IndexOutOfBoundsException("Index: " + var7 + ", Length: " + var2);
            }

            if (var7 < var6) {
               ++var3;
               var6 = var7;
            }
         }
      }

      Object var11 = Array.newInstance(var0.getClass().getComponentType(), var2 - var3);
      if (var3 < var2) {
         var6 = var2;
         var7 = var2 - var3;

         for(int var8 = var4.length - 1; var8 >= 0; --var8) {
            int var9 = var4[var8];
            if (var6 - var9 > 1) {
               int var10 = var6 - var9 - 1;
               var7 -= var10;
               System.arraycopy(var0, var9 + 1, var11, var7, var10);
            }

            var6 = var9;
         }

         if (var6 > 0) {
            System.arraycopy(var0, 0, var11, 0, var6);
         }
      }

      return var11;
   }

   public static short[] removeAll(short[] var0, int... var1) {
      return (short[])((short[])removeAll((Object)var0, (int[])var1));
   }

   public static <T> T[] removeAll(T[] var0, int... var1) {
      return (Object[])((Object[])removeAll((Object)var0, (int[])var1));
   }

   /** @deprecated */
   @Deprecated
   public static boolean[] removeAllOccurences(boolean[] var0, boolean var1) {
      return (boolean[])((boolean[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static byte[] removeAllOccurences(byte[] var0, byte var1) {
      return (byte[])((byte[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static char[] removeAllOccurences(char[] var0, char var1) {
      return (char[])((char[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static double[] removeAllOccurences(double[] var0, double var1) {
      return (double[])((double[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static float[] removeAllOccurences(float[] var0, float var1) {
      return (float[])((float[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static int[] removeAllOccurences(int[] var0, int var1) {
      return (int[])((int[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static long[] removeAllOccurences(long[] var0, long var1) {
      return (long[])((long[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static short[] removeAllOccurences(short[] var0, short var1) {
      return (short[])((short[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   /** @deprecated */
   @Deprecated
   public static <T> T[] removeAllOccurences(T[] var0, T var1) {
      return (Object[])((Object[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static boolean[] removeAllOccurrences(boolean[] var0, boolean var1) {
      return (boolean[])((boolean[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static byte[] removeAllOccurrences(byte[] var0, byte var1) {
      return (byte[])((byte[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static char[] removeAllOccurrences(char[] var0, char var1) {
      return (char[])((char[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static double[] removeAllOccurrences(double[] var0, double var1) {
      return (double[])((double[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static float[] removeAllOccurrences(float[] var0, float var1) {
      return (float[])((float[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static int[] removeAllOccurrences(int[] var0, int var1) {
      return (int[])((int[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static long[] removeAllOccurrences(long[] var0, long var1) {
      return (long[])((long[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static short[] removeAllOccurrences(short[] var0, short var1) {
      return (short[])((short[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static <T> T[] removeAllOccurrences(T[] var0, T var1) {
      return (Object[])((Object[])removeAll((Object)var0, (BitSet)indexesOf(var0, var1)));
   }

   public static boolean[] removeElement(boolean[] var0, boolean var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static byte[] removeElement(byte[] var0, byte var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static char[] removeElement(char[] var0, char var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static double[] removeElement(double[] var0, double var1) {
      int var3 = indexOf(var0, var1);
      return var3 == -1 ? clone(var0) : remove(var0, var3);
   }

   public static float[] removeElement(float[] var0, float var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static int[] removeElement(int[] var0, int var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static long[] removeElement(long[] var0, long var1) {
      int var3 = indexOf(var0, var1);
      return var3 == -1 ? clone(var0) : remove(var0, var3);
   }

   public static short[] removeElement(short[] var0, short var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static <T> T[] removeElement(T[] var0, Object var1) {
      int var2 = indexOf(var0, var1);
      return var2 == -1 ? clone(var0) : remove(var0, var2);
   }

   public static boolean[] removeElements(boolean[] var0, boolean... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(2);
         boolean[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            boolean var6 = var3[var5];
            Boolean var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            boolean var10 = var0[var4];
            MutableInt var11 = (MutableInt)var2.get(var10);
            if (var11 != null) {
               if (var11.decrementAndGet() == 0) {
                  var2.remove(var10);
               }

               var9.set(var4);
            }
         }

         return (boolean[])((boolean[])removeAll((Object)var0, (BitSet)var9));
      } else {
         return clone(var0);
      }
   }

   public static byte[] removeElements(byte[] var0, byte... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         byte[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte var6 = var3[var5];
            Byte var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            byte var10 = var0[var4];
            MutableInt var11 = (MutableInt)var2.get(var10);
            if (var11 != null) {
               if (var11.decrementAndGet() == 0) {
                  var2.remove(var10);
               }

               var9.set(var4);
            }
         }

         return (byte[])((byte[])removeAll((Object)var0, (BitSet)var9));
      } else {
         return clone(var0);
      }
   }

   public static char[] removeElements(char[] var0, char... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         char[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char var6 = var3[var5];
            Character var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            char var10 = var0[var4];
            MutableInt var11 = (MutableInt)var2.get(var10);
            if (var11 != null) {
               if (var11.decrementAndGet() == 0) {
                  var2.remove(var10);
               }

               var9.set(var4);
            }
         }

         return (char[])((char[])removeAll((Object)var0, (BitSet)var9));
      } else {
         return clone(var0);
      }
   }

   public static double[] removeElements(double[] var0, double... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         double[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            double var6 = var3[var5];
            Double var8 = var6;
            MutableInt var9 = (MutableInt)var2.get(var8);
            if (var9 == null) {
               var2.put(var8, new MutableInt(1));
            } else {
               var9.increment();
            }
         }

         BitSet var10 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            double var11 = var0[var4];
            MutableInt var7 = (MutableInt)var2.get(var11);
            if (var7 != null) {
               if (var7.decrementAndGet() == 0) {
                  var2.remove(var11);
               }

               var10.set(var4);
            }
         }

         return (double[])((double[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static float[] removeElements(float[] var0, float... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         float[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            float var6 = var3[var5];
            Float var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            float var10 = var0[var4];
            MutableInt var11 = (MutableInt)var2.get(var10);
            if (var11 != null) {
               if (var11.decrementAndGet() == 0) {
                  var2.remove(var10);
               }

               var9.set(var4);
            }
         }

         return (float[])((float[])removeAll((Object)var0, (BitSet)var9));
      } else {
         return clone(var0);
      }
   }

   public static int[] removeElements(int[] var0, int... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         int[] var3 = var1;
         int var4 = var1.length;

         int var5;
         for(var5 = 0; var5 < var4; ++var5) {
            int var6 = var3[var5];
            Integer var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            var5 = var0[var4];
            MutableInt var10 = (MutableInt)var2.get(var5);
            if (var10 != null) {
               if (var10.decrementAndGet() == 0) {
                  var2.remove(var5);
               }

               var9.set(var4);
            }
         }

         return (int[])((int[])removeAll((Object)var0, (BitSet)var9));
      } else {
         return clone(var0);
      }
   }

   public static long[] removeElements(long[] var0, long... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         long[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            long var6 = var3[var5];
            Long var8 = var6;
            MutableInt var9 = (MutableInt)var2.get(var8);
            if (var9 == null) {
               var2.put(var8, new MutableInt(1));
            } else {
               var9.increment();
            }
         }

         BitSet var10 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            long var11 = var0[var4];
            MutableInt var7 = (MutableInt)var2.get(var11);
            if (var7 != null) {
               if (var7.decrementAndGet() == 0) {
                  var2.remove(var11);
               }

               var10.set(var4);
            }
         }

         return (long[])((long[])removeAll((Object)var0, (BitSet)var10));
      } else {
         return clone(var0);
      }
   }

   public static short[] removeElements(short[] var0, short... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         short[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            short var6 = var3[var5];
            Short var7 = var6;
            MutableInt var8 = (MutableInt)var2.get(var7);
            if (var8 == null) {
               var2.put(var7, new MutableInt(1));
            } else {
               var8.increment();
            }
         }

         BitSet var9 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            short var10 = var0[var4];
            MutableInt var11 = (MutableInt)var2.get(var10);
            if (var11 != null) {
               if (var11.decrementAndGet() == 0) {
                  var2.remove(var10);
               }

               var9.set(var4);
            }
         }

         return (short[])((short[])removeAll((Object)var0, (BitSet)var9));
      } else {
         return clone(var0);
      }
   }

   @SafeVarargs
   public static <T> T[] removeElements(T[] var0, T... var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         HashMap var2 = new HashMap(var1.length);
         Object[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Object var6 = var3[var5];
            MutableInt var7 = (MutableInt)var2.get(var6);
            if (var7 == null) {
               var2.put(var6, new MutableInt(1));
            } else {
               var7.increment();
            }
         }

         BitSet var8 = new BitSet();

         for(var4 = 0; var4 < var0.length; ++var4) {
            Object var10 = var0[var4];
            MutableInt var11 = (MutableInt)var2.get(var10);
            if (var11 != null) {
               if (var11.decrementAndGet() == 0) {
                  var2.remove(var10);
               }

               var8.set(var4);
            }
         }

         Object[] var9 = (Object[])((Object[])removeAll((Object)var0, (BitSet)var8));
         return var9;
      } else {
         return clone(var0);
      }
   }

   public static void reverse(boolean[] var0) {
      if (var0 != null) {
         reverse((boolean[])var0, 0, var0.length);
      }
   }

   public static void reverse(boolean[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            boolean var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(byte[] var0) {
      if (var0 != null) {
         reverse((byte[])var0, 0, var0.length);
      }
   }

   public static void reverse(byte[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            byte var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(char[] var0) {
      if (var0 != null) {
         reverse((char[])var0, 0, var0.length);
      }
   }

   public static void reverse(char[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            char var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(double[] var0) {
      if (var0 != null) {
         reverse((double[])var0, 0, var0.length);
      }
   }

   public static void reverse(double[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            double var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(float[] var0) {
      if (var0 != null) {
         reverse((float[])var0, 0, var0.length);
      }
   }

   public static void reverse(float[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            float var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(int[] var0) {
      if (var0 != null) {
         reverse((int[])var0, 0, var0.length);
      }
   }

   public static void reverse(int[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            int var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(long[] var0) {
      if (var0 != null) {
         reverse((long[])var0, 0, var0.length);
      }
   }

   public static void reverse(long[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            long var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(Object[] var0) {
      if (var0 != null) {
         reverse((Object[])var0, 0, var0.length);
      }
   }

   public static void reverse(Object[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            Object var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void reverse(short[] var0) {
      if (var0 != null) {
         reverse((short[])var0, 0, var0.length);
      }
   }

   public static void reverse(short[] var0, int var1, int var2) {
      if (var0 != null) {
         int var3 = Math.max(var1, 0);

         for(int var4 = Math.min(var0.length, var2) - 1; var4 > var3; ++var3) {
            short var5 = var0[var4];
            var0[var4] = var0[var3];
            var0[var3] = var5;
            --var4;
         }

      }
   }

   public static void shift(boolean[] var0, int var1) {
      if (var0 != null) {
         shift((boolean[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(boolean[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(byte[] var0, int var1) {
      if (var0 != null) {
         shift((byte[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(byte[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(char[] var0, int var1) {
      if (var0 != null) {
         shift((char[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(char[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(double[] var0, int var1) {
      if (var0 != null) {
         shift((double[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(double[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(float[] var0, int var1) {
      if (var0 != null) {
         shift((float[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(float[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(int[] var0, int var1) {
      if (var0 != null) {
         shift((int[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(int[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(long[] var0, int var1) {
      if (var0 != null) {
         shift((long[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(long[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(Object[] var0, int var1) {
      if (var0 != null) {
         shift((Object[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(Object[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shift(short[] var0, int var1) {
      if (var0 != null) {
         shift((short[])var0, 0, var0.length, var1);
      }
   }

   public static void shift(short[] var0, int var1, int var2, int var3) {
      if (var0 != null) {
         if (var1 < var0.length - 1 && var2 > 0) {
            if (var1 < 0) {
               var1 = 0;
            }

            if (var2 >= var0.length) {
               var2 = var0.length;
            }

            int var4 = var2 - var1;
            if (var4 > 1) {
               var3 %= var4;
               if (var3 < 0) {
                  var3 += var4;
               }

               while(var4 > 1 && var3 > 0) {
                  int var5 = var4 - var3;
                  if (var3 > var5) {
                     swap(var0, var1, var1 + var4 - var5, var5);
                     var4 = var3;
                     var3 -= var5;
                  } else {
                     if (var3 >= var5) {
                        swap(var0, var1, var1 + var5, var3);
                        break;
                     }

                     swap(var0, var1, var1 + var5, var3);
                     var1 += var3;
                     var4 = var5;
                  }
               }

            }
         }
      }
   }

   public static void shuffle(boolean[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(boolean[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((boolean[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(byte[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(byte[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((byte[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(char[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(char[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((char[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(double[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(double[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((double[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(float[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(float[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((float[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(int[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(int[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((int[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(long[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(long[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((long[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(Object[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(Object[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((Object[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static void shuffle(short[] var0) {
      shuffle(var0, new Random());
   }

   public static void shuffle(short[] var0, Random var1) {
      for(int var2 = var0.length; var2 > 1; --var2) {
         swap((short[])var0, var2 - 1, var1.nextInt(var2), 1);
      }

   }

   public static boolean[] subarray(boolean[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
         } else {
            boolean[] var4 = new boolean[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static byte[] subarray(byte[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_BYTE_ARRAY;
         } else {
            byte[] var4 = new byte[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static char[] subarray(char[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_CHAR_ARRAY;
         } else {
            char[] var4 = new char[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static double[] subarray(double[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_DOUBLE_ARRAY;
         } else {
            double[] var4 = new double[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static float[] subarray(float[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_FLOAT_ARRAY;
         } else {
            float[] var4 = new float[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static int[] subarray(int[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_INT_ARRAY;
         } else {
            int[] var4 = new int[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static long[] subarray(long[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_LONG_ARRAY;
         } else {
            long[] var4 = new long[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static short[] subarray(short[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         if (var3 <= 0) {
            return EMPTY_SHORT_ARRAY;
         } else {
            short[] var4 = new short[var3];
            System.arraycopy(var0, var1, var4, 0, var3);
            return var4;
         }
      }
   }

   public static <T> T[] subarray(T[] var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 > var0.length) {
            var2 = var0.length;
         }

         int var3 = var2 - var1;
         Class var4 = var0.getClass().getComponentType();
         Object[] var5;
         if (var3 <= 0) {
            var5 = (Object[])((Object[])Array.newInstance(var4, 0));
            return var5;
         } else {
            var5 = (Object[])((Object[])Array.newInstance(var4, var3));
            System.arraycopy(var0, var1, var5, 0, var3);
            return var5;
         }
      }
   }

   public static void swap(boolean[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((boolean[])var0, var1, var2, 1);
      }
   }

   public static void swap(boolean[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            boolean var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(byte[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((byte[])var0, var1, var2, 1);
      }
   }

   public static void swap(byte[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            byte var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(char[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((char[])var0, var1, var2, 1);
      }
   }

   public static void swap(char[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            char var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(double[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((double[])var0, var1, var2, 1);
      }
   }

   public static void swap(double[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            double var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(float[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((float[])var0, var1, var2, 1);
      }
   }

   public static void swap(float[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            float var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(int[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((int[])var0, var1, var2, 1);
      }
   }

   public static void swap(int[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            int var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(long[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((long[])var0, var1, var2, 1);
      }
   }

   public static void swap(long[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            long var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(Object[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((Object[])var0, var1, var2, 1);
      }
   }

   public static void swap(Object[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

         for(int var4 = 0; var4 < var3; ++var2) {
            Object var5 = var0[var1];
            var0[var1] = var0[var2];
            var0[var2] = var5;
            ++var4;
            ++var1;
         }

      }
   }

   public static void swap(short[] var0, int var1, int var2) {
      if (!isEmpty(var0)) {
         swap((short[])var0, var1, var2, 1);
      }
   }

   public static void swap(short[] var0, int var1, int var2, int var3) {
      if (!isEmpty(var0) && var1 < var0.length && var2 < var0.length) {
         if (var1 < 0) {
            var1 = 0;
         }

         if (var2 < 0) {
            var2 = 0;
         }

         if (var1 != var2) {
            var3 = Math.min(Math.min(var3, var0.length - var1), var0.length - var2);

            for(int var4 = 0; var4 < var3; ++var2) {
               short var5 = var0[var1];
               var0[var1] = var0[var2];
               var0[var2] = var5;
               ++var4;
               ++var1;
            }

         }
      }
   }

   public static <T> T[] toArray(T... var0) {
      return var0;
   }

   public static Map<Object, Object> toMap(Object[] var0) {
      if (var0 == null) {
         return null;
      } else {
         HashMap var1 = new HashMap((int)((double)var0.length * 1.5D));

         for(int var2 = 0; var2 < var0.length; ++var2) {
            Object var3 = var0[var2];
            if (var3 instanceof Entry) {
               Entry var4 = (Entry)var3;
               var1.put(var4.getKey(), var4.getValue());
            } else {
               if (!(var3 instanceof Object[])) {
                  throw new IllegalArgumentException("Array element " + var2 + ", '" + var3 + "', is neither of type Map.Entry nor an Array");
               }

               Object[] var5 = (Object[])((Object[])var3);
               if (var5.length < 2) {
                  throw new IllegalArgumentException("Array element " + var2 + ", '" + var3 + "', has a length less than 2");
               }

               var1.put(var5[0], var5[1]);
            }
         }

         return var1;
      }
   }

   public static Boolean[] toObject(boolean[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_OBJECT_ARRAY;
      } else {
         Boolean[] var1 = new Boolean[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2] ? Boolean.TRUE : Boolean.FALSE;
         }

         return var1;
      }
   }

   public static Byte[] toObject(byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_OBJECT_ARRAY;
      } else {
         Byte[] var1 = new Byte[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static Character[] toObject(char[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHARACTER_OBJECT_ARRAY;
      } else {
         Character[] var1 = new Character[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static Double[] toObject(double[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_OBJECT_ARRAY;
      } else {
         Double[] var1 = new Double[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static Float[] toObject(float[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_OBJECT_ARRAY;
      } else {
         Float[] var1 = new Float[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static Integer[] toObject(int[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INTEGER_OBJECT_ARRAY;
      } else {
         Integer[] var1 = new Integer[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static Long[] toObject(long[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_OBJECT_ARRAY;
      } else {
         Long[] var1 = new Long[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static Short[] toObject(short[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_OBJECT_ARRAY;
      } else {
         Short[] var1 = new Short[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static boolean[] toPrimitive(Boolean[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_ARRAY;
      } else {
         boolean[] var1 = new boolean[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static boolean[] toPrimitive(Boolean[] var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BOOLEAN_ARRAY;
      } else {
         boolean[] var2 = new boolean[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Boolean var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static byte[] toPrimitive(Byte[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var1 = new byte[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static byte[] toPrimitive(Byte[] var0, byte var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var2 = new byte[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Byte var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static char[] toPrimitive(Character[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var1 = new char[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static char[] toPrimitive(Character[] var0, char var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var2 = new char[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Character var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static double[] toPrimitive(Double[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_ARRAY;
      } else {
         double[] var1 = new double[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static double[] toPrimitive(Double[] var0, double var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_DOUBLE_ARRAY;
      } else {
         double[] var3 = new double[var0.length];

         for(int var4 = 0; var4 < var0.length; ++var4) {
            Double var5 = var0[var4];
            var3[var4] = var5 == null ? var1 : var5;
         }

         return var3;
      }
   }

   public static float[] toPrimitive(Float[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_ARRAY;
      } else {
         float[] var1 = new float[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static float[] toPrimitive(Float[] var0, float var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_FLOAT_ARRAY;
      } else {
         float[] var2 = new float[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Float var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static int[] toPrimitive(Integer[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INT_ARRAY;
      } else {
         int[] var1 = new int[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static int[] toPrimitive(Integer[] var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_INT_ARRAY;
      } else {
         int[] var2 = new int[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Integer var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static long[] toPrimitive(Long[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_ARRAY;
      } else {
         long[] var1 = new long[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static long[] toPrimitive(Long[] var0, long var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_LONG_ARRAY;
      } else {
         long[] var3 = new long[var0.length];

         for(int var4 = 0; var4 < var0.length; ++var4) {
            Long var5 = var0[var4];
            var3[var4] = var5 == null ? var1 : var5;
         }

         return var3;
      }
   }

   public static Object toPrimitive(Object var0) {
      if (var0 == null) {
         return null;
      } else {
         Class var1 = var0.getClass().getComponentType();
         Class var2 = ClassUtils.wrapperToPrimitive(var1);
         if (Boolean.TYPE.equals(var2)) {
            return toPrimitive((Boolean[])((Boolean[])var0));
         } else if (Character.TYPE.equals(var2)) {
            return toPrimitive((Character[])((Character[])var0));
         } else if (Byte.TYPE.equals(var2)) {
            return toPrimitive((Byte[])((Byte[])var0));
         } else if (Integer.TYPE.equals(var2)) {
            return toPrimitive((Integer[])((Integer[])var0));
         } else if (Long.TYPE.equals(var2)) {
            return toPrimitive((Long[])((Long[])var0));
         } else if (Short.TYPE.equals(var2)) {
            return toPrimitive((Short[])((Short[])var0));
         } else if (Double.TYPE.equals(var2)) {
            return toPrimitive((Double[])((Double[])var0));
         } else {
            return Float.TYPE.equals(var2) ? toPrimitive((Float[])((Float[])var0)) : var0;
         }
      }
   }

   public static short[] toPrimitive(Short[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_ARRAY;
      } else {
         short[] var1 = new short[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2];
         }

         return var1;
      }
   }

   public static short[] toPrimitive(Short[] var0, short var1) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_SHORT_ARRAY;
      } else {
         short[] var2 = new short[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Short var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4;
         }

         return var2;
      }
   }

   public static String toString(Object var0) {
      return toString(var0, "{}");
   }

   public static String toString(Object var0, String var1) {
      return var0 == null ? var1 : (new ToStringBuilder(var0, ToStringStyle.SIMPLE_STYLE)).append(var0).toString();
   }

   public static String[] toStringArray(Object[] var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_STRING_ARRAY;
      } else {
         String[] var1 = new String[var0.length];

         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1[var2] = var0[var2].toString();
         }

         return var1;
      }
   }

   public static String[] toStringArray(Object[] var0, String var1) {
      if (null == var0) {
         return null;
      } else if (var0.length == 0) {
         return EMPTY_STRING_ARRAY;
      } else {
         String[] var2 = new String[var0.length];

         for(int var3 = 0; var3 < var0.length; ++var3) {
            Object var4 = var0[var3];
            var2[var3] = var4 == null ? var1 : var4.toString();
         }

         return var2;
      }
   }
}
