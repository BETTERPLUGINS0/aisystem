/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class ExceptionsAttribute
extends AttributeInfo {
    public static final String tag = "Exceptions";

    ExceptionsAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    private ExceptionsAttribute(ConstPool constPool, ExceptionsAttribute exceptionsAttribute, Map<String, String> map) {
        super(constPool, tag);
        this.copyFrom(exceptionsAttribute, map);
    }

    public ExceptionsAttribute(ConstPool constPool) {
        super(constPool, tag);
        byte[] byArray = new byte[2];
        byArray[1] = 0;
        byArray[0] = 0;
        this.info = byArray;
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        return new ExceptionsAttribute(constPool, this, map);
    }

    private void copyFrom(ExceptionsAttribute exceptionsAttribute, Map<String, String> map) {
        ConstPool constPool = exceptionsAttribute.constPool;
        ConstPool constPool2 = this.constPool;
        byte[] byArray = exceptionsAttribute.info;
        int n = byArray.length;
        byte[] byArray2 = new byte[n];
        byArray2[0] = byArray[0];
        byArray2[1] = byArray[1];
        for (int i = 2; i < n; i += 2) {
            int n2 = ByteArray.readU16bit(byArray, i);
            ByteArray.write16bit(constPool.copy(n2, constPool2, map), byArray2, i);
        }
        this.info = byArray2;
    }

    public int[] getExceptionIndexes() {
        byte[] byArray = this.info;
        int n = byArray.length;
        if (n <= 2) {
            return null;
        }
        int[] nArray = new int[n / 2 - 1];
        int n2 = 0;
        for (int i = 2; i < n; i += 2) {
            nArray[n2++] = (byArray[i] & 0xFF) << 8 | byArray[i + 1] & 0xFF;
        }
        return nArray;
    }

    public String[] getExceptions() {
        byte[] byArray = this.info;
        int n = byArray.length;
        if (n <= 2) {
            return null;
        }
        String[] stringArray = new String[n / 2 - 1];
        int n2 = 0;
        for (int i = 2; i < n; i += 2) {
            int n3 = (byArray[i] & 0xFF) << 8 | byArray[i + 1] & 0xFF;
            stringArray[n2++] = this.constPool.getClassInfo(n3);
        }
        return stringArray;
    }

    public void setExceptionIndexes(int[] nArray) {
        int n = nArray.length;
        byte[] byArray = new byte[n * 2 + 2];
        ByteArray.write16bit(n, byArray, 0);
        for (int i = 0; i < n; ++i) {
            ByteArray.write16bit(nArray[i], byArray, i * 2 + 2);
        }
        this.info = byArray;
    }

    public void setExceptions(String[] stringArray) {
        int n = stringArray.length;
        byte[] byArray = new byte[n * 2 + 2];
        ByteArray.write16bit(n, byArray, 0);
        for (int i = 0; i < n; ++i) {
            ByteArray.write16bit(this.constPool.addClassInfo(stringArray[i]), byArray, i * 2 + 2);
        }
        this.info = byArray;
    }

    public int tableLength() {
        return this.info.length / 2 - 1;
    }

    public int getException(int n) {
        int n2 = n * 2 + 2;
        return (this.info[n2] & 0xFF) << 8 | this.info[n2 + 1] & 0xFF;
    }
}

