/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewWrappedMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;

public class CtNewMethod {
    public static CtMethod make(String string, CtClass ctClass) {
        return CtNewMethod.make(string, ctClass, null, null);
    }

    public static CtMethod make(String string, CtClass ctClass, String string2, String string3) {
        Javac javac = new Javac(ctClass);
        try {
            CtMember ctMember;
            if (string3 != null) {
                javac.recordProceed(string2, string3);
            }
            if ((ctMember = javac.compile(string)) instanceof CtMethod) {
                return (CtMethod)ctMember;
            }
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        }
        throw new CannotCompileException("not a method");
    }

    public static CtMethod make(CtClass ctClass, String string, CtClass[] ctClassArray, CtClass[] ctClassArray2, String string2, CtClass ctClass2) {
        return CtNewMethod.make(1, ctClass, string, ctClassArray, ctClassArray2, string2, ctClass2);
    }

    public static CtMethod make(int n, CtClass ctClass, String string, CtClass[] ctClassArray, CtClass[] ctClassArray2, String string2, CtClass ctClass2) {
        try {
            CtMethod ctMethod = new CtMethod(ctClass, string, ctClassArray, ctClass2);
            ctMethod.setModifiers(n);
            ctMethod.setExceptionTypes(ctClassArray2);
            ctMethod.setBody(string2);
            return ctMethod;
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
    }

    public static CtMethod copy(CtMethod ctMethod, CtClass ctClass, ClassMap classMap) {
        return new CtMethod(ctMethod, ctClass, classMap);
    }

    public static CtMethod copy(CtMethod ctMethod, String string, CtClass ctClass, ClassMap classMap) {
        CtMethod ctMethod2 = new CtMethod(ctMethod, ctClass, classMap);
        ctMethod2.setName(string);
        return ctMethod2;
    }

    public static CtMethod abstractMethod(CtClass ctClass, String string, CtClass[] ctClassArray, CtClass[] ctClassArray2, CtClass ctClass2) {
        CtMethod ctMethod = new CtMethod(ctClass, string, ctClassArray, ctClass2);
        ctMethod.setExceptionTypes(ctClassArray2);
        return ctMethod;
    }

    public static CtMethod getter(String string, CtField ctField) {
        Object object;
        FieldInfo fieldInfo = ctField.getFieldInfo2();
        String string2 = fieldInfo.getDescriptor();
        String string3 = "()" + string2;
        ConstPool constPool = fieldInfo.getConstPool();
        MethodInfo methodInfo = new MethodInfo(constPool, string, string3);
        methodInfo.setAccessFlags(1);
        Bytecode bytecode = new Bytecode(constPool, 2, 1);
        try {
            object = fieldInfo.getName();
            if ((fieldInfo.getAccessFlags() & 8) == 0) {
                bytecode.addAload(0);
                bytecode.addGetfield(Bytecode.THIS, (String)object, string2);
            } else {
                bytecode.addGetstatic(Bytecode.THIS, (String)object, string2);
            }
            bytecode.addReturn(ctField.getType());
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        object = ctField.getDeclaringClass();
        return new CtMethod(methodInfo, (CtClass)object);
    }

    public static CtMethod setter(String string, CtField ctField) {
        Object object;
        FieldInfo fieldInfo = ctField.getFieldInfo2();
        String string2 = fieldInfo.getDescriptor();
        String string3 = "(" + string2 + ")V";
        ConstPool constPool = fieldInfo.getConstPool();
        MethodInfo methodInfo = new MethodInfo(constPool, string, string3);
        methodInfo.setAccessFlags(1);
        Bytecode bytecode = new Bytecode(constPool, 3, 3);
        try {
            object = fieldInfo.getName();
            if ((fieldInfo.getAccessFlags() & 8) == 0) {
                bytecode.addAload(0);
                bytecode.addLoad(1, ctField.getType());
                bytecode.addPutfield(Bytecode.THIS, (String)object, string2);
            } else {
                bytecode.addLoad(1, ctField.getType());
                bytecode.addPutstatic(Bytecode.THIS, (String)object, string2);
            }
            bytecode.addReturn(null);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        object = ctField.getDeclaringClass();
        return new CtMethod(methodInfo, (CtClass)object);
    }

    public static CtMethod delegator(CtMethod ctMethod, CtClass ctClass) {
        try {
            return CtNewMethod.delegator0(ctMethod, ctClass);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
    }

    private static CtMethod delegator0(CtMethod ctMethod, CtClass ctClass) {
        int n;
        MethodInfo methodInfo = ctMethod.getMethodInfo2();
        String string = methodInfo.getName();
        String string2 = methodInfo.getDescriptor();
        ConstPool constPool = ctClass.getClassFile2().getConstPool();
        MethodInfo methodInfo2 = new MethodInfo(constPool, string, string2);
        methodInfo2.setAccessFlags(methodInfo.getAccessFlags());
        ExceptionsAttribute exceptionsAttribute = methodInfo.getExceptionsAttribute();
        if (exceptionsAttribute != null) {
            methodInfo2.setExceptionsAttribute((ExceptionsAttribute)exceptionsAttribute.copy(constPool, null));
        }
        Bytecode bytecode = new Bytecode(constPool, 0, 0);
        boolean bl = Modifier.isStatic(ctMethod.getModifiers());
        CtClass ctClass2 = ctMethod.getDeclaringClass();
        CtClass[] ctClassArray = ctMethod.getParameterTypes();
        if (bl) {
            n = bytecode.addLoadParameters(ctClassArray, 0);
            bytecode.addInvokestatic(ctClass2, string, string2);
        } else {
            bytecode.addLoad(0, ctClass2);
            n = bytecode.addLoadParameters(ctClassArray, 1);
            bytecode.addInvokespecial(ctClass2, string, string2);
        }
        bytecode.addReturn(ctMethod.getReturnType());
        bytecode.setMaxLocals(++n);
        bytecode.setMaxStack(n < 2 ? 2 : n);
        methodInfo2.setCodeAttribute(bytecode.toCodeAttribute());
        return new CtMethod(methodInfo2, ctClass);
    }

    public static CtMethod wrapped(CtClass ctClass, String string, CtClass[] ctClassArray, CtClass[] ctClassArray2, CtMethod ctMethod, CtMethod.ConstParameter constParameter, CtClass ctClass2) {
        return CtNewWrappedMethod.wrapped(ctClass, string, ctClassArray, ctClassArray2, ctMethod, constParameter, ctClass2);
    }
}

