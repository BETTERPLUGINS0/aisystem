/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Entity
 */
package nl.sbdeveloper.vehiclesplus.utils.nms;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.XReflection;
import nl.sbdeveloper.vehiclesplus.libs.xseries.reflection.minecraft.NMSExtras;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;

public final class MovementUtil {
    private static final MethodHandle SET_LOCATION;

    public static void setPosition(ArmorStand armorStand, Location location) {
        try {
            SET_LOCATION.invoke(NMSExtras.getEntityHandle((Entity)armorStand), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } catch (Throwable throwable) {
            // empty catch block
        }
    }

    @Generated
    private MovementUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static {
        Class<?> clazz = XReflection.getNMSClass("world.entity", "Entity");
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle methodHandle = null;
        try {
            methodHandle = lookup.findVirtual(clazz, XReflection.v(18, "a").orElse("setLocation"), MethodType.methodType(Void.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE));
        } catch (IllegalAccessException | NoSuchMethodException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
        SET_LOCATION = methodHandle;
    }
}

