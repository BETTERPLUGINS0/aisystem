/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractRealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.GammaDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;

public class ChiSquaredDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = -8352658048349159782L;
    private final GammaDistribution gamma;
    private final double solverAbsoluteAccuracy;

    public ChiSquaredDistribution(double d) {
        this(d, 1.0E-9);
    }

    public ChiSquaredDistribution(double d, double d2) {
        this(new Well19937c(), d, d2);
    }

    public ChiSquaredDistribution(RandomGenerator randomGenerator, double d) {
        this(randomGenerator, d, 1.0E-9);
    }

    public ChiSquaredDistribution(RandomGenerator randomGenerator, double d, double d2) {
        super(randomGenerator);
        this.gamma = new GammaDistribution(d / 2.0, 2.0);
        this.solverAbsoluteAccuracy = d2;
    }

    public double getDegreesOfFreedom() {
        return this.gamma.getShape() * 2.0;
    }

    public double density(double d) {
        return this.gamma.density(d);
    }

    public double logDensity(double d) {
        return this.gamma.logDensity(d);
    }

    public double cumulativeProbability(double d) {
        return this.gamma.cumulativeProbability(d);
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return this.getDegreesOfFreedom();
    }

    public double getNumericalVariance() {
        return 2.0 * this.getDegreesOfFreedom();
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

