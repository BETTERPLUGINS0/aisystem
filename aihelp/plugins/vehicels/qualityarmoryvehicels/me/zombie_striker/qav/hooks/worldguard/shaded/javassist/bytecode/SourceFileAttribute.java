/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class SourceFileAttribute
extends AttributeInfo {
    public static final String tag = "SourceFile";

    SourceFileAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    public SourceFileAttribute(ConstPool constPool, String string) {
        super(constPool, tag);
        int n = constPool.addUtf8Info(string);
        byte[] byArray = new byte[]{(byte)(n >>> 8), (byte)n};
        this.set(byArray);
    }

    public String getFileName() {
        return this.getConstPool().getUtf8Info(ByteArray.readU16bit(this.get(), 0));
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        return new SourceFileAttribute(constPool, this.getFileName());
    }
}

