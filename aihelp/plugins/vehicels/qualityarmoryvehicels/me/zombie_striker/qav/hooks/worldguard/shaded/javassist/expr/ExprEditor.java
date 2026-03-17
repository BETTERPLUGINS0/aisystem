/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CannotCompileException;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtClass;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.BadBytecode;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeAttribute;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.CodeIterator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.ExceptionTable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.bytecode.MethodInfo;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Cast;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.ConstructorCall;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.FieldAccess;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Handler;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.Instanceof;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.MethodCall;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.NewArray;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.expr.NewExpr;

public class ExprEditor {
    public boolean doit(CtClass ctClass, MethodInfo methodInfo) {
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        if (codeAttribute == null) {
            return false;
        }
        CodeIterator codeIterator = codeAttribute.iterator();
        boolean bl = false;
        LoopContext loopContext = new LoopContext(codeAttribute.getMaxLocals());
        while (codeIterator.hasNext()) {
            if (!this.loopBody(codeIterator, ctClass, methodInfo, loopContext)) continue;
            bl = true;
        }
        ExceptionTable exceptionTable = codeAttribute.getExceptionTable();
        int n = exceptionTable.size();
        for (int i = 0; i < n; ++i) {
            Handler handler = new Handler(exceptionTable, i, codeIterator, ctClass, methodInfo);
            this.edit(handler);
            if (!handler.edited()) continue;
            bl = true;
            loopContext.updateMax(handler.locals(), handler.stack());
        }
        if (codeAttribute.getMaxLocals() < loopContext.maxLocals) {
            codeAttribute.setMaxLocals(loopContext.maxLocals);
        }
        codeAttribute.setMaxStack(codeAttribute.getMaxStack() + loopContext.maxStack);
        try {
            if (bl) {
                methodInfo.rebuildStackMapIf6(ctClass.getClassPool(), ctClass.getClassFile2());
            }
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode.getMessage(), badBytecode);
        }
        return bl;
    }

    boolean doit(CtClass ctClass, MethodInfo methodInfo, LoopContext loopContext, CodeIterator codeIterator, int n) {
        boolean bl = false;
        while (codeIterator.hasNext() && codeIterator.lookAhead() < n) {
            int n2 = codeIterator.getCodeLength();
            if (!this.loopBody(codeIterator, ctClass, methodInfo, loopContext)) continue;
            bl = true;
            int n3 = codeIterator.getCodeLength();
            if (n2 == n3) continue;
            n += n3 - n2;
        }
        return bl;
    }

    final boolean loopBody(CodeIterator codeIterator, CtClass ctClass, MethodInfo methodInfo, LoopContext loopContext) {
        try {
            Expr expr = null;
            int n = codeIterator.next();
            int n2 = codeIterator.byteAt(n);
            if (n2 >= 178) {
                if (n2 < 188) {
                    if (n2 == 184 || n2 == 185 || n2 == 182) {
                        expr = new MethodCall(n, codeIterator, ctClass, methodInfo);
                        this.edit((MethodCall)expr);
                    } else if (n2 == 180 || n2 == 178 || n2 == 181 || n2 == 179) {
                        expr = new FieldAccess(n, codeIterator, ctClass, methodInfo, n2);
                        this.edit((FieldAccess)expr);
                    } else if (n2 == 187) {
                        int n3 = codeIterator.u16bitAt(n + 1);
                        loopContext.newList = new NewOp(loopContext.newList, n, methodInfo.getConstPool().getClassInfo(n3));
                    } else if (n2 == 183) {
                        NewOp newOp = loopContext.newList;
                        if (newOp != null && methodInfo.getConstPool().isConstructor(newOp.type, codeIterator.u16bitAt(n + 1)) > 0) {
                            expr = new NewExpr(n, codeIterator, ctClass, methodInfo, newOp.type, newOp.pos);
                            this.edit((NewExpr)expr);
                            loopContext.newList = newOp.next;
                        } else {
                            MethodCall methodCall = new MethodCall(n, codeIterator, ctClass, methodInfo);
                            if (methodCall.getMethodName().equals("<init>")) {
                                ConstructorCall constructorCall = new ConstructorCall(n, codeIterator, ctClass, methodInfo);
                                expr = constructorCall;
                                this.edit(constructorCall);
                            } else {
                                expr = methodCall;
                                this.edit(methodCall);
                            }
                        }
                    }
                } else if (n2 == 188 || n2 == 189 || n2 == 197) {
                    expr = new NewArray(n, codeIterator, ctClass, methodInfo, n2);
                    this.edit((NewArray)expr);
                } else if (n2 == 193) {
                    expr = new Instanceof(n, codeIterator, ctClass, methodInfo);
                    this.edit((Instanceof)expr);
                } else if (n2 == 192) {
                    expr = new Cast(n, codeIterator, ctClass, methodInfo);
                    this.edit((Cast)expr);
                }
            }
            if (expr != null && expr.edited()) {
                loopContext.updateMax(expr.locals(), expr.stack());
                return true;
            }
            return false;
        } catch (BadBytecode badBytecode) {
            throw new CannotCompileException(badBytecode);
        }
    }

    public void edit(NewExpr newExpr) {
    }

    public void edit(NewArray newArray) {
    }

    public void edit(MethodCall methodCall) {
    }

    public void edit(ConstructorCall constructorCall) {
    }

    public void edit(FieldAccess fieldAccess) {
    }

    public void edit(Instanceof instanceof_) {
    }

    public void edit(Cast cast) {
    }

    public void edit(Handler handler) {
    }

    static final class LoopContext {
        NewOp newList;
        int maxLocals;
        int maxStack;

        LoopContext(int n) {
            this.maxLocals = n;
            this.maxStack = 0;
            this.newList = null;
        }

        void updateMax(int n, int n2) {
            if (this.maxLocals < n) {
                this.maxLocals = n;
            }
            if (this.maxStack < n2) {
                this.maxStack = n2;
            }
        }
    }

    static final class NewOp {
        NewOp next;
        int pos;
        String type;

        NewOp(NewOp newOp, int n, String string) {
            this.next = newOp;
            this.pos = n;
            this.type = string;
        }
    }
}

