/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import net.advancedplugins.as.libs.apache.commons.math3.exception.ConvergenceException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MaxCountExceededException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;
import net.advancedplugins.as.libs.apache.commons.math3.util.Precision;

public abstract class ContinuedFraction {
    private static final double DEFAULT_EPSILON = 1.0E-8;

    protected ContinuedFraction() {
    }

    protected abstract double getA(int var1, double var2);

    protected abstract double getB(int var1, double var2);

    public double evaluate(double d) {
        return this.evaluate(d, 1.0E-8, Integer.MAX_VALUE);
    }

    public double evaluate(double d, double d2) {
        return this.evaluate(d, d2, Integer.MAX_VALUE);
    }

    public double evaluate(double d, int n) {
        return this.evaluate(d, 1.0E-8, n);
    }

    public double evaluate(double d, double d2, int n) {
        int n2;
        double d3 = 1.0E-50;
        double d4 = this.getA(0, d);
        if (Precision.equals(d4, 0.0, 1.0E-50)) {
            d4 = 1.0E-50;
        }
        double d5 = 0.0;
        double d6 = d4;
        double d7 = d4;
        for (n2 = 1; n2 < n; ++n2) {
            double d8;
            double d9;
            double d10;
            double d11 = this.getA(n2, d);
            double d12 = d11 + (d10 = this.getB(n2, d)) * d5;
            if (Precision.equals(d12, 0.0, 1.0E-50)) {
                d12 = 1.0E-50;
            }
            if (Precision.equals(d9 = d11 + d10 / d6, 0.0, 1.0E-50)) {
                d9 = 1.0E-50;
            }
            if (Double.isInfinite(d7 = d4 * (d8 = d9 * (d12 = 1.0 / d12)))) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, d);
            }
            if (Double.isNaN(d7)) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, d);
            }
            if (FastMath.abs(d8 - 1.0) < d2) break;
            d5 = d12;
            d6 = d9;
            d4 = d7;
        }
        if (n2 >= n) {
            throw new MaxCountExceededException((Localizable)LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, n, d);
        }
        return d7;
    }
}

