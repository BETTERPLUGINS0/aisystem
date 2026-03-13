/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class ZipfDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = -140627372283420404L;
    private final int numberOfElements;
    private final double exponent;
    private double numericalMean = Double.NaN;
    private boolean numericalMeanIsCalculated = false;
    private double numericalVariance = Double.NaN;
    private boolean numericalVarianceIsCalculated = false;
    private transient ZipfRejectionInversionSampler sampler;

    public ZipfDistribution(int n, double d) {
        this(new Well19937c(), n, d);
    }

    public ZipfDistribution(RandomGenerator randomGenerator, int n, double d) {
        super(randomGenerator);
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.DIMENSION, n);
        }
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.EXPONENT, d);
        }
        this.numberOfElements = n;
        this.exponent = d;
    }

    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    public double getExponent() {
        return this.exponent;
    }

    public double probability(int n) {
        if (n <= 0 || n > this.numberOfElements) {
            return 0.0;
        }
        return 1.0 / FastMath.pow((double)n, this.exponent) / this.generalizedHarmonic(this.numberOfElements, this.exponent);
    }

    public double logProbability(int n) {
        if (n <= 0 || n > this.numberOfElements) {
            return Double.NEGATIVE_INFINITY;
        }
        return -FastMath.log(n) * this.exponent - FastMath.log(this.generalizedHarmonic(this.numberOfElements, this.exponent));
    }

    public double cumulativeProbability(int n) {
        if (n <= 0) {
            return 0.0;
        }
        if (n >= this.numberOfElements) {
            return 1.0;
        }
        return this.generalizedHarmonic(n, this.exponent) / this.generalizedHarmonic(this.numberOfElements, this.exponent);
    }

    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = this.calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    protected double calculateNumericalMean() {
        int n = this.getNumberOfElements();
        double d = this.getExponent();
        double d2 = this.generalizedHarmonic(n, d - 1.0);
        double d3 = this.generalizedHarmonic(n, d);
        return d2 / d3;
    }

    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = this.calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        int n = this.getNumberOfElements();
        double d = this.getExponent();
        double d2 = this.generalizedHarmonic(n, d - 2.0);
        double d3 = this.generalizedHarmonic(n, d - 1.0);
        double d4 = this.generalizedHarmonic(n, d);
        return d2 / d4 - d3 * d3 / (d4 * d4);
    }

    private double generalizedHarmonic(int n, double d) {
        double d2 = 0.0;
        for (int i = n; i > 0; --i) {
            d2 += 1.0 / FastMath.pow((double)i, d);
        }
        return d2;
    }

    public int getSupportLowerBound() {
        return 1;
    }

    public int getSupportUpperBound() {
        return this.getNumberOfElements();
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int sample() {
        if (this.sampler == null) {
            this.sampler = new ZipfRejectionInversionSampler(this.numberOfElements, this.exponent);
        }
        return this.sampler.sample(this.random);
    }

    static final class ZipfRejectionInversionSampler {
        private final double exponent;
        private final int numberOfElements;
        private final double hIntegralX1;
        private final double hIntegralNumberOfElements;
        private final double s;

        ZipfRejectionInversionSampler(int n, double d) {
            this.exponent = d;
            this.numberOfElements = n;
            this.hIntegralX1 = this.hIntegral(1.5) - 1.0;
            this.hIntegralNumberOfElements = this.hIntegral((double)n + 0.5);
            this.s = 2.0 - this.hIntegralInverse(this.hIntegral(2.5) - this.h(2.0));
        }

        int sample(RandomGenerator randomGenerator) {
            double d;
            double d2;
            int n;
            do {
                if ((n = (int)((d2 = this.hIntegralInverse(d = this.hIntegralNumberOfElements + randomGenerator.nextDouble() * (this.hIntegralX1 - this.hIntegralNumberOfElements))) + 0.5)) < 1) {
                    n = 1;
                    continue;
                }
                if (n <= this.numberOfElements) continue;
                n = this.numberOfElements;
            } while (!((double)n - d2 <= this.s) && !(d >= this.hIntegral((double)n + 0.5) - this.h(n)));
            return n;
        }

        private double hIntegral(double d) {
            double d2 = FastMath.log(d);
            return ZipfRejectionInversionSampler.helper2((1.0 - this.exponent) * d2) * d2;
        }

        private double h(double d) {
            return FastMath.exp(-this.exponent * FastMath.log(d));
        }

        private double hIntegralInverse(double d) {
            double d2 = d * (1.0 - this.exponent);
            if (d2 < -1.0) {
                d2 = -1.0;
            }
            return FastMath.exp(ZipfRejectionInversionSampler.helper1(d2) * d);
        }

        static double helper1(double d) {
            if (FastMath.abs(d) > 1.0E-8) {
                return FastMath.log1p(d) / d;
            }
            return 1.0 - d * (0.5 - d * (0.3333333333333333 - d * 0.25));
        }

        static double helper2(double d) {
            if (FastMath.abs(d) > 1.0E-8) {
                return FastMath.expm1(d) / d;
            }
            return 1.0 + d * 0.5 * (1.0 + d * 0.3333333333333333 * (1.0 + d * 0.25));
        }
    }
}

