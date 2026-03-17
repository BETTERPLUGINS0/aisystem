/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

class ByteVector
implements Cloneable {
    private byte[] buffer = new byte[64];
    private int size = 0;

    public Object clone() {
        ByteVector byteVector = (ByteVector)super.clone();
        byteVector.buffer = (byte[])this.buffer.clone();
        return byteVector;
    }

    public final int getSize() {
        return this.size;
    }

    public final byte[] copy() {
        byte[] byArray = new byte[this.size];
        System.arraycopy(this.buffer, 0, byArray, 0, this.size);
        return byArray;
    }

    public int read(int n) {
        if (n < 0 || this.size <= n) {
            throw new ArrayIndexOutOfBoundsException(n);
        }
        return this.buffer[n];
    }

    public void write(int n, int n2) {
        if (n < 0 || this.size <= n) {
            throw new ArrayIndexOutOfBoundsException(n);
        }
        this.buffer[n] = (byte)n2;
    }

    public void add(int n) {
        this.addGap(1);
        this.buffer[this.size - 1] = (byte)n;
    }

    public void add(int n, int n2) {
        this.addGap(2);
        this.buffer[this.size - 2] = (byte)n;
        this.buffer[this.size - 1] = (byte)n2;
    }

    public void add(int n, int n2, int n3, int n4) {
        this.addGap(4);
        this.buffer[this.size - 4] = (byte)n;
        this.buffer[this.size - 3] = (byte)n2;
        this.buffer[this.size - 2] = (byte)n3;
        this.buffer[this.size - 1] = (byte)n4;
    }

    public void addGap(int n) {
        if (this.size + n > this.buffer.length) {
            int n2 = this.size << 1;
            if (n2 < this.size + n) {
                n2 = this.size + n;
            }
            byte[] byArray = new byte[n2];
            System.arraycopy(this.buffer, 0, byArray, 0, this.size);
            this.buffer = byArray;
        }
        this.size += n;
    }
}

