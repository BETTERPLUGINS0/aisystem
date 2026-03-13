/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import net.advancedplugins.as.impl.utils.evalex.AbstractOperator;
import net.advancedplugins.as.impl.utils.evalex.Expression;

public abstract class AbstractUnaryOperator
extends AbstractOperator {
    protected AbstractUnaryOperator(String string, int n, boolean bl) {
        super(string, n, bl);
    }

    @Override
    public Expression.LazyNumber eval(final Expression.LazyNumber lazyNumber, Expression.LazyNumber lazyNumber2) {
        if (lazyNumber2 != null) {
            throw new Expression.ExpressionException("Did not expect a second parameter for unary operator");
        }
        return new Expression.LazyNumber(){

            @Override
            public String getString() {
                return String.valueOf(AbstractUnaryOperator.this.evalUnary(lazyNumber.eval()));
            }

            @Override
            public BigDecimal eval() {
                return AbstractUnaryOperator.this.evalUnary(lazyNumber.eval());
            }
        };
    }

    @Override
    public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        if (bigDecimal2 != null) {
            throw new Expression.ExpressionException("Did not expect a second parameter for unary operator");
        }
        return this.evalUnary(bigDecimal);
    }

    public abstract BigDecimal evalUnary(BigDecimal var1);
}

