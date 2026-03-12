package fr.xephi.authme.libs.org.postgresql.core;

import java.util.Arrays;
import org.checkerframework.checker.nullness.qual.Nullable;

class QueryWithReturningColumnsKey extends BaseQueryKey {
   public final String[] columnNames;
   private int size;

   QueryWithReturningColumnsKey(String sql, boolean isParameterized, boolean escapeProcessing, @Nullable String[] columnNames) {
      super(sql, isParameterized, escapeProcessing);
      if (columnNames == null) {
         columnNames = new String[]{"*"};
      }

      this.columnNames = columnNames;
   }

   public long getSize() {
      int size = this.size;
      if (size != 0) {
         return (long)size;
      } else {
         size = (int)super.getSize();
         if (this.columnNames != null) {
            size = (int)((long)size + 16L);
            String[] var2 = this.columnNames;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               String columnName = var2[var4];
               size = (int)((long)size + (long)columnName.length() * 2L);
            }
         }

         this.size = size;
         return (long)size;
      }
   }

   public String toString() {
      return "QueryWithReturningColumnsKey{sql='" + this.sql + '\'' + ", isParameterized=" + this.isParameterized + ", escapeProcessing=" + this.escapeProcessing + ", columnNames=" + Arrays.toString(this.columnNames) + '}';
   }

   public boolean equals(@Nullable Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         if (!super.equals(o)) {
            return false;
         } else {
            QueryWithReturningColumnsKey that = (QueryWithReturningColumnsKey)o;
            return Arrays.equals(this.columnNames, that.columnNames);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + Arrays.hashCode(this.columnNames);
      return result;
   }
}
