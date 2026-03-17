/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstInfo;

final class LongVector {
    static final int ASIZE = 128;
    static final int ABITS = 7;
    static final int VSIZE = 8;
    private ConstInfo[][] objects;
    private int elements;

    public LongVector() {
        this.objects = new ConstInfo[8][];
        this.elements = 0;
    }

    public LongVector(int n) {
        int n2 = (n >> 7 & 0xFFFFFFF8) + 8;
        this.objects = new ConstInfo[n2][];
        this.elements = 0;
    }

    public int size() {
        return this.elements;
    }

    public int capacity() {
        return this.objects.length * 128;
    }

    public ConstInfo elementAt(int n) {
        if (n < 0 || this.elements <= n) {
            return null;
        }
        return this.objects[n >> 7][n & 0x7F];
    }

    public void addElement(ConstInfo constInfo) {
        int n = this.elements >> 7;
        int n2 = this.elements & 0x7F;
        int n3 = this.objects.length;
        if (n >= n3) {
            ConstInfo[][] constInfoArrayArray = new ConstInfo[n3 + 8][];
            System.arraycopy(this.objects, 0, constInfoArrayArray, 0, n3);
            this.objects = constInfoArrayArray;
        }
        if (this.objects[n] == null) {
            this.objects[n] = new ConstInfo[128];
        }
        this.objects[n][n2] = constInfo;
        ++this.elements;
    }
}

