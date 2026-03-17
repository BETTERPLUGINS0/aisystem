package es.outlook.adriansrj.bstats.charts;

import es.outlook.adriansrj.bstats.json.JsonObjectBuilder;
import java.util.concurrent.Callable;

public class SimplePie extends CustomChart {
   private final Callable<String> callable;

   public SimplePie(String var1, Callable<String> var2) {
      super(var1);
      this.callable = var2;
   }

   protected JsonObjectBuilder.JsonObject getChartData() {
      String var1 = (String)this.callable.call();
      return var1 != null && !var1.isEmpty() ? (new JsonObjectBuilder()).appendField("value", var1).build() : null;
   }
}
