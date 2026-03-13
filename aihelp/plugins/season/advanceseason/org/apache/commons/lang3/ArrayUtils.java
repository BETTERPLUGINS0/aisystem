/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import org.apache.commons.lang3.ArraySorter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.stream.Streams;

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

    public static boolean[] add(boolean[] blArray, boolean bl) {
        boolean[] blArray2 = (boolean[])ArrayUtils.copyArrayGrow1(blArray, Boolean.TYPE);
        blArray2[blArray2.length - 1] = bl;
        return blArray2;
    }

    @Deprecated
    public static boolean[] add(boolean[] blArray, int n, boolean bl) {
        return (boolean[])ArrayUtils.add(blArray, n, bl, Boolean.TYPE);
    }

    public static byte[] add(byte[] byArray, byte by) {
        byte[] byArray2 = (byte[])ArrayUtils.copyArrayGrow1(byArray, Byte.TYPE);
        byArray2[byArray2.length - 1] = by;
        return byArray2;
    }

    @Deprecated
    public static byte[] add(byte[] byArray, int n, byte by) {
        return (byte[])ArrayUtils.add(byArray, n, by, Byte.TYPE);
    }

    public static char[] add(char[] cArray, char c2) {
        char[] cArray2 = (char[])ArrayUtils.copyArrayGrow1(cArray, Character.TYPE);
        cArray2[cArray2.length - 1] = c2;
        return cArray2;
    }

    @Deprecated
    public static char[] add(char[] cArray, int n, char c2) {
        return (char[])ArrayUtils.add(cArray, n, Character.valueOf(c2), Character.TYPE);
    }

    public static double[] add(double[] dArray, double d) {
        double[] dArray2 = (double[])ArrayUtils.copyArrayGrow1(dArray, Double.TYPE);
        dArray2[dArray2.length - 1] = d;
        return dArray2;
    }

    @Deprecated
    public static double[] add(double[] dArray, int n, double d) {
        return (double[])ArrayUtils.add(dArray, n, d, Double.TYPE);
    }

    public static float[] add(float[] fArray, float f) {
        float[] fArray2 = (float[])ArrayUtils.copyArrayGrow1(fArray, Float.TYPE);
        fArray2[fArray2.length - 1] = f;
        return fArray2;
    }

    @Deprecated
    public static float[] add(float[] fArray, int n, float f) {
        return (float[])ArrayUtils.add(fArray, n, Float.valueOf(f), Float.TYPE);
    }

    public static int[] add(int[] nArray, int n) {
        int[] nArray2 = (int[])ArrayUtils.copyArrayGrow1(nArray, Integer.TYPE);
        nArray2[nArray2.length - 1] = n;
        return nArray2;
    }

    @Deprecated
    public static int[] add(int[] nArray, int n, int n2) {
        return (int[])ArrayUtils.add(nArray, n, n2, Integer.TYPE);
    }

    @Deprecated
    public static long[] add(long[] lArray, int n, long l) {
        return (long[])ArrayUtils.add(lArray, n, l, Long.TYPE);
    }

    public static long[] add(long[] lArray, long l) {
        long[] lArray2 = (long[])ArrayUtils.copyArrayGrow1(lArray, Long.TYPE);
        lArray2[lArray2.length - 1] = l;
        return lArray2;
    }

    private static Object add(Object object, int n, Object object2, Class<?> clazz) {
        if (object == null) {
            if (n != 0) {
                throw new IndexOutOfBoundsException("Index: " + n + ", Length: 0");
            }
            Object object3 = Array.newInstance(clazz, 1);
            Array.set(object3, 0, object2);
            return object3;
        }
        int n2 = Array.getLength(object);
        if (n > n2 || n < 0) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + n2);
        }
        Object object4 = ArrayUtils.arraycopy(object, 0, 0, n, () -> Array.newInstance(clazz, n2 + 1));
        Array.set(object4, n, object2);
        if (n < n2) {
            System.arraycopy(object, n, object4, n + 1, n2 - n);
        }
        return object4;
    }

    @Deprecated
    public static short[] add(short[] sArray, int n, short s) {
        return (short[])ArrayUtils.add(sArray, n, s, Short.TYPE);
    }

    public static short[] add(short[] sArray, short s) {
        short[] sArray2 = (short[])ArrayUtils.copyArrayGrow1(sArray, Short.TYPE);
        sArray2[sArray2.length - 1] = s;
        return sArray2;
    }

    @Deprecated
    public static <T> T[] add(T[] TArray, int n, T t) {
        Class<T> clazz;
        if (TArray != null) {
            clazz = ArrayUtils.getComponentType(TArray);
        } else if (t != null) {
            clazz = ObjectUtils.getClass(t);
        } else {
            throw new IllegalArgumentException("Array and element cannot both be null");
        }
        return (Object[])ArrayUtils.add(TArray, n, t, clazz);
    }

    public static <T> T[] add(T[] TArray, T t) {
        Class<?> clazz;
        if (TArray != null) {
            clazz = TArray.getClass().getComponentType();
        } else if (t != null) {
            clazz = t.getClass();
        } else {
            throw new IllegalArgumentException("Arguments cannot both be null");
        }
        Object[] objectArray = (Object[])ArrayUtils.copyArrayGrow1(TArray, clazz);
        objectArray[objectArray.length - 1] = t;
        return objectArray;
    }

    public static boolean[] addAll(boolean[] blArray, boolean ... blArray2) {
        if (blArray == null) {
            return ArrayUtils.clone(blArray2);
        }
        if (blArray2 == null) {
            return ArrayUtils.clone(blArray);
        }
        boolean[] blArray3 = new boolean[blArray.length + blArray2.length];
        System.arraycopy(blArray, 0, blArray3, 0, blArray.length);
        System.arraycopy(blArray2, 0, blArray3, blArray.length, blArray2.length);
        return blArray3;
    }

    public static byte[] addAll(byte[] byArray, byte ... byArray2) {
        if (byArray == null) {
            return ArrayUtils.clone(byArray2);
        }
        if (byArray2 == null) {
            return ArrayUtils.clone(byArray);
        }
        byte[] byArray3 = new byte[byArray.length + byArray2.length];
        System.arraycopy(byArray, 0, byArray3, 0, byArray.length);
        System.arraycopy(byArray2, 0, byArray3, byArray.length, byArray2.length);
        return byArray3;
    }

    public static char[] addAll(char[] cArray, char ... cArray2) {
        if (cArray == null) {
            return ArrayUtils.clone(cArray2);
        }
        if (cArray2 == null) {
            return ArrayUtils.clone(cArray);
        }
        char[] cArray3 = new char[cArray.length + cArray2.length];
        System.arraycopy(cArray, 0, cArray3, 0, cArray.length);
        System.arraycopy(cArray2, 0, cArray3, cArray.length, cArray2.length);
        return cArray3;
    }

    public static double[] addAll(double[] dArray, double ... dArray2) {
        if (dArray == null) {
            return ArrayUtils.clone(dArray2);
        }
        if (dArray2 == null) {
            return ArrayUtils.clone(dArray);
        }
        double[] dArray3 = new double[dArray.length + dArray2.length];
        System.arraycopy(dArray, 0, dArray3, 0, dArray.length);
        System.arraycopy(dArray2, 0, dArray3, dArray.length, dArray2.length);
        return dArray3;
    }

    public static float[] addAll(float[] fArray, float ... fArray2) {
        if (fArray == null) {
            return ArrayUtils.clone(fArray2);
        }
        if (fArray2 == null) {
            return ArrayUtils.clone(fArray);
        }
        float[] fArray3 = new float[fArray.length + fArray2.length];
        System.arraycopy(fArray, 0, fArray3, 0, fArray.length);
        System.arraycopy(fArray2, 0, fArray3, fArray.length, fArray2.length);
        return fArray3;
    }

    public static int[] addAll(int[] nArray, int ... nArray2) {
        if (nArray == null) {
            return ArrayUtils.clone(nArray2);
        }
        if (nArray2 == null) {
            return ArrayUtils.clone(nArray);
        }
        int[] nArray3 = new int[nArray.length + nArray2.length];
        System.arraycopy(nArray, 0, nArray3, 0, nArray.length);
        System.arraycopy(nArray2, 0, nArray3, nArray.length, nArray2.length);
        return nArray3;
    }

    public static long[] addAll(long[] lArray, long ... lArray2) {
        if (lArray == null) {
            return ArrayUtils.clone(lArray2);
        }
        if (lArray2 == null) {
            return ArrayUtils.clone(lArray);
        }
        long[] lArray3 = new long[lArray.length + lArray2.length];
        System.arraycopy(lArray, 0, lArray3, 0, lArray.length);
        System.arraycopy(lArray2, 0, lArray3, lArray.length, lArray2.length);
        return lArray3;
    }

    public static short[] addAll(short[] sArray, short ... sArray2) {
        if (sArray == null) {
            return ArrayUtils.clone(sArray2);
        }
        if (sArray2 == null) {
            return ArrayUtils.clone(sArray);
        }
        short[] sArray3 = new short[sArray.length + sArray2.length];
        System.arraycopy(sArray, 0, sArray3, 0, sArray.length);
        System.arraycopy(sArray2, 0, sArray3, sArray.length, sArray2.length);
        return sArray3;
    }

    public static <T> T[] addAll(T[] TArray, T ... TArray2) {
        if (TArray == null) {
            return ArrayUtils.clone(TArray2);
        }
        if (TArray2 == null) {
            return ArrayUtils.clone(TArray);
        }
        Class clazz = ArrayUtils.getComponentType(TArray);
        Object[] objectArray = ArrayUtils.arraycopy(TArray, 0, 0, TArray.length, () -> ArrayUtils.newInstance(clazz, TArray.length + TArray2.length));
        try {
            System.arraycopy(TArray2, 0, objectArray, TArray.length, TArray2.length);
        } catch (ArrayStoreException arrayStoreException) {
            Class<?> clazz2 = TArray2.getClass().getComponentType();
            if (!clazz.isAssignableFrom(clazz2)) {
                throw new IllegalArgumentException("Cannot store " + clazz2.getName() + " in an array of " + clazz.getName(), arrayStoreException);
            }
            throw arrayStoreException;
        }
        return objectArray;
    }

    public static boolean[] addFirst(boolean[] blArray, boolean bl) {
        return blArray == null ? ArrayUtils.add(blArray, bl) : ArrayUtils.insert(0, blArray, bl);
    }

    public static byte[] addFirst(byte[] byArray, byte by) {
        return byArray == null ? ArrayUtils.add(byArray, by) : ArrayUtils.insert(0, byArray, by);
    }

    public static char[] addFirst(char[] cArray, char c2) {
        return cArray == null ? ArrayUtils.add(cArray, c2) : ArrayUtils.insert(0, cArray, c2);
    }

    public static double[] addFirst(double[] dArray, double d) {
        return dArray == null ? ArrayUtils.add(dArray, d) : ArrayUtils.insert(0, dArray, d);
    }

    public static float[] addFirst(float[] fArray, float f) {
        return fArray == null ? ArrayUtils.add(fArray, f) : ArrayUtils.insert(0, fArray, f);
    }

    public static int[] addFirst(int[] nArray, int n) {
        return nArray == null ? ArrayUtils.add(nArray, n) : ArrayUtils.insert(0, nArray, n);
    }

    public static long[] addFirst(long[] lArray, long l) {
        return lArray == null ? ArrayUtils.add(lArray, l) : ArrayUtils.insert(0, lArray, l);
    }

    public static short[] addFirst(short[] sArray, short s) {
        return sArray == null ? ArrayUtils.add(sArray, s) : ArrayUtils.insert(0, sArray, s);
    }

    public static <T> T[] addFirst(T[] TArray, T t) {
        return TArray == null ? ArrayUtils.add(TArray, t) : ArrayUtils.insert(0, TArray, t);
    }

    public static <T> T arraycopy(T t, int n, int n2, int n3, Function<Integer, T> function) {
        return ArrayUtils.arraycopy(t, n, function.apply(n3), n2, n3);
    }

    public static <T> T arraycopy(T t, int n, int n2, int n3, Supplier<T> supplier) {
        return ArrayUtils.arraycopy(t, n, supplier.get(), n2, n3);
    }

    public static <T> T arraycopy(T t, int n, T t2, int n2, int n3) {
        System.arraycopy(t, n, t2, n2, n3);
        return t2;
    }

    public static boolean[] clone(boolean[] blArray) {
        return blArray != null ? (boolean[])blArray.clone() : null;
    }

    public static byte[] clone(byte[] byArray) {
        return byArray != null ? (byte[])byArray.clone() : null;
    }

    public static char[] clone(char[] cArray) {
        return cArray != null ? (char[])cArray.clone() : null;
    }

    public static double[] clone(double[] dArray) {
        return dArray != null ? (double[])dArray.clone() : null;
    }

    public static float[] clone(float[] fArray) {
        return fArray != null ? (float[])fArray.clone() : null;
    }

    public static int[] clone(int[] nArray) {
        return nArray != null ? (int[])nArray.clone() : null;
    }

    public static long[] clone(long[] lArray) {
        return lArray != null ? (long[])lArray.clone() : null;
    }

    public static short[] clone(short[] sArray) {
        return sArray != null ? (short[])sArray.clone() : null;
    }

    public static <T> T[] clone(T[] TArray) {
        return TArray != null ? (Object[])TArray.clone() : null;
    }

    public static boolean contains(boolean[] blArray, boolean bl) {
        return ArrayUtils.indexOf(blArray, bl) != -1;
    }

    public static boolean contains(byte[] byArray, byte by) {
        return ArrayUtils.indexOf(byArray, by) != -1;
    }

    public static boolean contains(char[] cArray, char c2) {
        return ArrayUtils.indexOf(cArray, c2) != -1;
    }

    public static boolean contains(double[] dArray, double d) {
        return ArrayUtils.indexOf(dArray, d) != -1;
    }

    public static boolean contains(double[] dArray, double d, double d2) {
        return ArrayUtils.indexOf(dArray, d, 0, d2) != -1;
    }

    public static boolean contains(float[] fArray, float f) {
        return ArrayUtils.indexOf(fArray, f) != -1;
    }

    public static boolean contains(int[] nArray, int n) {
        return ArrayUtils.indexOf(nArray, n) != -1;
    }

    public static boolean contains(long[] lArray, long l) {
        return ArrayUtils.indexOf(lArray, l) != -1;
    }

    public static boolean contains(Object[] objectArray, Object object) {
        return ArrayUtils.indexOf(objectArray, object) != -1;
    }

    public static boolean contains(short[] sArray, short s) {
        return ArrayUtils.indexOf(sArray, s) != -1;
    }

    public static boolean containsAny(Object[] objectArray, Object ... objectArray2) {
        return Streams.of(objectArray2).anyMatch(object -> ArrayUtils.contains(objectArray, object));
    }

    private static Object copyArrayGrow1(Object object, Class<?> clazz) {
        if (object != null) {
            int n = Array.getLength(object);
            Object object2 = Array.newInstance(object.getClass().getComponentType(), n + 1);
            System.arraycopy(object, 0, object2, 0, n);
            return object2;
        }
        return Array.newInstance(clazz, 1);
    }

    public static <T> T get(T[] TArray, int n) {
        return ArrayUtils.get(TArray, n, null);
    }

    public static <T> T get(T[] TArray, int n, T t) {
        return ArrayUtils.isArrayIndexValid(TArray, n) ? TArray[n] : t;
    }

    public static <T> Class<T> getComponentType(T[] TArray) {
        return ClassUtils.getComponentType(ObjectUtils.getClass(TArray));
    }

    public static int getLength(Object object) {
        return object != null ? Array.getLength(object) : 0;
    }

    public static int hashCode(Object object) {
        return new HashCodeBuilder().append(object).toHashCode();
    }

    public static BitSet indexesOf(boolean[] blArray, boolean bl) {
        return ArrayUtils.indexesOf(blArray, bl, 0);
    }

    public static BitSet indexesOf(boolean[] blArray, boolean bl, int n) {
        BitSet bitSet = new BitSet();
        if (blArray == null) {
            return bitSet;
        }
        while (n < blArray.length && (n = ArrayUtils.indexOf(blArray, bl, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(byte[] byArray, byte by) {
        return ArrayUtils.indexesOf(byArray, by, 0);
    }

    public static BitSet indexesOf(byte[] byArray, byte by, int n) {
        BitSet bitSet = new BitSet();
        if (byArray == null) {
            return bitSet;
        }
        while (n < byArray.length && (n = ArrayUtils.indexOf(byArray, by, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(char[] cArray, char c2) {
        return ArrayUtils.indexesOf(cArray, c2, 0);
    }

    public static BitSet indexesOf(char[] cArray, char c2, int n) {
        BitSet bitSet = new BitSet();
        if (cArray == null) {
            return bitSet;
        }
        while (n < cArray.length && (n = ArrayUtils.indexOf(cArray, c2, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(double[] dArray, double d) {
        return ArrayUtils.indexesOf(dArray, d, 0);
    }

    public static BitSet indexesOf(double[] dArray, double d, double d2) {
        return ArrayUtils.indexesOf(dArray, d, 0, d2);
    }

    public static BitSet indexesOf(double[] dArray, double d, int n) {
        BitSet bitSet = new BitSet();
        if (dArray == null) {
            return bitSet;
        }
        while (n < dArray.length && (n = ArrayUtils.indexOf(dArray, d, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(double[] dArray, double d, int n, double d2) {
        BitSet bitSet = new BitSet();
        if (dArray == null) {
            return bitSet;
        }
        while (n < dArray.length && (n = ArrayUtils.indexOf(dArray, d, n, d2)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(float[] fArray, float f) {
        return ArrayUtils.indexesOf(fArray, f, 0);
    }

    public static BitSet indexesOf(float[] fArray, float f, int n) {
        BitSet bitSet = new BitSet();
        if (fArray == null) {
            return bitSet;
        }
        while (n < fArray.length && (n = ArrayUtils.indexOf(fArray, f, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(int[] nArray, int n) {
        return ArrayUtils.indexesOf(nArray, n, 0);
    }

    public static BitSet indexesOf(int[] nArray, int n, int n2) {
        BitSet bitSet = new BitSet();
        if (nArray == null) {
            return bitSet;
        }
        while (n2 < nArray.length && (n2 = ArrayUtils.indexOf(nArray, n, n2)) != -1) {
            bitSet.set(n2);
            ++n2;
        }
        return bitSet;
    }

    public static BitSet indexesOf(long[] lArray, long l) {
        return ArrayUtils.indexesOf(lArray, l, 0);
    }

    public static BitSet indexesOf(long[] lArray, long l, int n) {
        BitSet bitSet = new BitSet();
        if (lArray == null) {
            return bitSet;
        }
        while (n < lArray.length && (n = ArrayUtils.indexOf(lArray, l, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(Object[] objectArray, Object object) {
        return ArrayUtils.indexesOf(objectArray, object, 0);
    }

    public static BitSet indexesOf(Object[] objectArray, Object object, int n) {
        BitSet bitSet = new BitSet();
        if (objectArray == null) {
            return bitSet;
        }
        while (n < objectArray.length && (n = ArrayUtils.indexOf(objectArray, object, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static BitSet indexesOf(short[] sArray, short s) {
        return ArrayUtils.indexesOf(sArray, s, 0);
    }

    public static BitSet indexesOf(short[] sArray, short s, int n) {
        BitSet bitSet = new BitSet();
        if (sArray == null) {
            return bitSet;
        }
        while (n < sArray.length && (n = ArrayUtils.indexOf(sArray, s, n)) != -1) {
            bitSet.set(n);
            ++n;
        }
        return bitSet;
    }

    public static int indexOf(boolean[] blArray, boolean bl) {
        return ArrayUtils.indexOf(blArray, bl, 0);
    }

    public static int indexOf(boolean[] blArray, boolean bl, int n) {
        if (ArrayUtils.isEmpty(blArray)) {
            return -1;
        }
        for (int i = ArrayUtils.max0(n); i < blArray.length; ++i) {
            if (bl != blArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(byte[] byArray, byte by) {
        return ArrayUtils.indexOf(byArray, by, 0);
    }

    public static int indexOf(byte[] byArray, byte by, int n) {
        if (byArray == null) {
            return -1;
        }
        for (int i = ArrayUtils.max0(n); i < byArray.length; ++i) {
            if (by != byArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(char[] cArray, char c2) {
        return ArrayUtils.indexOf(cArray, c2, 0);
    }

    public static int indexOf(char[] cArray, char c2, int n) {
        if (cArray == null) {
            return -1;
        }
        for (int i = ArrayUtils.max0(n); i < cArray.length; ++i) {
            if (c2 != cArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(double[] dArray, double d) {
        return ArrayUtils.indexOf(dArray, d, 0);
    }

    public static int indexOf(double[] dArray, double d, double d2) {
        return ArrayUtils.indexOf(dArray, d, 0, d2);
    }

    public static int indexOf(double[] dArray, double d, int n) {
        if (ArrayUtils.isEmpty(dArray)) {
            return -1;
        }
        boolean bl = Double.isNaN(d);
        for (int i = ArrayUtils.max0(n); i < dArray.length; ++i) {
            double d2 = dArray[i];
            if (d != d2 && (!bl || !Double.isNaN(d2))) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(double[] dArray, double d, int n, double d2) {
        if (ArrayUtils.isEmpty(dArray)) {
            return -1;
        }
        double d3 = d - d2;
        double d4 = d + d2;
        for (int i = ArrayUtils.max0(n); i < dArray.length; ++i) {
            if (!(dArray[i] >= d3) || !(dArray[i] <= d4)) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(float[] fArray, float f) {
        return ArrayUtils.indexOf(fArray, f, 0);
    }

    public static int indexOf(float[] fArray, float f, int n) {
        if (ArrayUtils.isEmpty(fArray)) {
            return -1;
        }
        boolean bl = Float.isNaN(f);
        for (int i = ArrayUtils.max0(n); i < fArray.length; ++i) {
            float f2 = fArray[i];
            if (f != f2 && (!bl || !Float.isNaN(f2))) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(int[] nArray, int n) {
        return ArrayUtils.indexOf(nArray, n, 0);
    }

    public static int indexOf(int[] nArray, int n, int n2) {
        if (nArray == null) {
            return -1;
        }
        for (int i = ArrayUtils.max0(n2); i < nArray.length; ++i) {
            if (n != nArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(long[] lArray, long l) {
        return ArrayUtils.indexOf(lArray, l, 0);
    }

    public static int indexOf(long[] lArray, long l, int n) {
        if (lArray == null) {
            return -1;
        }
        for (int i = ArrayUtils.max0(n); i < lArray.length; ++i) {
            if (l != lArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int indexOf(Object[] objectArray, Object object) {
        return ArrayUtils.indexOf(objectArray, object, 0);
    }

    public static int indexOf(Object[] objectArray, Object object, int n) {
        if (objectArray == null) {
            return -1;
        }
        n = ArrayUtils.max0(n);
        if (object == null) {
            for (int i = n; i < objectArray.length; ++i) {
                if (objectArray[i] != null) continue;
                return i;
            }
        } else {
            for (int i = n; i < objectArray.length; ++i) {
                if (!object.equals(objectArray[i])) continue;
                return i;
            }
        }
        return -1;
    }

    public static int indexOf(short[] sArray, short s) {
        return ArrayUtils.indexOf(sArray, s, 0);
    }

    public static int indexOf(short[] sArray, short s, int n) {
        if (sArray == null) {
            return -1;
        }
        for (int i = ArrayUtils.max0(n); i < sArray.length; ++i) {
            if (s != sArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static boolean[] insert(int n, boolean[] blArray, boolean ... blArray2) {
        if (blArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(blArray2)) {
            return ArrayUtils.clone(blArray);
        }
        if (n < 0 || n > blArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + blArray.length);
        }
        boolean[] blArray3 = new boolean[blArray.length + blArray2.length];
        System.arraycopy(blArray2, 0, blArray3, n, blArray2.length);
        if (n > 0) {
            System.arraycopy(blArray, 0, blArray3, 0, n);
        }
        if (n < blArray.length) {
            System.arraycopy(blArray, n, blArray3, n + blArray2.length, blArray.length - n);
        }
        return blArray3;
    }

    public static byte[] insert(int n, byte[] byArray, byte ... byArray2) {
        if (byArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(byArray2)) {
            return ArrayUtils.clone(byArray);
        }
        if (n < 0 || n > byArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + byArray.length);
        }
        byte[] byArray3 = new byte[byArray.length + byArray2.length];
        System.arraycopy(byArray2, 0, byArray3, n, byArray2.length);
        if (n > 0) {
            System.arraycopy(byArray, 0, byArray3, 0, n);
        }
        if (n < byArray.length) {
            System.arraycopy(byArray, n, byArray3, n + byArray2.length, byArray.length - n);
        }
        return byArray3;
    }

    public static char[] insert(int n, char[] cArray, char ... cArray2) {
        if (cArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(cArray2)) {
            return ArrayUtils.clone(cArray);
        }
        if (n < 0 || n > cArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + cArray.length);
        }
        char[] cArray3 = new char[cArray.length + cArray2.length];
        System.arraycopy(cArray2, 0, cArray3, n, cArray2.length);
        if (n > 0) {
            System.arraycopy(cArray, 0, cArray3, 0, n);
        }
        if (n < cArray.length) {
            System.arraycopy(cArray, n, cArray3, n + cArray2.length, cArray.length - n);
        }
        return cArray3;
    }

    public static double[] insert(int n, double[] dArray, double ... dArray2) {
        if (dArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(dArray2)) {
            return ArrayUtils.clone(dArray);
        }
        if (n < 0 || n > dArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + dArray.length);
        }
        double[] dArray3 = new double[dArray.length + dArray2.length];
        System.arraycopy(dArray2, 0, dArray3, n, dArray2.length);
        if (n > 0) {
            System.arraycopy(dArray, 0, dArray3, 0, n);
        }
        if (n < dArray.length) {
            System.arraycopy(dArray, n, dArray3, n + dArray2.length, dArray.length - n);
        }
        return dArray3;
    }

    public static float[] insert(int n, float[] fArray, float ... fArray2) {
        if (fArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(fArray2)) {
            return ArrayUtils.clone(fArray);
        }
        if (n < 0 || n > fArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + fArray.length);
        }
        float[] fArray3 = new float[fArray.length + fArray2.length];
        System.arraycopy(fArray2, 0, fArray3, n, fArray2.length);
        if (n > 0) {
            System.arraycopy(fArray, 0, fArray3, 0, n);
        }
        if (n < fArray.length) {
            System.arraycopy(fArray, n, fArray3, n + fArray2.length, fArray.length - n);
        }
        return fArray3;
    }

    public static int[] insert(int n, int[] nArray, int ... nArray2) {
        if (nArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(nArray2)) {
            return ArrayUtils.clone(nArray);
        }
        if (n < 0 || n > nArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + nArray.length);
        }
        int[] nArray3 = new int[nArray.length + nArray2.length];
        System.arraycopy(nArray2, 0, nArray3, n, nArray2.length);
        if (n > 0) {
            System.arraycopy(nArray, 0, nArray3, 0, n);
        }
        if (n < nArray.length) {
            System.arraycopy(nArray, n, nArray3, n + nArray2.length, nArray.length - n);
        }
        return nArray3;
    }

    public static long[] insert(int n, long[] lArray, long ... lArray2) {
        if (lArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(lArray2)) {
            return ArrayUtils.clone(lArray);
        }
        if (n < 0 || n > lArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + lArray.length);
        }
        long[] lArray3 = new long[lArray.length + lArray2.length];
        System.arraycopy(lArray2, 0, lArray3, n, lArray2.length);
        if (n > 0) {
            System.arraycopy(lArray, 0, lArray3, 0, n);
        }
        if (n < lArray.length) {
            System.arraycopy(lArray, n, lArray3, n + lArray2.length, lArray.length - n);
        }
        return lArray3;
    }

    public static short[] insert(int n, short[] sArray, short ... sArray2) {
        if (sArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(sArray2)) {
            return ArrayUtils.clone(sArray);
        }
        if (n < 0 || n > sArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + sArray.length);
        }
        short[] sArray3 = new short[sArray.length + sArray2.length];
        System.arraycopy(sArray2, 0, sArray3, n, sArray2.length);
        if (n > 0) {
            System.arraycopy(sArray, 0, sArray3, 0, n);
        }
        if (n < sArray.length) {
            System.arraycopy(sArray, n, sArray3, n + sArray2.length, sArray.length - n);
        }
        return sArray3;
    }

    @SafeVarargs
    public static <T> T[] insert(int n, T[] TArray, T ... TArray2) {
        if (TArray == null) {
            return null;
        }
        if (ArrayUtils.isEmpty(TArray2)) {
            return ArrayUtils.clone(TArray);
        }
        if (n < 0 || n > TArray.length) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + TArray.length);
        }
        Class<T> clazz = ArrayUtils.getComponentType(TArray);
        int n2 = TArray.length + TArray2.length;
        T[] TArray3 = ArrayUtils.newInstance(clazz, n2);
        System.arraycopy(TArray2, 0, TArray3, n, TArray2.length);
        if (n > 0) {
            System.arraycopy(TArray, 0, TArray3, 0, n);
        }
        if (n < TArray.length) {
            System.arraycopy(TArray, n, TArray3, n + TArray2.length, TArray.length - n);
        }
        return TArray3;
    }

    private static boolean isArrayEmpty(Object object) {
        return ArrayUtils.getLength(object) == 0;
    }

    public static <T> boolean isArrayIndexValid(T[] TArray, int n) {
        return n >= 0 && ArrayUtils.getLength(TArray) > n;
    }

    public static boolean isEmpty(boolean[] blArray) {
        return ArrayUtils.isArrayEmpty(blArray);
    }

    public static boolean isEmpty(byte[] byArray) {
        return ArrayUtils.isArrayEmpty(byArray);
    }

    public static boolean isEmpty(char[] cArray) {
        return ArrayUtils.isArrayEmpty(cArray);
    }

    public static boolean isEmpty(double[] dArray) {
        return ArrayUtils.isArrayEmpty(dArray);
    }

    public static boolean isEmpty(float[] fArray) {
        return ArrayUtils.isArrayEmpty(fArray);
    }

    public static boolean isEmpty(int[] nArray) {
        return ArrayUtils.isArrayEmpty(nArray);
    }

    public static boolean isEmpty(long[] lArray) {
        return ArrayUtils.isArrayEmpty(lArray);
    }

    public static boolean isEmpty(Object[] objectArray) {
        return ArrayUtils.isArrayEmpty(objectArray);
    }

    public static boolean isEmpty(short[] sArray) {
        return ArrayUtils.isArrayEmpty(sArray);
    }

    @Deprecated
    public static boolean isEquals(Object object, Object object2) {
        return new EqualsBuilder().append(object, object2).isEquals();
    }

    public static boolean isNotEmpty(boolean[] blArray) {
        return !ArrayUtils.isEmpty(blArray);
    }

    public static boolean isNotEmpty(byte[] byArray) {
        return !ArrayUtils.isEmpty(byArray);
    }

    public static boolean isNotEmpty(char[] cArray) {
        return !ArrayUtils.isEmpty(cArray);
    }

    public static boolean isNotEmpty(double[] dArray) {
        return !ArrayUtils.isEmpty(dArray);
    }

    public static boolean isNotEmpty(float[] fArray) {
        return !ArrayUtils.isEmpty(fArray);
    }

    public static boolean isNotEmpty(int[] nArray) {
        return !ArrayUtils.isEmpty(nArray);
    }

    public static boolean isNotEmpty(long[] lArray) {
        return !ArrayUtils.isEmpty(lArray);
    }

    public static boolean isNotEmpty(short[] sArray) {
        return !ArrayUtils.isEmpty(sArray);
    }

    public static <T> boolean isNotEmpty(T[] TArray) {
        return !ArrayUtils.isEmpty(TArray);
    }

    public static boolean isSameLength(boolean[] blArray, boolean[] blArray2) {
        return ArrayUtils.getLength(blArray) == ArrayUtils.getLength(blArray2);
    }

    public static boolean isSameLength(byte[] byArray, byte[] byArray2) {
        return ArrayUtils.getLength(byArray) == ArrayUtils.getLength(byArray2);
    }

    public static boolean isSameLength(char[] cArray, char[] cArray2) {
        return ArrayUtils.getLength(cArray) == ArrayUtils.getLength(cArray2);
    }

    public static boolean isSameLength(double[] dArray, double[] dArray2) {
        return ArrayUtils.getLength(dArray) == ArrayUtils.getLength(dArray2);
    }

    public static boolean isSameLength(float[] fArray, float[] fArray2) {
        return ArrayUtils.getLength(fArray) == ArrayUtils.getLength(fArray2);
    }

    public static boolean isSameLength(int[] nArray, int[] nArray2) {
        return ArrayUtils.getLength(nArray) == ArrayUtils.getLength(nArray2);
    }

    public static boolean isSameLength(long[] lArray, long[] lArray2) {
        return ArrayUtils.getLength(lArray) == ArrayUtils.getLength(lArray2);
    }

    public static boolean isSameLength(Object object, Object object2) {
        return ArrayUtils.getLength(object) == ArrayUtils.getLength(object2);
    }

    public static boolean isSameLength(Object[] objectArray, Object[] objectArray2) {
        return ArrayUtils.getLength(objectArray) == ArrayUtils.getLength(objectArray2);
    }

    public static boolean isSameLength(short[] sArray, short[] sArray2) {
        return ArrayUtils.getLength(sArray) == ArrayUtils.getLength(sArray2);
    }

    public static boolean isSameType(Object object, Object object2) {
        if (object == null || object2 == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        return object.getClass().getName().equals(object2.getClass().getName());
    }

    public static boolean isSorted(boolean[] blArray) {
        if (ArrayUtils.getLength(blArray) < 2) {
            return true;
        }
        boolean bl = blArray[0];
        int n = blArray.length;
        for (int i = 1; i < n; ++i) {
            boolean bl2 = blArray[i];
            if (BooleanUtils.compare(bl, bl2) > 0) {
                return false;
            }
            bl = bl2;
        }
        return true;
    }

    public static boolean isSorted(byte[] byArray) {
        if (ArrayUtils.getLength(byArray) < 2) {
            return true;
        }
        byte by = byArray[0];
        int n = byArray.length;
        for (int i = 1; i < n; ++i) {
            byte by2 = byArray[i];
            if (NumberUtils.compare(by, by2) > 0) {
                return false;
            }
            by = by2;
        }
        return true;
    }

    public static boolean isSorted(char[] cArray) {
        if (ArrayUtils.getLength(cArray) < 2) {
            return true;
        }
        char c2 = cArray[0];
        int n = cArray.length;
        for (int i = 1; i < n; ++i) {
            char c3 = cArray[i];
            if (CharUtils.compare(c2, c3) > 0) {
                return false;
            }
            c2 = c3;
        }
        return true;
    }

    public static boolean isSorted(double[] dArray) {
        if (ArrayUtils.getLength(dArray) < 2) {
            return true;
        }
        double d = dArray[0];
        int n = dArray.length;
        for (int i = 1; i < n; ++i) {
            double d2 = dArray[i];
            if (Double.compare(d, d2) > 0) {
                return false;
            }
            d = d2;
        }
        return true;
    }

    public static boolean isSorted(float[] fArray) {
        if (ArrayUtils.getLength(fArray) < 2) {
            return true;
        }
        float f = fArray[0];
        int n = fArray.length;
        for (int i = 1; i < n; ++i) {
            float f2 = fArray[i];
            if (Float.compare(f, f2) > 0) {
                return false;
            }
            f = f2;
        }
        return true;
    }

    public static boolean isSorted(int[] nArray) {
        if (ArrayUtils.getLength(nArray) < 2) {
            return true;
        }
        int n = nArray[0];
        int n2 = nArray.length;
        for (int i = 1; i < n2; ++i) {
            int n3 = nArray[i];
            if (NumberUtils.compare(n, n3) > 0) {
                return false;
            }
            n = n3;
        }
        return true;
    }

    public static boolean isSorted(long[] lArray) {
        if (ArrayUtils.getLength(lArray) < 2) {
            return true;
        }
        long l = lArray[0];
        int n = lArray.length;
        for (int i = 1; i < n; ++i) {
            long l2 = lArray[i];
            if (NumberUtils.compare(l, l2) > 0) {
                return false;
            }
            l = l2;
        }
        return true;
    }

    public static boolean isSorted(short[] sArray) {
        if (ArrayUtils.getLength(sArray) < 2) {
            return true;
        }
        short s = sArray[0];
        int n = sArray.length;
        for (int i = 1; i < n; ++i) {
            short s2 = sArray[i];
            if (NumberUtils.compare(s, s2) > 0) {
                return false;
            }
            s = s2;
        }
        return true;
    }

    public static <T extends Comparable<? super T>> boolean isSorted(T[] TArray) {
        return ArrayUtils.isSorted(TArray, Comparable::compareTo);
    }

    public static <T> boolean isSorted(T[] TArray, Comparator<T> comparator) {
        Objects.requireNonNull(comparator, "comparator");
        if (ArrayUtils.getLength(TArray) < 2) {
            return true;
        }
        T t = TArray[0];
        int n = TArray.length;
        for (int i = 1; i < n; ++i) {
            T t2 = TArray[i];
            if (comparator.compare(t, t2) > 0) {
                return false;
            }
            t = t2;
        }
        return true;
    }

    public static int lastIndexOf(boolean[] blArray, boolean bl) {
        return ArrayUtils.lastIndexOf(blArray, bl, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(boolean[] blArray, boolean bl, int n) {
        if (ArrayUtils.isEmpty(blArray) || n < 0) {
            return -1;
        }
        if (n >= blArray.length) {
            n = blArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (bl != blArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(byte[] byArray, byte by) {
        return ArrayUtils.lastIndexOf(byArray, by, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(byte[] byArray, byte by, int n) {
        if (byArray == null || n < 0) {
            return -1;
        }
        if (n >= byArray.length) {
            n = byArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (by != byArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(char[] cArray, char c2) {
        return ArrayUtils.lastIndexOf(cArray, c2, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(char[] cArray, char c2, int n) {
        if (cArray == null || n < 0) {
            return -1;
        }
        if (n >= cArray.length) {
            n = cArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (c2 != cArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(double[] dArray, double d) {
        return ArrayUtils.lastIndexOf(dArray, d, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(double[] dArray, double d, double d2) {
        return ArrayUtils.lastIndexOf(dArray, d, Integer.MAX_VALUE, d2);
    }

    public static int lastIndexOf(double[] dArray, double d, int n) {
        if (ArrayUtils.isEmpty(dArray) || n < 0) {
            return -1;
        }
        if (n >= dArray.length) {
            n = dArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (d != dArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(double[] dArray, double d, int n, double d2) {
        if (ArrayUtils.isEmpty(dArray) || n < 0) {
            return -1;
        }
        if (n >= dArray.length) {
            n = dArray.length - 1;
        }
        double d3 = d - d2;
        double d4 = d + d2;
        for (int i = n; i >= 0; --i) {
            if (!(dArray[i] >= d3) || !(dArray[i] <= d4)) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(float[] fArray, float f) {
        return ArrayUtils.lastIndexOf(fArray, f, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(float[] fArray, float f, int n) {
        if (ArrayUtils.isEmpty(fArray) || n < 0) {
            return -1;
        }
        if (n >= fArray.length) {
            n = fArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (f != fArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(int[] nArray, int n) {
        return ArrayUtils.lastIndexOf(nArray, n, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(int[] nArray, int n, int n2) {
        if (nArray == null || n2 < 0) {
            return -1;
        }
        if (n2 >= nArray.length) {
            n2 = nArray.length - 1;
        }
        for (int i = n2; i >= 0; --i) {
            if (n != nArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(long[] lArray, long l) {
        return ArrayUtils.lastIndexOf(lArray, l, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(long[] lArray, long l, int n) {
        if (lArray == null || n < 0) {
            return -1;
        }
        if (n >= lArray.length) {
            n = lArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (l != lArray[i]) continue;
            return i;
        }
        return -1;
    }

    public static int lastIndexOf(Object[] objectArray, Object object) {
        return ArrayUtils.lastIndexOf(objectArray, object, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(Object[] objectArray, Object object, int n) {
        block5: {
            block4: {
                if (objectArray == null || n < 0) {
                    return -1;
                }
                if (n >= objectArray.length) {
                    n = objectArray.length - 1;
                }
                if (object != null) break block4;
                for (int i = n; i >= 0; --i) {
                    if (objectArray[i] != null) continue;
                    return i;
                }
                break block5;
            }
            if (!objectArray.getClass().getComponentType().isInstance(object)) break block5;
            for (int i = n; i >= 0; --i) {
                if (!object.equals(objectArray[i])) continue;
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(short[] sArray, short s) {
        return ArrayUtils.lastIndexOf(sArray, s, Integer.MAX_VALUE);
    }

    public static int lastIndexOf(short[] sArray, short s, int n) {
        if (sArray == null || n < 0) {
            return -1;
        }
        if (n >= sArray.length) {
            n = sArray.length - 1;
        }
        for (int i = n; i >= 0; --i) {
            if (s != sArray[i]) continue;
            return i;
        }
        return -1;
    }

    private static int max0(int n) {
        return Math.max(0, n);
    }

    public static <T> T[] newInstance(Class<T> clazz, int n) {
        return (Object[])Array.newInstance(clazz, n);
    }

    public static <T> T[] nullTo(T[] TArray, T[] TArray2) {
        return ArrayUtils.isEmpty(TArray) ? TArray2 : TArray;
    }

    public static boolean[] nullToEmpty(boolean[] blArray) {
        return ArrayUtils.isEmpty(blArray) ? EMPTY_BOOLEAN_ARRAY : blArray;
    }

    public static Boolean[] nullToEmpty(Boolean[] booleanArray) {
        return ArrayUtils.nullTo(booleanArray, EMPTY_BOOLEAN_OBJECT_ARRAY);
    }

    public static byte[] nullToEmpty(byte[] byArray) {
        return ArrayUtils.isEmpty(byArray) ? EMPTY_BYTE_ARRAY : byArray;
    }

    public static Byte[] nullToEmpty(Byte[] byteArray) {
        return ArrayUtils.nullTo(byteArray, EMPTY_BYTE_OBJECT_ARRAY);
    }

    public static char[] nullToEmpty(char[] cArray) {
        return ArrayUtils.isEmpty(cArray) ? EMPTY_CHAR_ARRAY : cArray;
    }

    public static Character[] nullToEmpty(Character[] characterArray) {
        return ArrayUtils.nullTo(characterArray, EMPTY_CHARACTER_OBJECT_ARRAY);
    }

    public static Class<?>[] nullToEmpty(Class<?>[] classArray) {
        return ArrayUtils.nullTo(classArray, EMPTY_CLASS_ARRAY);
    }

    public static double[] nullToEmpty(double[] dArray) {
        return ArrayUtils.isEmpty(dArray) ? EMPTY_DOUBLE_ARRAY : dArray;
    }

    public static Double[] nullToEmpty(Double[] doubleArray) {
        return ArrayUtils.nullTo(doubleArray, EMPTY_DOUBLE_OBJECT_ARRAY);
    }

    public static float[] nullToEmpty(float[] fArray) {
        return ArrayUtils.isEmpty(fArray) ? EMPTY_FLOAT_ARRAY : fArray;
    }

    public static Float[] nullToEmpty(Float[] floatArray) {
        return ArrayUtils.nullTo(floatArray, EMPTY_FLOAT_OBJECT_ARRAY);
    }

    public static int[] nullToEmpty(int[] nArray) {
        return ArrayUtils.isEmpty(nArray) ? EMPTY_INT_ARRAY : nArray;
    }

    public static Integer[] nullToEmpty(Integer[] integerArray) {
        return ArrayUtils.nullTo(integerArray, EMPTY_INTEGER_OBJECT_ARRAY);
    }

    public static long[] nullToEmpty(long[] lArray) {
        return ArrayUtils.isEmpty(lArray) ? EMPTY_LONG_ARRAY : lArray;
    }

    public static Long[] nullToEmpty(Long[] longArray) {
        return ArrayUtils.nullTo(longArray, EMPTY_LONG_OBJECT_ARRAY);
    }

    public static Object[] nullToEmpty(Object[] objectArray) {
        return ArrayUtils.nullTo(objectArray, EMPTY_OBJECT_ARRAY);
    }

    public static short[] nullToEmpty(short[] sArray) {
        return ArrayUtils.isEmpty(sArray) ? EMPTY_SHORT_ARRAY : sArray;
    }

    public static Short[] nullToEmpty(Short[] shortArray) {
        return ArrayUtils.nullTo(shortArray, EMPTY_SHORT_OBJECT_ARRAY);
    }

    public static String[] nullToEmpty(String[] stringArray) {
        return ArrayUtils.nullTo(stringArray, EMPTY_STRING_ARRAY);
    }

    public static <T> T[] nullToEmpty(T[] TArray, Class<T[]> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("The type must not be null");
        }
        if (TArray == null) {
            return clazz.cast(Array.newInstance(clazz.getComponentType(), 0));
        }
        return TArray;
    }

    private static ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

    public static boolean[] remove(boolean[] blArray, int n) {
        return (boolean[])ArrayUtils.remove((Object)blArray, n);
    }

    public static byte[] remove(byte[] byArray, int n) {
        return (byte[])ArrayUtils.remove((Object)byArray, n);
    }

    public static char[] remove(char[] cArray, int n) {
        return (char[])ArrayUtils.remove((Object)cArray, n);
    }

    public static double[] remove(double[] dArray, int n) {
        return (double[])ArrayUtils.remove((Object)dArray, n);
    }

    public static float[] remove(float[] fArray, int n) {
        return (float[])ArrayUtils.remove((Object)fArray, n);
    }

    public static int[] remove(int[] nArray, int n) {
        return (int[])ArrayUtils.remove((Object)nArray, n);
    }

    public static long[] remove(long[] lArray, int n) {
        return (long[])ArrayUtils.remove((Object)lArray, n);
    }

    private static Object remove(Object object, int n) {
        int n2 = ArrayUtils.getLength(object);
        if (n < 0 || n >= n2) {
            throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + n2);
        }
        Object object2 = Array.newInstance(object.getClass().getComponentType(), n2 - 1);
        System.arraycopy(object, 0, object2, 0, n);
        if (n < n2 - 1) {
            System.arraycopy(object, n + 1, object2, n, n2 - n - 1);
        }
        return object2;
    }

    public static short[] remove(short[] sArray, int n) {
        return (short[])ArrayUtils.remove((Object)sArray, n);
    }

    public static <T> T[] remove(T[] TArray, int n) {
        return (Object[])ArrayUtils.remove(TArray, n);
    }

    public static boolean[] removeAll(boolean[] blArray, int ... nArray) {
        return (boolean[])ArrayUtils.removeAll((Object)blArray, nArray);
    }

    public static byte[] removeAll(byte[] byArray, int ... nArray) {
        return (byte[])ArrayUtils.removeAll((Object)byArray, nArray);
    }

    public static char[] removeAll(char[] cArray, int ... nArray) {
        return (char[])ArrayUtils.removeAll((Object)cArray, nArray);
    }

    public static double[] removeAll(double[] dArray, int ... nArray) {
        return (double[])ArrayUtils.removeAll((Object)dArray, nArray);
    }

    public static float[] removeAll(float[] fArray, int ... nArray) {
        return (float[])ArrayUtils.removeAll((Object)fArray, nArray);
    }

    public static int[] removeAll(int[] nArray, int ... nArray2) {
        return (int[])ArrayUtils.removeAll((Object)nArray, nArray2);
    }

    public static long[] removeAll(long[] lArray, int ... nArray) {
        return (long[])ArrayUtils.removeAll((Object)lArray, nArray);
    }

    static Object removeAll(Object object, BitSet bitSet) {
        int n;
        int n2;
        if (object == null) {
            return null;
        }
        int n3 = ArrayUtils.getLength(object);
        int n4 = bitSet.cardinality();
        Object object2 = Array.newInstance(object.getClass().getComponentType(), n3 - n4);
        int n5 = 0;
        int n6 = 0;
        while ((n2 = bitSet.nextSetBit(n5)) != -1) {
            n = n2 - n5;
            if (n > 0) {
                System.arraycopy(object, n5, object2, n6, n);
                n6 += n;
            }
            n5 = bitSet.nextClearBit(n2);
        }
        n = n3 - n5;
        if (n > 0) {
            System.arraycopy(object, n5, object2, n6, n);
        }
        return object2;
    }

    static Object removeAll(Object object, int ... nArray) {
        int n;
        int n2;
        if (object == null) {
            return null;
        }
        int n3 = ArrayUtils.getLength(object);
        int n4 = 0;
        int[] nArray2 = ArraySorter.sort(ArrayUtils.clone(nArray));
        if (ArrayUtils.isNotEmpty(nArray2)) {
            int n5 = nArray2.length;
            n2 = n3;
            while (--n5 >= 0) {
                n = nArray2[n5];
                if (n < 0 || n >= n3) {
                    throw new IndexOutOfBoundsException("Index: " + n + ", Length: " + n3);
                }
                if (n >= n2) continue;
                ++n4;
                n2 = n;
            }
        }
        Object object2 = Array.newInstance(object.getClass().getComponentType(), n3 - n4);
        if (n4 < n3 && nArray2 != null) {
            n2 = n3;
            n = n3 - n4;
            for (int i = nArray2.length - 1; i >= 0; --i) {
                int n6 = nArray2[i];
                if (n2 - n6 > 1) {
                    int n7 = n2 - n6 - 1;
                    System.arraycopy(object, n6 + 1, object2, n -= n7, n7);
                }
                n2 = n6;
            }
            if (n2 > 0) {
                System.arraycopy(object, 0, object2, 0, n2);
            }
        }
        return object2;
    }

    public static short[] removeAll(short[] sArray, int ... nArray) {
        return (short[])ArrayUtils.removeAll((Object)sArray, nArray);
    }

    public static <T> T[] removeAll(T[] TArray, int ... nArray) {
        return (Object[])ArrayUtils.removeAll(TArray, nArray);
    }

    @Deprecated
    public static boolean[] removeAllOccurences(boolean[] blArray, boolean bl) {
        return (boolean[])ArrayUtils.removeAll((Object)blArray, ArrayUtils.indexesOf(blArray, bl));
    }

    @Deprecated
    public static byte[] removeAllOccurences(byte[] byArray, byte by) {
        return (byte[])ArrayUtils.removeAll((Object)byArray, ArrayUtils.indexesOf(byArray, by));
    }

    @Deprecated
    public static char[] removeAllOccurences(char[] cArray, char c2) {
        return (char[])ArrayUtils.removeAll((Object)cArray, ArrayUtils.indexesOf(cArray, c2));
    }

    @Deprecated
    public static double[] removeAllOccurences(double[] dArray, double d) {
        return (double[])ArrayUtils.removeAll((Object)dArray, ArrayUtils.indexesOf(dArray, d));
    }

    @Deprecated
    public static float[] removeAllOccurences(float[] fArray, float f) {
        return (float[])ArrayUtils.removeAll((Object)fArray, ArrayUtils.indexesOf(fArray, f));
    }

    @Deprecated
    public static int[] removeAllOccurences(int[] nArray, int n) {
        return (int[])ArrayUtils.removeAll((Object)nArray, ArrayUtils.indexesOf(nArray, n));
    }

    @Deprecated
    public static long[] removeAllOccurences(long[] lArray, long l) {
        return (long[])ArrayUtils.removeAll((Object)lArray, ArrayUtils.indexesOf(lArray, l));
    }

    @Deprecated
    public static short[] removeAllOccurences(short[] sArray, short s) {
        return (short[])ArrayUtils.removeAll((Object)sArray, ArrayUtils.indexesOf(sArray, s));
    }

    @Deprecated
    public static <T> T[] removeAllOccurences(T[] TArray, T t) {
        return (Object[])ArrayUtils.removeAll(TArray, ArrayUtils.indexesOf(TArray, t));
    }

    public static boolean[] removeAllOccurrences(boolean[] blArray, boolean bl) {
        return (boolean[])ArrayUtils.removeAll((Object)blArray, ArrayUtils.indexesOf(blArray, bl));
    }

    public static byte[] removeAllOccurrences(byte[] byArray, byte by) {
        return (byte[])ArrayUtils.removeAll((Object)byArray, ArrayUtils.indexesOf(byArray, by));
    }

    public static char[] removeAllOccurrences(char[] cArray, char c2) {
        return (char[])ArrayUtils.removeAll((Object)cArray, ArrayUtils.indexesOf(cArray, c2));
    }

    public static double[] removeAllOccurrences(double[] dArray, double d) {
        return (double[])ArrayUtils.removeAll((Object)dArray, ArrayUtils.indexesOf(dArray, d));
    }

    public static float[] removeAllOccurrences(float[] fArray, float f) {
        return (float[])ArrayUtils.removeAll((Object)fArray, ArrayUtils.indexesOf(fArray, f));
    }

    public static int[] removeAllOccurrences(int[] nArray, int n) {
        return (int[])ArrayUtils.removeAll((Object)nArray, ArrayUtils.indexesOf(nArray, n));
    }

    public static long[] removeAllOccurrences(long[] lArray, long l) {
        return (long[])ArrayUtils.removeAll((Object)lArray, ArrayUtils.indexesOf(lArray, l));
    }

    public static short[] removeAllOccurrences(short[] sArray, short s) {
        return (short[])ArrayUtils.removeAll((Object)sArray, ArrayUtils.indexesOf(sArray, s));
    }

    public static <T> T[] removeAllOccurrences(T[] TArray, T t) {
        return (Object[])ArrayUtils.removeAll(TArray, ArrayUtils.indexesOf(TArray, t));
    }

    public static boolean[] removeElement(boolean[] blArray, boolean bl) {
        int n = ArrayUtils.indexOf(blArray, bl);
        return n == -1 ? ArrayUtils.clone(blArray) : ArrayUtils.remove(blArray, n);
    }

    public static byte[] removeElement(byte[] byArray, byte by) {
        int n = ArrayUtils.indexOf(byArray, by);
        return n == -1 ? ArrayUtils.clone(byArray) : ArrayUtils.remove(byArray, n);
    }

    public static char[] removeElement(char[] cArray, char c2) {
        int n = ArrayUtils.indexOf(cArray, c2);
        return n == -1 ? ArrayUtils.clone(cArray) : ArrayUtils.remove(cArray, n);
    }

    public static double[] removeElement(double[] dArray, double d) {
        int n = ArrayUtils.indexOf(dArray, d);
        return n == -1 ? ArrayUtils.clone(dArray) : ArrayUtils.remove(dArray, n);
    }

    public static float[] removeElement(float[] fArray, float f) {
        int n = ArrayUtils.indexOf(fArray, f);
        return n == -1 ? ArrayUtils.clone(fArray) : ArrayUtils.remove(fArray, n);
    }

    public static int[] removeElement(int[] nArray, int n) {
        int n2 = ArrayUtils.indexOf(nArray, n);
        return n2 == -1 ? ArrayUtils.clone(nArray) : ArrayUtils.remove(nArray, n2);
    }

    public static long[] removeElement(long[] lArray, long l) {
        int n = ArrayUtils.indexOf(lArray, l);
        return n == -1 ? ArrayUtils.clone(lArray) : ArrayUtils.remove(lArray, n);
    }

    public static short[] removeElement(short[] sArray, short s) {
        int n = ArrayUtils.indexOf(sArray, s);
        return n == -1 ? ArrayUtils.clone(sArray) : ArrayUtils.remove(sArray, n);
    }

    public static <T> T[] removeElement(T[] TArray, Object object) {
        int n = ArrayUtils.indexOf(TArray, object);
        return n == -1 ? ArrayUtils.clone(TArray) : ArrayUtils.remove(TArray, n);
    }

    public static boolean[] removeElements(boolean[] blArray, boolean ... blArray2) {
        if (ArrayUtils.isEmpty(blArray) || ArrayUtils.isEmpty(blArray2)) {
            return ArrayUtils.clone(blArray);
        }
        HashMap<Boolean, MutableInt> hashMap = new HashMap<Boolean, MutableInt>(2);
        for (boolean bl : blArray2) {
            Boolean bl2 = bl;
            MutableInt mutableInt = (MutableInt)hashMap.get(bl2);
            if (mutableInt == null) {
                hashMap.put(bl2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < blArray.length; ++i) {
            int n = blArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get(n != 0);
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove(n != 0);
            }
            ((BitSet)object).set(i);
        }
        return (boolean[])ArrayUtils.removeAll((Object)blArray, (BitSet)object);
    }

    public static byte[] removeElements(byte[] byArray, byte ... byArray2) {
        if (ArrayUtils.isEmpty(byArray) || ArrayUtils.isEmpty(byArray2)) {
            return ArrayUtils.clone(byArray);
        }
        HashMap<Byte, MutableInt> hashMap = new HashMap<Byte, MutableInt>(byArray2.length);
        for (byte by : byArray2) {
            Byte by2 = by;
            MutableInt mutableInt = (MutableInt)hashMap.get(by2);
            if (mutableInt == null) {
                hashMap.put(by2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < byArray.length; ++i) {
            int n = byArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get((byte)n);
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove((byte)n);
            }
            ((BitSet)object).set(i);
        }
        return (byte[])ArrayUtils.removeAll((Object)byArray, (BitSet)object);
    }

    public static char[] removeElements(char[] cArray, char ... cArray2) {
        if (ArrayUtils.isEmpty(cArray) || ArrayUtils.isEmpty(cArray2)) {
            return ArrayUtils.clone(cArray);
        }
        HashMap<Character, MutableInt> hashMap = new HashMap<Character, MutableInt>(cArray2.length);
        for (char c2 : cArray2) {
            Character c3 = Character.valueOf(c2);
            MutableInt mutableInt = (MutableInt)hashMap.get(c3);
            if (mutableInt == null) {
                hashMap.put(c3, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < cArray.length; ++i) {
            int n = cArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get(Character.valueOf((char)n));
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove(Character.valueOf((char)n));
            }
            ((BitSet)object).set(i);
        }
        return (char[])ArrayUtils.removeAll((Object)cArray, (BitSet)object);
    }

    public static double[] removeElements(double[] dArray, double ... dArray2) {
        if (ArrayUtils.isEmpty(dArray) || ArrayUtils.isEmpty(dArray2)) {
            return ArrayUtils.clone(dArray);
        }
        HashMap<Double, MutableInt> hashMap = new HashMap<Double, MutableInt>(dArray2.length);
        for (double d : dArray2) {
            Double d2 = d;
            MutableInt mutableInt = (MutableInt)hashMap.get(d2);
            if (mutableInt == null) {
                hashMap.put(d2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < dArray.length; ++i) {
            double d = dArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get(d);
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove(d);
            }
            ((BitSet)object).set(i);
        }
        return (double[])ArrayUtils.removeAll((Object)dArray, (BitSet)object);
    }

    public static float[] removeElements(float[] fArray, float ... fArray2) {
        if (ArrayUtils.isEmpty(fArray) || ArrayUtils.isEmpty(fArray2)) {
            return ArrayUtils.clone(fArray);
        }
        HashMap<Float, MutableInt> hashMap = new HashMap<Float, MutableInt>(fArray2.length);
        for (float f : fArray2) {
            Float f2 = Float.valueOf(f);
            MutableInt mutableInt = (MutableInt)hashMap.get(f2);
            if (mutableInt == null) {
                hashMap.put(f2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < fArray.length; ++i) {
            float f = fArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get(Float.valueOf(f));
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove(Float.valueOf(f));
            }
            ((BitSet)object).set(i);
        }
        return (float[])ArrayUtils.removeAll((Object)fArray, (BitSet)object);
    }

    public static int[] removeElements(int[] nArray, int ... nArray2) {
        if (ArrayUtils.isEmpty(nArray) || ArrayUtils.isEmpty(nArray2)) {
            return ArrayUtils.clone(nArray);
        }
        HashMap<Integer, MutableInt> hashMap = new HashMap<Integer, MutableInt>(nArray2.length);
        for (int n : nArray2) {
            Integer n2 = n;
            MutableInt mutableInt = (MutableInt)hashMap.get(n2);
            if (mutableInt == null) {
                hashMap.put(n2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < nArray.length; ++i) {
            int n = nArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get(n);
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove(n);
            }
            ((BitSet)object).set(i);
        }
        return (int[])ArrayUtils.removeAll((Object)nArray, (BitSet)object);
    }

    public static long[] removeElements(long[] lArray, long ... lArray2) {
        if (ArrayUtils.isEmpty(lArray) || ArrayUtils.isEmpty(lArray2)) {
            return ArrayUtils.clone(lArray);
        }
        HashMap<Long, MutableInt> hashMap = new HashMap<Long, MutableInt>(lArray2.length);
        for (long l : lArray2) {
            Long l2 = l;
            MutableInt mutableInt = (MutableInt)hashMap.get(l2);
            if (mutableInt == null) {
                hashMap.put(l2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < lArray.length; ++i) {
            long l = lArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get(l);
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove(l);
            }
            ((BitSet)object).set(i);
        }
        return (long[])ArrayUtils.removeAll((Object)lArray, (BitSet)object);
    }

    public static short[] removeElements(short[] sArray, short ... sArray2) {
        if (ArrayUtils.isEmpty(sArray) || ArrayUtils.isEmpty(sArray2)) {
            return ArrayUtils.clone(sArray);
        }
        HashMap<Short, MutableInt> hashMap = new HashMap<Short, MutableInt>(sArray2.length);
        for (short s : sArray2) {
            Short s2 = s;
            MutableInt mutableInt = (MutableInt)hashMap.get(s2);
            if (mutableInt == null) {
                hashMap.put(s2, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        Object object = new BitSet();
        for (int i = 0; i < sArray.length; ++i) {
            int n = sArray[i];
            MutableInt mutableInt = (MutableInt)hashMap.get((short)n);
            if (mutableInt == null) continue;
            if (mutableInt.decrementAndGet() == 0) {
                hashMap.remove((short)n);
            }
            ((BitSet)object).set(i);
        }
        return (short[])ArrayUtils.removeAll((Object)sArray, (BitSet)object);
    }

    @SafeVarargs
    public static <T> T[] removeElements(T[] TArray, T ... TArray2) {
        if (ArrayUtils.isEmpty(TArray) || ArrayUtils.isEmpty(TArray2)) {
            return ArrayUtils.clone(TArray);
        }
        HashMap<T, MutableInt> hashMap = new HashMap<T, MutableInt>(TArray2.length);
        for (Object object : TArray2) {
            MutableInt mutableInt = (MutableInt)hashMap.get(object);
            if (mutableInt == null) {
                hashMap.put(object, new MutableInt(1));
                continue;
            }
            mutableInt.increment();
        }
        BitSet bitSet = new BitSet();
        for (int i = 0; i < TArray.length; ++i) {
            Object object;
            T t = TArray[i];
            object = (MutableInt)hashMap.get(t);
            if (object == null) continue;
            if (((MutableInt)object).decrementAndGet() == 0) {
                hashMap.remove(t);
            }
            bitSet.set(i);
        }
        Object[] objectArray = (Object[])ArrayUtils.removeAll(TArray, bitSet);
        return objectArray;
    }

    public static void reverse(boolean[] blArray) {
        if (blArray == null) {
            return;
        }
        ArrayUtils.reverse(blArray, 0, blArray.length);
    }

    public static void reverse(boolean[] blArray, int n, int n2) {
        if (blArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(blArray.length, n2) - 1; i > n3; --i, ++n3) {
            boolean bl = blArray[i];
            blArray[i] = blArray[n3];
            blArray[n3] = bl;
        }
    }

    public static void reverse(byte[] byArray) {
        if (byArray != null) {
            ArrayUtils.reverse(byArray, 0, byArray.length);
        }
    }

    public static void reverse(byte[] byArray, int n, int n2) {
        if (byArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(byArray.length, n2) - 1; i > n3; --i, ++n3) {
            byte by = byArray[i];
            byArray[i] = byArray[n3];
            byArray[n3] = by;
        }
    }

    public static void reverse(char[] cArray) {
        if (cArray != null) {
            ArrayUtils.reverse(cArray, 0, cArray.length);
        }
    }

    public static void reverse(char[] cArray, int n, int n2) {
        if (cArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(cArray.length, n2) - 1; i > n3; --i, ++n3) {
            char c2 = cArray[i];
            cArray[i] = cArray[n3];
            cArray[n3] = c2;
        }
    }

    public static void reverse(double[] dArray) {
        if (dArray != null) {
            ArrayUtils.reverse(dArray, 0, dArray.length);
        }
    }

    public static void reverse(double[] dArray, int n, int n2) {
        if (dArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(dArray.length, n2) - 1; i > n3; --i, ++n3) {
            double d = dArray[i];
            dArray[i] = dArray[n3];
            dArray[n3] = d;
        }
    }

    public static void reverse(float[] fArray) {
        if (fArray != null) {
            ArrayUtils.reverse(fArray, 0, fArray.length);
        }
    }

    public static void reverse(float[] fArray, int n, int n2) {
        if (fArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(fArray.length, n2) - 1; i > n3; --i, ++n3) {
            float f = fArray[i];
            fArray[i] = fArray[n3];
            fArray[n3] = f;
        }
    }

    public static void reverse(int[] nArray) {
        if (nArray != null) {
            ArrayUtils.reverse(nArray, 0, nArray.length);
        }
    }

    public static void reverse(int[] nArray, int n, int n2) {
        if (nArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(nArray.length, n2) - 1; i > n3; --i, ++n3) {
            int n4 = nArray[i];
            nArray[i] = nArray[n3];
            nArray[n3] = n4;
        }
    }

    public static void reverse(long[] lArray) {
        if (lArray != null) {
            ArrayUtils.reverse(lArray, 0, lArray.length);
        }
    }

    public static void reverse(long[] lArray, int n, int n2) {
        if (lArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(lArray.length, n2) - 1; i > n3; --i, ++n3) {
            long l = lArray[i];
            lArray[i] = lArray[n3];
            lArray[n3] = l;
        }
    }

    public static void reverse(Object[] objectArray) {
        if (objectArray != null) {
            ArrayUtils.reverse(objectArray, 0, objectArray.length);
        }
    }

    public static void reverse(Object[] objectArray, int n, int n2) {
        if (objectArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(objectArray.length, n2) - 1; i > n3; --i, ++n3) {
            Object object = objectArray[i];
            objectArray[i] = objectArray[n3];
            objectArray[n3] = object;
        }
    }

    public static void reverse(short[] sArray) {
        if (sArray != null) {
            ArrayUtils.reverse(sArray, 0, sArray.length);
        }
    }

    public static void reverse(short[] sArray, int n, int n2) {
        if (sArray == null) {
            return;
        }
        int n3 = Math.max(n, 0);
        for (int i = Math.min(sArray.length, n2) - 1; i > n3; --i, ++n3) {
            short s = sArray[i];
            sArray[i] = sArray[n3];
            sArray[n3] = s;
        }
    }

    public static <T> T[] setAll(T[] TArray, IntFunction<? extends T> intFunction) {
        if (TArray != null && intFunction != null) {
            Arrays.setAll(TArray, intFunction);
        }
        return TArray;
    }

    public static <T> T[] setAll(T[] TArray, Supplier<? extends T> supplier) {
        if (TArray != null && supplier != null) {
            for (int i = 0; i < TArray.length; ++i) {
                TArray[i] = supplier.get();
            }
        }
        return TArray;
    }

    public static void shift(boolean[] blArray, int n) {
        if (blArray != null) {
            ArrayUtils.shift(blArray, 0, blArray.length, n);
        }
    }

    public static void shift(boolean[] blArray, int n, int n2, int n3) {
        if (blArray == null || n >= blArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, blArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(blArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(blArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(blArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(byte[] byArray, int n) {
        if (byArray != null) {
            ArrayUtils.shift(byArray, 0, byArray.length, n);
        }
    }

    public static void shift(byte[] byArray, int n, int n2, int n3) {
        if (byArray == null || n >= byArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, byArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(byArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(byArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(byArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(char[] cArray, int n) {
        if (cArray != null) {
            ArrayUtils.shift(cArray, 0, cArray.length, n);
        }
    }

    public static void shift(char[] cArray, int n, int n2, int n3) {
        if (cArray == null || n >= cArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, cArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(cArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(cArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(cArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(double[] dArray, int n) {
        if (dArray != null) {
            ArrayUtils.shift(dArray, 0, dArray.length, n);
        }
    }

    public static void shift(double[] dArray, int n, int n2, int n3) {
        if (dArray == null || n >= dArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, dArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(dArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(dArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(dArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(float[] fArray, int n) {
        if (fArray != null) {
            ArrayUtils.shift(fArray, 0, fArray.length, n);
        }
    }

    public static void shift(float[] fArray, int n, int n2, int n3) {
        if (fArray == null || n >= fArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, fArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(fArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(fArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(fArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(int[] nArray, int n) {
        if (nArray != null) {
            ArrayUtils.shift(nArray, 0, nArray.length, n);
        }
    }

    public static void shift(int[] nArray, int n, int n2, int n3) {
        if (nArray == null || n >= nArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, nArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(nArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(nArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(nArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(long[] lArray, int n) {
        if (lArray != null) {
            ArrayUtils.shift(lArray, 0, lArray.length, n);
        }
    }

    public static void shift(long[] lArray, int n, int n2, int n3) {
        if (lArray == null || n >= lArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, lArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(lArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(lArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(lArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(Object[] objectArray, int n) {
        if (objectArray != null) {
            ArrayUtils.shift(objectArray, 0, objectArray.length, n);
        }
    }

    public static void shift(Object[] objectArray, int n, int n2, int n3) {
        if (objectArray == null || n >= objectArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, objectArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(objectArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(objectArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(objectArray, n, n + n5, n3);
            break;
        }
    }

    public static void shift(short[] sArray, int n) {
        if (sArray != null) {
            ArrayUtils.shift(sArray, 0, sArray.length, n);
        }
    }

    public static void shift(short[] sArray, int n, int n2, int n3) {
        if (sArray == null || n >= sArray.length - 1 || n2 <= 0) {
            return;
        }
        n = ArrayUtils.max0(n);
        int n4 = (n2 = Math.min(n2, sArray.length)) - n;
        if (n4 <= 1) {
            return;
        }
        if ((n3 %= n4) < 0) {
            n3 += n4;
        }
        while (n4 > 1 && n3 > 0) {
            int n5 = n4 - n3;
            if (n3 > n5) {
                ArrayUtils.swap(sArray, n, n + n4 - n5, n5);
                n4 = n3;
                n3 -= n5;
                continue;
            }
            if (n3 < n5) {
                ArrayUtils.swap(sArray, n, n + n5, n3);
                n += n3;
                n4 = n5;
                continue;
            }
            ArrayUtils.swap(sArray, n, n + n5, n3);
            break;
        }
    }

    public static void shuffle(boolean[] blArray) {
        ArrayUtils.shuffle(blArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(boolean[] blArray, Random random) {
        for (int i = blArray.length; i > 1; --i) {
            ArrayUtils.swap(blArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(byte[] byArray) {
        ArrayUtils.shuffle(byArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(byte[] byArray, Random random) {
        for (int i = byArray.length; i > 1; --i) {
            ArrayUtils.swap(byArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(char[] cArray) {
        ArrayUtils.shuffle(cArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(char[] cArray, Random random) {
        for (int i = cArray.length; i > 1; --i) {
            ArrayUtils.swap(cArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(double[] dArray) {
        ArrayUtils.shuffle(dArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(double[] dArray, Random random) {
        for (int i = dArray.length; i > 1; --i) {
            ArrayUtils.swap(dArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(float[] fArray) {
        ArrayUtils.shuffle(fArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(float[] fArray, Random random) {
        for (int i = fArray.length; i > 1; --i) {
            ArrayUtils.swap(fArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(int[] nArray) {
        ArrayUtils.shuffle(nArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(int[] nArray, Random random) {
        for (int i = nArray.length; i > 1; --i) {
            ArrayUtils.swap(nArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(long[] lArray) {
        ArrayUtils.shuffle(lArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(long[] lArray, Random random) {
        for (int i = lArray.length; i > 1; --i) {
            ArrayUtils.swap(lArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(Object[] objectArray) {
        ArrayUtils.shuffle(objectArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(Object[] objectArray, Random random) {
        for (int i = objectArray.length; i > 1; --i) {
            ArrayUtils.swap(objectArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static void shuffle(short[] sArray) {
        ArrayUtils.shuffle(sArray, (Random)ArrayUtils.random());
    }

    public static void shuffle(short[] sArray, Random random) {
        for (int i = sArray.length; i > 1; --i) {
            ArrayUtils.swap(sArray, i - 1, random.nextInt(i), 1);
        }
    }

    public static boolean[] subarray(boolean[] blArray, int n, int n2) {
        if (blArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, blArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        return ArrayUtils.arraycopy(blArray, n, 0, n3, boolean[]::new);
    }

    public static byte[] subarray(byte[] byArray, int n, int n2) {
        if (byArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, byArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_BYTE_ARRAY;
        }
        return ArrayUtils.arraycopy(byArray, n, 0, n3, byte[]::new);
    }

    public static char[] subarray(char[] cArray, int n, int n2) {
        if (cArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, cArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_CHAR_ARRAY;
        }
        return ArrayUtils.arraycopy(cArray, n, 0, n3, char[]::new);
    }

    public static double[] subarray(double[] dArray, int n, int n2) {
        if (dArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, dArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        return ArrayUtils.arraycopy(dArray, n, 0, n3, double[]::new);
    }

    public static float[] subarray(float[] fArray, int n, int n2) {
        if (fArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, fArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        return ArrayUtils.arraycopy(fArray, n, 0, n3, float[]::new);
    }

    public static int[] subarray(int[] nArray, int n, int n2) {
        if (nArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, nArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_INT_ARRAY;
        }
        return ArrayUtils.arraycopy(nArray, n, 0, n3, int[]::new);
    }

    public static long[] subarray(long[] lArray, int n, int n2) {
        if (lArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, lArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_LONG_ARRAY;
        }
        return ArrayUtils.arraycopy(lArray, n, 0, n3, long[]::new);
    }

    public static short[] subarray(short[] sArray, int n, int n2) {
        if (sArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        int n3 = (n2 = Math.min(n2, sArray.length)) - n;
        if (n3 <= 0) {
            return EMPTY_SHORT_ARRAY;
        }
        return ArrayUtils.arraycopy(sArray, n, 0, n3, short[]::new);
    }

    public static <T> T[] subarray(T[] TArray, int n, int n2) {
        if (TArray == null) {
            return null;
        }
        n = ArrayUtils.max0(n);
        n2 = Math.min(n2, TArray.length);
        int n3 = n2 - n;
        Class clazz = ArrayUtils.getComponentType(TArray);
        if (n3 <= 0) {
            return ArrayUtils.newInstance(clazz, 0);
        }
        return ArrayUtils.arraycopy(TArray, n, 0, n3, () -> ArrayUtils.newInstance(clazz, n3));
    }

    public static void swap(boolean[] blArray, int n, int n2) {
        ArrayUtils.swap(blArray, n, n2, 1);
    }

    public static void swap(boolean[] blArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(blArray) || n >= blArray.length || n2 >= blArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, blArray.length - n), blArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            boolean bl = blArray[n];
            blArray[n] = blArray[n2];
            blArray[n2] = bl;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(byte[] byArray, int n, int n2) {
        ArrayUtils.swap(byArray, n, n2, 1);
    }

    public static void swap(byte[] byArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(byArray) || n >= byArray.length || n2 >= byArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, byArray.length - n), byArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            byte by = byArray[n];
            byArray[n] = byArray[n2];
            byArray[n2] = by;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(char[] cArray, int n, int n2) {
        ArrayUtils.swap(cArray, n, n2, 1);
    }

    public static void swap(char[] cArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(cArray) || n >= cArray.length || n2 >= cArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, cArray.length - n), cArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            char c2 = cArray[n];
            cArray[n] = cArray[n2];
            cArray[n2] = c2;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(double[] dArray, int n, int n2) {
        ArrayUtils.swap(dArray, n, n2, 1);
    }

    public static void swap(double[] dArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(dArray) || n >= dArray.length || n2 >= dArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, dArray.length - n), dArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            double d = dArray[n];
            dArray[n] = dArray[n2];
            dArray[n2] = d;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(float[] fArray, int n, int n2) {
        ArrayUtils.swap(fArray, n, n2, 1);
    }

    public static void swap(float[] fArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(fArray) || n >= fArray.length || n2 >= fArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, fArray.length - n), fArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            float f = fArray[n];
            fArray[n] = fArray[n2];
            fArray[n2] = f;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(int[] nArray, int n, int n2) {
        ArrayUtils.swap(nArray, n, n2, 1);
    }

    public static void swap(int[] nArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(nArray) || n >= nArray.length || n2 >= nArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, nArray.length - n), nArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            int n5 = nArray[n];
            nArray[n] = nArray[n2];
            nArray[n2] = n5;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(long[] lArray, int n, int n2) {
        ArrayUtils.swap(lArray, n, n2, 1);
    }

    public static void swap(long[] lArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(lArray) || n >= lArray.length || n2 >= lArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, lArray.length - n), lArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            long l = lArray[n];
            lArray[n] = lArray[n2];
            lArray[n2] = l;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(Object[] objectArray, int n, int n2) {
        ArrayUtils.swap(objectArray, n, n2, 1);
    }

    public static void swap(Object[] objectArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(objectArray) || n >= objectArray.length || n2 >= objectArray.length) {
            return;
        }
        n = ArrayUtils.max0(n);
        n2 = ArrayUtils.max0(n2);
        n3 = Math.min(Math.min(n3, objectArray.length - n), objectArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            Object object = objectArray[n];
            objectArray[n] = objectArray[n2];
            objectArray[n2] = object;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static void swap(short[] sArray, int n, int n2) {
        ArrayUtils.swap(sArray, n, n2, 1);
    }

    public static void swap(short[] sArray, int n, int n2, int n3) {
        if (ArrayUtils.isEmpty(sArray) || n >= sArray.length || n2 >= sArray.length) {
            return;
        }
        if ((n = ArrayUtils.max0(n)) == (n2 = ArrayUtils.max0(n2))) {
            return;
        }
        n3 = Math.min(Math.min(n3, sArray.length - n), sArray.length - n2);
        int n4 = 0;
        while (n4 < n3) {
            short s = sArray[n];
            sArray[n] = sArray[n2];
            sArray[n2] = s;
            ++n4;
            ++n;
            ++n2;
        }
    }

    public static <T> T[] toArray(T ... TArray) {
        return TArray;
    }

    public static Map<Object, Object> toMap(Object[] objectArray) {
        if (objectArray == null) {
            return null;
        }
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>((int)((double)objectArray.length * 1.5));
        for (int i = 0; i < objectArray.length; ++i) {
            Object[] objectArray2;
            Object object = objectArray[i];
            if (object instanceof Map.Entry) {
                objectArray2 = (Object[])object;
                hashMap.put(objectArray2.getKey(), objectArray2.getValue());
                continue;
            }
            if (object instanceof Object[]) {
                objectArray2 = (Object[])object;
                if (objectArray2.length < 2) {
                    throw new IllegalArgumentException("Array element " + i + ", '" + object + "', has a length less than 2");
                }
                hashMap.put(objectArray2[0], objectArray2[1]);
                continue;
            }
            throw new IllegalArgumentException("Array element " + i + ", '" + object + "', is neither of type Map.Entry nor an Array");
        }
        return hashMap;
    }

    public static Boolean[] toObject(boolean[] blArray) {
        if (blArray == null) {
            return null;
        }
        if (blArray.length == 0) {
            return EMPTY_BOOLEAN_OBJECT_ARRAY;
        }
        Boolean[] booleanArray = new Boolean[blArray.length];
        return ArrayUtils.setAll(booleanArray, (int n) -> blArray[n] ? Boolean.TRUE : Boolean.FALSE);
    }

    public static Byte[] toObject(byte[] byArray) {
        if (byArray == null) {
            return null;
        }
        if (byArray.length == 0) {
            return EMPTY_BYTE_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Byte[byArray.length], (int n) -> byArray[n]);
    }

    public static Character[] toObject(char[] cArray) {
        if (cArray == null) {
            return null;
        }
        if (cArray.length == 0) {
            return EMPTY_CHARACTER_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Character[cArray.length], (int n) -> Character.valueOf(cArray[n]));
    }

    public static Double[] toObject(double[] dArray) {
        if (dArray == null) {
            return null;
        }
        if (dArray.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Double[dArray.length], (int n) -> dArray[n]);
    }

    public static Float[] toObject(float[] fArray) {
        if (fArray == null) {
            return null;
        }
        if (fArray.length == 0) {
            return EMPTY_FLOAT_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Float[fArray.length], (int n) -> Float.valueOf(fArray[n]));
    }

    public static Integer[] toObject(int[] nArray) {
        if (nArray == null) {
            return null;
        }
        if (nArray.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Integer[nArray.length], (int n) -> nArray[n]);
    }

    public static Long[] toObject(long[] lArray) {
        if (lArray == null) {
            return null;
        }
        if (lArray.length == 0) {
            return EMPTY_LONG_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Long[lArray.length], (int n) -> lArray[n]);
    }

    public static Short[] toObject(short[] sArray) {
        if (sArray == null) {
            return null;
        }
        if (sArray.length == 0) {
            return EMPTY_SHORT_OBJECT_ARRAY;
        }
        return ArrayUtils.setAll(new Short[sArray.length], (int n) -> sArray[n]);
    }

    public static boolean[] toPrimitive(Boolean[] booleanArray) {
        return ArrayUtils.toPrimitive(booleanArray, false);
    }

    public static boolean[] toPrimitive(Boolean[] booleanArray, boolean bl) {
        if (booleanArray == null) {
            return null;
        }
        if (booleanArray.length == 0) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        boolean[] blArray = new boolean[booleanArray.length];
        for (int i = 0; i < booleanArray.length; ++i) {
            Boolean bl2 = booleanArray[i];
            blArray[i] = bl2 == null ? bl : bl2;
        }
        return blArray;
    }

    public static byte[] toPrimitive(Byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        if (byteArray.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] byArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; ++i) {
            byArray[i] = byteArray[i];
        }
        return byArray;
    }

    public static byte[] toPrimitive(Byte[] byteArray, byte by) {
        if (byteArray == null) {
            return null;
        }
        if (byteArray.length == 0) {
            return EMPTY_BYTE_ARRAY;
        }
        byte[] byArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; ++i) {
            Byte by2 = byteArray[i];
            byArray[i] = by2 == null ? by : by2;
        }
        return byArray;
    }

    public static char[] toPrimitive(Character[] characterArray) {
        if (characterArray == null) {
            return null;
        }
        if (characterArray.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] cArray = new char[characterArray.length];
        for (int i = 0; i < characterArray.length; ++i) {
            cArray[i] = characterArray[i].charValue();
        }
        return cArray;
    }

    public static char[] toPrimitive(Character[] characterArray, char c2) {
        if (characterArray == null) {
            return null;
        }
        if (characterArray.length == 0) {
            return EMPTY_CHAR_ARRAY;
        }
        char[] cArray = new char[characterArray.length];
        for (int i = 0; i < characterArray.length; ++i) {
            Character c3 = characterArray[i];
            cArray[i] = c3 == null ? c2 : c3.charValue();
        }
        return cArray;
    }

    public static double[] toPrimitive(Double[] doubleArray) {
        if (doubleArray == null) {
            return null;
        }
        if (doubleArray.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] dArray = new double[doubleArray.length];
        for (int i = 0; i < doubleArray.length; ++i) {
            dArray[i] = doubleArray[i];
        }
        return dArray;
    }

    public static double[] toPrimitive(Double[] doubleArray, double d) {
        if (doubleArray == null) {
            return null;
        }
        if (doubleArray.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        double[] dArray = new double[doubleArray.length];
        for (int i = 0; i < doubleArray.length; ++i) {
            Double d2 = doubleArray[i];
            dArray[i] = d2 == null ? d : d2;
        }
        return dArray;
    }

    public static float[] toPrimitive(Float[] floatArray) {
        if (floatArray == null) {
            return null;
        }
        if (floatArray.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] fArray = new float[floatArray.length];
        for (int i = 0; i < floatArray.length; ++i) {
            fArray[i] = floatArray[i].floatValue();
        }
        return fArray;
    }

    public static float[] toPrimitive(Float[] floatArray, float f) {
        if (floatArray == null) {
            return null;
        }
        if (floatArray.length == 0) {
            return EMPTY_FLOAT_ARRAY;
        }
        float[] fArray = new float[floatArray.length];
        for (int i = 0; i < floatArray.length; ++i) {
            Float f2 = floatArray[i];
            fArray[i] = f2 == null ? f : f2.floatValue();
        }
        return fArray;
    }

    public static int[] toPrimitive(Integer[] integerArray) {
        if (integerArray == null) {
            return null;
        }
        if (integerArray.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] nArray = new int[integerArray.length];
        for (int i = 0; i < integerArray.length; ++i) {
            nArray[i] = integerArray[i];
        }
        return nArray;
    }

    public static int[] toPrimitive(Integer[] integerArray, int n) {
        if (integerArray == null) {
            return null;
        }
        if (integerArray.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        int[] nArray = new int[integerArray.length];
        for (int i = 0; i < integerArray.length; ++i) {
            Integer n2 = integerArray[i];
            nArray[i] = n2 == null ? n : n2;
        }
        return nArray;
    }

    public static long[] toPrimitive(Long[] longArray) {
        if (longArray == null) {
            return null;
        }
        if (longArray.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] lArray = new long[longArray.length];
        for (int i = 0; i < longArray.length; ++i) {
            lArray[i] = longArray[i];
        }
        return lArray;
    }

    public static long[] toPrimitive(Long[] longArray, long l) {
        if (longArray == null) {
            return null;
        }
        if (longArray.length == 0) {
            return EMPTY_LONG_ARRAY;
        }
        long[] lArray = new long[longArray.length];
        for (int i = 0; i < longArray.length; ++i) {
            Long l2 = longArray[i];
            lArray[i] = l2 == null ? l : l2;
        }
        return lArray;
    }

    public static Object toPrimitive(Object object) {
        if (object == null) {
            return null;
        }
        Class<?> clazz = object.getClass().getComponentType();
        Class<?> clazz2 = ClassUtils.wrapperToPrimitive(clazz);
        if (Boolean.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Boolean[])object);
        }
        if (Character.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Character[])object);
        }
        if (Byte.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Byte[])object);
        }
        if (Integer.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Integer[])object);
        }
        if (Long.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Long[])object);
        }
        if (Short.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Short[])object);
        }
        if (Double.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Double[])object);
        }
        if (Float.TYPE.equals(clazz2)) {
            return ArrayUtils.toPrimitive((Float[])object);
        }
        return object;
    }

    public static short[] toPrimitive(Short[] shortArray) {
        if (shortArray == null) {
            return null;
        }
        if (shortArray.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] sArray = new short[shortArray.length];
        for (int i = 0; i < shortArray.length; ++i) {
            sArray[i] = shortArray[i];
        }
        return sArray;
    }

    public static short[] toPrimitive(Short[] shortArray, short s) {
        if (shortArray == null) {
            return null;
        }
        if (shortArray.length == 0) {
            return EMPTY_SHORT_ARRAY;
        }
        short[] sArray = new short[shortArray.length];
        for (int i = 0; i < shortArray.length; ++i) {
            Short s2 = shortArray[i];
            sArray[i] = s2 == null ? s : s2;
        }
        return sArray;
    }

    public static String toString(Object object) {
        return ArrayUtils.toString(object, "{}");
    }

    public static String toString(Object object, String string) {
        if (object == null) {
            return string;
        }
        return new ToStringBuilder(object, ToStringStyle.SIMPLE_STYLE).append(object).toString();
    }

    public static String[] toStringArray(Object[] objectArray) {
        if (objectArray == null) {
            return null;
        }
        if (objectArray.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; ++i) {
            stringArray[i] = objectArray[i].toString();
        }
        return stringArray;
    }

    public static String[] toStringArray(Object[] objectArray, String string) {
        if (null == objectArray) {
            return null;
        }
        if (objectArray.length == 0) {
            return EMPTY_STRING_ARRAY;
        }
        String[] stringArray = new String[objectArray.length];
        for (int i = 0; i < objectArray.length; ++i) {
            stringArray[i] = Objects.toString(objectArray[i], string);
        }
        return stringArray;
    }

    @Deprecated
    public ArrayUtils() {
    }
}

