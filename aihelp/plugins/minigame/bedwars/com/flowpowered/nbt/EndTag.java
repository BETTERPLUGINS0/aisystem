/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public final class EndTag
extends Tag<Object> {
    public EndTag() {
        super(TagType.TAG_END);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<EndTag> getAsEndTag() {
        return Optional.of(this);
    }

    public String toString() {
        return "TAG_End";
    }

    @Override
    public EndTag clone() {
        return new EndTag();
    }
}

