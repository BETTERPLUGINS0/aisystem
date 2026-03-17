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

class PackageInfo
extends ConstInfo {
    static final int tag = 20;
    int name;

    public PackageInfo(int n, int n2) {
        super(n2);
        this.name = n;
    }

    public PackageInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.name = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.name;
    }

    public boolean equals(Object object) {
        return object instanceof PackageInfo && ((PackageInfo)object).name == this.name;
    }

    @Override
    public int getTag() {
        return 20;
    }

    public String getPackageName(ConstPool constPool) {
        return constPool.getUtf8Info(this.name);
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        String string = constPool.getUtf8Info(this.name);
        int n = constPool2.addUtf8Info(string);
        return constPool2.addModuleInfo(n);
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(20);
        dataOutputStream.writeShort(this.name);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("Package #");
        printWriter.println(this.name);
    }
}

