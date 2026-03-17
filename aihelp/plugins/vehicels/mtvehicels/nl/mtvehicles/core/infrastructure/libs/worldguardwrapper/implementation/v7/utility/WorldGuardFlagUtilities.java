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
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.utility;

import com.google.common.collect.Maps;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import java.util.Map;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.IWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag.WrappedPrimitiveFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag.WrappedStatusFlag;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class WorldGuardFlagUtilities {
    public static <T> IWrappedFlag<T> wrap(Flag<?> flag, Class<T> type) {
        AbstractWrappedFlag wrappedFlag;
        if (type.equals(WrappedState.class)) {
            wrappedFlag = new WrappedStatusFlag((Flag<StateFlag.State>)flag);
        } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(Enum.class)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(Location.class)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(String.class)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(Vector.class)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else if (type.equals(Object.class)) {
            wrappedFlag = new WrappedPrimitiveFlag(flag);
        } else {
            throw new IllegalArgumentException("Unsupported flag type " + type.getName());
        }
        return wrappedFlag;
    }

    public static IWrappedFlag<?> wrapFixType(Flag<?> flag, Class<?> type) {
        if (StateFlag.State.class.isAssignableFrom(type)) {
            type = WrappedState.class;
        } else if (com.sk89q.worldedit.util.Location.class.isAssignableFrom(type)) {
            type = Location.class;
        } else if (Vector3.class.isAssignableFrom(type)) {
            type = Vector.class;
        }
        return WorldGuardFlagUtilities.wrap(flag, type);
    }

    public static Map.Entry<IWrappedFlag<?>, Object> wrap(Flag<?> flag, Object value) {
        IWrappedFlag<?> wrappedFlag = WorldGuardFlagUtilities.wrapFixType(flag, value.getClass());
        Object wrappedValue = ((AbstractWrappedFlag)wrappedFlag).fromWGValue(value).get();
        return Maps.immutableEntry(wrappedFlag, wrappedValue);
    }

    public static Vector adaptVector(Vector3 vector) {
        return new Vector(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Vector3 adaptVector(Vector vector) {
        return Vector3.at((double)vector.getX(), (double)vector.getY(), (double)vector.getZ());
    }

    private WorldGuardFlagUtilities() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

