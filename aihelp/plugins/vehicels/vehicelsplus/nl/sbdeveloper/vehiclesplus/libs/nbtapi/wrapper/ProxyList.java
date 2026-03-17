/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper.NBTProxy;

public interface ProxyList<T extends NBTProxy>
extends Iterable<T> {
    public T addCompound();

    public int size();

    public boolean isEmpty();

    public T get(int var1);

    public void remove(int var1);
}

