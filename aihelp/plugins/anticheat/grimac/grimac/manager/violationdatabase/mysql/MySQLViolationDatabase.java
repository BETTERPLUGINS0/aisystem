package ac.grim.grimac.manager.violationdatabase.mysql;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;
import ac.grim.grimac.manager.violationdatabase.DatabaseUtils;
import ac.grim.grimac.manager.violationdatabase.Violation;
import ac.grim.grimac.manager.violationdatabase.ViolationDatabase;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.zaxxer.hikari.HikariConfig;
import ac.grim.grimac.shaded.zaxxer.hikari.HikariDataSource;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MySQLViolationDatabase implements ViolationDatabase {
   private final GrimPlugin plugin;
   private HikariDataSource dataSource;
   private final DatabaseDialect dialect;

   public MySQLViolationDatabase(GrimPlugin plugin, String url, String database, String username, String password) {
      this.plugin = plugin;
      this.dialect = new MySQLDialect();
      this.setupDataSource(url, database, username, password);
   }

   private void setupDataSource(String url, String database, String username, String password) {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl("jdbc:mysql://" + url + "/" + database);
      config.setUsername(username);
      config.setPassword(password);
      config.addDataSourceProperty("cachePrepStmts", "true");
      config.addDataSourceProperty("prepStmtCacheSize", "250");
      config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
      config.setMaximumPoolSize(10);
      config.setAutoCommit(true);
      this.dataSource = new HikariDataSource(config);
   }

   public void connect() throws SQLException {
      try {
         Connection connection = this.dataSource.getConnection();

         try {
            String pkSyntax = this.dialect.getAutoIncrementPrimaryKeySyntax();
            String uuidType = this.dialect.getUuidColumnType();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_servers(id " + pkSyntax + ", server_name VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_servers_name ON grim_history_servers(server_name);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_check_names(id " + pkSyntax + ", check_name_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_check_names_string ON grim_history_check_names(check_name_string);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_versions(id " + pkSyntax + ", grim_version_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_versions_string ON grim_history_versions(grim_version_string);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_client_brands(id " + pkSyntax + ", client_brand_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_client_brands_string ON grim_history_client_brands(client_brand_string);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_client_versions(id " + pkSyntax + ", client_version_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_client_versions_string ON grim_history_client_versions(client_version_string);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_server_versions(id " + pkSyntax + ", server_version_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_server_versions_string ON grim_history_server_versions(server_version_string);").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_violations(id " + pkSyntax + ", server_id BIGINT NOT NULL, uuid " + uuidType + " NOT NULL, check_name_id BIGINT NOT NULL, verbose TEXT NOT NULL, vl INT NOT NULL, created_at BIGINT NOT NULL, grim_version_id BIGINT NOT NULL, client_brand_id BIGINT NOT NULL, client_version_id BIGINT NOT NULL, server_version_id BIGINT NOT NULL, FOREIGN KEY (server_id) REFERENCES grim_history_servers(id), FOREIGN KEY (check_name_id) REFERENCES grim_history_check_names(id), FOREIGN KEY (grim_version_id) REFERENCES grim_history_versions(id), FOREIGN KEY (client_brand_id) REFERENCES grim_history_client_brands(id), FOREIGN KEY (client_version_id) REFERENCES grim_history_client_versions(id), FOREIGN KEY (server_version_id) REFERENCES grim_history_server_versions(id))").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_uuid ON grim_history_violations(uuid);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_created_at ON grim_history_violations(created_at);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_server_id ON grim_history_violations(server_id);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_check_name_id ON grim_history_violations(check_name_id);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_grim_version_id ON grim_history_violations(grim_version_id);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_client_brand_id ON grim_history_violations(client_brand_id);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_client_version_id ON grim_history_violations(client_version_id);").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_server_version_id ON grim_history_violations(server_version_id);").execute();
         } catch (Throwable var5) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (connection != null) {
            connection.close();
         }

      } catch (SQLException var6) {
         LogUtil.error("Failed to generate violations database:", var6);
         throw var6;
      }
   }

   public synchronized void logAlert(GrimPlayer player, String grimVersion, String verbose, String checkName, int vls) {
      try {
         Connection connection = this.dataSource.getConnection();

         try {
            PreparedStatement insertAlert = connection.prepareStatement("INSERT INTO grim_history_violations (server_id, uuid, check_name_id, verbose, vl, created_at, grim_version_id, client_brand_id, client_version_id, server_version_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            try {
               String serverName = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("history.server-name", "Prison");
               long serverId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_servers", "server_name", serverName);
               long checkNameId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_check_names", "check_name_string", checkName);
               long grimVersionId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_versions", "grim_version_string", grimVersion);
               long clientBrandId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_client_brands", "client_brand_string", player.getBrand());
               long clientVersionId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_client_versions", "client_version_string", player.getClientVersion().getReleaseName());
               long serverVersionId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_server_versions", "server_version_string", PacketEvents.getAPI().getServerManager().getVersion().toString());
               insertAlert.setLong(1, serverId);
               insertAlert.setBytes(2, DatabaseUtils.uuidToBytes(player.getUniqueId()));
               insertAlert.setLong(3, checkNameId);
               insertAlert.setString(4, verbose);
               insertAlert.setInt(5, vls);
               insertAlert.setLong(6, System.currentTimeMillis());
               insertAlert.setLong(7, grimVersionId);
               insertAlert.setLong(8, clientBrandId);
               insertAlert.setLong(9, clientVersionId);
               insertAlert.setLong(10, serverVersionId);
               insertAlert.execute();
            } catch (Throwable var23) {
               if (insertAlert != null) {
                  try {
                     insertAlert.close();
                  } catch (Throwable var22) {
                     var23.addSuppressed(var22);
                  }
               }

               throw var23;
            }

            if (insertAlert != null) {
               insertAlert.close();
            }
         } catch (Throwable var24) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var21) {
                  var24.addSuppressed(var21);
               }
            }

            throw var24;
         }

         if (connection != null) {
            connection.close();
         }
      } catch (SQLException var25) {
         LogUtil.error("Failed to log alert", var25);
      }

   }

   public synchronized int getLogCount(UUID player) {
      try {
         Connection connection = this.dataSource.getConnection();

         label74: {
            int var5;
            try {
               PreparedStatement countLogs = connection.prepareStatement("SELECT COUNT(*) FROM grim_history_violations WHERE uuid = ?");

               label76: {
                  try {
                     countLogs.setBytes(1, DatabaseUtils.uuidToBytes(player));
                     ResultSet result = countLogs.executeQuery();
                     if (result.next()) {
                        var5 = result.getInt(1);
                        break label76;
                     }
                  } catch (Throwable var8) {
                     if (countLogs != null) {
                        try {
                           countLogs.close();
                        } catch (Throwable var7) {
                           var8.addSuppressed(var7);
                        }
                     }

                     throw var8;
                  }

                  if (countLogs != null) {
                     countLogs.close();
                  }
                  break label74;
               }

               if (countLogs != null) {
                  countLogs.close();
               }
            } catch (Throwable var9) {
               if (connection != null) {
                  try {
                     connection.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (connection != null) {
               connection.close();
            }

            return var5;
         }

         if (connection != null) {
            connection.close();
         }
      } catch (SQLException var10) {
         LogUtil.error("Failed to count logs", var10);
      }

      return 0;
   }

   public synchronized List<Violation> getViolations(UUID player, int page, int limit) {
      try {
         Connection connection = this.dataSource.getConnection();

         List var6;
         try {
            PreparedStatement fetchLogs = connection.prepareStatement("SELECT v.id, s.server_name, v.uuid, cn.check_name_string, v.verbose, v.vl, v.created_at, gv.grim_version_string, cb.client_brand_string, clv.client_version_string, srv.server_version_string FROM grim_history_violations v JOIN grim_history_servers s ON v.server_id = s.id JOIN grim_history_check_names cn ON v.check_name_id = cn.id JOIN grim_history_versions gv ON v.grim_version_id = gv.id JOIN grim_history_client_brands cb ON v.client_brand_id = cb.id JOIN grim_history_client_versions clv ON v.client_version_id = clv.id JOIN grim_history_server_versions srv ON v.server_version_id = srv.id WHERE v.uuid = ? ORDER BY v.created_at DESC LIMIT ? OFFSET ?");

            try {
               fetchLogs.setBytes(1, DatabaseUtils.uuidToBytes(player));
               fetchLogs.setInt(2, limit);
               fetchLogs.setInt(3, (page - 1) * limit);
               var6 = Violation.fromResultSet(fetchLogs.executeQuery());
            } catch (Throwable var10) {
               if (fetchLogs != null) {
                  try {
                     fetchLogs.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (fetchLogs != null) {
               fetchLogs.close();
            }
         } catch (Throwable var11) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (connection != null) {
            connection.close();
         }

         return var6;
      } catch (SQLException var12) {
         LogUtil.error("Failed to fetch logs", var12);
         return null;
      }
   }

   public void disconnect() {
      if (this.dataSource != null && !this.dataSource.isClosed()) {
         this.dataSource.close();
      }

   }

   public boolean sameConfig(String host, String db, String user, String pwd) {
      String wantUrl = "jdbc:mysql://" + host + "/" + db;
      return wantUrl.equalsIgnoreCase(this.dataSource.getJdbcUrl()) && user.equals(this.dataSource.getUsername()) && pwd.equals(this.dataSource.getPassword());
   }
}
