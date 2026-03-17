/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.exceptions;

import lombok.Generated;

public class APINotOverwrittenException
extends Exception {
    public APINotOverwrittenException(String string) {
        super(string);
    }

    @Generated
    public APINotOverwrittenException() {
        this(null, null);
    }

    @Generated
    public APINotOverwrittenException(Throwable throwable) {
        this(throwable != null ? throwable.getMessage() : null, throwable);
    }

    @Generated
    public APINotOverwrittenException(String string, Throwable throwable) {
        super(string);
        if (throwable != null) {
            super.initCause(throwable);
        }
    }
}

