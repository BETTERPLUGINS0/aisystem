/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class Pair
extends ASTree {
    private static final long serialVersionUID = 1L;
    protected ASTree left;
    protected ASTree right;

    public Pair(ASTree aSTree, ASTree aSTree2) {
        this.left = aSTree;
        this.right = aSTree2;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atPair(this);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(<Pair> ");
        stringBuilder.append(this.left == null ? "<null>" : this.left.toString());
        stringBuilder.append(" . ");
        stringBuilder.append(this.right == null ? "<null>" : this.right.toString());
        stringBuilder.append(')');
        return stringBuilder.toString();
    }

    @Override
    public ASTree getLeft() {
        return this.left;
    }

    @Override
    public ASTree getRight() {
        return this.right;
    }

    @Override
    public void setLeft(ASTree aSTree) {
        this.left = aSTree;
    }

    @Override
    public void setRight(ASTree aSTree) {
        this.right = aSTree;
    }
}

