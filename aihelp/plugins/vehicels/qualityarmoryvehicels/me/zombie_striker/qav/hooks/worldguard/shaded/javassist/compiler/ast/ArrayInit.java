/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class ArrayInit
extends ASTList {
    private static final long serialVersionUID = 1L;

    public ArrayInit(ASTree aSTree) {
        super(aSTree);
    }

    public int size() {
        int n = this.length();
        if (n == 1 && this.head() == null) {
            return 0;
        }
        return n;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atArrayInit(this);
    }

    @Override
    public String getTag() {
        return "array";
    }
}

