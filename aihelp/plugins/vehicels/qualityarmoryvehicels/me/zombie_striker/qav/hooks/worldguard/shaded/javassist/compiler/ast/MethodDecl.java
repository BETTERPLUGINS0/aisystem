/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Declarator;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Stmnt;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class MethodDecl
extends ASTList {
    private static final long serialVersionUID = 1L;
    public static final String initName = "<init>";

    public MethodDecl(ASTree aSTree, ASTList aSTList) {
        super(aSTree, aSTList);
    }

    public boolean isConstructor() {
        Symbol symbol = this.getReturn().getVariable();
        return symbol != null && initName.equals(symbol.get());
    }

    public ASTList getModifiers() {
        return (ASTList)this.getLeft();
    }

    public Declarator getReturn() {
        return (Declarator)this.tail().head();
    }

    public ASTList getParams() {
        return (ASTList)this.sublist(2).head();
    }

    public ASTList getThrows() {
        return (ASTList)this.sublist(3).head();
    }

    public Stmnt getBody() {
        return (Stmnt)this.sublist(4).head();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atMethodDecl(this);
    }
}

