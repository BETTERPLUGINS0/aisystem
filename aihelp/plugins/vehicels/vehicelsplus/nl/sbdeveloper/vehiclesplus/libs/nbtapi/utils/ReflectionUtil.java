/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import java.lang.reflect.Field;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.MojangToMapping;

public final class ReflectionUtil {
    public static Field getMappedField(Class<?> clazz, String string) {
        String string2 = string.split("#")[1];
        try {
            return clazz.getField(string2);
        } catch (NoSuchFieldException | SecurityException exception) {
            try {
                return clazz.getDeclaredField(MojangToMapping.getMapping().get(string));
            } catch (Exception exception2) {
                throw new NbtApiException("Unable to find field " + string + " in class " + clazz.getName(), exception2);
            }
        }
    }
}

