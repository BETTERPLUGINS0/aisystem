/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.exceptions;

import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.exceptions.ProfileException;
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

