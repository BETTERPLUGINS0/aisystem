package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;

public class EqualsIgnoreCasePredicate<C> extends AbstractPredicate<C> {
   private final Column<String, C> column;
   private final String value;
   private final boolean isNegated;

   public EqualsIgnoreCasePredicate(Column<String, C> column, String value, boolean isNegated) {
      this.column = column;
      this.value = value;
      this.isNegated = isNegated;
   }

   public Column<String, C> getColumn() {
      return this.column;
   }

   public String getValue() {
      return this.value;
   }

   public boolean isNegated() {
      return this.isNegated;
   }
}
