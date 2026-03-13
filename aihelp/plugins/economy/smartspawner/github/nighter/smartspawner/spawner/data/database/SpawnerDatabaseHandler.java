package github.nighter.smartspawner.spawner.data.database;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.commands.list.gui.CrossServerSpawnerData;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.data.storage.StorageMode;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import github.nighter.smartspawner.spawner.utils.ItemStackSerializer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SpawnerDatabaseHandler implements SpawnerStorage {
   private final SmartSpawner plugin;
   private final Logger logger;
   private final DatabaseManager databaseManager;
   private final String serverName;
   private final Set<String> dirtySpawners = ConcurrentHashMap.newKeySet();
   private final Set<String> deletedSpawners = ConcurrentHashMap.newKeySet();
   private volatile boolean isSaving = false;
   private Scheduler.Task saveTask = null;
   private final Map<String, String> locationCache = new ConcurrentHashMap();
   private static final String SELECT_ALL_SQL = "SELECT spawner_id, world_name, loc_x, loc_y, loc_z, entity_type, item_spawner_material,\n       spawner_exp, spawner_active, spawner_range, spawner_stop, spawn_delay,\n       max_spawner_loot_slots, max_stored_exp, min_mobs, max_mobs, stack_size,\n       max_stack_size, last_spawn_time, is_at_capacity, last_interacted_player,\n       preferred_sort_item, filtered_items, inventory_data\nFROM smart_spawners WHERE server_name = ?\n";
   private static final String SELECT_ONE_SQL = "SELECT spawner_id, world_name, loc_x, loc_y, loc_z, entity_type, item_spawner_material,\n       spawner_exp, spawner_active, spawner_range, spawner_stop, spawn_delay,\n       max_spawner_loot_slots, max_stored_exp, min_mobs, max_mobs, stack_size,\n       max_stack_size, last_spawn_time, is_at_capacity, last_interacted_player,\n       preferred_sort_item, filtered_items, inventory_data\nFROM smart_spawners WHERE server_name = ? AND spawner_id = ?\n";
   private static final String SELECT_LOCATION_SQL = "SELECT world_name, loc_x, loc_y, loc_z FROM smart_spawners\nWHERE server_name = ? AND spawner_id = ?\n";
   private static final String UPSERT_SQL_MYSQL = "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON DUPLICATE KEY UPDATE\n    world_name = VALUES(world_name),\n    loc_x = VALUES(loc_x),\n    loc_y = VALUES(loc_y),\n    loc_z = VALUES(loc_z),\n    entity_type = VALUES(entity_type),\n    item_spawner_material = VALUES(item_spawner_material),\n    spawner_exp = VALUES(spawner_exp),\n    spawner_active = VALUES(spawner_active),\n    spawner_range = VALUES(spawner_range),\n    spawner_stop = VALUES(spawner_stop),\n    spawn_delay = VALUES(spawn_delay),\n    max_spawner_loot_slots = VALUES(max_spawner_loot_slots),\n    max_stored_exp = VALUES(max_stored_exp),\n    min_mobs = VALUES(min_mobs),\n    max_mobs = VALUES(max_mobs),\n    stack_size = VALUES(stack_size),\n    max_stack_size = VALUES(max_stack_size),\n    last_spawn_time = VALUES(last_spawn_time),\n    is_at_capacity = VALUES(is_at_capacity),\n    last_interacted_player = VALUES(last_interacted_player),\n    preferred_sort_item = VALUES(preferred_sort_item),\n    filtered_items = VALUES(filtered_items),\n    inventory_data = VALUES(inventory_data)\n";
   private static final String UPSERT_SQL_SQLITE = "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON CONFLICT(server_name, spawner_id) DO UPDATE SET\n    world_name = excluded.world_name,\n    loc_x = excluded.loc_x,\n    loc_y = excluded.loc_y,\n    loc_z = excluded.loc_z,\n    entity_type = excluded.entity_type,\n    item_spawner_material = excluded.item_spawner_material,\n    spawner_exp = excluded.spawner_exp,\n    spawner_active = excluded.spawner_active,\n    spawner_range = excluded.spawner_range,\n    spawner_stop = excluded.spawner_stop,\n    spawn_delay = excluded.spawn_delay,\n    max_spawner_loot_slots = excluded.max_spawner_loot_slots,\n    max_stored_exp = excluded.max_stored_exp,\n    min_mobs = excluded.min_mobs,\n    max_mobs = excluded.max_mobs,\n    stack_size = excluded.stack_size,\n    max_stack_size = excluded.max_stack_size,\n    last_spawn_time = excluded.last_spawn_time,\n    is_at_capacity = excluded.is_at_capacity,\n    last_interacted_player = excluded.last_interacted_player,\n    preferred_sort_item = excluded.preferred_sort_item,\n    filtered_items = excluded.filtered_items,\n    inventory_data = excluded.inventory_data\n";
   private static final String DELETE_SQL = "DELETE FROM smart_spawners WHERE server_name = ? AND spawner_id = ?\n";

   public SpawnerDatabaseHandler(SmartSpawner plugin, DatabaseManager databaseManager) {
      this.plugin = plugin;
      this.logger = plugin.getLogger();
      this.databaseManager = databaseManager;
      this.serverName = databaseManager.getServerName();
   }

   public boolean initialize() {
      if (!this.databaseManager.isActive()) {
         this.logger.severe("Database manager is not active, cannot initialize SpawnerDatabaseHandler");
         return false;
      } else {
         this.startSaveTask();
         return true;
      }
   }

   private void startSaveTask() {
      long intervalTicks = 6000L;
      if (this.saveTask != null) {
         this.saveTask.cancel();
         this.saveTask = null;
      }

      this.saveTask = Scheduler.runTaskTimerAsync(() -> {
         this.plugin.debug("Running scheduled database save task");
         this.flushChanges();
      }, intervalTicks, intervalTicks);
   }

   public void markSpawnerModified(String spawnerId) {
      if (spawnerId != null) {
         this.dirtySpawners.add(spawnerId);
         this.deletedSpawners.remove(spawnerId);
      }

   }

   public void markSpawnerDeleted(String spawnerId) {
      if (spawnerId != null) {
         this.deletedSpawners.add(spawnerId);
         this.dirtySpawners.remove(spawnerId);
         this.locationCache.remove(spawnerId);
      }

   }

   public void queueSpawnerForSaving(String spawnerId) {
      this.markSpawnerModified(spawnerId);
   }

   public void flushChanges() {
      if (this.dirtySpawners.isEmpty() && this.deletedSpawners.isEmpty()) {
         this.plugin.debug("No database changes to flush");
      } else if (this.isSaving) {
         this.plugin.debug("Database flush operation already in progress");
      } else {
         this.isSaving = true;
         SmartSpawner var10000 = this.plugin;
         int var10001 = this.dirtySpawners.size();
         var10000.debug("Flushing " + var10001 + " modified and " + this.deletedSpawners.size() + " deleted spawners to database");
         Scheduler.runTaskAsync(() -> {
            try {
               HashSet toDelete;
               if (!this.dirtySpawners.isEmpty()) {
                  toDelete = new HashSet(this.dirtySpawners);
                  this.dirtySpawners.removeAll(toDelete);
                  this.saveSpawnerBatch(toDelete);
               }

               if (!this.deletedSpawners.isEmpty()) {
                  toDelete = new HashSet(this.deletedSpawners);
                  this.deletedSpawners.removeAll(toDelete);
                  this.deleteSpawnerBatch(toDelete);
               }
            } catch (Exception var5) {
               this.logger.log(Level.SEVERE, "Error during database flush", var5);
            } finally {
               this.isSaving = false;
            }

         });
      }
   }

   private void saveSpawnerBatch(Set<String> spawnerIds) {
      if (!spawnerIds.isEmpty()) {
         String upsertSql = this.databaseManager.getStorageMode() == StorageMode.SQLITE ? "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON CONFLICT(server_name, spawner_id) DO UPDATE SET\n    world_name = excluded.world_name,\n    loc_x = excluded.loc_x,\n    loc_y = excluded.loc_y,\n    loc_z = excluded.loc_z,\n    entity_type = excluded.entity_type,\n    item_spawner_material = excluded.item_spawner_material,\n    spawner_exp = excluded.spawner_exp,\n    spawner_active = excluded.spawner_active,\n    spawner_range = excluded.spawner_range,\n    spawner_stop = excluded.spawner_stop,\n    spawn_delay = excluded.spawn_delay,\n    max_spawner_loot_slots = excluded.max_spawner_loot_slots,\n    max_stored_exp = excluded.max_stored_exp,\n    min_mobs = excluded.min_mobs,\n    max_mobs = excluded.max_mobs,\n    stack_size = excluded.stack_size,\n    max_stack_size = excluded.max_stack_size,\n    last_spawn_time = excluded.last_spawn_time,\n    is_at_capacity = excluded.is_at_capacity,\n    last_interacted_player = excluded.last_interacted_player,\n    preferred_sort_item = excluded.preferred_sort_item,\n    filtered_items = excluded.filtered_items,\n    inventory_data = excluded.inventory_data\n" : "INSERT INTO smart_spawners (\n    spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n    entity_type, item_spawner_material, spawner_exp, spawner_active,\n    spawner_range, spawner_stop, spawn_delay, max_spawner_loot_slots,\n    max_stored_exp, min_mobs, max_mobs, stack_size, max_stack_size,\n    last_spawn_time, is_at_capacity, last_interacted_player,\n    preferred_sort_item, filtered_items, inventory_data\n) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\nON DUPLICATE KEY UPDATE\n    world_name = VALUES(world_name),\n    loc_x = VALUES(loc_x),\n    loc_y = VALUES(loc_y),\n    loc_z = VALUES(loc_z),\n    entity_type = VALUES(entity_type),\n    item_spawner_material = VALUES(item_spawner_material),\n    spawner_exp = VALUES(spawner_exp),\n    spawner_active = VALUES(spawner_active),\n    spawner_range = VALUES(spawner_range),\n    spawner_stop = VALUES(spawner_stop),\n    spawn_delay = VALUES(spawn_delay),\n    max_spawner_loot_slots = VALUES(max_spawner_loot_slots),\n    max_stored_exp = VALUES(max_stored_exp),\n    min_mobs = VALUES(min_mobs),\n    max_mobs = VALUES(max_mobs),\n    stack_size = VALUES(stack_size),\n    max_stack_size = VALUES(max_stack_size),\n    last_spawn_time = VALUES(last_spawn_time),\n    is_at_capacity = VALUES(is_at_capacity),\n    last_interacted_player = VALUES(last_interacted_player),\n    preferred_sort_item = VALUES(preferred_sort_item),\n    filtered_items = VALUES(filtered_items),\n    inventory_data = VALUES(inventory_data)\n";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(upsertSql);

               try {
                  conn.setAutoCommit(false);
                  Iterator var5 = spawnerIds.iterator();

                  while(true) {
                     if (!var5.hasNext()) {
                        stmt.executeBatch();
                        conn.commit();
                        this.plugin.debug("Saved " + spawnerIds.size() + " spawners to database");
                        break;
                     }

                     String spawnerId = (String)var5.next();
                     SpawnerData spawner = this.plugin.getSpawnerManager().getSpawnerById(spawnerId);
                     if (spawner != null) {
                        this.setSpawnerParameters(stmt, spawner);
                        stmt.addBatch();
                     }
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
            } catch (Throwable var11) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var12) {
            this.logger.log(Level.SEVERE, "Error saving spawner batch to database", var12);
            this.dirtySpawners.addAll(spawnerIds);
         }

      }
   }

   private void deleteSpawnerBatch(Set<String> spawnerIds) {
      if (!spawnerIds.isEmpty()) {
         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement("DELETE FROM smart_spawners WHERE server_name = ? AND spawner_id = ?\n");

               try {
                  conn.setAutoCommit(false);
                  Iterator var4 = spawnerIds.iterator();

                  while(var4.hasNext()) {
                     String spawnerId = (String)var4.next();
                     stmt.setString(1, this.serverName);
                     stmt.setString(2, spawnerId);
                     stmt.addBatch();
                  }

                  stmt.executeBatch();
                  conn.commit();
                  this.plugin.debug("Deleted " + spawnerIds.size() + " spawners from database");
               } catch (Throwable var8) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                     }
                  }

                  throw var8;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var9) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var6) {
                     var9.addSuppressed(var6);
                  }
               }

               throw var9;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var10) {
            this.logger.log(Level.SEVERE, "Error deleting spawner batch from database", var10);
            this.deletedSpawners.addAll(spawnerIds);
         }

      }
   }

   private void setSpawnerParameters(PreparedStatement stmt, SpawnerData spawner) throws SQLException {
      Location loc = spawner.getSpawnerLocation();
      stmt.setString(1, spawner.getSpawnerId());
      stmt.setString(2, this.serverName);
      stmt.setString(3, loc.getWorld().getName());
      stmt.setInt(4, loc.getBlockX());
      stmt.setInt(5, loc.getBlockY());
      stmt.setInt(6, loc.getBlockZ());
      stmt.setString(7, spawner.getEntityType().name());
      stmt.setString(8, spawner.isItemSpawner() ? spawner.getSpawnedItemMaterial().name() : null);
      stmt.setInt(9, spawner.getSpawnerExp());
      stmt.setBoolean(10, spawner.getSpawnerActive());
      stmt.setInt(11, spawner.getSpawnerRange());
      stmt.setBoolean(12, spawner.getSpawnerStop().get());
      stmt.setLong(13, spawner.getSpawnDelay());
      stmt.setInt(14, spawner.getMaxSpawnerLootSlots());
      stmt.setInt(15, spawner.getMaxStoredExp());
      stmt.setInt(16, spawner.getMinMobs());
      stmt.setInt(17, spawner.getMaxMobs());
      stmt.setInt(18, spawner.getStackSize());
      stmt.setInt(19, spawner.getMaxStackSize());
      stmt.setLong(20, spawner.getLastSpawnTime());
      stmt.setBoolean(21, spawner.getIsAtCapacity());
      stmt.setString(22, spawner.getLastInteractedPlayer());
      stmt.setString(23, spawner.getPreferredSortItem() != null ? spawner.getPreferredSortItem().name() : null);
      stmt.setString(24, this.serializeFilteredItems(spawner.getFilteredItems()));
      stmt.setString(25, this.serializeInventory(spawner.getVirtualInventory()));
   }

   public Map<String, SpawnerData> loadAllSpawnersRaw() {
      HashMap loadedSpawners = new HashMap();

      try {
         Connection conn = this.databaseManager.getConnection();

         try {
            PreparedStatement stmt = conn.prepareStatement("SELECT spawner_id, world_name, loc_x, loc_y, loc_z, entity_type, item_spawner_material,\n       spawner_exp, spawner_active, spawner_range, spawner_stop, spawn_delay,\n       max_spawner_loot_slots, max_stored_exp, min_mobs, max_mobs, stack_size,\n       max_stack_size, last_spawn_time, is_at_capacity, last_interacted_player,\n       preferred_sort_item, filtered_items, inventory_data\nFROM smart_spawners WHERE server_name = ?\n");

            try {
               stmt.setString(1, this.serverName);
               ResultSet rs = stmt.executeQuery();

               try {
                  while(rs.next()) {
                     String spawnerId = rs.getString("spawner_id");

                     try {
                        SpawnerData spawner = this.loadSpawnerFromResultSet(rs);
                        loadedSpawners.put(spawnerId, spawner);
                        if (spawner == null) {
                           String worldName = rs.getString("world_name");
                           int x = rs.getInt("loc_x");
                           int y = rs.getInt("loc_y");
                           int z = rs.getInt("loc_z");
                           this.locationCache.put(spawnerId, String.format("%s,%d,%d,%d", worldName, x, y, z));
                        }
                     } catch (Exception var14) {
                        this.plugin.debug("Error loading spawner " + spawnerId + ": " + var14.getMessage());
                        loadedSpawners.put(spawnerId, (Object)null);
                     }
                  }
               } catch (Throwable var15) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var13) {
                        var15.addSuppressed(var13);
                     }
                  }

                  throw var15;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var16) {
               if (stmt != null) {
                  try {
                     stmt.close();
                  } catch (Throwable var12) {
                     var16.addSuppressed(var12);
                  }
               }

               throw var16;
            }

            if (stmt != null) {
               stmt.close();
            }
         } catch (Throwable var17) {
            if (conn != null) {
               try {
                  conn.close();
               } catch (Throwable var11) {
                  var17.addSuppressed(var11);
               }
            }

            throw var17;
         }

         if (conn != null) {
            conn.close();
         }
      } catch (SQLException var18) {
         this.logger.log(Level.SEVERE, "Error loading spawners from database", var18);
      }

      return loadedSpawners;
   }

   public SpawnerData loadSpecificSpawner(String spawnerId) {
      try {
         Connection conn = this.databaseManager.getConnection();

         SpawnerData var5;
         label110: {
            try {
               PreparedStatement stmt;
               label102: {
                  stmt = conn.prepareStatement("SELECT spawner_id, world_name, loc_x, loc_y, loc_z, entity_type, item_spawner_material,\n       spawner_exp, spawner_active, spawner_range, spawner_stop, spawn_delay,\n       max_spawner_loot_slots, max_stored_exp, min_mobs, max_mobs, stack_size,\n       max_stack_size, last_spawn_time, is_at_capacity, last_interacted_player,\n       preferred_sort_item, filtered_items, inventory_data\nFROM smart_spawners WHERE server_name = ? AND spawner_id = ?\n");

                  try {
                     stmt.setString(1, this.serverName);
                     stmt.setString(2, spawnerId);
                     ResultSet rs = stmt.executeQuery();

                     label87: {
                        try {
                           if (rs.next()) {
                              var5 = this.loadSpawnerFromResultSet(rs);
                              break label87;
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
                        break label102;
                     }

                     if (rs != null) {
                        rs.close();
                     }
                  } catch (Throwable var11) {
                     if (stmt != null) {
                        try {
                           stmt.close();
                        } catch (Throwable var8) {
                           var11.addSuppressed(var8);
                        }
                     }

                     throw var11;
                  }

                  if (stmt != null) {
                     stmt.close();
                  }
                  break label110;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var12) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var7) {
                     var12.addSuppressed(var7);
                  }
               }

               throw var12;
            }

            if (conn != null) {
               conn.close();
            }

            return null;
         }

         if (conn != null) {
            conn.close();
         }

         return var5;
      } catch (SQLException var13) {
         this.logger.log(Level.SEVERE, "Error loading spawner " + spawnerId + " from database", var13);
         return null;
      }
   }

   public String getRawLocationString(String spawnerId) {
      String cached = (String)this.locationCache.get(spawnerId);
      if (cached != null) {
         return cached;
      } else {
         try {
            Connection conn = this.databaseManager.getConnection();

            String var11;
            label117: {
               try {
                  PreparedStatement stmt;
                  label108: {
                     stmt = conn.prepareStatement("SELECT world_name, loc_x, loc_y, loc_z FROM smart_spawners\nWHERE server_name = ? AND spawner_id = ?\n");

                     try {
                        stmt.setString(1, this.serverName);
                        stmt.setString(2, spawnerId);
                        ResultSet rs = stmt.executeQuery();

                        label90: {
                           try {
                              if (rs.next()) {
                                 String worldName = rs.getString("world_name");
                                 int x = rs.getInt("loc_x");
                                 int y = rs.getInt("loc_y");
                                 int z = rs.getInt("loc_z");
                                 String location = String.format("%s,%d,%d,%d", worldName, x, y, z);
                                 this.locationCache.put(spawnerId, location);
                                 var11 = location;
                                 break label90;
                              }
                           } catch (Throwable var15) {
                              if (rs != null) {
                                 try {
                                    rs.close();
                                 } catch (Throwable var14) {
                                    var15.addSuppressed(var14);
                                 }
                              }

                              throw var15;
                           }

                           if (rs != null) {
                              rs.close();
                           }
                           break label108;
                        }

                        if (rs != null) {
                           rs.close();
                        }
                     } catch (Throwable var16) {
                        if (stmt != null) {
                           try {
                              stmt.close();
                           } catch (Throwable var13) {
                              var16.addSuppressed(var13);
                           }
                        }

                        throw var16;
                     }

                     if (stmt != null) {
                        stmt.close();
                     }
                     break label117;
                  }

                  if (stmt != null) {
                     stmt.close();
                  }
               } catch (Throwable var17) {
                  if (conn != null) {
                     try {
                        conn.close();
                     } catch (Throwable var12) {
                        var17.addSuppressed(var12);
                     }
                  }

                  throw var17;
               }

               if (conn != null) {
                  conn.close();
               }

               return null;
            }

            if (conn != null) {
               conn.close();
            }

            return var11;
         } catch (SQLException var18) {
            this.logger.log(Level.SEVERE, "Error getting location for spawner " + spawnerId, var18);
            return null;
         }
      }
   }

   private SpawnerData loadSpawnerFromResultSet(ResultSet rs) throws SQLException {
      String spawnerId = rs.getString("spawner_id");
      String worldName = rs.getString("world_name");
      int x = rs.getInt("loc_x");
      int y = rs.getInt("loc_y");
      int z = rs.getInt("loc_z");
      World world = Bukkit.getWorld(worldName);
      if (world == null) {
         this.plugin.debug("World not yet loaded for spawner " + spawnerId + ": " + worldName);
         return null;
      } else {
         Location location = new Location(world, (double)x, (double)y, (double)z);
         String entityTypeStr = rs.getString("entity_type");

         EntityType entityType;
         try {
            entityType = EntityType.valueOf(entityTypeStr);
         } catch (IllegalArgumentException var21) {
            this.logger.severe("Invalid entity type for spawner " + spawnerId + ": " + entityTypeStr);
            return null;
         }

         String itemMaterialStr = rs.getString("item_spawner_material");
         SpawnerData spawner;
         if (entityType == EntityType.ITEM && itemMaterialStr != null) {
            try {
               Material itemMaterial = Material.valueOf(itemMaterialStr);
               spawner = new SpawnerData(spawnerId, location, itemMaterial, this.plugin);
            } catch (IllegalArgumentException var20) {
               this.logger.severe("Invalid item spawner material for spawner " + spawnerId + ": " + itemMaterialStr);
               return null;
            }
         } else {
            spawner = new SpawnerData(spawnerId, location, entityType, this.plugin);
         }

         spawner.setSpawnerExpData(rs.getInt("spawner_exp"));
         spawner.setSpawnerActive(rs.getBoolean("spawner_active"));
         spawner.setSpawnerRange(rs.getInt("spawner_range"));
         spawner.getSpawnerStop().set(rs.getBoolean("spawner_stop"));
         spawner.setSpawnDelayFromConfig();
         spawner.setMaxSpawnerLootSlots(rs.getInt("max_spawner_loot_slots"));
         spawner.setMaxStoredExp(rs.getInt("max_stored_exp"));
         spawner.setMinMobs(rs.getInt("min_mobs"));
         spawner.setMaxMobs(rs.getInt("max_mobs"));
         spawner.setStackSize(rs.getInt("stack_size"), false);
         spawner.setMaxStackSize(rs.getInt("max_stack_size"));
         spawner.setLastSpawnTime(rs.getLong("last_spawn_time"));
         spawner.setIsAtCapacity(rs.getBoolean("is_at_capacity"));
         spawner.setLastInteractedPlayer(rs.getString("last_interacted_player"));
         String preferredSortItemStr = rs.getString("preferred_sort_item");
         if (preferredSortItemStr != null && !preferredSortItemStr.isEmpty()) {
            try {
               Material preferredSortItem = Material.valueOf(preferredSortItemStr);
               spawner.setPreferredSortItem(preferredSortItem);
            } catch (IllegalArgumentException var19) {
               this.logger.warning("Invalid preferred sort item for spawner " + spawnerId + ": " + preferredSortItemStr);
            }
         }

         String filteredItemsStr = rs.getString("filtered_items");
         if (filteredItemsStr != null && !filteredItemsStr.isEmpty()) {
            this.deserializeFilteredItems(filteredItemsStr, spawner.getFilteredItems());
         }

         String inventoryData = rs.getString("inventory_data");
         VirtualInventory virtualInv = new VirtualInventory(spawner.getMaxSpawnerLootSlots());
         if (inventoryData != null && !inventoryData.isEmpty()) {
            try {
               this.loadInventoryFromJson(inventoryData, virtualInv);
            } catch (Exception var18) {
               this.logger.warning("Error loading inventory for spawner " + spawnerId + ": " + var18.getMessage());
            }
         }

         spawner.setVirtualInventory(virtualInv);
         spawner.recalculateSellValue();
         if (spawner.getPreferredSortItem() != null) {
            virtualInv.sortItems(spawner.getPreferredSortItem());
         }

         if (spawner.isItemSpawner()) {
            Scheduler.runLocationTask(location, () -> {
               Block block = location.getBlock();
               if (block.getType() == Material.SPAWNER) {
                  BlockState state = block.getState(false);
                  if (state instanceof CreatureSpawner) {
                     CreatureSpawner cs = (CreatureSpawner)state;
                     cs.setSpawnedType(EntityType.ITEM);
                     ItemStack spawnedItem = new ItemStack(spawner.getSpawnedItemMaterial(), 1);
                     cs.setSpawnedItem(spawnedItem);
                     cs.update(true, false);
                  }
               }

            });
         }

         return spawner;
      }
   }

   public void shutdown() {
      if (this.saveTask != null) {
         this.saveTask.cancel();
         this.saveTask = null;
      }

      if (!this.dirtySpawners.isEmpty() || !this.deletedSpawners.isEmpty()) {
         try {
            this.isSaving = true;
            this.logger.info("Saving " + this.dirtySpawners.size() + " spawners to database on shutdown...");
            if (!this.dirtySpawners.isEmpty()) {
               this.saveSpawnerBatch(new HashSet(this.dirtySpawners));
            }

            if (!this.deletedSpawners.isEmpty()) {
               this.deleteSpawnerBatch(new HashSet(this.deletedSpawners));
            }

            this.dirtySpawners.clear();
            this.deletedSpawners.clear();
            this.logger.info("Database shutdown save completed.");
         } catch (Exception var5) {
            this.logger.log(Level.SEVERE, "Error during database shutdown flush", var5);
         } finally {
            this.isSaving = false;
         }
      }

      this.locationCache.clear();
   }

   private String serializeFilteredItems(Set<Material> filteredItems) {
      return filteredItems != null && !filteredItems.isEmpty() ? (String)filteredItems.stream().map(Enum::name).collect(Collectors.joining(",")) : null;
   }

   private void deserializeFilteredItems(String data, Set<Material> filteredItems) {
      if (data != null && !data.isEmpty()) {
         String[] materialNames = data.split(",");
         String[] var4 = materialNames;
         int var5 = materialNames.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String materialName = var4[var6];

            try {
               Material material = Material.valueOf(materialName.trim());
               filteredItems.add(material);
            } catch (IllegalArgumentException var9) {
               this.logger.warning("Invalid material in filtered items: " + materialName);
            }
         }

      }
   }

   private String serializeInventory(VirtualInventory virtualInv) {
      if (virtualInv == null) {
         return null;
      } else {
         Map<VirtualInventory.ItemSignature, Long> items = virtualInv.getConsolidatedItems();
         if (items.isEmpty()) {
            return null;
         } else {
            List<String> serializedItems = ItemStackSerializer.serializeInventory(items);
            if (serializedItems.isEmpty()) {
               return null;
            } else {
               StringBuilder sb = new StringBuilder();
               sb.append("[");

               for(int i = 0; i < serializedItems.size(); ++i) {
                  if (i > 0) {
                     sb.append(",");
                  }

                  sb.append("\"").append(((String)serializedItems.get(i)).replace("\"", "\\\"")).append("\"");
               }

               sb.append("]");
               return sb.toString();
            }
         }
      }
   }

   private void loadInventoryFromJson(String jsonData, VirtualInventory virtualInv) {
      if (jsonData != null && !jsonData.isEmpty()) {
         if (jsonData.startsWith("[") && jsonData.endsWith("]")) {
            String content = jsonData.substring(1, jsonData.length() - 1);
            if (!content.isEmpty()) {
               List<String> items = new ArrayList();
               StringBuilder current = new StringBuilder();
               boolean inQuotes = false;
               boolean escaped = false;
               char[] var8 = content.toCharArray();
               int var9 = var8.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  char c = var8[var10];
                  if (escaped) {
                     current.append(c);
                     escaped = false;
                  } else if (c == '\\') {
                     escaped = true;
                  } else if (c == '"') {
                     inQuotes = !inQuotes;
                  } else if (c == ',' && !inQuotes) {
                     if (current.length() > 0) {
                        items.add(current.toString());
                        current = new StringBuilder();
                     }
                  } else {
                     current.append(c);
                  }
               }

               if (current.length() > 0) {
                  items.add(current.toString());
               }

               if (!items.isEmpty()) {
                  try {
                     Map<ItemStack, Integer> deserializedItems = ItemStackSerializer.deserializeInventory(items);
                     Iterator var17 = deserializedItems.entrySet().iterator();

                     while(true) {
                        int amount;
                        ItemStack item;
                        do {
                           do {
                              if (!var17.hasNext()) {
                                 return;
                              }

                              Entry<ItemStack, Integer> entry = (Entry)var17.next();
                              item = (ItemStack)entry.getKey();
                              amount = (Integer)entry.getValue();
                           } while(item == null);
                        } while(amount <= 0);

                        while(amount > 0) {
                           int batchSize = Math.min(amount, item.getMaxStackSize());
                           ItemStack batch = item.clone();
                           batch.setAmount(batchSize);
                           virtualInv.addItems(Collections.singletonList(batch));
                           amount -= batchSize;
                        }
                     }
                  } catch (Exception var15) {
                     this.logger.warning("Error deserializing inventory data: " + var15.getMessage());
                  }
               }
            }
         } else {
            this.logger.warning("Invalid inventory JSON format: " + jsonData);
         }
      }
   }

   public String getServerName() {
      return this.serverName;
   }

   public void getDistinctServerNamesAsync(Consumer<List<String>> callback) {
      Scheduler.runTaskAsync(() -> {
         List<String> servers = new ArrayList();
         String sql = "SELECT DISTINCT server_name FROM smart_spawners ORDER BY server_name";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  ResultSet rs = stmt.executeQuery();

                  try {
                     while(rs.next()) {
                        servers.add(rs.getString("server_name"));
                     }
                  } catch (Throwable var12) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var11) {
                           var12.addSuppressed(var11);
                        }
                     }

                     throw var12;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var13) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var10) {
                        var13.addSuppressed(var10);
                     }
                  }

                  throw var13;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var14) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var9) {
                     var14.addSuppressed(var9);
                  }
               }

               throw var14;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var15) {
            this.logger.log(Level.SEVERE, "Error fetching server names from database", var15);
         }

         Scheduler.runTask(() -> {
            callback.accept(servers);
         });
      });
   }

   public void getWorldsForServerAsync(String targetServer, Consumer<Map<String, Integer>> callback) {
      Scheduler.runTaskAsync(() -> {
         Map<String, Integer> worlds = new LinkedHashMap();
         String sql = "SELECT world_name, COUNT(*) as count FROM smart_spawners WHERE server_name = ? GROUP BY world_name ORDER BY world_name";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setString(1, targetServer);
                  ResultSet rs = stmt.executeQuery();

                  try {
                     while(rs.next()) {
                        worlds.put(rs.getString("world_name"), rs.getInt("count"));
                     }
                  } catch (Throwable var13) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var12) {
                           var13.addSuppressed(var12);
                        }
                     }

                     throw var13;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var14) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var11) {
                        var14.addSuppressed(var11);
                     }
                  }

                  throw var14;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var15) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var10) {
                     var15.addSuppressed(var10);
                  }
               }

               throw var15;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var16) {
            this.logger.log(Level.SEVERE, "Error fetching worlds for server " + targetServer, var16);
         }

         Scheduler.runTask(() -> {
            callback.accept(worlds);
         });
      });
   }

   public void getTotalStacksForWorldAsync(String targetServer, String worldName, Consumer<Integer> callback) {
      Scheduler.runTaskAsync(() -> {
         int total = 0;
         String sql = "SELECT SUM(stack_size) as total FROM smart_spawners WHERE server_name = ? AND world_name = ?";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setString(1, targetServer);
                  stmt.setString(2, worldName);
                  ResultSet rs = stmt.executeQuery();

                  try {
                     if (rs.next()) {
                        total = rs.getInt("total");
                     }
                  } catch (Throwable var14) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var13) {
                           var14.addSuppressed(var13);
                        }
                     }

                     throw var14;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var15) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var12) {
                        var15.addSuppressed(var12);
                     }
                  }

                  throw var15;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var16) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var11) {
                     var16.addSuppressed(var11);
                  }
               }

               throw var16;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var17) {
            this.logger.log(Level.SEVERE, "Error fetching stack total for " + targetServer + "/" + worldName, var17);
         }

         Scheduler.runTask(() -> {
            callback.accept(total);
         });
      });
   }

   public void getCrossServerSpawnersAsync(String targetServer, String worldName, Consumer<List<CrossServerSpawnerData>> callback) {
      Scheduler.runTaskAsync(() -> {
         List<CrossServerSpawnerData> spawners = new ArrayList();
         String sql = "SELECT spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n       entity_type, stack_size, spawner_stop, last_interacted_player,\n       spawner_exp, inventory_data\nFROM smart_spawners\nWHERE server_name = ? AND world_name = ?\nORDER BY stack_size DESC\n";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setString(1, targetServer);
                  stmt.setString(2, worldName);
                  ResultSet rs = stmt.executeQuery();

                  try {
                     while(rs.next()) {
                        String spawnerId = rs.getString("spawner_id");
                        String server = rs.getString("server_name");
                        String world = rs.getString("world_name");
                        int x = rs.getInt("loc_x");
                        int y = rs.getInt("loc_y");
                        int z = rs.getInt("loc_z");

                        EntityType entityType;
                        try {
                           entityType = EntityType.valueOf(rs.getString("entity_type"));
                        } catch (IllegalArgumentException var25) {
                           entityType = EntityType.PIG;
                        }

                        int stackSize = rs.getInt("stack_size");
                        boolean active = !rs.getBoolean("spawner_stop");
                        String lastPlayer = rs.getString("last_interacted_player");
                        int storedExp = rs.getInt("spawner_exp");
                        long totalItems = this.estimateItemCount(rs.getString("inventory_data"));
                        spawners.add(new CrossServerSpawnerData(spawnerId, server, world, x, y, z, entityType, stackSize, active, lastPlayer, storedExp, totalItems));
                     }
                  } catch (Throwable var26) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var24) {
                           var26.addSuppressed(var24);
                        }
                     }

                     throw var26;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var27) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var23) {
                        var27.addSuppressed(var23);
                     }
                  }

                  throw var27;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var28) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var22) {
                     var28.addSuppressed(var22);
                  }
               }

               throw var28;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var29) {
            this.logger.log(Level.SEVERE, "Error fetching spawners for " + targetServer + "/" + worldName, var29);
         }

         Scheduler.runTask(() -> {
            callback.accept(spawners);
         });
      });
   }

   public void getSpawnerCountForServerAsync(String targetServer, Consumer<Integer> callback) {
      Scheduler.runTaskAsync(() -> {
         int count = 0;
         String sql = "SELECT COUNT(*) as count FROM smart_spawners WHERE server_name = ?";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setString(1, targetServer);
                  ResultSet rs = stmt.executeQuery();

                  try {
                     if (rs.next()) {
                        count = rs.getInt("count");
                     }
                  } catch (Throwable var13) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var12) {
                           var13.addSuppressed(var12);
                        }
                     }

                     throw var13;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var14) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var11) {
                        var14.addSuppressed(var11);
                     }
                  }

                  throw var14;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var15) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var10) {
                     var15.addSuppressed(var10);
                  }
               }

               throw var15;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var16) {
            this.logger.log(Level.SEVERE, "Error fetching spawner count for " + targetServer, var16);
         }

         Scheduler.runTask(() -> {
            callback.accept(count);
         });
      });
   }

   public void getCrossServerSpawnersAsync(String targetServer, String worldName, String filter, String sort, Consumer<List<CrossServerSpawnerData>> callback) {
      Scheduler.runTaskAsync(() -> {
         List<CrossServerSpawnerData> spawners = new ArrayList();
         StringBuilder sql = new StringBuilder("SELECT spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n       entity_type, stack_size, spawner_stop, last_interacted_player,\n       spawner_exp, inventory_data\nFROM smart_spawners\nWHERE server_name = ? AND world_name = ?\n");
         if ("ACTIVE".equalsIgnoreCase(filter)) {
            sql.append(" AND spawner_stop = FALSE");
         } else if ("INACTIVE".equalsIgnoreCase(filter)) {
            sql.append(" AND spawner_stop = TRUE");
         }

         if ("STACK_SIZE_ASC".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY stack_size ASC");
         } else if ("STACK_SIZE_DESC".equalsIgnoreCase(sort)) {
            sql.append(" ORDER BY stack_size DESC");
         } else {
            sql.append(" ORDER BY spawner_id ASC");
         }

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql.toString());

               try {
                  stmt.setString(1, targetServer);
                  stmt.setString(2, worldName);
                  ResultSet rs = stmt.executeQuery();

                  try {
                     while(rs.next()) {
                        String spawnerId = rs.getString("spawner_id");
                        String server = rs.getString("server_name");
                        String world = rs.getString("world_name");
                        int x = rs.getInt("loc_x");
                        int y = rs.getInt("loc_y");
                        int z = rs.getInt("loc_z");

                        EntityType entityType;
                        try {
                           entityType = EntityType.valueOf(rs.getString("entity_type"));
                        } catch (IllegalArgumentException var27) {
                           entityType = EntityType.PIG;
                        }

                        int stackSize = rs.getInt("stack_size");
                        boolean active = !rs.getBoolean("spawner_stop");
                        String lastPlayer = rs.getString("last_interacted_player");
                        int storedExp = rs.getInt("spawner_exp");
                        long totalItems = this.estimateItemCount(rs.getString("inventory_data"));
                        spawners.add(new CrossServerSpawnerData(spawnerId, server, world, x, y, z, entityType, stackSize, active, lastPlayer, storedExp, totalItems));
                     }
                  } catch (Throwable var28) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var26) {
                           var28.addSuppressed(var26);
                        }
                     }

                     throw var28;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var29) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var25) {
                        var29.addSuppressed(var25);
                     }
                  }

                  throw var29;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var30) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var24) {
                     var30.addSuppressed(var24);
                  }
               }

               throw var30;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var31) {
            this.logger.log(Level.SEVERE, "Error fetching spawners for " + targetServer + "/" + worldName, var31);
         }

         Scheduler.runTask(() -> {
            callback.accept(spawners);
         });
      });
   }

   public void getRemoteSpawnerByIdAsync(String targetServer, String spawnerId, Consumer<CrossServerSpawnerData> callback) {
      Scheduler.runTaskAsync(() -> {
         CrossServerSpawnerData spawnerData = null;
         String sql = "SELECT spawner_id, server_name, world_name, loc_x, loc_y, loc_z,\n       entity_type, stack_size, spawner_stop, last_interacted_player,\n       spawner_exp, inventory_data\nFROM smart_spawners\nWHERE server_name = ? AND spawner_id = ?\n";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setString(1, targetServer);
                  stmt.setString(2, spawnerId);
                  ResultSet rs = stmt.executeQuery();

                  try {
                     if (rs.next()) {
                        String world = rs.getString("world_name");
                        int x = rs.getInt("loc_x");
                        int y = rs.getInt("loc_y");
                        int z = rs.getInt("loc_z");

                        EntityType entityType;
                        try {
                           entityType = EntityType.valueOf(rs.getString("entity_type"));
                        } catch (IllegalArgumentException var23) {
                           entityType = EntityType.PIG;
                        }

                        int stackSize = rs.getInt("stack_size");
                        boolean active = !rs.getBoolean("spawner_stop");
                        String lastPlayer = rs.getString("last_interacted_player");
                        int storedExp = rs.getInt("spawner_exp");
                        long totalItems = this.estimateItemCount(rs.getString("inventory_data"));
                        spawnerData = new CrossServerSpawnerData(spawnerId, targetServer, world, x, y, z, entityType, stackSize, active, lastPlayer, storedExp, totalItems);
                     }
                  } catch (Throwable var24) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var22) {
                           var24.addSuppressed(var22);
                        }
                     }

                     throw var24;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var25) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var21) {
                        var25.addSuppressed(var21);
                     }
                  }

                  throw var25;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var26) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var20) {
                     var26.addSuppressed(var20);
                  }
               }

               throw var26;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var27) {
            this.logger.log(Level.SEVERE, "Error fetching remote spawner " + spawnerId + " from " + targetServer, var27);
         }

         Scheduler.runTask(() -> {
            callback.accept(spawnerData);
         });
      });
   }

   public void updateRemoteSpawnerStackSizeAsync(String targetServer, String spawnerId, int newStackSize, Consumer<Boolean> callback) {
      Scheduler.runTaskAsync(() -> {
         boolean success = false;
         String sql = "UPDATE smart_spawners SET stack_size = ?, updated_at = CURRENT_TIMESTAMP WHERE server_name = ? AND spawner_id = ?";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setInt(1, newStackSize);
                  stmt.setString(2, targetServer);
                  stmt.setString(3, spawnerId);
                  int affected = stmt.executeUpdate();
                  success = affected > 0;
                  if (success) {
                     this.plugin.debug("Updated remote spawner " + spawnerId + " on " + targetServer + " to stack size " + newStackSize);
                  }
               } catch (Throwable var13) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                     }
                  }

                  throw var13;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var14) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var11) {
                     var14.addSuppressed(var11);
                  }
               }

               throw var14;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var15) {
            this.logger.log(Level.SEVERE, "Error updating remote spawner stack size", var15);
         }

         Scheduler.runTask(() -> {
            callback.accept(success);
         });
      });
   }

   public void deleteRemoteSpawnerAsync(String targetServer, String spawnerId, Consumer<Boolean> callback) {
      Scheduler.runTaskAsync(() -> {
         boolean success = false;
         String sql = "DELETE FROM smart_spawners WHERE server_name = ? AND spawner_id = ?";

         try {
            Connection conn = this.databaseManager.getConnection();

            try {
               PreparedStatement stmt = conn.prepareStatement(sql);

               try {
                  stmt.setString(1, targetServer);
                  stmt.setString(2, spawnerId);
                  int affected = stmt.executeUpdate();
                  success = affected > 0;
                  if (success) {
                     this.logger.info("Deleted remote spawner " + spawnerId + " from " + targetServer + " database record");
                  }
               } catch (Throwable var12) {
                  if (stmt != null) {
                     try {
                        stmt.close();
                     } catch (Throwable var11) {
                        var12.addSuppressed(var11);
                     }
                  }

                  throw var12;
               }

               if (stmt != null) {
                  stmt.close();
               }
            } catch (Throwable var13) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var10) {
                     var13.addSuppressed(var10);
                  }
               }

               throw var13;
            }

            if (conn != null) {
               conn.close();
            }
         } catch (SQLException var14) {
            this.logger.log(Level.SEVERE, "Error deleting remote spawner", var14);
         }

         Scheduler.runTask(() -> {
            callback.accept(success);
         });
      });
   }

   private long estimateItemCount(String inventoryData) {
      if (inventoryData != null && !inventoryData.isEmpty()) {
         long total = 0L;

         try {
            String[] parts = inventoryData.split(":");

            for(int i = 1; i < parts.length; ++i) {
               String numPart = parts[i].replaceAll("[^0-9]", " ").trim().split(" ")[0];
               if (!numPart.isEmpty()) {
                  total += Long.parseLong(numPart);
               }
            }
         } catch (Exception var7) {
         }

         return total;
      } else {
         return 0L;
      }
   }
}
