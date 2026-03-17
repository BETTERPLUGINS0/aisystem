/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.key;

import java.util.stream.Stream;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyedValue;
import net.kyori.examination.Examinable;
import net.kyori.examination.ExaminableProperty;
import net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class KeyedValueImpl<T>
implements Examinable,
KeyedValue<T> {
    private final Key key;
    private final T value;

    KeyedValueImpl(Key key, T t) {
        this.key = key;
        this.value = t;
    }

    @Override
    @NotNull
    public Key key() {
        return this.key;
    }

    @Override
    @NotNull
    public T value() {
        return this.value;
    }

    public boolean equals(@Nullable Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        KeyedValueImpl keyedValueImpl = (KeyedValueImpl)object;
        return this.key.equals(keyedValueImpl.key) && this.value.equals(keyedValueImpl.value);
    }

    public int hashCode() {
        int n = this.key.hashCode();
        n = 31 * n + this.value.hashCode();
        return n;
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("key", this.key), ExaminableProperty.of("value", this.value));
    }

    public String toString() {
        return this.examine(StringExaminer.simpleEscaping());
    }
}

