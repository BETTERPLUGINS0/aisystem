/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTCompound;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTList;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NBTType;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.NbtApiException;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.nmsmappings.ReflectionMethod;

public class NBTStringList
extends NBTList<String> {
    protected NBTStringList(NBTCompound owner, String name, NBTType type, Object list) {
        super(owner, name, type, list);
    }

    @Override
    public String get(int index) {
        try {
            Object ret = ReflectionMethod.LIST_GET_STRING.run(this.listObject, index);
            if (ret instanceof Optional) {
                return ((Optional)ret).orElse("");
            }
            return (String)ret;
        } catch (Exception ex) {
            throw new NbtApiException(ex);
        }
    }

    @Override
    protected Object asTag(String object) {
        try {
            Constructor<?> con = ClassWrapper.NMS_NBTTAGSTRING.getClazz().getDeclaredConstructor(String.class);
            con.setAccessible(true);
            return con.newInstance(object);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
        }
    }
}

