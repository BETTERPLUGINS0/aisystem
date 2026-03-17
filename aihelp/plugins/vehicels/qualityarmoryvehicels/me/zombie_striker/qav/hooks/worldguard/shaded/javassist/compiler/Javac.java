/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtBehavior;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtConstructor;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMember;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtMethod;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtPrimitiveType;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.Modifier;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.NotFoundException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.Bytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.LocalVariableAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstCodeGen;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.JvstTypeChecker;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Lex;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.Parser;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ProceedHandler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.SymbolTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CallExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.FieldDecl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Member;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.MethodDecl;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Stmnt;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;

public class Javac {
    JvstCodeGen gen;
    SymbolTable stable;
    private Bytecode bytecode;
    public static final String param0Name = "$0";
    public static final String resultVarName = "$_";
    public static final String proceedName = "$proceed";

    public Javac(CtClass ctClass) {
        this(new Bytecode(ctClass.getClassFile2().getConstPool(), 0, 0), ctClass);
    }

    public Javac(Bytecode bytecode, CtClass ctClass) {
        this.gen = new JvstCodeGen(bytecode, ctClass, ctClass.getClassPool());
        this.stable = new SymbolTable();
        this.bytecode = bytecode;
    }

    public Bytecode getBytecode() {
        return this.bytecode;
    }

    public CtMember compile(String string) {
        Parser parser = new Parser(new Lex(string));
        ASTList aSTList = parser.parseMember1(this.stable);
        try {
            if (aSTList instanceof FieldDecl) {
                return this.compileField((FieldDecl)aSTList);
            }
            CtBehavior ctBehavior = this.compileMethod(parser, (MethodDecl)aSTList);
            CtClass ctClass = ctBehavior.getDeclaringClass();
            ctBehavior.getMethodInfo2().rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
            return ctBehavior;
        } catch (BadBytecode badBytecode) {
            throw new CompileError(badBytecode.getMessage());
        } catch (CannotCompileException cannotCompileException) {
            throw new CompileError(cannotCompileException.getMessage());
        }
    }

    private CtField compileField(FieldDecl fieldDecl) {
        Declarator declarator = fieldDecl.getDeclarator();
        CtFieldWithInit ctFieldWithInit = new CtFieldWithInit(this.gen.resolver.lookupClass(declarator), declarator.getVariable().get(), this.gen.getThisClass());
        ctFieldWithInit.setModifiers(MemberResolver.getModifiers(fieldDecl.getModifiers()));
        if (fieldDecl.getInit() != null) {
            ctFieldWithInit.setInit(fieldDecl.getInit());
        }
        return ctFieldWithInit;
    }

    private CtBehavior compileMethod(Parser parser, MethodDecl methodDecl) {
        int n = MemberResolver.getModifiers(methodDecl.getModifiers());
        CtClass[] ctClassArray = this.gen.makeParamList(methodDecl);
        CtClass[] ctClassArray2 = this.gen.makeThrowsList(methodDecl);
        this.recordParams(ctClassArray, Modifier.isStatic(n));
        methodDecl = parser.parseMethod2(this.stable, methodDecl);
        try {
            if (methodDecl.isConstructor()) {
                CtConstructor ctConstructor = new CtConstructor(ctClassArray, this.gen.getThisClass());
                ctConstructor.setModifiers(n);
                methodDecl.accept(this.gen);
                ctConstructor.getMethodInfo().setCodeAttribute(this.bytecode.toCodeAttribute());
                ctConstructor.setExceptionTypes(ctClassArray2);
                return ctConstructor;
            }
            Declarator declarator = methodDecl.getReturn();
            CtClass ctClass = this.gen.resolver.lookupClass(declarator);
            this.recordReturnType(ctClass, false);
            CtMethod ctMethod = new CtMethod(ctClass, declarator.getVariable().get(), ctClassArray, this.gen.getThisClass());
            ctMethod.setModifiers(n);
            this.gen.setThisMethod(ctMethod);
            methodDecl.accept(this.gen);
            if (methodDecl.getBody() != null) {
                ctMethod.getMethodInfo().setCodeAttribute(this.bytecode.toCodeAttribute());
            } else {
                ctMethod.setModifiers(n | 0x400);
            }
            ctMethod.setExceptionTypes(ctClassArray2);
            return ctMethod;
        } catch (NotFoundException notFoundException) {
            throw new CompileError(notFoundException.toString());
        }
    }

    public Bytecode compileBody(CtBehavior ctBehavior, String string) {
        try {
            boolean bl;
            CtClass ctClass;
            int n = ctBehavior.getModifiers();
            this.recordParams(ctBehavior.getParameterTypes(), Modifier.isStatic(n));
            if (ctBehavior instanceof CtMethod) {
                this.gen.setThisMethod((CtMethod)ctBehavior);
                ctClass = ((CtMethod)ctBehavior).getReturnType();
            } else {
                ctClass = CtClass.voidType;
            }
            this.recordReturnType(ctClass, false);
            boolean bl2 = bl = ctClass == CtClass.voidType;
            if (string == null) {
                Javac.makeDefaultBody(this.bytecode, ctClass);
            } else {
                Parser parser = new Parser(new Lex(string));
                SymbolTable symbolTable = new SymbolTable(this.stable);
                Stmnt stmnt = parser.parseStatement(symbolTable);
                if (parser.hasMore()) {
                    throw new CompileError("the method/constructor body must be surrounded by {}");
                }
                boolean bl3 = false;
                if (ctBehavior instanceof CtConstructor) {
                    bl3 = !((CtConstructor)ctBehavior).isClassInitializer();
                }
                this.gen.atMethodBody(stmnt, bl3, bl);
            }
            return this.bytecode;
        } catch (NotFoundException notFoundException) {
            throw new CompileError(notFoundException.toString());
        }
    }

    private static void makeDefaultBody(Bytecode bytecode, CtClass ctClass) {
        int n;
        int n2;
        if (ctClass instanceof CtPrimitiveType) {
            CtPrimitiveType ctPrimitiveType = (CtPrimitiveType)ctClass;
            n2 = ctPrimitiveType.getReturnOp();
            n = n2 == 175 ? 14 : (n2 == 174 ? 11 : (n2 == 173 ? 9 : (n2 == 177 ? 0 : 3)));
        } else {
            n2 = 176;
            n = 1;
        }
        if (n != 0) {
            bytecode.addOpcode(n);
        }
        bytecode.addOpcode(n2);
    }

    public boolean recordLocalVariables(CodeAttribute codeAttribute, int n) {
        LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
        if (localVariableAttribute == null) {
            return false;
        }
        int n2 = localVariableAttribute.tableLength();
        for (int i = 0; i < n2; ++i) {
            int n3 = localVariableAttribute.startPc(i);
            int n4 = localVariableAttribute.codeLength(i);
            if (n3 > n || n >= n3 + n4) continue;
            this.gen.recordVariable(localVariableAttribute.descriptor(i), localVariableAttribute.variableName(i), localVariableAttribute.index(i), this.stable);
        }
        return true;
    }

    public boolean recordParamNames(CodeAttribute codeAttribute, int n) {
        LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute)codeAttribute.getAttribute("LocalVariableTable");
        if (localVariableAttribute == null) {
            return false;
        }
        int n2 = localVariableAttribute.tableLength();
        for (int i = 0; i < n2; ++i) {
            int n3 = localVariableAttribute.index(i);
            if (n3 >= n) continue;
            this.gen.recordVariable(localVariableAttribute.descriptor(i), localVariableAttribute.variableName(i), n3, this.stable);
        }
        return true;
    }

    public int recordParams(CtClass[] ctClassArray, boolean bl) {
        return this.gen.recordParams(ctClassArray, bl, "$", "$args", "$$", this.stable);
    }

    public int recordParams(String string, CtClass[] ctClassArray, boolean bl, int n, boolean bl2) {
        return this.gen.recordParams(ctClassArray, bl2, "$", "$args", "$$", bl, n, string, this.stable);
    }

    public void setMaxLocals(int n) {
        this.gen.setMaxLocals(n);
    }

    public int recordReturnType(CtClass ctClass, boolean bl) {
        this.gen.recordType(ctClass);
        return this.gen.recordReturnType(ctClass, "$r", bl ? resultVarName : null, this.stable);
    }

    public void recordType(CtClass ctClass) {
        this.gen.recordType(ctClass);
    }

    public int recordVariable(CtClass ctClass, String string) {
        return this.gen.recordVariable(ctClass, string, this.stable);
    }

    public void recordProceed(String string, String string2) {
        Parser parser = new Parser(new Lex(string));
        final ASTree aSTree = parser.parseExpression(this.stable);
        final String string3 = string2;
        ProceedHandler proceedHandler = new ProceedHandler(){

            @Override
            public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
                ASTree aSTree2 = new Member(string3);
                if (aSTree != null) {
                    aSTree2 = Expr.make(46, aSTree, aSTree2);
                }
                aSTree2 = CallExpr.makeCall(aSTree2, aSTList);
                jvstCodeGen.compileExpr(aSTree2);
                jvstCodeGen.addNullIfVoid();
            }

            @Override
            public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
                ASTree aSTree2 = new Member(string3);
                if (aSTree != null) {
                    aSTree2 = Expr.make(46, aSTree, aSTree2);
                }
                aSTree2 = CallExpr.makeCall(aSTree2, aSTList);
                ((ASTree)aSTree2).accept(jvstTypeChecker);
                jvstTypeChecker.addNullIfVoid();
            }
        };
        this.gen.setProceedHandler(proceedHandler, proceedName);
    }

    public void recordStaticProceed(String string, String string2) {
        final String string3 = string;
        final String string4 = string2;
        ProceedHandler proceedHandler = new ProceedHandler(){

            @Override
            public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
                Expr expr = Expr.make(35, (ASTree)new Symbol(string3), (ASTree)new Member(string4));
                expr = CallExpr.makeCall(expr, aSTList);
                jvstCodeGen.compileExpr(expr);
                jvstCodeGen.addNullIfVoid();
            }

            @Override
            public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
                Expr expr = Expr.make(35, (ASTree)new Symbol(string3), (ASTree)new Member(string4));
                expr = CallExpr.makeCall(expr, aSTList);
                expr.accept(jvstTypeChecker);
                jvstTypeChecker.addNullIfVoid();
            }
        };
        this.gen.setProceedHandler(proceedHandler, proceedName);
    }

    public void recordSpecialProceed(String string, final String string2, final String string3, final String string4, final int n) {
        Parser parser = new Parser(new Lex(string));
        final ASTree aSTree = parser.parseExpression(this.stable);
        ProceedHandler proceedHandler = new ProceedHandler(){

            @Override
            public void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) {
                jvstCodeGen.compileInvokeSpecial(aSTree, n, string4, aSTList);
            }

            @Override
            public void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) {
                jvstTypeChecker.compileInvokeSpecial(aSTree, string2, string3, string4, aSTList);
            }
        };
        this.gen.setProceedHandler(proceedHandler, proceedName);
    }

    public void recordProceed(ProceedHandler proceedHandler) {
        this.gen.setProceedHandler(proceedHandler, proceedName);
    }

    public void compileStmnt(String string) {
        Parser parser = new Parser(new Lex(string));
        SymbolTable symbolTable = new SymbolTable(this.stable);
        while (parser.hasMore()) {
            Stmnt stmnt = parser.parseStatement(symbolTable);
            if (stmnt == null) continue;
            stmnt.accept(this.gen);
        }
    }

    public void compileExpr(String string) {
        ASTree aSTree = Javac.parseExpr(string, this.stable);
        this.compileExpr(aSTree);
    }

    public static ASTree parseExpr(String string, SymbolTable symbolTable) {
        Parser parser = new Parser(new Lex(string));
        return parser.parseExpression(symbolTable);
    }

    public void compileExpr(ASTree aSTree) {
        if (aSTree != null) {
            this.gen.compileExpr(aSTree);
        }
    }

    public static class CtFieldWithInit
    extends CtField {
        private ASTree init = null;

        CtFieldWithInit(CtClass ctClass, String string, CtClass ctClass2) {
            super(ctClass, string, ctClass2);
        }

        protected void setInit(ASTree aSTree) {
            this.init = aSTree;
        }

        @Override
        protected ASTree getInitAST() {
            return this.init;
        }
    }
}

