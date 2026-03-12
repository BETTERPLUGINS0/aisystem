package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.dependencies.citizens.CitizensDependency;
import com.nisovin.shopkeepers.dependencies.towny.TownyDependency;
import com.nisovin.shopkeepers.dependencies.worldguard.WorldGuardDependency;
import com.nisovin.shopkeepers.libs.bstats.Metrics;
import com.nisovin.shopkeepers.util.java.MapUtils;
import java.util.LinkedHashMap;
import java.util.Map;

public class FeaturesChart extends Metrics.DrilldownPie {
   public FeaturesChart() {
      super("used_features", () -> {
         Map<String, Map<String, Integer>> allFeatures = new LinkedHashMap();
         addFeatureEntry(allFeatures, "check-shop-interaction-result", Settings.checkShopInteractionResult);
         addFeatureEntry(allFeatures, "bypass-spawn-blocking", Settings.bypassSpawnBlocking);
         addFeatureEntry(allFeatures, "enable-world-guard-restrictions", Settings.enableWorldGuardRestrictions && WorldGuardDependency.isPluginEnabled());
         addFeatureEntry(allFeatures, "require-world-guard-allow-shop-flag", Settings.requireWorldGuardAllowShopFlag && WorldGuardDependency.isPluginEnabled());
         addFeatureEntry(allFeatures, "enable-towny-restrictions", Settings.enableTownyRestrictions && TownyDependency.isPluginEnabled());
         addFeatureEntry(allFeatures, "enable-citizen-shops", Settings.enableCitizenShops && CitizensDependency.isPluginEnabled());
         addFeatureEntry(allFeatures, "disable-gravity", Settings.disableGravity);
         addFeatureEntry(allFeatures, "gravity-chunk-range", Settings.gravityChunkRange);
         addFeatureEntry(allFeatures, "mob-behavior-tick-period", Settings.entityBehaviorTickPeriod);
         addFeatureEntry(allFeatures, "save-instantly", Settings.saveInstantly);
         addFeatureEntry(allFeatures, "colored names allowed", Settings.nameRegex.contains("&"));
         addFeatureEntry(allFeatures, "protect-containers", Settings.protectContainers);
         addFeatureEntry(allFeatures, "prevent-item-movement", Settings.preventItemMovement);
         addFeatureEntry(allFeatures, "delete-shopkeeper-on-break-container", Settings.deleteShopkeeperOnBreakContainer);
         addFeatureEntry(allFeatures, "player-shopkeeper-inactive-days", Settings.playerShopkeeperInactiveDays > 0);
         addFeatureEntry(allFeatures, "tax-rate", Settings.taxRate > 0);
         addFeatureEntry(allFeatures, "use-strict-item-comparison", Settings.useStrictItemComparison);
         addFeatureEntry(allFeatures, "trade-log-storage", Settings.tradeLogStorage);
         addFeatureEntry(allFeatures, "notify-players-about-trades", Settings.notifyPlayersAboutTrades);
         addFeatureEntry(allFeatures, "notify-shop-owners-about-trades", Settings.notifyShopOwnersAboutTrades);
         addFeatureEntry(allFeatures, "disable-other-villagers", Settings.disableOtherVillagers);
         addFeatureEntry(allFeatures, "block-villager-spawns", Settings.blockVillagerSpawns);
         addFeatureEntry(allFeatures, "hire-other-villagers", Settings.hireOtherVillagers);
         addFeatureEntry(allFeatures, "increment-villager-statistics", Settings.incrementVillagerStatistics);
         return (Map)Unsafe.cast(allFeatures);
      });
   }

   private static void addFeatureEntry(Map<String, Map<String, Integer>> allFeatures, String featureName, boolean value) {
      assert allFeatures != null && featureName != null;

      addFeatureEntry(allFeatures, featureName, value ? "Yes" : "No");
   }

   private static void addFeatureEntry(Map<String, Map<String, Integer>> allFeatures, String featureName, Object value) {
      assert allFeatures != null && featureName != null;

      allFeatures.put(featureName, MapUtils.createMap(String.valueOf(value), 1));
   }
}
