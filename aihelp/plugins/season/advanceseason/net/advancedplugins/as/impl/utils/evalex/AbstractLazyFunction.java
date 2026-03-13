/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.util.Locale;
import net.advancedplugins.as.impl.utils.evalex.LazyFunction;

public abstract class AbstractLazyFunction
implements LazyFunction {
    protected String name;
    protected int numParams;
    protected boolean booleanFunction;

    protected AbstractLazyFunction(String string, int n, boolean bl) {
        this.name = string.toUpperCase(Locale.ROOT);
        this.numParams = n;
        this.booleanFunction = bl;
    }

    protected AbstractLazyFunction(String string, int n) {
        this(string, n, false);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNumParams() {
        return this.numParams;
    }

    @Override
    public boolean numParamsVaries() {
        return this.numParams < 0;
    }

    @Override
    public boolean isBooleanFunction() {
        return this.booleanFunction;
    }
}

