/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Beta;
import net.advancedplugins.as.libs.apache.commons.math3.util.CombinatoricsUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class PascalDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 6751309484392813623L;
    private final int numberOfSuccesses;
    private final double probabilityOfSuccess;
    private final double logProbabilityOfSuccess;
    private final double log1mProbabilityOfSuccess;

    public PascalDistribution(int n, double d) {
        this(new Well19937c(), n, d);
    }

    public PascalDistribution(RandomGenerator randomGenerator, int n, double d) {
        super(randomGenerator);
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.NUMBER_OF_SUCCESSES, n);
        }
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0, 1);
        }
        this.numberOfSuccesses = n;
        this.probabilityOfSuccess = d;
        this.logProbabilityOfSuccess = FastMath.log(d);
        this.log1mProbabilityOfSuccess = FastMath.log1p(-d);
    }

    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }

    public double getProbabilityOfSuccess() {
        return this.probabilityOfSuccess;
    }

    public double probability(int n) {
        double d = n < 0 ? 0.0 : CombinatoricsUtils.binomialCoefficientDouble(n + this.numberOfSuccesses - 1, this.numberOfSuccesses - 1) * FastMath.pow(this.probabilityOfSuccess, this.numberOfSuccesses) * FastMath.pow(1.0 - this.probabilityOfSuccess, n);
        return d;
    }

    public double logProbability(int n) {
        double d = n < 0 ? Double.NEGATIVE_INFINITY : CombinatoricsUtils.binomialCoefficientLog(n + this.numberOfSuccesses - 1, this.numberOfSuccesses - 1) + this.logProbabilityOfSuccess * (double)this.numberOfSuccesses + this.log1mProbabilityOfSuccess * (double)n;
        return d;
    }

    public double cumulativeProbability(int n) {
        double d = n < 0 ? 0.0 : Beta.regularizedBeta(this.probabilityOfSuccess, this.numberOfSuccesses, (double)n + 1.0);
        return d;
    }

    public double getNumericalMean() {
        double d = this.getProbabilityOfSuccess();
        double d2 = this.getNumberOfSuccesses();
        return d2 * (1.0 - d) / d;
    }

    public double getNumericalVariance() {
        double d = this.getProbabilityOfSuccess();
        double d2 = this.getNumberOfSuccesses();
        return d2 * (1.0 - d) / (d * d);
    }

    public int getSupportLowerBound() {
        return 0;
    }

    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    public boolean isSupportConnected() {
        return true;
    }
}

