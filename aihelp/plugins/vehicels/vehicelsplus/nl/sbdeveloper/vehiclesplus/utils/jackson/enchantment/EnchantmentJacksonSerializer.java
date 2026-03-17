/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.enchantment;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XEnchantment;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentJacksonSerializer
extends StdSerializer<Enchantment> {
    public EnchantmentJacksonSerializer() {
        this((Class<Enchantment>)null);
    }

    public EnchantmentJacksonSerializer(Class<Enchantment> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(Enchantment enchantment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            jsonGenerator.writeString(XEnchantment.matchXEnchantment(enchantment).name());
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to serialize an Enchantment.", iOException);
        }
    }
}

