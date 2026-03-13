/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt.stream;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LittleEndianOutputStream
extends FilterOutputStream
implements DataOutput {
    public LittleEndianOutputStream(OutputStream backingStream) {
        super(backingStream instanceof DataOutputStream ? (DataOutputStream)backingStream : new DataOutputStream(backingStream));
    }

    protected DataOutputStream getBackingStream() {
        return (DataOutputStream)this.out;
    }

    @Override
    public void writeBoolean(boolean b2) throws IOException {
        this.getBackingStream().writeBoolean(b2);
    }

    @Override
    public void writeByte(int i) throws IOException {
        this.getBackingStream().writeByte(i);
    }

    @Override
    public void writeShort(int i) throws IOException {
        this.getBackingStream().writeShort(Integer.reverseBytes(i) >> 16);
    }

    @Override
    public void writeChar(int i) throws IOException {
        this.getBackingStream().writeChar(Character.reverseBytes((char)i));
    }

    @Override
    public void writeInt(int i) throws IOException {
        this.getBackingStream().writeInt(Integer.reverseBytes(i));
    }

    @Override
    public void writeLong(long l) throws IOException {
        this.getBackingStream().writeLong(Long.reverseBytes(l));
    }

    @Override
    public void writeFloat(float v) throws IOException {
        this.getBackingStream().writeInt(Integer.reverseBytes(Float.floatToIntBits(v)));
    }

    @Override
    public void writeDouble(double v) throws IOException {
        this.getBackingStream().writeLong(Long.reverseBytes(Double.doubleToLongBits(v)));
    }

    @Override
    public void writeBytes(String s) throws IOException {
        this.getBackingStream().writeBytes(s);
    }

    @Override
    public void writeChars(String s) throws IOException {
        this.getBackingStream().writeChars(s);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        this.getBackingStream().writeUTF(s);
    }
}

