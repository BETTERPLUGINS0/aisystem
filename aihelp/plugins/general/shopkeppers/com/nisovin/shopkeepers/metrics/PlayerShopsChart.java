package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.libs.bstats.Metrics;

public class PlayerShopsChart extends Metrics.SimplePie {
   public PlayerShopsChart(ShopkeeperRegistry shopkeeperRegistry) {
      super("uses_player_shops", () -> {
         return shopkeeperRegistry.getAllPlayerShopkeepers().size() > 0 ? "Yes" : "No";
      });
   }
}
