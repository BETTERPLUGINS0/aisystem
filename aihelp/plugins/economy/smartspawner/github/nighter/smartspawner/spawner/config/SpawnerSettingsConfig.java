package github.nighter.smartspawner.spawner.config;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.ItemPriceManager;
import github.nighter.smartspawner.spawner.lootgen.loot.EntityLootConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import github.nighter.smartspawner.updates.Version;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionType;

public class SpawnerSettingsConfig {
   private final SmartSpawner plugin;
   private FileConfiguration config;
   private final File configFile;
   private static final String CONFIG_VERSION_KEY = "config_version";
   private final String CURRENT_CONFIG_VERSION;
   private Material defaultMaterial;
   private final Map<EntityType, SpawnerSettingsConfig.MobHeadData> mobHeadMap = new EnumMap(EntityType.class);
   private final Map<String, EntityLootConfig> entityLootConfigs = new ConcurrentHashMap();
   private final Set<Material> loadedMaterials = new HashSet();

   public SpawnerSettingsConfig(SmartSpawner plugin) {
      this.plugin = plugin;
      this.configFile = new File(plugin.getDataFolder(), "spawners_settings.yml");
      this.CURRENT_CONFIG_VERSION = plugin.getDescription().getVersion();
   }

   public void load() {
      if (!this.configFile.exists()) {
         this.saveDefaultConfig();
      }

      this.checkAndUpdateConfig();
      this.config = YamlConfiguration.loadConfiguration(this.configFile);
      this.parseConfig();
   }

   private void checkAndUpdateConfig() {
      if (this.configFile.exists()) {
         FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(this.configFile);
         String configVersionStr = currentConfig.getString("config_version", "0.0.0");
         Version configVersion = new Version(configVersionStr);
         Version currentConfigVersion = new Version(this.CURRENT_CONFIG_VERSION);
         if (configVersion.compareTo(currentConfigVersion) < 0) {
            this.plugin.getLogger().info("Updating spawners_settings.yml from version " + configVersionStr + " to " + this.CURRENT_CONFIG_VERSION);

            try {
               Map<String, Object> userValues = this.flattenConfig(currentConfig);
               File tempFile = new File(this.plugin.getDataFolder(), "spawners_settings_new.yml");
               this.createDefaultConfigWithHeader(tempFile);
               FileConfiguration newConfig = YamlConfiguration.loadConfiguration(tempFile);
               newConfig.set("config_version", this.CURRENT_CONFIG_VERSION);
               boolean configDiffers = this.hasConfigDifferences(userValues, newConfig);
               if (configDiffers) {
                  File backupFile = new File(this.plugin.getDataFolder(), "spawners_settings_backup_" + configVersionStr + ".yml");
                  Files.copy(this.configFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                  this.plugin.getLogger().info("Config backup created at " + backupFile.getName());
               } else {
                  this.plugin.debug("No significant config changes detected, skipping backup creation");
               }

               this.applyUserValues(newConfig, userValues);
               newConfig.save(this.configFile);
               tempFile.delete();
            } catch (Exception var10) {
               this.plugin.getLogger().log(Level.SEVERE, "Failed to update spawners_settings.yml", var10);
            }

         }
      }
   }

   private void createDefaultConfigWithHeader(File destinationFile) {
      try {
         File parentDir = destinationFile.getParentFile();
         if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
         }

         InputStream in = this.plugin.getResource("spawners_settings.yml");

         try {
            if (in != null) {
               List<String> defaultLines = (new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))).lines().toList();
               List<String> newLines = new ArrayList();
               newLines.add("# Configuration version - Do not modify this value");
               newLines.add("config_version: " + this.CURRENT_CONFIG_VERSION);
               newLines.add("");
               newLines.addAll(defaultLines);
               Files.write(destinationFile.toPath(), newLines, StandardCharsets.UTF_8);
            } else {
               this.plugin.getLogger().warning("Default spawners_settings.yml not found in the plugin's resources.");
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
         this.plugin.getLogger().severe("Failed to create default spawners_settings.yml with header: " + var8.getMessage());
         var8.printStackTrace();
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
         if (!path.equals("config_version")) {
            if (newConfig.contains(path)) {
               newConfig.set(path, value);
            } else {
               this.plugin.getLogger().warning("Config path '" + path + "' from old config no longer exists in new config");
            }
         }
      }

   }

   private void saveDefaultConfig() {
      if (!this.configFile.exists()) {
         this.createDefaultConfigWithHeader(this.configFile);
         this.plugin.getLogger().info("Created default spawners_settings.yml configuration");
      }

   }

   private void parseConfig() {
      this.mobHeadMap.clear();
      this.entityLootConfigs.clear();
      this.loadedMaterials.clear();
      String defaultMaterialName = this.config.getString("default_material", "SPAWNER");

      try {
         this.defaultMaterial = Material.valueOf(defaultMaterialName.toUpperCase());
      } catch (IllegalArgumentException var6) {
         this.plugin.getLogger().warning("Invalid default_material in spawners_settings.yml: " + defaultMaterialName + ", using SPAWNER");
         this.defaultMaterial = Material.SPAWNER;
      }

      Iterator var2 = this.config.getKeys(false).iterator();

      while(true) {
         String entityName;
         do {
            do {
               if (!var2.hasNext()) {
                  return;
               }

               entityName = (String)var2.next();
            } while(entityName.equals("config_version"));
         } while(entityName.equals("default_material"));

         EntityType entityType;
         try {
            entityType = EntityType.valueOf(entityName.toUpperCase());
         } catch (IllegalArgumentException var7) {
            this.plugin.getLogger().warning("Entity type '" + entityName + "' is invalid or not available in server version " + this.plugin.getServer().getBukkitVersion());
            continue;
         }

         ConfigurationSection entitySection = this.config.getConfigurationSection(entityName);
         if (entitySection != null) {
            this.parseHeadTexture(entityType, entitySection);
            this.parseLootData(entityName, entitySection);
         }
      }
   }

   private void parseHeadTexture(EntityType entityType, ConfigurationSection entitySection) {
      ConfigurationSection headSection = entitySection.getConfigurationSection("head_texture");
      if (headSection != null) {
         String materialName = headSection.getString("material", "SPAWNER");
         String customTexture = headSection.getString("custom_texture");

         Material material;
         try {
            material = Material.valueOf(materialName.toUpperCase());
            if (!material.isItem()) {
               this.plugin.getLogger().warning("Material " + materialName + " for " + String.valueOf(entityType) + " is not an item, using default");
               material = this.defaultMaterial;
            }
         } catch (IllegalArgumentException var8) {
            this.plugin.getLogger().warning("Invalid material " + materialName + " for " + String.valueOf(entityType) + ", using default");
            material = this.defaultMaterial;
         }

         this.mobHeadMap.put(entityType, new SpawnerSettingsConfig.MobHeadData(material, customTexture));
      }
   }

   private void parseLootData(String entityName, ConfigurationSection entitySection) {
      int experience = entitySection.getInt("experience", 0);
      List<LootItem> items = new ArrayList();
      ItemPriceManager priceManager = this.plugin.getItemPriceManager();
      ConfigurationSection lootSection = entitySection.getConfigurationSection("loot");
      if (lootSection != null) {
         Iterator var7 = lootSection.getKeys(false).iterator();

         label69:
         while(true) {
            String itemKey;
            ConfigurationSection itemSection;
            do {
               if (!var7.hasNext()) {
                  break label69;
               }

               itemKey = (String)var7.next();
               itemSection = lootSection.getConfigurationSection(itemKey);
            } while(itemSection == null);

            try {
               Material material;
               try {
                  material = Material.valueOf(itemKey.toUpperCase());
               } catch (IllegalArgumentException var23) {
                  material = null;
               }

               if (material == null) {
                  this.plugin.getLogger().warning("Material '" + itemKey + "' is not available in server version " + this.plugin.getServer().getBukkitVersion() + " - skipping for entity " + entityName);
               } else {
                  this.loadedMaterials.add(material);
                  String[] amounts = itemSection.getString("amount", "1-1").split("-");
                  int minAmount = Integer.parseInt(amounts[0]);
                  int maxAmount = Integer.parseInt(amounts.length > 1 ? amounts[1] : amounts[0]);
                  double chance = itemSection.getDouble("chance", 100.0D);
                  double sellPrice = 0.0D;
                  if (priceManager != null) {
                     sellPrice = priceManager.getPrice(material);
                  }

                  Integer minDurability = null;
                  Integer maxDurability = null;
                  if (itemSection.contains("durability")) {
                     String[] durabilities = itemSection.getString("durability").split("-");
                     minDurability = Integer.parseInt(durabilities[0]);
                     maxDurability = Integer.parseInt(durabilities.length > 1 ? durabilities[1] : durabilities[0]);
                  }

                  PotionType potionType = null;
                  if (material == Material.TIPPED_ARROW && itemSection.contains("potion_type")) {
                     String potionTypeName = itemSection.getString("potion_type");
                     if (potionTypeName != null) {
                        try {
                           potionType = PotionType.valueOf(potionTypeName.toUpperCase());
                        } catch (IllegalArgumentException var24) {
                           this.plugin.getLogger().warning("Invalid potion type '" + potionTypeName + "' for entity " + entityName);
                           continue;
                        }
                     }
                  }

                  items.add(new LootItem(material, minAmount, maxAmount, chance, minDurability, maxDurability, potionType, sellPrice));
               }
            } catch (Exception var25) {
               this.plugin.getLogger().warning("Error processing material '" + itemKey + "' for entity " + entityName + ": " + var25.getMessage());
            }
         }
      }

      this.entityLootConfigs.put(entityName.toLowerCase(), new EntityLootConfig(experience, items));
   }

   public Material getMaterial(EntityType entityType) {
      SpawnerSettingsConfig.MobHeadData data = (SpawnerSettingsConfig.MobHeadData)this.mobHeadMap.get(entityType);
      return data != null ? data.material : this.defaultMaterial;
   }

   public String getCustomTexture(EntityType entityType) {
      SpawnerSettingsConfig.MobHeadData data = (SpawnerSettingsConfig.MobHeadData)this.mobHeadMap.get(entityType);
      return data != null ? data.customTexture : null;
   }

   public boolean hasCustomTexture(EntityType entityType) {
      SpawnerSettingsConfig.MobHeadData data = (SpawnerSettingsConfig.MobHeadData)this.mobHeadMap.get(entityType);
      return data != null && data.customTexture != null && !data.customTexture.isEmpty();
   }

   public EntityLootConfig getLootConfig(EntityType entityType) {
      return entityType != null && entityType != EntityType.UNKNOWN ? (EntityLootConfig)this.entityLootConfigs.get(entityType.name().toLowerCase()) : null;
   }

   public Set<Material> getLoadedMaterials() {
      return new HashSet(this.loadedMaterials);
   }

   public void reload() {
      this.load();
   }

   private static class MobHeadData {
      final Material material;
      final String customTexture;

      MobHeadData(Material material, String customTexture) {
         this.material = material;
         this.customTexture = customTexture;
      }
   }
}
