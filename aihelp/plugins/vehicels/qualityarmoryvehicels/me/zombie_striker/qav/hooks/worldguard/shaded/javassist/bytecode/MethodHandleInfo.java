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

class MethodHandleInfo
extends ConstInfo {
    static final int tag = 15;
    int refKind;
    int refIndex;

    public MethodHandleInfo(int n, int n2, int n3) {
        super(n3);
        this.refKind = n;
        this.refIndex = n2;
    }

    public MethodHandleInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.refKind = dataInputStream.readUnsignedByte();
        this.refIndex = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.refKind << 16 ^ this.refIndex;
    }

    public boolean equals(Object object) {
        if (object instanceof MethodHandleInfo) {
            MethodHandleInfo methodHandleInfo = (MethodHandleInfo)object;
            return methodHandleInfo.refKind == this.refKind && methodHandleInfo.refIndex == this.refIndex;
        }
        return false;
    }

    @Override
    public int getTag() {
        return 15;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        return constPool2.addMethodHandleInfo(this.refKind, constPool.getItem(this.refIndex).copy(constPool, constPool2, map));
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(15);
        dataOutputStream.writeByte(this.refKind);
        dataOutputStream.writeShort(this.refIndex);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("MethodHandle #");
        printWriter.print(this.refKind);
        printWriter.print(", index #");
        printWriter.println(this.refIndex);
    }
}

