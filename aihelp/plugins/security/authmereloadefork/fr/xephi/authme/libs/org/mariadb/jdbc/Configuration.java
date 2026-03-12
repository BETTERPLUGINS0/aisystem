package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.export.HaMode;
import fr.xephi.authme.libs.org.mariadb.jdbc.export.SslMode;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.Codec;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.CredentialPlugin;
import fr.xephi.authme.libs.org.mariadb.jdbc.plugin.credential.CredentialPluginLoader;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.constants.CatalogTerm;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Logger;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.log.Loggers;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.options.OptionAliases;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.Map.Entry;

public class Configuration {
   private static final Logger logger = Loggers.getLogger(Configuration.class);
   private String user;
   private String password;
   private String database;
   private List<HostAddress> addresses;
   private HaMode haMode;
   private String initialUrl;
   private Properties nonMappedOptions;
   private String timezone;
   private Boolean autocommit;
   private boolean useMysqlMetadata;
   private CatalogTerm useCatalogTerm;
   private boolean createDatabaseIfNotExist;
   private boolean useLocalSessionState;
   private boolean returnMultiValuesGeneratedIds;
   private TransactionIsolation transactionIsolation;
   private int defaultFetchSize;
   private int maxQuerySizeToLog;
   private Integer maxAllowedPacket;
   private String geometryDefaultType;
   private String restrictedAuth;
   private String initSql;
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
   private String keyStorePassword;
   private String keyPassword;
   private String keyStoreType;
   private String trustStoreType;
   private String enabledSslCipherSuites;
   private String enabledSslProtocolSuites;
   private boolean allowMultiQueries;
   private boolean allowLocalInfile;
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

   private Configuration() {
      this.user = null;
      this.password = null;
      this.database = null;
      this.addresses = null;
      this.haMode = HaMode.NONE;
      this.initialUrl = null;
      this.nonMappedOptions = null;
      this.timezone = null;
      this.autocommit = null;
      this.useMysqlMetadata = false;
      this.useCatalogTerm = CatalogTerm.UseCatalog;
      this.createDatabaseIfNotExist = false;
      this.useLocalSessionState = false;
      this.returnMultiValuesGeneratedIds = false;
      this.transactionIsolation = null;
      this.defaultFetchSize = 0;
      this.maxQuerySizeToLog = 1024;
      this.maxAllowedPacket = null;
      this.geometryDefaultType = null;
      this.restrictedAuth = null;
      this.initSql = null;
      this.socketFactory = null;
      this.connectTimeout = DriverManager.getLoginTimeout() > 0 ? DriverManager.getLoginTimeout() * 1000 : 30000;
      this.pipe = null;
      this.localSocket = null;
      this.uuidAsString = false;
      this.tcpKeepAlive = true;
      this.tcpKeepIdle = 0;
      this.tcpKeepCount = 0;
      this.tcpKeepInterval = 0;
      this.tcpAbortiveClose = false;
      this.localSocketAddress = null;
      this.socketTimeout = 0;
      this.useReadAheadInput = false;
      this.tlsSocketType = null;
      this.sslMode = SslMode.DISABLE;
      this.serverSslCert = null;
      this.keyStore = null;
      this.keyStorePassword = null;
      this.keyPassword = null;
      this.keyStoreType = null;
      this.trustStoreType = null;
      this.enabledSslCipherSuites = null;
      this.enabledSslProtocolSuites = null;
      this.allowMultiQueries = false;
      this.allowLocalInfile = true;
      this.useCompression = false;
      this.useAffectedRows = false;
      this.useBulkStmts = false;
      this.useBulkStmtsForInserts = true;
      this.disablePipeline = false;
      this.cachePrepStmts = true;
      this.prepStmtCacheSize = 250;
      this.useServerPrepStmts = false;
      this.credentialType = null;
      this.sessionVariables = null;
      this.connectionAttributes = null;
      this.servicePrincipalName = null;
      this.blankTableNameMeta = false;
      this.tinyInt1isBit = true;
      this.transformedBitIsBoolean = true;
      this.yearIsDateType = true;
      this.dumpQueriesOnException = false;
      this.includeInnodbStatusInDeadlockExceptions = false;
      this.includeThreadDumpInDeadlockExceptions = false;
      this.retriesAllDown = 120;
      this.galeraAllowedState = null;
      this.transactionReplay = false;
      this.transactionReplaySize = 64;
      this.pool = false;
      this.poolName = null;
      this.maxPoolSize = 8;
      this.minPoolSize = 8;
      this.maxIdleTime = 600000;
      this.registerJmxPool = true;
      this.poolValidMinDelay = 1000;
      this.useResetConnection = false;
      this.serverRsaPublicKeyFile = null;
      this.allowPublicKeyRetrieval = false;
      this.codecs = null;
   }

   private Configuration(String user, String password, String database, List<HostAddress> addresses, HaMode haMode, Properties nonMappedOptions, String timezone, Boolean autocommit, boolean useMysqlMetadata, CatalogTerm useCatalogTerm, boolean createDatabaseIfNotExist, boolean useLocalSessionState, boolean returnMultiValuesGeneratedIds, TransactionIsolation transactionIsolation, int defaultFetchSize, int maxQuerySizeToLog, Integer maxAllowedPacket, String geometryDefaultType, String restrictedAuth, String initSql, String socketFactory, int connectTimeout, String pipe, String localSocket, boolean tcpKeepAlive, boolean uuidAsString, int tcpKeepIdle, int tcpKeepCount, int tcpKeepInterval, boolean tcpAbortiveClose, String localSocketAddress, int socketTimeout, boolean useReadAheadInput, String tlsSocketType, SslMode sslMode, String serverSslCert, String keyStore, String keyStorePassword, String keyPassword, String keyStoreType, String trustStoreType, String enabledSslCipherSuites, String enabledSslProtocolSuites, boolean allowMultiQueries, boolean allowLocalInfile, boolean useCompression, boolean useAffectedRows, boolean useBulkStmts, boolean useBulkStmtsForInserts, boolean disablePipeline, boolean cachePrepStmts, int prepStmtCacheSize, boolean useServerPrepStmts, CredentialPlugin credentialType, String sessionVariables, String connectionAttributes, String servicePrincipalName, boolean blankTableNameMeta, boolean tinyInt1isBit, boolean transformedBitIsBoolean, boolean yearIsDateType, boolean dumpQueriesOnException, boolean includeInnodbStatusInDeadlockExceptions, boolean includeThreadDumpInDeadlockExceptions, int retriesAllDown, String galeraAllowedState, boolean transactionReplay, int transactionReplaySize, boolean pool, String poolName, int maxPoolSize, int minPoolSize, int maxIdleTime, boolean registerJmxPool, int poolValidMinDelay, boolean useResetConnection, String serverRsaPublicKeyFile, boolean allowPublicKeyRetrieval) {
      this.user = null;
      this.password = null;
      this.database = null;
      this.addresses = null;
      this.haMode = HaMode.NONE;
      this.initialUrl = null;
      this.nonMappedOptions = null;
      this.timezone = null;
      this.autocommit = null;
      this.useMysqlMetadata = false;
      this.useCatalogTerm = CatalogTerm.UseCatalog;
      this.createDatabaseIfNotExist = false;
      this.useLocalSessionState = false;
      this.returnMultiValuesGeneratedIds = false;
      this.transactionIsolation = null;
      this.defaultFetchSize = 0;
      this.maxQuerySizeToLog = 1024;
      this.maxAllowedPacket = null;
      this.geometryDefaultType = null;
      this.restrictedAuth = null;
      this.initSql = null;
      this.socketFactory = null;
      this.connectTimeout = DriverManager.getLoginTimeout() > 0 ? DriverManager.getLoginTimeout() * 1000 : 30000;
      this.pipe = null;
      this.localSocket = null;
      this.uuidAsString = false;
      this.tcpKeepAlive = true;
      this.tcpKeepIdle = 0;
      this.tcpKeepCount = 0;
      this.tcpKeepInterval = 0;
      this.tcpAbortiveClose = false;
      this.localSocketAddress = null;
      this.socketTimeout = 0;
      this.useReadAheadInput = false;
      this.tlsSocketType = null;
      this.sslMode = SslMode.DISABLE;
      this.serverSslCert = null;
      this.keyStore = null;
      this.keyStorePassword = null;
      this.keyPassword = null;
      this.keyStoreType = null;
      this.trustStoreType = null;
      this.enabledSslCipherSuites = null;
      this.enabledSslProtocolSuites = null;
      this.allowMultiQueries = false;
      this.allowLocalInfile = true;
      this.useCompression = false;
      this.useAffectedRows = false;
      this.useBulkStmts = false;
      this.useBulkStmtsForInserts = true;
      this.disablePipeline = false;
      this.cachePrepStmts = true;
      this.prepStmtCacheSize = 250;
      this.useServerPrepStmts = false;
      this.credentialType = null;
      this.sessionVariables = null;
      this.connectionAttributes = null;
      this.servicePrincipalName = null;
      this.blankTableNameMeta = false;
      this.tinyInt1isBit = true;
      this.transformedBitIsBoolean = true;
      this.yearIsDateType = true;
      this.dumpQueriesOnException = false;
      this.includeInnodbStatusInDeadlockExceptions = false;
      this.includeThreadDumpInDeadlockExceptions = false;
      this.retriesAllDown = 120;
      this.galeraAllowedState = null;
      this.transactionReplay = false;
      this.transactionReplaySize = 64;
      this.pool = false;
      this.poolName = null;
      this.maxPoolSize = 8;
      this.minPoolSize = 8;
      this.maxIdleTime = 600000;
      this.registerJmxPool = true;
      this.poolValidMinDelay = 1000;
      this.useResetConnection = false;
      this.serverRsaPublicKeyFile = null;
      this.allowPublicKeyRetrieval = false;
      this.codecs = null;
      this.user = user;
      this.password = password;
      this.database = database;
      this.addresses = addresses;
      this.haMode = haMode;
      this.nonMappedOptions = nonMappedOptions;
      this.timezone = timezone;
      this.autocommit = autocommit;
      this.useMysqlMetadata = useMysqlMetadata;
      this.useCatalogTerm = useCatalogTerm;
      this.createDatabaseIfNotExist = createDatabaseIfNotExist;
      this.returnMultiValuesGeneratedIds = returnMultiValuesGeneratedIds;
      this.useLocalSessionState = useLocalSessionState;
      this.transactionIsolation = transactionIsolation;
      this.defaultFetchSize = defaultFetchSize;
      this.maxQuerySizeToLog = maxQuerySizeToLog;
      this.maxAllowedPacket = maxAllowedPacket;
      this.geometryDefaultType = geometryDefaultType;
      this.restrictedAuth = restrictedAuth;
      this.initSql = initSql;
      this.socketFactory = socketFactory;
      this.connectTimeout = connectTimeout;
      this.pipe = pipe;
      this.localSocket = localSocket;
      this.tcpKeepAlive = tcpKeepAlive;
      this.uuidAsString = uuidAsString;
      this.tcpKeepIdle = tcpKeepIdle;
      this.tcpKeepCount = tcpKeepCount;
      this.tcpKeepInterval = tcpKeepInterval;
      this.tcpAbortiveClose = tcpAbortiveClose;
      this.localSocketAddress = localSocketAddress;
      this.socketTimeout = socketTimeout;
      this.useReadAheadInput = useReadAheadInput;
      this.tlsSocketType = tlsSocketType;
      this.sslMode = sslMode;
      this.serverSslCert = serverSslCert;
      this.keyStore = keyStore;
      this.keyStorePassword = keyStorePassword;
      this.keyPassword = keyPassword;
      this.keyStoreType = keyStoreType;
      this.trustStoreType = trustStoreType;
      this.enabledSslCipherSuites = enabledSslCipherSuites;
      this.enabledSslProtocolSuites = enabledSslProtocolSuites;
      this.allowMultiQueries = allowMultiQueries;
      this.allowLocalInfile = allowLocalInfile;
      this.useCompression = useCompression;
      this.useAffectedRows = useAffectedRows;
      this.useBulkStmts = useBulkStmts;
      this.useBulkStmtsForInserts = useBulkStmtsForInserts;
      this.disablePipeline = disablePipeline;
      this.cachePrepStmts = cachePrepStmts;
      this.prepStmtCacheSize = prepStmtCacheSize;
      this.useServerPrepStmts = useServerPrepStmts;
      this.credentialType = credentialType;
      this.sessionVariables = sessionVariables;
      this.connectionAttributes = connectionAttributes;
      this.servicePrincipalName = servicePrincipalName;
      this.blankTableNameMeta = blankTableNameMeta;
      this.tinyInt1isBit = tinyInt1isBit;
      this.transformedBitIsBoolean = transformedBitIsBoolean;
      this.yearIsDateType = yearIsDateType;
      this.dumpQueriesOnException = dumpQueriesOnException;
      this.includeInnodbStatusInDeadlockExceptions = includeInnodbStatusInDeadlockExceptions;
      this.includeThreadDumpInDeadlockExceptions = includeThreadDumpInDeadlockExceptions;
      this.retriesAllDown = retriesAllDown;
      this.galeraAllowedState = galeraAllowedState;
      this.transactionReplay = transactionReplay;
      this.transactionReplaySize = transactionReplaySize;
      this.pool = pool;
      this.poolName = poolName;
      this.maxPoolSize = maxPoolSize;
      this.minPoolSize = minPoolSize;
      this.maxIdleTime = maxIdleTime;
      this.registerJmxPool = registerJmxPool;
      this.poolValidMinDelay = poolValidMinDelay;
      this.useResetConnection = useResetConnection;
      this.serverRsaPublicKeyFile = serverRsaPublicKeyFile;
      this.allowPublicKeyRetrieval = allowPublicKeyRetrieval;
      this.initialUrl = buildUrl(this);
   }

   private Configuration(String database, List<HostAddress> addresses, HaMode haMode, String user, String password, String enabledSslProtocolSuites, String socketFactory, Integer connectTimeout, String pipe, String localSocket, Boolean tcpKeepAlive, Boolean uuidAsString, Integer tcpKeepIdle, Integer tcpKeepCount, Integer tcpKeepInterval, Boolean tcpAbortiveClose, String localSocketAddress, Integer socketTimeout, Boolean allowMultiQueries, Boolean allowLocalInfile, Boolean useCompression, Boolean blankTableNameMeta, String credentialType, String sslMode, String transactionIsolation, String enabledSslCipherSuites, String sessionVariables, Boolean tinyInt1isBit, Boolean transformedBitIsBoolean, Boolean yearIsDateType, String timezone, Boolean dumpQueriesOnException, Integer prepStmtCacheSize, Boolean useAffectedRows, Boolean useServerPrepStmts, String connectionAttributes, Boolean useBulkStmts, Boolean useBulkStmtsForInserts, Boolean disablePipeline, Boolean autocommit, Boolean useMysqlMetadata, String useCatalogTerm, Boolean createDatabaseIfNotExist, Boolean useLocalSessionState, Boolean returnMultiValuesGeneratedIds, Boolean includeInnodbStatusInDeadlockExceptions, Boolean includeThreadDumpInDeadlockExceptions, String servicePrincipalName, Integer defaultFetchSize, String tlsSocketType, Integer maxQuerySizeToLog, Integer maxAllowedPacket, Integer retriesAllDown, String galeraAllowedState, Boolean pool, String poolName, Integer maxPoolSize, Integer minPoolSize, Integer maxIdleTime, Boolean registerJmxPool, Integer poolValidMinDelay, Boolean useResetConnection, String serverRsaPublicKeyFile, Boolean allowPublicKeyRetrieval, String serverSslCert, String keyStore, String keyStorePassword, String keyPassword, String keyStoreType, String trustStoreType, Boolean useReadAheadInput, Boolean cachePrepStmts, Boolean transactionReplay, Integer transactionReplaySize, String geometryDefaultType, String restrictedAuth, String initSql, Properties nonMappedOptions) throws SQLException {
      this.user = null;
      this.password = null;
      this.database = null;
      this.addresses = null;
      this.haMode = HaMode.NONE;
      this.initialUrl = null;
      this.nonMappedOptions = null;
      this.timezone = null;
      this.autocommit = null;
      this.useMysqlMetadata = false;
      this.useCatalogTerm = CatalogTerm.UseCatalog;
      this.createDatabaseIfNotExist = false;
      this.useLocalSessionState = false;
      this.returnMultiValuesGeneratedIds = false;
      this.transactionIsolation = null;
      this.defaultFetchSize = 0;
      this.maxQuerySizeToLog = 1024;
      this.maxAllowedPacket = null;
      this.geometryDefaultType = null;
      this.restrictedAuth = null;
      this.initSql = null;
      this.socketFactory = null;
      this.connectTimeout = DriverManager.getLoginTimeout() > 0 ? DriverManager.getLoginTimeout() * 1000 : 30000;
      this.pipe = null;
      this.localSocket = null;
      this.uuidAsString = false;
      this.tcpKeepAlive = true;
      this.tcpKeepIdle = 0;
      this.tcpKeepCount = 0;
      this.tcpKeepInterval = 0;
      this.tcpAbortiveClose = false;
      this.localSocketAddress = null;
      this.socketTimeout = 0;
      this.useReadAheadInput = false;
      this.tlsSocketType = null;
      this.sslMode = SslMode.DISABLE;
      this.serverSslCert = null;
      this.keyStore = null;
      this.keyStorePassword = null;
      this.keyPassword = null;
      this.keyStoreType = null;
      this.trustStoreType = null;
      this.enabledSslCipherSuites = null;
      this.enabledSslProtocolSuites = null;
      this.allowMultiQueries = false;
      this.allowLocalInfile = true;
      this.useCompression = false;
      this.useAffectedRows = false;
      this.useBulkStmts = false;
      this.useBulkStmtsForInserts = true;
      this.disablePipeline = false;
      this.cachePrepStmts = true;
      this.prepStmtCacheSize = 250;
      this.useServerPrepStmts = false;
      this.credentialType = null;
      this.sessionVariables = null;
      this.connectionAttributes = null;
      this.servicePrincipalName = null;
      this.blankTableNameMeta = false;
      this.tinyInt1isBit = true;
      this.transformedBitIsBoolean = true;
      this.yearIsDateType = true;
      this.dumpQueriesOnException = false;
      this.includeInnodbStatusInDeadlockExceptions = false;
      this.includeThreadDumpInDeadlockExceptions = false;
      this.retriesAllDown = 120;
      this.galeraAllowedState = null;
      this.transactionReplay = false;
      this.transactionReplaySize = 64;
      this.pool = false;
      this.poolName = null;
      this.maxPoolSize = 8;
      this.minPoolSize = 8;
      this.maxIdleTime = 600000;
      this.registerJmxPool = true;
      this.poolValidMinDelay = 1000;
      this.useResetConnection = false;
      this.serverRsaPublicKeyFile = null;
      this.allowPublicKeyRetrieval = false;
      this.codecs = null;
      this.database = database;
      this.addresses = addresses;
      this.nonMappedOptions = nonMappedOptions;
      if (haMode != null) {
         this.haMode = haMode;
      }

      this.credentialType = CredentialPluginLoader.get(credentialType);
      this.user = user;
      this.password = password;
      this.enabledSslProtocolSuites = enabledSslProtocolSuites;
      this.socketFactory = socketFactory;
      if (connectTimeout != null) {
         this.connectTimeout = connectTimeout;
      }

      this.pipe = pipe;
      this.localSocket = localSocket;
      if (tcpKeepAlive != null) {
         this.tcpKeepAlive = tcpKeepAlive;
      }

      if (uuidAsString != null) {
         this.uuidAsString = uuidAsString;
      }

      if (tcpKeepIdle != null) {
         this.tcpKeepIdle = tcpKeepIdle;
      }

      if (tcpKeepCount != null) {
         this.tcpKeepCount = tcpKeepCount;
      }

      if (tcpKeepInterval != null) {
         this.tcpKeepInterval = tcpKeepInterval;
      }

      if (tcpAbortiveClose != null) {
         this.tcpAbortiveClose = tcpAbortiveClose;
      }

      this.localSocketAddress = localSocketAddress;
      if (socketTimeout != null) {
         this.socketTimeout = socketTimeout;
      }

      if (allowMultiQueries != null) {
         this.allowMultiQueries = allowMultiQueries;
      }

      if (allowLocalInfile != null) {
         this.allowLocalInfile = allowLocalInfile;
      }

      if (useCompression != null) {
         this.useCompression = useCompression;
      }

      if (blankTableNameMeta != null) {
         this.blankTableNameMeta = blankTableNameMeta;
      }

      if (this.credentialType == null || !this.credentialType.mustUseSsl() || sslMode != null && SslMode.from(sslMode) != SslMode.DISABLE) {
         this.sslMode = sslMode != null ? SslMode.from(sslMode) : SslMode.DISABLE;
      } else {
         this.sslMode = SslMode.VERIFY_FULL;
      }

      if (transactionIsolation != null) {
         this.transactionIsolation = TransactionIsolation.from(transactionIsolation);
      }

      this.enabledSslCipherSuites = enabledSslCipherSuites;
      this.sessionVariables = sessionVariables;
      if (tinyInt1isBit != null) {
         this.tinyInt1isBit = tinyInt1isBit;
      }

      if (transformedBitIsBoolean != null) {
         this.transformedBitIsBoolean = transformedBitIsBoolean;
      }

      if (yearIsDateType != null) {
         this.yearIsDateType = yearIsDateType;
      }

      this.timezone = timezone;
      if (dumpQueriesOnException != null) {
         this.dumpQueriesOnException = dumpQueriesOnException;
      }

      if (prepStmtCacheSize != null) {
         this.prepStmtCacheSize = prepStmtCacheSize;
      }

      if (useAffectedRows != null) {
         this.useAffectedRows = useAffectedRows;
      }

      if (useServerPrepStmts != null) {
         this.useServerPrepStmts = useServerPrepStmts;
      }

      this.connectionAttributes = connectionAttributes;
      if (useBulkStmts != null) {
         this.useBulkStmts = useBulkStmts;
      }

      if (useBulkStmtsForInserts != null) {
         this.useBulkStmtsForInserts = useBulkStmtsForInserts;
      }

      if (disablePipeline != null) {
         this.disablePipeline = disablePipeline;
      }

      if (autocommit != null) {
         this.autocommit = autocommit;
      }

      if (useMysqlMetadata != null) {
         this.useMysqlMetadata = useMysqlMetadata;
      }

      if (useCatalogTerm != null) {
         if (!"CATALOG".equalsIgnoreCase(useCatalogTerm) && !"SCHEMA".equalsIgnoreCase(useCatalogTerm)) {
            throw new IllegalArgumentException("useCatalogTerm can only have CATALOG/SCHEMA value, current set value is " + useCatalogTerm);
         }

         this.useCatalogTerm = "CATALOG".equalsIgnoreCase(useCatalogTerm) ? CatalogTerm.UseCatalog : CatalogTerm.UseSchema;
      }

      if (createDatabaseIfNotExist != null) {
         this.createDatabaseIfNotExist = createDatabaseIfNotExist;
      }

      if (useLocalSessionState != null) {
         this.useLocalSessionState = useLocalSessionState;
      }

      if (returnMultiValuesGeneratedIds != null) {
         this.returnMultiValuesGeneratedIds = returnMultiValuesGeneratedIds;
      }

      if (includeInnodbStatusInDeadlockExceptions != null) {
         this.includeInnodbStatusInDeadlockExceptions = includeInnodbStatusInDeadlockExceptions;
      }

      if (includeThreadDumpInDeadlockExceptions != null) {
         this.includeThreadDumpInDeadlockExceptions = includeThreadDumpInDeadlockExceptions;
      }

      if (servicePrincipalName != null) {
         this.servicePrincipalName = servicePrincipalName;
      }

      if (defaultFetchSize != null) {
         this.defaultFetchSize = defaultFetchSize;
      }

      if (tlsSocketType != null) {
         this.tlsSocketType = tlsSocketType;
      }

      if (maxQuerySizeToLog != null) {
         this.maxQuerySizeToLog = maxQuerySizeToLog;
      }

      if (maxAllowedPacket != null) {
         this.maxAllowedPacket = maxAllowedPacket;
      }

      if (retriesAllDown != null) {
         this.retriesAllDown = retriesAllDown;
      }

      if (galeraAllowedState != null) {
         this.galeraAllowedState = galeraAllowedState;
      }

      if (pool != null) {
         this.pool = pool;
      }

      if (poolName != null) {
         this.poolName = poolName;
      }

      if (maxPoolSize != null) {
         this.maxPoolSize = maxPoolSize;
      }

      if (minPoolSize != null) {
         this.minPoolSize = minPoolSize;
      } else {
         this.minPoolSize = this.maxPoolSize;
      }

      if (maxIdleTime != null) {
         this.maxIdleTime = maxIdleTime;
      }

      if (registerJmxPool != null) {
         this.registerJmxPool = registerJmxPool;
      }

      if (poolValidMinDelay != null) {
         this.poolValidMinDelay = poolValidMinDelay;
      }

      if (useResetConnection != null) {
         this.useResetConnection = useResetConnection;
      }

      if (serverRsaPublicKeyFile != null) {
         this.serverRsaPublicKeyFile = serverRsaPublicKeyFile;
      }

      if (allowPublicKeyRetrieval != null) {
         this.allowPublicKeyRetrieval = allowPublicKeyRetrieval;
      }

      if (useReadAheadInput != null) {
         this.useReadAheadInput = useReadAheadInput;
      }

      if (cachePrepStmts != null) {
         this.cachePrepStmts = cachePrepStmts;
      }

      if (transactionReplay != null) {
         this.transactionReplay = transactionReplay;
      }

      if (transactionReplaySize != null) {
         this.transactionReplaySize = transactionReplaySize;
      }

      if (geometryDefaultType != null) {
         this.geometryDefaultType = geometryDefaultType;
      }

      if (restrictedAuth != null) {
         this.restrictedAuth = restrictedAuth;
      }

      if (initSql != null) {
         this.initSql = initSql;
      }

      if (serverSslCert != null) {
         this.serverSslCert = serverSslCert;
      }

      if (keyStore != null) {
         this.keyStore = keyStore;
      }

      if (keyStorePassword != null) {
         this.keyStorePassword = keyStorePassword;
      }

      if (keyPassword != null) {
         this.keyPassword = keyPassword;
      }

      if (keyStoreType != null) {
         this.keyStoreType = keyStoreType;
      }

      if (trustStoreType != null) {
         this.trustStoreType = trustStoreType;
      }

      boolean first = true;

      for(Iterator var80 = addresses.iterator(); var80.hasNext(); first = false) {
         HostAddress host = (HostAddress)var80.next();
         boolean primary = haMode != HaMode.REPLICATION || first;
         if (host.primary == null) {
            host.primary = primary;
         }
      }

      Field[] fields = Configuration.class.getDeclaredFields();

      try {
         Field[] var88 = fields;
         int var89 = fields.length;

         for(int var83 = 0; var83 < var89; ++var83) {
            Field field = var88[var83];
            if (field.getType().equals(Integer.TYPE)) {
               int val = field.getInt(this);
               if (val < 0) {
                  throw new SQLException(String.format("Value for %s must be >= 1 (value is %s)", field.getName(), val));
               }
            }
         }
      } catch (IllegalAccessException | IllegalArgumentException var86) {
      }

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
         int separator = url.indexOf("//");
         if (separator == -1) {
            throw new IllegalArgumentException("url parsing error : '//' is not present in the url " + url);
         } else {
            builder.haMode(parseHaMode(url, separator));
            String urlSecondPart = url.substring(separator + 2);
            int dbIndex = urlSecondPart.indexOf("/");
            int paramIndex = urlSecondPart.indexOf("?");
            String hostAddressesString;
            String additionalParameters;
            if (dbIndex < paramIndex && dbIndex < 0 || dbIndex > paramIndex && paramIndex > -1) {
               hostAddressesString = urlSecondPart.substring(0, paramIndex);
               additionalParameters = urlSecondPart.substring(paramIndex);
            } else if (dbIndex >= paramIndex && dbIndex <= paramIndex) {
               hostAddressesString = urlSecondPart;
               additionalParameters = null;
            } else {
               hostAddressesString = urlSecondPart.substring(0, dbIndex);
               additionalParameters = urlSecondPart.substring(dbIndex);
            }

            if (additionalParameters != null) {
               int optIndex = additionalParameters.indexOf("?");
               String database;
               if (optIndex < 0) {
                  database = additionalParameters.length() > 1 ? additionalParameters.substring(1) : null;
               } else {
                  if (optIndex == 0) {
                     database = null;
                  } else {
                     database = additionalParameters.substring(1, optIndex);
                     if (database.isEmpty()) {
                        database = null;
                     }
                  }

                  String urlParameters = additionalParameters.substring(optIndex + 1);
                  if (urlParameters != null && !urlParameters.isEmpty()) {
                     String[] parameters = urlParameters.split("&");
                     String[] var13 = parameters;
                     int var14 = parameters.length;

                     for(int var15 = 0; var15 < var14; ++var15) {
                        String parameter = var13[var15];
                        int pos = parameter.indexOf(61);
                        if (pos == -1) {
                           properties.setProperty(parameter, "");
                        } else {
                           properties.setProperty(parameter.substring(0, pos), parameter.substring(pos + 1));
                        }
                     }
                  }
               }

               builder.database(database);
            } else {
               builder.database((String)null);
            }

            mapPropertiesToOption(builder, properties);
            builder._addresses = HostAddress.parse(hostAddressesString, builder._haMode);
            return builder.build();
         }
      } catch (IllegalArgumentException var18) {
         throw new SQLException("error parsing url : " + var18.getMessage(), var18);
      }
   }

   private static void mapPropertiesToOption(Configuration.Builder builder, Properties properties) {
      Properties nonMappedOptions = new Properties();

      try {
         Iterator var3 = properties.keySet().iterator();

         label128:
         while(true) {
            Object keyObj;
            String realKey;
            Object propertyValue;
            do {
               do {
                  if (!var3.hasNext()) {
                     if (isSet("useSsl", nonMappedOptions) || isSet("useSSL", nonMappedOptions)) {
                        Properties deprecatedDesc = new Properties();

                        try {
                           InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream("deprecated.properties");

                           try {
                              deprecatedDesc.load(inputStream);
                           } catch (Throwable var16) {
                              if (inputStream != null) {
                                 try {
                                    inputStream.close();
                                 } catch (Throwable var14) {
                                    var16.addSuppressed(var14);
                                 }
                              }

                              throw var16;
                           }

                           if (inputStream != null) {
                              inputStream.close();
                           }
                        } catch (IOException var17) {
                        }

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
                     }
                     break label128;
                  }

                  keyObj = var3.next();
                  realKey = (String)OptionAliases.OPTIONS_ALIASES.get(keyObj.toString().toLowerCase(Locale.ROOT));
                  if (realKey == null) {
                     realKey = keyObj.toString();
                  }

                  propertyValue = properties.get(keyObj);
               } while(propertyValue == null);
            } while(realKey == null);

            boolean used = false;
            Field[] var8 = Configuration.Builder.class.getDeclaredFields();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               Field field = var8[var10];
               if (realKey.toLowerCase(Locale.ROOT).equals(field.getName().toLowerCase(Locale.ROOT))) {
                  field.setAccessible(true);
                  used = true;
                  if (field.getGenericType().equals(String.class) && !propertyValue.toString().isEmpty()) {
                     field.set(builder, propertyValue);
                  } else if (field.getGenericType().equals(Boolean.class)) {
                     String var12 = propertyValue.toString().toLowerCase();
                     byte var13 = -1;
                     switch(var12.hashCode()) {
                     case 0:
                        if (var12.equals("")) {
                           var13 = 0;
                        }
                        break;
                     case 48:
                        if (var12.equals("0")) {
                           var13 = 3;
                        }
                        break;
                     case 49:
                        if (var12.equals("1")) {
                           var13 = 1;
                        }
                        break;
                     case 3569038:
                        if (var12.equals("true")) {
                           var13 = 2;
                        }
                        break;
                     case 97196323:
                        if (var12.equals("false")) {
                           var13 = 4;
                        }
                     }

                     switch(var13) {
                     case 0:
                     case 1:
                     case 2:
                        field.set(builder, Boolean.TRUE);
                        break;
                     case 3:
                     case 4:
                        field.set(builder, Boolean.FALSE);
                        break;
                     default:
                        throw new IllegalArgumentException(String.format("Optional parameter %s must be boolean (true/false or 0/1) was '%s'", keyObj, propertyValue));
                     }
                  } else if (field.getGenericType().equals(Integer.class)) {
                     try {
                        Integer value = Integer.parseInt(propertyValue.toString());
                        field.set(builder, value);
                     } catch (NumberFormatException var15) {
                        throw new IllegalArgumentException(String.format("Optional parameter %s must be Integer, was '%s'", keyObj, propertyValue));
                     }
                  }
               }
            }

            if (!used) {
               nonMappedOptions.put(realKey, propertyValue);
            }
         }
      } catch (SecurityException | IllegalAccessException var18) {
         throw new IllegalArgumentException("Unexpected error", var18);
      }

      builder._nonMappedOptions = nonMappedOptions;
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
      StringBuilder sb = new StringBuilder();
      StringBuilder sbUnknownOpts = new StringBuilder();
      if (conf.nonMappedOptions.isEmpty()) {
         sbUnknownOpts.append("None");
      } else {
         Iterator var4 = conf.nonMappedOptions.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<Object, Object> entry = (Entry)var4.next();
            sbUnknownOpts.append("\n * ").append(entry.getKey()).append(" : ").append(entry.getValue());
         }
      }

      sb.append("Configuration:").append("\n * resulting Url : ").append(conf.initialUrl).append("\nUnknown options : ").append(sbUnknownOpts).append("\n").append("\nNon default options : ");
      Configuration defaultConf = parse("jdbc:mariadb://localhost/");
      StringBuilder sbDefaultOpts = new StringBuilder();
      StringBuilder sbDifferentOpts = new StringBuilder();

      try {
         List<String> propertyToSkip = Arrays.asList("initialUrl", "logger", "codecs", "$jacocoData");
         Field[] fields = Configuration.class.getDeclaredFields();
         Arrays.sort(fields, Comparator.comparing(Field::getName));
         Field[] var9 = fields;
         int var10 = fields.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            Field field = var9[var11];
            if (!propertyToSkip.contains(field.getName())) {
               Object fieldValue = field.get(conf);
               if (fieldValue == null) {
                  (Objects.equals(fieldValue, field.get(defaultConf)) ? sbDefaultOpts : sbDifferentOpts).append("\n * ").append(field.getName()).append(" : ").append(fieldValue);
               } else if (field.getName().equals("haMode")) {
                  (Objects.equals(fieldValue, field.get(defaultConf)) ? sbDefaultOpts : sbDifferentOpts).append("\n * ").append(field.getName()).append(" : ").append(fieldValue);
               } else {
                  String var14 = fieldValue.getClass().getSimpleName();
                  byte var15 = -1;
                  switch(var14.hashCode()) {
                  case -2141683108:
                     if (var14.equals("HaMode")) {
                        var15 = 2;
                     }
                     break;
                  case -1808118735:
                     if (var14.equals("String")) {
                        var15 = 0;
                     }
                     break;
                  case -705177168:
                     if (var14.equals("TransactionIsolation")) {
                        var15 = 3;
                     }
                     break;
                  case -672261858:
                     if (var14.equals("Integer")) {
                        var15 = 4;
                     }
                     break;
                  case -252109393:
                     if (var14.equals("SslMode")) {
                        var15 = 5;
                     }
                     break;
                  case 578806391:
                     if (var14.equals("ArrayList")) {
                        var15 = 7;
                     }
                     break;
                  case 660671493:
                     if (var14.equals("CatalogTerm")) {
                        var15 = 6;
                     }
                     break;
                  case 1067411795:
                     if (var14.equals("Properties")) {
                        var15 = 8;
                     }
                     break;
                  case 1729365000:
                     if (var14.equals("Boolean")) {
                        var15 = 1;
                     }
                  }

                  switch(var15) {
                  case 0:
                  case 1:
                  case 2:
                  case 3:
                  case 4:
                  case 5:
                  case 6:
                     (Objects.equals(fieldValue, field.get(defaultConf)) ? sbDefaultOpts : sbDifferentOpts).append("\n * ").append(field.getName()).append(" : ").append(fieldValue);
                     break;
                  case 7:
                     (Objects.equals(fieldValue.toString(), field.get(defaultConf).toString()) ? sbDefaultOpts : sbDifferentOpts).append("\n * ").append(field.getName()).append(" : ").append(fieldValue);
                  case 8:
                     break;
                  default:
                     throw new IllegalArgumentException("field type not expected for fields " + field.getName());
                  }
               }
            }
         }

         String diff = sbDifferentOpts.toString();
         if (diff.isEmpty()) {
            sb.append("None\n");
         } else {
            sb.append(diff);
         }

         sb.append("\n\ndefault options :");
         String same = sbDefaultOpts.toString();
         if (same.isEmpty()) {
            sb.append("None\n");
         } else {
            sb.append(same);
         }
      } catch (IllegalAccessException | IllegalArgumentException var16) {
         throw new IllegalArgumentException("Wrong parsing", var16);
      }

      return sb.toString();
   }

   protected static String buildUrl(Configuration conf) {
      Configuration defaultConf = new Configuration();
      StringBuilder sb = new StringBuilder();
      sb.append("jdbc:mariadb:");
      if (conf.haMode != HaMode.NONE) {
         sb.append(conf.haMode.toString().toLowerCase(Locale.ROOT)).append(":");
      }

      sb.append("//");

      for(int i = 0; i < conf.addresses.size(); ++i) {
         HostAddress hostAddress = (HostAddress)conf.addresses.get(i);
         if (i > 0) {
            sb.append(",");
         }

         if (conf.haMode == HaMode.NONE && hostAddress.primary || conf.haMode == HaMode.REPLICATION && (i == 0 && hostAddress.primary || i != 0 && !hostAddress.primary)) {
            sb.append(hostAddress.host);
            if (hostAddress.port != 3306) {
               sb.append(":").append(hostAddress.port);
            }
         } else {
            sb.append("address=(host=").append(hostAddress.host).append(")").append("(port=").append(hostAddress.port).append(")");
            sb.append("(type=").append(hostAddress.primary ? "primary" : "replica").append(")");
         }
      }

      sb.append("/");
      if (conf.database != null) {
         sb.append(conf.database);
      }

      try {
         boolean first = true;
         Field[] fields = Configuration.class.getDeclaredFields();
         Field[] var5 = fields;
         int var6 = fields.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Field field = var5[var7];
            if (!"database".equals(field.getName()) && !"haMode".equals(field.getName()) && !"$jacocoData".equals(field.getName()) && !"addresses".equals(field.getName())) {
               Object obj = field.get(conf);
               if (obj != null && (!(obj instanceof Properties) || ((Properties)obj).size() > 0)) {
                  if ("password".equals(field.getName())) {
                     sb.append((char)(first ? '?' : '&'));
                     first = false;
                     sb.append(field.getName()).append('=');
                     sb.append("***");
                  } else if (field.getType().equals(String.class)) {
                     sb.append((char)(first ? '?' : '&'));
                     first = false;
                     sb.append(field.getName()).append('=');
                     sb.append((String)obj);
                  } else {
                     boolean firstProp;
                     if (field.getType().equals(Boolean.TYPE)) {
                        firstProp = field.getBoolean(defaultConf);
                        if (!obj.equals(firstProp)) {
                           sb.append((char)(first ? '?' : '&'));
                           first = false;
                           sb.append(field.getName()).append('=');
                           sb.append(obj);
                        }
                     } else if (field.getType().equals(Integer.TYPE)) {
                        try {
                           int defaultValue = field.getInt(defaultConf);
                           if (!obj.equals(defaultValue)) {
                              sb.append((char)(first ? '?' : '&'));
                              sb.append(field.getName()).append('=').append(obj);
                              first = false;
                           }
                        } catch (IllegalAccessException var14) {
                        }
                     } else if (field.getType().equals(Properties.class)) {
                        sb.append((char)(first ? '?' : '&'));
                        first = false;
                        firstProp = true;
                        Properties properties = (Properties)obj;
                        Iterator var12 = properties.keySet().iterator();

                        while(var12.hasNext()) {
                           Object key = var12.next();
                           if (firstProp) {
                              firstProp = false;
                           } else {
                              sb.append('&');
                           }

                           sb.append(key).append('=');
                           sb.append(properties.get(key));
                        }
                     } else {
                        Object defaultValue;
                        if (field.getType().equals(CredentialPlugin.class)) {
                           defaultValue = field.get(defaultConf);
                           if (!obj.equals(defaultValue)) {
                              sb.append((char)(first ? '?' : '&'));
                              first = false;
                              sb.append(field.getName()).append('=');
                              sb.append(((CredentialPlugin)obj).type());
                           }
                        } else {
                           defaultValue = field.get(defaultConf);
                           if (!obj.equals(defaultValue)) {
                              sb.append((char)(first ? '?' : '&'));
                              first = false;
                              sb.append(field.getName()).append('=');
                              sb.append(obj);
                           }
                        }
                     }
                  }
               }
            }
         }
      } catch (IllegalAccessException var15) {
         var15.printStackTrace();
      } catch (SecurityException var16) {
         throw new IllegalArgumentException("Security too restrictive : " + var16.getMessage());
      }

      conf.loadCodecs();
      return sb.toString();
   }

   private static String nullOrEmpty(String val) {
      return val != null && !val.isEmpty() ? val : null;
   }

   public Configuration clone(String username, String password) {
      return new Configuration(username != null && username.isEmpty() ? null : username, password != null && password.isEmpty() ? null : password, this.database, this.addresses, this.haMode, this.nonMappedOptions, this.timezone, this.autocommit, this.useMysqlMetadata, this.useCatalogTerm, this.createDatabaseIfNotExist, this.useLocalSessionState, this.returnMultiValuesGeneratedIds, this.transactionIsolation, this.defaultFetchSize, this.maxQuerySizeToLog, this.maxAllowedPacket, this.geometryDefaultType, this.restrictedAuth, this.initSql, this.socketFactory, this.connectTimeout, this.pipe, this.localSocket, this.tcpKeepAlive, this.uuidAsString, this.tcpKeepIdle, this.tcpKeepCount, this.tcpKeepInterval, this.tcpAbortiveClose, this.localSocketAddress, this.socketTimeout, this.useReadAheadInput, this.tlsSocketType, this.sslMode, this.serverSslCert, this.keyStore, this.keyStorePassword, this.keyPassword, this.keyStoreType, this.trustStoreType, this.enabledSslCipherSuites, this.enabledSslProtocolSuites, this.allowMultiQueries, this.allowLocalInfile, this.useCompression, this.useAffectedRows, this.useBulkStmts, this.useBulkStmtsForInserts, this.disablePipeline, this.cachePrepStmts, this.prepStmtCacheSize, this.useServerPrepStmts, this.credentialType, this.sessionVariables, this.connectionAttributes, this.servicePrincipalName, this.blankTableNameMeta, this.tinyInt1isBit, this.transformedBitIsBoolean, this.yearIsDateType, this.dumpQueriesOnException, this.includeInnodbStatusInDeadlockExceptions, this.includeThreadDumpInDeadlockExceptions, this.retriesAllDown, this.galeraAllowedState, this.transactionReplay, this.transactionReplaySize, this.pool, this.poolName, this.maxPoolSize, this.minPoolSize, this.maxIdleTime, this.registerJmxPool, this.poolValidMinDelay, this.useResetConnection, this.serverRsaPublicKeyFile, this.allowPublicKeyRetrieval);
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

   public String keyStorePassword() {
      return this.keyStorePassword;
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

   public SslMode sslMode() {
      return this.sslMode;
   }

   public TransactionIsolation transactionIsolation() {
      return this.transactionIsolation;
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

   public boolean dumpQueriesOnException() {
      return this.dumpQueriesOnException;
   }

   public int prepStmtCacheSize() {
      return this.prepStmtCacheSize;
   }

   public boolean useAffectedRows() {
      return this.useAffectedRows;
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
      ServiceLoader<Codec> loader = ServiceLoader.load(Codec.class, Configuration.class.getClassLoader());
      List<Codec<?>> result = new ArrayList();
      Iterator var10000 = loader.iterator();
      Objects.requireNonNull(result);
      var10000.forEachRemaining(result::add);
      this.codecs = (Codec[])result.toArray(new Codec[0]);
   }

   public int hashCode() {
      return this.initialUrl.hashCode();
   }

   // $FF: synthetic method
   Configuration(String x0, List x1, HaMode x2, String x3, String x4, String x5, String x6, Integer x7, String x8, String x9, Boolean x10, Boolean x11, Integer x12, Integer x13, Integer x14, Boolean x15, String x16, Integer x17, Boolean x18, Boolean x19, Boolean x20, Boolean x21, String x22, String x23, String x24, String x25, String x26, Boolean x27, Boolean x28, Boolean x29, String x30, Boolean x31, Integer x32, Boolean x33, Boolean x34, String x35, Boolean x36, Boolean x37, Boolean x38, Boolean x39, Boolean x40, String x41, Boolean x42, Boolean x43, Boolean x44, Boolean x45, Boolean x46, String x47, Integer x48, String x49, Integer x50, Integer x51, Integer x52, String x53, Boolean x54, String x55, Integer x56, Integer x57, Integer x58, Boolean x59, Integer x60, Boolean x61, String x62, Boolean x63, String x64, String x65, String x66, String x67, String x68, String x69, Boolean x70, Boolean x71, Boolean x72, Integer x73, String x74, String x75, String x76, Properties x77, Object x78) throws SQLException {
      this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10, x11, x12, x13, x14, x15, x16, x17, x18, x19, x20, x21, x22, x23, x24, x25, x26, x27, x28, x29, x30, x31, x32, x33, x34, x35, x36, x37, x38, x39, x40, x41, x42, x43, x44, x45, x46, x47, x48, x49, x50, x51, x52, x53, x54, x55, x56, x57, x58, x59, x60, x61, x62, x63, x64, x65, x66, x67, x68, x69, x70, x71, x72, x73, x74, x75, x76, x77);
   }

   public static final class Builder implements Cloneable {
      private Properties _nonMappedOptions;
      private HaMode _haMode;
      private List<HostAddress> _addresses = new ArrayList();
      private String user;
      private String password;
      private String database;
      private String timezone;
      private Boolean autocommit;
      private Boolean useMysqlMetadata;
      private String useCatalogTerm;
      private Boolean createDatabaseIfNotExist;
      private Boolean useLocalSessionState;
      private Boolean returnMultiValuesGeneratedIds;
      private Integer defaultFetchSize;
      private Integer maxQuerySizeToLog;
      private Integer maxAllowedPacket;
      private String geometryDefaultType;
      private String restrictedAuth;
      private String initSql;
      private String transactionIsolation;
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
      private String keyStorePassword;
      private String keyPassword;
      private String keyStoreType;
      private String trustStoreType;
      private String enabledSslCipherSuites;
      private String enabledSslProtocolSuites;
      private Boolean allowMultiQueries;
      private Boolean allowLocalInfile;
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

      public Configuration.Builder keyStorePassword(String keyStorePassword) {
         this.keyStorePassword = Configuration.nullOrEmpty(keyStorePassword);
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

      public Configuration.Builder addHost(String host, int port, boolean master) {
         this._addresses.add(HostAddress.from(Configuration.nullOrEmpty(host), port, master));
         return this;
      }

      public Configuration.Builder addresses(HostAddress... hostAddress) {
         this._addresses = new ArrayList();
         this._addresses.addAll(Arrays.asList(hostAddress));
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

      public Configuration.Builder useCompression(Boolean useCompression) {
         this.useCompression = useCompression;
         return this;
      }

      public Configuration.Builder blankTableNameMeta(Boolean blankTableNameMeta) {
         this.blankTableNameMeta = blankTableNameMeta;
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

      public Configuration build() throws SQLException {
         Configuration conf = new Configuration(this.database, this._addresses, this._haMode, this.user, this.password, this.enabledSslProtocolSuites, this.socketFactory, this.connectTimeout, this.pipe, this.localSocket, this.tcpKeepAlive, this.uuidAsString, this.tcpKeepIdle, this.tcpKeepCount, this.tcpKeepInterval, this.tcpAbortiveClose, this.localSocketAddress, this.socketTimeout, this.allowMultiQueries, this.allowLocalInfile, this.useCompression, this.blankTableNameMeta, this.credentialType, this.sslMode, this.transactionIsolation, this.enabledSslCipherSuites, this.sessionVariables, this.tinyInt1isBit, this.transformedBitIsBoolean, this.yearIsDateType, this.timezone, this.dumpQueriesOnException, this.prepStmtCacheSize, this.useAffectedRows, this.useServerPrepStmts, this.connectionAttributes, this.useBulkStmts, this.useBulkStmtsForInserts, this.disablePipeline, this.autocommit, this.useMysqlMetadata, this.useCatalogTerm, this.createDatabaseIfNotExist, this.useLocalSessionState, this.returnMultiValuesGeneratedIds, this.includeInnodbStatusInDeadlockExceptions, this.includeThreadDumpInDeadlockExceptions, this.servicePrincipalName, this.defaultFetchSize, this.tlsSocketType, this.maxQuerySizeToLog, this.maxAllowedPacket, this.retriesAllDown, this.galeraAllowedState, this.pool, this.poolName, this.maxPoolSize, this.minPoolSize, this.maxIdleTime, this.registerJmxPool, this.poolValidMinDelay, this.useResetConnection, this.serverRsaPublicKeyFile, this.allowPublicKeyRetrieval, this.serverSslCert, this.keyStore, this.keyStorePassword, this.keyPassword, this.keyStoreType, this.trustStoreType, this.useReadAheadInput, this.cachePrepStmts, this.transactionReplay, this.transactionReplaySize, this.geometryDefaultType, this.restrictedAuth, this.initSql, this._nonMappedOptions);
         conf.initialUrl = Configuration.buildUrl(conf);
         return conf;
      }
   }
}
