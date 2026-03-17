/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.pointer;

import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.pointer.Pointer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PointerImpl<T>
implements Pointer<T> {
    private final Class<T> type;
    private final Key key;

    PointerImpl(Class<T> clazz, Key key) {
        this.type = clazz;
        this.key = key;
    }

    @Override
    @NotNull
    public Class<T> type() {
        return this.type;
    }

    @Override
    @NotNull
    public Key key() {
        return this.key;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        PointerImpl pointerImpl = (PointerImpl)object;
        return this.type.equals(pointerImpl.type) && this.key.equals(pointerImpl.key);
    }

    public int hashCode() {
        int n = this.type.hashCode();
        n = 31 * n + this.key.hashCode();
        return n;
    }
}

