/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.exceptions;

import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.exceptions.InvalidProfileException;
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

