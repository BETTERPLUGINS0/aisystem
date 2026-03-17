/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler;

import java.util.Set;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegionSet;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface IHandler {
    default public void initialize(Player player, Location current, IWrappedRegionSet regionSet) {
    }

    default public boolean testMoveTo(Player player, Location from, Location to, IWrappedRegionSet regionSet, String moveType) {
        return true;
    }

    default public boolean onCrossBoundary(Player player, Location from, Location to, IWrappedRegionSet toSet, Set<IWrappedRegion> entered, Set<IWrappedRegion> exited, String moveType) {
        return true;
    }

    default public void tick(Player player, IWrappedRegionSet regionSet) {
    }

    default public WrappedState getInvincibility(Player player) {
        return null;
    }
}

