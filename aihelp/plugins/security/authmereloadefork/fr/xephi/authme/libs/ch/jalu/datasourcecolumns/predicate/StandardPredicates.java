package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;

public class StandardPredicates {
   protected StandardPredicates() {
   }

   public static <T, C> ComparingPredicate<T, C> eq(Column<T, C> column, T requiredValue) {
      return new ComparingPredicate(column, requiredValue, ComparingPredicate.Type.EQUALS);
   }

   public static <T, C> ComparingPredicate<T, C> notEq(Column<T, C> column, T requiredValue) {
      return new ComparingPredicate(column, requiredValue, ComparingPredicate.Type.NOT_EQUALS);
   }

   public static <C> EqualsIgnoreCasePredicate<C> eqIgnoreCase(Column<String, C> column, String requiredValue) {
      return new EqualsIgnoreCasePredicate(column, requiredValue, false);
   }

   public static <C> EqualsIgnoreCasePredicate<C> notEqIgnoreCase(Column<String, C> column, String requiredValue) {
      return new EqualsIgnoreCasePredicate(column, requiredValue, true);
   }

   public static <T, C> ComparingPredicate<T, C> greaterThan(Column<T, C> column, T requiredValue) {
      return new ComparingPredicate(column, requiredValue, ComparingPredicate.Type.GREATER);
   }

   public static <T, C> ComparingPredicate<T, C> greaterThanEquals(Column<T, C> column, T requiredValue) {
      return new ComparingPredicate(column, requiredValue, ComparingPredicate.Type.GREATER_EQUALS);
   }

   public static <T, C> ComparingPredicate<T, C> lessThan(Column<T, C> column, T requiredValue) {
      return new ComparingPredicate(column, requiredValue, ComparingPredicate.Type.LESS);
   }

   public static <T, C> ComparingPredicate<T, C> lessThanEquals(Column<T, C> column, T requiredValue) {
      return new ComparingPredicate(column, requiredValue, ComparingPredicate.Type.LESS_EQUALS);
   }

   public static <C> IsNullPredicate<C> isNull(Column<?, C> column) {
      return new IsNullPredicate(column);
   }

   public static <C> IsNotNullPredicate<C> isNotNull(Column<?, C> column) {
      return new IsNotNullPredicate(column);
   }

   public static <C> AndPredicate<C> and(Predicate<C> left, Predicate<C> right) {
      return new AndPredicate(left, right);
   }

   public static <C> OrPredicate<C> or(Predicate<C> left, Predicate<C> right) {
      return new OrPredicate(left, right);
   }
}
