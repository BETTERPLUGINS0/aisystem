package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.libs.bstats.Metrics;

public class WorldsChart extends Metrics.SimplePie {
   public WorldsChart(ShopkeeperRegistry shopkeeperRegistry) {
      super("worlds_with_shops", () -> {
         return String.valueOf(shopkeeperRegistry.getWorldsWithShopkeepers().size());
      });
   }
}
