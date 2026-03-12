package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.PGNotification;
import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.jdbc.AutoSave;
import fr.xephi.authme.libs.org.postgresql.jdbc.EscapeSyntaxCallMode;
import fr.xephi.authme.libs.org.postgresql.jdbc.PreferQueryMode;
import fr.xephi.authme.libs.org.postgresql.jdbc.ResourceLock;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import fr.xephi.authme.libs.org.postgresql.util.LruCache;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.ServerErrorMessage;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class QueryExecutorBase implements QueryExecutor {
   private static final Logger LOGGER = Logger.getLogger(QueryExecutorBase.class.getName());
   protected final PGStream pgStream;
   private final String user;
   private final String database;
   private final int cancelSignalTimeout;
   private int cancelPid;
   private int cancelKey;
   protected final QueryExecutorCloseAction closeAction;
   @MonotonicNonNull
   private String serverVersion;
   private int serverVersionNum;
   private TransactionState transactionState;
   private final boolean reWriteBatchedInserts;
   private final boolean columnSanitiserDisabled;
   private final EscapeSyntaxCallMode escapeSyntaxCallMode;
   private final boolean quoteReturningIdentifiers;
   private PreferQueryMode preferQueryMode;
   private AutoSave autoSave;
   private boolean flushCacheOnDeallocate;
   protected final boolean logServerErrorDetail;
   private boolean standardConformingStrings;
   @Nullable
   private SQLWarning warnings;
   private final ArrayList<PGNotification> notifications;
   private final LruCache<Object, CachedQuery> statementCache;
   private final CachedQueryCreateAction cachedQueryCreateAction;
   private final TreeMap<String, String> parameterStatuses;
   protected final ResourceLock lock;
   protected final Condition lockCondition;

   protected QueryExecutorBase(PGStream pgStream, int cancelSignalTimeout, Properties info) throws SQLException {
      this.transactionState = TransactionState.IDLE;
      this.flushCacheOnDeallocate = true;
      this.notifications = new ArrayList();
      this.parameterStatuses = new TreeMap(String.CASE_INSENSITIVE_ORDER);
      this.lock = new ResourceLock();
      this.lockCondition = this.lock.newCondition();
      this.pgStream = pgStream;
      this.user = PGProperty.USER.getOrDefault(info);
      this.database = PGProperty.PG_DBNAME.getOrDefault(info);
      this.cancelSignalTimeout = cancelSignalTimeout;
      this.reWriteBatchedInserts = PGProperty.REWRITE_BATCHED_INSERTS.getBoolean(info);
      this.columnSanitiserDisabled = PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(info);
      String callMode = PGProperty.ESCAPE_SYNTAX_CALL_MODE.getOrDefault(info);
      this.escapeSyntaxCallMode = EscapeSyntaxCallMode.of(callMode);
      this.quoteReturningIdentifiers = PGProperty.QUOTE_RETURNING_IDENTIFIERS.getBoolean(info);
      String preferMode = PGProperty.PREFER_QUERY_MODE.getOrDefault(info);
      this.preferQueryMode = PreferQueryMode.of(preferMode);
      this.autoSave = AutoSave.of(PGProperty.AUTOSAVE.getOrDefault(info));
      this.logServerErrorDetail = PGProperty.LOG_SERVER_ERROR_DETAIL.getBoolean(info);
      this.cachedQueryCreateAction = new CachedQueryCreateAction(this);
      this.statementCache = new LruCache(Math.max(0, PGProperty.PREPARED_STATEMENT_CACHE_QUERIES.getInt(info)), Math.max(0L, (long)PGProperty.PREPARED_STATEMENT_CACHE_SIZE_MIB.getInt(info) * 1024L * 1024L), false, this.cachedQueryCreateAction, new LruCache.EvictAction<CachedQuery>() {
         public void evict(CachedQuery cachedQuery) throws SQLException {
            cachedQuery.query.close();
         }
      });
      this.closeAction = this.createCloseAction();
   }

   protected QueryExecutorCloseAction createCloseAction() {
      return new QueryExecutorCloseAction(this.pgStream);
   }

   /** @deprecated */
   @Deprecated
   protected abstract void sendCloseMessage() throws IOException;

   public void setNetworkTimeout(int milliseconds) throws IOException {
      this.pgStream.setNetworkTimeout(milliseconds);
   }

   public int getNetworkTimeout() throws IOException {
      return this.pgStream.getNetworkTimeout();
   }

   public HostSpec getHostSpec() {
      return this.pgStream.getHostSpec();
   }

   public String getUser() {
      return this.user;
   }

   public String getDatabase() {
      return this.database;
   }

   public void setBackendKeyData(int cancelPid, int cancelKey) {
      this.cancelPid = cancelPid;
      this.cancelKey = cancelKey;
   }

   public int getBackendPID() {
      return this.cancelPid;
   }

   public void abort() {
      this.closeAction.abort();
   }

   public Closeable getCloseAction() {
      return this.closeAction;
   }

   public void close() {
      if (!this.closeAction.isClosed()) {
         try {
            this.getCloseAction().close();
         } catch (IOException var2) {
            LOGGER.log(Level.FINEST, "Discarding IOException on close:", var2);
         }

      }
   }

   public boolean isClosed() {
      return this.closeAction.isClosed();
   }

   public void sendQueryCancel() throws SQLException {
      PGStream cancelStream = null;

      try {
         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, " FE=> CancelRequest(pid={0},ckey={1})", new Object[]{this.cancelPid, this.cancelKey});
         }

         cancelStream = new PGStream(this.pgStream.getSocketFactory(), this.pgStream.getHostSpec(), this.cancelSignalTimeout);
         if (this.cancelSignalTimeout > 0) {
            cancelStream.setNetworkTimeout(this.cancelSignalTimeout);
         }

         cancelStream.sendInteger4(16);
         cancelStream.sendInteger2(1234);
         cancelStream.sendInteger2(5678);
         cancelStream.sendInteger4(this.cancelPid);
         cancelStream.sendInteger4(this.cancelKey);
         cancelStream.flush();
         cancelStream.receiveEOF();
      } catch (IOException var11) {
         LOGGER.log(Level.FINEST, "Ignoring exception on cancel request:", var11);
      } finally {
         if (cancelStream != null) {
            try {
               cancelStream.close();
            } catch (IOException var10) {
            }
         }

      }

   }

   public void addWarning(SQLWarning newWarning) {
      ResourceLock ignore = this.lock.obtain();

      try {
         if (this.warnings == null) {
            this.warnings = newWarning;
         } else {
            this.warnings.setNextWarning(newWarning);
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

   public void addNotification(PGNotification notification) {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.notifications.add(notification);
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

   public PGNotification[] getNotifications() throws SQLException {
      ResourceLock ignore = this.lock.obtain();

      PGNotification[] var3;
      try {
         PGNotification[] array = (PGNotification[])this.notifications.toArray(new PGNotification[0]);
         this.notifications.clear();
         var3 = array;
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

   @Nullable
   public SQLWarning getWarnings() {
      ResourceLock ignore = this.lock.obtain();

      SQLWarning var3;
      try {
         SQLWarning chain = this.warnings;
         this.warnings = null;
         var3 = chain;
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

   public String getServerVersion() {
      String serverVersion = this.serverVersion;
      if (serverVersion == null) {
         throw new IllegalStateException("serverVersion must not be null");
      } else {
         return serverVersion;
      }
   }

   public int getServerVersionNum() {
      return this.serverVersionNum != 0 ? this.serverVersionNum : (this.serverVersionNum = Utils.parseServerVersionStr(this.getServerVersion()));
   }

   public void setServerVersion(String serverVersion) {
      this.serverVersion = serverVersion;
   }

   public void setServerVersionNum(int serverVersionNum) {
      this.serverVersionNum = serverVersionNum;
   }

   public void setTransactionState(TransactionState state) {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.transactionState = state;
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

   public void setStandardConformingStrings(boolean value) {
      ResourceLock ignore = this.lock.obtain();

      try {
         this.standardConformingStrings = value;
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

   public boolean getStandardConformingStrings() {
      ResourceLock ignore = this.lock.obtain();

      boolean var2;
      try {
         var2 = this.standardConformingStrings;
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

   public boolean getQuoteReturningIdentifiers() {
      return this.quoteReturningIdentifiers;
   }

   public TransactionState getTransactionState() {
      ResourceLock ignore = this.lock.obtain();

      TransactionState var2;
      try {
         var2 = this.transactionState;
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

   public void setEncoding(Encoding encoding) throws IOException {
      this.pgStream.setEncoding(encoding);
   }

   public Encoding getEncoding() {
      return this.pgStream.getEncoding();
   }

   public boolean isReWriteBatchedInsertsEnabled() {
      return this.reWriteBatchedInserts;
   }

   public final CachedQuery borrowQuery(String sql) throws SQLException {
      return (CachedQuery)this.statementCache.borrow(sql);
   }

   public final CachedQuery borrowCallableQuery(String sql) throws SQLException {
      return (CachedQuery)this.statementCache.borrow(new CallableQueryKey(sql));
   }

   public final CachedQuery borrowReturningQuery(String sql, @Nullable String[] columnNames) throws SQLException {
      return (CachedQuery)this.statementCache.borrow(new QueryWithReturningColumnsKey(sql, true, true, columnNames));
   }

   public CachedQuery borrowQueryByKey(Object key) throws SQLException {
      return (CachedQuery)this.statementCache.borrow(key);
   }

   public void releaseQuery(CachedQuery cachedQuery) {
      this.statementCache.put(cachedQuery.key, cachedQuery);
   }

   public final Object createQueryKey(String sql, boolean escapeProcessing, boolean isParameterized, @Nullable String... columnNames) {
      Object key;
      if (columnNames != null && columnNames.length == 0) {
         if (isParameterized) {
            key = sql;
         } else {
            key = new BaseQueryKey(sql, false, escapeProcessing);
         }
      } else {
         key = new QueryWithReturningColumnsKey(sql, isParameterized, escapeProcessing, columnNames);
      }

      return key;
   }

   public CachedQuery createQueryByKey(Object key) throws SQLException {
      return this.cachedQueryCreateAction.create(key);
   }

   public final CachedQuery createQuery(String sql, boolean escapeProcessing, boolean isParameterized, @Nullable String... columnNames) throws SQLException {
      Object key = this.createQueryKey(sql, escapeProcessing, isParameterized, columnNames);
      return this.createQueryByKey(key);
   }

   public boolean isColumnSanitiserDisabled() {
      return this.columnSanitiserDisabled;
   }

   public EscapeSyntaxCallMode getEscapeSyntaxCallMode() {
      return this.escapeSyntaxCallMode;
   }

   public PreferQueryMode getPreferQueryMode() {
      return this.preferQueryMode;
   }

   public void setPreferQueryMode(PreferQueryMode mode) {
      this.preferQueryMode = mode;
   }

   public AutoSave getAutoSave() {
      return this.autoSave;
   }

   public void setAutoSave(AutoSave autoSave) {
      this.autoSave = autoSave;
   }

   protected boolean willHealViaReparse(SQLException e) {
      if (e != null && e.getSQLState() != null) {
         if (PSQLState.INVALID_SQL_STATEMENT_NAME.getState().equals(e.getSQLState())) {
            return true;
         } else if (!PSQLState.NOT_IMPLEMENTED.getState().equals(e.getSQLState())) {
            return false;
         } else if (!(e instanceof PSQLException)) {
            return false;
         } else {
            PSQLException pe = (PSQLException)e;
            ServerErrorMessage serverErrorMessage = pe.getServerErrorMessage();
            if (serverErrorMessage == null) {
               return false;
            } else {
               String routine = serverErrorMessage.getRoutine();
               return "RevalidateCachedQuery".equals(routine) || "RevalidateCachedPlan".equals(routine);
            }
         }
      } else {
         return false;
      }
   }

   public boolean willHealOnRetry(SQLException e) {
      return this.autoSave == AutoSave.NEVER && this.getTransactionState() == TransactionState.FAILED ? false : this.willHealViaReparse(e);
   }

   public boolean isFlushCacheOnDeallocate() {
      return this.flushCacheOnDeallocate;
   }

   public void setFlushCacheOnDeallocate(boolean flushCacheOnDeallocate) {
      this.flushCacheOnDeallocate = flushCacheOnDeallocate;
   }

   protected boolean hasNotifications() {
      return !this.notifications.isEmpty();
   }

   public final Map<String, String> getParameterStatuses() {
      return Collections.unmodifiableMap(this.parameterStatuses);
   }

   @Nullable
   public final String getParameterStatus(String parameterName) {
      return (String)this.parameterStatuses.get(parameterName);
   }

   protected void onParameterStatus(String parameterName, String parameterStatus) {
      if (parameterName != null && !"".equals(parameterName)) {
         this.parameterStatuses.put(parameterName, parameterStatus);
      } else {
         throw new IllegalStateException("attempt to set GUC_REPORT parameter with null or empty-string name");
      }
   }
}
