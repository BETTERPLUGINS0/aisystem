/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalStateException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class MaxCountExceededException
extends MathIllegalStateException {
    private static final long serialVersionUID = 4330003017885151975L;
    private final Number max;

    public MaxCountExceededException(Number number) {
        this((Localizable)LocalizedFormats.MAX_COUNT_EXCEEDED, number, new Object[0]);
    }

    public MaxCountExceededException(Localizable localizable, Number number, Object ... objectArray) {
        this.getContext().addMessage(localizable, number, objectArray);
        this.max = number;
    }

    public Number getMax() {
        return this.max;
    }
}

