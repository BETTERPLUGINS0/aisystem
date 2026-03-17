/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class Variable
extends Symbol {
    private static final long serialVersionUID = 1L;
    protected Declarator declarator;

    public Variable(String string, Declarator declarator) {
        super(string);
        this.declarator = declarator;
    }

    public Declarator getDeclarator() {
        return this.declarator;
    }

    @Override
    public String toString() {
        return this.identifier + ":" + this.declarator.getType();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atVariable(this);
    }
}

