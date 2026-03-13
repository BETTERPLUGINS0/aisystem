/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.tuple;

import java.util.Objects;
import org.apache.commons.lang3.tuple.Triple;

public class ImmutableTriple<L, M, R>
extends Triple<L, M, R> {
    public static final ImmutableTriple<?, ?, ?>[] EMPTY_ARRAY = new ImmutableTriple[0];
    private static final ImmutableTriple NULL = new ImmutableTriple<Object, Object, Object>(null, null, null);
    private static final long serialVersionUID = 1L;
    public final L left;
    public final M middle;
    public final R right;

    public static <L, M, R> ImmutableTriple<L, M, R>[] emptyArray() {
        return EMPTY_ARRAY;
    }

    public static <L, M, R> ImmutableTriple<L, M, R> nullTriple() {
        return NULL;
    }

    public static <L, M, R> ImmutableTriple<L, M, R> of(L l, M m, R r) {
        return l != null | m != null || r != null ? new ImmutableTriple<L, M, R>(l, m, r) : ImmutableTriple.nullTriple();
    }

    public static <L, M, R> ImmutableTriple<L, M, R> ofNonNull(L l, M m, R r) {
        return ImmutableTriple.of(Objects.requireNonNull(l, "left"), Objects.requireNonNull(m, "middle"), Objects.requireNonNull(r, "right"));
    }

    public ImmutableTriple(L l, M m, R r) {
        this.left = l;
        this.middle = m;
        this.right = r;
    }

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public M getMiddle() {
        return this.middle;
    }

    @Override
    public R getRight() {
        return this.right;
    }
}

