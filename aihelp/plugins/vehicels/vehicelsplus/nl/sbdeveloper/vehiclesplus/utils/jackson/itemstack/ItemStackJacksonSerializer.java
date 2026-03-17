/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.Damageable
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.LeatherArmorMeta
 *  org.bukkit.inventory.meta.SkullMeta
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.itemstack;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.libs.xseries.profiles.builder.XSkull;
import nl.sbdeveloper.vehiclesplus.utils.jackson.JacksonHelper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemStackJacksonSerializer
extends StdSerializer<ItemStack> {
    public ItemStackJacksonSerializer() {
        this((Class<ItemStack>)null);
    }

    public ItemStackJacksonSerializer(Class<ItemStack> clazz) {
        super(clazz);
    }

    private void writeRawFieldSafe(@NotNull JsonGenerator jsonGenerator, @NotNull String string, @Nullable String string2) {
        if (string2 != null && !string2.isBlank()) {
            jsonGenerator.writeFieldName(string);
            jsonGenerator.writeRawValue(string2);
        }
    }

    @Override
    public void serialize(ItemStack itemStack, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            Damageable damageable;
            jsonGenerator.writeStartObject();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta == null) {
                throw JsonMappingException.from(jsonGenerator, "Can't serialize ItemStack because meta is null");
            }
            if (itemMeta.hasDisplayName()) {
                jsonGenerator.writeStringField("name", itemMeta.getDisplayName());
            }
            if (itemMeta.hasLore()) {
                this.writeRawFieldSafe(jsonGenerator, "lore", JacksonHelper.toJson(itemMeta.getLore()));
            }
            if (itemStack.getAmount() > 1) {
                jsonGenerator.writeNumberField("amount", itemStack.getAmount());
            }
            if (XMaterial.supports(13)) {
                if (itemMeta instanceof Damageable && (damageable = (Damageable)itemMeta).hasDamage()) {
                    jsonGenerator.writeNumberField("damage", damageable.getDamage());
                }
            } else if (itemStack.getType().getMaxDurability() > 0) {
                jsonGenerator.writeNumberField("damage", itemStack.getDurability());
            }
            jsonGenerator.writeStringField("material", XMaterial.matchXMaterial(itemStack).name());
            if (XMaterial.supports(14) && itemMeta.hasCustomModelData()) {
                jsonGenerator.writeNumberField("custommodeldata", itemMeta.getCustomModelData());
            }
            if (XMaterial.supports(11) && itemMeta.isUnbreakable()) {
                jsonGenerator.writeBooleanField("unbreakable", true);
            }
            if (itemMeta.hasEnchants()) {
                this.writeRawFieldSafe(jsonGenerator, "enchants", JacksonHelper.toJson(itemMeta.getEnchants()));
            }
            if (!itemMeta.getItemFlags().isEmpty()) {
                this.writeRawFieldSafe(jsonGenerator, "flags", JacksonHelper.toJson(itemMeta.getItemFlags()));
            }
            if (itemMeta instanceof SkullMeta) {
                jsonGenerator.writeStringField("skull", XSkull.of(itemMeta).getProfileValue());
            }
            if (itemMeta instanceof LeatherArmorMeta) {
                damageable = (LeatherArmorMeta)itemMeta;
                jsonGenerator.writeObjectField("color", damageable.getColor());
            }
            jsonGenerator.writeEndObject();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to serialize an ItemStack.", iOException);
        }
    }
}

