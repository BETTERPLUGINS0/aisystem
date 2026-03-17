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
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.utility.WorldGuardFlagUtilities;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class WrappedPrimitiveFlag<T>
extends AbstractWrappedFlag<T> {
    public WrappedPrimitiveFlag(Flag<T> handle) {
        super(handle);
    }

    @Override
    public Optional<T> fromWGValue(Object value) {
        if (value instanceof com.sk89q.worldedit.util.Location) {
            return Optional.of(BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)((com.sk89q.worldedit.util.Location)value)));
        }
        if (value instanceof Vector3) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((Vector3)value));
        }
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<Object> fromWrapperValue(T value) {
        if (value instanceof Location) {
            return Optional.of(BukkitAdapter.adapt((Location)((Location)value)));
        }
        if (value instanceof Vector) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((Vector)value));
        }
        return Optional.ofNullable(value);
    }
}

