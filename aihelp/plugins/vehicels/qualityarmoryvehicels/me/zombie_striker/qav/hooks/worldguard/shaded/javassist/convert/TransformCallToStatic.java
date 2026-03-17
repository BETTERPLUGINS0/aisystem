/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformCall;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public class TransformCallToStatic
extends TransformCall {
    public TransformCallToStatic(Transformer transformer, CtMethod ctMethod, CtMethod ctMethod2) {
        super(transformer, ctMethod, ctMethod2);
        this.methodDescriptor = ctMethod.getMethodInfo2().getDescriptor();
    }

    @Override
    protected int match(int n, int n2, CodeIterator codeIterator, int n3, ConstPool constPool) {
        if (this.newIndex == 0) {
            String string = Descriptor.insertParameter(this.classname, this.methodDescriptor);
            int n4 = constPool.addNameAndTypeInfo(this.newMethodname, string);
            int n5 = constPool.addClassInfo(this.newClassname);
            this.newIndex = constPool.addMethodrefInfo(n5, n4);
            this.constPool = constPool;
        }
        codeIterator.writeByte(184, n2);
        codeIterator.write16bit(this.newIndex, n2 + 1);
        return n2;
    }
}

