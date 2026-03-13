package com.nisovin.shopkeepers.metrics;

import com.nisovin.shopkeepers.libs.bstats.Metrics;
import org.bukkit.Bukkit;

public class GringottsChart extends Metrics.SimplePie {
   private static final String GRINGOTTS_PLUGIN_NAME = "Gringotts";

   public GringottsChart() {
      super("uses_gringotts", () -> {
         return Bukkit.getPluginManager().isPluginEnabled("Gringotts") ? "Yes" : "No";
      });
   }
}
