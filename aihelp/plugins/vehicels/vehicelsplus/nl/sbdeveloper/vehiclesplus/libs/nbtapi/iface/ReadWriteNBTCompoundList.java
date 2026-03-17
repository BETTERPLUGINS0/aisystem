/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface;

import java.util.function.Predicate;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBTList;

public interface ReadWriteNBTCompoundList
extends ReadableNBTList<ReadWriteNBT> {
    public ReadWriteNBT addCompound();

    public ReadWriteNBT addCompound(ReadableNBT var1);

    public ReadWriteNBT remove(int var1);

    public void clear();

    public boolean removeIf(Predicate<? super ReadWriteNBT> var1);
}

