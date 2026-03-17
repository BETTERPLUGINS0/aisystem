package es.outlook.adriansrj.bstats.charts;

import es.outlook.adriansrj.bstats.json.JsonObjectBuilder;
import java.util.concurrent.Callable;

public class SingleLineChart extends CustomChart {
   private final Callable<Integer> callable;

   public SingleLineChart(String var1, Callable<Integer> var2) {
      super(var1);
      this.callable = var2;
   }

   protected JsonObjectBuilder.JsonObject getChartData() {
      int var1 = (Integer)this.callable.call();
      return var1 == 0 ? null : (new JsonObjectBuilder()).appendField("value", var1).build();
   }
}
