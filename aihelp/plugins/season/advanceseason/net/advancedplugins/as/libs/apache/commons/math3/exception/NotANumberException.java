/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class NotANumberException
extends MathIllegalNumberException {
    private static final long serialVersionUID = 20120906L;

    public NotANumberException() {
        super((Localizable)LocalizedFormats.NAN_NOT_ALLOWED, Double.NaN, new Object[0]);
    }
}

