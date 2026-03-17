/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.bstats.charts;

import java.util.function.BiConsumer;
import nl.sbdeveloper.vehiclesplus.libs.bstats.json.JsonObjectBuilder;

public abstract class CustomChart {
    private final String chartId;

    protected CustomChart(String string) {
        if (string == null) {
            throw new IllegalArgumentException("chartId must not be null");
        }
        this.chartId = string;
    }

    public JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> biConsumer, boolean bl) {
        JsonObjectBuilder jsonObjectBuilder = new JsonObjectBuilder();
        jsonObjectBuilder.appendField("chartId", this.chartId);
        try {
            JsonObjectBuilder.JsonObject jsonObject = this.getChartData();
            if (jsonObject == null) {
                return null;
            }
            jsonObjectBuilder.appendField("data", jsonObject);
        } catch (Throwable throwable) {
            if (bl) {
                biConsumer.accept("Failed to get data for custom chart with id " + this.chartId, throwable);
            }
            return null;
        }
        return jsonObjectBuilder.build();
    }

    protected abstract JsonObjectBuilder.JsonObject getChartData();
}

