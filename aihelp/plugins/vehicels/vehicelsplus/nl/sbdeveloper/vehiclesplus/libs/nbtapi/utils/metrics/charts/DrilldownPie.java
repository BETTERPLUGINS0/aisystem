/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts;

import java.util.Map;
import java.util.concurrent.Callable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class DrilldownPie
extends CustomChart {
    private final Callable<Map<String, Map<String, Integer>>> callable;

    public DrilldownPie(String string, Callable<Map<String, Map<String, Integer>>> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    public JsonObjectBuilder.JsonObject getChartData() {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        Map<String, Map<String, Integer>> map = this.callable.call();
        if (map == null || map.isEmpty()) {
            return null;
        }
        boolean bl = true;
        for (Map.Entry<String, Map<String, Integer>> entry : map.entrySet()) {
            JsonObjectBuilder jsonObjectBuilder2 = new JsonObjectBuilder();
            boolean bl2 = true;
            for (Map.Entry<String, Integer> entry2 : map.get(entry.getKey()).entrySet()) {
                jsonObjectBuilder2.appendField(entry2.getKey(), entry2.getValue());
                bl2 = false;
            }
            if (bl2) continue;
            bl = false;
            jsonObjectBuilder.appendField(entry.getKey(), jsonObjectBuilder2.build());
        }
        if (bl) {
            return null;
        }
        return new JsonObjectBuilder().appendField("values", jsonObjectBuilder.build()).build();
    }
}

