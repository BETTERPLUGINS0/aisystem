/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassMap;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;

public final class CtConstructor
extends CtBehavior {
    protected CtConstructor(MethodInfo methodInfo, CtClass ctClass) {
        super(ctClass, methodInfo);
    }

    public CtConstructor(CtClass[] ctClassArray, CtClass ctClass) {
        this((MethodInfo)null, ctClass);
        ConstPool constPool = ctClass.getClassFile2().getConstPool();
        String string = Descriptor.ofConstructor(ctClassArray);
        this.methodInfo = new MethodInfo(constPool, "<init>", string);
        this.setModifiers(1);
    }

    public CtConstructor(CtConstructor ctConstructor, CtClass ctClass, ClassMap classMap) {
        this((MethodInfo)null, ctClass);
        this.copy(ctConstructor, true, classMap);
    }

    public boolean isConstructor() {
        return this.methodInfo.isConstructor();
    }

    public boolean isClassInitializer() {
        return this.methodInfo.isStaticInitializer();
    }

    @Override
    public String getLongName() {
        return this.getDeclaringClass().getName() + (this.isConstructor() ? Descriptor.toString(this.getSignature()) : ".<clinit>()");
    }

    @Override
    public String getName() {
        if (this.methodInfo.isStaticInitializer()) {
            return "<clinit>";
        }
        return this.declaringClass.getSimpleName();
    }

    @Override
    public boolean isEmpty() {
        CodeAttribute codeAttribute = this.getMethodInfo2().getCodeAttribute();
        if (codeAttribute == null) {
            return false;
        }
        ConstPool constPool = codeAttribute.getConstPool();
        CodeIterator codeIterator = codeAttribute.iterator();
        try {
            int n;
            int n2;
            int n3 = codeIterator.byteAt(codeIterator.next());
            return n3 == 177 || n3 == 42 && codeIterator.byteAt(n2 = codeIterator.next()) == 183 && (n = constPool.isConstructor(this.getSuperclassName(), codeIterator.u16bitAt(n2 + 1))) != 0 && "()V".equals(constPool.getUtf8Info(n)) && codeIterator.byteAt(codeIterator.next()) == 177 && !codeIterator.hasNext();
        } catch (BadBytecode badBytecode) {
            return false;
        }
    }

    private String getSuperclassName() {
        ClassFile classFile = this.declaringClass.getClassFile2();
        return classFile.getSuperclass();
    }

    public boolean callsSuper() {
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        if (codeAttribute != null) {
            CodeIterator codeIterator = codeAttribute.iterator();
            try {
                int n = codeIterator.skipSuperConstructor();
                return n >= 0;
            } catch (BadBytecode badBytecode) {
                throw new CannotCompileException(badBytecode);
            }
        }
        return false;
    }

    @Override
    public void setBody(String string) {
        if (string == null) {
            string = this.isClassInitializer() ? ";" : "super();";
        }
        super.setBody(string);
    }

    public void setBody(CtConstructor ctConstructor, ClassMap classMap) {
        CtConstructor.setBody0(ctConstructor.declaringClass, ctConstructor.methodInfo, this.declaringClass, this.methodInfo, classMap);
    }

    public void insertBeforeBody(String string) {
        CtClass ctClass = this.declaringClass;
        ctClass.checkModify();
        if (this.isClassInitializer()) {
            throw new CannotCompileException("class initializer");
        }
        CodeAttribute codeAttribute = this.methodInfo.getCodeAttribute();
        CodeIterator codeIterator = codeAttribute.iterator();
        Bytecode bytecode = new Bytecode(this.methodInfo.getConstPool(), codeAttribute.getMaxStack(), codeAttribute.getMaxLocals());
        bytecode.setStackDepth(codeAttribute.getMaxStack());
        Javac javac = new Javac(bytecode, ctClass);
        try {
            javac.recordParams(this.getParameterTypes(), false);
            javac.compileStmnt(string);
            codeAttribute.setMaxStack(bytecode.getMaxStack());
            codeAttribute.setMaxLocals(bytecode.getMaxLocals());
            codeIterator.skipConstructor();
            int n = codeIterator.insertEx(bytecode.get());
            codeIterator.insert(bytecode.getExceptionTable(), n);
            this.methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    @Override
    int getStartPosOfBody(CodeAttribute codeAttribute) {
        CodeIterator codeIterator = codeAttribute.iterator();
        try {
            codeIterator.skipConstructor();
            return codeIterator.next();
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    public CtMethod toMethod(String string, CtClass ctClass) {
        return this.toMethod(string, ctClass, null);
    }

    public CtMethod toMethod(String string, CtClass ctClass, ClassMap classMap) {
        MethodInfo methodInfo;
        CodeAttribute codeAttribute;
        CtMethod ctMethod = new CtMethod(null, ctClass);
        ctMethod.copy(this, false, classMap);
        if (this.isConstructor() && (codeAttribute = (methodInfo = ctMethod.getMethodInfo2()).getCodeAttribute()) != null) {
            CtConstructor.removeConsCall(codeAttribute);
            try {
                this.methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
            } catch (BadBytecode badBytecode) {
                throw new CannotCompileException(badBytecode);
            }
        }
        ctMethod.setName(string);
        return ctMethod;
    }

    private static void removeConsCall(CodeAttribute codeAttribute) {
        CodeIterator codeIterator = codeAttribute.iterator();
        try {
            int n = codeIterator.skipConstructor();
            if (n >= 0) {
                int n2 = codeIterator.u16bitAt(n + 1);
                String string = codeAttribute.getConstPool().getMethodrefType(n2);
                int n3 = Descriptor.numOfParameters(string) + 1;
                if (n3 > 3) {
                    n = codeIterator.insertGapAt((int)n, (int)(n3 - 3), (boolean)false).position;
                }
                codeIterator.writeByte(87, n++);
                codeIterator.writeByte(0, n);
                codeIterator.writeByte(0, n + 1);
                Descriptor.Iterator iterator = new Descriptor.Iterator(string);
                while (true) {
                    iterator.next();
                    if (iterator.isParameter()) {
                        codeIterator.writeByte(iterator.is2byte() ? 88 : 87, n++);
                        continue;
                    }
                    break;
                }
            }
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }
}

