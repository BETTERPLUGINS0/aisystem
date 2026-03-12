package com.lenis0012.bukkit.loginsecurity.libs.bstats.charts;

import com.lenis0012.bukkit.loginsecurity.libs.bstats.json.JsonObjectBuilder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

public class DrilldownPie extends CustomChart {
   private final Callable<Map<String, Map<String, Integer>>> callable;

   public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
      super(chartId);
      this.callable = callable;
   }

   public JsonObjectBuilder.JsonObject getChartData() throws Exception {
      JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
      Map<String, Map<String, Integer>> map = (Map)this.callable.call();
      if (map != null && !map.isEmpty()) {
         boolean reallyAllSkipped = true;
         Iterator var4 = map.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<String, Map<String, Integer>> entryValues = (Entry)var4.next();
            JsonObjectBuilder valueBuilder = new JsonObjectBuilder();
            boolean allSkipped = true;

            for(Iterator var8 = ((Map)map.get(entryValues.getKey())).entrySet().iterator(); var8.hasNext(); allSkipped = false) {
               Entry<String, Integer> valueEntry = (Entry)var8.next();
               valueBuilder.appendField((String)valueEntry.getKey(), (Integer)valueEntry.getValue());
            }

            if (!allSkipped) {
               reallyAllSkipped = false;
               valuesBuilder.appendField((String)entryValues.getKey(), valueBuilder.build());
            }
         }

         if (reallyAllSkipped) {
            return null;
         } else {
            return (new JsonObjectBuilder()).appendField("values", valuesBuilder.build()).build();
         }
      } else {
         return null;
      }
   }
}
