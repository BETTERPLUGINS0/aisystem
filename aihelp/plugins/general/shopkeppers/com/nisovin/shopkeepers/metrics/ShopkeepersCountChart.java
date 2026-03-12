package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.libs.bstats.Metrics;

public class ShopkeepersCountChart extends Metrics.SimplePie {
   public ShopkeepersCountChart(ShopkeeperRegistry shopkeeperRegistry) {
      super("shopkeepers_count", () -> {
         int numberOfShopkeepers = shopkeeperRegistry.getAllShopkeepers().size();
         if (numberOfShopkeepers >= 100) {
            int hundreds = numberOfShopkeepers / 100;
            return "[" + hundreds + "00," + (hundreds + 1) + "00)";
         } else if (numberOfShopkeepers >= 50) {
            return "[50,100)";
         } else if (numberOfShopkeepers >= 10) {
            return "[10,50)";
         } else {
            return numberOfShopkeepers > 0 ? "<10" : "0";
         }
      });
   }
}
