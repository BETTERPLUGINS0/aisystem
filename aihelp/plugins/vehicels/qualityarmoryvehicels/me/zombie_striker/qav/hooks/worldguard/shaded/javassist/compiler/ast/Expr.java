/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class Expr
extends ASTList
implements TokenId {
    private static final long serialVersionUID = 1L;
    protected int operatorId;

    Expr(int n, ASTree aSTree, ASTList aSTList) {
        super(aSTree, aSTList);
        this.operatorId = n;
    }

    Expr(int n, ASTree aSTree) {
        super(aSTree);
        this.operatorId = n;
    }

    public static Expr make(int n, ASTree aSTree, ASTree aSTree2) {
        return new Expr(n, aSTree, new ASTList(aSTree2));
    }

    public static Expr make(int n, ASTree aSTree) {
        return new Expr(n, aSTree);
    }

    public int getOperator() {
        return this.operatorId;
    }

    public void setOperator(int n) {
        this.operatorId = n;
    }

    public ASTree oprand1() {
        return this.getLeft();
    }

    public void setOprand1(ASTree aSTree) {
        this.setLeft(aSTree);
    }

    public ASTree oprand2() {
        return this.getRight().getLeft();
    }

    public void setOprand2(ASTree aSTree) {
        this.getRight().setLeft(aSTree);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atExpr(this);
    }

    public String getName() {
        int n = this.operatorId;
        if (n < 128) {
            return String.valueOf((char)n);
        }
        if (350 <= n && n <= 371) {
            return opNames[n - 350];
        }
        if (n == 323) {
            return "instanceof";
        }
        return String.valueOf(n);
    }

    @Override
    protected String getTag() {
        return "op:" + this.getName();
    }
}

