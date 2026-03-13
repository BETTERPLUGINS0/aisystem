package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.Context;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardClient;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.export.ExceptionFactory;
import github.nighter.smartspawner.libs.mariadb.message.client.ChangeDbPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.PingPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.QueryPacket;
import github.nighter.smartspawner.libs.mariadb.message.client.ResetPacket;
import github.nighter.smartspawner.libs.mariadb.plugin.array.FloatArray;
import github.nighter.smartspawner.libs.mariadb.util.NativeSql;
import github.nighter.smartspawner.libs.mariadb.util.constants.CatalogTerm;
import github.nighter.smartspawner.libs.mariadb.util.timeout.NoOpQueryTimeoutHandler;
import github.nighter.smartspawner.libs.mariadb.util.timeout.QueryTimeoutHandler;
import github.nighter.smartspawner.libs.mariadb.util.timeout.QueryTimeoutHandlerImpl;
import java.nio.FloatBuffer;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.ConnectionEvent;

public class Connection implements java.sql.Connection {
   private static final Pattern CALLABLE_STATEMENT_PATTERN = Pattern.compile("^(\\s*\\{)?\\s*((\\?\\s*=)?(\\s*/\\*([^*]|\\*[^/])*\\*/)*\\s*call(\\s*/\\*([^*]|\\*[^/])*\\*/)*\\s*((((`[^`]+`)|([^`\\}]+))\\.)?((`[^`]+`)|([^`\\}(]+)))\\s*(\\(.*\\))?(\\s*/\\*([^*]|\\*[^/])*\\*/)*\\s*(#.*)?)\\s*(\\}\\s*)?$", 34);
   private final ClosableLock lock;
   private final Configuration conf;
   private final Client client;
   private final Properties clientInfo = new Properties();
   private final AtomicInteger savepointId = new AtomicInteger();
   private final boolean canUseServerTimeout;
   private final boolean canCachePrepStmts;
   private final boolean canUseServerMaxRows;
   private final int defaultFetchSize;
   private final boolean forceTransactionEnd;
   private ExceptionFactory exceptionFactory;
   private int lowercaseTableNames = -1;
   private boolean readOnly;
   private MariaDbPoolConnection poolConnection;
   private QueryTimeoutHandler queryTimeoutHandler;

   public Connection(Configuration conf, ClosableLock lock, Client client) {
      this.conf = conf;
      this.forceTransactionEnd = Boolean.parseBoolean(conf.nonMappedOptions().getProperty("forceTransactionEnd", "false"));
      this.lock = lock;
      this.exceptionFactory = client.getExceptionFactory().setConnection(this);
      this.client = client;
      Context context = this.client.getContext();
      this.canUseServerTimeout = context.getVersion().isMariaDBServer() && context.getVersion().versionGreaterOrEqual(10, 1, 2) && Boolean.parseBoolean(conf.nonMappedOptions().getProperty("canUseServerTimeout", "true"));
      this.queryTimeoutHandler = (QueryTimeoutHandler)(this.canUseServerTimeout ? NoOpQueryTimeoutHandler.INSTANCE : new QueryTimeoutHandlerImpl(this, lock));
      this.canUseServerMaxRows = context.getVersion().isMariaDBServer() && context.getVersion().versionGreaterOrEqual(10, 3, 0);
      this.canCachePrepStmts = conf.cachePrepStmts() && conf.useServerPrepStmts();
      this.defaultFetchSize = conf.defaultFetchSize();
   }

   public void setPoolConnection(MariaDbPoolConnection poolConnection) {
      this.poolConnection = poolConnection;
      this.exceptionFactory = this.exceptionFactory.setPoolConnection(poolConnection);
   }

   public void cancelCurrentQuery() throws SQLException {
      String currentIp = this.client.getSocketIp();
      HostAddress hostAddress = currentIp == null ? this.client.getHostAddress() : HostAddress.from(currentIp, this.client.getHostAddress().port, this.client.getHostAddress().primary);
      StandardClient cli = new StandardClient(this.conf, hostAddress, new ClosableLock(), true);

      try {
         cli.execute(new QueryPacket("KILL QUERY " + this.client.getContext().getThreadId()), false);
      } catch (Throwable var7) {
         try {
            cli.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      cli.close();
   }

   public Statement createStatement() {
      return new Statement(this, this.lock, 1, 1003, 1007, this.defaultFetchSize);
   }

   public PreparedStatement prepareStatement(String sql) throws SQLException {
      return this.prepareInternal(sql, 2, 1003, 1007, this.conf.useServerPrepStmts());
   }

   public PreparedStatement prepareInternal(String sql, int autoGeneratedKeys, int resultSetType, int resultSetConcurrency, boolean useBinary) throws SQLException {
      this.checkNotClosed();
      return (PreparedStatement)(useBinary && !sql.startsWith("/*client prepare*/") ? new ServerPreparedStatement(NativeSql.parse(sql, this.client.getContext()), this, this.lock, autoGeneratedKeys, resultSetType, resultSetConcurrency, this.defaultFetchSize) : new ClientPreparedStatement(NativeSql.parse(sql, this.client.getContext()), this, this.lock, autoGeneratedKeys, resultSetType, resultSetConcurrency, this.defaultFetchSize));
   }

   public CallableStatement prepareCall(String sql) throws SQLException {
      return this.prepareCall(sql, 1003, 1007);
   }

   public String nativeSQL(String sql) throws SQLException {
      this.checkNotClosed();
      return NativeSql.parse(sql, this.client.getContext());
   }

   public boolean getAutoCommit() throws SQLException {
      this.checkNotClosed();
      return (this.client.getContext().getServerStatus() & 2) > 0;
   }

   public void setAutoCommit(boolean autoCommit) throws SQLException {
      this.checkNotClosed();
      if (autoCommit != this.getAutoCommit()) {
         ClosableLock ignore = this.lock.closeableLock();

         try {
            this.getContext().addStateFlag(8);
            this.client.execute(new QueryPacket(autoCommit ? "set autocommit=1" : "set autocommit=0"), true);
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
   }

   public void commit() throws SQLException {
      this.checkNotClosed();
      ClosableLock ignore = this.lock.closeableLock();

      try {
         if (this.forceTransactionEnd || (this.client.getContext().getServerStatus() & 1) > 0) {
            this.client.execute(new QueryPacket("COMMIT"), false);
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

   public void rollback() throws SQLException {
      this.checkNotClosed();
      ClosableLock ignore = this.lock.closeableLock();

      try {
         if (this.forceTransactionEnd || (this.client.getContext().getServerStatus() & 1) > 0) {
            this.client.execute(new QueryPacket("ROLLBACK"), true);
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

   public void close() throws SQLException {
      if (this.poolConnection != null) {
         this.poolConnection.fireConnectionClosed(new ConnectionEvent(this.poolConnection));
      } else {
         this.client.close();
      }
   }

   public boolean isClosed() {
      return this.client.isClosed();
   }

   public Context getContext() {
      return this.client.getContext();
   }

   public int getLowercaseTableNames() throws SQLException {
      if (this.lowercaseTableNames == -1) {
         Statement st = this.createStatement();

         try {
            ResultSet rs = st.executeQuery("select @@lower_case_table_names");

            try {
               rs.next();
               this.lowercaseTableNames = rs.getInt(1);
            } catch (Throwable var7) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var8) {
            if (st != null) {
               try {
                  st.close();
               } catch (Throwable var5) {
                  var8.addSuppressed(var5);
               }
            }

            throw var8;
         }

         if (st != null) {
            st.close();
         }
      }

      return this.lowercaseTableNames;
   }

   public DatabaseMetaData getMetaData() {
      return new DatabaseMetaData(this, this.conf);
   }

   public boolean isReadOnly() {
      return this.readOnly;
   }

   public void setReadOnly(boolean readOnly) throws SQLException {
      this.checkNotClosed();
      ClosableLock ignore = this.lock.closeableLock();

      try {
         if (this.readOnly != readOnly) {
            this.client.setReadOnly(readOnly);
         }

         this.readOnly = readOnly;
         this.getContext().addStateFlag(4);
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

   public String getCatalog() throws SQLException {
      return this.conf.useCatalogTerm() == CatalogTerm.UseCatalog ? this.getDatabase() : "def";
   }

   public void setCatalog(String catalog) throws SQLException {
      if (this.conf.useCatalogTerm() == CatalogTerm.UseCatalog) {
         this.setDatabase(catalog);
      }

   }

   public String getSchema() throws SQLException {
      return this.conf.useCatalogTerm() == CatalogTerm.UseSchema ? this.getDatabase() : null;
   }

   public void setSchema(String schema) throws SQLException {
      if (this.conf.useCatalogTerm() == CatalogTerm.UseSchema) {
         this.setDatabase(schema);
      }

   }

   private String getDatabase() throws SQLException {
      this.checkNotClosed();
      if (this.client.getContext().hasClientCapability(8388608L)) {
         return this.client.getContext().getDatabase();
      } else {
         Statement stmt = this.createStatement();

         String var3;
         try {
            ResultSet rs = stmt.executeQuery("select database()");
            rs.next();
            this.client.getContext().setDatabase(rs.getString(1));
            var3 = this.client.getContext().getDatabase();
         } catch (Throwable var5) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (stmt != null) {
            stmt.close();
         }

         return var3;
      }
   }

   private void setDatabase(String database) throws SQLException {
      this.checkNotClosed();
      if (database != null && (!this.client.getContext().hasClientCapability(8388608L) || !database.equals(this.client.getContext().getDatabase()))) {
         ClosableLock ignore = this.lock.closeableLock();

         try {
            this.getContext().addStateFlag(2);
            this.client.execute(new ChangeDbPacket(database), true);
            this.client.getContext().setDatabase(database);
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
   }

   public int getTransactionIsolation() throws SQLException {
      this.checkNotClosed();
      boolean useContextState = this.conf.useLocalSessionState() || this.client.getContext().hasClientCapability(8388608L) && (this.client.getContext().getVersion().isMariaDBServer() && this.client.getContext().getVersion().versionGreaterOrEqual(10, 2, 2) || this.client.getContext().getVersion().versionGreaterOrEqual(5, 7, 0));
      if (useContextState && this.client.getContext().getTransactionIsolationLevel() != null) {
         return this.client.getContext().getTransactionIsolationLevel();
      } else {
         String sql = this.client.getContext().canUseTransactionIsolation() ? "SELECT @@session.transaction_isolation" : "SELECT @@session.tx_isolation";
         Statement stmt = this.createStatement();

         byte var8;
         label118: {
            label119: {
               label120: {
                  try {
                     ResultSet rs = stmt.executeQuery(sql);
                     if (!rs.next()) {
                        throw this.exceptionFactory.create("Failed to retrieve transaction isolation");
                     }

                     String response = rs.getString(1);
                     byte var7 = -1;
                     switch(response.hashCode()) {
                     case -1296331988:
                        if (response.equals("READ-UNCOMMITTED")) {
                           var7 = 1;
                        }
                        break;
                     case -1116651265:
                        if (response.equals("SERIALIZABLE")) {
                           var7 = 3;
                        }
                        break;
                     case -718034194:
                        if (response.equals("REPEATABLE-READ")) {
                           var7 = 0;
                        }
                        break;
                     case 1633007589:
                        if (response.equals("READ-COMMITTED")) {
                           var7 = 2;
                        }
                     }

                     switch(var7) {
                     case 0:
                        var8 = 4;
                        break label119;
                     case 1:
                        var8 = 1;
                        break label118;
                     case 2:
                        var8 = 2;
                        break;
                     case 3:
                        var8 = 8;
                        break label120;
                     default:
                        throw this.exceptionFactory.create(String.format("Could not get transaction isolation level: Invalid value \"%s\"", response));
                     }
                  } catch (Throwable var10) {
                     if (stmt != null) {
                        try {
                           stmt.close();
                        } catch (Throwable var9) {
                           var10.addSuppressed(var9);
                        }
                     }

                     throw var10;
                  }

                  if (stmt != null) {
                     stmt.close();
                  }

                  return var8;
               }

               if (stmt != null) {
                  stmt.close();
               }

               return var8;
            }

            if (stmt != null) {
               stmt.close();
            }

            return var8;
         }

         if (stmt != null) {
            stmt.close();
         }

         return var8;
      }
   }

   public void setTransactionIsolation(int level) throws SQLException {
      this.checkNotClosed();
      boolean useContextState = this.conf.useLocalSessionState() || this.client.getContext().hasClientCapability(8388608L) && (this.client.getContext().getVersion().isMariaDBServer() && this.client.getContext().getVersion().versionGreaterOrEqual(10, 2, 2) || this.client.getContext().getVersion().versionGreaterOrEqual(5, 7, 0));
      if (!useContextState || this.client.getContext().getTransactionIsolationLevel() == null || level != this.client.getContext().getTransactionIsolationLevel()) {
         String query = "SET SESSION TRANSACTION ISOLATION LEVEL";
         switch(level) {
         case 1:
            query = query + " READ UNCOMMITTED";
            break;
         case 2:
            query = query + " READ COMMITTED";
            break;
         case 3:
         case 5:
         case 6:
         case 7:
         default:
            throw new SQLException("Unsupported transaction isolation level");
         case 4:
            query = query + " REPEATABLE READ";
            break;
         case 8:
            query = query + " SERIALIZABLE";
         }

         ClosableLock ignore = this.lock.closeableLock();

         try {
            this.checkNotClosed();
            this.getContext().addStateFlag(16);
            if (this.conf.useLocalSessionState()) {
               this.client.getContext().setTransactionIsolationLevel(level);
            }

            this.client.execute(new QueryPacket(query), true);
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
   }

   public SQLWarning getWarnings() throws SQLException {
      this.checkNotClosed();
      if (this.client.getContext().getWarning() == 0) {
         return null;
      } else {
         SQLWarning last = null;
         SQLWarning first = null;
         Statement st = this.createStatement();

         try {
            ResultSet rs = st.executeQuery("show warnings");

            SQLWarning warning;
            try {
               for(; rs.next(); last = warning) {
                  int code = rs.getInt(2);
                  String message = rs.getString(3);
                  warning = new SQLWarning(message, (String)null, code);
                  if (first == null) {
                     first = warning;
                  } else {
                     last.setNextWarning(warning);
                  }
               }
            } catch (Throwable var10) {
               if (rs != null) {
                  try {
                     rs.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (rs != null) {
               rs.close();
            }
         } catch (Throwable var11) {
            if (st != null) {
               try {
                  st.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (st != null) {
            st.close();
         }

         return first;
      }
   }

   public void clearWarnings() throws SQLException {
      this.checkNotClosed();
      this.client.getContext().setWarning(0);
   }

   public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
      this.checkNotClosed();
      return new Statement(this, this.lock, 1, resultSetType, resultSetConcurrency, this.defaultFetchSize);
   }

   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      return this.prepareInternal(sql, 1, resultSetType, resultSetConcurrency, this.conf.useServerPrepStmts());
   }

   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
      this.checkNotClosed();
      Matcher matcher = CALLABLE_STATEMENT_PATTERN.matcher(sql);
      if (!matcher.matches()) {
         throw new SQLSyntaxErrorException("invalid callable syntax. must be like {[?=]call <procedure/function name>[(?,?, ...)]}\n but was : " + sql);
      } else {
         String query = NativeSql.parse(matcher.group(2), this.client.getContext());
         boolean isFunction = matcher.group(3) != null;
         String databaseAndProcedure = matcher.group(8);
         String database = matcher.group(10);
         String procedureName = matcher.group(13);
         String arguments = matcher.group(16);
         if (database == null) {
            database = this.getCatalog();
         }

         return (CallableStatement)(isFunction ? new FunctionStatement(this, database, databaseAndProcedure, arguments == null ? "()" : arguments, this.lock, resultSetType, resultSetConcurrency) : new ProcedureStatement(this, query, database, procedureName, this.lock, resultSetType, resultSetConcurrency));
      }
   }

   public Map<String, Class<?>> getTypeMap() {
      return new HashMap();
   }

   public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
      throw this.exceptionFactory.notSupported("TypeMap are not supported");
   }

   public int getHoldability() {
      return 1;
   }

   public void setHoldability(int holdability) {
   }

   public Savepoint setSavepoint() throws SQLException {
      Connection.MariaDbSavepoint savepoint = new Connection.MariaDbSavepoint(this.savepointId.incrementAndGet());
      this.client.execute(new QueryPacket("SAVEPOINT `" + savepoint.rawValue() + "`"), true);
      return savepoint;
   }

   public Savepoint setSavepoint(String name) throws SQLException {
      Connection.MariaDbSavepoint savepoint = new Connection.MariaDbSavepoint(name.replace("`", "``"));
      this.client.execute(new QueryPacket("SAVEPOINT `" + savepoint.rawValue() + "`"), true);
      return savepoint;
   }

   public void rollback(Savepoint savepoint) throws SQLException {
      this.checkNotClosed();
      ClosableLock ignore = this.lock.closeableLock();

      try {
         if (!(savepoint instanceof Connection.MariaDbSavepoint)) {
            throw this.exceptionFactory.create("Unknown savepoint type");
         }

         this.client.execute(new QueryPacket("ROLLBACK TO SAVEPOINT `" + ((Connection.MariaDbSavepoint)savepoint).rawValue() + "`"), true);
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

   public void releaseSavepoint(Savepoint savepoint) throws SQLException {
      this.checkNotClosed();
      ClosableLock ignore = this.lock.closeableLock();

      try {
         if (!(savepoint instanceof Connection.MariaDbSavepoint)) {
            throw this.exceptionFactory.create("Unknown savepoint type");
         }

         this.client.execute(new QueryPacket("RELEASE SAVEPOINT `" + ((Connection.MariaDbSavepoint)savepoint).rawValue() + "`"), true);
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

   public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
      this.checkNotClosed();
      return new Statement(this, this.lock, 2, resultSetType, resultSetConcurrency, this.defaultFetchSize);
   }

   public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
      return this.prepareStatement(sql, resultSetType, resultSetConcurrency);
   }

   public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
      return this.prepareCall(sql, resultSetType, resultSetConcurrency);
   }

   public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
      return this.prepareInternal(sql, autoGeneratedKeys, 1003, 1007, this.conf.useServerPrepStmts());
   }

   public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
      return this.prepareStatement(sql, 1);
   }

   public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
      return this.prepareStatement(sql, 1);
   }

   public Clob createClob() {
      return new MariaDbClob();
   }

   public Blob createBlob() {
      return new MariaDbBlob();
   }

   public NClob createNClob() {
      return new MariaDbClob();
   }

   public SQLXML createSQLXML() throws SQLException {
      throw this.exceptionFactory.notSupported("SQLXML type is not supported");
   }

   private void checkNotClosed() throws SQLException {
      if (this.client.isClosed()) {
         throw this.exceptionFactory.create("Connection is closed", "08000", 1220);
      }
   }

   public boolean isValid(int timeout) throws SQLException {
      if (timeout < 0) {
         throw this.exceptionFactory.create("the value supplied for timeout is negative");
      } else {
         int initialSocketTimeout = this.client.getSocketTimeout();

         boolean var20;
         try {
            ClosableLock ignore = this.lock.closeableLock();

            try {
               if (initialSocketTimeout != timeout * 1000) {
                  this.client.setSocketTimeout(timeout * 1000);
               }

               this.client.execute(PingPacket.INSTANCE, true);
               var20 = true;
            } catch (Throwable var17) {
               if (ignore != null) {
                  try {
                     ignore.close();
                  } catch (Throwable var16) {
                     var17.addSuppressed(var16);
                  }
               }

               throw var17;
            }

            if (ignore != null) {
               ignore.close();
            }

            return var20;
         } catch (SQLException var18) {
            if (this.poolConnection != null) {
               MariaDbPoolConnection poolConnection = this.poolConnection;
               poolConnection.fireConnectionErrorOccurred(var18);
               poolConnection.close();
            }

            var20 = false;
         } finally {
            if (initialSocketTimeout != timeout * 1000) {
               try {
                  this.client.setSocketTimeout(initialSocketTimeout);
               } catch (SQLException var15) {
               }
            }

         }

         return var20;
      }
   }

   public void setClientInfo(String name, String value) {
      this.clientInfo.put(name, value);
   }

   public String getClientInfo(String name) {
      return (String)this.clientInfo.get(name);
   }

   public Properties getClientInfo() {
      return this.clientInfo;
   }

   public void setClientInfo(Properties properties) {
      this.clientInfo.putAll(properties);
   }

   public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
      return this.createArrayOf(typeName, (Object)elements);
   }

   public Array createArrayOf(String typeName, Object elements) throws SQLException {
      this.checkNotClosed();
      if (typeName == null) {
         throw this.exceptionFactory.notSupported("typeName is not mandatory");
      } else if (elements == null) {
         return null;
      } else {
         byte var4 = -1;
         switch(typeName.hashCode()) {
         case 67973692:
            if (typeName.equals("Float")) {
               var4 = 1;
            }
            break;
         case 97526364:
            if (typeName.equals("float")) {
               var4 = 0;
            }
         }

         switch(var4) {
         case 0:
         case 1:
            if (float[].class.equals(elements.getClass())) {
               return new FloatArray((float[])elements, this.client.getContext());
            } else {
               if (Float[].class.equals(elements.getClass())) {
                  float[] result = ((FloatBuffer)Arrays.stream((Float[])elements).collect(() -> {
                     return FloatBuffer.allocate(((Float[])elements).length);
                  }, FloatBuffer::put, (left, right) -> {
                     throw new UnsupportedOperationException();
                  })).array();
                  return new FloatArray(result, this.client.getContext());
               }

               throw this.exceptionFactory.notSupported("elements class is expect to be float[]/Float[] for 'float/Float' typeName");
            }
         default:
            throw this.exceptionFactory.notSupported(String.format("typeName %s is not supported", typeName));
         }
      }
   }

   public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
      throw this.exceptionFactory.notSupported("Struct type is not supported");
   }

   public void abort(Executor executor) throws SQLException {
      if (this.poolConnection != null) {
         MariaDbPoolConnection poolConnection = this.poolConnection;
         poolConnection.close();
      } else {
         this.client.abort(executor);
      }
   }

   public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
      if (this.isClosed()) {
         throw this.exceptionFactory.create("Connection.setNetworkTimeout cannot be called on a closed connection");
      } else if (milliseconds < 0) {
         throw this.exceptionFactory.create("Connection.setNetworkTimeout cannot be called with a negative timeout");
      } else {
         this.getContext().addStateFlag(1);
         ClosableLock ignore = this.lock.closeableLock();

         try {
            this.client.setSocketTimeout(milliseconds);
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

      }
   }

   public int getNetworkTimeout() {
      return this.client.getSocketTimeout();
   }

   public <T> T unwrap(Class<T> iface) throws SQLException {
      if (this.isWrapperFor(iface)) {
         return iface.cast(this);
      } else {
         throw new SQLException("The receiver is not a wrapper for " + iface.getName());
      }
   }

   public boolean isWrapperFor(Class<?> iface) {
      return iface.isInstance(this);
   }

   public Client getClient() {
      return this.client;
   }

   public void reset() throws SQLException {
      this.checkNotClosed();
      boolean useComReset = this.conf.useResetConnection() && this.getContext().getVersion().isMariaDBServer() && (this.getContext().getVersion().versionGreaterOrEqual(10, 3, 13) || this.getContext().getVersion().getMajorVersion() == 10 && this.getContext().getVersion().getMinorVersion() == 2 && this.getContext().getVersion().versionGreaterOrEqual(10, 2, 22));
      if (useComReset) {
         this.client.execute(ResetPacket.INSTANCE, true);
      }

      if (this.forceTransactionEnd || (this.client.getContext().getServerStatus() & 1) > 0) {
         this.client.execute(new QueryPacket("ROLLBACK"), true);
      }

      int stateFlag = this.getContext().getStateFlag();
      if (stateFlag != 0) {
         try {
            if ((stateFlag & 1) != 0) {
               this.setNetworkTimeout((Executor)null, this.conf.socketTimeout());
            }

            if ((stateFlag & 8) != 0) {
               this.setAutoCommit(this.conf.autocommit() == null || this.conf.autocommit());
            }

            if ((stateFlag & 2) != 0) {
               this.setCatalog(this.conf.database());
            }

            if ((stateFlag & 4) != 0) {
               this.setReadOnly(false);
            }

            if (!useComReset && (stateFlag & 16) != 0) {
               this.setTransactionIsolation(this.conf.transactionIsolation() == null ? 4 : this.conf.transactionIsolation().getLevel());
            }
         } catch (SQLException var4) {
            throw this.exceptionFactory.create("error resetting connection");
         }
      }

      this.client.reset();
      this.clearWarnings();
   }

   public long getThreadId() {
      return this.client.getContext().getThreadId();
   }

   public void fireStatementClosed(PreparedStatement prep) {
      if (this.poolConnection != null) {
         this.poolConnection.fireStatementClosed(prep);
      }

   }

   protected ExceptionFactory getExceptionFactory() {
      return this.exceptionFactory;
   }

   public QueryTimeoutHandler handleTimeout(int queryTimeout) {
      return this.queryTimeoutHandler.create(queryTimeout);
   }

   protected ClosableLock getLock() {
      return this.lock;
   }

   public boolean useServerTimeout() {
      return this.canUseServerTimeout;
   }

   public boolean cachePrepStmts() {
      return this.canCachePrepStmts;
   }

   public boolean useServerMaxRows() {
      return this.canUseServerMaxRows;
   }

   public String __test_host() {
      return this.client.getHostAddress().toString();
   }

   class MariaDbSavepoint implements Savepoint {
      private final String name;
      private final Integer id;

      public MariaDbSavepoint(final String param2) {
         this.name = name;
         this.id = null;
      }

      public MariaDbSavepoint(final int param2) {
         this.id = savepointId;
         this.name = null;
      }

      public int getSavepointId() throws SQLException {
         if (this.id == null) {
            throw Connection.this.exceptionFactory.create("Cannot retrieve savepoint id of a named savepoint");
         } else {
            return this.id;
         }
      }

      public String getSavepointName() throws SQLException {
         if (this.id != null) {
            throw Connection.this.exceptionFactory.create("Cannot retrieve savepoint name of an unnamed savepoint");
         } else {
            return this.name;
         }
      }

      public String rawValue() {
         return this.id != null ? "_jid_" + this.id : this.name;
      }
   }
}
