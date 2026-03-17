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

class ModuleInfo
extends ConstInfo {
    static final int tag = 19;
    int name;

    public ModuleInfo(int n, int n2) {
        super(n2);
        this.name = n;
    }

    public ModuleInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.name = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.name;
    }

    public boolean equals(Object object) {
        return object instanceof ModuleInfo && ((ModuleInfo)object).name == this.name;
    }

    @Override
    public int getTag() {
        return 19;
    }

    public String getModuleName(ConstPool constPool) {
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
        dataOutputStream.writeByte(19);
        dataOutputStream.writeShort(this.name);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("Module #");
        printWriter.println(this.name);
    }
}

