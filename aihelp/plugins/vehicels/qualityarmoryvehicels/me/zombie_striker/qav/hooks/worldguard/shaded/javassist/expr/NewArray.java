/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
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

public class NewArray
extends Expr {
    int opcode;

    protected NewArray(int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo, int n2) {
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

    @Override
    public CtClass[] mayThrow() {
        return super.mayThrow();
    }

    public CtClass getComponentType() {
        if (this.opcode == 188) {
            int n = this.iterator.byteAt(this.currentPos + 1);
            return this.getPrimitiveType(n);
        }
        if (this.opcode == 189 || this.opcode == 197) {
            int n = this.iterator.u16bitAt(this.currentPos + 1);
            String string = this.getConstPool().getClassInfo(n);
            int n2 = Descriptor.arrayDimension(string);
            string = Descriptor.toArrayComponent(string, n2);
            return Descriptor.toCtClass(string, this.thisClass.getClassPool());
        }
        throw new RuntimeException("bad opcode: " + this.opcode);
    }

    CtClass getPrimitiveType(int n) {
        switch (n) {
            case 4: {
                return CtClass.booleanType;
            }
            case 5: {
                return CtClass.charType;
            }
            case 6: {
                return CtClass.floatType;
            }
            case 7: {
                return CtClass.doubleType;
            }
            case 8: {
                return CtClass.byteType;
            }
            case 9: {
                return CtClass.shortType;
            }
            case 10: {
                return CtClass.intType;
            }
            case 11: {
                return CtClass.longType;
            }
        }
        throw new RuntimeException("bad atype: " + n);
    }

    public int getDimension() {
        if (this.opcode == 188) {
            return 1;
        }
        if (this.opcode == 189 || this.opcode == 197) {
            int n = this.iterator.u16bitAt(this.currentPos + 1);
            String string = this.getConstPool().getClassInfo(n);
            return Descriptor.arrayDimension(string) + (this.opcode == 189 ? 1 : 0);
        }
        throw new RuntimeException("bad opcode: " + this.opcode);
    }

    public int getCreatedDimensions() {
        if (this.opcode == 197) {
            return this.iterator.byteAt(this.currentPos + 3);
        }
        return 1;
    }

    @Override
    public void replace(String string) {
        try {
            this.replace2(string);
        } catch (CompileError compileError) {
            throw new CannotCompileException(compileError);
        } catch (NotFoundException notFoundException) {
            throw new CannotCompileException(notFoundException);
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException("broken method");
        }
    }

    private void replace2(String string) {
        int n;
        int n2;
        String string2;
        Object object;
        this.thisClass.getClassFile();
        ConstPool constPool = this.getConstPool();
        int n3 = this.currentPos;
        int n4 = 0;
        int n5 = 1;
        if (this.opcode == 188) {
            n4 = this.iterator.byteAt(this.currentPos + 1);
            object = (CtPrimitiveType)this.getPrimitiveType(n4);
            string2 = "[" + ((CtPrimitiveType)object).getDescriptor();
            n2 = 2;
        } else if (this.opcode == 189) {
            n4 = this.iterator.u16bitAt(n3 + 1);
            string2 = constPool.getClassInfo(n4);
            string2 = string2.startsWith("[") ? "[" + string2 : "[L" + string2 + ";";
            n2 = 3;
        } else if (this.opcode == 197) {
            n4 = this.iterator.u16bitAt(this.currentPos + 1);
            string2 = constPool.getClassInfo(n4);
            n5 = this.iterator.byteAt(this.currentPos + 3);
            n2 = 4;
        } else {
            throw new RuntimeException("bad opcode: " + this.opcode);
        }
        CtClass ctClass = Descriptor.toCtClass(string2, this.thisClass.getClassPool());
        object = new Javac(this.thisClass);
        CodeAttribute codeAttribute = this.iterator.get();
        CtClass[] ctClassArray = new CtClass[n5];
        for (n = 0; n < n5; ++n) {
            ctClassArray[n] = CtClass.intType;
        }
        n = codeAttribute.getMaxLocals();
        ((Javac)object).recordParams("java.lang.Object", ctClassArray, true, n, this.withinStatic());
        NewArray.checkResultValue(ctClass, string);
        int n6 = ((Javac)object).recordReturnType(ctClass, true);
        ((Javac)object).recordProceed(new ProceedForArray(ctClass, this.opcode, n4, n5));
        Bytecode bytecode = ((Javac)object).getBytecode();
        NewArray.storeStack(ctClassArray, true, n, bytecode);
        ((Javac)object).recordLocalVariables(codeAttribute, n3);
        bytecode.addOpcode(1);
        bytecode.addAstore(n6);
        ((Javac)object).compileStmnt(string);
        bytecode.addAload(n6);
        this.replace0(n3, bytecode, n2);
    }

    static class ProceedForArray
    implements ProceedHandler {
        CtClass arrayType;
        int opcode;
        int index;
        int dimension;

        ProceedForArray(CtClass ctClass, int n, int n2, int n3) {
            this.arrayType = ctClass;
            this.opcode = n;
            this.index = n2;
            this.dimension = n3;
        }

        @Override
        public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
            int n = jvstCodeGen.getMethodArgsLength(aSTList);
            if (n != this.dimension) {
                throw new CompileError("$proceed() with a wrong number of parameters");
            }
            jvstCodeGen.atMethodArgs(aSTList, new int[n], new int[n], new String[n]);
            bytecode.addOpcode(this.opcode);
            if (this.opcode == 189) {
                bytecode.addIndex(this.index);
            } else if (this.opcode == 188) {
                bytecode.add(this.index);
            } else {
                bytecode.addIndex(this.index);
                bytecode.add(this.dimension);
                bytecode.growStack(1 - this.dimension);
            }
            jvstCodeGen.setType(this.arrayType);
        }

        @Override
        public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
            jvstTypeChecker.setType(this.arrayType);
        }
    }
}

