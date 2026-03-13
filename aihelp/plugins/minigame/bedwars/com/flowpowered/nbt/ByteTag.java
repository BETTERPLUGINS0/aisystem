/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class ByteTag
extends Tag<Byte> {
    private byte value;

    public ByteTag(String name, boolean value) {
        this(name, (byte)(value ? 1 : 0));
    }

    public ByteTag(String name, byte value) {
        super(TagType.TAG_BYTE, name);
        this.value = value;
    }

    @Override
    public Byte getValue() {
        return this.value;
    }

    @Override
    public void setValue(Byte value) {
        this.value = value;
    }

    public boolean getBooleanValue() {
        return this.value != 0;
    }

    @Override
    public Optional<ByteTag> getAsByteTag() {
        return Optional.of(this);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Byte" + append + ": " + this.value;
    }

    @Override
    public ByteTag clone() {
        return new ByteTag(this.getName(), this.value);
    }

    public static Boolean getBooleanValue(Tag<?> t) {
        if (t == null) {
            return null;
        }
        try {
            ByteTag byteTag = (ByteTag)t;
            return byteTag.getBooleanValue();
        } catch (ClassCastException e) {
            return null;
        }
    }
}

