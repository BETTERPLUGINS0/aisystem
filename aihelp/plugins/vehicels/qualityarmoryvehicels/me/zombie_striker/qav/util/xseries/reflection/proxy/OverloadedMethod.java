/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.reflection.proxy;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import me.zombie_striker.qav.util.xseries.reflection.proxy.ClassOverloadedMethods;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class OverloadedMethod<T> {
    public abstract T get(Supplier<String> var1);

    public abstract Collection<T> getOverloads();

    static String getParameterDescriptor(Class<?>[] classArray) {
        StringBuilder stringBuilder = new StringBuilder(classArray.length * 10);
        Class<?>[] classArray2 = classArray;
        int n = classArray2.length;
        for (int i = 0; i < n; ++i) {
            Class<?> clazz;
            Class<?> clazz2 = clazz = classArray2[i];
            while (clazz2.isArray()) {
                stringBuilder.append('[');
                clazz2 = clazz2.getComponentType();
            }
            if (clazz2.isPrimitive()) {
                stringBuilder.append(OverloadedMethod.getDescriptor(clazz2));
                continue;
            }
            if (clazz2 == String.class) {
                stringBuilder.append('@');
                continue;
            }
            stringBuilder.append(clazz2.getName());
        }
        return stringBuilder.toString();
    }

    static char getDescriptor(Class<?> clazz) {
        if (clazz == Integer.TYPE) {
            return 'I';
        }
        if (clazz == Void.TYPE) {
            return 'V';
        }
        if (clazz == Boolean.TYPE) {
            return 'Z';
        }
        if (clazz == Byte.TYPE) {
            return 'B';
        }
        if (clazz == Character.TYPE) {
            return 'C';
        }
        if (clazz == Short.TYPE) {
            return 'S';
        }
        if (clazz == Double.TYPE) {
            return 'D';
        }
        if (clazz == Float.TYPE) {
            return 'F';
        }
        if (clazz == Long.TYPE) {
            return 'J';
        }
        throw new AssertionError((Object)("Unknown primitive: " + clazz));
    }

    private static final class Multi<T>
    extends OverloadedMethod<T> {
        private final Map<String, T> descriptorMap;

        public Multi(Map<String, T> map) {
            this.descriptorMap = map;
        }

        @Override
        public T get(Supplier<String> supplier) {
            return this.descriptorMap.get(supplier.get());
        }

        @Override
        public Collection<T> getOverloads() {
            return this.descriptorMap.values();
        }

        public String toString() {
            return "Multi(" + this.descriptorMap.keySet() + ')';
        }
    }

    private static final class Single<T>
    extends OverloadedMethod<T> {
        private final T object;

        public Single(T t) {
            this.object = t;
        }

        @Override
        public T get(Supplier<String> supplier) {
            return this.object;
        }

        @Override
        public Collection<T> getOverloads() {
            return Collections.singleton(this.object);
        }

        public String toString() {
            return "Single";
        }
    }

    public static final class Builder<T> {
        private final Map<String, Map<String, T>> descriptorMap = new HashMap<String, Map<String, T>>(10);
        private final Function<T, String> descritporProcessor;

        public Builder(Function<T, String> function) {
            this.descritporProcessor = function;
        }

        public void add(T t, String string2) {
            String string3;
            Map map = this.descriptorMap.computeIfAbsent(string2, string -> new HashMap(3));
            if (map.put(string3 = this.descritporProcessor.apply(t), t) != null) {
                throw new IllegalArgumentException("Method named '" + string2 + "' with descriptor '" + string3 + "' was already added: " + map);
            }
        }

        public ClassOverloadedMethods<T> build() {
            HashMap hashMap = new HashMap(this.descriptorMap.size());
            ClassOverloadedMethods classOverloadedMethods = new ClassOverloadedMethods(hashMap);
            this.build(hashMap);
            return classOverloadedMethods;
        }

        public void build(Map<String, OverloadedMethod<T>> map) {
            for (Map.Entry<String, Map<String, T>> entry : this.descriptorMap.entrySet()) {
                OverloadedMethod overloadedMethod = entry.getValue().size() == 1 ? new Single<T>(entry.getValue().values().iterator().next()) : new Multi<T>(entry.getValue());
                map.put(entry.getKey(), overloadedMethod);
            }
        }
    }
}

