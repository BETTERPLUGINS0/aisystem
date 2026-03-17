/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.math.Vector3
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldguard.protection.flags.Flag
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.flag.AbstractWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.utility.WorldGuardFlagUtilities;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class WrappedPrimitiveFlag<T>
extends AbstractWrappedFlag<T> {
    public WrappedPrimitiveFlag(Flag<T> flag) {
        super(flag);
    }

    @Override
    public Optional<T> fromWGValue(Object object) {
        if (object instanceof com.sk89q.worldedit.util.Location) {
            return Optional.of(BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)((com.sk89q.worldedit.util.Location)object)));
        }
        if (object instanceof Vector3) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((Vector3)object));
        }
        return Optional.ofNullable(object);
    }

    @Override
    public Optional<Object> fromWrapperValue(T t) {
        if (t instanceof Location) {
            return Optional.of(BukkitAdapter.adapt((Location)((Location)t)));
        }
        if (t instanceof Vector) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((Vector)t));
        }
        return Optional.ofNullable(t);
    }
}

