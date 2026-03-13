/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.util.Iterator;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathUnsupportedOperationException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MaxCountExceededException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NullArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.ZeroException;

public class IntegerSequence {
    private IntegerSequence() {
    }

    public static Range range(int n, int n2) {
        return IntegerSequence.range(n, n2, 1);
    }

    public static Range range(int n, int n2, int n3) {
        return new Range(n, n2, n3);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class Incrementor
    implements Iterator<Integer> {
        private static final MaxCountExceededCallback CALLBACK = new MaxCountExceededCallback(){

            public void trigger(int n) {
                throw new MaxCountExceededException(n);
            }
        };
        private final int init;
        private final int maximalCount;
        private final int increment;
        private final MaxCountExceededCallback maxCountCallback;
        private int count = 0;

        private Incrementor(int n, int n2, int n3, MaxCountExceededCallback maxCountExceededCallback) {
            if (maxCountExceededCallback == null) {
                throw new NullArgumentException();
            }
            this.init = n;
            this.maximalCount = n2;
            this.increment = n3;
            this.maxCountCallback = maxCountExceededCallback;
            this.count = n;
        }

        public static Incrementor create() {
            return new Incrementor(0, 0, 1, CALLBACK);
        }

        public Incrementor withStart(int n) {
            return new Incrementor(n, this.maximalCount, this.increment, this.maxCountCallback);
        }

        public Incrementor withMaximalCount(int n) {
            return new Incrementor(this.init, n, this.increment, this.maxCountCallback);
        }

        public Incrementor withIncrement(int n) {
            if (n == 0) {
                throw new ZeroException();
            }
            return new Incrementor(this.init, this.maximalCount, n, this.maxCountCallback);
        }

        public Incrementor withCallback(MaxCountExceededCallback maxCountExceededCallback) {
            return new Incrementor(this.init, this.maximalCount, this.increment, maxCountExceededCallback);
        }

        public int getMaximalCount() {
            return this.maximalCount;
        }

        public int getCount() {
            return this.count;
        }

        public boolean canIncrement() {
            return this.canIncrement(1);
        }

        public boolean canIncrement(int n) {
            int n2 = this.count + n * this.increment;
            return this.increment < 0 ? n2 > this.maximalCount : n2 < this.maximalCount;
        }

        public void increment(int n) {
            if (n <= 0) {
                throw new NotStrictlyPositiveException(n);
            }
            if (!this.canIncrement(0)) {
                this.maxCountCallback.trigger(this.maximalCount);
            }
            this.count += n * this.increment;
        }

        public void increment() {
            this.increment(1);
        }

        @Override
        public boolean hasNext() {
            return this.canIncrement(0);
        }

        @Override
        public Integer next() {
            int n = this.count;
            this.increment();
            return n;
        }

        @Override
        public void remove() {
            throw new MathUnsupportedOperationException();
        }

        public static interface MaxCountExceededCallback {
            public void trigger(int var1) throws MaxCountExceededException;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class Range
    implements Iterable<Integer> {
        private final int size;
        private final int start;
        private final int max;
        private final int step;

        public Range(int n, int n2, int n3) {
            this.start = n;
            this.max = n2;
            this.step = n3;
            int n4 = (n2 - n) / n3 + 1;
            this.size = n4 < 0 ? 0 : n4;
        }

        public int size() {
            return this.size;
        }

        @Override
        public Iterator<Integer> iterator() {
            return Incrementor.create().withStart(this.start).withMaximalCount(this.max + (this.step > 0 ? 1 : -1)).withIncrement(this.step);
        }
    }
}

