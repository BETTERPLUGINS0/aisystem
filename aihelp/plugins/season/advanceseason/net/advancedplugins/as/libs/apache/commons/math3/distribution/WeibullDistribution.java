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
import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class WeibullDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = 8589540077390120676L;
    private final double shape;
    private final double scale;
    private final double solverAbsoluteAccuracy;
    private double numericalMean = Double.NaN;
    private boolean numericalMeanIsCalculated = false;
    private double numericalVariance = Double.NaN;
    private boolean numericalVarianceIsCalculated = false;

    public WeibullDistribution(double d, double d2) {
        this(d, d2, 1.0E-9);
    }

    public WeibullDistribution(double d, double d2, double d3) {
        this(new Well19937c(), d, d2, d3);
    }

    public WeibullDistribution(RandomGenerator randomGenerator, double d, double d2) {
        this(randomGenerator, d, d2, 1.0E-9);
    }

    public WeibullDistribution(RandomGenerator randomGenerator, double d, double d2, double d3) {
        super(randomGenerator);
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.SHAPE, d);
        }
        if (d2 <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.SCALE, d2);
        }
        this.scale = d2;
        this.shape = d;
        this.solverAbsoluteAccuracy = d3;
    }

    public double getShape() {
        return this.shape;
    }

    public double getScale() {
        return this.scale;
    }

    public double density(double d) {
        if (d < 0.0) {
            return 0.0;
        }
        double d2 = d / this.scale;
        double d3 = FastMath.pow(d2, this.shape - 1.0);
        double d4 = d3 * d2;
        return this.shape / this.scale * d3 * FastMath.exp(-d4);
    }

    public double logDensity(double d) {
        if (d < 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        double d2 = d / this.scale;
        double d3 = FastMath.log(d2) * (this.shape - 1.0);
        double d4 = FastMath.exp(d3) * d2;
        return FastMath.log(this.shape / this.scale) + d3 - d4;
    }

    public double cumulativeProbability(double d) {
        double d2 = d <= 0.0 ? 0.0 : 1.0 - FastMath.exp(-FastMath.pow(d / this.scale, this.shape));
        return d2;
    }

    public double inverseCumulativeProbability(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0.0, 1.0);
        }
        double d2 = d == 0.0 ? 0.0 : (d == 1.0 ? Double.POSITIVE_INFINITY : this.scale * FastMath.pow(-FastMath.log1p(-d), 1.0 / this.shape));
        return d2;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = this.calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    protected double calculateNumericalMean() {
        double d = this.getShape();
        double d2 = this.getScale();
        return d2 * FastMath.exp(Gamma.logGamma(1.0 + 1.0 / d));
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = this.calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        double d = this.getShape();
        double d2 = this.getScale();
        double d3 = this.getNumericalMean();
        return d2 * d2 * FastMath.exp(Gamma.logGamma(1.0 + 2.0 / d)) - d3 * d3;
    }

    public double getSupportLowerBound() {
        return 0.0;
    }

    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    public boolean isSupportConnected() {
        return true;
    }
}

