package github.nighter.smartspawner.hooks.economy.shops.providers.shopguiplus;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.event.ShopGUIPlusPostEnableEvent;
import net.brcdev.shopgui.event.ShopsPostLoadEvent;
import net.brcdev.shopgui.exception.api.ExternalSpawnerProviderNameConflictException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpawnerHook implements Listener {
   private final SmartSpawner plugin;

   public SpawnerHook(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onShopGUIPlusPostEnable(ShopGUIPlusPostEnableEvent event) {
      if (Bukkit.getPluginManager().getPlugin("ShopGUIPlus") != null) {
         try {
            ShopGuiPlusApi.registerSpawnerProvider(this.plugin.getSpawnerProvider());
         } catch (ExternalSpawnerProviderNameConflictException var3) {
            this.plugin.getLogger().warning("Failed to hook spawner into ShopGUI+: " + var3.getMessage());
         }

         this.plugin.getLogger().info("Registered spawner provider in ShopGUI+!");
      }

   }

   @EventHandler
   public void onShopsPostLoad(ShopsPostLoadEvent event) {
      Scheduler.runTaskLater(() -> {
         this.plugin.getItemPriceManager().reloadShopIntegration();
         this.plugin.getSpawnerSettingsConfig().reload();
         this.plugin.getSpawnerManager().reloadSpawnerDrops();
      }, 100L);
   }

   public void unregister() {
      ShopGUIPlusPostEnableEvent.getHandlerList().unregister(this);
      ShopsPostLoadEvent.getHandlerList().unregister(this);
   }
}
