/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class NumberIsTooSmallException
extends MathIllegalNumberException {
    private static final long serialVersionUID = -6100997100383932834L;
    private final Number min;
    private final boolean boundIsAllowed;

    public NumberIsTooSmallException(Number number, Number number2, boolean bl) {
        this((Localizable)(bl ? LocalizedFormats.NUMBER_TOO_SMALL : LocalizedFormats.NUMBER_TOO_SMALL_BOUND_EXCLUDED), number, number2, bl);
    }

    public NumberIsTooSmallException(Localizable localizable, Number number, Number number2, boolean bl) {
        super(localizable, number, number2);
        this.min = number2;
        this.boundIsAllowed = bl;
    }

    public boolean getBoundIsAllowed() {
        return this.boundIsAllowed;
    }

    public Number getMin() {
        return this.min;
    }
}

