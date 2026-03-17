/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ArrayInit;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.AssignExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.BinExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CallExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CastExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CondExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.DoubleConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.FieldDecl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.InstanceOfExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.IntConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Keyword;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Member;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.MethodDecl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.NewExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Pair;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Stmnt;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.StringL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Variable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public abstract class CodeGen
extends Visitor
implements Opcode,
TokenId {
    static final String javaLangObject = "java.lang.Object";
    static final String jvmJavaLangObject = "java/lang/Object";
    static final String javaLangString = "java.lang.String";
    static final String jvmJavaLangString = "java/lang/String";
    protected Bytecode bytecode;
    private int tempVar;
    TypeChecker typeChecker;
    protected boolean hasReturned;
    public boolean inStaticMethod;
    protected List<Integer> breakList;
    protected List<Integer> continueList;
    protected ReturnHook returnHooks;
    protected int exprType;
    protected int arrayDim;
    protected String className;
    static final int[] binOp = new int[]{43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, 111, 110, 109, 108, 37, 115, 114, 113, 112, 124, 0, 0, 129, 128, 94, 0, 0, 131, 130, 38, 0, 0, 127, 126, 364, 0, 0, 121, 120, 366, 0, 0, 123, 122, 370, 0, 0, 125, 124};
    private static final int[] ifOp = new int[]{358, 159, 160, 350, 160, 159, 357, 164, 163, 359, 162, 161, 60, 161, 162, 62, 163, 164};
    private static final int[] ifOp2 = new int[]{358, 153, 154, 350, 154, 153, 357, 158, 157, 359, 156, 155, 60, 155, 156, 62, 157, 158};
    private static final int P_DOUBLE = 0;
    private static final int P_FLOAT = 1;
    private static final int P_LONG = 2;
    private static final int P_INT = 3;
    private static final int P_OTHER = -1;
    private static final int[] castOp = new int[]{0, 144, 143, 142, 141, 0, 140, 139, 138, 137, 0, 136, 135, 134, 133, 0};

    public CodeGen(Bytecode bytecode) {
        this.bytecode = bytecode;
        this.tempVar = -1;
        this.typeChecker = null;
        this.hasReturned = false;
        this.inStaticMethod = false;
        this.breakList = null;
        this.continueList = null;
        this.returnHooks = null;
    }

    public void setTypeChecker(TypeChecker typeChecker) {
        this.typeChecker = typeChecker;
    }

    protected static void fatal() {
        throw new CompileError("fatal");
    }

    public static boolean is2word(int n, int n2) {
        return n2 == 0 && (n == 312 || n == 326);
    }

    public int getMaxLocals() {
        return this.bytecode.getMaxLocals();
    }

    public void setMaxLocals(int n) {
        this.bytecode.setMaxLocals(n);
    }

    protected void incMaxLocals(int n) {
        this.bytecode.incMaxLocals(n);
    }

    protected int getTempVar() {
        if (this.tempVar < 0) {
            this.tempVar = this.getMaxLocals();
            this.incMaxLocals(2);
        }
        return this.tempVar;
    }

    protected int getLocalVar(Declarator declarator) {
        int n = declarator.getLocalVar();
        if (n < 0) {
            n = this.getMaxLocals();
            declarator.setLocalVar(n);
            this.incMaxLocals(1);
        }
        return n;
    }

    protected abstract String getThisName();

    protected abstract String getSuperName();

    protected abstract String resolveClassName(ASTList var1);

    protected abstract String resolveClassName(String var1);

    protected static String toJvmArrayName(String string, int n) {
        if (string == null) {
            return null;
        }
        if (n == 0) {
            return string;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int n2 = n;
        while (n2-- > 0) {
            stringBuilder.append('[');
        }
        stringBuilder.append('L');
        stringBuilder.append(string);
        stringBuilder.append(';');
        return stringBuilder.toString();
    }

    protected static String toJvmTypeName(int n, int n2) {
        char c = 'I';
        switch (n) {
            case 301: {
                c = 'Z';
                break;
            }
            case 303: {
                c = 'B';
                break;
            }
            case 306: {
                c = 'C';
                break;
            }
            case 334: {
                c = 'S';
                break;
            }
            case 324: {
                c = 'I';
                break;
            }
            case 326: {
                c = 'J';
                break;
            }
            case 317: {
                c = 'F';
                break;
            }
            case 312: {
                c = 'D';
                break;
            }
            case 344: {
                c = 'V';
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (n2-- > 0) {
            stringBuilder.append('[');
        }
        stringBuilder.append(c);
        return stringBuilder.toString();
    }

    public void compileExpr(ASTree aSTree) {
        this.doTypeCheck(aSTree);
        aSTree.accept(this);
    }

    public boolean compileBooleanExpr(boolean bl, ASTree aSTree) {
        this.doTypeCheck(aSTree);
        return this.booleanExpr(bl, aSTree);
    }

    public void doTypeCheck(ASTree aSTree) {
        if (this.typeChecker != null) {
            aSTree.accept(this.typeChecker);
        }
    }

    @Override
    public void atASTList(ASTList aSTList) {
        CodeGen.fatal();
    }

    @Override
    public void atPair(Pair pair) {
        CodeGen.fatal();
    }

    @Override
    public void atSymbol(Symbol symbol) {
        CodeGen.fatal();
    }

    @Override
    public void atFieldDecl(FieldDecl fieldDecl) {
        fieldDecl.getInit().accept(this);
    }

    @Override
    public void atMethodDecl(MethodDecl methodDecl) {
        ASTree aSTree;
        this.setMaxLocals(1);
        for (ASTList aSTList = methodDecl.getModifiers(); aSTList != null; aSTList = aSTList.tail()) {
            aSTree = (Keyword)aSTList.head();
            if (((Keyword)aSTree).get() != 335) continue;
            this.setMaxLocals(0);
            this.inStaticMethod = true;
        }
        for (aSTree = methodDecl.getParams(); aSTree != null; aSTree = ((ASTList)aSTree).tail()) {
            this.atDeclarator((Declarator)((ASTList)aSTree).head());
        }
        Stmnt stmnt = methodDecl.getBody();
        this.atMethodBody(stmnt, methodDecl.isConstructor(), methodDecl.getReturn().getType() == 344);
    }

    public void atMethodBody(Stmnt stmnt, boolean bl, boolean bl2) {
        if (stmnt == null) {
            return;
        }
        if (bl && this.needsSuperCall(stmnt)) {
            this.insertDefaultSuperCall();
        }
        this.hasReturned = false;
        stmnt.accept(this);
        if (!this.hasReturned) {
            if (bl2) {
                this.bytecode.addOpcode(177);
                this.hasReturned = true;
            } else {
                throw new CompileError("no return statement");
            }
        }
    }

    private boolean needsSuperCall(Stmnt stmnt) {
        ASTree aSTree;
        ASTree aSTree2;
        if (stmnt.getOperator() == 66) {
            stmnt = (Stmnt)stmnt.head();
        }
        if (stmnt != null && stmnt.getOperator() == 69 && (aSTree2 = stmnt.head()) != null && aSTree2 instanceof Expr && ((Expr)aSTree2).getOperator() == 67 && (aSTree = ((Expr)aSTree2).head()) instanceof Keyword) {
            int n = ((Keyword)aSTree).get();
            return n != 339 && n != 336;
        }
        return true;
    }

    protected abstract void insertDefaultSuperCall();

    @Override
    public void atStmnt(Stmnt stmnt) {
        if (stmnt == null) {
            return;
        }
        int n = stmnt.getOperator();
        if (n == 69) {
            ASTree aSTree = stmnt.getLeft();
            this.doTypeCheck(aSTree);
            if (aSTree instanceof AssignExpr) {
                this.atAssignExpr((AssignExpr)aSTree, false);
            } else if (CodeGen.isPlusPlusExpr(aSTree)) {
                Expr expr = (Expr)aSTree;
                this.atPlusPlus(expr.getOperator(), expr.oprand1(), expr, false);
            } else {
                aSTree.accept(this);
                if (CodeGen.is2word(this.exprType, this.arrayDim)) {
                    this.bytecode.addOpcode(88);
                } else if (this.exprType != 344) {
                    this.bytecode.addOpcode(87);
                }
            }
        } else if (n == 68 || n == 66) {
            for (ASTList aSTList = stmnt; aSTList != null; aSTList = aSTList.tail()) {
                ASTree aSTree = aSTList.head();
                if (aSTree == null) continue;
                aSTree.accept(this);
            }
        } else if (n == 320) {
            this.atIfStmnt(stmnt);
        } else if (n == 346 || n == 311) {
            this.atWhileStmnt(stmnt, n == 346);
        } else if (n == 318) {
            this.atForStmnt(stmnt);
        } else if (n == 302 || n == 309) {
            this.atBreakStmnt(stmnt, n == 302);
        } else if (n == 333) {
            this.atReturnStmnt(stmnt);
        } else if (n == 340) {
            this.atThrowStmnt(stmnt);
        } else if (n == 343) {
            this.atTryStmnt(stmnt);
        } else if (n == 337) {
            this.atSwitchStmnt(stmnt);
        } else if (n == 338) {
            this.atSyncStmnt(stmnt);
        } else {
            this.hasReturned = false;
            throw new CompileError("sorry, not supported statement: TokenId " + n);
        }
    }

    private void atIfStmnt(Stmnt stmnt) {
        ASTree aSTree = stmnt.head();
        Stmnt stmnt2 = (Stmnt)stmnt.tail().head();
        Stmnt stmnt3 = (Stmnt)stmnt.tail().tail().head();
        if (this.compileBooleanExpr(false, aSTree)) {
            this.hasReturned = false;
            if (stmnt3 != null) {
                stmnt3.accept(this);
            }
            return;
        }
        int n = this.bytecode.currentPc();
        int n2 = 0;
        this.bytecode.addIndex(0);
        this.hasReturned = false;
        if (stmnt2 != null) {
            stmnt2.accept(this);
        }
        boolean bl = this.hasReturned;
        this.hasReturned = false;
        if (stmnt3 != null && !bl) {
            this.bytecode.addOpcode(167);
            n2 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
        }
        this.bytecode.write16bit(n, this.bytecode.currentPc() - n + 1);
        if (stmnt3 != null) {
            stmnt3.accept(this);
            if (!bl) {
                this.bytecode.write16bit(n2, this.bytecode.currentPc() - n2 + 1);
            }
            this.hasReturned = bl && this.hasReturned;
        }
    }

    private void atWhileStmnt(Stmnt stmnt, boolean bl) {
        boolean bl2;
        List<Integer> list = this.breakList;
        List<Integer> list2 = this.continueList;
        this.breakList = new ArrayList<Integer>();
        this.continueList = new ArrayList<Integer>();
        ASTree aSTree = stmnt.head();
        Stmnt stmnt2 = (Stmnt)stmnt.tail();
        int n = 0;
        if (bl) {
            this.bytecode.addOpcode(167);
            n = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
        }
        int n2 = this.bytecode.currentPc();
        if (stmnt2 != null) {
            stmnt2.accept(this);
        }
        int n3 = this.bytecode.currentPc();
        if (bl) {
            this.bytecode.write16bit(n, n3 - n + 1);
        }
        if (bl2 = this.compileBooleanExpr(true, aSTree)) {
            this.bytecode.addOpcode(167);
            bl2 = this.breakList.isEmpty();
        }
        this.bytecode.addIndex(n2 - this.bytecode.currentPc() + 1);
        this.patchGoto(this.breakList, this.bytecode.currentPc());
        this.patchGoto(this.continueList, n3);
        this.continueList = list2;
        this.breakList = list;
        this.hasReturned = bl2;
    }

    protected void patchGoto(List<Integer> list, int n) {
        for (int n2 : list) {
            this.bytecode.write16bit(n2, n - n2 + 1);
        }
    }

    private void atForStmnt(Stmnt stmnt) {
        List<Integer> list = this.breakList;
        List<Integer> list2 = this.continueList;
        this.breakList = new ArrayList<Integer>();
        this.continueList = new ArrayList<Integer>();
        Stmnt stmnt2 = (Stmnt)stmnt.head();
        ASTList aSTList = stmnt.tail();
        ASTree aSTree = aSTList.head();
        aSTList = aSTList.tail();
        Stmnt stmnt3 = (Stmnt)aSTList.head();
        Stmnt stmnt4 = (Stmnt)aSTList.tail();
        if (stmnt2 != null) {
            stmnt2.accept(this);
        }
        int n = this.bytecode.currentPc();
        int n2 = 0;
        if (aSTree != null) {
            if (this.compileBooleanExpr(false, aSTree)) {
                this.continueList = list2;
                this.breakList = list;
                this.hasReturned = false;
                return;
            }
            n2 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
        }
        if (stmnt4 != null) {
            stmnt4.accept(this);
        }
        int n3 = this.bytecode.currentPc();
        if (stmnt3 != null) {
            stmnt3.accept(this);
        }
        this.bytecode.addOpcode(167);
        this.bytecode.addIndex(n - this.bytecode.currentPc() + 1);
        int n4 = this.bytecode.currentPc();
        if (aSTree != null) {
            this.bytecode.write16bit(n2, n4 - n2 + 1);
        }
        this.patchGoto(this.breakList, n4);
        this.patchGoto(this.continueList, n3);
        this.continueList = list2;
        this.breakList = list;
        this.hasReturned = false;
    }

    private void atSwitchStmnt(Stmnt stmnt) {
        int n;
        boolean bl = false;
        if (this.typeChecker != null) {
            this.doTypeCheck(stmnt.head());
            bl = this.typeChecker.exprType == 307 && this.typeChecker.arrayDim == 0 && jvmJavaLangString.equals(this.typeChecker.className);
        }
        this.compileExpr(stmnt.head());
        int n2 = -1;
        if (bl) {
            n2 = this.getMaxLocals();
            this.incMaxLocals(1);
            this.bytecode.addAstore(n2);
            this.bytecode.addAload(n2);
            this.bytecode.addInvokevirtual(jvmJavaLangString, "hashCode", "()I");
        }
        List<Integer> list = this.breakList;
        this.breakList = new ArrayList<Integer>();
        int n3 = this.bytecode.currentPc();
        this.bytecode.addOpcode(171);
        int n4 = 3 - (n3 & 3);
        while (n4-- > 0) {
            this.bytecode.add(0);
        }
        Stmnt stmnt2 = (Stmnt)stmnt.tail();
        int n5 = 0;
        for (ASTList aSTList = stmnt2; aSTList != null; aSTList = aSTList.tail()) {
            if (((Stmnt)aSTList.head()).getOperator() != 304) continue;
            ++n5;
        }
        int n6 = this.bytecode.currentPc();
        this.bytecode.addGap(4);
        this.bytecode.add32bit(n5);
        this.bytecode.addGap(n5 * 8);
        long[] lArray = new long[n5];
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        int n7 = 0;
        int n8 = -1;
        for (ASTList aSTList = stmnt2; aSTList != null; aSTList = aSTList.tail()) {
            Stmnt stmnt3 = (Stmnt)aSTList.head();
            int n9 = stmnt3.getOperator();
            if (n9 == 310) {
                n8 = this.bytecode.currentPc();
            } else if (n9 != 304) {
                CodeGen.fatal();
            } else {
                int n10 = this.bytecode.currentPc();
                long l = bl ? (long)this.computeStringLabel(stmnt3.head(), n2, arrayList) : (long)this.computeLabel(stmnt3.head());
                lArray[n7++] = (l << 32) + ((long)(n10 - n3) & 0xFFFFFFFFFFFFFFFFL);
            }
            this.hasReturned = false;
            ((Stmnt)stmnt3.tail()).accept(this);
        }
        Arrays.sort(lArray);
        int n11 = n6 + 8;
        for (n = 0; n < n5; ++n) {
            this.bytecode.write32bit(n11, (int)(lArray[n] >>> 32));
            this.bytecode.write32bit(n11 + 4, (int)lArray[n]);
            n11 += 8;
        }
        if (n8 < 0 || this.breakList.size() > 0) {
            this.hasReturned = false;
        }
        n = this.bytecode.currentPc();
        if (n8 < 0) {
            n8 = n;
        }
        this.bytecode.write32bit(n6, n8 - n3);
        for (int n10 : arrayList) {
            this.bytecode.write16bit(n10, n8 - n10 + 1);
        }
        this.patchGoto(this.breakList, n);
        this.breakList = list;
    }

    private int computeLabel(ASTree aSTree) {
        this.doTypeCheck(aSTree);
        aSTree = TypeChecker.stripPlusExpr(aSTree);
        if (aSTree instanceof IntConst) {
            return (int)((IntConst)aSTree).get();
        }
        throw new CompileError("bad case label");
    }

    private int computeStringLabel(ASTree aSTree, int n, List<Integer> list) {
        this.doTypeCheck(aSTree);
        aSTree = TypeChecker.stripPlusExpr(aSTree);
        if (aSTree instanceof StringL) {
            String string = ((StringL)aSTree).get();
            this.bytecode.addAload(n);
            this.bytecode.addLdc(string);
            this.bytecode.addInvokevirtual(jvmJavaLangString, "equals", "(Ljava/lang/Object;)Z");
            this.bytecode.addOpcode(153);
            Integer n2 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
            list.add(n2);
            return string.hashCode();
        }
        throw new CompileError("bad case label");
    }

    private void atBreakStmnt(Stmnt stmnt, boolean bl) {
        if (stmnt.head() != null) {
            throw new CompileError("sorry, not support labeled break or continue");
        }
        this.bytecode.addOpcode(167);
        Integer n = this.bytecode.currentPc();
        this.bytecode.addIndex(0);
        if (bl) {
            this.breakList.add(n);
        } else {
            this.continueList.add(n);
        }
    }

    protected void atReturnStmnt(Stmnt stmnt) {
        this.atReturnStmnt2(stmnt.getLeft());
    }

    protected final void atReturnStmnt2(ASTree aSTree) {
        int n;
        if (aSTree == null) {
            n = 177;
        } else {
            int n2;
            this.compileExpr(aSTree);
            n = this.arrayDim > 0 ? 176 : ((n2 = this.exprType) == 312 ? 175 : (n2 == 317 ? 174 : (n2 == 326 ? 173 : (CodeGen.isRefType(n2) ? 176 : 172))));
        }
        ReturnHook returnHook = this.returnHooks;
        while (returnHook != null) {
            if (returnHook.doit(this.bytecode, n)) {
                this.hasReturned = true;
                return;
            }
            returnHook = returnHook.next;
        }
        this.bytecode.addOpcode(n);
        this.hasReturned = true;
    }

    private void atThrowStmnt(Stmnt stmnt) {
        ASTree aSTree = stmnt.getLeft();
        this.compileExpr(aSTree);
        if (this.exprType != 307 || this.arrayDim > 0) {
            throw new CompileError("bad throw statement");
        }
        this.bytecode.addOpcode(191);
        this.hasReturned = true;
    }

    protected void atTryStmnt(Stmnt stmnt) {
        this.hasReturned = false;
    }

    private void atSyncStmnt(Stmnt stmnt) {
        int n = CodeGen.getListSize(this.breakList);
        int n2 = CodeGen.getListSize(this.continueList);
        this.compileExpr(stmnt.head());
        if (this.exprType != 307 && this.arrayDim == 0) {
            throw new CompileError("bad type expr for synchronized block");
        }
        Bytecode bytecode = this.bytecode;
        final int n3 = bytecode.getMaxLocals();
        bytecode.incMaxLocals(1);
        bytecode.addOpcode(89);
        bytecode.addAstore(n3);
        bytecode.addOpcode(194);
        ReturnHook returnHook = new ReturnHook(this){

            @Override
            protected boolean doit(Bytecode bytecode, int n) {
                bytecode.addAload(n3);
                bytecode.addOpcode(195);
                return false;
            }
        };
        int n4 = bytecode.currentPc();
        Stmnt stmnt2 = (Stmnt)stmnt.tail();
        if (stmnt2 != null) {
            stmnt2.accept(this);
        }
        int n5 = bytecode.currentPc();
        int n6 = 0;
        if (!this.hasReturned) {
            returnHook.doit(bytecode, 0);
            bytecode.addOpcode(167);
            n6 = bytecode.currentPc();
            bytecode.addIndex(0);
        }
        if (n4 < n5) {
            int n7 = bytecode.currentPc();
            returnHook.doit(bytecode, 0);
            bytecode.addOpcode(191);
            bytecode.addExceptionHandler(n4, n5, n7, 0);
        }
        if (!this.hasReturned) {
            bytecode.write16bit(n6, bytecode.currentPc() - n6 + 1);
        }
        returnHook.remove(this);
        if (CodeGen.getListSize(this.breakList) != n || CodeGen.getListSize(this.continueList) != n2) {
            throw new CompileError("sorry, cannot break/continue in synchronized block");
        }
    }

    private static int getListSize(List<Integer> list) {
        return list == null ? 0 : list.size();
    }

    private static boolean isPlusPlusExpr(ASTree aSTree) {
        if (aSTree instanceof Expr) {
            int n = ((Expr)aSTree).getOperator();
            return n == 362 || n == 363;
        }
        return false;
    }

    @Override
    public void atDeclarator(Declarator declarator) {
        declarator.setLocalVar(this.getMaxLocals());
        declarator.setClassName(this.resolveClassName(declarator.getClassName()));
        int n = CodeGen.is2word(declarator.getType(), declarator.getArrayDim()) ? 2 : 1;
        this.incMaxLocals(n);
        ASTree aSTree = declarator.getInitializer();
        if (aSTree != null) {
            this.doTypeCheck(aSTree);
            this.atVariableAssign(null, 61, null, declarator, aSTree, false);
        }
    }

    @Override
    public abstract void atNewExpr(NewExpr var1);

    @Override
    public abstract void atArrayInit(ArrayInit var1);

    @Override
    public void atAssignExpr(AssignExpr assignExpr) {
        this.atAssignExpr(assignExpr, true);
    }

    protected void atAssignExpr(AssignExpr assignExpr, boolean bl) {
        int n = assignExpr.getOperator();
        ASTree aSTree = assignExpr.oprand1();
        ASTree aSTree2 = assignExpr.oprand2();
        if (aSTree instanceof Variable) {
            this.atVariableAssign(assignExpr, n, (Variable)aSTree, ((Variable)aSTree).getDeclarator(), aSTree2, bl);
        } else {
            Expr expr;
            if (aSTree instanceof Expr && (expr = (Expr)aSTree).getOperator() == 65) {
                this.atArrayAssign(assignExpr, n, (Expr)aSTree, aSTree2, bl);
                return;
            }
            this.atFieldAssign(assignExpr, n, aSTree, aSTree2, bl);
        }
    }

    protected static void badAssign(Expr expr) {
        String string = expr == null ? "incompatible type for assignment" : "incompatible type for " + expr.getName();
        throw new CompileError(string);
    }

    private void atVariableAssign(Expr expr, int n, Variable variable, Declarator declarator, ASTree aSTree, boolean bl) {
        int n2 = declarator.getType();
        int n3 = declarator.getArrayDim();
        String string = declarator.getClassName();
        int n4 = this.getLocalVar(declarator);
        if (n != 61) {
            this.atVariable(variable);
        }
        if (expr == null && aSTree instanceof ArrayInit) {
            this.atArrayVariableAssign((ArrayInit)aSTree, n2, n3, string);
        } else {
            this.atAssignCore(expr, n, aSTree, n2, n3, string);
        }
        if (bl) {
            if (CodeGen.is2word(n2, n3)) {
                this.bytecode.addOpcode(92);
            } else {
                this.bytecode.addOpcode(89);
            }
        }
        if (n3 > 0) {
            this.bytecode.addAstore(n4);
        } else if (n2 == 312) {
            this.bytecode.addDstore(n4);
        } else if (n2 == 317) {
            this.bytecode.addFstore(n4);
        } else if (n2 == 326) {
            this.bytecode.addLstore(n4);
        } else if (CodeGen.isRefType(n2)) {
            this.bytecode.addAstore(n4);
        } else {
            this.bytecode.addIstore(n4);
        }
        this.exprType = n2;
        this.arrayDim = n3;
        this.className = string;
    }

    protected abstract void atArrayVariableAssign(ArrayInit var1, int var2, int var3, String var4);

    private void atArrayAssign(Expr expr, int n, Expr expr2, ASTree aSTree, boolean bl) {
        this.arrayAccess(expr2.oprand1(), expr2.oprand2());
        if (n != 61) {
            this.bytecode.addOpcode(92);
            this.bytecode.addOpcode(CodeGen.getArrayReadOp(this.exprType, this.arrayDim));
        }
        int n2 = this.exprType;
        int n3 = this.arrayDim;
        String string = this.className;
        this.atAssignCore(expr, n, aSTree, n2, n3, string);
        if (bl) {
            if (CodeGen.is2word(n2, n3)) {
                this.bytecode.addOpcode(94);
            } else {
                this.bytecode.addOpcode(91);
            }
        }
        this.bytecode.addOpcode(CodeGen.getArrayWriteOp(n2, n3));
        this.exprType = n2;
        this.arrayDim = n3;
        this.className = string;
    }

    protected abstract void atFieldAssign(Expr var1, int var2, ASTree var3, ASTree var4, boolean var5);

    protected void atAssignCore(Expr expr, int n, ASTree aSTree, int n2, int n3, String string) {
        if (n == 354 && n3 == 0 && n2 == 307) {
            this.atStringPlusEq(expr, n2, n3, string, aSTree);
        } else {
            aSTree.accept(this);
            if (this.invalidDim(this.exprType, this.arrayDim, this.className, n2, n3, string, false) || n != 61 && n3 > 0) {
                CodeGen.badAssign(expr);
            }
            if (n != 61) {
                int n4 = assignOps[n - 351];
                int n5 = CodeGen.lookupBinOp(n4);
                if (n5 < 0) {
                    CodeGen.fatal();
                }
                this.atArithBinExpr(expr, n4, n5, n2);
            }
        }
        if (n != 61 || n3 == 0 && !CodeGen.isRefType(n2)) {
            this.atNumCastExpr(this.exprType, n2);
        }
    }

    private void atStringPlusEq(Expr expr, int n, int n2, String string, ASTree aSTree) {
        if (!jvmJavaLangString.equals(string)) {
            CodeGen.badAssign(expr);
        }
        this.convToString(n, n2);
        aSTree.accept(this);
        this.convToString(this.exprType, this.arrayDim);
        this.bytecode.addInvokevirtual(javaLangString, "concat", "(Ljava/lang/String;)Ljava/lang/String;");
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
    }

    private boolean invalidDim(int n, int n2, String string, int n3, int n4, String string2, boolean bl) {
        if (n2 != n4) {
            if (n == 412) {
                return false;
            }
            if (n4 == 0 && n3 == 307 && jvmJavaLangObject.equals(string2)) {
                return false;
            }
            return !bl || n2 != 0 || n != 307 || !jvmJavaLangObject.equals(string);
        }
        return false;
    }

    @Override
    public void atCondExpr(CondExpr condExpr) {
        if (this.booleanExpr(false, condExpr.condExpr())) {
            condExpr.elseExpr().accept(this);
        } else {
            int n = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
            condExpr.thenExpr().accept(this);
            int n2 = this.arrayDim;
            this.bytecode.addOpcode(167);
            int n3 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
            this.bytecode.write16bit(n, this.bytecode.currentPc() - n + 1);
            condExpr.elseExpr().accept(this);
            if (n2 != this.arrayDim) {
                throw new CompileError("type mismatch in ?:");
            }
            this.bytecode.write16bit(n3, this.bytecode.currentPc() - n3 + 1);
        }
    }

    static int lookupBinOp(int n) {
        int[] nArray = binOp;
        int n2 = nArray.length;
        for (int i = 0; i < n2; i += 5) {
            if (nArray[i] != n) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void atBinExpr(BinExpr binExpr) {
        int n = binExpr.getOperator();
        int n2 = CodeGen.lookupBinOp(n);
        if (n2 >= 0) {
            binExpr.oprand1().accept(this);
            ASTree aSTree = binExpr.oprand2();
            if (aSTree == null) {
                return;
            }
            int n3 = this.exprType;
            int n4 = this.arrayDim;
            String string = this.className;
            aSTree.accept(this);
            if (n4 != this.arrayDim) {
                throw new CompileError("incompatible array types");
            }
            if (n == 43 && n4 == 0 && (n3 == 307 || this.exprType == 307)) {
                this.atStringConcatExpr(binExpr, n3, n4, string);
            } else {
                this.atArithBinExpr(binExpr, n, n2, n3);
            }
        } else {
            if (!this.booleanExpr(true, binExpr)) {
                this.bytecode.addIndex(7);
                this.bytecode.addIconst(0);
                this.bytecode.addOpcode(167);
                this.bytecode.addIndex(4);
            }
            this.bytecode.addIconst(1);
        }
    }

    private void atArithBinExpr(Expr expr, int n, int n2, int n3) {
        int n4;
        if (this.arrayDim != 0) {
            CodeGen.badTypes(expr);
        }
        int n5 = this.exprType;
        if (n == 364 || n == 366 || n == 370) {
            if (n5 == 324 || n5 == 334 || n5 == 306 || n5 == 303) {
                this.exprType = n3;
            } else {
                CodeGen.badTypes(expr);
            }
        } else {
            this.convertOprandTypes(n3, n5, expr);
        }
        int n6 = CodeGen.typePrecedence(this.exprType);
        if (n6 >= 0 && (n4 = binOp[n2 + n6 + 1]) != 0) {
            if (n6 == 3 && this.exprType != 301) {
                this.exprType = 324;
            }
            this.bytecode.addOpcode(n4);
            return;
        }
        CodeGen.badTypes(expr);
    }

    private void atStringConcatExpr(Expr expr, int n, int n2, String string) {
        boolean bl;
        int n3 = this.exprType;
        int n4 = this.arrayDim;
        boolean bl2 = CodeGen.is2word(n3, n4);
        boolean bl3 = bl = n3 == 307 && jvmJavaLangString.equals(this.className);
        if (bl2) {
            this.convToString(n3, n4);
        }
        if (CodeGen.is2word(n, n2)) {
            this.bytecode.addOpcode(91);
            this.bytecode.addOpcode(87);
        } else {
            this.bytecode.addOpcode(95);
        }
        this.convToString(n, n2);
        this.bytecode.addOpcode(95);
        if (!bl2 && !bl) {
            this.convToString(n3, n4);
        }
        this.bytecode.addInvokevirtual(javaLangString, "concat", "(Ljava/lang/String;)Ljava/lang/String;");
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
    }

    private void convToString(int n, int n2) {
        String string = "valueOf";
        if (CodeGen.isRefType(n) || n2 > 0) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
        } else if (n == 312) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(D)Ljava/lang/String;");
        } else if (n == 317) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(F)Ljava/lang/String;");
        } else if (n == 326) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(J)Ljava/lang/String;");
        } else if (n == 301) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(Z)Ljava/lang/String;");
        } else if (n == 306) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(C)Ljava/lang/String;");
        } else {
            if (n == 344) {
                throw new CompileError("void type expression");
            }
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(I)Ljava/lang/String;");
        }
    }

    private boolean booleanExpr(boolean bl, ASTree aSTree) {
        int n = CodeGen.getCompOperator(aSTree);
        if (n == 358) {
            BinExpr binExpr = (BinExpr)aSTree;
            int n2 = this.compileOprands(binExpr);
            this.compareExpr(bl, binExpr.getOperator(), n2, binExpr);
        } else {
            if (n == 33) {
                return this.booleanExpr(!bl, ((Expr)aSTree).oprand1());
            }
            boolean bl2 = n == 369;
            if (bl2 || n == 368) {
                BinExpr binExpr;
                if (this.booleanExpr(!bl2, (binExpr = (BinExpr)aSTree).oprand1())) {
                    this.exprType = 301;
                    this.arrayDim = 0;
                    return true;
                }
                int n3 = this.bytecode.currentPc();
                this.bytecode.addIndex(0);
                if (this.booleanExpr(bl2, binExpr.oprand2())) {
                    this.bytecode.addOpcode(167);
                }
                this.bytecode.write16bit(n3, this.bytecode.currentPc() - n3 + 3);
                if (bl != bl2) {
                    this.bytecode.addIndex(6);
                    this.bytecode.addOpcode(167);
                }
            } else {
                if (CodeGen.isAlwaysBranch(aSTree, bl)) {
                    this.exprType = 301;
                    this.arrayDim = 0;
                    return true;
                }
                aSTree.accept(this);
                if (this.exprType != 301 || this.arrayDim != 0) {
                    throw new CompileError("boolean expr is required");
                }
                this.bytecode.addOpcode(bl ? 154 : 153);
            }
        }
        this.exprType = 301;
        this.arrayDim = 0;
        return false;
    }

    private static boolean isAlwaysBranch(ASTree aSTree, boolean bl) {
        if (aSTree instanceof Keyword) {
            int n = ((Keyword)aSTree).get();
            return bl ? n == 410 : n == 411;
        }
        return false;
    }

    static int getCompOperator(ASTree aSTree) {
        if (aSTree instanceof Expr) {
            Expr expr = (Expr)aSTree;
            int n = expr.getOperator();
            if (n == 33) {
                return 33;
            }
            if (expr instanceof BinExpr && n != 368 && n != 369 && n != 38 && n != 124) {
                return 358;
            }
            return n;
        }
        return 32;
    }

    private int compileOprands(BinExpr binExpr) {
        binExpr.oprand1().accept(this);
        int n = this.exprType;
        int n2 = this.arrayDim;
        binExpr.oprand2().accept(this);
        if (n2 != this.arrayDim) {
            if (n != 412 && this.exprType != 412) {
                throw new CompileError("incompatible array types");
            }
            if (this.exprType == 412) {
                this.arrayDim = n2;
            }
        }
        if (n == 412) {
            return this.exprType;
        }
        return n;
    }

    private void compareExpr(boolean bl, int n, int n2, BinExpr binExpr) {
        int n3;
        if (this.arrayDim == 0) {
            this.convertOprandTypes(n2, this.exprType, binExpr);
        }
        if ((n3 = CodeGen.typePrecedence(this.exprType)) == -1 || this.arrayDim > 0) {
            if (n == 358) {
                this.bytecode.addOpcode(bl ? 165 : 166);
            } else if (n == 350) {
                this.bytecode.addOpcode(bl ? 166 : 165);
            } else {
                CodeGen.badTypes(binExpr);
            }
        } else if (n3 == 3) {
            int[] nArray = ifOp;
            for (int i = 0; i < nArray.length; i += 3) {
                if (nArray[i] != n) continue;
                this.bytecode.addOpcode(nArray[i + (bl ? 1 : 2)]);
                return;
            }
            CodeGen.badTypes(binExpr);
        } else {
            if (n3 == 0) {
                if (n == 60 || n == 357) {
                    this.bytecode.addOpcode(152);
                } else {
                    this.bytecode.addOpcode(151);
                }
            } else if (n3 == 1) {
                if (n == 60 || n == 357) {
                    this.bytecode.addOpcode(150);
                } else {
                    this.bytecode.addOpcode(149);
                }
            } else if (n3 == 2) {
                this.bytecode.addOpcode(148);
            } else {
                CodeGen.fatal();
            }
            int[] nArray = ifOp2;
            for (int i = 0; i < nArray.length; i += 3) {
                if (nArray[i] != n) continue;
                this.bytecode.addOpcode(nArray[i + (bl ? 1 : 2)]);
                return;
            }
            CodeGen.badTypes(binExpr);
        }
    }

    protected static void badTypes(Expr expr) {
        throw new CompileError("invalid types for " + expr.getName());
    }

    protected static boolean isRefType(int n) {
        return n == 307 || n == 412;
    }

    private static int typePrecedence(int n) {
        if (n == 312) {
            return 0;
        }
        if (n == 317) {
            return 1;
        }
        if (n == 326) {
            return 2;
        }
        if (CodeGen.isRefType(n)) {
            return -1;
        }
        if (n == 344) {
            return -1;
        }
        return 3;
    }

    static boolean isP_INT(int n) {
        return CodeGen.typePrecedence(n) == 3;
    }

    static boolean rightIsStrong(int n, int n2) {
        int n3 = CodeGen.typePrecedence(n);
        int n4 = CodeGen.typePrecedence(n2);
        return n3 >= 0 && n4 >= 0 && n3 > n4;
    }

    private void convertOprandTypes(int n, int n2, Expr expr) {
        int n3;
        int n4;
        boolean bl;
        int n5 = CodeGen.typePrecedence(n);
        int n6 = CodeGen.typePrecedence(n2);
        if (n6 < 0 && n5 < 0) {
            return;
        }
        if (n6 < 0 || n5 < 0) {
            CodeGen.badTypes(expr);
        }
        if (n5 <= n6) {
            bl = false;
            this.exprType = n;
            n4 = castOp[n6 * 4 + n5];
            n3 = n5;
        } else {
            bl = true;
            n4 = castOp[n5 * 4 + n6];
            n3 = n6;
        }
        if (bl) {
            if (n3 == 0 || n3 == 2) {
                if (n5 == 0 || n5 == 2) {
                    this.bytecode.addOpcode(94);
                } else {
                    this.bytecode.addOpcode(93);
                }
                this.bytecode.addOpcode(88);
                this.bytecode.addOpcode(n4);
                this.bytecode.addOpcode(94);
                this.bytecode.addOpcode(88);
            } else if (n3 == 1) {
                if (n5 == 2) {
                    this.bytecode.addOpcode(91);
                    this.bytecode.addOpcode(87);
                } else {
                    this.bytecode.addOpcode(95);
                }
                this.bytecode.addOpcode(n4);
                this.bytecode.addOpcode(95);
            } else {
                CodeGen.fatal();
            }
        } else if (n4 != 0) {
            this.bytecode.addOpcode(n4);
        }
    }

    @Override
    public void atCastExpr(CastExpr castExpr) {
        String string = this.resolveClassName(castExpr.getClassName());
        String string2 = this.checkCastExpr(castExpr, string);
        int n = this.exprType;
        this.exprType = castExpr.getType();
        this.arrayDim = castExpr.getArrayDim();
        this.className = string;
        if (string2 == null) {
            this.atNumCastExpr(n, this.exprType);
        } else {
            this.bytecode.addCheckcast(string2);
        }
    }

    @Override
    public void atInstanceOfExpr(InstanceOfExpr instanceOfExpr) {
        String string = this.resolveClassName(instanceOfExpr.getClassName());
        String string2 = this.checkCastExpr(instanceOfExpr, string);
        this.bytecode.addInstanceof(string2);
        this.exprType = 301;
        this.arrayDim = 0;
    }

    private String checkCastExpr(CastExpr castExpr, String string) {
        String string2 = "invalid cast";
        ASTree aSTree = castExpr.getOprand();
        int n = castExpr.getArrayDim();
        int n2 = castExpr.getType();
        aSTree.accept(this);
        int n3 = this.exprType;
        int n4 = this.arrayDim;
        if (this.invalidDim(n3, this.arrayDim, this.className, n2, n, string, true) || n3 == 344 || n2 == 344) {
            throw new CompileError("invalid cast");
        }
        if (n2 == 307) {
            if (!CodeGen.isRefType(n3) && n4 == 0) {
                throw new CompileError("invalid cast");
            }
            return CodeGen.toJvmArrayName(string, n);
        }
        if (n > 0) {
            return CodeGen.toJvmTypeName(n2, n);
        }
        return null;
    }

    void atNumCastExpr(int n, int n2) {
        if (n == n2) {
            return;
        }
        int n3 = CodeGen.typePrecedence(n);
        int n4 = CodeGen.typePrecedence(n2);
        int n5 = 0 <= n3 && n3 < 3 ? castOp[n3 * 4 + n4] : 0;
        int n6 = n2 == 312 ? 135 : (n2 == 317 ? 134 : (n2 == 326 ? 133 : (n2 == 334 ? 147 : (n2 == 306 ? 146 : (n2 == 303 ? 145 : 0)))));
        if (n5 != 0) {
            this.bytecode.addOpcode(n5);
        }
        if ((n5 == 0 || n5 == 136 || n5 == 139 || n5 == 142) && n6 != 0) {
            this.bytecode.addOpcode(n6);
        }
    }

    @Override
    public void atExpr(Expr expr) {
        int n = expr.getOperator();
        ASTree aSTree = expr.oprand1();
        if (n == 46) {
            String string = ((Symbol)expr.oprand2()).get();
            if (string.equals("class")) {
                this.atClassObject(expr);
            } else {
                this.atFieldRead(expr);
            }
        } else if (n == 35) {
            this.atFieldRead(expr);
        } else if (n == 65) {
            this.atArrayRead(aSTree, expr.oprand2());
        } else if (n == 362 || n == 363) {
            this.atPlusPlus(n, aSTree, expr, true);
        } else if (n == 33) {
            if (!this.booleanExpr(false, expr)) {
                this.bytecode.addIndex(7);
                this.bytecode.addIconst(1);
                this.bytecode.addOpcode(167);
                this.bytecode.addIndex(4);
            }
            this.bytecode.addIconst(0);
        } else if (n == 67) {
            CodeGen.fatal();
        } else {
            expr.oprand1().accept(this);
            int n2 = CodeGen.typePrecedence(this.exprType);
            if (this.arrayDim > 0) {
                CodeGen.badType(expr);
            }
            if (n == 45) {
                if (n2 == 0) {
                    this.bytecode.addOpcode(119);
                } else if (n2 == 1) {
                    this.bytecode.addOpcode(118);
                } else if (n2 == 2) {
                    this.bytecode.addOpcode(117);
                } else if (n2 == 3) {
                    this.bytecode.addOpcode(116);
                    this.exprType = 324;
                } else {
                    CodeGen.badType(expr);
                }
            } else if (n == 126) {
                if (n2 == 3) {
                    this.bytecode.addIconst(-1);
                    this.bytecode.addOpcode(130);
                    this.exprType = 324;
                } else if (n2 == 2) {
                    this.bytecode.addLconst(-1L);
                    this.bytecode.addOpcode(131);
                } else {
                    CodeGen.badType(expr);
                }
            } else if (n == 43) {
                if (n2 == -1) {
                    CodeGen.badType(expr);
                }
            } else {
                CodeGen.fatal();
            }
        }
    }

    protected static void badType(Expr expr) {
        throw new CompileError("invalid type for " + expr.getName());
    }

    @Override
    public abstract void atCallExpr(CallExpr var1);

    protected abstract void atFieldRead(ASTree var1);

    public void atClassObject(Expr expr) {
        ASTree aSTree = expr.oprand1();
        if (!(aSTree instanceof Symbol)) {
            throw new CompileError("fatal error: badly parsed .class expr");
        }
        String string = ((Symbol)aSTree).get();
        if (string.startsWith("[")) {
            String string2;
            String string3;
            int n = string.indexOf("[L");
            if (n >= 0 && !(string3 = string.substring(n + 2, string.length() - 1)).equals(string2 = this.resolveClassName(string3))) {
                string2 = MemberResolver.jvmToJavaName(string2);
                StringBuilder stringBuilder = new StringBuilder();
                while (n-- >= 0) {
                    stringBuilder.append('[');
                }
                stringBuilder.append('L').append(string2).append(';');
                string = stringBuilder.toString();
            }
        } else {
            string = this.resolveClassName(MemberResolver.javaToJvmName(string));
            string = MemberResolver.jvmToJavaName(string);
        }
        this.atClassObject2(string);
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = "java/lang/Class";
    }

    protected void atClassObject2(String string) {
        int n = this.bytecode.currentPc();
        this.bytecode.addLdc(string);
        this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        int n2 = this.bytecode.currentPc();
        this.bytecode.addOpcode(167);
        int n3 = this.bytecode.currentPc();
        this.bytecode.addIndex(0);
        this.bytecode.addExceptionHandler(n, n2, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
        this.bytecode.growStack(1);
        this.bytecode.addInvokestatic("me.zombie_striker.qav.hooks.worldguard.shaded.javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
        this.bytecode.addOpcode(191);
        this.bytecode.write16bit(n3, this.bytecode.currentPc() - n3 + 1);
    }

    public void atArrayRead(ASTree aSTree, ASTree aSTree2) {
        this.arrayAccess(aSTree, aSTree2);
        this.bytecode.addOpcode(CodeGen.getArrayReadOp(this.exprType, this.arrayDim));
    }

    protected void arrayAccess(ASTree aSTree, ASTree aSTree2) {
        aSTree.accept(this);
        int n = this.exprType;
        int n2 = this.arrayDim;
        if (n2 == 0) {
            throw new CompileError("bad array access");
        }
        String string = this.className;
        aSTree2.accept(this);
        if (CodeGen.typePrecedence(this.exprType) != 3 || this.arrayDim > 0) {
            throw new CompileError("bad array index");
        }
        this.exprType = n;
        this.arrayDim = n2 - 1;
        this.className = string;
    }

    protected static int getArrayReadOp(int n, int n2) {
        if (n2 > 0) {
            return 50;
        }
        switch (n) {
            case 312: {
                return 49;
            }
            case 317: {
                return 48;
            }
            case 326: {
                return 47;
            }
            case 324: {
                return 46;
            }
            case 334: {
                return 53;
            }
            case 306: {
                return 52;
            }
            case 301: 
            case 303: {
                return 51;
            }
        }
        return 50;
    }

    protected static int getArrayWriteOp(int n, int n2) {
        if (n2 > 0) {
            return 83;
        }
        switch (n) {
            case 312: {
                return 82;
            }
            case 317: {
                return 81;
            }
            case 326: {
                return 80;
            }
            case 324: {
                return 79;
            }
            case 334: {
                return 86;
            }
            case 306: {
                return 85;
            }
            case 301: 
            case 303: {
                return 84;
            }
        }
        return 83;
    }

    private void atPlusPlus(int n, ASTree aSTree, Expr expr, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = aSTree == null;
        if (bl2) {
            aSTree = expr.oprand2();
        }
        if (aSTree instanceof Variable) {
            Declarator declarator = ((Variable)aSTree).getDeclarator();
            int n2 = this.exprType = declarator.getType();
            this.arrayDim = declarator.getArrayDim();
            int n3 = this.getLocalVar(declarator);
            if (this.arrayDim > 0) {
                CodeGen.badType(expr);
            }
            if (n2 == 312) {
                this.bytecode.addDload(n3);
                if (bl && bl2) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addDconst(1.0);
                this.bytecode.addOpcode(n == 362 ? 99 : 103);
                if (bl && !bl2) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addDstore(n3);
            } else if (n2 == 326) {
                this.bytecode.addLload(n3);
                if (bl && bl2) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addLconst(1L);
                this.bytecode.addOpcode(n == 362 ? 97 : 101);
                if (bl && !bl2) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addLstore(n3);
            } else if (n2 == 317) {
                this.bytecode.addFload(n3);
                if (bl && bl2) {
                    this.bytecode.addOpcode(89);
                }
                this.bytecode.addFconst(1.0f);
                this.bytecode.addOpcode(n == 362 ? 98 : 102);
                if (bl && !bl2) {
                    this.bytecode.addOpcode(89);
                }
                this.bytecode.addFstore(n3);
            } else if (n2 == 303 || n2 == 306 || n2 == 334 || n2 == 324) {
                int n4;
                if (bl && bl2) {
                    this.bytecode.addIload(n3);
                }
                int n5 = n4 = n == 362 ? 1 : -1;
                if (n3 > 255) {
                    this.bytecode.addOpcode(196);
                    this.bytecode.addOpcode(132);
                    this.bytecode.addIndex(n3);
                    this.bytecode.addIndex(n4);
                } else {
                    this.bytecode.addOpcode(132);
                    this.bytecode.add(n3);
                    this.bytecode.add(n4);
                }
                if (bl && !bl2) {
                    this.bytecode.addIload(n3);
                }
            } else {
                CodeGen.badType(expr);
            }
        } else {
            Expr expr2;
            if (aSTree instanceof Expr && (expr2 = (Expr)aSTree).getOperator() == 65) {
                this.atArrayPlusPlus(n, bl2, expr2, bl);
                return;
            }
            this.atFieldPlusPlus(n, bl2, aSTree, expr, bl);
        }
    }

    public void atArrayPlusPlus(int n, boolean bl, Expr expr, boolean bl2) {
        this.arrayAccess(expr.oprand1(), expr.oprand2());
        int n2 = this.exprType;
        int n3 = this.arrayDim;
        if (n3 > 0) {
            CodeGen.badType(expr);
        }
        this.bytecode.addOpcode(92);
        this.bytecode.addOpcode(CodeGen.getArrayReadOp(n2, this.arrayDim));
        int n4 = CodeGen.is2word(n2, n3) ? 94 : 91;
        this.atPlusPlusCore(n4, bl2, n, bl, expr);
        this.bytecode.addOpcode(CodeGen.getArrayWriteOp(n2, n3));
    }

    protected void atPlusPlusCore(int n, boolean bl, int n2, boolean bl2, Expr expr) {
        int n3 = this.exprType;
        if (bl && bl2) {
            this.bytecode.addOpcode(n);
        }
        if (n3 == 324 || n3 == 303 || n3 == 306 || n3 == 334) {
            this.bytecode.addIconst(1);
            this.bytecode.addOpcode(n2 == 362 ? 96 : 100);
            this.exprType = 324;
        } else if (n3 == 326) {
            this.bytecode.addLconst(1L);
            this.bytecode.addOpcode(n2 == 362 ? 97 : 101);
        } else if (n3 == 317) {
            this.bytecode.addFconst(1.0f);
            this.bytecode.addOpcode(n2 == 362 ? 98 : 102);
        } else if (n3 == 312) {
            this.bytecode.addDconst(1.0);
            this.bytecode.addOpcode(n2 == 362 ? 99 : 103);
        } else {
            CodeGen.badType(expr);
        }
        if (bl && !bl2) {
            this.bytecode.addOpcode(n);
        }
    }

    protected abstract void atFieldPlusPlus(int var1, boolean var2, ASTree var3, Expr var4, boolean var5);

    @Override
    public abstract void atMember(Member var1);

    @Override
    public void atVariable(Variable variable) {
        Declarator declarator = variable.getDeclarator();
        this.exprType = declarator.getType();
        this.arrayDim = declarator.getArrayDim();
        this.className = declarator.getClassName();
        int n = this.getLocalVar(declarator);
        if (this.arrayDim > 0) {
            this.bytecode.addAload(n);
        } else {
            switch (this.exprType) {
                case 307: {
                    this.bytecode.addAload(n);
                    break;
                }
                case 326: {
                    this.bytecode.addLload(n);
                    break;
                }
                case 317: {
                    this.bytecode.addFload(n);
                    break;
                }
                case 312: {
                    this.bytecode.addDload(n);
                    break;
                }
                default: {
                    this.bytecode.addIload(n);
                }
            }
        }
    }

    @Override
    public void atKeyword(Keyword keyword) {
        this.arrayDim = 0;
        int n = keyword.get();
        switch (n) {
            case 410: {
                this.bytecode.addIconst(1);
                this.exprType = 301;
                break;
            }
            case 411: {
                this.bytecode.addIconst(0);
                this.exprType = 301;
                break;
            }
            case 412: {
                this.bytecode.addOpcode(1);
                this.exprType = 412;
                break;
            }
            case 336: 
            case 339: {
                if (this.inStaticMethod) {
                    throw new CompileError("not-available: " + (n == 339 ? "this" : "super"));
                }
                this.bytecode.addAload(0);
                this.exprType = 307;
                if (n == 339) {
                    this.className = this.getThisName();
                    break;
                }
                this.className = this.getSuperName();
                break;
            }
            default: {
                CodeGen.fatal();
            }
        }
    }

    @Override
    public void atStringL(StringL stringL) {
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
        this.bytecode.addLdc(stringL.get());
    }

    @Override
    public void atIntConst(IntConst intConst) {
        this.arrayDim = 0;
        long l = intConst.get();
        int n = intConst.getType();
        if (n == 402 || n == 401) {
            this.exprType = n == 402 ? 324 : 306;
            this.bytecode.addIconst((int)l);
        } else {
            this.exprType = 326;
            this.bytecode.addLconst(l);
        }
    }

    @Override
    public void atDoubleConst(DoubleConst doubleConst) {
        this.arrayDim = 0;
        if (doubleConst.getType() == 405) {
            this.exprType = 312;
            this.bytecode.addDconst(doubleConst.get());
        } else {
            this.exprType = 317;
            this.bytecode.addFconst((float)doubleConst.get());
        }
    }

    protected static abstract class ReturnHook {
        ReturnHook next;

        protected abstract boolean doit(Bytecode var1, int var2);

        protected ReturnHook(CodeGen codeGen) {
            this.next = codeGen.returnHooks;
            codeGen.returnHooks = this;
        }

        protected void remove(CodeGen codeGen) {
            codeGen.returnHooks = this.next;
        }
    }
}

