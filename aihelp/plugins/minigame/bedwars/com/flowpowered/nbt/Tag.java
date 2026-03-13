/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.EndTag;
import com.flowpowered.nbt.FloatTag;
import com.flowpowered.nbt.IntArrayTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.LongArrayTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.ShortArrayTag;
import com.flowpowered.nbt.ShortTag;
import com.flowpowered.nbt.StringTag;
import com.flowpowered.nbt.TagType;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class Tag<T>
implements Comparable<Tag<?>> {
    private final String name;
    private final TagType type;

    public Tag(TagType type) {
        this(type, "");
    }

    public Tag(TagType type, String name) {
        this.name = name;
        this.type = type;
    }

    public final String getName() {
        return this.name;
    }

    public TagType getType() {
        return this.type;
    }

    public abstract T getValue();

    public abstract void setValue(T var1);

    public Optional<EndTag> getAsEndTag() {
        return Optional.empty();
    }

    public Optional<ByteTag> getAsByteTag() {
        return Optional.empty();
    }

    public Optional<ShortTag> getAsShortTag() {
        return Optional.empty();
    }

    public Optional<IntTag> getAsIntTag() {
        return Optional.empty();
    }

    public Optional<LongTag> getAsLongTag() {
        return Optional.empty();
    }

    public Optional<FloatTag> getAsFloatTag() {
        return Optional.empty();
    }

    public Optional<DoubleTag> getAsDoubleTag() {
        return Optional.empty();
    }

    public Optional<ByteArrayTag> getAsByteArrayTag() {
        return Optional.empty();
    }

    public Optional<StringTag> getAsStringTag() {
        return Optional.empty();
    }

    public Optional<ListTag<?>> getAsListTag() {
        return Optional.empty();
    }

    public Optional<CompoundTag> getAsCompoundTag() {
        return Optional.empty();
    }

    public Optional<IntArrayTag> getAsIntArrayTag() {
        return Optional.empty();
    }

    public Optional<LongArrayTag> getAsLongArrayTag() {
        return Optional.empty();
    }

    public Optional<ShortArrayTag> getAsShortArrayTag() {
        return Optional.empty();
    }

    public static Map<String, Tag<?>> cloneMap(Map<String, Tag<?>> map) {
        if (map == null) {
            return null;
        }
        HashMap newMap = new HashMap();
        for (Map.Entry<String, Tag<?>> entry : map.entrySet()) {
            newMap.put(entry.getKey(), (Tag<?>)entry.getValue().clone());
        }
        return newMap;
    }

    public boolean equals(Object other) {
        if (!(other instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag)other;
        return this.getValue().equals(tag.getValue()) && this.getName().equals(tag.getName());
    }

    @Override
    public int compareTo(Tag<?> other) {
        if (this.equals(other)) {
            return 0;
        }
        if (other.getName().equals(this.getName())) {
            throw new IllegalStateException("Cannot compare two Tags with the same name but different values for sorting");
        }
        return this.getName().compareTo(other.getName());
    }

    public abstract Tag<T> clone();
}

