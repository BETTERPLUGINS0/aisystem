/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTContainer;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTList;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.UUIDUtil;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTUUIDList
extends NBTList<UUID> {
    private final NBTContainer tmpContainer = new NBTContainer();

    protected NBTUUIDList(NBTCompound owner, String name, NBTType type, Object list) {
        super(owner, name, type, list);
    }

    @Override
    protected Object asTag(UUID object) {
        try {
            Constructor<?> con = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(int[].class);
            con.setAccessible(true);
            return con.newInstance(new Object[]{UUIDUtil.uuidToIntArray(object)});
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
        }
    }

    @Override
    public UUID get(int index) {
        try {
            Object obj = ReflectionMethod.LIST_GET.run(this.listObject, index);
            ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), "tmp", obj);
            int[] val = this.tmpContainer.getIntArray("tmp");
            this.tmpContainer.removeKey("tmp");
            return UUIDUtil.uuidFromIntArray(val);
        } catch (NumberFormatException nf) {
            return null;
        } catch (Exception ex) {
            throw new NbtApiException(ex);
        }
    }
}

