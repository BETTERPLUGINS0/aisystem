/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;

class MethodTypeInfo
extends ConstInfo {
    static final int tag = 16;
    int descriptor;

    public MethodTypeInfo(int n, int n2) {
        super(n2);
        this.descriptor = n;
    }

    public MethodTypeInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.descriptor = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.descriptor;
    }

    public boolean equals(Object object) {
        if (object instanceof MethodTypeInfo) {
            return ((MethodTypeInfo)object).descriptor == this.descriptor;
        }
        return false;
    }

    @Override
    public int getTag() {
        return 16;
    }

    @Override
    public void renameClass(ConstPool constPool, String string, String string2, Map<ConstInfo, ConstInfo> map) {
        String string3;
        String string4 = constPool.getUtf8Info(this.descriptor);
        if (string4 != (string3 = Descriptor.rename(string4, string, string2))) {
            if (map == null) {
                this.descriptor = constPool.addUtf8Info(string3);
            } else {
                map.remove(this);
                this.descriptor = constPool.addUtf8Info(string3);
                map.put(this, this);
            }
        }
    }

    @Override
    public void renameClass(ConstPool constPool, Map<String, String> map, Map<ConstInfo, ConstInfo> map2) {
        String string;
        String string2 = constPool.getUtf8Info(this.descriptor);
        if (string2 != (string = Descriptor.rename(string2, map))) {
            if (map2 == null) {
                this.descriptor = constPool.addUtf8Info(string);
            } else {
                map2.remove(this);
                this.descriptor = constPool.addUtf8Info(string);
                map2.put(this, this);
            }
        }
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        String string = constPool.getUtf8Info(this.descriptor);
        string = Descriptor.rename(string, map);
        return constPool2.addMethodTypeInfo(constPool2.addUtf8Info(string));
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(16);
        dataOutputStream.writeShort(this.descriptor);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("MethodType #");
        printWriter.println(this.descriptor);
    }
}

