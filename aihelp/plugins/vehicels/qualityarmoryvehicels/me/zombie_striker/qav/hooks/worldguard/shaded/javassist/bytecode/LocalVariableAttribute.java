/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;

public class LocalVariableAttribute
extends AttributeInfo {
    public static final String tag = "LocalVariableTable";
    public static final String typeTag = "LocalVariableTypeTable";

    public LocalVariableAttribute(ConstPool constPool) {
        super(constPool, tag, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    @Deprecated
    public LocalVariableAttribute(ConstPool constPool, String string) {
        super(constPool, string, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    LocalVariableAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    LocalVariableAttribute(ConstPool constPool, String string, byte[] byArray) {
        super(constPool, string, byArray);
    }

    public void addEntry(int n, int n2, int n3, int n4, int n5) {
        int n6 = this.info.length;
        byte[] byArray = new byte[n6 + 10];
        ByteArray.write16bit(this.tableLength() + 1, byArray, 0);
        for (int i = 2; i < n6; ++i) {
            byArray[i] = this.info[i];
        }
        ByteArray.write16bit(n, byArray, n6);
        ByteArray.write16bit(n2, byArray, n6 + 2);
        ByteArray.write16bit(n3, byArray, n6 + 4);
        ByteArray.write16bit(n4, byArray, n6 + 6);
        ByteArray.write16bit(n5, byArray, n6 + 8);
        this.info = byArray;
    }

    @Override
    void renameClass(String string, String string2) {
        ConstPool constPool = this.getConstPool();
        int n = this.tableLength();
        for (int i = 0; i < n; ++i) {
            int n2 = i * 10 + 2;
            int n3 = ByteArray.readU16bit(this.info, n2 + 6);
            if (n3 == 0) continue;
            String string3 = constPool.getUtf8Info(n3);
            string3 = this.renameEntry(string3, string, string2);
            ByteArray.write16bit(constPool.addUtf8Info(string3), this.info, n2 + 6);
        }
    }

    String renameEntry(String string, String string2, String string3) {
        return Descriptor.rename(string, string2, string3);
    }

    @Override
    void renameClass(Map<String, String> map) {
        ConstPool constPool = this.getConstPool();
        int n = this.tableLength();
        for (int i = 0; i < n; ++i) {
            int n2 = i * 10 + 2;
            int n3 = ByteArray.readU16bit(this.info, n2 + 6);
            if (n3 == 0) continue;
            String string = constPool.getUtf8Info(n3);
            string = this.renameEntry(string, map);
            ByteArray.write16bit(constPool.addUtf8Info(string), this.info, n2 + 6);
        }
    }

    String renameEntry(String string, Map<String, String> map) {
        return Descriptor.rename(string, map);
    }

    public void shiftIndex(int n, int n2) {
        int n3 = this.info.length;
        for (int i = 2; i < n3; i += 10) {
            int n4 = ByteArray.readU16bit(this.info, i + 8);
            if (n4 < n) continue;
            ByteArray.write16bit(n4 + n2, this.info, i + 8);
        }
    }

    public int tableLength() {
        return ByteArray.readU16bit(this.info, 0);
    }

    public int startPc(int n) {
        return ByteArray.readU16bit(this.info, n * 10 + 2);
    }

    public int codeLength(int n) {
        return ByteArray.readU16bit(this.info, n * 10 + 4);
    }

    void shiftPc(int n, int n2, boolean bl) {
        int n3 = this.tableLength();
        for (int i = 0; i < n3; ++i) {
            int n4 = i * 10 + 2;
            int n5 = ByteArray.readU16bit(this.info, n4);
            int n6 = ByteArray.readU16bit(this.info, n4 + 2);
            if (n5 > n || bl && n5 == n && n5 != 0) {
                ByteArray.write16bit(n5 + n2, this.info, n4);
                continue;
            }
            if (n5 + n6 <= n && (!bl || n5 + n6 != n)) continue;
            ByteArray.write16bit(n6 + n2, this.info, n4 + 2);
        }
    }

    public int nameIndex(int n) {
        return ByteArray.readU16bit(this.info, n * 10 + 6);
    }

    public String variableName(int n) {
        return this.getConstPool().getUtf8Info(this.nameIndex(n));
    }

    public String variableNameByIndex(int n) {
        for (int i = 0; i < this.tableLength(); ++i) {
            if (this.index(i) != n) continue;
            return this.variableName(i);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int descriptorIndex(int n) {
        return ByteArray.readU16bit(this.info, n * 10 + 8);
    }

    public int signatureIndex(int n) {
        return this.descriptorIndex(n);
    }

    public String descriptor(int n) {
        return this.getConstPool().getUtf8Info(this.descriptorIndex(n));
    }

    public String signature(int n) {
        return this.descriptor(n);
    }

    public int index(int n) {
        return ByteArray.readU16bit(this.info, n * 10 + 10);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        byte[] byArray = this.get();
        byte[] byArray2 = new byte[byArray.length];
        ConstPool constPool2 = this.getConstPool();
        LocalVariableAttribute localVariableAttribute = this.makeThisAttr(constPool, byArray2);
        int n = ByteArray.readU16bit(byArray, 0);
        ByteArray.write16bit(n, byArray2, 0);
        int n2 = 2;
        for (int i = 0; i < n; ++i) {
            int n3 = ByteArray.readU16bit(byArray, n2);
            int n4 = ByteArray.readU16bit(byArray, n2 + 2);
            int n5 = ByteArray.readU16bit(byArray, n2 + 4);
            int n6 = ByteArray.readU16bit(byArray, n2 + 6);
            int n7 = ByteArray.readU16bit(byArray, n2 + 8);
            ByteArray.write16bit(n3, byArray2, n2);
            ByteArray.write16bit(n4, byArray2, n2 + 2);
            if (n5 != 0) {
                n5 = constPool2.copy(n5, constPool, null);
            }
            ByteArray.write16bit(n5, byArray2, n2 + 4);
            if (n6 != 0) {
                String string = constPool2.getUtf8Info(n6);
                string = Descriptor.rename(string, map);
                n6 = constPool.addUtf8Info(string);
            }
            ByteArray.write16bit(n6, byArray2, n2 + 6);
            ByteArray.write16bit(n7, byArray2, n2 + 8);
            n2 += 10;
        }
        return localVariableAttribute;
    }

    LocalVariableAttribute makeThisAttr(ConstPool constPool, byte[] byArray) {
        return new LocalVariableAttribute(constPool, tag, byArray);
    }
}

