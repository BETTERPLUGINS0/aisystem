/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

final class SaddlePointExpansion {
    private static final double HALF_LOG_2_PI = 0.5 * FastMath.log(Math.PI * 2);
    private static final double[] EXACT_STIRLING_ERRORS = new double[]{0.0, 0.15342640972002736, 0.08106146679532726, 0.05481412105191765, 0.0413406959554093, 0.03316287351993629, 0.02767792568499834, 0.023746163656297496, 0.020790672103765093, 0.018488450532673187, 0.016644691189821193, 0.015134973221917378, 0.013876128823070748, 0.012810465242920227, 0.01189670994589177, 0.011104559758206917, 0.010411265261972096, 0.009799416126158804, 0.009255462182712733, 0.008768700134139386, 0.00833056343336287, 0.00793411456431402, 0.007573675487951841, 0.007244554301320383, 0.00694284010720953, 0.006665247032707682, 0.006408994188004207, 0.006171712263039458, 0.0059513701127588475, 0.0057462165130101155, 0.005554733551962801};

    private SaddlePointExpansion() {
    }

    static double getStirlingError(double d) {
        double d2;
        if (d < 15.0) {
            double d3 = 2.0 * d;
            d2 = FastMath.floor(d3) == d3 ? EXACT_STIRLING_ERRORS[(int)d3] : Gamma.logGamma(d + 1.0) - (d + 0.5) * FastMath.log(d) + d - HALF_LOG_2_PI;
        } else {
            double d4 = d * d;
            d2 = (0.08333333333333333 - (0.002777777777777778 - (7.936507936507937E-4 - (5.952380952380953E-4 - 8.417508417508417E-4 / d4) / d4) / d4) / d4) / d;
        }
        return d2;
    }

    static double getDeviancePart(double d, double d2) {
        double d3;
        if (FastMath.abs(d - d2) < 0.1 * (d + d2)) {
            double d4 = d - d2;
            double d5 = d4 / (d + d2);
            double d6 = d5 * d4;
            double d7 = Double.NaN;
            double d8 = 2.0 * d * d5;
            d5 *= d5;
            int n = 1;
            while (d6 != d7) {
                d7 = d6;
                d6 = d7 + (d8 *= d5) / (double)(n * 2 + 1);
                ++n;
            }
            d3 = d6;
        } else {
            d3 = d * FastMath.log(d / d2) + d2 - d;
        }
        return d3;
    }

    static double logBinomialProbability(int n, int n2, double d, double d2) {
        double d3;
        if (n == 0) {
            d3 = d < 0.1 ? -SaddlePointExpansion.getDeviancePart(n2, (double)n2 * d2) - (double)n2 * d : (double)n2 * FastMath.log(d2);
        } else if (n == n2) {
            d3 = d2 < 0.1 ? -SaddlePointExpansion.getDeviancePart(n2, (double)n2 * d) - (double)n2 * d2 : (double)n2 * FastMath.log(d);
        } else {
            d3 = SaddlePointExpansion.getStirlingError(n2) - SaddlePointExpansion.getStirlingError(n) - SaddlePointExpansion.getStirlingError(n2 - n) - SaddlePointExpansion.getDeviancePart(n, (double)n2 * d) - SaddlePointExpansion.getDeviancePart(n2 - n, (double)n2 * d2);
            double d4 = Math.PI * 2 * (double)n * (double)(n2 - n) / (double)n2;
            d3 = -0.5 * FastMath.log(d4) + d3;
        }
        return d3;
    }
}

