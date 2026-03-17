/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.api.nbt;

public interface NBTSerializable<T> {
    public String serialize(T var1);

    public T deserialize(String var1);
}

