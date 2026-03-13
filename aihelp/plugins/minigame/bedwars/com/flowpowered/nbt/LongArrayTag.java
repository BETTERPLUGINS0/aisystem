/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.flowpowered.nbt;

import com.flowpowered.nbt.Tag;
import com.flowpowered.nbt.TagType;
import java.util.Arrays;
import java.util.Optional;

public class LongArrayTag
extends Tag<long[]> {
    private long[] value;

    public LongArrayTag(String name, long[] value) {
        super(TagType.TAG_LONG_ARRAY, name);
        this.value = value;
    }

    @Override
    public long[] getValue() {
        return this.value;
    }

    @Override
    public void setValue(long[] value) {
        this.value = value;
    }

    @Override
    public Optional<LongArrayTag> getAsLongArrayTag() {
        return Optional.of(this);
    }

    public String toString() {
        StringBuilder hex = new StringBuilder();
        for (long s : this.value) {
            String hexDigits = Long.toHexString(s).toUpperCase();
            if (hexDigits.length() == 1) {
                hex.append("0");
            }
            hex.append(hexDigits).append(" ");
        }
        String name = this.getName();
        String append = "";
        if (name != null && !name.equals("")) {
            append = "(\"" + this.getName() + "\")";
        }
        return "TAG_Long_Array" + append + ": " + hex.toString();
    }

    @Override
    public LongArrayTag clone() {
        long[] clonedArray = this.cloneArray(this.value);
        return new LongArrayTag(this.getName(), clonedArray);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LongArrayTag)) {
            return false;
        }
        LongArrayTag tag = (LongArrayTag)other;
        return Arrays.equals(this.value, tag.value) && this.getName().equals(tag.getName());
    }

    private long[] cloneArray(long[] longArray) {
        if (longArray == null) {
            return null;
        }
        int length = longArray.length;
        byte[] newArray = new byte[length];
        System.arraycopy(longArray, 0, newArray, 0, length);
        return longArray;
    }
}

