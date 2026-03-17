/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTCompound;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTContainer;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTList;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTType;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTIntArrayList
extends NBTList<int[]> {
    private final NBTContainer tmpContainer = new NBTContainer();

    protected NBTIntArrayList(NBTCompound nBTCompound, String string, NBTType nBTType, Object object) {
        super(nBTCompound, string, nBTType, object);
    }

    @Override
    protected Object asTag(int[] nArray) {
        try {
            Constructor<?> constructor = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(int[].class);
            constructor.setAccessible(true);
            return constructor.newInstance(new Object[]{nArray});
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException exception) {
            throw new NbtApiException("Error while wrapping the Object " + nArray + " to it's NMS object!", exception);
        }
    }

    @Override
    public int[] get(int n) {
        try {
            Object object = ReflectionMethod.LIST_GET.run(this.listObject, n);
            ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), "tmp", object);
            int[] nArray = this.tmpContainer.getIntArray("tmp");
            this.tmpContainer.removeKey("tmp");
            return nArray;
        } catch (NumberFormatException numberFormatException) {
            return null;
        } catch (Exception exception) {
            throw new NbtApiException(exception);
        }
    }
}

