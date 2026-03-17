/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings;

import java.lang.reflect.Constructor;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.NbtApiException;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.MinecraftVersion;
import nl.sbdeveloper.vehiclesplus.libs.nbtapi.utils.nmsmappings.ClassWrapper;

public enum ObjectCreator {
    NMS_NBTTAGCOMPOUND(null, null, ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), new Class[0]),
    NMS_CUSTOMDATA(MinecraftVersion.MC1_20_R4, null, ClassWrapper.NMS_CUSTOMDATA.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz()),
    NMS_BLOCKPOSITION(null, null, ClassWrapper.NMS_BLOCKPOSITION.getClazz(), Integer.TYPE, Integer.TYPE, Integer.TYPE),
    NMS_COMPOUNDFROMITEM(MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_20_R3, ClassWrapper.NMS_ITEMSTACK.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz());

    private Constructor<?> construct;
    private Class<?> targetClass;

    private ObjectCreator(MinecraftVersion minecraftVersion, MinecraftVersion minecraftVersion2, Class<?> clazz, Class<?> ... classArray) {
        if (clazz == null) {
            return;
        }
        if (minecraftVersion != null && MinecraftVersion.getVersion().getVersionId() < minecraftVersion.getVersionId()) {
            return;
        }
        if (minecraftVersion2 != null && MinecraftVersion.getVersion().getVersionId() > minecraftVersion2.getVersionId()) {
            return;
        }
        try {
            this.targetClass = clazz;
            this.construct = clazz.getDeclaredConstructor(classArray);
            this.construct.setAccessible(true);
        } catch (Exception exception) {
            MinecraftVersion.getLogger().log(Level.SEVERE, "Unable to find the constructor for the class '" + clazz.getName() + "'", exception);
        }
    }

    public Object getInstance(Object ... objectArray) {
        try {
            return this.construct.newInstance(objectArray);
        } catch (Exception exception) {
            throw new NbtApiException("Exception while creating a new instance of '" + this.targetClass + "'", exception);
        }
    }
}

