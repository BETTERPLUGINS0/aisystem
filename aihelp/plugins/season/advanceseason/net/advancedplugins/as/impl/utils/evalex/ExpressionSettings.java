/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package net.advancedplugins.as.impl.utils.evalex;

import java.math.MathContext;

public class ExpressionSettings {
    private MathContext mathContext;
    private int powerOperatorPrecedence;

    private ExpressionSettings() {
    }

    public ExpressionSettings(MathContext mathContext, int n) {
        this.mathContext = mathContext;
        this.powerOperatorPrecedence = n;
    }

    public MathContext getMathContext() {
        return this.mathContext;
    }

    public int getPowerOperatorPrecedence() {
        return this.powerOperatorPrecedence;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MathContext mathContext = MathContext.DECIMAL32;
        private int powerOperatorPrecedence = 40;

        public Builder mathContext(MathContext mathContext) {
            this.mathContext = mathContext;
            return this;
        }

        public Builder powerOperatorPrecedenceHigher() {
            this.powerOperatorPrecedence = 80;
            return this;
        }

        public Builder powerOperatorPrecedence(int n) {
            this.powerOperatorPrecedence = n;
            return this;
        }

        public ExpressionSettings build() {
            return new ExpressionSettings(this.mathContext, this.powerOperatorPrecedence);
        }
    }
}

