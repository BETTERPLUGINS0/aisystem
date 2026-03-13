package github.nighter.smartspawner.spawner.data.database;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.storage.StorageMode;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

public class YamlToDatabaseMigration {
   private final SmartSpawner plugin;
   private final Logger logger;
   private final DatabaseManager databaseManager;
   private final String serverName;
   private static final String YAML_FILE_NAME = "spawners_data.yml";
   private static final String MIGRATED_FILE_SUFFIX = ".migrated";
   private static final String INSERT_SQL_MYSQL = "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON DUPLICATE KEY UPDATE\n    world_name = VALUES(world_name),\n    loc_x = VALUES(loc_x),\n    loc_y = VALUES(loc_y),\n    loc_z = VALUES(loc_z),\n    entity_type = VALUES(entity_type),\n    item_spawner_material = VALUES(item_spawner_material),\n    spawner_exp = VALUES(spawner_exp),\n    spawner_active = VALUES(spawner_active),\n    spawner_range = VALUES(spawner_range),\n    spawner_stop = VALUES(spawner_stop),\n    spawn_delay = VALUES(spawn_delay),\n    max_spawner_loot_slots = VALUES(max_spawner_loot_slots),\n    max_stored_exp = VALUES(max_stored_exp),\n    min_mobs = VALUES(min_mobs),\n    max_mobs = VALUES(max_mobs),\n    stack_size = VALUES(stack_size),\n    max_stack_size = VALUES(max_stack_size),\n    last_spawn_time = VALUES(last_spawn_time),\n    is_at_capacity = VALUES(is_at_capacity),\n    last_interacted_player = VALUES(last_interacted_player),\n    preferred_sort_item = VALUES(preferred_sort_item),\n    filtered_items = VALUES(filtered_items),\n    inventory_data = VALUES(inventory_data)\n";
   private static final String INSERT_SQL_SQLITE = "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON CONFLICT(server_name, spawner_id) DO UPDATE SET\n    world_name = excluded.world_name,\n    loc_x = excluded.loc_x,\n    loc_y = excluded.loc_y,\n    loc_z = excluded.loc_z,\n    entity_type = excluded.entity_type,\n    item_spawner_material = excluded.item_spawner_material,\n    spawner_exp = excluded.spawner_exp,\n    spawner_active = excluded.spawner_active,\n    spawner_range = excluded.spawner_range,\n    spawner_stop = excluded.spawner_stop,\n    spawn_delay = excluded.spawn_delay,\n    max_spawner_loot_slots = excluded.max_spawner_loot_slots,\n    max_stored_exp = excluded.max_stored_exp,\n    min_mobs = excluded.min_mobs,\n    max_mobs = excluded.max_mobs,\n    stack_size = excluded.stack_size,\n    max_stack_size = excluded.max_stack_size,\n    last_spawn_time = excluded.last_spawn_time,\n    is_at_capacity = excluded.is_at_capacity,\n    last_interacted_player = excluded.last_interacted_player,\n    preferred_sort_item = excluded.preferred_sort_item,\n    filtered_items = excluded.filtered_items,\n    inventory_data = excluded.inventory_data\n";

   public YamlToDatabaseMigration(SmartSpawner plugin, DatabaseManager databaseManager) {
      this.plugin = plugin;
      this.logger = plugin.getLogger();
      this.databaseManager = databaseManager;
      this.serverName = databaseManager.getServerName();
   }

   public boolean needsMigration() {
      File yamlFile = new File(this.plugin.getDataFolder(), "spawners_data.yml");
      if (!yamlFile.exists()) {
         return false;
      } else {
         File migratedFile = new File(this.plugin.getDataFolder(), "spawners_data.yml.migrated");
         if (migratedFile.exists()) {
            return false;
         } else {
            FileConfiguration yamlData = YamlConfiguration.loadConfiguration(yamlFile);
            ConfigurationSection spawnersSection = yamlData.getConfigurationSection("spawners");
            return spawnersSection != null && !spawnersSection.getKeys(false).isEmpty();
         }
      }
   }

   public boolean migrate() {
      this.logger.info("Starting YAML to database migration...");
      File yamlFile = new File(this.plugin.getDataFolder(), "spawners_data.yml");
      if (!yamlFile.exists()) {
         this.logger.info("No YAML file found, skipping migration.");
         return true;
      } else {
         FileConfiguration yamlData = YamlConfiguration.loadConfiguration(yamlFile);
         ConfigurationSection spawnersSection = yamlData.getConfigurationSection("spawners");
         if (spawnersSection != null && !spawnersSection.getKeys(false).isEmpty()) {
            int totalSpawners = spawnersSection.getKeys(false).size();
            int migratedCount = 0;
            int failedCount = 0;
            this.logger.info("Found " + totalSpawners + " spawners to migrate.");
            String insertSql = this.databaseManager.getStorageMode() == StorageMode.SQLITE ? "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON CONFLICT(server_name, spawner_id) DO UPDATE SET\n    world_name = excluded.world_name,\n    loc_x = excluded.loc_x,\n    loc_y = excluded.loc_y,\n    loc_z = excluded.loc_z,\n    entity_type = excluded.entity_type,\n    item_spawner_material = excluded.item_spawner_material,\n    spawner_exp = excluded.spawner_exp,\n    spawner_active = excluded.spawner_active,\n    spawner_range = excluded.spawner_range,\n    spawner_stop = excluded.spawner_stop,\n    spawn_delay = excluded.spawn_delay,\n    max_spawner_loot_slots = excluded.max_spawner_loot_slots,\n    max_stored_exp = excluded.max_stored_exp,\n    min_mobs = excluded.min_mobs,\n    max_mobs = excluded.max_mobs,\n    stack_size = excluded.stack_size,\n    max_stack_size = excluded.max_stack_size,\n    last_spawn_time = excluded.last_spawn_time,\n    is_at_capacity = excluded.is_at_capacity,\n    last_interacted_player = excluded.last_interacted_player,\n    preferred_sort_item = excluded.preferred_sort_item,\n    filtered_items = excluded.filtered_items,\n    inventory_data = excluded.inventory_data\n" : "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON DUPLICATE KEY UPDATE\n    world_name = VALUES(world_name),\n    loc_x = VALUES(loc_x),\n    loc_y = VALUES(loc_y),\n    loc_z = VALUES(loc_z),\n    entity_type = VALUES(entity_type),\n    item_spawner_material = VALUES(item_spawner_material),\n    spawner_exp = VALUES(spawner_exp),\n    spawner_active = VALUES(spawner_active),\n    spawner_range = VALUES(spawner_range),\n    spawner_stop = VALUES(spawner_stop),\n    spawn_delay = VALUES(spawn_delay),\n    max_spawner_loot_slots = VALUES(max_spawner_loot_slots),\n    max_stored_exp = VALUES(max_stored_exp),\n    min_mobs = VALUES(min_mobs),\n    max_mobs = VALUES(max_mobs),\n    stack_size = VALUES(stack_size),\n    max_stack_size = VALUES(max_stack_size),\n    last_spawn_time = VALUES(last_spawn_time),\n    is_at_capacity = VALUES(is_at_capacity),\n    last_interacted_player = VALUES(last_interacted_player),\n    preferred_sort_item = VALUES(preferred_sort_item),\n    filtered_items = VALUES(filtered_items),\n    inventory_data = VALUES(inventory_data)\n";

            try {
               Connection conn = this.databaseManager.getConnection();

               boolean var22;
               try {
                  PreparedStatement stmt = conn.prepareStatement(insertSql);

                  try {
                     conn.setAutoCommit(false);
                     int batchCount = 0;
                     int BATCH_SIZE = true;
                     Iterator var12 = spawnersSection.getKeys(false).iterator();

                     while(var12.hasNext()) {
                        String spawnerId = (String)var12.next();

                        try {
                           if (this.migrateSpawner(stmt, yamlData, spawnerId)) {
                              stmt.addBatch();
                              ++batchCount;
                              ++migratedCount;
                              if (batchCount >= 100) {
                                 stmt.executeBatch();
                                 conn.commit();
                                 batchCount = 0;
                                 this.logger.info("Migrated " + migratedCount + "/" + totalSpawners + " spawners...");
                              }
                           } else {
                              ++failedCount;
                           }
                        } catch (Exception var17) {
                           this.logger.log(Level.WARNING, "Failed to migrate spawner " + spawnerId, var17);
                           ++failedCount;
                        }
                     }

                     if (batchCount > 0) {
                        stmt.executeBatch();
                        conn.commit();
                     }

                     this.logger.info("Migration completed. Migrated: " + migratedCount + ", Failed: " + failedCount);
                     if (failedCount == 0 || migratedCount > 0) {
                        File migratedFile = new File(this.plugin.getDataFolder(), "spawners_data.yml.migrated");
                        if (yamlFile.renameTo(migratedFile)) {
                           this.logger.info("YAML file renamed to spawners_data.yml.migrated");
                        } else {
                           this.logger.warning("Failed to rename YAML file. Manual cleanup may be required.");
                        }
                     }

                     var22 = failedCount == 0;
                  } catch (Throwable var18) {
                     if (stmt != null) {
                        try {
                           stmt.close();
                        } catch (Throwable var16) {
                           var18.addSuppressed(var16);
                        }
                     }

                     throw var18;
                  }

                  if (stmt != null) {
                     stmt.close();
                  }
               } catch (Throwable var19) {
                  if (conn != null) {
                     try {
                        conn.close();
                     } catch (Throwable var15) {
                        var19.addSuppressed(var15);
                     }
                  }

                  throw var19;
               }

               if (conn != null) {
                  conn.close();
               }

               return var22;
            } catch (SQLException var20) {
               this.logger.log(Level.SEVERE, "Database error during migration", var20);
               return false;
            }
         } else {
            this.logger.info("No spawners found in YAML file, skipping migration.");
            return true;
         }
      }
   }

   private boolean migrateSpawner(PreparedStatement stmt, FileConfiguration yamlData, String spawnerId) throws SQLException {
      String path = "spawners." + spawnerId;
      String locationString = yamlData.getString(path + ".location");
      if (locationString == null) {
         this.logger.warning("No location for spawner " + spawnerId + ", skipping.");
         return false;
      } else {
         String[] locParts = locationString.split(",");
         if (locParts.length != 4) {
            this.logger.warning("Invalid location format for spawner " + spawnerId + ", skipping.");
            return false;
         } else {
            String worldName = locParts[0];

            int locX;
            int locY;
            int locZ;
            try {
               locX = Integer.parseInt(locParts[1]);
               locY = Integer.parseInt(locParts[2]);
               locZ = Integer.parseInt(locParts[3]);
            } catch (NumberFormatException var36) {
               this.logger.warning("Invalid location coordinates for spawner " + spawnerId + ", skipping.");
               return false;
            }

            String entityTypeString = yamlData.getString(path + ".entityType");
            if (entityTypeString == null) {
               this.logger.warning("No entity type for spawner " + spawnerId + ", skipping.");
               return false;
            } else {
               EntityType entityType;
               try {
                  entityType = EntityType.valueOf(entityTypeString);
               } catch (IllegalArgumentException var35) {
                  this.logger.warning("Invalid entity type for spawner " + spawnerId + ": " + entityTypeString + ", skipping.");
                  return false;
               }

               String itemSpawnerMaterial = yamlData.getString(path + ".itemSpawnerMaterial");
               String settingsString = yamlData.getString(path + ".settings");
               int spawnerExp = 0;
               boolean spawnerActive = true;
               int spawnerRange = 16;
               boolean spawnerStop = true;
               long spawnDelay = 500L;
               int maxSpawnerLootSlots = 45;
               int maxStoredExp = 1000;
               int minMobs = 1;
               int maxMobs = 4;
               int stackSize = 1;
               int maxStackSize = 1000;
               long lastSpawnTime = 0L;
               boolean isAtCapacity = false;
               if (settingsString != null) {
                  String[] settings = settingsString.split(",");
                  int version = yamlData.getInt("data_version", 1);

                  try {
                     if (version >= 3 && settings.length >= 13) {
                        spawnerExp = Integer.parseInt(settings[0]);
                        spawnerActive = Boolean.parseBoolean(settings[1]);
                        spawnerRange = Integer.parseInt(settings[2]);
                        spawnerStop = Boolean.parseBoolean(settings[3]);
                        spawnDelay = Long.parseLong(settings[4]);
                        maxSpawnerLootSlots = Integer.parseInt(settings[5]);
                        maxStoredExp = Integer.parseInt(settings[6]);
                        minMobs = Integer.parseInt(settings[7]);
                        maxMobs = Integer.parseInt(settings[8]);
                        stackSize = Integer.parseInt(settings[9]);
                        maxStackSize = Integer.parseInt(settings[10]);
                        lastSpawnTime = Long.parseLong(settings[11]);
                        isAtCapacity = Boolean.parseBoolean(settings[12]);
                     } else if (settings.length >= 11) {
                        spawnerExp = Integer.parseInt(settings[0]);
                        spawnerActive = Boolean.parseBoolean(settings[1]);
                        spawnerRange = Integer.parseInt(settings[2]);
                        spawnerStop = Boolean.parseBoolean(settings[3]);
                        spawnDelay = Long.parseLong(settings[4]);
                        maxSpawnerLootSlots = Integer.parseInt(settings[5]);
                        maxStoredExp = Integer.parseInt(settings[6]);
                        minMobs = Integer.parseInt(settings[7]);
                        maxMobs = Integer.parseInt(settings[8]);
                        stackSize = Integer.parseInt(settings[9]);
                        lastSpawnTime = Long.parseLong(settings[10]);
                     }
                  } catch (NumberFormatException var37) {
                     this.logger.warning("Invalid settings format for spawner " + spawnerId + ", using defaults.");
                  }
               }

               String filteredItemsStr = yamlData.getString(path + ".filteredItems");
               String preferredSortItemStr = yamlData.getString(path + ".preferredSortItem");
               String lastInteractedPlayer = yamlData.getString(path + ".lastInteractedPlayer");
               List<String> inventoryData = yamlData.getStringList(path + ".inventory");
               String inventoryJson = this.serializeInventoryToJson(inventoryData);
               stmt.setString(1, spawnerId);
               stmt.setString(2, this.serverName);
               stmt.setString(3, worldName);
               stmt.setInt(4, locX);
               stmt.setInt(5, locY);
               stmt.setInt(6, locZ);
               stmt.setString(7, entityType.name());
               stmt.setString(8, itemSpawnerMaterial);
               stmt.setInt(9, spawnerExp);
               stmt.setBoolean(10, spawnerActive);
               stmt.setInt(11, spawnerRange);
               stmt.setBoolean(12, spawnerStop);
               stmt.setLong(13, spawnDelay);
               stmt.setInt(14, maxSpawnerLootSlots);
               stmt.setInt(15, maxStoredExp);
               stmt.setInt(16, minMobs);
               stmt.setInt(17, maxMobs);
               stmt.setInt(18, stackSize);
               stmt.setInt(19, maxStackSize);
               stmt.setLong(20, lastSpawnTime);
               stmt.setBoolean(21, isAtCapacity);
               stmt.setString(22, lastInteractedPlayer);
               stmt.setString(23, preferredSortItemStr);
               stmt.setString(24, filteredItemsStr);
               stmt.setString(25, inventoryJson);
               return true;
            }
         }
      }
   }

   private String serializeInventoryToJson(List<String> inventoryData) {
      if (inventoryData != null && !inventoryData.isEmpty()) {
         StringBuilder sb = new StringBuilder();
         sb.append("[");

         for(int i = 0; i < inventoryData.size(); ++i) {
            if (i > 0) {
               sb.append(",");
            }

            sb.append("\"").append(((String)inventoryData.get(i)).replace("\"", "\\\"")).append("\"");
         }

         sb.append("]");
         return sb.toString();
      } else {
         return null;
      }
   }
}
