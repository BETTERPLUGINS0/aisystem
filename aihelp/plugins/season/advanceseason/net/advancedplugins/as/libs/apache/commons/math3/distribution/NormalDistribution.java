/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractRealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Erf;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class NormalDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = 8589540077390120676L;
    private static final double SQRT2 = FastMath.sqrt(2.0);
    private final double mean;
    private final double standardDeviation;
    private final double logStandardDeviationPlusHalfLog2Pi;
    private final double solverAbsoluteAccuracy;

    public NormalDistribution() {
        this(0.0, 1.0);
    }

    public NormalDistribution(double d, double d2) {
        this(d, d2, 1.0E-9);
    }

    public NormalDistribution(double d, double d2, double d3) {
        this(new Well19937c(), d, d2, d3);
    }

    public NormalDistribution(RandomGenerator randomGenerator, double d, double d2) {
        this(randomGenerator, d, d2, 1.0E-9);
    }

    public NormalDistribution(RandomGenerator randomGenerator, double d, double d2, double d3) {
        super(randomGenerator);
        if (d2 <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.STANDARD_DEVIATION, d2);
        }
        this.mean = d;
        this.standardDeviation = d2;
        this.logStandardDeviationPlusHalfLog2Pi = FastMath.log(d2) + 0.5 * FastMath.log(Math.PI * 2);
        this.solverAbsoluteAccuracy = d3;
    }

    public double getMean() {
        return this.mean;
    }

    public double getStandardDeviation() {
        return this.standardDeviation;
    }

    public double density(double d) {
        return FastMath.exp(this.logDensity(d));
    }

    public double logDensity(double d) {
        double d2 = d - this.mean;
        double d3 = d2 / this.standardDeviation;
        return -0.5 * d3 * d3 - this.logStandardDeviationPlusHalfLog2Pi;
    }

    public double cumulativeProbability(double d) {
        double d2 = d - this.mean;
        if (FastMath.abs(d2) > 40.0 * this.standardDeviation) {
            return d2 < 0.0 ? 0.0 : 1.0;
        }
        return 0.5 * Erf.erfc(-d2 / (this.standardDeviation * SQRT2));
    }

    public double inverseCumulativeProbability(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0, 1);
        }
        return this.mean + this.standardDeviation * SQRT2 * Erf.erfInv(2.0 * d - 1.0);
    }

    @Deprecated
    public double cumulativeProbability(double d, double d2) {
        return this.probability(d, d2);
    }

    public double probability(double d, double d2) {
        if (d > d2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, (Number)d, d2, true);
        }
        double d3 = this.standardDeviation * SQRT2;
        double d4 = (d - this.mean) / d3;
        double d5 = (d2 - this.mean) / d3;
        return 0.5 * Erf.erf(d4, d5);
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return this.getMean();
    }

    public double getNumericalVariance() {
        double d = this.getStandardDeviation();
        return d * d;
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

    public double sample() {
        return this.standardDeviation * this.random.nextGaussian() + this.mean;
    }
}

