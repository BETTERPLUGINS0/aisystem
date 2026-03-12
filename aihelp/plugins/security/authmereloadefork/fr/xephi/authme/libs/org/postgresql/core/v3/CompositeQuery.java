package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.SqlCommand;
import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

class CompositeQuery implements Query {
   private final SimpleQuery[] subqueries;
   private final int[] offsets;

   CompositeQuery(SimpleQuery[] subqueries, int[] offsets) {
      this.subqueries = subqueries;
      this.offsets = offsets;
   }

   public ParameterList createParameterList() {
      SimpleParameterList[] subparams = new SimpleParameterList[this.subqueries.length];

      for(int i = 0; i < this.subqueries.length; ++i) {
         subparams[i] = (SimpleParameterList)this.subqueries[i].createParameterList();
      }

      return new CompositeParameterList(subparams, this.offsets);
   }

   public String toString(@Nullable ParameterList parameters) {
      StringBuilder sbuf = new StringBuilder(this.subqueries[0].toString());

      for(int i = 1; i < this.subqueries.length; ++i) {
         sbuf.append(';');
         sbuf.append(this.subqueries[i]);
      }

      return sbuf.toString();
   }

   public String getNativeSql() {
      StringBuilder sbuf = new StringBuilder(this.subqueries[0].getNativeSql());

      for(int i = 1; i < this.subqueries.length; ++i) {
         sbuf.append(';');
         sbuf.append(this.subqueries[i].getNativeSql());
      }

      return sbuf.toString();
   }

   @Nullable
   public SqlCommand getSqlCommand() {
      return null;
   }

   public String toString() {
      return this.toString((ParameterList)null);
   }

   public void close() {
      SimpleQuery[] var1 = this.subqueries;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SimpleQuery subquery = var1[var3];
         subquery.close();
      }

   }

   public Query[] getSubqueries() {
      return this.subqueries;
   }

   public boolean isStatementDescribed() {
      SimpleQuery[] var1 = this.subqueries;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SimpleQuery subquery = var1[var3];
         if (!subquery.isStatementDescribed()) {
            return false;
         }
      }

      return true;
   }

   public boolean isEmpty() {
      SimpleQuery[] var1 = this.subqueries;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SimpleQuery subquery = var1[var3];
         if (!subquery.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public int getBatchSize() {
      return 0;
   }

   @Nullable
   public Map<String, Integer> getResultSetColumnNameIndexMap() {
      return null;
   }
}
