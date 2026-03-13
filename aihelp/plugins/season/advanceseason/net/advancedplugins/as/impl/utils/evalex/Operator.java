/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import net.advancedplugins.as.impl.utils.evalex.LazyOperator;

public interface Operator
extends LazyOperator {
    public BigDecimal eval(BigDecimal var1, BigDecimal var2);
}

