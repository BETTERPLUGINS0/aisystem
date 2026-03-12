package ac.grim.grimac.manager.violationdatabase.sqlite;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.api.plugin.GrimPlugin;
import ac.grim.grimac.manager.violationdatabase.DatabaseDialect;
import ac.grim.grimac.manager.violationdatabase.DatabaseUtils;
import ac.grim.grimac.manager.violationdatabase.Violation;
import ac.grim.grimac.manager.violationdatabase.ViolationDatabase;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.utils.anticheat.LogUtil;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLiteViolationDatabase implements ViolationDatabase {
   private final GrimPlugin plugin;
   private Connection openConnection;
   private final DatabaseDialect dialect;

   public SQLiteViolationDatabase(@NotNull GrimPlugin plugin) {
      this.plugin = plugin;
      this.dialect = new SQLiteDialect();
   }

   public void connect() throws SQLException {
      try {
         Connection connection = this.getConnection();

         try {
            Statement stmt = connection.createStatement();

            try {
               stmt.execute("PRAGMA foreign_keys = ON;");
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

            String pkSyntax = this.dialect.getAutoIncrementPrimaryKeySyntax();
            String uuidType = this.dialect.getUuidColumnType();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_servers(id " + pkSyntax + ", server_name VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_servers_name ON grim_history_servers(server_name)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_check_names(id " + pkSyntax + ", check_name_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_check_names_string ON grim_history_check_names(check_name_string)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_versions(id " + pkSyntax + ", grim_version_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_versions_string ON grim_history_versions(grim_version_string)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_client_brands(id " + pkSyntax + ", client_brand_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_client_brands_string ON grim_history_client_brands(client_brand_string)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_client_versions(id " + pkSyntax + ", client_version_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_client_versions_string ON grim_history_client_versions(client_version_string)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_server_versions(id " + pkSyntax + ", server_version_string VARCHAR(255) NOT NULL UNIQUE)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_server_versions_string ON grim_history_server_versions(server_version_string)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS grim_history_violations(id " + pkSyntax + ", server_id INTEGER NOT NULL, uuid " + uuidType + " NOT NULL, check_name_id INTEGER NOT NULL, verbose TEXT NOT NULL, vl INTEGER NOT NULL, created_at BIGINT NOT NULL, grim_version_id INTEGER NOT NULL, client_brand_id INTEGER NOT NULL, client_version_id INTEGER NOT NULL, server_version_id INTEGER NOT NULL, FOREIGN KEY (server_id) REFERENCES grim_history_servers(id), FOREIGN KEY (check_name_id) REFERENCES grim_history_check_names(id), FOREIGN KEY (grim_version_id) REFERENCES grim_history_versions(id), FOREIGN KEY (client_brand_id) REFERENCES grim_history_client_brands(id), FOREIGN KEY (client_version_id) REFERENCES grim_history_client_versions(id), FOREIGN KEY (server_version_id) REFERENCES grim_history_server_versions(id))").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_uuid ON grim_history_violations(uuid)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_created_at ON grim_history_violations(created_at)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_server_id ON grim_history_violations(server_id)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_check_name_id ON grim_history_violations(check_name_id)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_grim_version_id ON grim_history_violations(grim_version_id)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_client_brand_id ON grim_history_violations(client_brand_id)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_client_version_id ON grim_history_violations(client_version_id)").execute();
            connection.prepareStatement("CREATE INDEX IF NOT EXISTS idx_grim_history_violations_server_version_id ON grim_history_violations(server_version_id)").execute();
         } catch (Throwable var8) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var5) {
                  var8.addSuppressed(var5);
               }
            }

            throw var8;
         }

         if (connection != null) {
            connection.close();
         }

      } catch (SQLException var9) {
         LogUtil.error("Failed to generate violations database:", var9);
         throw var9;
      }
   }

   public synchronized void logAlert(GrimPlayer player, String grimVersion, String verbose, String checkName, int vls) {
      try {
         Connection connection = this.getConnection();

         try {
            PreparedStatement insertLog = connection.prepareStatement("INSERT INTO grim_history_violations (server_id, uuid, check_name_id, verbose, vl, created_at, grim_version_id, client_brand_id, client_version_id, server_version_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            try {
               String serverName = GrimAPI.INSTANCE.getConfigManager().getConfig().getStringElse("history.server-name", "Prison");
               long serverId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_servers", "server_name", serverName);
               long checkNameId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_check_names", "check_name_string", checkName);
               long grimVersionId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_versions", "grim_version_string", grimVersion);
               long clientBrandId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_client_brands", "client_brand_string", player.getBrand());
               long clientVersionId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_client_versions", "client_version_string", player.getClientVersion().getReleaseName());
               long serverVersionId = DatabaseUtils.getOrCreateId(connection, this.dialect, "grim_history_server_versions", "server_version_string", PacketEvents.getAPI().getServerManager().getVersion().toString());
               insertLog.setLong(1, serverId);
               insertLog.setBytes(2, DatabaseUtils.uuidToBytes(player.getUniqueId()));
               insertLog.setLong(3, checkNameId);
               insertLog.setString(4, verbose);
               insertLog.setInt(5, vls);
               insertLog.setLong(6, System.currentTimeMillis());
               insertLog.setLong(7, grimVersionId);
               insertLog.setLong(8, clientBrandId);
               insertLog.setLong(9, clientVersionId);
               insertLog.setLong(10, serverVersionId);
               insertLog.executeUpdate();
            } catch (Throwable var23) {
               if (insertLog != null) {
                  try {
                     insertLog.close();
                  } catch (Throwable var22) {
                     var23.addSuppressed(var22);
                  }
               }

               throw var23;
            }

            if (insertLog != null) {
               insertLog.close();
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
         LogUtil.error("Failed to insert violation:", var25);
      }

   }

   public synchronized int getLogCount(UUID player) {
      try {
         Connection connection = this.getConnection();

         label74: {
            int var5;
            try {
               PreparedStatement fetchLogs = connection.prepareStatement("SELECT COUNT(*) FROM grim_history_violations WHERE uuid = ?");

               label76: {
                  try {
                     fetchLogs.setBytes(1, DatabaseUtils.uuidToBytes(player));
                     ResultSet resultSet = fetchLogs.executeQuery();
                     if (resultSet.next()) {
                        var5 = resultSet.getInt(1);
                        break label76;
                     }
                  } catch (Throwable var8) {
                     if (fetchLogs != null) {
                        try {
                           fetchLogs.close();
                        } catch (Throwable var7) {
                           var8.addSuppressed(var7);
                        }
                     }

                     throw var8;
                  }

                  if (fetchLogs != null) {
                     fetchLogs.close();
                  }
                  break label74;
               }

               if (fetchLogs != null) {
                  fetchLogs.close();
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
         LogUtil.error("Failed to fetch number of violations:", var10);
      }

      return 0;
   }

   public synchronized List<Violation> getViolations(UUID player, int page, int limit) {
      ArrayList violations = new ArrayList();

      try {
         Connection connection = this.getConnection();

         List var7;
         try {
            PreparedStatement fetchLogs = connection.prepareStatement("SELECT v.id, s.server_name, v.uuid, cn.check_name_string, v.verbose, v.vl, v.created_at, gv.grim_version_string, cb.client_brand_string, clv.client_version_string, srv.server_version_string FROM grim_history_violations v JOIN grim_history_servers s ON v.server_id = s.id JOIN grim_history_check_names cn ON v.check_name_id = cn.id JOIN grim_history_versions gv ON v.grim_version_id = gv.id JOIN grim_history_client_brands cb ON v.client_brand_id = cb.id JOIN grim_history_client_versions clv ON v.client_version_id = clv.id JOIN grim_history_server_versions srv ON v.server_version_id = srv.id WHERE v.uuid = ? ORDER BY v.created_at DESC LIMIT ? OFFSET ?");

            try {
               fetchLogs.setBytes(1, DatabaseUtils.uuidToBytes(player));
               fetchLogs.setInt(2, limit);
               fetchLogs.setInt(3, (page - 1) * limit);
               var7 = Violation.fromResultSet(fetchLogs.executeQuery());
            } catch (Throwable var11) {
               if (fetchLogs != null) {
                  try {
                     fetchLogs.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (fetchLogs != null) {
               fetchLogs.close();
            }
         } catch (Throwable var12) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }
            }

            throw var12;
         }

         if (connection != null) {
            connection.close();
         }

         return var7;
      } catch (SQLException var13) {
         LogUtil.error("Failed to fetch violations:", var13);
         return violations;
      }
   }

   public void disconnect() {
      try {
         if (this.openConnection != null && !this.openConnection.isClosed()) {
            this.openConnection.close();
         }
      } catch (SQLException var2) {
         LogUtil.error("Failed to close connection", var2);
      }

   }

   protected synchronized Connection getConnection() throws SQLException {
      if (this.openConnection == null || this.openConnection.isClosed()) {
         this.openConnection = this.openConnection();
      }

      return this.openConnection;
   }

   protected Connection openConnection() throws SQLException {
      String var10000 = this.plugin.getDataFolder().getAbsolutePath();
      return DriverManager.getConnection("jdbc:sqlite:" + var10000 + File.separator + "violations.sqlite");
   }
}
