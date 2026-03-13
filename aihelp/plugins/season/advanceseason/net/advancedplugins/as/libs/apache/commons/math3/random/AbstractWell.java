/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.random;

import java.io.Serializable;
import net.advancedplugins.as.libs.apache.commons.math3.random.BitsStreamGenerator;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

public abstract class AbstractWell
extends BitsStreamGenerator
implements Serializable {
    private static final long serialVersionUID = -817701723016583596L;
    protected int index;
    protected final int[] v;
    protected final int[] iRm1;
    protected final int[] iRm2;
    protected final int[] i1;
    protected final int[] i2;
    protected final int[] i3;

    protected AbstractWell(int n, int n2, int n3, int n4) {
        this(n, n2, n3, n4, null);
    }

    protected AbstractWell(int n, int n2, int n3, int n4, int n5) {
        this(n, n2, n3, n4, new int[]{n5});
    }

    protected AbstractWell(int n, int n2, int n3, int n4, int[] nArray) {
        int n5 = 32;
        int n6 = (n + 32 - 1) / 32;
        this.v = new int[n6];
        this.index = 0;
        this.iRm1 = new int[n6];
        this.iRm2 = new int[n6];
        this.i1 = new int[n6];
        this.i2 = new int[n6];
        this.i3 = new int[n6];
        for (int i = 0; i < n6; ++i) {
            this.iRm1[i] = (i + n6 - 1) % n6;
            this.iRm2[i] = (i + n6 - 2) % n6;
            this.i1[i] = (i + n2) % n6;
            this.i2[i] = (i + n3) % n6;
            this.i3[i] = (i + n4) % n6;
        }
        this.setSeed(nArray);
    }

    protected AbstractWell(int n, int n2, int n3, int n4, long l) {
        this(n, n2, n3, n4, new int[]{(int)(l >>> 32), (int)(l & 0xFFFFFFFFL)});
    }

    public void setSeed(int n) {
        this.setSeed(new int[]{n});
    }

    public void setSeed(int[] nArray) {
        if (nArray == null) {
            this.setSeed(System.currentTimeMillis() + (long)System.identityHashCode(this));
            return;
        }
        System.arraycopy(nArray, 0, this.v, 0, FastMath.min(nArray.length, this.v.length));
        if (nArray.length < this.v.length) {
            for (int i = nArray.length; i < this.v.length; ++i) {
                long l = this.v[i - nArray.length];
                this.v[i] = (int)(1812433253L * (l ^ l >> 30) + (long)i & 0xFFFFFFFFL);
            }
        }
        this.index = 0;
        this.clear();
    }

    public void setSeed(long l) {
        this.setSeed(new int[]{(int)(l >>> 32), (int)(l & 0xFFFFFFFFL)});
    }

    protected abstract int next(int var1);
}

