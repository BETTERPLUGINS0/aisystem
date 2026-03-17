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

class NameAndTypeInfo
extends ConstInfo {
    static final int tag = 12;
    int memberName;
    int typeDescriptor;

    public NameAndTypeInfo(int n, int n2, int n3) {
        super(n3);
        this.memberName = n;
        this.typeDescriptor = n2;
    }

    public NameAndTypeInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.memberName = dataInputStream.readUnsignedShort();
        this.typeDescriptor = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.memberName << 16 ^ this.typeDescriptor;
    }

    public boolean equals(Object object) {
        if (object instanceof NameAndTypeInfo) {
            NameAndTypeInfo nameAndTypeInfo = (NameAndTypeInfo)object;
            return nameAndTypeInfo.memberName == this.memberName && nameAndTypeInfo.typeDescriptor == this.typeDescriptor;
        }
        return false;
    }

    @Override
    public int getTag() {
        return 12;
    }

    @Override
    public void renameClass(ConstPool constPool, String string, String string2, Map<ConstInfo, ConstInfo> map) {
        String string3;
        String string4 = constPool.getUtf8Info(this.typeDescriptor);
        if (string4 != (string3 = Descriptor.rename(string4, string, string2))) {
            if (map == null) {
                this.typeDescriptor = constPool.addUtf8Info(string3);
            } else {
                map.remove(this);
                this.typeDescriptor = constPool.addUtf8Info(string3);
                map.put(this, this);
            }
        }
    }

    @Override
    public void renameClass(ConstPool constPool, Map<String, String> map, Map<ConstInfo, ConstInfo> map2) {
        String string;
        String string2 = constPool.getUtf8Info(this.typeDescriptor);
        if (string2 != (string = Descriptor.rename(string2, map))) {
            if (map2 == null) {
                this.typeDescriptor = constPool.addUtf8Info(string);
            } else {
                map2.remove(this);
                this.typeDescriptor = constPool.addUtf8Info(string);
                map2.put(this, this);
            }
        }
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        String string = constPool.getUtf8Info(this.memberName);
        String string2 = constPool.getUtf8Info(this.typeDescriptor);
        string2 = Descriptor.rename(string2, map);
        return constPool2.addNameAndTypeInfo(constPool2.addUtf8Info(string), constPool2.addUtf8Info(string2));
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(12);
        dataOutputStream.writeShort(this.memberName);
        dataOutputStream.writeShort(this.typeDescriptor);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("NameAndType #");
        printWriter.print(this.memberName);
        printWriter.print(", type #");
        printWriter.println(this.typeDescriptor);
    }
}

