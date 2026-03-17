/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

public class ByteArray {
    public static int readU16bit(byte[] byArray, int n) {
        return (byArray[n] & 0xFF) << 8 | byArray[n + 1] & 0xFF;
    }

    public static int readS16bit(byte[] byArray, int n) {
        return byArray[n] << 8 | byArray[n + 1] & 0xFF;
    }

    public static void write16bit(int n, byte[] byArray, int n2) {
        byArray[n2] = (byte)(n >>> 8);
        byArray[n2 + 1] = (byte)n;
    }

    public static int read32bit(byte[] byArray, int n) {
        return byArray[n] << 24 | (byArray[n + 1] & 0xFF) << 16 | (byArray[n + 2] & 0xFF) << 8 | byArray[n + 3] & 0xFF;
    }

    public static void write32bit(int n, byte[] byArray, int n2) {
        byArray[n2] = (byte)(n >>> 24);
        byArray[n2 + 1] = (byte)(n >>> 16);
        byArray[n2 + 2] = (byte)(n >>> 8);
        byArray[n2 + 3] = (byte)n;
    }

    static void copy32bit(byte[] byArray, int n, byte[] byArray2, int n2) {
        byArray2[n2] = byArray[n];
        byArray2[n2 + 1] = byArray[n + 1];
        byArray2[n2 + 2] = byArray[n + 2];
        byArray2[n2 + 3] = byArray[n + 3];
    }
}

