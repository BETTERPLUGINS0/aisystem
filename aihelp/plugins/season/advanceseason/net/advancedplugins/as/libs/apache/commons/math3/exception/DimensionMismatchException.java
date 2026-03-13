/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalNumberException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class DimensionMismatchException
extends MathIllegalNumberException {
    private static final long serialVersionUID = -8415396756375798143L;
    private final int dimension;

    public DimensionMismatchException(Localizable localizable, int n, int n2) {
        super(localizable, n, n2);
        this.dimension = n2;
    }

    public DimensionMismatchException(int n, int n2) {
        this((Localizable)LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, n, n2);
    }

    public int getDimension() {
        return this.dimension;
    }
}

