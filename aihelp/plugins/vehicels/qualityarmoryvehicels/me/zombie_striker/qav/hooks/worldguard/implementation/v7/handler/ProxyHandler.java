/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldedit.bukkit.BukkitAdapter
 *  com.sk89q.worldedit.entity.Player
 *  com.sk89q.worldedit.util.Location
 *  com.sk89q.worldguard.LocalPlayer
 *  com.sk89q.worldguard.protection.ApplicableRegionSet
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  com.sk89q.worldguard.session.MoveType
 *  com.sk89q.worldguard.session.Session
 *  com.sk89q.worldguard.session.handler.Handler
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks.worldguard.implementation.v7.handler;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.session.MoveType;
import com.sk89q.worldguard.session.Session;
import com.sk89q.worldguard.session.handler.Handler;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nullable;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import me.zombie_striker.qav.hooks.worldguard.handler.IHandler;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.WorldGuardImplementation;
import me.zombie_striker.qav.hooks.worldguard.implementation.v7.region.WrappedRegion;
import me.zombie_striker.qav.hooks.worldguard.region.IWrappedRegion;
import org.bukkit.Location;

public class ProxyHandler
extends Handler {
    private final WorldGuardImplementation implementation;
    private final IHandler handler;

    public ProxyHandler(WorldGuardImplementation worldGuardImplementation, IHandler iHandler, Session session) {
        super(session);
        this.implementation = worldGuardImplementation;
        this.handler = iHandler;
    }

    public void initialize(LocalPlayer localPlayer, com.sk89q.worldedit.util.Location location, ApplicableRegionSet applicableRegionSet) {
        org.bukkit.entity.Player player = BukkitAdapter.adapt((Player)localPlayer);
        Location location2 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location);
        this.handler.initialize(player, location2, this.implementation.wrapRegionSet(Objects.requireNonNull(location2.getWorld()), applicableRegionSet));
    }

    public boolean testMoveTo(LocalPlayer localPlayer, com.sk89q.worldedit.util.Location location, com.sk89q.worldedit.util.Location location2, ApplicableRegionSet applicableRegionSet, MoveType moveType) {
        org.bukkit.entity.Player player = BukkitAdapter.adapt((Player)localPlayer);
        Location location3 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location);
        Location location4 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location2);
        return this.handler.testMoveTo(player, location3, location4, this.implementation.wrapRegionSet(Objects.requireNonNull(location4.getWorld()), applicableRegionSet), moveType.name());
    }

    public boolean onCrossBoundary(LocalPlayer localPlayer, com.sk89q.worldedit.util.Location location, com.sk89q.worldedit.util.Location location2, ApplicableRegionSet applicableRegionSet, Set<ProtectedRegion> set, Set<ProtectedRegion> set2, MoveType moveType) {
        org.bukkit.entity.Player player = BukkitAdapter.adapt((Player)localPlayer);
        Location location3 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location);
        Location location4 = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)location2);
        ImmutableSet<IWrappedRegion> immutableSet = ImmutableSet.copyOf(Collections2.transform(set, protectedRegion -> new WrappedRegion(location4.getWorld(), (ProtectedRegion)protectedRegion)));
        ImmutableSet<IWrappedRegion> immutableSet2 = ImmutableSet.copyOf(Collections2.transform(set2, protectedRegion -> new WrappedRegion(location3.getWorld(), (ProtectedRegion)protectedRegion)));
        return this.handler.onCrossBoundary(player, location3, location4, this.implementation.wrapRegionSet(Objects.requireNonNull(location4.getWorld()), applicableRegionSet), immutableSet, immutableSet2, moveType.name());
    }

    public void tick(LocalPlayer localPlayer, ApplicableRegionSet applicableRegionSet) {
        org.bukkit.entity.Player player = BukkitAdapter.adapt((Player)localPlayer);
        this.handler.tick(player, this.implementation.wrapRegionSet(player.getWorld(), applicableRegionSet));
    }

    @Nullable
    public StateFlag.State getInvincibility(LocalPlayer localPlayer) {
        org.bukkit.entity.Player player = BukkitAdapter.adapt((Player)localPlayer);
        WrappedState wrappedState = this.handler.getInvincibility(player);
        if (wrappedState == null) {
            return null;
        }
        return wrappedState == WrappedState.ALLOW ? StateFlag.State.ALLOW : StateFlag.State.DENY;
    }
}

