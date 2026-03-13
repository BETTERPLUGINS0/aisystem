/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import net.advancedplugins.as.libs.apache.commons.math3.Field;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.UniformIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.DimensionMismatchException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathArithmeticException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathInternalError;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NoDataException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NonMonotonicSequenceException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotANumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NullArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.Precision;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class MathArrays {
    private MathArrays() {
    }

    public static double[] scale(double d, double[] dArray) {
        double[] dArray2 = new double[dArray.length];
        for (int i = 0; i < dArray.length; ++i) {
            dArray2[i] = dArray[i] * d;
        }
        return dArray2;
    }

    public static void scaleInPlace(double d, double[] dArray) {
        int n = 0;
        while (n < dArray.length) {
            int n2 = n++;
            dArray[n2] = dArray[n2] * d;
        }
    }

    public static double[] ebeAdd(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double[] dArray3 = (double[])dArray.clone();
        for (int i = 0; i < dArray.length; ++i) {
            int n = i;
            dArray3[n] = dArray3[n] + dArray2[i];
        }
        return dArray3;
    }

    public static double[] ebeSubtract(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double[] dArray3 = (double[])dArray.clone();
        for (int i = 0; i < dArray.length; ++i) {
            int n = i;
            dArray3[n] = dArray3[n] - dArray2[i];
        }
        return dArray3;
    }

    public static double[] ebeMultiply(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double[] dArray3 = (double[])dArray.clone();
        for (int i = 0; i < dArray.length; ++i) {
            int n = i;
            dArray3[n] = dArray3[n] * dArray2[i];
        }
        return dArray3;
    }

    public static double[] ebeDivide(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double[] dArray3 = (double[])dArray.clone();
        for (int i = 0; i < dArray.length; ++i) {
            int n = i;
            dArray3[n] = dArray3[n] / dArray2[i];
        }
        return dArray3;
    }

    public static double distance1(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double d = 0.0;
        for (int i = 0; i < dArray.length; ++i) {
            d += FastMath.abs(dArray[i] - dArray2[i]);
        }
        return d;
    }

    public static int distance1(int[] nArray, int[] nArray2) {
        MathArrays.checkEqualLength(nArray, nArray2);
        int n = 0;
        for (int i = 0; i < nArray.length; ++i) {
            n += FastMath.abs(nArray[i] - nArray2[i]);
        }
        return n;
    }

    public static double distance(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double d = 0.0;
        for (int i = 0; i < dArray.length; ++i) {
            double d2 = dArray[i] - dArray2[i];
            d += d2 * d2;
        }
        return FastMath.sqrt(d);
    }

    public static double cosAngle(double[] dArray, double[] dArray2) {
        return MathArrays.linearCombination(dArray, dArray2) / (MathArrays.safeNorm(dArray) * MathArrays.safeNorm(dArray2));
    }

    public static double distance(int[] nArray, int[] nArray2) {
        MathArrays.checkEqualLength(nArray, nArray2);
        double d = 0.0;
        for (int i = 0; i < nArray.length; ++i) {
            double d2 = nArray[i] - nArray2[i];
            d += d2 * d2;
        }
        return FastMath.sqrt(d);
    }

    public static double distanceInf(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        double d = 0.0;
        for (int i = 0; i < dArray.length; ++i) {
            d = FastMath.max(d, FastMath.abs(dArray[i] - dArray2[i]));
        }
        return d;
    }

    public static int distanceInf(int[] nArray, int[] nArray2) {
        MathArrays.checkEqualLength(nArray, nArray2);
        int n = 0;
        for (int i = 0; i < nArray.length; ++i) {
            n = FastMath.max(n, FastMath.abs(nArray[i] - nArray2[i]));
        }
        return n;
    }

    public static <T extends Comparable<? super T>> boolean isMonotonic(T[] TArray, OrderDirection orderDirection, boolean bl) {
        T t = TArray[0];
        int n = TArray.length;
        for (int i = 1; i < n; ++i) {
            switch (orderDirection) {
                case INCREASING: {
                    int n2 = t.compareTo(TArray[i]);
                    if (!(bl ? n2 >= 0 : n2 > 0)) break;
                    return false;
                }
                case DECREASING: {
                    int n2 = TArray[i].compareTo(t);
                    if (!(bl ? n2 >= 0 : n2 > 0)) break;
                    return false;
                }
                default: {
                    throw new MathInternalError();
                }
            }
            t = TArray[i];
        }
        return true;
    }

    public static boolean isMonotonic(double[] dArray, OrderDirection orderDirection, boolean bl) {
        return MathArrays.checkOrder(dArray, orderDirection, bl, false);
    }

    public static boolean checkEqualLength(double[] dArray, double[] dArray2, boolean bl) {
        if (dArray.length == dArray2.length) {
            return true;
        }
        if (bl) {
            throw new DimensionMismatchException(dArray.length, dArray2.length);
        }
        return false;
    }

    public static void checkEqualLength(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2, true);
    }

    public static boolean checkEqualLength(int[] nArray, int[] nArray2, boolean bl) {
        if (nArray.length == nArray2.length) {
            return true;
        }
        if (bl) {
            throw new DimensionMismatchException(nArray.length, nArray2.length);
        }
        return false;
    }

    public static void checkEqualLength(int[] nArray, int[] nArray2) {
        MathArrays.checkEqualLength(nArray, nArray2, true);
    }

    public static boolean checkOrder(double[] dArray, OrderDirection orderDirection, boolean bl, boolean bl2) {
        int n;
        double d = dArray[0];
        int n2 = dArray.length;
        block4: for (n = 1; n < n2; ++n) {
            switch (orderDirection) {
                case INCREASING: {
                    if (!(bl ? dArray[n] <= d : dArray[n] < d)) break;
                    break block4;
                }
                case DECREASING: {
                    if (!(bl ? dArray[n] >= d : dArray[n] > d)) break;
                    break block4;
                }
                default: {
                    throw new MathInternalError();
                }
            }
            d = dArray[n];
        }
        if (n == n2) {
            return true;
        }
        if (bl2) {
            throw new NonMonotonicSequenceException(dArray[n], (Number)d, n, orderDirection, bl);
        }
        return false;
    }

    public static void checkOrder(double[] dArray, OrderDirection orderDirection, boolean bl) {
        MathArrays.checkOrder(dArray, orderDirection, bl, true);
    }

    public static void checkOrder(double[] dArray) {
        MathArrays.checkOrder(dArray, OrderDirection.INCREASING, true);
    }

    public static void checkRectangular(long[][] lArray) {
        MathUtils.checkNotNull(lArray);
        for (int i = 1; i < lArray.length; ++i) {
            if (lArray[i].length == lArray[0].length) continue;
            throw new DimensionMismatchException((Localizable)LocalizedFormats.DIFFERENT_ROWS_LENGTHS, lArray[i].length, lArray[0].length);
        }
    }

    public static void checkPositive(double[] dArray) {
        for (int i = 0; i < dArray.length; ++i) {
            if (!(dArray[i] <= 0.0)) continue;
            throw new NotStrictlyPositiveException(dArray[i]);
        }
    }

    public static void checkNotNaN(double[] dArray) {
        for (int i = 0; i < dArray.length; ++i) {
            if (!Double.isNaN(dArray[i])) continue;
            throw new NotANumberException();
        }
    }

    public static void checkNonNegative(long[] lArray) {
        for (int i = 0; i < lArray.length; ++i) {
            if (lArray[i] >= 0L) continue;
            throw new NotPositiveException(lArray[i]);
        }
    }

    public static void checkNonNegative(long[][] lArray) {
        for (int i = 0; i < lArray.length; ++i) {
            for (int j = 0; j < lArray[i].length; ++j) {
                if (lArray[i][j] >= 0L) continue;
                throw new NotPositiveException(lArray[i][j]);
            }
        }
    }

    public static double safeNorm(double[] dArray) {
        double d = 3.834E-20;
        double d2 = 1.304E19;
        double d3 = 0.0;
        double d4 = 0.0;
        double d5 = 0.0;
        double d6 = 0.0;
        double d7 = 0.0;
        double d8 = dArray.length;
        double d9 = d2 / d8;
        for (int i = 0; i < dArray.length; ++i) {
            double d10 = FastMath.abs(dArray[i]);
            if (d10 < d || d10 > d9) {
                double d11;
                if (d10 > d) {
                    if (d10 > d6) {
                        d11 = d6 / d10;
                        d3 = 1.0 + d3 * d11 * d11;
                        d6 = d10;
                        continue;
                    }
                    d11 = d10 / d6;
                    d3 += d11 * d11;
                    continue;
                }
                if (d10 > d7) {
                    d11 = d7 / d10;
                    d5 = 1.0 + d5 * d11 * d11;
                    d7 = d10;
                    continue;
                }
                if (d10 == 0.0) continue;
                d11 = d10 / d7;
                d5 += d11 * d11;
                continue;
            }
            d4 += d10 * d10;
        }
        double d12 = d3 != 0.0 ? d6 * Math.sqrt(d3 + d4 / d6 / d6) : (d4 == 0.0 ? d7 * Math.sqrt(d5) : (d4 >= d7 ? Math.sqrt(d4 * (1.0 + d7 / d4 * (d7 * d5))) : Math.sqrt(d7 * (d4 / d7 + d7 * d5))));
        return d12;
    }

    public static void sortInPlace(double[] dArray, double[] ... dArray2) {
        MathArrays.sortInPlace(dArray, OrderDirection.INCREASING, dArray2);
    }

    public static void sortInPlace(double[] dArray, OrderDirection orderDirection, double[] ... dArray2) {
        Object object;
        int n;
        if (dArray == null) {
            throw new NullArgumentException();
        }
        int n2 = dArray2.length;
        int n3 = dArray.length;
        for (int i = 0; i < n2; ++i) {
            double[] dArray3 = dArray2[i];
            if (dArray3 == null) {
                throw new NullArgumentException();
            }
            if (dArray3.length == n3) continue;
            throw new DimensionMismatchException(dArray3.length, n3);
        }
        ArrayList<PairDoubleInteger> arrayList = new ArrayList<PairDoubleInteger>(n3);
        for (int i = 0; i < n3; ++i) {
            arrayList.add(new PairDoubleInteger(dArray[i], i));
        }
        Comparator<PairDoubleInteger> comparator = orderDirection == OrderDirection.INCREASING ? new Comparator<PairDoubleInteger>(){

            @Override
            public int compare(PairDoubleInteger pairDoubleInteger, PairDoubleInteger pairDoubleInteger2) {
                return Double.compare(pairDoubleInteger.getKey(), pairDoubleInteger2.getKey());
            }
        } : new Comparator<PairDoubleInteger>(){

            @Override
            public int compare(PairDoubleInteger pairDoubleInteger, PairDoubleInteger pairDoubleInteger2) {
                return Double.compare(pairDoubleInteger2.getKey(), pairDoubleInteger.getKey());
            }
        };
        Collections.sort(arrayList, comparator);
        int[] nArray = new int[n3];
        for (n = 0; n < n3; ++n) {
            object = (PairDoubleInteger)arrayList.get(n);
            dArray[n] = ((PairDoubleInteger)object).getKey();
            nArray[n] = ((PairDoubleInteger)object).getValue();
        }
        for (n = 0; n < n2; ++n) {
            object = dArray2[n];
            double[] dArray4 = (double[])object.clone();
            for (int i = 0; i < n3; ++i) {
                object[i] = dArray4[nArray[i]];
            }
        }
    }

    public static int[] copyOf(int[] nArray) {
        return MathArrays.copyOf(nArray, nArray.length);
    }

    public static double[] copyOf(double[] dArray) {
        return MathArrays.copyOf(dArray, dArray.length);
    }

    public static int[] copyOf(int[] nArray, int n) {
        int[] nArray2 = new int[n];
        System.arraycopy(nArray, 0, nArray2, 0, FastMath.min(n, nArray.length));
        return nArray2;
    }

    public static double[] copyOf(double[] dArray, int n) {
        double[] dArray2 = new double[n];
        System.arraycopy(dArray, 0, dArray2, 0, FastMath.min(n, dArray.length));
        return dArray2;
    }

    public static double[] copyOfRange(double[] dArray, int n, int n2) {
        int n3 = n2 - n;
        double[] dArray2 = new double[n3];
        System.arraycopy(dArray, n, dArray2, 0, FastMath.min(n3, dArray.length - n));
        return dArray2;
    }

    public static double linearCombination(double[] dArray, double[] dArray2) {
        MathArrays.checkEqualLength(dArray, dArray2);
        int n = dArray.length;
        if (n == 1) {
            return dArray[0] * dArray2[0];
        }
        double[] dArray3 = new double[n];
        double d = 0.0;
        for (int i = 0; i < n; ++i) {
            double d2 = dArray[i];
            double d3 = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & 0xFFFFFFFFF8000000L);
            double d4 = d2 - d3;
            double d5 = dArray2[i];
            double d6 = Double.longBitsToDouble(Double.doubleToRawLongBits(d5) & 0xFFFFFFFFF8000000L);
            double d7 = d5 - d6;
            dArray3[i] = d2 * d5;
            double d8 = d4 * d7 - (dArray3[i] - d3 * d6 - d4 * d6 - d3 * d7);
            d += d8;
        }
        double d9 = dArray3[0];
        double d10 = dArray3[1];
        double d11 = d9 + d10;
        double d12 = d11 - d10;
        double d13 = d10 - (d11 - d12) + (d9 - d12);
        int n2 = n - 1;
        for (int i = 1; i < n2; ++i) {
            d10 = dArray3[i + 1];
            double d14 = d11 + d10;
            d12 = d14 - d10;
            d13 += d10 - (d14 - d12) + (d11 - d12);
            d11 = d14;
        }
        double d15 = d11 + (d + d13);
        if (Double.isNaN(d15)) {
            d15 = 0.0;
            for (int i = 0; i < n; ++i) {
                d15 += dArray[i] * dArray2[i];
            }
        }
        return d15;
    }

    public static double linearCombination(double d, double d2, double d3, double d4) {
        double d5;
        double d6;
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14 = d * d2;
        double d15 = d3 * d4;
        double d16 = d14 + d15;
        double d17 = Double.longBitsToDouble(Double.doubleToRawLongBits(d) & 0xFFFFFFFFF8000000L);
        double d18 = d - d17;
        double d19 = d18 * (d13 = d2 - (d12 = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & 0xFFFFFFFFF8000000L))) - (d14 - d17 * d12 - d18 * d12 - d17 * d13);
        double d20 = d16 + (d19 + (d11 = (d10 = d3 - (d9 = Double.longBitsToDouble(Double.doubleToRawLongBits(d3) & 0xFFFFFFFFF8000000L))) * (d8 = d4 - (d7 = Double.longBitsToDouble(Double.doubleToRawLongBits(d4) & 0xFFFFFFFFF8000000L))) - (d15 - d9 * d7 - d10 * d7 - d9 * d8)) + (d6 = d15 - (d16 - (d5 = d16 - d15)) + (d14 - d5)));
        if (Double.isNaN(d20)) {
            d20 = d * d2 + d3 * d4;
        }
        return d20;
    }

    public static double linearCombination(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7;
        double d8;
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14;
        double d15;
        double d16;
        double d17;
        double d18;
        double d19;
        double d20;
        double d21;
        double d22;
        double d23 = d * d2;
        double d24 = d3 * d4;
        double d25 = d23 + d24;
        double d26 = d5 * d6;
        double d27 = d25 + d26;
        double d28 = Double.longBitsToDouble(Double.doubleToRawLongBits(d) & 0xFFFFFFFFF8000000L);
        double d29 = d - d28;
        double d30 = d29 * (d22 = d2 - (d21 = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & 0xFFFFFFFFF8000000L))) - (d23 - d28 * d21 - d29 * d21 - d28 * d22);
        double d31 = d27 + (d30 + (d20 = (d19 = d3 - (d18 = Double.longBitsToDouble(Double.doubleToRawLongBits(d3) & 0xFFFFFFFFF8000000L))) * (d17 = d4 - (d16 = Double.longBitsToDouble(Double.doubleToRawLongBits(d4) & 0xFFFFFFFFF8000000L))) - (d24 - d18 * d16 - d19 * d16 - d18 * d17)) + (d15 = (d14 = d5 - (d13 = Double.longBitsToDouble(Double.doubleToRawLongBits(d5) & 0xFFFFFFFFF8000000L))) * (d12 = d6 - (d11 = Double.longBitsToDouble(Double.doubleToRawLongBits(d6) & 0xFFFFFFFFF8000000L))) - (d26 - d13 * d11 - d14 * d11 - d13 * d12)) + (d10 = d24 - (d25 - (d9 = d25 - d24)) + (d23 - d9)) + (d8 = d26 - (d27 - (d7 = d27 - d26)) + (d25 - d7)));
        if (Double.isNaN(d31)) {
            d31 = d * d2 + d3 * d4 + d5 * d6;
        }
        return d31;
    }

    public static double linearCombination(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8) {
        double d9;
        double d10;
        double d11;
        double d12;
        double d13;
        double d14;
        double d15;
        double d16;
        double d17;
        double d18;
        double d19;
        double d20;
        double d21;
        double d22;
        double d23;
        double d24;
        double d25;
        double d26;
        double d27;
        double d28;
        double d29;
        double d30;
        double d31;
        double d32 = d * d2;
        double d33 = d3 * d4;
        double d34 = d32 + d33;
        double d35 = d5 * d6;
        double d36 = d34 + d35;
        double d37 = d7 * d8;
        double d38 = d36 + d37;
        double d39 = Double.longBitsToDouble(Double.doubleToRawLongBits(d) & 0xFFFFFFFFF8000000L);
        double d40 = d - d39;
        double d41 = d40 * (d31 = d2 - (d30 = Double.longBitsToDouble(Double.doubleToRawLongBits(d2) & 0xFFFFFFFFF8000000L))) - (d32 - d39 * d30 - d40 * d30 - d39 * d31);
        double d42 = d38 + (d41 + (d29 = (d28 = d3 - (d27 = Double.longBitsToDouble(Double.doubleToRawLongBits(d3) & 0xFFFFFFFFF8000000L))) * (d26 = d4 - (d25 = Double.longBitsToDouble(Double.doubleToRawLongBits(d4) & 0xFFFFFFFFF8000000L))) - (d33 - d27 * d25 - d28 * d25 - d27 * d26)) + (d24 = (d23 = d5 - (d22 = Double.longBitsToDouble(Double.doubleToRawLongBits(d5) & 0xFFFFFFFFF8000000L))) * (d21 = d6 - (d20 = Double.longBitsToDouble(Double.doubleToRawLongBits(d6) & 0xFFFFFFFFF8000000L))) - (d35 - d22 * d20 - d23 * d20 - d22 * d21)) + (d19 = (d18 = d7 - (d17 = Double.longBitsToDouble(Double.doubleToRawLongBits(d7) & 0xFFFFFFFFF8000000L))) * (d16 = d8 - (d15 = Double.longBitsToDouble(Double.doubleToRawLongBits(d8) & 0xFFFFFFFFF8000000L))) - (d37 - d17 * d15 - d18 * d15 - d17 * d16)) + (d14 = d33 - (d34 - (d13 = d34 - d33)) + (d32 - d13)) + (d12 = d35 - (d36 - (d11 = d36 - d35)) + (d34 - d11)) + (d10 = d37 - (d38 - (d9 = d38 - d37)) + (d36 - d9)));
        if (Double.isNaN(d42)) {
            d42 = d * d2 + d3 * d4 + d5 * d6 + d7 * d8;
        }
        return d42;
    }

    public static boolean equals(float[] fArray, float[] fArray2) {
        if (fArray == null || fArray2 == null) {
            return !(fArray == null ^ fArray2 == null);
        }
        if (fArray.length != fArray2.length) {
            return false;
        }
        for (int i = 0; i < fArray.length; ++i) {
            if (Precision.equals(fArray[i], fArray2[i])) continue;
            return false;
        }
        return true;
    }

    public static boolean equalsIncludingNaN(float[] fArray, float[] fArray2) {
        if (fArray == null || fArray2 == null) {
            return !(fArray == null ^ fArray2 == null);
        }
        if (fArray.length != fArray2.length) {
            return false;
        }
        for (int i = 0; i < fArray.length; ++i) {
            if (Precision.equalsIncludingNaN(fArray[i], fArray2[i])) continue;
            return false;
        }
        return true;
    }

    public static boolean equals(double[] dArray, double[] dArray2) {
        if (dArray == null || dArray2 == null) {
            return !(dArray == null ^ dArray2 == null);
        }
        if (dArray.length != dArray2.length) {
            return false;
        }
        for (int i = 0; i < dArray.length; ++i) {
            if (Precision.equals(dArray[i], dArray2[i])) continue;
            return false;
        }
        return true;
    }

    public static boolean equalsIncludingNaN(double[] dArray, double[] dArray2) {
        if (dArray == null || dArray2 == null) {
            return !(dArray == null ^ dArray2 == null);
        }
        if (dArray.length != dArray2.length) {
            return false;
        }
        for (int i = 0; i < dArray.length; ++i) {
            if (Precision.equalsIncludingNaN(dArray[i], dArray2[i])) continue;
            return false;
        }
        return true;
    }

    public static double[] normalizeArray(double[] dArray, double d) {
        int n;
        if (Double.isInfinite(d)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_INFINITE, new Object[0]);
        }
        if (Double.isNaN(d)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_NAN, new Object[0]);
        }
        double d2 = 0.0;
        int n2 = dArray.length;
        double[] dArray2 = new double[n2];
        for (n = 0; n < n2; ++n) {
            if (Double.isInfinite(dArray[n])) {
                throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, dArray[n], n);
            }
            if (Double.isNaN(dArray[n])) continue;
            d2 += dArray[n];
        }
        if (d2 == 0.0) {
            throw new MathArithmeticException(LocalizedFormats.ARRAY_SUMS_TO_ZERO, new Object[0]);
        }
        for (n = 0; n < n2; ++n) {
            dArray2[n] = Double.isNaN(dArray[n]) ? Double.NaN : dArray[n] * d / d2;
        }
        return dArray2;
    }

    public static <T> T[] buildArray(Field<T> field, int n) {
        Object[] objectArray = (Object[])Array.newInstance(field.getRuntimeClass(), n);
        Arrays.fill(objectArray, field.getZero());
        return objectArray;
    }

    public static <T> T[][] buildArray(Field<T> field, int n, int n2) {
        Object[][] objectArray;
        if (n2 < 0) {
            T[] TArray = MathArrays.buildArray(field, 0);
            objectArray = (Object[][])Array.newInstance(TArray.getClass(), n);
        } else {
            objectArray = (Object[][])Array.newInstance(field.getRuntimeClass(), n, n2);
            for (int i = 0; i < n; ++i) {
                Arrays.fill(objectArray[i], field.getZero());
            }
        }
        return objectArray;
    }

    public static double[] convolve(double[] dArray, double[] dArray2) {
        MathUtils.checkNotNull(dArray);
        MathUtils.checkNotNull(dArray2);
        int n = dArray.length;
        int n2 = dArray2.length;
        if (n == 0 || n2 == 0) {
            throw new NoDataException();
        }
        int n3 = n + n2 - 1;
        double[] dArray3 = new double[n3];
        for (int i = 0; i < n3; ++i) {
            double d = 0.0;
            int n4 = FastMath.max(0, i + 1 - n);
            int n5 = i - n4;
            while (n4 < n2 && n5 >= 0) {
                d += dArray[n5--] * dArray2[n4++];
            }
            dArray3[i] = d;
        }
        return dArray3;
    }

    public static void shuffle(int[] nArray, int n, Position position) {
        MathArrays.shuffle(nArray, n, position, new Well19937c());
    }

    public static void shuffle(int[] nArray, int n, Position position, RandomGenerator randomGenerator) {
        switch (position) {
            case TAIL: {
                for (int i = nArray.length - 1; i >= n; --i) {
                    int n2 = i == n ? n : new UniformIntegerDistribution(randomGenerator, n, i).sample();
                    int n3 = nArray[n2];
                    nArray[n2] = nArray[i];
                    nArray[i] = n3;
                }
                break;
            }
            case HEAD: {
                for (int i = 0; i <= n; ++i) {
                    int n4 = i == n ? n : new UniformIntegerDistribution(randomGenerator, i, n).sample();
                    int n5 = nArray[n4];
                    nArray[n4] = nArray[i];
                    nArray[i] = n5;
                }
                break;
            }
            default: {
                throw new MathInternalError();
            }
        }
    }

    public static void shuffle(int[] nArray, RandomGenerator randomGenerator) {
        MathArrays.shuffle(nArray, 0, Position.TAIL, randomGenerator);
    }

    public static void shuffle(int[] nArray) {
        MathArrays.shuffle(nArray, new Well19937c());
    }

    public static int[] natural(int n) {
        return MathArrays.sequence(n, 0, 1);
    }

    public static int[] sequence(int n, int n2, int n3) {
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            nArray[i] = n2 + i * n3;
        }
        return nArray;
    }

    public static boolean verifyValues(double[] dArray, int n, int n2) {
        return MathArrays.verifyValues(dArray, n, n2, false);
    }

    public static boolean verifyValues(double[] dArray, int n, int n2, boolean bl) {
        if (dArray == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.START_POSITION, n);
        }
        if (n2 < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.LENGTH, n2);
        }
        if (n + n2 > dArray.length) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.SUBARRAY_ENDS_AFTER_ARRAY_END, (Number)(n + n2), dArray.length, true);
        }
        return n2 != 0 || bl;
    }

    public static boolean verifyValues(double[] dArray, double[] dArray2, int n, int n2) {
        return MathArrays.verifyValues(dArray, dArray2, n, n2, false);
    }

    public static boolean verifyValues(double[] dArray, double[] dArray2, int n, int n2, boolean bl) {
        if (dArray2 == null || dArray == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        MathArrays.checkEqualLength(dArray2, dArray);
        boolean bl2 = false;
        for (int i = n; i < n + n2; ++i) {
            double d = dArray2[i];
            if (Double.isNaN(d)) {
                throw new MathIllegalArgumentException(LocalizedFormats.NAN_ELEMENT_AT_INDEX, i);
            }
            if (Double.isInfinite(d)) {
                throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, d, i);
            }
            if (d < 0.0) {
                throw new MathIllegalArgumentException(LocalizedFormats.NEGATIVE_ELEMENT_AT_INDEX, i, d);
            }
            if (bl2 || !(d > 0.0)) continue;
            bl2 = true;
        }
        if (!bl2) {
            throw new MathIllegalArgumentException(LocalizedFormats.WEIGHT_AT_LEAST_ONE_NON_ZERO, new Object[0]);
        }
        return MathArrays.verifyValues(dArray, n, n2, bl);
    }

    public static double[] concatenate(double[] ... dArray) {
        int n = 0;
        for (double[] dArray2 : dArray) {
            n += dArray2.length;
        }
        int n2 = 0;
        int n3 = 0;
        double[] dArray3 = new double[n];
        for (int i = 0; i < dArray.length; ++i) {
            n3 = dArray[i].length;
            System.arraycopy(dArray[i], 0, dArray3, n2, n3);
            n2 += n3;
        }
        return dArray3;
    }

    public static double[] unique(double[] dArray) {
        int n;
        TreeSet<Double> treeSet = new TreeSet<Double>();
        for (n = 0; n < dArray.length; ++n) {
            treeSet.add(dArray[n]);
        }
        n = treeSet.size();
        double[] dArray2 = new double[n];
        Iterator iterator = treeSet.iterator();
        int n2 = 0;
        while (iterator.hasNext()) {
            dArray2[n - ++n2] = (Double)iterator.next();
        }
        return dArray2;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum Position {
        HEAD,
        TAIL;

    }

    private static class PairDoubleInteger {
        private final double key;
        private final int value;

        PairDoubleInteger(double d, int n) {
            this.key = d;
            this.value = n;
        }

        public double getKey() {
            return this.key;
        }

        public int getValue() {
            return this.value;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum OrderDirection {
        INCREASING,
        DECREASING;

    }

    public static interface Function {
        public double evaluate(double[] var1);

        public double evaluate(double[] var1, int var2, int var3);
    }
}

