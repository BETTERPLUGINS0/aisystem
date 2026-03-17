/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Particle
 *  org.bukkit.enchantments.Enchantment
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.particle;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import nl.sbdeveloper.vehiclesplus.libs.xseries.particles.XParticle;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;

public class ParticleJacksonDeserializer
extends StdDeserializer<Particle> {
    public ParticleJacksonDeserializer() {
        this((Class<Enchantment>)null);
    }

    public ParticleJacksonDeserializer(Class<Enchantment> clazz) {
        super(clazz);
    }

    @Override
    public Particle deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            XParticle xParticle = XParticle.of(jsonParser.getValueAsString()).orElse(null);
            if (xParticle == null) {
                throw JsonMappingException.from(jsonParser, "Can't deserialize Particle because enchantment is null / invalid");
            }
            return xParticle.get();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to deserialize an Enchantment.", iOException);
        }
    }
}

