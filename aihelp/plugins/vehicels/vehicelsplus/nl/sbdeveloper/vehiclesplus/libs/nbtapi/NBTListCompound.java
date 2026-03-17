/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;

public class NBTListCompound
extends NBTCompound {
    private NBTList<?> owner;
    private Object compound;

    protected NBTListCompound(NBTList<?> nBTList, Object object) {
        super(null, null);
        this.owner = nBTList;
        this.compound = object;
    }

    public NBTList<?> getListParent() {
        return this.owner;
    }

    @Override
    protected boolean isClosed() {
        return this.owner.getParent().isClosed();
    }

    @Override
    protected boolean isReadOnly() {
        return this.owner.getParent().isReadOnly();
    }

    @Override
    public Object getCompound() {
        if (this.isClosed()) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
        return this.compound;
    }

    @Override
    protected void setCompound(Object object) {
        if (this.isClosed()) {
            throw new NbtApiException("Tried using closed NBT data!");
        }
        if (this.isReadOnly()) {
            throw new NbtApiException("Tried setting data in read only mode!");
        }
        this.compound = object;
    }

    @Override
    protected void saveCompound() {
        this.owner.save();
    }
}

