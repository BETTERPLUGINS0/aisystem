/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.MaxCountExceededException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class TooManyEvaluationsException
extends MaxCountExceededException {
    private static final long serialVersionUID = 4330003017885151975L;

    public TooManyEvaluationsException(Number number) {
        super(number);
        this.getContext().addMessage(LocalizedFormats.EVALUATIONS, new Object[0]);
    }
}

