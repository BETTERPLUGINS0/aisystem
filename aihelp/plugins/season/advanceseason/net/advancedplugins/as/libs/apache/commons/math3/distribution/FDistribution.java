/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractRealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Beta;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class FDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = -8516354193418641566L;
    private final double numeratorDegreesOfFreedom;
    private final double denominatorDegreesOfFreedom;
    private final double solverAbsoluteAccuracy;
    private double numericalVariance = Double.NaN;
    private boolean numericalVarianceIsCalculated = false;

    public FDistribution(double d, double d2) {
        this(d, d2, 1.0E-9);
    }

    public FDistribution(double d, double d2, double d3) {
        this(new Well19937c(), d, d2, d3);
    }

    public FDistribution(RandomGenerator randomGenerator, double d, double d2) {
        this(randomGenerator, d, d2, 1.0E-9);
    }

    public FDistribution(RandomGenerator randomGenerator, double d, double d2, double d3) {
        super(randomGenerator);
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.DEGREES_OF_FREEDOM, d);
        }
        if (d2 <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.DEGREES_OF_FREEDOM, d2);
        }
        this.numeratorDegreesOfFreedom = d;
        this.denominatorDegreesOfFreedom = d2;
        this.solverAbsoluteAccuracy = d3;
    }

    public double density(double d) {
        return FastMath.exp(this.logDensity(d));
    }

    public double logDensity(double d) {
        double d2 = this.numeratorDegreesOfFreedom / 2.0;
        double d3 = this.denominatorDegreesOfFreedom / 2.0;
        double d4 = FastMath.log(d);
        double d5 = FastMath.log(this.numeratorDegreesOfFreedom);
        double d6 = FastMath.log(this.denominatorDegreesOfFreedom);
        double d7 = FastMath.log(this.numeratorDegreesOfFreedom * d + this.denominatorDegreesOfFreedom);
        return d2 * d5 + d2 * d4 - d4 + d3 * d6 - d2 * d7 - d3 * d7 - Beta.logBeta(d2, d3);
    }

    public double cumulativeProbability(double d) {
        double d2;
        if (d <= 0.0) {
            d2 = 0.0;
        } else {
            double d3 = this.numeratorDegreesOfFreedom;
            double d4 = this.denominatorDegreesOfFreedom;
            d2 = Beta.regularizedBeta(d3 * d / (d4 + d3 * d), 0.5 * d3, 0.5 * d4);
        }
        return d2;
    }

    public double getNumeratorDegreesOfFreedom() {
        return this.numeratorDegreesOfFreedom;
    }

    public double getDenominatorDegreesOfFreedom() {
        return this.denominatorDegreesOfFreedom;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        double d = this.getDenominatorDegreesOfFreedom();
        if (d > 2.0) {
            return d / (d - 2.0);
        }
        return Double.NaN;
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = this.calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        double d = this.getDenominatorDegreesOfFreedom();
        if (d > 4.0) {
            double d2 = this.getNumeratorDegreesOfFreedom();
            double d3 = d - 2.0;
            return 2.0 * (d * d) * (d2 + d - 2.0) / (d2 * (d3 * d3) * (d - 4.0));
        }
        return Double.NaN;
    }

    public double getSupportLowerBound() {
        return 0.0;
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

