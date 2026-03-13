/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class NumberIsTooLargeException
extends MathIllegalNumberException {
    private static final long serialVersionUID = 4330003017885151975L;
    private final Number max;
    private final boolean boundIsAllowed;

    public NumberIsTooLargeException(Number number, Number number2, boolean bl) {
        this((Localizable)(bl ? LocalizedFormats.NUMBER_TOO_LARGE : LocalizedFormats.NUMBER_TOO_LARGE_BOUND_EXCLUDED), number, number2, bl);
    }

    public NumberIsTooLargeException(Localizable localizable, Number number, Number number2, boolean bl) {
        super(localizable, number, number2);
        this.max = number2;
        this.boundIsAllowed = bl;
    }

    public boolean getBoundIsAllowed() {
        return this.boundIsAllowed;
    }

    public Number getMax() {
        return this.max;
    }
}

