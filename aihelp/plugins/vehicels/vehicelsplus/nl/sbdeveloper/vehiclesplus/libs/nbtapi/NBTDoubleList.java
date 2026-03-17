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

public class NBTDoubleList
extends NBTList<Double> {
    protected NBTDoubleList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        super(nBTCompound, string, nBTType, object);
    }

    @Override
    protected Object asTag(Double d) {
        try {
            Constructor<?> constructor = ClassWrapper.NMS_NBTTAGDOUBLE.getClazz().getDeclaredConstructor(Double.TYPE);
            constructor.setAccessible(true);
            return constructor.newInstance(d);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException exception) {
            throw new NbtApiException("Error while wrapping the Object " + d + " to it's NMS object!", exception);
        }
    }

    @Override
    public Double get(int n) {
        try {
            Object object = ReflectionMethod.LIST_GET.run(this.listObject, n);
            return Double.valueOf(object.toString());
        } catch (NumberFormatException numberFormatException) {
            return 0.0;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }
}

