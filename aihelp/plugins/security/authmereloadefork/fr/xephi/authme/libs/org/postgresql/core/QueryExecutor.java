package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.PGNotification;
import fr.xephi.authme.libs.org.postgresql.copy.CopyOperation;
import fr.xephi.authme.libs.org.postgresql.core.v3.TypeTransferModeRegistry;
import fr.xephi.authme.libs.org.postgresql.jdbc.AutoSave;
import fr.xephi.authme.libs.org.postgresql.jdbc.BatchResultHandler;
import fr.xephi.authme.libs.org.postgresql.jdbc.EscapeSyntaxCallMode;
import fr.xephi.authme.libs.org.postgresql.jdbc.PreferQueryMode;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface QueryExecutor extends TypeTransferModeRegistry {
   int QUERY_ONESHOT = 1;
   int QUERY_NO_METADATA = 2;
   int QUERY_NO_RESULTS = 4;
   int QUERY_FORWARD_CURSOR = 8;
   int QUERY_SUPPRESS_BEGIN = 16;
   int QUERY_DESCRIBE_ONLY = 32;
   int QUERY_BOTH_ROWS_AND_STATUS = 64;
   int QUERY_FORCE_DESCRIBE_PORTAL = 512;
   /** @deprecated */
   @Deprecated
   int QUERY_DISALLOW_BATCHING = 128;
   int QUERY_NO_BINARY_TRANSFER = 256;
   int QUERY_EXECUTE_AS_SIMPLE = 1024;
   int MAX_SAVE_POINTS = 1000;
   int QUERY_READ_ONLY_HINT = 2048;

   void execute(Query var1, @Nullable ParameterList var2, ResultHandler var3, int var4, int var5, int var6) throws SQLException;

   void execute(Query var1, @Nullable ParameterList var2, ResultHandler var3, int var4, int var5, int var6, boolean var7) throws SQLException;

   void execute(Query[] var1, ParameterList[] var2, BatchResultHandler var3, int var4, int var5, int var6) throws SQLException;

   void execute(Query[] var1, ParameterList[] var2, BatchResultHandler var3, int var4, int var5, int var6, boolean var7) throws SQLException;

   void fetch(ResultCursor var1, ResultHandler var2, int var3, boolean var4) throws SQLException;

   Query createSimpleQuery(String var1) throws SQLException;

   boolean isReWriteBatchedInsertsEnabled();

   CachedQuery createQuery(String var1, boolean var2, boolean var3, @Nullable String... var4) throws SQLException;

   Object createQueryKey(String var1, boolean var2, boolean var3, @Nullable String... var4);

   CachedQuery createQueryByKey(Object var1) throws SQLException;

   CachedQuery borrowQueryByKey(Object var1) throws SQLException;

   CachedQuery borrowQuery(String var1) throws SQLException;

   CachedQuery borrowCallableQuery(String var1) throws SQLException;

   CachedQuery borrowReturningQuery(String var1, @Nullable String[] var2) throws SQLException;

   void releaseQuery(CachedQuery var1);

   Query wrap(List<NativeQuery> var1);

   void processNotifies() throws SQLException;

   void processNotifies(int var1) throws SQLException;

   /** @deprecated */
   @Deprecated
   ParameterList createFastpathParameters(int var1);

   /** @deprecated */
   @Deprecated
   @Nullable
   byte[] fastpathCall(int var1, ParameterList var2, boolean var3) throws SQLException;

   CopyOperation startCopy(String var1, boolean var2) throws SQLException;

   int getProtocolVersion();

   void addBinaryReceiveOid(int var1);

   void removeBinaryReceiveOid(int var1);

   /** @deprecated */
   @Deprecated
   Set<? extends Integer> getBinaryReceiveOids();

   void setBinaryReceiveOids(Set<Integer> var1);

   void addBinarySendOid(int var1);

   void removeBinarySendOid(int var1);

   /** @deprecated */
   @Deprecated
   Set<? extends Integer> getBinarySendOids();

   void setBinarySendOids(Set<Integer> var1);

   boolean getIntegerDateTimes();

   HostSpec getHostSpec();

   String getUser();

   String getDatabase();

   void sendQueryCancel() throws SQLException;

   int getBackendPID();

   void abort();

   void close();

   Closeable getCloseAction();

   boolean isClosed();

   String getServerVersion();

   PGNotification[] getNotifications() throws SQLException;

   @Nullable
   SQLWarning getWarnings();

   int getServerVersionNum();

   TransactionState getTransactionState();

   boolean getStandardConformingStrings();

   boolean getQuoteReturningIdentifiers();

   @Nullable
   TimeZone getTimeZone();

   Encoding getEncoding();

   String getApplicationName();

   boolean isColumnSanitiserDisabled();

   EscapeSyntaxCallMode getEscapeSyntaxCallMode();

   PreferQueryMode getPreferQueryMode();

   void setPreferQueryMode(PreferQueryMode var1);

   AutoSave getAutoSave();

   void setAutoSave(AutoSave var1);

   boolean willHealOnRetry(SQLException var1);

   void setFlushCacheOnDeallocate(boolean var1);

   ReplicationProtocol getReplicationProtocol();

   void setNetworkTimeout(int var1) throws IOException;

   int getNetworkTimeout() throws IOException;

   Map<String, String> getParameterStatuses();

   @Nullable
   String getParameterStatus(String var1);

   int getAdaptiveFetchSize(boolean var1, ResultCursor var2);

   boolean getAdaptiveFetch();

   void setAdaptiveFetch(boolean var1);

   void addQueryToAdaptiveFetchCache(boolean var1, ResultCursor var2);

   void removeQueryFromAdaptiveFetchCache(boolean var1, ResultCursor var2);
}
