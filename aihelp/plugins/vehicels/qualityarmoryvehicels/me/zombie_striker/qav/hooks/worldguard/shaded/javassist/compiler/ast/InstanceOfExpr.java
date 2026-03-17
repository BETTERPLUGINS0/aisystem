/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.CastExpr;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class InstanceOfExpr
extends CastExpr {
    private static final long serialVersionUID = 1L;

    public InstanceOfExpr(ASTList aSTList, int n, ASTree aSTree) {
        super(aSTList, n, aSTree);
    }

    public InstanceOfExpr(int n, int n2, ASTree aSTree) {
        super(n, n2, aSTree);
    }

    @Override
    public String getTag() {
        return "instanceof:" + this.castType + ":" + this.arrayDim;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atInstanceOfExpr(this);
    }
}

