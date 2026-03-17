/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.zombie_striker.qav.hooks.worldguard.flag;

import java.util.Optional;

public interface IWrappedFlag<T> {
    public String getName();

    public Optional<T> getDefaultValue();
}

