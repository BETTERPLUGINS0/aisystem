/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class OutOfRangeException
extends MathIllegalNumberException {
    private static final long serialVersionUID = 111601815794403609L;
    private final Number lo;
    private final Number hi;

    public OutOfRangeException(Number number, Number number2, Number number3) {
        this((Localizable)LocalizedFormats.OUT_OF_RANGE_SIMPLE, number, number2, number3);
    }

    public OutOfRangeException(Localizable localizable, Number number, Number number2, Number number3) {
        super(localizable, number, number2, number3);
        this.lo = number2;
        this.hi = number3;
    }

    public Number getLo() {
        return this.lo;
    }

    public Number getHi() {
        return this.hi;
    }
}

