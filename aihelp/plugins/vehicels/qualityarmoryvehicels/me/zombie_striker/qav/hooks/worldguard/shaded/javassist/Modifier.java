/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

public class Modifier {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 4;
    public static final int STATIC = 8;
    public static final int FINAL = 16;
    public static final int SYNCHRONIZED = 32;
    public static final int VOLATILE = 64;
    public static final int VARARGS = 128;
    public static final int TRANSIENT = 128;
    public static final int NATIVE = 256;
    public static final int INTERFACE = 512;
    public static final int ABSTRACT = 1024;
    public static final int STRICT = 2048;
    public static final int ANNOTATION = 8192;
    public static final int ENUM = 16384;

    public static boolean isPublic(int n) {
        return (n & 1) != 0;
    }

    public static boolean isPrivate(int n) {
        return (n & 2) != 0;
    }

    public static boolean isProtected(int n) {
        return (n & 4) != 0;
    }

    public static boolean isPackage(int n) {
        return (n & 7) == 0;
    }

    public static boolean isStatic(int n) {
        return (n & 8) != 0;
    }

    public static boolean isFinal(int n) {
        return (n & 0x10) != 0;
    }

    public static boolean isSynchronized(int n) {
        return (n & 0x20) != 0;
    }

    public static boolean isVolatile(int n) {
        return (n & 0x40) != 0;
    }

    public static boolean isTransient(int n) {
        return (n & 0x80) != 0;
    }

    public static boolean isNative(int n) {
        return (n & 0x100) != 0;
    }

    public static boolean isInterface(int n) {
        return (n & 0x200) != 0;
    }

    public static boolean isAnnotation(int n) {
        return (n & 0x2000) != 0;
    }

    public static boolean isEnum(int n) {
        return (n & 0x4000) != 0;
    }

    public static boolean isAbstract(int n) {
        return (n & 0x400) != 0;
    }

    public static boolean isStrict(int n) {
        return (n & 0x800) != 0;
    }

    public static boolean isVarArgs(int n) {
        return (n & 0x80) != 0;
    }

    public static int setPublic(int n) {
        return n & 0xFFFFFFF9 | 1;
    }

    public static int setProtected(int n) {
        return n & 0xFFFFFFFC | 4;
    }

    public static int setPrivate(int n) {
        return n & 0xFFFFFFFA | 2;
    }

    public static int setPackage(int n) {
        return n & 0xFFFFFFF8;
    }

    public static int clear(int n, int n2) {
        return n & ~n2;
    }

    public static String toString(int n) {
        return java.lang.reflect.Modifier.toString(n);
    }
}

