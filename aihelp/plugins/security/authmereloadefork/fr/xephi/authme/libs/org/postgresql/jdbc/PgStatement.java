package fr.xephi.authme.libs.org.postgresql.jdbc;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.core.BaseConnection;
import fr.xephi.authme.libs.org.postgresql.core.BaseStatement;
import fr.xephi.authme.libs.org.postgresql.core.CachedQuery;
import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutor;
import fr.xephi.authme.libs.org.postgresql.core.ResultCursor;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandler;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandlerBase;
import fr.xephi.authme.libs.org.postgresql.core.SqlCommand;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.lock.qual.GuardedBy;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.RequiresNonNull;

public class PgStatement implements Statement, BaseStatement {
   private static final String[] NO_RETURNING_COLUMNS = new String[0];
   private static final boolean DEFAULT_FORCE_BINARY_TRANSFERS = Boolean.getBoolean("fr.xephi.authme.libs.org.postgresql.forceBinary");
   private boolean forceBinaryTransfers;
   protected final ResourceLock lock;
   @Nullable
   protected ArrayList<Query> batchStatements;
   @Nullable
   protected ArrayList<ParameterList> batchParameters;
   protected final int resultsettype;
   protected final int concurrency;
   private final int rsHoldability;
   private boolean poolable;
   private boolean closeOnCompletion;
   protected int fetchdirection;
   @Nullable
   private volatile TimerTask cancelTimerTask;
   private static final AtomicReferenceFieldUpdater<PgStatement, TimerTask> CANCEL_TIMER_UPDATER = AtomicReferenceFieldUpdater.newUpdater(PgStatement.class, TimerTask.class, "cancelTimerTask");
   private volatile StatementCancelState statementState;
   private static final AtomicReferenceFieldUpdater<PgStatement, StatementCancelState> STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(PgStatement.class, StatementCancelState.class, "statementState");
   protected boolean wantsGeneratedKeysOnce;
   public boolean wantsGeneratedKeysAlways;
   protected final PgConnection connection;
   @Nullable
   protected volatile PSQLWarningWrapper warnings;
   protected int maxrows;
   protected int fetchSize;
   protected long timeout;
   protected boolean replaceProcessingEnabled;
   @Nullable
   protected ResultWrapper result;
   @Nullable
   @GuardedBy({"<self>"})
   protected ResultWrapper firstUnclosedResult;
   @Nullable
   protected ResultWrapper generatedKeys;
   protected int mPrepareThreshold;
   protected int maxFieldSize;
   protected boolean adaptiveFetch;
   @Nullable
   private TimestampUtils timestampUtils;
   private volatile int isClosed;
   private static final AtomicIntegerFieldUpdater<PgStatement> IS_CLOSED_UPDATER = AtomicIntegerFieldUpdater.newUpdater(PgStatement.class, "isClosed");

   PgStatement(PgConnection c, int rsType, int rsConcurrency, int rsHoldability) throws SQLException {
      this.forceBinaryTransfers = DEFAULT_FORCE_BINARY_TRANSFERS;
      this.lock = new ResourceLock();
      this.fetchdirection = 1000;
      this.statementState = StatementCancelState.IDLE;
      this.replaceProcessingEnabled = true;
      this.connection = c;
      this.forceBinaryTransfers |= c.getForceBinary();
      this.resultsettype = rsType;
      this.concurrency = rsConcurrency;
      this.setFetchSize(c.getDefaultFetchSize());
      this.setPrepareThreshold(c.getPrepareThreshold());
      this.setAdaptiveFetch(c.getAdaptiveFetch());
      this.rsHoldability = rsHoldability;
   }

   public ResultSet createResultSet(@Nullable Query originalQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) throws SQLException {
      PgResultSet newResult = new PgResultSet(originalQuery, this, fields, tuples, cursor, this.getMaxRows(), this.getMaxFieldSize(), this.getResultSetType(), this.getResultSetConcurrency(), this.getResultSetHoldability(), this.getAdaptiveFetch());
      newResult.setFetchSize(this.getFetchSize());
      newResult.setFetchDirection(this.getFetchDirection());
      return newResult;
   }

   public BaseConnection getPGConnection() {
      return this.connection;
   }

   @Nullable
   public String getFetchingCursorName() {
      return null;
   }

   @NonNegative
   public int getFetchSize() {
      return this.fetchSize;
   }

   protected boolean wantsScrollableResultSet() {
      return this.resultsettype != 1003;
   }

   protected boolean wantsHoldableResultSet() {
      return this.rsHoldability == 1;
   }

   public ResultSet executeQuery(String sql) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ResultSet var3;
      try {
         if (!this.executeWithFlags((String)sql, 0)) {
            throw new PSQLException(GT.tr("No results were returned by the query."), PSQLState.NO_DATA);
         }

         var3 = this.getSingleResultSet();
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   protected ResultSet getSingleResultSet() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ResultSet var3;
      try {
         this.checkClosed();
         ResultWrapper result = (ResultWrapper)Nullness.castNonNull(this.result);
         if (result.getNext() != null) {
            throw new PSQLException(GT.tr("Multiple ResultSets were returned by the query."), PSQLState.TOO_MANY_RESULTS);
         }

         var3 = (ResultSet)Nullness.castNonNull(result.getResultSet(), "result.getResultSet()");
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public int executeUpdate(String sql) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      int var3;
      try {
         this.executeWithFlags((String)sql, 4);
         this.checkNoResultUpdate();
         var3 = this.getUpdateCount();
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   protected final void checkNoResultUpdate() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.checkClosed();

         for(ResultWrapper iter = this.result; iter != null; iter = iter.getNext()) {
            if (iter.getResultSet() != null) {
               throw new PSQLException(GT.tr("A result was returned when none was expected."), PSQLState.TOO_MANY_RESULTS);
            }
         }
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public boolean execute(String sql) throws SQLException {
      return this.executeWithFlags((String)sql, 0);
   }

   public boolean executeWithFlags(String sql, int flags) throws SQLException {
      return this.executeCachedSql(sql, flags, NO_RETURNING_COLUMNS);
   }

   private boolean executeCachedSql(String sql, int flags, @Nullable String[] columnNames) throws SQLException {
      PreferQueryMode preferQueryMode = this.connection.getPreferQueryMode();
      boolean shouldUseParameterized = false;
      QueryExecutor queryExecutor = this.connection.getQueryExecutor();
      Object key = queryExecutor.createQueryKey(sql, this.replaceProcessingEnabled, shouldUseParameterized, columnNames);
      boolean shouldCache = preferQueryMode == PreferQueryMode.EXTENDED_CACHE_EVERYTHING;
      CachedQuery cachedQuery;
      if (shouldCache) {
         cachedQuery = queryExecutor.borrowQueryByKey(key);
      } else {
         cachedQuery = queryExecutor.createQueryByKey(key);
      }

      if (this.wantsGeneratedKeysOnce) {
         SqlCommand sqlCommand = cachedQuery.query.getSqlCommand();
         this.wantsGeneratedKeysOnce = sqlCommand != null && sqlCommand.isReturningKeywordPresent();
      }

      boolean res;
      try {
         res = this.executeWithFlags(cachedQuery, flags);
      } finally {
         if (shouldCache) {
            queryExecutor.releaseQuery(cachedQuery);
         }

      }

      return res;
   }

   public boolean executeWithFlags(CachedQuery simpleQuery, int flags) throws SQLException {
      this.checkClosed();
      if (this.connection.getPreferQueryMode().compareTo(PreferQueryMode.EXTENDED) < 0) {
         flags |= 1024;
      }

      this.execute(simpleQuery, (ParameterList)null, flags);
      ResourceLock ignore = this.lock.obtain();

      boolean var4;
      try {
         this.checkClosed();
         var4 = this.result != null && this.result.getResultSet() != null;
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var4;
   }

   public boolean executeWithFlags(int flags) throws SQLException {
      this.checkClosed();
      throw new PSQLException(GT.tr("Can''t use executeWithFlags(int) on a Statement."), PSQLState.WRONG_OBJECT_TYPE);
   }

   private void closeUnclosedProcessedResults() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         ResultWrapper resultWrapper = this.firstUnclosedResult;
         ResultWrapper currentResult = this.result;

         while(true) {
            if (resultWrapper == currentResult || resultWrapper == null) {
               this.firstUnclosedResult = resultWrapper;
               break;
            }

            PgResultSet rs = (PgResultSet)resultWrapper.getResultSet();
            if (rs != null) {
               rs.closeInternally();
            }

            resultWrapper = resultWrapper.getNext();
         }
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   protected void closeForNextExecution() throws SQLException {
      this.clearWarnings();
      ResourceLock ignore = this.lock.obtain();

      try {
         this.closeUnclosedProcessedResults();
         if (this.result != null && this.result.getResultSet() != null) {
            this.result.getResultSet().close();
         }

         this.result = null;
         ResultWrapper generatedKeys = this.generatedKeys;
         if (generatedKeys != null) {
            ResultSet resultSet = generatedKeys.getResultSet();
            if (resultSet != null) {
               resultSet.close();
            }

            this.generatedKeys = null;
         }
      } catch (Throwable var5) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var4) {
               var5.addSuppressed(var4);
            }
         }

         throw var5;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   protected boolean isOneShotQuery(@Nullable CachedQuery cachedQuery) {
      if (cachedQuery == null) {
         return true;
      } else {
         cachedQuery.increaseExecuteCount();
         return (this.mPrepareThreshold == 0 || cachedQuery.getExecuteCount() < this.mPrepareThreshold) && !this.getForceBinaryTransfer();
      }
   }

   protected final void execute(CachedQuery cachedQuery, @Nullable ParameterList queryParameters, int flags) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         try {
            this.executeInternal(cachedQuery, queryParameters, flags);
         } catch (SQLException var8) {
            if (cachedQuery.query.getSubqueries() != null || !this.connection.getQueryExecutor().willHealOnRetry(var8)) {
               throw var8;
            }

            cachedQuery.query.close();
            this.executeInternal(cachedQuery, queryParameters, flags);
         }
      } catch (Throwable var9) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var9.addSuppressed(var7);
            }
         }

         throw var9;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   private void executeInternal(CachedQuery cachedQuery, @Nullable ParameterList queryParameters, int flags) throws SQLException {
      this.closeForNextExecution();
      if (this.fetchSize > 0 && !this.wantsScrollableResultSet() && !this.connection.getAutoCommit() && !this.wantsHoldableResultSet()) {
         flags |= 8;
      }

      if (this.wantsGeneratedKeysOnce || this.wantsGeneratedKeysAlways) {
         flags |= 64;
         if ((flags & 4) != 0) {
            flags &= -5;
         }
      }

      if (this.isOneShotQuery(cachedQuery)) {
         flags |= 1;
      }

      if (this.connection.getAutoCommit()) {
         flags |= 16;
      }

      if (this.connection.hintReadOnly()) {
         flags |= 2048;
      }

      if (this.concurrency != 1007) {
         flags |= 256;
      }

      Query queryToExecute = cachedQuery.query;
      if (queryToExecute.isEmpty()) {
         flags |= 16;
      }

      ResultWrapper currentResult;
      if (!queryToExecute.isStatementDescribed() && this.forceBinaryTransfers && (flags & 1024) == 0) {
         int flags2 = flags | 32;
         PgStatement.StatementResultHandler handler2 = new PgStatement.StatementResultHandler();
         this.connection.getQueryExecutor().execute((Query)queryToExecute, (ParameterList)queryParameters, (ResultHandler)handler2, 0, 0, flags2);
         currentResult = handler2.getResults();
         if (currentResult != null) {
            ((ResultSet)Nullness.castNonNull(currentResult.getResultSet(), "result2.getResultSet()")).close();
         }
      }

      PgStatement.StatementResultHandler handler = new PgStatement.StatementResultHandler();
      ResourceLock ignore = this.lock.obtain();

      try {
         this.result = null;
      } catch (Throwable var18) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var16) {
               var18.addSuppressed(var16);
            }
         }

         throw var18;
      }

      if (ignore != null) {
         ignore.close();
      }

      try {
         this.startTimer();
         this.connection.getQueryExecutor().execute((Query)queryToExecute, (ParameterList)queryParameters, (ResultHandler)handler, this.maxrows, this.fetchSize, flags, this.adaptiveFetch);
      } finally {
         this.killTimerTask();
      }

      ignore = this.lock.obtain();

      try {
         this.checkClosed();
         currentResult = handler.getResults();
         this.result = this.firstUnclosedResult = currentResult;
         if (this.wantsGeneratedKeysOnce || this.wantsGeneratedKeysAlways) {
            this.generatedKeys = currentResult;
            this.result = ((ResultWrapper)Nullness.castNonNull(currentResult, "handler.getResults()")).getNext();
            if (this.wantsGeneratedKeysOnce) {
               this.wantsGeneratedKeysOnce = false;
            }
         }
      } catch (Throwable var19) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var15) {
               var19.addSuppressed(var15);
            }
         }

         throw var19;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void setCursorName(String name) throws SQLException {
      this.checkClosed();
   }

   public int getUpdateCount() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      byte var7;
      label49: {
         int var4;
         try {
            this.checkClosed();
            if (this.result == null || this.result.getResultSet() != null) {
               var7 = -1;
               break label49;
            }

            long count = this.result.getUpdateCount();
            var4 = count > 2147483647L ? -2 : (int)count;
         } catch (Throwable var6) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var7;
   }

   public boolean getMoreResults() throws SQLException {
      return this.getMoreResults(3);
   }

   public int getMaxRows() throws SQLException {
      this.checkClosed();
      return this.maxrows;
   }

   public void setMaxRows(int max) throws SQLException {
      this.checkClosed();
      if (max < 0) {
         throw new PSQLException(GT.tr("Maximum number of rows must be a value greater than or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.maxrows = max;
      }
   }

   public void setEscapeProcessing(boolean enable) throws SQLException {
      this.checkClosed();
      this.replaceProcessingEnabled = enable;
   }

   public int getQueryTimeout() throws SQLException {
      this.checkClosed();
      long seconds = this.timeout / 1000L;
      return seconds >= 2147483647L ? Integer.MAX_VALUE : (int)seconds;
   }

   public void setQueryTimeout(int seconds) throws SQLException {
      this.setQueryTimeoutMs((long)seconds * 1000L);
   }

   public long getQueryTimeoutMs() throws SQLException {
      this.checkClosed();
      return this.timeout;
   }

   public void setQueryTimeoutMs(long millis) throws SQLException {
      this.checkClosed();
      if (millis < 0L) {
         throw new PSQLException(GT.tr("Query timeout must be a value greater than or equals to 0."), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.timeout = millis;
      }
   }

   public void addWarning(SQLWarning warn) {
      PSQLWarningWrapper warnWrap = this.warnings;
      if (warnWrap == null) {
         this.warnings = new PSQLWarningWrapper(warn);
      } else {
         warnWrap.addWarning(warn);
      }

   }

   @Nullable
   public SQLWarning getWarnings() throws SQLException {
      this.checkClosed();
      PSQLWarningWrapper warnWrap = this.warnings;
      return warnWrap != null ? warnWrap.getFirstWarning() : null;
   }

   public int getMaxFieldSize() throws SQLException {
      return this.maxFieldSize;
   }

   public void setMaxFieldSize(int max) throws SQLException {
      this.checkClosed();
      if (max < 0) {
         throw new PSQLException(GT.tr("The maximum field size must be a value greater than or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.maxFieldSize = max;
      }
   }

   public void clearWarnings() throws SQLException {
      this.warnings = null;
   }

   @Nullable
   public ResultSet getResultSet() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ResultSet var2;
      label43: {
         try {
            this.checkClosed();
            if (this.result == null) {
               var2 = null;
               break label43;
            }

            var2 = this.result.getResultSet();
         } catch (Throwable var5) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public final void close() throws SQLException {
      if (IS_CLOSED_UPDATER.compareAndSet(this, 0, 1)) {
         this.cancel();
         this.closeForNextExecution();
         this.closeImpl();
      }
   }

   protected void closeImpl() throws SQLException {
   }

   public long getLastOID() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var2;
      label43: {
         try {
            this.checkClosed();
            if (this.result == null) {
               var2 = 0L;
               break label43;
            }

            var2 = this.result.getInsertOID();
         } catch (Throwable var5) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public void setPrepareThreshold(int newThreshold) throws SQLException {
      this.checkClosed();
      if (newThreshold < 0) {
         this.forceBinaryTransfers = true;
         newThreshold = 1;
      }

      this.mPrepareThreshold = newThreshold;
   }

   public int getPrepareThreshold() {
      return this.mPrepareThreshold;
   }

   public void setUseServerPrepare(boolean flag) throws SQLException {
      this.setPrepareThreshold(flag ? 1 : 0);
   }

   public boolean isUseServerPrepare() {
      return false;
   }

   protected void checkClosed() throws SQLException {
      if (this.isClosed()) {
         throw new PSQLException(GT.tr("This statement has been closed."), PSQLState.OBJECT_NOT_IN_STATE);
      }
   }

   public void addBatch(String sql) throws SQLException {
      this.checkClosed();
      ArrayList<Query> batchStatements = this.batchStatements;
      if (batchStatements == null) {
         this.batchStatements = batchStatements = new ArrayList();
      }

      ArrayList<ParameterList> batchParameters = this.batchParameters;
      if (batchParameters == null) {
         this.batchParameters = batchParameters = new ArrayList();
      }

      boolean shouldUseParameterized = false;
      CachedQuery cachedQuery = this.connection.createQuery(sql, this.replaceProcessingEnabled, shouldUseParameterized);
      batchStatements.add(cachedQuery.query);
      batchParameters.add((Object)null);
   }

   public void clearBatch() throws SQLException {
      if (this.batchStatements != null) {
         this.batchStatements.clear();
      }

      if (this.batchParameters != null) {
         this.batchParameters.clear();
      }

   }

   protected BatchResultHandler createBatchHandler(Query[] queries, ParameterList[] parameterLists) {
      return new BatchResultHandler(this, queries, parameterLists, this.wantsGeneratedKeysAlways);
   }

   @RequiresNonNull({"batchStatements", "batchParameters"})
   private BatchResultHandler internalExecuteBatch() throws SQLException {
      this.transformQueriesAndParameters();
      ArrayList<Query> batchStatements = (ArrayList)Nullness.castNonNull(this.batchStatements);
      ArrayList<ParameterList> batchParameters = (ArrayList)Nullness.castNonNull(this.batchParameters);
      Query[] queries = (Query[])batchStatements.toArray(new Query[0]);
      ParameterList[] parameterLists = (ParameterList[])batchParameters.toArray(new ParameterList[0]);
      batchStatements.clear();
      batchParameters.clear();
      boolean preDescribe = false;
      int flags;
      if (this.wantsGeneratedKeysAlways) {
         flags = 320;
      } else {
         flags = 4;
      }

      PreferQueryMode preferQueryMode = this.connection.getPreferQueryMode();
      if (preferQueryMode == PreferQueryMode.SIMPLE || preferQueryMode == PreferQueryMode.EXTENDED_FOR_PREPARED && parameterLists[0] == null) {
         flags |= 1024;
      }

      boolean sameQueryAhead = queries.length > 1 && queries[0] == queries[1];
      if (sameQueryAhead && !this.isOneShotQuery((CachedQuery)null)) {
         preDescribe = (this.wantsGeneratedKeysAlways || sameQueryAhead) && !queries[0].isStatementDescribed();
         flags |= 512;
      } else {
         flags |= 1;
      }

      if (this.connection.getAutoCommit()) {
         flags |= 16;
      }

      if (this.connection.hintReadOnly()) {
         flags |= 2048;
      }

      BatchResultHandler handler = this.createBatchHandler(queries, parameterLists);
      if ((preDescribe || this.forceBinaryTransfers) && (flags & 1024) == 0) {
         int flags2 = flags | 32;
         PgStatement.StatementResultHandler handler2 = new PgStatement.StatementResultHandler();

         try {
            this.connection.getQueryExecutor().execute((Query)queries[0], (ParameterList)parameterLists[0], (ResultHandler)handler2, 0, 0, flags2);
         } catch (SQLException var29) {
            handler.handleError(var29);
            handler.handleCompletion();
         }

         ResultWrapper result2 = handler2.getResults();
         if (result2 != null) {
            ((ResultSet)Nullness.castNonNull(result2.getResultSet(), "result2.getResultSet()")).close();
         }
      }

      ResourceLock ignore = this.lock.obtain();

      try {
         this.result = null;
      } catch (Throwable var30) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var28) {
               var30.addSuppressed(var28);
            }
         }

         throw var30;
      }

      if (ignore != null) {
         ignore.close();
      }

      boolean var25 = false;

      try {
         var25 = true;
         this.startTimer();
         this.connection.getQueryExecutor().execute(queries, parameterLists, handler, this.maxrows, this.fetchSize, flags, this.adaptiveFetch);
         var25 = false;
      } finally {
         if (var25) {
            this.killTimerTask();
            ResourceLock ignore = this.lock.obtain();

            try {
               this.checkClosed();
               if (this.wantsGeneratedKeysAlways) {
                  this.generatedKeys = new ResultWrapper(handler.getGeneratedKeys());
               }
            } catch (Throwable var31) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var27) {
                     var31.addSuppressed(var27);
                  }
               }

               throw var31;
            }

            if (ignore != null) {
               ignore.close();
            }

         }
      }

      this.killTimerTask();
      ignore = this.lock.obtain();

      try {
         this.checkClosed();
         if (this.wantsGeneratedKeysAlways) {
            this.generatedKeys = new ResultWrapper(handler.getGeneratedKeys());
         }
      } catch (Throwable var33) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var26) {
               var33.addSuppressed(var26);
            }
         }

         throw var33;
      }

      if (ignore != null) {
         ignore.close();
      }

      return handler;
   }

   public int[] executeBatch() throws SQLException {
      this.checkClosed();
      this.closeForNextExecution();
      return this.batchStatements != null && !this.batchStatements.isEmpty() && this.batchParameters != null ? this.internalExecuteBatch().getUpdateCount() : new int[0];
   }

   public void cancel() throws SQLException {
      if (this.statementState != StatementCancelState.IDLE) {
         if (STATE_UPDATER.compareAndSet(this, StatementCancelState.IN_QUERY, StatementCancelState.CANCELING)) {
            ResourceLock connectionLock = this.connection.obtainLock();

            try {
               try {
                  this.connection.cancelQuery();
               } finally {
                  STATE_UPDATER.set(this, StatementCancelState.CANCELLED);
                  this.connection.lockCondition().signalAll();
               }
            } catch (Throwable var9) {
               if (connectionLock != null) {
                  try {
                     connectionLock.close();
                  } catch (Throwable var7) {
                     var9.addSuppressed(var7);
                  }
               }

               throw var9;
            }

            if (connectionLock != null) {
               connectionLock.close();
            }

         }
      }
   }

   public Connection getConnection() throws SQLException {
      return this.connection;
   }

   public int getFetchDirection() {
      return this.fetchdirection;
   }

   public int getResultSetConcurrency() {
      return this.concurrency;
   }

   public int getResultSetType() {
      return this.resultsettype;
   }

   public void setFetchDirection(int direction) throws SQLException {
      switch(direction) {
      case 1000:
      case 1001:
      case 1002:
         this.fetchdirection = direction;
         return;
      default:
         throw new PSQLException(GT.tr("Invalid fetch direction constant: {0}.", direction), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   public void setFetchSize(@NonNegative int rows) throws SQLException {
      this.checkClosed();
      if (rows < 0) {
         throw new PSQLException(GT.tr("Fetch size must be a value greater to or equal to 0."), PSQLState.INVALID_PARAMETER_VALUE);
      } else {
         this.fetchSize = rows;
      }
   }

   private void startTimer() {
      this.cleanupTimer();
      STATE_UPDATER.set(this, StatementCancelState.IN_QUERY);
      if (this.timeout != 0L) {
         TimerTask cancelTask = new StatementCancelTimerTask(this);
         CANCEL_TIMER_UPDATER.set(this, cancelTask);
         this.connection.addTimerTask(cancelTask, this.timeout);
      }
   }

   void cancelIfStillNeeded(TimerTask timerTask) {
      try {
         if (!CANCEL_TIMER_UPDATER.compareAndSet(this, timerTask, (Object)null)) {
            return;
         }

         this.cancel();
      } catch (SQLException var3) {
      }

   }

   private boolean cleanupTimer() {
      TimerTask timerTask = (TimerTask)CANCEL_TIMER_UPDATER.get(this);
      if (timerTask == null) {
         return this.timeout == 0L;
      } else if (!CANCEL_TIMER_UPDATER.compareAndSet(this, timerTask, (Object)null)) {
         return false;
      } else {
         timerTask.cancel();
         this.connection.purgeTimerTasks();
         return true;
      }
   }

   private void killTimerTask() {
      boolean timerTaskIsClear = this.cleanupTimer();
      if (!timerTaskIsClear || !STATE_UPDATER.compareAndSet(this, StatementCancelState.IN_QUERY, StatementCancelState.IDLE)) {
         boolean interrupted = false;
         ResourceLock connectionLock = this.connection.obtainLock();

         try {
            while(!STATE_UPDATER.compareAndSet(this, StatementCancelState.CANCELLED, StatementCancelState.IDLE)) {
               try {
                  this.connection.lockCondition().await(10L, TimeUnit.MILLISECONDS);
               } catch (InterruptedException var7) {
                  interrupted = true;
               }
            }
         } catch (Throwable var8) {
            if (connectionLock != null) {
               try {
                  connectionLock.close();
               } catch (Throwable var6) {
                  var8.addSuppressed(var6);
               }
            }

            throw var8;
         }

         if (connectionLock != null) {
            connectionLock.close();
         }

         if (interrupted) {
            Thread.currentThread().interrupt();
         }

      }
   }

   protected boolean getForceBinaryTransfer() {
      return this.forceBinaryTransfers;
   }

   public long getLargeUpdateCount() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var2;
      label45: {
         try {
            this.checkClosed();
            if (this.result != null && this.result.getResultSet() == null) {
               var2 = this.result.getUpdateCount();
               break label45;
            }

            var2 = -1L;
         } catch (Throwable var5) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public void setLargeMaxRows(long max) throws SQLException {
      throw Driver.notImplemented(this.getClass(), "setLargeMaxRows");
   }

   public long getLargeMaxRows() throws SQLException {
      throw Driver.notImplemented(this.getClass(), "getLargeMaxRows");
   }

   public long[] executeLargeBatch() throws SQLException {
      this.checkClosed();
      this.closeForNextExecution();
      return this.batchStatements != null && !this.batchStatements.isEmpty() && this.batchParameters != null ? this.internalExecuteBatch().getLargeUpdateCount() : new long[0];
   }

   public long executeLargeUpdate(String sql) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var3;
      try {
         this.executeWithFlags((String)sql, 4);
         this.checkNoResultUpdate();
         var3 = this.getLargeUpdateCount();
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
      return autoGeneratedKeys == 2 ? this.executeLargeUpdate(sql) : this.executeLargeUpdate(sql, (String[])null);
   }

   public long executeLargeUpdate(String sql, int[] columnIndexes) throws SQLException {
      if (columnIndexes != null && columnIndexes.length != 0) {
         throw new PSQLException(GT.tr("Returning autogenerated keys by column index is not supported."), PSQLState.NOT_IMPLEMENTED);
      } else {
         return this.executeLargeUpdate(sql);
      }
   }

   public long executeLargeUpdate(String sql, @Nullable String[] columnNames) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var4;
      label49: {
         try {
            if (columnNames != null && columnNames.length == 0) {
               var4 = this.executeLargeUpdate(sql);
               break label49;
            }

            this.wantsGeneratedKeysOnce = true;
            if (!this.executeCachedSql(sql, 0, columnNames)) {
            }

            var4 = this.getLargeUpdateCount();
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var4;
   }

   public boolean isClosed() throws SQLException {
      return this.isClosed == 1;
   }

   public void setPoolable(boolean poolable) throws SQLException {
      this.checkClosed();
      this.poolable = poolable;
   }

   public boolean isPoolable() throws SQLException {
      this.checkClosed();
      return this.poolable;
   }

   public boolean isWrapperFor(Class<?> iface) throws SQLException {
      return iface.isAssignableFrom(this.getClass());
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (iface.isAssignableFrom(this.getClass())) {
         return iface.cast(this);
      } else {
         throw new SQLException("Cannot unwrap to " + iface.getName());
      }
   }

   public void closeOnCompletion() throws SQLException {
      this.closeOnCompletion = true;
   }

   public boolean isCloseOnCompletion() throws SQLException {
      return this.closeOnCompletion;
   }

   protected void checkCompletion() throws SQLException {
      if (this.closeOnCompletion) {
         ResourceLock ignore = this.lock.obtain();

         label117: {
            try {
               for(ResultWrapper result = this.firstUnclosedResult; result != null; result = result.getNext()) {
                  ResultSet resultSet = result.getResultSet();
                  if (resultSet != null && !resultSet.isClosed()) {
                     break label117;
                  }
               }
            } catch (Throwable var10) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var8) {
                     var10.addSuppressed(var8);
                  }
               }

               throw var10;
            }

            if (ignore != null) {
               ignore.close();
            }

            this.closeOnCompletion = false;

            try {
               this.close();
            } finally {
               this.closeOnCompletion = true;
            }

            return;
         }

         if (ignore != null) {
            ignore.close();
         }

      }
   }

   public boolean getMoreResults(int current) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      boolean var3;
      try {
         this.checkClosed();
         if (current == 1 && this.result != null && this.result.getResultSet() != null) {
            this.result.getResultSet().close();
         }

         if (this.result != null) {
            this.result = this.result.getNext();
         }

         if (current == 3) {
            this.closeUnclosedProcessedResults();
         }

         var3 = this.result != null && this.result.getResultSet() != null;
      } catch (Throwable var6) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public ResultSet getGeneratedKeys() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      ResultSet var2;
      label45: {
         try {
            this.checkClosed();
            if (this.generatedKeys != null && this.generatedKeys.getResultSet() != null) {
               var2 = this.generatedKeys.getResultSet();
               break label45;
            }

            var2 = this.createDriverResultSet(new Field[0], new ArrayList());
         } catch (Throwable var5) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var2;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var2;
   }

   public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
      return autoGeneratedKeys == 2 ? this.executeUpdate(sql) : this.executeUpdate(sql, (String[])null);
   }

   public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
      if (columnIndexes != null && columnIndexes.length != 0) {
         throw new PSQLException(GT.tr("Returning autogenerated keys by column index is not supported."), PSQLState.NOT_IMPLEMENTED);
      } else {
         return this.executeUpdate(sql);
      }
   }

   public int executeUpdate(String sql, @Nullable String[] columnNames) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      int var4;
      label49: {
         try {
            if (columnNames != null && columnNames.length == 0) {
               var4 = this.executeUpdate(sql);
               break label49;
            }

            this.wantsGeneratedKeysOnce = true;
            if (!this.executeCachedSql(sql, 0, columnNames)) {
            }

            var4 = this.getUpdateCount();
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var4;
   }

   public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
      return autoGeneratedKeys == 2 ? this.execute(sql) : this.execute(sql, (String[])null);
   }

   public boolean execute(String sql, @Nullable int[] columnIndexes) throws SQLException {
      if (columnIndexes != null && columnIndexes.length == 0) {
         return this.execute(sql);
      } else {
         throw new PSQLException(GT.tr("Returning autogenerated keys by column index is not supported."), PSQLState.NOT_IMPLEMENTED);
      }
   }

   public boolean execute(String sql, @Nullable String[] columnNames) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      boolean var4;
      label45: {
         try {
            if (columnNames != null && columnNames.length == 0) {
               var4 = this.execute(sql);
               break label45;
            }

            this.wantsGeneratedKeysOnce = true;
            var4 = this.executeCachedSql(sql, 0, columnNames);
         } catch (Throwable var7) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (ignore != null) {
            ignore.close();
         }

         return var4;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var4;
   }

   public int getResultSetHoldability() throws SQLException {
      return this.rsHoldability;
   }

   public ResultSet createDriverResultSet(Field[] fields, List<Tuple> tuples) throws SQLException {
      return this.createResultSet((Query)null, fields, tuples, (ResultCursor)null);
   }

   protected void transformQueriesAndParameters() throws SQLException {
   }

   public void setAdaptiveFetch(boolean adaptiveFetch) {
      this.adaptiveFetch = adaptiveFetch;
   }

   public boolean getAdaptiveFetch() {
      return this.adaptiveFetch;
   }

   protected TimestampUtils getTimestampUtils() {
      if (this.timestampUtils == null) {
         this.timestampUtils = new TimestampUtils(!this.connection.getQueryExecutor().getIntegerDateTimes(), new QueryExecutorTimeZoneProvider(this.connection.getQueryExecutor()));
      }

      return this.timestampUtils;
   }

   public class StatementResultHandler extends ResultHandlerBase {
      @Nullable
      private ResultWrapper results;
      @Nullable
      private ResultWrapper lastResult;

      @Nullable
      ResultWrapper getResults() {
         return this.results;
      }

      private void append(ResultWrapper newResult) {
         if (this.results == null) {
            this.lastResult = this.results = newResult;
         } else {
            ((ResultWrapper)Nullness.castNonNull(this.lastResult)).append(newResult);
         }

      }

      public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
         try {
            ResultSet rs = PgStatement.this.createResultSet(fromQuery, fields, tuples, cursor);
            this.append(new ResultWrapper(rs));
         } catch (SQLException var6) {
            this.handleError(var6);
         }

      }

      public void handleCommandStatus(String status, long updateCount, long insertOID) {
         this.append(new ResultWrapper(updateCount, insertOID));
      }

      public void handleWarning(SQLWarning warning) {
         PgStatement.this.addWarning(warning);
      }
   }
}
