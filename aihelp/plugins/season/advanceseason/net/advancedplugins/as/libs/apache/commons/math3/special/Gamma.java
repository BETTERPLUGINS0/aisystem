/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.special;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MaxCountExceededException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooSmallException;
import net.advancedplugins.as.libs.apache.commons.math3.util.ContinuedFraction;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class Gamma {
    public static final double GAMMA = 0.5772156649015329;
    public static final double LANCZOS_G = 4.7421875;
    private static final double DEFAULT_EPSILON = 1.0E-14;
    private static final double[] LANCZOS = new double[]{0.9999999999999971, 57.15623566586292, -59.59796035547549, 14.136097974741746, -0.4919138160976202, 3.399464998481189E-5, 4.652362892704858E-5, -9.837447530487956E-5, 1.580887032249125E-4, -2.1026444172410488E-4, 2.1743961811521265E-4, -1.643181065367639E-4, 8.441822398385275E-5, -2.6190838401581408E-5, 3.6899182659531625E-6};
    private static final double HALF_LOG_2_PI = 0.5 * FastMath.log(Math.PI * 2);
    private static final double SQRT_TWO_PI = 2.5066282746310007;
    private static final double C_LIMIT = 49.0;
    private static final double S_LIMIT = 1.0E-5;
    private static final double INV_GAMMA1P_M1_A0 = 6.116095104481416E-9;
    private static final double INV_GAMMA1P_M1_A1 = 6.247308301164655E-9;
    private static final double INV_GAMMA1P_M1_B1 = 0.203610414066807;
    private static final double INV_GAMMA1P_M1_B2 = 0.026620534842894922;
    private static final double INV_GAMMA1P_M1_B3 = 4.939449793824468E-4;
    private static final double INV_GAMMA1P_M1_B4 = -8.514194324403149E-6;
    private static final double INV_GAMMA1P_M1_B5 = -6.4304548177935305E-6;
    private static final double INV_GAMMA1P_M1_B6 = 9.926418406727737E-7;
    private static final double INV_GAMMA1P_M1_B7 = -6.077618957228252E-8;
    private static final double INV_GAMMA1P_M1_B8 = 1.9575583661463974E-10;
    private static final double INV_GAMMA1P_M1_P0 = 6.116095104481416E-9;
    private static final double INV_GAMMA1P_M1_P1 = 6.8716741130671986E-9;
    private static final double INV_GAMMA1P_M1_P2 = 6.820161668496171E-10;
    private static final double INV_GAMMA1P_M1_P3 = 4.686843322948848E-11;
    private static final double INV_GAMMA1P_M1_P4 = 1.5728330277104463E-12;
    private static final double INV_GAMMA1P_M1_P5 = -1.2494415722763663E-13;
    private static final double INV_GAMMA1P_M1_P6 = 4.343529937408594E-15;
    private static final double INV_GAMMA1P_M1_Q1 = 0.3056961078365221;
    private static final double INV_GAMMA1P_M1_Q2 = 0.054642130860422966;
    private static final double INV_GAMMA1P_M1_Q3 = 0.004956830093825887;
    private static final double INV_GAMMA1P_M1_Q4 = 2.6923694661863613E-4;
    private static final double INV_GAMMA1P_M1_C = -0.42278433509846713;
    private static final double INV_GAMMA1P_M1_C0 = 0.5772156649015329;
    private static final double INV_GAMMA1P_M1_C1 = -0.6558780715202539;
    private static final double INV_GAMMA1P_M1_C2 = -0.04200263503409524;
    private static final double INV_GAMMA1P_M1_C3 = 0.16653861138229148;
    private static final double INV_GAMMA1P_M1_C4 = -0.04219773455554433;
    private static final double INV_GAMMA1P_M1_C5 = -0.009621971527876973;
    private static final double INV_GAMMA1P_M1_C6 = 0.0072189432466631;
    private static final double INV_GAMMA1P_M1_C7 = -0.0011651675918590652;
    private static final double INV_GAMMA1P_M1_C8 = -2.1524167411495098E-4;
    private static final double INV_GAMMA1P_M1_C9 = 1.280502823881162E-4;
    private static final double INV_GAMMA1P_M1_C10 = -2.013485478078824E-5;
    private static final double INV_GAMMA1P_M1_C11 = -1.2504934821426706E-6;
    private static final double INV_GAMMA1P_M1_C12 = 1.133027231981696E-6;
    private static final double INV_GAMMA1P_M1_C13 = -2.056338416977607E-7;

    private Gamma() {
    }

    public static double logGamma(double d) {
        double d2;
        if (Double.isNaN(d) || d <= 0.0) {
            d2 = Double.NaN;
        } else {
            if (d < 0.5) {
                return Gamma.logGamma1p(d) - FastMath.log(d);
            }
            if (d <= 2.5) {
                return Gamma.logGamma1p(d - 0.5 - 0.5);
            }
            if (d <= 8.0) {
                int n = (int)FastMath.floor(d - 1.5);
                double d3 = 1.0;
                for (int i = 1; i <= n; ++i) {
                    d3 *= d - (double)i;
                }
                return Gamma.logGamma1p(d - (double)(n + 1)) + FastMath.log(d3);
            }
            double d4 = Gamma.lanczos(d);
            double d5 = d + 4.7421875 + 0.5;
            d2 = (d + 0.5) * FastMath.log(d5) - d5 + HALF_LOG_2_PI + FastMath.log(d4 / d);
        }
        return d2;
    }

    public static double regularizedGammaP(double d, double d2) {
        return Gamma.regularizedGammaP(d, d2, 1.0E-14, Integer.MAX_VALUE);
    }

    public static double regularizedGammaP(double d, double d2, double d3, int n) {
        double d4;
        if (Double.isNaN(d) || Double.isNaN(d2) || d <= 0.0 || d2 < 0.0) {
            d4 = Double.NaN;
        } else if (d2 == 0.0) {
            d4 = 0.0;
        } else if (d2 >= d + 1.0) {
            d4 = 1.0 - Gamma.regularizedGammaQ(d, d2, d3, n);
        } else {
            double d5;
            double d6;
            double d7 = 0.0;
            for (d5 = d6 = 1.0 / d; FastMath.abs(d6 / d5) > d3 && d7 < (double)n && d5 < Double.POSITIVE_INFINITY; d5 += (d6 *= d2 / (d + (d7 += 1.0)))) {
            }
            if (d7 >= (double)n) {
                throw new MaxCountExceededException(n);
            }
            d4 = Double.isInfinite(d5) ? 1.0 : FastMath.exp(-d2 + d * FastMath.log(d2) - Gamma.logGamma(d)) * d5;
        }
        return d4;
    }

    public static double regularizedGammaQ(double d, double d2) {
        return Gamma.regularizedGammaQ(d, d2, 1.0E-14, Integer.MAX_VALUE);
    }

    public static double regularizedGammaQ(final double d, double d2, double d3, int n) {
        double d4;
        if (Double.isNaN(d) || Double.isNaN(d2) || d <= 0.0 || d2 < 0.0) {
            d4 = Double.NaN;
        } else if (d2 == 0.0) {
            d4 = 1.0;
        } else if (d2 < d + 1.0) {
            d4 = 1.0 - Gamma.regularizedGammaP(d, d2, d3, n);
        } else {
            ContinuedFraction continuedFraction = new ContinuedFraction(){

                protected double getA(int n, double d2) {
                    return 2.0 * (double)n + 1.0 - d + d2;
                }

                protected double getB(int n, double d2) {
                    return (double)n * (d - (double)n);
                }
            };
            d4 = 1.0 / continuedFraction.evaluate(d2, d3, n);
            d4 = FastMath.exp(-d2 + d * FastMath.log(d2) - Gamma.logGamma(d)) * d4;
        }
        return d4;
    }

    public static double digamma(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return d;
        }
        if (d > 0.0 && d <= 1.0E-5) {
            return -0.5772156649015329 - 1.0 / d;
        }
        if (d >= 49.0) {
            double d2 = 1.0 / (d * d);
            return FastMath.log(d) - 0.5 / d - d2 * (0.08333333333333333 + d2 * (0.008333333333333333 - d2 / 252.0));
        }
        return Gamma.digamma(d + 1.0) - 1.0 / d;
    }

    public static double trigamma(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            return d;
        }
        if (d > 0.0 && d <= 1.0E-5) {
            return 1.0 / (d * d);
        }
        if (d >= 49.0) {
            double d2 = 1.0 / (d * d);
            return 1.0 / d + d2 / 2.0 + d2 / d * (0.16666666666666666 - d2 * (0.03333333333333333 + d2 / 42.0));
        }
        return Gamma.trigamma(d + 1.0) + 1.0 / (d * d);
    }

    public static double lanczos(double d) {
        double d2 = 0.0;
        for (int i = LANCZOS.length - 1; i > 0; --i) {
            d2 += LANCZOS[i] / (d + (double)i);
        }
        return d2 + LANCZOS[0];
    }

    public static double invGamma1pm1(double d) {
        double d2;
        double d3;
        if (d < -0.5) {
            throw new NumberIsTooSmallException(d, (Number)-0.5, true);
        }
        if (d > 1.5) {
            throw new NumberIsTooLargeException(d, (Number)1.5, true);
        }
        double d4 = d3 = d <= 0.5 ? d : d - 0.5 - 0.5;
        if (d3 < 0.0) {
            double d5 = 6.116095104481416E-9 + d3 * 6.247308301164655E-9;
            double d6 = 1.9575583661463974E-10;
            d6 = -6.077618957228252E-8 + d3 * d6;
            d6 = 9.926418406727737E-7 + d3 * d6;
            d6 = -6.4304548177935305E-6 + d3 * d6;
            d6 = -8.514194324403149E-6 + d3 * d6;
            d6 = 4.939449793824468E-4 + d3 * d6;
            d6 = 0.026620534842894922 + d3 * d6;
            d6 = 0.203610414066807 + d3 * d6;
            d6 = 1.0 + d3 * d6;
            double d7 = -2.056338416977607E-7 + d3 * (d5 / d6);
            d7 = 1.133027231981696E-6 + d3 * d7;
            d7 = -1.2504934821426706E-6 + d3 * d7;
            d7 = -2.013485478078824E-5 + d3 * d7;
            d7 = 1.280502823881162E-4 + d3 * d7;
            d7 = -2.1524167411495098E-4 + d3 * d7;
            d7 = -0.0011651675918590652 + d3 * d7;
            d7 = 0.0072189432466631 + d3 * d7;
            d7 = -0.009621971527876973 + d3 * d7;
            d7 = -0.04219773455554433 + d3 * d7;
            d7 = 0.16653861138229148 + d3 * d7;
            d7 = -0.04200263503409524 + d3 * d7;
            d7 = -0.6558780715202539 + d3 * d7;
            d7 = -0.42278433509846713 + d3 * d7;
            d2 = d > 0.5 ? d3 * d7 / d : d * (d7 + 0.5 + 0.5);
        } else {
            double d8 = 4.343529937408594E-15;
            d8 = -1.2494415722763663E-13 + d3 * d8;
            d8 = 1.5728330277104463E-12 + d3 * d8;
            d8 = 4.686843322948848E-11 + d3 * d8;
            d8 = 6.820161668496171E-10 + d3 * d8;
            d8 = 6.8716741130671986E-9 + d3 * d8;
            d8 = 6.116095104481416E-9 + d3 * d8;
            double d9 = 2.6923694661863613E-4;
            d9 = 0.004956830093825887 + d3 * d9;
            d9 = 0.054642130860422966 + d3 * d9;
            d9 = 0.3056961078365221 + d3 * d9;
            d9 = 1.0 + d3 * d9;
            double d10 = -2.056338416977607E-7 + d8 / d9 * d3;
            d10 = 1.133027231981696E-6 + d3 * d10;
            d10 = -1.2504934821426706E-6 + d3 * d10;
            d10 = -2.013485478078824E-5 + d3 * d10;
            d10 = 1.280502823881162E-4 + d3 * d10;
            d10 = -2.1524167411495098E-4 + d3 * d10;
            d10 = -0.0011651675918590652 + d3 * d10;
            d10 = 0.0072189432466631 + d3 * d10;
            d10 = -0.009621971527876973 + d3 * d10;
            d10 = -0.04219773455554433 + d3 * d10;
            d10 = 0.16653861138229148 + d3 * d10;
            d10 = -0.04200263503409524 + d3 * d10;
            d10 = -0.6558780715202539 + d3 * d10;
            d10 = 0.5772156649015329 + d3 * d10;
            d2 = d > 0.5 ? d3 / d * (d10 - 0.5 - 0.5) : d * d10;
        }
        return d2;
    }

    public static double logGamma1p(double d) {
        if (d < -0.5) {
            throw new NumberIsTooSmallException(d, (Number)-0.5, true);
        }
        if (d > 1.5) {
            throw new NumberIsTooLargeException(d, (Number)1.5, true);
        }
        return -FastMath.log1p(Gamma.invGamma1pm1(d));
    }

    public static double gamma(double d) {
        double d2;
        if (d == FastMath.rint(d) && d <= 0.0) {
            return Double.NaN;
        }
        double d3 = FastMath.abs(d);
        if (d3 <= 20.0) {
            if (d >= 1.0) {
                double d4 = 1.0;
                double d5 = d;
                while (d5 > 2.5) {
                    d4 *= (d5 -= 1.0);
                }
                d2 = d4 / (1.0 + Gamma.invGamma1pm1(d5 - 1.0));
            } else {
                double d6 = d;
                double d7 = d;
                while (d7 < -0.5) {
                    d6 *= (d7 += 1.0);
                }
                d2 = 1.0 / (d6 * (1.0 + Gamma.invGamma1pm1(d7)));
            }
        } else {
            double d8 = d3 + 4.7421875 + 0.5;
            double d9 = 2.5066282746310007 / d3 * FastMath.pow(d8, d3 + 0.5) * FastMath.exp(-d8) * Gamma.lanczos(d3);
            d2 = d > 0.0 ? d9 : -Math.PI / (d * FastMath.sin(Math.PI * d) * d9);
        }
        return d2;
    }
}

