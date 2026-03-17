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

class InvokeDynamicInfo
extends ConstInfo {
    static final int tag = 18;
    int bootstrap;
    int nameAndType;

    public InvokeDynamicInfo(int n, int n2, int n3) {
        super(n3);
        this.bootstrap = n;
        this.nameAndType = n2;
    }

    public InvokeDynamicInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.bootstrap = dataInputStream.readUnsignedShort();
        this.nameAndType = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.bootstrap << 16 ^ this.nameAndType;
    }

    public boolean equals(Object object) {
        if (object instanceof InvokeDynamicInfo) {
            InvokeDynamicInfo invokeDynamicInfo = (InvokeDynamicInfo)object;
            return invokeDynamicInfo.bootstrap == this.bootstrap && invokeDynamicInfo.nameAndType == this.nameAndType;
        }
        return false;
    }

    @Override
    public int getTag() {
        return 18;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        return constPool2.addInvokeDynamicInfo(this.bootstrap, constPool.getItem(this.nameAndType).copy(constPool, constPool2, map));
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(18);
        dataOutputStream.writeShort(this.bootstrap);
        dataOutputStream.writeShort(this.nameAndType);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("InvokeDynamic #");
        printWriter.print(this.bootstrap);
        printWriter.print(", name&type #");
        printWriter.println(this.nameAndType);
    }
}

