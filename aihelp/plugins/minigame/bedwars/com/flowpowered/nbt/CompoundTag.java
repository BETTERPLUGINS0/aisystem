/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.ByteTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.DoubleTag;
import com.flowpowered.nbt.FloatTag;
import com.flowpowered.nbt.IntArrayTag;
import com.flowpowered.nbt.IntTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.LongArrayTag;
import com.flowpowered.nbt.LongTag;
import com.flowpowered.nbt.ShortArrayTag;
import com.flowpowered.nbt.ShortTag;
import com.flowpowered.nbt.StringTag;
import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Optional;

public class CompoundTag
extends Tag<CompoundMap> {
    private CompoundMap value;

    public CompoundTag(String name, CompoundMap value) {
        super(TagType.TAG_COMPOUND, name);
        this.value = value;
    }

    @Override
    public CompoundMap getValue() {
        return this.value;
    }

    @Override
    public void setValue(CompoundMap value) {
        this.value = value;
    }

    @Override
    public Optional<CompoundTag> getAsCompoundTag() {
        return Optional.of(this);
    }

    public Optional<ByteTag> getAsByteTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsByteTag);
    }

    public Optional<ShortTag> getAsShortTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsShortTag);
    }

    public Optional<IntTag> getAsIntTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsIntTag);
    }

    public Optional<LongTag> getAsLongTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsLongTag);
    }

    public Optional<FloatTag> getAsFloatTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsFloatTag);
    }

    public Optional<DoubleTag> getAsDoubleTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsDoubleTag);
    }

    public Optional<ByteArrayTag> getAsByteArrayTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsByteArrayTag);
    }

    public Optional<StringTag> getAsStringTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsStringTag);
    }

    public Optional<ListTag<?>> getAsListTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsListTag);
    }

    public Optional<CompoundTag> getAsCompoundTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsCompoundTag);
    }

    public Optional<IntArrayTag> getAsIntArrayTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsIntArrayTag);
    }

    public Optional<LongArrayTag> getAsLongArrayTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsLongArrayTag);
    }

    public Optional<ShortArrayTag> getAsShortArrayTag(String childName) {
        return Optional.ofNullable(this.value.get(childName)).flatMap(Tag::getAsShortArrayTag);
    }

    public Optional<Byte> getByteValue(String childName) {
        return this.getAsByteTag(childName).map(Tag::getValue);
    }

    public Optional<Short> getShortValue(String childName) {
        return this.getAsShortTag(childName).map(Tag::getValue);
    }

    public Optional<Integer> getIntValue(String childName) {
        return this.getAsIntTag(childName).map(Tag::getValue);
    }

    public Optional<Long> getLongValue(String childName) {
        return this.getAsLongTag(childName).map(Tag::getValue);
    }

    public Optional<Float> getFloatValue(String childName) {
        return this.getAsFloatTag(childName).map(Tag::getValue);
    }

    public Optional<Double> getDoubleValue(String childName) {
        return this.getAsDoubleTag(childName).map(Tag::getValue);
    }

    public Optional<byte[]> getByteArrayValue(String childName) {
        return this.getAsByteArrayTag(childName).map(Tag::getValue);
    }

    public Optional<String> getStringValue(String childName) {
        return this.getAsStringTag(childName).map(Tag::getValue);
    }

    public Optional<int[]> getIntArrayValue(String childName) {
        return this.getAsIntArrayTag(childName).map(Tag::getValue);
    }

    public Optional<long[]> getLongArrayValue(String childName) {
        return this.getAsLongArrayTag(childName).map(Tag::getValue);
    }

    public Optional<short[]> getShortArrayValue(String childName) {
        return this.getAsShortArrayTag(childName).map(Tag::getValue);
    }

    public String toString() {
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound").append(append).append(": ").append(this.value.size()).append(" entries\r\n{\r\n");
        for (Tag<?> entry : this.value.values()) {
            bldr.append("   ").append(entry.toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }

    @Override
    public CompoundTag clone() {
        CompoundMap map = new CompoundMap(this.value);
        return new CompoundTag(this.getName(), map);
    }
}

