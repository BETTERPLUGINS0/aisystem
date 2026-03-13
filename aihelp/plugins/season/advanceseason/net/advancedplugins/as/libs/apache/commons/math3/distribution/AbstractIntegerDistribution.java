/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import java.io.Serializable;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.IntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathInternalError;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomDataImpl;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public abstract class AbstractIntegerDistribution
implements IntegerDistribution,
Serializable {
    private static final long serialVersionUID = -1146319659338487221L;
    @Deprecated
    protected final RandomDataImpl randomData = new RandomDataImpl();
    protected final RandomGenerator random;

    @Deprecated
    protected AbstractIntegerDistribution() {
        this.random = null;
    }

    protected AbstractIntegerDistribution(RandomGenerator randomGenerator) {
        this.random = randomGenerator;
    }

    public double cumulativeProbability(int n, int n2) {
        if (n2 < n) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, (Number)n, n2, true);
        }
        return this.cumulativeProbability(n2) - this.cumulativeProbability(n);
    }

    public int inverseCumulativeProbability(double d) {
        boolean bl;
        if (d < 0.0 || d > 1.0) {
            throw new OutOfRangeException(d, (Number)0, 1);
        }
        int n = this.getSupportLowerBound();
        if (d == 0.0) {
            return n;
        }
        if (n == Integer.MIN_VALUE) {
            if (this.checkedCumulativeProbability(n) >= d) {
                return n;
            }
        } else {
            --n;
        }
        int n2 = this.getSupportUpperBound();
        if (d == 1.0) {
            return n2;
        }
        double d2 = this.getNumericalMean();
        double d3 = FastMath.sqrt(this.getNumericalVariance());
        boolean bl2 = bl = !Double.isInfinite(d2) && !Double.isNaN(d2) && !Double.isInfinite(d3) && !Double.isNaN(d3) && d3 != 0.0;
        if (bl) {
            double d4 = FastMath.sqrt((1.0 - d) / d);
            double d5 = d2 - d4 * d3;
            if (d5 > (double)n) {
                n = (int)FastMath.ceil(d5) - 1;
            }
            if ((d5 = d2 + (d4 = 1.0 / d4) * d3) < (double)n2) {
                n2 = (int)FastMath.ceil(d5) - 1;
            }
        }
        return this.solveInverseCumulativeProbability(d, n, n2);
    }

    protected int solveInverseCumulativeProbability(double d, int n, int n2) {
        while (n + 1 < n2) {
            double d2;
            int n3 = (n + n2) / 2;
            if (n3 < n || n3 > n2) {
                n3 = n + (n2 - n) / 2;
            }
            if ((d2 = this.checkedCumulativeProbability(n3)) >= d) {
                n2 = n3;
                continue;
            }
            n = n3;
        }
        return n2;
    }

    public void reseedRandomGenerator(long l) {
        this.random.setSeed(l);
        this.randomData.reSeed(l);
    }

    public int sample() {
        return this.inverseCumulativeProbability(this.random.nextDouble());
    }

    public int[] sample(int n) {
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.NUMBER_OF_SAMPLES, n);
        }
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            nArray[i] = this.sample();
        }
        return nArray;
    }

    private double checkedCumulativeProbability(int n) {
        double d = Double.NaN;
        d = this.cumulativeProbability(n);
        if (Double.isNaN(d)) {
            throw new MathInternalError(LocalizedFormats.DISCRETE_CUMULATIVE_PROBABILITY_RETURNED_NAN, n);
        }
        return d;
    }

    public double logProbability(int n) {
        return FastMath.log(this.probability(n));
    }
}

