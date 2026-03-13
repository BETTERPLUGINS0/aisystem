package github.nighter.smartspawner.spawner.data;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.properties.VirtualInventory;
import github.nighter.smartspawner.spawner.utils.ItemStackSerializer;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class SpawnerFileHandler implements SpawnerStorage {
   private final SmartSpawner plugin;
   private final Logger logger;
   private File spawnerDataFile;
   private FileConfiguration spawnerData;
   private static final String DATA_VERSION_KEY = "data_version";
   private final int CURRENT_VERSION;
   private final Set<String> dirtySpawners = ConcurrentHashMap.newKeySet();
   private final Set<String> deletedSpawners = ConcurrentHashMap.newKeySet();
   private volatile boolean isSaving = false;
   private Scheduler.Task saveTask = null;

   public SpawnerFileHandler(SmartSpawner plugin) {
      this.plugin = plugin;
      this.logger = plugin.getLogger();
      this.CURRENT_VERSION = plugin.getDATA_VERSION();
      this.setupSpawnerDataFile();
      this.startSaveTask();
   }

   public boolean initialize() {
      return this.spawnerDataFile != null && this.spawnerDataFile.exists();
   }

   private void setupSpawnerDataFile() {
      this.spawnerDataFile = new File(this.plugin.getDataFolder(), "spawners_data.yml");
      if (!this.spawnerDataFile.exists()) {
         this.plugin.saveResource("spawners_data.yml", false);
      }

      this.spawnerData = YamlConfiguration.loadConfiguration(this.spawnerDataFile);
      int version = this.spawnerData.getInt("data_version", 1);
      if (version < this.CURRENT_VERSION) {
         this.logger.info("Data version " + version + " detected. Current version is " + this.CURRENT_VERSION + ".");
         this.logger.info("A migration will be attempted when the plugin fully loads.");
      }

   }

   private void startSaveTask() {
      long intervalTicks = 6000L;
      if (this.saveTask != null) {
         this.saveTask.cancel();
         this.saveTask = null;
      }

      this.saveTask = Scheduler.runTaskTimerAsync(() -> {
         this.plugin.debug("Running scheduled save task");
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
      }

   }

   public void flushChanges() {
      if (this.dirtySpawners.isEmpty() && this.deletedSpawners.isEmpty()) {
         this.plugin.debug("No changes to flush");
      } else if (this.isSaving) {
         this.plugin.debug("Flush operation already in progress");
      } else {
         this.isSaving = true;
         SmartSpawner var10000 = this.plugin;
         int var10001 = this.dirtySpawners.size();
         var10000.debug("Flushing " + var10001 + " modified and " + this.deletedSpawners.size() + " deleted spawners");
         Scheduler.runTaskAsync(() -> {
            try {
               Iterator var2;
               String id;
               try {
                  HashSet toDelete;
                  String idx;
                  if (!this.dirtySpawners.isEmpty()) {
                     toDelete = new HashSet(this.dirtySpawners);
                     this.dirtySpawners.removeAll(toDelete);
                     Map<String, SpawnerData> batch = new HashMap();
                     Iterator var12 = toDelete.iterator();

                     while(true) {
                        if (!var12.hasNext()) {
                           if (!batch.isEmpty()) {
                              this.saveSpawnerBatch(batch);
                           }
                           break;
                        }

                        idx = (String)var12.next();
                        SpawnerData spawner = this.plugin.getSpawnerManager().getSpawnerById(idx);
                        if (spawner != null) {
                           batch.put(idx, spawner);
                        }
                     }
                  }

                  if (!this.deletedSpawners.isEmpty()) {
                     toDelete = new HashSet(this.deletedSpawners);
                     this.deletedSpawners.removeAll(toDelete);
                     var2 = toDelete.iterator();

                     while(var2.hasNext()) {
                        id = (String)var2.next();
                        idx = "spawners." + id;
                        this.spawnerData.set(idx, (Object)null);
                     }

                     if (!toDelete.isEmpty()) {
                        this.spawnerData.save(this.spawnerDataFile);
                     }
                  }
               } catch (Exception var9) {
                  this.plugin.getLogger().severe("Error during flush: " + var9.getMessage());
                  var9.printStackTrace();
                  var2 = this.dirtySpawners.iterator();

                  while(var2.hasNext()) {
                     id = (String)var2.next();
                     this.dirtySpawners.add(id);
                  }

                  var2 = this.deletedSpawners.iterator();

                  while(var2.hasNext()) {
                     id = (String)var2.next();
                     this.deletedSpawners.add(id);
                  }
               }
            } finally {
               this.isSaving = false;
            }

         });
      }
   }

   private boolean saveSpawnerBatch(Map<String, SpawnerData> spawners) {
      if (spawners.isEmpty()) {
         return true;
      } else {
         try {
            ConfigurationSection spawnersSection = this.spawnerData.getConfigurationSection("spawners");
            if (spawnersSection == null) {
               spawnersSection = this.spawnerData.createSection("spawners");
            }

            this.spawnerData.set("data_version", this.CURRENT_VERSION);
            Iterator var3 = spawners.entrySet().iterator();

            while(var3.hasNext()) {
               Entry<String, SpawnerData> entry = (Entry)var3.next();
               String spawnerId = (String)entry.getKey();
               SpawnerData spawner = (SpawnerData)entry.getValue();
               String path = "spawners." + spawnerId;
               Location loc = spawner.getSpawnerLocation();
               this.spawnerData.set(path + ".location", String.format("%s,%d,%d,%d", loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()));
               this.spawnerData.set(path + ".entityType", spawner.getEntityType() != null ? spawner.getEntityType().name() : null);
               if (spawner.isItemSpawner()) {
                  this.spawnerData.set(path + ".itemSpawnerMaterial", spawner.getSpawnedItemMaterial().name());
               } else {
                  this.spawnerData.set(path + ".itemSpawnerMaterial", (Object)null);
               }

               String settings = String.format("%d,%b,%d,%b,%d,%d,%d,%d,%d,%d,%d,%d,%b", spawner.getSpawnerExp(), spawner.getSpawnerActive(), spawner.getSpawnerRange(), spawner.getSpawnerStop().get(), spawner.getSpawnDelay(), spawner.getMaxSpawnerLootSlots(), spawner.getMaxStoredExp(), spawner.getMinMobs(), spawner.getMaxMobs(), spawner.getStackSize(), spawner.getMaxStackSize(), spawner.getLastSpawnTime(), spawner.getIsAtCapacity());
               this.spawnerData.set(path + ".settings", settings);
               this.spawnerData.set(path + ".lastInteractedPlayer", spawner.getLastInteractedPlayer());
               this.spawnerData.set(path + ".preferredSortItem", spawner.getPreferredSortItem() != null ? spawner.getPreferredSortItem().name() : null);
               Set<Material> filteredItems = spawner.getFilteredItems();
               if (filteredItems != null && !filteredItems.isEmpty()) {
                  List<String> materials = (List)filteredItems.stream().map(Enum::name).collect(Collectors.toList());
                  this.spawnerData.set(path + ".filteredItems", String.join(",", materials));
               } else {
                  this.spawnerData.set(path + ".filteredItems", (Object)null);
               }

               VirtualInventory virtualInv = spawner.getVirtualInventory();
               if (virtualInv != null) {
                  Map<VirtualInventory.ItemSignature, Long> items = virtualInv.getConsolidatedItems();
                  List<String> serializedItems = ItemStackSerializer.serializeInventory(items);
                  this.spawnerData.set(path + ".inventory", serializedItems);
               }
            }

            this.spawnerData.save(this.spawnerDataFile);
            return true;
         } catch (IOException var14) {
            this.plugin.getLogger().severe("Could not save spawner batch to file!");
            var14.printStackTrace();
            return false;
         }
      }
   }

   public Map<String, SpawnerData> loadAllSpawnersRaw() {
      Map<String, SpawnerData> loadedSpawners = new HashMap();
      ConfigurationSection spawnersSection = this.spawnerData.getConfigurationSection("spawners");
      if (spawnersSection == null) {
         return loadedSpawners;
      } else {
         Iterator var3 = spawnersSection.getKeys(false).iterator();

         while(var3.hasNext()) {
            String spawnerId = (String)var3.next();

            try {
               SpawnerData spawner = this.loadSpawnerFromConfig(spawnerId, false, false);
               loadedSpawners.put(spawnerId, spawner);
            } catch (Exception var6) {
               this.plugin.debug("Error loading spawner " + spawnerId + ": " + var6.getMessage());
               loadedSpawners.put(spawnerId, (Object)null);
            }
         }

         return loadedSpawners;
      }
   }

   public SpawnerData loadSpecificSpawner(String spawnerId) {
      try {
         return this.loadSpawnerFromConfig(spawnerId, false);
      } catch (Exception var3) {
         this.plugin.debug("Error loading spawner " + spawnerId + ": " + var3.getMessage());
         return null;
      }
   }

   public String getRawLocationString(String spawnerId) {
      String path = "spawners." + spawnerId + ".location";
      return this.spawnerData.getString(path);
   }

   private SpawnerData loadSpawnerFromConfig(String spawnerId) {
      return this.loadSpawnerFromConfig(spawnerId, true, true);
   }

   private SpawnerData loadSpawnerFromConfig(String spawnerId, boolean logErrors) {
      return this.loadSpawnerFromConfig(spawnerId, logErrors, true);
   }

   private SpawnerData loadSpawnerFromConfig(String spawnerId, boolean logErrors, boolean restartHopper) {
      String path = "spawners." + spawnerId;
      String locationString = this.spawnerData.getString(path + ".location");
      if (locationString == null) {
         if (logErrors) {
            this.logger.severe("Invalid location for spawner " + spawnerId);
         }

         return null;
      } else {
         String[] locParts = locationString.split(",");
         if (locParts.length != 4) {
            if (logErrors) {
               this.logger.severe("Invalid location format for spawner " + spawnerId);
            }

            return null;
         } else {
            World world = Bukkit.getWorld(locParts[0]);
            if (world == null) {
               if (logErrors) {
                  this.logger.severe("World not found for spawner " + spawnerId + ": " + locParts[0]);
               } else {
                  this.plugin.debug("World not yet loaded for spawner " + spawnerId + ": " + locParts[0]);
               }

               return null;
            } else {
               Location location = new Location(world, (double)Integer.parseInt(locParts[1]), (double)Integer.parseInt(locParts[2]), (double)Integer.parseInt(locParts[3]));
               String entityTypeString = this.spawnerData.getString(path + ".entityType");
               if (entityTypeString == null) {
                  if (logErrors) {
                     this.logger.severe("Missing entity type for spawner " + spawnerId);
                  }

                  return null;
               } else {
                  EntityType entityType;
                  try {
                     entityType = EntityType.valueOf(entityTypeString);
                  } catch (IllegalArgumentException var27) {
                     if (logErrors) {
                        this.logger.severe("Invalid entity type for spawner " + spawnerId + ": " + entityTypeString);
                     }

                     return null;
                  }

                  SpawnerData spawner;
                  String itemSpawnerMaterialString;
                  if (entityType == EntityType.ITEM) {
                     itemSpawnerMaterialString = this.spawnerData.getString(path + ".itemSpawnerMaterial");
                     if (itemSpawnerMaterialString != null) {
                        try {
                           Material itemMaterial = Material.valueOf(itemSpawnerMaterialString);
                           spawner = new SpawnerData(spawnerId, location, itemMaterial, this.plugin);
                        } catch (IllegalArgumentException var26) {
                           if (logErrors) {
                              this.logger.severe("Invalid item spawner material for spawner " + spawnerId + ": " + itemSpawnerMaterialString);
                           }

                           return null;
                        }
                     } else {
                        spawner = new SpawnerData(spawnerId, location, entityType, this.plugin);
                     }
                  } else {
                     spawner = new SpawnerData(spawnerId, location, entityType, this.plugin);
                  }

                  itemSpawnerMaterialString = this.spawnerData.getString(path + ".settings");
                  if (itemSpawnerMaterialString != null) {
                     String[] settings = itemSpawnerMaterialString.split(",");
                     int version = this.spawnerData.getInt("data_version", 1);

                     try {
                        if (version >= 3) {
                           if (settings.length >= 13) {
                              spawner.setSpawnerExpData(Integer.parseInt(settings[0]));
                              spawner.setSpawnerActive(Boolean.parseBoolean(settings[1]));
                              spawner.setSpawnerRange(Integer.parseInt(settings[2]));
                              spawner.getSpawnerStop().set(Boolean.parseBoolean(settings[3]));
                              spawner.setSpawnDelayFromConfig();
                              spawner.setMaxSpawnerLootSlots(Integer.parseInt(settings[5]));
                              spawner.setMaxStoredExp(Integer.parseInt(settings[6]));
                              spawner.setMinMobs(Integer.parseInt(settings[7]));
                              spawner.setMaxMobs(Integer.parseInt(settings[8]));
                              spawner.setStackSize(Integer.parseInt(settings[9]), restartHopper);
                              spawner.setMaxStackSize(Integer.parseInt(settings[10]));
                              spawner.setLastSpawnTime(Long.parseLong(settings[11]));
                              spawner.setIsAtCapacity(Boolean.parseBoolean(settings[12]));
                           }
                        } else {
                           spawner.setSpawnerExpData(Integer.parseInt(settings[0]));
                           spawner.setSpawnerActive(Boolean.parseBoolean(settings[1]));
                           spawner.setSpawnerRange(Integer.parseInt(settings[2]));
                           spawner.getSpawnerStop().set(Boolean.parseBoolean(settings[3]));
                           spawner.setSpawnDelayFromConfig();
                           spawner.setMaxSpawnerLootSlots(Integer.parseInt(settings[5]));
                           spawner.setMaxStoredExp(Integer.parseInt(settings[6]));
                           spawner.setMinMobs(Integer.parseInt(settings[7]));
                           spawner.setMaxMobs(Integer.parseInt(settings[8]));
                           spawner.setStackSize(Integer.parseInt(settings[9]), restartHopper);
                           spawner.setLastSpawnTime(Long.parseLong(settings[10]));
                           spawner.setIsAtCapacity(false);
                        }
                     } catch (NumberFormatException var25) {
                        this.logger.severe("Invalid settings format for spawner " + spawnerId);
                        this.logger.severe("Settings: " + itemSpawnerMaterialString);
                        var25.printStackTrace();
                        return null;
                     }
                  }

                  String filteredItemsStr = this.spawnerData.getString(path + ".filteredItems");
                  if (filteredItemsStr != null && !filteredItemsStr.isEmpty()) {
                     String[] materialNames = filteredItemsStr.split(",");
                     String[] var15 = materialNames;
                     int var16 = materialNames.length;

                     for(int var17 = 0; var17 < var16; ++var17) {
                        String materialName = var15[var17];

                        try {
                           Material material = Material.valueOf(materialName.trim());
                           spawner.getFilteredItems().add(material);
                        } catch (IllegalArgumentException var24) {
                           this.logger.warning("Invalid material in filtered items for spawner " + spawnerId + ": " + materialName);
                        }
                     }
                  }

                  List<String> inventoryData = this.spawnerData.getStringList(path + ".inventory");
                  VirtualInventory virtualInv = new VirtualInventory(spawner.getMaxSpawnerLootSlots());
                  if (inventoryData != null && !inventoryData.isEmpty()) {
                     try {
                        Map<ItemStack, Integer> items = ItemStackSerializer.deserializeInventory(inventoryData);
                        Iterator var36 = items.entrySet().iterator();

                        label132:
                        while(true) {
                           int amount;
                           ItemStack item;
                           do {
                              do {
                                 if (!var36.hasNext()) {
                                    break label132;
                                 }

                                 Entry<ItemStack, Integer> entry = (Entry)var36.next();
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
                     } catch (Exception var28) {
                        this.logger.warning("Error loading inventory for spawner " + spawnerId);
                        var28.printStackTrace();
                     }
                  }

                  spawner.setVirtualInventory(virtualInv);
                  spawner.recalculateSellValue();
                  String lastInteractedPlayer = this.spawnerData.getString(path + ".lastInteractedPlayer");
                  spawner.setLastInteractedPlayer(lastInteractedPlayer);
                  String preferredSortItemStr = this.spawnerData.getString(path + ".preferredSortItem");
                  if (preferredSortItemStr != null && !preferredSortItemStr.isEmpty()) {
                     try {
                        Material preferredSortItem = Material.valueOf(preferredSortItemStr);
                        spawner.setPreferredSortItem(preferredSortItem);
                        virtualInv.sortItems(preferredSortItem);
                     } catch (IllegalArgumentException var23) {
                        this.logger.warning("Invalid preferred sort item for spawner " + spawnerId + ": " + preferredSortItemStr);
                     }
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
         }
      }
   }

   public void queueSpawnerForSaving(String spawnerId) {
      this.markSpawnerModified(spawnerId);
   }

   public void shutdown() {
      if (this.saveTask != null) {
         this.saveTask.cancel();
         this.saveTask = null;
      }

      if (!this.dirtySpawners.isEmpty() || !this.deletedSpawners.isEmpty()) {
         try {
            this.isSaving = true;
            String path;
            if (!this.dirtySpawners.isEmpty()) {
               Map<String, SpawnerData> batch = new HashMap();
               Iterator var2 = this.dirtySpawners.iterator();

               while(var2.hasNext()) {
                  path = (String)var2.next();
                  SpawnerData spawner = this.plugin.getSpawnerManager().getSpawnerById(path);
                  if (spawner != null) {
                     batch.put(path, spawner);
                  }
               }

               if (!batch.isEmpty()) {
                  this.saveSpawnerBatch(batch);
               }
            }

            if (!this.deletedSpawners.isEmpty()) {
               Iterator var10 = this.deletedSpawners.iterator();

               while(var10.hasNext()) {
                  String id = (String)var10.next();
                  path = "spawners." + id;
                  this.spawnerData.set(path, (Object)null);
               }

               this.spawnerData.save(this.spawnerDataFile);
            }

            this.dirtySpawners.clear();
            this.deletedSpawners.clear();
         } catch (Exception var8) {
            this.logger.severe("Error during shutdown flush: " + var8.getMessage());
            var8.printStackTrace();
         } finally {
            this.isSaving = false;
         }
      }

   }
}
