/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.DataResult
 *  com.mojang.serialization.DynamicOps
 *  com.mojang.serialization.JsonOps
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.MojangToMapping;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
import org.bukkit.inventory.ItemStack;

public class NBTJsonUtil {
    public static JsonElement itemStackToJson(ItemStack itemStack) {
        try {
            Codec codec = (Codec)ClassWrapper.NMS_ITEMSTACK.getClazz().getField(MojangToMapping.getMapping().get("net.minecraft.world.item.ItemStack#CODEC")).get(null);
            Object object = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, itemStack);
            DataResult dataResult = codec.encode(object, (DynamicOps)JsonOps.INSTANCE, (Object)((JsonElement)JsonOps.INSTANCE.emptyMap()));
            Optional optional = (Optional)dataResult.getClass().getMethod("result", new Class[0]).invoke(dataResult, new Object[0]);
            return optional.orElse(null);
        } catch (Exception exception) {
            throw new NbtApiException("Error trying to get Json of an ItemStack.", exception);
        }
    }
}

