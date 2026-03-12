package fr.xephi.authme.libs.org.postgresql.core;

import org.checkerframework.checker.nullness.qual.Nullable;

class CallableQueryKey extends BaseQueryKey {
   CallableQueryKey(String sql) {
      super(sql, true, true);
   }

   public String toString() {
      return "CallableQueryKey{sql='" + this.sql + '\'' + ", isParameterized=" + this.isParameterized + ", escapeProcessing=" + this.escapeProcessing + '}';
   }

   public int hashCode() {
      return super.hashCode() * 31;
   }

   public boolean equals(@Nullable Object o) {
      return super.equals(o);
   }
}
