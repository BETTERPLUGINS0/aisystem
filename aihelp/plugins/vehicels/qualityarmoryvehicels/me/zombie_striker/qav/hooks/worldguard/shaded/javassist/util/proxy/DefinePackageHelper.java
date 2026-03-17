/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.SecurityActions;

public class DefinePackageHelper {
    private static final Helper privileged = ClassFile.MAJOR_VERSION >= 53 ? new Java9() : (ClassFile.MAJOR_VERSION >= 51 ? new Java7() : new JavaOther());

    public static void definePackage(String string, ClassLoader classLoader) {
        try {
            privileged.definePackage(classLoader, string, null, null, null, null, null, null, null);
        } catch (IllegalArgumentException illegalArgumentException) {
            return;
        } catch (Exception exception) {
            throw new CannotCompileException(exception);
        }
    }

    private DefinePackageHelper() {
    }

    private static abstract class Helper {
        private Helper() {
        }

        abstract Package definePackage(ClassLoader var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8, URL var9);
    }

    private static class Java9
    extends Helper {
        private Java9() {
        }

        @Override
        Package definePackage(ClassLoader classLoader, String string, String string2, String string3, String string4, String string5, String string6, String string7, URL uRL) {
            throw new RuntimeException("define package has been disabled for jigsaw");
        }
    }

    private static class Java7
    extends Helper {
        private final SecurityActions stack = SecurityActions.stack;
        private final MethodHandle definePackage = this.getDefinePackageMethodHandle();

        private Java7() {
        }

        private MethodHandle getDefinePackageMethodHandle() {
            if (this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getMethodHandle(ClassLoader.class, "definePackage", new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class});
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new RuntimeException("cannot initialize", noSuchMethodException);
            }
        }

        @Override
        Package definePackage(ClassLoader classLoader, String string, String string2, String string3, String string4, String string5, String string6, String string7, URL uRL) {
            if (this.stack.getCallerClass() != DefinePackageHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return (Package)this.definePackage.invokeWithArguments(classLoader, string, string2, string3, string4, string5, string6, string7, uRL);
            } catch (Throwable throwable) {
                if (throwable instanceof IllegalArgumentException) {
                    throw (IllegalArgumentException)throwable;
                }
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException)throwable;
                }
                return null;
            }
        }
    }

    private static class JavaOther
    extends Helper {
        private final SecurityActions stack = SecurityActions.stack;
        private final Method definePackage = this.getDefinePackageMethod();

        private JavaOther() {
        }

        private Method getDefinePackageMethod() {
            if (this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getDeclaredMethod(ClassLoader.class, "definePackage", new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, URL.class});
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new RuntimeException("cannot initialize", noSuchMethodException);
            }
        }

        @Override
        Package definePackage(ClassLoader classLoader, String string, String string2, String string3, String string4, String string5, String string6, String string7, URL uRL) {
            if (this.stack.getCallerClass() != DefinePackageHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                this.definePackage.setAccessible(true);
                return (Package)this.definePackage.invoke(classLoader, string, string2, string3, string4, string5, string6, string7, uRL);
            } catch (Throwable throwable) {
                Throwable throwable2;
                if (throwable instanceof InvocationTargetException && (throwable2 = ((InvocationTargetException)throwable).getTargetException()) instanceof IllegalArgumentException) {
                    throw (IllegalArgumentException)throwable2;
                }
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException)throwable;
                }
                return null;
            }
        }
    }
}

