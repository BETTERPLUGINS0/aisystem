/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.TransformCall;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.convert.Transformer;

public class TransformBefore
extends TransformCall {
    protected CtClass[] parameterTypes;
    protected int locals;
    protected int maxLocals;
    protected byte[] saveCode;
    protected byte[] loadCode;

    public TransformBefore(Transformer transformer, CtMethod ctMethod, CtMethod ctMethod2) {
        super(transformer, ctMethod, ctMethod2);
        this.methodDescriptor = ctMethod.getMethodInfo2().getDescriptor();
        this.parameterTypes = ctMethod.getParameterTypes();
        this.locals = 0;
        this.maxLocals = 0;
        this.loadCode = null;
        this.saveCode = null;
    }

    @Override
    public void initialize(ConstPool constPool, CodeAttribute codeAttribute) {
        super.initialize(constPool, codeAttribute);
        this.locals = 0;
        this.maxLocals = codeAttribute.getMaxLocals();
        this.loadCode = null;
        this.saveCode = null;
    }

    @Override
    protected int match(int n, int n2, CodeIterator codeIterator, int n3, ConstPool constPool) {
        if (this.newIndex == 0) {
            String string = Descriptor.ofParameters(this.parameterTypes) + 'V';
            string = Descriptor.insertParameter(this.classname, string);
            int n4 = constPool.addNameAndTypeInfo(this.newMethodname, string);
            int n5 = constPool.addClassInfo(this.newClassname);
            this.newIndex = constPool.addMethodrefInfo(n5, n4);
            this.constPool = constPool;
        }
        if (this.saveCode == null) {
            this.makeCode(this.parameterTypes, constPool);
        }
        return this.match2(n2, codeIterator);
    }

    protected int match2(int n, CodeIterator codeIterator) {
        codeIterator.move(n);
        codeIterator.insert(this.saveCode);
        codeIterator.insert(this.loadCode);
        int n2 = codeIterator.insertGap(3);
        codeIterator.writeByte(184, n2);
        codeIterator.write16bit(this.newIndex, n2 + 1);
        codeIterator.insert(this.loadCode);
        return codeIterator.next();
    }

    @Override
    public int extraLocals() {
        return this.locals;
    }

    protected void makeCode(CtClass[] ctClassArray, ConstPool constPool) {
        Bytecode bytecode = new Bytecode(constPool, 0, 0);
        Bytecode bytecode2 = new Bytecode(constPool, 0, 0);
        int n = this.maxLocals;
        int n2 = ctClassArray == null ? 0 : ctClassArray.length;
        bytecode2.addAload(n);
        this.makeCode2(bytecode, bytecode2, 0, n2, ctClassArray, n + 1);
        bytecode.addAstore(n);
        this.saveCode = bytecode.get();
        this.loadCode = bytecode2.get();
    }

    private void makeCode2(Bytecode bytecode, Bytecode bytecode2, int n, int n2, CtClass[] ctClassArray, int n3) {
        if (n < n2) {
            int n4 = bytecode2.addLoad(n3, ctClassArray[n]);
            this.makeCode2(bytecode, bytecode2, n + 1, n2, ctClassArray, n3 + n4);
            bytecode.addStore(n3, ctClassArray[n]);
        } else {
            this.locals = n3 - this.maxLocals;
        }
    }
}

