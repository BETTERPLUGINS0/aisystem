package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.function.BinaryOperator;

@FunctionalInterface
public interface DoubleBinaryOperator extends BinaryOperator<Double>, java.util.function.DoubleBinaryOperator {
   double apply(double var1, double var3);

   /** @deprecated */
   @Deprecated
   default double applyAsDouble(double x, double y) {
      return this.apply(x, y);
   }

   /** @deprecated */
   @Deprecated
   default Double apply(Double x, Double y) {
      return this.apply(x, y);
   }
}
