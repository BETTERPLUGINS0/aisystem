/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class NotFiniteNumberException
extends MathIllegalNumberException {
    private static final long serialVersionUID = -6100997100383932834L;

    public NotFiniteNumberException(Number number, Object ... objectArray) {
        this((Localizable)LocalizedFormats.NOT_FINITE_NUMBER, number, objectArray);
    }

    public NotFiniteNumberException(Localizable localizable, Number number, Object ... objectArray) {
        super(localizable, number, objectArray);
    }
}

