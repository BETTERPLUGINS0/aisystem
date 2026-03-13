package github.nighter.smartspawner.spawner.config;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.ItemPriceManager;
import github.nighter.smartspawner.spawner.lootgen.loot.EntityLootConfig;
import github.nighter.smartspawner.spawner.lootgen.loot.LootItem;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionType;

public class ItemSpawnerSettingsConfig {
   private final SmartSpawner plugin;
   private FileConfiguration config;
   private final File configFile;
   private Material defaultMaterial;
   private final Map<Material, ItemSpawnerSettingsConfig.ItemHeadData> itemHeadMap = new EnumMap(Material.class);
   private final Set<Material> validItemSpawnerMaterials = new HashSet();
   private final Map<Material, EntityLootConfig> itemLootConfigs = new ConcurrentHashMap();

   public ItemSpawnerSettingsConfig(SmartSpawner plugin) {
      this.plugin = plugin;
      this.configFile = new File(plugin.getDataFolder(), "item_spawners_settings.yml");
   }

   public void load() {
      if (!this.configFile.exists()) {
         this.saveDefaultConfig();
      }

      this.config = YamlConfiguration.loadConfiguration(this.configFile);
      this.parseConfig();
   }

   private void saveDefaultConfig() {
      try {
         InputStream inputStream = this.plugin.getResource("item_spawners_settings.yml");
         if (inputStream == null) {
            this.plugin.debug("Could not find item_spawners_settings.yml in plugin resources");
            return;
         }

         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

         try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.configFile), StandardCharsets.UTF_8));

            String line;
            try {
               while((line = reader.readLine()) != null) {
                  writer.write(line);
                  writer.newLine();
               }
            } catch (Throwable var8) {
               try {
                  writer.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            writer.close();
         } catch (Throwable var9) {
            try {
               reader.close();
            } catch (Throwable var6) {
               var9.addSuppressed(var6);
            }

            throw var9;
         }

         reader.close();
         this.plugin.debug("Created default item_spawners_settings.yml configuration");
      } catch (IOException var10) {
         this.plugin.getLogger().severe("Failed to create default item_spawners_settings.yml: " + var10.getMessage());
      }

   }

   private void parseConfig() {
      this.itemHeadMap.clear();
      this.validItemSpawnerMaterials.clear();
      this.itemLootConfigs.clear();
      String defaultMaterialName = this.config.getString("default_material", "SPAWNER");

      try {
         this.defaultMaterial = Material.valueOf(defaultMaterialName.toUpperCase());
      } catch (IllegalArgumentException var7) {
         this.plugin.getLogger().warning("Invalid default_material in item_spawners_settings.yml: " + defaultMaterialName + ", using SPAWNER");
         this.defaultMaterial = Material.SPAWNER;
      }

      Iterator var2 = this.config.getKeys(false).iterator();

      while(true) {
         while(true) {
            String materialName;
            Material material;
            ConfigurationSection itemSection;
            do {
               while(true) {
                  do {
                     if (!var2.hasNext()) {
                        this.plugin.getLogger().info("Loaded " + this.validItemSpawnerMaterials.size() + " item spawner configurations");
                        return;
                     }

                     materialName = (String)var2.next();
                  } while(materialName.equals("default_material"));

                  try {
                     material = Material.valueOf(materialName.toUpperCase());
                     break;
                  } catch (IllegalArgumentException var8) {
                     this.plugin.getLogger().warning("Material '" + materialName + "' is invalid or not available in server version " + this.plugin.getServer().getBukkitVersion());
                  }
               }

               itemSection = this.config.getConfigurationSection(materialName);
            } while(itemSection == null);

            String configMaterial = itemSection.getString("material");
            if (configMaterial != null && configMaterial.equalsIgnoreCase(materialName)) {
               this.parseHeadTexture(material, itemSection);
               this.parseLootData(material, itemSection);
               this.validItemSpawnerMaterials.add(material);
            } else {
               this.plugin.getLogger().warning("Material mismatch for " + materialName + " in item_spawners_settings.yml");
            }
         }
      }
   }

   private void parseLootData(Material material, ConfigurationSection itemSection) {
      int experience = itemSection.getInt("experience", 0);
      List<LootItem> items = new ArrayList();
      ItemPriceManager priceManager = this.plugin.getItemPriceManager();
      ConfigurationSection lootSection = itemSection.getConfigurationSection("loot");
      if (lootSection != null) {
         Iterator var7 = lootSection.getKeys(false).iterator();

         label42:
         while(true) {
            String itemKey;
            ConfigurationSection lootItemSection;
            do {
               if (!var7.hasNext()) {
                  break label42;
               }

               itemKey = (String)var7.next();
               lootItemSection = lootSection.getConfigurationSection(itemKey);
            } while(lootItemSection == null);

            try {
               Material lootMaterial;
               try {
                  lootMaterial = Material.valueOf(itemKey.toUpperCase());
               } catch (IllegalArgumentException var22) {
                  this.plugin.getLogger().warning("Material '" + itemKey + "' is not available in server version " + this.plugin.getServer().getBukkitVersion() + " - skipping for item spawner " + material.name());
                  continue;
               }

               String[] amounts = lootItemSection.getString("amount", "1-1").split("-");
               int minAmount = Integer.parseInt(amounts[0]);
               int maxAmount = Integer.parseInt(amounts.length > 1 ? amounts[1] : amounts[0]);
               double chance = lootItemSection.getDouble("chance", 100.0D);
               double sellPrice = 0.0D;
               if (priceManager != null) {
                  sellPrice = priceManager.getPrice(lootMaterial);
               }

               Integer minDurability = null;
               Integer maxDurability = null;
               PotionType potionType = null;
               LootItem lootItem = new LootItem(lootMaterial, minAmount, maxAmount, chance, (Integer)minDurability, (Integer)maxDurability, (PotionType)potionType, sellPrice);
               items.add(lootItem);
            } catch (Exception var23) {
               this.plugin.getLogger().warning("Error parsing loot item " + itemKey + " for item spawner " + material.name() + ": " + var23.getMessage());
            }
         }
      }

      EntityLootConfig lootConfig = new EntityLootConfig(experience, items);
      this.itemLootConfigs.put(material, lootConfig);
   }

   private void parseHeadTexture(Material material, ConfigurationSection itemSection) {
      ConfigurationSection headSection = itemSection.getConfigurationSection("head_texture");
      if (headSection != null) {
         String headMaterialName = headSection.getString("material", material.name());
         String customTexture = headSection.getString("custom_texture");

         Material headMaterial;
         try {
            headMaterial = Material.valueOf(headMaterialName.toUpperCase());
            if (!headMaterial.isItem()) {
               this.plugin.getLogger().warning("Material " + headMaterialName + " for " + String.valueOf(material) + " is not an item, using the item itself");
               headMaterial = material;
            }
         } catch (IllegalArgumentException var8) {
            this.plugin.getLogger().warning("Invalid head material " + headMaterialName + " for " + String.valueOf(material) + ", using the item itself");
            headMaterial = material;
         }

         this.itemHeadMap.put(material, new ItemSpawnerSettingsConfig.ItemHeadData(headMaterial, customTexture));
      }
   }

   public ItemSpawnerSettingsConfig.ItemHeadData getHeadData(Material material) {
      return (ItemSpawnerSettingsConfig.ItemHeadData)this.itemHeadMap.getOrDefault(material, new ItemSpawnerSettingsConfig.ItemHeadData(this.defaultMaterial, (String)null));
   }

   public EntityLootConfig getLootConfig(Material material) {
      return (EntityLootConfig)this.itemLootConfigs.get(material);
   }

   public boolean isValidItemSpawner(Material material) {
      return this.validItemSpawnerMaterials.contains(material);
   }

   public Set<Material> getValidItemSpawnerMaterials() {
      return Collections.unmodifiableSet(this.validItemSpawnerMaterials);
   }

   public void reload() {
      this.load();
   }

   public static class ItemHeadData {
      private final Material material;
      private final String customTexture;

      public ItemHeadData(Material material, String customTexture) {
         this.material = material;
         this.customTexture = customTexture;
      }

      public Material getMaterial() {
         return this.material;
      }

      public String getCustomTexture() {
         return this.customTexture;
      }

      public boolean hasCustomTexture() {
         return this.customTexture != null && !this.customTexture.isEmpty() && !this.customTexture.equalsIgnoreCase("null");
      }
   }
}
