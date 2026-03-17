/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast;

import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.CompileError;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.ASTList;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.ASTree;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.Declarator;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.shaded.javassist.compiler.ast.Visitor;

public class FieldDecl
extends ASTList {
    private static final long serialVersionUID = 1L;

    public FieldDecl(ASTree _head, ASTList _tail) {
        super(_head, _tail);
    }

    public ASTList getModifiers() {
        return (ASTList)this.getLeft();
    }

    public Declarator getDeclarator() {
        return (Declarator)this.tail().head();
    }

    public ASTree getInit() {
        return this.sublist(2).head();
    }

    @Override
    public void accept(Visitor v) throws CompileError {
        v.atFieldDecl(this);
    }
}

