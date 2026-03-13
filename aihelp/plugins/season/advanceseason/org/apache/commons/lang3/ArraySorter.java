/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.util.Arrays;
import java.util.Comparator;

public class ArraySorter {
    public static byte[] sort(byte[] byArray) {
        if (byArray != null) {
            Arrays.sort(byArray);
        }
        return byArray;
    }

    public static char[] sort(char[] cArray) {
        if (cArray != null) {
            Arrays.sort(cArray);
        }
        return cArray;
    }

    public static double[] sort(double[] dArray) {
        if (dArray != null) {
            Arrays.sort(dArray);
        }
        return dArray;
    }

    public static float[] sort(float[] fArray) {
        if (fArray != null) {
            Arrays.sort(fArray);
        }
        return fArray;
    }

    public static int[] sort(int[] nArray) {
        if (nArray != null) {
            Arrays.sort(nArray);
        }
        return nArray;
    }

    public static long[] sort(long[] lArray) {
        if (lArray != null) {
            Arrays.sort(lArray);
        }
        return lArray;
    }

    public static short[] sort(short[] sArray) {
        if (sArray != null) {
            Arrays.sort(sArray);
        }
        return sArray;
    }

    public static <T> T[] sort(T[] TArray) {
        if (TArray != null) {
            Arrays.sort(TArray);
        }
        return TArray;
    }

    public static <T> T[] sort(T[] TArray, Comparator<? super T> comparator) {
        if (TArray != null) {
            Arrays.sort(TArray, comparator);
        }
        return TArray;
    }

    @Deprecated
    public ArraySorter() {
    }
}

