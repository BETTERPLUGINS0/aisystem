/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.ExponentialDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.NormalDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.SaddlePointExpansion;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.CombinatoricsUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class PoissonDistribution
extends AbstractIntegerDistribution {
    public static final int DEFAULT_MAX_ITERATIONS = 10000000;
    public static final double DEFAULT_EPSILON = 1.0E-12;
    private static final long serialVersionUID = -3349935121172596109L;
    private final NormalDistribution normal;
    private final ExponentialDistribution exponential;
    private final double mean;
    private final int maxIterations;
    private final double epsilon;

    public PoissonDistribution(double d) {
        this(d, 1.0E-12, 10000000);
    }

    public PoissonDistribution(double d, double d2, int n) {
        this(new Well19937c(), d, d2, n);
    }

    public PoissonDistribution(RandomGenerator randomGenerator, double d, double d2, int n) {
        super(randomGenerator);
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.MEAN, d);
        }
        this.mean = d;
        this.epsilon = d2;
        this.maxIterations = n;
        this.normal = new NormalDistribution(randomGenerator, d, FastMath.sqrt(d), 1.0E-9);
        this.exponential = new ExponentialDistribution(randomGenerator, 1.0, 1.0E-9);
    }

    public PoissonDistribution(double d, double d2) {
        this(d, d2, 10000000);
    }

    public PoissonDistribution(double d, int n) {
        this(d, 1.0E-12, n);
    }

    public double getMean() {
        return this.mean;
    }

    public double probability(int n) {
        double d = this.logProbability(n);
        return d == Double.NEGATIVE_INFINITY ? 0.0 : FastMath.exp(d);
    }

    public double logProbability(int n) {
        double d = n < 0 || n == Integer.MAX_VALUE ? Double.NEGATIVE_INFINITY : (n == 0 ? -this.mean : -SaddlePointExpansion.getStirlingError(n) - SaddlePointExpansion.getDeviancePart(n, this.mean) - 0.5 * FastMath.log(Math.PI * 2) - 0.5 * FastMath.log(n));
        return d;
    }

    public double cumulativeProbability(int n) {
        if (n < 0) {
            return 0.0;
        }
        if (n == Integer.MAX_VALUE) {
            return 1.0;
        }
        return Gamma.regularizedGammaQ((double)n + 1.0, this.mean, this.epsilon, this.maxIterations);
    }

    public double normalApproximateProbability(int n) {
        return this.normal.cumulativeProbability((double)n + 0.5);
    }

    public double getNumericalMean() {
        return this.getMean();
    }

    public double getNumericalVariance() {
        return this.getMean();
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

    public int sample() {
        return (int)FastMath.min(this.nextPoisson(this.mean), Integer.MAX_VALUE);
    }

    private long nextPoisson(double d) {
        double d2;
        long l;
        block9: {
            double d3 = 40.0;
            if (d < 40.0) {
                double d4 = FastMath.exp(-d);
                long l2 = 0L;
                double d5 = 1.0;
                double d6 = 1.0;
                while ((double)l2 < 1000.0 * d) {
                    d6 = this.random.nextDouble();
                    if ((d5 *= d6) >= d4) {
                        ++l2;
                        continue;
                    }
                    return l2;
                }
                return l2;
            }
            double d7 = FastMath.floor(d);
            double d8 = d - d7;
            double d9 = FastMath.log(d7);
            double d10 = CombinatoricsUtils.factorialLog((int)d7);
            l = d8 < Double.MIN_VALUE ? 0L : this.nextPoisson(d8);
            double d11 = FastMath.sqrt(d7 * FastMath.log(32.0 * d7 / Math.PI + 1.0));
            double d12 = d11 / 2.0;
            double d13 = 2.0 * d7 + d11;
            double d14 = FastMath.sqrt(Math.PI * d13) * FastMath.exp(1.0 / (8.0 * d7));
            double d15 = d13 / d11 * FastMath.exp(-d11 * (1.0 + d11) / d13);
            double d16 = d14 + d15 + 1.0;
            double d17 = d14 / d16;
            double d18 = d15 / d16;
            double d19 = 1.0 / (8.0 * d7);
            double d20 = 0.0;
            d2 = 0.0;
            double d21 = 0.0;
            boolean bl = false;
            double d22 = 0.0;
            double d23 = 0.0;
            double d24 = 0.0;
            while (true) {
                double d25;
                if ((d25 = this.random.nextDouble()) <= d17) {
                    double d26 = this.random.nextGaussian();
                    d20 = d26 * FastMath.sqrt(d7 + d12) - 0.5;
                    if (d20 > d11 || d20 < -d7) continue;
                    d2 = d20 < 0.0 ? FastMath.floor(d20) : FastMath.ceil(d20);
                    double d27 = this.exponential.sample();
                    d21 = -d27 - d26 * d26 / 2.0 + d19;
                } else {
                    if (d25 > d17 + d18) {
                        d2 = d7;
                        break block9;
                    }
                    d20 = d11 + d13 / d11 * this.exponential.sample();
                    d2 = FastMath.ceil(d20);
                    d21 = -this.exponential.sample() - d11 * (d20 + 1.0) / d13;
                }
                bl = d20 < 0.0;
                d22 = d2 * (d2 + 1.0) / (2.0 * d7);
                if (d21 < -d22 && !bl) {
                    d2 = d7 + d2;
                    break block9;
                }
                d23 = d22 * ((2.0 * d2 + 1.0) / (6.0 * d7) - 1.0);
                d24 = d23 - d22 * d22 / (3.0 * (d7 + (double)bl * (d2 + 1.0)));
                if (d21 < d24) {
                    d2 = d7 + d2;
                    break block9;
                }
                if (!(d21 > d23) && d21 < d2 * d9 - CombinatoricsUtils.factorialLog((int)(d2 + d7)) + d10) break;
            }
            d2 = d7 + d2;
        }
        return l + (long)d2;
    }
}

