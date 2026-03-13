/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.function.FailableBiConsumer;
import org.apache.commons.lang3.function.FailableBiFunction;
import org.apache.commons.lang3.tuple.ImmutablePair;

public abstract class Pair<L, R>
implements Map.Entry<L, R>,
Comparable<Pair<L, R>>,
Serializable {
    private static final long serialVersionUID = 4954918890077093841L;
    public static final Pair<?, ?>[] EMPTY_ARRAY = new Pair[0];

    public static <L, R> Pair<L, R>[] emptyArray() {
        return EMPTY_ARRAY;
    }

    public static <L, R> Pair<L, R> of(L l, R r) {
        return ImmutablePair.of(l, r);
    }

    public static <L, R> Pair<L, R> of(Map.Entry<L, R> entry) {
        return ImmutablePair.of(entry);
    }

    public static <L, R> Pair<L, R> ofNonNull(L l, R r) {
        return ImmutablePair.ofNonNull(l, r);
    }

    public <E extends Throwable> void accept(FailableBiConsumer<L, R, E> failableBiConsumer) {
        failableBiConsumer.accept(this.getKey(), this.getValue());
    }

    public <V, E extends Throwable> V apply(FailableBiFunction<L, R, V, E> failableBiFunction) {
        return failableBiFunction.apply(this.getKey(), this.getValue());
    }

    @Override
    public int compareTo(Pair<L, R> pair) {
        return new CompareToBuilder().append(this.getLeft(), pair.getLeft()).append(this.getRight(), pair.getRight()).toComparison();
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Map.Entry) {
            Map.Entry entry = (Map.Entry)object;
            return Objects.equals(this.getKey(), entry.getKey()) && Objects.equals(this.getValue(), entry.getValue());
        }
        return false;
    }

    @Override
    public final L getKey() {
        return this.getLeft();
    }

    public abstract L getLeft();

    public abstract R getRight();

    @Override
    public R getValue() {
        return this.getRight();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getKey()) ^ Objects.hashCode(this.getValue());
    }

    public String toString() {
        return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }

    public String toString(String string) {
        return String.format(string, this.getLeft(), this.getRight());
    }
}

