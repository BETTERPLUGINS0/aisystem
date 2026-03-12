package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.jdbc.PreferQueryMode;
import fr.xephi.authme.libs.org.postgresql.util.LruCache;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.SQLException;
import java.util.List;

class CachedQueryCreateAction implements LruCache.CreateAction<Object, CachedQuery> {
   private static final String[] EMPTY_RETURNING = new String[0];
   private final QueryExecutor queryExecutor;

   CachedQueryCreateAction(QueryExecutor queryExecutor) {
      this.queryExecutor = queryExecutor;
   }

   public CachedQuery create(Object key) throws SQLException {
      assert key instanceof String || key instanceof BaseQueryKey : "Query key should be String or BaseQueryKey. Given " + key.getClass() + ", sql: " + key;

      BaseQueryKey queryKey;
      String parsedSql;
      if (key instanceof BaseQueryKey) {
         queryKey = (BaseQueryKey)key;
         parsedSql = queryKey.sql;
      } else {
         queryKey = null;
         parsedSql = (String)key;
      }

      if (key instanceof String || ((BaseQueryKey)Nullness.castNonNull(queryKey)).escapeProcessing) {
         parsedSql = Parser.replaceProcessing(parsedSql, true, this.queryExecutor.getStandardConformingStrings());
      }

      boolean isFunction;
      if (key instanceof CallableQueryKey) {
         JdbcCallParseInfo callInfo = Parser.modifyJdbcCall(parsedSql, this.queryExecutor.getStandardConformingStrings(), this.queryExecutor.getServerVersionNum(), this.queryExecutor.getProtocolVersion(), this.queryExecutor.getEscapeSyntaxCallMode());
         parsedSql = callInfo.getSql();
         isFunction = callInfo.isFunction();
      } else {
         isFunction = false;
      }

      boolean isParameterized = key instanceof String || ((BaseQueryKey)Nullness.castNonNull(queryKey)).isParameterized;
      boolean splitStatements = isParameterized || this.queryExecutor.getPreferQueryMode().compareTo(PreferQueryMode.EXTENDED) >= 0;
      String[] returningColumns;
      if (key instanceof QueryWithReturningColumnsKey) {
         returningColumns = ((QueryWithReturningColumnsKey)key).columnNames;
      } else {
         returningColumns = EMPTY_RETURNING;
      }

      List<NativeQuery> queries = Parser.parseJdbcSql(parsedSql, this.queryExecutor.getStandardConformingStrings(), isParameterized, splitStatements, this.queryExecutor.isReWriteBatchedInsertsEnabled(), this.queryExecutor.getQuoteReturningIdentifiers(), returningColumns);
      Query query = this.queryExecutor.wrap(queries);
      return new CachedQuery(key, query, isFunction);
   }
}
