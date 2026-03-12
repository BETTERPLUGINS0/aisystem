package fr.xephi.authme.libs.org.postgresql.core.v3;

import org.checkerframework.checker.nullness.qual.Nullable;

class DescribeRequest {
   public final SimpleQuery query;
   public final SimpleParameterList parameterList;
   public final boolean describeOnly;
   @Nullable
   public final String statementName;

   DescribeRequest(SimpleQuery query, SimpleParameterList parameterList, boolean describeOnly, @Nullable String statementName) {
      this.query = query;
      this.parameterList = parameterList;
      this.describeOnly = describeOnly;
      this.statementName = statementName;
   }
}
