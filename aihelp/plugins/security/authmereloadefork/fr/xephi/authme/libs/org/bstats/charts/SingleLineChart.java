package fr.xephi.authme.libs.org.bstats.charts;

import fr.xephi.authme.libs.org.bstats.json.JsonObjectBuilder;
import java.util.concurrent.Callable;

public class SingleLineChart extends CustomChart {
   private final Callable<Integer> callable;

   public SingleLineChart(String chartId, Callable<Integer> callable) {
      super(chartId);
      this.callable = callable;
   }

   protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
      int value = (Integer)this.callable.call();
      return value == 0 ? null : (new JsonObjectBuilder()).appendField("value", value).build();
   }
}
