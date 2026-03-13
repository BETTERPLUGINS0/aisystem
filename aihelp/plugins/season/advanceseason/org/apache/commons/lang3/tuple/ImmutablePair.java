/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.tuple;

import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.tuple.Pair;

public class ImmutablePair<L, R>
extends Pair<L, R> {
    public static final ImmutablePair<?, ?>[] EMPTY_ARRAY = new ImmutablePair[0];
    private static final ImmutablePair NULL = new ImmutablePair<Object, Object>(null, null);
    private static final long serialVersionUID = 4954918890077093841L;
    public final L left;
    public final R right;

    public static <L, R> ImmutablePair<L, R>[] emptyArray() {
        return EMPTY_ARRAY;
    }

    public static <L, R> Pair<L, R> left(L l) {
        return ImmutablePair.of(l, null);
    }

    public static <L, R> ImmutablePair<L, R> nullPair() {
        return NULL;
    }

    public static <L, R> ImmutablePair<L, R> of(L l, R r) {
        return l != null || r != null ? new ImmutablePair<L, R>(l, r) : ImmutablePair.nullPair();
    }

    public static <L, R> ImmutablePair<L, R> of(Map.Entry<L, R> entry) {
        return entry != null ? new ImmutablePair<L, R>(entry.getKey(), entry.getValue()) : ImmutablePair.nullPair();
    }

    public static <L, R> ImmutablePair<L, R> ofNonNull(L l, R r) {
        return ImmutablePair.of(Objects.requireNonNull(l, "left"), Objects.requireNonNull(r, "right"));
    }

    public static <L, R> Pair<L, R> right(R r) {
        return ImmutablePair.of(null, r);
    }

    public ImmutablePair(L l, R r) {
        this.left = l;
        this.right = r;
    }

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public R getRight() {
        return this.right;
    }

    @Override
    public R setValue(R r) {
        throw new UnsupportedOperationException();
    }
}

