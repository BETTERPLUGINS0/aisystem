/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 */
package nl.sbdeveloper.vehiclesplus.storage.db.exceptions;

import lombok.Generated;

public class DataStorageException
extends Exception {
    @Generated
    public DataStorageException() {
        this(null, null);
    }

    @Generated
    public DataStorageException(String string) {
        this(string, null);
    }

    @Generated
    public DataStorageException(Throwable throwable) {
        this(throwable != null ? throwable.getMessage() : null, throwable);
    }

    @Generated
    public DataStorageException(String string, Throwable throwable) {
        super(string);
        if (throwable != null) {
            super.initCause(throwable);
        }
    }
}

