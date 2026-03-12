package fr.xephi.authme.libs.org.postgresql.core;

import java.util.Map;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface Query {
   ParameterList createParameterList();

   String toString(@Nullable ParameterList var1);

   String getNativeSql();

   @Nullable
   SqlCommand getSqlCommand();

   void close();

   boolean isStatementDescribed();

   boolean isEmpty();

   int getBatchSize();

   @Nullable
   Map<String, Integer> getResultSetColumnNameIndexMap();

   @Nullable
   Query[] getSubqueries();
}
