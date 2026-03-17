/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.UUIDUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTUUIDList
extends NBTList<UUID> {
    private final NBTContainer tmpContainer = new NBTContainer();

    protected NBTUUIDList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        super(nBTCompound, string, nBTType, object);
    }

    @Override
    protected Object asTag(UUID uUID) {
        try {
            Constructor<?> constructor = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(int[].class);
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[]{UUIDUtil.uuidToIntArray(uUID)});
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException exception) {
            throw new NbtApiException("Error while wrapping the Object " + uUID + " to it's NMS object!", exception);
        }
    }

    @Override
    public UUID get(int n) {
        try {
            Object object = ReflectionMethod.LIST_GET.run(this.listObject, n);
            ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), "tmp", object);
            int[] nArray = this.tmpContainer.getIntArray("tmp");
            this.tmpContainer.removeKey("tmp");
            return UUIDUtil.uuidFromIntArray(nArray);
        } catch (NumberFormatException numberFormatException) {
            return null;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }
}

