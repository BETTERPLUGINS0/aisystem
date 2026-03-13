/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.util.List;
import net.advancedplugins.as.impl.utils.evalex.Expression;

public interface LazyFunction {
    public String getName();

    public int getNumParams();

    public boolean numParamsVaries();

    public boolean isBooleanFunction();

    public Expression.LazyNumber lazyEval(List<Expression.LazyNumber> var1);
}

