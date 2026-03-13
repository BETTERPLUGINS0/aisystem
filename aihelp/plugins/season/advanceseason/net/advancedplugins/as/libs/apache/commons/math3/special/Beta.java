/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.special;

import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooSmallException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.ContinuedFraction;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class Beta {
    private static final double DEFAULT_EPSILON = 1.0E-14;
    private static final double HALF_LOG_TWO_PI = 0.9189385332046727;
    private static final double[] DELTA = new double[]{0.08333333333333333, -2.777777777777778E-5, 7.936507936507937E-8, -5.952380952380953E-10, 8.417508417508329E-12, -1.917526917518546E-13, 6.410256405103255E-15, -2.955065141253382E-16, 1.7964371635940225E-17, -1.3922896466162779E-18, 1.338028550140209E-19, -1.542460098679661E-20, 1.9770199298095743E-21, -2.3406566479399704E-22, 1.713480149663986E-23};

    private Beta() {
    }

    public static double regularizedBeta(double d, double d2, double d3) {
        return Beta.regularizedBeta(d, d2, d3, 1.0E-14, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double d, double d2, double d3, double d4) {
        return Beta.regularizedBeta(d, d2, d3, d4, Integer.MAX_VALUE);
    }

    public static double regularizedBeta(double d, double d2, double d3, int n) {
        return Beta.regularizedBeta(d, d2, d3, 1.0E-14, n);
    }

    public static double regularizedBeta(double d, final double d2, final double d3, double d4, int n) {
        double d5;
        if (Double.isNaN(d) || Double.isNaN(d2) || Double.isNaN(d3) || d < 0.0 || d > 1.0 || d2 <= 0.0 || d3 <= 0.0) {
            d5 = Double.NaN;
        } else if (d > (d2 + 1.0) / (2.0 + d3 + d2) && 1.0 - d <= (d3 + 1.0) / (2.0 + d3 + d2)) {
            d5 = 1.0 - Beta.regularizedBeta(1.0 - d, d3, d2, d4, n);
        } else {
            ContinuedFraction continuedFraction = new ContinuedFraction(){

                protected double getB(int n, double d) {
                    double d22;
                    if (n % 2 == 0) {
                        double d32 = (double)n / 2.0;
                        d22 = d32 * (d3 - d32) * d / ((d2 + 2.0 * d32 - 1.0) * (d2 + 2.0 * d32));
                    } else {
                        double d4 = ((double)n - 1.0) / 2.0;
                        d22 = -((d2 + d4) * (d2 + d3 + d4) * d) / ((d2 + 2.0 * d4) * (d2 + 2.0 * d4 + 1.0));
                    }
                    return d22;
                }

                protected double getA(int n, double d) {
                    return 1.0;
                }
            };
            d5 = FastMath.exp(d2 * FastMath.log(d) + d3 * FastMath.log1p(-d) - FastMath.log(d2) - Beta.logBeta(d2, d3)) * 1.0 / continuedFraction.evaluate(d, d4, n);
        }
        return d5;
    }

    @Deprecated
    public static double logBeta(double d, double d2, double d3, int n) {
        return Beta.logBeta(d, d2);
    }

    private static double logGammaSum(double d, double d2) {
        if (d < 1.0 || d > 2.0) {
            throw new OutOfRangeException(d, (Number)1.0, 2.0);
        }
        if (d2 < 1.0 || d2 > 2.0) {
            throw new OutOfRangeException(d2, (Number)1.0, 2.0);
        }
        double d3 = d - 1.0 + (d2 - 1.0);
        if (d3 <= 0.5) {
            return Gamma.logGamma1p(1.0 + d3);
        }
        if (d3 <= 1.5) {
            return Gamma.logGamma1p(d3) + FastMath.log1p(d3);
        }
        return Gamma.logGamma1p(d3 - 1.0) + FastMath.log(d3 * (1.0 + d3));
    }

    private static double logGammaMinusLogGammaSum(double d, double d2) {
        double d3;
        double d4;
        if (d < 0.0) {
            throw new NumberIsTooSmallException(d, (Number)0.0, true);
        }
        if (d2 < 10.0) {
            throw new NumberIsTooSmallException(d2, (Number)10.0, true);
        }
        if (d <= d2) {
            d4 = d2 + (d - 0.5);
            d3 = Beta.deltaMinusDeltaSum(d, d2);
        } else {
            d4 = d + (d2 - 0.5);
            d3 = Beta.deltaMinusDeltaSum(d2, d);
        }
        double d5 = d4 * FastMath.log1p(d / d2);
        double d6 = d * (FastMath.log(d2) - 1.0);
        return d5 <= d6 ? d3 - d5 - d6 : d3 - d6 - d5;
    }

    private static double deltaMinusDeltaSum(double d, double d2) {
        if (d < 0.0 || d > d2) {
            throw new OutOfRangeException(d, (Number)0, d2);
        }
        if (d2 < 10.0) {
            throw new NumberIsTooSmallException(d2, (Number)10, true);
        }
        double d3 = d / d2;
        double d4 = d3 / (1.0 + d3);
        double d5 = 1.0 / (1.0 + d3);
        double d6 = d5 * d5;
        double[] dArray = new double[DELTA.length];
        dArray[0] = 1.0;
        for (int i = 1; i < dArray.length; ++i) {
            dArray[i] = 1.0 + (d5 + d6 * dArray[i - 1]);
        }
        double d7 = 10.0 / d2;
        double d8 = d7 * d7;
        double d9 = DELTA[DELTA.length - 1] * dArray[dArray.length - 1];
        for (int i = DELTA.length - 2; i >= 0; --i) {
            d9 = d8 * d9 + DELTA[i] * dArray[i];
        }
        return d9 * d4 / d2;
    }

    private static double sumDeltaMinusDeltaSum(double d, double d2) {
        if (d < 10.0) {
            throw new NumberIsTooSmallException(d, (Number)10.0, true);
        }
        if (d2 < 10.0) {
            throw new NumberIsTooSmallException(d2, (Number)10.0, true);
        }
        double d3 = FastMath.min(d, d2);
        double d4 = FastMath.max(d, d2);
        double d5 = 10.0 / d3;
        double d6 = d5 * d5;
        double d7 = DELTA[DELTA.length - 1];
        for (int i = DELTA.length - 2; i >= 0; --i) {
            d7 = d6 * d7 + DELTA[i];
        }
        return d7 / d3 + Beta.deltaMinusDeltaSum(d3, d4);
    }

    public static double logBeta(double d, double d2) {
        if (Double.isNaN(d) || Double.isNaN(d2) || d <= 0.0 || d2 <= 0.0) {
            return Double.NaN;
        }
        double d3 = FastMath.min(d, d2);
        double d4 = FastMath.max(d, d2);
        if (d3 >= 10.0) {
            double d5;
            double d6 = Beta.sumDeltaMinusDeltaSum(d3, d4);
            double d7 = d3 / d4;
            double d8 = d7 / (1.0 + d7);
            double d9 = -(d3 - 0.5) * FastMath.log(d8);
            if (d9 <= (d5 = d4 * FastMath.log1p(d7))) {
                return -0.5 * FastMath.log(d4) + 0.9189385332046727 + d6 - d9 - d5;
            }
            return -0.5 * FastMath.log(d4) + 0.9189385332046727 + d6 - d5 - d9;
        }
        if (d3 > 2.0) {
            double d10;
            if (d4 > 1000.0) {
                int n = (int)FastMath.floor(d3 - 1.0);
                double d11 = 1.0;
                double d12 = d3;
                for (int i = 0; i < n; ++i) {
                    d11 *= (d12 -= 1.0) / (1.0 + d12 / d4);
                }
                return FastMath.log(d11) - (double)n * FastMath.log(d4) + (Gamma.logGamma(d12) + Beta.logGammaMinusLogGammaSum(d12, d4));
            }
            double d13 = 1.0;
            double d14 = d3;
            while (d14 > 2.0) {
                d10 = (d14 -= 1.0) / d4;
                d13 *= d10 / (1.0 + d10);
            }
            if (d4 < 10.0) {
                d10 = 1.0;
                double d15 = d4;
                while (d15 > 2.0) {
                    d10 *= (d15 -= 1.0) / (d14 + d15);
                }
                return FastMath.log(d13) + FastMath.log(d10) + (Gamma.logGamma(d14) + (Gamma.logGamma(d15) - Beta.logGammaSum(d14, d15)));
            }
            return FastMath.log(d13) + Gamma.logGamma(d14) + Beta.logGammaMinusLogGammaSum(d14, d4);
        }
        if (d3 >= 1.0) {
            if (d4 > 2.0) {
                if (d4 < 10.0) {
                    double d16 = 1.0;
                    double d17 = d4;
                    while (d17 > 2.0) {
                        d16 *= (d17 -= 1.0) / (d3 + d17);
                    }
                    return FastMath.log(d16) + (Gamma.logGamma(d3) + (Gamma.logGamma(d17) - Beta.logGammaSum(d3, d17)));
                }
                return Gamma.logGamma(d3) + Beta.logGammaMinusLogGammaSum(d3, d4);
            }
            return Gamma.logGamma(d3) + Gamma.logGamma(d4) - Beta.logGammaSum(d3, d4);
        }
        if (d4 >= 10.0) {
            return Gamma.logGamma(d3) + Beta.logGammaMinusLogGammaSum(d3, d4);
        }
        return FastMath.log(Gamma.gamma(d3) * Gamma.gamma(d4) / Gamma.gamma(d3 + d4));
    }
}

