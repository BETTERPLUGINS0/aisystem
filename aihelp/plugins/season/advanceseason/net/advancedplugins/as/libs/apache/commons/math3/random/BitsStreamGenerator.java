/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.random;

import java.io.Serializable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.random.RandomGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public abstract class BitsStreamGenerator
implements RandomGenerator,
Serializable {
    private static final long serialVersionUID = 20130104L;
    private double nextGaussian = Double.NaN;

    public abstract void setSeed(int var1);

    public abstract void setSeed(int[] var1);

    public abstract void setSeed(long var1);

    protected abstract int next(int var1);

    public boolean nextBoolean() {
        return this.next(1) != 0;
    }

    public double nextDouble() {
        long l = (long)this.next(26) << 26;
        int n = this.next(26);
        return (double)(l | (long)n) * 2.220446049250313E-16;
    }

    public float nextFloat() {
        return (float)this.next(23) * 1.1920929E-7f;
    }

    public double nextGaussian() {
        double d;
        if (Double.isNaN(this.nextGaussian)) {
            double d2 = this.nextDouble();
            double d3 = this.nextDouble();
            double d4 = Math.PI * 2 * d2;
            double d5 = FastMath.sqrt(-2.0 * FastMath.log(d3));
            d = d5 * FastMath.cos(d4);
            this.nextGaussian = d5 * FastMath.sin(d4);
        } else {
            d = this.nextGaussian;
            this.nextGaussian = Double.NaN;
        }
        return d;
    }

    public int nextInt() {
        return this.next(32);
    }

    public int nextInt(int n) {
        if (n > 0) {
            int n2;
            int n3;
            if ((n & -n) == n) {
                return (int)((long)n * (long)this.next(31) >> 31);
            }
            while ((n3 = this.next(31)) - (n2 = n3 % n) + (n - 1) < 0) {
            }
            return n2;
        }
        throw new NotStrictlyPositiveException(n);
    }

    public long nextLong() {
        long l = (long)this.next(32) << 32;
        long l2 = (long)this.next(32) & 0xFFFFFFFFL;
        return l | l2;
    }

    public long nextLong(long l) {
        if (l > 0L) {
            long l2;
            long l3;
            do {
                l2 = (long)this.next(31) << 32;
            } while ((l2 |= (long)this.next(32) & 0xFFFFFFFFL) - (l3 = l2 % l) + (l - 1L) < 0L);
            return l3;
        }
        throw new NotStrictlyPositiveException(l);
    }

    public void clear() {
        this.nextGaussian = Double.NaN;
    }

    public void nextBytes(byte[] byArray) {
        this.nextBytesFill(byArray, 0, byArray.length);
    }

    public void nextBytes(byte[] byArray, int n, int n2) {
        if (n < 0 || n >= byArray.length) {
            throw new OutOfRangeException(n, (Number)0, byArray.length);
        }
        if (n2 < 0 || n2 > byArray.length - n) {
            throw new OutOfRangeException(n2, (Number)0, byArray.length - n);
        }
        this.nextBytesFill(byArray, n, n2);
    }

    private void nextBytesFill(byte[] byArray, int n, int n2) {
        int n3;
        int n4 = n;
        int n5 = n4 + (n2 & 0x7FFFFFFC);
        while (n4 < n5) {
            n3 = this.next(32);
            byArray[n4++] = (byte)n3;
            byArray[n4++] = (byte)(n3 >>> 8);
            byArray[n4++] = (byte)(n3 >>> 16);
            byArray[n4++] = (byte)(n3 >>> 24);
        }
        n3 = n + n2;
        if (n4 < n3) {
            int n6 = this.next(32);
            while (true) {
                byArray[n4++] = (byte)n6;
                if (n4 >= n3) break;
                n6 >>>= 8;
            }
        }
    }
}

