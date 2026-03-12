package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;

public class ComparingPredicate<T, C> extends AbstractPredicate<C> {
   private final Column<T, C> column;
   private final T value;
   private final ComparingPredicate.Type type;

   public ComparingPredicate(Column<T, C> column, T value, ComparingPredicate.Type type) {
      this.column = column;
      this.value = value;
      this.type = type;
   }

   public Column<?, C> getColumn() {
      return this.column;
   }

   public Object getValue() {
      return this.value;
   }

   public ComparingPredicate.Type getType() {
      return this.type;
   }

   public static enum Type {
      LESS,
      LESS_EQUALS,
      EQUALS,
      NOT_EQUALS,
      GREATER,
      GREATER_EQUALS;
   }
}
