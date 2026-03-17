/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast;

import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.CtField;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Symbol;
import me.zombie_striker.qav.hooks.worldguard.shaded.javassist.compiler.ast.Visitor;

public class Member
extends Symbol {
    private static final long serialVersionUID = 1L;
    private CtField field = null;

    public Member(String string) {
        super(string);
    }

    public void setField(CtField ctField) {
        this.field = ctField;
    }

    public CtField getField() {
        return this.field;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.atMember(this);
    }
}

