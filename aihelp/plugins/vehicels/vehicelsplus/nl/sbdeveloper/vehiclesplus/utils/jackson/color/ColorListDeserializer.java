/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.color;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.ArrayList;
import java.util.List;
import nl.sbdeveloper.vehiclesplus.utils.jackson.ColorList;
import org.bukkit.Color;

public class ColorListDeserializer
extends JsonDeserializer<ColorList> {
    @Override
    public ColorList deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        ArrayList<Color> arrayList = new ArrayList<Color>();
        while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
            int n = 0;
            int n2 = 0;
            int n3 = 0;
            while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                String string = jsonParser.getCurrentName();
                jsonParser.nextToken();
                switch (string) {
                    case "red": {
                        n = jsonParser.getIntValue();
                        break;
                    }
                    case "green": {
                        n2 = jsonParser.getIntValue();
                        break;
                    }
                    case "blue": {
                        n3 = jsonParser.getIntValue();
                    }
                }
            }
            arrayList.add(Color.fromRGB((int)n, (int)n2, (int)n3));
        }
        return new ColorList((List<Color>)arrayList);
    }
}

