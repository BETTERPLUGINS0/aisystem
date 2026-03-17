/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import net.kyori.adventure.pointer.Pointer;
import net.kyori.adventure.pointer.Pointers;
import net.kyori.adventure.pointer.PointersSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PointersSupplierImpl<T>
implements PointersSupplier<T> {
    private final PointersSupplier<? super T> parent;
    private final Map<Pointer<?>, Function<T, ?>> resolvers;

    PointersSupplierImpl(@NotNull BuilderImpl<T> builderImpl) {
        this.parent = ((BuilderImpl)builderImpl).parent;
        this.resolvers = new HashMap(((BuilderImpl)builderImpl).resolvers);
    }

    @Override
    @NotNull
    public Pointers view(@NotNull T t) {
        return new ForwardingPointers<T>(t, this);
    }

    @Override
    public <P> boolean supports(@NotNull Pointer<P> pointer) {
        if (this.resolvers.containsKey(Objects.requireNonNull(pointer, "pointer"))) {
            return true;
        }
        if (this.parent == null) {
            return false;
        }
        return this.parent.supports(pointer);
    }

    @Override
    @Nullable
    public <P> Function<? super T, P> resolver(@NotNull Pointer<P> pointer) {
        Function<T, ?> function = this.resolvers.get(Objects.requireNonNull(pointer, "pointer"));
        if (function != null) {
            return function;
        }
        if (this.parent == null) {
            return null;
        }
        return this.parent.resolver(pointer);
    }

    static final class BuilderImpl<T>
    implements PointersSupplier.Builder<T> {
        private PointersSupplier<? super T> parent = null;
        private final Map<Pointer<?>, Function<T, ?>> resolvers = new HashMap();

        BuilderImpl() {
        }

        @Override
        @NotNull
        public PointersSupplier.Builder<T> parent(@Nullable PointersSupplier<? super T> pointersSupplier) {
            this.parent = pointersSupplier;
            return this;
        }

        @Override
        @NotNull
        public <P> PointersSupplier.Builder<T> resolving(@NotNull Pointer<P> pointer, @NotNull Function<T, P> function) {
            this.resolvers.put(pointer, function);
            return this;
        }

        @Override
        @NotNull
        public PointersSupplier<T> build() {
            return new PointersSupplierImpl(this);
        }
    }

    static final class ForwardingPointers<U>
    implements Pointers {
        private final U instance;
        private final PointersSupplierImpl<U> supplier;

        ForwardingPointers(@NotNull U u, @NotNull PointersSupplierImpl<U> pointersSupplierImpl) {
            this.instance = u;
            this.supplier = pointersSupplierImpl;
        }

        @Override
        @NotNull
        public <T> Optional<T> get(@NotNull Pointer<T> pointer) {
            Function<U, U> function = (Function<U, U>)((PointersSupplierImpl)this.supplier).resolvers.get(Objects.requireNonNull(pointer, "pointer"));
            if (function == null) {
                function = ((PointersSupplierImpl)this.supplier).parent.resolver(pointer);
            }
            if (function == null) {
                return Optional.empty();
            }
            return Optional.ofNullable(function.apply(this.instance));
        }

        @Override
        public <T> boolean supports(@NotNull Pointer<T> pointer) {
            return this.supplier.supports(pointer);
        }

        @Override
        public @NotNull Pointers.Builder toBuilder() {
            Pointers.Builder builder = ((PointersSupplierImpl)this.supplier).parent == null ? Pointers.builder() : (Pointers.Builder)((PointersSupplierImpl)this.supplier).parent.view(this.instance).toBuilder();
            for (Map.Entry entry : ((PointersSupplierImpl)this.supplier).resolvers.entrySet()) {
                builder.withDynamic((Pointer)entry.getKey(), () -> ((Function)entry.getValue()).apply(this.instance));
            }
            return builder;
        }
    }
}

