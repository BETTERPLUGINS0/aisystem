/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.nbt;

import java.io.BufferedInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Map;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IOStreamUtil;
import net.kyori.adventure.nbt.TrackingDataInput;
import org.jetbrains.annotations.NotNull;

final class BinaryTagReaderImpl
implements BinaryTagIO.Reader {
    private final long maxBytes;
    static final BinaryTagIO.Reader UNLIMITED = new BinaryTagReaderImpl(-1L);
    static final BinaryTagIO.Reader DEFAULT_LIMIT = new BinaryTagReaderImpl(131082L);

    BinaryTagReaderImpl(long l) {
        this.maxBytes = l;
    }

    @Override
    @NotNull
    public CompoundBinaryTag read(@NotNull Path path, @NotNull BinaryTagIO.Compression compression) {
        try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
            CompoundBinaryTag compoundBinaryTag = this.read(inputStream, compression);
            return compoundBinaryTag;
        }
    }

    @Override
    @NotNull
    public CompoundBinaryTag read(@NotNull InputStream inputStream, @NotNull BinaryTagIO.Compression compression) {
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(inputStream))));){
            CompoundBinaryTag compoundBinaryTag = this.read(dataInputStream);
            return compoundBinaryTag;
        }
    }

    @Override
    @NotNull
    public CompoundBinaryTag read(@NotNull DataInput dataInput) {
        return this.read(dataInput, true);
    }

    @NotNull
    private CompoundBinaryTag read(@NotNull DataInput dataInput, boolean bl) {
        if (!(dataInput instanceof TrackingDataInput)) {
            dataInput = new TrackingDataInput(dataInput, this.maxBytes);
        }
        BinaryTagType<BinaryTag> binaryTagType = BinaryTagType.binaryTagType(dataInput.readByte());
        BinaryTagReaderImpl.requireCompound(binaryTagType);
        if (bl) {
            dataInput.skipBytes(dataInput.readUnsignedShort());
        }
        return BinaryTagTypes.COMPOUND.read(dataInput);
    }

    @Override
    @NotNull
    public CompoundBinaryTag readNameless(@NotNull Path path, @NotNull BinaryTagIO.Compression compression) {
        try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
            CompoundBinaryTag compoundBinaryTag = this.readNameless(inputStream, compression);
            return compoundBinaryTag;
        }
    }

    @Override
    @NotNull
    public CompoundBinaryTag readNameless(@NotNull InputStream inputStream, @NotNull BinaryTagIO.Compression compression) {
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(inputStream))));){
            CompoundBinaryTag compoundBinaryTag = this.readNameless(dataInputStream);
            return compoundBinaryTag;
        }
    }

    @Override
    @NotNull
    public CompoundBinaryTag readNameless(@NotNull DataInput dataInput) {
        return this.read(dataInput, false);
    }

    @Override
    public  @NotNull Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull Path path, @NotNull BinaryTagIO.Compression compression) {
        try (InputStream inputStream = Files.newInputStream(path, new OpenOption[0]);){
            Map.Entry<String, CompoundBinaryTag> entry = this.readNamed(inputStream, compression);
            return entry;
        }
    }

    @Override
    public  @NotNull Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull InputStream inputStream, @NotNull BinaryTagIO.Compression compression) {
        try (DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(compression.decompress(IOStreamUtil.closeShield(inputStream))));){
            Map.Entry<String, CompoundBinaryTag> entry = this.readNamed(dataInputStream);
            return entry;
        }
    }

    @Override
    public  @NotNull Map.Entry<String, CompoundBinaryTag> readNamed(@NotNull DataInput dataInput) {
        BinaryTagType<BinaryTag> binaryTagType = BinaryTagType.binaryTagType(dataInput.readByte());
        BinaryTagReaderImpl.requireCompound(binaryTagType);
        String string = dataInput.readUTF();
        return new AbstractMap.SimpleImmutableEntry<String, CompoundBinaryTag>(string, BinaryTagTypes.COMPOUND.read(dataInput));
    }

    private static void requireCompound(BinaryTagType<? extends BinaryTag> binaryTagType) {
        if (binaryTagType != BinaryTagTypes.COMPOUND) {
            throw new IOException(String.format("Expected root tag to be a %s, was %s", BinaryTagTypes.COMPOUND, binaryTagType));
        }
    }
}

