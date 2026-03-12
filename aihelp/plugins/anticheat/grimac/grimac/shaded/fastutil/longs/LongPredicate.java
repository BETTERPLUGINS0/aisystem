package ac.grim.grimac.shaded.fastutil.longs;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface LongPredicate extends Predicate<Long>, java.util.function.LongPredicate {
   /** @deprecated */
   @Deprecated
   default boolean test(Long t) {
      return this.test(t);
   }

   default LongPredicate and(java.util.function.LongPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) && other.test(t);
      };
   }

   default LongPredicate and(LongPredicate other) {
      return this.and((java.util.function.LongPredicate)other);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Long> and(Predicate<? super Long> other) {
      return super.and(other);
   }

   default LongPredicate negate() {
      return (t) -> {
         return !this.test(t);
      };
   }

   default LongPredicate or(java.util.function.LongPredicate other) {
      Objects.requireNonNull(other);
      return (t) -> {
         return this.test(t) || other.test(t);
      };
   }

   default LongPredicate or(LongPredicate other) {
      return this.or((java.util.function.LongPredicate)other);
   }

   /** @deprecated */
   @Deprecated
   default Predicate<Long> or(Predicate<? super Long> other) {
      return super.or(other);
   }
}
