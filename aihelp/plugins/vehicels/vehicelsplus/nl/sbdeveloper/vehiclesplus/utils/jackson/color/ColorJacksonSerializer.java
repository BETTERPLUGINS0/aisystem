/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.color;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.bukkit.Color;

public class ColorJacksonSerializer
extends StdSerializer<Color> {
    public ColorJacksonSerializer() {
        this((Class<Color>)null);
    }

    public ColorJacksonSerializer(Class<Color> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(Color color, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeNumberField("red", color.getRed());
            jsonGenerator.writeNumberField("green", color.getGreen());
            jsonGenerator.writeNumberField("blue", color.getBlue());
            jsonGenerator.writeEndObject();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to serialize a Color.", iOException);
        }
    }
}

