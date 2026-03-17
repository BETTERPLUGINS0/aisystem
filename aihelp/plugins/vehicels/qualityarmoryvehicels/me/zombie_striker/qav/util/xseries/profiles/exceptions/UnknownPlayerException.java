/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.profiles.exceptions;

import me.zombie_striker.qav.util.xseries.profiles.exceptions.InvalidProfileException;
import org.jetbrains.annotations.NotNull;

public final class UnknownPlayerException
extends InvalidProfileException {
    private final Object unknownObject;

    public UnknownPlayerException(Object object, String string) {
        super(object.toString(), string);
        this.unknownObject = object;
    }

    @NotNull
    public Object getUnknownObject() {
        return this.unknownObject;
    }
}

