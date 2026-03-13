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
import net.advancedplugins.as.libs.apache.commons.math3.util.CombinatoricsUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;
import net.advancedplugins.as.libs.apache.commons.math3.util.ResizableDoubleArray;

public class ExponentialDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = 2401296428283614780L;
    private static final double[] EXPONENTIAL_SA_QI;
    private final double mean;
    private final double logMean;
    private final double solverAbsoluteAccuracy;

    public ExponentialDistribution(double d) {
        this(d, 1.0E-9);
    }

    public ExponentialDistribution(double d, double d2) {
        this(new Well19937c(), d, d2);
    }

    public ExponentialDistribution(RandomGenerator randomGenerator, double d) {
        this(randomGenerator, d, 1.0E-9);
    }

    public ExponentialDistribution(RandomGenerator randomGenerator, double d, double d2) {
        super(randomGenerator);
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.MEAN, d);
        }
        this.mean = d;
        this.logMean = FastMath.log(d);
        this.solverAbsoluteAccuracy = d2;
    }

    public double getMean() {
        return this.mean;
    }

    public double density(double d) {
        double d2 = this.logDensity(d);
        return d2 == Double.NEGATIVE_INFINITY ? 0.0 : FastMath.exp(d2);
    }

    public double logDensity(double d) {
        if (d < 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        return -d / this.mean - this.logMean;
    }

    public double cumulativeProbability(double d) {
        double d2 = d <= 0.0 ? 0.0 : 1.0 - FastMath.exp(-d / this.mean);
        return d2;
    }

    public double inverseCumulativeProbability(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0.0, 1.0);
        }
        double d2 = d == 1.0 ? Double.POSITIVE_INFINITY : -this.mean * FastMath.log(1.0 - d);
        return d2;
    }

    public double sample() {
        double d;
        double d2;
        double d3 = 0.0;
        for (d2 = this.random.nextDouble(); d2 < 0.5; d2 *= 2.0) {
            d3 += EXPONENTIAL_SA_QI[0];
        }
        if ((d2 += d2 - 1.0) <= EXPONENTIAL_SA_QI[0]) {
            return this.mean * (d3 + d2);
        }
        int n = 0;
        double d4 = d = this.random.nextDouble();
        do {
            ++n;
            d = this.random.nextDouble();
            if (!(d < d4)) continue;
            d4 = d;
        } while (d2 > EXPONENTIAL_SA_QI[n]);
        return this.mean * (d3 + d4 * EXPONENTIAL_SA_QI[0]);
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return this.getMean();
    }

    public double getNumericalVariance() {
        double d = this.getMean();
        return d * d;
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

    static {
        double d = FastMath.log(2.0);
        double d2 = 0.0;
        int n = 1;
        ResizableDoubleArray resizableDoubleArray = new ResizableDoubleArray(20);
        while (d2 < 1.0) {
            resizableDoubleArray.addElement(d2 += FastMath.pow(d, n) / (double)CombinatoricsUtils.factorial(n));
            ++n;
        }
        EXPONENTIAL_SA_QI = resizableDoubleArray.getElements();
    }
}

