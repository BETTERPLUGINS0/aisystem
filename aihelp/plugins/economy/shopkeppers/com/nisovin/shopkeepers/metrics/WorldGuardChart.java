package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.dependencies.worldguard.WorldGuardDependency;
import com.nisovin.shopkeepers.libs.bstats.Metrics;

public class WorldGuardChart extends Metrics.SimplePie {
   public WorldGuardChart() {
      super("uses_worldguard", () -> {
         return WorldGuardDependency.isPluginEnabled() ? "Yes" : "No";
      });
   }
}
