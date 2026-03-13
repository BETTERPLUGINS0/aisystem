/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class ShortTag
extends Tag<Short> {
    private short value;

    public ShortTag(String name, short value) {
        super(TagType.TAG_SHORT, name);
        this.value = value;
    }

    @Override
    public Short getValue() {
        return this.value;
    }

    @Override
    public void setValue(Short value) {
        this.value = value;
    }

    @Override
    public Optional<ShortTag> getAsShortTag() {
        return Optional.of(this);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Short" + append + ": " + this.value;
    }

    @Override
    public ShortTag clone() {
        return new ShortTag(this.getName(), this.value);
    }
}

