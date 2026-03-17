/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import java.util.HashMap;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.SyntheticAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;

public class AccessorMaker {
    private CtClass clazz;
    private int uniqueNumber;
    private Map<String, Object> accessors;
    static final String lastParamType = "me.zombie_striker.qav.hooks.worldguard.shaded.javassist.runtime.Inner";

    public AccessorMaker(CtClass ctClass) {
        this.clazz = ctClass;
        this.uniqueNumber = 1;
        this.accessors = new HashMap<String, Object>();
    }

    public String getConstructor(CtClass ctClass, String string, MethodInfo methodInfo) {
        String string2 = "<init>:" + string;
        String string3 = (String)this.accessors.get(string2);
        if (string3 != null) {
            return string3;
        }
        string3 = Descriptor.appendParameter(lastParamType, string);
        ClassFile classFile = this.clazz.getClassFile();
        try {
            ConstPool constPool = classFile.getConstPool();
            ClassPool classPool = this.clazz.getClassPool();
            MethodInfo methodInfo2 = new MethodInfo(constPool, "<init>", string3);
            methodInfo2.setAccessFlags(0);
            methodInfo2.addAttribute(new SyntheticAttribute(constPool));
            ExceptionsAttribute exceptionsAttribute = methodInfo.getExceptionsAttribute();
            if (exceptionsAttribute != null) {
                methodInfo2.addAttribute(exceptionsAttribute.copy(constPool, null));
            }
            CtClass[] ctClassArray = Descriptor.getParameterTypes(string, classPool);
            Bytecode bytecode = new Bytecode(constPool);
            bytecode.addAload(0);
            int n = 1;
            for (int i = 0; i < ctClassArray.length; ++i) {
                n += bytecode.addLoad(n, ctClassArray[i]);
            }
            bytecode.setMaxLocals(n + 1);
            bytecode.addInvokespecial(this.clazz, "<init>", string);
            bytecode.addReturn(null);
            methodInfo2.setCodeAttribute(bytecode.toCodeAttribute());
            classFile.addMethod(methodInfo2);
        } catch (CannotCompileException cannotCompileException) {
            throw new CompileError(cannotCompileException);
        } catch (NotFoundException notFoundException) {
            throw new CompileError(notFoundException);
        }
        this.accessors.put(string2, string3);
        return string3;
    }

    public String getMethodAccessor(String string, String string2, String string3, MethodInfo methodInfo) {
        String string4 = string + ":" + string2;
        String string5 = (String)this.accessors.get(string4);
        if (string5 != null) {
            return string5;
        }
        ClassFile classFile = this.clazz.getClassFile();
        string5 = this.findAccessorName(classFile);
        try {
            ConstPool constPool = classFile.getConstPool();
            ClassPool classPool = this.clazz.getClassPool();
            MethodInfo methodInfo2 = new MethodInfo(constPool, string5, string3);
            methodInfo2.setAccessFlags(8);
            methodInfo2.addAttribute(new SyntheticAttribute(constPool));
            ExceptionsAttribute exceptionsAttribute = methodInfo.getExceptionsAttribute();
            if (exceptionsAttribute != null) {
                methodInfo2.addAttribute(exceptionsAttribute.copy(constPool, null));
            }
            CtClass[] ctClassArray = Descriptor.getParameterTypes(string3, classPool);
            int n = 0;
            Bytecode bytecode = new Bytecode(constPool);
            for (int i = 0; i < ctClassArray.length; ++i) {
                n += bytecode.addLoad(n, ctClassArray[i]);
            }
            bytecode.setMaxLocals(n);
            if (string2 == string3) {
                bytecode.addInvokestatic(this.clazz, string, string2);
            } else {
                bytecode.addInvokevirtual(this.clazz, string, string2);
            }
            bytecode.addReturn(Descriptor.getReturnType(string2, classPool));
            methodInfo2.setCodeAttribute(bytecode.toCodeAttribute());
            classFile.addMethod(methodInfo2);
        } catch (CannotCompileException cannotCompileException) {
            throw new CompileError(cannotCompileException);
        } catch (NotFoundException notFoundException) {
            throw new CompileError(notFoundException);
        }
        this.accessors.put(string4, string5);
        return string5;
    }

    public MethodInfo getFieldGetter(FieldInfo fieldInfo, boolean bl) {
        String string = fieldInfo.getName();
        String string2 = string + ":getter";
        Object object = this.accessors.get(string2);
        if (object != null) {
            return (MethodInfo)object;
        }
        ClassFile classFile = this.clazz.getClassFile();
        String string3 = this.findAccessorName(classFile);
        try {
            ConstPool constPool = classFile.getConstPool();
            ClassPool classPool = this.clazz.getClassPool();
            String string4 = fieldInfo.getDescriptor();
            String string5 = bl ? "()" + string4 : "(" + Descriptor.of(this.clazz) + ")" + string4;
            MethodInfo methodInfo = new MethodInfo(constPool, string3, string5);
            methodInfo.setAccessFlags(8);
            methodInfo.addAttribute(new SyntheticAttribute(constPool));
            Bytecode bytecode = new Bytecode(constPool);
            if (bl) {
                bytecode.addGetstatic(Bytecode.THIS, string, string4);
            } else {
                bytecode.addAload(0);
                bytecode.addGetfield(Bytecode.THIS, string, string4);
                bytecode.setMaxLocals(1);
            }
            bytecode.addReturn(Descriptor.toCtClass(string4, classPool));
            methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
            classFile.addMethod(methodInfo);
            this.accessors.put(string2, methodInfo);
            return methodInfo;
        } catch (CannotCompileException cannotCompileException) {
            throw new CompileError(cannotCompileException);
        } catch (NotFoundException notFoundException) {
            throw new CompileError(notFoundException);
        }
    }

    public MethodInfo getFieldSetter(FieldInfo fieldInfo, boolean bl) {
        String string = fieldInfo.getName();
        String string2 = string + ":setter";
        Object object = this.accessors.get(string2);
        if (object != null) {
            return (MethodInfo)object;
        }
        ClassFile classFile = this.clazz.getClassFile();
        String string3 = this.findAccessorName(classFile);
        try {
            int n;
            ConstPool constPool = classFile.getConstPool();
            ClassPool classPool = this.clazz.getClassPool();
            String string4 = fieldInfo.getDescriptor();
            String string5 = bl ? "(" + string4 + ")V" : "(" + Descriptor.of(this.clazz) + string4 + ")V";
            MethodInfo methodInfo = new MethodInfo(constPool, string3, string5);
            methodInfo.setAccessFlags(8);
            methodInfo.addAttribute(new SyntheticAttribute(constPool));
            Bytecode bytecode = new Bytecode(constPool);
            if (bl) {
                n = bytecode.addLoad(0, Descriptor.toCtClass(string4, classPool));
                bytecode.addPutstatic(Bytecode.THIS, string, string4);
            } else {
                bytecode.addAload(0);
                n = bytecode.addLoad(1, Descriptor.toCtClass(string4, classPool)) + 1;
                bytecode.addPutfield(Bytecode.THIS, string, string4);
            }
            bytecode.addReturn(null);
            bytecode.setMaxLocals(n);
            methodInfo.setCodeAttribute(bytecode.toCodeAttribute());
            classFile.addMethod(methodInfo);
            this.accessors.put(string2, methodInfo);
            return methodInfo;
        } catch (CannotCompileException cannotCompileException) {
            throw new CompileError(cannotCompileException);
        } catch (NotFoundException notFoundException) {
            throw new CompileError(notFoundException);
        }
    }

    private String findAccessorName(ClassFile classFile) {
        String string;
        while (classFile.getMethod(string = "access$" + this.uniqueNumber++) != null) {
        }
        return string;
    }
}

