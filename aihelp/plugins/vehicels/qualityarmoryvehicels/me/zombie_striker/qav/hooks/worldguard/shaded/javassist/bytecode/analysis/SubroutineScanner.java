/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Subroutine;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.analysis.Util;

public class SubroutineScanner
implements Opcode {
    private Subroutine[] subroutines;
    Map<Integer, Subroutine> subTable = new HashMap<Integer, Subroutine>();
    Set<Integer> done = new HashSet<Integer>();

    public Subroutine[] scan(MethodInfo methodInfo) {
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        CodeIterator codeIterator = codeAttribute.iterator();
        this.subroutines = new Subroutine[codeAttribute.getCodeLength()];
        this.subTable.clear();
        this.done.clear();
        this.scan(0, codeIterator, null);
        ExceptionTable exceptionTable = codeAttribute.getExceptionTable();
        for (int i = 0; i < exceptionTable.size(); ++i) {
            int n = exceptionTable.handlerPc(i);
            this.scan(n, codeIterator, this.subroutines[exceptionTable.startPc(i)]);
        }
        return this.subroutines;
    }

    private void scan(int n, CodeIterator codeIterator, Subroutine subroutine) {
        boolean bl;
        if (this.done.contains(n)) {
            return;
        }
        this.done.add(n);
        int n2 = codeIterator.lookAhead();
        codeIterator.move(n);
        while (bl = this.scanOp(n = codeIterator.next(), codeIterator, subroutine) && codeIterator.hasNext()) {
        }
        codeIterator.move(n2);
    }

    private boolean scanOp(int n, CodeIterator codeIterator, Subroutine subroutine) {
        this.subroutines[n] = subroutine;
        int n2 = codeIterator.byteAt(n);
        if (n2 == 170) {
            this.scanTableSwitch(n, codeIterator, subroutine);
            return false;
        }
        if (n2 == 171) {
            this.scanLookupSwitch(n, codeIterator, subroutine);
            return false;
        }
        if (Util.isReturn(n2) || n2 == 169 || n2 == 191) {
            return false;
        }
        if (Util.isJumpInstruction(n2)) {
            int n3 = Util.getJumpTarget(n, codeIterator);
            if (n2 == 168 || n2 == 201) {
                Subroutine subroutine2 = this.subTable.get(n3);
                if (subroutine2 == null) {
                    subroutine2 = new Subroutine(n3, n);
                    this.subTable.put(n3, subroutine2);
                    this.scan(n3, codeIterator, subroutine2);
                } else {
                    subroutine2.addCaller(n);
                }
            } else {
                this.scan(n3, codeIterator, subroutine);
                if (Util.isGoto(n2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void scanLookupSwitch(int n, CodeIterator codeIterator, Subroutine subroutine) {
        int n2 = (n & 0xFFFFFFFC) + 4;
        this.scan(n + codeIterator.s32bitAt(n2), codeIterator, subroutine);
        int n3 = codeIterator.s32bitAt(n2 += 4);
        int n4 = n3 * 8 + (n2 += 4);
        n2 += 4;
        while (n2 < n4) {
            int n5 = codeIterator.s32bitAt(n2) + n;
            this.scan(n5, codeIterator, subroutine);
            n2 += 8;
        }
    }

    private void scanTableSwitch(int n, CodeIterator codeIterator, Subroutine subroutine) {
        int n2 = (n & 0xFFFFFFFC) + 4;
        this.scan(n + codeIterator.s32bitAt(n2), codeIterator, subroutine);
        int n3 = codeIterator.s32bitAt(n2 += 4);
        int n4 = codeIterator.s32bitAt(n2 += 4);
        int n5 = (n4 - n3 + 1) * 4 + (n2 += 4);
        while (n2 < n5) {
            int n6 = codeIterator.s32bitAt(n2) + n;
            this.scan(n6, codeIterator, subroutine);
            n2 += 4;
        }
    }
}

