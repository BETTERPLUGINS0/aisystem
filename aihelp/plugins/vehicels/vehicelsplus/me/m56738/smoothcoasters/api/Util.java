/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.m56738.smoothcoasters.api;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

class Util {
    Util() {
    }

    public static int readVarInt(ByteBuffer byteBuffer) {
        byte by;
        int n = 0;
        int n2 = 0;
        do {
            by = byteBuffer.get();
            n2 |= (by & 0x7F) << n * 7;
            if (++n <= 5) continue;
            throw new IllegalArgumentException();
        } while ((by & 0x80) != 0);
        return n2;
    }

    public static String readString(ByteBuffer byteBuffer, int n) {
        int n2 = Util.readVarInt(byteBuffer);
        if (n2 > byteBuffer.remaining()) {
            throw new BufferUnderflowException();
        }
        if (n2 > n) {
            throw new IllegalArgumentException("String too long: " + n2 + " > " + n);
        }
        byte[] byArray = new byte[n2];
        byteBuffer.get(byArray);
        return new String(byArray);
    }
}

