/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.nbt.utils;

public class NbtApiException
extends RuntimeException {
    private static final long serialVersionUID = -993309714559452334L;

    public NbtApiException() {
    }

    public NbtApiException(String string, Throwable throwable, boolean bl, boolean bl2) {
        super(string, throwable, bl, bl2);
    }

    public NbtApiException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public NbtApiException(String string) {
        super(string);
    }

    public NbtApiException(Throwable throwable) {
        super(throwable);
    }
}

