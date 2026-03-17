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

class StringInfo
extends ConstInfo {
    static final int tag = 8;
    int string;

    public StringInfo(int n, int n2) {
        super(n2);
        this.string = n;
    }

    public StringInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.string = dataInputStream.readUnsignedShort();
    }

    public int hashCode() {
        return this.string;
    }

    public boolean equals(Object object) {
        return object instanceof StringInfo && ((StringInfo)object).string == this.string;
    }

    @Override
    public int getTag() {
        return 8;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        return constPool2.addStringInfo(constPool.getUtf8Info(this.string));
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(8);
        dataOutputStream.writeShort(this.string);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("String #");
        printWriter.println(this.string);
    }
}

