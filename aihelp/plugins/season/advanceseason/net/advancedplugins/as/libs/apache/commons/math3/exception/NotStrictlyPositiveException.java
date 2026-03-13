/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooSmallException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;

public class NotStrictlyPositiveException
extends NumberIsTooSmallException {
    private static final long serialVersionUID = -7824848630829852237L;

    public NotStrictlyPositiveException(Number number) {
        super(number, (Number)INTEGER_ZERO, false);
    }

    public NotStrictlyPositiveException(Localizable localizable, Number number) {
        super(localizable, number, INTEGER_ZERO, false);
    }
}

