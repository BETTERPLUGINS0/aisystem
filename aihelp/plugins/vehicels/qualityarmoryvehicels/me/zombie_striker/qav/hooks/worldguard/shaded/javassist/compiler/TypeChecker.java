/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Opcode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.NoFieldException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;
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
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.InstanceOfExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.IntConst;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Keyword;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Member;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.NewExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.StringL;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Variable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class TypeChecker
extends Visitor
implements Opcode,
TokenId {
    static final String javaLangObject = "java.lang.Object";
    static final String jvmJavaLangObject = "java/lang/Object";
    static final String jvmJavaLangString = "java/lang/String";
    static final String jvmJavaLangClass = "java/lang/Class";
    protected int exprType;
    protected int arrayDim;
    protected String className;
    protected MemberResolver resolver;
    protected CtClass thisClass;
    protected MethodInfo thisMethod;

    public TypeChecker(CtClass ctClass, ClassPool classPool) {
        this.resolver = new MemberResolver(classPool);
        this.thisClass = ctClass;
        this.thisMethod = null;
    }

    protected static String argTypesToString(int[] nArray, int[] nArray2, String[] stringArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('(');
        int n = nArray.length;
        if (n > 0) {
            int n2 = 0;
            while (true) {
                TypeChecker.typeToString(stringBuilder, nArray[n2], nArray2[n2], stringArray[n2]);
                if (++n2 >= n) break;
                stringBuilder.append(',');
            }
        }
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    protected static StringBuilder typeToString(StringBuilder stringBuilder, int n, int n2, String string) {
        String string2;
        if (n == 307) {
            string2 = MemberResolver.jvmToJavaName(string);
        } else if (n == 412) {
            string2 = "Object";
        } else {
            try {
                string2 = MemberResolver.getTypeName(n);
            } catch (CompileError compileError) {
                string2 = "?";
            }
        }
        stringBuilder.append(string2);
        while (n2-- > 0) {
            stringBuilder.append("[]");
        }
        return stringBuilder;
    }

    public void setThisMethod(MethodInfo methodInfo) {
        this.thisMethod = methodInfo;
    }

    protected static void fatal() {
        throw new CompileError("fatal");
    }

    protected String getThisName() {
        return MemberResolver.javaToJvmName(this.thisClass.getName());
    }

    protected String getSuperName() {
        return MemberResolver.javaToJvmName(MemberResolver.getSuperclass(this.thisClass).getName());
    }

    protected String resolveClassName(ASTList aSTList) {
        return this.resolver.resolveClassName(aSTList);
    }

    protected String resolveClassName(String string) {
        return this.resolver.resolveJvmClassName(string);
    }

    @Override
    public void atNewExpr(NewExpr newExpr) {
        if (newExpr.isArray()) {
            this.atNewArrayExpr(newExpr);
        } else {
            CtClass ctClass = this.resolver.lookupClassByName(newExpr.getClassName());
            String string = ctClass.getName();
            ASTList aSTList = newExpr.getArguments();
            this.atMethodCallCore(ctClass, "<init>", aSTList);
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = MemberResolver.javaToJvmName(string);
        }
    }

    public void atNewArrayExpr(NewExpr newExpr) {
        int n = newExpr.getArrayType();
        ASTList aSTList = newExpr.getArraySize();
        ASTList aSTList2 = newExpr.getClassName();
        ArrayInit arrayInit = newExpr.getInitializer();
        if (arrayInit != null) {
            ((ASTree)arrayInit).accept(this);
        }
        if (aSTList.length() > 1) {
            this.atMultiNewArray(n, aSTList2, aSTList);
        } else {
            ASTree aSTree = aSTList.head();
            if (aSTree != null) {
                aSTree.accept(this);
            }
            this.exprType = n;
            this.arrayDim = 1;
            this.className = n == 307 ? this.resolveClassName(aSTList2) : null;
        }
    }

    @Override
    public void atArrayInit(ArrayInit arrayInit) {
        for (ASTList aSTList = arrayInit; aSTList != null; aSTList = aSTList.tail()) {
            ASTree aSTree = aSTList.head();
            if (aSTree == null) continue;
            aSTree.accept(this);
        }
    }

    protected void atMultiNewArray(int n, ASTList aSTList, ASTList aSTList2) {
        ASTree aSTree;
        int n2 = aSTList2.length();
        int n3 = 0;
        while (aSTList2 != null && (aSTree = aSTList2.head()) != null) {
            ++n3;
            aSTree.accept(this);
            aSTList2 = aSTList2.tail();
        }
        this.exprType = n;
        this.arrayDim = n2;
        this.className = n == 307 ? this.resolveClassName(aSTList) : null;
    }

    @Override
    public void atAssignExpr(AssignExpr assignExpr) {
        int n = assignExpr.getOperator();
        ASTree aSTree = assignExpr.oprand1();
        ASTree aSTree2 = assignExpr.oprand2();
        if (aSTree instanceof Variable) {
            this.atVariableAssign(assignExpr, n, (Variable)aSTree, ((Variable)aSTree).getDeclarator(), aSTree2);
        } else {
            Expr expr;
            if (aSTree instanceof Expr && (expr = (Expr)aSTree).getOperator() == 65) {
                this.atArrayAssign(assignExpr, n, (Expr)aSTree, aSTree2);
                return;
            }
            this.atFieldAssign(assignExpr, n, aSTree, aSTree2);
        }
    }

    private void atVariableAssign(Expr expr, int n, Variable variable, Declarator declarator, ASTree aSTree) {
        int n2 = declarator.getType();
        int n3 = declarator.getArrayDim();
        String string = declarator.getClassName();
        if (n != 61) {
            this.atVariable(variable);
        }
        aSTree.accept(this);
        this.exprType = n2;
        this.arrayDim = n3;
        this.className = string;
    }

    private void atArrayAssign(Expr expr, int n, Expr expr2, ASTree aSTree) {
        this.atArrayRead(expr2.oprand1(), expr2.oprand2());
        int n2 = this.exprType;
        int n3 = this.arrayDim;
        String string = this.className;
        aSTree.accept(this);
        this.exprType = n2;
        this.arrayDim = n3;
        this.className = string;
    }

    protected void atFieldAssign(Expr expr, int n, ASTree aSTree, ASTree aSTree2) {
        CtField ctField = this.fieldAccess(aSTree);
        this.atFieldRead(ctField);
        int n2 = this.exprType;
        int n3 = this.arrayDim;
        String string = this.className;
        aSTree2.accept(this);
        this.exprType = n2;
        this.arrayDim = n3;
        this.className = string;
    }

    @Override
    public void atCondExpr(CondExpr condExpr) {
        this.booleanExpr(condExpr.condExpr());
        condExpr.thenExpr().accept(this);
        int n = this.exprType;
        int n2 = this.arrayDim;
        String string = this.className;
        condExpr.elseExpr().accept(this);
        if (n2 == 0 && n2 == this.arrayDim) {
            if (CodeGen.rightIsStrong(n, this.exprType)) {
                condExpr.setThen(new CastExpr(this.exprType, 0, condExpr.thenExpr()));
            } else if (CodeGen.rightIsStrong(this.exprType, n)) {
                condExpr.setElse(new CastExpr(n, 0, condExpr.elseExpr()));
                this.exprType = n;
            }
        }
    }

    @Override
    public void atBinExpr(BinExpr binExpr) {
        int n = binExpr.getOperator();
        int n2 = CodeGen.lookupBinOp(n);
        if (n2 >= 0) {
            if (n == 43) {
                Expr expr = this.atPlusExpr(binExpr);
                if (expr != null) {
                    expr = CallExpr.makeCall(Expr.make(46, (ASTree)expr, (ASTree)new Member("toString")), null);
                    binExpr.setOprand1(expr);
                    binExpr.setOprand2(null);
                    this.className = jvmJavaLangString;
                }
            } else {
                ASTree aSTree = binExpr.oprand1();
                ASTree aSTree2 = binExpr.oprand2();
                aSTree.accept(this);
                int n3 = this.exprType;
                aSTree2.accept(this);
                if (!this.isConstant(binExpr, n, aSTree, aSTree2)) {
                    this.computeBinExprType(binExpr, n, n3);
                }
            }
        } else {
            this.booleanExpr(binExpr);
        }
    }

    private Expr atPlusExpr(BinExpr binExpr) {
        ASTree aSTree = binExpr.oprand1();
        ASTree aSTree2 = binExpr.oprand2();
        if (aSTree2 == null) {
            aSTree.accept(this);
            return null;
        }
        if (TypeChecker.isPlusExpr(aSTree)) {
            Expr expr = this.atPlusExpr((BinExpr)aSTree);
            if (expr != null) {
                aSTree2.accept(this);
                this.exprType = 307;
                this.arrayDim = 0;
                this.className = "java/lang/StringBuffer";
                return TypeChecker.makeAppendCall(expr, aSTree2);
            }
        } else {
            aSTree.accept(this);
        }
        int n = this.exprType;
        int n2 = this.arrayDim;
        String string = this.className;
        aSTree2.accept(this);
        if (this.isConstant(binExpr, 43, aSTree, aSTree2)) {
            return null;
        }
        if (n == 307 && n2 == 0 && jvmJavaLangString.equals(string) || this.exprType == 307 && this.arrayDim == 0 && jvmJavaLangString.equals(this.className)) {
            ASTList aSTList = ASTList.make(new Symbol("java"), new Symbol("lang"), new Symbol("StringBuffer"));
            NewExpr newExpr = new NewExpr(aSTList, null);
            this.exprType = 307;
            this.arrayDim = 0;
            this.className = "java/lang/StringBuffer";
            return TypeChecker.makeAppendCall(TypeChecker.makeAppendCall(newExpr, aSTree), aSTree2);
        }
        this.computeBinExprType(binExpr, 43, n);
        return null;
    }

    private boolean isConstant(BinExpr binExpr, int n, ASTree aSTree, ASTree aSTree2) {
        aSTree = TypeChecker.stripPlusExpr(aSTree);
        aSTree2 = TypeChecker.stripPlusExpr(aSTree2);
        ASTree aSTree3 = null;
        if (aSTree instanceof StringL && aSTree2 instanceof StringL && n == 43) {
            aSTree3 = new StringL(((StringL)aSTree).get() + ((StringL)aSTree2).get());
        } else if (aSTree instanceof IntConst) {
            aSTree3 = ((IntConst)aSTree).compute(n, aSTree2);
        } else if (aSTree instanceof DoubleConst) {
            aSTree3 = ((DoubleConst)aSTree).compute(n, aSTree2);
        }
        if (aSTree3 == null) {
            return false;
        }
        binExpr.setOperator(43);
        binExpr.setOprand1(aSTree3);
        binExpr.setOprand2(null);
        aSTree3.accept(this);
        return true;
    }

    static ASTree stripPlusExpr(ASTree aSTree) {
        ASTree aSTree2;
        if (aSTree instanceof BinExpr) {
            BinExpr binExpr = (BinExpr)aSTree;
            if (binExpr.getOperator() == 43 && binExpr.oprand2() == null) {
                return binExpr.getLeft();
            }
        } else if (aSTree instanceof Expr) {
            Expr expr = (Expr)aSTree;
            int n = expr.getOperator();
            if (n == 35) {
                ASTree aSTree3 = TypeChecker.getConstantFieldValue((Member)expr.oprand2());
                if (aSTree3 != null) {
                    return aSTree3;
                }
            } else if (n == 43 && expr.getRight() == null) {
                return expr.getLeft();
            }
        } else if (aSTree instanceof Member && (aSTree2 = TypeChecker.getConstantFieldValue((Member)aSTree)) != null) {
            return aSTree2;
        }
        return aSTree;
    }

    private static ASTree getConstantFieldValue(Member member) {
        return TypeChecker.getConstantFieldValue(member.getField());
    }

    public static ASTree getConstantFieldValue(CtField ctField) {
        if (ctField == null) {
            return null;
        }
        Object object = ctField.getConstantValue();
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return new StringL((String)object);
        }
        if (object instanceof Double || object instanceof Float) {
            int n = object instanceof Double ? 405 : 404;
            return new DoubleConst(((Number)object).doubleValue(), n);
        }
        if (object instanceof Number) {
            int n = object instanceof Long ? 403 : 402;
            return new IntConst(((Number)object).longValue(), n);
        }
        if (object instanceof Boolean) {
            return new Keyword((Boolean)object != false ? 410 : 411);
        }
        return null;
    }

    private static boolean isPlusExpr(ASTree aSTree) {
        if (aSTree instanceof BinExpr) {
            BinExpr binExpr = (BinExpr)aSTree;
            int n = binExpr.getOperator();
            return n == 43;
        }
        return false;
    }

    private static Expr makeAppendCall(ASTree aSTree, ASTree aSTree2) {
        return CallExpr.makeCall(Expr.make(46, aSTree, (ASTree)new Member("append")), new ASTList(aSTree2));
    }

    private void computeBinExprType(BinExpr binExpr, int n, int n2) {
        int n3 = this.exprType;
        if (n == 364 || n == 366 || n == 370) {
            this.exprType = n2;
        } else {
            this.insertCast(binExpr, n2, n3);
        }
        if (CodeGen.isP_INT(this.exprType) && this.exprType != 301) {
            this.exprType = 324;
        }
    }

    private void booleanExpr(ASTree aSTree) {
        int n = CodeGen.getCompOperator(aSTree);
        if (n == 358) {
            BinExpr binExpr = (BinExpr)aSTree;
            binExpr.oprand1().accept(this);
            int n2 = this.exprType;
            int n3 = this.arrayDim;
            binExpr.oprand2().accept(this);
            if (n3 == 0 && this.arrayDim == 0) {
                this.insertCast(binExpr, n2, this.exprType);
            }
        } else if (n == 33) {
            ((Expr)aSTree).oprand1().accept(this);
        } else if (n == 369 || n == 368) {
            BinExpr binExpr = (BinExpr)aSTree;
            binExpr.oprand1().accept(this);
            binExpr.oprand2().accept(this);
        } else {
            aSTree.accept(this);
        }
        this.exprType = 301;
        this.arrayDim = 0;
    }

    private void insertCast(BinExpr binExpr, int n, int n2) {
        if (CodeGen.rightIsStrong(n, n2)) {
            binExpr.setLeft(new CastExpr(n2, 0, binExpr.oprand1()));
        } else {
            this.exprType = n;
        }
    }

    @Override
    public void atCastExpr(CastExpr castExpr) {
        String string = this.resolveClassName(castExpr.getClassName());
        castExpr.getOprand().accept(this);
        this.exprType = castExpr.getType();
        this.arrayDim = castExpr.getArrayDim();
        this.className = string;
    }

    @Override
    public void atInstanceOfExpr(InstanceOfExpr instanceOfExpr) {
        instanceOfExpr.getOprand().accept(this);
        this.exprType = 301;
        this.arrayDim = 0;
    }

    @Override
    public void atExpr(Expr expr) {
        int n = expr.getOperator();
        ASTree aSTree = expr.oprand1();
        if (n == 46) {
            String string = ((Symbol)expr.oprand2()).get();
            if (string.equals("length")) {
                try {
                    this.atArrayLength(expr);
                } catch (NoFieldException noFieldException) {
                    this.atFieldRead(expr);
                }
            } else if (string.equals("class")) {
                this.atClassObject(expr);
            } else {
                this.atFieldRead(expr);
            }
        } else if (n == 35) {
            String string = ((Symbol)expr.oprand2()).get();
            if (string.equals("class")) {
                this.atClassObject(expr);
            } else {
                this.atFieldRead(expr);
            }
        } else if (n == 65) {
            this.atArrayRead(aSTree, expr.oprand2());
        } else if (n == 362 || n == 363) {
            this.atPlusPlus(n, aSTree, expr);
        } else if (n == 33) {
            this.booleanExpr(expr);
        } else if (n == 67) {
            TypeChecker.fatal();
        } else {
            aSTree.accept(this);
            if (!this.isConstant(expr, n, aSTree) && (n == 45 || n == 126) && CodeGen.isP_INT(this.exprType)) {
                this.exprType = 324;
            }
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private boolean isConstant(Expr expr, int n, ASTree aSTree) {
        if ((aSTree = TypeChecker.stripPlusExpr(aSTree)) instanceof IntConst) {
            IntConst intConst = (IntConst)aSTree;
            long l = intConst.get();
            if (n == 45) {
                l = -l;
            } else {
                if (n != 126) return false;
                l ^= 0xFFFFFFFFFFFFFFFFL;
            }
            intConst.set(l);
        } else {
            if (!(aSTree instanceof DoubleConst)) return false;
            DoubleConst doubleConst = (DoubleConst)aSTree;
            if (n != 45) return false;
            doubleConst.set(-doubleConst.get());
        }
        expr.setOperator(43);
        return true;
    }

    @Override
    public void atCallExpr(CallExpr callExpr) {
        Object object;
        String string = null;
        CtClass ctClass = null;
        ASTree aSTree = callExpr.oprand1();
        ASTList aSTList = (ASTList)callExpr.oprand2();
        if (aSTree instanceof Member) {
            string = ((Member)aSTree).get();
            ctClass = this.thisClass;
        } else if (aSTree instanceof Keyword) {
            string = "<init>";
            ctClass = ((Keyword)aSTree).get() == 336 ? MemberResolver.getSuperclass(this.thisClass) : this.thisClass;
        } else if (aSTree instanceof Expr) {
            object = (Expr)aSTree;
            string = ((Symbol)((Expr)object).oprand2()).get();
            int n = ((Expr)object).getOperator();
            if (n == 35) {
                ctClass = this.resolver.lookupClass(((Symbol)((Expr)object).oprand1()).get(), false);
            } else if (n == 46) {
                ASTree aSTree2 = ((Expr)object).oprand1();
                String string2 = TypeChecker.isDotSuper(aSTree2);
                if (string2 != null) {
                    ctClass = MemberResolver.getSuperInterface(this.thisClass, string2);
                } else {
                    try {
                        aSTree2.accept(this);
                    } catch (NoFieldException noFieldException) {
                        if (noFieldException.getExpr() != aSTree2) {
                            throw noFieldException;
                        }
                        this.exprType = 307;
                        this.arrayDim = 0;
                        this.className = noFieldException.getField();
                        ((Expr)object).setOperator(35);
                        ((Expr)object).setOprand1(new Symbol(MemberResolver.jvmToJavaName(this.className)));
                    }
                    if (this.arrayDim > 0) {
                        ctClass = this.resolver.lookupClass(javaLangObject, true);
                    } else if (this.exprType == 307) {
                        ctClass = this.resolver.lookupClassByJvmName(this.className);
                    } else {
                        TypeChecker.badMethod();
                    }
                }
            } else {
                TypeChecker.badMethod();
            }
        } else {
            TypeChecker.fatal();
        }
        object = this.atMethodCallCore(ctClass, string, aSTList);
        callExpr.setMethod((MemberResolver.Method)object);
    }

    private static void badMethod() {
        throw new CompileError("bad method");
    }

    static String isDotSuper(ASTree aSTree) {
        ASTree aSTree2;
        Expr expr;
        if (aSTree instanceof Expr && (expr = (Expr)aSTree).getOperator() == 46 && (aSTree2 = expr.oprand2()) instanceof Keyword && ((Keyword)aSTree2).get() == 336) {
            return ((Symbol)expr.oprand1()).get();
        }
        return null;
    }

    public MemberResolver.Method atMethodCallCore(CtClass ctClass, String string, ASTList aSTList) {
        int n = this.getMethodArgsLength(aSTList);
        int[] nArray = new int[n];
        int[] nArray2 = new int[n];
        String[] stringArray = new String[n];
        this.atMethodArgs(aSTList, nArray, nArray2, stringArray);
        MemberResolver.Method method = this.resolver.lookupMethod(ctClass, this.thisClass, this.thisMethod, string, nArray, nArray2, stringArray);
        if (method == null) {
            String string2 = ctClass.getName();
            String string3 = TypeChecker.argTypesToString(nArray, nArray2, stringArray);
            String string4 = string.equals("<init>") ? "cannot find constructor " + string2 + string3 : string + string3 + " not found in " + string2;
            throw new CompileError(string4);
        }
        String string5 = method.info.getDescriptor();
        this.setReturnType(string5);
        return method;
    }

    public int getMethodArgsLength(ASTList aSTList) {
        return ASTList.length(aSTList);
    }

    public void atMethodArgs(ASTList aSTList, int[] nArray, int[] nArray2, String[] stringArray) {
        int n = 0;
        while (aSTList != null) {
            ASTree aSTree = aSTList.head();
            aSTree.accept(this);
            nArray[n] = this.exprType;
            nArray2[n] = this.arrayDim;
            stringArray[n] = this.className;
            ++n;
            aSTList = aSTList.tail();
        }
    }

    void setReturnType(String string) {
        int n = string.indexOf(41);
        if (n < 0) {
            TypeChecker.badMethod();
        }
        char c = string.charAt(++n);
        int n2 = 0;
        while (c == '[') {
            ++n2;
            c = string.charAt(++n);
        }
        this.arrayDim = n2;
        if (c == 'L') {
            int n3 = string.indexOf(59, n + 1);
            if (n3 < 0) {
                TypeChecker.badMethod();
            }
            this.exprType = 307;
            this.className = string.substring(n + 1, n3);
        } else {
            this.exprType = MemberResolver.descToType(c);
            this.className = null;
        }
    }

    private void atFieldRead(ASTree aSTree) {
        this.atFieldRead(this.fieldAccess(aSTree));
    }

    private void atFieldRead(CtField ctField) {
        FieldInfo fieldInfo = ctField.getFieldInfo2();
        String string = fieldInfo.getDescriptor();
        int n = 0;
        int n2 = 0;
        char c = string.charAt(n);
        while (c == '[') {
            ++n2;
            c = string.charAt(++n);
        }
        this.arrayDim = n2;
        this.exprType = MemberResolver.descToType(c);
        this.className = c == 'L' ? string.substring(n + 1, string.indexOf(59, n + 1)) : null;
    }

    protected CtField fieldAccess(ASTree aSTree) {
        if (aSTree instanceof Member) {
            Member member = (Member)aSTree;
            String string = member.get();
            try {
                CtField ctField = this.thisClass.getField(string);
                if (Modifier.isStatic(ctField.getModifiers())) {
                    member.setField(ctField);
                }
                return ctField;
            } catch (NotFoundException notFoundException) {
                throw new NoFieldException(string, aSTree);
            }
        }
        if (aSTree instanceof Expr) {
            Expr expr = (Expr)aSTree;
            int n = expr.getOperator();
            if (n == 35) {
                Member member = (Member)expr.oprand2();
                CtField ctField = this.resolver.lookupField(((Symbol)expr.oprand1()).get(), member);
                member.setField(ctField);
                return ctField;
            }
            if (n == 46) {
                try {
                    expr.oprand1().accept(this);
                } catch (NoFieldException noFieldException) {
                    if (noFieldException.getExpr() != expr.oprand1()) {
                        throw noFieldException;
                    }
                    return this.fieldAccess2(expr, noFieldException.getField());
                }
                CompileError compileError = null;
                try {
                    if (this.exprType == 307 && this.arrayDim == 0) {
                        return this.resolver.lookupFieldByJvmName(this.className, (Symbol)expr.oprand2());
                    }
                } catch (CompileError compileError2) {
                    compileError = compileError2;
                }
                ASTree aSTree2 = expr.oprand1();
                if (aSTree2 instanceof Symbol) {
                    return this.fieldAccess2(expr, ((Symbol)aSTree2).get());
                }
                if (compileError != null) {
                    throw compileError;
                }
            }
        }
        throw new CompileError("bad field access");
    }

    private CtField fieldAccess2(Expr expr, String string) {
        Member member = (Member)expr.oprand2();
        CtField ctField = this.resolver.lookupFieldByJvmName2(string, member, expr);
        expr.setOperator(35);
        expr.setOprand1(new Symbol(MemberResolver.jvmToJavaName(string)));
        member.setField(ctField);
        return ctField;
    }

    public void atClassObject(Expr expr) {
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = jvmJavaLangClass;
    }

    public void atArrayLength(Expr expr) {
        expr.oprand1().accept(this);
        if (this.arrayDim == 0) {
            throw new NoFieldException("length", expr);
        }
        this.exprType = 324;
        this.arrayDim = 0;
    }

    public void atArrayRead(ASTree aSTree, ASTree aSTree2) {
        aSTree.accept(this);
        int n = this.exprType;
        int n2 = this.arrayDim;
        String string = this.className;
        aSTree2.accept(this);
        this.exprType = n;
        this.arrayDim = n2 - 1;
        this.className = string;
    }

    private void atPlusPlus(int n, ASTree aSTree, Expr expr) {
        boolean bl;
        boolean bl2 = bl = aSTree == null;
        if (bl) {
            aSTree = expr.oprand2();
        }
        if (aSTree instanceof Variable) {
            Declarator declarator = ((Variable)aSTree).getDeclarator();
            this.exprType = declarator.getType();
            this.arrayDim = declarator.getArrayDim();
        } else {
            Expr expr2;
            if (aSTree instanceof Expr && (expr2 = (Expr)aSTree).getOperator() == 65) {
                this.atArrayRead(expr2.oprand1(), expr2.oprand2());
                int n2 = this.exprType;
                if (n2 == 324 || n2 == 303 || n2 == 306 || n2 == 334) {
                    this.exprType = 324;
                }
                return;
            }
            this.atFieldPlusPlus(aSTree);
        }
    }

    protected void atFieldPlusPlus(ASTree aSTree) {
        CtField ctField = this.fieldAccess(aSTree);
        this.atFieldRead(ctField);
        int n = this.exprType;
        if (n == 324 || n == 303 || n == 306 || n == 334) {
            this.exprType = 324;
        }
    }

    @Override
    public void atMember(Member member) {
        this.atFieldRead(member);
    }

    @Override
    public void atVariable(Variable variable) {
        Declarator declarator = variable.getDeclarator();
        this.exprType = declarator.getType();
        this.arrayDim = declarator.getArrayDim();
        this.className = declarator.getClassName();
    }

    @Override
    public void atKeyword(Keyword keyword) {
        this.arrayDim = 0;
        int n = keyword.get();
        switch (n) {
            case 410: 
            case 411: {
                this.exprType = 301;
                break;
            }
            case 412: {
                this.exprType = 412;
                break;
            }
            case 336: 
            case 339: {
                this.exprType = 307;
                if (n == 339) {
                    this.className = this.getThisName();
                    break;
                }
                this.className = this.getSuperName();
                break;
            }
            default: {
                TypeChecker.fatal();
            }
        }
    }

    @Override
    public void atStringL(StringL stringL) {
        this.exprType = 307;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
    }

    @Override
    public void atIntConst(IntConst intConst) {
        this.arrayDim = 0;
        int n = intConst.getType();
        this.exprType = n == 402 || n == 401 ? (n == 402 ? 324 : 306) : 326;
    }

    @Override
    public void atDoubleConst(DoubleConst doubleConst) {
        this.arrayDim = 0;
        this.exprType = doubleConst.getType() == 405 ? 312 : 317;
    }
}

