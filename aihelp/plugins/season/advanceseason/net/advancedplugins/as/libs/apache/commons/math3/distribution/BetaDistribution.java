/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractRealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooSmallException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Beta;
import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;
import net.advancedplugins.as.libs.apache.commons.math3.util.Precision;

public class BetaDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = -1221965979403477668L;
    private final double alpha;
    private final double beta;
    private double z;
    private final double solverAbsoluteAccuracy;

    public BetaDistribution(double d, double d2) {
        this(d, d2, 1.0E-9);
    }

    public BetaDistribution(double d, double d2, double d3) {
        this(new Well19937c(), d, d2, d3);
    }

    public BetaDistribution(RandomGenerator randomGenerator, double d, double d2) {
        this(randomGenerator, d, d2, 1.0E-9);
    }

    public BetaDistribution(RandomGenerator randomGenerator, double d, double d2, double d3) {
        super(randomGenerator);
        this.alpha = d;
        this.beta = d2;
        this.z = Double.NaN;
        this.solverAbsoluteAccuracy = d3;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public double getBeta() {
        return this.beta;
    }

    private void recomputeZ() {
        if (Double.isNaN(this.z)) {
            this.z = Gamma.logGamma(this.alpha) + Gamma.logGamma(this.beta) - Gamma.logGamma(this.alpha + this.beta);
        }
    }

    public double density(double d) {
        double d2 = this.logDensity(d);
        return d2 == Double.NEGATIVE_INFINITY ? 0.0 : FastMath.exp(d2);
    }

    public double logDensity(double d) {
        this.recomputeZ();
        if (d < 0.0 || d > 1.0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (d == 0.0) {
            if (this.alpha < 1.0) {
                throw new NumberIsTooSmallException((Localizable)LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA, (Number)this.alpha, 1, false);
            }
            return Double.NEGATIVE_INFINITY;
        }
        if (d == 1.0) {
            if (this.beta < 1.0) {
                throw new NumberIsTooSmallException((Localizable)LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA, (Number)this.beta, 1, false);
            }
            return Double.NEGATIVE_INFINITY;
        }
        double d2 = FastMath.log(d);
        double d3 = FastMath.log1p(-d);
        return (this.alpha - 1.0) * d2 + (this.beta - 1.0) * d3 - this.z;
    }

    public double cumulativeProbability(double d) {
        if (d <= 0.0) {
            return 0.0;
        }
        if (d >= 1.0) {
            return 1.0;
        }
        return Beta.regularizedBeta(d, this.alpha, this.beta);
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        double d = this.getAlpha();
        return d / (d + this.getBeta());
    }

    public double getNumericalVariance() {
        double d = this.getAlpha();
        double d2 = this.getBeta();
        double d3 = d + d2;
        return d * d2 / (d3 * d3 * (d3 + 1.0));
    }

    public double getSupportLowerBound() {
        return 0.0;
    }

    public double getSupportUpperBound() {
        return 1.0;
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
        return ChengBetaSampler.sample(this.random, this.alpha, this.beta);
    }

    private static final class ChengBetaSampler {
        private ChengBetaSampler() {
        }

        static double sample(RandomGenerator randomGenerator, double d, double d2) {
            double d3 = FastMath.min(d, d2);
            double d4 = FastMath.max(d, d2);
            if (d3 > 1.0) {
                return ChengBetaSampler.algorithmBB(randomGenerator, d, d3, d4);
            }
            return ChengBetaSampler.algorithmBC(randomGenerator, d, d4, d3);
        }

        private static double algorithmBB(RandomGenerator randomGenerator, double d, double d2, double d3) {
            double d4;
            double d5;
            double d6;
            double d7;
            double d8;
            double d9;
            double d10;
            double d11;
            double d12 = d2 + d3;
            double d13 = FastMath.sqrt((d12 - 2.0) / (2.0 * d2 * d3 - d12));
            double d14 = d2 + 1.0 / d13;
            do {
                d8 = randomGenerator.nextDouble();
                d5 = randomGenerator.nextDouble();
            } while (!((d11 = d2 + (d10 = d14 * (d9 = d13 * (FastMath.log(d8) - FastMath.log1p(-d8))) - 1.3862944) - (d7 = d2 * FastMath.exp(d9))) + 2.609438 >= 5.0 * (d6 = d8 * d8 * d5)) && !(d11 >= (d4 = FastMath.log(d6))) && d10 + d12 * (FastMath.log(d12) - FastMath.log(d3 + d7)) < d4);
            d7 = FastMath.min(d7, Double.MAX_VALUE);
            return Precision.equals(d2, d) ? d7 / (d3 + d7) : d3 / (d3 + d7);
        }

        private static double algorithmBC(RandomGenerator randomGenerator, double d, double d2, double d3) {
            double d4;
            double d5 = d2 + d3;
            double d6 = 1.0 / d3;
            double d7 = 1.0 + d2 - d3;
            double d8 = d7 * (0.0138889 + 0.0416667 * d3) / (d2 * d6 - 0.777778);
            double d9 = 0.25 + (0.5 + 0.25 / d7) * d3;
            while (true) {
                double d10;
                double d11 = randomGenerator.nextDouble();
                double d12 = randomGenerator.nextDouble();
                double d13 = d11 * d12;
                double d14 = d11 * d13;
                if (d11 < 0.5) {
                    if (0.25 * d12 + d14 - d13 >= d8) {
                        continue;
                    }
                } else {
                    if (d14 <= 0.25) {
                        d10 = d6 * (FastMath.log(d11) - FastMath.log1p(-d11));
                        d4 = d2 * FastMath.exp(d10);
                        break;
                    }
                    if (d14 >= d9) continue;
                }
                d10 = d6 * (FastMath.log(d11) - FastMath.log1p(-d11));
                d4 = d2 * FastMath.exp(d10);
                if (d5 * (FastMath.log(d5) - FastMath.log(d3 + d4) + d10) - 1.3862944 >= FastMath.log(d14)) break;
            }
            d4 = FastMath.min(d4, Double.MAX_VALUE);
            return Precision.equals(d2, d) ? d4 / (d3 + d4) : d3 / (d3 + d4);
        }
    }
}

