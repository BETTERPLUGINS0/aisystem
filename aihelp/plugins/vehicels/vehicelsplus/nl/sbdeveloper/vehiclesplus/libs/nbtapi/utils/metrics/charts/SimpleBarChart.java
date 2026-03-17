/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts;

import java.util.Map;
import java.util.concurrent.Callable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class SimpleBarChart
extends CustomChart {
    private final Callable<Map<String, Integer>> callable;

    public SimpleBarChart(String string, Callable<Map<String, Integer>> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Map<String, Integer> map = this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            jsonObjectBuilder.appendField(entry.getKey(), new int[]{entry.getValue()});
        }
        return new JsonObjectBuilder().appendField("values", jsonObjectBuilder.build()).build();
    }
}

