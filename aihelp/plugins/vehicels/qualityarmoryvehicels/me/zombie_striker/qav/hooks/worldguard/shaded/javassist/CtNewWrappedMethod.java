/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClassType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AccessFlag;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SyntheticAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstCodeGen;

class CtNewWrappedMethod {
    private static final String addedWrappedMethod = "_added_m$";

    CtNewWrappedMethod() {
    }

    public static CtMethod wrapped(CtClass ctClass, String string, CtClass[] ctClassArray, CtClass[] ctClassArray2, CtMethod ctMethod, CtMethod.ConstParameter constParameter, CtClass ctClass2) {
        CtMethod ctMethod2 = new CtMethod(ctClass, string, ctClassArray, ctClass2);
        ctMethod2.setModifiers(ctMethod.getModifiers());
        try {
            ctMethod2.setExceptionTypes(ctClassArray2);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
        Bytecode bytecode = CtNewWrappedMethod.makeBody(ctClass2, ctClass2.getClassFile2(), ctMethod, ctClassArray, ctClass, constParameter);
        MethodInfo methodInfo = ctMethod2.getMethodInfo2();
        methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
        return ctMethod2;
    }

    static Bytecode makeBody(CtClass ctClass, ClassFile classFile, CtMethod ctMethod, CtClass[] ctClassArray, CtClass ctClass2, CtMethod.ConstParameter constParameter) {
        boolean bl = Modifier.isStatic(ctMethod.getModifiers());
        Bytecode bytecode = new Bytecode(classFile.getConstPool(), 0, 0);
        int n = CtNewWrappedMethod.makeBody0(ctClass, classFile, ctMethod, bl, ctClassArray, ctClass2, constParameter, bytecode);
        bytecode.setMaxStack(n);
        bytecode.setMaxLocals(bl, ctClassArray, 0);
        return bytecode;
    }

    protected static int makeBody0(CtClass ctClass, ClassFile classFile, CtMethod ctMethod, boolean bl, CtClass[] ctClassArray, CtClass ctClass2, CtMethod.ConstParameter constParameter, Bytecode bytecode) {
        String string;
        String string2;
        int n;
        if (!(ctClass instanceof CtClassType)) {
            throw new CannotCompileException("bad declaring class" + ctClass.getName());
        }
        if (!bl) {
            bytecode.addAload(0);
        }
        int n2 = CtNewWrappedMethod.compileParameterList(bytecode, ctClassArray, bl ? 0 : 1);
        if (constParameter == null) {
            n = 0;
            string2 = CtMethod.ConstParameter.defaultDescriptor();
        } else {
            n = constParameter.compile(bytecode);
            string2 = constParameter.descriptor();
        }
        CtNewWrappedMethod.checkSignature(ctMethod, string2);
        try {
            string = CtNewWrappedMethod.addBodyMethod((CtClassType)ctClass, classFile, ctMethod);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
        if (bl) {
            bytecode.addInvokestatic(Bytecode.THIS, string, string2);
        } else {
            bytecode.addInvokespecial(Bytecode.THIS, string, string2);
        }
        CtNewWrappedMethod.compileReturn(bytecode, ctClass2);
        if (n2 < n + 2) {
            n2 = n + 2;
        }
        return n2;
    }

    private static void checkSignature(CtMethod ctMethod, String string) {
        if (!string.equals(ctMethod.getMethodInfo2().getDescriptor())) {
            throw new CannotCompileException("wrapped method with a bad signature: " + ctMethod.getDeclaringClass().getName() + '.' + ctMethod.getName());
        }
    }

    private static String addBodyMethod(CtClassType ctClassType, ClassFile classFile, CtMethod ctMethod) {
        Map<CtMethod, String> map = ctClassType.getHiddenMethods();
        String string = map.get(ctMethod);
        if (string == null) {
            while (classFile.getMethod(string = addedWrappedMethod + ctClassType.getUniqueNumber()) != null) {
            }
            ClassMap classMap = new ClassMap();
            classMap.put(ctMethod.getDeclaringClass().getName(), ctClassType.getName());
            MethodInfo methodInfo = new MethodInfo(classFile.getConstPool(), string, ctMethod.getMethodInfo2(), classMap);
            int n = methodInfo.getAccessFlags();
            methodInfo.setAccessFlags(AccessFlag.setPrivate(n));
            methodInfo.addAttribute(new SyntheticAttribute(classFile.getConstPool()));
            classFile.addMethod(methodInfo);
            map.put(ctMethod, string);
            CtMember.Cache cache = ctClassType.hasMemberCache();
            if (cache != null) {
                cache.addMethod(new CtMethod(methodInfo, ctClassType));
            }
        }
        return string;
    }

    static int compileParameterList(Bytecode bytecode, CtClass[] ctClassArray, int n) {
        return JvstCodeGen.compileParameterList(bytecode, ctClassArray, n);
    }

    private static void compileReturn(Bytecode bytecode, CtClass ctClass) {
        if (ctClass.isPrimitive()) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            if (ctPrimitiveType != CtClass.voidType) {
                String string = ctPrimitiveType.getWrapperName();
                bytecode.addCheckcast(string);
                bytecode.addInvokevirtual(string, ctPrimitiveType.getGetMethodName(), ctPrimitiveType.getGetMethodDescriptor());
            }
            bytecode.addOpcode(ctPrimitiveType.getReturnOp());
        } else {
            bytecode.addCheckcast(ctClass);
            bytecode.addOpcode(176);
        }
    }
}

