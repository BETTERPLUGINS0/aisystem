package github.nighter.smartspawner.migration;

import github.nighter.smartspawner.SmartSpawner;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpawnerDataMigration {
   private final SmartSpawner plugin;
   private final File dataFolder;
   private static final String DATA_FILE = "spawners_data.yml";
   private static final String BACKUP_FILE = "spawners_data_backup.yml";
   private static final String MIGRATION_FLAG = "data_version";
   private static final int DEFAULT_MAX_STACK_SIZE = 10;
   private static final int VERSION_3_SETTINGS_FIELD_COUNT = 13;
   private static final int VERSION_2_MIN_SETTINGS_FIELD_COUNT = 11;
   private static final int VERSION_2_MAX_SETTINGS_FIELD_COUNT = 12;
   private static final int VERSION_2_FIELDS_BEFORE_MAX_STACK = 10;
   private final int CURRENT_VERSION;

   public SpawnerDataMigration(SmartSpawner plugin) {
      this.plugin = plugin;
      this.dataFolder = plugin.getDataFolder();
      this.CURRENT_VERSION = plugin.getDATA_VERSION();
   }

   public boolean checkAndMigrateData() {
      File dataFile = new File(this.dataFolder, "spawners_data.yml");
      if (!dataFile.exists()) {
         this.plugin.getLogger().info("Data file does not exist. No migration needed.");
         return false;
      } else {
         YamlConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

         boolean needsMigration;
         try {
            needsMigration = false;
            int dataVersion = config.getInt("data_version", 0);
            if (dataVersion == 0) {
               this.plugin.getLogger().info("No data_version found. Checking data structure...");
               needsMigration = true;
            } else if (dataVersion < this.CURRENT_VERSION) {
               this.plugin.getLogger().info("Data version " + dataVersion + " is outdated. Current version is " + this.CURRENT_VERSION + ".");
               needsMigration = true;
            }

            if (needsMigration && config.contains("spawners")) {
               boolean hasNewFormat = true;
               ConfigurationSection spawnersSection = config.getConfigurationSection("spawners");
               if (spawnersSection != null) {
                  Iterator var7 = spawnersSection.getKeys(false).iterator();

                  while(var7.hasNext()) {
                     String spawnerId = (String)var7.next();
                     String spawnerPath = "spawners." + spawnerId;
                     if (!config.contains(spawnerPath + ".location") || !config.contains(spawnerPath + ".settings") || !config.contains(spawnerPath + ".inventory")) {
                        hasNewFormat = false;
                        break;
                     }

                     String settingsString = config.getString(spawnerPath + ".settings");
                     if (settingsString != null) {
                        String[] settings = settingsString.split(",");
                        if (settings.length < 13) {
                           hasNewFormat = false;
                           break;
                        }
                     }
                  }
               }

               if (hasNewFormat) {
                  this.plugin.getLogger().info("Data structure is already in current format. Updating version flag...");
                  config.set("data_version", this.CURRENT_VERSION);

                  try {
                     config.save(dataFile);
                  } catch (IOException var13) {
                     this.plugin.getLogger().warning("Could not save data_version flag: " + var13.getMessage());
                  }

                  needsMigration = false;
               }
            }

            if (!needsMigration) {
               return false;
            }
         } catch (Exception var14) {
            this.plugin.getLogger().warning("Error validating current data format: " + var14.getMessage());
            needsMigration = true;
         }

         this.plugin.getLogger().info("Starting data migration process...");

         try {
            if (!this.createBackup(dataFile)) {
               this.plugin.getLogger().severe("Failed to create backup. Migration aborted.");
               return false;
            } else {
               boolean success = this.migrateData(config, dataFile);
               if (success) {
                  return true;
               } else {
                  this.restoreFromBackup(dataFile);
                  return false;
               }
            }
         } catch (Exception var12) {
            this.plugin.getLogger().severe("Error during data migration: " + var12.getMessage());
            var12.printStackTrace();
            this.restoreFromBackup(dataFile);
            return false;
         }
      }
   }

   private boolean createBackup(File sourceFile) {
      try {
         File backupFile = new File(this.dataFolder, "spawners_data_backup.yml");
         Files.copy(sourceFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
         this.plugin.getLogger().info("Backup created successfully at: " + backupFile.getPath());
         return true;
      } catch (IOException var3) {
         this.plugin.getLogger().severe("Failed to create backup: " + var3.getMessage());
         var3.printStackTrace();
         return false;
      }
   }

   private void restoreFromBackup(File dataFile) {
      try {
         File backupFile = new File(this.dataFolder, "spawners_data_backup.yml");
         if (backupFile.exists()) {
            Files.copy(backupFile.toPath(), dataFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.plugin.getLogger().info("Data restored from backup.");
         }
      } catch (IOException var3) {
         this.plugin.getLogger().severe("Failed to restore from backup: " + var3.getMessage());
         var3.printStackTrace();
      }

   }

   private boolean migrateData(FileConfiguration oldConfig, File dataFile) {
      try {
         int oldVersion = oldConfig.getInt("data_version", 0);
         if (oldVersion == 2 && oldConfig.contains("spawners")) {
            this.plugin.getLogger().info("Migrating from version 2 to version 3...");
            return this.migrateVersion2ToVersion3(oldConfig, dataFile);
         } else {
            FileConfiguration newConfig = new YamlConfiguration();
            newConfig.set("data_version", this.CURRENT_VERSION);
            SpawnerDataConverter converter = new SpawnerDataConverter(this.plugin, oldConfig, newConfig);
            converter.convertData();
            newConfig.save(dataFile);
            return true;
         }
      } catch (Exception var6) {
         this.plugin.getLogger().severe("Failed to migrate data: " + var6.getMessage());
         var6.printStackTrace();
         return false;
      }
   }

   private boolean migrateVersion2ToVersion3(FileConfiguration config, File dataFile) {
      try {
         ConfigurationSection spawnersSection = config.getConfigurationSection("spawners");
         if (spawnersSection == null) {
            this.plugin.getLogger().warning("No spawners section found in version 2 data");
            return false;
         } else {
            int migratedCount = 0;
            Iterator var5 = spawnersSection.getKeys(false).iterator();

            while(true) {
               String settingsPath;
               String[] settings;
               do {
                  do {
                     String settingsString;
                     do {
                        if (!var5.hasNext()) {
                           config.set("data_version", this.CURRENT_VERSION);
                           config.save(dataFile);
                           this.plugin.getLogger().info("Successfully migrated " + migratedCount + " spawners from version 2 to version 3");
                           return true;
                        }

                        String spawnerId = (String)var5.next();
                        settingsPath = "spawners." + spawnerId + ".settings";
                        settingsString = config.getString(settingsPath);
                     } while(settingsString == null);

                     settings = settingsString.split(",");
                  } while(settings.length < 11);
               } while(settings.length > 12);

               int defaultMaxStackSize = this.plugin.getConfig().getInt("spawner.max_stack_size", 10);
               StringBuilder newSettingsBuilder = new StringBuilder();

               for(int i = 0; i < 10; ++i) {
                  if (i > 0) {
                     newSettingsBuilder.append(",");
                  }

                  newSettingsBuilder.append(settings[i]);
               }

               newSettingsBuilder.append(",").append(defaultMaxStackSize);
               newSettingsBuilder.append(",").append(settings[10]);
               boolean isAtCapacity = settings.length == 12 ? Boolean.parseBoolean(settings[11]) : false;
               newSettingsBuilder.append(",").append(isAtCapacity);
               config.set(settingsPath, newSettingsBuilder.toString());
               ++migratedCount;
            }
         }
      } catch (Exception var13) {
         this.plugin.getLogger().severe("Failed to migrate from version 2 to version 3: " + var13.getMessage());
         var13.printStackTrace();
         return false;
      }
   }
}
