/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class Range<T>
implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Comparator<T> comparator;
    private transient int hashCode;
    private final T maximum;
    private final T minimum;
    private transient String toString;

    @Deprecated
    public static <T extends Comparable<? super T>> Range<T> between(T t, T t2) {
        return Range.of(t, t2, null);
    }

    @Deprecated
    public static <T> Range<T> between(T t, T t2, Comparator<T> comparator) {
        return new Range<T>(t, t2, comparator);
    }

    public static <T extends Comparable<? super T>> Range<T> is(T t) {
        return Range.of(t, t, null);
    }

    public static <T> Range<T> is(T t, Comparator<T> comparator) {
        return Range.of(t, t, comparator);
    }

    public static <T extends Comparable<? super T>> Range<T> of(T t, T t2) {
        return Range.of(t, t2, null);
    }

    public static <T> Range<T> of(T t, T t2, Comparator<T> comparator) {
        return new Range<T>(t, t2, comparator);
    }

    Range(T t, T t2, Comparator<T> comparator) {
        Objects.requireNonNull(t, "element1");
        Objects.requireNonNull(t2, "element2");
        this.comparator = comparator == null ? ComparableComparator.INSTANCE : comparator;
        if (this.comparator.compare(t, t2) < 1) {
            this.minimum = t;
            this.maximum = t2;
        } else {
            this.minimum = t2;
            this.maximum = t;
        }
    }

    public boolean contains(T t) {
        if (t == null) {
            return false;
        }
        return this.comparator.compare(t, this.minimum) > -1 && this.comparator.compare(t, this.maximum) < 1;
    }

    public boolean containsRange(Range<T> range) {
        if (range == null) {
            return false;
        }
        return this.contains(range.minimum) && this.contains(range.maximum);
    }

    public int elementCompareTo(T t) {
        Objects.requireNonNull(t, "element");
        if (this.isAfter(t)) {
            return -1;
        }
        if (this.isBefore(t)) {
            return 1;
        }
        return 0;
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != this.getClass()) {
            return false;
        }
        Range range = (Range)object;
        return this.minimum.equals(range.minimum) && this.maximum.equals(range.maximum);
    }

    public T fit(T t) {
        Objects.requireNonNull(t, "element");
        if (this.isAfter(t)) {
            return this.minimum;
        }
        if (this.isBefore(t)) {
            return this.maximum;
        }
        return t;
    }

    public Comparator<T> getComparator() {
        return this.comparator;
    }

    public T getMaximum() {
        return this.maximum;
    }

    public T getMinimum() {
        return this.minimum;
    }

    public int hashCode() {
        int n = this.hashCode;
        if (this.hashCode == 0) {
            n = 17;
            n = 37 * n + this.getClass().hashCode();
            n = 37 * n + this.minimum.hashCode();
            this.hashCode = n = 37 * n + this.maximum.hashCode();
        }
        return n;
    }

    public Range<T> intersectionWith(Range<T> range) {
        if (!this.isOverlappedBy(range)) {
            throw new IllegalArgumentException(String.format("Cannot calculate intersection with non-overlapping range %s", range));
        }
        if (this.equals(range)) {
            return this;
        }
        T t = this.getComparator().compare(this.minimum, range.minimum) < 0 ? range.minimum : this.minimum;
        T t2 = this.getComparator().compare(this.maximum, range.maximum) < 0 ? this.maximum : range.maximum;
        return Range.of(t, t2, this.getComparator());
    }

    public boolean isAfter(T t) {
        if (t == null) {
            return false;
        }
        return this.comparator.compare(t, this.minimum) < 0;
    }

    public boolean isAfterRange(Range<T> range) {
        if (range == null) {
            return false;
        }
        return this.isAfter(range.maximum);
    }

    public boolean isBefore(T t) {
        if (t == null) {
            return false;
        }
        return this.comparator.compare(t, this.maximum) > 0;
    }

    public boolean isBeforeRange(Range<T> range) {
        if (range == null) {
            return false;
        }
        return this.isBefore(range.minimum);
    }

    public boolean isEndedBy(T t) {
        if (t == null) {
            return false;
        }
        return this.comparator.compare(t, this.maximum) == 0;
    }

    public boolean isNaturalOrdering() {
        return this.comparator == ComparableComparator.INSTANCE;
    }

    public boolean isOverlappedBy(Range<T> range) {
        if (range == null) {
            return false;
        }
        return range.contains(this.minimum) || range.contains(this.maximum) || this.contains(range.minimum);
    }

    public boolean isStartedBy(T t) {
        if (t == null) {
            return false;
        }
        return this.comparator.compare(t, this.minimum) == 0;
    }

    public String toString() {
        if (this.toString == null) {
            this.toString = "[" + this.minimum + ".." + this.maximum + "]";
        }
        return this.toString;
    }

    public String toString(String string) {
        return String.format(string, this.minimum, this.maximum, this.comparator);
    }

    private static enum ComparableComparator implements Comparator
    {
        INSTANCE;


        public int compare(Object object, Object object2) {
            return ((Comparable)object).compareTo(object2);
        }
    }
}

