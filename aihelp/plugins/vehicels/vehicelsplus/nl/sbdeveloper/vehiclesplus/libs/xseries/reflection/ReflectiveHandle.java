/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection;

import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ReflectiveHandle<T>
extends Cloneable {
    @ApiStatus.Experimental
    public ReflectiveHandle<T> clone();

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
    default public T unreflect() {
        try {
            return this.reflect();
        } catch (ReflectiveOperationException e) {
            throw XReflection.throwCheckedException(e);
        }
    }

    @Nullable
    default public T reflectOrNull() {
        try {
            return this.reflect();
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }

    @NotNull
    public T reflect() throws ReflectiveOperationException;
}

