/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.io.BukkitObjectInputStream
 *  org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackByteJacksonDeserializer
extends StdDeserializer<ItemStack> {
    public ItemStackByteJacksonDeserializer() {
        this((Class<ItemStack>)null);
    }

    public ItemStackByteJacksonDeserializer(Class<ItemStack> clazz) {
        super(clazz);
    }

    @Override
    public ItemStack deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            ObjectCodec objectCodec = jsonParser.getCodec();
            JsonNode jsonNode = (JsonNode)objectCodec.readTree(jsonParser);
            JsonNode jsonNode2 = jsonNode.get("item");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64Coder.decodeLines((String)jsonNode2.asText()));
            BukkitObjectInputStream bukkitObjectInputStream = new BukkitObjectInputStream((InputStream)byteArrayInputStream);
            ItemStack[] itemStackArray = new ItemStack[bukkitObjectInputStream.readInt()];
            for (int i = 0; i < itemStackArray.length; ++i) {
                itemStackArray[i] = (ItemStack)bukkitObjectInputStream.readObject();
            }
            bukkitObjectInputStream.close();
            return itemStackArray[0];
        } catch (IOException | ClassNotFoundException exception) {
            throw new IllegalStateException("Failed to deserialize an ItemStack.", exception);
        }
    }
}

