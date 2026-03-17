/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$Obsolete
 *  org.jetbrains.annotations.Contract
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection;

import me.zombie_striker.qav.util.xseries.reflection.CachedReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import me.zombie_striker.qav.util.xseries.reflection.jvm.objects.ReflectedObject;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReflectiveHandle<T> {
    @ApiStatus.Experimental
    @Contract(value="-> new", pure=true)
    public ReflectiveHandle<T> copy();

    @Contract(pure=true)
    default public boolean exists() {
        try {
            this.reflect();
            return true;
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }

    @Deprecated
    @Nullable
    @ApiStatus.Obsolete
    default public ReflectiveOperationException catchError() {
        try {
            this.reflect();
            return null;
        } catch (ReflectiveOperationException ex) {
            return ex;
        }
    }

    @NotNull
    @Contract(pure=true)
    default public T unreflect() {
        try {
            return this.reflect();
        } catch (ReflectiveOperationException e) {
            throw XReflection.throwCheckedException(e);
        }
    }

    @Nullable
    @Contract(pure=true)
    default public T reflectOrNull() {
        try {
            return this.reflect();
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    @NotNull
    @Contract(pure=true)
    public T reflect() throws ReflectiveOperationException;

    @NotNull
    @ApiStatus.Experimental
    @Contract(pure=true)
    public ReflectiveHandle<ReflectedObject> jvm();

    @NotNull
    @ApiStatus.Experimental
    @Contract(value="-> new", pure=true)
    default public ReflectiveHandle<T> cached() {
        return new CachedReflectiveHandle<T>(this.copy());
    }

    @NotNull
    @ApiStatus.Experimental
    @Contract(pure=true)
    default public ReflectiveHandle<T> unwrap() {
        if (this instanceof CachedReflectiveHandle) {
            return ((CachedReflectiveHandle)this).getDelegate();
        }
        return this;
    }
}

