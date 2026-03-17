/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.Location
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldguard.protection.flags.Flag
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.legacy.flag;

import com.sk89q.worldedit.Location;
import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.legacy.utility.WorldGuardFlagUtilities;
import org.bukkit.util.Vector;

public class WrappedPrimitiveFlag<T>
extends AbstractWrappedFlag<T> {
    public WrappedPrimitiveFlag(Flag<T> flag) {
        super(flag);
    }

    @Override
    public Optional<T> fromWGValue(Object object) {
        if (object instanceof Location) {
            return Optional.of(WorldGuardFlagUtilities.adaptLocation((Location)object));
        }
        if (object instanceof com.sk89q.worldedit.Vector) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((com.sk89q.worldedit.Vector)object));
        }
        return Optional.ofNullable(object);
    }

    @Override
    public Optional<Object> fromWrapperValue(T t) {
        if (t instanceof org.bukkit.Location) {
            return Optional.of(WorldGuardFlagUtilities.adaptLocation((org.bukkit.Location)t));
        }
        if (t instanceof Vector) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((Vector)t));
        }
        return Optional.ofNullable(t);
    }
}

