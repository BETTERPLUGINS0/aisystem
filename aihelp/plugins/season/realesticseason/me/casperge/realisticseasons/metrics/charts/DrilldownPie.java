package me.casperge.realisticseasons.metrics.charts;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import me.casperge.realisticseasons.metrics.json.JsonObjectBuilder;

public class DrilldownPie extends CustomChart {
   private final Callable<Map<String, Map<String, Integer>>> callable;

   public DrilldownPie(String var1, Callable<Map<String, Map<String, Integer>>> var2) {
      super(var1);
      this.callable = var2;
   }

   public JsonObjectBuilder.JsonObject getChartData() {
      JsonObjectBuilder var1 = new JsonObjectBuilder();
      Map var2 = (Map)this.callable.call();
      if (var2 != null && !var2.isEmpty()) {
         boolean var3 = true;
         Iterator var4 = var2.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            JsonObjectBuilder var6 = new JsonObjectBuilder();
            boolean var7 = true;

            for(Iterator var8 = ((Map)var2.get(var5.getKey())).entrySet().iterator(); var8.hasNext(); var7 = false) {
               Entry var9 = (Entry)var8.next();
               var6.appendField((String)var9.getKey(), (Integer)var9.getValue());
            }

            if (!var7) {
               var3 = false;
               var1.appendField((String)var5.getKey(), var6.build());
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
