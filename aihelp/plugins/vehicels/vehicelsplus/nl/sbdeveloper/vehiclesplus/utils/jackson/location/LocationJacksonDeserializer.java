/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.location;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationJacksonDeserializer
extends StdDeserializer<Location> {
    public LocationJacksonDeserializer() {
        this((Class<Location>)null);
    }

    public LocationJacksonDeserializer(Class<Location> clazz) {
        super(clazz);
    }

    @Override
    public Location deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            ObjectCodec objectCodec = jsonParser.getCodec();
            JsonNode jsonNode = (JsonNode)objectCodec.readTree(jsonParser);
            World world = Bukkit.getWorld((String)jsonNode.get("world").asText());
            if (world == null) {
                throw JsonMappingException.from(jsonParser, "Can't deserialize a Location because of an unknown/not loaded world");
            }
            return new Location(world, jsonNode.get("x").asDouble(), jsonNode.get("y").asDouble(), jsonNode.get("z").asDouble(), (float)jsonNode.get("yaw").asInt(), (float)jsonNode.get("pitch").asInt());
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to deserialize a Location.", iOException);
        }
    }
}

