/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.profiles.exceptions;

import me.zombie_striker.qav.util.xseries.profiles.exceptions.MojangAPIException;

public final class MojangAPIRetryException
extends MojangAPIException {
    private final Reason reason;

    public MojangAPIRetryException(Reason reason, String string) {
        super(string);
        this.reason = reason;
    }

    public MojangAPIRetryException(Reason reason, String string, Throwable throwable) {
        super(string, throwable);
        this.reason = reason;
    }

    public Reason getReason() {
        return this.reason;
    }

    public static enum Reason {
        CONNECTION_RESET,
        CONNECTION_TIMEOUT,
        RATELIMITED;

    }
}

