/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import net.advancedplugins.as.impl.utils.evalex.Expression;

public interface LazyOperator {
    public String getOper();

    public int getPrecedence();

    public boolean isLeftAssoc();

    public boolean isBooleanOperator();

    public Expression.LazyNumber eval(Expression.LazyNumber var1, Expression.LazyNumber var2);
}

