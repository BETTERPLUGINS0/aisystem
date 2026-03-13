/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import java.util.List;
import net.advancedplugins.as.impl.utils.evalex.Expression;

public class LazyIfNumber
implements Expression.LazyNumber {
    private final List<Expression.LazyNumber> lazyParams;

    public LazyIfNumber(List<Expression.LazyNumber> list) {
        this.lazyParams = list;
    }

    @Override
    public BigDecimal eval() {
        BigDecimal bigDecimal = this.lazyParams.get(0).eval();
        this.assertNotNull(bigDecimal);
        boolean bl = bigDecimal.compareTo(BigDecimal.ZERO) != 0;
        return bl ? this.lazyParams.get(1).eval() : this.lazyParams.get(2).eval();
    }

    @Override
    public String getString() {
        return this.lazyParams.get(0).getString();
    }

    private void assertNotNull(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            throw new ArithmeticException("Operand may not be null");
        }
    }
}

