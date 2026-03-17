/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.location;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import org.bukkit.Location;

public class LocationJacksonSerializer
extends StdSerializer<Location> {
    public LocationJacksonSerializer() {
        this((Class<Location>)null);
    }

    public LocationJacksonSerializer(Class<Location> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(Location location, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("world", location.getWorld().getName());
            jsonGenerator.writeNumberField("x", location.getX());
            jsonGenerator.writeNumberField("y", location.getY());
            jsonGenerator.writeNumberField("z", location.getZ());
            jsonGenerator.writeNumberField("yaw", location.getYaw());
            jsonGenerator.writeNumberField("pitch", location.getPitch());
            jsonGenerator.writeEndObject();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to serialize a Location.", iOException);
        }
    }
}

