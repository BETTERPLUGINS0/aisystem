/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;

public final class CtPrimitiveType
extends CtClass {
    private char descriptor;
    private String wrapperName;
    private String getMethodName;
    private String mDescriptor;
    private int returnOp;
    private int arrayType;
    private int dataSize;

    CtPrimitiveType(String string, char c, String string2, String string3, String string4, int n, int n2, int n3) {
        super(string);
        this.descriptor = c;
        this.wrapperName = string2;
        this.getMethodName = string3;
        this.mDescriptor = string4;
        this.returnOp = n;
        this.arrayType = n2;
        this.dataSize = n3;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public int getModifiers() {
        return 17;
    }

    public char getDescriptor() {
        return this.descriptor;
    }

    public String getWrapperName() {
        return this.wrapperName;
    }

    public String getGetMethodName() {
        return this.getMethodName;
    }

    public String getGetMethodDescriptor() {
        return this.mDescriptor;
    }

    public int getReturnOp() {
        return this.returnOp;
    }

    public int getArrayType() {
        return this.arrayType;
    }

    public int getDataSize() {
        return this.dataSize;
    }
}

