/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.random;

import java.io.Serializable;
import java.util.Collection;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.IntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.RealDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomData;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomDataGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
public class RandomDataImpl
implements RandomData,
Serializable {
    private static final long serialVersionUID = -626730818244969716L;
    private final RandomDataGenerator delegate;

    public RandomDataImpl() {
        this.delegate = new RandomDataGenerator();
    }

    public RandomDataImpl(RandomGenerator randomGenerator) {
        this.delegate = new RandomDataGenerator(randomGenerator);
    }

    @Deprecated
    RandomDataGenerator getDelegate() {
        return this.delegate;
    }

    @Override
    public String nextHexString(int n) {
        return this.delegate.nextHexString(n);
    }

    @Override
    public int nextInt(int n, int n2) {
        return this.delegate.nextInt(n, n2);
    }

    @Override
    public long nextLong(long l, long l2) {
        return this.delegate.nextLong(l, l2);
    }

    @Override
    public String nextSecureHexString(int n) {
        return this.delegate.nextSecureHexString(n);
    }

    @Override
    public int nextSecureInt(int n, int n2) {
        return this.delegate.nextSecureInt(n, n2);
    }

    @Override
    public long nextSecureLong(long l, long l2) {
        return this.delegate.nextSecureLong(l, l2);
    }

    @Override
    public long nextPoisson(double d) {
        return this.delegate.nextPoisson(d);
    }

    @Override
    public double nextGaussian(double d, double d2) {
        return this.delegate.nextGaussian(d, d2);
    }

    @Override
    public double nextExponential(double d) {
        return this.delegate.nextExponential(d);
    }

    @Override
    public double nextUniform(double d, double d2) {
        return this.delegate.nextUniform(d, d2);
    }

    @Override
    public double nextUniform(double d, double d2, boolean bl) {
        return this.delegate.nextUniform(d, d2, bl);
    }

    public double nextBeta(double d, double d2) {
        return this.delegate.nextBeta(d, d2);
    }

    public int nextBinomial(int n, double d) {
        return this.delegate.nextBinomial(n, d);
    }

    public double nextCauchy(double d, double d2) {
        return this.delegate.nextCauchy(d, d2);
    }

    public double nextChiSquare(double d) {
        return this.delegate.nextChiSquare(d);
    }

    public double nextF(double d, double d2) {
        return this.delegate.nextF(d, d2);
    }

    public double nextGamma(double d, double d2) {
        return this.delegate.nextGamma(d, d2);
    }

    public int nextHypergeometric(int n, int n2, int n3) {
        return this.delegate.nextHypergeometric(n, n2, n3);
    }

    public int nextPascal(int n, double d) {
        return this.delegate.nextPascal(n, d);
    }

    public double nextT(double d) {
        return this.delegate.nextT(d);
    }

    public double nextWeibull(double d, double d2) {
        return this.delegate.nextWeibull(d, d2);
    }

    public int nextZipf(int n, double d) {
        return this.delegate.nextZipf(n, d);
    }

    public void reSeed(long l) {
        this.delegate.reSeed(l);
    }

    public void reSeedSecure() {
        this.delegate.reSeedSecure();
    }

    public void reSeedSecure(long l) {
        this.delegate.reSeedSecure(l);
    }

    public void reSeed() {
        this.delegate.reSeed();
    }

    public void setSecureAlgorithm(String string, String string2) {
        this.delegate.setSecureAlgorithm(string, string2);
    }

    @Override
    public int[] nextPermutation(int n, int n2) {
        return this.delegate.nextPermutation(n, n2);
    }

    @Override
    public Object[] nextSample(Collection<?> collection, int n) {
        return this.delegate.nextSample(collection, n);
    }

    @Deprecated
    public double nextInversionDeviate(RealDistribution realDistribution) {
        return realDistribution.inverseCumulativeProbability(this.nextUniform(0.0, 1.0));
    }

    @Deprecated
    public int nextInversionDeviate(IntegerDistribution integerDistribution) {
        return integerDistribution.inverseCumulativeProbability(this.nextUniform(0.0, 1.0));
    }
}

