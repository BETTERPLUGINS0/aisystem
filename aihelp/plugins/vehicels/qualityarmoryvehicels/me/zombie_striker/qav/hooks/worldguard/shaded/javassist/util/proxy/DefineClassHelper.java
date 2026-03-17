/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.util.proxy.SecurityActions;

public class DefineClassHelper {
    private static final Helper privileged = ClassFile.MAJOR_VERSION > 54 ? new Java11() : (ClassFile.MAJOR_VERSION >= 53 ? new Java9() : (ClassFile.MAJOR_VERSION >= 51 ? new Java7() : new JavaOther()));

    public static Class<?> toClass(String string, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain, byte[] byArray) {
        try {
            return privileged.defineClass(string, byArray, 0, byArray.length, clazz, classLoader, protectionDomain);
        } catch (RuntimeException runtimeException) {
            throw runtimeException;
        } catch (CannotCompileException cannotCompileException) {
            throw cannotCompileException;
        } catch (ClassFormatError classFormatError) {
            Throwable throwable = classFormatError.getCause();
            throw new CannotCompileException(throwable == null ? classFormatError : throwable);
        } catch (Exception exception) {
            throw new CannotCompileException(exception);
        }
    }

    public static Class<?> toClass(Class<?> clazz, byte[] byArray) {
        try {
            DefineClassHelper.class.getModule().addReads(clazz.getModule());
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandles.Lookup lookup2 = MethodHandles.privateLookupIn(clazz, (MethodHandles.Lookup)lookup);
            return lookup2.defineClass(byArray);
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            throw new CannotCompileException(exception.getMessage() + ": " + clazz.getName() + " has no permission to define the class");
        }
    }

    public static Class<?> toClass(MethodHandles.Lookup lookup, byte[] byArray) {
        try {
            return lookup.defineClass(byArray);
        } catch (IllegalAccessException | IllegalArgumentException exception) {
            throw new CannotCompileException(exception.getMessage());
        }
    }

    static Class<?> toPublicClass(String string, byte[] byArray) {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            lookup = lookup.dropLookupMode(2);
            return lookup.defineClass(byArray);
        } catch (Throwable throwable) {
            throw new CannotCompileException(throwable);
        }
    }

    private DefineClassHelper() {
    }

    private static abstract class Helper {
        private Helper() {
        }

        abstract Class<?> defineClass(String var1, byte[] var2, int var3, int var4, Class<?> var5, ClassLoader var6, ProtectionDomain var7);
    }

    private static class Java11
    extends JavaOther {
        private Java11() {
        }

        @Override
        Class<?> defineClass(String string, byte[] byArray, int n, int n2, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain) {
            if (clazz != null) {
                return DefineClassHelper.toClass(clazz, byArray);
            }
            return super.defineClass(string, byArray, n, n2, clazz, classLoader, protectionDomain);
        }
    }

    private static class Java9
    extends Helper {
        private final Object stack;
        private final Method getCallerClass;
        private final ReferencedUnsafe sunMiscUnsafe = this.getReferencedUnsafe();

        Java9() {
            Class<?> clazz = null;
            try {
                clazz = Class.forName("java.lang.StackWalker");
            } catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            if (clazz != null) {
                try {
                    Class<?> clazz2 = Class.forName("java.lang.StackWalker$Option");
                    this.stack = clazz.getMethod("getInstance", clazz2).invoke(null, clazz2.getEnumConstants()[0]);
                    this.getCallerClass = clazz.getMethod("getCallerClass", new Class[0]);
                } catch (Throwable throwable) {
                    throw new RuntimeException("cannot initialize", throwable);
                }
            } else {
                this.stack = null;
                this.getCallerClass = null;
            }
        }

        private final ReferencedUnsafe getReferencedUnsafe() {
            try {
                if (privileged != null && this.getCallerClass.invoke(this.stack, new Object[0]) != this.getClass()) {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            } catch (Exception exception) {
                throw new RuntimeException("cannot initialize", exception);
            }
            try {
                SecurityActions.TheUnsafe theUnsafe = SecurityActions.getSunMiscUnsafeAnonymously();
                List<Method> list = theUnsafe.methods.get("defineClass");
                if (null == list) {
                    return null;
                }
                MethodHandle methodHandle = MethodHandles.lookup().unreflect(list.get(0));
                return new ReferencedUnsafe(theUnsafe, methodHandle);
            } catch (Throwable throwable) {
                throw new RuntimeException("cannot initialize", throwable);
            }
        }

        @Override
        Class<?> defineClass(String string, byte[] byArray, int n, int n2, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain) {
            try {
                if (this.getCallerClass.invoke(this.stack, new Object[0]) != DefineClassHelper.class) {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            } catch (Exception exception) {
                throw new RuntimeException("cannot initialize", exception);
            }
            return this.sunMiscUnsafe.defineClass(string, byArray, n, n2, classLoader, protectionDomain);
        }

        final class ReferencedUnsafe {
            private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;
            private final MethodHandle defineClass;

            ReferencedUnsafe(SecurityActions.TheUnsafe theUnsafe, MethodHandle methodHandle) {
                this.sunMiscUnsafeTheUnsafe = theUnsafe;
                this.defineClass = methodHandle;
            }

            Class<?> defineClass(String string, byte[] byArray, int n, int n2, ClassLoader classLoader, ProtectionDomain protectionDomain) {
                try {
                    if (Java9.this.getCallerClass.invoke(Java9.this.stack, new Object[0]) != Java9.class) {
                        throw new IllegalAccessError("Access denied for caller.");
                    }
                } catch (Exception exception) {
                    throw new RuntimeException("cannot initialize", exception);
                }
                try {
                    return (Class)this.defineClass.invokeWithArguments(this.sunMiscUnsafeTheUnsafe.theUnsafe, string, byArray, n, n2, classLoader, protectionDomain);
                } catch (Throwable throwable) {
                    if (throwable instanceof RuntimeException) {
                        throw (RuntimeException)throwable;
                    }
                    if (throwable instanceof ClassFormatError) {
                        throw (ClassFormatError)throwable;
                    }
                    throw new ClassFormatError(throwable.getMessage());
                }
            }
        }
    }

    private static class Java7
    extends Helper {
        private final SecurityActions stack = SecurityActions.stack;
        private final MethodHandle defineClass = this.getDefineClassMethodHandle();

        private Java7() {
        }

        private final MethodHandle getDefineClassMethodHandle() {
            if (privileged != null && this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new RuntimeException("cannot initialize", noSuchMethodException);
            }
        }

        @Override
        Class<?> defineClass(String string, byte[] byArray, int n, int n2, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain) {
            if (this.stack.getCallerClass() != DefineClassHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return (Class)this.defineClass.invokeWithArguments(classLoader, string, byArray, n, n2, protectionDomain);
            } catch (Throwable throwable) {
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException)throwable;
                }
                if (throwable instanceof ClassFormatError) {
                    throw (ClassFormatError)throwable;
                }
                throw new ClassFormatError(throwable.getMessage());
            }
        }
    }

    private static class JavaOther
    extends Helper {
        private final Method defineClass = this.getDefineClassMethod();
        private final SecurityActions stack = SecurityActions.stack;

        private JavaOther() {
        }

        private final Method getDefineClassMethod() {
            if (privileged != null && this.stack.getCallerClass() != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
            } catch (NoSuchMethodException noSuchMethodException) {
                throw new RuntimeException("cannot initialize", noSuchMethodException);
            }
        }

        @Override
        Class<?> defineClass(String string, byte[] byArray, int n, int n2, Class<?> clazz, ClassLoader classLoader, ProtectionDomain protectionDomain) {
            Class<?> clazz2 = this.stack.getCallerClass();
            if (clazz2 != DefineClassHelper.class && clazz2 != this.getClass()) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                SecurityActions.setAccessible(this.defineClass, true);
                return (Class)this.defineClass.invoke(classLoader, string, byArray, n, n2, protectionDomain);
            } catch (Throwable throwable) {
                if (throwable instanceof ClassFormatError) {
                    throw (ClassFormatError)throwable;
                }
                if (throwable instanceof RuntimeException) {
                    throw (RuntimeException)throwable;
                }
                throw new CannotCompileException(throwable);
            }
        }
    }
}

