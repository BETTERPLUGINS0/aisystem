package com.ryandw11.structure.bstats.charts;

import com.ryandw11.structure.bstats.json.JsonObjectBuilder;
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
