/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;

public interface ReadableNBTList<T>
extends Iterable<T> {
    public T get(int var1);

    public int size();

    public NBTType getType();

    public boolean isEmpty();

    public boolean contains(Object var1);

    public int indexOf(Object var1);

    public boolean containsAll(Collection<?> var1);

    public int lastIndexOf(Object var1);

    public Object[] toArray();

    public <E> E[] toArray(E[] var1);

    public List<T> subList(int var1, int var2);

    default public List<T> toListCopy() {
        ArrayList list = new ArrayList();
        this.iterator().forEachRemaining(list::add);
        return list;
    }
}

