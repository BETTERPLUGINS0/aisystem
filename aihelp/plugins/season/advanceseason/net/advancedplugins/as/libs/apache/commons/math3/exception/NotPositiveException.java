/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooSmallException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;

public class NotPositiveException
extends NumberIsTooSmallException {
    private static final long serialVersionUID = -2250556892093726375L;

    public NotPositiveException(Number number) {
        super(number, (Number)INTEGER_ZERO, true);
    }

    public NotPositiveException(Localizable localizable, Number number) {
        super(localizable, number, INTEGER_ZERO, true);
    }
}

