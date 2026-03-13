package github.nighter.smartspawner.hooks.economy.shops.providers.shopguiplus;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.shops.providers.ShopProvider;
import lombok.Generated;
import net.brcdev.shopgui.ShopGuiPlusApi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ShopGuiPlusProvider implements ShopProvider {
   private final SmartSpawner plugin;

   public String getPluginName() {
      return "ShopGUIPlus";
   }

   public boolean isAvailable() {
      try {
         Plugin shopGuiPlugin = Bukkit.getPluginManager().getPlugin("ShopGUIPlus");
         if (shopGuiPlugin != null && shopGuiPlugin.isEnabled()) {
            Class.forName("net.brcdev.shopgui.ShopGuiPlusApi");
            return true;
         }
      } catch (NoClassDefFoundError | ClassNotFoundException var2) {
         this.plugin.debug("ShopGUIPlus API not found: " + var2.getMessage());
      } catch (Exception var3) {
         this.plugin.getLogger().warning("Error initializing ShopGUIPlus integration: " + var3.getMessage());
      }

      return false;
   }

   public double getSellPrice(Material material) {
      try {
         ItemStack item = new ItemStack(material);
         double sellPrice = ShopGuiPlusApi.getItemStackPriceSell(item);
         return sellPrice > 0.0D ? sellPrice : 0.0D;
      } catch (Exception var5) {
         SmartSpawner var10000 = this.plugin;
         String var10001 = String.valueOf(material);
         var10000.debug("Error getting sell price for " + var10001 + " from ShopGUIPlus: " + var5.getMessage());
         return 0.0D;
      }
   }

   @Generated
   public ShopGuiPlusProvider(SmartSpawner plugin) {
      this.plugin = plugin;
   }
}
