/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.nbt;

import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.IntBinaryTagImpl;
import net.kyori.adventure.nbt.NumberBinaryTag;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface IntBinaryTag
extends NumberBinaryTag {
    @NotNull
    public static IntBinaryTag intBinaryTag(int value) {
        return new IntBinaryTagImpl(value);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static IntBinaryTag of(int value) {
        return new IntBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<IntBinaryTag> type() {
        return BinaryTagTypes.INT;
    }

    public int value();
}

