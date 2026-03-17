/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.kyori.adventure.util.InheritanceAwareMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class InheritanceAwareMapImpl<C, V>
implements InheritanceAwareMap<C, V> {
    private static final Object NONE = new Object();
    static final InheritanceAwareMapImpl EMPTY = new InheritanceAwareMapImpl(false, Collections.emptyMap());
    private final Map<Class<? extends C>, V> declaredValues;
    private final boolean strict;
    private final transient ConcurrentMap<Class<? extends C>, Object> cache = new ConcurrentHashMap<Class<? extends C>, Object>();

    InheritanceAwareMapImpl(boolean bl, Map<Class<? extends C>, V> map) {
        this.strict = bl;
        this.declaredValues = map;
    }

    @Override
    public boolean containsKey(@NotNull Class<? extends C> clazz) {
        return this.get(clazz) != null;
    }

    @Override
    @Nullable
    public V get(@NotNull Class<? extends C> clazz2) {
        Object object = this.cache.computeIfAbsent(clazz2, clazz -> {
            @Nullable V v = this.declaredValues.get(clazz);
            if (v != null) {
                return v;
            }
            for (Map.Entry<Class<C>, V> entry : this.declaredValues.entrySet()) {
                if (!entry.getKey().isAssignableFrom((Class<?>)clazz)) continue;
                return entry.getValue();
            }
            return NONE;
        });
        return (V)(object == NONE ? null : object);
    }

    @Override
    @NotNull
    public InheritanceAwareMap<C, V> with(@NotNull Class<? extends C> clazz, @NotNull V v) {
        if (Objects.equals(this.declaredValues.get(clazz), v)) {
            return this;
        }
        if (this.strict) {
            InheritanceAwareMapImpl.validateNoneInHierarchy(clazz, this.declaredValues);
        }
        LinkedHashMap<Class<C>, V> linkedHashMap = new LinkedHashMap<Class<C>, V>(this.declaredValues);
        linkedHashMap.put(clazz, v);
        return new InheritanceAwareMapImpl<C, V>(this.strict, Collections.unmodifiableMap(linkedHashMap));
    }

    @Override
    @NotNull
    public InheritanceAwareMap<C, V> without(@NotNull Class<? extends C> clazz) {
        if (!this.declaredValues.containsKey(clazz)) {
            return this;
        }
        LinkedHashMap<Class<C>, V> linkedHashMap = new LinkedHashMap<Class<C>, V>(this.declaredValues);
        linkedHashMap.remove(clazz);
        return new InheritanceAwareMapImpl<C, V>(this.strict, Collections.unmodifiableMap(linkedHashMap));
    }

    private static void validateNoneInHierarchy(Class<?> clazz, Map<? extends Class<?>, ?> map) {
        for (Class<?> clazz2 : map.keySet()) {
            InheritanceAwareMapImpl.testHierarchy(clazz2, clazz);
        }
    }

    private static void testHierarchy(Class<?> clazz, Class<?> clazz2) {
        if (!clazz.equals(clazz2) && (clazz.isAssignableFrom(clazz2) || clazz2.isAssignableFrom(clazz))) {
            throw new IllegalArgumentException("Conflict detected between already registered type " + clazz + " and newly registered type " + clazz2 + "! Types in a strict inheritance-aware map must not share a common hierarchy!");
        }
    }

    static final class BuilderImpl<C, V>
    implements InheritanceAwareMap.Builder<C, V> {
        private boolean strict;
        private final Map<Class<? extends C>, V> values = new LinkedHashMap<Class<? extends C>, V>();

        BuilderImpl() {
        }

        @Override
        @NotNull
        public InheritanceAwareMap<C, V> build() {
            return new InheritanceAwareMapImpl<C, V>(this.strict, Collections.unmodifiableMap(new LinkedHashMap<Class<? extends C>, V>(this.values)));
        }

        @Override
        @NotNull
        public InheritanceAwareMap.Builder<C, V> strict(boolean bl) {
            if (bl && !this.strict) {
                for (Class<? extends C> clazz : this.values.keySet()) {
                    InheritanceAwareMapImpl.validateNoneInHierarchy(clazz, this.values);
                }
            }
            this.strict = bl;
            return this;
        }

        @Override
        @NotNull
        public InheritanceAwareMap.Builder<C, V> put(@NotNull Class<? extends C> clazz, @NotNull V v) {
            if (this.strict) {
                InheritanceAwareMapImpl.validateNoneInHierarchy(clazz, this.values);
            }
            this.values.put(Objects.requireNonNull(clazz, "clazz"), Objects.requireNonNull(v, "value"));
            return this;
        }

        @Override
        @NotNull
        public InheritanceAwareMap.Builder<C, V> remove(@NotNull Class<? extends C> clazz) {
            this.values.remove(Objects.requireNonNull(clazz, "clazz"));
            return this;
        }

        @Override
        @NotNull
        public InheritanceAwareMap.Builder<C, V> putAll(@NotNull InheritanceAwareMap<? extends C, ? extends V> inheritanceAwareMap) {
            InheritanceAwareMapImpl inheritanceAwareMapImpl = (InheritanceAwareMapImpl)inheritanceAwareMap;
            if (!(!this.strict || this.values.isEmpty() && inheritanceAwareMapImpl.strict)) {
                for (Map.Entry entry : inheritanceAwareMapImpl.declaredValues.entrySet()) {
                    InheritanceAwareMapImpl.validateNoneInHierarchy((Class)entry.getKey(), this.values);
                    this.values.put((Class)entry.getKey(), entry.getValue());
                }
                return this;
            }
            this.values.putAll(inheritanceAwareMapImpl.declaredValues);
            return this;
        }
    }
}

