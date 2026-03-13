/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathIllegalStateException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MathInternalError;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooSmallException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.DoubleArray;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathArrays;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathUtils;

public class ResizableDoubleArray
implements DoubleArray,
Serializable {
    @Deprecated
    public static final int ADDITIVE_MODE = 1;
    @Deprecated
    public static final int MULTIPLICATIVE_MODE = 0;
    private static final long serialVersionUID = -3485529955529426875L;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_EXPANSION_FACTOR = 2.0;
    private static final double DEFAULT_CONTRACTION_DELTA = 0.5;
    private double contractionCriterion = 2.5;
    private double expansionFactor = 2.0;
    private ExpansionMode expansionMode = ExpansionMode.MULTIPLICATIVE;
    private double[] internalArray;
    private int numElements = 0;
    private int startIndex = 0;

    public ResizableDoubleArray() {
        this(16);
    }

    public ResizableDoubleArray(int n) {
        this(n, 2.0);
    }

    public ResizableDoubleArray(double[] dArray) {
        this(16, 2.0, 2.5, ExpansionMode.MULTIPLICATIVE, dArray);
    }

    @Deprecated
    public ResizableDoubleArray(int n, float f) {
        this(n, (double)f);
    }

    public ResizableDoubleArray(int n, double d) {
        this(n, d, 0.5 + d);
    }

    @Deprecated
    public ResizableDoubleArray(int n, float f, float f2) {
        this(n, (double)f, (double)f2);
    }

    public ResizableDoubleArray(int n, double d, double d2) {
        this(n, d, d2, ExpansionMode.MULTIPLICATIVE, null);
    }

    @Deprecated
    public ResizableDoubleArray(int n, float f, float f2, int n2) {
        this(n, f, f2, n2 == 1 ? ExpansionMode.ADDITIVE : ExpansionMode.MULTIPLICATIVE, null);
        this.setExpansionMode(n2);
    }

    public ResizableDoubleArray(int n, double d, double d2, ExpansionMode expansionMode, double ... dArray) {
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.INITIAL_CAPACITY_NOT_POSITIVE, n);
        }
        this.checkContractExpand(d2, d);
        this.expansionFactor = d;
        this.contractionCriterion = d2;
        this.expansionMode = expansionMode;
        this.internalArray = new double[n];
        this.numElements = 0;
        this.startIndex = 0;
        if (dArray != null && dArray.length > 0) {
            this.addElements(dArray);
        }
    }

    public ResizableDoubleArray(ResizableDoubleArray resizableDoubleArray) {
        MathUtils.checkNotNull(resizableDoubleArray);
        ResizableDoubleArray.copy(resizableDoubleArray, this);
    }

    public synchronized void addElement(double d) {
        if (this.internalArray.length <= this.startIndex + this.numElements) {
            this.expand();
        }
        this.internalArray[this.startIndex + this.numElements++] = d;
    }

    public synchronized void addElements(double[] dArray) {
        double[] dArray2 = new double[this.numElements + dArray.length + 1];
        System.arraycopy(this.internalArray, this.startIndex, dArray2, 0, this.numElements);
        System.arraycopy(dArray, 0, dArray2, this.numElements, dArray.length);
        this.internalArray = dArray2;
        this.startIndex = 0;
        this.numElements += dArray.length;
    }

    public synchronized double addElementRolling(double d) {
        double d2 = this.internalArray[this.startIndex];
        if (this.startIndex + (this.numElements + 1) > this.internalArray.length) {
            this.expand();
        }
        ++this.startIndex;
        this.internalArray[this.startIndex + (this.numElements - 1)] = d;
        if (this.shouldContract()) {
            this.contract();
        }
        return d2;
    }

    public synchronized double substituteMostRecentElement(double d) {
        if (this.numElements < 1) {
            throw new MathIllegalStateException(LocalizedFormats.CANNOT_SUBSTITUTE_ELEMENT_FROM_EMPTY_ARRAY, new Object[0]);
        }
        int n = this.startIndex + (this.numElements - 1);
        double d2 = this.internalArray[n];
        this.internalArray[n] = d;
        return d2;
    }

    @Deprecated
    protected void checkContractExpand(float f, float f2) {
        this.checkContractExpand((double)f, (double)f2);
    }

    protected void checkContractExpand(double d, double d2) {
        if (d < d2) {
            NumberIsTooSmallException numberIsTooSmallException = new NumberIsTooSmallException(d, (Number)1, true);
            numberIsTooSmallException.getContext().addMessage(LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_EXPANSION_FACTOR, d, d2);
            throw numberIsTooSmallException;
        }
        if (d <= 1.0) {
            NumberIsTooSmallException numberIsTooSmallException = new NumberIsTooSmallException(d, (Number)1, false);
            numberIsTooSmallException.getContext().addMessage(LocalizedFormats.CONTRACTION_CRITERIA_SMALLER_THAN_ONE, d);
            throw numberIsTooSmallException;
        }
        if (d2 <= 1.0) {
            NumberIsTooSmallException numberIsTooSmallException = new NumberIsTooSmallException(d, (Number)1, false);
            numberIsTooSmallException.getContext().addMessage(LocalizedFormats.EXPANSION_FACTOR_SMALLER_THAN_ONE, d2);
            throw numberIsTooSmallException;
        }
    }

    public synchronized void clear() {
        this.numElements = 0;
        this.startIndex = 0;
    }

    public synchronized void contract() {
        double[] dArray = new double[this.numElements + 1];
        System.arraycopy(this.internalArray, this.startIndex, dArray, 0, this.numElements);
        this.internalArray = dArray;
        this.startIndex = 0;
    }

    public synchronized void discardFrontElements(int n) {
        this.discardExtremeElements(n, true);
    }

    public synchronized void discardMostRecentElements(int n) {
        this.discardExtremeElements(n, false);
    }

    private synchronized void discardExtremeElements(int n, boolean bl) {
        if (n > this.numElements) {
            throw new MathIllegalArgumentException(LocalizedFormats.TOO_MANY_ELEMENTS_TO_DISCARD_FROM_ARRAY, n, this.numElements);
        }
        if (n < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_DISCARD_NEGATIVE_NUMBER_OF_ELEMENTS, n);
        }
        this.numElements -= n;
        if (bl) {
            this.startIndex += n;
        }
        if (this.shouldContract()) {
            this.contract();
        }
    }

    protected synchronized void expand() {
        int n = 0;
        n = this.expansionMode == ExpansionMode.MULTIPLICATIVE ? (int)FastMath.ceil((double)this.internalArray.length * this.expansionFactor) : (int)((long)this.internalArray.length + FastMath.round(this.expansionFactor));
        double[] dArray = new double[n];
        System.arraycopy(this.internalArray, 0, dArray, 0, this.internalArray.length);
        this.internalArray = dArray;
    }

    private synchronized void expandTo(int n) {
        double[] dArray = new double[n];
        System.arraycopy(this.internalArray, 0, dArray, 0, this.internalArray.length);
        this.internalArray = dArray;
    }

    @Deprecated
    public float getContractionCriteria() {
        return (float)this.getContractionCriterion();
    }

    public double getContractionCriterion() {
        return this.contractionCriterion;
    }

    public synchronized double getElement(int n) {
        if (n >= this.numElements) {
            throw new ArrayIndexOutOfBoundsException(n);
        }
        if (n >= 0) {
            return this.internalArray[this.startIndex + n];
        }
        throw new ArrayIndexOutOfBoundsException(n);
    }

    public synchronized double[] getElements() {
        double[] dArray = new double[this.numElements];
        System.arraycopy(this.internalArray, this.startIndex, dArray, 0, this.numElements);
        return dArray;
    }

    @Deprecated
    public float getExpansionFactor() {
        return (float)this.expansionFactor;
    }

    @Deprecated
    public int getExpansionMode() {
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            switch (this.expansionMode) {
                case MULTIPLICATIVE: {
                    return 0;
                }
                case ADDITIVE: {
                    return 1;
                }
            }
            throw new MathInternalError();
        }
    }

    @Deprecated
    synchronized int getInternalLength() {
        return this.internalArray.length;
    }

    public int getCapacity() {
        return this.internalArray.length;
    }

    public synchronized int getNumElements() {
        return this.numElements;
    }

    @Deprecated
    public synchronized double[] getInternalValues() {
        return this.internalArray;
    }

    protected double[] getArrayRef() {
        return this.internalArray;
    }

    protected int getStartIndex() {
        return this.startIndex;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public void setContractionCriteria(float f) {
        this.checkContractExpand(f, this.getExpansionFactor());
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            this.contractionCriterion = f;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public double compute(MathArrays.Function function) {
        int n;
        int n2;
        double[] dArray;
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            dArray = this.internalArray;
            n2 = this.startIndex;
            n = this.numElements;
        }
        return function.evaluate(dArray, n2, n);
    }

    public synchronized void setElement(int n, double d) {
        if (n < 0) {
            throw new ArrayIndexOutOfBoundsException(n);
        }
        if (n + 1 > this.numElements) {
            this.numElements = n + 1;
        }
        if (this.startIndex + n >= this.internalArray.length) {
            this.expandTo(this.startIndex + (n + 1));
        }
        this.internalArray[this.startIndex + n] = d;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public void setExpansionFactor(float f) {
        this.checkContractExpand(this.getContractionCriterion(), (double)f);
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            this.expansionFactor = f;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public void setExpansionMode(int n) {
        if (n != 0 && n != 1) {
            throw new MathIllegalArgumentException(LocalizedFormats.UNSUPPORTED_EXPANSION_MODE, n, 0, "MULTIPLICATIVE_MODE", 1, "ADDITIVE_MODE");
        }
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            if (n == 0) {
                this.setExpansionMode(ExpansionMode.MULTIPLICATIVE);
            } else if (n == 1) {
                this.setExpansionMode(ExpansionMode.ADDITIVE);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Deprecated
    public void setExpansionMode(ExpansionMode expansionMode) {
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            this.expansionMode = expansionMode;
        }
    }

    @Deprecated
    protected void setInitialCapacity(int n) {
    }

    public synchronized void setNumElements(int n) {
        if (n < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.INDEX_NOT_POSITIVE, n);
        }
        int n2 = this.startIndex + n;
        if (n2 > this.internalArray.length) {
            this.expandTo(n2);
        }
        this.numElements = n;
    }

    private synchronized boolean shouldContract() {
        if (this.expansionMode == ExpansionMode.MULTIPLICATIVE) {
            return (double)((float)this.internalArray.length / (float)this.numElements) > this.contractionCriterion;
        }
        return (double)(this.internalArray.length - this.numElements) > this.contractionCriterion;
    }

    @Deprecated
    public synchronized int start() {
        return this.startIndex;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void copy(ResizableDoubleArray resizableDoubleArray, ResizableDoubleArray resizableDoubleArray2) {
        MathUtils.checkNotNull(resizableDoubleArray);
        MathUtils.checkNotNull(resizableDoubleArray2);
        ResizableDoubleArray resizableDoubleArray3 = resizableDoubleArray;
        synchronized (resizableDoubleArray3) {
            ResizableDoubleArray resizableDoubleArray4 = resizableDoubleArray2;
            synchronized (resizableDoubleArray4) {
                resizableDoubleArray2.contractionCriterion = resizableDoubleArray.contractionCriterion;
                resizableDoubleArray2.expansionFactor = resizableDoubleArray.expansionFactor;
                resizableDoubleArray2.expansionMode = resizableDoubleArray.expansionMode;
                resizableDoubleArray2.internalArray = new double[resizableDoubleArray.internalArray.length];
                System.arraycopy(resizableDoubleArray.internalArray, 0, resizableDoubleArray2.internalArray, 0, resizableDoubleArray2.internalArray.length);
                resizableDoubleArray2.numElements = resizableDoubleArray.numElements;
                resizableDoubleArray2.startIndex = resizableDoubleArray.startIndex;
            }
        }
    }

    public synchronized ResizableDoubleArray copy() {
        ResizableDoubleArray resizableDoubleArray = new ResizableDoubleArray();
        ResizableDoubleArray.copy(this, resizableDoubleArray);
        return resizableDoubleArray;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ResizableDoubleArray)) {
            return false;
        }
        ResizableDoubleArray resizableDoubleArray = this;
        synchronized (resizableDoubleArray) {
            Object object2 = object;
            synchronized (object2) {
                boolean bl = true;
                ResizableDoubleArray resizableDoubleArray2 = (ResizableDoubleArray)object;
                bl = bl && resizableDoubleArray2.contractionCriterion == this.contractionCriterion;
                bl = bl && resizableDoubleArray2.expansionFactor == this.expansionFactor;
                bl = bl && resizableDoubleArray2.expansionMode == this.expansionMode;
                bl = bl && resizableDoubleArray2.numElements == this.numElements;
                boolean bl2 = bl = bl && resizableDoubleArray2.startIndex == this.startIndex;
                if (!bl) {
                    return false;
                }
                return Arrays.equals(this.internalArray, resizableDoubleArray2.internalArray);
            }
        }
    }

    public synchronized int hashCode() {
        int[] nArray = new int[]{Double.valueOf(this.expansionFactor).hashCode(), Double.valueOf(this.contractionCriterion).hashCode(), this.expansionMode.hashCode(), Arrays.hashCode(this.internalArray), this.numElements, this.startIndex};
        return Arrays.hashCode(nArray);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum ExpansionMode {
        MULTIPLICATIVE,
        ADDITIVE;

    }
}

