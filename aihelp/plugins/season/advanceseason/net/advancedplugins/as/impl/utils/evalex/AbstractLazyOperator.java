/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import net.advancedplugins.as.impl.utils.evalex.LazyOperator;

public abstract class AbstractLazyOperator
implements LazyOperator {
    protected String oper;
    protected int precedence;
    protected boolean leftAssoc;
    protected boolean booleanOperator = false;

    protected AbstractLazyOperator(String string, int n, boolean bl, boolean bl2) {
        this.oper = string;
        this.precedence = n;
        this.leftAssoc = bl;
        this.booleanOperator = bl2;
    }

    protected AbstractLazyOperator(String string, int n, boolean bl) {
        this.oper = string;
        this.precedence = n;
        this.leftAssoc = bl;
    }

    @Override
    public String getOper() {
        return this.oper;
    }

    @Override
    public int getPrecedence() {
        return this.precedence;
    }

    @Override
    public boolean isLeftAssoc() {
        return this.leftAssoc;
    }

    @Override
    public boolean isBooleanOperator() {
        return this.booleanOperator;
    }
}

