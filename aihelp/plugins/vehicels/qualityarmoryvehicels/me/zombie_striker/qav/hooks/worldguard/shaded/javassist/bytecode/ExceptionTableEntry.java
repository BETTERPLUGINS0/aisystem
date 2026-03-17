/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode;

class ExceptionTableEntry {
    int startPc;
    int endPc;
    int handlerPc;
    int catchType;

    ExceptionTableEntry(int n, int n2, int n3, int n4) {
        this.startPc = n;
        this.endPc = n2;
        this.handlerPc = n3;
        this.catchType = n4;
    }
}

