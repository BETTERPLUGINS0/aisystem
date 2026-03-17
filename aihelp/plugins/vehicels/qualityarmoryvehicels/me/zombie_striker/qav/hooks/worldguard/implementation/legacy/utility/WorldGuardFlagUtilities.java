/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.LocalWorld
 *  com.sk89q.worldedit.Location
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.bukkit.BukkitWorld
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.legacy.utility;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Location;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.flag.WrappedPrimitiveFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.flag.WrappedStatusFlag;
import org.bukkit.Bukkit;
import org.bukkit.World;

public final class WorldGuardFlagUtilities {
    public static <T> IWrappedFlag<T> wrap(Flag<?> flag, Class<T> clazz) {
        AbstractWrappedFlag abstractWrappedFlag;
        if (clazz.equals(WrappedState.class)) {
            abstractWrappedFlag = new WrappedStatusFlag((Flag<StateFlag.State>)flag);
        } else if (clazz.equals(Boolean.class) || clazz.equals(Boolean.TYPE)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(Double.class) || clazz.equals(Double.TYPE)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(Enum.class)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(Integer.class) || clazz.equals(Integer.TYPE)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(org.bukkit.Location.class)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(String.class)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(org.bukkit.util.Vector.class)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (clazz.equals(Object.class)) {
            abstractWrappedFlag = new WrappedPrimitiveFlag(flag);
        } else {
            throw new IllegalArgumentException("Unsupported flag type " + clazz.getName());
        }
        return abstractWrappedFlag;
    }

    public static IWrappedFlag<?> wrapFixType(Flag<?> flag, Class<?> clazz) {
        if (StateFlag.State.class.isAssignableFrom(clazz)) {
            clazz = WrappedState.class;
        } else if (Location.class.isAssignableFrom(clazz)) {
            clazz = org.bukkit.Location.class;
        } else if (Vector.class.isAssignableFrom(clazz)) {
            clazz = org.bukkit.util.Vector.class;
        }
        return WorldGuardFlagUtilities.wrap(flag, clazz);
    }

    public static Map.Entry<IWrappedFlag<?>, Object> wrap(Flag<?> flag, Object object) {
        IWrappedFlag<?> iWrappedFlag = WorldGuardFlagUtilities.wrapFixType(flag, object.getClass());
        Object t = ((AbstractWrappedFlag)iWrappedFlag).fromWGValue(object).orElseThrow(NullPointerException::new);
        return Maps.immutableEntry(iWrappedFlag, t);
    }

    public static org.bukkit.util.Vector adaptVector(Vector vector) {
        return new org.bukkit.util.Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector adaptVector(org.bukkit.util.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static org.bukkit.Location adaptLocation(Location location) {
        Vector vector = location.getPosition();
        World world = location.getWorld() instanceof BukkitWorld ? ((BukkitWorld)location.getWorld()).getWorld() : Bukkit.getWorld((String)location.getWorld().getName());
        return new org.bukkit.Location(world, vector.getX(), vector.getY(), vector.getZ());
    }

    public static Location adaptLocation(org.bukkit.Location location) {
        return new Location((LocalWorld)new BukkitWorld(location.getWorld()), new Vector(location.getX(), location.getY(), location.getZ()));
    }

    private WorldGuardFlagUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

