/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class LongTag
extends Tag<Long> {
    private long value;

    public LongTag(String name, long value) {
        super(TagType.TAG_LONG, name);
        this.value = value;
    }

    @Override
    public Long getValue() {
        return this.value;
    }

    @Override
    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public Optional<LongTag> getAsLongTag() {
        return Optional.of(this);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Long" + append + ": " + this.value;
    }

    @Override
    public LongTag clone() {
        return new LongTag(this.getName(), this.value);
    }
}

