package github.nighter.smartspawner.hooks.economy.shops.providers.excellentshop;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.economy.shops.providers.ShopProvider;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import su.nightexpress.nexshop.ShopAPI;
import su.nightexpress.nexshop.api.shop.type.TradeType;
import su.nightexpress.nexshop.shop.virtual.impl.VirtualProduct;

public class ExcellentShopProvider implements ShopProvider {
   private final SmartSpawner plugin;

   public String getPluginName() {
      return "ExcellentShop";
   }

   public boolean isAvailable() {
      try {
         Plugin excellentShopPlugin = Bukkit.getPluginManager().getPlugin("ExcellentShop");
         if (excellentShopPlugin != null && excellentShopPlugin.isEnabled()) {
            Class.forName("su.nightexpress.nexshop.ShopAPI");
            Class.forName("su.nightexpress.nexshop.api.shop.type.TradeType");
            Class.forName("su.nightexpress.nexshop.shop.virtual.impl.VirtualProduct");
            ShopAPI.getVirtualShop();
            return true;
         }
      } catch (NoClassDefFoundError | ClassNotFoundException var2) {
         this.plugin.debug("ExcellentShop API not found: " + var2.getMessage());
      } catch (Exception var3) {
         this.plugin.getLogger().warning("Error initializing ExcellentShop integration: " + var3.getMessage());
      }

      return false;
   }

   public double getSellPrice(Material material) {
      try {
         ItemStack item = new ItemStack(material);
         VirtualProduct product = ShopAPI.getVirtualShop().getBestProductFor(item, TradeType.SELL);
         if (product != null && product.isSellable()) {
            double pricePerUnit = product.getPrice(TradeType.SELL) / (double)product.getUnitAmount();
            return pricePerUnit > 0.0D ? pricePerUnit : 0.0D;
         } else {
            return 0.0D;
         }
      } catch (Exception var6) {
         SmartSpawner var10000 = this.plugin;
         String var10001 = String.valueOf(material);
         var10000.debug("Error getting sell price for " + var10001 + " from ExcellentShop: " + var6.getMessage());
         return 0.0D;
      }
   }

   @Generated
   public ExcellentShopProvider(SmartSpawner plugin) {
      this.plugin = plugin;
   }
}
