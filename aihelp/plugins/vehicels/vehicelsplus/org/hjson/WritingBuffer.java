/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.hjson;

import java.io.Writer;

class WritingBuffer
extends Writer {
    private final Writer writer;
    private final char[] buffer;
    private int fill = 0;

    WritingBuffer(Writer writer) {
        this(writer, 16);
    }

    WritingBuffer(Writer writer, int n) {
        this.writer = writer;
        this.buffer = new char[n];
    }

    @Override
    public void write(int n) {
        if (this.fill > this.buffer.length - 1) {
            this.flush();
        }
        this.buffer[this.fill++] = (char)n;
    }

    @Override
    public void write(char[] cArray, int n, int n2) {
        if (this.fill > this.buffer.length - n2) {
            this.flush();
            if (n2 > this.buffer.length) {
                this.writer.write(cArray, n, n2);
                return;
            }
        }
        System.arraycopy(cArray, n, this.buffer, this.fill, n2);
        this.fill += n2;
    }

    @Override
    public void write(String string, int n, int n2) {
        if (this.fill > this.buffer.length - n2) {
            this.flush();
            if (n2 > this.buffer.length) {
                this.writer.write(string, n, n2);
                return;
            }
        }
        string.getChars(n, n + n2, this.buffer, this.fill);
        this.fill += n2;
    }

    @Override
    public void flush() {
        this.writer.write(this.buffer, 0, this.fill);
        this.fill = 0;
    }

    @Override
    public void close() {
    }
}

