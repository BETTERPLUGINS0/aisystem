/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts;

import java.util.concurrent.Callable;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.charts.CustomChart;
import nl.mtvehicles.core.infrastructure.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;

public class SimplePie
extends CustomChart {
    private final Callable<String> callable;

    public SimplePie(String chartId, Callable<String> callable) {
        super(chartId);
        this.callable = callable;
    }

    @Override
    protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
        String value = this.callable.call();
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new JsonObjectBuilder().appendField("value", value).build();
    }
}

