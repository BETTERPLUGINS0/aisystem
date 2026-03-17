/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.option;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.kyori.option.Option;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class OptionImpl<V>
implements Option<V> {
    private static final Set<String> KNOWN_KEYS = ConcurrentHashMap.newKeySet();
    private final String id;
    private final Class<V> type;
    @Nullable
    private final V defaultValue;

    OptionImpl(@NotNull String string, @NotNull Class<V> clazz, @Nullable V v) {
        this.id = string;
        this.type = clazz;
        this.defaultValue = v;
    }

    static <T> Option<T> option(String string, Class<T> clazz, @Nullable T t) {
        if (!KNOWN_KEYS.add(string)) {
            throw new IllegalStateException("Key " + string + " has already been used. Option keys must be unique.");
        }
        return new OptionImpl<T>(Objects.requireNonNull(string, "id"), Objects.requireNonNull(clazz, "type"), t);
    }

    @Override
    @NotNull
    public String id() {
        return this.id;
    }

    @Override
    @NotNull
    public Class<V> type() {
        return this.type;
    }

    @Override
    @Nullable
    public V defaultValue() {
        return this.defaultValue;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        OptionImpl optionImpl = (OptionImpl)object;
        return Objects.equals(this.id, optionImpl.id) && Objects.equals(this.type, optionImpl.type);
    }

    public int hashCode() {
        return Objects.hash(this.id, this.type);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "{id=" + this.id + ",type=" + this.type + ",defaultValue=" + this.defaultValue + '}';
    }
}

