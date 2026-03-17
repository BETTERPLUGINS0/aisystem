/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.nbt;

import java.io.DataInput;
import java.io.IOException;
import net.kyori.adventure.nbt.BinaryTagScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TrackingDataInput
implements DataInput,
BinaryTagScope {
    private static final int MAX_DEPTH = 512;
    private final DataInput input;
    private final long maxLength;
    private long counter;
    private int depth;

    TrackingDataInput(DataInput dataInput, long l) {
        this.input = dataInput;
        this.maxLength = l;
    }

    public static BinaryTagScope enter(DataInput dataInput) {
        if (dataInput instanceof TrackingDataInput) {
            return ((TrackingDataInput)dataInput).enter();
        }
        return BinaryTagScope.NoOp.INSTANCE;
    }

    public static BinaryTagScope enter(DataInput dataInput, long l) {
        if (dataInput instanceof TrackingDataInput) {
            return ((TrackingDataInput)dataInput).enter(l);
        }
        return BinaryTagScope.NoOp.INSTANCE;
    }

    public DataInput input() {
        return this.input;
    }

    public TrackingDataInput enter(long l) {
        if (this.depth++ > 512) {
            throw new IOException("NBT read exceeded maximum depth of 512");
        }
        this.ensureMaxLength(l);
        return this;
    }

    public TrackingDataInput enter() {
        return this.enter(0L);
    }

    public void exit() {
        --this.depth;
        this.ensureMaxLength(0L);
    }

    private void ensureMaxLength(long l) {
        if (this.maxLength > 0L && this.counter + l > this.maxLength) {
            throw new IOException("The read NBT was longer than the maximum allowed size of " + this.maxLength + " bytes!");
        }
    }

    @Override
    public void readFully(byte @NotNull [] byArray) {
        this.counter += (long)byArray.length;
        this.input.readFully(byArray);
    }

    @Override
    public void readFully(byte @NotNull [] byArray, int n, int n2) {
        this.counter += (long)n2;
        this.input.readFully(byArray, n, n2);
    }

    @Override
    public int skipBytes(int n) {
        return this.input.skipBytes(n);
    }

    @Override
    public boolean readBoolean() {
        ++this.counter;
        return this.input.readBoolean();
    }

    @Override
    public byte readByte() {
        ++this.counter;
        return this.input.readByte();
    }

    @Override
    public int readUnsignedByte() {
        ++this.counter;
        return this.input.readUnsignedByte();
    }

    @Override
    public short readShort() {
        this.counter += 2L;
        return this.input.readShort();
    }

    @Override
    public int readUnsignedShort() {
        this.counter += 2L;
        return this.input.readUnsignedShort();
    }

    @Override
    public char readChar() {
        this.counter += 2L;
        return this.input.readChar();
    }

    @Override
    public int readInt() {
        this.counter += 4L;
        return this.input.readInt();
    }

    @Override
    public long readLong() {
        this.counter += 8L;
        return this.input.readLong();
    }

    @Override
    public float readFloat() {
        this.counter += 4L;
        return this.input.readFloat();
    }

    @Override
    public double readDouble() {
        this.counter += 8L;
        return this.input.readDouble();
    }

    @Override
    @Nullable
    public String readLine() {
        @Nullable String string = this.input.readLine();
        if (string != null) {
            this.counter += (long)(string.length() + 1);
        }
        return string;
    }

    @Override
    @NotNull
    public String readUTF() {
        String string = this.input.readUTF();
        this.counter += (long)string.length() * 2L + 2L;
        return string;
    }

    @Override
    public void close() {
        this.exit();
    }
}

