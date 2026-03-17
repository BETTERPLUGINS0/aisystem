/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.itemstack;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import nl.sbdeveloper.vehiclesplus.VehiclesPlusPluginManager;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.Color;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ItemStackJacksonDeserializer
extends StdDeserializer<ItemStack> {
    public ItemStackJacksonDeserializer() {
        this((Class<ItemStack>)null);
    }

    public ItemStackJacksonDeserializer(Class<ItemStack> clazz) {
        super(clazz);
    }

    @Override
    public ItemStack deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            JsonNode jsonNode;
            ObjectCodec objectCodec = jsonParser.getCodec();
            JsonNode jsonNode2 = (JsonNode)objectCodec.readTree(jsonParser);
            XMaterial xMaterial = XMaterial.matchXMaterial(jsonNode2.get("material").asText()).orElse(null);
            if (xMaterial == null || xMaterial.parseMaterial() == null) {
                throw JsonMappingException.from(jsonParser, "Can't deserialize ItemStack because material is null / invalid");
            }
            ItemBuilder itemBuilder2 = new ItemBuilder(xMaterial.parseItem());
            if (jsonNode2.has("amount")) {
                itemBuilder2 = itemBuilder2.amount(jsonNode2.get("amount").asInt());
            }
            if (jsonNode2.has("damage")) {
                itemBuilder2 = itemBuilder2.durability(jsonNode2.get("damage").asInt());
            }
            if (jsonNode2.has("skull") && xMaterial == XMaterial.PLAYER_HEAD) {
                itemBuilder2 = itemBuilder2.skullTexture(jsonNode2.get("skull").asText());
            }
            if (jsonNode2.has("color")) {
                itemBuilder2 = itemBuilder2.armorColor(JacksonHelper.fromJSON(Color.class, jsonNode2.get("color")));
            }
            if (jsonNode2.has("name")) {
                itemBuilder2 = itemBuilder2.displayname(ColorUtil.__(jsonNode2.get("name").asText()));
            }
            if (XMaterial.supports(11) && jsonNode2.has("unbreakable") && jsonNode2.get("unbreakable").asBoolean()) {
                itemBuilder2 = itemBuilder2.unbreakable();
            }
            if (XMaterial.supports(14) && jsonNode2.has("custommodeldata")) {
                itemBuilder2 = itemBuilder2.customModelData(jsonNode2.get("custommodeldata").asInt(), itemBuilder -> {
                    VehiclesPlusPluginManager.getVehiclesPlusPlugin().getLogger().info("CustomModelData is not supported on this server version. Ignored `custommodeldata` setting.");
                    return itemBuilder;
                });
            }
            if (jsonNode2.has("lore")) {
                itemBuilder2 = itemBuilder2.lore(JacksonHelper.fromJSON(ArrayList.class, jsonNode2.get("lore").asText()));
            }
            if (jsonNode2.has("enchants")) {
                itemBuilder2 = itemBuilder2.enchant(JacksonHelper.fromJSON(HashMap.class, jsonNode2.get("enchants").asText()));
            }
            if (jsonNode2.has("flags") && (jsonNode = jsonNode2.get("flags")).isArray()) {
                for (JsonNode jsonNode3 : jsonNode) {
                    itemBuilder2 = itemBuilder2.flag(ItemFlag.valueOf((String)jsonNode3.asText()));
                }
            }
            return itemBuilder2.getItemStack();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to deserialize an ItemStack.", iOException);
        }
    }
}

