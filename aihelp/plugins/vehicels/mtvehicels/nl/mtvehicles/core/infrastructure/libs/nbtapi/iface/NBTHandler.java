/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.iface;

import javax.annotation.Nonnull;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadWriteNBT;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.iface.ReadableNBT;

public interface NBTHandler<T> {
    default public boolean fuzzyMatch(Object obj) {
        return false;
    }

    public void set(@Nonnull ReadWriteNBT var1, @Nonnull String var2, @Nonnull T var3);

    public T get(@Nonnull ReadableNBT var1, @Nonnull String var2);
}

