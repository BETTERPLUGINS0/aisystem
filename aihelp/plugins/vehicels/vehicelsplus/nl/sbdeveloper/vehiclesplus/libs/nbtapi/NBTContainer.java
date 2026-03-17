/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.io.InputStream;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ObjectCreator;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTContainer
extends NBTCompound {
    private Object nbt;
    private boolean closed;
    private boolean readOnly;

    @Deprecated
    public NBTContainer() {
        super(null, null);
        this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
    }

    @Deprecated
    public NBTContainer(Object object) {
        super(null, null);
        if (object == null) {
            object = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
        }
        if (!ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().isAssignableFrom(object.getClass())) {
            throw new NbtApiException("The object '" + object.getClass() + "' is not a valid NBT-Object!");
        }
        this.nbt = object;
    }

    @Deprecated
    public NBTContainer(InputStream inputStream) {
        super(null, null);
        this.nbt = NBTReflectionUtil.readNBT(inputStream);
    }

    @Deprecated
    public NBTContainer(String string) {
        super(null, null);
        if (string == null) {
            throw new NullPointerException("The String can't be null!");
        }
        try {
            this.nbt = ReflectionMethod.PARSE_NBT.run(null, string);
        } catch (Exception exception) {
            throw new NbtApiException("Unable to parse Malformed Json!", exception);
        }
    }

    @Override
    public Object getCompound() {
        return this.nbt;
    }

    @Override
    public void setCompound(Object object) {
        this.nbt = object;
    }

    @Override
    protected void setClosed() {
        this.closed = true;
    }

    @Override
    protected boolean isClosed() {
        return this.closed;
    }

    @Override
    protected boolean isReadOnly() {
        return this.readOnly;
    }

    protected NBTContainer setReadOnly(boolean bl) {
        this.readOnly = true;
        return this;
    }
}

