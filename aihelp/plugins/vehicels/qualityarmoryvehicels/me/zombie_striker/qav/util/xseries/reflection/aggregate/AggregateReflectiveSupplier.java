/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Experimental
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package me.zombie_striker.qav.util.xseries.reflection.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.zombie_striker.qav.util.xseries.reflection.ReflectiveHandle;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Experimental
public class AggregateReflectiveSupplier<H extends ReflectiveHandle<?>, O> {
    private final List<ReflectivePair> handles = new ArrayList<ReflectivePair>();
    private Consumer<H> handleModifier;

    @ApiStatus.Internal
    public AggregateReflectiveSupplier() {
    }

    public AggregateReflectiveSupplier<H, O> or(@NotNull H h, O o) {
        return this.or(() -> h, o);
    }

    public AggregateReflectiveSupplier<H, O> or(@NotNull Callable<H> callable, O o) {
        return this.or(callable, () -> o);
    }

    public AggregateReflectiveSupplier<H, O> or(@NotNull H h, Supplier<O> supplier) {
        return this.or(() -> h, supplier);
    }

    public AggregateReflectiveSupplier<H, O> or(@NotNull Callable<H> callable, Supplier<O> supplier) {
        this.handles.add(new ReflectivePair(callable, supplier));
        return this;
    }

    public AggregateReflectiveSupplier<H, O> modify(@Nullable Consumer<H> consumer) {
        this.handleModifier = consumer;
        return this;
    }

    public O get() {
        Throwable throwable = null;
        for (ReflectivePair reflectivePair : this.handles) {
            try {
                ReflectiveHandle reflectiveHandle = (ReflectiveHandle)reflectivePair.handle.call();
                if (this.handleModifier != null) {
                    this.handleModifier.accept(reflectiveHandle);
                }
                if (!reflectiveHandle.exists()) {
                    reflectiveHandle.reflect();
                }
                return (O)reflectivePair.object.get();
            } catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = new ClassNotFoundException("None of the aggregate handles were successful");
                }
                throwable.addSuppressed(throwable2);
            }
        }
        throw XReflection.throwCheckedException(XReflection.relativizeSuppressedExceptions(throwable));
    }

    private final class ReflectivePair {
        private final Callable<H> handle;
        private final Supplier<O> object;

        private ReflectivePair(Callable<H> callable, Supplier<O> supplier) {
            this.handle = callable;
            this.object = supplier;
        }
    }
}

