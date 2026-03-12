package ac.grim.grimac.shaded.fastutil.doubles;

import java.util.function.UnaryOperator;

@FunctionalInterface
public interface DoubleUnaryOperator extends UnaryOperator<Double>, java.util.function.DoubleUnaryOperator {
   double apply(double var1);

   static DoubleUnaryOperator identity() {
      return (i) -> {
         return i;
      };
   }

   static DoubleUnaryOperator negation() {
      return (i) -> {
         return -i;
      };
   }

   /** @deprecated */
   @Deprecated
   default double applyAsDouble(double x) {
      return this.apply(x);
   }

   /** @deprecated */
   @Deprecated
   default Double apply(Double x) {
      return this.apply(x);
   }
}
