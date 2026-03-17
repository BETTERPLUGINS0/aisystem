/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTListCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadWriteNBTCompoundList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.iface.ReadableNBT;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTCompoundList
extends NBTList<ReadWriteNBT>
implements ReadWriteNBTCompoundList {
    protected NBTCompoundList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        super(nBTCompound, string, nBTType, object);
    }

    @Override
    public NBTListCompound addCompound() {
        return (NBTListCompound)this.addCompound(null);
    }

    public NBTCompound addCompound(NBTCompound nBTCompound) {
        if (this.getParent().isReadOnly()) {
            throw new NbtApiException("Tried setting data in read only mode!");
        }
        try {
            Object obj = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
            if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
                ReflectionMethod.LIST_ADD.run(this.listObject, this.size(), obj);
            } else {
                ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, obj);
            }
            this.getParent().saveCompound();
            NBTListCompound nBTListCompound = new NBTListCompound(this, obj);
            if (nBTCompound != null) {
                nBTListCompound.mergeCompound(nBTCompound);
            }
            return nBTListCompound;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }

    @Override
    public ReadWriteNBT addCompound(ReadableNBT readableNBT) {
        if (readableNBT instanceof NBTCompound) {
            return this.addCompound((NBTCompound)readableNBT);
        }
        return null;
    }

    @Override
    @Deprecated
    public boolean add(ReadWriteNBT readWriteNBT) {
        return this.addCompound(readWriteNBT) != null;
    }

    @Override
    public void add(int n, ReadWriteNBT readWriteNBT) {
        if (readWriteNBT != null) {
            throw new NbtApiException("You need to pass null! ListCompounds from other lists won't work.");
        }
        if (this.getParent().isReadOnly()) {
            throw new NbtApiException("Tried setting data in read only mode!");
        }
        try {
            Object obj = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
            if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
                ReflectionMethod.LIST_ADD.run(this.listObject, n, obj);
            } else {
                ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, obj);
            }
            super.getParent().saveCompound();
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }

    @Override
    public NBTListCompound get(int n) {
        try {
            Object object = ReflectionMethod.LIST_GET_COMPOUND.run(this.listObject, n);
            return new NBTListCompound(this, object);
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }

    @Override
    public NBTListCompound set(int n, ReadWriteNBT readWriteNBT) {
        throw new NbtApiException("This method doesn't work in the ListCompound context.");
    }

    @Override
    protected Object asTag(ReadWriteNBT readWriteNBT) {
        return null;
    }
}

