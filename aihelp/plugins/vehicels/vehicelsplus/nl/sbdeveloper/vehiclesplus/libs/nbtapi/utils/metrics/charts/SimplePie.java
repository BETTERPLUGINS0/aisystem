/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts;

import java.util.concurrent.Callable;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class SimplePie
extends CustomChart {
    private final Callable<String> callable;

    public SimplePie(String string, Callable<String> callable) {
        super(string);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() {
        String string = this.callable.call();
        if (string == null || string.isEmpty()) {
            return null;
        }
        return new JsonObjectBuilder().appendField("value", string).build();
    }
}

