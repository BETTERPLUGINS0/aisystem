/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class CondExpr
extends ASTList {
    private static final long serialVersionUID = 1L;

    public CondExpr(ASTree aSTree, ASTree aSTree2, ASTree aSTree3) {
        super(aSTree, new ASTList(aSTree2, new ASTList(aSTree3)));
    }

    public ASTree condExpr() {
        return this.head();
    }

    public void setCond(ASTree aSTree) {
        this.setHead(aSTree);
    }

    public ASTree thenExpr() {
        return this.tail().head();
    }

    public void setThen(ASTree aSTree) {
        this.tail().setHead(aSTree);
    }

    public ASTree elseExpr() {
        return this.tail().tail().head();
    }

    public void setElse(ASTree aSTree) {
        this.tail().tail().setHead(aSTree);
    }

    @Override
    public String getTag() {
        return "?:";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atCondExpr(this);
    }
}

