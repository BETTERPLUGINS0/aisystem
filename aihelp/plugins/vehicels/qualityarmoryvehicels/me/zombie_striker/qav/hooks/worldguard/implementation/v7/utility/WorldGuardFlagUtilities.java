/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.math.Vector3
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldguard.protection.flags.Flag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v7.utility;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Map;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag.WrappedPrimitiveFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag.WrappedStatusFlag;
import org.bukkit.Location;
import org.bukkit.util.Vector;

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
        } else if (clazz.equals(Vector.class)) {
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
        } else if (Vector3.class.isAssignableFrom(clazz)) {
            clazz = Vector.class;
        }
        return WorldGuardFlagUtilities.wrap(flag, clazz);
    }

    public static Map.Entry<IWrappedFlag<?>, Object> wrap(Flag<?> flag, Object object) {
        IWrappedFlag<?> iWrappedFlag = WorldGuardFlagUtilities.wrapFixType(flag, object.getClass());
        Object t = ((AbstractWrappedFlag)iWrappedFlag).fromWGValue(object).get();
        return Maps.immutableEntry(iWrappedFlag, t);
    }

    public static Vector adaptVector(Vector3 vector3) {
        return new Vector(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public static Vector3 adaptVector(Vector vector) {
        return Vector3.at((double)vector.getX(), (double)vector.getY(), (double)vector.getZ());
    }

    private WorldGuardFlagUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

