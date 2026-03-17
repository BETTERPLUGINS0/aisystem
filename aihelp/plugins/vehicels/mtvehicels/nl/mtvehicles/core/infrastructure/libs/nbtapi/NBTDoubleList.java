/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTList;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTDoubleList
extends NBTList<Double> {
    protected NBTDoubleList(NBTCompound owner, String name, NBTType type, Object list) {
        super(owner, name, type, list);
    }

    @Override
    protected Object asTag(Double object) {
        try {
            Constructor<?> con = ClassWrapper.NMS_NBTTAGDOUBLE.getClazz().getDeclaredConstructor(Double.TYPE);
            con.setAccessible(true);
            return con.newInstance(object);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
        }
    }

    @Override
    public Double get(int index) {
        try {
            Object obj = ReflectionMethod.LIST_GET.run(this.listObject, index);
            return Double.valueOf(obj.toString());
        } catch (NumberFormatException nf) {
            return 0.0;
        } catch (Exception ex) {
            throw new NbtApiException(ex);
        }
    }
}

