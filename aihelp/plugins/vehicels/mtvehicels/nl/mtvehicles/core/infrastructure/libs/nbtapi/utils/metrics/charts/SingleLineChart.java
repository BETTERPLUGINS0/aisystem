/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts;

import java.util.concurrent.Callable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class SingleLineChart
extends CustomChart {
    private final Callable<Integer> callable;

    public SingleLineChart(String chartId, Callable<Integer> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        int value = this.callable.call();
        if (value == 0) {
            return null;
        }
        return new JsonObjectBuilder().appendField("value", value).build();
    }
}

