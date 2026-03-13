/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.exception;

import net.advancedplugins.as.libs.apache.commons.math3.exception.util.ExceptionContext;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.ExceptionContextProvider;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;

public class MathIllegalArgumentException
extends IllegalArgumentException
implements ExceptionContextProvider {
    private static final long serialVersionUID = -6024911025449780478L;
    private final ExceptionContext context = new ExceptionContext(this);

    public MathIllegalArgumentException(Localizable localizable, Object ... objectArray) {
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

