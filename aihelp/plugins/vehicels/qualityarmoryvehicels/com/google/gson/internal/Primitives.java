/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.google.gson.internal;

import com.google.gson.internal.$Gson$Preconditions;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Primitives {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    private Primitives() {
        throw new UnsupportedOperationException();
    }

    private static void add(Map<Class<?>, Class<?>> map, Map<Class<?>, Class<?>> map2, Class<?> clazz, Class<?> clazz2) {
        map.put(clazz, clazz2);
        map2.put(clazz2, clazz);
    }

    public static boolean isPrimitive(Type type) {
        return PRIMITIVE_TO_WRAPPER_TYPE.containsKey(type);
    }

    public static boolean isWrapperType(Type type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey($Gson$Preconditions.checkNotNull(type));
    }

    public static <T> Class<T> wrap(Class<T> clazz) {
        Class<?> clazz2 = PRIMITIVE_TO_WRAPPER_TYPE.get($Gson$Preconditions.checkNotNull(clazz));
        return clazz2 == null ? clazz : clazz2;
    }

    public static <T> Class<T> unwrap(Class<T> clazz) {
        Class<?> clazz2 = WRAPPER_TO_PRIMITIVE_TYPE.get($Gson$Preconditions.checkNotNull(clazz));
        return clazz2 == null ? clazz : clazz2;
    }

    static {
        HashMap hashMap = new HashMap(16);
        HashMap hashMap2 = new HashMap(16);
        Primitives.add(hashMap, hashMap2, Boolean.TYPE, Boolean.class);
        Primitives.add(hashMap, hashMap2, Byte.TYPE, Byte.class);
        Primitives.add(hashMap, hashMap2, Character.TYPE, Character.class);
        Primitives.add(hashMap, hashMap2, Double.TYPE, Double.class);
        Primitives.add(hashMap, hashMap2, Float.TYPE, Float.class);
        Primitives.add(hashMap, hashMap2, Integer.TYPE, Integer.class);
        Primitives.add(hashMap, hashMap2, Long.TYPE, Long.class);
        Primitives.add(hashMap, hashMap2, Short.TYPE, Short.class);
        Primitives.add(hashMap, hashMap2, Void.TYPE, Void.class);
        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(hashMap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(hashMap2);
    }
}

