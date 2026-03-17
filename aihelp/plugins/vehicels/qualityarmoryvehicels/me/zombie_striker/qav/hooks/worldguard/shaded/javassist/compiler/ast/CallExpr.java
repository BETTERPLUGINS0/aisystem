/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.MemberResolver;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class CallExpr
extends Expr {
    private static final long serialVersionUID = 1L;
    private MemberResolver.Method method = null;

    private CallExpr(ASTree aSTree, ASTList aSTList) {
        super(67, aSTree, aSTList);
    }

    public void setMethod(MemberResolver.Method method) {
        this.method = method;
    }

    public MemberResolver.Method getMethod() {
        return this.method;
    }

    public static CallExpr makeCall(ASTree aSTree, ASTree aSTree2) {
        return new CallExpr(aSTree, new ASTList(aSTree2));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atCallExpr(this);
    }
}

