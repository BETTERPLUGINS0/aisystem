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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;
import lombok.Generated;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageUpdater {
   private final String currentVersion;
   private final SmartSpawner plugin;
   private static final String LANGUAGE_VERSION_KEY = "language_version";
   private static final List<String> SUPPORTED_LANGUAGES = Arrays.asList("en_US", "vi_VN", "de_DE");
   private final Set<LanguageUpdater.LanguageFileType> activeFileTypes;

   public LanguageUpdater(SmartSpawner plugin) {
      this(plugin, LanguageUpdater.LanguageFileType.values());
   }

   public LanguageUpdater(SmartSpawner plugin, LanguageUpdater.LanguageFileType... fileTypes) {
      this.activeFileTypes = new HashSet();
      this.plugin = plugin;
      this.currentVersion = plugin.getDescription().getVersion();
      this.activeFileTypes.addAll(Arrays.asList(fileTypes));
      this.checkAndUpdateLanguageFiles();
   }

   public void checkAndUpdateLanguageFiles() {
      Iterator var1 = SUPPORTED_LANGUAGES.iterator();

      while(var1.hasNext()) {
         String language = (String)var1.next();
         File langDir = new File(this.plugin.getDataFolder(), "language/" + language);
         if (!langDir.exists()) {
            langDir.mkdirs();
         }

         Iterator var4 = this.activeFileTypes.iterator();

         while(var4.hasNext()) {
            LanguageUpdater.LanguageFileType fileType = (LanguageUpdater.LanguageFileType)var4.next();
            File languageFile = new File(langDir, fileType.getFileName());
            this.updateLanguageFile(language, languageFile, fileType);
         }
      }

   }

   private void updateLanguageFile(String language, File languageFile, LanguageUpdater.LanguageFileType fileType) {
      try {
         if (!languageFile.getParentFile().exists()) {
            languageFile.getParentFile().mkdirs();
         }

         if (!languageFile.exists()) {
            this.createDefaultLanguageFileWithHeader(language, languageFile, fileType);
            Logger var10000 = this.plugin.getLogger();
            String var10001 = fileType.getFileName();
            var10000.info("Created new " + var10001 + " for " + language);
            return;
         }

         FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(languageFile);
         String configVersionStr = currentConfig.getString("language_version", "0.0.0");
         Version configVersion = new Version(configVersionStr);
         Version pluginVersion = new Version(this.currentVersion);
         if (configVersion.compareTo(pluginVersion) >= 0) {
            return;
         }

         if (!configVersionStr.equals("0.0.0")) {
            this.plugin.debug("Updating " + language + " " + fileType.getFileName() + " from version " + configVersionStr + " to " + this.currentVersion);
         }

         Map<String, Object> userValues = this.flattenConfig(currentConfig);
         File tempFile = new File(this.plugin.getDataFolder(), "language/" + language + "/" + fileType.getFileName().replace(".yml", "_new.yml"));
         this.createDefaultLanguageFileWithHeader(language, tempFile, fileType);
         FileConfiguration newConfig = YamlConfiguration.loadConfiguration(tempFile);
         newConfig.set("language_version", this.currentVersion);
         boolean configDiffers = this.hasConfigDifferences(userValues, newConfig);
         if (configDiffers) {
            File backupFile = new File(this.plugin.getDataFolder(), "language/" + language + "/" + fileType.getFileName().replace(".yml", "_backup_" + configVersionStr + ".yml"));
            Files.copy(languageFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            this.plugin.debug(language + " " + fileType.getFileName() + " backup created at " + backupFile.getName());
         } else if (!configVersionStr.equals("0.0.0")) {
            this.plugin.debug("No significant changes detected in " + language + " " + fileType.getFileName() + ", skipping backup creation");
         }

         this.applyUserValues(newConfig, userValues);
         newConfig.save(languageFile);
         tempFile.delete();
      } catch (Exception var13) {
         this.plugin.getLogger().severe("Failed to update " + language + " " + fileType.getFileName() + ": " + var13.getMessage());
         var13.printStackTrace();
      }

   }

   private void createDefaultLanguageFileWithHeader(String language, File destinationFile, LanguageUpdater.LanguageFileType fileType) {
      Logger var10000;
      String var10001;
      try {
         InputStream in = this.plugin.getResource("language/" + language + "/" + fileType.getFileName());

         try {
            if (in != null) {
               List<String> defaultLines = (new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))).lines().toList();
               List<String> newLines = new ArrayList();
               newLines.add("# Language file version - Do not modify this value");
               newLines.add("language_version: " + this.currentVersion);
               newLines.add("");
               newLines.addAll(defaultLines);
               destinationFile.getParentFile().mkdirs();
               Files.write(destinationFile.toPath(), newLines, StandardCharsets.UTF_8);
            } else {
               var10000 = this.plugin.getLogger();
               var10001 = fileType.getFileName();
               var10000.warning("Default " + var10001 + " for " + language + " not found in the plugin's resources.");
               destinationFile.getParentFile().mkdirs();
               YamlConfiguration emptyConfig = new YamlConfiguration();
               emptyConfig.set("language_version", this.currentVersion);
               emptyConfig.set("_note", "This is an empty " + fileType.getFileName() + " created because no default was found in the plugin resources.");
               emptyConfig.save(destinationFile);
            }
         } catch (Throwable var8) {
            if (in != null) {
               try {
                  in.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (in != null) {
            in.close();
         }
      } catch (IOException var9) {
         var10000 = this.plugin.getLogger();
         var10001 = fileType.getFileName();
         var10000.severe("Failed to create default language file " + var10001 + " for " + language + ": " + var9.getMessage());
         var9.printStackTrace();
      }

   }

   private boolean hasConfigDifferences(Map<String, Object> userValues, FileConfiguration newConfig) {
      Map<String, Object> newConfigMap = this.flattenConfig(newConfig);
      Iterator var4 = userValues.entrySet().iterator();

      while(var4.hasNext()) {
         Entry<String, Object> entry = (Entry)var4.next();
         String path = (String)entry.getKey();
         Object oldValue = entry.getValue();
         if (!path.equals("language_version")) {
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
      } while(path.equals("language_version") || userValues.containsKey(path));

      return true;
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
      Iterator var3 = userValues.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, Object> entry = (Entry)var3.next();
         String path = (String)entry.getKey();
         Object value = entry.getValue();
         if (!path.equals("language_version")) {
            if (newConfig.contains(path)) {
               newConfig.set(path, value);
            } else {
               this.plugin.getLogger().fine("Config path '" + path + "' from old config no longer exists in new config");
            }
         }
      }

   }

   public static enum LanguageFileType {
      MESSAGES("messages.yml"),
      GUI("gui.yml"),
      FORMATTING("formatting.yml"),
      ITEMS("items.yml");

      private final String fileName;

      private LanguageFileType(String param3) {
         this.fileName = fileName;
      }

      @Generated
      public String getFileName() {
         return this.fileName;
      }

      // $FF: synthetic method
      private static LanguageUpdater.LanguageFileType[] $values() {
         return new LanguageUpdater.LanguageFileType[]{MESSAGES, GUI, FORMATTING, ITEMS};
      }
   }
}
