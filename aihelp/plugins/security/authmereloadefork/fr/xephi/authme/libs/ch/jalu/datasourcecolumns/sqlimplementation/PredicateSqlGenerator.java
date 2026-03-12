package fr.xephi.authme.libs.ch.jalu.datasourcecolumns.sqlimplementation;

import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.Column;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.AlwaysTruePredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.AndPredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.ComparingPredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.EqualsIgnoreCasePredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.IsNotNullPredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.IsNullPredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.OrPredicate;
import fr.xephi.authme.libs.ch.jalu.datasourcecolumns.predicate.Predicate;
import java.util.LinkedList;
import java.util.List;

public class PredicateSqlGenerator<C> {
   private final C context;
   private final boolean useNoCaseCollationForCaseInsensitiveEquals;

   public PredicateSqlGenerator(C context) {
      this(context, false);
   }

   public PredicateSqlGenerator(C context, boolean useNoCaseCollationForCaseInsensitiveEquals) {
      this.context = context;
      this.useNoCaseCollationForCaseInsensitiveEquals = useNoCaseCollationForCaseInsensitiveEquals;
   }

   public GeneratedSqlWithBindings generateWhereClause(Predicate<C> predicate) {
      StringBuilder sqlResult = new StringBuilder();
      List<Object> bindings = new LinkedList();
      this.generateWhereClause(predicate, sqlResult, bindings);
      return new GeneratedSqlWithBindings(sqlResult.toString(), bindings);
   }

   protected void generateWhereClause(Predicate<C> predicate, StringBuilder sqlResult, List<Object> objects) {
      Class<?> clazz = predicate.getClass();
      if (clazz == ComparingPredicate.class) {
         ComparingPredicate<?, C> eq = (ComparingPredicate)predicate;
         this.processComparingClause(eq, sqlResult, objects);
      } else if (clazz == EqualsIgnoreCasePredicate.class) {
         EqualsIgnoreCasePredicate<C> equalsIgnore = (EqualsIgnoreCasePredicate)predicate;
         this.processEqualsIgnoreCasePredicate(equalsIgnore, sqlResult, objects);
      } else if (clazz == OrPredicate.class) {
         OrPredicate<C> or = (OrPredicate)predicate;
         this.processCombiningClause(or.getLeft(), or.getRight(), "OR", sqlResult, objects);
      } else if (clazz == AndPredicate.class) {
         AndPredicate<C> and = (AndPredicate)predicate;
         this.processCombiningClause(and.getLeft(), and.getRight(), "AND", sqlResult, objects);
      } else if (clazz == IsNullPredicate.class) {
         IsNullPredicate<C> isNull = (IsNullPredicate)predicate;
         this.processIsNullAndNotNullPredicate(false, isNull.getColumn(), sqlResult);
      } else if (clazz == IsNotNullPredicate.class) {
         IsNotNullPredicate<C> isNotNull = (IsNotNullPredicate)predicate;
         this.processIsNullAndNotNullPredicate(true, isNotNull.getColumn(), sqlResult);
      } else {
         if (clazz != AlwaysTruePredicate.class) {
            throw new IllegalStateException("Unhandled predicate '" + predicate + "'");
         }

         this.addAlwaysTruePredicate(sqlResult);
      }

   }

   protected void addAlwaysTruePredicate(StringBuilder sqlResult) {
      sqlResult.append("1 = 1");
   }

   protected void processComparingClause(ComparingPredicate<?, C> predicate, StringBuilder sqlResult, List<Object> objects) {
      if (predicate.getColumn().isColumnUsed(this.context)) {
         sqlResult.append(predicate.getColumn().resolveName(this.context)).append(this.convertComparingTypeToSqlOperator(predicate.getType())).append("?");
         objects.add(predicate.getValue());
      } else {
         this.addAlwaysTruePredicate(sqlResult);
      }

   }

   protected void processEqualsIgnoreCasePredicate(EqualsIgnoreCasePredicate<C> predicate, StringBuilder sqlResult, List<Object> objects) {
      if (predicate.getColumn().isColumnUsed(this.context)) {
         String operator = predicate.isNegated() ? " <> ?" : " = ?";
         sqlResult.append(predicate.getColumn().resolveName(this.context)).append(operator);
         if (this.useNoCaseCollationForCaseInsensitiveEquals) {
            sqlResult.append(" COLLATE NOCASE");
         }

         objects.add(predicate.getValue());
      } else {
         this.addAlwaysTruePredicate(sqlResult);
      }

   }

   protected String convertComparingTypeToSqlOperator(ComparingPredicate.Type type) {
      switch(type) {
      case LESS:
         return " < ";
      case LESS_EQUALS:
         return " <= ";
      case EQUALS:
         return " = ";
      case NOT_EQUALS:
         return " <> ";
      case GREATER:
         return " > ";
      case GREATER_EQUALS:
         return " >= ";
      default:
         throw new IllegalStateException("Unknown comparing predicate type '" + type + "'");
      }
   }

   protected void processIsNullAndNotNullPredicate(boolean isNegated, Column<?, C> column, StringBuilder sqlResult) {
      if (column.isColumnUsed(this.context)) {
         String condition = isNegated ? " IS NOT NULL" : " IS NULL";
         sqlResult.append(column.resolveName(this.context)).append(condition);
      } else {
         this.addAlwaysTruePredicate(sqlResult);
      }

   }

   protected void processCombiningClause(Predicate<C> left, Predicate<C> right, String operator, StringBuilder sqlResult, List<Object> objects) {
      sqlResult.append("(");
      this.generateWhereClause(left, sqlResult, objects);
      sqlResult.append(") ").append(operator).append(" (");
      this.generateWhereClause(right, sqlResult, objects);
      sqlResult.append(")");
   }
}
