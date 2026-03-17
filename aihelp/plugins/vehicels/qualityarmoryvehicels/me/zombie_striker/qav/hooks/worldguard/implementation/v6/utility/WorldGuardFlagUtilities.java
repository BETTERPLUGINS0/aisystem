/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.bukkit.BukkitWorld
 *  com.sk89q.worldedit.extent.Extent
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v6.utility;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.flag.WrappedPrimitiveFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v6.flag.WrappedStatusFlag;
import org.bukkit.Location;
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
        } else if (clazz.equals(Location.class)) {
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
        } else if (com.sk89q.worldedit.util.Location.class.isAssignableFrom(clazz)) {
            clazz = Location.class;
        } else if (Vector.class.isAssignableFrom(clazz)) {
            clazz = org.bukkit.util.Vector.class;
        }
        return WorldGuardFlagUtilities.wrap(flag, clazz);
    }

    public static Map.Entry<IWrappedFlag<?>, Object> wrap(Flag<?> flag, Object object) {
        IWrappedFlag<?> iWrappedFlag = WorldGuardFlagUtilities.wrapFixType(flag, object.getClass());
        Object t = ((AbstractWrappedFlag)iWrappedFlag).fromWGValue(object).get();
        return Maps.immutableEntry(iWrappedFlag, t);
    }

    public static org.bukkit.util.Vector adaptVector(Vector vector) {
        return new org.bukkit.util.Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector adaptVector(org.bukkit.util.Vector vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Location adaptLocation(com.sk89q.worldedit.util.Location location) {
        World world = location.getExtent() instanceof BukkitWorld ? ((BukkitWorld)location.getExtent()).getWorld() : null;
        return new Location(world, location.getX(), location.getY(), location.getZ());
    }

    public static com.sk89q.worldedit.util.Location adaptLocation(Location location) {
        return new com.sk89q.worldedit.util.Location((Extent)new BukkitWorld(location.getWorld()), location.getX(), location.getY(), location.getZ());
    }

    private WorldGuardFlagUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

