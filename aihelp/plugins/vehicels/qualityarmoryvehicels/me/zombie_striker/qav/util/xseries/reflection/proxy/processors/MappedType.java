/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy.processors;

import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ReflectiveProxyObject;
import me.zombie_striker.qav.util.xseries.reflection.proxy.processors.ReflectiveAnnotationProcessor;

public final class MappedType {
    public static final Map<Class<? extends ReflectiveProxyObject>, Class<?>> LOOK_AHEAD = new IdentityHashMap();
    public final Class<?> synthetic;
    public final Class<?> real;

    public MappedType(Class<?> clazz, Class<?> clazz2) {
        this.synthetic = clazz;
        this.real = clazz2;
    }

    public boolean isDifferent() {
        return this.synthetic != this.real;
    }

    public static Class<?>[] getRealTypes(MappedType[] mappedTypeArray) {
        return (Class[])Arrays.stream(mappedTypeArray).map(mappedType -> mappedType.real).toArray(Class[]::new);
    }

    public static Class<?> getMappedTypeOrCreate(Class<? extends ReflectiveProxyObject> clazz) {
        Class<?> clazz2 = LOOK_AHEAD.get(clazz);
        if (clazz2 == null) {
            ReflectiveAnnotationProcessor reflectiveAnnotationProcessor = new ReflectiveAnnotationProcessor(clazz);
            reflectiveAnnotationProcessor.processTargetClass();
            clazz2 = reflectiveAnnotationProcessor.getTargetClass();
            if (clazz2 == null) {
                throw new IllegalStateException("Look ahead type " + clazz + " could not be processed.");
            }
        }
        return clazz2;
    }

    public String toString() {
        return "MappedType[synthetic: " + this.synthetic + ", real: " + this.real + ']';
    }
}

