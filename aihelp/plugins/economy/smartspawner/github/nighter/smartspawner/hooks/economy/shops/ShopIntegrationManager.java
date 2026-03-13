package github.nighter.smartspawner.hooks.economy.shops;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.shops.providers.ShopProvider;
import github.nighter.smartspawner.hooks.economy.shops.providers.economyshopgui.ESGUICompatibilityHandler;
import github.nighter.smartspawner.hooks.economy.shops.providers.economyshopgui.EconomyShopGUIProvider;
import github.nighter.smartspawner.hooks.economy.shops.providers.excellentshop.ExcellentShopProvider;
import github.nighter.smartspawner.hooks.economy.shops.providers.shopguiplus.ShopGuiPlusProvider;
import github.nighter.smartspawner.hooks.economy.shops.providers.shopguiplus.SpawnerHook;
import github.nighter.smartspawner.hooks.economy.shops.providers.zshop.ZShopProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class ShopIntegrationManager {
   private final SmartSpawner plugin;
   private ShopProvider activeProvider;
   private final List<ShopProvider> availableProviders = new ArrayList();
   private SpawnerHook spawnerHook = null;
   private ESGUICompatibilityHandler esguiCompatibilityHandler = null;

   public void initialize() {
      this.availableProviders.clear();
      this.activeProvider = null;
      this.detectAndRegisterActiveProviders();
      this.selectActiveProvider();
   }

   private void detectAndRegisterActiveProviders() {
      String configuredShop = this.plugin.getConfig().getString("custom_economy.shop_integration.preferred_plugin", "auto");
      boolean autoDetect = "auto".equalsIgnoreCase(configuredShop);
      if (!autoDetect) {
         if (this.tryRegisterSpecificProvider(configuredShop)) {
            return;
         }

         this.plugin.getLogger().warning("Configured shop plugin '" + configuredShop + "' could not be loaded. Falling back to auto-detection.");
      }

      this.registerProviderIfAvailable("EconomyShopGUI", () -> {
         EconomyShopGUIProvider provider = new EconomyShopGUIProvider(this.plugin);
         if (provider.isAvailable() && this.esguiCompatibilityHandler == null) {
            try {
               this.esguiCompatibilityHandler = new ESGUICompatibilityHandler(this.plugin);
               this.plugin.getServer().getPluginManager().registerEvents(this.esguiCompatibilityHandler, this.plugin);
            } catch (Exception var3) {
               this.plugin.getLogger().warning("Failed to register ESGUICompatibilityHandler: " + var3.getMessage());
            }
         }

         return provider;
      });
      if (this.isPluginAvailable("ShopGUIPlus")) {
         this.registerProviderIfAvailable("ShopGUIPlus", () -> {
            if (this.spawnerHook == null) {
               try {
                  this.spawnerHook = new SpawnerHook(this.plugin);
                  this.plugin.getServer().getPluginManager().registerEvents(this.spawnerHook, this.plugin);
               } catch (Exception var2) {
                  this.plugin.debug("Failed to register SpawnerHook: " + var2.getMessage());
                  throw var2;
               }
            }

            return new ShopGuiPlusProvider(this.plugin);
         });
      }

      this.registerProviderIfAvailable("ExcellentShop", () -> {
         return new ExcellentShopProvider(this.plugin);
      });
   }

   private boolean tryRegisterSpecificProvider(String providerName) {
      try {
         String var2 = providerName.toLowerCase();
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -1419603924:
            if (var2.equals("excellentshop")) {
               var3 = 3;
            }
            break;
         case -217641569:
            if (var2.equals("shopguiplus")) {
               var3 = 1;
            }
            break;
         case 116199024:
            if (var2.equals("zshop")) {
               var3 = 2;
            }
            break;
         case 1259758215:
            if (var2.equals("economyshopgui")) {
               var3 = 0;
            }
         }

         switch(var3) {
         case 0:
            if (this.isPluginAvailable("EconomyShopGUI")) {
               this.registerProviderIfAvailable("EconomyShopGUI", () -> {
                  EconomyShopGUIProvider provider = new EconomyShopGUIProvider(this.plugin);
                  if (provider.isAvailable() && this.esguiCompatibilityHandler == null) {
                     try {
                        this.esguiCompatibilityHandler = new ESGUICompatibilityHandler(this.plugin);
                        this.plugin.getServer().getPluginManager().registerEvents(this.esguiCompatibilityHandler, this.plugin);
                     } catch (Exception var3) {
                        this.plugin.getLogger().warning("Failed to register ESGUICompatibilityHandler: " + var3.getMessage());
                     }
                  }

                  return provider;
               });
               return !this.availableProviders.isEmpty();
            }
            break;
         case 1:
            if (this.isPluginAvailable("ShopGUIPlus")) {
               this.registerProviderIfAvailable("ShopGUIPlus", () -> {
                  if (this.spawnerHook == null) {
                     this.spawnerHook = new SpawnerHook(this.plugin);
                     this.plugin.getServer().getPluginManager().registerEvents(this.spawnerHook, this.plugin);
                     this.plugin.debug("Registered SpawnerHook event listener for ShopGUIPlus");
                  }

                  return new ShopGuiPlusProvider(this.plugin);
               });
               return !this.availableProviders.isEmpty();
            }
            break;
         case 2:
            if (this.isPluginAvailable("ZShop")) {
               this.registerProviderIfAvailable("ZShop", () -> {
                  return new ZShopProvider(this.plugin);
               });
               return !this.availableProviders.isEmpty();
            }
            break;
         case 3:
            if (this.isPluginAvailable("ExcellentShop")) {
               this.registerProviderIfAvailable("ExcellentShop", () -> {
                  return new ExcellentShopProvider(this.plugin);
               });
               return !this.availableProviders.isEmpty();
            }
         }
      } catch (Exception var4) {
         this.plugin.debug("Failed to load specific provider " + providerName + ": " + var4.getMessage());
      }

      return false;
   }

   private boolean isPluginAvailable(String pluginName) {
      Plugin targetPlugin = this.plugin.getServer().getPluginManager().getPlugin(pluginName);
      return targetPlugin != null && targetPlugin.isEnabled();
   }

   private void registerProviderIfAvailable(String providerName, Supplier<ShopProvider> providerSupplier) {
      if (!this.availableProviders.isEmpty()) {
         this.plugin.debug("Skipping " + providerName + " registration - already have active provider: " + ((ShopProvider)this.availableProviders.getFirst()).getPluginName());
      } else {
         try {
            ShopProvider provider = (ShopProvider)providerSupplier.get();
            if (provider.isAvailable()) {
               this.availableProviders.add(provider);
            }
         } catch (NoClassDefFoundError var4) {
            this.plugin.debug("Shop provider " + providerName + " classes not found (plugin not installed): " + var4.getMessage());
         } catch (Exception var5) {
            this.plugin.debug("Failed to initialize shop provider " + providerName + ": " + var5.getMessage());
         }

      }
   }

   private void selectActiveProvider() {
      if (this.availableProviders.isEmpty()) {
         this.plugin.getLogger().info("No compatible shop plugins found. Shop integration is disabled.");
      } else {
         this.activeProvider = (ShopProvider)this.availableProviders.getFirst();
         this.plugin.getLogger().info("Auto-detected & successfully hook into shop plugin: " + this.activeProvider.getPluginName());
      }
   }

   public double getPrice(Material material) {
      if (this.activeProvider != null && material != null) {
         try {
            return this.activeProvider.getSellPrice(material);
         } catch (Exception var3) {
            SmartSpawner var10000 = this.plugin;
            String var10001 = String.valueOf(material);
            var10000.debug("Error getting price for " + var10001 + " from " + this.activeProvider.getPluginName() + ": " + var3.getMessage());
            return 0.0D;
         }
      } else {
         return 0.0D;
      }
   }

   public String getActiveShopPlugin() {
      return this.activeProvider != null ? this.activeProvider.getPluginName() : "None";
   }

   public boolean hasActiveProvider() {
      return this.activeProvider != null;
   }

   public void cleanup() {
      this.availableProviders.clear();
      this.activeProvider = null;
      if (this.spawnerHook != null) {
         this.spawnerHook.unregister();
         this.spawnerHook = null;
      }

      if (this.esguiCompatibilityHandler != null) {
         this.esguiCompatibilityHandler = null;
      }

   }

   @Generated
   public ShopIntegrationManager(SmartSpawner plugin) {
      this.plugin = plugin;
   }
}
