package github.nighter.smartspawner.hooks.economy.shops.providers.economyshopgui;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.shops.providers.ShopProvider;
import lombok.Generated;
import me.gypopo.economyshopgui.api.EconomyShopGUIHook;
import me.gypopo.economyshopgui.objects.ShopItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class EconomyShopGUIProvider implements ShopProvider {
   private final SmartSpawner plugin;
   private static final String[] PLUGIN_NAMES = new String[]{"EconomyShopGUI", "EconomyShopGUI-Premium"};

   public String getPluginName() {
      return this.plugin.getServer().getPluginManager().getPlugin("EconomyShopGUI-Premium") != null ? "EconomyShopGUI-Premium" : "EconomyShopGUI";
   }

   public boolean isAvailable() {
      try {
         Plugin economyShopGUI = null;
         String[] var2 = PLUGIN_NAMES;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            String pluginName = var2[var4];
            economyShopGUI = Bukkit.getPluginManager().getPlugin(pluginName);
            if (economyShopGUI != null) {
               this.plugin.debug("Found " + pluginName + " plugin");
               break;
            }
         }

         if (economyShopGUI != null) {
            return true;
         }
      } catch (Exception var6) {
         this.plugin.getLogger().warning("Error initializing EconomyShopGUI integration: " + var6.getMessage());
      }

      return false;
   }

   public double getSellPrice(Material material) {
      try {
         ItemStack item = new ItemStack(material);
         ShopItem shopItem = EconomyShopGUIHook.getShopItem(item);
         if (shopItem == null) {
            return 0.0D;
         } else {
            Double sellPrice = EconomyShopGUIHook.getItemSellPrice(shopItem, item);
            return sellPrice != null ? sellPrice : 0.0D;
         }
      } catch (Exception var5) {
         SmartSpawner var10000 = this.plugin;
         String var10001 = String.valueOf(material);
         var10000.debug("Error getting sell price for " + var10001 + " from EconomyShopGUI: " + var5.getMessage());
         return 0.0D;
      }
   }

   @Generated
   public EconomyShopGUIProvider(SmartSpawner plugin) {
      this.plugin = plugin;
   }
}
