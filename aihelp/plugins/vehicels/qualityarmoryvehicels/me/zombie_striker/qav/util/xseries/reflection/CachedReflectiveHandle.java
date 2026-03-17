/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.util.xseries.reflection;

import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
class CachedReflectiveHandle<T>
implements ReflectiveHandle<T> {
    private final ReflectiveHandle<T> delegate;
    private T cache;
    private CachedReflectiveHandle<ReflectedObject> jvm;

    CachedReflectiveHandle(ReflectiveHandle<T> reflectiveHandle) {
        this.delegate = reflectiveHandle;
    }

    public ReflectiveHandle<T> getDelegate() {
        return this.delegate;
    }

    @Override
    public ReflectiveHandle<T> copy() {
        return this.delegate.copy();
    }

    @Override
    @NotNull
    public T reflect() {
        if (this.cache == null) {
            this.cache = this.delegate.reflect();
            return this.cache;
        }
        return this.cache;
    }

    @Override
    @NotNull
    public ReflectiveHandle<ReflectedObject> jvm() {
        if (this.jvm == null) {
            this.jvm = new CachedReflectiveHandle<ReflectedObject>(this.delegate.jvm());
            return this.jvm;
        }
        return this.jvm;
    }
}

