/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag;

import java.util.Optional;

public interface IWrappedFlag<T> {
    public String getName();

    public Optional<T> getDefaultValue();
}

