/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class ZeroException
extends MathIllegalNumberException {
    private static final long serialVersionUID = -1960874856936000015L;

    public ZeroException() {
        this((Localizable)LocalizedFormats.ZERO_NOT_ALLOWED, new Object[0]);
    }

    public ZeroException(Localizable localizable, Object ... objectArray) {
        super(localizable, INTEGER_ZERO, objectArray);
    }
}

