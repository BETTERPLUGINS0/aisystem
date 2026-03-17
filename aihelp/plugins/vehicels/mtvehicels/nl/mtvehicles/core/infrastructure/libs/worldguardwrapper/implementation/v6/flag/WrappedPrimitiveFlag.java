/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldguard.protection.flags.Flag
 *  org.bukkit.Location
 *  org.bukkit.util.Vector
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag;

import com.sk89q.worldguard.protection.flags.Flag;
import java.util.Optional;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.flag.AbstractWrappedFlag;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.utility.WorldGuardFlagUtilities;
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
            return Optional.of(WorldGuardFlagUtilities.adaptLocation((com.sk89q.worldedit.util.Location)value));
        }
        if (value instanceof com.sk89q.worldedit.Vector) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((com.sk89q.worldedit.Vector)value));
        }
        return Optional.ofNullable(value);
    }

    @Override
    public Optional<Object> fromWrapperValue(T value) {
        if (value instanceof Location) {
            return Optional.of(WorldGuardFlagUtilities.adaptLocation((Location)value));
        }
        if (value instanceof Vector) {
            return Optional.of(WorldGuardFlagUtilities.adaptVector((Vector)value));
        }
        return Optional.ofNullable(value);
    }
}

