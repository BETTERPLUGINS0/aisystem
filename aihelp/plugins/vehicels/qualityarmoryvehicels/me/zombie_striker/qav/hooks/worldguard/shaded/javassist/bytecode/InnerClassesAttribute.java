/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class InnerClassesAttribute
extends AttributeInfo {
    public static final String tag = "InnerClasses";

    InnerClassesAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    private InnerClassesAttribute(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    public InnerClassesAttribute(ConstPool constPool) {
        super(constPool, tag, new byte[2]);
        ByteArray.write16bit(0, this.get(), 0);
    }

    public int tableLength() {
        return ByteArray.readU16bit(this.get(), 0);
    }

    public int innerClassIndex(int n) {
        return ByteArray.readU16bit(this.get(), n * 8 + 2);
    }

    public String innerClass(int n) {
        int n2 = this.innerClassIndex(n);
        if (n2 == 0) {
            return null;
        }
        return this.constPool.getClassInfo(n2);
    }

    public void setInnerClassIndex(int n, int n2) {
        ByteArray.write16bit(n2, this.get(), n * 8 + 2);
    }

    public int outerClassIndex(int n) {
        return ByteArray.readU16bit(this.get(), n * 8 + 4);
    }

    public String outerClass(int n) {
        int n2 = this.outerClassIndex(n);
        if (n2 == 0) {
            return null;
        }
        return this.constPool.getClassInfo(n2);
    }

    public void setOuterClassIndex(int n, int n2) {
        ByteArray.write16bit(n2, this.get(), n * 8 + 4);
    }

    public int innerNameIndex(int n) {
        return ByteArray.readU16bit(this.get(), n * 8 + 6);
    }

    public String innerName(int n) {
        int n2 = this.innerNameIndex(n);
        if (n2 == 0) {
            return null;
        }
        return this.constPool.getUtf8Info(n2);
    }

    public void setInnerNameIndex(int n, int n2) {
        ByteArray.write16bit(n2, this.get(), n * 8 + 6);
    }

    public int accessFlags(int n) {
        return ByteArray.readU16bit(this.get(), n * 8 + 8);
    }

    public void setAccessFlags(int n, int n2) {
        ByteArray.write16bit(n2, this.get(), n * 8 + 8);
    }

    public int find(String string) {
        int n = this.tableLength();
        for (int i = 0; i < n; ++i) {
            if (!string.equals(this.innerClass(i))) continue;
            return i;
        }
        return -1;
    }

    public void append(String string, String string2, String string3, int n) {
        int n2 = this.constPool.addClassInfo(string);
        int n3 = this.constPool.addClassInfo(string2);
        int n4 = this.constPool.addUtf8Info(string3);
        this.append(n2, n3, n4, n);
    }

    public void append(int n, int n2, int n3, int n4) {
        int n5;
        byte[] byArray = this.get();
        int n6 = byArray.length;
        byte[] byArray2 = new byte[n6 + 8];
        for (n5 = 2; n5 < n6; ++n5) {
            byArray2[n5] = byArray[n5];
        }
        n5 = ByteArray.readU16bit(byArray, 0);
        ByteArray.write16bit(n5 + 1, byArray2, 0);
        ByteArray.write16bit(n, byArray2, n6);
        ByteArray.write16bit(n2, byArray2, n6 + 2);
        ByteArray.write16bit(n3, byArray2, n6 + 4);
        ByteArray.write16bit(n4, byArray2, n6 + 6);
        this.set(byArray2);
    }

    public int remove(int n) {
        byte[] byArray = this.get();
        int n2 = byArray.length;
        if (n2 < 10) {
            return 0;
        }
        int n3 = ByteArray.readU16bit(byArray, 0);
        int n4 = 2 + n * 8;
        if (n3 <= n) {
            return n3;
        }
        byte[] byArray2 = new byte[n2 - 8];
        ByteArray.write16bit(n3 - 1, byArray2, 0);
        int n5 = 2;
        int n6 = 2;
        while (n5 < n2) {
            if (n5 == n4) {
                n5 += 8;
                continue;
            }
            byArray2[n6++] = byArray[n5++];
        }
        this.set(byArray2);
        return n3 - 1;
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        byte[] byArray = this.get();
        byte[] byArray2 = new byte[byArray.length];
        ConstPool constPool2 = this.getConstPool();
        InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(constPool, byArray2);
        int n = ByteArray.readU16bit(byArray, 0);
        ByteArray.write16bit(n, byArray2, 0);
        int n2 = 2;
        for (int i = 0; i < n; ++i) {
            int n3 = ByteArray.readU16bit(byArray, n2);
            int n4 = ByteArray.readU16bit(byArray, n2 + 2);
            int n5 = ByteArray.readU16bit(byArray, n2 + 4);
            int n6 = ByteArray.readU16bit(byArray, n2 + 6);
            if (n3 != 0) {
                n3 = constPool2.copy(n3, constPool, map);
            }
            ByteArray.write16bit(n3, byArray2, n2);
            if (n4 != 0) {
                n4 = constPool2.copy(n4, constPool, map);
            }
            ByteArray.write16bit(n4, byArray2, n2 + 2);
            if (n5 != 0) {
                n5 = constPool2.copy(n5, constPool, map);
            }
            ByteArray.write16bit(n5, byArray2, n2 + 4);
            ByteArray.write16bit(n6, byArray2, n2 + 6);
            n2 += 8;
        }
        return innerClassesAttribute;
    }
}

