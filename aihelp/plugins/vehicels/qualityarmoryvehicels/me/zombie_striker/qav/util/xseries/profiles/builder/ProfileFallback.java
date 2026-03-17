/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.util.xseries.profiles.builder;

import me.zombie_striker.qav.util.xseries.profiles.builder.ProfileInstruction;
import me.zombie_striker.qav.util.xseries.profiles.exceptions.ProfileChangeException;

public final class ProfileFallback<T> {
    private final ProfileInstruction<T> instruction;
    private T object;
    private final ProfileChangeException error;

    public ProfileFallback(ProfileInstruction<T> profileInstruction, T t, ProfileChangeException profileChangeException) {
        this.instruction = profileInstruction;
        this.object = t;
        this.error = profileChangeException;
    }

    public T getObject() {
        return this.object;
    }

    public ProfileInstruction<T> getInstruction() {
        return this.instruction;
    }

    public void setObject(T t) {
        this.object = t;
    }

    public ProfileChangeException getError() {
        return this.error;
    }
}

