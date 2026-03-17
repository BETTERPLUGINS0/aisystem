/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

public class AccessFlag {
    public static final int PUBLIC = 1;
    public static final int PRIVATE = 2;
    public static final int PROTECTED = 4;
    public static final int STATIC = 8;
    public static final int FINAL = 16;
    public static final int SYNCHRONIZED = 32;
    public static final int VOLATILE = 64;
    public static final int BRIDGE = 64;
    public static final int TRANSIENT = 128;
    public static final int VARARGS = 128;
    public static final int NATIVE = 256;
    public static final int INTERFACE = 512;
    public static final int ABSTRACT = 1024;
    public static final int STRICT = 2048;
    public static final int SYNTHETIC = 4096;
    public static final int ANNOTATION = 8192;
    public static final int ENUM = 16384;
    public static final int MANDATED = 32768;
    public static final int SUPER = 32;
    public static final int MODULE = 32768;

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

    public static boolean isPublic(int n) {
        return (n & 1) != 0;
    }

    public static boolean isProtected(int n) {
        return (n & 4) != 0;
    }

    public static boolean isPrivate(int n) {
        return (n & 2) != 0;
    }

    public static boolean isPackage(int n) {
        return (n & 7) == 0;
    }

    public static int clear(int n, int n2) {
        return n & ~n2;
    }

    public static int of(int n) {
        return n;
    }

    public static int toModifier(int n) {
        return n;
    }
}

