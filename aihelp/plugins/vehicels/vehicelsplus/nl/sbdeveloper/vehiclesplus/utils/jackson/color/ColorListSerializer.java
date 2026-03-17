/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.color;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import nl.sbdeveloper.vehiclesplus.utils.jackson.ColorList;
import org.bukkit.Color;

public class ColorListSerializer
extends JsonSerializer<ColorList> {
    @Override
    public void serialize(ColorList colorList, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        jsonGenerator.writeStartArray();
        for (Color color : colorList) {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("red", color.getRed());
            jsonGenerator.writeNumberField("green", color.getGreen());
            jsonGenerator.writeNumberField("blue", color.getBlue());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
    }
}

