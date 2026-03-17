/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.profiles.exceptions;

import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileException;
import org.jetbrains.annotations.NotNull;

public class InvalidProfileException
extends ProfileException {
    private final String value;

    public InvalidProfileException(String string, String string2) {
        super(string2);
        this.value = string;
    }

    public InvalidProfileException(String string, String string2, Throwable throwable) {
        super(string2, throwable);
        this.value = string;
    }

    @NotNull
    public String getValue() {
        return this.value;
    }
}

