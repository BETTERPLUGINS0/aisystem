/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Expr;

public class MethodCall
extends Expr {
    protected MethodCall(int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo) {
        super(n, codeIterator, ctClass, methodInfo);
    }

    private int getNameAndType(ConstPool constPool) {
        int n = this.currentPos;
        int n2 = this.iterator.byteAt(n);
        int n3 = this.iterator.u16bitAt(n + 1);
        if (n2 == 185) {
            return constPool.getInterfaceMethodrefNameAndType(n3);
        }
        return constPool.getMethodrefNameAndType(n3);
    }

    @Override
    public CtBehavior where() {
        return super.where();
    }

    @Override
    public int getLineNumber() {
        return super.getLineNumber();
    }

    @Override
    public String getFileName() {
        return super.getFileName();
    }

    protected CtClass getCtClass() {
        return this.thisClass.getClassPool().get(this.getClassName());
    }

    public String getClassName() {
        ConstPool constPool = this.getConstPool();
        int n = this.currentPos;
        int n2 = this.iterator.byteAt(n);
        int n3 = this.iterator.u16bitAt(n + 1);
        String string = n2 == 185 ? constPool.getInterfaceMethodrefClassName(n3) : constPool.getMethodrefClassName(n3);
        if (string.charAt(0) == '[') {
            string = Descriptor.toClassName(string);
        }
        return string;
    }

    public String getMethodName() {
        ConstPool constPool = this.getConstPool();
        int n = this.getNameAndType(constPool);
        return constPool.getUtf8Info(constPool.getNameAndTypeName(n));
    }

    public CtMethod getMethod() {
        return this.getCtClass().getMethod(this.getMethodName(), this.getSignature());
    }

    public String getSignature() {
        ConstPool constPool = this.getConstPool();
        int n = this.getNameAndType(constPool);
        return constPool.getUtf8Info(constPool.getNameAndTypeDescriptor(n));
    }

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    public boolean isSuper() {
        return this.iterator.byteAt(this.currentPos) == 183 && !this.where().getDeclaringClass().getName().equals(this.getClassName());
    }

    @Override
    public void replace(String string) {
        String string2;
        String string3;
        String string4;
        int n;
        this.thisClass.getClassFile();
        ConstPool constPool = this.getConstPool();
        int n2 = this.currentPos;
        int n3 = this.iterator.u16bitAt(n2 + 1);
        int n4 = this.iterator.byteAt(n2);
        if (n4 == 185) {
            n = 5;
            string4 = constPool.getInterfaceMethodrefClassName(n3);
            string3 = constPool.getInterfaceMethodrefName(n3);
            string2 = constPool.getInterfaceMethodrefType(n3);
        } else if (n4 == 184 || n4 == 183 || n4 == 182) {
            n = 3;
            string4 = constPool.getMethodrefClassName(n3);
            string3 = constPool.getMethodrefName(n3);
            string2 = constPool.getMethodrefType(n3);
        } else {
            throw new CannotCompileException("not method invocation");
        }
        Javac javac = new Javac(this.thisClass);
        ClassPool classPool = this.thisClass.getClassPool();
        CodeAttribute codeAttribute = this.iterator.get();
        try {
            CtClass[] ctClassArray = Descriptor.getParameterTypes(string2, classPool);
            CtClass ctClass = Descriptor.getReturnType(string2, classPool);
            int n5 = codeAttribute.getMaxLocals();
            javac.recordParams(string4, ctClassArray, true, n5, this.withinStatic());
            int n6 = javac.recordReturnType(ctClass, true);
            if (n4 == 184) {
                javac.recordStaticProceed(string4, string3);
            } else if (n4 == 183) {
                javac.recordSpecialProceed("$0", string4, string3, string2, n3);
            } else {
                javac.recordProceed("$0", string3);
            }
            MethodCall.checkResultValue(ctClass, string);
            Bytecode bytecode = javac.getBytecode();
            MethodCall.storeStack(ctClassArray, n4 == 184, n5, bytecode);
            javac.recordLocalVariables(codeAttribute, n2);
            if (ctClass != CtClass.voidType) {
                bytecode.addConstZero(ctClass);
                bytecode.addStore(n6, ctClass);
            }
            javac.compileStmnt(string);
            if (ctClass != CtClass.voidType) {
                bytecode.addLoad(n6, ctClass);
            }
            this.replace0(n2, bytecode, n);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException("broken method");
        }
    }
}

