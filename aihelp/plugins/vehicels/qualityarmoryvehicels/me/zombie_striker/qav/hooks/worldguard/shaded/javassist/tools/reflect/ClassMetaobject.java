/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.CannotCreateException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.tools.reflect.CannotInvokeException;

public class ClassMetaobject
implements Serializable {
    private static final long serialVersionUID = 1L;
    static final String methodPrefix = "_m_";
    static final int methodPrefixLen = 3;
    private Class<?> javaClass;
    private Constructor<?>[] constructors;
    private Method[] methods;
    public static boolean useContextClassLoader = false;

    public ClassMetaobject(String[] stringArray) {
        try {
            this.javaClass = this.getClassObject(stringArray[0]);
        } catch (ClassNotFoundException classNotFoundException) {
            throw new RuntimeException("not found: " + stringArray[0] + ", useContextClassLoader: " + Boolean.toString(useContextClassLoader), classNotFoundException);
        }
        this.constructors = this.javaClass.getConstructors();
        this.methods = null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeUTF(this.javaClass.getName());
    }

    private void readObject(ObjectInputStream objectInputStream) {
        this.javaClass = this.getClassObject(objectInputStream.readUTF());
        this.constructors = this.javaClass.getConstructors();
        this.methods = null;
    }

    private Class<?> getClassObject(String string) {
        if (useContextClassLoader) {
            return Thread.currentThread().getContextClassLoader().loadClass(string);
        }
        return Class.forName(string);
    }

    public final Class<?> getJavaClass() {
        return this.javaClass;
    }

    public final String getName() {
        return this.javaClass.getName();
    }

    public final boolean isInstance(Object object) {
        return this.javaClass.isInstance(object);
    }

    public final Object newInstance(Object[] objectArray) {
        int n = this.constructors.length;
        for (int i = 0; i < n; ++i) {
            try {
                return this.constructors[i].newInstance(objectArray);
            } catch (IllegalArgumentException illegalArgumentException) {
                continue;
            } catch (InstantiationException instantiationException) {
                throw new CannotCreateException(instantiationException);
            } catch (IllegalAccessException illegalAccessException) {
                throw new CannotCreateException(illegalAccessException);
            } catch (InvocationTargetException invocationTargetException) {
                throw new CannotCreateException(invocationTargetException);
            }
        }
        throw new CannotCreateException("no constructor matches");
    }

    public Object trapFieldRead(String string) {
        Class<?> clazz = this.getJavaClass();
        try {
            return clazz.getField(string).get(null);
        } catch (NoSuchFieldException noSuchFieldException) {
            throw new RuntimeException(noSuchFieldException.toString());
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException.toString());
        }
    }

    public void trapFieldWrite(String string, Object object) {
        Class<?> clazz = this.getJavaClass();
        try {
            clazz.getField(string).set(null, object);
        } catch (NoSuchFieldException noSuchFieldException) {
            throw new RuntimeException(noSuchFieldException.toString());
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException.toString());
        }
    }

    public static Object invoke(Object object, int n, Object[] objectArray) {
        Method[] methodArray = object.getClass().getMethods();
        int n2 = methodArray.length;
        String string = methodPrefix + n;
        for (int i = 0; i < n2; ++i) {
            if (!methodArray[i].getName().startsWith(string)) continue;
            try {
                return methodArray[i].invoke(object, objectArray);
            } catch (InvocationTargetException invocationTargetException) {
                throw invocationTargetException.getTargetException();
            } catch (IllegalAccessException illegalAccessException) {
                throw new CannotInvokeException(illegalAccessException);
            }
        }
        throw new CannotInvokeException("cannot find a method");
    }

    public Object trapMethodcall(int n, Object[] objectArray) {
        try {
            Method[] methodArray = this.getReflectiveMethods();
            return methodArray[n].invoke(null, objectArray);
        } catch (InvocationTargetException invocationTargetException) {
            throw invocationTargetException.getTargetException();
        } catch (IllegalAccessException illegalAccessException) {
            throw new CannotInvokeException(illegalAccessException);
        }
    }

    public final Method[] getReflectiveMethods() {
        int n;
        if (this.methods != null) {
            return this.methods;
        }
        Class<?> clazz = this.getJavaClass();
        Method[] methodArray = clazz.getDeclaredMethods();
        int n2 = methodArray.length;
        int[] nArray = new int[n2];
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            char c;
            Method method = methodArray[n];
            String string = method.getName();
            if (!string.startsWith(methodPrefix)) continue;
            int n4 = 0;
            int n5 = 3;
            while ('0' <= (c = string.charAt(n5)) && c <= '9') {
                n4 = n4 * 10 + c - 48;
                ++n5;
            }
            nArray[n] = ++n4;
            if (n4 <= n3) continue;
            n3 = n4;
        }
        this.methods = new Method[n3];
        for (n = 0; n < n2; ++n) {
            if (nArray[n] <= 0) continue;
            this.methods[nArray[n] - 1] = methodArray[n];
        }
        return this.methods;
    }

    public final Method getMethod(int n) {
        return this.getReflectiveMethods()[n];
    }

    public final String getMethodName(int n) {
        char c;
        String string = this.getReflectiveMethods()[n].getName();
        int n2 = 3;
        while ((c = string.charAt(n2++)) >= '0' && '9' >= c) {
        }
        return string.substring(n2);
    }

    public final Class<?>[] getParameterTypes(int n) {
        return this.getReflectiveMethods()[n].getParameterTypes();
    }

    public final Class<?> getReturnType(int n) {
        return this.getReflectiveMethods()[n].getReturnType();
    }

    public final int getMethodIndex(String string, Class<?>[] classArray) {
        Method[] methodArray = this.getReflectiveMethods();
        for (int i = 0; i < methodArray.length; ++i) {
            if (methodArray[i] == null || !this.getMethodName(i).equals(string) || !Arrays.equals(classArray, methodArray[i].getParameterTypes())) continue;
            return i;
        }
        throw new NoSuchMethodException("Method " + string + " not found");
    }
}

