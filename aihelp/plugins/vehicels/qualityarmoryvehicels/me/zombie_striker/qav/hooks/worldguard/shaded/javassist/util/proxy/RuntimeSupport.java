/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.MethodHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.Proxy;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyFactory;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.ProxyObject;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.SecurityActions;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.SerializedProxy;

public class RuntimeSupport {
    public static MethodHandler default_interceptor = new DefaultMethodHandler();

    public static void find2Methods(Class<?> clazz, String string, String string2, int n, String string3, Method[] methodArray) {
        methodArray[n + 1] = string2 == null ? null : RuntimeSupport.findMethod(clazz, string2, string3);
        methodArray[n] = RuntimeSupport.findSuperClassMethod(clazz, string, string3);
    }

    @Deprecated
    public static void find2Methods(Object object, String string, String string2, int n, String string3, Method[] methodArray) {
        methodArray[n + 1] = string2 == null ? null : RuntimeSupport.findMethod(object, string2, string3);
        methodArray[n] = RuntimeSupport.findSuperMethod(object, string, string3);
    }

    @Deprecated
    public static Method findMethod(Object object, String string, String string2) {
        Method method = RuntimeSupport.findMethod2(object.getClass(), string, string2);
        if (method == null) {
            RuntimeSupport.error(object.getClass(), string, string2);
        }
        return method;
    }

    public static Method findMethod(Class<?> clazz, String string, String string2) {
        Method method = RuntimeSupport.findMethod2(clazz, string, string2);
        if (method == null) {
            RuntimeSupport.error(clazz, string, string2);
        }
        return method;
    }

    public static Method findSuperMethod(Object object, String string, String string2) {
        Class<?> clazz = object.getClass();
        return RuntimeSupport.findSuperClassMethod(clazz, string, string2);
    }

    public static Method findSuperClassMethod(Class<?> clazz, String string, String string2) {
        Method method = RuntimeSupport.findSuperMethod2(clazz.getSuperclass(), string, string2);
        if (method == null) {
            method = RuntimeSupport.searchInterfaces(clazz, string, string2);
        }
        if (method == null) {
            RuntimeSupport.error(clazz, string, string2);
        }
        return method;
    }

    private static void error(Class<?> clazz, String string, String string2) {
        throw new RuntimeException("not found " + string + ":" + string2 + " in " + clazz.getName());
    }

    private static Method findSuperMethod2(Class<?> clazz, String string, String string2) {
        Method method = RuntimeSupport.findMethod2(clazz, string, string2);
        if (method != null) {
            return method;
        }
        Class<?> clazz2 = clazz.getSuperclass();
        if (clazz2 != null && (method = RuntimeSupport.findSuperMethod2(clazz2, string, string2)) != null) {
            return method;
        }
        return RuntimeSupport.searchInterfaces(clazz, string, string2);
    }

    private static Method searchInterfaces(Class<?> clazz, String string, String string2) {
        Method method = null;
        Class<?>[] classArray = clazz.getInterfaces();
        for (int i = 0; i < classArray.length; ++i) {
            method = RuntimeSupport.findSuperMethod2(classArray[i], string, string2);
            if (method == null) continue;
            return method;
        }
        return method;
    }

    private static Method findMethod2(Class<?> clazz, String string, String string2) {
        Method[] methodArray = SecurityActions.getDeclaredMethods(clazz);
        int n = methodArray.length;
        for (int i = 0; i < n; ++i) {
            if (!methodArray[i].getName().equals(string) || !RuntimeSupport.makeDescriptor(methodArray[i]).equals(string2)) continue;
            return methodArray[i];
        }
        return null;
    }

    public static String makeDescriptor(Method method) {
        Class<?>[] classArray = method.getParameterTypes();
        return RuntimeSupport.makeDescriptor(classArray, method.getReturnType());
    }

    public static String makeDescriptor(Class<?>[] classArray, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        for (int i = 0; i < classArray.length; ++i) {
            RuntimeSupport.makeDesc(stringBuilder, classArray[i]);
        }
        stringBuilder.append(')');
        if (clazz != null) {
            RuntimeSupport.makeDesc(stringBuilder, clazz);
        }
        return stringBuilder.toString();
    }

    public static String makeDescriptor(String string, Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder(string);
        RuntimeSupport.makeDesc(stringBuilder, clazz);
        return stringBuilder.toString();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static void makeDesc(StringBuilder stringBuilder, Class<?> clazz) {
        if (clazz.isArray()) {
            stringBuilder.append('[');
            RuntimeSupport.makeDesc(stringBuilder, clazz.getComponentType());
            return;
        } else if (clazz.isPrimitive()) {
            if (clazz == Void.TYPE) {
                stringBuilder.append('V');
                return;
            } else if (clazz == Integer.TYPE) {
                stringBuilder.append('I');
                return;
            } else if (clazz == Byte.TYPE) {
                stringBuilder.append('B');
                return;
            } else if (clazz == Long.TYPE) {
                stringBuilder.append('J');
                return;
            } else if (clazz == Double.TYPE) {
                stringBuilder.append('D');
                return;
            } else if (clazz == Float.TYPE) {
                stringBuilder.append('F');
                return;
            } else if (clazz == Character.TYPE) {
                stringBuilder.append('C');
                return;
            } else if (clazz == Short.TYPE) {
                stringBuilder.append('S');
                return;
            } else {
                if (clazz != Boolean.TYPE) throw new RuntimeException("bad type: " + clazz.getName());
                stringBuilder.append('Z');
            }
            return;
        } else {
            stringBuilder.append('L').append(clazz.getName().replace('.', '/')).append(';');
        }
    }

    public static SerializedProxy makeSerializedProxy(Object object) {
        Class<?> clazz = object.getClass();
        MethodHandler methodHandler = null;
        if (object instanceof ProxyObject) {
            methodHandler = ((ProxyObject)object).getHandler();
        } else if (object instanceof Proxy) {
            methodHandler = ProxyFactory.getHandler((Proxy)object);
        }
        return new SerializedProxy(clazz, ProxyFactory.getFilterSignature(clazz), methodHandler);
    }

    static class DefaultMethodHandler
    implements MethodHandler,
    Serializable {
        private static final long serialVersionUID = 1L;

        DefaultMethodHandler() {
        }

        @Override
        public Object invoke(Object object, Method method, Method method2, Object[] objectArray) {
            return method2.invoke(object, objectArray);
        }
    }
}

