package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.DoublePredicate;
import java.util.function.Predicate;

@FunctionalInterface
public interface FloatPredicate extends Predicate<Float>, DoublePredicate {
   boolean test(float var1);

   /** @deprecated */
   @Deprecated
   default boolean test(double t) {
      return this.test(SafeMath.safeDoubleToFloat(t));
   }

   /** @deprecated */
   @Deprecated
   default boolean test(Float t) {
      return this.test(t);
   }

   default FloatPredicate and(FloatPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) && other.test(t);
      };
   }

   default FloatPredicate and(DoublePredicate other) {
      FloatPredicate var10001;
      if (other instanceof FloatPredicate) {
         var10001 = (FloatPredicate)other;
      } else {
         Objects.requireNonNull(other);
         var10001 = other::test;
      }

      return this.and(var10001);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Float> and(Predicate<? super Float> other) {
      return super.and(other);
   }

   default FloatPredicate negate() {
      return (t) -> {
         return !this.test(t);
      };
   }

   default FloatPredicate or(FloatPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) || other.test(t);
      };
   }

   default FloatPredicate or(DoublePredicate other) {
      FloatPredicate var10001;
      if (other instanceof FloatPredicate) {
         var10001 = (FloatPredicate)other;
      } else {
         Objects.requireNonNull(other);
         var10001 = other::test;
      }

      return this.or(var10001);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Float> or(Predicate<? super Float> other) {
      return super.or(other);
   }
}
