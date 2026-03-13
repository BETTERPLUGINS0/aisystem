/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.util.Arrays;
import net.advancedplugins.as.libs.apache.commons.math3.RealFieldElement;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathArithmeticException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotFiniteNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NullArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MathUtils {
    public static final double TWO_PI = Math.PI * 2;
    public static final double PI_SQUARED = Math.PI * Math.PI;

    private MathUtils() {
    }

    public static int hash(double d) {
        return new Double(d).hashCode();
    }

    public static boolean equals(double d, double d2) {
        return new Double(d).equals(new Double(d2));
    }

    public static int hash(double[] dArray) {
        return Arrays.hashCode(dArray);
    }

    public static double normalizeAngle(double d, double d2) {
        return d - Math.PI * 2 * FastMath.floor((d + Math.PI - d2) / (Math.PI * 2));
    }

    public static <T extends RealFieldElement<T>> T max(T t, T t2) {
        return t.subtract(t2).getReal() >= 0.0 ? t : t2;
    }

    public static <T extends RealFieldElement<T>> T min(T t, T t2) {
        return t.subtract(t2).getReal() >= 0.0 ? t2 : t;
    }

    public static double reduce(double d, double d2, double d3) {
        double d4 = FastMath.abs(d2);
        return d - d4 * FastMath.floor((d - d3) / d4) - d3;
    }

    public static byte copySign(byte by, byte by2) {
        if (by >= 0 && by2 >= 0 || by < 0 && by2 < 0) {
            return by;
        }
        if (by2 >= 0 && by == -128) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -by;
    }

    public static short copySign(short s, short s2) {
        if (s >= 0 && s2 >= 0 || s < 0 && s2 < 0) {
            return s;
        }
        if (s2 >= 0 && s == Short.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -s;
    }

    public static int copySign(int n, int n2) {
        if (n >= 0 && n2 >= 0 || n < 0 && n2 < 0) {
            return n;
        }
        if (n2 >= 0 && n == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -n;
    }

    public static long copySign(long l, long l2) {
        if (l >= 0L && l2 >= 0L || l < 0L && l2 < 0L) {
            return l;
        }
        if (l2 >= 0L && l == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW, new Object[0]);
        }
        return -l;
    }

    public static void checkFinite(double d) {
        if (Double.isInfinite(d) || Double.isNaN(d)) {
            throw new NotFiniteNumberException(d, new Object[0]);
        }
    }

    public static void checkFinite(double[] dArray) {
        for (int i = 0; i < dArray.length; ++i) {
            double d = dArray[i];
            if (!Double.isInfinite(d) && !Double.isNaN(d)) continue;
            throw new NotFiniteNumberException((Localizable)LocalizedFormats.ARRAY_ELEMENT, d, i);
        }
    }

    public static void checkNotNull(Object object, Localizable localizable, Object ... objectArray) {
        if (object == null) {
            throw new NullArgumentException(localizable, objectArray);
        }
    }

    public static void checkNotNull(Object object) {
        if (object == null) {
            throw new NullArgumentException();
        }
    }
}

