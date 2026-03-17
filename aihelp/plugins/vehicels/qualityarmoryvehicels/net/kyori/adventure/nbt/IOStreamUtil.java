/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.kyori.adventure.nbt;

import java.io.InputStream;
import java.io.OutputStream;

final class IOStreamUtil {
    private IOStreamUtil() {
    }

    static InputStream closeShield(final InputStream inputStream) {
        return new InputStream(){

            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public int read(byte[] byArray) {
                return inputStream.read(byArray);
            }

            @Override
            public int read(byte[] byArray, int n, int n2) {
                return inputStream.read(byArray, n, n2);
            }
        };
    }

    static OutputStream closeShield(final OutputStream outputStream) {
        return new OutputStream(){

            @Override
            public void write(int n) {
                outputStream.write(n);
            }

            @Override
            public void write(byte[] byArray) {
                outputStream.write(byArray);
            }

            @Override
            public void write(byte[] byArray, int n, int n2) {
                outputStream.write(byArray, n, n2);
            }
        };
    }
}

