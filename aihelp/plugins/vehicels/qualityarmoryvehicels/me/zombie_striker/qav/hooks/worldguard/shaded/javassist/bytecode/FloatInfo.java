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

class FloatInfo
extends ConstInfo {
    static final int tag = 4;
    float value;

    public FloatInfo(float f, int n) {
        super(n);
        this.value = f;
    }

    public FloatInfo(DataInputStream dataInputStream, int n) {
        super(n);
        this.value = dataInputStream.readFloat();
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    public boolean equals(Object object) {
        return object instanceof FloatInfo && ((FloatInfo)object).value == this.value;
    }

    @Override
    public int getTag() {
        return 4;
    }

    @Override
    public int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map) {
        return constPool2.addFloatInfo(this.value);
    }

    @Override
    public void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeByte(4);
        dataOutputStream.writeFloat(this.value);
    }

    @Override
    public void print(PrintWriter printWriter) {
        printWriter.print("Float ");
        printWriter.println(this.value);
    }
}

