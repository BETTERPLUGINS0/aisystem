/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractRealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class CauchyDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = 8589540077390120676L;
    private final double median;
    private final double scale;
    private final double solverAbsoluteAccuracy;

    public CauchyDistribution() {
        this(0.0, 1.0);
    }

    public CauchyDistribution(double d, double d2) {
        this(d, d2, 1.0E-9);
    }

    public CauchyDistribution(double d, double d2, double d3) {
        this(new Well19937c(), d, d2, d3);
    }

    public CauchyDistribution(RandomGenerator randomGenerator, double d, double d2) {
        this(randomGenerator, d, d2, 1.0E-9);
    }

    public CauchyDistribution(RandomGenerator randomGenerator, double d, double d2, double d3) {
        super(randomGenerator);
        if (d2 <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.SCALE, d2);
        }
        this.scale = d2;
        this.median = d;
        this.solverAbsoluteAccuracy = d3;
    }

    public double cumulativeProbability(double d) {
        return 0.5 + FastMath.atan((d - this.median) / this.scale) / Math.PI;
    }

    public double getMedian() {
        return this.median;
    }

    public double getScale() {
        return this.scale;
    }

    public double density(double d) {
        double d2 = d - this.median;
        return 0.3183098861837907 * (this.scale / (d2 * d2 + this.scale * this.scale));
    }

    public double inverseCumulativeProbability(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0, 1);
        }
        double d2 = d == 0.0 ? Double.NEGATIVE_INFINITY : (d == 1.0 ? Double.POSITIVE_INFINITY : this.median + this.scale * FastMath.tan(Math.PI * (d - 0.5)));
        return d2;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return Double.NaN;
    }

    public double getNumericalVariance() {
        return Double.NaN;
    }

    public double getSupportLowerBound() {
        return Double.NEGATIVE_INFINITY;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    public boolean isSupportConnected() {
        return true;
    }
}

