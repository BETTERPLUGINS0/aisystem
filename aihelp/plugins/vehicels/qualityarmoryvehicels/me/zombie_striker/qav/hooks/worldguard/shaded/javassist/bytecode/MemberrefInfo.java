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

abstract class MemberrefInfo
extends ConstInfo {
    int classIndex;
    int nameAndTypeIndex;

    public MemberrefInfo(int n, int n2, int n3) {
        super(n3);
        this.classIndex = n;
        this.nameAndTypeIndex = n2;
    }

    public MemberrefInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.classIndex = dataInputStream.readUnsignedShort();
        this.nameAndTypeIndex = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.classIndex << 16 ^ this.nameAndTypeIndex;
    }

    public boolean equals(Object object) {
        if (object instanceof MemberrefInfo) {
            MemberrefInfo memberrefInfo = (MemberrefInfo)object;
            return memberrefInfo.classIndex == this.classIndex && memberrefInfo.nameAndTypeIndex == this.nameAndTypeIndex && memberrefInfo.getClass() == this.getClass();
        }
        return false;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        int n = constPool.getItem(this.classIndex).copy(constPool, constPool2, map);
        int n2 = constPool.getItem(this.nameAndTypeIndex).copy(constPool, constPool2, map);
        return this.copy2(constPool2, n, n2);
    }

    protected abstract int copy2(ConstPool var1, int var2, int var3);

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(this.getTag());
        dataOutputStream.writeShort(this.classIndex);
        dataOutputStream.writeShort(this.nameAndTypeIndex);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print(this.getTagName() + " #");
        printWriter.print(this.classIndex);
        printWriter.print(", name&type #");
        printWriter.println(this.nameAndTypeIndex);
    }

    public abstract String getTagName();
}

