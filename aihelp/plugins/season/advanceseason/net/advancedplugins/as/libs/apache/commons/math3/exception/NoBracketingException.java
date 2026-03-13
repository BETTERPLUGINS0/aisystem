/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class NoBracketingException
extends MathIllegalArgumentException {
    private static final long serialVersionUID = -3629324471511904459L;
    private final double lo;
    private final double hi;
    private final double fLo;
    private final double fHi;

    public NoBracketingException(double d, double d2, double d3, double d4) {
        this((Localizable)LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, d, d2, d3, d4, new Object[0]);
    }

    public NoBracketingException(Localizable localizable, double d, double d2, double d3, double d4, Object ... objectArray) {
        super(localizable, d, d2, d3, d4, objectArray);
        this.lo = d;
        this.hi = d2;
        this.fLo = d3;
        this.fHi = d4;
    }

    public double getLo() {
        return this.lo;
    }

    public double getHi() {
        return this.hi;
    }

    public double getFLo() {
        return this.fLo;
    }

    public double getFHi() {
        return this.fHi;
    }
}

