/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;

public class Util
implements Opcode {
    public static int getJumpTarget(int n, CodeIterator codeIterator) {
        int n2;
        return n += (n2 = codeIterator.byteAt(n)) == 201 || n2 == 200 ? codeIterator.s32bitAt(n + 1) : codeIterator.s16bitAt(n + 1);
    }

    public static boolean isJumpInstruction(int n) {
        return n >= 153 && n <= 168 || n == 198 || n == 199 || n == 201 || n == 200;
    }

    public static boolean isGoto(int n) {
        return n == 167 || n == 200;
    }

    public static boolean isJsr(int n) {
        return n == 168 || n == 201;
    }

    public static boolean isReturn(int n) {
        return n >= 172 && n <= 177;
    }
}

