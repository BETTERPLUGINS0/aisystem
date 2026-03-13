/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers;

import net.advancedplugins.as.libs.apache.commons.math3.analysis.UnivariateFunction;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.BaseUnivariateSolver;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import net.advancedplugins.as.libs.apache.commons.math3.exception.MaxCountExceededException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.TooManyEvaluationsException;
import net.advancedplugins.as.libs.apache.commons.math3.util.IntegerSequence;
import net.advancedplugins.as.libs.apache.commons.math3.util.MathUtils;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public abstract class BaseAbstractUnivariateSolver<FUNC extends UnivariateFunction>
implements BaseUnivariateSolver<FUNC> {
    private static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-14;
    private static final double DEFAULT_FUNCTION_VALUE_ACCURACY = 1.0E-15;
    private final double functionValueAccuracy;
    private final double absoluteAccuracy;
    private final double relativeAccuracy;
    private IntegerSequence.Incrementor evaluations;
    private double searchMin;
    private double searchMax;
    private double searchStart;
    private FUNC function;

    protected BaseAbstractUnivariateSolver(double d) {
        this(1.0E-14, d, 1.0E-15);
    }

    protected BaseAbstractUnivariateSolver(double d, double d2) {
        this(d, d2, 1.0E-15);
    }

    protected BaseAbstractUnivariateSolver(double d, double d2, double d3) {
        this.absoluteAccuracy = d2;
        this.relativeAccuracy = d;
        this.functionValueAccuracy = d3;
        this.evaluations = IntegerSequence.Incrementor.create();
    }

    @Override
    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    @Override
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    public double getMin() {
        return this.searchMin;
    }

    public double getMax() {
        return this.searchMax;
    }

    public double getStartValue() {
        return this.searchStart;
    }

    @Override
    public double getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }

    @Override
    public double getRelativeAccuracy() {
        return this.relativeAccuracy;
    }

    @Override
    public double getFunctionValueAccuracy() {
        return this.functionValueAccuracy;
    }

    protected double computeObjectiveValue(double d) {
        this.incrementEvaluationCount();
        return this.function.value(d);
    }

    protected void setup(int n, FUNC FUNC, double d, double d2, double d3) {
        MathUtils.checkNotNull(FUNC);
        this.searchMin = d;
        this.searchMax = d2;
        this.searchStart = d3;
        this.function = FUNC;
        this.evaluations = this.evaluations.withMaximalCount(n).withStart(0);
    }

    @Override
    public double solve(int n, FUNC FUNC, double d, double d2, double d3) {
        this.setup(n, FUNC, d, d2, d3);
        return this.doSolve();
    }

    @Override
    public double solve(int n, FUNC FUNC, double d, double d2) {
        return this.solve(n, FUNC, d, d2, d + 0.5 * (d2 - d));
    }

    @Override
    public double solve(int n, FUNC FUNC, double d) {
        return this.solve(n, FUNC, Double.NaN, Double.NaN, d);
    }

    protected abstract double doSolve();

    protected boolean isBracketing(double d, double d2) {
        return UnivariateSolverUtils.isBracketing(this.function, d, d2);
    }

    protected boolean isSequence(double d, double d2, double d3) {
        return UnivariateSolverUtils.isSequence(d, d2, d3);
    }

    protected void verifyInterval(double d, double d2) {
        UnivariateSolverUtils.verifyInterval(d, d2);
    }

    protected void verifySequence(double d, double d2, double d3) {
        UnivariateSolverUtils.verifySequence(d, d2, d3);
    }

    protected void verifyBracketing(double d, double d2) {
        UnivariateSolverUtils.verifyBracketing(this.function, d, d2);
    }

    protected void incrementEvaluationCount() {
        try {
            this.evaluations.increment();
        } catch (MaxCountExceededException maxCountExceededException) {
            throw new TooManyEvaluationsException(maxCountExceededException.getMax());
        }
    }
}

