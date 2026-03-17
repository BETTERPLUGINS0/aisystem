/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.wrapper;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;

class DefaultMethodInvoker {
    private static Method invokeDefaultMethod;

    DefaultMethodInvoker() {
    }

    public static Object invokeDefault(Class<?> clazz, Object object, Method method, Object[] objectArray) {
        if (invokeDefaultMethod != null) {
            try {
                return invokeDefaultMethod.invoke(null, object, method, objectArray);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException exception) {
                throw new NbtApiException("Error while trying to invoke a default method for Java 9+. " + object + " " + method + " " + Arrays.toString(objectArray), exception);
            }
        }
        try {
            Constructor constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);
            return ((MethodHandles.Lookup)constructor.newInstance(clazz)).in(clazz).unreflectSpecial(method, clazz).bindTo(object).invokeWithArguments(objectArray);
        } catch (Throwable throwable) {
            throw new NbtApiException("Error while trying to invoke a default method for Java 8. " + object + " " + method + " " + Arrays.toString(objectArray), throwable);
        }
    }

    static {
        try {
            invokeDefaultMethod = InvocationHandler.class.getDeclaredMethod("invokeDefault", Object.class, Method.class, Object[].class);
            invokeDefaultMethod.setAccessible(true);
        } catch (NoSuchMethodException | SecurityException exception) {
            // empty catch block
        }
    }
}

