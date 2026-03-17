/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtNewWrappedMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;

public final class CtMethod
extends CtBehavior {
    protected String cachedStringRep = null;

    CtMethod(MethodInfo methodInfo, CtClass ctClass) {
        super(ctClass, methodInfo);
    }

    public CtMethod(CtClass ctClass, String string, CtClass[] ctClassArray, CtClass ctClass2) {
        this(null, ctClass2);
        ConstPool constPool = ctClass2.getClassFile2().getConstPool();
        String string2 = Descriptor.ofMethod(ctClass, ctClassArray);
        this.methodInfo = new MethodInfo(constPool, string, string2);
        this.setModifiers(1025);
    }

    public CtMethod(CtMethod ctMethod, CtClass ctClass, ClassMap classMap) {
        this(null, ctClass);
        this.copy(ctMethod, false, classMap);
    }

    public static CtMethod make(String string, CtClass ctClass) {
        return CtNewMethod.make(string, ctClass);
    }

    public static CtMethod make(MethodInfo methodInfo, CtClass ctClass) {
        if (ctClass.getClassFile2().getConstPool() != methodInfo.getConstPool()) {
            throw new CannotCompileException("bad declaring class");
        }
        return new CtMethod(methodInfo, ctClass);
    }

    public int hashCode() {
        return this.getStringRep().hashCode();
    }

    @Override
    void nameReplaced() {
        this.cachedStringRep = null;
    }

    final String getStringRep() {
        if (this.cachedStringRep == null) {
            this.cachedStringRep = this.methodInfo.getName() + Descriptor.getParamDescriptor(this.methodInfo.getDescriptor());
        }
        return this.cachedStringRep;
    }

    public boolean equals(Object object) {
        return object != null && object instanceof CtMethod && ((CtMethod)object).getStringRep().equals(this.getStringRep());
    }

    @Override
    public String getLongName() {
        return this.getDeclaringClass().getName() + "." + this.getName() + Descriptor.toString(this.getSignature());
    }

    @Override
    public String getName() {
        return this.methodInfo.getName();
    }

    public void setName(String string) {
        this.declaringClass.checkModify();
        this.methodInfo.setName(string);
    }

    public CtClass getReturnType() {
        return this.getReturnType0();
    }

    @Override
    public boolean isEmpty() {
        CodeAttribute codeAttribute = this.getMethodInfo2().getCodeAttribute();
        if (codeAttribute == null) {
            return (this.getModifiers() & 0x400) != 0;
        }
        CodeIterator codeIterator = codeAttribute.iterator();
        try {
            return codeIterator.hasNext() && codeIterator.byteAt(codeIterator.next()) == 177 && !codeIterator.hasNext();
        } catch (BadBytecode badBytecode) {
            return false;
        }
    }

    public void setBody(CtMethod ctMethod, ClassMap classMap) {
        CtMethod.setBody0(ctMethod.declaringClass, ctMethod.methodInfo, this.declaringClass, this.methodInfo, classMap);
    }

    public void setWrappedBody(CtMethod ctMethod, ConstParameter constParameter) {
        CtClass ctClass;
        CtClass[] ctClassArray;
        this.declaringClass.checkModify();
        CtClass ctClass2 = this.getDeclaringClass();
        try {
            ctClassArray = this.getParameterTypes();
            ctClass = this.getReturnType();
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        }
        Bytecode bytecode = CtNewWrappedMethod.makeBody(ctClass2, ctClass2.getClassFile2(), ctMethod, ctClassArray, ctClass, constParameter);
        CodeAttribute codeAttribute = bytecode.toCodeAttribute();
        this.methodInfo.setCodeAttribute(codeAttribute);
        this.methodInfo.setAccessFlags(this.methodInfo.getAccessFlags() & 0xFFFFFBFF);
    }

    public static class ConstParameter {
        public static ConstParameter integer(int n) {
            return new IntConstParameter(n);
        }

        public static ConstParameter integer(long l) {
            return new LongConstParameter(l);
        }

        public static ConstParameter string(String string) {
            return new StringConstParameter(string);
        }

        ConstParameter() {
        }

        int compile(Bytecode bytecode) {
            return 0;
        }

        String descriptor() {
            return ConstParameter.defaultDescriptor();
        }

        static String defaultDescriptor() {
            return "([Ljava/lang/Object;)Ljava/lang/Object;";
        }

        String constDescriptor() {
            return ConstParameter.defaultConstDescriptor();
        }

        static String defaultConstDescriptor() {
            return "([Ljava/lang/Object;)V";
        }
    }

    static class StringConstParameter
    extends ConstParameter {
        String param;

        StringConstParameter(String string) {
            this.param = string;
        }

        @Override
        int compile(Bytecode bytecode) {
            bytecode.addLdc(this.param);
            return 1;
        }

        @Override
        String descriptor() {
            return "([Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;";
        }

        @Override
        String constDescriptor() {
            return "([Ljava/lang/Object;Ljava/lang/String;)V";
        }
    }

    static class LongConstParameter
    extends ConstParameter {
        long param;

        LongConstParameter(long l) {
            this.param = l;
        }

        @Override
        int compile(Bytecode bytecode) {
            bytecode.addLconst(this.param);
            return 2;
        }

        @Override
        String descriptor() {
            return "([Ljava/lang/Object;J)Ljava/lang/Object;";
        }

        @Override
        String constDescriptor() {
            return "([Ljava/lang/Object;J)V";
        }
    }

    static class IntConstParameter
    extends ConstParameter {
        int param;

        IntConstParameter(int n) {
            this.param = n;
        }

        @Override
        int compile(Bytecode bytecode) {
            bytecode.addIconst(this.param);
            return 1;
        }

        @Override
        String descriptor() {
            return "([Ljava/lang/Object;I)Ljava/lang/Object;";
        }

        @Override
        String constDescriptor() {
            return "([Ljava/lang/Object;I)V";
        }
    }
}

