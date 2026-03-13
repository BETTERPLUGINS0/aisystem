package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.dependencies.citizens.CitizensDependency;
import com.nisovin.shopkeepers.libs.bstats.Metrics;

public class CitizensChart extends Metrics.SimplePie {
   public CitizensChart() {
      super("uses_citizens", () -> {
         return CitizensDependency.isPluginEnabled() ? "Yes" : "No";
      });
   }
}
