package fr.xephi.authme.libs.org.postgresql.core.v3;

import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.copy.CopyIn;
import fr.xephi.authme.libs.org.postgresql.copy.CopyOperation;
import fr.xephi.authme.libs.org.postgresql.copy.CopyOut;
import fr.xephi.authme.libs.org.postgresql.core.CommandCompleteParser;
import fr.xephi.authme.libs.org.postgresql.core.Encoding;
import fr.xephi.authme.libs.org.postgresql.core.EncodingPredictor;
import fr.xephi.authme.libs.org.postgresql.core.Field;
import fr.xephi.authme.libs.org.postgresql.core.NativeQuery;
import fr.xephi.authme.libs.org.postgresql.core.Notification;
import fr.xephi.authme.libs.org.postgresql.core.Oid;
import fr.xephi.authme.libs.org.postgresql.core.PGBindException;
import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.core.ParameterList;
import fr.xephi.authme.libs.org.postgresql.core.Parser;
import fr.xephi.authme.libs.org.postgresql.core.Query;
import fr.xephi.authme.libs.org.postgresql.core.QueryExecutorBase;
import fr.xephi.authme.libs.org.postgresql.core.ReplicationProtocol;
import fr.xephi.authme.libs.org.postgresql.core.ResultCursor;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandler;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandlerBase;
import fr.xephi.authme.libs.org.postgresql.core.ResultHandlerDelegate;
import fr.xephi.authme.libs.org.postgresql.core.SqlCommand;
import fr.xephi.authme.libs.org.postgresql.core.SqlCommandType;
import fr.xephi.authme.libs.org.postgresql.core.TransactionState;
import fr.xephi.authme.libs.org.postgresql.core.Tuple;
import fr.xephi.authme.libs.org.postgresql.core.v3.adaptivefetch.AdaptiveFetchCache;
import fr.xephi.authme.libs.org.postgresql.core.v3.replication.V3ReplicationProtocol;
import fr.xephi.authme.libs.org.postgresql.jdbc.AutoSave;
import fr.xephi.authme.libs.org.postgresql.jdbc.BatchResultHandler;
import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import fr.xephi.authme.libs.org.postgresql.jdbc.TimestampUtils;
import fr.xephi.authme.libs.org.postgresql.util.ByteStreamWriter;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.PSQLWarning;
import fr.xephi.authme.libs.org.postgresql.util.ServerErrorMessage;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class QueryExecutorImpl extends QueryExecutorBase {
   private static final Logger LOGGER = Logger.getLogger(QueryExecutorImpl.class.getName());
   private static final Field[] NO_FIELDS = new Field[0];
   @Nullable
   private TimeZone timeZone;
   @Nullable
   private String applicationName;
   private boolean integerDateTimes;
   private final Set<Integer> useBinaryReceiveForOids = new HashSet();
   private final Set<Integer> useBinarySendForOids = new HashSet();
   private final SimpleQuery sync;
   private short deallocateEpoch;
   @Nullable
   private String lastSetSearchPathQuery;
   @Nullable
   private SQLException transactionFailCause;
   private final ReplicationProtocol replicationProtocol;
   private final CommandCompleteParser commandCompleteParser;
   private final AdaptiveFetchCache adaptiveFetchCache;
   @Nullable
   private Object lockedFor;
   private static final int MAX_BUFFERED_RECV_BYTES = 64000;
   private static final int NODATA_QUERY_RESPONSE_SIZE_BYTES = 250;
   AtomicBoolean processingCopyResults;
   private final HashMap<PhantomReference<SimpleQuery>, String> parsedQueryMap;
   private final ReferenceQueue<SimpleQuery> parsedQueryCleanupQueue;
   private final HashMap<PhantomReference<Portal>, String> openPortalMap;
   private final ReferenceQueue<Portal> openPortalCleanupQueue;
   private static final Portal UNNAMED_PORTAL;
   private final Deque<SimpleQuery> pendingParseQueue;
   private final Deque<Portal> pendingBindQueue;
   private final Deque<ExecuteRequest> pendingExecuteQueue;
   private final Deque<DescribeRequest> pendingDescribeStatementQueue;
   private final Deque<SimpleQuery> pendingDescribePortalQueue;
   private long nextUniqueID;
   private final boolean allowEncodingChanges;
   private final boolean cleanupSavePoints;
   private int estimatedReceiveBufferBytes;
   private final SimpleQuery beginTransactionQuery;
   private final SimpleQuery beginReadOnlyTransactionQuery;
   private final SimpleQuery emptyQuery;
   private final SimpleQuery autoSaveQuery;
   private final SimpleQuery releaseAutoSave;
   private final SimpleQuery restoreToAutoSave;

   public QueryExecutorImpl(PGStream pgStream, int cancelSignalTimeout, Properties info) throws SQLException, IOException {
      super(pgStream, cancelSignalTimeout, info);
      this.sync = (SimpleQuery)this.createQuery("SYNC", false, true, new String[0]).query;
      this.commandCompleteParser = new CommandCompleteParser();
      this.processingCopyResults = new AtomicBoolean(false);
      this.parsedQueryMap = new HashMap();
      this.parsedQueryCleanupQueue = new ReferenceQueue();
      this.openPortalMap = new HashMap();
      this.openPortalCleanupQueue = new ReferenceQueue();
      this.pendingParseQueue = new ArrayDeque();
      this.pendingBindQueue = new ArrayDeque();
      this.pendingExecuteQueue = new ArrayDeque();
      this.pendingDescribeStatementQueue = new ArrayDeque();
      this.pendingDescribePortalQueue = new ArrayDeque();
      this.nextUniqueID = 1L;
      this.beginTransactionQuery = new SimpleQuery(new NativeQuery("BEGIN", (int[])null, false, SqlCommand.BLANK), (TypeTransferModeRegistry)null, false);
      this.beginReadOnlyTransactionQuery = new SimpleQuery(new NativeQuery("BEGIN READ ONLY", (int[])null, false, SqlCommand.BLANK), (TypeTransferModeRegistry)null, false);
      this.emptyQuery = new SimpleQuery(new NativeQuery("", (int[])null, false, SqlCommand.createStatementTypeInfo(SqlCommandType.BLANK)), (TypeTransferModeRegistry)null, false);
      this.autoSaveQuery = new SimpleQuery(new NativeQuery("SAVEPOINT PGJDBC_AUTOSAVE", (int[])null, false, SqlCommand.BLANK), (TypeTransferModeRegistry)null, false);
      this.releaseAutoSave = new SimpleQuery(new NativeQuery("RELEASE SAVEPOINT PGJDBC_AUTOSAVE", (int[])null, false, SqlCommand.BLANK), (TypeTransferModeRegistry)null, false);
      this.restoreToAutoSave = new SimpleQuery(new NativeQuery("ROLLBACK TO SAVEPOINT PGJDBC_AUTOSAVE", (int[])null, false, SqlCommand.BLANK), (TypeTransferModeRegistry)null, false);
      long maxResultBuffer = pgStream.getMaxResultBuffer();
      this.adaptiveFetchCache = new AdaptiveFetchCache(maxResultBuffer, info);
      this.allowEncodingChanges = PGProperty.ALLOW_ENCODING_CHANGES.getBoolean(info);
      this.cleanupSavePoints = PGProperty.CLEANUP_SAVEPOINTS.getBoolean(info);
      this.replicationProtocol = new V3ReplicationProtocol(this, pgStream);
      this.readStartupMessages();
   }

   public int getProtocolVersion() {
      return 3;
   }

   private void lock(Object obtainer) throws PSQLException {
      if (this.lockedFor == obtainer) {
         throw new PSQLException(GT.tr("Tried to obtain lock while already holding it"), PSQLState.OBJECT_NOT_IN_STATE);
      } else {
         this.waitOnLock();
         this.lockedFor = obtainer;
      }
   }

   private void unlock(Object holder) throws PSQLException {
      if (this.lockedFor != holder) {
         throw new PSQLException(GT.tr("Tried to break lock on database connection"), PSQLState.OBJECT_NOT_IN_STATE);
      } else {
         this.lockedFor = null;
         this.lockCondition.signal();
      }
   }

   private void waitOnLock() throws PSQLException {
      while(this.lockedFor != null) {
         try {
            this.lockCondition.await();
         } catch (InterruptedException var2) {
            Thread.currentThread().interrupt();
            throw new PSQLException(GT.tr("Interrupted while waiting to obtain lock on database connection"), PSQLState.OBJECT_NOT_IN_STATE, var2);
         }
      }

   }

   boolean hasLockOn(@Nullable Object holder) {
      ResourceLock ignore = this.lock.obtain();

      boolean var3;
      try {
         var3 = this.lockedFor == holder;
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

   private boolean hasLock(@Nullable Object holder) {
      return this.lockedFor == holder;
   }

   public Query createSimpleQuery(String sql) throws SQLException {
      List<NativeQuery> queries = Parser.parseJdbcSql(sql, this.getStandardConformingStrings(), false, true, this.isReWriteBatchedInsertsEnabled(), this.getQuoteReturningIdentifiers());
      return this.wrap(queries);
   }

   public Query wrap(List<NativeQuery> queries) {
      if (queries.isEmpty()) {
         return this.emptyQuery;
      } else {
         int offset;
         if (queries.size() == 1) {
            NativeQuery firstQuery = (NativeQuery)queries.get(0);
            if (this.isReWriteBatchedInsertsEnabled() && firstQuery.getCommand().isBatchedReWriteCompatible()) {
               int valuesBraceOpenPosition = firstQuery.getCommand().getBatchRewriteValuesBraceOpenPosition();
               offset = firstQuery.getCommand().getBatchRewriteValuesBraceClosePosition();
               return new BatchedQuery(firstQuery, this, valuesBraceOpenPosition, offset, this.isColumnSanitiserDisabled());
            } else {
               return new SimpleQuery(firstQuery, this, this.isColumnSanitiserDisabled());
            }
         } else {
            SimpleQuery[] subqueries = new SimpleQuery[queries.size()];
            int[] offsets = new int[subqueries.length];
            offset = 0;

            for(int i = 0; i < queries.size(); ++i) {
               NativeQuery nativeQuery = (NativeQuery)queries.get(i);
               offsets[i] = offset;
               subqueries[i] = new SimpleQuery(nativeQuery, this, this.isColumnSanitiserDisabled());
               offset += nativeQuery.bindPositions.length;
            }

            return new CompositeQuery(subqueries, offsets);
         }
      }
   }

   private int updateQueryMode(int flags) {
      switch(this.getPreferQueryMode()) {
      case SIMPLE:
         return flags | 1024;
      case EXTENDED:
         return flags & -1025;
      default:
         return flags;
      }
   }

   public void execute(Query query, @Nullable ParameterList parameters, ResultHandler handler, int maxRows, int fetchSize, int flags) throws SQLException {
      this.execute(query, parameters, handler, maxRows, fetchSize, flags, false);
   }

   public void execute(Query query, @Nullable ParameterList parameters, ResultHandler handler, int maxRows, int fetchSize, int flags, boolean adaptiveFetch) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.waitOnLock();
         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "  simple execute, handler={0}, maxRows={1}, fetchSize={2}, flags={3}", new Object[]{handler, maxRows, fetchSize, flags});
         }

         if (parameters == null) {
            parameters = SimpleQuery.NO_PARAMETERS;
         }

         flags = this.updateQueryMode(flags);
         boolean describeOnly = (32 & flags) != 0;
         ((V3ParameterList)parameters).convertFunctionOutParameters();
         if (!describeOnly) {
            ((V3ParameterList)parameters).checkAllParametersSet();
         }

         boolean autosave = false;

         try {
            try {
               handler = this.sendQueryPreamble(handler, flags);
               autosave = this.sendAutomaticSavepoint(query, flags);
               this.sendQuery(query, (V3ParameterList)parameters, maxRows, fetchSize, flags, handler, (BatchResultHandler)null, adaptiveFetch);
               if ((flags & 1024) == 0) {
                  this.sendSync();
               }

               this.processResults(handler, flags, adaptiveFetch);
               this.estimatedReceiveBufferBytes = 0;
            } catch (PGBindException var14) {
               this.sendSync();
               this.processResults(handler, flags, adaptiveFetch);
               this.estimatedReceiveBufferBytes = 0;
               handler.handleError(new PSQLException(GT.tr("Unable to bind parameter values for statement."), PSQLState.INVALID_PARAMETER_VALUE, var14.getIOException()));
            }
         } catch (IOException var15) {
            this.abort();
            handler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, var15));
         }

         try {
            handler.handleCompletion();
            if (this.cleanupSavePoints) {
               this.releaseSavePoint(autosave, flags);
            }
         } catch (SQLException var13) {
            this.rollbackIfRequired(autosave, var13);
         }
      } catch (Throwable var16) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var12) {
               var16.addSuppressed(var12);
            }
         }

         throw var16;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   private boolean sendAutomaticSavepoint(Query query, int flags) throws IOException {
      if ((flags & 16) != 0 && this.getTransactionState() != TransactionState.OPEN || query == this.restoreToAutoSave || "COMMIT".equalsIgnoreCase(query.getNativeSql()) || this.getAutoSave() == AutoSave.NEVER || this.getAutoSave() != AutoSave.ALWAYS && query instanceof SimpleQuery && ((SimpleQuery)query).getFields() == null) {
         return false;
      } else {
         this.sendOneQuery(this.autoSaveQuery, SimpleQuery.NO_PARAMETERS, 1, 0, 1030);
         return true;
      }
   }

   private void releaseSavePoint(boolean autosave, int flags) throws SQLException {
      if (autosave && this.getAutoSave() == AutoSave.ALWAYS && this.getTransactionState() == TransactionState.OPEN) {
         try {
            this.sendOneQuery(this.releaseAutoSave, SimpleQuery.NO_PARAMETERS, 1, 0, 1030);
         } catch (IOException var4) {
            throw new PSQLException(GT.tr("Error releasing savepoint"), PSQLState.IO_ERROR);
         }
      }

   }

   private void rollbackIfRequired(boolean autosave, SQLException e) throws SQLException {
      if (autosave && this.getTransactionState() == TransactionState.FAILED && (this.getAutoSave() == AutoSave.ALWAYS || this.willHealOnRetry(e))) {
         try {
            this.execute((Query)this.restoreToAutoSave, (ParameterList)SimpleQuery.NO_PARAMETERS, (ResultHandler)(new ResultHandlerDelegate((ResultHandler)null)), 1, 0, 1030);
         } catch (SQLException var4) {
            e.setNextException(var4);
         }
      }

      throw e;
   }

   public void execute(Query[] queries, ParameterList[] parameterLists, BatchResultHandler batchHandler, int maxRows, int fetchSize, int flags) throws SQLException {
      this.execute(queries, parameterLists, batchHandler, maxRows, fetchSize, flags, false);
   }

   public void execute(Query[] queries, ParameterList[] parameterLists, BatchResultHandler batchHandler, int maxRows, int fetchSize, int flags, boolean adaptiveFetch) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.waitOnLock();
         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, "  batch execute {0} queries, handler={1}, maxRows={2}, fetchSize={3}, flags={4}", new Object[]{queries.length, batchHandler, maxRows, fetchSize, flags});
         }

         flags = this.updateQueryMode(flags);
         boolean describeOnly = (32 & flags) != 0;
         int i;
         if (!describeOnly) {
            ParameterList[] var10 = parameterLists;
            int var11 = parameterLists.length;

            for(i = 0; i < var11; ++i) {
               ParameterList parameterList = var10[i];
               if (parameterList != null) {
                  ((V3ParameterList)parameterList).checkAllParametersSet();
               }
            }
         }

         boolean autosave = false;
         Object handler = batchHandler;

         try {
            handler = this.sendQueryPreamble(batchHandler, flags);
            autosave = this.sendAutomaticSavepoint(queries[0], flags);
            this.estimatedReceiveBufferBytes = 0;

            for(i = 0; i < queries.length; ++i) {
               Query query = queries[i];
               V3ParameterList parameters = (V3ParameterList)parameterLists[i];
               if (parameters == null) {
                  parameters = SimpleQuery.NO_PARAMETERS;
               }

               this.sendQuery(query, (V3ParameterList)parameters, maxRows, fetchSize, flags, (ResultHandler)handler, batchHandler, adaptiveFetch);
               if (((ResultHandler)handler).getException() != null) {
                  break;
               }
            }

            if (((ResultHandler)handler).getException() == null) {
               if ((flags & 1024) == 0) {
                  this.sendSync();
               }

               this.processResults((ResultHandler)handler, flags, adaptiveFetch);
               this.estimatedReceiveBufferBytes = 0;
            }
         } catch (IOException var17) {
            this.abort();
            batchHandler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, var17));
         }

         try {
            ((ResultHandler)handler).handleCompletion();
            if (this.cleanupSavePoints) {
               this.releaseSavePoint(autosave, flags);
            }
         } catch (SQLException var16) {
            this.rollbackIfRequired(autosave, var16);
         }
      } catch (Throwable var18) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var15) {
               var18.addSuppressed(var15);
            }
         }

         throw var18;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   private ResultHandler sendQueryPreamble(ResultHandler delegateHandler, int flags) throws IOException {
      this.processDeadParsedQueries();
      this.processDeadPortals();
      if ((flags & 16) == 0 && this.getTransactionState() == TransactionState.IDLE) {
         int beginFlags = 2;
         if ((flags & 1) != 0) {
            beginFlags |= 1;
         }

         beginFlags |= 1024;
         beginFlags = this.updateQueryMode(beginFlags);
         SimpleQuery beginQuery = (flags & 2048) == 0 ? this.beginTransactionQuery : this.beginReadOnlyTransactionQuery;
         this.sendOneQuery(beginQuery, SimpleQuery.NO_PARAMETERS, 0, 0, beginFlags);
         return new ResultHandlerDelegate(delegateHandler) {
            private boolean sawBegin = false;

            public void handleResultRows(Query fromQuery, Field[] fields, List<Tuple> tuples, @Nullable ResultCursor cursor) {
               if (this.sawBegin) {
                  super.handleResultRows(fromQuery, fields, tuples, cursor);
               }

            }

            public void handleCommandStatus(String status, long updateCount, long insertOID) {
               if (!this.sawBegin) {
                  this.sawBegin = true;
                  if (!"BEGIN".equals(status)) {
                     this.handleError(new PSQLException(GT.tr("Expected command status BEGIN, got {0}.", status), PSQLState.PROTOCOL_VIOLATION));
                  }
               } else {
                  super.handleCommandStatus(status, updateCount, insertOID);
               }

            }
         };
      } else {
         return delegateHandler;
      }
   }

   @Nullable
   public byte[] fastpathCall(int fnid, ParameterList parameters, boolean suppressBegin) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      byte[] var5;
      try {
         this.waitOnLock();
         if (!suppressBegin) {
            this.doSubprotocolBegin();
         }

         try {
            this.sendFastpathCall(fnid, (SimpleParameterList)parameters);
            var5 = this.receiveFastpathResult();
         } catch (IOException var8) {
            this.abort();
            throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, var8);
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

      return var5;
   }

   public void doSubprotocolBegin() throws SQLException {
      if (this.getTransactionState() == TransactionState.IDLE) {
         LOGGER.log(Level.FINEST, "Issuing BEGIN before fastpath or copy call.");
         ResultHandlerBase handler = new ResultHandlerBase() {
            private boolean sawBegin = false;

            public void handleCommandStatus(String status, long updateCount, long insertOID) {
               if (!this.sawBegin) {
                  if (!"BEGIN".equals(status)) {
                     this.handleError(new PSQLException(GT.tr("Expected command status BEGIN, got {0}.", status), PSQLState.PROTOCOL_VIOLATION));
                  }

                  this.sawBegin = true;
               } else {
                  this.handleError(new PSQLException(GT.tr("Unexpected command status: {0}.", status), PSQLState.PROTOCOL_VIOLATION));
               }

            }

            public void handleWarning(SQLWarning warning) {
               this.handleError(warning);
            }
         };

         try {
            int beginFlags = 1027;
            int beginFlags = this.updateQueryMode(beginFlags);
            this.sendOneQuery(this.beginTransactionQuery, SimpleQuery.NO_PARAMETERS, 0, 0, beginFlags);
            this.sendSync();
            this.processResults(handler, 0);
            this.estimatedReceiveBufferBytes = 0;
         } catch (IOException var3) {
            throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, var3);
         }
      }

   }

   public ParameterList createFastpathParameters(int count) {
      return new SimpleParameterList(count, this);
   }

   private void sendFastpathCall(int fnid, SimpleParameterList params) throws SQLException, IOException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " FE=> FunctionCall({0}, {1} params)", new Object[]{fnid, params.getParameterCount()});
      }

      int paramCount = params.getParameterCount();
      int encodedSize = 0;

      int i;
      for(i = 1; i <= paramCount; ++i) {
         if (params.isNull(i)) {
            encodedSize += 4;
         } else {
            encodedSize += 4 + params.getV3Length(i);
         }
      }

      this.pgStream.sendChar(70);
      this.pgStream.sendInteger4(10 + 2 * paramCount + 2 + encodedSize + 2);
      this.pgStream.sendInteger4(fnid);
      this.pgStream.sendInteger2(paramCount);

      for(i = 1; i <= paramCount; ++i) {
         this.pgStream.sendInteger2(params.isBinary(i) ? 1 : 0);
      }

      this.pgStream.sendInteger2(paramCount);

      for(i = 1; i <= paramCount; ++i) {
         if (params.isNull(i)) {
            this.pgStream.sendInteger4(-1);
         } else {
            this.pgStream.sendInteger4(params.getV3Length(i));
            params.writeV3Value(i, this.pgStream);
         }
      }

      this.pgStream.sendInteger2(1);
      this.pgStream.flush();
   }

   public void processNotifies() throws SQLException {
      this.processNotifies(-1);
   }

   public void processNotifies(int timeoutMillis) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      label210: {
         try {
            this.waitOnLock();
            if (this.getTransactionState() != TransactionState.IDLE) {
               break label210;
            }

            if (this.hasNotifications()) {
               timeoutMillis = -1;
            }

            boolean useTimeout = timeoutMillis > 0;
            long startTime = 0L;
            int oldTimeout = 0;
            if (useTimeout) {
               startTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

               try {
                  oldTimeout = this.pgStream.getSocket().getSoTimeout();
               } catch (SocketException var19) {
                  throw new PSQLException(GT.tr("An error occurred while trying to get the socket timeout."), PSQLState.CONNECTION_FAILURE, var19);
               }
            }

            try {
               while(timeoutMillis >= 0 || this.pgStream.hasMessagePending()) {
                  if (useTimeout && timeoutMillis >= 0) {
                     this.setSocketTimeout(timeoutMillis);
                  }

                  int c = this.pgStream.receiveChar();
                  if (useTimeout && timeoutMillis >= 0) {
                     this.setSocketTimeout(0);
                  }

                  switch(c) {
                  case 65:
                     this.receiveAsyncNotify();
                     timeoutMillis = -1;
                     break;
                  case 69:
                     throw this.receiveErrorResponse();
                  case 78:
                     SQLWarning warning = this.receiveNoticeResponse();
                     this.addWarning(warning);
                     if (useTimeout) {
                        long newTimeMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                        timeoutMillis = (int)((long)timeoutMillis + (startTime - newTimeMillis));
                        startTime = newTimeMillis;
                        if (timeoutMillis == 0) {
                           timeoutMillis = -1;
                        }
                     }
                     break;
                  default:
                     throw new PSQLException(GT.tr("Unknown Response Type {0}.", (char)c), PSQLState.CONNECTION_FAILURE);
                  }
               }
            } catch (SocketTimeoutException var20) {
            } catch (IOException var21) {
               throw new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, var21);
            } finally {
               if (useTimeout) {
                  this.setSocketTimeout(oldTimeout);
               }

            }
         } catch (Throwable var23) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var18) {
                  var23.addSuppressed(var18);
               }
            }

            throw var23;
         }

         if (ignore != null) {
            ignore.close();
         }

         return;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   private void setSocketTimeout(int millis) throws PSQLException {
      try {
         Socket s = this.pgStream.getSocket();
         if (!s.isClosed()) {
            this.pgStream.setNetworkTimeout(millis);
         }

      } catch (IOException var3) {
         throw new PSQLException(GT.tr("An error occurred while trying to reset the socket timeout."), PSQLState.CONNECTION_FAILURE, var3);
      }
   }

   @Nullable
   private byte[] receiveFastpathResult() throws IOException, SQLException {
      boolean endQuery = false;
      SQLException error = null;
      byte[] returnValue = null;

      while(!endQuery) {
         int c = this.pgStream.receiveChar();
         switch(c) {
         case 65:
            this.receiveAsyncNotify();
            break;
         case 69:
            SQLException newError = this.receiveErrorResponse();
            if (error == null) {
               error = newError;
            } else {
               error.setNextException(newError);
            }
            break;
         case 78:
            SQLWarning warning = this.receiveNoticeResponse();
            this.addWarning(warning);
            break;
         case 83:
            try {
               this.receiveParameterStatus();
            } catch (SQLException var10) {
               if (error == null) {
                  error = var10;
               } else {
                  error.setNextException(var10);
               }

               endQuery = true;
            }
            break;
         case 86:
            int msgLen = this.pgStream.receiveInteger4();
            int valueLen = this.pgStream.receiveInteger4();
            LOGGER.log(Level.FINEST, " <=BE FunctionCallResponse({0} bytes)", valueLen);
            if (valueLen != -1) {
               byte[] buf = new byte[valueLen];
               this.pgStream.receive(buf, 0, valueLen);
               returnValue = buf;
            }
            break;
         case 90:
            this.receiveRFQ();
            endQuery = true;
            break;
         default:
            throw new PSQLException(GT.tr("Unknown Response Type {0}.", (char)c), PSQLState.CONNECTION_FAILURE);
         }
      }

      if (error != null) {
         throw error;
      } else {
         return returnValue;
      }
   }

   public CopyOperation startCopy(String sql, boolean suppressBegin) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      CopyOperation var5;
      try {
         this.waitOnLock();
         if (!suppressBegin) {
            this.doSubprotocolBegin();
         }

         byte[] buf = sql.getBytes(StandardCharsets.UTF_8);

         try {
            LOGGER.log(Level.FINEST, " FE=> Query(CopyStart)");
            this.pgStream.sendChar(81);
            this.pgStream.sendInteger4(buf.length + 4 + 1);
            this.pgStream.send(buf);
            this.pgStream.sendChar(0);
            this.pgStream.flush();
            var5 = (CopyOperation)Nullness.castNonNull(this.processCopyResults((CopyOperationImpl)null, true));
         } catch (IOException var7) {
            throw new PSQLException(GT.tr("Database connection failed when starting copy"), PSQLState.CONNECTION_FAILURE, var7);
         }
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var8.addSuppressed(var6);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var5;
   }

   private void initCopy(CopyOperationImpl op) throws SQLException, IOException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.pgStream.receiveInteger4();
         int rowFormat = this.pgStream.receiveChar();
         int numFields = this.pgStream.receiveInteger2();
         int[] fieldFormats = new int[numFields];
         int i = 0;

         while(true) {
            if (i >= numFields) {
               this.lock(op);
               op.init(this, rowFormat, fieldFormats);
               break;
            }

            fieldFormats[i] = this.pgStream.receiveInteger2();
            ++i;
         }
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void cancelCopy(CopyOperationImpl op) throws SQLException {
      if (!this.hasLock(op)) {
         throw new PSQLException(GT.tr("Tried to cancel an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
      } else {
         SQLException error = null;
         int errors = 0;
         boolean var22 = false;

         ResourceLock ignore;
         try {
            var22 = true;
            if (op instanceof CopyIn) {
               ignore = this.lock.obtain();

               try {
                  LOGGER.log(Level.FINEST, "FE => CopyFail");
                  byte[] msg = "Copy cancel requested".getBytes(StandardCharsets.US_ASCII);
                  this.pgStream.sendChar(102);
                  this.pgStream.sendInteger4(5 + msg.length);
                  this.pgStream.send(msg);
                  this.pgStream.sendChar(0);
                  this.pgStream.flush();

                  do {
                     try {
                        this.processCopyResults(op, true);
                     } catch (SQLException var28) {
                        ++errors;
                        if (error != null) {
                           SQLException e;
                           SQLException next;
                           for(e = var28; (next = e.getNextException()) != null; e = next) {
                           }

                           e.setNextException(error);
                        }

                        error = var28;
                     }
                  } while(this.hasLock(op));
               } catch (Throwable var29) {
                  if (ignore != null) {
                     try {
                        ignore.close();
                     } catch (Throwable var25) {
                        var29.addSuppressed(var25);
                     }
                  }

                  throw var29;
               }

               if (ignore != null) {
                  ignore.close();
                  var22 = false;
               } else {
                  var22 = false;
               }
            } else if (op instanceof CopyOut) {
               this.sendQueryCancel();
               var22 = false;
            } else {
               var22 = false;
            }
         } catch (IOException var30) {
            throw new PSQLException(GT.tr("Database connection failed when canceling copy operation"), PSQLState.CONNECTION_FAILURE, var30);
         } finally {
            if (var22) {
               ResourceLock ignore = this.lock.obtain();

               try {
                  if (this.hasLock(op)) {
                     this.unlock(op);
                  }
               } catch (Throwable var26) {
                  if (ignore != null) {
                     try {
                        ignore.close();
                     } catch (Throwable var24) {
                        var26.addSuppressed(var24);
                     }
                  }

                  throw var26;
               }

               if (ignore != null) {
                  ignore.close();
               }

            }
         }

         ignore = this.lock.obtain();

         try {
            if (this.hasLock(op)) {
               this.unlock(op);
            }
         } catch (Throwable var27) {
            if (ignore != null) {
               try {
                  ignore.close();
               } catch (Throwable var23) {
                  var27.addSuppressed(var23);
               }
            }

            throw var27;
         }

         if (ignore != null) {
            ignore.close();
         }

         if (op instanceof CopyIn) {
            if (errors < 1) {
               throw new PSQLException(GT.tr("Missing expected error response to copy cancel request"), PSQLState.COMMUNICATION_ERROR);
            }

            if (errors > 1) {
               throw new PSQLException(GT.tr("Got {0} error responses to single copy cancel request", String.valueOf(errors)), PSQLState.COMMUNICATION_ERROR, error);
            }
         }

      }
   }

   public long endCopy(CopyOperationImpl op) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      long var3;
      try {
         if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to end inactive copy"), PSQLState.OBJECT_NOT_IN_STATE);
         }

         try {
            LOGGER.log(Level.FINEST, " FE=> CopyDone");
            this.pgStream.sendChar(99);
            this.pgStream.sendInteger4(4);
            this.pgStream.flush();

            while(true) {
               this.processCopyResults(op, true);
               if (!this.hasLock(op)) {
                  var3 = op.getHandledRowCount();
                  break;
               }
            }
         } catch (IOException var6) {
            throw new PSQLException(GT.tr("Database connection failed when ending copy"), PSQLState.CONNECTION_FAILURE, var6);
         }
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var7.addSuppressed(var5);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

      return var3;
   }

   public void writeToCopy(CopyOperationImpl op, byte[] data, int off, int siz) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to write to an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
         }

         LOGGER.log(Level.FINEST, " FE=> CopyData({0})", siz);

         try {
            this.pgStream.sendChar(100);
            this.pgStream.sendInteger4(siz + 4);
            this.pgStream.send(data, off, siz);
         } catch (IOException var9) {
            throw new PSQLException(GT.tr("Database connection failed when writing to copy"), PSQLState.CONNECTION_FAILURE, var9);
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

   }

   public void writeToCopy(CopyOperationImpl op, ByteStreamWriter from) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to write to an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
         }

         int siz = from.getLength();
         LOGGER.log(Level.FINEST, " FE=> CopyData({0})", siz);

         try {
            this.pgStream.sendChar(100);
            this.pgStream.sendInteger4(siz + 4);
            this.pgStream.send(from);
         } catch (IOException var7) {
            throw new PSQLException(GT.tr("Database connection failed when writing to copy"), PSQLState.CONNECTION_FAILURE, var7);
         }
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var8.addSuppressed(var6);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public void flushCopy(CopyOperationImpl op) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to write to an inactive copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
         }

         try {
            this.pgStream.flush();
         } catch (IOException var6) {
            throw new PSQLException(GT.tr("Database connection failed when writing to copy"), PSQLState.CONNECTION_FAILURE, var6);
         }
      } catch (Throwable var7) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var5) {
               var7.addSuppressed(var5);
            }
         }

         throw var7;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   void readFromCopy(CopyOperationImpl op, boolean block) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (!this.hasLock(op)) {
            throw new PSQLException(GT.tr("Tried to read from inactive copy"), PSQLState.OBJECT_NOT_IN_STATE);
         }

         try {
            this.processCopyResults(op, block);
         } catch (IOException var7) {
            throw new PSQLException(GT.tr("Database connection failed when reading from copy"), PSQLState.CONNECTION_FAILURE, var7);
         }
      } catch (Throwable var8) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var6) {
               var8.addSuppressed(var6);
            }
         }

         throw var8;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   @Nullable
   CopyOperationImpl processCopyResults(@Nullable CopyOperationImpl op, boolean block) throws SQLException, IOException {
      if (this.pgStream.isClosed()) {
         throw new PSQLException(GT.tr("PGStream is closed"), PSQLState.CONNECTION_DOES_NOT_EXIST);
      } else if (!this.processingCopyResults.compareAndSet(false, true)) {
         LOGGER.log(Level.INFO, "Ignoring request to process copy results, already processing");
         return null;
      } else {
         Object var18;
         try {
            boolean endReceiving = false;
            SQLException error = null;
            Object errors = null;

            while(!endReceiving && (block || this.pgStream.hasMessagePending())) {
               int c;
               if (!block) {
                  c = this.pgStream.peekChar();
                  if (c == 67) {
                     LOGGER.log(Level.FINEST, " <=BE CommandStatus, Ignored until CopyDone");
                     break;
                  }
               }

               c = this.pgStream.receiveChar();
               int len;
               switch(c) {
               case 65:
                  LOGGER.log(Level.FINEST, " <=BE Asynchronous Notification while copying");
                  this.receiveAsyncNotify();
                  break;
               case 66:
               case 70:
               case 73:
               case 74:
               case 75:
               case 76:
               case 77:
               case 79:
               case 80:
               case 81:
               case 82:
               case 85:
               case 86:
               case 88:
               case 89:
               case 91:
               case 92:
               case 93:
               case 94:
               case 95:
               case 96:
               case 97:
               case 98:
               default:
                  throw new IOException(GT.tr("Unexpected packet type during copy: {0}", Integer.toString(c)));
               case 67:
                  String status = this.receiveCommandStatus();

                  try {
                     if (op == null) {
                        throw new PSQLException(GT.tr("Received CommandComplete ''{0}'' without an active copy operation", status), PSQLState.OBJECT_NOT_IN_STATE);
                     }

                     ((CopyOperationImpl)op).handleCommandStatus(status);
                  } catch (SQLException var16) {
                     error = var16;
                  }

                  block = true;
                  break;
               case 68:
                  LOGGER.log(Level.FINEST, " <=BE DataRow (during copy ignored)");
                  this.skipMessage();
                  break;
               case 69:
                  error = this.receiveErrorResponse();
                  block = true;
                  break;
               case 71:
                  LOGGER.log(Level.FINEST, " <=BE CopyInResponse");
                  if (op != null) {
                     error = new PSQLException(GT.tr("Got CopyInResponse from server during an active {0}", op.getClass().getName()), PSQLState.OBJECT_NOT_IN_STATE);
                  }

                  op = new CopyInImpl();
                  this.initCopy((CopyOperationImpl)op);
                  endReceiving = true;
                  break;
               case 72:
                  LOGGER.log(Level.FINEST, " <=BE CopyOutResponse");
                  if (op != null) {
                     error = new PSQLException(GT.tr("Got CopyOutResponse from server during an active {0}", op.getClass().getName()), PSQLState.OBJECT_NOT_IN_STATE);
                  }

                  op = new CopyOutImpl();
                  this.initCopy((CopyOperationImpl)op);
                  endReceiving = true;
                  break;
               case 78:
                  LOGGER.log(Level.FINEST, " <=BE Notification while copying");
                  this.addWarning(this.receiveNoticeResponse());
                  break;
               case 83:
                  try {
                     this.receiveParameterStatus();
                  } catch (SQLException var15) {
                     error = var15;
                     endReceiving = true;
                  }
                  break;
               case 84:
                  LOGGER.log(Level.FINEST, " <=BE RowDescription (during copy ignored)");
                  this.skipMessage();
                  break;
               case 87:
                  LOGGER.log(Level.FINEST, " <=BE CopyBothResponse");
                  if (op != null) {
                     error = new PSQLException(GT.tr("Got CopyBothResponse from server during an active {0}", op.getClass().getName()), PSQLState.OBJECT_NOT_IN_STATE);
                  }

                  op = new CopyDualImpl();
                  this.initCopy((CopyOperationImpl)op);
                  endReceiving = true;
                  break;
               case 90:
                  this.receiveRFQ();
                  if (op != null && this.hasLock(op)) {
                     this.unlock(op);
                  }

                  op = null;
                  endReceiving = true;
                  break;
               case 99:
                  LOGGER.log(Level.FINEST, " <=BE CopyDone");
                  len = this.pgStream.receiveInteger4() - 4;
                  if (len > 0) {
                     this.pgStream.receive(len);
                  }

                  if (!(op instanceof CopyOut)) {
                     error = new PSQLException("Got CopyDone while not copying from server", PSQLState.OBJECT_NOT_IN_STATE);
                  }

                  block = true;
                  break;
               case 100:
                  LOGGER.log(Level.FINEST, " <=BE CopyData");
                  len = this.pgStream.receiveInteger4() - 4;

                  assert len > 0 : "Copy Data length must be greater than 4";

                  byte[] buf = this.pgStream.receive(len);
                  if (op == null) {
                     error = new PSQLException(GT.tr("Got CopyData without an active copy operation"), PSQLState.OBJECT_NOT_IN_STATE);
                  } else if (!(op instanceof CopyOut)) {
                     error = new PSQLException(GT.tr("Unexpected copydata from server for {0}", op.getClass().getName()), PSQLState.COMMUNICATION_ERROR);
                  } else {
                     ((CopyOperationImpl)op).handleCopydata(buf);
                  }

                  endReceiving = true;
               }

               if (error != null) {
                  if (errors != null) {
                     ((SQLException)error).setNextException((SQLException)errors);
                  }

                  errors = error;
                  error = null;
               }
            }

            if (errors != null) {
               throw errors;
            }

            var18 = op;
         } finally {
            this.processingCopyResults.set(false);
         }

         return (CopyOperationImpl)var18;
      }
   }

   private void flushIfDeadlockRisk(Query query, boolean disallowBatching, ResultHandler resultHandler, @Nullable BatchResultHandler batchHandler, int flags) throws IOException {
      this.estimatedReceiveBufferBytes += 250;
      SimpleQuery sq = (SimpleQuery)query;
      if (sq.isStatementDescribed()) {
         int maxResultRowSize = sq.getMaxResultRowSize();
         if (maxResultRowSize >= 0) {
            this.estimatedReceiveBufferBytes += maxResultRowSize;
         } else {
            LOGGER.log(Level.FINEST, "Couldn't estimate result size or result size unbounded, disabling batching for this query.");
            disallowBatching = true;
         }
      }

      if (disallowBatching || this.estimatedReceiveBufferBytes >= 64000) {
         LOGGER.log(Level.FINEST, "Forcing Sync, receive buffer full or batching disallowed");
         this.sendSync();
         this.processResults(resultHandler, flags);
         this.estimatedReceiveBufferBytes = 0;
         if (batchHandler != null) {
            batchHandler.secureProgress();
         }
      }

   }

   private void sendQuery(Query query, V3ParameterList parameters, int maxRows, int fetchSize, int flags, ResultHandler resultHandler, @Nullable BatchResultHandler batchHandler, boolean adaptiveFetch) throws IOException, SQLException {
      Query[] subqueries = query.getSubqueries();
      SimpleParameterList[] subparams = parameters.getSubparams();
      boolean disallowBatching = (flags & 128) != 0;
      if (subqueries == null) {
         this.flushIfDeadlockRisk(query, disallowBatching, resultHandler, batchHandler, flags);
         if (resultHandler.getException() == null) {
            if (fetchSize != 0) {
               this.adaptiveFetchCache.addNewQuery(adaptiveFetch, query);
            }

            this.sendOneQuery((SimpleQuery)query, (SimpleParameterList)parameters, maxRows, fetchSize, flags);
         }
      } else {
         for(int i = 0; i < subqueries.length; ++i) {
            Query subquery = subqueries[i];
            this.flushIfDeadlockRisk(subquery, disallowBatching, resultHandler, batchHandler, flags);
            if (resultHandler.getException() != null) {
               break;
            }

            SimpleParameterList subparam = SimpleQuery.NO_PARAMETERS;
            if (subparams != null) {
               subparam = subparams[i];
            }

            if (fetchSize != 0) {
               this.adaptiveFetchCache.addNewQuery(adaptiveFetch, subquery);
            }

            this.sendOneQuery((SimpleQuery)subquery, subparam, maxRows, fetchSize, flags);
         }
      }

   }

   private void sendSync() throws IOException {
      LOGGER.log(Level.FINEST, " FE=> Sync");
      this.pgStream.sendChar(83);
      this.pgStream.sendInteger4(4);
      this.pgStream.flush();
      this.pendingExecuteQueue.add(new ExecuteRequest(this.sync, (Portal)null, true));
      this.pendingDescribePortalQueue.add(this.sync);
   }

   private void sendParse(SimpleQuery query, SimpleParameterList params, boolean oneShot) throws IOException {
      int[] typeOIDs = params.getTypeOIDs();
      if (!query.isPreparedFor(typeOIDs, this.deallocateEpoch)) {
         query.unprepare();
         this.processDeadParsedQueries();
         query.setFields((Field[])null);
         String statementName = null;
         if (!oneShot) {
            statementName = "S_" + this.nextUniqueID++;
            query.setStatementName(statementName, this.deallocateEpoch);
            query.setPrepareTypes(typeOIDs);
            this.registerParsedQuery(query, statementName);
         }

         byte[] encodedStatementName = query.getEncodedStatementName();
         String nativeSql = query.getNativeSql();
         int i;
         if (LOGGER.isLoggable(Level.FINEST)) {
            StringBuilder sbuf = new StringBuilder(" FE=> Parse(stmt=" + statementName + ",query=\"");
            sbuf.append(nativeSql);
            sbuf.append("\",oids={");

            for(i = 1; i <= params.getParameterCount(); ++i) {
               if (i != 1) {
                  sbuf.append(",");
               }

               sbuf.append(params.getTypeOID(i));
            }

            sbuf.append("})");
            LOGGER.log(Level.FINEST, sbuf.toString());
         }

         byte[] queryUtf8 = nativeSql.getBytes(StandardCharsets.UTF_8);
         i = 4 + (encodedStatementName == null ? 0 : encodedStatementName.length) + 1 + queryUtf8.length + 1 + 2 + 4 * params.getParameterCount();
         this.pgStream.sendChar(80);
         this.pgStream.sendInteger4(i);
         if (encodedStatementName != null) {
            this.pgStream.send(encodedStatementName);
         }

         this.pgStream.sendChar(0);
         this.pgStream.send(queryUtf8);
         this.pgStream.sendChar(0);
         this.pgStream.sendInteger2(params.getParameterCount());

         for(int i = 1; i <= params.getParameterCount(); ++i) {
            this.pgStream.sendInteger4(params.getTypeOID(i));
         }

         this.pendingParseQueue.add(query);
      }
   }

   private void sendBind(SimpleQuery query, SimpleParameterList params, @Nullable Portal portal, boolean noBinaryTransfer) throws IOException {
      String statementName = query.getStatementName();
      byte[] encodedStatementName = query.getEncodedStatementName();
      byte[] encodedPortalName = portal == null ? null : portal.getEncodedPortalName();
      if (LOGGER.isLoggable(Level.FINEST)) {
         StringBuilder sbuf = new StringBuilder(" FE=> Bind(stmt=" + statementName + ",portal=" + portal);

         for(int i = 1; i <= params.getParameterCount(); ++i) {
            sbuf.append(",$").append(i).append("=<").append(params.toString(i, true)).append(">,type=").append(Oid.toString(params.getTypeOID(i)));
         }

         sbuf.append(")");
         LOGGER.log(Level.FINEST, sbuf.toString());
      }

      long encodedSize = 0L;

      for(int i = 1; i <= params.getParameterCount(); ++i) {
         if (params.isNull(i)) {
            encodedSize += 4L;
         } else {
            encodedSize += 4L + (long)params.getV3Length(i);
         }
      }

      Field[] fields = query.getFields();
      Field[] var11;
      int i;
      int i;
      Field field;
      if (!noBinaryTransfer && query.needUpdateFieldFormats() && fields != null) {
         var11 = fields;
         i = fields.length;

         for(i = 0; i < i; ++i) {
            field = var11[i];
            if (this.useBinary(field)) {
               field.setFormat(1);
               query.setHasBinaryFields(true);
            }
         }
      }

      if (noBinaryTransfer && query.hasBinaryFields() && fields != null) {
         var11 = fields;
         i = fields.length;

         for(i = 0; i < i; ++i) {
            field = var11[i];
            if (field.getFormat() != 0) {
               field.setFormat(0);
            }
         }

         query.resetNeedUpdateFieldFormats();
         query.setHasBinaryFields(false);
      }

      int numBinaryFields = !noBinaryTransfer && query.hasBinaryFields() && fields != null ? fields.length : 0;
      encodedSize = (long)(4 + (encodedPortalName == null ? 0 : encodedPortalName.length) + 1 + (encodedStatementName == null ? 0 : encodedStatementName.length) + 1 + 2 + params.getParameterCount() * 2 + 2) + encodedSize + 2L + (long)(numBinaryFields * 2);
      if (encodedSize > 1073741823L) {
         throw new PGBindException(new IOException(GT.tr("Bind message length {0} too long.  This can be caused by very large or incorrect length specifications on InputStream parameters.", encodedSize)));
      } else {
         this.pgStream.sendChar(66);
         this.pgStream.sendInteger4((int)encodedSize);
         if (encodedPortalName != null) {
            this.pgStream.send(encodedPortalName);
         }

         this.pgStream.sendChar(0);
         if (encodedStatementName != null) {
            this.pgStream.send(encodedStatementName);
         }

         this.pgStream.sendChar(0);
         this.pgStream.sendInteger2(params.getParameterCount());

         for(i = 1; i <= params.getParameterCount(); ++i) {
            this.pgStream.sendInteger2(params.isBinary(i) ? 1 : 0);
         }

         this.pgStream.sendInteger2(params.getParameterCount());
         PGBindException bindException = null;

         for(i = 1; i <= params.getParameterCount(); ++i) {
            if (params.isNull(i)) {
               this.pgStream.sendInteger4(-1);
            } else {
               this.pgStream.sendInteger4(params.getV3Length(i));

               try {
                  params.writeV3Value(i, this.pgStream);
               } catch (PGBindException var15) {
                  bindException = var15;
               }
            }
         }

         this.pgStream.sendInteger2(numBinaryFields);

         for(i = 0; fields != null && i < numBinaryFields; ++i) {
            this.pgStream.sendInteger2(fields[i].getFormat());
         }

         this.pendingBindQueue.add(portal == null ? UNNAMED_PORTAL : portal);
         if (bindException != null) {
            throw bindException;
         }
      }
   }

   private boolean useBinary(Field field) {
      int oid = field.getOID();
      return this.useBinaryForReceive(oid);
   }

   private void sendDescribePortal(SimpleQuery query, @Nullable Portal portal) throws IOException {
      LOGGER.log(Level.FINEST, " FE=> Describe(portal={0})", portal);
      byte[] encodedPortalName = portal == null ? null : portal.getEncodedPortalName();
      int encodedSize = 5 + (encodedPortalName == null ? 0 : encodedPortalName.length) + 1;
      this.pgStream.sendChar(68);
      this.pgStream.sendInteger4(encodedSize);
      this.pgStream.sendChar(80);
      if (encodedPortalName != null) {
         this.pgStream.send(encodedPortalName);
      }

      this.pgStream.sendChar(0);
      this.pendingDescribePortalQueue.add(query);
      query.setPortalDescribed(true);
   }

   private void sendDescribeStatement(SimpleQuery query, SimpleParameterList params, boolean describeOnly) throws IOException {
      LOGGER.log(Level.FINEST, " FE=> Describe(statement={0})", query.getStatementName());
      byte[] encodedStatementName = query.getEncodedStatementName();
      int encodedSize = 5 + (encodedStatementName == null ? 0 : encodedStatementName.length) + 1;
      this.pgStream.sendChar(68);
      this.pgStream.sendInteger4(encodedSize);
      this.pgStream.sendChar(83);
      if (encodedStatementName != null) {
         this.pgStream.send(encodedStatementName);
      }

      this.pgStream.sendChar(0);
      this.pendingDescribeStatementQueue.add(new DescribeRequest(query, params, describeOnly, query.getStatementName()));
      this.pendingDescribePortalQueue.add(query);
      query.setStatementDescribed(true);
      query.setPortalDescribed(true);
   }

   private void sendExecute(SimpleQuery query, @Nullable Portal portal, int limit) throws IOException {
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " FE=> Execute(portal={0},limit={1})", new Object[]{portal, limit});
      }

      byte[] encodedPortalName = portal == null ? null : portal.getEncodedPortalName();
      int encodedSize = encodedPortalName == null ? 0 : encodedPortalName.length;
      this.pgStream.sendChar(69);
      this.pgStream.sendInteger4(5 + encodedSize + 4);
      if (encodedPortalName != null) {
         this.pgStream.send(encodedPortalName);
      }

      this.pgStream.sendChar(0);
      this.pgStream.sendInteger4(limit);
      this.pendingExecuteQueue.add(new ExecuteRequest(query, portal, false));
   }

   private void sendClosePortal(String portalName) throws IOException {
      LOGGER.log(Level.FINEST, " FE=> ClosePortal({0})", portalName);
      byte[] encodedPortalName = portalName == null ? null : portalName.getBytes(StandardCharsets.UTF_8);
      int encodedSize = encodedPortalName == null ? 0 : encodedPortalName.length;
      this.pgStream.sendChar(67);
      this.pgStream.sendInteger4(6 + encodedSize);
      this.pgStream.sendChar(80);
      if (encodedPortalName != null) {
         this.pgStream.send(encodedPortalName);
      }

      this.pgStream.sendChar(0);
   }

   private void sendCloseStatement(String statementName) throws IOException {
      LOGGER.log(Level.FINEST, " FE=> CloseStatement({0})", statementName);
      byte[] encodedStatementName = statementName.getBytes(StandardCharsets.UTF_8);
      this.pgStream.sendChar(67);
      this.pgStream.sendInteger4(5 + encodedStatementName.length + 1);
      this.pgStream.sendChar(83);
      this.pgStream.send(encodedStatementName);
      this.pgStream.sendChar(0);
   }

   private void sendOneQuery(SimpleQuery query, SimpleParameterList params, int maxRows, int fetchSize, int flags) throws IOException {
      boolean asSimple = (flags & 1024) != 0;
      if (asSimple) {
         assert (flags & 32) == 0 : "Simple mode does not support describe requests. sql = " + query.getNativeSql() + ", flags = " + flags;

         this.sendSimpleQuery(query, params);
      } else {
         assert !query.getNativeQuery().multiStatement : "Queries that might contain ; must be executed with QueryExecutor.QUERY_EXECUTE_AS_SIMPLE mode. Given query is " + query.getNativeSql();

         boolean noResults = (flags & 4) != 0;
         boolean noMeta = (flags & 2) != 0;
         boolean describeOnly = (flags & 32) != 0;
         boolean usePortal = (flags & 8) != 0 && !noResults && !noMeta && fetchSize > 0 && !describeOnly;
         boolean oneShot = (flags & 1) != 0;
         boolean noBinaryTransfer = (flags & 256) != 0;
         boolean forceDescribePortal = (flags & 512) != 0;
         int rows;
         if (noResults) {
            rows = 1;
         } else if (!usePortal) {
            rows = maxRows;
         } else if (maxRows != 0 && fetchSize > maxRows) {
            rows = maxRows;
         } else {
            rows = fetchSize;
         }

         this.sendParse(query, params, oneShot);
         boolean queryHasUnknown = query.hasUnresolvedTypes();
         boolean paramsHasUnknown = params.hasUnresolvedTypes();
         boolean describeStatement = describeOnly || !oneShot && paramsHasUnknown && queryHasUnknown && !query.isStatementDescribed();
         if (!describeStatement && paramsHasUnknown && !queryHasUnknown) {
            int[] queryOIDs = (int[])Nullness.castNonNull(query.getPrepareTypes());
            int[] paramOIDs = params.getTypeOIDs();

            for(int i = 0; i < paramOIDs.length; ++i) {
               if (paramOIDs[i] == 0) {
                  params.setResolvedType(i + 1, queryOIDs[i]);
               }
            }
         }

         if (describeStatement) {
            this.sendDescribeStatement(query, params, describeOnly);
            if (describeOnly) {
               return;
            }
         }

         Portal portal = null;
         if (usePortal) {
            String portalName = "C_" + this.nextUniqueID++;
            portal = new Portal(query, portalName);
         }

         this.sendBind(query, params, portal, noBinaryTransfer);
         if (!noMeta && !describeStatement && (!query.isPortalDescribed() || forceDescribePortal)) {
            this.sendDescribePortal(query, portal);
         }

         this.sendExecute(query, portal, rows);
      }
   }

   private void sendSimpleQuery(SimpleQuery query, SimpleParameterList params) throws IOException {
      String nativeSql = query.toString(params);
      LOGGER.log(Level.FINEST, " FE=> SimpleQuery(query=\"{0}\")", nativeSql);
      Encoding encoding = this.pgStream.getEncoding();
      byte[] encoded = encoding.encode(nativeSql);
      this.pgStream.sendChar(81);
      this.pgStream.sendInteger4(encoded.length + 4 + 1);
      this.pgStream.send(encoded);
      this.pgStream.sendChar(0);
      this.pgStream.flush();
      this.pendingExecuteQueue.add(new ExecuteRequest(query, (Portal)null, true));
      this.pendingDescribePortalQueue.add(query);
   }

   private void registerParsedQuery(SimpleQuery query, String statementName) {
      if (statementName != null) {
         PhantomReference<SimpleQuery> cleanupRef = new PhantomReference(query, this.parsedQueryCleanupQueue);
         this.parsedQueryMap.put(cleanupRef, statementName);
         query.setCleanupRef(cleanupRef);
      }
   }

   private void processDeadParsedQueries() throws IOException {
      Reference deadQuery;
      while((deadQuery = this.parsedQueryCleanupQueue.poll()) != null) {
         String statementName = (String)Nullness.castNonNull((String)this.parsedQueryMap.remove(deadQuery));
         this.sendCloseStatement(statementName);
         deadQuery.clear();
      }

   }

   private void registerOpenPortal(Portal portal) {
      if (portal != UNNAMED_PORTAL) {
         String portalName = portal.getPortalName();
         PhantomReference<Portal> cleanupRef = new PhantomReference(portal, this.openPortalCleanupQueue);
         this.openPortalMap.put(cleanupRef, portalName);
         portal.setCleanupRef(cleanupRef);
      }
   }

   private void processDeadPortals() throws IOException {
      Reference deadPortal;
      while((deadPortal = this.openPortalCleanupQueue.poll()) != null) {
         String portalName = (String)Nullness.castNonNull((String)this.openPortalMap.remove(deadPortal));
         this.sendClosePortal(portalName);
         deadPortal.clear();
      }

   }

   protected void processResults(ResultHandler handler, int flags) throws IOException {
      this.processResults(handler, flags, false);
   }

   protected void processResults(ResultHandler handler, int flags, boolean adaptiveFetch) throws IOException {
      boolean noResults = (flags & 4) != 0;
      boolean bothRowsAndStatus = (flags & 64) != 0;
      List<Tuple> tuples = null;
      boolean endQuery = false;
      boolean doneAfterRowDescNoData = false;

      while(!endQuery) {
         int c = this.pgStream.receiveChar();
         SimpleQuery currentQuery;
         Field[] fields;
         Portal currentPortal;
         DescribeRequest request;
         switch(c) {
         case 49:
            this.pgStream.receiveInteger4();
            SimpleQuery parsedQuery = (SimpleQuery)this.pendingParseQueue.removeFirst();
            String parsedStatementName = parsedQuery.getStatementName();
            LOGGER.log(Level.FINEST, " <=BE ParseComplete [{0}]", parsedStatementName);
            break;
         case 50:
            this.pgStream.receiveInteger4();
            Portal boundPortal = (Portal)this.pendingBindQueue.removeFirst();
            LOGGER.log(Level.FINEST, " <=BE BindComplete [{0}]", boundPortal);
            this.registerOpenPortal(boundPortal);
            break;
         case 51:
            this.pgStream.receiveInteger4();
            LOGGER.log(Level.FINEST, " <=BE CloseComplete");
            break;
         case 52:
         case 53:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 66:
         case 70:
         case 74:
         case 75:
         case 76:
         case 77:
         case 79:
         case 80:
         case 81:
         case 82:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 111:
         case 112:
         case 113:
         case 114:
         default:
            throw new IOException("Unexpected packet type: " + c);
         case 65:
            this.receiveAsyncNotify();
            break;
         case 67:
            String status = this.receiveCommandStatus();
            if (this.isFlushCacheOnDeallocate() && (status.startsWith("DEALLOCATE ALL") || status.startsWith("DISCARD ALL"))) {
               ++this.deallocateEpoch;
            }

            doneAfterRowDescNoData = false;
            ExecuteRequest executeData = (ExecuteRequest)Nullness.castNonNull((ExecuteRequest)this.pendingExecuteQueue.peekFirst());
            SimpleQuery currentQuery = executeData.query;
            currentPortal = executeData.portal;
            if (currentPortal != null) {
               this.adaptiveFetchCache.removeQuery(adaptiveFetch, currentQuery);
               this.adaptiveFetchCache.updateQueryFetchSize(adaptiveFetch, currentQuery, this.pgStream.getMaxRowSizeBytes());
            }

            this.pgStream.clearMaxRowSizeBytes();
            if (status.startsWith("SET")) {
               String nativeSql = currentQuery.getNativeQuery().nativeSql;
               if (nativeSql.lastIndexOf("search_path", 1024) != -1 && !nativeSql.equals(this.lastSetSearchPathQuery)) {
                  this.lastSetSearchPathQuery = nativeSql;
                  ++this.deallocateEpoch;
               }
            }

            if (!executeData.asSimple) {
               this.pendingExecuteQueue.removeFirst();
            }

            if (currentQuery != this.autoSaveQuery && currentQuery != this.releaseAutoSave) {
               Field[] fields = currentQuery.getFields();
               if (fields != null && tuples == null) {
                  tuples = noResults ? Collections.emptyList() : new ArrayList();
               }

               if (fields == null && tuples != null) {
                  throw new IllegalStateException("Received resultset tuples, but no field structure for them");
               }

               if (fields != null && tuples != null) {
                  handler.handleResultRows(currentQuery, fields, (List)tuples, (ResultCursor)null);
                  tuples = null;
                  if (bothRowsAndStatus) {
                     this.interpretCommandStatus(status, handler);
                  }
               } else {
                  this.interpretCommandStatus(status, handler);
               }

               if (executeData.asSimple) {
                  currentQuery.setFields((Field[])null);
               }

               if (currentPortal != null) {
                  currentPortal.close();
               }
            }
            break;
         case 68:
            Tuple tuple = null;

            try {
               tuple = this.pgStream.receiveTupleV3();
            } catch (OutOfMemoryError var21) {
               if (!noResults) {
                  handler.handleError(new PSQLException(GT.tr("Ran out of memory retrieving query results."), PSQLState.OUT_OF_MEMORY, var21));
               }
            } catch (SQLException var22) {
               handler.handleError(var22);
            }

            if (!noResults) {
               if (tuples == null) {
                  tuples = new ArrayList();
               }

               if (tuple != null) {
                  ((List)tuples).add(tuple);
               }
            }

            if (LOGGER.isLoggable(Level.FINEST)) {
               int length;
               if (tuple == null) {
                  length = -1;
               } else {
                  length = tuple.length();
               }

               LOGGER.log(Level.FINEST, " <=BE DataRow(len={0})", length);
            }
            break;
         case 69:
            SQLException error = this.receiveErrorResponse();
            handler.handleError(error);
            if (this.willHealViaReparse(error)) {
               ++this.deallocateEpoch;
               if (LOGGER.isLoggable(Level.FINEST)) {
                  LOGGER.log(Level.FINEST, " FE: received {0}, will invalidate statements. deallocateEpoch is now {1}", new Object[]{error.getSQLState(), this.deallocateEpoch});
               }
            }
            break;
         case 71:
            LOGGER.log(Level.FINEST, " <=BE CopyInResponse");
            LOGGER.log(Level.FINEST, " FE=> CopyFail");
            byte[] buf = "COPY commands are only supported using the CopyManager API.".getBytes(StandardCharsets.US_ASCII);
            this.pgStream.sendChar(102);
            this.pgStream.sendInteger4(buf.length + 4 + 1);
            this.pgStream.send(buf);
            this.pgStream.sendChar(0);
            this.pgStream.flush();
            this.sendSync();
            this.skipMessage();
            break;
         case 72:
            LOGGER.log(Level.FINEST, " <=BE CopyOutResponse");
            this.skipMessage();
            handler.handleError(new PSQLException(GT.tr("COPY commands are only supported using the CopyManager API."), PSQLState.NOT_IMPLEMENTED));
            break;
         case 73:
            this.pgStream.receiveInteger4();
            LOGGER.log(Level.FINEST, " <=BE EmptyQuery");
            ExecuteRequest executeData = (ExecuteRequest)this.pendingExecuteQueue.removeFirst();
            currentPortal = executeData.portal;
            handler.handleCommandStatus("EMPTY", 0L, 0L);
            if (currentPortal != null) {
               currentPortal.close();
            }
            break;
         case 78:
            SQLWarning warning = this.receiveNoticeResponse();
            handler.handleWarning(warning);
            break;
         case 83:
            try {
               this.receiveParameterStatus();
            } catch (SQLException var20) {
               handler.handleError(var20);
               endQuery = true;
            }
            break;
         case 84:
            fields = this.receiveFields();
            tuples = new ArrayList();
            SimpleQuery query = (SimpleQuery)Nullness.castNonNull((SimpleQuery)this.pendingDescribePortalQueue.peekFirst());
            if (!this.pendingExecuteQueue.isEmpty() && !((ExecuteRequest)Nullness.castNonNull((ExecuteRequest)this.pendingExecuteQueue.peekFirst())).asSimple) {
               this.pendingDescribePortalQueue.removeFirst();
            }

            query.setFields(fields);
            if (doneAfterRowDescNoData) {
               request = (DescribeRequest)this.pendingDescribeStatementQueue.removeFirst();
               SimpleQuery currentQuery = request.query;
               currentQuery.setFields(fields);
               handler.handleResultRows(currentQuery, fields, (List)tuples, (ResultCursor)null);
               tuples = null;
            }
            break;
         case 90:
            this.receiveRFQ();
            if (!this.pendingExecuteQueue.isEmpty() && ((ExecuteRequest)Nullness.castNonNull((ExecuteRequest)this.pendingExecuteQueue.peekFirst())).asSimple) {
               tuples = null;
               this.pgStream.clearResultBufferCount();
               ExecuteRequest executeRequest = (ExecuteRequest)this.pendingExecuteQueue.removeFirst();
               executeRequest.query.setFields((Field[])null);
               this.pendingDescribePortalQueue.removeFirst();
               if (!this.pendingExecuteQueue.isEmpty()) {
                  if (this.getTransactionState() == TransactionState.IDLE) {
                     handler.secureProgress();
                  }
                  break;
               }
            }

            endQuery = true;

            SimpleQuery describePortalQuery;
            while(!this.pendingParseQueue.isEmpty()) {
               describePortalQuery = (SimpleQuery)this.pendingParseQueue.removeFirst();
               describePortalQuery.unprepare();
            }

            this.pendingParseQueue.clear();

            while(!this.pendingDescribeStatementQueue.isEmpty()) {
               request = (DescribeRequest)this.pendingDescribeStatementQueue.removeFirst();
               LOGGER.log(Level.FINEST, " FE marking setStatementDescribed(false) for query {0}", request.query);
               request.query.setStatementDescribed(false);
            }

            while(!this.pendingDescribePortalQueue.isEmpty()) {
               describePortalQuery = (SimpleQuery)this.pendingDescribePortalQueue.removeFirst();
               LOGGER.log(Level.FINEST, " FE marking setPortalDescribed(false) for query {0}", describePortalQuery);
               describePortalQuery.setPortalDescribed(false);
            }

            this.pendingBindQueue.clear();
            this.pendingExecuteQueue.clear();
            break;
         case 99:
            this.skipMessage();
            LOGGER.log(Level.FINEST, " <=BE CopyDone");
            break;
         case 100:
            this.skipMessage();
            LOGGER.log(Level.FINEST, " <=BE CopyData");
            break;
         case 110:
            this.pgStream.receiveInteger4();
            LOGGER.log(Level.FINEST, " <=BE NoData");
            this.pendingDescribePortalQueue.removeFirst();
            if (doneAfterRowDescNoData) {
               DescribeRequest describeData = (DescribeRequest)this.pendingDescribeStatementQueue.removeFirst();
               currentQuery = describeData.query;
               Field[] fields = currentQuery.getFields();
               if (fields != null) {
                  List<Tuple> tuples = new ArrayList();
                  handler.handleResultRows(currentQuery, fields, tuples, (ResultCursor)null);
                  tuples = null;
               }
            }
            break;
         case 115:
            this.pgStream.receiveInteger4();
            LOGGER.log(Level.FINEST, " <=BE PortalSuspended");
            ExecuteRequest executeData = (ExecuteRequest)this.pendingExecuteQueue.removeFirst();
            currentQuery = executeData.query;
            Portal currentPortal = executeData.portal;
            if (currentPortal != null) {
               this.adaptiveFetchCache.updateQueryFetchSize(adaptiveFetch, currentQuery, this.pgStream.getMaxRowSizeBytes());
            }

            this.pgStream.clearMaxRowSizeBytes();
            fields = currentQuery.getFields();
            if (fields != null && tuples == null) {
               tuples = noResults ? Collections.emptyList() : new ArrayList();
            }

            if (fields != null && tuples != null) {
               handler.handleResultRows(currentQuery, fields, (List)tuples, currentPortal);
            }

            tuples = null;
            break;
         case 116:
            this.pgStream.receiveInteger4();
            LOGGER.log(Level.FINEST, " <=BE ParameterDescription");
            DescribeRequest describeData = (DescribeRequest)this.pendingDescribeStatementQueue.getFirst();
            SimpleQuery query = describeData.query;
            SimpleParameterList params = describeData.parameterList;
            boolean describeOnly = describeData.describeOnly;
            String origStatementName = describeData.statementName;
            int numParams = this.pgStream.receiveInteger2();

            for(int i = 1; i <= numParams; ++i) {
               int typeOid = this.pgStream.receiveInteger4();
               params.setResolvedType(i, typeOid);
            }

            if (origStatementName == null && query.getStatementName() == null || origStatementName != null && origStatementName.equals(query.getStatementName())) {
               query.setPrepareTypes(params.getTypeOIDs());
            }

            if (describeOnly) {
               doneAfterRowDescNoData = true;
            } else {
               this.pendingDescribeStatementQueue.removeFirst();
            }
         }
      }

   }

   private void skipMessage() throws IOException {
      int len = this.pgStream.receiveInteger4();

      assert len >= 4 : "Length from skip message must be at least 4 ";

      this.pgStream.skip(len - 4);
   }

   public void fetch(ResultCursor cursor, ResultHandler handler, int fetchSize, boolean adaptiveFetch) throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.waitOnLock();
         Portal portal = (Portal)cursor;
         final SimpleQuery query = (SimpleQuery)Nullness.castNonNull(portal.getQuery());
         ResultHandlerDelegate handler = new ResultHandlerDelegate(handler) {
            public void handleCommandStatus(String status, long updateCount, long insertOID) {
               this.handleResultRows(query, QueryExecutorImpl.NO_FIELDS, new ArrayList(), (ResultCursor)null);
            }
         };

         try {
            this.processDeadParsedQueries();
            this.processDeadPortals();
            this.sendExecute(query, portal, fetchSize);
            this.sendSync();
            this.processResults(handler, 0, adaptiveFetch);
            this.estimatedReceiveBufferBytes = 0;
         } catch (IOException var11) {
            this.abort();
            handler.handleError(new PSQLException(GT.tr("An I/O error occurred while sending to the backend."), PSQLState.CONNECTION_FAILURE, var11));
         }

         handler.handleCompletion();
      } catch (Throwable var12) {
         if (ignore != null) {
            try {
               ignore.close();
            } catch (Throwable var10) {
               var12.addSuppressed(var10);
            }
         }

         throw var12;
      }

      if (ignore != null) {
         ignore.close();
      }

   }

   public int getAdaptiveFetchSize(boolean adaptiveFetch, ResultCursor cursor) {
      if (cursor instanceof Portal) {
         Query query = ((Portal)cursor).getQuery();
         if (Objects.nonNull(query)) {
            return this.adaptiveFetchCache.getFetchSizeForQuery(adaptiveFetch, query);
         }
      }

      return -1;
   }

   public void setAdaptiveFetch(boolean adaptiveFetch) {
      this.adaptiveFetchCache.setAdaptiveFetch(adaptiveFetch);
   }

   public boolean getAdaptiveFetch() {
      return this.adaptiveFetchCache.getAdaptiveFetch();
   }

   public void addQueryToAdaptiveFetchCache(boolean adaptiveFetch, ResultCursor cursor) {
      if (cursor instanceof Portal) {
         Query query = ((Portal)cursor).getQuery();
         if (Objects.nonNull(query)) {
            this.adaptiveFetchCache.addNewQuery(adaptiveFetch, query);
         }
      }

   }

   public void removeQueryFromAdaptiveFetchCache(boolean adaptiveFetch, ResultCursor cursor) {
      if (cursor instanceof Portal) {
         Query query = ((Portal)cursor).getQuery();
         if (Objects.nonNull(query)) {
            this.adaptiveFetchCache.removeQuery(adaptiveFetch, query);
         }
      }

   }

   private Field[] receiveFields() throws IOException {
      this.pgStream.receiveInteger4();
      int size = this.pgStream.receiveInteger2();
      Field[] fields = new Field[size];
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " <=BE RowDescription({0})", size);
      }

      for(int i = 0; i < fields.length; ++i) {
         String columnLabel = this.pgStream.receiveCanonicalString();
         int tableOid = this.pgStream.receiveInteger4();
         short positionInTable = (short)this.pgStream.receiveInteger2();
         int typeOid = this.pgStream.receiveInteger4();
         int typeLength = this.pgStream.receiveInteger2();
         int typeModifier = this.pgStream.receiveInteger4();
         int formatType = this.pgStream.receiveInteger2();
         fields[i] = new Field(columnLabel, typeOid, typeLength, typeModifier, tableOid, positionInTable);
         fields[i].setFormat(formatType);
         LOGGER.log(Level.FINEST, "        {0}", fields[i]);
      }

      return fields;
   }

   private void receiveAsyncNotify() throws IOException {
      int len = this.pgStream.receiveInteger4();

      assert len > 4 : "Length for AsyncNotify must be at least 4";

      int pid = this.pgStream.receiveInteger4();
      String msg = this.pgStream.receiveCanonicalString();
      String param = this.pgStream.receiveString();
      this.addNotification(new Notification(msg, pid, param));
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " <=BE AsyncNotify({0},{1},{2})", new Object[]{pid, msg, param});
      }

   }

   private SQLException receiveErrorResponse() throws IOException {
      int elen = this.pgStream.receiveInteger4();

      assert elen > 4 : "Error response length must be greater than 4";

      EncodingPredictor.DecodeResult totalMessage = this.pgStream.receiveErrorString(elen - 4);
      ServerErrorMessage errorMsg = new ServerErrorMessage(totalMessage);
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " <=BE ErrorMessage({0})", errorMsg.toString());
      }

      PSQLException error = new PSQLException(errorMsg, this.logServerErrorDetail);
      if (this.transactionFailCause == null) {
         this.transactionFailCause = error;
      } else {
         error.initCause(this.transactionFailCause);
      }

      return error;
   }

   private SQLWarning receiveNoticeResponse() throws IOException {
      int nlen = this.pgStream.receiveInteger4();

      assert nlen > 4 : "Notice Response length must be greater than 4";

      ServerErrorMessage warnMsg = new ServerErrorMessage(this.pgStream.receiveString(nlen - 4));
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " <=BE NoticeResponse({0})", warnMsg.toString());
      }

      return new PSQLWarning(warnMsg);
   }

   private String receiveCommandStatus() throws IOException {
      int len = this.pgStream.receiveInteger4();
      String status = this.pgStream.receiveString(len - 5);
      this.pgStream.receiveChar();
      LOGGER.log(Level.FINEST, " <=BE CommandStatus({0})", status);
      return status;
   }

   private void interpretCommandStatus(String status, ResultHandler handler) {
      try {
         this.commandCompleteParser.parse(status);
      } catch (SQLException var7) {
         handler.handleError(var7);
         return;
      }

      long oid = this.commandCompleteParser.getOid();
      long count = this.commandCompleteParser.getRows();
      handler.handleCommandStatus(status, count, oid);
   }

   private void receiveRFQ() throws IOException {
      if (this.pgStream.receiveInteger4() != 5) {
         throw new IOException("unexpected length of ReadyForQuery message");
      } else {
         char tStatus = (char)this.pgStream.receiveChar();
         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, " <=BE ReadyForQuery({0})", tStatus);
         }

         switch(tStatus) {
         case 'E':
            this.setTransactionState(TransactionState.FAILED);
            break;
         case 'I':
            this.transactionFailCause = null;
            this.setTransactionState(TransactionState.IDLE);
            break;
         case 'T':
            this.transactionFailCause = null;
            this.setTransactionState(TransactionState.OPEN);
            break;
         default:
            throw new IOException("unexpected transaction state in ReadyForQuery message: " + tStatus);
         }

      }
   }

   protected void sendCloseMessage() throws IOException {
      this.closeAction.sendCloseMessage(this.pgStream);
   }

   public void readStartupMessages() throws IOException, SQLException {
      for(int i = 0; i < 1000; ++i) {
         int beresp = this.pgStream.receiveChar();
         switch(beresp) {
         case 69:
            throw this.receiveErrorResponse();
         case 75:
            int msgLen = this.pgStream.receiveInteger4();
            if (msgLen != 12) {
               throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
            }

            int pid = this.pgStream.receiveInteger4();
            int ckey = this.pgStream.receiveInteger4();
            if (LOGGER.isLoggable(Level.FINEST)) {
               LOGGER.log(Level.FINEST, " <=BE BackendKeyData(pid={0},ckey={1})", new Object[]{pid, ckey});
            }

            this.setBackendKeyData(pid, ckey);
            break;
         case 78:
            this.addWarning(this.receiveNoticeResponse());
            break;
         case 83:
            this.receiveParameterStatus();
            break;
         case 90:
            this.receiveRFQ();
            return;
         default:
            if (LOGGER.isLoggable(Level.FINEST)) {
               LOGGER.log(Level.FINEST, "  invalid message type={0}", (char)beresp);
            }

            throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
         }
      }

      throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
   }

   public void receiveParameterStatus() throws IOException, SQLException {
      this.pgStream.receiveInteger4();
      String name = this.pgStream.receiveCanonicalStringIfPresent();
      String value = this.pgStream.receiveCanonicalStringIfPresent();
      if (LOGGER.isLoggable(Level.FINEST)) {
         LOGGER.log(Level.FINEST, " <=BE ParameterStatus({0} = {1})", new Object[]{name, value});
      }

      if (!name.isEmpty()) {
         this.onParameterStatus(name, value);
         if ("client_encoding".equals(name)) {
            if (this.allowEncodingChanges) {
               if (!"UTF8".equalsIgnoreCase(value) && !"UTF-8".equalsIgnoreCase(value)) {
                  LOGGER.log(Level.FINE, "pgjdbc expects client_encoding to be UTF8 for proper operation. Actual encoding is {0}", value);
               }

               this.pgStream.setEncoding(Encoding.getDatabaseEncoding(value));
            } else if (!"UTF8".equalsIgnoreCase(value) && !"UTF-8".equalsIgnoreCase(value)) {
               this.close();
               throw new PSQLException(GT.tr("The server''s client_encoding parameter was changed to {0}. The JDBC driver requires client_encoding to be UTF8 for correct operation.", value), PSQLState.CONNECTION_FAILURE);
            }
         }

         if ("DateStyle".equals(name) && !value.startsWith("ISO") && !value.toUpperCase(Locale.ROOT).startsWith("ISO")) {
            this.close();
            throw new PSQLException(GT.tr("The server''s DateStyle parameter was changed to {0}. The JDBC driver requires DateStyle to begin with ISO for correct operation.", value), PSQLState.CONNECTION_FAILURE);
         } else if ("standard_conforming_strings".equals(name)) {
            if ("on".equals(value)) {
               this.setStandardConformingStrings(true);
            } else {
               if (!"off".equals(value)) {
                  this.close();
                  throw new PSQLException(GT.tr("The server''s standard_conforming_strings parameter was reported as {0}. The JDBC driver expected on or off.", value), PSQLState.CONNECTION_FAILURE);
               }

               this.setStandardConformingStrings(false);
            }

         } else {
            if ("TimeZone".equals(name)) {
               this.setTimeZone(TimestampUtils.parseBackendTimeZone(value));
            } else if ("application_name".equals(name)) {
               this.setApplicationName(value);
            } else if ("server_version_num".equals(name)) {
               this.setServerVersionNum(Integer.parseInt(value));
            } else if ("server_version".equals(name)) {
               this.setServerVersion(value);
            } else if ("integer_datetimes".equals(name)) {
               if ("on".equals(value)) {
                  this.setIntegerDateTimes(true);
               } else {
                  if (!"off".equals(value)) {
                     throw new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.PROTOCOL_VIOLATION);
                  }

                  this.setIntegerDateTimes(false);
               }
            }

         }
      }
   }

   public void setTimeZone(TimeZone timeZone) {
      this.timeZone = timeZone;
   }

   @Nullable
   public TimeZone getTimeZone() {
      return this.timeZone;
   }

   public void setApplicationName(String applicationName) {
      this.applicationName = applicationName;
   }

   public String getApplicationName() {
      return this.applicationName == null ? "" : this.applicationName;
   }

   public ReplicationProtocol getReplicationProtocol() {
      return this.replicationProtocol;
   }

   public void addBinaryReceiveOid(int oid) {
      synchronized(this.useBinaryReceiveForOids) {
         this.useBinaryReceiveForOids.add(oid);
      }
   }

   public void removeBinaryReceiveOid(int oid) {
      synchronized(this.useBinaryReceiveForOids) {
         this.useBinaryReceiveForOids.remove(oid);
      }
   }

   public Set<? extends Integer> getBinaryReceiveOids() {
      synchronized(this.useBinaryReceiveForOids) {
         return new HashSet(this.useBinaryReceiveForOids);
      }
   }

   public boolean useBinaryForReceive(int oid) {
      synchronized(this.useBinaryReceiveForOids) {
         return this.useBinaryReceiveForOids.contains(oid);
      }
   }

   public void setBinaryReceiveOids(Set<Integer> oids) {
      synchronized(this.useBinaryReceiveForOids) {
         this.useBinaryReceiveForOids.clear();
         this.useBinaryReceiveForOids.addAll(oids);
      }
   }

   public void addBinarySendOid(int oid) {
      synchronized(this.useBinarySendForOids) {
         this.useBinarySendForOids.add(oid);
      }
   }

   public void removeBinarySendOid(int oid) {
      synchronized(this.useBinarySendForOids) {
         this.useBinarySendForOids.remove(oid);
      }
   }

   public Set<? extends Integer> getBinarySendOids() {
      synchronized(this.useBinarySendForOids) {
         return new HashSet(this.useBinarySendForOids);
      }
   }

   public boolean useBinaryForSend(int oid) {
      synchronized(this.useBinarySendForOids) {
         return this.useBinarySendForOids.contains(oid);
      }
   }

   public void setBinarySendOids(Set<Integer> oids) {
      synchronized(this.useBinarySendForOids) {
         this.useBinarySendForOids.clear();
         this.useBinarySendForOids.addAll(oids);
      }
   }

   private void setIntegerDateTimes(boolean state) {
      this.integerDateTimes = state;
   }

   public boolean getIntegerDateTimes() {
      return this.integerDateTimes;
   }

   static {
      Encoding.canonicalize("application_name");
      Encoding.canonicalize("client_encoding");
      Encoding.canonicalize("DateStyle");
      Encoding.canonicalize("integer_datetimes");
      Encoding.canonicalize("off");
      Encoding.canonicalize("on");
      Encoding.canonicalize("server_encoding");
      Encoding.canonicalize("server_version");
      Encoding.canonicalize("server_version_num");
      Encoding.canonicalize("standard_conforming_strings");
      Encoding.canonicalize("TimeZone");
      Encoding.canonicalize("UTF8");
      Encoding.canonicalize("UTF-8");
      Encoding.canonicalize("in_hot_standby");
      UNNAMED_PORTAL = new Portal((SimpleQuery)null, "unnamed");
   }
}
