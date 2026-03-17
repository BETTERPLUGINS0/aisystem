/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class MethodParametersAttribute
extends AttributeInfo {
    public static final String tag = "MethodParameters";

    MethodParametersAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public MethodParametersAttribute(ConstPool constPool, String[] stringArray, int[] nArray) {
        super(constPool, tag);
        byte[] byArray = new byte[stringArray.length * 4 + 1];
        byArray[0] = (byte)stringArray.length;
        for (int i = 0; i < stringArray.length; ++i) {
            ByteArray.write16bit(constPool.addUtf8Info(stringArray[i]), byArray, i * 4 + 1);
            ByteArray.write16bit(nArray[i], byArray, i * 4 + 3);
        }
        this.set(byArray);
    }

    public int size() {
        return this.info[0] & 0xFF;
    }

    public int name(int n) {
        return ByteArray.readU16bit(this.info, n * 4 + 1);
    }

    public String parameterName(int n) {
        return this.getConstPool().getUtf8Info(this.name(n));
    }

    public int accessFlags(int n) {
        return ByteArray.readU16bit(this.info, n * 4 + 3);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        int n = this.size();
        ConstPool constPool2 = this.getConstPool();
        String[] stringArray = new String[n];
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            stringArray[i] = constPool2.getUtf8Info(this.name(i));
            nArray[i] = this.accessFlags(i);
        }
        return new MethodParametersAttribute(constPool, stringArray, nArray);
    }
}

