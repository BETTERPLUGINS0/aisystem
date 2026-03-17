/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

abstract class ConstInfo {
    int index;

    public ConstInfo(int n) {
        this.index = n;
    }

    public abstract int getTag();

    public String getClassName(ConstPool constPool) {
        return null;
    }

    public void renameClass(ConstPool constPool, String string, String string2, Map<ConstInfo, ConstInfo> map) {
    }

    public void renameClass(ConstPool constPool, Map<String, String> map, Map<ConstInfo, ConstInfo> map2) {
    }

    public abstract int copy(ConstPool var1, ConstPool var2, Map<String, String> var3);

    public abstract void write(DataOutputStream var1);

    public abstract void print(PrintWriter var1);

    public String toString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
        this.print(printWriter);
        return byteArrayOutputStream.toString();
    }
}

