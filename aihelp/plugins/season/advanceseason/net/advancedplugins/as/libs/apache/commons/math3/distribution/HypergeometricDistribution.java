/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.SaddlePointExpansion;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class HypergeometricDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = -436928820673516179L;
    private final int numberOfSuccesses;
    private final int populationSize;
    private final int sampleSize;
    private double numericalVariance = Double.NaN;
    private boolean numericalVarianceIsCalculated = false;

    public HypergeometricDistribution(int n, int n2, int n3) {
        this(new Well19937c(), n, n2, n3);
    }

    public HypergeometricDistribution(RandomGenerator randomGenerator, int n, int n2, int n3) {
        super(randomGenerator);
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.POPULATION_SIZE, n);
        }
        if (n2 < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.NUMBER_OF_SUCCESSES, n2);
        }
        if (n3 < 0) {
            throw new NotPositiveException((Localizable)LocalizedFormats.NUMBER_OF_SAMPLES, n3);
        }
        if (n2 > n) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.NUMBER_OF_SUCCESS_LARGER_THAN_POPULATION_SIZE, (Number)n2, n, true);
        }
        if (n3 > n) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.SAMPLE_SIZE_LARGER_THAN_POPULATION_SIZE, (Number)n3, n, true);
        }
        this.numberOfSuccesses = n2;
        this.populationSize = n;
        this.sampleSize = n3;
    }

    public double cumulativeProbability(int n) {
        int[] nArray = this.getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        double d = n < nArray[0] ? 0.0 : (n >= nArray[1] ? 1.0 : this.innerCumulativeProbability(nArray[0], n, 1));
        return d;
    }

    private int[] getDomain(int n, int n2, int n3) {
        return new int[]{this.getLowerDomain(n, n2, n3), this.getUpperDomain(n2, n3)};
    }

    private int getLowerDomain(int n, int n2, int n3) {
        return FastMath.max(0, n2 - (n - n3));
    }

    public int getNumberOfSuccesses() {
        return this.numberOfSuccesses;
    }

    public int getPopulationSize() {
        return this.populationSize;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    private int getUpperDomain(int n, int n2) {
        return FastMath.min(n2, n);
    }

    public double probability(int n) {
        double d = this.logProbability(n);
        return d == Double.NEGATIVE_INFINITY ? 0.0 : FastMath.exp(d);
    }

    public double logProbability(int n) {
        double d;
        int[] nArray = this.getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        if (n < nArray[0] || n > nArray[1]) {
            d = Double.NEGATIVE_INFINITY;
        } else {
            double d2 = (double)this.sampleSize / (double)this.populationSize;
            double d3 = (double)(this.populationSize - this.sampleSize) / (double)this.populationSize;
            double d4 = SaddlePointExpansion.logBinomialProbability(n, this.numberOfSuccesses, d2, d3);
            double d5 = SaddlePointExpansion.logBinomialProbability(this.sampleSize - n, this.populationSize - this.numberOfSuccesses, d2, d3);
            double d6 = SaddlePointExpansion.logBinomialProbability(this.sampleSize, this.populationSize, d2, d3);
            d = d4 + d5 - d6;
        }
        return d;
    }

    public double upperCumulativeProbability(int n) {
        int[] nArray = this.getDomain(this.populationSize, this.numberOfSuccesses, this.sampleSize);
        double d = n <= nArray[0] ? 1.0 : (n > nArray[1] ? 0.0 : this.innerCumulativeProbability(nArray[1], n, -1));
        return d;
    }

    private double innerCumulativeProbability(int n, int n2, int n3) {
        double d = this.probability(n);
        while (n != n2) {
            d += this.probability(n += n3);
        }
        return d;
    }

    public double getNumericalMean() {
        return (double)this.getSampleSize() * ((double)this.getNumberOfSuccesses() / (double)this.getPopulationSize());
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = this.calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        double d = this.getPopulationSize();
        double d2 = this.getNumberOfSuccesses();
        double d3 = this.getSampleSize();
        return d3 * d2 * (d - d3) * (d - d2) / (d * d * (d - 1.0));
    }

    public int getSupportLowerBound() {
        return FastMath.max(0, this.getSampleSize() + this.getNumberOfSuccesses() - this.getPopulationSize());
    }

    public int getSupportUpperBound() {
        return FastMath.min(this.getNumberOfSuccesses(), this.getSampleSize());
    }

    public boolean isSupportConnected() {
        return true;
    }
}

