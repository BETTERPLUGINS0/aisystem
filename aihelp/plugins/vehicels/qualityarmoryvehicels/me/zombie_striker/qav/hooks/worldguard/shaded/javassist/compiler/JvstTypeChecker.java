/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstCodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CallExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CastExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Member;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;

public class JvstTypeChecker
extends TypeChecker {
    private JvstCodeGen codeGen;

    public JvstTypeChecker(CtClass ctClass, ClassPool classPool, JvstCodeGen jvstCodeGen) {
        super(ctClass, classPool);
        this.codeGen = jvstCodeGen;
    }

    public void addNullIfVoid() {
        if (this.exprType == 344) {
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/Object";
        }
    }

    @Override
    public void atMember(Member member) {
        String string = member.get();
        if (string.equals(this.codeGen.paramArrayName)) {
            this.exprType = 307;
            this.arrayDim = 1;
            this.className = "java/lang/Object";
        } else if (string.equals("$sig")) {
            this.exprType = 307;
            this.arrayDim = 1;
            this.className = "java/lang/Class";
        } else if (string.equals("$type") || string.equals("$class")) {
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/Class";
        } else {
            super.atMember(member);
        }
    }

    @Override
    protected void atFieldAssign(Expr expr, int n, ASTree aSTree, ASTree aSTree2) {
        if (aSTree instanceof Member && ((Member)aSTree).get().equals(this.codeGen.paramArrayName)) {
            aSTree2.accept(this);
            CtClass[] ctClassArray = this.codeGen.paramTypeList;
            if (ctClassArray == null) {
                return;
            }
            int n2 = ctClassArray.length;
            for (int i = 0; i < n2; ++i) {
                this.compileUnwrapValue(ctClassArray[i]);
            }
        } else {
            super.atFieldAssign(expr, n, aSTree, aSTree2);
        }
    }

    @Override
    public void atCastExpr(CastExpr castExpr) {
        ASTree aSTree;
        ASTList aSTList = castExpr.getClassName();
        if (aSTList != null && castExpr.getArrayDim() == 0 && (aSTree = aSTList.head()) instanceof Symbol && aSTList.tail() == null) {
            String string = ((Symbol)aSTree).get();
            if (string.equals(this.codeGen.returnCastName)) {
                this.atCastToRtype(castExpr);
                return;
            }
            if (string.equals("$w")) {
                this.atCastToWrapper(castExpr);
                return;
            }
        }
        super.atCastExpr(castExpr);
    }

    protected void atCastToRtype(CastExpr castExpr) {
        CtClass ctClass = this.codeGen.returnType;
        castExpr.getOprand().accept(this);
        if (this.exprType == 344 || CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            this.compileUnwrapValue(ctClass);
        } else if (ctClass instanceof CtPrimitiveType) {
            int n;
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            this.exprType = n = MemberResolver.descToType(ctPrimitiveType.getDescriptor());
            this.arrayDim = 0;
            this.className = null;
        }
    }

    protected void atCastToWrapper(CastExpr castExpr) {
        castExpr.getOprand().accept(this);
        if (CodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            return;
        }
        CtClass ctClass = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
        if (ctClass instanceof CtPrimitiveType) {
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/Object";
        }
    }

    @Override
    public void atCallExpr(CallExpr callExpr) {
        ASTree aSTree = callExpr.oprand1();
        if (aSTree instanceof Member) {
            String string = ((Member)aSTree).get();
            if (this.codeGen.procHandler != null && string.equals(this.codeGen.proceedName)) {
                this.codeGen.procHandler.setReturnType(this, (ASTList)callExpr.oprand2());
                return;
            }
            if (string.equals("$cflow")) {
                this.atCflow((ASTList)callExpr.oprand2());
                return;
            }
        }
        super.atCallExpr(callExpr);
    }

    protected void atCflow(ASTList aSTList) {
        this.exprType = 324;
        this.arrayDim = 0;
        this.className = null;
    }

    public boolean isParamListName(ASTList aSTList) {
        if (this.codeGen.paramTypeList != null && aSTList != null && aSTList.tail() == null) {
            ASTree aSTree = aSTList.head();
            return aSTree instanceof Member && ((Member)aSTree).get().equals(this.codeGen.paramListName);
        }
        return false;
    }

    @Override
    public int getMethodArgsLength(ASTList aSTList) {
        String string = this.codeGen.paramListName;
        int n = 0;
        while (aSTList != null) {
            ASTree aSTree = aSTList.head();
            if (aSTree instanceof Member && ((Member)aSTree).get().equals(string)) {
                if (this.codeGen.paramTypeList != null) {
                    n += this.codeGen.paramTypeList.length;
                }
            } else {
                ++n;
            }
            aSTList = aSTList.tail();
        }
        return n;
    }

    @Override
    public void atMethodArgs(ASTList aSTList, int[] nArray, int[] nArray2, String[] stringArray) {
        CtClass[] ctClassArray = this.codeGen.paramTypeList;
        String string = this.codeGen.paramListName;
        int n = 0;
        while (aSTList != null) {
            ASTree aSTree = aSTList.head();
            if (aSTree instanceof Member && ((Member)aSTree).get().equals(string)) {
                if (ctClassArray != null) {
                    int n2 = ctClassArray.length;
                    for (int i = 0; i < n2; ++i) {
                        CtClass ctClass = ctClassArray[i];
                        this.setType(ctClass);
                        nArray[n] = this.exprType;
                        nArray2[n] = this.arrayDim;
                        stringArray[n] = this.className;
                        ++n;
                    }
                }
            } else {
                aSTree.accept(this);
                nArray[n] = this.exprType;
                nArray2[n] = this.arrayDim;
                stringArray[n] = this.className;
                ++n;
            }
            aSTList = aSTList.tail();
        }
    }

    void compileInvokeSpecial(ASTree aSTree, String string, String string2, String string3, ASTList aSTList) {
        aSTree.accept(this);
        int n = this.getMethodArgsLength(aSTList);
        this.atMethodArgs(aSTList, new int[n], new int[n], new String[n]);
        this.setReturnType(string3);
        this.addNullIfVoid();
    }

    protected void compileUnwrapValue(CtClass ctClass) {
        if (ctClass == CtClass.voidType) {
            this.addNullIfVoid();
        } else {
            this.setType(ctClass);
        }
    }

    public void setType(CtClass ctClass) {
        this.setType(ctClass, 0);
    }

    private void setType(CtClass ctClass, int n) {
        if (ctClass.isPrimitive()) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            this.exprType = MemberResolver.descToType(ctPrimitiveType.getDescriptor());
            this.arrayDim = n;
            this.className = null;
        } else if (ctClass.isArray()) {
            try {
                this.setType(ctClass.getComponentType(), n + 1);
            } catch (NotFoundException notFoundException) {
                throw new CompileError("undefined type: " + ctClass.getName());
            }
        } else {
            this.exprType = 307;
            this.arrayDim = n;
            this.className = MemberResolver.javaToJvmName(ctClass.getName());
        }
    }
}

