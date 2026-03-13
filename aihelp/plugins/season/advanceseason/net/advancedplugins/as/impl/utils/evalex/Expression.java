/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import net.advancedplugins.as.impl.utils.evalex.AbstractFunction;
import net.advancedplugins.as.impl.utils.evalex.AbstractLazyFunction;
import net.advancedplugins.as.impl.utils.evalex.AbstractOperator;
import net.advancedplugins.as.impl.utils.evalex.AbstractUnaryOperator;
import net.advancedplugins.as.impl.utils.evalex.ExpressionSettings;
import net.advancedplugins.as.impl.utils.evalex.LazyIfNumber;
import net.advancedplugins.as.impl.utils.evalex.LazyOperator;

public class Expression {
    public static final int OPERATOR_PRECEDENCE_UNARY = 60;
    public static final int OPERATOR_PRECEDENCE_EQUALITY = 7;
    public static final int OPERATOR_PRECEDENCE_COMPARISON = 10;
    public static final int OPERATOR_PRECEDENCE_OR = 2;
    public static final int OPERATOR_PRECEDENCE_AND = 4;
    public static final int OPERATOR_PRECEDENCE_POWER = 40;
    public static final int OPERATOR_PRECEDENCE_POWER_HIGHER = 80;
    public static final int OPERATOR_PRECEDENCE_MULTIPLICATIVE = 30;
    public static final int OPERATOR_PRECEDENCE_ADDITIVE = 20;
    public static final BigDecimal PI = new BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679");
    public static final BigDecimal e = new BigDecimal("2.71828182845904523536028747135266249775724709369995957496696762772407663");
    public static final String MISSING_PARAMETERS_FOR_OPERATOR = "Missing parameter(s) for operator ";
    private MathContext mc = null;
    private int powerOperatorPrecedence = 40;
    private String firstVarChars = "_";
    private String varChars = "_";
    private final String originalExpression;
    private String expressionString = null;
    private List<Token> rpn = null;
    protected Map<String, LazyOperator> operators = new TreeMap<String, LazyOperator>(String.CASE_INSENSITIVE_ORDER);
    protected Map<String, net.advancedplugins.as.impl.utils.evalex.LazyFunction> functions = new TreeMap<String, net.advancedplugins.as.impl.utils.evalex.LazyFunction>(String.CASE_INSENSITIVE_ORDER);
    protected Map<String, LazyNumber> variables = new TreeMap<String, LazyNumber>(String.CASE_INSENSITIVE_ORDER);
    private static final char DECIMAL_SEPARATOR = '.';
    private static final char MINUS_SIGN = '-';
    private static final LazyNumber PARAMS_START = new LazyNumber(){

        @Override
        public BigDecimal eval() {
            return null;
        }

        @Override
        public String getString() {
            return null;
        }
    };

    protected LazyNumber createLazyNumber(final BigDecimal bigDecimal) {
        return new LazyNumber(){

            @Override
            public String getString() {
                return bigDecimal.toPlainString();
            }

            @Override
            public BigDecimal eval() {
                return bigDecimal;
            }
        };
    }

    public Expression(String string) {
        this(string, MathContext.DECIMAL32);
    }

    public Expression(String string, MathContext mathContext) {
        this(string, ExpressionSettings.builder().mathContext(mathContext).build());
    }

    public Expression(String string, ExpressionSettings expressionSettings) {
        this.mc = expressionSettings.getMathContext();
        this.powerOperatorPrecedence = expressionSettings.getPowerOperatorPrecedence();
        this.expressionString = string;
        this.originalExpression = string;
        this.addOperator(new Operator("+", 20, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.add(bigDecimal2, Expression.this.mc);
            }
        });
        this.addOperator(new Operator("-", 20, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.subtract(bigDecimal2, Expression.this.mc);
            }
        });
        this.addOperator(new Operator("*", 30, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.multiply(bigDecimal2, Expression.this.mc);
            }
        });
        this.addOperator(new Operator("/", 30, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.divide(bigDecimal2, Expression.this.mc);
            }
        });
        this.addOperator(new Operator("%", 30, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.remainder(bigDecimal2, Expression.this.mc);
            }
        });
        this.addOperator(new Operator("^", this.powerOperatorPrecedence, false){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                int n = bigDecimal2.signum();
                double d = bigDecimal.doubleValue();
                bigDecimal2 = bigDecimal2.multiply(new BigDecimal(n));
                BigDecimal bigDecimal3 = bigDecimal2.remainder(BigDecimal.ONE);
                BigDecimal bigDecimal4 = bigDecimal2.subtract(bigDecimal3);
                BigDecimal bigDecimal5 = bigDecimal.pow(bigDecimal4.intValueExact(), Expression.this.mc);
                BigDecimal bigDecimal6 = BigDecimal.valueOf(Math.pow(d, bigDecimal3.doubleValue()));
                BigDecimal bigDecimal7 = bigDecimal5.multiply(bigDecimal6, Expression.this.mc);
                if (n == -1) {
                    bigDecimal7 = BigDecimal.ONE.divide(bigDecimal7, Expression.this.mc.getPrecision(), RoundingMode.HALF_UP);
                }
                return bigDecimal7;
            }
        });
        this.addOperator(new Operator("&&", 4, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                boolean bl;
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                boolean bl2 = bl = bigDecimal.compareTo(BigDecimal.ZERO) != 0;
                if (!bl) {
                    return BigDecimal.ZERO;
                }
                boolean bl3 = bigDecimal2.compareTo(BigDecimal.ZERO) != 0;
                return bl3 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator("||", 2, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                boolean bl;
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                boolean bl2 = bl = bigDecimal.compareTo(BigDecimal.ZERO) != 0;
                if (bl) {
                    return BigDecimal.ONE;
                }
                boolean bl3 = bigDecimal2.compareTo(BigDecimal.ZERO) != 0;
                return bl3 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator(">", 10, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.compareTo(bigDecimal2) > 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator(">=", 10, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.compareTo(bigDecimal2) >= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator("<", 10, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.compareTo(bigDecimal2) < 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator("<=", 10, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return bigDecimal.compareTo(bigDecimal2) <= 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator("=", 7, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                if (bigDecimal == bigDecimal2) {
                    return BigDecimal.ONE;
                }
                if (bigDecimal == null || bigDecimal2 == null) {
                    return BigDecimal.ZERO;
                }
                return bigDecimal.compareTo(bigDecimal2) == 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator("==", 7, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                return ((Operator)Expression.this.operators.get("=")).eval(bigDecimal, bigDecimal2);
            }
        });
        this.addOperator(new Operator("!=", 7, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                if (bigDecimal == bigDecimal2) {
                    return BigDecimal.ZERO;
                }
                if (bigDecimal == null || bigDecimal2 == null) {
                    return BigDecimal.ONE;
                }
                return bigDecimal.compareTo(bigDecimal2) != 0 ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addOperator(new Operator("<>", 7, false, true){

            @Override
            public BigDecimal eval(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
                Expression.this.assertNotNull(bigDecimal, bigDecimal2);
                return ((Operator)Expression.this.operators.get("!=")).eval(bigDecimal, bigDecimal2);
            }
        });
        this.addOperator(new UnaryOperator("-", 60, false){

            @Override
            public BigDecimal evalUnary(BigDecimal bigDecimal) {
                return bigDecimal.multiply(new BigDecimal(-1));
            }
        });
        this.addOperator(new UnaryOperator("+", 60, false){

            @Override
            public BigDecimal evalUnary(BigDecimal bigDecimal) {
                return bigDecimal.multiply(BigDecimal.ONE);
            }
        });
        this.addFunction(new Function("FACT", 1, false){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                int n = list.get(0).intValue();
                BigDecimal bigDecimal = BigDecimal.ONE;
                for (int i = 1; i <= n; ++i) {
                    bigDecimal = bigDecimal.multiply(new BigDecimal(i));
                }
                return bigDecimal;
            }
        });
        this.addFunction(new Function("NOT", 1, true){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                boolean bl = list.get(0).compareTo(BigDecimal.ZERO) == 0;
                return bl ? BigDecimal.ONE : BigDecimal.ZERO;
            }
        });
        this.addLazyFunction(new LazyFunction("IF", 3){

            @Override
            public LazyNumber lazyEval(List<LazyNumber> list) {
                return new LazyIfNumber(list);
            }
        });
        this.addFunction(new Function("RANDOM", 0){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                double d = Math.random();
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("SINR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.sin(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("COSR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.cos(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("TANR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.tan(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("COTR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.tan(list.get(0).doubleValue());
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("SECR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.cos(list.get(0).doubleValue());
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("CSCR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.sin(list.get(0).doubleValue());
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("SIN", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.sin(Math.toRadians(list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("COS", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.cos(Math.toRadians(list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("TAN", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.tan(Math.toRadians(list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("COT", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.tan(Math.toRadians(list.get(0).doubleValue()));
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("SEC", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.cos(Math.toRadians(list.get(0).doubleValue()));
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("CSC", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.sin(Math.toRadians(list.get(0).doubleValue()));
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ASINR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.asin(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ACOSR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.acos(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ATANR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.atan(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ACOTR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                if (list.get(0).doubleValue() == 0.0) {
                    throw new ExpressionException("Number must not be 0");
                }
                double d = Math.atan(1.0 / list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ATAN2R", 2){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0), list.get(1));
                double d = Math.atan2(list.get(0).doubleValue(), list.get(1).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ASIN", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.toDegrees(Math.asin(list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ACOS", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.toDegrees(Math.acos(list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ATAN", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.toDegrees(Math.atan(list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ACOT", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                if (list.get(0).doubleValue() == 0.0) {
                    throw new ExpressionException("Number must not be 0");
                }
                double d = Math.toDegrees(Math.atan(1.0 / list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ATAN2", 2){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0), list.get(1));
                double d = Math.toDegrees(Math.atan2(list.get(0).doubleValue(), list.get(1).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("SINH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.sinh(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("COSH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.cosh(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("TANH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.tanh(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("SECH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.cosh(list.get(0).doubleValue());
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("CSCH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.sinh(list.get(0).doubleValue());
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("COTH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = 1.0;
                double d2 = Math.tanh(list.get(0).doubleValue());
                return new BigDecimal(d / d2, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ASINH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.log(list.get(0).doubleValue() + Math.sqrt(Math.pow(list.get(0).doubleValue(), 2.0) + 1.0));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ACOSH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                if (Double.compare(list.get(0).doubleValue(), 1.0) < 0) {
                    throw new ExpressionException("Number must be x >= 1");
                }
                double d = Math.log(list.get(0).doubleValue() + Math.sqrt(Math.pow(list.get(0).doubleValue(), 2.0) - 1.0));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ATANH", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                if (Math.abs(list.get(0).doubleValue()) > 1.0 || Math.abs(list.get(0).doubleValue()) == 1.0) {
                    throw new ExpressionException("Number must be |x| < 1");
                }
                double d = 0.5 * Math.log((1.0 + list.get(0).doubleValue()) / (1.0 - list.get(0).doubleValue()));
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("RAD", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.toRadians(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("DEG", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.toDegrees(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("MAX", -1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                if (list.isEmpty()) {
                    throw new ExpressionException("MAX requires at least one parameter");
                }
                BigDecimal bigDecimal = null;
                for (BigDecimal bigDecimal2 : list) {
                    Expression.this.assertNotNull(bigDecimal2);
                    if (bigDecimal != null && bigDecimal2.compareTo(bigDecimal) <= 0) continue;
                    bigDecimal = bigDecimal2;
                }
                return bigDecimal;
            }
        });
        this.addFunction(new Function("MIN", -1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                if (list.isEmpty()) {
                    throw new ExpressionException("MIN requires at least one parameter");
                }
                BigDecimal bigDecimal = null;
                for (BigDecimal bigDecimal2 : list) {
                    Expression.this.assertNotNull(bigDecimal2);
                    if (bigDecimal != null && bigDecimal2.compareTo(bigDecimal) >= 0) continue;
                    bigDecimal = bigDecimal2;
                }
                return bigDecimal;
            }
        });
        this.addFunction(new Function("ABS", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                return list.get(0).abs(Expression.this.mc);
            }
        });
        this.addFunction(new Function("LOG", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.log(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("LOG10", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                double d = Math.log10(list.get(0).doubleValue());
                return new BigDecimal(d, Expression.this.mc);
            }
        });
        this.addFunction(new Function("ROUND", 2){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0), list.get(1));
                BigDecimal bigDecimal = list.get(0);
                int n = list.get(1).intValue();
                return bigDecimal.setScale(n, Expression.this.mc.getRoundingMode());
            }
        });
        this.addFunction(new Function("FLOOR", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                BigDecimal bigDecimal = list.get(0);
                return bigDecimal.setScale(0, RoundingMode.FLOOR);
            }
        });
        this.addFunction(new Function("CEILING", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                Expression.this.assertNotNull(list.get(0));
                BigDecimal bigDecimal = list.get(0);
                return bigDecimal.setScale(0, RoundingMode.CEILING);
            }
        });
        this.addFunction(new Function("SQRT", 1){

            @Override
            public BigDecimal eval(List<BigDecimal> list) {
                BigInteger bigInteger;
                BigInteger bigInteger2;
                Expression.this.assertNotNull(list.get(0));
                BigDecimal bigDecimal = list.get(0);
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                    return new BigDecimal(0);
                }
                if (bigDecimal.signum() < 0) {
                    throw new ExpressionException("Argument to SQRT() function must not be negative");
                }
                BigInteger bigInteger3 = bigDecimal.movePointRight(Expression.this.mc.getPrecision() << 1).toBigInteger();
                int n = bigInteger3.bitLength() + 1 >> 1;
                BigInteger bigInteger4 = bigInteger3.shiftRight(n);
                do {
                    bigInteger = bigInteger4;
                    bigInteger4 = bigInteger4.add(bigInteger3.divide(bigInteger4)).shiftRight(1);
                    Thread.yield();
                } while ((bigInteger2 = bigInteger4.subtract(bigInteger).abs()).compareTo(BigInteger.ZERO) != 0 && bigInteger2.compareTo(BigInteger.ONE) != 0);
                return new BigDecimal(bigInteger4, Expression.this.mc.getPrecision());
            }
        });
        this.variables.put("e", this.createLazyNumber(e));
        this.variables.put("PI", this.createLazyNumber(PI));
        this.variables.put("NULL", null);
        this.variables.put("TRUE", this.createLazyNumber(BigDecimal.ONE));
        this.variables.put("FALSE", this.createLazyNumber(BigDecimal.ZERO));
    }

    protected void assertNotNull(BigDecimal bigDecimal) {
        if (bigDecimal == null) {
            throw new ArithmeticException("Operand may not be null");
        }
    }

    protected void assertNotNull(BigDecimal bigDecimal, BigDecimal bigDecimal2) {
        if (bigDecimal == null) {
            throw new ArithmeticException("First operand may not be null");
        }
        if (bigDecimal2 == null) {
            throw new ArithmeticException("Second operand may not be null");
        }
    }

    protected boolean isNumber(String string) {
        if (string.charAt(0) == '-' && string.length() == 1) {
            return false;
        }
        if (string.charAt(0) == '+' && string.length() == 1) {
            return false;
        }
        if (!(string.charAt(0) != '.' || string.length() != 1 && Character.isDigit(string.charAt(1)))) {
            return false;
        }
        if (string.charAt(0) == 'e' || string.charAt(0) == 'E') {
            return false;
        }
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c) || c == '-' || c == '.' || c == 'e' || c == 'E' || c == '+') continue;
            return false;
        }
        return true;
    }

    private List<Token> shuntingYard(String string) {
        Token token;
        ArrayList<Token> arrayList = new ArrayList<Token>();
        Stack<Token> stack = new Stack<Token>();
        Tokenizer tokenizer = new Tokenizer(string);
        Token token2 = null;
        Token token3 = null;
        while (tokenizer.hasNext()) {
            token = tokenizer.next();
            switch (token.type.ordinal()) {
                case 9: {
                    stack.push(token);
                    break;
                }
                case 2: 
                case 8: {
                    if (token3 != null && (token3.type == TokenType.LITERAL || token3.type == TokenType.HEX_LITERAL)) {
                        throw new ExpressionException("Missing operator", token.pos);
                    }
                    arrayList.add(token);
                    break;
                }
                case 0: {
                    arrayList.add(token);
                    break;
                }
                case 1: {
                    stack.push(token);
                    token2 = token;
                    break;
                }
                case 6: {
                    if (token3 != null && token3.type == TokenType.OPERATOR) {
                        throw new ExpressionException(MISSING_PARAMETERS_FOR_OPERATOR + String.valueOf(token3), token3.pos);
                    }
                    while (!stack.isEmpty() && ((Token)stack.peek()).type != TokenType.OPEN_PAREN) {
                        arrayList.add((Token)stack.pop());
                    }
                    if (!stack.isEmpty()) break;
                    if (token2 == null) {
                        throw new ExpressionException("Unexpected comma", token.pos);
                    }
                    throw new ExpressionException("Parse error for function " + String.valueOf(token2), token.pos);
                }
                case 3: {
                    if (token3 != null && (token3.type == TokenType.COMMA || token3.type == TokenType.OPEN_PAREN)) {
                        throw new ExpressionException(MISSING_PARAMETERS_FOR_OPERATOR + String.valueOf(token), token.pos);
                    }
                    Object object = this.operators.get(token.surface);
                    if (object == null) {
                        throw new ExpressionException("Unknown operator " + String.valueOf(token), token.pos + 1);
                    }
                    this.shuntOperators(arrayList, stack, (LazyOperator)object);
                    stack.push(token);
                    break;
                }
                case 4: {
                    if (token3 != null && token3.type != TokenType.OPERATOR && token3.type != TokenType.COMMA && token3.type != TokenType.OPEN_PAREN && token3.type != TokenType.UNARY_OPERATOR) {
                        throw new ExpressionException("Invalid position for unary operator " + String.valueOf(token), token.pos);
                    }
                    Object object = this.operators.get(token.surface);
                    if (object == null) {
                        throw new ExpressionException("Unknown unary operator " + token.surface.substring(0, token.surface.length() - 1), token.pos + 1);
                    }
                    this.shuntOperators(arrayList, stack, (LazyOperator)object);
                    stack.push(token);
                    break;
                }
                case 5: {
                    Object object;
                    if (token3 != null) {
                        if (token3.type == TokenType.LITERAL || token3.type == TokenType.CLOSE_PAREN || token3.type == TokenType.VARIABLE || token3.type == TokenType.HEX_LITERAL) {
                            object = new Token();
                            ((Token)object).append("*");
                            ((Token)object).type = TokenType.OPERATOR;
                            stack.push((Token)object);
                        }
                        if (token3.type == TokenType.FUNCTION) {
                            arrayList.add(token);
                        }
                    }
                    stack.push(token);
                    break;
                }
                case 7: {
                    if (token3 != null && token3.type == TokenType.OPERATOR) {
                        throw new ExpressionException(MISSING_PARAMETERS_FOR_OPERATOR + String.valueOf(token3), token3.pos);
                    }
                    while (!stack.isEmpty() && ((Token)stack.peek()).type != TokenType.OPEN_PAREN) {
                        arrayList.add((Token)stack.pop());
                    }
                    if (stack.isEmpty()) {
                        throw new ExpressionException("Mismatched parentheses");
                    }
                    stack.pop();
                    if (stack.isEmpty() || stack.peek().type != TokenType.FUNCTION) break;
                    arrayList.add(stack.pop());
                }
            }
            token3 = token;
        }
        while (!stack.isEmpty()) {
            token = (Token)stack.pop();
            if (token.type == TokenType.OPEN_PAREN || token.type == TokenType.CLOSE_PAREN) {
                throw new ExpressionException("Mismatched parentheses");
            }
            arrayList.add(token);
        }
        return arrayList;
    }

    private void shuntOperators(List<Token> list, Stack<Token> stack, LazyOperator lazyOperator) {
        Token token;
        Token token2 = token = stack.isEmpty() ? null : stack.peek();
        while (token != null && (token.type == TokenType.OPERATOR || token.type == TokenType.UNARY_OPERATOR) && (lazyOperator.isLeftAssoc() && lazyOperator.getPrecedence() <= this.operators.get(token.surface).getPrecedence() || lazyOperator.getPrecedence() < this.operators.get(token.surface).getPrecedence())) {
            list.add(stack.pop());
            token = stack.isEmpty() ? null : stack.peek();
        }
    }

    public BigDecimal eval() {
        return this.eval(true);
    }

    public BigDecimal eval(boolean bl) {
        ArrayDeque<LazyNumber> arrayDeque = new ArrayDeque<LazyNumber>();
        block10: for (final Token token : this.getRPN()) {
            switch (token.type.ordinal()) {
                case 4: {
                    final LazyNumber lazyNumber = (LazyNumber)arrayDeque.pop();
                    final LazyNumber lazyNumber2 = new LazyNumber(){

                        @Override
                        public BigDecimal eval() {
                            return Expression.this.operators.get(token.surface).eval(lazyNumber, null).eval();
                        }

                        @Override
                        public String getString() {
                            return String.valueOf(Expression.this.operators.get(token.surface).eval(lazyNumber, null).eval());
                        }
                    };
                    arrayDeque.push(lazyNumber2);
                    continue block10;
                }
                case 3: {
                    final LazyNumber lazyNumber = (LazyNumber)arrayDeque.pop();
                    final LazyNumber lazyNumber2 = (LazyNumber)arrayDeque.pop();
                    LazyNumber lazyNumber3 = new LazyNumber(){

                        @Override
                        public BigDecimal eval() {
                            return Expression.this.operators.get(token.surface).eval(lazyNumber2, lazyNumber).eval();
                        }

                        @Override
                        public String getString() {
                            return String.valueOf(Expression.this.operators.get(token.surface).eval(lazyNumber2, lazyNumber).eval());
                        }
                    };
                    arrayDeque.push(lazyNumber3);
                    continue block10;
                }
                case 0: {
                    if (!this.variables.containsKey(token.surface)) {
                        throw new ExpressionException("Unknown operator or function: " + String.valueOf(token));
                    }
                    arrayDeque.push(new LazyNumber(){

                        @Override
                        public BigDecimal eval() {
                            LazyNumber lazyNumber = Expression.this.variables.get(token.surface);
                            BigDecimal bigDecimal = lazyNumber == null ? null : lazyNumber.eval();
                            return bigDecimal == null ? null : bigDecimal.round(Expression.this.mc);
                        }

                        @Override
                        public String getString() {
                            return token.surface;
                        }
                    });
                    continue block10;
                }
                case 1: {
                    net.advancedplugins.as.impl.utils.evalex.LazyFunction lazyFunction = this.functions.get(token.surface.toUpperCase(Locale.ROOT));
                    ArrayList<LazyNumber> arrayList = new ArrayList<LazyNumber>(!lazyFunction.numParamsVaries() ? lazyFunction.getNumParams() : 0);
                    while (!arrayDeque.isEmpty() && arrayDeque.peek() != PARAMS_START) {
                        arrayList.add(0, (LazyNumber)arrayDeque.pop());
                    }
                    if (arrayDeque.peek() == PARAMS_START) {
                        arrayDeque.pop();
                    }
                    LazyNumber lazyNumber = lazyFunction.lazyEval(arrayList);
                    arrayDeque.push(lazyNumber);
                    continue block10;
                }
                case 5: {
                    arrayDeque.push(PARAMS_START);
                    continue block10;
                }
                case 2: {
                    arrayDeque.push(new LazyNumber(){

                        @Override
                        public BigDecimal eval() {
                            if (token.surface.equalsIgnoreCase("NULL")) {
                                return null;
                            }
                            return new BigDecimal(token.surface, Expression.this.mc);
                        }

                        @Override
                        public String getString() {
                            return String.valueOf(new BigDecimal(token.surface, Expression.this.mc));
                        }
                    });
                    continue block10;
                }
                case 9: {
                    arrayDeque.push(new LazyNumber(){

                        @Override
                        public BigDecimal eval() {
                            return null;
                        }

                        @Override
                        public String getString() {
                            return token.surface;
                        }
                    });
                    continue block10;
                }
                case 8: {
                    arrayDeque.push(new LazyNumber(){

                        @Override
                        public BigDecimal eval() {
                            return new BigDecimal(new BigInteger(token.surface.substring(2), 16), Expression.this.mc);
                        }

                        @Override
                        public String getString() {
                            return new BigInteger(token.surface.substring(2), 16).toString();
                        }
                    });
                    continue block10;
                }
            }
            throw new ExpressionException("Unexpected token " + token.surface, token.pos);
        }
        Object object = ((LazyNumber)arrayDeque.pop()).eval();
        if (object == null) {
            return null;
        }
        if (bl) {
            object = ((BigDecimal)object).stripTrailingZeros();
        }
        return object;
    }

    public Expression setPrecision(int n) {
        this.mc = new MathContext(n);
        return this;
    }

    public Expression setRoundingMode(RoundingMode roundingMode) {
        this.mc = new MathContext(this.mc.getPrecision(), roundingMode);
        return this;
    }

    public Expression setFirstVariableCharacters(String string) {
        this.firstVarChars = string;
        return this;
    }

    public Expression setVariableCharacters(String string) {
        this.varChars = string;
        return this;
    }

    public <OPERATOR extends LazyOperator> OPERATOR addOperator(OPERATOR OPERATOR) {
        Object object = OPERATOR.getOper();
        if (OPERATOR instanceof AbstractUnaryOperator) {
            object = (String)object + "u";
        }
        return (OPERATOR)this.operators.put((String)object, OPERATOR);
    }

    public net.advancedplugins.as.impl.utils.evalex.Function addFunction(net.advancedplugins.as.impl.utils.evalex.Function function) {
        return (net.advancedplugins.as.impl.utils.evalex.Function)this.functions.put(function.getName(), function);
    }

    public net.advancedplugins.as.impl.utils.evalex.LazyFunction addLazyFunction(net.advancedplugins.as.impl.utils.evalex.LazyFunction lazyFunction) {
        return this.functions.put(lazyFunction.getName(), lazyFunction);
    }

    public Expression setVariable(String string, BigDecimal bigDecimal) {
        return this.setVariable(string, this.createLazyNumber(bigDecimal));
    }

    public Expression setVariable(String string, LazyNumber lazyNumber) {
        this.variables.put(string, lazyNumber);
        return this;
    }

    public Expression setVariable(String string, String string2) {
        if (this.isNumber(string2)) {
            this.variables.put(string, this.createLazyNumber(new BigDecimal(string2, this.mc)));
        } else if (string2.equalsIgnoreCase("null")) {
            this.variables.put(string, null);
        } else {
            final String string3 = string2;
            this.variables.put(string, new LazyNumber(){
                private final Map<String, LazyNumber> outerVariables;
                private final Map<String, net.advancedplugins.as.impl.utils.evalex.LazyFunction> outerFunctions;
                private final Map<String, LazyOperator> outerOperators;
                private final String innerExpressionString;
                private final MathContext inneMc;
                {
                    this.outerVariables = Expression.this.variables;
                    this.outerFunctions = Expression.this.functions;
                    this.outerOperators = Expression.this.operators;
                    this.innerExpressionString = string3;
                    this.inneMc = Expression.this.mc;
                }

                @Override
                public String getString() {
                    return this.innerExpressionString;
                }

                @Override
                public BigDecimal eval() {
                    Expression expression = new Expression(this.innerExpressionString, this.inneMc);
                    expression.variables = this.outerVariables;
                    expression.functions = this.outerFunctions;
                    expression.operators = this.outerOperators;
                    return expression.eval();
                }
            });
            this.rpn = null;
        }
        return this;
    }

    private Expression createEmbeddedExpression(String string) {
        Map<String, LazyNumber> map = this.variables;
        Map<String, net.advancedplugins.as.impl.utils.evalex.LazyFunction> map2 = this.functions;
        Map<String, LazyOperator> map3 = this.operators;
        MathContext mathContext = this.mc;
        Expression expression = new Expression(string, mathContext);
        expression.variables = map;
        expression.functions = map2;
        expression.operators = map3;
        return expression;
    }

    public Expression with(String string, BigDecimal bigDecimal) {
        return this.setVariable(string, bigDecimal);
    }

    public Expression with(String string, LazyNumber lazyNumber) {
        return this.setVariable(string, lazyNumber);
    }

    public Expression and(String string, String string2) {
        return this.setVariable(string, string2);
    }

    public Expression and(String string, BigDecimal bigDecimal) {
        return this.setVariable(string, bigDecimal);
    }

    public Expression and(String string, LazyNumber lazyNumber) {
        return this.setVariable(string, lazyNumber);
    }

    public Expression with(String string, String string2) {
        return this.setVariable(string, string2);
    }

    public Iterator<Token> getExpressionTokenizer() {
        String string = this.expressionString;
        return new Tokenizer(string);
    }

    private List<Token> getRPN() {
        if (this.rpn == null) {
            this.rpn = this.shuntingYard(this.expressionString);
            this.validate(this.rpn);
        }
        return this.rpn;
    }

    private void validate(List<Token> list) {
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(0);
        block6: for (Token token : list) {
            switch (token.type.ordinal()) {
                case 4: {
                    if ((Integer)stack.peek() >= 1) continue block6;
                    throw new ExpressionException(MISSING_PARAMETERS_FOR_OPERATOR + String.valueOf(token));
                }
                case 3: {
                    if ((Integer)stack.peek() < 2) {
                        throw new ExpressionException(MISSING_PARAMETERS_FOR_OPERATOR + String.valueOf(token));
                    }
                    stack.set(stack.size() - 1, (Integer)stack.peek() - 2 + 1);
                    continue block6;
                }
                case 1: {
                    net.advancedplugins.as.impl.utils.evalex.LazyFunction lazyFunction = this.functions.get(token.surface.toUpperCase(Locale.ROOT));
                    if (lazyFunction == null) {
                        throw new ExpressionException("Unknown function " + String.valueOf(token), token.pos + 1);
                    }
                    int n = (Integer)stack.pop();
                    if (!lazyFunction.numParamsVaries() && n != lazyFunction.getNumParams()) {
                        throw new ExpressionException("Function " + String.valueOf(token) + " expected " + lazyFunction.getNumParams() + " parameters, got " + n);
                    }
                    if (stack.isEmpty()) {
                        throw new ExpressionException("Too many function calls, maximum scope exceeded");
                    }
                    stack.set(stack.size() - 1, (Integer)stack.peek() + 1);
                    continue block6;
                }
                case 5: {
                    stack.push(0);
                    continue block6;
                }
            }
            stack.set(stack.size() - 1, (Integer)stack.peek() + 1);
        }
        if (stack.size() > 1) {
            throw new ExpressionException("Too many unhandled function parameter lists");
        }
        if ((Integer)stack.peek() > 1) {
            throw new ExpressionException("Too many numbers or variables");
        }
        if ((Integer)stack.peek() < 1) {
            throw new ExpressionException("Empty expression");
        }
    }

    public String toRPN() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Token token : this.getRPN()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(" ");
            }
            if (token.type == TokenType.VARIABLE && this.variables.containsKey(token.surface)) {
                LazyNumber lazyNumber = this.variables.get(token.surface);
                String string = lazyNumber.getString();
                if (this.isNumber(string)) {
                    stringBuilder.append(token);
                    continue;
                }
                Expression expression = this.createEmbeddedExpression(string);
                String string2 = expression.toRPN();
                stringBuilder.append(string2);
                continue;
            }
            stringBuilder.append(token);
        }
        return stringBuilder.toString();
    }

    public Set<String> getDeclaredVariables() {
        return Collections.unmodifiableSet(this.variables.keySet());
    }

    public Set<String> getDeclaredOperators() {
        return Collections.unmodifiableSet(this.operators.keySet());
    }

    public Set<String> getDeclaredFunctions() {
        return Collections.unmodifiableSet(this.functions.keySet());
    }

    public String getExpression() {
        return this.expressionString;
    }

    public List<String> getUsedVariables() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Tokenizer tokenizer = new Tokenizer(this.expressionString);
        while (tokenizer.hasNext()) {
            Token token = tokenizer.next();
            String string = token.toString();
            if (token.type != TokenType.VARIABLE || string.equals("PI") || string.equals("e") || string.equals("TRUE") || string.equals("FALSE")) continue;
            arrayList.add(string);
        }
        return arrayList;
    }

    public String getOriginalExpression() {
        return this.originalExpression;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        Expression expression = (Expression)object;
        if (this.expressionString == null) {
            return expression.expressionString == null;
        }
        return this.expressionString.equals(expression.expressionString);
    }

    public int hashCode() {
        return this.expressionString == null ? 0 : this.expressionString.hashCode();
    }

    public String toString() {
        return this.expressionString;
    }

    public boolean isBoolean() {
        List<Token> list = this.getRPN();
        if (!list.isEmpty()) {
            for (int i = list.size() - 1; i >= 0; --i) {
                Token token = list.get(i);
                if (token.surface.equals("IF")) continue;
                if (token.type == TokenType.FUNCTION) {
                    return this.functions.get(token.surface).isBooleanFunction();
                }
                if (token.type != TokenType.OPERATOR) continue;
                return this.operators.get(token.surface).isBooleanOperator();
            }
        }
        return false;
    }

    public List<String> infixNotation() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Tokenizer tokenizer = new Tokenizer(this.expressionString);
        while (tokenizer.hasNext()) {
            Token token = tokenizer.next();
            String string = "{" + String.valueOf((Object)token.type) + ":" + token.surface + "}";
            arrayList.add(string);
        }
        return arrayList;
    }

    public static interface LazyNumber {
        public BigDecimal eval();

        public String getString();
    }

    private class Tokenizer
    implements Iterator<Token> {
        private int pos = 0;
        private final String input;
        private Token previousToken;

        public Tokenizer(String string) {
            this.input = string.trim();
        }

        @Override
        public boolean hasNext() {
            return this.pos < this.input.length();
        }

        private char peekNextChar() {
            if (this.pos < this.input.length() - 1) {
                return this.input.charAt(this.pos + 1);
            }
            return '\u0000';
        }

        private boolean isHexDigit(char c) {
            return c == 'x' || c == 'X' || c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F';
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public Token next() {
            Token token = new Token();
            if (this.pos >= this.input.length()) {
                this.previousToken = null;
                return null;
            }
            char c = this.input.charAt(this.pos);
            while (Character.isWhitespace(c) && this.pos < this.input.length()) {
                c = this.input.charAt(++this.pos);
            }
            token.pos = this.pos;
            boolean bl = false;
            if (Character.isDigit(c) || c == '.' && Character.isDigit(this.peekNextChar())) {
                if (c == '0' && (this.peekNextChar() == 'x' || this.peekNextChar() == 'X')) {
                    bl = true;
                }
                while (bl && this.isHexDigit(c) || (Character.isDigit(c) || c == '.' || c == 'e' || c == 'E' || c == '-' && token.length() > 0 && ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1)) || c == '+' && token.length() > 0 && ('e' == token.charAt(token.length() - 1) || 'E' == token.charAt(token.length() - 1))) && this.pos < this.input.length()) {
                    token.append(this.input.charAt(this.pos++));
                    c = this.pos == this.input.length() ? (char)'\u0000' : this.input.charAt(this.pos);
                }
                token.type = bl ? TokenType.HEX_LITERAL : TokenType.LITERAL;
            } else if (c == '\"') {
                ++this.pos;
                if (this.previousToken.type == TokenType.STRINGPARAM) return this.next();
                c = this.input.charAt(this.pos);
                while (c != '\"') {
                    token.append(this.input.charAt(this.pos++));
                    c = this.pos == this.input.length() ? (char)'\u0000' : this.input.charAt(this.pos);
                }
                token.type = TokenType.STRINGPARAM;
            } else if (Character.isLetter(c) || Expression.this.firstVarChars.indexOf(c) >= 0) {
                while ((Character.isLetter(c) || Character.isDigit(c) || Expression.this.varChars.indexOf(c) >= 0 || token.length() == 0 && Expression.this.firstVarChars.indexOf(c) >= 0) && this.pos < this.input.length()) {
                    token.append(this.input.charAt(this.pos++));
                    c = this.pos == this.input.length() ? (char)'\u0000' : this.input.charAt(this.pos);
                }
                if (Character.isWhitespace(c)) {
                    while (Character.isWhitespace(c) && this.pos < this.input.length()) {
                        c = this.input.charAt(this.pos++);
                    }
                    --this.pos;
                }
                token.type = Expression.this.operators.containsKey(token.surface) ? TokenType.OPERATOR : (c == '(' ? TokenType.FUNCTION : TokenType.VARIABLE);
            } else if (c == '(' || c == ')' || c == ',') {
                token.type = c == '(' ? TokenType.OPEN_PAREN : (c == ')' ? TokenType.CLOSE_PAREN : TokenType.COMMA);
                token.append(c);
                ++this.pos;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                int n = this.pos;
                c = this.input.charAt(this.pos);
                int n2 = -1;
                while (!(Character.isLetter(c) || Character.isDigit(c) || Expression.this.firstVarChars.indexOf(c) >= 0 || Character.isWhitespace(c) || c == '(' || c == ')' || c == ',' || this.pos >= this.input.length())) {
                    stringBuilder.append(c);
                    ++this.pos;
                    if (Expression.this.operators.containsKey(stringBuilder.toString())) {
                        n2 = this.pos;
                    }
                    c = this.pos == this.input.length() ? (char)'\u0000' : this.input.charAt(this.pos);
                }
                if (n2 != -1) {
                    token.append(this.input.substring(n, n2));
                    this.pos = n2;
                } else {
                    token.append(stringBuilder.toString());
                }
                if (this.previousToken == null || this.previousToken.type == TokenType.OPERATOR || this.previousToken.type == TokenType.OPEN_PAREN || this.previousToken.type == TokenType.COMMA || this.previousToken.type == TokenType.UNARY_OPERATOR) {
                    token.surface = token.surface + "u";
                    token.type = TokenType.UNARY_OPERATOR;
                } else {
                    token.type = TokenType.OPERATOR;
                }
            }
            this.previousToken = token;
            return token;
        }

        @Override
        public void remove() {
            throw new ExpressionException("remove() not supported");
        }
    }

    public class Token {
        public String surface = "";
        public TokenType type;
        public int pos;

        public void append(char c) {
            this.surface = this.surface + c;
        }

        public void append(String string) {
            this.surface = this.surface + string;
        }

        public char charAt(int n) {
            return this.surface.charAt(n);
        }

        public int length() {
            return this.surface.length();
        }

        public String toString() {
            return this.surface;
        }
    }

    static enum TokenType {
        VARIABLE,
        FUNCTION,
        LITERAL,
        OPERATOR,
        UNARY_OPERATOR,
        OPEN_PAREN,
        COMMA,
        CLOSE_PAREN,
        HEX_LITERAL,
        STRINGPARAM;

    }

    public static class ExpressionException
    extends RuntimeException {
        private static final long serialVersionUID = 1118142866870779047L;

        public ExpressionException(String string) {
            super(string);
        }

        public ExpressionException(String string, int n) {
            super(string + " at character position " + n);
        }
    }

    public abstract class UnaryOperator
    extends AbstractUnaryOperator {
        public UnaryOperator(String string, int n, boolean bl) {
            super(string, n, bl);
        }
    }

    public abstract class Operator
    extends AbstractOperator {
        public Operator(String string, int n, boolean bl, boolean bl2) {
            super(string, n, bl, bl2);
        }

        public Operator(String string, int n, boolean bl) {
            super(string, n, bl);
        }
    }

    public abstract class Function
    extends AbstractFunction {
        public Function(String string, int n) {
            super(string, n);
        }

        public Function(String string, int n, boolean bl) {
            super(string, n, bl);
        }
    }

    public abstract class LazyFunction
    extends AbstractLazyFunction {
        public LazyFunction(String string, int n, boolean bl) {
            super(string, n, bl);
        }

        public LazyFunction(String string, int n) {
            super(string, n);
        }
    }
}

