/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.math.BigDecimal;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathArithmeticException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class Precision {
    public static final double EPSILON;
    public static final double SAFE_MIN;
    private static final long EXPONENT_OFFSET = 1023L;
    private static final long SGN_MASK = Long.MIN_VALUE;
    private static final int SGN_MASK_FLOAT = Integer.MIN_VALUE;
    private static final double POSITIVE_ZERO = 0.0;
    private static final long POSITIVE_ZERO_DOUBLE_BITS;
    private static final long NEGATIVE_ZERO_DOUBLE_BITS;
    private static final int POSITIVE_ZERO_FLOAT_BITS;
    private static final int NEGATIVE_ZERO_FLOAT_BITS;

    private Precision() {
    }

    public static int compareTo(double d, double d2, double d3) {
        if (Precision.equals(d, d2, d3)) {
            return 0;
        }
        if (d < d2) {
            return -1;
        }
        return 1;
    }

    public static int compareTo(double d, double d2, int n) {
        if (Precision.equals(d, d2, n)) {
            return 0;
        }
        if (d < d2) {
            return -1;
        }
        return 1;
    }

    public static boolean equals(float f, float f2) {
        return Precision.equals(f, f2, 1);
    }

    public static boolean equalsIncludingNaN(float f, float f2) {
        return f != f || f2 != f2 ? !(f != f ^ f2 != f2) : Precision.equals(f, f2, 1);
    }

    public static boolean equals(float f, float f2, float f3) {
        return Precision.equals(f, f2, 1) || FastMath.abs(f2 - f) <= f3;
    }

    public static boolean equalsIncludingNaN(float f, float f2, float f3) {
        return Precision.equalsIncludingNaN(f, f2) || FastMath.abs(f2 - f) <= f3;
    }

    public static boolean equals(float f, float f2, int n) {
        boolean bl;
        int n2;
        int n3 = Float.floatToRawIntBits(f);
        if (((n3 ^ (n2 = Float.floatToRawIntBits(f2))) & Integer.MIN_VALUE) == 0) {
            bl = FastMath.abs(n3 - n2) <= n;
        } else {
            int n4;
            int n5;
            if (n3 < n2) {
                n5 = n2 - POSITIVE_ZERO_FLOAT_BITS;
                n4 = n3 - NEGATIVE_ZERO_FLOAT_BITS;
            } else {
                n5 = n3 - POSITIVE_ZERO_FLOAT_BITS;
                n4 = n2 - NEGATIVE_ZERO_FLOAT_BITS;
            }
            bl = n5 > n ? false : n4 <= n - n5;
        }
        return bl && !Float.isNaN(f) && !Float.isNaN(f2);
    }

    public static boolean equalsIncludingNaN(float f, float f2, int n) {
        return f != f || f2 != f2 ? !(f != f ^ f2 != f2) : Precision.equals(f, f2, n);
    }

    public static boolean equals(double d, double d2) {
        return Precision.equals(d, d2, 1);
    }

    public static boolean equalsIncludingNaN(double d, double d2) {
        return d != d || d2 != d2 ? !(d != d ^ d2 != d2) : Precision.equals(d, d2, 1);
    }

    public static boolean equals(double d, double d2, double d3) {
        return Precision.equals(d, d2, 1) || FastMath.abs(d2 - d) <= d3;
    }

    public static boolean equalsWithRelativeTolerance(double d, double d2, double d3) {
        if (Precision.equals(d, d2, 1)) {
            return true;
        }
        double d4 = FastMath.max(FastMath.abs(d), FastMath.abs(d2));
        double d5 = FastMath.abs((d - d2) / d4);
        return d5 <= d3;
    }

    public static boolean equalsIncludingNaN(double d, double d2, double d3) {
        return Precision.equalsIncludingNaN(d, d2) || FastMath.abs(d2 - d) <= d3;
    }

    public static boolean equals(double d, double d2, int n) {
        boolean bl;
        long l;
        long l2 = Double.doubleToRawLongBits(d);
        if (((l2 ^ (l = Double.doubleToRawLongBits(d2))) & Long.MIN_VALUE) == 0L) {
            bl = FastMath.abs(l2 - l) <= (long)n;
        } else {
            long l3;
            long l4;
            if (l2 < l) {
                l4 = l - POSITIVE_ZERO_DOUBLE_BITS;
                l3 = l2 - NEGATIVE_ZERO_DOUBLE_BITS;
            } else {
                l4 = l2 - POSITIVE_ZERO_DOUBLE_BITS;
                l3 = l - NEGATIVE_ZERO_DOUBLE_BITS;
            }
            bl = l4 > (long)n ? false : l3 <= (long)n - l4;
        }
        return bl && !Double.isNaN(d) && !Double.isNaN(d2);
    }

    public static boolean equalsIncludingNaN(double d, double d2, int n) {
        return d != d || d2 != d2 ? !(d != d ^ d2 != d2) : Precision.equals(d, d2, n);
    }

    public static double round(double d, int n) {
        return Precision.round(d, n, 4);
    }

    public static double round(double d, int n, int n2) {
        try {
            double d2 = new BigDecimal(Double.toString(d)).setScale(n, n2).doubleValue();
            return d2 == 0.0 ? 0.0 * d : d2;
        } catch (NumberFormatException numberFormatException) {
            if (Double.isInfinite(d)) {
                return d;
            }
            return Double.NaN;
        }
    }

    public static float round(float f, int n) {
        return Precision.round(f, n, 4);
    }

    public static float round(float f, int n, int n2) {
        float f2 = FastMath.copySign(1.0f, f);
        float f3 = (float)FastMath.pow(10.0, n) * f2;
        return (float)Precision.roundUnscaled(f * f3, f2, n2) / f3;
    }

    private static double roundUnscaled(double d, double d2, int n) {
        switch (n) {
            case 2: {
                if (d2 == -1.0) {
                    d = FastMath.floor(FastMath.nextAfter(d, Double.NEGATIVE_INFINITY));
                    break;
                }
                d = FastMath.ceil(FastMath.nextAfter(d, Double.POSITIVE_INFINITY));
                break;
            }
            case 1: {
                d = FastMath.floor(FastMath.nextAfter(d, Double.NEGATIVE_INFINITY));
                break;
            }
            case 3: {
                if (d2 == -1.0) {
                    d = FastMath.ceil(FastMath.nextAfter(d, Double.POSITIVE_INFINITY));
                    break;
                }
                d = FastMath.floor(FastMath.nextAfter(d, Double.NEGATIVE_INFINITY));
                break;
            }
            case 5: {
                d = FastMath.nextAfter(d, Double.NEGATIVE_INFINITY);
                double d3 = d - FastMath.floor(d);
                if (d3 > 0.5) {
                    d = FastMath.ceil(d);
                    break;
                }
                d = FastMath.floor(d);
                break;
            }
            case 6: {
                double d4 = d - FastMath.floor(d);
                if (d4 > 0.5) {
                    d = FastMath.ceil(d);
                    break;
                }
                if (d4 < 0.5) {
                    d = FastMath.floor(d);
                    break;
                }
                if (FastMath.floor(d) / 2.0 == FastMath.floor(FastMath.floor(d) / 2.0)) {
                    d = FastMath.floor(d);
                    break;
                }
                d = FastMath.ceil(d);
                break;
            }
            case 4: {
                d = FastMath.nextAfter(d, Double.POSITIVE_INFINITY);
                double d5 = d - FastMath.floor(d);
                if (d5 >= 0.5) {
                    d = FastMath.ceil(d);
                    break;
                }
                d = FastMath.floor(d);
                break;
            }
            case 7: {
                if (d == FastMath.floor(d)) break;
                throw new MathArithmeticException();
            }
            case 0: {
                if (d == FastMath.floor(d)) break;
                d = FastMath.ceil(FastMath.nextAfter(d, Double.POSITIVE_INFINITY));
                break;
            }
            default: {
                throw new MathIllegalArgumentException(LocalizedFormats.INVALID_ROUNDING_METHOD, n, "ROUND_CEILING", 2, "ROUND_DOWN", 1, "ROUND_FLOOR", 3, "ROUND_HALF_DOWN", 5, "ROUND_HALF_EVEN", 6, "ROUND_HALF_UP", 4, "ROUND_UNNECESSARY", 7, "ROUND_UP", 0);
            }
        }
        return d;
    }

    public static double representableDelta(double d, double d2) {
        return d + d2 - d;
    }

    static {
        POSITIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(0.0);
        NEGATIVE_ZERO_DOUBLE_BITS = Double.doubleToRawLongBits(-0.0);
        POSITIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(0.0f);
        NEGATIVE_ZERO_FLOAT_BITS = Float.floatToRawIntBits(-0.0f);
        EPSILON = Double.longBitsToDouble(4368491638549381120L);
        SAFE_MIN = Double.longBitsToDouble(0x10000000000000L);
    }
}

