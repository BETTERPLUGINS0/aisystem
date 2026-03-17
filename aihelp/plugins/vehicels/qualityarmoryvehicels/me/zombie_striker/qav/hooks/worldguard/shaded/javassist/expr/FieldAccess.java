/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
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

public class FieldAccess
extends Expr {
    int opcode;

    protected FieldAccess(int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo, int n2) {
        super(n, codeIterator, ctClass, methodInfo);
        this.opcode = n2;
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

    public boolean isStatic() {
        return FieldAccess.isStatic(this.opcode);
    }

    static boolean isStatic(int n) {
        return n == 178 || n == 179;
    }

    public boolean isReader() {
        return this.opcode == 180 || this.opcode == 178;
    }

    public boolean isWriter() {
        return this.opcode == 181 || this.opcode == 179;
    }

    private CtClass getCtClass() {
        return this.thisClass.getClassPool().get(this.getClassName());
    }

    public String getClassName() {
        int n = this.iterator.u16bitAt(this.currentPos + 1);
        return this.getConstPool().getFieldrefClassName(n);
    }

    public String getFieldName() {
        int n = this.iterator.u16bitAt(this.currentPos + 1);
        return this.getConstPool().getFieldrefName(n);
    }

    public CtField getField() {
        CtClass ctClass = this.getCtClass();
        int n = this.iterator.u16bitAt(this.currentPos + 1);
        ConstPool constPool = this.getConstPool();
        return ctClass.getField(constPool.getFieldrefName(n), constPool.getFieldrefType(n));
    }

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    public String getSignature() {
        int n = this.iterator.u16bitAt(this.currentPos + 1);
        return this.getConstPool().getFieldrefType(n);
    }

    @Override
    public void replace(String string) {
        this.thisClass.getClassFile();
        ConstPool constPool = this.getConstPool();
        int n = this.currentPos;
        int n2 = this.iterator.u16bitAt(n + 1);
        Javac javac = new Javac(this.thisClass);
        CodeAttribute codeAttribute = this.iterator.get();
        try {
            CtClass ctClass;
            CtClass[] ctClassArray;
            CtClass ctClass2 = Descriptor.toCtClass(constPool.getFieldrefType(n2), this.thisClass.getClassPool());
            boolean bl = this.isReader();
            if (bl) {
                ctClassArray = new CtClass[]{};
                ctClass = ctClass2;
            } else {
                ctClassArray = new CtClass[]{ctClass2};
                ctClass = CtClass.voidType;
            }
            int n3 = codeAttribute.getMaxLocals();
            javac.recordParams(constPool.getFieldrefClassName(n2), ctClassArray, true, n3, this.withinStatic());
            boolean bl2 = FieldAccess.checkResultValue(ctClass, string);
            if (bl) {
                bl2 = true;
            }
            int n4 = javac.recordReturnType(ctClass, bl2);
            if (bl) {
                javac.recordProceed(new ProceedForRead(ctClass, this.opcode, n2, n3));
            } else {
                javac.recordType(ctClass2);
                javac.recordProceed(new ProceedForWrite(ctClassArray[0], this.opcode, n2, n3));
            }
            Bytecode bytecode = javac.getBytecode();
            FieldAccess.storeStack(ctClassArray, this.isStatic(), n3, bytecode);
            javac.recordLocalVariables(codeAttribute, n);
            if (bl2) {
                if (ctClass == CtClass.voidType) {
                    bytecode.addOpcode(1);
                    bytecode.addAstore(n4);
                } else {
                    bytecode.addConstZero(ctClass);
                    bytecode.addStore(n4, ctClass);
                }
            }
            javac.compileStmnt(string);
            if (bl) {
                bytecode.addLoad(n4, ctClass);
            }
            this.replace0(n, bytecode, 3);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException("broken method");
        }
    }

    static class ProceedForRead
    implements ProceedHandler {
        CtClass fieldType;
        int opcode;
        int targetVar;
        int index;

        ProceedForRead(CtClass ctClass, int n, int n2, int n3) {
            this.fieldType = ctClass;
            this.targetVar = n3;
            this.opcode = n;
            this.index = n2;
        }

        @Override
        public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
            int n;
            if (aSTList != null && !jvstCodeGen.isParamListName(aSTList)) {
                throw new CompileError("$proceed() cannot take a parameter for field reading");
            }
            if (FieldAccess.isStatic(this.opcode)) {
                n = 0;
            } else {
                n = -1;
                bytecode.addAload(this.targetVar);
            }
            n = this.fieldType instanceof CtPrimitiveType ? (n += ((CtPrimitiveType)this.fieldType).getDataSize()) : ++n;
            bytecode.add(this.opcode);
            bytecode.addIndex(this.index);
            bytecode.growStack(n);
            jvstCodeGen.setType(this.fieldType);
        }

        @Override
        public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
            jvstTypeChecker.setType(this.fieldType);
        }
    }

    static class ProceedForWrite
    implements ProceedHandler {
        CtClass fieldType;
        int opcode;
        int targetVar;
        int index;

        ProceedForWrite(CtClass ctClass, int n, int n2, int n3) {
            this.fieldType = ctClass;
            this.targetVar = n3;
            this.opcode = n;
            this.index = n2;
        }

        @Override
        public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
            int n;
            if (jvstCodeGen.getMethodArgsLength(aSTList) != 1) {
                throw new CompileError("$proceed() cannot take more than one parameter for field writing");
            }
            if (FieldAccess.isStatic(this.opcode)) {
                n = 0;
            } else {
                n = -1;
                bytecode.addAload(this.targetVar);
            }
            jvstCodeGen.atMethodArgs(aSTList, new int[1], new int[1], new String[1]);
            jvstCodeGen.doNumCast(this.fieldType);
            n = this.fieldType instanceof CtPrimitiveType ? (n -= ((CtPrimitiveType)this.fieldType).getDataSize()) : --n;
            bytecode.add(this.opcode);
            bytecode.addIndex(this.index);
            bytecode.growStack(n);
            jvstCodeGen.setType(CtClass.voidType);
            jvstCodeGen.addNullIfVoid();
        }

        @Override
        public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
            jvstTypeChecker.atMethodArgs(aSTList, new int[1], new int[1], new String[1]);
            jvstTypeChecker.setType(CtClass.voidType);
            jvstTypeChecker.addNullIfVoid();
        }
    }
}

