package github.nighter.smartspawner.updates;

import github.nighter.smartspawner.SmartSpawner;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUpdater {
   private final String currentVersion;
   private final SmartSpawner plugin;
   private static final String CONFIG_VERSION_KEY = "config_version";

   public ConfigUpdater(SmartSpawner plugin) {
      this.plugin = plugin;
      this.currentVersion = plugin.getDescription().getVersion();
   }

   public void checkAndUpdateConfig() {
      File configFile = new File(this.plugin.getDataFolder(), "config.yml");
      if (!configFile.exists()) {
         this.createDefaultConfigWithHeader(configFile);
      } else {
         FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(configFile);
         String configVersionStr = currentConfig.getString("config_version", "0.0.0");
         Version configVersion = new Version(configVersionStr);
         Version pluginVersion = new Version(this.currentVersion);
         if (configVersion.compareTo(pluginVersion) < 0) {
            this.plugin.getLogger().info("Updating config from version " + configVersionStr + " to " + this.currentVersion);

            try {
               Map<String, Object> userValues = this.flattenConfig(currentConfig);
               File tempFile = new File(this.plugin.getDataFolder(), "config_new.yml");
               this.createDefaultConfigWithHeader(tempFile);
               FileConfiguration newConfig = YamlConfiguration.loadConfiguration(tempFile);
               newConfig.set("config_version", this.currentVersion);
               boolean configDiffers = this.hasConfigDifferences(userValues, newConfig);
               if (configDiffers) {
                  File backupFile = new File(this.plugin.getDataFolder(), "config_backup_" + configVersionStr + ".yml");
                  Files.copy(configFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                  this.plugin.getLogger().info("Config backup created at " + backupFile.getName());
               } else {
                  this.plugin.debug("No significant config changes detected, skipping backup creation");
               }

               this.applyUserValues(newConfig, userValues);
               newConfig.save(configFile);
               tempFile.delete();
               this.plugin.reloadConfig();
            } catch (Exception var11) {
               this.plugin.getLogger().severe("Failed to update config: " + var11.getMessage());
               var11.printStackTrace();
            }

         }
      }
   }

   private boolean hasConfigDifferences(Map<String, Object> userValues, FileConfiguration newConfig) {
      Map<String, Object> newConfigMap = this.flattenConfig(newConfig);
      Iterator var4 = userValues.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, Object> entry = (Entry)var4.next();
         String path = (String)entry.getKey();
         Object oldValue = entry.getValue();
         if (!path.equals("config_version")) {
            if (!newConfig.contains(path)) {
               return true;
            }

            Object newDefaultValue = newConfig.get(path);
            if (newDefaultValue != null && !newDefaultValue.equals(oldValue)) {
               return true;
            }
         }
      }

      var4 = newConfigMap.keySet().iterator();

      String path;
      do {
         if (!var4.hasNext()) {
            return false;
         }

         path = (String)var4.next();
      } while(path.equals("config_version") || userValues.containsKey(path));

      return true;
   }

   private void createDefaultConfigWithHeader(File destinationFile) {
      try {
         File parentDir = destinationFile.getParentFile();
         if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
         }

         InputStream in = this.plugin.getResource("config.yml");

         try {
            if (in != null) {
               List<String> defaultLines = (new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))).lines().toList();
               List<String> newLines = new ArrayList();
               newLines.add("# Configuration version - Do not modify this value");
               newLines.add("config_version: " + this.currentVersion);
               newLines.add("");
               newLines.addAll(defaultLines);
               Files.write(destinationFile.toPath(), newLines, StandardCharsets.UTF_8);
            } else {
               this.plugin.getLogger().warning("Default config.yml not found in the plugin's resources.");
               destinationFile.createNewFile();
            }
         } catch (Throwable var7) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (in != null) {
            in.close();
         }
      } catch (IOException var8) {
         this.plugin.getLogger().severe("Failed to create default config with header: " + var8.getMessage());
         var8.printStackTrace();
      }

   }

   private Map<String, Object> flattenConfig(ConfigurationSection config) {
      Map<String, Object> result = new HashMap();
      Iterator var3 = config.getKeys(true).iterator();

      while(var3.hasNext()) {
         String key = (String)var3.next();
         if (!config.isConfigurationSection(key)) {
            result.put(key, config.get(key));
         }
      }

      return result;
   }

   private void applyUserValues(FileConfiguration newConfig, Map<String, Object> userValues) {
      this.migrateRenamedPaths(userValues);
      Iterator var3 = userValues.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Object> entry = (Entry)var3.next();
         String path = (String)entry.getKey();
         Object value = entry.getValue();
         if (!path.equals("config_version")) {
            if (newConfig.contains(path)) {
               newConfig.set(path, value);
            } else {
               this.plugin.debug("Config path '" + path + "' from old config no longer exists in new config");
            }
         }
      }

   }

   private void migrateRenamedPaths(Map<String, Object> userValues) {
      Map<String, String> renamedPaths = Map.ofEntries(new Entry[]{Map.entry("database.standalone.host", "database.sql.host"), Map.entry("database.standalone.port", "database.sql.port"), Map.entry("database.standalone.username", "database.sql.username"), Map.entry("database.standalone.password", "database.sql.password"), Map.entry("database.standalone.pool.maximum-size", "database.sql.pool.maximum-size"), Map.entry("database.standalone.pool.minimum-idle", "database.sql.pool.minimum-idle"), Map.entry("database.standalone.pool.connection-timeout", "database.sql.pool.connection-timeout"), Map.entry("database.standalone.pool.max-lifetime", "database.sql.pool.max-lifetime"), Map.entry("database.standalone.pool.idle-timeout", "database.sql.pool.idle-timeout"), Map.entry("database.standalone.pool.keepalive-time", "database.sql.pool.keepalive-time"), Map.entry("database.standalone.pool.leak-detection-threshold", "database.sql.pool.leak-detection-threshold")});
      Iterator var3 = renamedPaths.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, String> rename = (Entry)var3.next();
         String oldPath = (String)rename.getKey();
         String newPath = (String)rename.getValue();
         if (userValues.containsKey(oldPath) && !userValues.containsKey(newPath)) {
            Object value = userValues.remove(oldPath);
            userValues.put(newPath, value);
            this.plugin.debug("Migrated config: " + oldPath + " -> " + newPath);
         }
      }

      if (userValues.containsKey("database.mode")) {
         Object mode = userValues.get("database.mode");
         if ("DATABASE".equals(mode)) {
            userValues.put("database.mode", "MYSQL");
            this.plugin.getLogger().info("Migrated database.mode: DATABASE -> MYSQL");
         }
      }

   }
}
