/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.profiles.exceptions;

import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileException;
import org.jetbrains.annotations.NotNull;

public final class InvalidProfileContainerException
extends ProfileException {
    private final Object container;

    public InvalidProfileContainerException(Object object, String string) {
        super(string);
        this.container = object;
    }

    @NotNull
    public Object getContainer() {
        return this.container;
    }
}

