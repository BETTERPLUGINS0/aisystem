package me.casperge.realisticseasons.metrics.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import me.casperge.realisticseasons.metrics.json.JsonObjectBuilder;

public class SimpleBarChart extends CustomChart {
   private final Callable<Map<String, Integer>> callable;

   public SimpleBarChart(String var1, Callable<Map<String, Integer>> var2) {
      super(var1);
      this.callable = var2;
   }

   protected JsonObjectBuilder.JsonObject getChartData() {
      JsonObjectBuilder var1 = new JsonObjectBuilder();
      Map var2 = (Map)this.callable.call();
      if (var2 != null && !var2.isEmpty()) {
         Iterator var3 = var2.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            var1.appendField((String)var4.getKey(), new int[]{(Integer)var4.getValue()});
         }

         return (new JsonObjectBuilder()).appendField("values", var1.build()).build();
      } else {
         return null;
      }
   }
}
