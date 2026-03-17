/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.CompileError;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.ASTree;

public class NoFieldException
extends CompileError {
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private ASTree expr;

    public NoFieldException(String string, ASTree aSTree) {
        super("no such field: " + string);
        this.fieldName = string;
        this.expr = aSTree;
    }

    public String getField() {
        return this.fieldName;
    }

    public ASTree getExpr() {
        return this.expr;
    }
}

