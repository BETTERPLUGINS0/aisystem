/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import java.util.List;
import net.advancedplugins.as.impl.utils.evalex.LazyFunction;

public interface Function
extends LazyFunction {
    public BigDecimal eval(List<BigDecimal> var1);
}

