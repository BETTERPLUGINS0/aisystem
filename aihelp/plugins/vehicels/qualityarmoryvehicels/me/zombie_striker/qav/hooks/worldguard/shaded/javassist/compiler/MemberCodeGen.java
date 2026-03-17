/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import java.util.ArrayList;
import java.util.List;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.ClassPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.AccessFlag;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ClassFile;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ConstPool;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Descriptor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.FieldInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.AccessorMaker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.NoFieldException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ArrayInit;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CallExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Keyword;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Member;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.MethodDecl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.NewExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Pair;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Stmnt;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;

public class MemberCodeGen
extends CodeGen {
    protected MemberResolver resolver;
    protected CtClass thisClass;
    protected MethodInfo thisMethod;
    protected boolean resultStatic;

    public MemberCodeGen(Bytecode bytecode, CtClass ctClass, ClassPool classPool) {
        super(bytecode);
        this.resolver = new MemberResolver(classPool);
        this.thisClass = ctClass;
        this.thisMethod = null;
    }

    public int getMajorVersion() {
        ClassFile classFile = this.thisClass.getClassFile2();
        if (classFile == null) {
            return ClassFile.MAJOR_VERSION;
        }
        return classFile.getMajorVersion();
    }

    public void setThisMethod(CtMethod ctMethod) {
        this.thisMethod = ctMethod.getMethodInfo2();
        if (this.typeChecker != null) {
            this.typeChecker.setThisMethod(this.thisMethod);
        }
    }

    public CtClass getThisClass() {
        return this.thisClass;
    }

    @Override
    protected String getThisName() {
        return MemberResolver.javaToJvmName(this.thisClass.getName());
    }

    @Override
    protected String getSuperName() {
        return MemberResolver.javaToJvmName(MemberResolver.getSuperclass(this.thisClass).getName());
    }

    @Override
    protected void insertDefaultSuperCall() {
        this.bytecode.addAload(0);
        this.bytecode.addInvokespecial(MemberResolver.getSuperclass(this.thisClass), "<init>", "()V");
    }

    @Override
    protected void atTryStmnt(Stmnt stmnt) {
        boolean bl;
        Bytecode bytecode = this.bytecode;
        Stmnt stmnt2 = (Stmnt)stmnt.getLeft();
        if (stmnt2 == null) {
            return;
        }
        ASTList aSTList = (ASTList)stmnt.getRight().getLeft();
        Stmnt stmnt3 = (Stmnt)stmnt.getRight().getRight().getLeft();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        JsrHook jsrHook = null;
        if (stmnt3 != null) {
            jsrHook = new JsrHook(this);
        }
        int n = bytecode.currentPc();
        stmnt2.accept(this);
        int n2 = bytecode.currentPc();
        if (n == n2) {
            throw new CompileError("empty try block");
        }
        boolean bl2 = bl = !this.hasReturned;
        if (bl) {
            bytecode.addOpcode(167);
            arrayList.add(bytecode.currentPc());
            bytecode.addIndex(0);
        }
        int n3 = this.getMaxLocals();
        this.incMaxLocals(1);
        while (aSTList != null) {
            Pair pair = (Pair)aSTList.head();
            aSTList = aSTList.tail();
            Declarator declarator = (Declarator)pair.getLeft();
            Stmnt stmnt4 = (Stmnt)pair.getRight();
            declarator.setLocalVar(n3);
            CtClass ctClass = this.resolver.lookupClassByJvmName(declarator.getClassName());
            declarator.setClassName(MemberResolver.javaToJvmName(ctClass.getName()));
            bytecode.addExceptionHandler(n, n2, bytecode.currentPc(), ctClass);
            bytecode.growStack(1);
            bytecode.addAstore(n3);
            this.hasReturned = false;
            if (stmnt4 != null) {
                stmnt4.accept(this);
            }
            if (this.hasReturned) continue;
            bytecode.addOpcode(167);
            arrayList.add(bytecode.currentPc());
            bytecode.addIndex(0);
            bl = true;
        }
        if (stmnt3 != null) {
            jsrHook.remove(this);
            int n4 = bytecode.currentPc();
            bytecode.addExceptionHandler(n, n4, n4, 0);
            bytecode.growStack(1);
            bytecode.addAstore(n3);
            this.hasReturned = false;
            stmnt3.accept(this);
            if (!this.hasReturned) {
                bytecode.addAload(n3);
                bytecode.addOpcode(191);
            }
            this.addFinally(jsrHook.jsrList, stmnt3);
        }
        int n5 = bytecode.currentPc();
        this.patchGoto(arrayList, n5);
        boolean bl3 = this.hasReturned = !bl;
        if (stmnt3 != null && bl) {
            stmnt3.accept(this);
        }
    }

    private void addFinally(List<int[]> list, Stmnt stmnt) {
        Bytecode bytecode = this.bytecode;
        for (int[] nArray : list) {
            int n = nArray[0];
            bytecode.write16bit(n, bytecode.currentPc() - n + 1);
            JsrHook2 jsrHook2 = new JsrHook2(this, nArray);
            stmnt.accept(this);
            jsrHook2.remove(this);
            if (this.hasReturned) continue;
            bytecode.addOpcode(167);
            bytecode.addIndex(n + 3 - bytecode.currentPc());
        }
    }

    @Override
    public void atNewExpr(NewExpr newExpr) {
        if (newExpr.isArray()) {
            this.atNewArrayExpr(newExpr);
        } else {
            CtClass ctClass = this.resolver.lookupClassByName(newExpr.getClassName());
            String string = ctClass.getName();
            ASTList aSTList = newExpr.getArguments();
            this.bytecode.addNew(string);
            this.bytecode.addOpcode(89);
            this.atMethodCallCore(ctClass, "<init>", aSTList, false, true, -1, null);
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
        if (aSTList.length() > 1) {
            if (arrayInit != null) {
                throw new CompileError("sorry, multi-dimensional array initializer for new is not supported");
            }
            this.atMultiNewArray(n, aSTList2, aSTList);
            return;
        }
        ASTree aSTree = aSTList.head();
        this.atNewArrayExpr2(n, aSTree, Declarator.astToClassName(aSTList2, '/'), arrayInit);
    }

    private void atNewArrayExpr2(int n, ASTree aSTree, String string, ArrayInit arrayInit) {
        int n2;
        String string2;
        if (arrayInit == null) {
            if (aSTree == null) {
                throw new CompileError("no array size");
            }
            aSTree.accept(this);
        } else if (aSTree == null) {
            int n3 = arrayInit.size();
            this.bytecode.addIconst(n3);
        } else {
            throw new CompileError("unnecessary array size specified for new");
        }
        if (n == 307) {
            string2 = this.resolveClassName(string);
            this.bytecode.addAnewarray(MemberResolver.jvmToJavaName(string2));
        } else {
            string2 = null;
            n2 = 0;
            switch (n) {
                case 301: {
                    n2 = 4;
                    break;
                }
                case 306: {
                    n2 = 5;
                    break;
                }
                case 317: {
                    n2 = 6;
                    break;
                }
                case 312: {
                    n2 = 7;
                    break;
                }
                case 303: {
                    n2 = 8;
                    break;
                }
                case 334: {
                    n2 = 9;
                    break;
                }
                case 324: {
                    n2 = 10;
                    break;
                }
                case 326: {
                    n2 = 11;
                    break;
                }
                default: {
                    MemberCodeGen.badNewExpr();
                }
            }
            this.bytecode.addOpcode(188);
            this.bytecode.add(n2);
        }
        if (arrayInit != null) {
            n2 = arrayInit.size();
            ASTList aSTList = arrayInit;
            for (int i = 0; i < n2; ++i) {
                this.bytecode.addOpcode(89);
                this.bytecode.addIconst(i);
                aSTList.head().accept(this);
                if (!MemberCodeGen.isRefType(n)) {
                    this.atNumCastExpr(this.exprType, n);
                }
                this.bytecode.addOpcode(MemberCodeGen.getArrayWriteOp(n, 0));
                aSTList = aSTList.tail();
            }
        }
        this.exprType = n;
        this.arrayDim = 1;
        this.className = string2;
    }

    private static void badNewExpr() {
        throw new CompileError("bad new expression");
    }

    @Override
    protected void atArrayVariableAssign(ArrayInit arrayInit, int n, int n2, String string) {
        this.atNewArrayExpr2(n, null, string, arrayInit);
    }

    @Override
    public void atArrayInit(ArrayInit arrayInit) {
        throw new CompileError("array initializer is not supported");
    }

    protected void atMultiNewArray(int n, ASTList aSTList, ASTList aSTList2) {
        Object object;
        int n2 = aSTList2.length();
        int n3 = 0;
        while (aSTList2 != null && (object = aSTList2.head()) != null) {
            ++n3;
            ((ASTree)object).accept(this);
            if (this.exprType != 324) {
                throw new CompileError("bad type for array size");
            }
            aSTList2 = aSTList2.tail();
        }
        this.exprType = n;
        this.arrayDim = n2;
        if (n == 307) {
            this.className = this.resolveClassName(aSTList);
            object = MemberCodeGen.toJvmArrayName(this.className, n2);
        } else {
            object = MemberCodeGen.toJvmTypeName(n, n2);
        }
        this.bytecode.addMultiNewarray((String)object, n3);
    }

    @Override
    public void atCallExpr(CallExpr callExpr) {
        String string = null;
        CtClass ctClass = null;
        ASTree aSTree = callExpr.oprand1();
        ASTList aSTList = (ASTList)callExpr.oprand2();
        boolean bl = false;
        boolean bl2 = false;
        int n = -1;
        MemberResolver.Method method = callExpr.getMethod();
        if (aSTree instanceof Member) {
            string = ((Member)aSTree).get();
            ctClass = this.thisClass;
            if (this.inStaticMethod || method != null && method.isStatic()) {
                bl = true;
            } else {
                n = this.bytecode.currentPc();
                this.bytecode.addAload(0);
            }
        } else if (aSTree instanceof Keyword) {
            bl2 = true;
            string = "<init>";
            ctClass = this.thisClass;
            if (this.inStaticMethod) {
                throw new CompileError("a constructor cannot be static");
            }
            this.bytecode.addAload(0);
            if (((Keyword)aSTree).get() == 336) {
                ctClass = MemberResolver.getSuperclass(ctClass);
            }
        } else if (aSTree instanceof Expr) {
            Expr expr = (Expr)aSTree;
            string = ((Symbol)expr.oprand2()).get();
            int n2 = expr.getOperator();
            if (n2 == 35) {
                ctClass = this.resolver.lookupClass(((Symbol)expr.oprand1()).get(), false);
                bl = true;
            } else if (n2 == 46) {
                ASTree aSTree2 = expr.oprand1();
                String string2 = TypeChecker.isDotSuper(aSTree2);
                if (string2 != null) {
                    bl2 = true;
                    ctClass = MemberResolver.getSuperInterface(this.thisClass, string2);
                    if (this.inStaticMethod || method != null && method.isStatic()) {
                        bl = true;
                    } else {
                        n = this.bytecode.currentPc();
                        this.bytecode.addAload(0);
                    }
                } else {
                    if (aSTree2 instanceof Keyword && ((Keyword)aSTree2).get() == 336) {
                        bl2 = true;
                    }
                    try {
                        aSTree2.accept(this);
                    } catch (NoFieldException noFieldException) {
                        if (noFieldException.getExpr() != aSTree2) {
                            throw noFieldException;
                        }
                        this.exprType = 307;
                        this.arrayDim = 0;
                        this.className = noFieldException.getField();
                        bl = true;
                    }
                    if (this.arrayDim > 0) {
                        ctClass = this.resolver.lookupClass("java.lang.Object", true);
                    } else if (this.exprType == 307) {
                        ctClass = this.resolver.lookupClassByJvmName(this.className);
                    } else {
                        MemberCodeGen.badMethod();
                    }
                }
            } else {
                MemberCodeGen.badMethod();
            }
        } else {
            MemberCodeGen.fatal();
        }
        this.atMethodCallCore(ctClass, string, aSTList, bl, bl2, n, method);
    }

    private static void badMethod() {
        throw new CompileError("bad method");
    }

    public void atMethodCallCore(CtClass ctClass, String string, ASTList aSTList, boolean bl, boolean bl2, int n, MemberResolver.Method method) {
        int n2 = this.getMethodArgsLength(aSTList);
        int[] nArray = new int[n2];
        int[] nArray2 = new int[n2];
        String[] stringArray = new String[n2];
        if (!bl && method != null && method.isStatic()) {
            this.bytecode.addOpcode(87);
            bl = true;
        }
        int n3 = this.bytecode.getStackDepth();
        this.atMethodArgs(aSTList, nArray, nArray2, stringArray);
        if (method == null) {
            method = this.resolver.lookupMethod(ctClass, this.thisClass, this.thisMethod, string, nArray, nArray2, stringArray);
        }
        if (method == null) {
            String string2 = string.equals("<init>") ? "constructor not found" : "Method " + string + " not found in " + ctClass.getName();
            throw new CompileError(string2);
        }
        this.atMethodCallCore2(ctClass, string, bl, bl2, n, method);
    }

    private boolean isFromSameDeclaringClass(CtClass ctClass, CtClass ctClass2) {
        try {
            while (ctClass != null) {
                if (this.isEnclosing(ctClass, ctClass2)) {
                    return true;
                }
                ctClass = ctClass.getDeclaringClass();
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return false;
    }

    private void atMethodCallCore2(CtClass ctClass, String string, boolean bl, boolean bl2, int n, MemberResolver.Method method) {
        CtClass ctClass2 = method.declaring;
        MethodInfo methodInfo = method.info;
        String string2 = methodInfo.getDescriptor();
        int n2 = methodInfo.getAccessFlags();
        if (string.equals("<init>")) {
            bl2 = true;
            if (ctClass2 != ctClass) {
                throw new CompileError("no such constructor: " + ctClass.getName());
            }
            if (ctClass2 != this.thisClass && AccessFlag.isPrivate(n2) && (ctClass2.getClassFile().getMajorVersion() < 52 || !this.isFromSameDeclaringClass(ctClass2, this.thisClass))) {
                string2 = this.getAccessibleConstructor(string2, ctClass2, methodInfo);
                this.bytecode.addOpcode(1);
            }
        } else if (AccessFlag.isPrivate(n2)) {
            if (ctClass2 == this.thisClass) {
                bl2 = true;
            } else {
                bl2 = false;
                bl = true;
                String string3 = string2;
                if ((n2 & 8) == 0) {
                    string2 = Descriptor.insertParameter(ctClass2.getName(), string3);
                }
                n2 = AccessFlag.setPackage(n2) | 8;
                string = this.getAccessiblePrivate(string, string3, string2, methodInfo, ctClass2);
            }
        }
        boolean bl3 = false;
        if ((n2 & 8) != 0) {
            if (!bl) {
                bl = true;
                if (n >= 0) {
                    this.bytecode.write(n, 0);
                } else {
                    bl3 = true;
                }
            }
            this.bytecode.addInvokestatic(ctClass2, string, string2);
        } else if (bl2) {
            this.bytecode.addInvokespecial(ctClass, string, string2);
        } else {
            if (!Modifier.isPublic(ctClass2.getModifiers()) || ctClass2.isInterface() != ctClass.isInterface()) {
                ctClass2 = ctClass;
            }
            if (ctClass2.isInterface()) {
                int n3 = Descriptor.paramSize(string2) + 1;
                this.bytecode.addInvokeinterface(ctClass2, string, string2, n3);
            } else {
                if (bl) {
                    throw new CompileError(string + " is not static");
                }
                this.bytecode.addInvokevirtual(ctClass2, string, string2);
            }
        }
        this.setReturnType(string2, bl, bl3);
    }

    protected String getAccessiblePrivate(String string, String string2, String string3, MethodInfo methodInfo, CtClass ctClass) {
        AccessorMaker accessorMaker;
        if (this.isEnclosing(ctClass, this.thisClass) && (accessorMaker = ctClass.getAccessorMaker()) != null) {
            return accessorMaker.getMethodAccessor(string, string2, string3, methodInfo);
        }
        throw new CompileError("Method " + string + " is private");
    }

    protected String getAccessibleConstructor(String string, CtClass ctClass, MethodInfo methodInfo) {
        AccessorMaker accessorMaker;
        if (this.isEnclosing(ctClass, this.thisClass) && (accessorMaker = ctClass.getAccessorMaker()) != null) {
            return accessorMaker.getConstructor(ctClass, string, methodInfo);
        }
        throw new CompileError("the called constructor is private in " + ctClass.getName());
    }

    private boolean isEnclosing(CtClass ctClass, CtClass ctClass2) {
        try {
            while (ctClass2 != null) {
                if ((ctClass2 = ctClass2.getDeclaringClass()) != ctClass) continue;
                return true;
            }
        } catch (NotFoundException notFoundException) {
            // empty catch block
        }
        return false;
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

    void setReturnType(String string, boolean bl, boolean bl2) {
        int n;
        int n2 = string.indexOf(41);
        if (n2 < 0) {
            MemberCodeGen.badMethod();
        }
        char c = string.charAt(++n2);
        int n3 = 0;
        while (c == '[') {
            ++n3;
            c = string.charAt(++n2);
        }
        this.arrayDim = n3;
        if (c == 'L') {
            n = string.indexOf(59, n2 + 1);
            if (n < 0) {
                MemberCodeGen.badMethod();
            }
            this.exprType = 307;
            this.className = string.substring(n2 + 1, n);
        } else {
            this.exprType = MemberResolver.descToType(c);
            this.className = null;
        }
        n = this.exprType;
        if (bl && bl2) {
            if (MemberCodeGen.is2word(n, n3)) {
                this.bytecode.addOpcode(93);
                this.bytecode.addOpcode(88);
                this.bytecode.addOpcode(87);
            } else if (n == 344) {
                this.bytecode.addOpcode(87);
            } else {
                this.bytecode.addOpcode(95);
                this.bytecode.addOpcode(87);
            }
        }
    }

    @Override
    protected void atFieldAssign(Expr expr, int n, ASTree aSTree, ASTree aSTree2, boolean bl) {
        int n2;
        CtField ctField = this.fieldAccess(aSTree, false);
        boolean bl2 = this.resultStatic;
        if (n != 61 && !bl2) {
            this.bytecode.addOpcode(89);
        }
        if (n == 61) {
            FieldInfo fieldInfo = ctField.getFieldInfo2();
            this.setFieldType(fieldInfo);
            AccessorMaker accessorMaker = this.isAccessibleField(ctField, fieldInfo);
            n2 = accessorMaker == null ? this.addFieldrefInfo(ctField, fieldInfo) : 0;
        } else {
            n2 = this.atFieldRead(ctField, bl2);
        }
        int n3 = this.exprType;
        int n4 = this.arrayDim;
        String string = this.className;
        this.atAssignCore(expr, n, aSTree2, n3, n4, string);
        boolean bl3 = MemberCodeGen.is2word(n3, n4);
        if (bl) {
            int n5 = bl2 ? (bl3 ? 92 : 89) : (bl3 ? 93 : 90);
            this.bytecode.addOpcode(n5);
        }
        this.atFieldAssignCore(ctField, bl2, n2, bl3);
        this.exprType = n3;
        this.arrayDim = n4;
        this.className = string;
    }

    private void atFieldAssignCore(CtField ctField, boolean bl, int n, boolean bl2) {
        if (n != 0) {
            if (bl) {
                this.bytecode.add(179);
                this.bytecode.growStack(bl2 ? -2 : -1);
            } else {
                this.bytecode.add(181);
                this.bytecode.growStack(bl2 ? -3 : -2);
            }
            this.bytecode.addIndex(n);
        } else {
            CtClass ctClass = ctField.getDeclaringClass();
            AccessorMaker accessorMaker = ctClass.getAccessorMaker();
            FieldInfo fieldInfo = ctField.getFieldInfo2();
            MethodInfo methodInfo = accessorMaker.getFieldSetter(fieldInfo, bl);
            this.bytecode.addInvokestatic(ctClass, methodInfo.getName(), methodInfo.getDescriptor());
        }
    }

    @Override
    public void atMember(Member member) {
        this.atFieldRead(member);
    }

    @Override
    protected void atFieldRead(ASTree aSTree) {
        CtField ctField = this.fieldAccess(aSTree, true);
        if (ctField == null) {
            this.atArrayLength(aSTree);
            return;
        }
        boolean bl = this.resultStatic;
        ASTree aSTree2 = TypeChecker.getConstantFieldValue(ctField);
        if (aSTree2 == null) {
            this.atFieldRead(ctField, bl);
        } else {
            aSTree2.accept(this);
            this.setFieldType(ctField.getFieldInfo2());
        }
    }

    private void atArrayLength(ASTree aSTree) {
        if (this.arrayDim == 0) {
            throw new CompileError(".length applied to a non array");
        }
        this.bytecode.addOpcode(190);
        this.exprType = 324;
        this.arrayDim = 0;
    }

    private int atFieldRead(CtField ctField, boolean bl) {
        FieldInfo fieldInfo = ctField.getFieldInfo2();
        boolean bl2 = this.setFieldType(fieldInfo);
        AccessorMaker accessorMaker = this.isAccessibleField(ctField, fieldInfo);
        if (accessorMaker != null) {
            MethodInfo methodInfo = accessorMaker.getFieldGetter(fieldInfo, bl);
            this.bytecode.addInvokestatic(ctField.getDeclaringClass(), methodInfo.getName(), methodInfo.getDescriptor());
            return 0;
        }
        int n = this.addFieldrefInfo(ctField, fieldInfo);
        if (bl) {
            this.bytecode.add(178);
            this.bytecode.growStack(bl2 ? 2 : 1);
        } else {
            this.bytecode.add(180);
            this.bytecode.growStack(bl2 ? 1 : 0);
        }
        this.bytecode.addIndex(n);
        return n;
    }

    private AccessorMaker isAccessibleField(CtField ctField, FieldInfo fieldInfo) {
        if (AccessFlag.isPrivate(fieldInfo.getAccessFlags()) && ctField.getDeclaringClass() != this.thisClass) {
            AccessorMaker accessorMaker;
            CtClass ctClass = ctField.getDeclaringClass();
            if (this.isEnclosing(ctClass, this.thisClass) && (accessorMaker = ctClass.getAccessorMaker()) != null) {
                return accessorMaker;
            }
            throw new CompileError("Field " + ctField.getName() + " in " + ctClass.getName() + " is private.");
        }
        return null;
    }

    private boolean setFieldType(FieldInfo fieldInfo) {
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
        boolean bl = n2 == 0 && (c == 'J' || c == 'D');
        return bl;
    }

    private int addFieldrefInfo(CtField ctField, FieldInfo fieldInfo) {
        ConstPool constPool = this.bytecode.getConstPool();
        String string = ctField.getDeclaringClass().getName();
        int n = constPool.addClassInfo(string);
        String string2 = fieldInfo.getName();
        String string3 = fieldInfo.getDescriptor();
        return constPool.addFieldrefInfo(n, string2, string3);
    }

    @Override
    protected void atClassObject2(String string) {
        if (this.getMajorVersion() < 49) {
            super.atClassObject2(string);
        } else {
            this.bytecode.addLdc(this.bytecode.getConstPool().addClassInfo(string));
        }
    }

    @Override
    protected void atFieldPlusPlus(int n, boolean bl, ASTree aSTree, Expr expr, boolean bl2) {
        CtField ctField = this.fieldAccess(aSTree, false);
        boolean bl3 = this.resultStatic;
        if (!bl3) {
            this.bytecode.addOpcode(89);
        }
        int n2 = this.atFieldRead(ctField, bl3);
        int n3 = this.exprType;
        boolean bl4 = MemberCodeGen.is2word(n3, this.arrayDim);
        int n4 = bl3 ? (bl4 ? 92 : 89) : (bl4 ? 93 : 90);
        this.atPlusPlusCore(n4, bl2, n, bl, expr);
        this.atFieldAssignCore(ctField, bl3, n2, bl4);
    }

    protected CtField fieldAccess(ASTree aSTree, boolean bl) {
        if (aSTree instanceof Member) {
            String string = ((Member)aSTree).get();
            CtField ctField = null;
            try {
                ctField = this.thisClass.getField(string);
            } catch (NotFoundException notFoundException) {
                throw new NoFieldException(string, aSTree);
            }
            boolean bl2 = Modifier.isStatic(ctField.getModifiers());
            if (!bl2) {
                if (this.inStaticMethod) {
                    throw new CompileError("not available in a static method: " + string);
                }
                this.bytecode.addAload(0);
            }
            this.resultStatic = bl2;
            return ctField;
        }
        if (aSTree instanceof Expr) {
            Expr expr = (Expr)aSTree;
            int n = expr.getOperator();
            if (n == 35) {
                CtField ctField = this.resolver.lookupField(((Symbol)expr.oprand1()).get(), (Symbol)expr.oprand2());
                this.resultStatic = true;
                return ctField;
            }
            if (n == 46) {
                CtField ctField = null;
                try {
                    expr.oprand1().accept(this);
                    if (this.exprType == 307 && this.arrayDim == 0) {
                        ctField = this.resolver.lookupFieldByJvmName(this.className, (Symbol)expr.oprand2());
                    } else {
                        if (bl && this.arrayDim > 0 && ((Symbol)expr.oprand2()).get().equals("length")) {
                            return null;
                        }
                        MemberCodeGen.badLvalue();
                    }
                    boolean bl3 = Modifier.isStatic(ctField.getModifiers());
                    if (bl3) {
                        this.bytecode.addOpcode(87);
                    }
                    this.resultStatic = bl3;
                    return ctField;
                } catch (NoFieldException noFieldException) {
                    if (noFieldException.getExpr() != expr.oprand1()) {
                        throw noFieldException;
                    }
                    Symbol symbol = (Symbol)expr.oprand2();
                    String string = noFieldException.getField();
                    ctField = this.resolver.lookupFieldByJvmName2(string, symbol, aSTree);
                    this.resultStatic = true;
                    return ctField;
                }
            }
            MemberCodeGen.badLvalue();
        } else {
            MemberCodeGen.badLvalue();
        }
        this.resultStatic = false;
        return null;
    }

    private static void badLvalue() {
        throw new CompileError("bad l-value");
    }

    public CtClass[] makeParamList(MethodDecl methodDecl) {
        CtClass[] ctClassArray;
        ASTList aSTList = methodDecl.getParams();
        if (aSTList == null) {
            ctClassArray = new CtClass[]{};
        } else {
            int n = 0;
            ctClassArray = new CtClass[aSTList.length()];
            while (aSTList != null) {
                ctClassArray[n++] = this.resolver.lookupClass((Declarator)aSTList.head());
                aSTList = aSTList.tail();
            }
        }
        return ctClassArray;
    }

    public CtClass[] makeThrowsList(MethodDecl methodDecl) {
        ASTList aSTList = methodDecl.getThrows();
        if (aSTList == null) {
            return null;
        }
        int n = 0;
        CtClass[] ctClassArray = new CtClass[aSTList.length()];
        while (aSTList != null) {
            ctClassArray[n++] = this.resolver.lookupClassByName((ASTList)aSTList.head());
            aSTList = aSTList.tail();
        }
        return ctClassArray;
    }

    @Override
    protected String resolveClassName(ASTList aSTList) {
        return this.resolver.resolveClassName(aSTList);
    }

    @Override
    protected String resolveClassName(String string) {
        return this.resolver.resolveJvmClassName(string);
    }

    static class JsrHook
    extends CodeGen.ReturnHook {
        List<int[]> jsrList = new ArrayList<int[]>();
        CodeGen cgen;
        int var;

        JsrHook(CodeGen codeGen) {
            super(codeGen);
            this.cgen = codeGen;
            this.var = -1;
        }

        private int getVar(int n) {
            if (this.var < 0) {
                this.var = this.cgen.getMaxLocals();
                this.cgen.incMaxLocals(n);
            }
            return this.var;
        }

        private void jsrJmp(Bytecode bytecode) {
            bytecode.addOpcode(167);
            this.jsrList.add(new int[]{bytecode.currentPc(), this.var});
            bytecode.addIndex(0);
        }

        @Override
        protected boolean doit(Bytecode bytecode, int n) {
            switch (n) {
                case 177: {
                    this.jsrJmp(bytecode);
                    break;
                }
                case 176: {
                    bytecode.addAstore(this.getVar(1));
                    this.jsrJmp(bytecode);
                    bytecode.addAload(this.var);
                    break;
                }
                case 172: {
                    bytecode.addIstore(this.getVar(1));
                    this.jsrJmp(bytecode);
                    bytecode.addIload(this.var);
                    break;
                }
                case 173: {
                    bytecode.addLstore(this.getVar(2));
                    this.jsrJmp(bytecode);
                    bytecode.addLload(this.var);
                    break;
                }
                case 175: {
                    bytecode.addDstore(this.getVar(2));
                    this.jsrJmp(bytecode);
                    bytecode.addDload(this.var);
                    break;
                }
                case 174: {
                    bytecode.addFstore(this.getVar(1));
                    this.jsrJmp(bytecode);
                    bytecode.addFload(this.var);
                    break;
                }
                default: {
                    throw new RuntimeException("fatal");
                }
            }
            return false;
        }
    }

    static class JsrHook2
    extends CodeGen.ReturnHook {
        int var;
        int target;

        JsrHook2(CodeGen codeGen, int[] nArray) {
            super(codeGen);
            this.target = nArray[0];
            this.var = nArray[1];
        }

        @Override
        protected boolean doit(Bytecode bytecode, int n) {
            switch (n) {
                case 177: {
                    break;
                }
                case 176: {
                    bytecode.addAstore(this.var);
                    break;
                }
                case 172: {
                    bytecode.addIstore(this.var);
                    break;
                }
                case 173: {
                    bytecode.addLstore(this.var);
                    break;
                }
                case 175: {
                    bytecode.addDstore(this.var);
                    break;
                }
                case 174: {
                    bytecode.addFstore(this.var);
                    break;
                }
                default: {
                    throw new RuntimeException("fatal");
                }
            }
            bytecode.addOpcode(167);
            bytecode.addIndex(this.target - bytecode.currentPc() + 3);
            return true;
        }
    }
}

