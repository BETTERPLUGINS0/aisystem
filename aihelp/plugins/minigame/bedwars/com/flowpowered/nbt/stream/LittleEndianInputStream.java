/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt.stream;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class LittleEndianInputStream
extends FilterInputStream
implements DataInput {
    public LittleEndianInputStream(InputStream stream) {
        super(stream instanceof DataInputStream ? stream : new DataInputStream(stream));
    }

    protected DataInputStream getBackingStream() {
        return (DataInputStream)this.in;
    }

    @Override
    public void readFully(byte[] bytes) throws IOException {
        this.getBackingStream().readFully(bytes);
    }

    @Override
    public void readFully(byte[] bytes, int i, int i1) throws IOException {
        this.getBackingStream().readFully(bytes, i, i1);
    }

    @Override
    public int skipBytes(int i) throws IOException {
        return this.getBackingStream().skipBytes(i);
    }

    @Override
    public boolean readBoolean() throws IOException {
        return this.getBackingStream().readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return this.getBackingStream().readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return this.getBackingStream().readUnsignedByte();
    }

    @Override
    public short readShort() throws IOException {
        return Short.reverseBytes(this.getBackingStream().readShort());
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return (char)(Integer.reverseBytes(this.getBackingStream().readUnsignedShort()) >> 16);
    }

    @Override
    public char readChar() throws IOException {
        return Character.reverseBytes(this.getBackingStream().readChar());
    }

    @Override
    public int readInt() throws IOException {
        return Integer.reverseBytes(this.getBackingStream().readInt());
    }

    @Override
    public long readLong() throws IOException {
        return Long.reverseBytes(this.getBackingStream().readLong());
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(this.readInt()));
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(Long.reverseBytes(this.readLong()));
    }

    @Override
    public String readLine() throws IOException {
        return this.getBackingStream().readLine();
    }

    @Override
    public String readUTF() throws IOException {
        return this.getBackingStream().readUTF();
    }
}

