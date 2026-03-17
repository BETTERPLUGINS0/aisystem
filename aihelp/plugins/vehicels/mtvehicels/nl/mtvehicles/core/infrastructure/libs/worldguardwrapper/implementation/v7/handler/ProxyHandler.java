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
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.handler;

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
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.flag.WrappedState;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.handler.IHandler;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.WorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v7.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import org.bukkit.Location;

public class ProxyHandler
extends Handler {
    private final WorldGuardImplementation implementation;
    private final IHandler handler;

    public ProxyHandler(WorldGuardImplementation implementation, IHandler handler, Session session) {
        super(session);
        this.implementation = implementation;
        this.handler = handler;
    }

    public void initialize(LocalPlayer player, com.sk89q.worldedit.util.Location current, ApplicableRegionSet set) {
        org.bukkit.entity.Player bukkitPlayer = BukkitAdapter.adapt((Player)player);
        Location bukkitLocation = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)current);
        this.handler.initialize(bukkitPlayer, bukkitLocation, this.implementation.wrapRegionSet(Objects.requireNonNull(bukkitLocation.getWorld()), set));
    }

    public boolean testMoveTo(LocalPlayer player, com.sk89q.worldedit.util.Location from, com.sk89q.worldedit.util.Location to, ApplicableRegionSet toSet, MoveType moveType) {
        org.bukkit.entity.Player bukkitPlayer = BukkitAdapter.adapt((Player)player);
        Location bukkitFrom = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)from);
        Location bukkitTo = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)to);
        return this.handler.testMoveTo(bukkitPlayer, bukkitFrom, bukkitTo, this.implementation.wrapRegionSet(Objects.requireNonNull(bukkitTo.getWorld()), toSet), moveType.name());
    }

    public boolean onCrossBoundary(LocalPlayer player, com.sk89q.worldedit.util.Location from, com.sk89q.worldedit.util.Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        org.bukkit.entity.Player bukkitPlayer = BukkitAdapter.adapt((Player)player);
        Location bukkitFrom = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)from);
        Location bukkitTo = BukkitAdapter.adapt((com.sk89q.worldedit.util.Location)to);
        ImmutableSet<IWrappedRegion> mappedEntered = ImmutableSet.copyOf(Collections2.transform(entered, region -> new WrappedRegion(bukkitTo.getWorld(), (ProtectedRegion)region)));
        ImmutableSet<IWrappedRegion> mappedExited = ImmutableSet.copyOf(Collections2.transform(exited, region -> new WrappedRegion(bukkitFrom.getWorld(), (ProtectedRegion)region)));
        return this.handler.onCrossBoundary(bukkitPlayer, bukkitFrom, bukkitTo, this.implementation.wrapRegionSet(Objects.requireNonNull(bukkitTo.getWorld()), toSet), mappedEntered, mappedExited, moveType.name());
    }

    public void tick(LocalPlayer player, ApplicableRegionSet set) {
        org.bukkit.entity.Player bukkitPlayer = BukkitAdapter.adapt((Player)player);
        this.handler.tick(bukkitPlayer, this.implementation.wrapRegionSet(bukkitPlayer.getWorld(), set));
    }

    @Nullable
    public StateFlag.State getInvincibility(LocalPlayer player) {
        org.bukkit.entity.Player bukkitPlayer = BukkitAdapter.adapt((Player)player);
        WrappedState state = this.handler.getInvincibility(bukkitPlayer);
        if (state == null) {
            return null;
        }
        return state == WrappedState.ALLOW ? StateFlag.State.ALLOW : StateFlag.State.DENY;
    }
}

