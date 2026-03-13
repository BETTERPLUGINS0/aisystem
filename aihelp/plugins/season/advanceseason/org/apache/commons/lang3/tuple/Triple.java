/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.tuple;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.tuple.ImmutableTriple;

public abstract class Triple<L, M, R>
implements Comparable<Triple<L, M, R>>,
Serializable {
    private static final long serialVersionUID = 1L;
    public static final Triple<?, ?, ?>[] EMPTY_ARRAY = new Triple[0];

    public static <L, M, R> Triple<L, M, R>[] emptyArray() {
        return EMPTY_ARRAY;
    }

    public static <L, M, R> Triple<L, M, R> of(L l, M m, R r) {
        return ImmutableTriple.of(l, m, r);
    }

    public static <L, M, R> Triple<L, M, R> ofNonNull(L l, M m, R r) {
        return ImmutableTriple.ofNonNull(l, m, r);
    }

    @Override
    public int compareTo(Triple<L, M, R> triple) {
        return new CompareToBuilder().append(this.getLeft(), triple.getLeft()).append(this.getMiddle(), triple.getMiddle()).append(this.getRight(), triple.getRight()).toComparison();
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Triple) {
            Triple triple = (Triple)object;
            return Objects.equals(this.getLeft(), triple.getLeft()) && Objects.equals(this.getMiddle(), triple.getMiddle()) && Objects.equals(this.getRight(), triple.getRight());
        }
        return false;
    }

    public abstract L getLeft();

    public abstract M getMiddle();

    public abstract R getRight();

    public int hashCode() {
        return Objects.hashCode(this.getLeft()) ^ Objects.hashCode(this.getMiddle()) ^ Objects.hashCode(this.getRight());
    }

    public String toString() {
        return "(" + this.getLeft() + "," + this.getMiddle() + "," + this.getRight() + ")";
    }

    public String toString(String string) {
        return String.format(string, this.getLeft(), this.getMiddle(), this.getRight());
    }
}

