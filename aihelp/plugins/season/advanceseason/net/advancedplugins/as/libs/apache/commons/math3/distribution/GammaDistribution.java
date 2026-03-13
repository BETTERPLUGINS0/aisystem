/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.distribution;

import net.advancedplugins.as.libs.apache.commons.math3.distribution.AbstractRealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.special.Gamma;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public class GammaDistribution
extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9;
    private static final long serialVersionUID = 20120524L;
    private final double shape;
    private final double scale;
    private final double shiftedShape;
    private final double densityPrefactor1;
    private final double logDensityPrefactor1;
    private final double densityPrefactor2;
    private final double logDensityPrefactor2;
    private final double minY;
    private final double maxLogY;
    private final double solverAbsoluteAccuracy;

    public GammaDistribution(double d, double d2) {
        this(d, d2, 1.0E-9);
    }

    public GammaDistribution(double d, double d2, double d3) {
        this(new Well19937c(), d, d2, d3);
    }

    public GammaDistribution(RandomGenerator randomGenerator, double d, double d2) {
        this(randomGenerator, d, d2, 1.0E-9);
    }

    public GammaDistribution(RandomGenerator randomGenerator, double d, double d2, double d3) {
        super(randomGenerator);
        if (d <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.SHAPE, d);
        }
        if (d2 <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.SCALE, d2);
        }
        this.shape = d;
        this.scale = d2;
        this.solverAbsoluteAccuracy = d3;
        this.shiftedShape = d + 4.7421875 + 0.5;
        double d4 = Math.E / (Math.PI * 2 * this.shiftedShape);
        this.densityPrefactor2 = d * FastMath.sqrt(d4) / Gamma.lanczos(d);
        this.logDensityPrefactor2 = FastMath.log(d) + 0.5 * FastMath.log(d4) - FastMath.log(Gamma.lanczos(d));
        this.densityPrefactor1 = this.densityPrefactor2 / d2 * FastMath.pow(this.shiftedShape, -d) * FastMath.exp(d + 4.7421875);
        this.logDensityPrefactor1 = this.logDensityPrefactor2 - FastMath.log(d2) - FastMath.log(this.shiftedShape) * d + d + 4.7421875;
        this.minY = d + 4.7421875 - FastMath.log(Double.MAX_VALUE);
        this.maxLogY = FastMath.log(Double.MAX_VALUE) / (d - 1.0);
    }

    @Deprecated
    public double getAlpha() {
        return this.shape;
    }

    public double getShape() {
        return this.shape;
    }

    @Deprecated
    public double getBeta() {
        return this.scale;
    }

    public double getScale() {
        return this.scale;
    }

    public double density(double d) {
        if (d < 0.0) {
            return 0.0;
        }
        double d2 = d / this.scale;
        if (d2 <= this.minY || FastMath.log(d2) >= this.maxLogY) {
            double d3 = (d2 - this.shiftedShape) / this.shiftedShape;
            double d4 = this.shape * (FastMath.log1p(d3) - d3);
            double d5 = -d2 * 5.2421875 / this.shiftedShape + 4.7421875 + d4;
            return this.densityPrefactor2 / d * FastMath.exp(d5);
        }
        return this.densityPrefactor1 * FastMath.exp(-d2) * FastMath.pow(d2, this.shape - 1.0);
    }

    public double logDensity(double d) {
        if (d < 0.0) {
            return Double.NEGATIVE_INFINITY;
        }
        double d2 = d / this.scale;
        if (d2 <= this.minY || FastMath.log(d2) >= this.maxLogY) {
            double d3 = (d2 - this.shiftedShape) / this.shiftedShape;
            double d4 = this.shape * (FastMath.log1p(d3) - d3);
            double d5 = -d2 * 5.2421875 / this.shiftedShape + 4.7421875 + d4;
            return this.logDensityPrefactor2 - FastMath.log(d) + d5;
        }
        return this.logDensityPrefactor1 - d2 + FastMath.log(d2) * (this.shape - 1.0);
    }

    public double cumulativeProbability(double d) {
        double d2 = d <= 0.0 ? 0.0 : Gamma.regularizedGammaP(this.shape, d / this.scale);
        return d2;
    }

    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    public double getNumericalMean() {
        return this.shape * this.scale;
    }

    public double getNumericalVariance() {
        return this.shape * this.scale * this.scale;
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

    public double sample() {
        double d;
        if (this.shape < 1.0) {
            double d2;
            while (true) {
                double d3;
                double d4;
                double d5;
                double d6;
                if ((d6 = (d5 = 1.0 + this.shape / Math.E) * (d4 = this.random.nextDouble())) <= 1.0) {
                    d2 = FastMath.pow(d6, 1.0 / this.shape);
                    d3 = this.random.nextDouble();
                    if (d3 > FastMath.exp(-d2)) continue;
                    return this.scale * d2;
                }
                d2 = -1.0 * FastMath.log((d5 - d6) / this.shape);
                d3 = this.random.nextDouble();
                if (!(d3 > FastMath.pow(d2, this.shape - 1.0))) break;
            }
            return this.scale * d2;
        }
        double d7 = this.shape - 0.3333333333333333;
        double d8 = 1.0 / (3.0 * FastMath.sqrt(d7));
        while (true) {
            double d9;
            if ((d = (1.0 + d8 * (d9 = this.random.nextGaussian())) * (1.0 + d8 * d9) * (1.0 + d8 * d9)) <= 0.0) {
                continue;
            }
            double d10 = d9 * d9;
            double d11 = this.random.nextDouble();
            if (d11 < 1.0 - 0.0331 * d10 * d10) {
                return this.scale * d7 * d;
            }
            if (FastMath.log(d11) < 0.5 * d10 + d7 * (1.0 - d + FastMath.log(d))) break;
        }
        return this.scale * d7 * d;
    }
}

