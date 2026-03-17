/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.OutputStream;

final class ByteStream
extends OutputStream {
    private byte[] buf;
    private int count;

    public ByteStream() {
        this(32);
    }

    public ByteStream(int n) {
        this.buf = new byte[n];
        this.count = 0;
    }

    public int getPos() {
        return this.count;
    }

    public int size() {
        return this.count;
    }

    public void writeBlank(int n) {
        this.enlarge(n);
        this.count += n;
    }

    @Override
    public void write(byte[] byArray) {
        this.write(byArray, 0, byArray.length);
    }

    @Override
    public void write(byte[] byArray, int n, int n2) {
        this.enlarge(n2);
        System.arraycopy(byArray, n, this.buf, this.count, n2);
        this.count += n2;
    }

    @Override
    public void write(int n) {
        this.enlarge(1);
        int n2 = this.count;
        this.buf[n2] = (byte)n;
        this.count = n2 + 1;
    }

    public void writeShort(int n) {
        this.enlarge(2);
        int n2 = this.count;
        this.buf[n2] = (byte)(n >>> 8);
        this.buf[n2 + 1] = (byte)n;
        this.count = n2 + 2;
    }

    public void writeInt(int n) {
        this.enlarge(4);
        int n2 = this.count;
        this.buf[n2] = (byte)(n >>> 24);
        this.buf[n2 + 1] = (byte)(n >>> 16);
        this.buf[n2 + 2] = (byte)(n >>> 8);
        this.buf[n2 + 3] = (byte)n;
        this.count = n2 + 4;
    }

    public void writeLong(long l) {
        this.enlarge(8);
        int n = this.count;
        this.buf[n] = (byte)(l >>> 56);
        this.buf[n + 1] = (byte)(l >>> 48);
        this.buf[n + 2] = (byte)(l >>> 40);
        this.buf[n + 3] = (byte)(l >>> 32);
        this.buf[n + 4] = (byte)(l >>> 24);
        this.buf[n + 5] = (byte)(l >>> 16);
        this.buf[n + 6] = (byte)(l >>> 8);
        this.buf[n + 7] = (byte)l;
        this.count = n + 8;
    }

    public void writeFloat(float f) {
        this.writeInt(Float.floatToIntBits(f));
    }

    public void writeDouble(double d) {
        this.writeLong(Double.doubleToLongBits(d));
    }

    public void writeUTF(String string) {
        int n = string.length();
        int n2 = this.count;
        this.enlarge(n + 2);
        byte[] byArray = this.buf;
        byArray[n2++] = (byte)(n >>> 8);
        byArray[n2++] = (byte)n;
        for (int i = 0; i < n; ++i) {
            char c = string.charAt(i);
            if ('\u0001' > c || c > '\u007f') {
                this.writeUTF2(string, n, i);
                return;
            }
            byArray[n2++] = (byte)c;
        }
        this.count = n2;
    }

    private void writeUTF2(String string, int n, int n2) {
        int n3;
        int n4 = n;
        for (n3 = n2; n3 < n; ++n3) {
            char c = string.charAt(n3);
            if (c > '\u07ff') {
                n4 += 2;
                continue;
            }
            if (c != '\u0000' && c <= '\u007f') continue;
            ++n4;
        }
        if (n4 > 65535) {
            throw new RuntimeException("encoded string too long: " + n + n4 + " bytes");
        }
        this.enlarge(n4 + 2);
        n3 = this.count;
        byte[] byArray = this.buf;
        byArray[n3] = (byte)(n4 >>> 8);
        byArray[n3 + 1] = (byte)n4;
        n3 += 2 + n2;
        for (int i = n2; i < n; ++i) {
            char c = string.charAt(i);
            if ('\u0001' <= c && c <= '\u007f') {
                byArray[n3++] = (byte)c;
                continue;
            }
            if (c > '\u07ff') {
                byArray[n3] = (byte)(0xE0 | c >> 12 & 0xF);
                byArray[n3 + 1] = (byte)(0x80 | c >> 6 & 0x3F);
                byArray[n3 + 2] = (byte)(0x80 | c & 0x3F);
                n3 += 3;
                continue;
            }
            byArray[n3] = (byte)(0xC0 | c >> 6 & 0x1F);
            byArray[n3 + 1] = (byte)(0x80 | c & 0x3F);
            n3 += 2;
        }
        this.count = n3;
    }

    public void write(int n, int n2) {
        this.buf[n] = (byte)n2;
    }

    public void writeShort(int n, int n2) {
        this.buf[n] = (byte)(n2 >>> 8);
        this.buf[n + 1] = (byte)n2;
    }

    public void writeInt(int n, int n2) {
        this.buf[n] = (byte)(n2 >>> 24);
        this.buf[n + 1] = (byte)(n2 >>> 16);
        this.buf[n + 2] = (byte)(n2 >>> 8);
        this.buf[n + 3] = (byte)n2;
    }

    public byte[] toByteArray() {
        byte[] byArray = new byte[this.count];
        System.arraycopy(this.buf, 0, byArray, 0, this.count);
        return byArray;
    }

    public void writeTo(OutputStream outputStream) {
        outputStream.write(this.buf, 0, this.count);
    }

    public void enlarge(int n) {
        int n2 = this.count + n;
        if (n2 > this.buf.length) {
            int n3 = this.buf.length << 1;
            byte[] byArray = new byte[n3 > n2 ? n3 : n2];
            System.arraycopy(this.buf, 0, byArray, 0, this.count);
            this.buf = byArray;
        }
    }
}

