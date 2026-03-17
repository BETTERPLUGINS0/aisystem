/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.api.exceptions;

import lombok.Generated;

public class UnsupportedTrunkSizeException
extends IllegalArgumentException {
    @Generated
    public UnsupportedTrunkSizeException() {
        this(null, null);
    }

    @Generated
    public UnsupportedTrunkSizeException(String string) {
        this(string, null);
    }

    @Generated
    public UnsupportedTrunkSizeException(Throwable throwable) {
        this(throwable != null ? throwable.getMessage() : null, throwable);
    }

    @Generated
    public UnsupportedTrunkSizeException(String string, Throwable throwable) {
        super(string);
        if (throwable != null) {
            super.initCause(throwable);
        }
    }
}

