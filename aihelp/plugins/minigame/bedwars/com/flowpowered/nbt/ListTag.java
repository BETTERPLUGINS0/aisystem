/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.FloatTag;
import com.flowpowered.nbt.IntArrayTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.LongArrayTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.ShortArrayTag;
import com.flowpowered.nbt.ShortTag;
import com.flowpowered.nbt.StringTag;
import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ListTag<T extends Tag<?>>
extends Tag<List<T>> {
    private final TagType type;
    private List<T> value;

    public ListTag(String name, TagType type, List<T> value) {
        super(TagType.TAG_LIST, name);
        this.type = type;
        this.value = value;
    }

    public TagType getElementType() {
        return this.type;
    }

    @Override
    public List<T> getValue() {
        return this.value;
    }

    @Override
    public void setValue(List<T> value) {
        this.value = value;
    }

    @Override
    public Optional<ListTag<?>> getAsListTag() {
        return Optional.of(this);
    }

    private <T extends Tag<?>> Optional<ListTag<T>> getAsList(TagType type) {
        if (this.type == type) {
            return Optional.of(this);
        }
        return Optional.empty();
    }

    public Optional<ListTag<ByteTag>> getAsByteTagList() {
        return this.getAsList(TagType.TAG_BYTE);
    }

    public Optional<ListTag<ShortTag>> getAsShortTagList() {
        return this.getAsList(TagType.TAG_SHORT);
    }

    public Optional<ListTag<IntTag>> getAsIntTagList() {
        return this.getAsList(TagType.TAG_INT);
    }

    public Optional<ListTag<LongTag>> getAsLongTagList() {
        return this.getAsList(TagType.TAG_LONG);
    }

    public Optional<ListTag<FloatTag>> getAsFloatTagList() {
        return this.getAsList(TagType.TAG_FLOAT);
    }

    public Optional<ListTag<DoubleTag>> getAsDoubleTagList() {
        return this.getAsList(TagType.TAG_DOUBLE);
    }

    public Optional<ListTag<ByteArrayTag>> getAsByteArrayTagList() {
        return this.getAsList(TagType.TAG_BYTE_ARRAY);
    }

    public Optional<ListTag<StringTag>> getAsStringTagList() {
        return this.getAsList(TagType.TAG_STRING);
    }

    public Optional<ListTag<ListTag<?>>> getAsListTagList() {
        return this.getAsList(TagType.TAG_LIST);
    }

    public Optional<ListTag<CompoundTag>> getAsCompoundTagList() {
        return this.getAsList(TagType.TAG_COMPOUND);
    }

    public Optional<ListTag<IntArrayTag>> getAsIntArrayTagList() {
        return this.getAsList(TagType.TAG_INT_ARRAY);
    }

    public Optional<ListTag<LongArrayTag>> getAsLongArrayTagList() {
        return this.getAsList(TagType.TAG_LONG_ARRAY);
    }

    public Optional<ListTag<ShortArrayTag>> getAsShortArrayTagList() {
        return this.getAsList(TagType.TAG_SHORT_ARRAY);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_List").append(append).append(": ").append(this.value.size()).append(" entries of type ").append(this.type.getTypeName()).append("\r\n{\r\n");
        for (Tag t : this.value) {
            bldr.append("   ").append(t.toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }

    @Override
    public ListTag<T> clone() {
        ArrayList<Object> newList = new ArrayList<Object>();
        for (Tag v : this.value) {
            newList.add(v.clone());
        }
        return new ListTag(this.getName(), this.type, newList);
    }
}

