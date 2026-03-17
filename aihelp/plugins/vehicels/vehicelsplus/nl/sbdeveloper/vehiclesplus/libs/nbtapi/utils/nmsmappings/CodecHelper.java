/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.DataResult
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings;

import com.mojang.serialization.DataResult;
import java.util.Objects;
import java.util.Optional;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NBTReflectionUtil;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;

public class CodecHelper {
    public static Object convertItemStackToNbt(Object object) {
        DataResult dataResult = null;
        try {
            dataResult = NBTReflectionUtil.itemstack_codec.encodeStart(NBTReflectionUtil.nbtRegistryOps, object);
            Objects.requireNonNull(dataResult);
            return ((Optional)dataResult.getClass().getMethod("result", new Class[0]).invoke(dataResult, new Object[0])).get();
        } catch (Exception exception) {
            throw new NbtApiException("Failed to convert ItemStack to NBT. " + dataResult + " " + object, exception);
        }
    }

    public static Object convertNbtToItemStack(Object object) {
        DataResult dataResult = null;
        try {
            dataResult = NBTReflectionUtil.itemstack_codec.parse(NBTReflectionUtil.nbtRegistryOps, object);
            Objects.requireNonNull(dataResult);
            return ((Optional)dataResult.getClass().getMethod("result", new Class[0]).invoke(dataResult, new Object[0])).get();
        } catch (Exception exception) {
            throw new NbtApiException("Failed to convert NBT to ItemStack. " + dataResult + " " + object, exception);
        }
    }
}

