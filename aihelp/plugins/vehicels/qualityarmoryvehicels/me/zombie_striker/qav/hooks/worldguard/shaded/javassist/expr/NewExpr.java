/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
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
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstCodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstTypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ProceedHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Expr;

public class NewExpr
extends Expr {
    String newTypeName;
    int newPos;

    protected NewExpr(int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo, String string, int n2) {
        super(n, codeIterator, ctClass, methodInfo);
        this.newTypeName = string;
        this.newPos = n2;
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

    private CtClass getCtClass() {
        return this.thisClass.getClassPool().get(this.newTypeName);
    }

    public String getClassName() {
        return this.newTypeName;
    }

    public String getSignature() {
        ConstPool constPool = this.getConstPool();
        int n = this.iterator.u16bitAt(this.currentPos + 1);
        return constPool.getMethodrefType(n);
    }

    public CtConstructor getConstructor() {
        ConstPool constPool = this.getConstPool();
        int n = this.iterator.u16bitAt(this.currentPos + 1);
        String string = constPool.getMethodrefType(n);
        return this.getCtClass().getConstructor(string);
    }

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    private int canReplace() {
        int n = this.iterator.byteAt(this.newPos + 3);
        if (n == 89) {
            return this.iterator.byteAt(this.newPos + 4) == 94 && this.iterator.byteAt(this.newPos + 5) == 88 ? 6 : 4;
        }
        if (n == 90 && this.iterator.byteAt(this.newPos + 4) == 95) {
            return 5;
        }
        return 3;
    }

    @Override
    public void replace(String string) {
        this.thisClass.getClassFile();
        int n = 3;
        int n2 = this.newPos;
        int n3 = this.iterator.u16bitAt(n2 + 1);
        int n4 = this.canReplace();
        int n5 = n2 + n4;
        for (int i = n2; i < n5; ++i) {
            this.iterator.writeByte(0, i);
        }
        ConstPool constPool = this.getConstPool();
        n2 = this.currentPos;
        int n6 = this.iterator.u16bitAt(n2 + 1);
        String string2 = constPool.getMethodrefType(n6);
        Javac javac = new Javac(this.thisClass);
        ClassPool classPool = this.thisClass.getClassPool();
        CodeAttribute codeAttribute = this.iterator.get();
        try {
            CtClass[] ctClassArray = Descriptor.getParameterTypes(string2, classPool);
            CtClass ctClass = classPool.get(this.newTypeName);
            int n7 = codeAttribute.getMaxLocals();
            javac.recordParams(this.newTypeName, ctClassArray, true, n7, this.withinStatic());
            int n8 = javac.recordReturnType(ctClass, true);
            javac.recordProceed(new ProceedForNew(ctClass, n3, n6));
            NewExpr.checkResultValue(ctClass, string);
            Bytecode bytecode = javac.getBytecode();
            NewExpr.storeStack(ctClassArray, true, n7, bytecode);
            javac.recordLocalVariables(codeAttribute, n2);
            bytecode.addConstZero(ctClass);
            bytecode.addStore(n8, ctClass);
            javac.compileStmnt(string);
            if (n4 > 3) {
                bytecode.addAload(n8);
            }
            this.replace0(n2, bytecode, 3);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException("broken method");
        }
    }

    static class ProceedForNew
    implements ProceedHandler {
        CtClass newType;
        int newIndex;
        int methodIndex;

        ProceedForNew(CtClass ctClass, int n, int n2) {
            this.newType = ctClass;
            this.newIndex = n;
            this.methodIndex = n2;
        }

        @Override
        public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
            bytecode.addOpcode(187);
            bytecode.addIndex(this.newIndex);
            bytecode.addOpcode(89);
            jvstCodeGen.atMethodCallCore(this.newType, "<init>", aSTList, false, true, -1, null);
            jvstCodeGen.setType(this.newType);
        }

        @Override
        public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
            jvstTypeChecker.atMethodCallCore(this.newType, "<init>", aSTList);
            jvstTypeChecker.setType(this.newType);
        }
    }
}

