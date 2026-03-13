/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.util.ExceptionContext;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.ExceptionContextProvider;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;

public class MathArithmeticException
extends ArithmeticException
implements ExceptionContextProvider {
    private static final long serialVersionUID = -6024911025449780478L;
    private final ExceptionContext context = new ExceptionContext(this);

    public MathArithmeticException() {
        this.context.addMessage(LocalizedFormats.ARITHMETIC_EXCEPTION, new Object[0]);
    }

    public MathArithmeticException(Localizable localizable, Object ... objectArray) {
        this.context.addMessage(localizable, objectArray);
    }

    public ExceptionContext getContext() {
        return this.context;
    }

    public String getMessage() {
        return this.context.getMessage();
    }

    public String getLocalizedMessage() {
        return this.context.getLocalizedMessage();
    }
}

