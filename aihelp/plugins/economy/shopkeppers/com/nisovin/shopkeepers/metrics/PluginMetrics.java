package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.libs.bstats.Metrics;
import com.nisovin.shopkeepers.util.java.Validate;

public class PluginMetrics {
   private final SKShopkeepersPlugin plugin;

   public PluginMetrics(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public void onEnable() {
      if (Settings.enableMetrics) {
         this.setupMetrics();
      }

   }

   public void onDisable() {
   }

   private void setupMetrics() {
      ShopkeeperRegistry shopkeeperRegistry = this.plugin.getShopkeeperRegistry();
      Metrics metrics = new Metrics(this.plugin);
      metrics.addCustomChart(new CitizensChart());
      metrics.addCustomChart(new WorldGuardChart());
      metrics.addCustomChart(new TownyChart());
      metrics.addCustomChart(new VaultEconomyChart());
      metrics.addCustomChart(new GringottsChart());
      metrics.addCustomChart(new ShopkeepersCountChart(shopkeeperRegistry));
      metrics.addCustomChart(new PlayerShopsChart(shopkeeperRegistry));
      metrics.addCustomChart(new FeaturesChart());
      metrics.addCustomChart(new WorldsChart(shopkeeperRegistry));
   }
}
