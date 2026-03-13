package github.nighter.smartspawner.spawner.data.database;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.storage.StorageMode;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqliteToMySqlMigration {
   private final SmartSpawner plugin;
   private final Logger logger;
   private final DatabaseManager mysqlManager;
   private final String serverName;
   private static final String MIGRATED_FILE_SUFFIX = ".migrated";
   private static final String INSERT_SQL_MYSQL = "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON DUPLICATE KEY UPDATE\n    world_name = VALUES(world_name),\n    loc_x = VALUES(loc_x),\n    loc_y = VALUES(loc_y),\n    loc_z = VALUES(loc_z),\n    entity_type = VALUES(entity_type),\n    item_spawner_material = VALUES(item_spawner_material),\n    spawner_exp = VALUES(spawner_exp),\n    spawner_active = VALUES(spawner_active),\n    spawner_range = VALUES(spawner_range),\n    spawner_stop = VALUES(spawner_stop),\n    spawn_delay = VALUES(spawn_delay),\n    max_spawner_loot_slots = VALUES(max_spawner_loot_slots),\n    max_stored_exp = VALUES(max_stored_exp),\n    min_mobs = VALUES(min_mobs),\n    max_mobs = VALUES(max_mobs),\n    stack_size = VALUES(stack_size),\n    max_stack_size = VALUES(max_stack_size),\n    last_spawn_time = VALUES(last_spawn_time),\n    is_at_capacity = VALUES(is_at_capacity),\n    last_interacted_player = VALUES(last_interacted_player),\n    preferred_sort_item = VALUES(preferred_sort_item),\n    filtered_items = VALUES(filtered_items),\n    inventory_data = VALUES(inventory_data)\n";
   private static final String SELECT_ALL_SQLITE = "SELECT spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n       entity_type, item_spawner_material, spawner_exp, spawner_active,\n       spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n       max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n       last_spawn_time, is_at_capacity, last_interacted_player,\n       preferred_sort_item, filtered_items, inventory_data\nFROM smart_spawners\n";

   public SqliteToMySqlMigration(SmartSpawner plugin, DatabaseManager mysqlManager) {
      this.plugin = plugin;
      this.logger = plugin.getLogger();
      this.mysqlManager = mysqlManager;
      this.serverName = mysqlManager.getServerName();
   }

   public boolean needsMigration() {
      if (this.mysqlManager.getStorageMode() != StorageMode.MYSQL) {
         return false;
      } else {
         String sqliteFileName = this.plugin.getConfig().getString("database.sqlite.file", "spawners.db");
         File sqliteFile = new File(this.plugin.getDataFolder(), sqliteFileName);
         if (!sqliteFile.exists()) {
            return false;
         } else {
            File migratedFile = new File(this.plugin.getDataFolder(), sqliteFileName + ".migrated");
            return migratedFile.exists() ? false : this.hasSqliteData(sqliteFile);
         }
      }
   }

   private boolean hasSqliteData(File sqliteFile) {
      String jdbcUrl = "jdbc:sqlite:" + sqliteFile.getAbsolutePath();

      try {
         Connection conn = DriverManager.getConnection(jdbcUrl);

         boolean var6;
         label121: {
            try {
               Statement stmt;
               label110: {
                  stmt = conn.createStatement();

                  try {
                     label111: {
                        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM smart_spawners");

                        label93: {
                           try {
                              if (!rs.next()) {
                                 break label93;
                              }

                              var6 = rs.getInt(1) > 0;
                           } catch (Throwable var11) {
                              if (rs != null) {
                                 try {
                                    rs.close();
                                 } catch (Throwable var10) {
                                    var11.addSuppressed(var10);
                                 }
                              }

                              throw var11;
                           }

                           if (rs != null) {
                              rs.close();
                           }
                           break label111;
                        }

                        if (rs != null) {
                           rs.close();
                        }
                        break label110;
                     }
                  } catch (Throwable var12) {
                     if (stmt != null) {
                        try {
                           stmt.close();
                        } catch (Throwable var9) {
                           var12.addSuppressed(var9);
                        }
                     }

                     throw var12;
                  }

                  if (stmt != null) {
                     stmt.close();
                  }
                  break label121;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var13) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var8) {
                     var13.addSuppressed(var8);
                  }
               }

               throw var13;
            }

            if (conn != null) {
               conn.close();
            }

            return false;
         }

         if (conn != null) {
            conn.close();
         }

         return var6;
      } catch (SQLException var14) {
         this.plugin.debug("SQLite check failed: " + var14.getMessage());
         return false;
      }
   }

   public boolean migrate() {
      this.logger.info("Starting SQLite to MySQL migration...");
      String sqliteFileName = this.plugin.getConfig().getString("database.sqlite.file", "spawners.db");
      File sqliteFile = new File(this.plugin.getDataFolder(), sqliteFileName);
      if (!sqliteFile.exists()) {
         this.logger.info("No SQLite file found, skipping migration.");
         return true;
      } else {
         String sqliteJdbcUrl = "jdbc:sqlite:" + sqliteFile.getAbsolutePath();
         int totalSpawners = 0;
         int migratedCount = 0;
         int failedCount = 0;

         try {
            Connection sqliteConn = DriverManager.getConnection(sqliteJdbcUrl);

            boolean var28;
            try {
               Connection mysqlConn = this.mysqlManager.getConnection();

               try {
                  PreparedStatement selectStmt = sqliteConn.prepareStatement("SELECT spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n       entity_type, item_spawner_material, spawner_exp, spawner_active,\n       spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n       max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n       last_spawn_time, is_at_capacity, last_interacted_player,\n       preferred_sort_item, filtered_items, inventory_data\nFROM smart_spawners\n");

                  try {
                     PreparedStatement insertStmt = mysqlConn.prepareStatement("INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON DUPLICATE KEY UPDATE\n    world_name = VALUES(world_name),\n    loc_x = VALUES(loc_x),\n    loc_y = VALUES(loc_y),\n    loc_z = VALUES(loc_z),\n    entity_type = VALUES(entity_type),\n    item_spawner_material = VALUES(item_spawner_material),\n    spawner_exp = VALUES(spawner_exp),\n    spawner_active = VALUES(spawner_active),\n    spawner_range = VALUES(spawner_range),\n    spawner_stop = VALUES(spawner_stop),\n    spawn_delay = VALUES(spawn_delay),\n    max_spawner_loot_slots = VALUES(max_spawner_loot_slots),\n    max_stored_exp = VALUES(max_stored_exp),\n    min_mobs = VALUES(min_mobs),\n    max_mobs = VALUES(max_mobs),\n    stack_size = VALUES(stack_size),\n    max_stack_size = VALUES(max_stack_size),\n    last_spawn_time = VALUES(last_spawn_time),\n    is_at_capacity = VALUES(is_at_capacity),\n    last_interacted_player = VALUES(last_interacted_player),\n    preferred_sort_item = VALUES(preferred_sort_item),\n    filtered_items = VALUES(filtered_items),\n    inventory_data = VALUES(inventory_data)\n");

                     try {
                        mysqlConn.setAutoCommit(false);
                        ResultSet rs = selectStmt.executeQuery();

                        try {
                           int batchCount = 0;
                           boolean var13 = true;

                           while(rs.next()) {
                              ++totalSpawners;

                              try {
                                 insertStmt.setString(1, rs.getString("spawner_id"));
                                 insertStmt.setString(2, rs.getString("server_name"));
                                 insertStmt.setString(3, rs.getString("world_name"));
                                 insertStmt.setInt(4, rs.getInt("loc_x"));
                                 insertStmt.setInt(5, rs.getInt("loc_y"));
                                 insertStmt.setInt(6, rs.getInt("loc_z"));
                                 insertStmt.setString(7, rs.getString("entity_type"));
                                 insertStmt.setString(8, rs.getString("item_spawner_material"));
                                 insertStmt.setInt(9, rs.getInt("spawner_exp"));
                                 insertStmt.setBoolean(10, rs.getBoolean("spawner_active"));
                                 insertStmt.setInt(11, rs.getInt("spawner_range"));
                                 insertStmt.setBoolean(12, rs.getBoolean("spawner_stop"));
                                 insertStmt.setLong(13, rs.getLong("spawn_delay"));
                                 insertStmt.setInt(14, rs.getInt("max_spawner_loot_slots"));
                                 insertStmt.setInt(15, rs.getInt("max_stored_exp"));
                                 insertStmt.setInt(16, rs.getInt("min_mobs"));
                                 insertStmt.setInt(17, rs.getInt("max_mobs"));
                                 insertStmt.setInt(18, rs.getInt("stack_size"));
                                 insertStmt.setInt(19, rs.getInt("max_stack_size"));
                                 insertStmt.setLong(20, rs.getLong("last_spawn_time"));
                                 insertStmt.setBoolean(21, rs.getBoolean("is_at_capacity"));
                                 insertStmt.setString(22, rs.getString("last_interacted_player"));
                                 insertStmt.setString(23, rs.getString("preferred_sort_item"));
                                 insertStmt.setString(24, rs.getString("filtered_items"));
                                 insertStmt.setString(25, rs.getString("inventory_data"));
                                 insertStmt.addBatch();
                                 ++batchCount;
                                 ++migratedCount;
                                 if (batchCount >= 100) {
                                    insertStmt.executeBatch();
                                    mysqlConn.commit();
                                    batchCount = 0;
                                    this.logger.info("Migrated " + migratedCount + " spawners...");
                                 }
                              } catch (Exception var20) {
                                 this.logger.log(Level.WARNING, "Failed to migrate spawner: " + rs.getString("spawner_id"), var20);
                                 ++failedCount;
                              }
                           }

                           if (batchCount > 0) {
                              insertStmt.executeBatch();
                              mysqlConn.commit();
                           }
                        } catch (Throwable var21) {
                           if (rs != null) {
                              try {
                                 rs.close();
                              } catch (Throwable var19) {
                                 var21.addSuppressed(var19);
                              }
                           }

                           throw var21;
                        }

                        if (rs != null) {
                           rs.close();
                        }

                        this.logger.info("Migration completed. Total: " + totalSpawners + ", Migrated: " + migratedCount + ", Failed: " + failedCount);
                        if (failedCount == 0) {
                           File migratedFile = new File(this.plugin.getDataFolder(), sqliteFileName + ".migrated");
                           if (sqliteFile.renameTo(migratedFile)) {
                              this.logger.info("SQLite file renamed to " + sqliteFileName + ".migrated");
                           } else {
                              this.logger.warning("Failed to rename SQLite file. Manual cleanup may be required.");
                           }
                        }

                        var28 = failedCount == 0;
                     } catch (Throwable var22) {
                        if (insertStmt != null) {
                           try {
                              insertStmt.close();
                           } catch (Throwable var18) {
                              var22.addSuppressed(var18);
                           }
                        }

                        throw var22;
                     }

                     if (insertStmt != null) {
                        insertStmt.close();
                     }
                  } catch (Throwable var23) {
                     if (selectStmt != null) {
                        try {
                           selectStmt.close();
                        } catch (Throwable var17) {
                           var23.addSuppressed(var17);
                        }
                     }

                     throw var23;
                  }

                  if (selectStmt != null) {
                     selectStmt.close();
                  }
               } catch (Throwable var24) {
                  if (mysqlConn != null) {
                     try {
                        mysqlConn.close();
                     } catch (Throwable var16) {
                        var24.addSuppressed(var16);
                     }
                  }

                  throw var24;
               }

               if (mysqlConn != null) {
                  mysqlConn.close();
               }
            } catch (Throwable var25) {
               if (sqliteConn != null) {
                  try {
                     sqliteConn.close();
                  } catch (Throwable var15) {
                     var25.addSuppressed(var15);
                  }
               }

               throw var25;
            }

            if (sqliteConn != null) {
               sqliteConn.close();
            }

            return var28;
         } catch (SQLException var26) {
            this.logger.log(Level.SEVERE, "Database error during SQLite to MySQL migration", var26);
            return false;
         }
      }
   }
}
