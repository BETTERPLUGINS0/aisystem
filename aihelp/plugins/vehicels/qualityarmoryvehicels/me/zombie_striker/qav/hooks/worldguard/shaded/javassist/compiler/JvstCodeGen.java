/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstTypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberCodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ProceedHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.SymbolTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CallExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CastExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Member;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Stmnt;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;

public class JvstCodeGen
extends MemberCodeGen {
    String paramArrayName = null;
    String paramListName = null;
    CtClass[] paramTypeList = null;
    private int paramVarBase = 0;
    private boolean useParam0 = false;
    private String param0Type = null;
    public static final String sigName = "$sig";
    public static final String dollarTypeName = "$type";
    public static final String clazzName = "$class";
    private CtClass dollarType = null;
    CtClass returnType = null;
    String returnCastName = null;
    private String returnVarName = null;
    public static final String wrapperCastName = "$w";
    String proceedName = null;
    public static final String cflowName = "$cflow";
    ProceedHandler procHandler = null;

    public JvstCodeGen(Bytecode bytecode, CtClass ctClass, ClassPool classPool) {
        super(bytecode, ctClass, classPool);
        this.setTypeChecker(new JvstTypeChecker(ctClass, classPool, this));
    }

    private int indexOfParam1() {
        return this.paramVarBase + (this.useParam0 ? 1 : 0);
    }

    public void setProceedHandler(ProceedHandler proceedHandler, String string) {
        this.proceedName = string;
        this.procHandler = proceedHandler;
    }

    public void addNullIfVoid() {
        if (this.exprType == 344) {
            this.bytecode.addOpcode(1);
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/Object";
        }
    }

    @Override
    public void atMember(Member member) {
        String string = member.get();
        if (string.equals(this.paramArrayName)) {
            JvstCodeGen.compileParameterList(this.bytecode, this.paramTypeList, this.indexOfParam1());
            this.exprType = 307;
            this.arrayDim = 1;
            this.className = "java/lang/Object";
        } else if (string.equals(sigName)) {
            this.bytecode.addLdc(Descriptor.ofMethod(this.returnType, this.paramTypeList));
            this.bytecode.addInvokestatic("me/zombie_striker/qav/hooks/worldguard/shaded/javassist/runtime/Desc", "getParams", "(Ljava/lang/String;)[Ljava/lang/Class;");
            this.exprType = 307;
            this.arrayDim = 1;
            this.className = "java/lang/Class";
        } else if (string.equals(dollarTypeName)) {
            if (this.dollarType == null) {
                throw new CompileError("$type is not available");
            }
            this.bytecode.addLdc(Descriptor.of(this.dollarType));
            this.callGetType("getType");
        } else if (string.equals(clazzName)) {
            if (this.param0Type == null) {
                throw new CompileError("$class is not available");
            }
            this.bytecode.addLdc(this.param0Type);
            this.callGetType("getClazz");
        } else {
            super.atMember(member);
        }
    }

    private void callGetType(String string) {
        this.bytecode.addInvokestatic("me/zombie_striker/qav/hooks/worldguard/shaded/javassist/runtime/Desc", string, "(Ljava/lang/String;)Ljava/lang/Class;");
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = "java/lang/Class";
    }

    @Override
    protected void atFieldAssign(Expr expr, int n, ASTree aSTree, ASTree aSTree2, boolean bl) {
        if (aSTree instanceof Member && ((Member)aSTree).get().equals(this.paramArrayName)) {
            if (n != 61) {
                throw new CompileError("bad operator for " + this.paramArrayName);
            }
            aSTree2.accept(this);
            if (this.arrayDim != 1 || this.exprType != 307) {
                throw new CompileError("invalid type for " + this.paramArrayName);
            }
            this.atAssignParamList(this.paramTypeList, this.bytecode);
            if (!bl) {
                this.bytecode.addOpcode(87);
            }
        } else {
            super.atFieldAssign(expr, n, aSTree, aSTree2, bl);
        }
    }

    protected void atAssignParamList(CtClass[] ctClassArray, Bytecode bytecode) {
        if (ctClassArray == null) {
            return;
        }
        int n = this.indexOfParam1();
        int n2 = ctClassArray.length;
        for (int i = 0; i < n2; ++i) {
            bytecode.addOpcode(89);
            bytecode.addIconst(i);
            bytecode.addOpcode(50);
            this.compileUnwrapValue(ctClassArray[i], bytecode);
            bytecode.addStore(n, ctClassArray[i]);
            n += JvstCodeGen.is2word(this.exprType, this.arrayDim) ? 2 : 1;
        }
    }

    @Override
    public void atCastExpr(CastExpr castExpr) {
        ASTree aSTree;
        ASTList aSTList = castExpr.getClassName();
        if (aSTList != null && castExpr.getArrayDim() == 0 && (aSTree = aSTList.head()) instanceof Symbol && aSTList.tail() == null) {
            String string = ((Symbol)aSTree).get();
            if (string.equals(this.returnCastName)) {
                this.atCastToRtype(castExpr);
                return;
            }
            if (string.equals(wrapperCastName)) {
                this.atCastToWrapper(castExpr);
                return;
            }
        }
        super.atCastExpr(castExpr);
    }

    protected void atCastToRtype(CastExpr castExpr) {
        castExpr.getOprand().accept(this);
        if (this.exprType == 344 || JvstCodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            this.compileUnwrapValue(this.returnType, this.bytecode);
        } else if (this.returnType instanceof CtPrimitiveType) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)this.returnType;
            int n = MemberResolver.descToType(ctPrimitiveType.getDescriptor());
            this.atNumCastExpr(this.exprType, n);
            this.exprType = n;
            this.arrayDim = 0;
            this.className = null;
        } else {
            throw new CompileError("invalid cast");
        }
    }

    protected void atCastToWrapper(CastExpr castExpr) {
        castExpr.getOprand().accept(this);
        if (JvstCodeGen.isRefType(this.exprType) || this.arrayDim > 0) {
            return;
        }
        CtClass ctClass = this.resolver.lookupClass(this.exprType, this.arrayDim, this.className);
        if (ctClass instanceof CtPrimitiveType) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            String string = ctPrimitiveType.getWrapperName();
            this.bytecode.addNew(string);
            this.bytecode.addOpcode(89);
            if (ctPrimitiveType.getDataSize() > 1) {
                this.bytecode.addOpcode(94);
            } else {
                this.bytecode.addOpcode(93);
            }
            this.bytecode.addOpcode(88);
            this.bytecode.addInvokespecial(string, "<init>", "(" + ctPrimitiveType.getDescriptor() + ")V");
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
            if (this.procHandler != null && string.equals(this.proceedName)) {
                this.procHandler.doit(this, this.bytecode, (ASTList)callExpr.oprand2());
                return;
            }
            if (string.equals(cflowName)) {
                this.atCflow((ASTList)callExpr.oprand2());
                return;
            }
        }
        super.atCallExpr(callExpr);
    }

    protected void atCflow(ASTList aSTList) {
        StringBuilder stringBuilder = new StringBuilder();
        if (aSTList == null || aSTList.tail() != null) {
            throw new CompileError("bad $cflow");
        }
        JvstCodeGen.makeCflowName(stringBuilder, aSTList.head());
        String string = stringBuilder.toString();
        Object[] objectArray = this.resolver.getClassPool().lookupCflow(string);
        if (objectArray == null) {
            throw new CompileError("no such $cflow: " + string);
        }
        this.bytecode.addGetstatic((String)objectArray[0], (String)objectArray[1], "Lme/zombie_striker/qav/hooks/worldguard/shaded/javassist/runtime/Cflow;");
        this.bytecode.addInvokevirtual("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.runtime.Cflow", "value", "()I");
        this.exprType = 324;
        this.arrayDim = 0;
        this.className = null;
    }

    private static void makeCflowName(StringBuilder stringBuilder, ASTree aSTree) {
        Expr expr;
        if (aSTree instanceof Symbol) {
            stringBuilder.append(((Symbol)aSTree).get());
            return;
        }
        if (aSTree instanceof Expr && (expr = (Expr)aSTree).getOperator() == 46) {
            JvstCodeGen.makeCflowName(stringBuilder, expr.oprand1());
            stringBuilder.append('.');
            JvstCodeGen.makeCflowName(stringBuilder, expr.oprand2());
            return;
        }
        throw new CompileError("bad $cflow");
    }

    public boolean isParamListName(ASTList aSTList) {
        if (this.paramTypeList != null && aSTList != null && aSTList.tail() == null) {
            ASTree aSTree = aSTList.head();
            return aSTree instanceof Member && ((Member)aSTree).get().equals(this.paramListName);
        }
        return false;
    }

    @Override
    public int getMethodArgsLength(ASTList aSTList) {
        String string = this.paramListName;
        int n = 0;
        while (aSTList != null) {
            ASTree aSTree = aSTList.head();
            if (aSTree instanceof Member && ((Member)aSTree).get().equals(string)) {
                if (this.paramTypeList != null) {
                    n += this.paramTypeList.length;
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
        CtClass[] ctClassArray = this.paramTypeList;
        String string = this.paramListName;
        int n = 0;
        while (aSTList != null) {
            ASTree aSTree = aSTList.head();
            if (aSTree instanceof Member && ((Member)aSTree).get().equals(string)) {
                if (ctClassArray != null) {
                    int n2 = ctClassArray.length;
                    int n3 = this.indexOfParam1();
                    for (int i = 0; i < n2; ++i) {
                        CtClass ctClass = ctClassArray[i];
                        n3 += this.bytecode.addLoad(n3, ctClass);
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

    void compileInvokeSpecial(ASTree aSTree, int n, String string, ASTList aSTList) {
        aSTree.accept(this);
        int n2 = this.getMethodArgsLength(aSTList);
        this.atMethodArgs(aSTList, new int[n2], new int[n2], new String[n2]);
        this.bytecode.addInvokespecial(n, string);
        this.setReturnType(string, false, false);
        this.addNullIfVoid();
    }

    @Override
    protected void atReturnStmnt(Stmnt stmnt) {
        ASTree aSTree = stmnt.getLeft();
        if (aSTree != null && this.returnType == CtClass.voidType) {
            this.compileExpr(aSTree);
            if (JvstCodeGen.is2word(this.exprType, this.arrayDim)) {
                this.bytecode.addOpcode(88);
            } else if (this.exprType != 344) {
                this.bytecode.addOpcode(87);
            }
            aSTree = null;
        }
        this.atReturnStmnt2(aSTree);
    }

    public int recordReturnType(CtClass ctClass, String string, String string2, SymbolTable symbolTable) {
        this.returnType = ctClass;
        this.returnCastName = string;
        this.returnVarName = string2;
        if (string2 == null) {
            return -1;
        }
        int n = this.getMaxLocals();
        int n2 = n + this.recordVar(ctClass, string2, n, symbolTable);
        this.setMaxLocals(n2);
        return n;
    }

    public void recordType(CtClass ctClass) {
        this.dollarType = ctClass;
    }

    public int recordParams(CtClass[] ctClassArray, boolean bl, String string, String string2, String string3, SymbolTable symbolTable) {
        return this.recordParams(ctClassArray, bl, string, string2, string3, !bl, 0, this.getThisName(), symbolTable);
    }

    public int recordParams(CtClass[] ctClassArray, boolean bl, String string, String string2, String string3, boolean bl2, int n, String string4, SymbolTable symbolTable) {
        this.paramTypeList = ctClassArray;
        this.paramArrayName = string2;
        this.paramListName = string3;
        this.paramVarBase = n;
        this.useParam0 = bl2;
        if (string4 != null) {
            this.param0Type = MemberResolver.jvmToJavaName(string4);
        }
        this.inStaticMethod = bl;
        int n2 = n;
        if (bl2) {
            String string5 = string + "0";
            Declarator declarator = new Declarator(307, MemberResolver.javaToJvmName(string4), 0, n2++, new Symbol(string5));
            symbolTable.append(string5, declarator);
        }
        for (int i = 0; i < ctClassArray.length; ++i) {
            n2 += this.recordVar(ctClassArray[i], string + (i + 1), n2, symbolTable);
        }
        if (this.getMaxLocals() < n2) {
            this.setMaxLocals(n2);
        }
        return n2;
    }

    public int recordVariable(CtClass ctClass, String string, SymbolTable symbolTable) {
        if (string == null) {
            return -1;
        }
        int n = this.getMaxLocals();
        int n2 = n + this.recordVar(ctClass, string, n, symbolTable);
        this.setMaxLocals(n2);
        return n;
    }

    private int recordVar(CtClass ctClass, String string, int n, SymbolTable symbolTable) {
        if (ctClass == CtClass.voidType) {
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/Object";
        } else {
            this.setType(ctClass);
        }
        Declarator declarator = new Declarator(this.exprType, this.className, this.arrayDim, n, new Symbol(string));
        symbolTable.append(string, declarator);
        return JvstCodeGen.is2word(this.exprType, this.arrayDim) ? 2 : 1;
    }

    public void recordVariable(String string, String string2, int n, SymbolTable symbolTable) {
        char c;
        int n2 = 0;
        while ((c = string.charAt(n2)) == '[') {
            ++n2;
        }
        int n3 = MemberResolver.descToType(c);
        String string3 = null;
        if (n3 == 307) {
            string3 = n2 == 0 ? string.substring(1, string.length() - 1) : string.substring(n2 + 1, string.length() - 1);
        }
        Declarator declarator = new Declarator(n3, string3, n2, n, new Symbol(string2));
        symbolTable.append(string2, declarator);
    }

    public static int compileParameterList(Bytecode bytecode, CtClass[] ctClassArray, int n) {
        if (ctClassArray == null) {
            bytecode.addIconst(0);
            bytecode.addAnewarray("java.lang.Object");
            return 1;
        }
        CtClass[] ctClassArray2 = new CtClass[1];
        int n2 = ctClassArray.length;
        bytecode.addIconst(n2);
        bytecode.addAnewarray("java.lang.Object");
        for (int i = 0; i < n2; ++i) {
            bytecode.addOpcode(89);
            bytecode.addIconst(i);
            if (ctClassArray[i].isPrimitive()) {
                CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClassArray[i];
                String string = ctPrimitiveType.getWrapperName();
                bytecode.addNew(string);
                bytecode.addOpcode(89);
                int n3 = bytecode.addLoad(n, ctPrimitiveType);
                n += n3;
                ctClassArray2[0] = ctPrimitiveType;
                bytecode.addInvokespecial(string, "<init>", Descriptor.ofMethod(CtClass.voidType, ctClassArray2));
            } else {
                bytecode.addAload(n);
                ++n;
            }
            bytecode.addOpcode(83);
        }
        return 8;
    }

    protected void compileUnwrapValue(CtClass ctClass, Bytecode bytecode) {
        if (ctClass == CtClass.voidType) {
            this.addNullIfVoid();
            return;
        }
        if (this.exprType == 344) {
            throw new CompileError("invalid type for " + this.returnCastName);
        }
        if (ctClass instanceof CtPrimitiveType) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            String string = ctPrimitiveType.getWrapperName();
            bytecode.addCheckcast(string);
            bytecode.addInvokevirtual(string, ctPrimitiveType.getGetMethodName(), ctPrimitiveType.getGetMethodDescriptor());
            this.setType(ctClass);
        } else {
            bytecode.addCheckcast(ctClass);
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

    public void doNumCast(CtClass ctClass) {
        if (this.arrayDim == 0 && !JvstCodeGen.isRefType(this.exprType)) {
            if (ctClass instanceof CtPrimitiveType) {
                CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
                this.atNumCastExpr(this.exprType, MemberResolver.descToType(ctPrimitiveType.getDescriptor()));
            } else {
                throw new CompileError("type mismatch");
            }
        }
    }
}

