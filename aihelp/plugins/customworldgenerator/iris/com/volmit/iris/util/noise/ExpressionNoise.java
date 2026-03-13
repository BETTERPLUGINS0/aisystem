package com.volmit.iris.util.noise;

import com.volmit.iris.engine.object.IrisExpression;
import com.volmit.iris.util.math.RNG;

public class ExpressionNoise implements NoiseGenerator {
   private final RNG rng;
   private final IrisExpression expression;

   public ExpressionNoise(RNG rng, IrisExpression expression) {
      this.rng = var1;
      this.expression = var2;
   }

   public double noise(double x) {
      return this.expression.evaluate(this.rng, var1, -1.0D);
   }

   public double noise(double x, double z) {
      return this.expression.evaluate(this.rng, var1, var3);
   }

   public double noise(double x, double y, double z) {
      return this.expression.evaluate(this.rng, var1, var3, var5);
   }
}
