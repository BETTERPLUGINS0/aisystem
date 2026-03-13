/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.util.Arrays;

public final class ArrayFill {
    public static byte[] fill(byte[] byArray, byte by) {
        if (byArray != null) {
            Arrays.fill(byArray, by);
        }
        return byArray;
    }

    public static char[] fill(char[] cArray, char c2) {
        if (cArray != null) {
            Arrays.fill(cArray, c2);
        }
        return cArray;
    }

    public static double[] fill(double[] dArray, double d) {
        if (dArray != null) {
            Arrays.fill(dArray, d);
        }
        return dArray;
    }

    public static float[] fill(float[] fArray, float f) {
        if (fArray != null) {
            Arrays.fill(fArray, f);
        }
        return fArray;
    }

    public static int[] fill(int[] nArray, int n) {
        if (nArray != null) {
            Arrays.fill(nArray, n);
        }
        return nArray;
    }

    public static long[] fill(long[] lArray, long l) {
        if (lArray != null) {
            Arrays.fill(lArray, l);
        }
        return lArray;
    }

    public static short[] fill(short[] sArray, short s) {
        if (sArray != null) {
            Arrays.fill(sArray, s);
        }
        return sArray;
    }

    public static <T> T[] fill(T[] TArray, T t) {
        if (TArray != null) {
            Arrays.fill(TArray, t);
        }
        return TArray;
    }

    private ArrayFill() {
    }
}

