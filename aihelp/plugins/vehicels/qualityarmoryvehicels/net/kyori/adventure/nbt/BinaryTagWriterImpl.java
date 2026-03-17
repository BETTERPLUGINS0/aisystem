/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.nbt;

import java.io.BufferedOutputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.IOStreamUtil;
import org.jetbrains.annotations.NotNull;

final class BinaryTagWriterImpl
implements BinaryTagIO.Writer {
    static final BinaryTagIO.Writer INSTANCE = new BinaryTagWriterImpl();

    BinaryTagWriterImpl() {
    }

    @Override
    public void write(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull Path path, @NotNull BinaryTagIO.Compression compression) {
        try (OutputStream outputStream = Files.newOutputStream(path, new OpenOption[0]);){
            this.write(compoundBinaryTag, outputStream, compression);
        }
    }

    @Override
    public void write(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull OutputStream outputStream, @NotNull BinaryTagIO.Compression compression) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(outputStream))));){
            this.write(compoundBinaryTag, dataOutputStream);
        }
    }

    @Override
    public void write(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull DataOutput dataOutput) {
        this.write(compoundBinaryTag, dataOutput, true);
    }

    private void write(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull DataOutput dataOutput, boolean bl) {
        dataOutput.writeByte(BinaryTagTypes.COMPOUND.id());
        if (bl) {
            dataOutput.writeUTF("");
        }
        BinaryTagTypes.COMPOUND.write(compoundBinaryTag, dataOutput);
    }

    @Override
    public void writeNameless(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull Path path, @NotNull BinaryTagIO.Compression compression) {
        try (OutputStream outputStream = Files.newOutputStream(path, new OpenOption[0]);){
            this.writeNameless(compoundBinaryTag, outputStream, compression);
        }
    }

    @Override
    public void writeNameless(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull OutputStream outputStream, @NotNull BinaryTagIO.Compression compression) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(outputStream))));){
            this.writeNameless(compoundBinaryTag, dataOutputStream);
        }
    }

    @Override
    public void writeNameless(@NotNull CompoundBinaryTag compoundBinaryTag, @NotNull DataOutput dataOutput) {
        this.write(compoundBinaryTag, dataOutput, false);
    }

    @Override
    public void writeNamed( @NotNull Map.Entry<String, CompoundBinaryTag> entry, @NotNull Path path, @NotNull BinaryTagIO.Compression compression) {
        try (OutputStream outputStream = Files.newOutputStream(path, new OpenOption[0]);){
            this.writeNamed(entry, outputStream, compression);
        }
    }

    @Override
    public void writeNamed( @NotNull Map.Entry<String, CompoundBinaryTag> entry, @NotNull OutputStream outputStream, @NotNull BinaryTagIO.Compression compression) {
        try (DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(compression.compress(IOStreamUtil.closeShield(outputStream))));){
            this.writeNamed(entry, dataOutputStream);
        }
    }

    @Override
    public void writeNamed( @NotNull Map.Entry<String, CompoundBinaryTag> entry, @NotNull DataOutput dataOutput) {
        dataOutput.writeByte(BinaryTagTypes.COMPOUND.id());
        dataOutput.writeUTF(entry.getKey());
        BinaryTagTypes.COMPOUND.write(entry.getValue(), dataOutput);
    }
}

