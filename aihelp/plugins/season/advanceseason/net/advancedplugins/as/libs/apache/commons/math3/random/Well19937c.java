/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.random;

import net.advancedplugins.as.libs.apache.commons.math3.random.AbstractWell;

public class Well19937c
extends AbstractWell {
    private static final long serialVersionUID = -7203498180754925124L;
    private static final int K = 19937;
    private static final int M1 = 70;
    private static final int M2 = 179;
    private static final int M3 = 449;

    public Well19937c() {
        super(19937, 70, 179, 449);
    }

    public Well19937c(int n) {
        super(19937, 70, 179, 449, n);
    }

    public Well19937c(int[] nArray) {
        super(19937, 70, 179, 449, nArray);
    }

    public Well19937c(long l) {
        super(19937, 70, 179, 449, l);
    }

    protected int next(int n) {
        int n2 = this.iRm1[this.index];
        int n3 = this.iRm2[this.index];
        int n4 = this.v[this.index];
        int n5 = this.v[this.i1[this.index]];
        int n6 = this.v[this.i2[this.index]];
        int n7 = this.v[this.i3[this.index]];
        int n8 = Integer.MIN_VALUE & this.v[n2] ^ Integer.MAX_VALUE & this.v[n3];
        int n9 = n4 ^ n4 << 25 ^ (n5 ^ n5 >>> 27);
        int n10 = n6 >>> 9 ^ (n7 ^ n7 >>> 1);
        int n11 = n9 ^ n10;
        int n12 = n8 ^ (n9 ^ n9 << 9) ^ (n10 ^ n10 << 21) ^ (n11 ^ n11 >>> 21);
        this.v[this.index] = n11;
        this.v[n2] = n12;
        int n13 = n3;
        this.v[n13] = this.v[n13] & Integer.MIN_VALUE;
        this.index = n2;
        n12 ^= n12 << 7 & 0xE46E1700;
        n12 ^= n12 << 15 & 0x9B868000;
        return n12 >>> 32 - n;
    }
}

