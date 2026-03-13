package github.nighter.smartspawner.hooks.economy.shops.providers.economyshopgui;

import github.nighter.smartspawner.SmartSpawner;
import me.gypopo.economyshopgui.api.events.ShopItemsLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ESGUICompatibilityHandler implements Listener {
   private final SmartSpawner plugin;

   public ESGUICompatibilityHandler(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onESGUIShopItemsLoad(ShopItemsLoadEvent event) {
      this.plugin.getItemPriceManager().reloadShopIntegration();
      this.plugin.getSpawnerSettingsConfig().reload();
      this.plugin.getSpawnerManager().reloadSpawnerDrops();
   }
}
