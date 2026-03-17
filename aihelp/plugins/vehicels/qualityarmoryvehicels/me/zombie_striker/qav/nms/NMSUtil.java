/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.plugin.Plugin
 */
package me.zombie_striker.qav.nms;

import java.lang.reflect.Method;
import me.zombie_striker.qav.Main;
import me.zombie_striker.qav.api.QualityArmoryVehicles;
import me.zombie_striker.qav.util.xseries.reflection.XReflection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public final class NMSUtil {
    private static Method teleport;
    private static Method getHandle;

    public static void init() {
        if (!XReflection.supports(18)) {
            QualityArmoryVehicles.getPlugin().getLogger().info("[NMS] Legacy NMS support loaded.");
            return;
        }
        try {
            Class<?> clazz;
            try {
                clazz = XReflection.getCraftClass("entity.CraftEntity");
                getHandle = clazz.getMethod("getHandle", new Class[0]);
            } catch (Error | Exception throwable) {
                Main.DEBUG("[NMS] Unable to find getHandle method. This may cause bugs. Please report this error.");
            }
            clazz = XReflection.getNMSClass("world.entity", "Entity");
            try {
                teleport = clazz.getMethod("a", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
            } catch (Error | Exception throwable) {
                try {
                    teleport = clazz.getMethod("setLocation", Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
                } catch (Error | Exception throwable2) {
                    Main.DEBUG("[NMS] Unable to find teleport method. This may cause bugs. Please report this error.");
                }
            }
            QualityArmoryVehicles.getPlugin().getLogger().info("[NMS] Modern NMS support loaded.");
        } catch (Error | Exception throwable) {
            Main.DEBUG("[NMS] An exception occurred while loading data. This may cause bugs. Please report this error.");
        }
    }

    public static void teleport(Entity entity, Location location) {
        if (getHandle == null || teleport == null) {
            Main.DEBUG(getHandle == null ? "getHandle is null" : "teleport is null");
            NMSUtil.fallbackTeleport(entity, location);
            return;
        }
        try {
            Main.DEBUG("Trying to teleporting with NMS.");
            teleport.invoke(getHandle.invoke(entity, new Object[0]), location.getX(), location.getY(), location.getZ(), Float.valueOf(location.getYaw()), Float.valueOf(location.getPitch()));
        } catch (Error | Exception throwable) {
            Main.DEBUG("Unable to teleport with NMS. Falling back to legacy teleport.");
            NMSUtil.fallbackTeleport(entity, location);
        }
    }

    private static void fallbackTeleport(Entity entity, Location location) {
        Main.DEBUG("Falling back to legacy teleport.");
        Entity entity2 = entity.getPassenger();
        entity.eject();
        if (entity2 != null) {
            entity2.teleport(location);
        }
        entity.teleport(location);
        if (entity2 != null) {
            Bukkit.getScheduler().runTaskLater((Plugin)QualityArmoryVehicles.getPlugin(), () -> entity.setPassenger(entity2), 2L);
        }
    }
}

