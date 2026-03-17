/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.nbt;

import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.nbt.BinaryTag;
import net.kyori.adventure.nbt.BinaryTagType;
import net.kyori.adventure.nbt.BinaryTagTypes;
import net.kyori.adventure.nbt.ListBinaryTag;
import net.kyori.adventure.nbt.ListBinaryTagImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ListTagBuilder<T extends BinaryTag>
implements ListBinaryTag.Builder<T> {
    @Nullable
    private List<BinaryTag> tags;
    private BinaryTagType<? extends BinaryTag> elementType;

    ListTagBuilder() {
        this(BinaryTagTypes.END);
    }

    ListTagBuilder(BinaryTagType<? extends BinaryTag> binaryTagType) {
        this.elementType = binaryTagType;
    }

    @Override
    public @NotNull ListBinaryTag.Builder<T> add(BinaryTag binaryTag) {
        ListBinaryTagImpl.noAddEnd(binaryTag);
        if (this.elementType == BinaryTagTypes.END) {
            this.elementType = binaryTag.type();
        }
        ListBinaryTagImpl.mustBeSameType(binaryTag, this.elementType);
        if (this.tags == null) {
            this.tags = new ArrayList<BinaryTag>();
        }
        this.tags.add(binaryTag);
        return this;
    }

    @Override
    public @NotNull ListBinaryTag.Builder<T> add(Iterable<? extends T> iterable) {
        for (BinaryTag binaryTag : iterable) {
            this.add(binaryTag);
        }
        return this;
    }

    @Override
    @NotNull
    public ListBinaryTag build() {
        if (this.tags == null) {
            return ListBinaryTag.empty();
        }
        return new ListBinaryTagImpl(this.elementType, new ArrayList<BinaryTag>(this.tags));
    }
}

