/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$ScheduledForRemoval
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.nbt;

import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.StringBinaryTagImpl;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface StringBinaryTag
extends BinaryTag {
    @NotNull
    public static StringBinaryTag stringBinaryTag(@NotNull String value) {
        return new StringBinaryTagImpl(value);
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion="5.0.0")
    @NotNull
    public static StringBinaryTag of(@NotNull String value) {
        return new StringBinaryTagImpl(value);
    }

    @NotNull
    default public BinaryTagType<StringBinaryTag> type() {
        return BinaryTagTypes.STRING;
    }

    @NotNull
    public String value();
}

