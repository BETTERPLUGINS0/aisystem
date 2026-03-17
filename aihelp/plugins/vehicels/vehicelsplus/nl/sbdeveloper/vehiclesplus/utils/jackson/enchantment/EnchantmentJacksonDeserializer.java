/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.enchantments.Enchantment
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.enchantment;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XEnchantment;
import org.bukkit.enchantments.Enchantment;

public class EnchantmentJacksonDeserializer
extends StdDeserializer<Enchantment> {
    public EnchantmentJacksonDeserializer() {
        this((Class<Enchantment>)null);
    }

    public EnchantmentJacksonDeserializer(Class<Enchantment> clazz) {
        super(clazz);
    }

    @Override
    public Enchantment deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            XEnchantment xEnchantment = XEnchantment.matchXEnchantment(jsonParser.getValueAsString()).orElse(null);
            if (xEnchantment == null) {
                throw JsonMappingException.from(jsonParser, "Can't deserialize Enchantment because enchantment is null / invalid");
            }
            return xEnchantment.getEnchant();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to deserialize an Enchantment.", iOException);
        }
    }
}

