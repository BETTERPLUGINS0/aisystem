/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.reflection.asm;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import me.zombie_striker.qav.util.xseries.reflection.asm.XReflectASM;

final class ASMClassLoader
extends ClassLoader {
    private static final String DEFINE_CLASS = "defineClass";

    protected ASMClassLoader() {
    }

    protected Class<?> defineClass(String string, byte[] byArray) {
        return super.defineClass(ASMClassLoader.asmTypeToBinary(string), byArray, 0, byArray.length);
    }

    private static Class<?> defineClassLombockTransplanter(String string, byte[] byArray, ClassLoader classLoader) {
        try {
            Class<?> clazz = Class.forName("java.lang.invoke.MethodHandles");
            Class<?> clazz2 = Class.forName("java.lang.invoke.MethodHandle");
            Class<?> clazz3 = Class.forName("java.lang.invoke.MethodType");
            Class<?> clazz4 = Class.forName("java.lang.invoke.MethodHandles$Lookup");
            Method method = clazz.getDeclaredMethod("lookup", new Class[0]);
            Method method2 = clazz3.getDeclaredMethod("methodType", Class.class, Class[].class);
            Method method3 = clazz4.getDeclaredMethod("findVirtual", Class.class, String.class, clazz3);
            Method method4 = clazz2.getDeclaredMethod("invokeWithArguments", Object[].class);
            Object object = method.invoke(null, new Object[0]);
            Object object2 = method2.invoke(null, Class.class, new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE});
            Object object3 = method3.invoke(object, classLoader.getClass(), DEFINE_CLASS, object2);
            return (Class)method4.invoke(object3, new Object[]{new Object[]{classLoader, string, byArray, 0, byArray.length}});
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            throw new IllegalStateException(reflectiveOperationException);
        }
    }

    private static Class<?> defineClassJLA(String string, byte[] byArray) {
        try {
            Class<?> clazz = Class.forName("jdk.internal.reflect.ClassDefiner");
            Method method = clazz.getDeclaredMethod(DEFINE_CLASS, String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class);
            return (Class)method.invoke(null, string, byArray, 0, byArray.length, XReflectASM.class.getClassLoader());
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException reflectiveOperationException) {
            throw new IllegalStateException(reflectiveOperationException);
        }
    }

    private static String asmTypeToBinary(String string) {
        return string.replace('/', '.');
    }

    private static Class<?> methodHandleLoadClass(byte[] byArray) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            MethodHandle methodHandle = lookup.findVirtual(MethodHandles.Lookup.class, DEFINE_CLASS, MethodType.methodType(Class.class, byte[].class));
            return methodHandle.invoke(lookup, byArray);
        } catch (Throwable throwable) {
            throw new IllegalStateException(throwable);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static Class<?> loadClass(String string, byte[] byArray) {
        Class clazz;
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> clazz2 = Class.forName("java.lang.ClassLoader");
            Method method = clazz2.getDeclaredMethod(DEFINE_CLASS, String.class, byte[].class, Integer.TYPE, Integer.TYPE);
            method.setAccessible(true);
            try {
                Object[] objectArray = new Object[]{string, byArray, 0, byArray.length};
                clazz = (Class)method.invoke(classLoader, objectArray);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to load class " + string + " into the system class loader", exception);
        }
        return clazz;
    }
}

