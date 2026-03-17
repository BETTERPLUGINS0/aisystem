/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformBefore;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public class TransformAfter
extends TransformBefore {
    public TransformAfter(Transformer transformer, CtMethod ctMethod, CtMethod ctMethod2) {
        super(transformer, ctMethod, ctMethod2);
    }

    @Override
    protected int match2(int n, CodeIterator codeIterator) {
        codeIterator.move(n);
        codeIterator.insert(this.saveCode);
        codeIterator.insert(this.loadCode);
        int n2 = codeIterator.insertGap(3);
        codeIterator.setMark(n2);
        codeIterator.insert(this.loadCode);
        n = codeIterator.next();
        n2 = codeIterator.getMark();
        codeIterator.writeByte(codeIterator.byteAt(n), n2);
        codeIterator.write16bit(codeIterator.u16bitAt(n + 1), n2 + 1);
        codeIterator.writeByte(184, n);
        codeIterator.write16bit(this.newIndex, n + 1);
        codeIterator.move(n2);
        return codeIterator.next();
    }
}

