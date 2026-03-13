/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.advancedplugins.as.impl.utils.evalex.AbstractLazyFunction;
import net.advancedplugins.as.impl.utils.evalex.Expression;
import net.advancedplugins.as.impl.utils.evalex.Function;

public abstract class AbstractFunction
extends AbstractLazyFunction
implements Function {
    protected AbstractFunction(String string, int n) {
        super(string, n);
    }

    protected AbstractFunction(String string, int n, boolean bl) {
        super(string, n, bl);
    }

    @Override
    public Expression.LazyNumber lazyEval(final List<Expression.LazyNumber> list) {
        return new Expression.LazyNumber(){
            private List<BigDecimal> params;

            @Override
            public BigDecimal eval() {
                return AbstractFunction.this.eval(this.getParams());
            }

            @Override
            public String getString() {
                return String.valueOf(AbstractFunction.this.eval(this.getParams()));
            }

            private List<BigDecimal> getParams() {
                if (this.params == null) {
                    this.params = new ArrayList<BigDecimal>();
                    for (Expression.LazyNumber lazyNumber : list) {
                        this.params.add(lazyNumber.eval());
                    }
                }
                return this.params;
            }
        };
    }
}

