/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.TokenId;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTList;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class Declarator
extends ASTList
implements TokenId {
    private static final long serialVersionUID = 1L;
    protected int varType;
    protected int arrayDim;
    protected int localVar;
    protected String qualifiedClass;

    public Declarator(int n, int n2) {
        super(null);
        this.varType = n;
        this.arrayDim = n2;
        this.localVar = -1;
        this.qualifiedClass = null;
    }

    public Declarator(ASTList aSTList, int n) {
        super(null);
        this.varType = 307;
        this.arrayDim = n;
        this.localVar = -1;
        this.qualifiedClass = Declarator.astToClassName(aSTList, '/');
    }

    public Declarator(int n, String string, int n2, int n3, Symbol symbol) {
        super(null);
        this.varType = n;
        this.arrayDim = n2;
        this.localVar = n3;
        this.qualifiedClass = string;
        this.setLeft(symbol);
        Declarator.append(this, null);
    }

    public Declarator make(Symbol symbol, int n, ASTree aSTree) {
        Declarator declarator = new Declarator(this.varType, this.arrayDim + n);
        declarator.qualifiedClass = this.qualifiedClass;
        declarator.setLeft(symbol);
        Declarator.append(declarator, aSTree);
        return declarator;
    }

    public int getType() {
        return this.varType;
    }

    public int getArrayDim() {
        return this.arrayDim;
    }

    public void addArrayDim(int n) {
        this.arrayDim += n;
    }

    public String getClassName() {
        return this.qualifiedClass;
    }

    public void setClassName(String string) {
        this.qualifiedClass = string;
    }

    public Symbol getVariable() {
        return (Symbol)this.getLeft();
    }

    public void setVariable(Symbol symbol) {
        this.setLeft(symbol);
    }

    public ASTree getInitializer() {
        ASTList aSTList = this.tail();
        if (aSTList != null) {
            return aSTList.head();
        }
        return null;
    }

    public void setLocalVar(int n) {
        this.localVar = n;
    }

    public int getLocalVar() {
        return this.localVar;
    }

    @Override
    public String getTag() {
        return "decl";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atDeclarator(this);
    }

    public static String astToClassName(ASTList aSTList, char c) {
        if (aSTList == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        Declarator.astToClassName(stringBuilder, aSTList, c);
        return stringBuilder.toString();
    }

    private static void astToClassName(StringBuilder stringBuilder, ASTList aSTList, char c) {
        while (true) {
            ASTree aSTree;
            if ((aSTree = aSTList.head()) instanceof Symbol) {
                stringBuilder.append(((Symbol)aSTree).get());
            } else if (aSTree instanceof ASTList) {
                Declarator.astToClassName(stringBuilder, (ASTList)aSTree, c);
            }
            aSTList = aSTList.tail();
            if (aSTList == null) break;
            stringBuilder.append(c);
        }
    }
}

