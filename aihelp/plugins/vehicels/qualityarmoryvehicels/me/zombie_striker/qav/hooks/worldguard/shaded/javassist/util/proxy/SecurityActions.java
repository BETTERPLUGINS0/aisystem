/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;

class SecurityActions
extends SecurityManager {
    public static final SecurityActions stack = new SecurityActions();

    SecurityActions() {
    }

    public Class<?> getCallerClass() {
        return this.getClassContext()[2];
    }

    static Method[] getDeclaredMethods(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredMethods();
        }
        return AccessController.doPrivileged(new PrivilegedAction<Method[]>(){

            @Override
            public Method[] run() {
                return clazz.getDeclaredMethods();
            }
        });
    }

    static Constructor<?>[] getDeclaredConstructors(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredConstructors();
        }
        return AccessController.doPrivileged(new PrivilegedAction<Constructor<?>[]>(){

            @Override
            public Constructor<?>[] run() {
                return clazz.getDeclaredConstructors();
            }
        });
    }

    static MethodHandle getMethodHandle(final Class<?> clazz, final String string, final Class<?>[] classArray) {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<MethodHandle>(){

                @Override
                public MethodHandle run() {
                    Method method = clazz.getDeclaredMethod(string, classArray);
                    method.setAccessible(true);
                    MethodHandle methodHandle = MethodHandles.lookup().unreflect(method);
                    method.setAccessible(false);
                    return methodHandle;
                }
            });
        } catch (PrivilegedActionException privilegedActionException) {
            if (privilegedActionException.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)privilegedActionException.getCause();
            }
            throw new RuntimeException(privilegedActionException.getCause());
        }
    }

    static Method getDeclaredMethod(final Class<?> clazz, final String string, final Class<?>[] classArray) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredMethod(string, classArray);
        }
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<Method>(){

                @Override
                public Method run() {
                    return clazz.getDeclaredMethod(string, classArray);
                }
            });
        } catch (PrivilegedActionException privilegedActionException) {
            if (privilegedActionException.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)privilegedActionException.getCause();
            }
            throw new RuntimeException(privilegedActionException.getCause());
        }
    }

    static Constructor<?> getDeclaredConstructor(final Class<?> clazz, final Class<?>[] classArray) {
        if (System.getSecurityManager() == null) {
            return clazz.getDeclaredConstructor(classArray);
        }
        try {
            return (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<?>>(){

                @Override
                public Constructor<?> run() {
                    return clazz.getDeclaredConstructor(classArray);
                }
            });
        } catch (PrivilegedActionException privilegedActionException) {
            if (privilegedActionException.getCause() instanceof NoSuchMethodException) {
                throw (NoSuchMethodException)privilegedActionException.getCause();
            }
            throw new RuntimeException(privilegedActionException.getCause());
        }
    }

    static void setAccessible(final AccessibleObject accessibleObject, final boolean bl) {
        if (System.getSecurityManager() == null) {
            accessibleObject.setAccessible(bl);
        } else {
            AccessController.doPrivileged(new PrivilegedAction<Void>(){

                @Override
                public Void run() {
                    accessibleObject.setAccessible(bl);
                    return null;
                }
            });
        }
    }

    static void set(final Field field, final Object object, final Object object2) {
        if (System.getSecurityManager() == null) {
            field.set(object, object2);
        } else {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>(){

                    @Override
                    public Void run() {
                        field.set(object, object2);
                        return null;
                    }
                });
            } catch (PrivilegedActionException privilegedActionException) {
                if (privilegedActionException.getCause() instanceof NoSuchMethodException) {
                    throw (IllegalAccessException)privilegedActionException.getCause();
                }
                throw new RuntimeException(privilegedActionException.getCause());
            }
        }
    }

    static TheUnsafe getSunMiscUnsafeAnonymously() {
        try {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<TheUnsafe>(){

                @Override
                public TheUnsafe run() {
                    Class<?> clazz = Class.forName("sun.misc.Unsafe");
                    Field field = clazz.getDeclaredField("theUnsafe");
                    field.setAccessible(true);
                    SecurityActions securityActions = stack;
                    Objects.requireNonNull(securityActions);
                    TheUnsafe theUnsafe = securityActions.new TheUnsafe(clazz, field.get(null));
                    field.setAccessible(false);
                    SecurityActions.disableWarning(theUnsafe);
                    return theUnsafe;
                }
            });
        } catch (PrivilegedActionException privilegedActionException) {
            if (privilegedActionException.getCause() instanceof ClassNotFoundException) {
                throw (ClassNotFoundException)privilegedActionException.getCause();
            }
            if (privilegedActionException.getCause() instanceof NoSuchFieldException) {
                throw new ClassNotFoundException("No such instance.", privilegedActionException.getCause());
            }
            if (privilegedActionException.getCause() instanceof IllegalAccessException || privilegedActionException.getCause() instanceof SecurityException) {
                throw new ClassNotFoundException("Security denied access.", privilegedActionException.getCause());
            }
            throw new RuntimeException(privilegedActionException.getCause());
        }
    }

    static void disableWarning(TheUnsafe theUnsafe) {
        try {
            if (ClassFile.MAJOR_VERSION < 53) {
                return;
            }
            Class<?> clazz = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field field = clazz.getDeclaredField("logger");
            theUnsafe.call("putObjectVolatile", clazz, theUnsafe.call("staticFieldOffset", field), null);
        } catch (Exception exception) {
            // empty catch block
        }
    }

    class TheUnsafe {
        final Class<?> unsafe;
        final Object theUnsafe;
        final Map<String, List<Method>> methods = new HashMap<String, List<Method>>();

        TheUnsafe(Class<?> clazz, Object object) {
            this.unsafe = clazz;
            this.theUnsafe = object;
            for (Method method : this.unsafe.getDeclaredMethods()) {
                if (!this.methods.containsKey(method.getName())) {
                    this.methods.put(method.getName(), Collections.singletonList(method));
                    continue;
                }
                if (this.methods.get(method.getName()).size() == 1) {
                    this.methods.put(method.getName(), new ArrayList(this.methods.get(method.getName())));
                }
                this.methods.get(method.getName()).add(method);
            }
        }

        private Method getM(String string, Object[] objectArray) {
            return this.methods.get(string).get(0);
        }

        public Object call(String string, Object ... objectArray) {
            try {
                return this.getM(string, objectArray).invoke(this.theUnsafe, objectArray);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return null;
            }
        }
    }
}

