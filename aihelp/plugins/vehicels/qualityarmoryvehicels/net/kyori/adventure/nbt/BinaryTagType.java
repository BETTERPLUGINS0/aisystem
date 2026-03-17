/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BinaryTagType<T extends BinaryTag>
implements Predicate<BinaryTagType<? extends BinaryTag>> {
    private static final List<BinaryTagType<? extends BinaryTag>> TYPES = new ArrayList<BinaryTagType<? extends BinaryTag>>();

    public abstract byte id();

    abstract boolean numeric();

    @NotNull
    public abstract T read(@NotNull DataInput var1);

    public abstract void write(@NotNull T var1, @NotNull DataOutput var2);

    static <T extends BinaryTag> void writeUntyped(BinaryTagType<? extends BinaryTag> binaryTagType, T t, DataOutput dataOutput) {
        binaryTagType.write(t, dataOutput);
    }

    @NotNull
    static BinaryTagType<? extends BinaryTag> binaryTagType(byte by) {
        for (int i = 0; i < TYPES.size(); ++i) {
            BinaryTagType<? extends BinaryTag> binaryTagType = TYPES.get(i);
            if (binaryTagType.id() != by) continue;
            return binaryTagType;
        }
        throw new IllegalArgumentException(String.valueOf(by));
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    static BinaryTagType<? extends BinaryTag> of(byte by) {
        return BinaryTagType.binaryTagType(by);
    }

    @NotNull
    static <T extends BinaryTag> BinaryTagType<T> register(Class<T> clazz, byte by, Reader<T> reader, @Nullable Writer<T> writer) {
        return BinaryTagType.register(new Impl<T>(clazz, by, reader, writer));
    }

    @NotNull
    static <T extends NumberBinaryTag> BinaryTagType<T> registerNumeric(Class<T> clazz, byte by, Reader<T> reader, Writer<T> writer) {
        return BinaryTagType.register(new Impl.Numeric<T>(clazz, by, reader, writer));
    }

    private static <T extends BinaryTag, Y extends BinaryTagType<T>> Y register(Y y) {
        TYPES.add(y);
        return y;
    }

    @Override
    public boolean test(BinaryTagType<? extends BinaryTag> binaryTagType) {
        return this == binaryTagType || this.numeric() && binaryTagType.numeric();
    }

    static class Impl<T extends BinaryTag>
    extends BinaryTagType<T> {
        final Class<T> type;
        final byte id;
        private final Reader<T> reader;
        @Nullable
        private final Writer<T> writer;

        Impl(Class<T> clazz, byte by, Reader<T> reader, @Nullable Writer<T> writer) {
            this.type = clazz;
            this.id = by;
            this.reader = reader;
            this.writer = writer;
        }

        @Override
        @NotNull
        public final T read(@NotNull DataInput dataInput) {
            return this.reader.read(dataInput);
        }

        @Override
        public final void write(@NotNull T t, @NotNull DataOutput dataOutput) {
            if (this.writer != null) {
                this.writer.write(t, dataOutput);
            }
        }

        @Override
        public final byte id() {
            return this.id;
        }

        @Override
        boolean numeric() {
            return false;
        }

        public String toString() {
            return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + "]";
        }

        static class Numeric<T extends BinaryTag>
        extends Impl<T> {
            Numeric(Class<T> clazz, byte by, Reader<T> reader, @Nullable Writer<T> writer) {
                super(clazz, by, reader, writer);
            }

            @Override
            boolean numeric() {
                return true;
            }

            @Override
            public String toString() {
                return BinaryTagType.class.getSimpleName() + '[' + this.type.getSimpleName() + " " + this.id + " (numeric)]";
            }
        }
    }

    static interface Writer<T extends BinaryTag> {
        public void write(@NotNull T var1, @NotNull DataOutput var2) throws IOException;
    }

    static interface Reader<T extends BinaryTag> {
        @NotNull
        public T read(@NotNull DataInput var1) throws IOException;
    }
}

