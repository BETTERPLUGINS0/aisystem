package fr.xephi.authme.libs.org.postgresql.ds.common;

import fr.xephi.authme.libs.org.postgresql.Driver;
import fr.xephi.authme.libs.org.postgresql.PGProperty;
import fr.xephi.authme.libs.org.postgresql.jdbc.AutoSave;
import fr.xephi.authme.libs.org.postgresql.jdbc.PreferQueryMode;
import fr.xephi.authme.libs.org.postgresql.util.ExpressionProperties;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.URLCoder;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.CommonDataSource;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class BaseDataSource implements CommonDataSource, Referenceable {
   private static final Logger LOGGER = Logger.getLogger(BaseDataSource.class.getName());
   private String[] serverNames = new String[]{"localhost"};
   @Nullable
   private String databaseName = "";
   @Nullable
   private String user;
   @Nullable
   private String password;
   private int[] portNumbers = new int[]{0};
   private Properties properties = new Properties();

   public Connection getConnection() throws SQLException {
      return this.getConnection(this.user, this.password);
   }

   public Connection getConnection(@Nullable String user, @Nullable String password) throws SQLException {
      try {
         Connection con = DriverManager.getConnection(this.getUrl(), user, password);
         if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Created a {0} for {1} at {2}", new Object[]{this.getDescription(), user, this.getUrl()});
         }

         return con;
      } catch (SQLException var4) {
         LOGGER.log(Level.FINE, "Failed to create a {0} for {1} at {2}: {3}", new Object[]{this.getDescription(), user, this.getUrl(), var4});
         throw var4;
      }
   }

   @Nullable
   public PrintWriter getLogWriter() {
      return null;
   }

   public void setLogWriter(@Nullable PrintWriter printWriter) {
   }

   /** @deprecated */
   @Deprecated
   public String getServerName() {
      return this.serverNames[0];
   }

   public String[] getServerNames() {
      return this.serverNames;
   }

   /** @deprecated */
   @Deprecated
   public void setServerName(String serverName) {
      this.setServerNames(new String[]{serverName});
   }

   public void setServerNames(@Nullable String[] serverNames) {
      if (serverNames != null && serverNames.length != 0) {
         serverNames = (String[])serverNames.clone();

         for(int i = 0; i < serverNames.length; ++i) {
            String serverName = serverNames[i];
            if (serverName == null || "".equals(serverName)) {
               serverNames[i] = "localhost";
            }
         }

         this.serverNames = serverNames;
      } else {
         this.serverNames = new String[]{"localhost"};
      }

   }

   @Nullable
   public String getDatabaseName() {
      return this.databaseName;
   }

   public void setDatabaseName(@Nullable String databaseName) {
      this.databaseName = databaseName;
   }

   public abstract String getDescription();

   @Nullable
   public String getUser() {
      return this.user;
   }

   public void setUser(@Nullable String user) {
      this.user = user;
   }

   @Nullable
   public String getPassword() {
      return this.password;
   }

   public void setPassword(@Nullable String password) {
      this.password = password;
   }

   /** @deprecated */
   @Deprecated
   public int getPortNumber() {
      return this.portNumbers != null && this.portNumbers.length != 0 ? this.portNumbers[0] : 0;
   }

   public int[] getPortNumbers() {
      return this.portNumbers;
   }

   /** @deprecated */
   @Deprecated
   public void setPortNumber(int portNumber) {
      this.setPortNumbers(new int[]{portNumber});
   }

   public void setPortNumbers(@Nullable int[] portNumbers) {
      if (portNumbers == null || portNumbers.length == 0) {
         portNumbers = new int[]{0};
      }

      this.portNumbers = Arrays.copyOf(portNumbers, portNumbers.length);
   }

   @Nullable
   public String getOptions() {
      return PGProperty.OPTIONS.getOrDefault(this.properties);
   }

   public void setOptions(@Nullable String options) {
      PGProperty.OPTIONS.set(this.properties, options);
   }

   public int getLoginTimeout() {
      return PGProperty.LOGIN_TIMEOUT.getIntNoCheck(this.properties);
   }

   public void setLoginTimeout(int loginTimeout) {
      PGProperty.LOGIN_TIMEOUT.set(this.properties, loginTimeout);
   }

   public int getConnectTimeout() {
      return PGProperty.CONNECT_TIMEOUT.getIntNoCheck(this.properties);
   }

   public void setConnectTimeout(int connectTimeout) {
      PGProperty.CONNECT_TIMEOUT.set(this.properties, connectTimeout);
   }

   public int getGssResponseTimeout() {
      return PGProperty.GSS_RESPONSE_TIMEOUT.getIntNoCheck(this.properties);
   }

   public void setGssResponseTimeout(int gssResponseTimeout) {
      PGProperty.GSS_RESPONSE_TIMEOUT.set(this.properties, gssResponseTimeout);
   }

   public int getSslResponseTimeout() {
      return PGProperty.SSL_RESPONSE_TIMEOUT.getIntNoCheck(this.properties);
   }

   public void setSslResponseTimeout(int sslResponseTimeout) {
      PGProperty.SSL_RESPONSE_TIMEOUT.set(this.properties, sslResponseTimeout);
   }

   public int getProtocolVersion() {
      return !PGProperty.PROTOCOL_VERSION.isPresent(this.properties) ? 0 : PGProperty.PROTOCOL_VERSION.getIntNoCheck(this.properties);
   }

   public void setProtocolVersion(int protocolVersion) {
      if (protocolVersion == 0) {
         PGProperty.PROTOCOL_VERSION.set(this.properties, (String)null);
      } else {
         PGProperty.PROTOCOL_VERSION.set(this.properties, protocolVersion);
      }

   }

   public boolean getQuoteReturningIdentifiers() {
      return PGProperty.QUOTE_RETURNING_IDENTIFIERS.getBoolean(this.properties);
   }

   public void setQuoteReturningIdentifiers(boolean quoteIdentifiers) {
      PGProperty.QUOTE_RETURNING_IDENTIFIERS.set(this.properties, quoteIdentifiers);
   }

   public int getReceiveBufferSize() {
      return PGProperty.RECEIVE_BUFFER_SIZE.getIntNoCheck(this.properties);
   }

   public void setReceiveBufferSize(int nbytes) {
      PGProperty.RECEIVE_BUFFER_SIZE.set(this.properties, nbytes);
   }

   public int getSendBufferSize() {
      return PGProperty.SEND_BUFFER_SIZE.getIntNoCheck(this.properties);
   }

   public void setSendBufferSize(int nbytes) {
      PGProperty.SEND_BUFFER_SIZE.set(this.properties, nbytes);
   }

   public void setPrepareThreshold(int count) {
      PGProperty.PREPARE_THRESHOLD.set(this.properties, count);
   }

   public int getPrepareThreshold() {
      return PGProperty.PREPARE_THRESHOLD.getIntNoCheck(this.properties);
   }

   public int getPreparedStatementCacheQueries() {
      return PGProperty.PREPARED_STATEMENT_CACHE_QUERIES.getIntNoCheck(this.properties);
   }

   public void setPreparedStatementCacheQueries(int cacheSize) {
      PGProperty.PREPARED_STATEMENT_CACHE_QUERIES.set(this.properties, cacheSize);
   }

   public int getPreparedStatementCacheSizeMiB() {
      return PGProperty.PREPARED_STATEMENT_CACHE_SIZE_MIB.getIntNoCheck(this.properties);
   }

   public void setPreparedStatementCacheSizeMiB(int cacheSize) {
      PGProperty.PREPARED_STATEMENT_CACHE_SIZE_MIB.set(this.properties, cacheSize);
   }

   public int getDatabaseMetadataCacheFields() {
      return PGProperty.DATABASE_METADATA_CACHE_FIELDS.getIntNoCheck(this.properties);
   }

   public void setDatabaseMetadataCacheFields(int cacheSize) {
      PGProperty.DATABASE_METADATA_CACHE_FIELDS.set(this.properties, cacheSize);
   }

   public int getDatabaseMetadataCacheFieldsMiB() {
      return PGProperty.DATABASE_METADATA_CACHE_FIELDS_MIB.getIntNoCheck(this.properties);
   }

   public void setDatabaseMetadataCacheFieldsMiB(int cacheSize) {
      PGProperty.DATABASE_METADATA_CACHE_FIELDS_MIB.set(this.properties, cacheSize);
   }

   public void setDefaultRowFetchSize(int fetchSize) {
      PGProperty.DEFAULT_ROW_FETCH_SIZE.set(this.properties, fetchSize);
   }

   public int getDefaultRowFetchSize() {
      return PGProperty.DEFAULT_ROW_FETCH_SIZE.getIntNoCheck(this.properties);
   }

   public void setUnknownLength(int unknownLength) {
      PGProperty.UNKNOWN_LENGTH.set(this.properties, unknownLength);
   }

   public int getUnknownLength() {
      return PGProperty.UNKNOWN_LENGTH.getIntNoCheck(this.properties);
   }

   public void setSocketTimeout(int seconds) {
      PGProperty.SOCKET_TIMEOUT.set(this.properties, seconds);
   }

   public int getSocketTimeout() {
      return PGProperty.SOCKET_TIMEOUT.getIntNoCheck(this.properties);
   }

   public void setCancelSignalTimeout(int seconds) {
      PGProperty.CANCEL_SIGNAL_TIMEOUT.set(this.properties, seconds);
   }

   public int getCancelSignalTimeout() {
      return PGProperty.CANCEL_SIGNAL_TIMEOUT.getIntNoCheck(this.properties);
   }

   public void setSsl(boolean enabled) {
      if (enabled) {
         PGProperty.SSL.set(this.properties, true);
      } else {
         PGProperty.SSL.set(this.properties, false);
      }

   }

   public boolean getSsl() {
      return PGProperty.SSL.getBoolean(this.properties) || "".equals(PGProperty.SSL.getOrDefault(this.properties));
   }

   public void setSslfactory(String classname) {
      PGProperty.SSL_FACTORY.set(this.properties, classname);
   }

   @Nullable
   public String getSslfactory() {
      return PGProperty.SSL_FACTORY.getOrDefault(this.properties);
   }

   @Nullable
   public String getSslMode() {
      return PGProperty.SSL_MODE.getOrDefault(this.properties);
   }

   public void setSslMode(@Nullable String mode) {
      PGProperty.SSL_MODE.set(this.properties, mode);
   }

   @Nullable
   public String getSslFactoryArg() {
      return PGProperty.SSL_FACTORY_ARG.getOrDefault(this.properties);
   }

   public void setSslFactoryArg(@Nullable String arg) {
      PGProperty.SSL_FACTORY_ARG.set(this.properties, arg);
   }

   @Nullable
   public String getSslHostnameVerifier() {
      return PGProperty.SSL_HOSTNAME_VERIFIER.getOrDefault(this.properties);
   }

   public void setSslHostnameVerifier(@Nullable String className) {
      PGProperty.SSL_HOSTNAME_VERIFIER.set(this.properties, className);
   }

   @Nullable
   public String getSslCert() {
      return PGProperty.SSL_CERT.getOrDefault(this.properties);
   }

   public void setSslCert(@Nullable String file) {
      PGProperty.SSL_CERT.set(this.properties, file);
   }

   @Nullable
   public String getSslKey() {
      return PGProperty.SSL_KEY.getOrDefault(this.properties);
   }

   public void setSslKey(@Nullable String file) {
      PGProperty.SSL_KEY.set(this.properties, file);
   }

   @Nullable
   public String getSslRootCert() {
      return PGProperty.SSL_ROOT_CERT.getOrDefault(this.properties);
   }

   public void setSslRootCert(@Nullable String file) {
      PGProperty.SSL_ROOT_CERT.set(this.properties, file);
   }

   @Nullable
   public String getSslPassword() {
      return PGProperty.SSL_PASSWORD.getOrDefault(this.properties);
   }

   public void setSslPassword(@Nullable String password) {
      PGProperty.SSL_PASSWORD.set(this.properties, password);
   }

   @Nullable
   public String getSslPasswordCallback() {
      return PGProperty.SSL_PASSWORD_CALLBACK.getOrDefault(this.properties);
   }

   public void setSslPasswordCallback(@Nullable String className) {
      PGProperty.SSL_PASSWORD_CALLBACK.set(this.properties, className);
   }

   public void setApplicationName(@Nullable String applicationName) {
      PGProperty.APPLICATION_NAME.set(this.properties, applicationName);
   }

   public String getApplicationName() {
      return (String)Nullness.castNonNull(PGProperty.APPLICATION_NAME.getOrDefault(this.properties));
   }

   public void setTargetServerType(@Nullable String targetServerType) {
      PGProperty.TARGET_SERVER_TYPE.set(this.properties, targetServerType);
   }

   public String getTargetServerType() {
      return (String)Nullness.castNonNull(PGProperty.TARGET_SERVER_TYPE.getOrDefault(this.properties));
   }

   public void setLoadBalanceHosts(boolean loadBalanceHosts) {
      PGProperty.LOAD_BALANCE_HOSTS.set(this.properties, loadBalanceHosts);
   }

   public boolean getLoadBalanceHosts() {
      return PGProperty.LOAD_BALANCE_HOSTS.isPresent(this.properties);
   }

   public void setHostRecheckSeconds(int hostRecheckSeconds) {
      PGProperty.HOST_RECHECK_SECONDS.set(this.properties, hostRecheckSeconds);
   }

   public int getHostRecheckSeconds() {
      return PGProperty.HOST_RECHECK_SECONDS.getIntNoCheck(this.properties);
   }

   public void setTcpKeepAlive(boolean enabled) {
      PGProperty.TCP_KEEP_ALIVE.set(this.properties, enabled);
   }

   public boolean getTcpKeepAlive() {
      return PGProperty.TCP_KEEP_ALIVE.getBoolean(this.properties);
   }

   public void setTcpNoDelay(boolean enabled) {
      PGProperty.TCP_NO_DELAY.set(this.properties, enabled);
   }

   public boolean getTcpNoDelay() {
      return PGProperty.TCP_NO_DELAY.getBoolean(this.properties);
   }

   public void setBinaryTransfer(boolean enabled) {
      PGProperty.BINARY_TRANSFER.set(this.properties, enabled);
   }

   public boolean getBinaryTransfer() {
      return PGProperty.BINARY_TRANSFER.getBoolean(this.properties);
   }

   public void setBinaryTransferEnable(@Nullable String oidList) {
      PGProperty.BINARY_TRANSFER_ENABLE.set(this.properties, oidList);
   }

   public String getBinaryTransferEnable() {
      return (String)Nullness.castNonNull(PGProperty.BINARY_TRANSFER_ENABLE.getOrDefault(this.properties));
   }

   public void setBinaryTransferDisable(@Nullable String oidList) {
      PGProperty.BINARY_TRANSFER_DISABLE.set(this.properties, oidList);
   }

   public String getBinaryTransferDisable() {
      return (String)Nullness.castNonNull(PGProperty.BINARY_TRANSFER_DISABLE.getOrDefault(this.properties));
   }

   @Nullable
   public String getStringType() {
      return PGProperty.STRING_TYPE.getOrDefault(this.properties);
   }

   public void setStringType(@Nullable String stringType) {
      PGProperty.STRING_TYPE.set(this.properties, stringType);
   }

   public boolean isColumnSanitiserDisabled() {
      return PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(this.properties);
   }

   public boolean getDisableColumnSanitiser() {
      return PGProperty.DISABLE_COLUMN_SANITISER.getBoolean(this.properties);
   }

   public void setDisableColumnSanitiser(boolean disableColumnSanitiser) {
      PGProperty.DISABLE_COLUMN_SANITISER.set(this.properties, disableColumnSanitiser);
   }

   @Nullable
   public String getCurrentSchema() {
      return PGProperty.CURRENT_SCHEMA.getOrDefault(this.properties);
   }

   public void setCurrentSchema(@Nullable String currentSchema) {
      PGProperty.CURRENT_SCHEMA.set(this.properties, currentSchema);
   }

   public boolean getReadOnly() {
      return PGProperty.READ_ONLY.getBoolean(this.properties);
   }

   public void setReadOnly(boolean readOnly) {
      PGProperty.READ_ONLY.set(this.properties, readOnly);
   }

   public String getReadOnlyMode() {
      return (String)Nullness.castNonNull(PGProperty.READ_ONLY_MODE.getOrDefault(this.properties));
   }

   public void setReadOnlyMode(@Nullable String mode) {
      PGProperty.READ_ONLY_MODE.set(this.properties, mode);
   }

   public boolean getLogUnclosedConnections() {
      return PGProperty.LOG_UNCLOSED_CONNECTIONS.getBoolean(this.properties);
   }

   public void setLogUnclosedConnections(boolean enabled) {
      PGProperty.LOG_UNCLOSED_CONNECTIONS.set(this.properties, enabled);
   }

   public boolean getLogServerErrorDetail() {
      return PGProperty.LOG_SERVER_ERROR_DETAIL.getBoolean(this.properties);
   }

   public void setLogServerErrorDetail(boolean enabled) {
      PGProperty.LOG_SERVER_ERROR_DETAIL.set(this.properties, enabled);
   }

   @Nullable
   public String getAssumeMinServerVersion() {
      return PGProperty.ASSUME_MIN_SERVER_VERSION.getOrDefault(this.properties);
   }

   public void setAssumeMinServerVersion(@Nullable String minVersion) {
      PGProperty.ASSUME_MIN_SERVER_VERSION.set(this.properties, minVersion);
   }

   public boolean getGroupStartupParameters() {
      return PGProperty.GROUP_STARTUP_PARAMETERS.getBoolean(this.properties);
   }

   public void setGroupStartupParameters(boolean groupStartupParameters) {
      PGProperty.GROUP_STARTUP_PARAMETERS.set(this.properties, groupStartupParameters);
   }

   @Nullable
   public String getJaasApplicationName() {
      return PGProperty.JAAS_APPLICATION_NAME.getOrDefault(this.properties);
   }

   public void setJaasApplicationName(@Nullable String name) {
      PGProperty.JAAS_APPLICATION_NAME.set(this.properties, name);
   }

   public boolean getJaasLogin() {
      return PGProperty.JAAS_LOGIN.getBoolean(this.properties);
   }

   public void setJaasLogin(boolean doLogin) {
      PGProperty.JAAS_LOGIN.set(this.properties, doLogin);
   }

   @Nullable
   public String getKerberosServerName() {
      return PGProperty.KERBEROS_SERVER_NAME.getOrDefault(this.properties);
   }

   public void setKerberosServerName(@Nullable String serverName) {
      PGProperty.KERBEROS_SERVER_NAME.set(this.properties, serverName);
   }

   public boolean getUseSpNego() {
      return PGProperty.USE_SPNEGO.getBoolean(this.properties);
   }

   public void setUseSpNego(boolean use) {
      PGProperty.USE_SPNEGO.set(this.properties, use);
   }

   @Nullable
   public String getGssLib() {
      return PGProperty.GSS_LIB.getOrDefault(this.properties);
   }

   public void setGssLib(@Nullable String lib) {
      PGProperty.GSS_LIB.set(this.properties, lib);
   }

   public String getGssEncMode() {
      return (String)Nullness.castNonNull(PGProperty.GSS_ENC_MODE.getOrDefault(this.properties));
   }

   public void setGssEncMode(@Nullable String mode) {
      PGProperty.GSS_ENC_MODE.set(this.properties, mode);
   }

   @Nullable
   public String getSspiServiceClass() {
      return PGProperty.SSPI_SERVICE_CLASS.getOrDefault(this.properties);
   }

   public void setSspiServiceClass(@Nullable String serviceClass) {
      PGProperty.SSPI_SERVICE_CLASS.set(this.properties, serviceClass);
   }

   public boolean getAllowEncodingChanges() {
      return PGProperty.ALLOW_ENCODING_CHANGES.getBoolean(this.properties);
   }

   public void setAllowEncodingChanges(boolean allow) {
      PGProperty.ALLOW_ENCODING_CHANGES.set(this.properties, allow);
   }

   @Nullable
   public String getSocketFactory() {
      return PGProperty.SOCKET_FACTORY.getOrDefault(this.properties);
   }

   public void setSocketFactory(@Nullable String socketFactoryClassName) {
      PGProperty.SOCKET_FACTORY.set(this.properties, socketFactoryClassName);
   }

   @Nullable
   public String getSocketFactoryArg() {
      return PGProperty.SOCKET_FACTORY_ARG.getOrDefault(this.properties);
   }

   public void setSocketFactoryArg(@Nullable String socketFactoryArg) {
      PGProperty.SOCKET_FACTORY_ARG.set(this.properties, socketFactoryArg);
   }

   public void setReplication(@Nullable String replication) {
      PGProperty.REPLICATION.set(this.properties, replication);
   }

   public String getEscapeSyntaxCallMode() {
      return (String)Nullness.castNonNull(PGProperty.ESCAPE_SYNTAX_CALL_MODE.getOrDefault(this.properties));
   }

   public void setEscapeSyntaxCallMode(@Nullable String callMode) {
      PGProperty.ESCAPE_SYNTAX_CALL_MODE.set(this.properties, callMode);
   }

   @Nullable
   public String getReplication() {
      return PGProperty.REPLICATION.getOrDefault(this.properties);
   }

   @Nullable
   public String getLocalSocketAddress() {
      return PGProperty.LOCAL_SOCKET_ADDRESS.getOrDefault(this.properties);
   }

   public void setLocalSocketAddress(String localSocketAddress) {
      PGProperty.LOCAL_SOCKET_ADDRESS.set(this.properties, localSocketAddress);
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public String getLoggerLevel() {
      return PGProperty.LOGGER_LEVEL.getOrDefault(this.properties);
   }

   /** @deprecated */
   @Deprecated
   public void setLoggerLevel(@Nullable String loggerLevel) {
      PGProperty.LOGGER_LEVEL.set(this.properties, loggerLevel);
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public String getLoggerFile() {
      ExpressionProperties exprProps = new ExpressionProperties(new Properties[]{this.properties, System.getProperties()});
      return PGProperty.LOGGER_FILE.getOrDefault(exprProps);
   }

   /** @deprecated */
   @Deprecated
   public void setLoggerFile(@Nullable String loggerFile) {
      PGProperty.LOGGER_FILE.set(this.properties, loggerFile);
   }

   public String getUrl() {
      StringBuilder url = new StringBuilder(100);
      url.append("jdbc:postgresql://");

      for(int i = 0; i < this.serverNames.length; ++i) {
         if (i > 0) {
            url.append(",");
         }

         url.append(this.serverNames[i]);
         if (this.portNumbers != null) {
            if (this.serverNames.length != this.portNumbers.length) {
               throw new IllegalArgumentException(String.format("Invalid argument: number of port %s entries must equal number of serverNames %s", Arrays.toString(this.portNumbers), Arrays.toString(this.serverNames)));
            }

            if (this.portNumbers.length >= i && this.portNumbers[i] != 0) {
               url.append(":").append(this.portNumbers[i]);
            }
         }
      }

      url.append("/");
      if (this.databaseName != null) {
         url.append(URLCoder.encode(this.databaseName));
      }

      StringBuilder query = new StringBuilder(100);
      PGProperty[] var3 = PGProperty.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PGProperty property = var3[var5];
         if (property.isPresent(this.properties)) {
            if (query.length() != 0) {
               query.append("&");
            }

            query.append(property.getName());
            query.append("=");
            String value = (String)Nullness.castNonNull(property.getOrDefault(this.properties));
            query.append(URLCoder.encode(value));
         }
      }

      if (query.length() > 0) {
         url.append("?");
         url.append(query);
      }

      return url.toString();
   }

   public String getURL() {
      return this.getUrl();
   }

   public void setUrl(String url) {
      Properties p = Driver.parseURL(url, (Properties)null);
      if (p == null) {
         throw new IllegalArgumentException("URL invalid " + url);
      } else {
         PGProperty[] var3 = PGProperty.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            PGProperty property = var3[var5];
            if (!this.properties.containsKey(property.getName())) {
               this.setProperty(property, property.getOrDefault(p));
            }
         }

      }
   }

   public void setURL(String url) {
      this.setUrl(url);
   }

   @Nullable
   public String getAuthenticationPluginClassName() {
      return PGProperty.AUTHENTICATION_PLUGIN_CLASS_NAME.getOrDefault(this.properties);
   }

   public void setAuthenticationPluginClassName(String className) {
      PGProperty.AUTHENTICATION_PLUGIN_CLASS_NAME.set(this.properties, className);
   }

   @Nullable
   public String getProperty(String name) throws SQLException {
      PGProperty pgProperty = PGProperty.forName(name);
      if (pgProperty != null) {
         return this.getProperty(pgProperty);
      } else {
         throw new PSQLException(GT.tr("Unsupported property name: {0}", name), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   public void setProperty(String name, @Nullable String value) throws SQLException {
      PGProperty pgProperty = PGProperty.forName(name);
      if (pgProperty != null) {
         this.setProperty(pgProperty, value);
      } else {
         throw new PSQLException(GT.tr("Unsupported property name: {0}", name), PSQLState.INVALID_PARAMETER_VALUE);
      }
   }

   @Nullable
   public String getProperty(PGProperty property) {
      return property.getOrDefault(this.properties);
   }

   public void setProperty(PGProperty property, @Nullable String value) {
      if (value != null) {
         switch(property) {
         case PG_HOST:
            this.setServerNames(value.split(","));
            break;
         case PG_PORT:
            String[] ps = value.split(",");
            int[] ports = new int[ps.length];

            for(int i = 0; i < ps.length; ++i) {
               try {
                  ports[i] = Integer.parseInt(ps[i]);
               } catch (NumberFormatException var7) {
                  ports[i] = 0;
               }
            }

            this.setPortNumbers(ports);
            break;
         case PG_DBNAME:
            this.setDatabaseName(value);
            break;
         case USER:
            this.setUser(value);
            break;
         case PASSWORD:
            this.setPassword(value);
            break;
         default:
            this.properties.setProperty(property.getName(), value);
         }

      }
   }

   protected Reference createReference() {
      return new Reference(this.getClass().getName(), PGObjectFactory.class.getName(), (String)null);
   }

   public Reference getReference() throws NamingException {
      Reference ref = this.createReference();
      StringBuilder serverString = new StringBuilder();

      for(int i = 0; i < this.serverNames.length; ++i) {
         if (i > 0) {
            serverString.append(",");
         }

         String serverName = this.serverNames[i];
         serverString.append(serverName);
      }

      ref.add(new StringRefAddr("serverName", serverString.toString()));
      StringBuilder portString = new StringBuilder();

      int p;
      for(int i = 0; i < this.portNumbers.length; ++i) {
         if (i > 0) {
            portString.append(",");
         }

         p = this.portNumbers[i];
         portString.append(Integer.toString(p));
      }

      ref.add(new StringRefAddr("portNumber", portString.toString()));
      ref.add(new StringRefAddr("databaseName", this.databaseName));
      if (this.user != null) {
         ref.add(new StringRefAddr("user", this.user));
      }

      if (this.password != null) {
         ref.add(new StringRefAddr("password", this.password));
      }

      PGProperty[] var11 = PGProperty.values();
      p = var11.length;

      for(int var6 = 0; var6 < p; ++var6) {
         PGProperty property = var11[var6];
         if (property.isPresent(this.properties)) {
            String value = (String)Nullness.castNonNull(property.getOrDefault(this.properties));
            ref.add(new StringRefAddr(property.getName(), value));
         }
      }

      return ref;
   }

   public void setFromReference(Reference ref) {
      this.databaseName = getReferenceProperty(ref, "databaseName");
      String portNumberString = getReferenceProperty(ref, "portNumber");
      int i;
      if (portNumberString != null) {
         String[] ps = portNumberString.split(",");
         int[] ports = new int[ps.length];

         for(i = 0; i < ps.length; ++i) {
            try {
               ports[i] = Integer.parseInt(ps[i]);
            } catch (NumberFormatException var8) {
               ports[i] = 0;
            }
         }

         this.setPortNumbers(ports);
      } else {
         this.setPortNumbers((int[])null);
      }

      String serverName = (String)Nullness.castNonNull(getReferenceProperty(ref, "serverName"));
      this.setServerNames(serverName.split(","));
      PGProperty[] var10 = PGProperty.values();
      i = var10.length;

      for(int var6 = 0; var6 < i; ++var6) {
         PGProperty property = var10[var6];
         this.setProperty(property, getReferenceProperty(ref, property.getName()));
      }

   }

   @Nullable
   private static String getReferenceProperty(Reference ref, String propertyName) {
      RefAddr addr = ref.get(propertyName);
      return addr == null ? null : (String)addr.getContent();
   }

   protected void writeBaseObject(ObjectOutputStream out) throws IOException {
      out.writeObject(this.serverNames);
      out.writeObject(this.databaseName);
      out.writeObject(this.user);
      out.writeObject(this.password);
      out.writeObject(this.portNumbers);
      out.writeObject(this.properties);
   }

   protected void readBaseObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
      this.serverNames = (String[])in.readObject();
      this.databaseName = (String)in.readObject();
      this.user = (String)in.readObject();
      this.password = (String)in.readObject();
      this.portNumbers = (int[])in.readObject();
      this.properties = (Properties)in.readObject();
   }

   public void initializeFrom(BaseDataSource source) throws IOException, ClassNotFoundException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      source.writeBaseObject(oos);
      oos.close();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      ObjectInputStream ois = new ObjectInputStream(bais);
      this.readBaseObject(ois);
   }

   public PreferQueryMode getPreferQueryMode() {
      return PreferQueryMode.of((String)Nullness.castNonNull(PGProperty.PREFER_QUERY_MODE.getOrDefault(this.properties)));
   }

   public void setPreferQueryMode(PreferQueryMode preferQueryMode) {
      PGProperty.PREFER_QUERY_MODE.set(this.properties, preferQueryMode.value());
   }

   public AutoSave getAutosave() {
      return AutoSave.of((String)Nullness.castNonNull(PGProperty.AUTOSAVE.getOrDefault(this.properties)));
   }

   public void setAutosave(AutoSave autoSave) {
      PGProperty.AUTOSAVE.set(this.properties, autoSave.value());
   }

   public boolean getCleanupSavepoints() {
      return PGProperty.CLEANUP_SAVEPOINTS.getBoolean(this.properties);
   }

   public void setCleanupSavepoints(boolean cleanupSavepoints) {
      PGProperty.CLEANUP_SAVEPOINTS.set(this.properties, cleanupSavepoints);
   }

   public boolean getReWriteBatchedInserts() {
      return PGProperty.REWRITE_BATCHED_INSERTS.getBoolean(this.properties);
   }

   public void setReWriteBatchedInserts(boolean reWrite) {
      PGProperty.REWRITE_BATCHED_INSERTS.set(this.properties, reWrite);
   }

   public boolean getHideUnprivilegedObjects() {
      return PGProperty.HIDE_UNPRIVILEGED_OBJECTS.getBoolean(this.properties);
   }

   public void setHideUnprivilegedObjects(boolean hideUnprivileged) {
      PGProperty.HIDE_UNPRIVILEGED_OBJECTS.set(this.properties, hideUnprivileged);
   }

   @Nullable
   public String getMaxResultBuffer() {
      return PGProperty.MAX_RESULT_BUFFER.getOrDefault(this.properties);
   }

   public void setMaxResultBuffer(@Nullable String maxResultBuffer) {
      PGProperty.MAX_RESULT_BUFFER.set(this.properties, maxResultBuffer);
   }

   public boolean getAdaptiveFetch() {
      return PGProperty.ADAPTIVE_FETCH.getBoolean(this.properties);
   }

   public void setAdaptiveFetch(boolean adaptiveFetch) {
      PGProperty.ADAPTIVE_FETCH.set(this.properties, adaptiveFetch);
   }

   public int getAdaptiveFetchMaximum() {
      return PGProperty.ADAPTIVE_FETCH_MAXIMUM.getIntNoCheck(this.properties);
   }

   public void setAdaptiveFetchMaximum(int adaptiveFetchMaximum) {
      PGProperty.ADAPTIVE_FETCH_MAXIMUM.set(this.properties, adaptiveFetchMaximum);
   }

   public int getAdaptiveFetchMinimum() {
      return PGProperty.ADAPTIVE_FETCH_MINIMUM.getIntNoCheck(this.properties);
   }

   public void setAdaptiveFetchMinimum(int adaptiveFetchMinimum) {
      PGProperty.ADAPTIVE_FETCH_MINIMUM.set(this.properties, adaptiveFetchMinimum);
   }

   public Logger getParentLogger() {
      return Logger.getLogger("fr.xephi.authme.libs.org.postgresql");
   }

   public String getXmlFactoryFactory() {
      return (String)Nullness.castNonNull(PGProperty.XML_FACTORY_FACTORY.getOrDefault(this.properties));
   }

   public void setXmlFactoryFactory(@Nullable String xmlFactoryFactory) {
      PGProperty.XML_FACTORY_FACTORY.set(this.properties, xmlFactoryFactory);
   }

   public boolean isSsl() {
      return this.getSsl();
   }

   @Nullable
   public String getSslfactoryarg() {
      return this.getSslFactoryArg();
   }

   public void setSslfactoryarg(@Nullable String arg) {
      this.setSslFactoryArg(arg);
   }

   @Nullable
   public String getSslcert() {
      return this.getSslCert();
   }

   public void setSslcert(@Nullable String file) {
      this.setSslCert(file);
   }

   @Nullable
   public String getSslmode() {
      return this.getSslMode();
   }

   public void setSslmode(@Nullable String mode) {
      this.setSslMode(mode);
   }

   @Nullable
   public String getSslhostnameverifier() {
      return this.getSslHostnameVerifier();
   }

   public void setSslhostnameverifier(@Nullable String className) {
      this.setSslHostnameVerifier(className);
   }

   @Nullable
   public String getSslkey() {
      return this.getSslKey();
   }

   public void setSslkey(@Nullable String file) {
      this.setSslKey(file);
   }

   @Nullable
   public String getSslrootcert() {
      return this.getSslRootCert();
   }

   public void setSslrootcert(@Nullable String file) {
      this.setSslRootCert(file);
   }

   @Nullable
   public String getSslpasswordcallback() {
      return this.getSslPasswordCallback();
   }

   public void setSslpasswordcallback(@Nullable String className) {
      this.setSslPasswordCallback(className);
   }

   @Nullable
   public String getSslpassword() {
      return this.getSslPassword();
   }

   public void setSslpassword(String sslpassword) {
      this.setSslPassword(sslpassword);
   }

   public int getRecvBufferSize() {
      return this.getReceiveBufferSize();
   }

   public void setRecvBufferSize(int nbytes) {
      this.setReceiveBufferSize(nbytes);
   }

   public boolean isAllowEncodingChanges() {
      return this.getAllowEncodingChanges();
   }

   public boolean isLogUnclosedConnections() {
      return this.getLogUnclosedConnections();
   }

   public boolean isTcpKeepAlive() {
      return this.getTcpKeepAlive();
   }

   public boolean isReadOnly() {
      return this.getReadOnly();
   }

   public boolean isDisableColumnSanitiser() {
      return this.getDisableColumnSanitiser();
   }

   public boolean isLoadBalanceHosts() {
      return this.getLoadBalanceHosts();
   }

   public boolean isCleanupSavePoints() {
      return this.getCleanupSavepoints();
   }

   public void setCleanupSavePoints(boolean cleanupSavepoints) {
      this.setCleanupSavepoints(cleanupSavepoints);
   }

   public boolean isReWriteBatchedInserts() {
      return this.getReWriteBatchedInserts();
   }

   static {
      try {
         Class.forName("fr.xephi.authme.libs.org.postgresql.Driver");
      } catch (ClassNotFoundException var1) {
         throw new IllegalStateException("BaseDataSource is unable to load org.postgresql.Driver. Please check if you have proper PostgreSQL JDBC Driver jar on the classpath", var1);
      }
   }
}
