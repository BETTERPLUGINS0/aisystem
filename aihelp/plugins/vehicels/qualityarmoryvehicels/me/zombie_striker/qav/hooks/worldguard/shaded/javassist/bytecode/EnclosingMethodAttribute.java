/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class EnclosingMethodAttribute
extends AttributeInfo {
    public static final String tag = "EnclosingMethod";

    EnclosingMethodAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public EnclosingMethodAttribute(ConstPool constPool, String string, String string2, String string3) {
        super(constPool, tag);
        int n = constPool.addClassInfo(string);
        int n2 = constPool.addNameAndTypeInfo(string2, string3);
        byte[] byArray = new byte[]{(byte)(n >>> 8), (byte)n, (byte)(n2 >>> 8), (byte)n2};
        this.set(byArray);
    }

    public EnclosingMethodAttribute(ConstPool constPool, String string) {
        super(constPool, tag);
        int n = constPool.addClassInfo(string);
        int n2 = 0;
        byte[] byArray = new byte[]{(byte)(n >>> 8), (byte)n, (byte)(n2 >>> 8), (byte)n2};
        this.set(byArray);
    }

    public int classIndex() {
        return ByteArray.readU16bit(this.get(), 0);
    }

    public int methodIndex() {
        return ByteArray.readU16bit(this.get(), 2);
    }

    public String className() {
        return this.getConstPool().getClassInfo(this.classIndex());
    }

    public String methodName() {
        ConstPool constPool = this.getConstPool();
        int n = this.methodIndex();
        if (n == 0) {
            return "<clinit>";
        }
        int n2 = constPool.getNameAndTypeName(n);
        return constPool.getUtf8Info(n2);
    }

    public String methodDescriptor() {
        ConstPool constPool = this.getConstPool();
        int n = this.methodIndex();
        int n2 = constPool.getNameAndTypeDescriptor(n);
        return constPool.getUtf8Info(n2);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        if (this.methodIndex() == 0) {
            return new EnclosingMethodAttribute(constPool, this.className());
        }
        return new EnclosingMethodAttribute(constPool, this.className(), this.methodName(), this.methodDescriptor());
    }
}

