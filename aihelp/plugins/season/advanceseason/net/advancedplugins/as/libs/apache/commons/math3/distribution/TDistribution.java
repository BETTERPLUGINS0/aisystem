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
import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class TDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = -5852615386664158222L;
    private final double degreesOfFreedom;
    private final double solverAbsoluteAccuracy;
    private final double factor;

    public TDistribution(double d) {
        this(d, 1.0E-9);
    }

    public TDistribution(double d, double d2) {
        this(new Well19937c(), d, d2);
    }

    public TDistribution(RandomGenerator randomGenerator, double d) {
        this(randomGenerator, d, 1.0E-9);
    }

    public TDistribution(RandomGenerator randomGenerator, double d, double d2) {
        super(randomGenerator);
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.DEGREES_OF_FREEDOM, d);
        }
        this.degreesOfFreedom = d;
        this.solverAbsoluteAccuracy = d2;
        double d3 = d;
        double d4 = (d3 + 1.0) / 2.0;
        this.factor = Gamma.logGamma(d4) - 0.5 * (FastMath.log(Math.PI) + FastMath.log(d3)) - Gamma.logGamma(d3 / 2.0);
    }

    public double getDegreesOfFreedom() {
        return this.degreesOfFreedom;
    }

    public double density(double d) {
        return FastMath.exp(this.logDensity(d));
    }

    public double logDensity(double d) {
        double d2 = this.degreesOfFreedom;
        double d3 = (d2 + 1.0) / 2.0;
        return this.factor - d3 * FastMath.log(1.0 + d * d / d2);
    }

    public double cumulativeProbability(double d) {
        double d2;
        if (d == 0.0) {
            d2 = 0.5;
        } else {
            double d3 = Beta.regularizedBeta(this.degreesOfFreedom / (this.degreesOfFreedom + d * d), 0.5 * this.degreesOfFreedom, 0.5);
            d2 = d < 0.0 ? 0.5 * d3 : 1.0 - 0.5 * d3;
        }
        return d2;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        double d = this.getDegreesOfFreedom();
        if (d > 1.0) {
            return 0.0;
        }
        return Double.NaN;
    }

    public double getNumericalVariance() {
        double d = this.getDegreesOfFreedom();
        if (d > 2.0) {
            return d / (d - 2.0);
        }
        if (d > 1.0 && d <= 2.0) {
            return Double.POSITIVE_INFINITY;
        }
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

