/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTLongList
extends NBTList<Long> {
    protected NBTLongList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        super(nBTCompound, string, nBTType, object);
    }

    @Override
    protected Object asTag(Long l) {
        try {
            Constructor<?> constructor = ClassWrapper.NMS_NBTTAGLONG.getClazz().getDeclaredConstructor(Long.TYPE);
            constructor.setAccessible(true);
            return constructor.newInstance(l);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException exception) {
            throw new NbtApiException("Error while wrapping the Object " + l + " to it's NMS object!", exception);
        }
    }

    @Override
    public Long get(int n) {
        try {
            Object object = ReflectionMethod.LIST_GET.run(this.listObject, n);
            return Long.valueOf(object.toString().replace("L", ""));
        } catch (NumberFormatException numberFormatException) {
            return 0L;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }
}

