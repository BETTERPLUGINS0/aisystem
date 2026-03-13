/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathArithmeticException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.ArithmeticUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.Combinations;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CombinatoricsUtils {
    static final long[] FACTORIALS = new long[]{1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L};
    static final AtomicReference<long[][]> STIRLING_S2 = new AtomicReference<Object>(null);

    private CombinatoricsUtils() {
    }

    public static long binomialCoefficient(int n, int n2) {
        CombinatoricsUtils.checkBinomial(n, n2);
        if (n == n2 || n2 == 0) {
            return 1L;
        }
        if (n2 == 1 || n2 == n - 1) {
            return n;
        }
        if (n2 > n / 2) {
            return CombinatoricsUtils.binomialCoefficient(n, n - n2);
        }
        long l = 1L;
        if (n <= 61) {
            int n3 = n - n2 + 1;
            for (int i = 1; i <= n2; ++i) {
                l = l * (long)n3 / (long)i;
                ++n3;
            }
        } else if (n <= 66) {
            int n4 = n - n2 + 1;
            for (int i = 1; i <= n2; ++i) {
                long l2 = ArithmeticUtils.gcd(n4, i);
                l = l / ((long)i / l2) * ((long)n4 / l2);
                ++n4;
            }
        } else {
            int n5 = n - n2 + 1;
            for (int i = 1; i <= n2; ++i) {
                long l3 = ArithmeticUtils.gcd(n5, i);
                l = ArithmeticUtils.mulAndCheck(l / ((long)i / l3), (long)n5 / l3);
                ++n5;
            }
        }
        return l;
    }

    public static double binomialCoefficientDouble(int n, int n2) {
        CombinatoricsUtils.checkBinomial(n, n2);
        if (n == n2 || n2 == 0) {
            return 1.0;
        }
        if (n2 == 1 || n2 == n - 1) {
            return n;
        }
        if (n2 > n / 2) {
            return CombinatoricsUtils.binomialCoefficientDouble(n, n - n2);
        }
        if (n < 67) {
            return CombinatoricsUtils.binomialCoefficient(n, n2);
        }
        double d = 1.0;
        for (int i = 1; i <= n2; ++i) {
            d *= (double)(n - n2 + i) / (double)i;
        }
        return FastMath.floor(d + 0.5);
    }

    public static double binomialCoefficientLog(int n, int n2) {
        int n3;
        CombinatoricsUtils.checkBinomial(n, n2);
        if (n == n2 || n2 == 0) {
            return 0.0;
        }
        if (n2 == 1 || n2 == n - 1) {
            return FastMath.log(n);
        }
        if (n < 67) {
            return FastMath.log(CombinatoricsUtils.binomialCoefficient(n, n2));
        }
        if (n < 1030) {
            return FastMath.log(CombinatoricsUtils.binomialCoefficientDouble(n, n2));
        }
        if (n2 > n / 2) {
            return CombinatoricsUtils.binomialCoefficientLog(n, n - n2);
        }
        double d = 0.0;
        for (n3 = n - n2 + 1; n3 <= n; ++n3) {
            d += FastMath.log(n3);
        }
        for (n3 = 2; n3 <= n2; ++n3) {
            d -= FastMath.log(n3);
        }
        return d;
    }

    public static long factorial(int n) {
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, n);
        }
        if (n > 20) {
            throw new MathArithmeticException();
        }
        return FACTORIALS[n];
    }

    public static double factorialDouble(int n) {
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, n);
        }
        if (n < 21) {
            return FACTORIALS[n];
        }
        return FastMath.floor(FastMath.exp(CombinatoricsUtils.factorialLog(n)) + 0.5);
    }

    public static double factorialLog(int n) {
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.FACTORIAL_NEGATIVE_PARAMETER, n);
        }
        if (n < 21) {
            return FastMath.log(FACTORIALS[n]);
        }
        double d = 0.0;
        for (int i = 2; i <= n; ++i) {
            d += FastMath.log(i);
        }
        return d;
    }

    public static long stirlingS2(int n, int n2) {
        if (n2 < 0) {
            throw new NotPositiveException(n2);
        }
        if (n2 > n) {
            throw new NumberIsTooLargeException(n2, (Number)n, true);
        }
        Object object = STIRLING_S2.get();
        if (object == null) {
            int n3 = 26;
            object = new long[26][];
            object[0] = new long[]{1L};
            for (int i = 1; i < ((long[][])object).length; ++i) {
                object[i] = new long[i + 1];
                object[i][0] = 0L;
                object[i][1] = 1L;
                object[i][i] = 1L;
                for (int j = 2; j < i; ++j) {
                    object[i][j] = (long)j * object[i - 1][j] + object[i - 1][j - 1];
                }
            }
            STIRLING_S2.compareAndSet((long[][])null, (long[][])object);
        }
        if (n < ((long[][])object).length) {
            return object[n][n2];
        }
        if (n2 == 0) {
            return 0L;
        }
        if (n2 == 1 || n2 == n) {
            return 1L;
        }
        if (n2 == 2) {
            return (1L << n - 1) - 1L;
        }
        if (n2 == n - 1) {
            return CombinatoricsUtils.binomialCoefficient(n, 2);
        }
        long l = 0L;
        long l2 = (n2 & 1) == 0 ? 1L : -1L;
        for (int i = 1; i <= n2; ++i) {
            if ((l += (l2 = -l2) * CombinatoricsUtils.binomialCoefficient(n2, i) * (long)ArithmeticUtils.pow(i, n)) >= 0L) continue;
            throw new MathArithmeticException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN, n, 0, ((long[][])object).length - 1);
        }
        return l / CombinatoricsUtils.factorial(n2);
    }

    public static Iterator<int[]> combinationsIterator(int n, int n2) {
        return new Combinations(n, n2).iterator();
    }

    public static void checkBinomial(int n, int n2) {
        if (n < n2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.BINOMIAL_INVALID_PARAMETERS_ORDER, (Number)n2, n, true);
        }
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.BINOMIAL_NEGATIVE_PARAMETER, n);
        }
    }
}

