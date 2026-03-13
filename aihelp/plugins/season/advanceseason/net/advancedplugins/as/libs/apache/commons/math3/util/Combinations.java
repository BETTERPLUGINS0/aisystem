/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.DimensionMismatchException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathInternalError;
import net.advancedplugins.as.libs.apache.commons.math3.exception.OutOfRangeException;
import net.advancedplugins.as.libs.apache.commons.math3.util.ArithmeticUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.CombinatoricsUtils;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathArrays;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class Combinations
implements Iterable<int[]> {
    private final int n;
    private final int k;
    private final IterationOrder iterationOrder;

    public Combinations(int n, int n2) {
        this(n, n2, IterationOrder.LEXICOGRAPHIC);
    }

    private Combinations(int n, int n2, IterationOrder iterationOrder) {
        CombinatoricsUtils.checkBinomial(n, n2);
        this.n = n;
        this.k = n2;
        this.iterationOrder = iterationOrder;
    }

    public int getN() {
        return this.n;
    }

    public int getK() {
        return this.k;
    }

    @Override
    public Iterator<int[]> iterator() {
        if (this.k == 0 || this.k == this.n) {
            return new SingletonIterator(MathArrays.natural(this.k));
        }
        switch (this.iterationOrder) {
            case LEXICOGRAPHIC: {
                return new LexicographicIterator(this.n, this.k);
            }
        }
        throw new MathInternalError();
    }

    public Comparator<int[]> comparator() {
        return new LexicographicComparator(this.n, this.k);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class LexicographicComparator
    implements Comparator<int[]>,
    Serializable {
        private static final long serialVersionUID = 20130906L;
        private final int n;
        private final int k;

        LexicographicComparator(int n, int n2) {
            this.n = n;
            this.k = n2;
        }

        @Override
        public int compare(int[] nArray, int[] nArray2) {
            if (nArray.length != this.k) {
                throw new DimensionMismatchException(nArray.length, this.k);
            }
            if (nArray2.length != this.k) {
                throw new DimensionMismatchException(nArray2.length, this.k);
            }
            int[] nArray3 = MathArrays.copyOf(nArray);
            Arrays.sort(nArray3);
            int[] nArray4 = MathArrays.copyOf(nArray2);
            Arrays.sort(nArray4);
            long l = this.lexNorm(nArray3);
            long l2 = this.lexNorm(nArray4);
            if (l < l2) {
                return -1;
            }
            if (l > l2) {
                return 1;
            }
            return 0;
        }

        private long lexNorm(int[] nArray) {
            long l = 0L;
            for (int i = 0; i < nArray.length; ++i) {
                int n = nArray[i];
                if (n < 0 || n >= this.n) {
                    throw new OutOfRangeException(n, (Number)0, this.n - 1);
                }
                l += (long)(nArray[i] * ArithmeticUtils.pow(this.n, i));
            }
            return l;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class SingletonIterator
    implements Iterator<int[]> {
        private final int[] singleton;
        private boolean more = true;

        SingletonIterator(int[] nArray) {
            this.singleton = nArray;
        }

        @Override
        public boolean hasNext() {
            return this.more;
        }

        @Override
        public int[] next() {
            if (this.more) {
                this.more = false;
                return this.singleton;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class LexicographicIterator
    implements Iterator<int[]> {
        private final int k;
        private final int[] c;
        private boolean more = true;
        private int j;

        LexicographicIterator(int n, int n2) {
            this.k = n2;
            this.c = new int[n2 + 3];
            if (n2 == 0 || n2 >= n) {
                this.more = false;
                return;
            }
            for (int i = 1; i <= n2; ++i) {
                this.c[i] = i - 1;
            }
            this.c[n2 + 1] = n;
            this.c[n2 + 2] = 0;
            this.j = n2;
        }

        @Override
        public boolean hasNext() {
            return this.more;
        }

        @Override
        public int[] next() {
            if (!this.more) {
                throw new NoSuchElementException();
            }
            int[] nArray = new int[this.k];
            System.arraycopy(this.c, 1, nArray, 0, this.k);
            int n = 0;
            if (this.j > 0) {
                this.c[this.j] = n = this.j;
                --this.j;
                return nArray;
            }
            if (this.c[1] + 1 < this.c[2]) {
                this.c[1] = this.c[1] + 1;
                return nArray;
            }
            this.j = 2;
            boolean bl = false;
            while (!bl) {
                this.c[this.j - 1] = this.j - 2;
                n = this.c[this.j] + 1;
                if (n == this.c[this.j + 1]) {
                    ++this.j;
                    continue;
                }
                bl = true;
            }
            if (this.j > this.k) {
                this.more = false;
                return nArray;
            }
            this.c[this.j] = n;
            --this.j;
            return nArray;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static enum IterationOrder {
        LEXICOGRAPHIC;

    }
}

