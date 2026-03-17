/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class Stmnt
extends ASTList
implements TokenId {
    private static final long serialVersionUID = 1L;
    protected int operatorId;

    public Stmnt(int n, ASTree aSTree, ASTList aSTList) {
        super(aSTree, aSTList);
        this.operatorId = n;
    }

    public Stmnt(int n, ASTree aSTree) {
        super(aSTree);
        this.operatorId = n;
    }

    public Stmnt(int n) {
        this(n, null);
    }

    public static Stmnt make(int n, ASTree aSTree, ASTree aSTree2) {
        return new Stmnt(n, aSTree, new ASTList(aSTree2));
    }

    public static Stmnt make(int n, ASTree aSTree, ASTree aSTree2, ASTree aSTree3) {
        return new Stmnt(n, aSTree, new ASTList(aSTree2, new ASTList(aSTree3)));
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atStmnt(this);
    }

    public int getOperator() {
        return this.operatorId;
    }

    @Override
    protected String getTag() {
        if (this.operatorId < 128) {
            return "stmnt:" + (char)this.operatorId;
        }
        return "stmnt:" + this.operatorId;
    }
}

