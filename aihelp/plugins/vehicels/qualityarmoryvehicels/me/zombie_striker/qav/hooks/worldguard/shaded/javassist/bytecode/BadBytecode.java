/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;

public class BadBytecode
extends Exception {
    private static final long serialVersionUID = 1L;

    public BadBytecode(int n) {
        super("bytecode " + n);
    }

    public BadBytecode(String string) {
        super(string);
    }

    public BadBytecode(String string, Throwable throwable) {
        super(string, throwable);
    }

    public BadBytecode(MethodInfo methodInfo, Throwable throwable) {
        super(methodInfo.toString() + " in " + methodInfo.getConstPool().getClassName() + ": " + throwable.getMessage(), throwable);
    }
}

