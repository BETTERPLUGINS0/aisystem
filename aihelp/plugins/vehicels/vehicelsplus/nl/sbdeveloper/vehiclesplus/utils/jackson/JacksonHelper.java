/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Particle
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.utils.jackson.color.ColorJacksonDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.color.ColorJacksonSerializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.enchantment.EnchantmentJacksonDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.enchantment.EnchantmentJacksonSerializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.itemstack.ItemStackJacksonDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.itemstack.ItemStackJacksonSerializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte.ItemStackByteJacksonDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte.ItemStackByteJacksonSerializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.location.LocationJacksonDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.location.LocationJacksonSerializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.particle.ParticleJacksonDeserializer;
import nl.sbdeveloper.vehiclesplus.utils.jackson.particle.ParticleJacksonSerializer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.hjson.JsonValue;

public final class JacksonHelper {
    private static final List<NamedType> partTypes = new ArrayList<NamedType>();

    public static ObjectMapper getMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = TypeFactory.defaultInstance().withClassLoader(VehiclesPlusPluginManager.getVehiclesPlusPlugin().getClass().getClassLoader());
        objectMapper.setTypeFactory(typeFactory);
        SimpleModule simpleModule = new SimpleModule("BukkitColorMapper");
        simpleModule.addSerializer(Color.class, new ColorJacksonSerializer());
        simpleModule.addDeserializer(Color.class, new ColorJacksonDeserializer());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    public static ObjectMapper getMapper(boolean bl) {
        ObjectMapper objectMapper = JacksonHelper.getMapper();
        TypeFactory typeFactory = TypeFactory.defaultInstance().withClassLoader(VehiclesPlusPluginManager.getVehiclesPlusPlugin().getClass().getClassLoader());
        objectMapper.setTypeFactory(typeFactory);
        objectMapper.registerSubtypes((NamedType[])partTypes.toArray(NamedType[]::new));
        SimpleModule simpleModule = new SimpleModule("BukkitObjectsMapper");
        if (bl) {
            simpleModule.addSerializer(ItemStack.class, new ItemStackJacksonSerializer());
            simpleModule.addDeserializer(ItemStack.class, new ItemStackJacksonDeserializer());
        } else {
            simpleModule.addSerializer(ItemStack.class, new ItemStackByteJacksonSerializer());
            simpleModule.addDeserializer(ItemStack.class, new ItemStackByteJacksonDeserializer());
        }
        simpleModule.addSerializer(Location.class, new LocationJacksonSerializer());
        simpleModule.addDeserializer(Location.class, new LocationJacksonDeserializer());
        simpleModule.addSerializer(Enchantment.class, new EnchantmentJacksonSerializer());
        simpleModule.addDeserializer(Enchantment.class, new EnchantmentJacksonDeserializer());
        simpleModule.addSerializer(Particle.class, new ParticleJacksonSerializer());
        simpleModule.addDeserializer(Particle.class, new ParticleJacksonDeserializer());
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    public static <T> T fromJSON(Class<T> clazz, String string, boolean bl) {
        return JacksonHelper.getMapper(bl).readValue(string, clazz);
    }

    public static <T> T fromHJSON(Class<T> clazz, File file, boolean bl) {
        return JacksonHelper.getMapper(bl).readValue(JsonValue.readHjson(new FileReader(file)).toString(), clazz);
    }

    public static <T> String toJson(T t, boolean bl) {
        return bl ? JacksonHelper.getMapper(true).writerWithDefaultPrettyPrinter().writeValueAsString(t) : JacksonHelper.getMapper(false).writeValueAsString(t);
    }

    public static <T> T fromJSON(Class<T> clazz, String string) {
        return JacksonHelper.getMapper().readValue(string, clazz);
    }

    public static <T> T fromJSON(Class<T> clazz, File file) {
        return JacksonHelper.getMapper().readValue(file, clazz);
    }

    public static <T> T fromJSON(Class<T> clazz, JsonNode jsonNode) {
        return JacksonHelper.getMapper().readValue(JacksonHelper.getMapper().writeValueAsString(jsonNode), clazz);
    }

    public static <T> String toJson(T t) {
        return JacksonHelper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(t);
    }

    @Generated
    private JacksonHelper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static List<NamedType> getPartTypes() {
        return partTypes;
    }
}

