/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.SaddlePointExpansion;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Beta;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class BinomialDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 6751309484392813623L;
    private final int numberOfTrials;
    private final double probabilityOfSuccess;

    public BinomialDistribution(int n, double d) {
        this(new Well19937c(), n, d);
    }

    public BinomialDistribution(RandomGenerator randomGenerator, int n, double d) {
        super(randomGenerator);
        if (n < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.NUMBER_OF_TRIALS, n);
        }
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0, 1);
        }
        this.probabilityOfSuccess = d;
        this.numberOfTrials = n;
    }

    public int getNumberOfTrials() {
        return this.numberOfTrials;
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    public double probability(int n) {
        double d = this.logProbability(n);
        return d == Double.NEGATIVE_INFINITY ? 0.0 : FastMath.exp(d);
    }

    public double logProbability(int n) {
        if (this.numberOfTrials == 0) {
            return n == 0 ? 0.0 : Double.NEGATIVE_INFINITY;
        }
        double d = n < 0 || n > this.numberOfTrials ? Double.NEGATIVE_INFINITY : SaddlePointExpansion.logBinomialProbability(n, this.numberOfTrials, this.probabilityOfSuccess, 1.0 - this.probabilityOfSuccess);
        return d;
    }

    public double cumulativeProbability(int n) {
        double d = n < 0 ? 0.0 : (n >= this.numberOfTrials ? 1.0 : 1.0 - Beta.regularizedBeta(this.probabilityOfSuccess, (double)n + 1.0, this.numberOfTrials - n));
        return d;
    }

    public double getNumericalMean() {
        return (double)this.numberOfTrials * this.probabilityOfSuccess;
    }

    public double getNumericalVariance() {
        double d = this.probabilityOfSuccess;
        return (double)this.numberOfTrials * d * (1.0 - d);
    }

    public int getSupportLowerBound() {
        return this.probabilityOfSuccess < 1.0 ? 0 : this.numberOfTrials;
    }

    public int getSupportUpperBound() {
        return this.probabilityOfSuccess > 0.0 ? this.numberOfTrials : 0;
    }

    public boolean isSupportConnected() {
        return true;
    }
}

