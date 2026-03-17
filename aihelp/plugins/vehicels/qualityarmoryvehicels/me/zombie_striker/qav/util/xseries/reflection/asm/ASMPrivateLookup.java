/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 */
package me.zombie_striker.qav.util.xseries.reflection.asm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class ASMPrivateLookup {
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private final Class<?> targetClass;

    public ASMPrivateLookup(Class<?> clazz) {
        this.targetClass = clazz;
    }

    public MethodHandle findMethod(String string, Class<?> clazz2, Class<?>[] classArray) {
        Method method = (Method)new ReflectionIterator(clazz -> clazz.getDeclaredMethod(string, classArray)).find();
        if (method == null) {
            throw new IllegalArgumentException("Couldn't find method named '" + string + "' with type: " + clazz2 + " (" + Arrays.toString(classArray) + ')');
        }
        method.setAccessible(true);
        return this.lookup.unreflect(method);
    }

    public MethodHandle findConstructor(Class<?>[] classArray) {
        try {
            Constructor<?> constructor = this.targetClass.getDeclaredConstructor(classArray);
            constructor.setAccessible(true);
            return this.lookup.unreflectConstructor(constructor);
        } catch (NoSuchMethodException noSuchMethodException) {
            throw new IllegalArgumentException("Couldn't find constructor with type:  (" + Arrays.toString(classArray) + ')', noSuchMethodException);
        }
    }

    public MethodHandle findField(String string, Class<?> clazz2, boolean bl) {
        Field field = (Field)new ReflectionIterator(clazz -> clazz.getDeclaredField(string)).find();
        if (field == null) {
            throw new IllegalArgumentException("Couldn't find field named '" + string + "' with type: " + clazz2);
        }
        field.setAccessible(true);
        return bl ? this.lookup.unreflectGetter(field) : this.lookup.unreflectSetter(field);
    }

    private final class ReflectionIterator<T> {
        private final UnsafeFunction<Class<?>, T> finder;

        private ReflectionIterator(UnsafeFunction<Class<?>, T> unsafeFunction) {
            this.finder = unsafeFunction;
        }

        private T find() {
            try {
                return this.iterate(ASMPrivateLookup.this.targetClass);
            } catch (Exception exception) {
                throw new IllegalStateException(exception);
            }
        }

        private T iterate(Class<?> clazz) {
            if (clazz == Object.class) {
                return null;
            }
            try {
                return this.finder.apply(clazz);
            } catch (NoSuchFieldException | NoSuchMethodException reflectiveOperationException) {
                return this.iterate(clazz.getSuperclass());
            }
        }
    }

    @FunctionalInterface
    private static interface UnsafeFunction<I, O> {
        public O apply(I var1) throws Exception;
    }
}

