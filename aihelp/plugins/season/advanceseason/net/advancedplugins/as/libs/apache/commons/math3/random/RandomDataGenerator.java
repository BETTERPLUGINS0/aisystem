/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.random;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collection;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.BetaDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.BinomialDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.CauchyDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.ChiSquaredDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.ExponentialDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.FDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.GammaDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.HypergeometricDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.PascalDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.PoissonDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.TDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.UniformIntegerDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.WeibullDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.distribution.ZipfDistribution;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathInternalError;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotANumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotFiniteNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomData;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGeneratorFactory;
import net.advancedplugins.as.libs.apache.commons.math3.random.Well19937c;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathArrays;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class RandomDataGenerator
implements RandomData,
Serializable {
    private static final long serialVersionUID = -626730818244969716L;
    private RandomGenerator rand = null;
    private RandomGenerator secRand = null;

    public RandomDataGenerator() {
    }

    public RandomDataGenerator(RandomGenerator randomGenerator) {
        this.rand = randomGenerator;
    }

    @Override
    public String nextHexString(int n) {
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.LENGTH, n);
        }
        RandomGenerator randomGenerator = this.getRandomGenerator();
        StringBuilder stringBuilder = new StringBuilder();
        byte[] byArray = new byte[n / 2 + 1];
        randomGenerator.nextBytes(byArray);
        for (int i = 0; i < byArray.length; ++i) {
            Integer n2 = byArray[i];
            String string = Integer.toHexString(n2 + 128);
            if (string.length() == 1) {
                string = "0" + string;
            }
            stringBuilder.append(string);
        }
        return stringBuilder.toString().substring(0, n);
    }

    @Override
    public int nextInt(int n, int n2) {
        return new UniformIntegerDistribution(this.getRandomGenerator(), n, n2).sample();
    }

    @Override
    public long nextLong(long l, long l2) {
        if (l >= l2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, (Number)l, l2, false);
        }
        long l3 = l2 - l + 1L;
        if (l3 <= 0L) {
            long l4;
            RandomGenerator randomGenerator = this.getRandomGenerator();
            while ((l4 = randomGenerator.nextLong()) < l || l4 > l2) {
            }
            return l4;
        }
        if (l3 < Integer.MAX_VALUE) {
            return l + (long)this.getRandomGenerator().nextInt((int)l3);
        }
        return l + RandomDataGenerator.nextLong(this.getRandomGenerator(), l3);
    }

    private static long nextLong(RandomGenerator randomGenerator, long l) {
        if (l > 0L) {
            long l2;
            long l3;
            byte[] byArray = new byte[8];
            do {
                randomGenerator.nextBytes(byArray);
                l2 = 0L;
                for (byte by : byArray) {
                    l2 = l2 << 8 | (long)by & 0xFFL;
                }
            } while ((l2 &= Long.MAX_VALUE) - (l3 = l2 % l) + (l - 1L) < 0L);
            return l3;
        }
        throw new NotStrictlyPositiveException(l);
    }

    @Override
    public String nextSecureHexString(int n) {
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.LENGTH, n);
        }
        RandomGenerator randomGenerator = this.getSecRan();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            throw new MathInternalError(noSuchAlgorithmException);
        }
        messageDigest.reset();
        int n2 = n / 40 + 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < n2 + 1; ++i) {
            byte[] byArray = new byte[40];
            randomGenerator.nextBytes(byArray);
            messageDigest.update(byArray);
            byte[] byArray2 = messageDigest.digest();
            for (int j = 0; j < byArray2.length; ++j) {
                Integer n3 = byArray2[j];
                String string = Integer.toHexString(n3 + 128);
                if (string.length() == 1) {
                    string = "0" + string;
                }
                stringBuilder.append(string);
            }
        }
        return stringBuilder.toString().substring(0, n);
    }

    @Override
    public int nextSecureInt(int n, int n2) {
        return new UniformIntegerDistribution(this.getSecRan(), n, n2).sample();
    }

    @Override
    public long nextSecureLong(long l, long l2) {
        if (l >= l2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, (Number)l, l2, false);
        }
        RandomGenerator randomGenerator = this.getSecRan();
        long l3 = l2 - l + 1L;
        if (l3 <= 0L) {
            long l4;
            while ((l4 = randomGenerator.nextLong()) < l || l4 > l2) {
            }
            return l4;
        }
        if (l3 < Integer.MAX_VALUE) {
            return l + (long)randomGenerator.nextInt((int)l3);
        }
        return l + RandomDataGenerator.nextLong(randomGenerator, l3);
    }

    @Override
    public long nextPoisson(double d) {
        return new PoissonDistribution(this.getRandomGenerator(), d, 1.0E-12, 10000000).sample();
    }

    @Override
    public double nextGaussian(double d, double d2) {
        if (d2 <= 0.0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.STANDARD_DEVIATION, d2);
        }
        return d2 * this.getRandomGenerator().nextGaussian() + d;
    }

    @Override
    public double nextExponential(double d) {
        return new ExponentialDistribution(this.getRandomGenerator(), d, 1.0E-9).sample();
    }

    public double nextGamma(double d, double d2) {
        return new GammaDistribution(this.getRandomGenerator(), d, d2, 1.0E-9).sample();
    }

    public int nextHypergeometric(int n, int n2, int n3) {
        return new HypergeometricDistribution(this.getRandomGenerator(), n, n2, n3).sample();
    }

    public int nextPascal(int n, double d) {
        return new PascalDistribution(this.getRandomGenerator(), n, d).sample();
    }

    public double nextT(double d) {
        return new TDistribution(this.getRandomGenerator(), d, 1.0E-9).sample();
    }

    public double nextWeibull(double d, double d2) {
        return new WeibullDistribution(this.getRandomGenerator(), d, d2, 1.0E-9).sample();
    }

    public int nextZipf(int n, double d) {
        return new ZipfDistribution(this.getRandomGenerator(), n, d).sample();
    }

    public double nextBeta(double d, double d2) {
        return new BetaDistribution(this.getRandomGenerator(), d, d2, 1.0E-9).sample();
    }

    public int nextBinomial(int n, double d) {
        return new BinomialDistribution(this.getRandomGenerator(), n, d).sample();
    }

    public double nextCauchy(double d, double d2) {
        return new CauchyDistribution(this.getRandomGenerator(), d, d2, 1.0E-9).sample();
    }

    public double nextChiSquare(double d) {
        return new ChiSquaredDistribution(this.getRandomGenerator(), d, 1.0E-9).sample();
    }

    public double nextF(double d, double d2) {
        return new FDistribution(this.getRandomGenerator(), d, d2, 1.0E-9).sample();
    }

    @Override
    public double nextUniform(double d, double d2) {
        return this.nextUniform(d, d2, false);
    }

    @Override
    public double nextUniform(double d, double d2, boolean bl) {
        if (d >= d2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, (Number)d, d2, false);
        }
        if (Double.isInfinite(d)) {
            throw new NotFiniteNumberException((Localizable)LocalizedFormats.INFINITE_BOUND, d, new Object[0]);
        }
        if (Double.isInfinite(d2)) {
            throw new NotFiniteNumberException((Localizable)LocalizedFormats.INFINITE_BOUND, d2, new Object[0]);
        }
        if (Double.isNaN(d) || Double.isNaN(d2)) {
            throw new NotANumberException();
        }
        RandomGenerator randomGenerator = this.getRandomGenerator();
        double d3 = randomGenerator.nextDouble();
        while (!bl && d3 <= 0.0) {
            d3 = randomGenerator.nextDouble();
        }
        return d3 * d2 + (1.0 - d3) * d;
    }

    @Override
    public int[] nextPermutation(int n, int n2) {
        if (n2 > n) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.PERMUTATION_EXCEEDS_N, (Number)n2, n, true);
        }
        if (n2 <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.PERMUTATION_SIZE, n2);
        }
        int[] nArray = MathArrays.natural(n);
        MathArrays.shuffle(nArray, this.getRandomGenerator());
        return MathArrays.copyOf(nArray, n2);
    }

    @Override
    public Object[] nextSample(Collection<?> collection, int n) {
        int n2 = collection.size();
        if (n > n2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE, (Number)n, n2, true);
        }
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.NUMBER_OF_SAMPLES, n);
        }
        Object[] objectArray = collection.toArray();
        int[] nArray = this.nextPermutation(n2, n);
        Object[] objectArray2 = new Object[n];
        for (int i = 0; i < n; ++i) {
            objectArray2[i] = objectArray[nArray[i]];
        }
        return objectArray2;
    }

    public void reSeed(long l) {
        this.getRandomGenerator().setSeed(l);
    }

    public void reSeedSecure() {
        this.getSecRan().setSeed(System.currentTimeMillis());
    }

    public void reSeedSecure(long l) {
        this.getSecRan().setSeed(l);
    }

    public void reSeed() {
        this.getRandomGenerator().setSeed(System.currentTimeMillis() + (long)System.identityHashCode(this));
    }

    public void setSecureAlgorithm(String string, String string2) {
        this.secRand = RandomGeneratorFactory.createRandomGenerator(SecureRandom.getInstance(string, string2));
    }

    public RandomGenerator getRandomGenerator() {
        if (this.rand == null) {
            this.initRan();
        }
        return this.rand;
    }

    private void initRan() {
        this.rand = new Well19937c(System.currentTimeMillis() + (long)System.identityHashCode(this));
    }

    private RandomGenerator getSecRan() {
        if (this.secRand == null) {
            this.secRand = RandomGeneratorFactory.createRandomGenerator(new SecureRandom());
            this.secRand.setSeed(System.currentTimeMillis() + (long)System.identityHashCode(this));
        }
        return this.secRand;
    }
}

