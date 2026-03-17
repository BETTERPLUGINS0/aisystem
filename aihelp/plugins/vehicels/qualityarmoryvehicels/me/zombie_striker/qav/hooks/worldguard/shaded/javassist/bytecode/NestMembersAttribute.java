/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class NestMembersAttribute
extends AttributeInfo {
    public static final String tag = "NestMembers";

    NestMembersAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    private NestMembersAttribute(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        byte[] byArray = this.get();
        byte[] byArray2 = new byte[byArray.length];
        ConstPool constPool2 = this.getConstPool();
        int n = ByteArray.readU16bit(byArray, 0);
        ByteArray.write16bit(n, byArray2, 0);
        int n2 = 0;
        int n3 = 2;
        while (n2 < n) {
            int n4 = ByteArray.readU16bit(byArray, n3);
            int n5 = constPool2.copy(n4, constPool, map);
            ByteArray.write16bit(n5, byArray2, n3);
            ++n2;
            n3 += 2;
        }
        return new NestMembersAttribute(constPool, byArray2);
    }

    public int numberOfClasses() {
        return ByteArray.readU16bit(this.info, 0);
    }

    public int memberClass(int n) {
        return ByteArray.readU16bit(this.info, n * 2 + 2);
    }
}

