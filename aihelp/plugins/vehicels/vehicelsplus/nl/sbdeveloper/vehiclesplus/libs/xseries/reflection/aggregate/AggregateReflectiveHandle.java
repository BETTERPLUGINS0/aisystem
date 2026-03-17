/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.aggregate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.ReflectiveHandle;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AggregateReflectiveHandle<T, H extends ReflectiveHandle<T>>
implements ReflectiveHandle<T> {
    private final List<Callable<H>> handles;
    private Consumer<H> handleModifier;

    @ApiStatus.Internal
    public AggregateReflectiveHandle(Collection<Callable<H>> collection) {
        this.handles = new ArrayList<Callable<H>>(collection.size());
        this.handles.addAll(collection);
    }

    public AggregateReflectiveHandle<T, H> or(@NotNull H h) {
        return this.or(() -> h);
    }

    public AggregateReflectiveHandle<T, H> or(@NotNull Callable<H> callable) {
        this.handles.add(callable);
        return this;
    }

    public AggregateReflectiveHandle<T, H> modify(@Nullable Consumer<H> consumer) {
        this.handleModifier = consumer;
        return this;
    }

    public H getHandle() {
        Throwable throwable = null;
        for (Callable<H> callable : this.handles) {
            try {
                ReflectiveHandle reflectiveHandle = (ReflectiveHandle)callable.call();
                if (this.handleModifier != null) {
                    this.handleModifier.accept(reflectiveHandle);
                }
                if (!reflectiveHandle.exists()) {
                    reflectiveHandle.reflect();
                }
                return (H)reflectiveHandle;
            } catch (Throwable throwable2) {
                if (throwable == null) {
                    throwable = new ClassNotFoundException("None of the aggregate handles were successful");
                }
                throwable.addSuppressed(throwable2);
            }
        }
        throw XReflection.throwCheckedException(XReflection.relativizeSuppressedExceptions(throwable));
    }

    public AggregateReflectiveHandle<T, H> clone() {
        AggregateReflectiveHandle<T, H> aggregateReflectiveHandle = new AggregateReflectiveHandle<T, H>(new ArrayList<Callable<H>>(this.handles));
        aggregateReflectiveHandle.handleModifier = this.handleModifier;
        return aggregateReflectiveHandle;
    }

    @Override
    public T reflect() {
        Throwable throwable = null;
        for (Callable<H> callable : this.handles) {
            ReflectiveHandle reflectiveHandle;
            block7: {
                try {
                    reflectiveHandle = (ReflectiveHandle)callable.call();
                    if (this.handleModifier == null) break block7;
                    this.handleModifier.accept(reflectiveHandle);
                } catch (Throwable throwable2) {
                    if (throwable == null) {
                        throwable = new ClassNotFoundException("None of the aggregate handles were successful");
                    }
                    throwable.addSuppressed(throwable2);
                    continue;
                }
            }
            try {
                return reflectiveHandle.reflect();
            } catch (Throwable throwable3) {
                if (throwable == null) {
                    throwable = new ClassNotFoundException("None of the aggregate handles were successful");
                }
                throwable.addSuppressed(throwable3);
            }
        }
        throw (ClassNotFoundException)XReflection.relativizeSuppressedExceptions(throwable);
    }
}

