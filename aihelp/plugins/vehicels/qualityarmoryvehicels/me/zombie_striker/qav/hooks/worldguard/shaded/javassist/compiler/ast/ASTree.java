/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import java.io.Serializable;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public abstract class ASTree
implements Serializable {
    private static final long serialVersionUID = 1L;

    public ASTree getLeft() {
        return null;
    }

    public ASTree getRight() {
        return null;
    }

    public void setLeft(ASTree aSTree) {
    }

    public void setRight(ASTree aSTree) {
    }

    public abstract void accept(Visitor var1);

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('<');
        stringBuilder.append(this.getTag());
        stringBuilder.append('>');
        return stringBuilder.toString();
    }

    protected String getTag() {
        String string = this.getClass().getName();
        return string.substring(string.lastIndexOf(46) + 1);
    }
}

