/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers;

import net.advancedplugins.as.libs.apache.commons.math3.analysis.UnivariateFunction;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.AllowedSolution;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import net.advancedplugins.as.libs.apache.commons.math3.analysis.solvers.BrentSolver;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NoBracketingException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NotStrictlyPositiveException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NullArgumentException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.NumberIsTooLargeException;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.Localizable;
import net.advancedplugins.as.libs.apache.commons.math3.exception.util.LocalizedFormats;
import net.advancedplugins.as.libs.apache.commons.math3.util.FastMath;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class UnivariateSolverUtils {
    private UnivariateSolverUtils() {
    }

    public static double solve(UnivariateFunction univariateFunction, double d, double d2) {
        if (univariateFunction == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        BrentSolver brentSolver = new BrentSolver();
        return brentSolver.solve(Integer.MAX_VALUE, univariateFunction, d, d2);
    }

    public static double solve(UnivariateFunction univariateFunction, double d, double d2, double d3) {
        if (univariateFunction == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        BrentSolver brentSolver = new BrentSolver(d3);
        return brentSolver.solve(Integer.MAX_VALUE, univariateFunction, d, d2);
    }

    public static double forceSide(int n, UnivariateFunction univariateFunction, BracketedUnivariateSolver<UnivariateFunction> bracketedUnivariateSolver, double d, double d2, double d3, AllowedSolution allowedSolution) {
        if (allowedSolution == AllowedSolution.ANY_SIDE) {
            return d;
        }
        double d4 = FastMath.max(bracketedUnivariateSolver.getAbsoluteAccuracy(), FastMath.abs(d * bracketedUnivariateSolver.getRelativeAccuracy()));
        double d5 = FastMath.max(d2, d - d4);
        double d6 = univariateFunction.value(d5);
        double d7 = FastMath.min(d3, d + d4);
        double d8 = univariateFunction.value(d7);
        int n2 = n - 2;
        while (n2 > 0) {
            if (d6 >= 0.0 && d8 <= 0.0 || d6 <= 0.0 && d8 >= 0.0) {
                return bracketedUnivariateSolver.solve(n2, univariateFunction, d5, d7, d, allowedSolution);
            }
            boolean bl = false;
            boolean bl2 = false;
            if (d6 < d8) {
                if (d6 >= 0.0) {
                    bl = true;
                } else {
                    bl2 = true;
                }
            } else if (d6 > d8) {
                if (d6 <= 0.0) {
                    bl = true;
                } else {
                    bl2 = true;
                }
            } else {
                bl = true;
                bl2 = true;
            }
            if (bl) {
                d5 = FastMath.max(d2, d5 - d4);
                d6 = univariateFunction.value(d5);
                --n2;
            }
            if (!bl2) continue;
            d7 = FastMath.min(d3, d7 + d4);
            d8 = univariateFunction.value(d7);
            --n2;
        }
        throw new NoBracketingException((Localizable)LocalizedFormats.FAILED_BRACKETING, d5, d7, d6, d8, new Object[]{n - n2, n, d, d2, d3});
    }

    public static double[] bracket(UnivariateFunction univariateFunction, double d, double d2, double d3) {
        return UnivariateSolverUtils.bracket(univariateFunction, d, d2, d3, 1.0, 1.0, Integer.MAX_VALUE);
    }

    public static double[] bracket(UnivariateFunction univariateFunction, double d, double d2, double d3, int n) {
        return UnivariateSolverUtils.bracket(univariateFunction, d, d2, d3, 1.0, 1.0, n);
    }

    public static double[] bracket(UnivariateFunction univariateFunction, double d, double d2, double d3, double d4, double d5, int n) {
        if (univariateFunction == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        if (d4 <= 0.0) {
            throw new NotStrictlyPositiveException(d4);
        }
        if (n <= 0) {
            throw new NotStrictlyPositiveException((Localizable)LocalizedFormats.INVALID_MAX_ITERATIONS, n);
        }
        UnivariateSolverUtils.verifySequence(d2, d, d3);
        double d6 = d;
        double d7 = d;
        double d8 = Double.NaN;
        double d9 = Double.NaN;
        double d10 = 0.0;
        for (int i = 0; i < n && (d6 > d2 || d7 < d3); ++i) {
            double d11 = d6;
            double d12 = d8;
            double d13 = d7;
            double d14 = d9;
            d10 = d5 * d10 + d4;
            d6 = FastMath.max(d - d10, d2);
            d7 = FastMath.min(d + d10, d3);
            d8 = univariateFunction.value(d6);
            d9 = univariateFunction.value(d7);
            if (i == 0) {
                if (!(d8 * d9 <= 0.0)) continue;
                return new double[]{d6, d7};
            }
            if (d8 * d12 <= 0.0) {
                return new double[]{d6, d11};
            }
            if (!(d9 * d14 <= 0.0)) continue;
            return new double[]{d13, d7};
        }
        throw new NoBracketingException(d6, d7, d8, d9);
    }

    public static double midpoint(double d, double d2) {
        return (d + d2) * 0.5;
    }

    public static boolean isBracketing(UnivariateFunction univariateFunction, double d, double d2) {
        if (univariateFunction == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        double d3 = univariateFunction.value(d);
        double d4 = univariateFunction.value(d2);
        return d3 >= 0.0 && d4 <= 0.0 || d3 <= 0.0 && d4 >= 0.0;
    }

    public static boolean isSequence(double d, double d2, double d3) {
        return d < d2 && d2 < d3;
    }

    public static void verifyInterval(double d, double d2) {
        if (d >= d2) {
            throw new NumberIsTooLargeException((Localizable)LocalizedFormats.ENDPOINTS_NOT_AN_INTERVAL, (Number)d, d2, false);
        }
    }

    public static void verifySequence(double d, double d2, double d3) {
        UnivariateSolverUtils.verifyInterval(d, d2);
        UnivariateSolverUtils.verifyInterval(d2, d3);
    }

    public static void verifyBracketing(UnivariateFunction univariateFunction, double d, double d2) {
        if (univariateFunction == null) {
            throw new NullArgumentException(LocalizedFormats.FUNCTION, new Object[0]);
        }
        UnivariateSolverUtils.verifyInterval(d, d2);
        if (!UnivariateSolverUtils.isBracketing(univariateFunction, d, d2)) {
            throw new NoBracketingException(d, d2, univariateFunction.value(d), univariateFunction.value(d2));
        }
    }
}

