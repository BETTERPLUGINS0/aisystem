/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.aggregate;

import java.util.concurrent.Callable;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus;

public final class VersionHandle<T> {
    private int version;
    private int patch;
    private T handle;

    @ApiStatus.Internal
    public VersionHandle(int n, T t) {
        this(n, 0, t);
    }

    @ApiStatus.Internal
    public VersionHandle(int n, int n2, T t) {
        if (XReflection.supports(n, n2)) {
            this.version = n;
            this.patch = n2;
            this.handle = t;
        }
    }

    @ApiStatus.Internal
    public VersionHandle(int n, int n2, Callable<T> callable) {
        if (XReflection.supports(n, n2)) {
            this.version = n;
            this.patch = n2;
            try {
                this.handle = callable.call();
            } catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @ApiStatus.Internal
    public VersionHandle(int n, Callable<T> callable) {
        this(n, 0, callable);
    }

    public VersionHandle<T> v(int n, T t) {
        return this.v(n, 0, t);
    }

    private boolean checkVersion(int n, int n2) {
        if (n == this.version && n2 == this.patch) {
            throw new IllegalArgumentException("Cannot have duplicate version handles for version: " + n + '.' + n2);
        }
        return n > this.version && n2 >= this.patch && XReflection.supports(n, n2);
    }

    public VersionHandle<T> v(int n, int n2, Callable<T> callable) {
        if (!this.checkVersion(n, n2)) {
            return this;
        }
        try {
            this.handle = callable.call();
        } catch (Exception exception) {
            // empty catch block
        }
        this.version = n;
        this.patch = n2;
        return this;
    }

    public VersionHandle<T> v(int n, int n2, T t) {
        if (this.checkVersion(n, n2)) {
            this.version = n;
            this.patch = n2;
            this.handle = t;
        }
        return this;
    }

    public T orElse(T t) {
        return this.version == 0 ? t : this.handle;
    }

    public T orElse(Callable<T> callable) {
        if (this.version == 0) {
            try {
                return callable.call();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return this.handle;
    }
}

