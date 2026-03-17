/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts;

import java.util.concurrent.Callable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class SingleLineChart
extends CustomChart {
    private final Callable<Integer> callable;

    public SingleLineChart(String string, Callable<Integer> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() {
        int n = this.callable.call();
        if (n == 0) {
            return null;
        }
        return new JsonObjectBuilder().appendField("value", n).build();
    }
}

