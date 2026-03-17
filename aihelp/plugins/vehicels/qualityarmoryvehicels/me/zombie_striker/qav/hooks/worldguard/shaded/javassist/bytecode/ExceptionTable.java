/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTableEntry;

public class ExceptionTable
implements Cloneable {
    private ConstPool constPool;
    private List<ExceptionTableEntry> entries;

    public ExceptionTable(ConstPool constPool) {
        this.constPool = constPool;
        this.entries = new ArrayList<ExceptionTableEntry>();
    }

    ExceptionTable(ConstPool constPool, DataInputStream dataInputStream) {
        this.constPool = constPool;
        int n = dataInputStream.readUnsignedShort();
        ArrayList<ExceptionTableEntry> arrayList = new ArrayList<ExceptionTableEntry>(n);
        for (int i = 0; i < n; ++i) {
            int n2 = dataInputStream.readUnsignedShort();
            int n3 = dataInputStream.readUnsignedShort();
            int n4 = dataInputStream.readUnsignedShort();
            int n5 = dataInputStream.readUnsignedShort();
            arrayList.add(new ExceptionTableEntry(n2, n3, n4, n5));
        }
        this.entries = arrayList;
    }

    public Object clone() {
        ExceptionTable exceptionTable = (ExceptionTable)super.clone();
        exceptionTable.entries = new ArrayList<ExceptionTableEntry>(this.entries);
        return exceptionTable;
    }

    public int size() {
        return this.entries.size();
    }

    public int startPc(int n) {
        return this.entries.get((int)n).startPc;
    }

    public void setStartPc(int n, int n2) {
        this.entries.get((int)n).startPc = n2;
    }

    public int endPc(int n) {
        return this.entries.get((int)n).endPc;
    }

    public void setEndPc(int n, int n2) {
        this.entries.get((int)n).endPc = n2;
    }

    public int handlerPc(int n) {
        return this.entries.get((int)n).handlerPc;
    }

    public void setHandlerPc(int n, int n2) {
        this.entries.get((int)n).handlerPc = n2;
    }

    public int catchType(int n) {
        return this.entries.get((int)n).catchType;
    }

    public void setCatchType(int n, int n2) {
        this.entries.get((int)n).catchType = n2;
    }

    public void add(int n, ExceptionTable exceptionTable, int n2) {
        int n3 = exceptionTable.size();
        while (--n3 >= 0) {
            ExceptionTableEntry exceptionTableEntry = exceptionTable.entries.get(n3);
            this.add(n, exceptionTableEntry.startPc + n2, exceptionTableEntry.endPc + n2, exceptionTableEntry.handlerPc + n2, exceptionTableEntry.catchType);
        }
    }

    public void add(int n, int n2, int n3, int n4, int n5) {
        if (n2 < n3) {
            this.entries.add(n, new ExceptionTableEntry(n2, n3, n4, n5));
        }
    }

    public void add(int n, int n2, int n3, int n4) {
        if (n < n2) {
            this.entries.add(new ExceptionTableEntry(n, n2, n3, n4));
        }
    }

    public void remove(int n) {
        this.entries.remove(n);
    }

    public ExceptionTable copy(ConstPool constPool, Map<String, String> map) {
        ExceptionTable exceptionTable = new ExceptionTable(constPool);
        ConstPool constPool2 = this.constPool;
        for (ExceptionTableEntry exceptionTableEntry : this.entries) {
            int n = constPool2.copy(exceptionTableEntry.catchType, constPool, map);
            exceptionTable.add(exceptionTableEntry.startPc, exceptionTableEntry.endPc, exceptionTableEntry.handlerPc, n);
        }
        return exceptionTable;
    }

    void shiftPc(int n, int n2, boolean bl) {
        for (ExceptionTableEntry exceptionTableEntry : this.entries) {
            exceptionTableEntry.startPc = ExceptionTable.shiftPc(exceptionTableEntry.startPc, n, n2, bl);
            exceptionTableEntry.endPc = ExceptionTable.shiftPc(exceptionTableEntry.endPc, n, n2, bl);
            exceptionTableEntry.handlerPc = ExceptionTable.shiftPc(exceptionTableEntry.handlerPc, n, n2, bl);
        }
    }

    private static int shiftPc(int n, int n2, int n3, boolean bl) {
        if (n > n2 || bl && n == n2) {
            n += n3;
        }
        return n;
    }

    void write(DataOutputStream dataOutputStream) {
        dataOutputStream.writeShort(this.size());
        for (ExceptionTableEntry exceptionTableEntry : this.entries) {
            dataOutputStream.writeShort(exceptionTableEntry.startPc);
            dataOutputStream.writeShort(exceptionTableEntry.endPc);
            dataOutputStream.writeShort(exceptionTableEntry.handlerPc);
            dataOutputStream.writeShort(exceptionTableEntry.catchType);
        }
    }
}

