package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.export.HaMode;
import github.nighter.smartspawner.libs.mariadb.export.SslMode;
import github.nighter.smartspawner.libs.mariadb.plugin.Codec;
import github.nighter.smartspawner.libs.mariadb.plugin.CredentialPlugin;
import github.nighter.smartspawner.libs.mariadb.plugin.credential.CredentialPluginLoader;
import github.nighter.smartspawner.libs.mariadb.util.constants.CatalogTerm;
import github.nighter.smartspawner.libs.mariadb.util.constants.MetaExportedKeys;
import github.nighter.smartspawner.libs.mariadb.util.log.Logger;
import github.nighter.smartspawner.libs.mariadb.util.log.Loggers;
import github.nighter.smartspawner.libs.mariadb.util.options.OptionAliases;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

public class Configuration {
   private static final Logger logger = Loggers.getLogger(Configuration.class);
   private static final Set<String> EXCLUDED_FIELDS = new HashSet();
   private static final Set<String> SECURE_FIELDS;
   private static final Set<String> PROPERTIES_TO_SKIP;
   private static final Set<String> SENSITIVE_FIELDS;
   private static final String CATALOG_TERM = "CATALOG";
   private static final String SCHEMA_TERM = "SCHEMA";
   private static Codec<?>[] cachedCodecs = null;
   private String user;
   private String password;
   private String database;
   private List<HostAddress> addresses;
   private HaMode haMode;
   private String initialUrl;
   private Properties nonMappedOptions;
   private String timezone;
   private String connectionCollation;
   private String connectionTimeZone;
   private Boolean forceConnectionTimeZoneToSession;
   private boolean preserveInstants;
   private Boolean autocommit;
   private boolean useMysqlMetadata;
   private boolean nullDatabaseMeansCurrent;
   private CatalogTerm useCatalogTerm;
   private boolean createDatabaseIfNotExist;
   private boolean useLocalSessionState;
   private boolean returnMultiValuesGeneratedIds;
   private boolean jdbcCompliantTruncation;
   private boolean oldModeNoPrecisionTimestamp;
   private Boolean permitRedirect;
   private TransactionIsolation transactionIsolation;
   private int defaultFetchSize;
   private int maxQuerySizeToLog;
   private Integer maxAllowedPacket;
   private String geometryDefaultType;
   private String restrictedAuth;
   private String initSql;
   private boolean pinGlobalTxToPhysicalConnection;
   private boolean permitNoResults;
   private boolean cacheCodecs;
   private MetaExportedKeys metaExportedKeys;
   private String socketFactory;
   private int connectTimeout;
   private String pipe;
   private String localSocket;
   private boolean uuidAsString;
   private boolean tcpKeepAlive;
   private int tcpKeepIdle;
   private int tcpKeepCount;
   private int tcpKeepInterval;
   private boolean tcpAbortiveClose;
   private String localSocketAddress;
   private int socketTimeout;
   private boolean useReadAheadInput;
   private String tlsSocketType;
   private SslMode sslMode;
   private String serverSslCert;
   private String keyStore;
   private String trustStore;
   private String keyStorePassword;
   private String trustStorePassword;
   private String keyPassword;
   private String keyStoreType;
   private String trustStoreType;
   private String enabledSslCipherSuites;
   private String enabledSslProtocolSuites;
   private boolean fallbackToSystemKeyStore;
   private boolean fallbackToSystemTrustStore;
   private boolean allowMultiQueries;
   private boolean allowLocalInfile;
   private boolean rewriteBatchedStatements;
   private boolean useCompression;
   private boolean useAffectedRows;
   private boolean useBulkStmts;
   private boolean useBulkStmtsForInserts;
   private boolean disablePipeline;
   private boolean cachePrepStmts;
   private int prepStmtCacheSize;
   private boolean useServerPrepStmts;
   private CredentialPlugin credentialType;
   private String sessionVariables;
   private String connectionAttributes;
   private String servicePrincipalName;
   private boolean disconnectOnExpiredPasswords;
   private boolean blankTableNameMeta;
   private boolean tinyInt1isBit;
   private boolean transformedBitIsBoolean;
   private boolean yearIsDateType;
   private boolean dumpQueriesOnException;
   private boolean includeInnodbStatusInDeadlockExceptions;
   private boolean includeThreadDumpInDeadlockExceptions;
   private int retriesAllDown;
   private String galeraAllowedState;
   private boolean transactionReplay;
   private int transactionReplaySize;
   private boolean pool;
   private String poolName;
   private int maxPoolSize;
   private int minPoolSize;
   private int maxIdleTime;
   private boolean registerJmxPool;
   private int poolValidMinDelay;
   private boolean useResetConnection;
   private String serverRsaPublicKeyFile;
   private boolean allowPublicKeyRetrieval;
   private Codec<?>[] codecs;

   private Configuration(Configuration.Builder builder) {
      this.initializeBasicConfig(builder);
      this.initializeSslConfig(builder);
      this.initializeSocketConfig(builder);
      this.initializeTransactionConfig(builder);
      this.initializeDataTypeConfig(builder);
      this.initializeTimezoneConfig(builder);
      this.initializeQueryConfig(builder);
      this.initializeBulkConfig(builder);
      this.initializePipelineConfig(builder);
      this.initializeDatabaseConfig(builder);
      this.initializeExceptionConfig(builder);
      this.initializePoolConfig(builder);
      this.initializeSecurityConfig(builder);
      this.initializeAdditionalConfig(builder);
      this.configureHosts();
      this.validateConfiguration();
   }

   private void initializeBasicConfig(Configuration.Builder builder) {
      this.database = builder.database;
      this.addresses = builder._addresses;
      this.nonMappedOptions = builder._nonMappedOptions;
      this.haMode = builder._haMode != null ? builder._haMode : HaMode.NONE;
      this.credentialType = CredentialPluginLoader.get(builder.credentialType);
      this.user = builder.user;
      this.password = builder.password;
      this.metaExportedKeys = builder.metaExportedKeys != null ? MetaExportedKeys.from(builder.metaExportedKeys) : MetaExportedKeys.Auto;
   }

   private void initializeSslConfig(Configuration.Builder builder) {
      this.enabledSslProtocolSuites = builder.enabledSslProtocolSuites;
      this.fallbackToSystemKeyStore = builder.fallbackToSystemKeyStore == null || builder.fallbackToSystemKeyStore;
      this.fallbackToSystemTrustStore = builder.fallbackToSystemTrustStore == null || builder.fallbackToSystemTrustStore;
      this.serverSslCert = builder.serverSslCert;
      this.keyStore = builder.keyStore;
      this.trustStore = builder.trustStore;
      this.keyStorePassword = builder.keyStorePassword;
      this.trustStorePassword = builder.trustStorePassword;
      this.keyPassword = builder.keyPassword;
      this.keyStoreType = builder.keyStoreType;
      this.trustStoreType = builder.trustStoreType;
      if (this.credentialType != null && this.credentialType.mustUseSsl() && (builder.sslMode == null || SslMode.from(builder.sslMode) == SslMode.DISABLE)) {
         this.sslMode = SslMode.VERIFY_FULL;
      } else {
         this.sslMode = builder.sslMode != null ? SslMode.from(builder.sslMode) : SslMode.DISABLE;
      }

   }

   private void initializeSocketConfig(Configuration.Builder builder) {
      this.socketFactory = builder.socketFactory;
      this.connectTimeout = builder.connectTimeout != null ? builder.connectTimeout : (DriverManager.getLoginTimeout() > 0 ? DriverManager.getLoginTimeout() * 1000 : 30000);
      this.pipe = builder.pipe;
      this.localSocket = builder.localSocket;
      this.tcpKeepAlive = builder.tcpKeepAlive == null || builder.tcpKeepAlive;
      this.uuidAsString = builder.uuidAsString != null && builder.uuidAsString;
      this.tcpKeepIdle = builder.tcpKeepIdle != null ? builder.tcpKeepIdle : 0;
      this.tcpKeepCount = builder.tcpKeepCount != null ? builder.tcpKeepCount : 0;
      this.tcpKeepInterval = builder.tcpKeepInterval != null ? builder.tcpKeepInterval : 0;
      this.tcpAbortiveClose = builder.tcpAbortiveClose != null && builder.tcpAbortiveClose;
      this.localSocketAddress = builder.localSocketAddress;
      this.socketTimeout = builder.socketTimeout != null ? builder.socketTimeout : 0;
      this.useReadAheadInput = builder.useReadAheadInput != null && builder.useReadAheadInput;
      this.tlsSocketType = builder.tlsSocketType;
      this.useCompression = builder.useCompression != null && builder.useCompression;
   }

   private void initializeTransactionConfig(Configuration.Builder builder) {
      this.transactionIsolation = builder.transactionIsolation != null ? TransactionIsolation.from(builder.transactionIsolation) : null;
      this.enabledSslCipherSuites = builder.enabledSslCipherSuites;
      this.sessionVariables = builder.sessionVariables;
   }

   private void initializeDataTypeConfig(Configuration.Builder builder) {
      this.tinyInt1isBit = builder.tinyInt1isBit == null || builder.tinyInt1isBit;
      this.transformedBitIsBoolean = builder.transformedBitIsBoolean == null || builder.transformedBitIsBoolean;
      this.yearIsDateType = builder.yearIsDateType == null || builder.yearIsDateType;
   }

   private void initializeTimezoneConfig(Configuration.Builder builder) {
      this.timezone = builder.timezone;
      this.connectionTimeZone = builder.connectionTimeZone;
      this.connectionCollation = builder.connectionCollation;
      this.forceConnectionTimeZoneToSession = builder.forceConnectionTimeZoneToSession;
      this.preserveInstants = builder.preserveInstants != null && builder.preserveInstants;
   }

   private void initializeQueryConfig(Configuration.Builder builder) {
      this.dumpQueriesOnException = builder.dumpQueriesOnException != null && builder.dumpQueriesOnException;
      this.prepStmtCacheSize = builder.prepStmtCacheSize != null ? builder.prepStmtCacheSize : 250;
      this.useAffectedRows = builder.useAffectedRows != null && builder.useAffectedRows;
      this.rewriteBatchedStatements = builder.rewriteBatchedStatements != null && builder.rewriteBatchedStatements;
      if (this.rewriteBatchedStatements) {
         this.useServerPrepStmts = false;
      } else {
         this.useServerPrepStmts = builder.useServerPrepStmts != null && builder.useServerPrepStmts;
      }

      this.connectionAttributes = builder.connectionAttributes;
      this.allowLocalInfile = builder.allowLocalInfile == null || builder.allowLocalInfile;
      this.allowMultiQueries = builder.allowMultiQueries != null && builder.allowMultiQueries;
   }

   private void initializeBulkConfig(Configuration.Builder builder) {
      this.useBulkStmts = builder.useBulkStmts != null && builder.useBulkStmts;
      this.useBulkStmtsForInserts = builder.useBulkStmtsForInserts != null ? builder.useBulkStmtsForInserts : builder.useBulkStmts == null || builder.useBulkStmts;
   }

   private void initializePipelineConfig(Configuration.Builder builder) {
      this.disablePipeline = builder.disablePipeline != null && builder.disablePipeline;
      this.autocommit = builder.autocommit;
      this.useMysqlMetadata = builder.useMysqlMetadata != null && builder.useMysqlMetadata;
      this.nullDatabaseMeansCurrent = builder.nullDatabaseMeansCurrent != null && builder.nullDatabaseMeansCurrent;
   }

   private void initializeDatabaseConfig(Configuration.Builder builder) {
      if (builder.useCatalogTerm != null) {
         if (!"CATALOG".equalsIgnoreCase(builder.useCatalogTerm) && !"SCHEMA".equalsIgnoreCase(builder.useCatalogTerm)) {
            throw new IllegalArgumentException("useCatalogTerm can only have CATALOG/SCHEMA value, current set value is " + builder.useCatalogTerm);
         }

         this.useCatalogTerm = "CATALOG".equalsIgnoreCase(builder.useCatalogTerm) ? CatalogTerm.UseCatalog : CatalogTerm.UseSchema;
      } else {
         this.useCatalogTerm = CatalogTerm.UseCatalog;
      }

      this.createDatabaseIfNotExist = builder.createDatabaseIfNotExist != null && builder.createDatabaseIfNotExist;
      this.useLocalSessionState = builder.useLocalSessionState != null && builder.useLocalSessionState;
      this.returnMultiValuesGeneratedIds = builder.returnMultiValuesGeneratedIds != null && builder.returnMultiValuesGeneratedIds;
      this.jdbcCompliantTruncation = builder.jdbcCompliantTruncation == null || builder.jdbcCompliantTruncation;
      this.oldModeNoPrecisionTimestamp = builder.oldModeNoPrecisionTimestamp != null && builder.oldModeNoPrecisionTimestamp;
      this.permitRedirect = builder.permitRedirect;
      this.pinGlobalTxToPhysicalConnection = builder.pinGlobalTxToPhysicalConnection != null && builder.pinGlobalTxToPhysicalConnection;
      this.permitNoResults = builder.permitNoResults == null || builder.permitNoResults;
      this.cacheCodecs = builder.cacheCodecs != null && builder.cacheCodecs;
      this.blankTableNameMeta = builder.blankTableNameMeta != null && builder.blankTableNameMeta;
      this.disconnectOnExpiredPasswords = builder.disconnectOnExpiredPasswords == null || builder.disconnectOnExpiredPasswords;
   }

   private void initializeExceptionConfig(Configuration.Builder builder) {
      this.includeInnodbStatusInDeadlockExceptions = builder.includeInnodbStatusInDeadlockExceptions != null && builder.includeInnodbStatusInDeadlockExceptions;
      this.includeThreadDumpInDeadlockExceptions = builder.includeThreadDumpInDeadlockExceptions != null && builder.includeThreadDumpInDeadlockExceptions;
   }

   private void initializePoolConfig(Configuration.Builder builder) {
      this.pool = builder.pool != null && builder.pool;
      this.poolName = builder.poolName;
      this.maxPoolSize = builder.maxPoolSize != null ? builder.maxPoolSize : 8;
      this.minPoolSize = builder.minPoolSize != null ? builder.minPoolSize : this.maxPoolSize;
      this.maxIdleTime = builder.maxIdleTime != null ? builder.maxIdleTime : 600000;
      this.registerJmxPool = builder.registerJmxPool == null || builder.registerJmxPool;
      this.poolValidMinDelay = builder.poolValidMinDelay != null ? builder.poolValidMinDelay : 1000;
      this.useResetConnection = builder.useResetConnection != null && builder.useResetConnection;
   }

   private void initializeSecurityConfig(Configuration.Builder builder) {
      this.serverRsaPublicKeyFile = builder.serverRsaPublicKeyFile != null && !builder.serverRsaPublicKeyFile.isEmpty() ? builder.serverRsaPublicKeyFile : null;
      this.allowPublicKeyRetrieval = builder.allowPublicKeyRetrieval != null && builder.allowPublicKeyRetrieval;
   }

   private void initializeAdditionalConfig(Configuration.Builder builder) {
      this.servicePrincipalName = builder.servicePrincipalName;
      this.defaultFetchSize = builder.defaultFetchSize != null ? builder.defaultFetchSize : 0;
      this.tlsSocketType = builder.tlsSocketType;
      this.maxQuerySizeToLog = builder.maxQuerySizeToLog != null ? builder.maxQuerySizeToLog : 1024;
      this.maxAllowedPacket = builder.maxAllowedPacket;
      this.retriesAllDown = builder.retriesAllDown != null ? builder.retriesAllDown : 120;
      this.galeraAllowedState = builder.galeraAllowedState;
      this.cachePrepStmts = builder.cachePrepStmts == null || builder.cachePrepStmts;
      this.transactionReplay = builder.transactionReplay != null && builder.transactionReplay;
      this.transactionReplaySize = builder.transactionReplaySize != null ? builder.transactionReplaySize : 64;
      this.geometryDefaultType = builder.geometryDefaultType;
      this.restrictedAuth = builder.restrictedAuth;
      this.initSql = builder.initSql;
      this.codecs = null;
   }

   private void configureHosts() {
      Iterator var2;
      HostAddress host;
      if (this.addresses.isEmpty()) {
         if (this.localSocket != null) {
            this.addresses.add(HostAddress.localSocket(this.localSocket));
         } else if (this.pipe != null) {
            this.addresses.add(HostAddress.pipe(this.pipe));
         }
      } else {
         ArrayList newAddresses;
         if (this.localSocket != null) {
            newAddresses = new ArrayList();
            var2 = this.addresses.iterator();

            while(var2.hasNext()) {
               host = (HostAddress)var2.next();
               newAddresses.add(host.withLocalSocket(this.localSocket));
            }

            this.addresses = newAddresses;
         }

         if (this.pipe != null) {
            newAddresses = new ArrayList();
            var2 = this.addresses.iterator();

            while(var2.hasNext()) {
               host = (HostAddress)var2.next();
               newAddresses.add(host.withPipe(this.pipe));
            }

            this.addresses = newAddresses;
         }
      }

      boolean first = true;

      for(var2 = this.addresses.iterator(); var2.hasNext(); first = false) {
         host = (HostAddress)var2.next();
         boolean primary = this.haMode != HaMode.REPLICATION || first;
         if (host.primary == null) {
            host.primary = primary;
         }
      }

   }

   private void validateConfiguration() {
      if (this.timezone != null && this.connectionTimeZone == null) {
         if ("disable".equalsIgnoreCase(this.timezone)) {
            this.forceConnectionTimeZoneToSession = false;
         } else {
            this.forceConnectionTimeZoneToSession = true;
            if (!"auto".equalsIgnoreCase(this.timezone)) {
               this.connectionTimeZone = this.timezone;
            }
         }
      }

      if (this.connectionCollation != null) {
         if (this.connectionCollation.trim().isEmpty()) {
            this.connectionCollation = null;
         } else {
            if (!this.connectionCollation.toLowerCase(Locale.ROOT).startsWith("utf8mb4_")) {
               throw new IllegalArgumentException(String.format("wrong connection collation '%s' only utf8mb4 collation are accepted", this.connectionCollation));
            }

            if (!this.connectionCollation.matches("\\w+$")) {
               throw new IllegalArgumentException(String.format("wrong connection collation '%s' name", this.connectionCollation));
            }
         }
      }

      this.validateIntegerFields();
   }

   private void validateIntegerFields() {
      Field[] fields = Configuration.class.getDeclaredFields();

      try {
         Field[] var2 = fields;
         int var3 = fields.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (field.getType().equals(Integer.TYPE)) {
               int val = field.getInt(this);
               if (val < 0) {
                  throw new IllegalArgumentException(String.format("Value for %s must be >= 1 (value is %s)", field.getName(), val));
               }
            }
         }
      } catch (IllegalAccessException var7) {
      }

   }

   public Configuration.Builder toBuilder() {
      Configuration.Builder builder = (new Configuration.Builder()).user(this.user).password(this.password).database(this.database).addresses(this.addresses == null ? null : (HostAddress[])this.addresses.toArray(new HostAddress[0])).haMode(this.haMode).timezone(this.timezone).connectionTimeZone(this.connectionTimeZone).connectionCollation(this.connectionCollation).forceConnectionTimeZoneToSession(this.forceConnectionTimeZoneToSession).preserveInstants(this.preserveInstants).autocommit(this.autocommit).useMysqlMetadata(this.useMysqlMetadata).nullDatabaseMeansCurrent(this.nullDatabaseMeansCurrent).useCatalogTerm(this.useCatalogTerm == CatalogTerm.UseCatalog ? "CATALOG" : "SCHEMA").createDatabaseIfNotExist(this.createDatabaseIfNotExist).useLocalSessionState(this.useLocalSessionState).returnMultiValuesGeneratedIds(this.returnMultiValuesGeneratedIds).jdbcCompliantTruncation(this.jdbcCompliantTruncation).oldModeNoPrecisionTimestamp(this.oldModeNoPrecisionTimestamp).permitRedirect(this.permitRedirect).pinGlobalTxToPhysicalConnection(this.pinGlobalTxToPhysicalConnection).permitNoResults(this.permitNoResults).cacheCodecs(this.cacheCodecs).transactionIsolation(this.transactionIsolation == null ? null : this.transactionIsolation.getValue()).metaExportedKeys(this.metaExportedKeys == null ? null : this.metaExportedKeys.name()).defaultFetchSize(this.defaultFetchSize).maxQuerySizeToLog(this.maxQuerySizeToLog).maxAllowedPacket(this.maxAllowedPacket).geometryDefaultType(this.geometryDefaultType).geometryDefaultType(this.geometryDefaultType).restrictedAuth(this.restrictedAuth).initSql(this.initSql).socketFactory(this.socketFactory).connectTimeout(this.connectTimeout).pipe(this.pipe).localSocket(this.localSocket).uuidAsString(this.uuidAsString).tcpKeepAlive(this.tcpKeepAlive).tcpKeepIdle(this.tcpKeepIdle).tcpKeepCount(this.tcpKeepCount).tcpKeepInterval(this.tcpKeepInterval).tcpAbortiveClose(this.tcpAbortiveClose).localSocketAddress(this.localSocketAddress).socketTimeout(this.socketTimeout).useReadAheadInput(this.useReadAheadInput).tlsSocketType(this.tlsSocketType).sslMode(this.sslMode.name()).serverSslCert(this.serverSslCert).keyStore(this.keyStore).trustStore(this.trustStore).keyStoreType(this.keyStoreType).keyStorePassword(this.keyStorePassword).trustStorePassword(this.trustStorePassword).keyPassword(this.keyPassword).trustStoreType(this.trustStoreType).enabledSslCipherSuites(this.enabledSslCipherSuites).enabledSslProtocolSuites(this.enabledSslProtocolSuites).fallbackToSystemKeyStore(this.fallbackToSystemKeyStore).fallbackToSystemTrustStore(this.fallbackToSystemTrustStore).allowMultiQueries(this.allowMultiQueries).allowLocalInfile(this.allowLocalInfile).useCompression(this.useCompression).useAffectedRows(this.useAffectedRows).useBulkStmts(this.useBulkStmts).useBulkStmtsForInserts(this.useBulkStmtsForInserts).disablePipeline(this.disablePipeline).cachePrepStmts(this.cachePrepStmts).prepStmtCacheSize(this.prepStmtCacheSize).useServerPrepStmts(this.useServerPrepStmts).credentialType(this.credentialType == null ? null : this.credentialType.type()).sessionVariables(this.sessionVariables).connectionAttributes(this.connectionAttributes).servicePrincipalName(this.servicePrincipalName).blankTableNameMeta(this.blankTableNameMeta).disconnectOnExpiredPasswords(this.disconnectOnExpiredPasswords).tinyInt1isBit(this.tinyInt1isBit).transformedBitIsBoolean(this.transformedBitIsBoolean).yearIsDateType(this.yearIsDateType).dumpQueriesOnException(this.dumpQueriesOnException).includeInnodbStatusInDeadlockExceptions(this.includeInnodbStatusInDeadlockExceptions).includeThreadDumpInDeadlockExceptions(this.includeThreadDumpInDeadlockExceptions).retriesAllDown(this.retriesAllDown).galeraAllowedState(this.galeraAllowedState).transactionReplay(this.transactionReplay).transactionReplaySize(this.transactionReplaySize).pool(this.pool).poolName(this.poolName).maxPoolSize(this.maxPoolSize).minPoolSize(this.minPoolSize).maxIdleTime(this.maxIdleTime).registerJmxPool(this.registerJmxPool).poolValidMinDelay(this.poolValidMinDelay).useResetConnection(this.useResetConnection).serverRsaPublicKeyFile(this.serverRsaPublicKeyFile).allowPublicKeyRetrieval(this.allowPublicKeyRetrieval);
      builder._nonMappedOptions = this.nonMappedOptions;
      return builder;
   }

   public static boolean acceptsUrl(String url) {
      return url != null && (url.startsWith("jdbc:mariadb:") || url.startsWith("jdbc:mysql:") && url.contains("permitMysqlScheme"));
   }

   public static Configuration parse(String url) throws SQLException {
      return parse(url, new Properties());
   }

   public static Configuration parse(String url, Properties prop) throws SQLException {
      return acceptsUrl(url) ? parseInternal(url, prop == null ? new Properties() : prop) : null;
   }

   private static Configuration parseInternal(String url, Properties properties) throws SQLException {
      try {
         Configuration.Builder builder = new Configuration.Builder();
         validateUrlFormat(url);
         int separator = url.indexOf("//");
         builder.haMode(parseHaMode(url, separator));
         String urlSecondPart = url.substring(separator + 2);
         int posToSkip = skipComplexAddresses(urlSecondPart);
         int dbIndex = urlSecondPart.indexOf("/", posToSkip);
         int paramIndex = urlSecondPart.indexOf("?");
         String hostAddressesString;
         String additionalParameters;
         if ((dbIndex >= paramIndex || dbIndex >= 0) && (dbIndex <= paramIndex || paramIndex <= -1)) {
            if (dbIndex >= paramIndex && dbIndex <= paramIndex) {
               hostAddressesString = urlSecondPart;
               additionalParameters = null;
            } else {
               hostAddressesString = urlSecondPart.substring(0, dbIndex);
               additionalParameters = urlSecondPart.substring(dbIndex);
            }
         } else {
            hostAddressesString = urlSecondPart.substring(0, paramIndex);
            additionalParameters = urlSecondPart.substring(paramIndex);
         }

         if (additionalParameters != null) {
            processDatabaseAndParameters(additionalParameters, builder, properties);
         } else {
            builder.database((String)null);
         }

         mapPropertiesToOption(builder, properties);
         builder._addresses = HostAddress.parse(hostAddressesString, builder._haMode);
         return builder.build();
      } catch (IllegalArgumentException var10) {
         throw new SQLException("error parsing url: " + var10.getMessage(), var10);
      }
   }

   private static void validateUrlFormat(String url) {
      int separator = url.indexOf("//");
      if (separator == -1) {
         throw new IllegalArgumentException("url parsing error : '//' is not present in the url " + url);
      }
   }

   private static int skipComplexAddresses(String urlSecondPart) {
      int posToSkip = 0;

      int skipPos;
      int endingBraceIndex;
      while((skipPos = urlSecondPart.indexOf("address=(", posToSkip)) > -1) {
         for(posToSkip = urlSecondPart.indexOf(")", skipPos) + 1; urlSecondPart.startsWith("(", posToSkip); posToSkip = endingBraceIndex + 1) {
            endingBraceIndex = urlSecondPart.indexOf(")", posToSkip);
            if (endingBraceIndex == -1) {
               break;
            }
         }
      }

      return posToSkip;
   }

   private static void processDatabaseAndParameters(String additionalParameters, Configuration.Builder builder, Properties properties) {
      int optIndex = additionalParameters.indexOf("?");
      String database;
      if (optIndex < 0) {
         database = additionalParameters.length() > 1 ? additionalParameters.substring(1) : null;
      } else {
         database = extractDatabase(additionalParameters, optIndex);
         processUrlParameters(additionalParameters.substring(optIndex + 1), properties);
      }

      builder.database(database);
   }

   private static String extractDatabase(String additionalParameters, int optIndex) {
      if (optIndex == 0) {
         return null;
      } else {
         String database = additionalParameters.substring(1, optIndex);
         return database.isEmpty() ? null : database;
      }
   }

   private static void processUrlParameters(String urlParameters, Properties properties) {
      if (!urlParameters.isEmpty()) {
         String[] parameters = urlParameters.split("&");
         String[] var3 = parameters;
         int var4 = parameters.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            String parameter = var3[var5];
            int pos = parameter.indexOf(61);
            if (pos == -1) {
               properties.setProperty(parameter, "");
            } else {
               properties.setProperty(parameter.substring(0, pos), parameter.substring(pos + 1));
            }
         }
      }

   }

   private static void mapPropertiesToOption(Configuration.Builder builder, Properties properties) {
      Properties nonMappedOptions = new Properties();

      try {
         processProperties(builder, properties, nonMappedOptions);
         handleLegacySslSettings(builder, nonMappedOptions);
         builder._nonMappedOptions = nonMappedOptions;
      } catch (ReflectiveOperationException var4) {
         throw new IllegalArgumentException("Unexpected error while mapping properties", var4);
      }
   }

   private static void processProperties(Configuration.Builder builder, Properties properties, Properties nonMappedOptions) throws ReflectiveOperationException {
      Iterator var3 = properties.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<Object, Object> entry = (Entry)var3.next();
         String realKey = getRealKey(entry.getKey().toString());
         Object propertyValue = entry.getValue();
         if (propertyValue != null) {
            processProperty(builder, realKey, propertyValue, entry.getKey(), nonMappedOptions);
         }
      }

   }

   private static String getRealKey(String key) {
      String lowercaseKey = key.toLowerCase(Locale.ROOT);
      String realKey = (String)OptionAliases.OPTIONS_ALIASES.get(lowercaseKey);
      return realKey != null ? realKey : key;
   }

   private static void processProperty(Configuration.Builder builder, String realKey, Object propertyValue, Object originalKey, Properties nonMappedOptions) throws ReflectiveOperationException {
      boolean used = false;
      Field[] var6 = Configuration.Builder.class.getDeclaredFields();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Field field = var6[var8];
         if (realKey.toLowerCase(Locale.ROOT).equals(field.getName().toLowerCase(Locale.ROOT))) {
            used = true;
            setFieldValue(builder, field, propertyValue, originalKey);
         }
      }

      if (!used) {
         nonMappedOptions.put(realKey, propertyValue);
      }

   }

   private static void setFieldValue(Configuration.Builder builder, Field field, Object propertyValue, Object originalKey) throws ReflectiveOperationException {
      if (field.getGenericType().equals(String.class)) {
         handleStringField(builder, field, propertyValue);
      } else if (field.getGenericType().equals(Boolean.class)) {
         handleBooleanField(builder, field, propertyValue, originalKey);
      } else if (field.getGenericType().equals(Integer.class)) {
         handleIntegerField(builder, field, propertyValue, originalKey);
      }

   }

   private static void handleStringField(Configuration.Builder builder, Field field, Object value) throws ReflectiveOperationException {
      String stringValue = value.toString();
      if (!stringValue.isEmpty()) {
         Method method = Configuration.Builder.class.getDeclaredMethod(field.getName(), String.class);
         method.invoke(builder, stringValue);
      }

   }

   private static void handleBooleanField(Configuration.Builder builder, Field field, Object value, Object originalKey) throws ReflectiveOperationException {
      Method method = Configuration.Builder.class.getDeclaredMethod(field.getName(), Boolean.class);
      String var5 = value.toString().toLowerCase();
      byte var6 = -1;
      switch(var5.hashCode()) {
      case 0:
         if (var5.equals("")) {
            var6 = 0;
         }
         break;
      case 48:
         if (var5.equals("0")) {
            var6 = 3;
         }
         break;
      case 49:
         if (var5.equals("1")) {
            var6 = 1;
         }
         break;
      case 3569038:
         if (var5.equals("true")) {
            var6 = 2;
         }
         break;
      case 97196323:
         if (var5.equals("false")) {
            var6 = 4;
         }
      }

      switch(var6) {
      case 0:
      case 1:
      case 2:
         method.invoke(builder, Boolean.TRUE);
         break;
      case 3:
      case 4:
         method.invoke(builder, Boolean.FALSE);
         break;
      default:
         throw new IllegalArgumentException(String.format("Optional parameter %s must be boolean (true/false or 0/1) was '%s'", originalKey, value));
      }

   }

   private static void handleIntegerField(Configuration.Builder builder, Field field, Object value, Object originalKey) throws ReflectiveOperationException {
      try {
         Method method = Configuration.Builder.class.getDeclaredMethod(field.getName(), Integer.class);
         Integer intValue = Integer.parseInt(value.toString());
         method.invoke(builder, intValue);
      } catch (NumberFormatException var6) {
         throw new IllegalArgumentException(String.format("Optional parameter %s must be Integer, was '%s'", originalKey, value));
      }
   }

   private static void handleLegacySslSettings(Configuration.Builder builder, Properties nonMappedOptions) {
      if (isSet("useSsl", nonMappedOptions) || isSet("useSSL", nonMappedOptions)) {
         Properties deprecatedDesc = new Properties();

         try {
            InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream("deprecated.properties");

            try {
               deprecatedDesc.load(inputStream);
               logger.warn(deprecatedDesc.getProperty("useSsl"));
               if (isSet("trustServerCertificate", nonMappedOptions)) {
                  builder.sslMode("trust");
                  logger.warn(deprecatedDesc.getProperty("trustServerCertificate"));
               } else if (isSet("disableSslHostnameVerification", nonMappedOptions)) {
                  logger.warn(deprecatedDesc.getProperty("disableSslHostnameVerification"));
                  builder.sslMode("verify-ca");
               } else {
                  builder.sslMode("verify-full");
               }
            } catch (Throwable var7) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException var8) {
         }
      }

   }

   private static boolean isSet(String key, Properties nonMappedOptions) {
      String value = nonMappedOptions.getProperty(key);
      return value != null && (value.equals("1") || value.equals("true") || value.isEmpty());
   }

   private static HaMode parseHaMode(String url, int separator) {
      int firstColonPos = url.indexOf(58);
      int secondColonPos = url.indexOf(58, firstColonPos + 1);
      int thirdColonPos = url.indexOf(58, secondColonPos + 1);
      if (thirdColonPos > separator || thirdColonPos == -1) {
         if (secondColonPos == separator - 1) {
            return HaMode.NONE;
         }

         thirdColonPos = separator;
      }

      try {
         String haModeString = url.substring(secondColonPos + 1, thirdColonPos);
         if ("FAILOVER".equalsIgnoreCase(haModeString)) {
            haModeString = "LOADBALANCE";
         }

         return HaMode.from(haModeString);
      } catch (IllegalArgumentException var6) {
         throw new IllegalArgumentException("wrong failover parameter format in connection String " + url);
      }
   }

   public static String toConf(String url) throws SQLException {
      Configuration conf = parseInternal(url, new Properties());
      Configuration defaultConf = parse("jdbc:mariadb://localhost/");
      StringBuilder result = new StringBuilder();
      appendBasicConfiguration(result, conf);
      appendUnknownOptions(result, conf);
      appendNonDefaultOptions(result, conf, defaultConf);
      appendDefaultOptions(result, conf, defaultConf);
      return result.toString();
   }

   private static void appendBasicConfiguration(StringBuilder sb, Configuration conf) {
      sb.append("Configuration:\n * resulting Url : ").append(conf.initialUrl);
   }

   private static void appendUnknownOptions(StringBuilder sb, Configuration conf) {
      sb.append("\nUnknown options : ");
      if (conf.nonMappedOptions.isEmpty()) {
         sb.append("None\n");
      } else {
         conf.nonMappedOptions.entrySet().stream().map((entry) -> {
            return new SimpleEntry(entry.getKey().toString(), entry.getValue() != null ? entry.getValue().toString() : "");
         }).sorted(Entry.comparingByKey()).forEach((entry) -> {
            sb.append("\n * ").append((String)entry.getKey()).append(" : ").append((String)entry.getValue());
         });
         sb.append("\n");
      }
   }

   private static void appendNonDefaultOptions(StringBuilder sb, Configuration conf, Configuration defaultConf) {
      try {
         StringBuilder diffOpts = new StringBuilder();
         processFields(conf, defaultConf, new StringBuilder(), diffOpts);
         sb.append("\nNon default options : ");
         if (diffOpts.length() == 0) {
            sb.append("None\n");
         } else {
            sb.append(diffOpts);
         }

      } catch (IllegalAccessException var4) {
         throw new IllegalArgumentException("Error processing non-default options", var4);
      }
   }

   private static void appendDefaultOptions(StringBuilder sb, Configuration conf, Configuration defaultConf) {
      try {
         StringBuilder defaultOpts = new StringBuilder();
         processFields(conf, defaultConf, defaultOpts, new StringBuilder());
         sb.append("\n\ndefault options :");
         if (defaultOpts.length() == 0) {
            sb.append("None\n");
         } else {
            sb.append(defaultOpts);
         }

      } catch (IllegalAccessException var4) {
         throw new IllegalArgumentException("Error processing default options", var4);
      }
   }

   private static void processFields(Configuration conf, Configuration defaultConf, StringBuilder defaultOpts, StringBuilder diffOpts) throws IllegalAccessException {
      Field[] fields = Configuration.class.getDeclaredFields();
      Arrays.sort(fields, Comparator.comparing(Field::getName));
      Field[] var5 = fields;
      int var6 = fields.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         Field field = var5[var7];
         if (!PROPERTIES_TO_SKIP.contains(field.getName())) {
            Object fieldValue = field.get(conf);
            Object defaultValue = field.get(defaultConf);
            processField(field, fieldValue, defaultValue, defaultOpts, diffOpts);
         }
      }

   }

   private static void processField(Field field, Object fieldValue, Object defaultValue, StringBuilder defaultOpts, StringBuilder diffOpts) {
      if (fieldValue == null) {
         appendNullField(field, defaultValue, defaultOpts, diffOpts);
      } else if (field.getName().equals("haMode")) {
         appendHaModeField(field, fieldValue, defaultValue, defaultOpts, diffOpts);
      } else {
         String typeName = fieldValue.getClass().getSimpleName();
         byte var7 = -1;
         switch(typeName.hashCode()) {
         case -2141683108:
            if (typeName.equals("HaMode")) {
               var7 = 2;
            }
            break;
         case -1932797868:
            if (typeName.equals("HashSet")) {
               var7 = 10;
            }
            break;
         case -1808118735:
            if (typeName.equals("String")) {
               var7 = 0;
            }
            break;
         case -705177168:
            if (typeName.equals("TransactionIsolation")) {
               var7 = 3;
            }
            break;
         case -672261858:
            if (typeName.equals("Integer")) {
               var7 = 5;
            }
            break;
         case -252109393:
            if (typeName.equals("SslMode")) {
               var7 = 6;
            }
            break;
         case -116419924:
            if (typeName.equals("MetaExportedKeys")) {
               var7 = 4;
            }
            break;
         case 578806391:
            if (typeName.equals("ArrayList")) {
               var7 = 8;
            }
            break;
         case 660671493:
            if (typeName.equals("CatalogTerm")) {
               var7 = 7;
            }
            break;
         case 1067411795:
            if (typeName.equals("Properties")) {
               var7 = 9;
            }
            break;
         case 1729365000:
            if (typeName.equals("Boolean")) {
               var7 = 1;
            }
         }

         switch(var7) {
         case 0:
         case 1:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
         case 7:
            appendSimpleField(field, fieldValue, defaultValue, defaultOpts, diffOpts);
            break;
         case 8:
            appendListField(field, fieldValue, defaultValue, defaultOpts, diffOpts);
         case 9:
         case 10:
            break;
         default:
            throw new IllegalArgumentException("Unexpected field type for: " + field.getName());
         }

      }
   }

   private static void appendNullField(Field field, Object defaultValue, StringBuilder defaultOpts, StringBuilder diffOpts) {
      StringBuilder target = defaultValue == null ? defaultOpts : diffOpts;
      target.append("\n * ").append(field.getName()).append(" : null");
   }

   private static void appendHaModeField(Field field, Object fieldValue, Object defaultValue, StringBuilder defaultOpts, StringBuilder diffOpts) {
      StringBuilder target = Objects.equals(fieldValue, defaultValue) ? defaultOpts : diffOpts;
      target.append("\n * ").append(field.getName()).append(" : ").append(fieldValue);
   }

   private static void appendSimpleField(Field field, Object fieldValue, Object defaultValue, StringBuilder defaultOpts, StringBuilder diffOpts) {
      StringBuilder target = Objects.equals(fieldValue, defaultValue) ? defaultOpts : diffOpts;
      target.append("\n * ").append(field.getName()).append(" : ");
      if (SENSITIVE_FIELDS.contains(field.getName())) {
         target.append("***");
      } else {
         target.append(fieldValue);
      }

   }

   private static void appendListField(Field field, Object fieldValue, Object defaultValue, StringBuilder defaultOpts, StringBuilder diffOpts) {
      StringBuilder target = Objects.equals(fieldValue.toString(), defaultValue.toString()) ? defaultOpts : diffOpts;
      target.append("\n * ").append(field.getName()).append(" : ").append(fieldValue);
   }

   protected static String buildUrl(Configuration conf) {
      try {
         StringBuilder urlBuilder = new StringBuilder("jdbc:mariadb:");
         appendHaModeIfPresent(urlBuilder, conf);
         appendHostAddresses(urlBuilder, conf);
         appendDatabase(urlBuilder, conf);
         appendConfigurationParameters(urlBuilder, conf);
         conf.loadCodecs();
         return urlBuilder.toString();
      } catch (SecurityException var2) {
         throw new IllegalArgumentException("Security too restrictive: " + var2.getMessage());
      }
   }

   private static void appendHostAddresses(StringBuilder sb, Configuration conf) {
      sb.append("//");

      for(int i = 0; i < conf.addresses.size(); ++i) {
         if (i > 0) {
            sb.append(",");
         }

         appendHostAddress(sb, conf, (HostAddress)conf.addresses.get(i), i);
      }

      sb.append("/");
   }

   private static void appendHostAddress(StringBuilder sb, Configuration conf, HostAddress hostAddress, int index) {
      boolean useSimpleFormat = shouldUseSimpleHostFormat(conf, hostAddress, index);
      if (useSimpleFormat) {
         sb.append(hostAddress.host);
         if (hostAddress.port != 3306) {
            sb.append(":").append(hostAddress.port);
         }
      } else {
         sb.append(hostAddress);
      }

   }

   private static boolean shouldUseSimpleHostFormat(Configuration conf, HostAddress hostAddress, int index) {
      return conf.haMode == HaMode.NONE && hostAddress.primary || conf.haMode == HaMode.REPLICATION && (index == 0 && hostAddress.primary || index != 0 && !hostAddress.primary);
   }

   private static void appendDatabase(StringBuilder sb, Configuration conf) {
      if (conf.database != null) {
         sb.append(conf.database);
      }

   }

   private static void appendHaModeIfPresent(StringBuilder sb, Configuration conf) {
      if (conf.haMode != HaMode.NONE) {
         sb.append(conf.haMode.toString().toLowerCase(Locale.ROOT).replace("_", "-")).append(":");
      }

   }

   private static void appendConfigurationParameters(StringBuilder sb, Configuration conf) {
      try {
         Configuration defaultConf = new Configuration(new Configuration.Builder());
         Configuration.ParameterAppender paramAppender = new Configuration.ParameterAppender(sb);
         Field[] var4 = Configuration.class.getDeclaredFields();
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            if (!EXCLUDED_FIELDS.contains(field.getName())) {
               Object value = field.get(conf);
               if (value != null && (!(value instanceof Properties) || !((Properties)value).isEmpty())) {
                  appendFieldParameter(paramAppender, field, value, defaultConf);
               }
            }
         }

      } catch (IllegalAccessException var9) {
         throw new IllegalStateException(var9);
      }
   }

   private static void appendFieldParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) throws IllegalAccessException {
      if (SECURE_FIELDS.contains(field.getName())) {
         appender.appendParameter(field.getName(), "***");
      } else {
         Class<?> fieldType = field.getType();
         if (fieldType.equals(String.class)) {
            appendStringParameter(appender, field, value, defaultConf);
         } else if (fieldType.equals(Boolean.TYPE)) {
            appendBooleanParameter(appender, field, value, defaultConf);
         } else if (fieldType.equals(Integer.TYPE)) {
            appendIntParameter(appender, field, value, defaultConf);
         } else if (fieldType.equals(Properties.class)) {
            appendPropertiesParameter(appender, (Properties)value);
         } else if (fieldType.equals(CatalogTerm.class)) {
            appendCatalogTermParameter(appender, field, value, defaultConf);
         } else if (fieldType.equals(CredentialPlugin.class)) {
            appendCredentialPluginParameter(appender, field, value, defaultConf);
         } else {
            appendDefaultParameter(appender, field, value, defaultConf);
         }

      }
   }

   private static void appendStringParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) throws IllegalAccessException {
      String defaultValue = (String)field.get(defaultConf);
      if (!value.equals(defaultValue)) {
         appender.appendParameter(field.getName(), (String)value);
      }

   }

   private static void appendBooleanParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) throws IllegalAccessException {
      boolean defaultValue = field.getBoolean(defaultConf);
      if (!value.equals(defaultValue)) {
         appender.appendParameter(field.getName(), value.toString());
      }

   }

   private static void appendIntParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) {
      try {
         int defaultValue = field.getInt(defaultConf);
         if (!value.equals(defaultValue)) {
            appender.appendParameter(field.getName(), value.toString());
         }
      } catch (IllegalAccessException var5) {
      }

   }

   private static void appendPropertiesParameter(Configuration.ParameterAppender appender, Properties props) {
      Iterator var2 = props.keySet().iterator();

      while(var2.hasNext()) {
         Object key = var2.next();
         appender.appendParameter(key.toString(), props.get(key).toString());
      }

   }

   private static void appendCatalogTermParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) throws IllegalAccessException {
      Object defaultValue = field.get(defaultConf);
      if (!value.equals(defaultValue)) {
         appender.appendParameter(field.getName(), "SCHEMA");
      }

   }

   private static void appendCredentialPluginParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) throws IllegalAccessException {
      Object defaultValue = field.get(defaultConf);
      if (!value.equals(defaultValue)) {
         appender.appendParameter(field.getName(), ((CredentialPlugin)value).type());
      }

   }

   private static void appendDefaultParameter(Configuration.ParameterAppender appender, Field field, Object value, Configuration defaultConf) throws IllegalAccessException {
      Object defaultValue = field.get(defaultConf);
      if (!value.equals(defaultValue)) {
         appender.appendParameter(field.getName(), value.toString());
      }

   }

   private static String nullOrEmpty(String val) {
      return val != null && !val.isEmpty() ? val : null;
   }

   public Configuration clone(String username, String password) {
      return this.toBuilder().user(username != null && username.isEmpty() ? null : username).password(password != null && password.isEmpty() ? null : password).build();
   }

   public boolean havePrimaryHostOnly() {
      Iterator var1 = this.addresses.iterator();

      HostAddress hostAddress;
      do {
         if (!var1.hasNext()) {
            return true;
         }

         hostAddress = (HostAddress)var1.next();
      } while(hostAddress.primary);

      return false;
   }

   public String database() {
      return this.database;
   }

   public List<HostAddress> addresses() {
      return this.addresses;
   }

   public HaMode haMode() {
      return this.haMode;
   }

   public CredentialPlugin credentialPlugin() {
      return this.credentialType;
   }

   public String user() {
      return this.user;
   }

   public String password() {
      return this.password;
   }

   public String initialUrl() {
      return this.initialUrl;
   }

   public String serverSslCert() {
      return this.serverSslCert;
   }

   public String keyStore() {
      return this.keyStore;
   }

   public String trustStore() {
      return this.trustStore;
   }

   public String keyStorePassword() {
      return this.keyStorePassword;
   }

   public String trustStorePassword() {
      return this.trustStorePassword;
   }

   public String keyPassword() {
      return this.keyPassword;
   }

   public String keyStoreType() {
      return this.keyStoreType;
   }

   public String trustStoreType() {
      return this.trustStoreType;
   }

   public String enabledSslProtocolSuites() {
      return this.enabledSslProtocolSuites;
   }

   public String credentialType() {
      return this.credentialType == null ? null : this.credentialType.type();
   }

   public boolean fallbackToSystemKeyStore() {
      return this.fallbackToSystemKeyStore;
   }

   public boolean fallbackToSystemTrustStore() {
      return this.fallbackToSystemTrustStore;
   }

   public String socketFactory() {
      return this.socketFactory;
   }

   public int connectTimeout() {
      return this.connectTimeout;
   }

   public Configuration connectTimeout(int connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
   }

   public String pipe() {
      return this.pipe;
   }

   public String localSocket() {
      return this.localSocket;
   }

   public boolean tcpKeepAlive() {
      return this.tcpKeepAlive;
   }

   public boolean uuidAsString() {
      return this.uuidAsString;
   }

   public int tcpKeepIdle() {
      return this.tcpKeepIdle;
   }

   public int tcpKeepCount() {
      return this.tcpKeepCount;
   }

   public int tcpKeepInterval() {
      return this.tcpKeepInterval;
   }

   public boolean tcpAbortiveClose() {
      return this.tcpAbortiveClose;
   }

   public String localSocketAddress() {
      return this.localSocketAddress;
   }

   public int socketTimeout() {
      return this.socketTimeout;
   }

   public boolean allowMultiQueries() {
      return this.allowMultiQueries;
   }

   public boolean allowLocalInfile() {
      return this.allowLocalInfile;
   }

   public boolean useCompression() {
      return this.useCompression;
   }

   public boolean blankTableNameMeta() {
      return this.blankTableNameMeta;
   }

   public boolean disconnectOnExpiredPasswords() {
      return this.disconnectOnExpiredPasswords;
   }

   public SslMode sslMode() {
      return this.sslMode;
   }

   public TransactionIsolation transactionIsolation() {
      return this.transactionIsolation;
   }

   public MetaExportedKeys metaExportedKeys() {
      return this.metaExportedKeys;
   }

   public String enabledSslCipherSuites() {
      return this.enabledSslCipherSuites;
   }

   public String sessionVariables() {
      return this.sessionVariables;
   }

   public boolean tinyInt1isBit() {
      return this.tinyInt1isBit;
   }

   public boolean transformedBitIsBoolean() {
      return this.transformedBitIsBoolean;
   }

   public boolean yearIsDateType() {
      return this.yearIsDateType;
   }

   public String timezone() {
      return this.timezone;
   }

   public String connectionTimeZone() {
      return this.connectionTimeZone;
   }

   public String connectionCollation() {
      return this.connectionCollation;
   }

   public Boolean forceConnectionTimeZoneToSession() {
      return this.forceConnectionTimeZoneToSession;
   }

   public boolean preserveInstants() {
      return this.preserveInstants;
   }

   public boolean dumpQueriesOnException() {
      return this.dumpQueriesOnException;
   }

   public int prepStmtCacheSize() {
      return this.prepStmtCacheSize;
   }

   public boolean useAffectedRows() {
      return this.useAffectedRows;
   }

   public boolean rewriteBatchedStatements() {
      return this.rewriteBatchedStatements;
   }

   public boolean useServerPrepStmts() {
      return this.useServerPrepStmts;
   }

   public String connectionAttributes() {
      return this.connectionAttributes;
   }

   public boolean useBulkStmts() {
      return this.useBulkStmts;
   }

   public boolean useBulkStmtsForInserts() {
      return this.useBulkStmtsForInserts;
   }

   public boolean disablePipeline() {
      return this.disablePipeline;
   }

   public Boolean autocommit() {
      return this.autocommit;
   }

   public boolean useMysqlMetadata() {
      return this.useMysqlMetadata;
   }

   public boolean nullDatabaseMeansCurrent() {
      return this.nullDatabaseMeansCurrent;
   }

   public CatalogTerm useCatalogTerm() {
      return this.useCatalogTerm;
   }

   public boolean createDatabaseIfNotExist() {
      return this.createDatabaseIfNotExist;
   }

   public boolean useLocalSessionState() {
      return this.useLocalSessionState;
   }

   public boolean returnMultiValuesGeneratedIds() {
      return this.returnMultiValuesGeneratedIds;
   }

   public boolean jdbcCompliantTruncation() {
      return this.jdbcCompliantTruncation;
   }

   public boolean oldModeNoPrecisionTimestamp() {
      return this.oldModeNoPrecisionTimestamp;
   }

   public Boolean permitRedirect() {
      return this.permitRedirect;
   }

   public boolean pinGlobalTxToPhysicalConnection() {
      return this.pinGlobalTxToPhysicalConnection;
   }

   public boolean permitNoResults() {
      return this.permitNoResults;
   }

   public boolean includeInnodbStatusInDeadlockExceptions() {
      return this.includeInnodbStatusInDeadlockExceptions;
   }

   public boolean includeThreadDumpInDeadlockExceptions() {
      return this.includeThreadDumpInDeadlockExceptions;
   }

   public String servicePrincipalName() {
      return this.servicePrincipalName;
   }

   public int defaultFetchSize() {
      return this.defaultFetchSize;
   }

   public Properties nonMappedOptions() {
      return this.nonMappedOptions;
   }

   public String tlsSocketType() {
      return this.tlsSocketType;
   }

   public int maxQuerySizeToLog() {
      return this.maxQuerySizeToLog;
   }

   public Integer maxAllowedPacket() {
      return this.maxAllowedPacket;
   }

   public int retriesAllDown() {
      return this.retriesAllDown;
   }

   public String galeraAllowedState() {
      return this.galeraAllowedState;
   }

   public boolean pool() {
      return this.pool;
   }

   public String poolName() {
      return this.poolName;
   }

   public int maxPoolSize() {
      return this.maxPoolSize;
   }

   public int minPoolSize() {
      return this.minPoolSize;
   }

   public int maxIdleTime() {
      return this.maxIdleTime;
   }

   public boolean registerJmxPool() {
      return this.registerJmxPool;
   }

   public int poolValidMinDelay() {
      return this.poolValidMinDelay;
   }

   public boolean useResetConnection() {
      return this.useResetConnection;
   }

   public String serverRsaPublicKeyFile() {
      return this.serverRsaPublicKeyFile;
   }

   public boolean allowPublicKeyRetrieval() {
      return this.allowPublicKeyRetrieval;
   }

   public boolean useReadAheadInput() {
      return this.useReadAheadInput;
   }

   public boolean cachePrepStmts() {
      return this.cachePrepStmts;
   }

   public boolean transactionReplay() {
      return this.transactionReplay;
   }

   public int transactionReplaySize() {
      return this.transactionReplaySize;
   }

   public String geometryDefaultType() {
      return this.geometryDefaultType;
   }

   public String restrictedAuth() {
      return this.restrictedAuth;
   }

   public String initSql() {
      return this.initSql;
   }

   public Codec<?>[] codecs() {
      return this.codecs;
   }

   public String toString() {
      return this.initialUrl;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Configuration that = (Configuration)o;
         if (this.password == null) {
            return this.initialUrl.equals(that.initialUrl) && that.password == null;
         } else {
            return this.initialUrl.equals(that.initialUrl) && this.password.equals(that.password);
         }
      } else {
         return false;
      }
   }

   private void loadCodecs() {
      if (this.cacheCodecs && cachedCodecs != null) {
         this.codecs = cachedCodecs;
      } else {
         ServiceLoader<Codec> loader = ServiceLoader.load(Codec.class, Configuration.class.getClassLoader());
         List<Codec<?>> result = new ArrayList();
         Iterator var10000 = loader.iterator();
         Objects.requireNonNull(result);
         var10000.forEachRemaining(result::add);
         this.codecs = (Codec[])result.toArray(new Codec[0]);
         if (this.cacheCodecs) {
            Class var3 = Configuration.class;
            synchronized(Configuration.class) {
               if (cachedCodecs == null) {
                  cachedCodecs = this.codecs;
               }
            }
         }

      }
   }

   public int hashCode() {
      return this.initialUrl.hashCode();
   }

   // $FF: synthetic method
   Configuration(Configuration.Builder x0, Object x1) {
      this(x0);
   }

   static {
      EXCLUDED_FIELDS.add("database");
      EXCLUDED_FIELDS.add("haMode");
      EXCLUDED_FIELDS.add("$jacocoData");
      EXCLUDED_FIELDS.add("addresses");
      SECURE_FIELDS = new HashSet();
      SECURE_FIELDS.add("password");
      SECURE_FIELDS.add("keyStorePassword");
      SECURE_FIELDS.add("trustStorePassword");
      PROPERTIES_TO_SKIP = new HashSet();
      PROPERTIES_TO_SKIP.add("initialUrl");
      PROPERTIES_TO_SKIP.add("logger");
      PROPERTIES_TO_SKIP.add("codecs");
      PROPERTIES_TO_SKIP.add("$jacocoData");
      PROPERTIES_TO_SKIP.add("CATALOG_TERM");
      PROPERTIES_TO_SKIP.add("SCHEMA_TERM");
      PROPERTIES_TO_SKIP.add("cachedCodecs");
      SENSITIVE_FIELDS = new HashSet();
      SENSITIVE_FIELDS.add("password");
      SENSITIVE_FIELDS.add("keyStorePassword");
      SENSITIVE_FIELDS.add("trustStorePassword");
   }

   public static final class Builder implements Cloneable {
      private Properties _nonMappedOptions;
      private HaMode _haMode;
      private List<HostAddress> _addresses = new ArrayList();
      private String user;
      private String password;
      private String database;
      private String timezone;
      private String connectionTimeZone;
      private String connectionCollation;
      private Boolean forceConnectionTimeZoneToSession;
      private Boolean preserveInstants;
      private Boolean autocommit;
      private Boolean useMysqlMetadata;
      private Boolean nullDatabaseMeansCurrent;
      private String useCatalogTerm;
      private Boolean createDatabaseIfNotExist;
      private Boolean useLocalSessionState;
      private Boolean returnMultiValuesGeneratedIds;
      private Boolean jdbcCompliantTruncation;
      private Boolean oldModeNoPrecisionTimestamp;
      private Boolean permitRedirect;
      private Boolean pinGlobalTxToPhysicalConnection;
      private Boolean permitNoResults;
      private Boolean cacheCodecs;
      private Integer defaultFetchSize;
      private Integer maxQuerySizeToLog;
      private Integer maxAllowedPacket;
      private String geometryDefaultType;
      private String restrictedAuth;
      private String initSql;
      private String transactionIsolation;
      private String metaExportedKeys;
      private String socketFactory;
      private Integer connectTimeout;
      private String pipe;
      private String localSocket;
      private Boolean tcpKeepAlive;
      private Boolean uuidAsString;
      private Integer tcpKeepIdle;
      private Integer tcpKeepCount;
      private Integer tcpKeepInterval;
      private Boolean tcpAbortiveClose;
      private String localSocketAddress;
      private Integer socketTimeout;
      private Boolean useReadAheadInput;
      private String tlsSocketType;
      private String sslMode;
      private String serverSslCert;
      private String keyStore;
      private String trustStore;
      private String keyStorePassword;
      private String trustStorePassword;
      private String keyPassword;
      private String keyStoreType;
      private String trustStoreType;
      private String enabledSslCipherSuites;
      private String enabledSslProtocolSuites;
      private Boolean fallbackToSystemKeyStore;
      private Boolean fallbackToSystemTrustStore;
      private Boolean allowMultiQueries;
      private Boolean allowLocalInfile;
      private Boolean rewriteBatchedStatements;
      private Boolean useCompression;
      private Boolean useAffectedRows;
      private Boolean useBulkStmts;
      private Boolean useBulkStmtsForInserts;
      private Boolean disablePipeline;
      private Boolean cachePrepStmts;
      private Integer prepStmtCacheSize;
      private Boolean useServerPrepStmts;
      private String credentialType;
      private String sessionVariables;
      private String connectionAttributes;
      private String servicePrincipalName;
      private Boolean disconnectOnExpiredPasswords;
      private Boolean blankTableNameMeta;
      private Boolean tinyInt1isBit;
      private Boolean transformedBitIsBoolean;
      private Boolean yearIsDateType;
      private Boolean dumpQueriesOnException;
      private Boolean includeInnodbStatusInDeadlockExceptions;
      private Boolean includeThreadDumpInDeadlockExceptions;
      private Integer retriesAllDown;
      private String galeraAllowedState;
      private Boolean transactionReplay;
      private Integer transactionReplaySize;
      private Boolean pool;
      private String poolName;
      private Integer maxPoolSize;
      private Integer minPoolSize;
      private Integer maxIdleTime;
      private Boolean registerJmxPool;
      private Integer poolValidMinDelay;
      private Boolean useResetConnection;
      private String serverRsaPublicKeyFile;
      private Boolean allowPublicKeyRetrieval;

      public Configuration.Builder user(String user) {
         this.user = Configuration.nullOrEmpty(user);
         return this;
      }

      public Configuration.Builder serverSslCert(String serverSslCert) {
         this.serverSslCert = Configuration.nullOrEmpty(serverSslCert);
         return this;
      }

      public Configuration.Builder keyStore(String keyStore) {
         this.keyStore = Configuration.nullOrEmpty(keyStore);
         return this;
      }

      public Configuration.Builder trustStore(String trustStore) {
         this.trustStore = Configuration.nullOrEmpty(trustStore);
         return this;
      }

      public Configuration.Builder keyStorePassword(String keyStorePassword) {
         this.keyStorePassword = Configuration.nullOrEmpty(keyStorePassword);
         return this;
      }

      public Configuration.Builder trustStorePassword(String trustStorePassword) {
         this.trustStorePassword = Configuration.nullOrEmpty(trustStorePassword);
         return this;
      }

      public Configuration.Builder keyPassword(String keyPassword) {
         this.keyPassword = Configuration.nullOrEmpty(keyPassword);
         return this;
      }

      public Configuration.Builder keyStoreType(String keyStoreType) {
         this.keyStoreType = Configuration.nullOrEmpty(keyStoreType);
         return this;
      }

      public Configuration.Builder trustStoreType(String trustStoreType) {
         this.trustStoreType = Configuration.nullOrEmpty(trustStoreType);
         return this;
      }

      public Configuration.Builder password(String password) {
         this.password = Configuration.nullOrEmpty(password);
         return this;
      }

      public Configuration.Builder enabledSslProtocolSuites(String enabledSslProtocolSuites) {
         this.enabledSslProtocolSuites = Configuration.nullOrEmpty(enabledSslProtocolSuites);
         return this;
      }

      public Configuration.Builder fallbackToSystemKeyStore(Boolean fallbackToSystemKeyStore) {
         this.fallbackToSystemKeyStore = fallbackToSystemKeyStore;
         return this;
      }

      public Configuration.Builder fallbackToSystemTrustStore(Boolean fallbackToSystemTrustStore) {
         this.fallbackToSystemTrustStore = fallbackToSystemTrustStore;
         return this;
      }

      public Configuration.Builder database(String database) {
         this.database = database;
         return this;
      }

      public Configuration.Builder haMode(HaMode haMode) {
         this._haMode = haMode;
         return this;
      }

      public Configuration.Builder addHost(String host, int port) {
         this._addresses.add(HostAddress.from(Configuration.nullOrEmpty(host), port));
         return this;
      }

      public Configuration.Builder addHost(String host, int port, String sslMode) {
         this._addresses.add(HostAddress.from(Configuration.nullOrEmpty(host), port, sslMode));
         return this;
      }

      public Configuration.Builder addHost(String host, int port, boolean master) {
         this._addresses.add(HostAddress.from(Configuration.nullOrEmpty(host), port, master));
         return this;
      }

      public Configuration.Builder addHost(String host, int port, boolean master, String sslMode) {
         this._addresses.add(HostAddress.from(Configuration.nullOrEmpty(host), port, master, sslMode));
         return this;
      }

      public Configuration.Builder addPipeHost(String pipe) {
         this._addresses.add(HostAddress.pipe(pipe));
         return this;
      }

      public Configuration.Builder addLocalSocketHost(String localSocket) {
         this._addresses.add(HostAddress.localSocket(localSocket));
         return this;
      }

      public Configuration.Builder addresses(HostAddress... hostAddress) {
         this._addresses = new ArrayList();
         this._addresses.addAll(Arrays.asList(hostAddress));
         return this;
      }

      public Configuration.Builder addresses(List<HostAddress> hostAddress) {
         this._addresses.addAll(hostAddress);
         return this;
      }

      public Configuration.Builder socketFactory(String socketFactory) {
         this.socketFactory = socketFactory;
         return this;
      }

      public Configuration.Builder connectTimeout(Integer connectTimeout) {
         this.connectTimeout = connectTimeout;
         return this;
      }

      public Configuration.Builder pipe(String pipe) {
         this.pipe = Configuration.nullOrEmpty(pipe);
         return this;
      }

      public Configuration.Builder localSocket(String localSocket) {
         this.localSocket = Configuration.nullOrEmpty(localSocket);
         return this;
      }

      public Configuration.Builder tcpKeepAlive(Boolean tcpKeepAlive) {
         this.tcpKeepAlive = tcpKeepAlive;
         return this;
      }

      public Configuration.Builder uuidAsString(Boolean uuidAsString) {
         this.uuidAsString = uuidAsString;
         return this;
      }

      public Configuration.Builder tcpKeepIdle(Integer tcpKeepIdle) {
         this.tcpKeepIdle = tcpKeepIdle;
         return this;
      }

      public Configuration.Builder tcpKeepCount(Integer tcpKeepCount) {
         this.tcpKeepCount = tcpKeepCount;
         return this;
      }

      public Configuration.Builder tcpKeepInterval(Integer tcpKeepInterval) {
         this.tcpKeepInterval = tcpKeepInterval;
         return this;
      }

      public Configuration.Builder tcpAbortiveClose(Boolean tcpAbortiveClose) {
         this.tcpAbortiveClose = tcpAbortiveClose;
         return this;
      }

      public Configuration.Builder geometryDefaultType(String geometryDefault) {
         this.geometryDefaultType = Configuration.nullOrEmpty(geometryDefault);
         return this;
      }

      public Configuration.Builder restrictedAuth(String restrictedAuth) {
         this.restrictedAuth = restrictedAuth;
         return this;
      }

      public Configuration.Builder initSql(String initSql) {
         this.initSql = initSql;
         return this;
      }

      public Configuration.Builder localSocketAddress(String localSocketAddress) {
         this.localSocketAddress = Configuration.nullOrEmpty(localSocketAddress);
         return this;
      }

      public Configuration.Builder socketTimeout(Integer socketTimeout) {
         this.socketTimeout = socketTimeout;
         return this;
      }

      public Configuration.Builder allowMultiQueries(Boolean allowMultiQueries) {
         this.allowMultiQueries = allowMultiQueries;
         return this;
      }

      public Configuration.Builder allowLocalInfile(Boolean allowLocalInfile) {
         this.allowLocalInfile = allowLocalInfile;
         return this;
      }

      public Configuration.Builder rewriteBatchedStatements(Boolean rewriteBatchedStatements) {
         this.rewriteBatchedStatements = rewriteBatchedStatements;
         return this;
      }

      public Configuration.Builder useCompression(Boolean useCompression) {
         this.useCompression = useCompression;
         return this;
      }

      public Configuration.Builder blankTableNameMeta(Boolean blankTableNameMeta) {
         this.blankTableNameMeta = blankTableNameMeta;
         return this;
      }

      public Configuration.Builder disconnectOnExpiredPasswords(Boolean disconnectOnExpiredPasswords) {
         this.disconnectOnExpiredPasswords = disconnectOnExpiredPasswords;
         return this;
      }

      public Configuration.Builder credentialType(String credentialType) {
         this.credentialType = Configuration.nullOrEmpty(credentialType);
         return this;
      }

      public Configuration.Builder sslMode(String sslMode) {
         this.sslMode = sslMode;
         return this;
      }

      public Configuration.Builder transactionIsolation(String transactionIsolation) {
         this.transactionIsolation = Configuration.nullOrEmpty(transactionIsolation);
         return this;
      }

      public Configuration.Builder metaExportedKeys(String metaExportedKeys) {
         this.metaExportedKeys = Configuration.nullOrEmpty(metaExportedKeys);
         return this;
      }

      public Configuration.Builder enabledSslCipherSuites(String enabledSslCipherSuites) {
         this.enabledSslCipherSuites = Configuration.nullOrEmpty(enabledSslCipherSuites);
         return this;
      }

      public Configuration.Builder sessionVariables(String sessionVariables) {
         this.sessionVariables = Configuration.nullOrEmpty(sessionVariables);
         return this;
      }

      public Configuration.Builder tinyInt1isBit(Boolean tinyInt1isBit) {
         this.tinyInt1isBit = tinyInt1isBit;
         return this;
      }

      public Configuration.Builder transformedBitIsBoolean(Boolean transformedBitIsBoolean) {
         this.transformedBitIsBoolean = transformedBitIsBoolean;
         return this;
      }

      public Configuration.Builder yearIsDateType(Boolean yearIsDateType) {
         this.yearIsDateType = yearIsDateType;
         return this;
      }

      public Configuration.Builder timezone(String timezone) {
         this.timezone = Configuration.nullOrEmpty(timezone);
         return this;
      }

      public Configuration.Builder connectionTimeZone(String connectionTimeZone) {
         this.connectionTimeZone = Configuration.nullOrEmpty(connectionTimeZone);
         return this;
      }

      public Configuration.Builder connectionCollation(String connectionCollation) {
         this.connectionCollation = Configuration.nullOrEmpty(connectionCollation);
         return this;
      }

      public Configuration.Builder forceConnectionTimeZoneToSession(Boolean forceConnectionTimeZoneToSession) {
         this.forceConnectionTimeZoneToSession = forceConnectionTimeZoneToSession;
         return this;
      }

      public Configuration.Builder preserveInstants(Boolean preserveInstants) {
         this.preserveInstants = preserveInstants;
         return this;
      }

      public Configuration.Builder dumpQueriesOnException(Boolean dumpQueriesOnException) {
         this.dumpQueriesOnException = dumpQueriesOnException;
         return this;
      }

      public Configuration.Builder prepStmtCacheSize(Integer prepStmtCacheSize) {
         this.prepStmtCacheSize = prepStmtCacheSize;
         return this;
      }

      public Configuration.Builder useAffectedRows(Boolean useAffectedRows) {
         this.useAffectedRows = useAffectedRows;
         return this;
      }

      public Configuration.Builder useServerPrepStmts(Boolean useServerPrepStmts) {
         this.useServerPrepStmts = useServerPrepStmts;
         return this;
      }

      public Configuration.Builder connectionAttributes(String connectionAttributes) {
         this.connectionAttributes = Configuration.nullOrEmpty(connectionAttributes);
         return this;
      }

      public Configuration.Builder useBulkStmts(Boolean useBulkStmts) {
         this.useBulkStmts = useBulkStmts;
         return this;
      }

      public Configuration.Builder useBulkStmtsForInserts(Boolean useBulkStmtsForInserts) {
         this.useBulkStmtsForInserts = useBulkStmtsForInserts;
         return this;
      }

      public Configuration.Builder disablePipeline(Boolean disablePipeline) {
         this.disablePipeline = disablePipeline;
         return this;
      }

      public Configuration.Builder autocommit(Boolean autocommit) {
         this.autocommit = autocommit;
         return this;
      }

      public Configuration.Builder useMysqlMetadata(Boolean useMysqlMetadata) {
         this.useMysqlMetadata = useMysqlMetadata;
         return this;
      }

      public Configuration.Builder nullDatabaseMeansCurrent(Boolean nullDatabaseMeansCurrent) {
         this.nullDatabaseMeansCurrent = nullDatabaseMeansCurrent;
         return this;
      }

      public Configuration.Builder useCatalogTerm(String useCatalogTerm) {
         this.useCatalogTerm = useCatalogTerm;
         return this;
      }

      public Configuration.Builder createDatabaseIfNotExist(Boolean createDatabaseIfNotExist) {
         this.createDatabaseIfNotExist = createDatabaseIfNotExist;
         return this;
      }

      public Configuration.Builder useLocalSessionState(Boolean useLocalSessionState) {
         this.useLocalSessionState = useLocalSessionState;
         return this;
      }

      public Configuration.Builder returnMultiValuesGeneratedIds(Boolean returnMultiValuesGeneratedIds) {
         this.returnMultiValuesGeneratedIds = returnMultiValuesGeneratedIds;
         return this;
      }

      public Configuration.Builder jdbcCompliantTruncation(Boolean jdbcCompliantTruncation) {
         this.jdbcCompliantTruncation = jdbcCompliantTruncation;
         return this;
      }

      public Configuration.Builder oldModeNoPrecisionTimestamp(Boolean oldModeNoPrecisionTimestamp) {
         this.oldModeNoPrecisionTimestamp = oldModeNoPrecisionTimestamp;
         return this;
      }

      public Configuration.Builder permitRedirect(Boolean permitRedirect) {
         this.permitRedirect = permitRedirect;
         return this;
      }

      public Configuration.Builder pinGlobalTxToPhysicalConnection(Boolean pinGlobalTxToPhysicalConnection) {
         this.pinGlobalTxToPhysicalConnection = pinGlobalTxToPhysicalConnection;
         return this;
      }

      public Configuration.Builder permitNoResults(Boolean permitNoResults) {
         this.permitNoResults = permitNoResults;
         return this;
      }

      public Configuration.Builder cacheCodecs(Boolean cacheCodecs) {
         this.cacheCodecs = cacheCodecs;
         return this;
      }

      public Configuration.Builder includeInnodbStatusInDeadlockExceptions(Boolean includeInnodbStatusInDeadlockExceptions) {
         this.includeInnodbStatusInDeadlockExceptions = includeInnodbStatusInDeadlockExceptions;
         return this;
      }

      public Configuration.Builder includeThreadDumpInDeadlockExceptions(Boolean includeThreadDumpInDeadlockExceptions) {
         this.includeThreadDumpInDeadlockExceptions = includeThreadDumpInDeadlockExceptions;
         return this;
      }

      public Configuration.Builder servicePrincipalName(String servicePrincipalName) {
         this.servicePrincipalName = Configuration.nullOrEmpty(servicePrincipalName);
         return this;
      }

      public Configuration.Builder defaultFetchSize(Integer defaultFetchSize) {
         this.defaultFetchSize = defaultFetchSize;
         return this;
      }

      public Configuration.Builder tlsSocketType(String tlsSocketType) {
         this.tlsSocketType = Configuration.nullOrEmpty(tlsSocketType);
         return this;
      }

      public Configuration.Builder maxQuerySizeToLog(Integer maxQuerySizeToLog) {
         this.maxQuerySizeToLog = maxQuerySizeToLog;
         return this;
      }

      public Configuration.Builder maxAllowedPacket(Integer maxAllowedPacket) {
         this.maxAllowedPacket = maxAllowedPacket;
         return this;
      }

      public Configuration.Builder retriesAllDown(Integer retriesAllDown) {
         this.retriesAllDown = retriesAllDown;
         return this;
      }

      public Configuration.Builder galeraAllowedState(String galeraAllowedState) {
         this.galeraAllowedState = Configuration.nullOrEmpty(galeraAllowedState);
         return this;
      }

      public Configuration.Builder pool(Boolean pool) {
         this.pool = pool;
         return this;
      }

      public Configuration.Builder poolName(String poolName) {
         this.poolName = Configuration.nullOrEmpty(poolName);
         return this;
      }

      public Configuration.Builder maxPoolSize(Integer maxPoolSize) {
         this.maxPoolSize = maxPoolSize;
         return this;
      }

      public Configuration.Builder minPoolSize(Integer minPoolSize) {
         this.minPoolSize = minPoolSize;
         return this;
      }

      public Configuration.Builder maxIdleTime(Integer maxIdleTime) {
         this.maxIdleTime = maxIdleTime;
         return this;
      }

      public Configuration.Builder registerJmxPool(Boolean registerJmxPool) {
         this.registerJmxPool = registerJmxPool;
         return this;
      }

      public Configuration.Builder poolValidMinDelay(Integer poolValidMinDelay) {
         this.poolValidMinDelay = poolValidMinDelay;
         return this;
      }

      public Configuration.Builder useResetConnection(Boolean useResetConnection) {
         this.useResetConnection = useResetConnection;
         return this;
      }

      public Configuration.Builder serverRsaPublicKeyFile(String serverRsaPublicKeyFile) {
         this.serverRsaPublicKeyFile = Configuration.nullOrEmpty(serverRsaPublicKeyFile);
         return this;
      }

      public Configuration.Builder allowPublicKeyRetrieval(Boolean allowPublicKeyRetrieval) {
         this.allowPublicKeyRetrieval = allowPublicKeyRetrieval;
         return this;
      }

      public Configuration.Builder useReadAheadInput(Boolean useReadAheadInput) {
         this.useReadAheadInput = useReadAheadInput;
         return this;
      }

      public Configuration.Builder cachePrepStmts(Boolean cachePrepStmts) {
         this.cachePrepStmts = cachePrepStmts;
         return this;
      }

      public Configuration.Builder transactionReplay(Boolean transactionReplay) {
         this.transactionReplay = transactionReplay;
         return this;
      }

      public Configuration.Builder transactionReplaySize(Integer transactionReplaySize) {
         this.transactionReplaySize = transactionReplaySize;
         return this;
      }

      public Configuration build() {
         Configuration conf = new Configuration(this);
         conf.initialUrl = Configuration.buildUrl(conf);
         return conf;
      }
   }

   private static class ParameterAppender {
      private final StringBuilder sb;
      private boolean first = true;

      ParameterAppender(StringBuilder sb) {
         this.sb = sb;
      }

      void appendParameter(String name, String value) {
         this.sb.append((char)(this.first ? '?' : '&')).append(name).append('=').append(value);
         this.first = false;
      }
   }
}
