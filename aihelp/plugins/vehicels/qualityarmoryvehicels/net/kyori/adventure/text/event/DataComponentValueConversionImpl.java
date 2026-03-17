/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.kyori.adventure.text.event;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import net.kyori.adventure.internal.Internals;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.DataComponentValueConverterRegistry;
import net.kyori.examination.ExaminableProperty;
import org.jetbrains.annotations.NotNull;

final class DataComponentValueConversionImpl<I, O>
implements DataComponentValueConverterRegistry.Conversion<I, O> {
    private final Class<I> source;
    private final Class<O> destination;
    private final BiFunction<Key, I, O> conversion;

    DataComponentValueConversionImpl(@NotNull Class<I> clazz, @NotNull Class<O> clazz2, @NotNull BiFunction<Key, I, O> biFunction) {
        this.source = clazz;
        this.destination = clazz2;
        this.conversion = biFunction;
    }

    @Override
    @NotNull
    public Class<I> source() {
        return this.source;
    }

    @Override
    @NotNull
    public Class<O> destination() {
        return this.destination;
    }

    @Override
    @NotNull
    public O convert(@NotNull Key key, @NotNull I i) {
        return this.conversion.apply(Objects.requireNonNull(key, "key"), Objects.requireNonNull(i, "input"));
    }

    @Override
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("source", this.source), ExaminableProperty.of("destination", this.destination), ExaminableProperty.of("conversion", this.conversion));
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        DataComponentValueConversionImpl dataComponentValueConversionImpl = (DataComponentValueConversionImpl)object;
        return Objects.equals(this.source, dataComponentValueConversionImpl.source) && Objects.equals(this.destination, dataComponentValueConversionImpl.destination) && Objects.equals(this.conversion, dataComponentValueConversionImpl.conversion);
    }

    public int hashCode() {
        return Objects.hash(this.source, this.destination, this.conversion);
    }
}

