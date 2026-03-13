package github.nighter.smartspawner.spawner.data.database;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.libs.hikari.HikariConfig;
import github.nighter.smartspawner.libs.hikari.HikariDataSource;
import github.nighter.smartspawner.spawner.data.storage.StorageMode;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
   private final SmartSpawner plugin;
   private final Logger logger;
   private final StorageMode storageMode;
   private HikariDataSource dataSource;
   private final String host;
   private final int port;
   private final String database;
   private final String username;
   private final String password;
   private final String serverName;
   private final String sqliteFile;
   private final int maxPoolSize;
   private final int minIdle;
   private final long connectionTimeout;
   private final long maxLifetime;
   private final long idleTimeout;
   private final long keepaliveTime;
   private final long leakDetectionThreshold;
   private static final String CREATE_TABLE_MYSQL = "CREATE TABLE IF NOT EXISTS smart_spawners (\n    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n    spawner_id VARCHAR(64) NOT NULL,\n    server_name VARCHAR(64) NOT NULL,\n\n    -- Location (separate columns for indexing)\n    world_name VARCHAR(128) NOT NULL,\n    loc_x INT NOT NULL,\n    loc_y INT NOT NULL,\n    loc_z INT NOT NULL,\n\n    -- Entity data\n    entity_type VARCHAR(64) NOT NULL,\n    item_spawner_material VARCHAR(64) DEFAULT NULL,\n\n    -- Settings\n    spawner_exp INT NOT NULL DEFAULT 0,\n    spawner_active BOOLEAN NOT NULL DEFAULT TRUE,\n    spawner_range INT NOT NULL DEFAULT 16,\n    spawner_stop BOOLEAN NOT NULL DEFAULT TRUE,\n    spawn_delay BIGINT NOT NULL DEFAULT 500,\n    max_spawner_loot_slots INT NOT NULL DEFAULT 45,\n    max_stored_exp INT NOT NULL DEFAULT 1000,\n    min_mobs INT NOT NULL DEFAULT 1,\n    max_mobs INT NOT NULL DEFAULT 4,\n    stack_size INT NOT NULL DEFAULT 1,\n    max_stack_size INT NOT NULL DEFAULT 1000,\n    last_spawn_time BIGINT NOT NULL DEFAULT 0,\n    is_at_capacity BOOLEAN NOT NULL DEFAULT FALSE,\n\n    -- Player interaction\n    last_interacted_player VARCHAR(64) DEFAULT NULL,\n    preferred_sort_item VARCHAR(64) DEFAULT NULL,\n    filtered_items TEXT DEFAULT NULL,\n\n    -- Inventory (JSON blob)\n    inventory_data MEDIUMTEXT DEFAULT NULL,\n\n    -- Timestamps\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n\n    -- Indexes\n    UNIQUE KEY uk_server_spawner (server_name, spawner_id),\n    UNIQUE KEY uk_location (server_name, world_name, loc_x, loc_y, loc_z),\n    INDEX idx_server (server_name),\n    INDEX idx_world (server_name, world_name)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4\n";
   private static final String CREATE_TABLE_SQLITE = "CREATE TABLE IF NOT EXISTS smart_spawners (\n    id INTEGER PRIMARY KEY AUTOINCREMENT,\n    spawner_id VARCHAR(64) NOT NULL,\n    server_name VARCHAR(64) NOT NULL,\n\n    -- Location (separate columns for indexing)\n    world_name VARCHAR(128) NOT NULL,\n    loc_x INT NOT NULL,\n    loc_y INT NOT NULL,\n    loc_z INT NOT NULL,\n\n    -- Entity data\n    entity_type VARCHAR(64) NOT NULL,\n    item_spawner_material VARCHAR(64) DEFAULT NULL,\n\n    -- Settings\n    spawner_exp INT NOT NULL DEFAULT 0,\n    spawner_active BOOLEAN NOT NULL DEFAULT 1,\n    spawner_range INT NOT NULL DEFAULT 16,\n    spawner_stop BOOLEAN NOT NULL DEFAULT 1,\n    spawn_delay BIGINT NOT NULL DEFAULT 500,\n    max_spawner_loot_slots INT NOT NULL DEFAULT 45,\n    max_stored_exp INT NOT NULL DEFAULT 1000,\n    min_mobs INT NOT NULL DEFAULT 1,\n    max_mobs INT NOT NULL DEFAULT 4,\n    stack_size INT NOT NULL DEFAULT 1,\n    max_stack_size INT NOT NULL DEFAULT 1000,\n    last_spawn_time BIGINT NOT NULL DEFAULT 0,\n    is_at_capacity BOOLEAN NOT NULL DEFAULT 0,\n\n    -- Player interaction\n    last_interacted_player VARCHAR(64) DEFAULT NULL,\n    preferred_sort_item VARCHAR(64) DEFAULT NULL,\n    filtered_items TEXT DEFAULT NULL,\n\n    -- Inventory (JSON blob)\n    inventory_data TEXT DEFAULT NULL,\n\n    -- Timestamps\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n\n    -- Unique constraints\n    UNIQUE (server_name, spawner_id),\n    UNIQUE (server_name, world_name, loc_x, loc_y, loc_z)\n)\n";
   private static final String CREATE_INDEX_SERVER_SQLITE = "CREATE INDEX IF NOT EXISTS idx_server ON smart_spawners (server_name)";
   private static final String CREATE_INDEX_WORLD_SQLITE = "CREATE INDEX IF NOT EXISTS idx_world ON smart_spawners (server_name, world_name)";

   public DatabaseManager(SmartSpawner plugin, StorageMode storageMode) {
      this.plugin = plugin;
      this.logger = plugin.getLogger();
      this.storageMode = storageMode;
      this.host = plugin.getConfig().getString("database.sql.host", "localhost");
      this.port = plugin.getConfig().getInt("database.sql.port", 3306);
      this.database = plugin.getConfig().getString("database.database", "smartspawner");
      this.username = plugin.getConfig().getString("database.sql.username", "root");
      this.password = plugin.getConfig().getString("database.sql.password", "");
      this.serverName = plugin.getConfig().getString("database.server_name", "server1");
      this.sqliteFile = plugin.getConfig().getString("database.sqlite.file", "spawners.db");
      this.maxPoolSize = plugin.getConfig().getInt("database.sql.pool.maximum-size", 10);
      this.minIdle = plugin.getConfig().getInt("database.sql.pool.minimum-idle", 2);
      this.connectionTimeout = plugin.getConfig().getLong("database.sql.pool.connection-timeout", 10000L);
      this.maxLifetime = plugin.getConfig().getLong("database.sql.pool.max-lifetime", 1800000L);
      this.idleTimeout = plugin.getConfig().getLong("database.sql.pool.idle-timeout", 600000L);
      this.keepaliveTime = plugin.getConfig().getLong("database.sql.pool.keepalive-time", 30000L);
      this.leakDetectionThreshold = plugin.getConfig().getLong("database.sql.pool.leak-detection-threshold", 0L);
   }

   public boolean initialize() {
      try {
         this.setupDataSource();
         this.createTables();
         this.logger.info("Database connection pool initialized successfully.");
         return true;
      } catch (Exception var2) {
         this.logger.log(Level.SEVERE, "Failed to initialize database connection pool", var2);
         return false;
      }
   }

   private void setupDataSource() {
      HikariConfig config = new HikariConfig();
      if (this.storageMode == StorageMode.SQLITE) {
         this.setupSQLiteDataSource(config);
      } else {
         this.setupMySQLDataSource(config);
      }

      this.dataSource = new HikariDataSource(config);
   }

   private void setupMySQLDataSource(HikariConfig config) {
      String jdbcUrl = String.format("jdbc:mariadb://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", this.host, this.port, this.database);
      config.setJdbcUrl(jdbcUrl);
      config.setDriverClassName("github.nighter.smartspawner.libs.mariadb.Driver");
      config.setUsername(this.username);
      config.setPassword(this.password);
      config.setMaximumPoolSize(this.maxPoolSize);
      config.setMinimumIdle(this.minIdle);
      config.setConnectionTimeout(this.connectionTimeout);
      config.setMaxLifetime(this.maxLifetime);
      config.setIdleTimeout(this.idleTimeout);
      config.setKeepaliveTime(this.keepaliveTime);
      config.setLeakDetectionThreshold(this.leakDetectionThreshold);
      config.setPoolName("SmartSpawner-HikariCP");
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      config.addDataSourceProperty("useServerPrepStmts", "true");
      config.addDataSourceProperty("useLocalSessionState", "true");
      config.addDataSourceProperty("rewriteBatchedStatements", "true");
      config.addDataSourceProperty("cacheResultSetMetadata", "true");
      config.addDataSourceProperty("cacheServerConfiguration", "true");
      config.addDataSourceProperty("elideSetAutoCommits", "true");
      config.addDataSourceProperty("maintainTimeStats", "false");
   }

   private void setupSQLiteDataSource(HikariConfig config) {
      File dataFolder = this.plugin.getDataFolder();
      if (!dataFolder.exists()) {
         dataFolder.mkdirs();
      }

      File dbFile = new File(dataFolder, this.sqliteFile);
      String jdbcUrl = "jdbc:sqlite:" + dbFile.getAbsolutePath();
      config.setJdbcUrl(jdbcUrl);
      config.setDriverClassName("org.sqlite.JDBC");
      config.setMaximumPoolSize(1);
      config.setMinimumIdle(1);
      config.setConnectionTimeout(this.connectionTimeout);
      config.setMaxLifetime(0L);
      config.setIdleTimeout(0L);
      config.setPoolName("SmartSpawner-SQLite-HikariCP");
      config.addDataSourceProperty("journal_mode", "WAL");
      config.addDataSourceProperty("synchronous", "NORMAL");
      config.addDataSourceProperty("cache_size", "10000");
      config.addDataSourceProperty("foreign_keys", "ON");
   }

   private void createTables() throws SQLException {
      Connection conn = this.getConnection();

      try {
         Statement stmt = conn.createStatement();

         try {
            if (this.storageMode == StorageMode.SQLITE) {
               stmt.execute("CREATE TABLE IF NOT EXISTS smart_spawners (\n    id INTEGER PRIMARY KEY AUTOINCREMENT,\n    spawner_id VARCHAR(64) NOT NULL,\n    server_name VARCHAR(64) NOT NULL,\n\n    -- Location (separate columns for indexing)\n    world_name VARCHAR(128) NOT NULL,\n    loc_x INT NOT NULL,\n    loc_y INT NOT NULL,\n    loc_z INT NOT NULL,\n\n    -- Entity data\n    entity_type VARCHAR(64) NOT NULL,\n    item_spawner_material VARCHAR(64) DEFAULT NULL,\n\n    -- Settings\n    spawner_exp INT NOT NULL DEFAULT 0,\n    spawner_active BOOLEAN NOT NULL DEFAULT 1,\n    spawner_range INT NOT NULL DEFAULT 16,\n    spawner_stop BOOLEAN NOT NULL DEFAULT 1,\n    spawn_delay BIGINT NOT NULL DEFAULT 500,\n    max_spawner_loot_slots INT NOT NULL DEFAULT 45,\n    max_stored_exp INT NOT NULL DEFAULT 1000,\n    min_mobs INT NOT NULL DEFAULT 1,\n    max_mobs INT NOT NULL DEFAULT 4,\n    stack_size INT NOT NULL DEFAULT 1,\n    max_stack_size INT NOT NULL DEFAULT 1000,\n    last_spawn_time BIGINT NOT NULL DEFAULT 0,\n    is_at_capacity BOOLEAN NOT NULL DEFAULT 0,\n\n    -- Player interaction\n    last_interacted_player VARCHAR(64) DEFAULT NULL,\n    preferred_sort_item VARCHAR(64) DEFAULT NULL,\n    filtered_items TEXT DEFAULT NULL,\n\n    -- Inventory (JSON blob)\n    inventory_data TEXT DEFAULT NULL,\n\n    -- Timestamps\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n\n    -- Unique constraints\n    UNIQUE (server_name, spawner_id),\n    UNIQUE (server_name, world_name, loc_x, loc_y, loc_z)\n)\n");
               stmt.execute("CREATE INDEX IF NOT EXISTS idx_server ON smart_spawners (server_name)");
               stmt.execute("CREATE INDEX IF NOT EXISTS idx_world ON smart_spawners (server_name, world_name)");
            } else {
               stmt.execute("CREATE TABLE IF NOT EXISTS smart_spawners (\n    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n    spawner_id VARCHAR(64) NOT NULL,\n    server_name VARCHAR(64) NOT NULL,\n\n    -- Location (separate columns for indexing)\n    world_name VARCHAR(128) NOT NULL,\n    loc_x INT NOT NULL,\n    loc_y INT NOT NULL,\n    loc_z INT NOT NULL,\n\n    -- Entity data\n    entity_type VARCHAR(64) NOT NULL,\n    item_spawner_material VARCHAR(64) DEFAULT NULL,\n\n    -- Settings\n    spawner_exp INT NOT NULL DEFAULT 0,\n    spawner_active BOOLEAN NOT NULL DEFAULT TRUE,\n    spawner_range INT NOT NULL DEFAULT 16,\n    spawner_stop BOOLEAN NOT NULL DEFAULT TRUE,\n    spawn_delay BIGINT NOT NULL DEFAULT 500,\n    max_spawner_loot_slots INT NOT NULL DEFAULT 45,\n    max_stored_exp INT NOT NULL DEFAULT 1000,\n    min_mobs INT NOT NULL DEFAULT 1,\n    max_mobs INT NOT NULL DEFAULT 4,\n    stack_size INT NOT NULL DEFAULT 1,\n    max_stack_size INT NOT NULL DEFAULT 1000,\n    last_spawn_time BIGINT NOT NULL DEFAULT 0,\n    is_at_capacity BOOLEAN NOT NULL DEFAULT FALSE,\n\n    -- Player interaction\n    last_interacted_player VARCHAR(64) DEFAULT NULL,\n    preferred_sort_item VARCHAR(64) DEFAULT NULL,\n    filtered_items TEXT DEFAULT NULL,\n\n    -- Inventory (JSON blob)\n    inventory_data MEDIUMTEXT DEFAULT NULL,\n\n    -- Timestamps\n    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n\n    -- Indexes\n    UNIQUE KEY uk_server_spawner (server_name, spawner_id),\n    UNIQUE KEY uk_location (server_name, world_name, loc_x, loc_y, loc_z),\n    INDEX idx_server (server_name),\n    INDEX idx_world (server_name, world_name)\n) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4\n");
            }

            this.plugin.debug("Database tables created/verified successfully.");
         } catch (Throwable var7) {
            if (stmt != null) {
               try {
                  stmt.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (stmt != null) {
            stmt.close();
         }
      } catch (Throwable var8) {
         if (conn != null) {
            try {
               conn.close();
            } catch (Throwable var5) {
               var8.addSuppressed(var5);
            }
         }

         throw var8;
      }

      if (conn != null) {
         conn.close();
      }

   }

   public Connection getConnection() throws SQLException {
      if (this.dataSource != null && !this.dataSource.isClosed()) {
         return this.dataSource.getConnection();
      } else {
         throw new SQLException("Database connection pool is not initialized or has been closed");
      }
   }

   public String getServerName() {
      return this.serverName;
   }

   public StorageMode getStorageMode() {
      return this.storageMode;
   }

   public boolean isActive() {
      return this.dataSource != null && !this.dataSource.isClosed();
   }

   public void shutdown() {
      if (this.dataSource != null && !this.dataSource.isClosed()) {
         this.dataSource.close();
         this.logger.info("Database connection pool closed.");
      }

   }
}
