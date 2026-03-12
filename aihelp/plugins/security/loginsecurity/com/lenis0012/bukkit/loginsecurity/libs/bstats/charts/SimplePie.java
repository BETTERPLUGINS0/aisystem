package com.lenis0012.bukkit.loginsecurity.libs.bstats.charts;

import com.lenis0012.bukkit.loginsecurity.libs.bstats.json.JsonObjectBuilder;
import java.util.concurrent.Callable;

public class SimplePie extends CustomChart {
   private final Callable<String> callable;

   public SimplePie(String chartId, Callable<String> callable) {
      super(chartId);
      this.callable = callable;
   }

   protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
      String value = (String)this.callable.call();
      return value != null && !value.isEmpty() ? (new JsonObjectBuilder()).appendField("value", value).build() : null;
   }
}
