/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package com.magmaguy.shaded.bstats.charts;

import com.magmaguy.shaded.bstats.charts.CustomChart;
import com.magmaguy.shaded.bstats.json.JsonObjectBuilder;
import java.util.concurrent.Callable;

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

