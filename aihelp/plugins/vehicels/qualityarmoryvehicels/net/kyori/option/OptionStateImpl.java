/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.option;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Consumer;
import net.kyori.option.Option;
import net.kyori.option.OptionState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OptionStateImpl
implements OptionState {
    static final OptionState EMPTY = new OptionStateImpl(new IdentityHashMap());
    private final IdentityHashMap<Option<?>, Object> values;

    OptionStateImpl(IdentityHashMap<Option<?>, Object> identityHashMap) {
        this.values = new IdentityHashMap(identityHashMap);
    }

    @Override
    public boolean has(@NotNull Option<?> option) {
        return this.values.containsKey(Objects.requireNonNull(option, "flag"));
    }

    @Override
    public <V> V value(@NotNull Option<V> option) {
        V v = option.type().cast(this.values.get(Objects.requireNonNull(option, "flag")));
        return v == null ? option.defaultValue() : v;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        OptionStateImpl optionStateImpl = (OptionStateImpl)object;
        return Objects.equals(this.values, optionStateImpl.values);
    }

    public int hashCode() {
        return Objects.hash(this.values);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{values=" + this.values + '}';
    }

    static final class VersionedBuilderImpl
    implements OptionState.VersionedBuilder {
        private final Map<Integer, BuilderImpl> builders = new TreeMap<Integer, BuilderImpl>();

        VersionedBuilderImpl() {
        }

        @Override
        public @NotNull OptionState.Versioned build() {
            if (this.builders.isEmpty()) {
                return new VersionedImpl(Collections.emptySortedMap(), 0, OptionState.emptyOptionState());
            }
            TreeMap<Integer, OptionState> treeMap = new TreeMap<Integer, OptionState>();
            for (Map.Entry<Integer, BuilderImpl> entry : this.builders.entrySet()) {
                treeMap.put(entry.getKey(), entry.getValue().build());
            }
            return new VersionedImpl(treeMap, (Integer)treeMap.lastKey(), VersionedImpl.flattened(treeMap, (Integer)treeMap.lastKey()));
        }

        @Override
        @NotNull
        public OptionState.VersionedBuilder version(int n2, @NotNull Consumer<OptionState.Builder> consumer) {
            Objects.requireNonNull(consumer, "versionBuilder").accept(this.builders.computeIfAbsent(n2, n -> new BuilderImpl()));
            return this;
        }
    }

    static final class BuilderImpl
    implements OptionState.Builder {
        private final IdentityHashMap<Option<?>, Object> values = new IdentityHashMap();

        BuilderImpl() {
        }

        @Override
        @NotNull
        public OptionState build() {
            if (this.values.isEmpty()) {
                return EMPTY;
            }
            return new OptionStateImpl(this.values);
        }

        @Override
        @NotNull
        public <V> OptionState.Builder value(@NotNull Option<V> option, @NotNull V v) {
            this.values.put(Objects.requireNonNull(option, "flag"), Objects.requireNonNull(v, "value"));
            return this;
        }

        @Override
        @NotNull
        public OptionState.Builder values(@NotNull OptionState optionState) {
            if (optionState instanceof OptionStateImpl) {
                this.values.putAll(((OptionStateImpl)optionState).values);
            } else if (optionState instanceof VersionedImpl) {
                this.values.putAll(((OptionStateImpl)((VersionedImpl)optionState).filtered).values);
            } else {
                throw new IllegalArgumentException("existing set " + optionState + " is of an unknown implementation type");
            }
            return this;
        }
    }

    static final class VersionedImpl
    implements OptionState.Versioned {
        private final SortedMap<Integer, OptionState> sets;
        private final int targetVersion;
        private final OptionState filtered;

        VersionedImpl(SortedMap<Integer, OptionState> sortedMap, int n, OptionState optionState) {
            this.sets = sortedMap;
            this.targetVersion = n;
            this.filtered = optionState;
        }

        @Override
        public boolean has(@NotNull Option<?> option) {
            return this.filtered.has(option);
        }

        @Override
        public <V> V value(@NotNull Option<V> option) {
            return this.filtered.value(option);
        }

        @Override
        @NotNull
        public Map<Integer, OptionState> childStates() {
            return Collections.unmodifiableSortedMap(this.sets.headMap(this.targetVersion + 1));
        }

        @Override
        @NotNull
        public OptionState.Versioned at(int n) {
            return new VersionedImpl(this.sets, n, VersionedImpl.flattened(this.sets, n));
        }

        public static OptionState flattened(SortedMap<Integer, OptionState> sortedMap, int n) {
            SortedMap<Integer, OptionState> sortedMap2 = sortedMap.headMap(n + 1);
            OptionState.Builder builder = OptionState.optionState();
            for (OptionState optionState : sortedMap2.values()) {
                builder.values(optionState);
            }
            return builder.build();
        }

        public boolean equals(@Nullable Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            VersionedImpl versionedImpl = (VersionedImpl)object;
            return this.targetVersion == versionedImpl.targetVersion && Objects.equals(this.sets, versionedImpl.sets) && Objects.equals(this.filtered, versionedImpl.filtered);
        }

        public int hashCode() {
            return Objects.hash(this.sets, this.targetVersion, this.filtered);
        }

        public String toString() {
            return this.getClass().getSimpleName() + "{sets=" + this.sets + ", targetVersion=" + this.targetVersion + ", filtered=" + this.filtered + '}';
        }
    }
}

