/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.nbt;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import net.kyori.adventure.nbt.CharBuffer;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.kyori.adventure.nbt.StringTagParseException;
import net.kyori.adventure.nbt.TagStringReader;
import net.kyori.adventure.nbt.TagStringWriter;
import org.jetbrains.annotations.NotNull;

public final class TagStringIO {
    private static final TagStringIO INSTANCE = new TagStringIO(new Builder());
    private final boolean acceptLegacy;
    private final boolean emitLegacy;
    private final String indent;

    @NotNull
    public static TagStringIO get() {
        return INSTANCE;
    }

    @NotNull
    public static Builder builder() {
        return new Builder();
    }

    private TagStringIO(@NotNull Builder builder) {
        this.acceptLegacy = builder.acceptLegacy;
        this.emitLegacy = builder.emitLegacy;
        this.indent = builder.indent;
    }

    public CompoundBinaryTag asCompound(String string) {
        try {
            CharBuffer charBuffer = new CharBuffer(string);
            TagStringReader tagStringReader = new TagStringReader(charBuffer);
            tagStringReader.legacy(this.acceptLegacy);
            CompoundBinaryTag compoundBinaryTag = tagStringReader.compound();
            if (charBuffer.skipWhitespace().hasMore()) {
                throw new IOException("Document had trailing content after first CompoundTag");
            }
            return compoundBinaryTag;
        } catch (StringTagParseException stringTagParseException) {
            throw new IOException(stringTagParseException);
        }
    }

    public String asString(CompoundBinaryTag compoundBinaryTag) {
        StringBuilder stringBuilder = new StringBuilder();
        try (TagStringWriter tagStringWriter = new TagStringWriter(stringBuilder, this.indent);){
            tagStringWriter.legacy(this.emitLegacy);
            tagStringWriter.writeTag(compoundBinaryTag);
        }
        return stringBuilder.toString();
    }

    public void toWriter(CompoundBinaryTag compoundBinaryTag, Writer writer) {
        try (TagStringWriter tagStringWriter = new TagStringWriter(writer, this.indent);){
            tagStringWriter.legacy(this.emitLegacy);
            tagStringWriter.writeTag(compoundBinaryTag);
        }
    }

    public static class Builder {
        private boolean acceptLegacy = true;
        private boolean emitLegacy = false;
        private String indent = "";

        Builder() {
        }

        @NotNull
        public Builder indent(int n) {
            if (n == 0) {
                this.indent = "";
            } else if (this.indent.length() > 0 && this.indent.charAt(0) != ' ' || n != this.indent.length()) {
                char[] cArray = new char[n];
                Arrays.fill(cArray, ' ');
                this.indent = String.copyValueOf(cArray);
            }
            return this;
        }

        @NotNull
        public Builder indentTab(int n) {
            if (n == 0) {
                this.indent = "";
            } else if (this.indent.length() > 0 && this.indent.charAt(0) != '\t' || n != this.indent.length()) {
                char[] cArray = new char[n];
                Arrays.fill(cArray, '\t');
                this.indent = String.copyValueOf(cArray);
            }
            return this;
        }

        @NotNull
        public Builder acceptLegacy(boolean bl) {
            this.acceptLegacy = bl;
            return this;
        }

        @NotNull
        public Builder emitLegacy(boolean bl) {
            this.emitLegacy = bl;
            return this;
        }

        @NotNull
        public TagStringIO build() {
            return new TagStringIO(this);
        }
    }
}

