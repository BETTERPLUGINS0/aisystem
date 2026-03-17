/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class TrunkSlotsDeserializer
extends StdDeserializer<Map<Integer, ItemStack>> {
    public TrunkSlotsDeserializer() {
        this((Class<?>)null);
    }

    protected TrunkSlotsDeserializer(Class<?> clazz) {
        super(clazz);
    }

    @Override
    public Map<Integer, ItemStack> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        JsonToken jsonToken = jsonParser.getCurrentToken();
        if (jsonToken != JsonToken.START_ARRAY) {
            if (jsonToken == JsonToken.START_OBJECT) {
                MapType mapType = deserializationContext.getTypeFactory().constructMapType(HashMap.class, Integer.class, ItemStack.class);
                return (Map)jsonParser.getCodec().readValue(jsonParser, mapType);
            }
            return jsonToken == JsonToken.VALUE_NULL ? null : (Map)deserializationContext.handleUnexpectedToken(Map.class, jsonParser);
        }
        CollectionType collectionType = deserializationContext.getTypeFactory().constructCollectionType(List.class, ItemStack.class);
        List list = (List)jsonParser.getCodec().readValue(jsonParser, collectionType);
        HashMap<Integer, ItemStack> hashMap = new HashMap<Integer, ItemStack>();
        if (list != null) {
            for (int i = 0; i < list.size(); ++i) {
                if (list.get(i) == null) continue;
                hashMap.put(i, (ItemStack)list.get(i));
            }
        }
        return hashMap;
    }
}

