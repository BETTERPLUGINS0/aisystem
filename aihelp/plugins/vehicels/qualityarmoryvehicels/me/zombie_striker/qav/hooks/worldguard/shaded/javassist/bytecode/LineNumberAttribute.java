/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ByteArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;

public class LineNumberAttribute
extends AttributeInfo {
    public static final String tag = "LineNumberTable";

    LineNumberAttribute(ConstPool constPool, int n, DataInputStream dataInputStream) {
        super(constPool, n, dataInputStream);
    }

    private LineNumberAttribute(ConstPool constPool, byte[] byArray) {
        super(constPool, tag, byArray);
    }

    public int tableLength() {
        return ByteArray.readU16bit(this.info, 0);
    }

    public int startPc(int n) {
        return ByteArray.readU16bit(this.info, n * 4 + 2);
    }

    public int lineNumber(int n) {
        return ByteArray.readU16bit(this.info, n * 4 + 4);
    }

    public int toLineNumber(int n) {
        int n2;
        int n3 = this.tableLength();
        for (n2 = 0; n2 < n3; ++n2) {
            if (n >= this.startPc(n2)) continue;
            if (n2 != 0) break;
            return this.lineNumber(0);
        }
        return this.lineNumber(n2 - 1);
    }

    public int toStartPc(int n) {
        int n2 = this.tableLength();
        for (int i = 0; i < n2; ++i) {
            if (n != this.lineNumber(i)) continue;
            return this.startPc(i);
        }
        return -1;
    }

    public Pc toNearPc(int n) {
        int n2 = this.tableLength();
        int n3 = 0;
        int n4 = 0;
        if (n2 > 0) {
            n4 = this.lineNumber(0) - n;
            n3 = this.startPc(0);
        }
        for (int i = 1; i < n2; ++i) {
            int n5 = this.lineNumber(i) - n;
            if ((n5 >= 0 || n5 <= n4) && (n5 < 0 || n5 >= n4 && n4 >= 0)) continue;
            n4 = n5;
            n3 = this.startPc(i);
        }
        Pc pc = new Pc();
        pc.index = n3;
        pc.line = n + n4;
        return pc;
    }

    @Override
    public AttributeInfo copy(ConstPool constPool, Map<String, String> map) {
        byte[] byArray = this.info;
        int n = byArray.length;
        byte[] byArray2 = new byte[n];
        for (int i = 0; i < n; ++i) {
            byArray2[i] = byArray[i];
        }
        LineNumberAttribute lineNumberAttribute = new LineNumberAttribute(constPool, byArray2);
        return lineNumberAttribute;
    }

    void shiftPc(int n, int n2, boolean bl) {
        int n3 = this.tableLength();
        for (int i = 0; i < n3; ++i) {
            int n4 = i * 4 + 2;
            int n5 = ByteArray.readU16bit(this.info, n4);
            if (n5 <= n && (!bl || n5 != n)) continue;
            ByteArray.write16bit(n5 + n2, this.info, n4);
        }
    }

    public static class Pc {
        public int index;
        public int line;
    }
}

