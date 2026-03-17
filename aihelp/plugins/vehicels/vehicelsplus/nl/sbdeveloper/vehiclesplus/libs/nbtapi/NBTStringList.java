/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTStringList
extends NBTList<String> {
    protected NBTStringList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        super(nBTCompound, string, nBTType, object);
    }

    @Override
    public String get(int n) {
        try {
            Object object = ReflectionMethod.LIST_GET_STRING.run(this.listObject, n);
            if (object instanceof Optional) {
                return ((Optional)object).orElse("");
            }
            return (String)object;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }

    @Override
    protected Object asTag(String string) {
        try {
            Constructor<?> constructor = ClassWrapper.NMS_NBTTAGSTRING.getClazz().getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            return constructor.newInstance(string);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException exception) {
            throw new NbtApiException("Error while wrapping the Object " + string + " to it's NMS object!", exception);
        }
    }
}

