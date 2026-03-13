/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    public static int clamp(int n, int n2, int n3) {
        return Math.max(n2, Math.min(n3, n));
    }

    public static long clamp(long l, long l2, long l3) {
        if (l2 > l3) {
            return l3;
        }
        return Math.max(l2, Math.min(l3, l));
    }

    public static double clamp(double d, double d2, double d3) {
        if (d2 > d3) {
            return d3;
        }
        return Math.max(d2, Math.min(d3, d));
    }

    public static int randomBetween(int n, int n2) {
        if (n > n2) {
            int n3 = n;
            n = n2;
            n2 = n3;
        }
        if (n == n2) {
            return n;
        }
        return ThreadLocalRandom.current().nextInt(n2 - n) + n;
    }

    public static int getClosestInt(int n, List<Integer> list) {
        int n2 = Integer.MAX_VALUE;
        int n3 = n;
        for (int n4 : list) {
            int n5 = Math.abs(n4 - n);
            if (n5 >= n2) continue;
            n2 = n5;
            n3 = n4;
        }
        return n3;
    }

    public static boolean isByte(String string) {
        try {
            Byte.parseByte(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isShort(String string) {
        try {
            Short.parseShort(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isLong(String string) {
        try {
            Long.parseLong(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isFloat(String string) {
        try {
            Float.parseFloat(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }

    public static boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}

