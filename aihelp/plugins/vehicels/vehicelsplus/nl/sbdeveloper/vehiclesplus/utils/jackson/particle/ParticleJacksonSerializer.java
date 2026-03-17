/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Particle
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.particle;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import nl.sbdeveloper.vehiclesplus.libs.xseries.particles.XParticle;
import org.bukkit.Particle;

public class ParticleJacksonSerializer
extends StdSerializer<Particle> {
    public ParticleJacksonSerializer() {
        this((Class<Particle>)null);
    }

    public ParticleJacksonSerializer(Class<Particle> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(Particle particle, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            jsonGenerator.writeString(XParticle.of(particle).name());
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to serialize a Particle.", iOException);
        }
    }
}

