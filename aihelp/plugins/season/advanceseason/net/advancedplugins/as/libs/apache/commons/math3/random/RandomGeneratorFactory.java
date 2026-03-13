/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.random;

import java.util.Random;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;

public class RandomGeneratorFactory {
    private RandomGeneratorFactory() {
    }

    public static RandomGenerator createRandomGenerator(final Random random) {
        return new RandomGenerator(){

            public void setSeed(int n) {
                random.setSeed(n);
            }

            public void setSeed(int[] nArray) {
                random.setSeed(RandomGeneratorFactory.convertToLong(nArray));
            }

            public void setSeed(long l) {
                random.setSeed(l);
            }

            public void nextBytes(byte[] byArray) {
                random.nextBytes(byArray);
            }

            public int nextInt() {
                return random.nextInt();
            }

            public int nextInt(int n) {
                if (n <= 0) {
                    throw new NotStrictlyPositiveException(n);
                }
                return random.nextInt(n);
            }

            public long nextLong() {
                return random.nextLong();
            }

            public boolean nextBoolean() {
                return random.nextBoolean();
            }

            public float nextFloat() {
                return random.nextFloat();
            }

            public double nextDouble() {
                return random.nextDouble();
            }

            public double nextGaussian() {
                return random.nextGaussian();
            }
        };
    }

    public static long convertToLong(int[] nArray) {
        long l = 0xFFFFFFFBL;
        long l2 = 0L;
        for (int n : nArray) {
            l2 = l2 * 0xFFFFFFFBL + (long)n;
        }
        return l2;
    }
}

