/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Javac;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstCodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstTypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ProceedHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Expr;

public class Cast
extends Expr {
    protected Cast(int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo) {
        super(n, codeIterator, ctClass, methodInfo);
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

    public CtClass getType() {
        ConstPool constPool = this.getConstPool();
        int n = this.currentPos;
        int n2 = this.iterator.u16bitAt(n + 1);
        String string = constPool.getClassInfo(n2);
        return this.thisClass.getClassPool().getCtClass(string);
    }

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    @Override
    public void replace(String string) {
        this.thisClass.getClassFile();
        ConstPool constPool = this.getConstPool();
        int n = this.currentPos;
        int n2 = this.iterator.u16bitAt(n + 1);
        Javac javac = new Javac(this.thisClass);
        ClassPool classPool = this.thisClass.getClassPool();
        CodeAttribute codeAttribute = this.iterator.get();
        try {
            CtClass[] ctClassArray = new CtClass[]{classPool.get("java.lang.Object")};
            CtClass ctClass = this.getType();
            int n3 = codeAttribute.getMaxLocals();
            javac.recordParams("java.lang.Object", ctClassArray, true, n3, this.withinStatic());
            int n4 = javac.recordReturnType(ctClass, true);
            javac.recordProceed(new ProceedForCast(n2, ctClass));
            Cast.checkResultValue(ctClass, string);
            Bytecode bytecode = javac.getBytecode();
            Cast.storeStack(ctClassArray, true, n3, bytecode);
            javac.recordLocalVariables(codeAttribute, n);
            bytecode.addConstZero(ctClass);
            bytecode.addStore(n4, ctClass);
            javac.compileStmnt(string);
            bytecode.addLoad(n4, ctClass);
            this.replace0(n, bytecode, 3);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException("broken method");
        }
    }

    static class ProceedForCast
    implements ProceedHandler {
        int index;
        CtClass retType;

        ProceedForCast(int n, CtClass ctClass) {
            this.index = n;
            this.retType = ctClass;
        }

        @Override
        public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
            if (jvstCodeGen.getMethodArgsLength(aSTList) != 1) {
                throw new CompileError("$proceed() cannot take more than one parameter for cast");
            }
            jvstCodeGen.atMethodArgs(aSTList, new int[1], new int[1], new String[1]);
            bytecode.addOpcode(192);
            bytecode.addIndex(this.index);
            jvstCodeGen.setType(this.retType);
        }

        @Override
        public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
            jvstTypeChecker.atMethodArgs(aSTList, new int[1], new int[1], new String[1]);
            jvstTypeChecker.setType(this.retType);
        }
    }
}

