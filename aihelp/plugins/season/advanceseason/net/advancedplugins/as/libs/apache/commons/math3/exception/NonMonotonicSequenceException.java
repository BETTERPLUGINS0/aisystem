/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathArrays;

public class NonMonotonicSequenceException
extends MathIllegalNumberException {
    private static final long serialVersionUID = 3596849179428944575L;
    private final MathArrays.OrderDirection direction;
    private final boolean strict;
    private final int index;
    private final Number previous;

    public NonMonotonicSequenceException(Number number, Number number2, int n) {
        this(number, number2, n, MathArrays.OrderDirection.INCREASING, true);
    }

    public NonMonotonicSequenceException(Number number, Number number2, int n, MathArrays.OrderDirection orderDirection, boolean bl) {
        super((Localizable)(orderDirection == MathArrays.OrderDirection.INCREASING ? (bl ? LocalizedFormats.NOT_STRICTLY_INCREASING_SEQUENCE : LocalizedFormats.NOT_INCREASING_SEQUENCE) : (bl ? LocalizedFormats.NOT_STRICTLY_DECREASING_SEQUENCE : LocalizedFormats.NOT_DECREASING_SEQUENCE)), number, number2, n, n - 1);
        this.direction = orderDirection;
        this.strict = bl;
        this.index = n;
        this.previous = number2;
    }

    public MathArrays.OrderDirection getDirection() {
        return this.direction;
    }

    public boolean getStrict() {
        return this.strict;
    }

    public int getIndex() {
        return this.index;
    }

    public Number getPrevious() {
        return this.previous;
    }
}

