/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers;

import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.AbstractUnivariateSolver;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NoBracketingException;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;
import net.advancedplugins.as.libs.apache.commons.math3.util.Precision;

public class BrentSolver
extends AbstractUnivariateSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6;

    public BrentSolver() {
        this(1.0E-6);
    }

    public BrentSolver(double d) {
        super(d);
    }

    public BrentSolver(double d, double d2) {
        super(d, d2);
    }

    public BrentSolver(double d, double d2, double d3) {
        super(d, d2, d3);
    }

    protected double doSolve() {
        double d = this.getMin();
        double d2 = this.getMax();
        double d3 = this.getStartValue();
        double d4 = this.getFunctionValueAccuracy();
        this.verifySequence(d, d3, d2);
        double d5 = this.computeObjectiveValue(d3);
        if (FastMath.abs(d5) <= d4) {
            return d3;
        }
        double d6 = this.computeObjectiveValue(d);
        if (FastMath.abs(d6) <= d4) {
            return d;
        }
        if (d5 * d6 < 0.0) {
            return this.brent(d, d3, d6, d5);
        }
        double d7 = this.computeObjectiveValue(d2);
        if (FastMath.abs(d7) <= d4) {
            return d2;
        }
        if (d5 * d7 < 0.0) {
            return this.brent(d3, d2, d5, d7);
        }
        throw new NoBracketingException(d, d2, d6, d7);
    }

    private double brent(double d, double d2, double d3, double d4) {
        double d5;
        double d6 = d;
        double d7 = d3;
        double d8 = d2;
        double d9 = d4;
        double d10 = d6;
        double d11 = d7;
        double d12 = d5 = d8 - d6;
        double d13 = this.getAbsoluteAccuracy();
        double d14 = this.getRelativeAccuracy();
        while (true) {
            if (FastMath.abs(d11) < FastMath.abs(d9)) {
                d6 = d8;
                d8 = d10;
                d10 = d6;
                d7 = d9;
                d9 = d11;
                d11 = d7;
            }
            double d15 = 2.0 * d14 * FastMath.abs(d8) + d13;
            double d16 = 0.5 * (d10 - d8);
            if (FastMath.abs(d16) <= d15 || Precision.equals(d9, 0.0)) {
                return d8;
            }
            if (FastMath.abs(d12) < d15 || FastMath.abs(d7) <= FastMath.abs(d9)) {
                d12 = d5 = d16;
            } else {
                double d17;
                double d18;
                double d19 = d9 / d7;
                if (d6 == d10) {
                    d18 = 2.0 * d16 * d19;
                    d17 = 1.0 - d19;
                } else {
                    d17 = d7 / d11;
                    double d20 = d9 / d11;
                    d18 = d19 * (2.0 * d16 * d17 * (d17 - d20) - (d8 - d6) * (d20 - 1.0));
                    d17 = (d17 - 1.0) * (d20 - 1.0) * (d19 - 1.0);
                }
                if (d18 > 0.0) {
                    d17 = -d17;
                } else {
                    d18 = -d18;
                }
                d19 = d12;
                d12 = d5;
                if (d18 >= 1.5 * d16 * d17 - FastMath.abs(d15 * d17) || d18 >= FastMath.abs(0.5 * d19 * d17)) {
                    d12 = d5 = d16;
                } else {
                    d5 = d18 / d17;
                }
            }
            d6 = d8;
            d7 = d9;
            d8 = FastMath.abs(d5) > d15 ? (d8 += d5) : (d16 > 0.0 ? (d8 += d15) : (d8 -= d15));
            d9 = this.computeObjectiveValue(d8);
            if (!(d9 > 0.0 && d11 > 0.0) && (!(d9 <= 0.0) || !(d11 <= 0.0))) continue;
            d10 = d6;
            d11 = d7;
            d12 = d5 = d8 - d6;
        }
    }
}

