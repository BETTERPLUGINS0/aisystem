/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class NestHostAttribute
extends AttributeInfo {
    public static final String tag = "NestHost";

    NestHostAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    private NestHostAttribute(ConstPool constPool, int n) {
        super(constPool, tag, new byte[2]);
        ByteArray.write16bit(n, this.get(), 0);
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        int n = ByteArray.readU16bit(this.get(), 0);
        int n2 = this.getConstPool().copy(n, constPool, map);
        return new NestHostAttribute(constPool, n2);
    }

    public int hostClassIndex() {
        return ByteArray.readU16bit(this.info, 0);
    }
}

