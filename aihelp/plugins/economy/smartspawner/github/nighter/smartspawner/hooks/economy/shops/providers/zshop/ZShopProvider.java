package github.nighter.smartspawner.hooks.economy.shops.providers.zshop;

import fr.maxlego08.shop.api.ShopManager;
import fr.maxlego08.shop.api.buttons.ItemButton;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.shops.providers.ShopProvider;
import java.util.Optional;
import java.util.logging.Level;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class ZShopProvider implements ShopProvider {
   private final SmartSpawner plugin;
   private ShopManager shopManager;

   public String getPluginName() {
      return "zShop";
   }

   public boolean isAvailable() {
      try {
         Plugin zShopPlugin = Bukkit.getPluginManager().getPlugin("zShop");
         if (zShopPlugin != null && zShopPlugin.isEnabled()) {
            Class.forName("fr.maxlego08.shop.api.ShopManager");
            Class.forName("fr.maxlego08.shop.api.buttons.ItemButton");
            ShopManager manager = this.getShopManager();
            return manager != null;
         }
      } catch (NoClassDefFoundError | ClassNotFoundException var3) {
         this.plugin.debug("zShop API not found: " + var3.getMessage());
      } catch (Exception var4) {
         this.plugin.getLogger().warning("Error initializing zShop integration: " + var4.getMessage());
      }

      return false;
   }

   public double getSellPrice(Material material) {
      try {
         Optional<ItemButton> itemButtonOpt = this.getItemButton(material);
         if (itemButtonOpt.isEmpty()) {
            return 0.0D;
         } else {
            ItemButton itemButton = (ItemButton)itemButtonOpt.get();
            double sellPrice = itemButton.getSellPrice();
            return sellPrice > 0.0D ? sellPrice : 0.0D;
         }
      } catch (Exception var6) {
         SmartSpawner var10000 = this.plugin;
         String var10001 = String.valueOf(material);
         var10000.debug("Error getting sell price for " + var10001 + " from zShop: " + var6.getMessage());
         return 0.0D;
      }
   }

   private ShopManager getShopManager() {
      if (this.shopManager != null) {
         return this.shopManager;
      } else {
         try {
            this.shopManager = (ShopManager)this.plugin.getServer().getServicesManager().getRegistration(ShopManager.class).getProvider();
            return this.shopManager;
         } catch (Exception var2) {
            this.plugin.debug("Failed to get zShop ShopManager: " + var2.getMessage());
            return null;
         }
      }
   }

   private Optional<ItemButton> getItemButton(Material material) {
      try {
         ShopManager manager = this.getShopManager();
         return manager == null ? Optional.empty() : manager.getItemButton(material);
      } catch (Exception var3) {
         this.plugin.getLogger().log(Level.SEVERE, "Error getting item button from zShop", var3);
         return Optional.empty();
      }
   }

   @Generated
   public ZShopProvider(SmartSpawner plugin) {
      this.plugin = plugin;
   }
}
