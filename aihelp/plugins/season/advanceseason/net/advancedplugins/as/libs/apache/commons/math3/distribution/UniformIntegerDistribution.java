/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;

public class UniformIntegerDistribution
extends AbstractIntegerDistribution {
    private static final long serialVersionUID = 20120109L;
    private final int lower;
    private final int upper;

    public UniformIntegerDistribution(int n, int n2) {
        this(new Well19937c(), n, n2);
    }

    public UniformIntegerDistribution(RandomGenerator randomGenerator, int n, int n2) {
        super(randomGenerator);
        if (n > n2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, (Number)n, n2, true);
        }
        this.lower = n;
        this.upper = n2;
    }

    public double probability(int n) {
        if (n < this.lower || n > this.upper) {
            return 0.0;
        }
        return 1.0 / (double)(this.upper - this.lower + 1);
    }

    public double cumulativeProbability(int n) {
        if (n < this.lower) {
            return 0.0;
        }
        if (n > this.upper) {
            return 1.0;
        }
        return ((double)(n - this.lower) + 1.0) / ((double)(this.upper - this.lower) + 1.0);
    }

    public double getNumericalMean() {
        return 0.5 * (double)(this.lower + this.upper);
    }

    public double getNumericalVariance() {
        double d = this.upper - this.lower + 1;
        return (d * d - 1.0) / 12.0;
    }

    public int getSupportLowerBound() {
        return this.lower;
    }

    public int getSupportUpperBound() {
        return this.upper;
    }

    public boolean isSupportConnected() {
        return true;
    }

    public int sample() {
        int n = this.upper - this.lower + 1;
        if (n <= 0) {
            int n2;
            while ((n2 = this.random.nextInt()) < this.lower || n2 > this.upper) {
            }
            return n2;
        }
        return this.lower + this.random.nextInt(n);
    }
}

