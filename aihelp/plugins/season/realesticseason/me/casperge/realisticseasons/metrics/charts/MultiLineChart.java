package me.casperge.realisticseasons.metrics.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import me.casperge.realisticseasons.metrics.json.JsonObjectBuilder;

public class MultiLineChart extends CustomChart {
   private final Callable<Map<String, Integer>> callable;

   public MultiLineChart(String var1, Callable<Map<String, Integer>> var2) {
      super(var1);
      this.callable = var2;
   }

   protected JsonObjectBuilder.JsonObject getChartData() {
      JsonObjectBuilder var1 = new JsonObjectBuilder();
      Map var2 = (Map)this.callable.call();
      if (var2 != null && !var2.isEmpty()) {
         boolean var3 = true;
         Iterator var4 = var2.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            if ((Integer)var5.getValue() != 0) {
               var3 = false;
               var1.appendField((String)var5.getKey(), (Integer)var5.getValue());
            }
         }

         if (var3) {
            return null;
         } else {
            return (new JsonObjectBuilder()).appendField("values", var1.build()).build();
         }
      } else {
         return null;
      }
   }
}
