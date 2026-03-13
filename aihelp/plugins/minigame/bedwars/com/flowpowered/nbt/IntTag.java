/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class IntTag
extends Tag<Integer> {
    private int value;

    public IntTag(String name, int value) {
        super(TagType.TAG_INT, name);
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public Optional<IntTag> getAsIntTag() {
        return Optional.of(this);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Int" + append + ": " + this.value;
    }

    @Override
    public IntTag clone() {
        return new IntTag(this.getName(), this.value);
    }
}

