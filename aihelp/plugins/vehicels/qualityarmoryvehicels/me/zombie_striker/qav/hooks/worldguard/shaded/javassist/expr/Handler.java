/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Expr;

public class Handler
extends Expr {
    private static String EXCEPTION_NAME = "$1";
    private ExceptionTable etable;
    private int index;

    protected Handler(ExceptionTable exceptionTable, int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo) {
        super(exceptionTable.handlerPc(n), codeIterator, ctClass, methodInfo);
        this.etable = exceptionTable;
        this.index = n;
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

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    public CtClass getType() {
        int n = this.etable.catchType(this.index);
        if (n == 0) {
            return null;
        }
        ConstPool constPool = this.getConstPool();
        String string = constPool.getClassInfo(n);
        return this.thisClass.getClassPool().getCtClass(string);
    }

    public boolean isFinally() {
        return this.etable.catchType(this.index) == 0;
    }

    @Override
    public void replace(String string) {
        throw new RuntimeException("not implemented yet");
    }

    public void insertBefore(String string) {
        this.edited = true;
        ConstPool constPool = this.getConstPool();
        CodeAttribute codeAttribute = this.iterator.get();
        Javac javac = new Javac(this.thisClass);
        Bytecode bytecode = javac.getBytecode();
        bytecode.setStackDepth(1);
        bytecode.setMaxLocals(codeAttribute.getMaxLocals());
        try {
            CtClass ctClass = this.getType();
            int n = javac.recordVariable(ctClass, EXCEPTION_NAME);
            javac.recordReturnType(ctClass, false);
            bytecode.addAstore(n);
            javac.compileStmnt(string);
            bytecode.addAload(n);
            int n2 = this.etable.handlerPc(this.index);
            bytecode.addOpcode(167);
            bytecode.addIndex(n2 - this.iterator.getCodeLength() - bytecode.currentPc() + 1);
            this.maxStack = bytecode.getMaxStack();
            this.maxLocals = bytecode.getMaxLocals();
            int n3 = this.iterator.append(bytecode.get());
            this.iterator.append(bytecode.getExceptionTable(), n3);
            this.etable.setHandlerPc(this.index, n3);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        }
    }
}

