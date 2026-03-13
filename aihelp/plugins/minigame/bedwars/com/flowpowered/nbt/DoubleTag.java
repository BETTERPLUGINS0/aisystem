/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class DoubleTag
extends Tag<Double> {
    private double value;

    public DoubleTag(String name, double value) {
        super(TagType.TAG_DOUBLE, name);
        this.value = value;
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public Optional<DoubleTag> getAsDoubleTag() {
        return Optional.of(this);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Double" + append + ": " + this.value;
    }

    @Override
    public DoubleTag clone() {
        return new DoubleTag(this.getName(), this.value);
    }
}

