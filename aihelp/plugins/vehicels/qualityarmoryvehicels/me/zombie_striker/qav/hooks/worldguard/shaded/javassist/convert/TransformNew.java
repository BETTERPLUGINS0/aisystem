/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.StackMapTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public final class TransformNew
extends Transformer {
    private int nested;
    private String classname;
    private String trapClass;
    private String trapMethod;

    public TransformNew(Transformer transformer, String string, String string2, String string3) {
        super(transformer);
        this.classname = string;
        this.trapClass = string2;
        this.trapMethod = string3;
    }

    @Override
    public void initialize(ConstPool constPool, CodeAttribute codeAttribute) {
        this.nested = 0;
    }

    @Override
    public int transform(CtClass ctClass, int n, CodeIterator codeIterator, ConstPool constPool) {
        int n2;
        int n3;
        int n4 = codeIterator.byteAt(n);
        if (n4 == 187) {
            int n5 = codeIterator.u16bitAt(n + 1);
            if (constPool.getClassInfo(n5).equals(this.classname)) {
                StackMap stackMap;
                if (codeIterator.byteAt(n + 3) != 89) {
                    throw new CannotCompileException("NEW followed by no DUP was found");
                }
                codeIterator.writeByte(0, n);
                codeIterator.writeByte(0, n + 1);
                codeIterator.writeByte(0, n + 2);
                codeIterator.writeByte(0, n + 3);
                ++this.nested;
                StackMapTable stackMapTable = (StackMapTable)codeIterator.get().getAttribute("StackMapTable");
                if (stackMapTable != null) {
                    stackMapTable.removeNew(n);
                }
                if ((stackMap = (StackMap)codeIterator.get().getAttribute("StackMap")) != null) {
                    stackMap.removeNew(n);
                }
            }
        } else if (n4 == 183 && (n3 = constPool.isConstructor(this.classname, n2 = codeIterator.u16bitAt(n + 1))) != 0 && this.nested > 0) {
            int n6 = this.computeMethodref(n3, constPool);
            codeIterator.writeByte(184, n);
            codeIterator.write16bit(n6, n + 1);
            --this.nested;
        }
        return n;
    }

    private int computeMethodref(int n, ConstPool constPool) {
        int n2 = constPool.addClassInfo(this.trapClass);
        int n3 = constPool.addUtf8Info(this.trapMethod);
        n = constPool.addUtf8Info(Descriptor.changeReturnType(this.classname, constPool.getUtf8Info(n)));
        return constPool.addMethodrefInfo(n2, constPool.addNameAndTypeInfo(n3, n));
    }
}

