/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import net.advancedplugins.as.impl.utils.evalex.AbstractLazyOperator;
import net.advancedplugins.as.impl.utils.evalex.Expression;
import net.advancedplugins.as.impl.utils.evalex.Operator;

public abstract class AbstractOperator
extends AbstractLazyOperator
implements Operator {
    protected AbstractOperator(String string, int n, boolean bl, boolean bl2) {
        super(string, n, bl, bl2);
    }

    protected AbstractOperator(String string, int n, boolean bl) {
        super(string, n, bl);
    }

    @Override
    public Expression.LazyNumber eval(final Expression.LazyNumber lazyNumber, final Expression.LazyNumber lazyNumber2) {
        return new Expression.LazyNumber(){

            @Override
            public BigDecimal eval() {
                return AbstractOperator.this.eval(lazyNumber.eval(), lazyNumber2.eval());
            }

            @Override
            public String getString() {
                return String.valueOf(AbstractOperator.this.eval(lazyNumber.eval(), lazyNumber2.eval()));
            }
        };
    }
}

