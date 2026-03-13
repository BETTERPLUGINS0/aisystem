/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import java.io.Serializable;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.UnivariateFunction;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.RealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomDataImpl;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public abstract class AbstractRealDistribution
implements RealDistribution,
Serializable {
    public static final double SOLVER_DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6;
    private static final long serialVersionUID = -38038050983108802L;
    @Deprecated
    protected RandomDataImpl randomData = new RandomDataImpl();
    protected final RandomGenerator random;
    private double solverAbsoluteAccuracy = 1.0E-6;

    @Deprecated
    protected AbstractRealDistribution() {
        this.random = null;
    }

    protected AbstractRealDistribution(RandomGenerator randomGenerator) {
        this.random = randomGenerator;
    }

    @Deprecated
    public double cumulativeProbability(double d, double d2) {
        return this.probability(d, d2);
    }

    public double probability(double d, double d2) {
        if (d > d2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, (Number)d, d2, true);
        }
        return this.cumulativeProbability(d2) - this.cumulativeProbability(d);
    }

    public double inverseCumulativeProbability(final double d) {
        double d2;
        boolean bl;
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0, 1);
        }
        double d3 = this.getSupportLowerBound();
        if (d == 0.0) {
            return d3;
        }
        double d4 = this.getSupportUpperBound();
        if (d == 1.0) {
            return d4;
        }
        double d5 = this.getNumericalMean();
        double d6 = FastMath.sqrt(this.getNumericalVariance());
        boolean bl2 = bl = !Double.isInfinite(d5) && !Double.isNaN(d5) && !Double.isInfinite(d6) && !Double.isNaN(d6);
        if (d3 == Double.NEGATIVE_INFINITY) {
            if (bl) {
                d3 = d5 - d6 * FastMath.sqrt((1.0 - d) / d);
            } else {
                d3 = -1.0;
                while (this.cumulativeProbability(d3) >= d) {
                    d3 *= 2.0;
                }
            }
        }
        if (d4 == Double.POSITIVE_INFINITY) {
            if (bl) {
                d4 = d5 + d6 * FastMath.sqrt(d / (1.0 - d));
            } else {
                d4 = 1.0;
                while (this.cumulativeProbability(d4) < d) {
                    d4 *= 2.0;
                }
            }
        }
        UnivariateFunction univariateFunction = new UnivariateFunction(){

            public double value(double d2) {
                return AbstractRealDistribution.this.cumulativeProbability(d2) - d;
            }
        };
        double d7 = UnivariateSolverUtils.solve(univariateFunction, d3, d4, this.getSolverAbsoluteAccuracy());
        if (!this.isSupportConnected() && d7 - (d2 = this.getSolverAbsoluteAccuracy()) >= this.getSupportLowerBound()) {
            double d8 = this.cumulativeProbability(d7);
            if (this.cumulativeProbability(d7 - d2) == d8) {
                d4 = d7;
                while (d4 - d3 > d2) {
                    double d9 = 0.5 * (d3 + d4);
                    if (this.cumulativeProbability(d9) < d8) {
                        d3 = d9;
                        continue;
                    }
                    d4 = d9;
                }
                return d4;
            }
        }
        return d7;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public void reseedRandomGenerator(long l) {
        this.random.setSeed(l);
        this.randomData.reSeed(l);
    }

    public double sample() {
        return this.inverseCumulativeProbability(this.random.nextDouble());
    }

    public double[] sample(int n) {
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.NUMBER_OF_SAMPLES, n);
        }
        double[] dArray = new double[n];
        for (int i = 0; i < n; ++i) {
            dArray[i] = this.sample();
        }
        return dArray;
    }

    public double probability(double d) {
        return 0.0;
    }

    public double logDensity(double d) {
        return FastMath.log(this.density(d));
    }
}

