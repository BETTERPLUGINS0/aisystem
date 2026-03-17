/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.nbt.api;

import java.util.Objects;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.util.Codec;
import org.jetbrains.annotations.NotNull;

final class BinaryTagHolderImpl
implements BinaryTagHolder {
    private final String string;

    BinaryTagHolderImpl(String string) {
        this.string = Objects.requireNonNull(string, "string");
    }

    @Override
    @NotNull
    public String string() {
        return this.string;
    }

    @Override
    @NotNull
    public <T, DX extends Exception> T get(@NotNull Codec<T, String, DX, ?> codec) {
        return codec.decode(this.string);
    }

    public int hashCode() {
        return 31 * this.string.hashCode();
    }

    public boolean equals(Object object) {
        if (!(object instanceof BinaryTagHolderImpl)) {
            return false;
        }
        return this.string.equals(((BinaryTagHolderImpl)object).string);
    }

    public String toString() {
        return this.string;
    }
}

