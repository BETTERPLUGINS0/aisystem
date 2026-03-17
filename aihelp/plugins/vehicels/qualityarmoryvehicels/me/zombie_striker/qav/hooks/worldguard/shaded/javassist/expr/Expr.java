/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import java.util.LinkedList;
import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AttributeInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionsAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.ExprEditor;

public abstract class Expr
implements Opcode {
    int currentPos;
    CodeIterator iterator;
    CtClass thisClass;
    MethodInfo thisMethod;
    boolean edited;
    int maxLocals;
    int maxStack;
    static final String javaLangObject = "java.lang.Object";

    protected Expr(int n, CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo) {
        this.currentPos = n;
        this.iterator = codeIterator;
        this.thisClass = ctClass;
        this.thisMethod = methodInfo;
    }

    public CtClass getEnclosingClass() {
        return this.thisClass;
    }

    protected final ConstPool getConstPool() {
        return this.thisMethod.getConstPool();
    }

    protected final boolean edited() {
        return this.edited;
    }

    protected final int locals() {
        return this.maxLocals;
    }

    protected final int stack() {
        return this.maxStack;
    }

    protected final boolean withinStatic() {
        return (this.thisMethod.getAccessFlags() & 8) != 0;
    }

    public CtBehavior where() {
        MethodInfo methodInfo = this.thisMethod;
        CtBehavior[] ctBehaviorArray = this.thisClass.getDeclaredBehaviors();
        for (int i = ctBehaviorArray.length - 1; i >= 0; --i) {
            if (ctBehaviorArray[i].getMethodInfo2() != methodInfo) continue;
            return ctBehaviorArray[i];
        }
        CtConstructor ctConstructor = this.thisClass.getClassInitializer();
        if (ctConstructor != null && ctConstructor.getMethodInfo2() == methodInfo) {
            return ctConstructor;
        }
        for (int i = ctBehaviorArray.length - 1; i >= 0; --i) {
            if (!this.thisMethod.getName().equals(ctBehaviorArray[i].getMethodInfo2().getName()) || !this.thisMethod.getDescriptor().equals(ctBehaviorArray[i].getMethodInfo2().getDescriptor())) continue;
            return ctBehaviorArray[i];
        }
        throw new RuntimeException("fatal: not found");
    }

    public CtClass[] mayThrow() {
        int n;
        int n2;
        String[] stringArray;
        AttributeInfo attributeInfo;
        ClassPool classPool = this.thisClass.getClassPool();
        ConstPool constPool = this.thisMethod.getConstPool();
        LinkedList<CtClass> linkedList = new LinkedList<CtClass>();
        try {
            attributeInfo = this.thisMethod.getCodeAttribute();
            stringArray = ((CodeAttribute)attributeInfo).getExceptionTable();
            n2 = this.currentPos;
            n = stringArray.size();
            for (int i = 0; i < n; ++i) {
                int n3;
                if (stringArray.startPc(i) > n2 || n2 >= stringArray.endPc(i) || (n3 = stringArray.catchType(i)) <= 0) continue;
                try {
                    Expr.addClass(linkedList, classPool.get(constPool.getClassInfo(n3)));
                    continue;
                } catch (NotFoundException notFoundException) {
                    // empty catch block
                }
            }
        } catch (NullPointerException nullPointerException) {
            // empty catch block
        }
        if ((attributeInfo = this.thisMethod.getExceptionsAttribute()) != null && (stringArray = ((ExceptionsAttribute)attributeInfo).getExceptions()) != null) {
            n2 = stringArray.length;
            for (n = 0; n < n2; ++n) {
                try {
                    Expr.addClass(linkedList, classPool.get(stringArray[n]));
                    continue;
                } catch (NotFoundException notFoundException) {
                    // empty catch block
                }
            }
        }
        return linkedList.toArray(new CtClass[linkedList.size()]);
    }

    private static void addClass(List<CtClass> list, CtClass ctClass) {
        if (list.contains(ctClass)) {
            return;
        }
        list.add(ctClass);
    }

    public int indexOfBytecode() {
        return this.currentPos;
    }

    public int getLineNumber() {
        return this.thisMethod.getLineNumber(this.currentPos);
    }

    public String getFileName() {
        ClassFile classFile = this.thisClass.getClassFile2();
        if (classFile == null) {
            return null;
        }
        return classFile.getSourceFile();
    }

    static final boolean checkResultValue(CtClass ctClass, String string) {
        boolean bl;
        boolean bl2 = bl = string.indexOf("$_") >= 0;
        if (!bl && ctClass != CtClass.voidType) {
            throw new CannotCompileException("the resulting value is not stored in $_");
        }
        return bl;
    }

    static final void storeStack(CtClass[] ctClassArray, boolean bl, int n, Bytecode bytecode) {
        Expr.storeStack0(0, ctClassArray.length, ctClassArray, n + 1, bytecode);
        if (bl) {
            bytecode.addOpcode(1);
        }
        bytecode.addAstore(n);
    }

    private static void storeStack0(int n, int n2, CtClass[] ctClassArray, int n3, Bytecode bytecode) {
        if (n >= n2) {
            return;
        }
        CtClass ctClass = ctClassArray[n];
        int n4 = ctClass instanceof CtPrimitiveType ? ((CtPrimitiveType)ctClass).getDataSize() : 1;
        Expr.storeStack0(n + 1, n2, ctClassArray, n3 + n4, bytecode);
        bytecode.addStore(n3, ctClass);
    }

    public abstract void replace(String var1);

    public void replace(String string, ExprEditor exprEditor) {
        this.replace(string);
        if (exprEditor != null) {
            this.runEditor(exprEditor, this.iterator);
        }
    }

    protected void replace0(int n, Bytecode bytecode, int n2) {
        byte[] byArray = bytecode.get();
        this.edited = true;
        int n3 = byArray.length - n2;
        for (int i = 0; i < n2; ++i) {
            this.iterator.writeByte(0, n + i);
        }
        if (n3 > 0) {
            n = this.iterator.insertGapAt((int)n, (int)n3, (boolean)false).position;
        }
        this.iterator.write(byArray, n);
        this.iterator.insert(bytecode.getExceptionTable(), n);
        this.maxLocals = bytecode.getMaxLocals();
        this.maxStack = bytecode.getMaxStack();
    }

    protected void runEditor(ExprEditor exprEditor, CodeIterator codeIterator) {
        CodeAttribute codeAttribute = codeIterator.get();
        int n = codeAttribute.getMaxLocals();
        int n2 = codeAttribute.getMaxStack();
        int n3 = this.locals();
        codeAttribute.setMaxStack(this.stack());
        codeAttribute.setMaxLocals(n3);
        ExprEditor.LoopContext loopContext = new ExprEditor.LoopContext(n3);
        int n4 = codeIterator.getCodeLength();
        int n5 = codeIterator.lookAhead();
        codeIterator.move(this.currentPos);
        if (exprEditor.doit(this.thisClass, this.thisMethod, loopContext, codeIterator, n5)) {
            this.edited = true;
        }
        codeIterator.move(n5 + codeIterator.getCodeLength() - n4);
        codeAttribute.setMaxLocals(n);
        codeAttribute.setMaxStack(n2);
        this.maxLocals = loopContext.maxLocals;
        this.maxStack += loopContext.maxStack;
    }
}

