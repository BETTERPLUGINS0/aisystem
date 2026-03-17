/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewWrappedMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;

class CtNewWrappedConstructor
extends CtNewWrappedMethod {
    private static final int PASS_NONE = 0;
    private static final int PASS_PARAMS = 2;

    CtNewWrappedConstructor() {
    }

    public static CtConstructor wrapped(CtClass[] ctClassArray, CtClass[] ctClassArray2, int n, CtMethod ctMethod, CtMethod.ConstParameter constParameter, CtClass ctClass) {
        try {
            CtConstructor ctConstructor = new CtConstructor(ctClassArray, ctClass);
            ctConstructor.setExceptionTypes(ctClassArray2);
            Bytecode bytecode = CtNewWrappedConstructor.makeBody(ctClass, ctClass.getClassFile2(), n, ctMethod, ctClassArray, constParameter);
            ctConstructor.getMethodInfo2().setCodeAttribute(bytecode.toCodeAttribute());
            return ctConstructor;
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
    }

    protected static Bytecode makeBody(CtClass ctClass, ClassFile classFile, int n, CtMethod ctMethod, CtClass[] ctClassArray, CtMethod.ConstParameter constParameter) {
        int n2;
        int n3;
        int n4 = classFile.getSuperclassId();
        Bytecode bytecode = new Bytecode(classFile.getConstPool(), 0, 0);
        bytecode.setMaxLocals(false, ctClassArray, 0);
        bytecode.addAload(0);
        if (n == 0) {
            n3 = 1;
            bytecode.addInvokespecial(n4, "<init>", "()V");
        } else if (n == 2) {
            n3 = bytecode.addLoadParameters(ctClassArray, 1) + 1;
            bytecode.addInvokespecial(n4, "<init>", Descriptor.ofConstructor(ctClassArray));
        } else {
            String string;
            n3 = CtNewWrappedConstructor.compileParameterList(bytecode, ctClassArray, 1);
            if (constParameter == null) {
                n2 = 2;
                string = CtMethod.ConstParameter.defaultConstDescriptor();
            } else {
                n2 = constParameter.compile(bytecode) + 2;
                string = constParameter.constDescriptor();
            }
            if (n3 < n2) {
                n3 = n2;
            }
            bytecode.addInvokespecial(n4, "<init>", string);
        }
        if (ctMethod == null) {
            bytecode.add(177);
        } else {
            n2 = CtNewWrappedConstructor.makeBody0(ctClass, classFile, ctMethod, false, ctClassArray, CtClass.voidType, constParameter, bytecode);
            if (n3 < n2) {
                n3 = n2;
            }
        }
        bytecode.setMaxStack(n3);
        return bytecode;
    }
}

