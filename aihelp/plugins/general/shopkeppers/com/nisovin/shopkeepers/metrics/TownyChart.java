package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.dependencies.towny.TownyDependency;
import com.nisovin.shopkeepers.libs.bstats.Metrics;

public class TownyChart extends Metrics.SimplePie {
   public TownyChart() {
      super("uses_towny", () -> {
         return TownyDependency.isPluginEnabled() ? "Yes" : "No";
      });
   }
}
