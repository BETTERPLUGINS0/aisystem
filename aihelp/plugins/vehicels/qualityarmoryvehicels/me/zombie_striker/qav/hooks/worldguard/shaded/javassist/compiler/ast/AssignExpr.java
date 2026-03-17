/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Expr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class AssignExpr
extends Expr {
    private static final long serialVersionUID = 1L;

    private AssignExpr(int n, ASTree aSTree, ASTList aSTList) {
        super(n, aSTree, aSTList);
    }

    public static AssignExpr makeAssign(int n, ASTree aSTree, ASTree aSTree2) {
        return new AssignExpr(n, aSTree, new ASTList(aSTree2));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atAssignExpr(this);
    }
}

