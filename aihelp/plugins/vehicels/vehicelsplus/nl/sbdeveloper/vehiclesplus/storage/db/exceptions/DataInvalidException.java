/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.storage.db.exceptions;

import lombok.Generated;

public class DataInvalidException
extends Exception {
    @Generated
    public DataInvalidException() {
        this(null, null);
    }

    @Generated
    public DataInvalidException(String string) {
        this(string, null);
    }

    @Generated
    public DataInvalidException(Throwable throwable) {
        this(throwable != null ? throwable.getMessage() : null, throwable);
    }

    @Generated
    public DataInvalidException(String string, Throwable throwable) {
        super(string);
        if (throwable != null) {
            super.initCause(throwable);
        }
    }
}

