/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public final class TransformNewClass
extends Transformer {
    private int nested;
    private String classname;
    private String newClassName;
    private int newClassIndex;
    private int newMethodNTIndex;
    private int newMethodIndex;

    public TransformNewClass(Transformer transformer, String string, String string2) {
        super(transformer);
        this.classname = string;
        this.newClassName = string2;
    }

    @Override
    public void initialize(ConstPool constPool, CodeAttribute codeAttribute) {
        this.nested = 0;
        this.newMethodIndex = 0;
        this.newMethodNTIndex = 0;
        this.newClassIndex = 0;
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        int n2;
        int n3;
        int n4 = codeIterator.byteAt(n);
        if (n4 == 187) {
            int n5 = codeIterator.u16bitAt(n + 1);
            if (constPool.getClassInfo(n5).equals(this.classname)) {
                if (codeIterator.byteAt(n + 3) != 89) {
                    throw new CannotCompileException("NEW followed by no DUP was found");
                }
                if (this.newClassIndex == 0) {
                    this.newClassIndex = constPool.addClassInfo(this.newClassName);
                }
                codeIterator.write16bit(this.newClassIndex, n + 1);
                ++this.nested;
            }
        } else if (n4 == 183 && (n3 = constPool.isConstructor(this.classname, n2 = codeIterator.u16bitAt(n + 1))) != 0 && this.nested > 0) {
            int n6 = constPool.getMethodrefNameAndType(n2);
            if (this.newMethodNTIndex != n6) {
                this.newMethodNTIndex = n6;
                this.newMethodIndex = constPool.addMethodrefInfo(this.newClassIndex, n6);
            }
            codeIterator.write16bit(this.newMethodIndex, n + 1);
            --this.nested;
        }
        return n;
    }
}

