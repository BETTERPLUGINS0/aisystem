package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.PGConnection;
import fr.xephi.authme.libs.org.postgresql.jdbc.FieldMetadata;
import fr.xephi.authme.libs.org.postgresql.jdbc.TimestampUtils;
import fr.xephi.authme.libs.org.postgresql.util.LruCache;
import fr.xephi.authme.libs.org.postgresql.xml.PGXmlFactoryFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TimerTask;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;
import org.checkerframework.dataflow.qual.Pure;

public interface BaseConnection extends PGConnection, Connection {
   void cancelQuery() throws SQLException;

   ResultSet execSQLQuery(String var1) throws SQLException;

   ResultSet execSQLQuery(String var1, int var2, int var3) throws SQLException;

   void execSQLUpdate(String var1) throws SQLException;

   QueryExecutor getQueryExecutor();

   ReplicationProtocol getReplicationProtocol();

   Object getObject(String var1, @Nullable String var2, @Nullable byte[] var3) throws SQLException;

   @Pure
   Encoding getEncoding() throws SQLException;

   TypeInfo getTypeInfo();

   boolean haveMinimumServerVersion(int var1);

   boolean haveMinimumServerVersion(Version var1);

   @PolyNull
   byte[] encodeString(@PolyNull String var1) throws SQLException;

   String escapeString(String var1) throws SQLException;

   boolean getStandardConformingStrings();

   /** @deprecated */
   @Deprecated
   TimestampUtils getTimestampUtils();

   Logger getLogger();

   boolean getStringVarcharFlag();

   TransactionState getTransactionState();

   boolean binaryTransferSend(int var1);

   boolean isColumnSanitiserDisabled();

   void addTimerTask(TimerTask var1, long var2);

   void purgeTimerTasks();

   LruCache<FieldMetadata.Key, FieldMetadata> getFieldMetadataCache();

   CachedQuery createQuery(String var1, boolean var2, boolean var3, String... var4) throws SQLException;

   void setFlushCacheOnDeallocate(boolean var1);

   boolean hintReadOnly();

   PGXmlFactoryFactory getXmlFactoryFactory() throws SQLException;

   boolean getLogServerErrorDetail();
}
