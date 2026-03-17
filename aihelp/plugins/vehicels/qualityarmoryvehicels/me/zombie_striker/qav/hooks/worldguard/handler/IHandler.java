/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks.worldguard.handler;

import java.util.Set;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegionSet;
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

