package github.nighter.smartspawner.hooks.economy;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.currency.CurrencyManager;
import github.nighter.smartspawner.hooks.economy.shops.ShopIntegrationManager;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ItemPriceManager {
   private final SmartSpawner plugin;
   private final Map<String, Double> itemPrices = new ConcurrentHashMap();
   private File priceFile;
   private FileConfiguration priceConfig;
   private ShopIntegrationManager shopIntegrationManager;
   private CurrencyManager currencyManager;
   private double defaultPrice;
   private ItemPriceManager.PriceSourceMode priceSourceMode;
   private boolean economyEnabled;
   private String priceFileName;
   public boolean customPricesEnabled;
   public boolean shopIntegrationEnabled;

   public void init() {
      if (!this.plugin.getDataFolder().exists()) {
         this.plugin.getDataFolder().mkdirs();
      }

      this.loadConfiguration();
      this.priceFile = new File(this.plugin.getDataFolder(), this.priceFileName);
      if (!this.priceFile.exists()) {
         String defaultFileName = "item_prices.yml";
         if (!this.priceFileName.equals(defaultFileName)) {
            this.plugin.saveResource(defaultFileName, false);
            File defaultFile = new File(this.plugin.getDataFolder(), defaultFileName);
            if (defaultFile.exists()) {
               defaultFile.renameTo(this.priceFile);
            }
         } else {
            this.plugin.saveResource(this.priceFileName, false);
         }
      }

      this.priceConfig = YamlConfiguration.loadConfiguration(this.priceFile);
      if (this.economyEnabled) {
         this.currencyManager = new CurrencyManager(this.plugin);
         this.currencyManager.initialize();
         if (this.shopIntegrationEnabled) {
            this.shopIntegrationManager = new ShopIntegrationManager(this.plugin);
            this.shopIntegrationManager.initialize();
         }

         if (this.customPricesEnabled) {
            this.loadPrices();
         }

         this.validatePriceSourceMode();
      } else {
         this.plugin.getLogger().info("Custom economy is disabled. No sell integration will be available.");
      }

   }

   private void loadConfiguration() {
      FileConfiguration config = this.plugin.getConfig();
      this.economyEnabled = config.getBoolean("custom_economy.enabled", true);
      this.priceFileName = config.getString("custom_economy.price_file_name", "item_prices.yml");
      if (!this.priceFileName.endsWith(".yml") && !this.priceFileName.endsWith(".yaml")) {
         this.priceFileName = this.priceFileName + ".yml";
      }

      this.defaultPrice = config.getDouble("custom_economy.custom_prices.default_price", 1.0D);
      this.customPricesEnabled = config.getBoolean("custom_economy.custom_prices.enabled", true);
      this.shopIntegrationEnabled = config.getBoolean("custom_economy.shop_integration.enabled", true);
      String modeString = config.getString("custom_economy.price_source_mode", "SHOP_PRIORITY");

      try {
         this.priceSourceMode = ItemPriceManager.PriceSourceMode.valueOf(modeString.toUpperCase());
      } catch (IllegalArgumentException var4) {
         this.plugin.getLogger().warning("Invalid price source mode: " + modeString + ". Using SHOP_PRIORITY");
         this.priceSourceMode = ItemPriceManager.PriceSourceMode.SHOP_PRIORITY;
      }

   }

   private void validatePriceSourceMode() {
      if (this.economyEnabled) {
         boolean hasValidShopIntegration = this.shopIntegrationEnabled && this.shopIntegrationManager != null && this.shopIntegrationManager.hasActiveProvider();
         boolean hasValidCustomPrices = this.customPricesEnabled && !this.itemPrices.isEmpty();
         if (this.priceSourceMode == ItemPriceManager.PriceSourceMode.CUSTOM_ONLY && hasValidShopIntegration) {
            this.plugin.getLogger().warning("Price source mode is set to CUSTOM_ONLY but shop integration is enabled and working.");
            this.plugin.getLogger().warning("Prices from shop integration will not be used.");
         }

         if (this.priceSourceMode == ItemPriceManager.PriceSourceMode.SHOP_ONLY && !hasValidShopIntegration) {
            this.plugin.getLogger().warning("Price source mode is set to SHOP_ONLY but no valid shop integration is available.");
            this.plugin.getLogger().warning("Selling items from spawner will be disabled.");
         }

         if ((this.priceSourceMode == ItemPriceManager.PriceSourceMode.CUSTOM_PRIORITY || this.priceSourceMode == ItemPriceManager.PriceSourceMode.SHOP_PRIORITY) && !hasValidCustomPrices && !hasValidShopIntegration) {
            this.plugin.getLogger().warning("Price source mode " + String.valueOf(this.priceSourceMode) + " requires at least one valid price source (custom or shop).");
            this.plugin.getLogger().warning("Selling items from spawner will be disabled.");
         }

         if (this.priceSourceMode == ItemPriceManager.PriceSourceMode.CUSTOM_ONLY && !hasValidCustomPrices) {
            this.plugin.getLogger().warning("Price source mode is set to CUSTOM_ONLY but no valid custom prices are available.");
            Logger var10000 = this.plugin.getLogger();
            boolean var10001 = this.customPricesEnabled;
            var10000.warning("Custom prices enabled: " + var10001 + ", Loaded prices: " + this.itemPrices.size());
            this.plugin.getLogger().warning("Selling items from spawner will be disabled.");
         }

      }
   }

   private void loadPrices() {
      this.itemPrices.clear();
      Iterator var1 = this.priceConfig.getKeys(false).iterator();

      while(var1.hasNext()) {
         String key = (String)var1.next();
         double price = this.priceConfig.getDouble(key, this.defaultPrice);
         this.itemPrices.put(key, price);
      }

   }

   public double getPrice(Material material) {
      if (material != null && this.economyEnabled) {
         switch(this.priceSourceMode.ordinal()) {
         case 0:
            return this.getCustomPrice(material);
         case 1:
            return this.getShopPrice(material);
         case 2:
            double customPrice = this.getCustomPrice(material);
            return customPrice > 0.0D ? customPrice : this.getShopPrice(material);
         case 3:
            double shopPrice = this.getShopPrice(material);
            return shopPrice > 0.0D ? shopPrice : this.getCustomPrice(material);
         default:
            return this.defaultPrice;
         }
      } else {
         return 0.0D;
      }
   }

   private double getCustomPrice(Material material) {
      return this.economyEnabled && this.customPricesEnabled ? (Double)this.itemPrices.getOrDefault(material.name(), this.defaultPrice) : 0.0D;
   }

   private double getShopPrice(Material material) {
      return this.economyEnabled && this.shopIntegrationEnabled && this.shopIntegrationManager != null ? this.shopIntegrationManager.getPrice(material) : 0.0D;
   }

   public void setPrice(Material material, double price) {
      if (material != null && this.economyEnabled && this.customPricesEnabled) {
         this.itemPrices.put(material.name(), price);
         this.priceConfig.set(material.name(), price);
         this.saveConfig();
      }
   }

   public void reload() {
      this.loadConfiguration();
      if (this.economyEnabled) {
         this.priceFile = new File(this.plugin.getDataFolder(), this.priceFileName);
         if (this.currencyManager != null) {
            this.currencyManager.reload();
         } else {
            this.currencyManager = new CurrencyManager(this.plugin);
            this.currencyManager.initialize();
         }

         if (this.shopIntegrationEnabled) {
            if (this.shopIntegrationManager == null) {
               this.shopIntegrationManager = new ShopIntegrationManager(this.plugin);
            }

            this.shopIntegrationManager.initialize();
         } else {
            this.shopIntegrationManager = null;
         }

         if (this.customPricesEnabled) {
            this.priceConfig = YamlConfiguration.loadConfiguration(this.priceFile);
            this.loadPrices();
         } else {
            this.itemPrices.clear();
         }

         this.validatePriceSourceMode();
      } else {
         if (this.currencyManager != null) {
            this.currencyManager.cleanup();
            this.currencyManager = null;
         }

         this.shopIntegrationManager = null;
         this.itemPrices.clear();
         this.plugin.getLogger().info("Custom economy disabled - all sell integration cleaned up.");
      }

   }

   public void reloadShopIntegration() {
      if (this.shopIntegrationEnabled) {
         if (this.shopIntegrationManager == null) {
            this.shopIntegrationManager = new ShopIntegrationManager(this.plugin);
         }

         this.shopIntegrationManager.initialize();
      } else {
         this.shopIntegrationManager = null;
      }

   }

   public boolean hasSellIntegration() {
      if (!this.economyEnabled) {
         return false;
      } else if (this.currencyManager != null && this.currencyManager.isCurrencyAvailable()) {
         boolean hasValidCustomPrices = this.customPricesEnabled && !this.itemPrices.isEmpty();
         boolean hasValidShopIntegration = this.shopIntegrationEnabled && this.shopIntegrationManager != null && this.shopIntegrationManager.hasActiveProvider();
         if (this.priceSourceMode == ItemPriceManager.PriceSourceMode.CUSTOM_ONLY) {
            return hasValidCustomPrices;
         } else if (this.priceSourceMode == ItemPriceManager.PriceSourceMode.SHOP_ONLY) {
            return hasValidShopIntegration;
         } else {
            return hasValidCustomPrices || hasValidShopIntegration;
         }
      } else {
         return false;
      }
   }

   public boolean hasPriceFor(Material material) {
      if (material != null && this.economyEnabled) {
         boolean var10000;
         switch(this.priceSourceMode.ordinal()) {
         case 0:
            var10000 = this.customPricesEnabled && this.itemPrices.containsKey(material.name());
            break;
         case 1:
            var10000 = this.shopIntegrationEnabled && this.shopIntegrationManager != null && this.shopIntegrationManager.getPrice(material) > 0.0D;
            break;
         case 2:
         case 3:
            var10000 = this.customPricesEnabled && this.itemPrices.containsKey(material.name()) || this.shopIntegrationEnabled && this.shopIntegrationManager != null && this.shopIntegrationManager.getPrice(material) > 0.0D;
            break;
         default:
            var10000 = false;
         }

         return var10000;
      } else {
         return false;
      }
   }

   public void removePrice(Material material) {
      if (material != null && this.economyEnabled && this.customPricesEnabled) {
         this.itemPrices.remove(material.name());
         this.priceConfig.set(material.name(), (Object)null);
         this.saveConfig();
      }
   }

   public Map<String, Double> getAllPrices() {
      return !this.economyEnabled ? new ConcurrentHashMap() : new ConcurrentHashMap(this.itemPrices);
   }

   public String getActivePriceSource() {
      if (!this.economyEnabled) {
         return "Economy Disabled";
      } else {
         StringBuilder sources = new StringBuilder();
         if (!this.customPricesEnabled && !this.shopIntegrationEnabled) {
            sources.append("None (using default prices)");
         } else {
            if (this.customPricesEnabled) {
               sources.append("Custom");
            }

            if (this.shopIntegrationEnabled) {
               if (sources.length() > 0) {
                  sources.append(" + ");
               }

               String activeShop = this.shopIntegrationManager != null ? this.shopIntegrationManager.getActiveShopPlugin() : "None";
               sources.append("Shop (").append(activeShop).append(")");
            }
         }

         sources.append(" [Mode: ").append(this.priceSourceMode).append("]");
         if (this.currencyManager != null) {
            sources.append(" [Currency: ").append(this.currencyManager.getActiveCurrencyProvider()).append("]");
         }

         return sources.toString();
      }
   }

   public void debugPricesForMaterials(Set<Material> materials) {
      this.plugin.debug("=== Item Prices Debug Info ===");
      this.plugin.debug("Economy Enabled: " + this.economyEnabled);
      this.plugin.debug("Mode: " + String.valueOf(this.priceSourceMode));
      this.plugin.debug("Custom Prices Enabled: " + this.customPricesEnabled);
      this.plugin.debug("Shop Integration Enabled: " + this.shopIntegrationEnabled);
      this.plugin.debug("Default Price: " + this.defaultPrice);
      this.plugin.debug("Active Price Sources: " + this.getActivePriceSource());
      this.plugin.debug("Sell Integration Available: " + this.hasSellIntegration());
      if (!this.economyEnabled) {
         this.plugin.debug("Economy is disabled - skipping detailed price debug");
      } else {
         this.plugin.debug("Loaded " + materials.size() + " loot items with prices:");
         Iterator var2 = materials.iterator();

         while(var2.hasNext()) {
            Material material = (Material)var2.next();
            double finalPrice = this.getPrice(material);
            double customPrice = this.getCustomPrice(material);
            double shopPrice = this.getShopPrice(material);
            StringBuilder debug = new StringBuilder();
            debug.append("  ").append(material.name()).append(": Final=").append(String.format("%.2f", finalPrice));
            debug.append(" [");
            if (this.customPricesEnabled) {
               debug.append("Custom=").append(String.format("%.2f", customPrice));
            }

            if (this.shopIntegrationEnabled) {
               if (this.customPricesEnabled) {
                  debug.append(", ");
               }

               debug.append("Shop=").append(String.format("%.2f", shopPrice));
            }

            debug.append("]");
            String source = this.determineActiveSource(customPrice, shopPrice);
            debug.append(" <- ").append(source);
            this.plugin.debug(debug.toString());
         }

      }
   }

   private String determineActiveSource(double customPrice, double shopPrice) {
      if (!this.economyEnabled) {
         return "Disabled";
      } else {
         String var10000;
         switch(this.priceSourceMode.ordinal()) {
         case 0:
            var10000 = "Custom";
            break;
         case 1:
            var10000 = "Shop";
            break;
         case 2:
            var10000 = customPrice > 0.0D ? "Custom" : "Shop";
            break;
         case 3:
            var10000 = shopPrice > 0.0D ? "Shop" : "Custom";
            break;
         default:
            var10000 = "Default";
         }

         return var10000;
      }
   }

   private void saveConfig() {
      if (this.economyEnabled) {
         try {
            this.priceConfig.save(this.priceFile);
         } catch (IOException var2) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not save item prices configuration", var2);
         }

      }
   }

   public void cleanup() {
      if (this.currencyManager != null) {
         this.currencyManager.cleanup();
         this.currencyManager = null;
      }

      if (this.shopIntegrationManager != null) {
         this.shopIntegrationManager.cleanup();
         this.shopIntegrationManager = null;
      }

      this.itemPrices.clear();
   }

   @Generated
   public ItemPriceManager(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   @Generated
   public ShopIntegrationManager getShopIntegrationManager() {
      return this.shopIntegrationManager;
   }

   @Generated
   public CurrencyManager getCurrencyManager() {
      return this.currencyManager;
   }

   public static enum PriceSourceMode {
      CUSTOM_ONLY,
      SHOP_ONLY,
      CUSTOM_PRIORITY,
      SHOP_PRIORITY;

      // $FF: synthetic method
      private static ItemPriceManager.PriceSourceMode[] $values() {
         return new ItemPriceManager.PriceSourceMode[]{CUSTOM_ONLY, SHOP_ONLY, CUSTOM_PRIORITY, SHOP_PRIORITY};
      }
   }
}
