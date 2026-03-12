package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;

public class IsNullPredicate<C> extends AbstractPredicate<C> {
   private final Column<?, C> column;

   public IsNullPredicate(Column<?, C> column) {
      this.column = column;
   }

   public Column<?, C> getColumn() {
      return this.column;
   }
}
