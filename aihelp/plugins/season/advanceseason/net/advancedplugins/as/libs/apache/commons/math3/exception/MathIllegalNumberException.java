/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;

public class MathIllegalNumberException
extends MathIllegalArgumentException {
    protected static final Integer INTEGER_ZERO = 0;
    private static final long serialVersionUID = -7447085893598031110L;
    private final Number argument;

    protected MathIllegalNumberException(Localizable localizable, Number number, Object ... objectArray) {
        super(localizable, number, objectArray);
        this.argument = number;
    }

    public Number getArgument() {
        return this.argument;
    }
}

