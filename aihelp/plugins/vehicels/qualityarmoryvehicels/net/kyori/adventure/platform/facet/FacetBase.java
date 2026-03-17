/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.facet;

import net.kyori.adventure.platform.facet.Facet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class FacetBase<V>
implements Facet<V> {
    protected final Class<? extends V> viewerClass;

    protected FacetBase(@Nullable Class<? extends V> clazz) {
        this.viewerClass = clazz;
    }

    @Override
    public boolean isSupported() {
        return this.viewerClass != null;
    }

    @Override
    public boolean isApplicable(@NotNull V v) {
        return this.viewerClass != null && this.viewerClass.isInstance(v);
    }
}

