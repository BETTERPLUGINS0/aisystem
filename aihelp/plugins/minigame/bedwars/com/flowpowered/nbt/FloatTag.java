/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class FloatTag
extends Tag<Float> {
    private float value;

    public FloatTag(String name, float value) {
        super(TagType.TAG_FLOAT, name);
        this.value = value;
    }

    @Override
    public Float getValue() {
        return Float.valueOf(this.value);
    }

    @Override
    public void setValue(Float value) {
        this.value = value.floatValue();
    }

    @Override
    public Optional<FloatTag> getAsFloatTag() {
        return Optional.of(this);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Float" + append + ": " + this.value;
    }

    @Override
    public FloatTag clone() {
        return new FloatTag(this.getName(), this.value);
    }
}

