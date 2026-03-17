/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewWrappedConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;

public class CtNewConstructor {
    public static final int PASS_NONE = 0;
    public static final int PASS_ARRAY = 1;
    public static final int PASS_PARAMS = 2;

    public static CtConstructor make(String string, CtClass ctClass) {
        Javac javac = new Javac(ctClass);
        try {
            CtMember ctMember = javac.compile(string);
            if (ctMember instanceof CtConstructor) {
                return (CtConstructor)ctMember;
            }
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        }
        throw new CannotCompileException("not a constructor");
    }

    public static CtConstructor make(CtClass[] ctClassArray, CtClass[] ctClassArray2, String string, CtClass ctClass) {
        try {
            CtConstructor ctConstructor = new CtConstructor(ctClassArray, ctClass);
            ctConstructor.setExceptionTypes(ctClassArray2);
            ctConstructor.setBody(string);
            return ctConstructor;
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
    }

    public static CtConstructor copy(CtConstructor ctConstructor, CtClass ctClass, ClassMap classMap) {
        return new CtConstructor(ctConstructor, ctClass, classMap);
    }

    public static CtConstructor defaultConstructor(CtClass ctClass) {
        CtConstructor ctConstructor = new CtConstructor((CtClass[])null, ctClass);
        ConstPool constPool = ctClass.getClassFile2().getConstPool();
        Bytecode bytecode = new Bytecode(constPool, 1, 1);
        bytecode.addAload(0);
        try {
            bytecode.addInvokespecial(ctClass.getSuperclass(), "<init>", "()V");
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
        bytecode.add(177);
        ctConstructor.getMethodInfo2().setCodeAttribute(bytecode.toCodeAttribute());
        return ctConstructor;
    }

    public static CtConstructor skeleton(CtClass[] ctClassArray, CtClass[] ctClassArray2, CtClass ctClass) {
        return CtNewConstructor.make(ctClassArray, ctClassArray2, 0, null, null, ctClass);
    }

    public static CtConstructor make(CtClass[] ctClassArray, CtClass[] ctClassArray2, CtClass ctClass) {
        return CtNewConstructor.make(ctClassArray, ctClassArray2, 2, null, null, ctClass);
    }

    public static CtConstructor make(CtClass[] ctClassArray, CtClass[] ctClassArray2, int n, CtMethod ctMethod, CtMethod.ConstParameter constParameter, CtClass ctClass) {
        return CtNewWrappedConstructor.wrapped(ctClassArray, ctClassArray2, n, ctMethod, constParameter, ctClass);
    }
}

