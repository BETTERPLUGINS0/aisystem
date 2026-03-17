/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.bstats.charts;

import java.util.Map;
import java.util.concurrent.Callable;
import nl.sbdeveloper.vehiclesplus.libs.bstats.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.bstats.json.JsonObjectBuilder;

public class AdvancedBarChart
extends CustomChart {
    private final Callable<Map<String, int[]>> callable;

    public AdvancedBarChart(String string, Callable<Map<String, int[]>> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Map<String, int[]> map = this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        boolean bl = true;
        for (Map.Entry<String, int[]> entry : map.entrySet()) {
            if (entry.getValue().length == 0) continue;
            bl = false;
            jsonObjectBuilder.appendField(entry.getKey(), entry.getValue());
        }
        if (bl) {
            return null;
        }
        return new JsonObjectBuilder().appendField("values", jsonObjectBuilder.build()).build();
    }
}

