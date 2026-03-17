/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.util.io.BukkitObjectOutputStream
 *  org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
 */
package nl.sbdeveloper.vehiclesplus.utils.jackson.itemstackbyte;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ItemStackByteJacksonSerializer
extends StdSerializer<ItemStack> {
    public ItemStackByteJacksonSerializer() {
        this((Class<ItemStack>)null);
    }

    public ItemStackByteJacksonSerializer(Class<ItemStack> clazz) {
        super(clazz);
    }

    @Override
    public void serialize(ItemStack itemStack, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream bukkitObjectOutputStream = new BukkitObjectOutputStream((OutputStream)byteArrayOutputStream);
            bukkitObjectOutputStream.writeInt(1);
            bukkitObjectOutputStream.writeObject((Object)itemStack);
            bukkitObjectOutputStream.close();
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("item", Base64Coder.encodeLines((byte[])byteArrayOutputStream.toByteArray()));
            jsonGenerator.writeEndObject();
        } catch (IOException iOException) {
            throw new IllegalStateException("Failed to serialize an ItemStack.", iOException);
        }
    }
}

