/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.color;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.bukkit.Color;

public class ColorJacksonDeserializer
extends StdDeserializer<Color> {
    public ColorJacksonDeserializer() {
        this((Class<Color>)null);
    }

    public ColorJacksonDeserializer(Class<Color> clazz) {
        super(clazz);
    }

    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            ObjectCodec objectCodec = jsonParser.getCodec();
            JsonNode jsonNode = (JsonNode)objectCodec.readTree(jsonParser);
            int n = jsonNode.get("red").asInt();
            int n2 = jsonNode.get("green").asInt();
            int n3 = jsonNode.get("blue").asInt();
            return Color.fromRGB((int)n, (int)n2, (int)n3);
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to deserialize a Color.", iOException);
        }
    }
}

