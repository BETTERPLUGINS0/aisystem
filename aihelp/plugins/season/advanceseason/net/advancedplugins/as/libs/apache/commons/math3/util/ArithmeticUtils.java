/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.math.BigInteger;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathArithmeticException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.CombinatoricsUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public final class ArithmeticUtils {
    private ArithmeticUtils() {
    }

    public static int addAndCheck(int n, int n2) {
        long l = (long)n + (long)n2;
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, n, n2);
        }
        return (int)l;
    }

    public static long addAndCheck(long l, long l2) {
        return ArithmeticUtils.addAndCheck(l, l2, LocalizedFormats.OVERFLOW_IN_ADDITION);
    }

    @Deprecated
    public static long binomialCoefficient(int n, int n2) {
        return CombinatoricsUtils.binomialCoefficient(n, n2);
    }

    @Deprecated
    public static double binomialCoefficientDouble(int n, int n2) {
        return CombinatoricsUtils.binomialCoefficientDouble(n, n2);
    }

    @Deprecated
    public static double binomialCoefficientLog(int n, int n2) {
        return CombinatoricsUtils.binomialCoefficientLog(n, n2);
    }

    @Deprecated
    public static long factorial(int n) {
        return CombinatoricsUtils.factorial(n);
    }

    @Deprecated
    public static double factorialDouble(int n) {
        return CombinatoricsUtils.factorialDouble(n);
    }

    @Deprecated
    public static double factorialLog(int n) {
        return CombinatoricsUtils.factorialLog(n);
    }

    public static int gcd(int n, int n2) {
        int n3 = n;
        int n4 = n2;
        if (n3 == 0 || n4 == 0) {
            if (n3 == Integer.MIN_VALUE || n4 == Integer.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, n, n2);
            }
            return FastMath.abs(n3 + n4);
        }
        long l = n3;
        long l2 = n4;
        boolean bl = false;
        if (n3 < 0) {
            if (Integer.MIN_VALUE == n3) {
                bl = true;
            } else {
                n3 = -n3;
            }
            l = -l;
        }
        if (n4 < 0) {
            if (Integer.MIN_VALUE == n4) {
                bl = true;
            } else {
                n4 = -n4;
            }
            l2 = -l2;
        }
        if (bl) {
            if (l == l2) {
                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, n, n2);
            }
            long l3 = l2;
            l2 = l;
            if ((l = l3 % l) == 0L) {
                if (l2 > Integer.MAX_VALUE) {
                    throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_32_BITS, n, n2);
                }
                return (int)l2;
            }
            l3 = l2;
            n4 = (int)l;
            n3 = (int)(l3 % l);
        }
        return ArithmeticUtils.gcdPositive(n3, n4);
    }

    private static int gcdPositive(int n, int n2) {
        if (n == 0) {
            return n2;
        }
        if (n2 == 0) {
            return n;
        }
        int n3 = Integer.numberOfTrailingZeros(n);
        n >>= n3;
        int n4 = Integer.numberOfTrailingZeros(n2);
        n2 >>= n4;
        int n5 = FastMath.min(n3, n4);
        while (n != n2) {
            int n6 = n - n2;
            n2 = Math.min(n, n2);
            n = Math.abs(n6);
            n >>= Integer.numberOfTrailingZeros(n);
        }
        return n << n5;
    }

    public static long gcd(long l, long l2) {
        long l3;
        int n;
        long l4 = l;
        long l5 = l2;
        if (l4 == 0L || l5 == 0L) {
            if (l4 == Long.MIN_VALUE || l5 == Long.MIN_VALUE) {
                throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_64_BITS, l, l2);
            }
            return FastMath.abs(l4) + FastMath.abs(l5);
        }
        if (l4 > 0L) {
            l4 = -l4;
        }
        if (l5 > 0L) {
            l5 = -l5;
        }
        for (n = 0; (l4 & 1L) == 0L && (l5 & 1L) == 0L && n < 63; ++n) {
            l4 /= 2L;
            l5 /= 2L;
        }
        if (n == 63) {
            throw new MathArithmeticException(LocalizedFormats.GCD_OVERFLOW_64_BITS, l, l2);
        }
        long l6 = l3 = (l4 & 1L) == 1L ? l5 : -(l4 / 2L);
        while (true) {
            if ((l3 & 1L) == 0L) {
                l3 /= 2L;
                continue;
            }
            if (l3 > 0L) {
                l4 = -l3;
            } else {
                l5 = l3;
            }
            if ((l3 = (l5 - l4) / 2L) == 0L) break;
        }
        return -l4 * (1L << n);
    }

    public static int lcm(int n, int n2) {
        if (n == 0 || n2 == 0) {
            return 0;
        }
        int n3 = FastMath.abs(ArithmeticUtils.mulAndCheck(n / ArithmeticUtils.gcd(n, n2), n2));
        if (n3 == Integer.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_32_BITS, n, n2);
        }
        return n3;
    }

    public static long lcm(long l, long l2) {
        if (l == 0L || l2 == 0L) {
            return 0L;
        }
        long l3 = FastMath.abs(ArithmeticUtils.mulAndCheck(l / ArithmeticUtils.gcd(l, l2), l2));
        if (l3 == Long.MIN_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.LCM_OVERFLOW_64_BITS, l, l2);
        }
        return l3;
    }

    public static int mulAndCheck(int n, int n2) {
        long l = (long)n * (long)n2;
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new MathArithmeticException();
        }
        return (int)l;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static long mulAndCheck(long l, long l2) {
        if (l > l2) {
            return ArithmeticUtils.mulAndCheck(l2, l);
        }
        if (l < 0L) {
            if (l2 < 0L) {
                if (l < Long.MAX_VALUE / l2) throw new MathArithmeticException();
                return l * l2;
            }
            if (l2 <= 0L) return 0L;
            if (Long.MIN_VALUE / l2 > l) throw new MathArithmeticException();
            return l * l2;
        }
        if (l <= 0L) return 0L;
        if (l > Long.MAX_VALUE / l2) throw new MathArithmeticException();
        return l * l2;
    }

    public static int subAndCheck(int n, int n2) {
        long l = (long)n - (long)n2;
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_SUBTRACTION, n, n2);
        }
        return (int)l;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static long subAndCheck(long l, long l2) {
        if (l2 != Long.MIN_VALUE) return ArithmeticUtils.addAndCheck(l, -l2, LocalizedFormats.OVERFLOW_IN_ADDITION);
        if (l >= 0L) throw new MathArithmeticException(LocalizedFormats.OVERFLOW_IN_ADDITION, l, -l2);
        return l - l2;
    }

    public static int pow(int n, int n2) {
        if (n2 < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, n2);
        }
        try {
            int n3 = n2;
            int n4 = 1;
            int n5 = n;
            while (true) {
                if ((n3 & 1) != 0) {
                    n4 = ArithmeticUtils.mulAndCheck(n4, n5);
                }
                if ((n3 >>= 1) == 0) break;
                n5 = ArithmeticUtils.mulAndCheck(n5, n5);
            }
            return n4;
        } catch (MathArithmeticException mathArithmeticException) {
            mathArithmeticException.getContext().addMessage(LocalizedFormats.OVERFLOW, new Object[0]);
            mathArithmeticException.getContext().addMessage(LocalizedFormats.BASE, n);
            mathArithmeticException.getContext().addMessage(LocalizedFormats.EXPONENT, n2);
            throw mathArithmeticException;
        }
    }

    @Deprecated
    public static int pow(int n, long l) {
        if (l < 0L) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, l);
        }
        int n2 = 1;
        int n3 = n;
        while (l != 0L) {
            if ((l & 1L) != 0L) {
                n2 *= n3;
            }
            n3 *= n3;
            l >>= 1;
        }
        return n2;
    }

    public static long pow(long l, int n) {
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, n);
        }
        try {
            int n2 = n;
            long l2 = 1L;
            long l3 = l;
            while (true) {
                if ((n2 & 1) != 0) {
                    l2 = ArithmeticUtils.mulAndCheck(l2, l3);
                }
                if ((n2 >>= 1) == 0) break;
                l3 = ArithmeticUtils.mulAndCheck(l3, l3);
            }
            return l2;
        } catch (MathArithmeticException mathArithmeticException) {
            mathArithmeticException.getContext().addMessage(LocalizedFormats.OVERFLOW, new Object[0]);
            mathArithmeticException.getContext().addMessage(LocalizedFormats.BASE, l);
            mathArithmeticException.getContext().addMessage(LocalizedFormats.EXPONENT, n);
            throw mathArithmeticException;
        }
    }

    @Deprecated
    public static long pow(long l, long l2) {
        if (l2 < 0L) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, l2);
        }
        long l3 = 1L;
        long l4 = l;
        while (l2 != 0L) {
            if ((l2 & 1L) != 0L) {
                l3 *= l4;
            }
            l4 *= l4;
            l2 >>= 1;
        }
        return l3;
    }

    public static BigInteger pow(BigInteger bigInteger, int n) {
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, n);
        }
        return bigInteger.pow(n);
    }

    public static BigInteger pow(BigInteger bigInteger, long l) {
        if (l < 0L) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, l);
        }
        BigInteger bigInteger2 = BigInteger.ONE;
        BigInteger bigInteger3 = bigInteger;
        while (l != 0L) {
            if ((l & 1L) != 0L) {
                bigInteger2 = bigInteger2.multiply(bigInteger3);
            }
            bigInteger3 = bigInteger3.multiply(bigInteger3);
            l >>= 1;
        }
        return bigInteger2;
    }

    public static BigInteger pow(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger2.compareTo(BigInteger.ZERO) < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.EXPONENT, bigInteger2);
        }
        BigInteger bigInteger3 = BigInteger.ONE;
        BigInteger bigInteger4 = bigInteger;
        while (!BigInteger.ZERO.equals(bigInteger2)) {
            if (bigInteger2.testBit(0)) {
                bigInteger3 = bigInteger3.multiply(bigInteger4);
            }
            bigInteger4 = bigInteger4.multiply(bigInteger4);
            bigInteger2 = bigInteger2.shiftRight(1);
        }
        return bigInteger3;
    }

    @Deprecated
    public static long stirlingS2(int n, int n2) {
        return CombinatoricsUtils.stirlingS2(n, n2);
    }

    private static long addAndCheck(long l, long l2, Localizable localizable) {
        long l3;
        if (!((l ^ l2) < 0L | (l ^ (l3 = l + l2)) >= 0L)) {
            throw new MathArithmeticException(localizable, l, l2);
        }
        return l3;
    }

    public static boolean isPowerOfTwo(long l) {
        return l > 0L && (l & l - 1L) == 0L;
    }
}

