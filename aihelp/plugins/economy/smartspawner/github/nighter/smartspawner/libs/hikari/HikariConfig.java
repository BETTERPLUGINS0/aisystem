package github.nighter.smartspawner.libs.hikari;

import com.codahale.metrics.health.HealthCheckRegistry;
import github.nighter.smartspawner.libs.hikari.metrics.MetricsTrackerFactory;
import github.nighter.smartspawner.libs.hikari.util.Credentials;
import github.nighter.smartspawner.libs.hikari.util.PropertyElf;
import github.nighter.smartspawner.libs.hikari.util.UtilityElf;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessControlException;
import java.sql.Driver;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HikariConfig implements HikariConfigMXBean {
   private static final Logger LOGGER = LoggerFactory.getLogger(HikariConfig.class);
   private static final char[] ID_CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
   private static final long CONNECTION_TIMEOUT;
   private static final long VALIDATION_TIMEOUT;
   private static final long SOFT_TIMEOUT_FLOOR;
   private static final long IDLE_TIMEOUT;
   private static final long MAX_LIFETIME;
   private static final long DEFAULT_KEEPALIVE_TIME;
   private static final int DEFAULT_POOL_SIZE = 10;
   private static boolean unitTest;
   private volatile String catalog;
   private volatile long connectionTimeout;
   private volatile long validationTimeout;
   private volatile long idleTimeout;
   private volatile long leakDetectionThreshold;
   private volatile long maxLifetime;
   private volatile int maxPoolSize;
   private volatile int minIdle;
   private final AtomicReference<Credentials> credentials;
   private long initializationFailTimeout;
   private String connectionInitSql;
   private String connectionTestQuery;
   private String credentialsProviderClassName;
   private String dataSourceClassName;
   private String dataSourceJndiName;
   private String driverClassName;
   private String exceptionOverrideClassName;
   private SQLExceptionOverride exceptionOverride;
   private String jdbcUrl;
   private String poolName;
   private String schema;
   private String transactionIsolationName;
   private boolean isAutoCommit;
   private boolean isReadOnly;
   private boolean isIsolateInternalQueries;
   private boolean isRegisterMbeans;
   private boolean isAllowPoolSuspension;
   private HikariCredentialsProvider credentialsProvider;
   private DataSource dataSource;
   private Properties dataSourceProperties;
   private ThreadFactory threadFactory;
   private ScheduledExecutorService scheduledExecutor;
   private MetricsTrackerFactory metricsTrackerFactory;
   private Object metricRegistry;
   private Object healthCheckRegistry;
   private Properties healthCheckProperties;
   private long keepaliveTime;
   private volatile boolean sealed;

   public HikariConfig() {
      this.credentials = new AtomicReference(Credentials.of((String)null, (String)null));
      this.dataSourceProperties = new Properties();
      this.healthCheckProperties = new Properties();
      this.minIdle = -1;
      this.maxPoolSize = 10;
      this.maxLifetime = MAX_LIFETIME;
      this.connectionTimeout = CONNECTION_TIMEOUT;
      this.validationTimeout = VALIDATION_TIMEOUT;
      this.idleTimeout = IDLE_TIMEOUT;
      this.initializationFailTimeout = 1L;
      this.isAutoCommit = true;
      this.keepaliveTime = DEFAULT_KEEPALIVE_TIME;
      String systemProp = System.getProperty("hikaricp.configurationFile");
      if (systemProp != null) {
         this.loadProperties(systemProp);
      }

   }

   public HikariConfig(Properties properties) {
      this();
      PropertyElf.setTargetFromProperties(this, properties);
   }

   public HikariConfig(String propertyFileName) {
      this();
      this.loadProperties(propertyFileName);
   }

   public String getCatalog() {
      return this.catalog;
   }

   public void setCatalog(String catalog) {
      this.catalog = catalog;
   }

   public long getConnectionTimeout() {
      return this.connectionTimeout;
   }

   public void setConnectionTimeout(long connectionTimeoutMs) {
      if (connectionTimeoutMs == 0L) {
         this.connectionTimeout = 2147483647L;
      } else {
         if (connectionTimeoutMs < SOFT_TIMEOUT_FLOOR) {
            throw new IllegalArgumentException("connectionTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
         }

         this.connectionTimeout = connectionTimeoutMs;
      }

   }

   public long getIdleTimeout() {
      return this.idleTimeout;
   }

   public void setIdleTimeout(long idleTimeoutMs) {
      if (idleTimeoutMs < 0L) {
         throw new IllegalArgumentException("idleTimeout cannot be negative");
      } else {
         this.idleTimeout = idleTimeoutMs;
      }
   }

   public long getLeakDetectionThreshold() {
      return this.leakDetectionThreshold;
   }

   public void setLeakDetectionThreshold(long leakDetectionThresholdMs) {
      this.leakDetectionThreshold = leakDetectionThresholdMs;
   }

   public long getMaxLifetime() {
      return this.maxLifetime;
   }

   public void setMaxLifetime(long maxLifetimeMs) {
      this.maxLifetime = maxLifetimeMs;
   }

   public int getMaximumPoolSize() {
      return this.maxPoolSize;
   }

   public void setMaximumPoolSize(int maxPoolSize) {
      if (maxPoolSize < 1) {
         throw new IllegalArgumentException("maxPoolSize cannot be less than 1");
      } else {
         this.maxPoolSize = maxPoolSize;
      }
   }

   public int getMinimumIdle() {
      return this.minIdle;
   }

   public void setMinimumIdle(int minIdle) {
      if (minIdle < 0) {
         throw new IllegalArgumentException("minimumIdle cannot be negative");
      } else {
         this.minIdle = minIdle;
      }
   }

   public String getPassword() {
      return ((Credentials)this.credentials.get()).getPassword();
   }

   public void setPassword(String password) {
      this.credentials.updateAndGet((current) -> {
         return Credentials.of(current.getUsername(), password);
      });
   }

   public String getUsername() {
      return ((Credentials)this.credentials.get()).getUsername();
   }

   public void setUsername(String username) {
      this.credentials.updateAndGet((current) -> {
         return Credentials.of(username, current.getPassword());
      });
   }

   public void setCredentials(Credentials credentials) {
      this.credentials.set(credentials);
   }

   public Credentials getCredentials() {
      return (Credentials)this.credentials.get();
   }

   public long getValidationTimeout() {
      return this.validationTimeout;
   }

   public void setValidationTimeout(long validationTimeoutMs) {
      if (validationTimeoutMs < SOFT_TIMEOUT_FLOOR) {
         throw new IllegalArgumentException("validationTimeout cannot be less than " + SOFT_TIMEOUT_FLOOR + "ms");
      } else {
         this.validationTimeout = validationTimeoutMs;
      }
   }

   public String getConnectionTestQuery() {
      return this.connectionTestQuery;
   }

   public void setConnectionTestQuery(String connectionTestQuery) {
      this.checkIfSealed();
      this.connectionTestQuery = connectionTestQuery;
   }

   public String getConnectionInitSql() {
      return this.connectionInitSql;
   }

   public void setConnectionInitSql(String connectionInitSql) {
      this.checkIfSealed();
      this.connectionInitSql = connectionInitSql;
   }

   public DataSource getDataSource() {
      return this.dataSource;
   }

   public void setDataSource(DataSource dataSource) {
      this.checkIfSealed();
      this.dataSource = dataSource;
   }

   public String getDataSourceClassName() {
      return this.dataSourceClassName;
   }

   public void setDataSourceClassName(String className) {
      this.checkIfSealed();
      this.dataSourceClassName = className;
   }

   public void addDataSourceProperty(String propertyName, Object value) {
      this.checkIfSealed();
      this.dataSourceProperties.put(propertyName, value);
   }

   public String getDataSourceJNDI() {
      return this.dataSourceJndiName;
   }

   public void setDataSourceJNDI(String jndiDataSource) {
      this.checkIfSealed();
      this.dataSourceJndiName = jndiDataSource;
   }

   public Properties getDataSourceProperties() {
      return this.dataSourceProperties;
   }

   public void setDataSourceProperties(Properties dsProperties) {
      this.checkIfSealed();
      this.dataSourceProperties.putAll(dsProperties);
   }

   public String getDriverClassName() {
      return this.driverClassName;
   }

   public void setDriverClassName(String driverClassName) {
      this.checkIfSealed();

      try {
         UtilityElf.createInstance(driverClassName, Driver.class);
         this.driverClassName = driverClassName;
      } catch (Exception var3) {
         throw new RuntimeException("Failed to load driver class " + driverClassName, var3);
      }
   }

   public String getJdbcUrl() {
      return this.jdbcUrl;
   }

   public void setJdbcUrl(String jdbcUrl) {
      this.checkIfSealed();
      this.jdbcUrl = jdbcUrl;
   }

   public boolean isAutoCommit() {
      return this.isAutoCommit;
   }

   public void setAutoCommit(boolean isAutoCommit) {
      this.checkIfSealed();
      this.isAutoCommit = isAutoCommit;
   }

   public boolean isAllowPoolSuspension() {
      return this.isAllowPoolSuspension;
   }

   public void setAllowPoolSuspension(boolean isAllowPoolSuspension) {
      this.checkIfSealed();
      this.isAllowPoolSuspension = isAllowPoolSuspension;
   }

   public long getInitializationFailTimeout() {
      return this.initializationFailTimeout;
   }

   public void setInitializationFailTimeout(long initializationFailTimeout) {
      this.checkIfSealed();
      this.initializationFailTimeout = initializationFailTimeout;
   }

   public boolean isIsolateInternalQueries() {
      return this.isIsolateInternalQueries;
   }

   public void setIsolateInternalQueries(boolean isolate) {
      this.checkIfSealed();
      this.isIsolateInternalQueries = isolate;
   }

   public MetricsTrackerFactory getMetricsTrackerFactory() {
      return this.metricsTrackerFactory;
   }

   public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
      if (this.metricRegistry != null) {
         throw new IllegalStateException("cannot use setMetricsTrackerFactory() and setMetricRegistry() together");
      } else {
         this.metricsTrackerFactory = metricsTrackerFactory;
      }
   }

   public Object getMetricRegistry() {
      return this.metricRegistry;
   }

   public void setMetricRegistry(Object metricRegistry) {
      if (this.metricsTrackerFactory != null) {
         throw new IllegalStateException("cannot use setMetricRegistry() and setMetricsTrackerFactory() together");
      } else {
         if (metricRegistry != null) {
            metricRegistry = this.getObjectOrPerformJndiLookup(metricRegistry);
            if (!UtilityElf.safeIsAssignableFrom(metricRegistry, "com.codahale.metrics.MetricRegistry") && !UtilityElf.safeIsAssignableFrom(metricRegistry, "io.dropwizard.metrics5.MetricRegistry") && !UtilityElf.safeIsAssignableFrom(metricRegistry, "io.micrometer.core.instrument.MeterRegistry")) {
               throw new IllegalArgumentException("Class must be instance of com.codahale.metrics.MetricRegistry, io.dropwizard.metrics5.MetricRegistry, or io.micrometer.core.instrument.MeterRegistry");
            }
         }

         this.metricRegistry = metricRegistry;
      }
   }

   public Object getHealthCheckRegistry() {
      return this.healthCheckRegistry;
   }

   public void setHealthCheckRegistry(Object healthCheckRegistry) {
      this.checkIfSealed();
      if (healthCheckRegistry != null) {
         healthCheckRegistry = this.getObjectOrPerformJndiLookup(healthCheckRegistry);
         if (!(healthCheckRegistry instanceof HealthCheckRegistry)) {
            throw new IllegalArgumentException("Class must be an instance of com.codahale.metrics.health.HealthCheckRegistry");
         }
      }

      this.healthCheckRegistry = healthCheckRegistry;
   }

   public Properties getHealthCheckProperties() {
      return this.healthCheckProperties;
   }

   public void setHealthCheckProperties(Properties healthCheckProperties) {
      this.checkIfSealed();
      this.healthCheckProperties.putAll(healthCheckProperties);
   }

   public void addHealthCheckProperty(String key, String value) {
      this.checkIfSealed();
      this.healthCheckProperties.setProperty(key, value);
   }

   public long getKeepaliveTime() {
      return this.keepaliveTime;
   }

   public void setKeepaliveTime(long keepaliveTimeMs) {
      this.keepaliveTime = keepaliveTimeMs;
   }

   public boolean isReadOnly() {
      return this.isReadOnly;
   }

   public void setReadOnly(boolean readOnly) {
      this.checkIfSealed();
      this.isReadOnly = readOnly;
   }

   public boolean isRegisterMbeans() {
      return this.isRegisterMbeans;
   }

   public void setRegisterMbeans(boolean register) {
      this.checkIfSealed();
      this.isRegisterMbeans = register;
   }

   public String getPoolName() {
      return this.poolName;
   }

   public void setPoolName(String poolName) {
      this.checkIfSealed();
      this.poolName = poolName;
   }

   public ScheduledExecutorService getScheduledExecutor() {
      return this.scheduledExecutor;
   }

   public void setScheduledExecutor(ScheduledExecutorService executor) {
      this.checkIfSealed();
      this.scheduledExecutor = executor;
   }

   public String getTransactionIsolation() {
      return this.transactionIsolationName;
   }

   public String getSchema() {
      return this.schema;
   }

   public void setSchema(String schema) {
      this.checkIfSealed();
      this.schema = schema;
   }

   public String getCredentialsProviderClassName() {
      return this.credentialsProviderClassName;
   }

   public void setCredentialsProviderClassName(String credentialsProviderClassName) {
      this.checkIfSealed();

      try {
         this.credentialsProvider = (HikariCredentialsProvider)UtilityElf.createInstance(credentialsProviderClassName, HikariCredentialsProvider.class);
         this.exceptionOverrideClassName = credentialsProviderClassName;
      } catch (Exception var3) {
         throw new RuntimeException("Failed to instantiate class " + credentialsProviderClassName, var3);
      }
   }

   public HikariCredentialsProvider getCredentialsProvider() {
      return this.credentialsProvider;
   }

   public void setCredentialsProvider(HikariCredentialsProvider credentialsProvider) {
      this.checkIfSealed();
      this.credentialsProvider = credentialsProvider;
   }

   public String getExceptionOverrideClassName() {
      return this.exceptionOverrideClassName;
   }

   public void setExceptionOverrideClassName(String exceptionOverrideClassName) {
      this.checkIfSealed();

      try {
         this.exceptionOverride = (SQLExceptionOverride)UtilityElf.createInstance(exceptionOverrideClassName, SQLExceptionOverride.class);
         this.exceptionOverrideClassName = exceptionOverrideClassName;
      } catch (Exception var3) {
         throw new RuntimeException("Failed to instantiate class " + exceptionOverrideClassName, var3);
      }
   }

   public SQLExceptionOverride getExceptionOverride() {
      return this.exceptionOverride;
   }

   public void setExceptionOverride(SQLExceptionOverride exceptionOverride) {
      this.checkIfSealed();
      this.exceptionOverride = exceptionOverride;
   }

   public void setTransactionIsolation(String isolationLevel) {
      this.checkIfSealed();
      this.transactionIsolationName = isolationLevel;
   }

   public ThreadFactory getThreadFactory() {
      return this.threadFactory;
   }

   public void setThreadFactory(ThreadFactory threadFactory) {
      this.checkIfSealed();
      this.threadFactory = threadFactory;
   }

   void seal() {
      this.sealed = true;
   }

   public void copyStateTo(HikariConfig other) {
      Field[] var2 = HikariConfig.class.getDeclaredFields();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field field = var2[var4];

         try {
            if (!Modifier.isFinal(field.getModifiers())) {
               field.setAccessible(true);
               field.set(other, field.get(this));
            } else if (field.getType().isAssignableFrom(AtomicReference.class)) {
               ((AtomicReference)field.get(other)).set(((AtomicReference)field.get(this)).get());
            }
         } catch (Exception var7) {
            throw new RuntimeException("Failed to copy HikariConfig state: " + var7.getMessage(), var7);
         }
      }

      other.sealed = false;
   }

   public void validate() {
      if (this.poolName == null) {
         this.poolName = this.generatePoolName();
      } else if (this.isRegisterMbeans && this.poolName.contains(":")) {
         throw new IllegalArgumentException("poolName cannot contain ':' when used with JMX");
      }

      this.catalog = UtilityElf.getNullIfEmpty(this.catalog);
      this.connectionInitSql = UtilityElf.getNullIfEmpty(this.connectionInitSql);
      this.connectionTestQuery = UtilityElf.getNullIfEmpty(this.connectionTestQuery);
      this.transactionIsolationName = UtilityElf.getNullIfEmpty(this.transactionIsolationName);
      this.dataSourceClassName = UtilityElf.getNullIfEmpty(this.dataSourceClassName);
      this.dataSourceJndiName = UtilityElf.getNullIfEmpty(this.dataSourceJndiName);
      this.driverClassName = UtilityElf.getNullIfEmpty(this.driverClassName);
      this.jdbcUrl = UtilityElf.getNullIfEmpty(this.jdbcUrl);
      if (this.dataSource != null) {
         if (this.dataSourceClassName != null) {
            LOGGER.warn("{} - using dataSource and ignoring dataSourceClassName.", this.poolName);
         }
      } else if (this.dataSourceClassName != null) {
         if (this.driverClassName != null) {
            LOGGER.error("{} - cannot use driverClassName and dataSourceClassName together.", this.poolName);
            throw new IllegalStateException("cannot use driverClassName and dataSourceClassName together.");
         }

         if (this.jdbcUrl != null) {
            LOGGER.warn("{} - using dataSourceClassName and ignoring jdbcUrl.", this.poolName);
         }
      } else if (this.jdbcUrl == null && this.dataSourceJndiName == null) {
         if (this.driverClassName != null) {
            LOGGER.error("{} - jdbcUrl is required with driverClassName.", this.poolName);
            throw new IllegalArgumentException("jdbcUrl is required with driverClassName.");
         }

         LOGGER.error("{} - dataSource or dataSourceClassName or jdbcUrl is required.", this.poolName);
         throw new IllegalArgumentException("dataSource or dataSourceClassName or jdbcUrl is required.");
      }

      this.validateNumerics();
      if (LOGGER.isDebugEnabled() || unitTest) {
         this.logConfiguration();
      }

   }

   private void validateNumerics() {
      if (this.maxLifetime != 0L && this.maxLifetime < TimeUnit.SECONDS.toMillis(30L)) {
         LOGGER.warn("{} - maxLifetime is less than 30000ms, setting to default {}ms.", this.poolName, MAX_LIFETIME);
         this.maxLifetime = MAX_LIFETIME;
      }

      if (this.keepaliveTime != 0L && this.keepaliveTime < TimeUnit.SECONDS.toMillis(30L)) {
         LOGGER.warn("{} - keepaliveTime is less than 30000ms, disabling it.", this.poolName);
         this.keepaliveTime = 0L;
      }

      if (this.keepaliveTime != 0L && this.maxLifetime != 0L && this.keepaliveTime >= this.maxLifetime) {
         LOGGER.warn("{} - keepaliveTime is greater than or equal to maxLifetime, disabling it.", this.poolName);
         this.keepaliveTime = 0L;
      }

      if (this.leakDetectionThreshold > 0L && !unitTest && (this.leakDetectionThreshold < TimeUnit.SECONDS.toMillis(2L) || this.leakDetectionThreshold > this.maxLifetime && this.maxLifetime > 0L)) {
         LOGGER.warn("{} - leakDetectionThreshold is less than 2000ms or more than maxLifetime, disabling it.", this.poolName);
         this.leakDetectionThreshold = 0L;
      }

      if (this.connectionTimeout < SOFT_TIMEOUT_FLOOR) {
         LOGGER.warn("{} - connectionTimeout is less than {}ms, setting to {}ms.", new Object[]{this.poolName, SOFT_TIMEOUT_FLOOR, CONNECTION_TIMEOUT});
         this.connectionTimeout = CONNECTION_TIMEOUT;
      }

      if (this.validationTimeout < SOFT_TIMEOUT_FLOOR) {
         LOGGER.warn("{} - validationTimeout is less than {}ms, setting to {}ms.", new Object[]{this.poolName, SOFT_TIMEOUT_FLOOR, VALIDATION_TIMEOUT});
         this.validationTimeout = VALIDATION_TIMEOUT;
      }

      if (this.minIdle < 0 || this.minIdle > this.maxPoolSize) {
         this.minIdle = this.maxPoolSize;
      }

      if (this.idleTimeout + TimeUnit.SECONDS.toMillis(1L) > this.maxLifetime && this.maxLifetime > 0L && this.minIdle < this.maxPoolSize) {
         LOGGER.warn("{} - idleTimeout is close to or more than maxLifetime, disabling it.", this.poolName);
         this.idleTimeout = 0L;
      } else if (this.idleTimeout != 0L && this.idleTimeout < TimeUnit.SECONDS.toMillis(10L) && this.minIdle < this.maxPoolSize) {
         LOGGER.warn("{} - idleTimeout is less than 10000ms, setting to default {}ms.", this.poolName, IDLE_TIMEOUT);
         this.idleTimeout = IDLE_TIMEOUT;
      } else if (this.idleTimeout != IDLE_TIMEOUT && this.idleTimeout != 0L && this.minIdle == this.maxPoolSize) {
         LOGGER.warn("{} - idleTimeout has been set but has no effect because the pool is operating as a fixed size pool.", this.poolName);
      }

   }

   private void checkIfSealed() {
      if (this.sealed) {
         throw new IllegalStateException("The configuration of the pool is sealed once started. Use HikariConfigMXBean for runtime changes.");
      }
   }

   private void logConfiguration() {
      LOGGER.debug("{} - configuration:", this.poolName);
      TreeSet<String> propertyNames = new TreeSet(PropertyElf.getPropertyNames(HikariConfig.class));
      Iterator var2 = propertyNames.iterator();

      while(var2.hasNext()) {
         String prop = (String)var2.next();

         try {
            Object value = PropertyElf.getProperty(prop, this);
            if ("dataSourceProperties".equals(prop)) {
               Properties dsProps = PropertyElf.copyProperties(this.dataSourceProperties);
               dsProps.setProperty("password", "<masked>");
               value = dsProps;
            }

            if ("initializationFailTimeout".equals(prop) && this.initializationFailTimeout == Long.MAX_VALUE) {
               value = "infinite";
            } else if ("transactionIsolation".equals(prop) && this.transactionIsolationName == null) {
               value = "default";
            } else if (prop.matches("scheduledExecutorService|threadFactory") && value == null) {
               value = "internal";
            } else if (prop.contains("jdbcUrl") && value instanceof String) {
               value = UtilityElf.maskPasswordInJdbcUrl((String)value);
            } else if (prop.contains("password")) {
               value = "<masked>";
            } else if (value instanceof String) {
               value = "\"" + value + "\"";
            } else if (value == null) {
               value = "none";
            }

            LOGGER.debug("{}{}", (prop + "................................................").substring(0, 32), value);
         } catch (Exception var6) {
         }
      }

   }

   private void loadProperties(String propertyFileName) {
      try {
         InputStream is = this.openPropertiesInputStream(propertyFileName);

         try {
            if (is == null) {
               throw new IllegalArgumentException("Cannot find property file: " + propertyFileName);
            }

            Properties props = new Properties();
            props.load(is);
            PropertyElf.setTargetFromProperties(this, props);
         } catch (Throwable var6) {
            if (is != null) {
               try {
                  is.close();
               } catch (Throwable var5) {
                  var6.addSuppressed(var5);
               }
            }

            throw var6;
         }

         if (is != null) {
            is.close();
         }

      } catch (IOException var7) {
         throw new RuntimeException("Failed to read property file", var7);
      }
   }

   private InputStream openPropertiesInputStream(String propertyFileName) throws FileNotFoundException {
      File propFile = new File(propertyFileName);
      if (propFile.isFile()) {
         return new FileInputStream(propFile);
      } else {
         InputStream propertiesInputStream = this.getClass().getResourceAsStream(propertyFileName);
         if (propertiesInputStream == null) {
            propertiesInputStream = this.getClass().getClassLoader().getResourceAsStream(propertyFileName);
         }

         return propertiesInputStream;
      }
   }

   private String generatePoolName() {
      String var1 = "HikariPool-";

      try {
         synchronized(System.getProperties()) {
            String next = String.valueOf(Integer.getInteger("github.nighter.smartspawner.libs.hikari.pool_number", 0) + 1);
            System.setProperty("github.nighter.smartspawner.libs.hikari.pool_number", next);
            return "HikariPool-" + next;
         }
      } catch (AccessControlException var7) {
         ThreadLocalRandom random = ThreadLocalRandom.current();
         StringBuilder buf = new StringBuilder("HikariPool-");

         for(int i = 0; i < 4; ++i) {
            buf.append(ID_CHARACTERS[random.nextInt(62)]);
         }

         LOGGER.info("assigned random pool name '{}' (security manager prevented access to system properties)", buf);
         return buf.toString();
      }
   }

   private Object getObjectOrPerformJndiLookup(Object object) {
      if (object instanceof String) {
         try {
            InitialContext initCtx = new InitialContext();
            return initCtx.lookup((String)object);
         } catch (NamingException var3) {
            throw new IllegalArgumentException(var3);
         }
      } else {
         return object;
      }
   }

   static {
      CONNECTION_TIMEOUT = TimeUnit.SECONDS.toMillis(30L);
      VALIDATION_TIMEOUT = TimeUnit.SECONDS.toMillis(5L);
      SOFT_TIMEOUT_FLOOR = Long.getLong("github.nighter.smartspawner.libs.hikari.timeoutMs.floor", 250L);
      IDLE_TIMEOUT = TimeUnit.MINUTES.toMillis(10L);
      MAX_LIFETIME = TimeUnit.MINUTES.toMillis(30L);
      DEFAULT_KEEPALIVE_TIME = TimeUnit.MINUTES.toMillis(2L);
      unitTest = false;
   }
}
