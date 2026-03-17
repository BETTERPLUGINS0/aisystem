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

class IntegerInfo
extends ConstInfo {
    static final int tag = 3;
    int value;

    public IntegerInfo(int n, int n2) {
        super(n2);
        this.value = n;
    }

    public IntegerInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.value = dataInputStream.readInt();
    }

    public int hashCode() {
        return this.value;
    }

    public boolean equals(Object object) {
        return object instanceof IntegerInfo && ((IntegerInfo)object).value == this.value;
    }

    @Override
    public int getTag() {
        return 3;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        return constPool2.addIntegerInfo(this.value);
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(3);
        dataOutputStream.writeInt(this.value);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("Integer ");
        printWriter.println(this.value);
    }
}

