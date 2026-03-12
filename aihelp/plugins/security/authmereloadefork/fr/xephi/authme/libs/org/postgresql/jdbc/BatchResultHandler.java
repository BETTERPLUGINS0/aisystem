package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.ResultCursor;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandlerBase;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import fr.xephi.authme.libs.org.postgresql.core.v3.BatchedQuery;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BatchResultHandler extends ResultHandlerBase {
   private final PgStatement pgStatement;
   private int resultIndex;
   private final Query[] queries;
   private final long[] longUpdateCounts;
   @Nullable
   private final ParameterList[] parameterLists;
   private final boolean expectGeneratedKeys;
   @Nullable
   private PgResultSet generatedKeys;
   private int committedRows;
   @Nullable
   private final List<List<Tuple>> allGeneratedRows;
   @Nullable
   private List<Tuple> latestGeneratedRows;
   @Nullable
   private PgResultSet latestGeneratedKeysRs;

   BatchResultHandler(PgStatement pgStatement, Query[] queries, @Nullable ParameterList[] parameterLists, boolean expectGeneratedKeys) {
      this.pgStatement = pgStatement;
      this.queries = queries;
      this.parameterLists = parameterLists;
      this.longUpdateCounts = new long[queries.length];
      this.expectGeneratedKeys = expectGeneratedKeys;
      this.allGeneratedRows = !expectGeneratedKeys ? null : new ArrayList();
   }

   public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
      ++this.resultIndex;
      if (this.expectGeneratedKeys) {
         if (this.generatedKeys == null) {
            try {
               this.latestGeneratedKeysRs = (PgResultSet)this.pgStatement.createResultSet(fromQuery, fields, new ArrayList(), cursor);
            } catch (SQLException var6) {
               this.handleError(var6);
            }
         }

         this.latestGeneratedRows = tuples;
      }
   }

   public void handleCommandStatus(String status, long updateCount, long insertOID) {
      List<Tuple> latestGeneratedRows = this.latestGeneratedRows;
      if (latestGeneratedRows != null) {
         --this.resultIndex;
         if (updateCount > 0L && (this.getException() == null || this.isAutoCommit())) {
            List<List<Tuple>> allGeneratedRows = (List)Nullness.castNonNull(this.allGeneratedRows, "allGeneratedRows");
            allGeneratedRows.add(latestGeneratedRows);
            if (this.generatedKeys == null) {
               this.generatedKeys = this.latestGeneratedKeysRs;
            }
         }

         this.latestGeneratedRows = null;
      }

      if (this.resultIndex >= this.queries.length) {
         this.handleError(new PSQLException(GT.tr("Too many update results were returned."), PSQLState.TOO_MANY_RESULTS));
      } else {
         this.latestGeneratedKeysRs = null;
         this.longUpdateCounts[this.resultIndex++] = updateCount;
      }
   }

   private boolean isAutoCommit() {
      try {
         return this.pgStatement.getConnection().getAutoCommit();
      } catch (SQLException var2) {
         assert false : "pgStatement.getConnection().getAutoCommit() should not throw";

         return false;
      }
   }

   public void secureProgress() {
      if (this.isAutoCommit()) {
         this.committedRows = this.resultIndex;
         this.updateGeneratedKeys();
      }

   }

   private void updateGeneratedKeys() {
      List<List<Tuple>> allGeneratedRows = this.allGeneratedRows;
      if (allGeneratedRows != null && !allGeneratedRows.isEmpty()) {
         PgResultSet generatedKeys = (PgResultSet)Nullness.castNonNull(this.generatedKeys, "generatedKeys");
         Iterator var3 = allGeneratedRows.iterator();

         while(var3.hasNext()) {
            List<Tuple> rows = (List)var3.next();
            generatedKeys.addRows(rows);
         }

         allGeneratedRows.clear();
      }
   }

   public void handleWarning(SQLWarning warning) {
      this.pgStatement.addWarning(warning);
   }

   public void handleError(SQLException newError) {
      if (this.getException() == null) {
         Arrays.fill(this.longUpdateCounts, this.committedRows, this.longUpdateCounts.length, -3L);
         if (this.allGeneratedRows != null) {
            this.allGeneratedRows.clear();
         }

         String queryString = "<unknown>";
         if (this.pgStatement.getPGConnection().getLogServerErrorDetail() && this.resultIndex < this.queries.length) {
            queryString = this.queries[this.resultIndex].toString(this.parameterLists == null ? null : this.parameterLists[this.resultIndex]);
         }

         BatchUpdateException batchException = new BatchUpdateException(GT.tr("Batch entry {0} {1} was aborted: {2}  Call getNextException to see other errors in the batch.", this.resultIndex, queryString, newError.getMessage()), newError.getSQLState(), 0, this.uncompressLongUpdateCount(), newError);
         super.handleError(batchException);
      }

      ++this.resultIndex;
      super.handleError(newError);
   }

   public void handleCompletion() throws SQLException {
      this.updateGeneratedKeys();
      SQLException batchException = this.getException();
      if (batchException != null) {
         if (this.isAutoCommit()) {
            BatchUpdateException newException = new BatchUpdateException(((SQLException)batchException).getMessage(), ((SQLException)batchException).getSQLState(), 0, this.uncompressLongUpdateCount(), ((SQLException)batchException).getCause());
            SQLException next = ((SQLException)batchException).getNextException();
            if (next != null) {
               newException.setNextException(next);
            }

            batchException = newException;
         }

         throw batchException;
      }
   }

   @Nullable
   public ResultSet getGeneratedKeys() {
      return this.generatedKeys;
   }

   private int[] uncompressUpdateCount() {
      long[] original = this.uncompressLongUpdateCount();
      int[] copy = new int[original.length];

      for(int i = 0; i < original.length; ++i) {
         copy[i] = original[i] > 2147483647L ? -2 : (int)original[i];
      }

      return copy;
   }

   public int[] getUpdateCount() {
      return this.uncompressUpdateCount();
   }

   private long[] uncompressLongUpdateCount() {
      if (!(this.queries[0] instanceof BatchedQuery)) {
         return this.longUpdateCounts;
      } else {
         int totalRows = 0;
         boolean hasRewrites = false;
         Query[] var3 = this.queries;
         int offset = var3.length;

         int i;
         Query query;
         int batchSize;
         for(i = 0; i < offset; ++i) {
            query = var3[i];
            batchSize = query.getBatchSize();
            totalRows += batchSize;
            hasRewrites |= batchSize > 1;
         }

         if (!hasRewrites) {
            return this.longUpdateCounts;
         } else {
            long[] newUpdateCounts = new long[totalRows];
            offset = 0;

            for(i = 0; i < this.queries.length; ++i) {
               query = this.queries[i];
               batchSize = query.getBatchSize();
               long superBatchResult = this.longUpdateCounts[i];
               if (batchSize == 1) {
                  newUpdateCounts[offset++] = superBatchResult;
               } else {
                  if (superBatchResult > 0L) {
                     superBatchResult = -2L;
                  }

                  Arrays.fill(newUpdateCounts, offset, offset + batchSize, superBatchResult);
                  offset += batchSize;
               }
            }

            return newUpdateCounts;
         }
      }
   }

   public long[] getLargeUpdateCount() {
      return this.uncompressLongUpdateCount();
   }
}
