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

class DoubleInfo
extends ConstInfo {
    static final int tag = 6;
    double value;

    public DoubleInfo(double d, int n) {
        super(n);
        this.value = d;
    }

    public DoubleInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.value = dataInputStream.readDouble();
    }

    public int hashCode() {
        long l = Double.doubleToLongBits(this.value);
        return (int)(l ^ l >>> 32);
    }

    public boolean equals(Object object) {
        return object instanceof DoubleInfo && ((DoubleInfo)object).value == this.value;
    }

    @Override
    public int getTag() {
        return 6;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        return constPool2.addDoubleInfo(this.value);
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(6);
        dataOutputStream.writeDouble(this.value);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("Double ");
        printWriter.println(this.value);
    }
}

