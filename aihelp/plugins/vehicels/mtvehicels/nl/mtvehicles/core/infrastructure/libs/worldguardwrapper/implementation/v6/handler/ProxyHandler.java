/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  com.sk89q.worldguard.protection.ApplicableRegionSet
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  com.sk89q.worldguard.protection.regions.ProtectedRegion
 *  com.sk89q.worldguard.session.MoveType
 *  com.sk89q.worldguard.session.Session
 *  com.sk89q.worldguard.session.handler.Handler
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.handler;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
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
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.WorldGuardImplementation;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.implementation.v6.region.WrappedRegion;
import nl.mtvehicles.core.infrastructure.libs.worldguardwrapper.region.IWrappedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ProxyHandler
extends Handler {
    private final WorldGuardImplementation implementation;
    private final IHandler handler;

    public ProxyHandler(WorldGuardImplementation implementation, IHandler handler, Session session) {
        super(session);
        this.implementation = implementation;
        this.handler = handler;
    }

    public void initialize(Player player, Location current, ApplicableRegionSet set) {
        this.handler.initialize(player, current, this.implementation.wrapRegionSet(Objects.requireNonNull(current.getWorld()), set));
    }

    public boolean testMoveTo(Player player, Location from, Location to, ApplicableRegionSet toSet, MoveType moveType) {
        return this.handler.testMoveTo(player, from, to, this.implementation.wrapRegionSet(Objects.requireNonNull(to.getWorld()), toSet), moveType.name());
    }

    public boolean onCrossBoundary(Player player, Location from, Location to, ApplicableRegionSet toSet, Set<ProtectedRegion> entered, Set<ProtectedRegion> exited, MoveType moveType) {
        ImmutableSet<IWrappedRegion> mappedEntered = ImmutableSet.copyOf(Collections2.transform(entered, region -> new WrappedRegion(to.getWorld(), (ProtectedRegion)region)));
        ImmutableSet<IWrappedRegion> mappedExited = ImmutableSet.copyOf(Collections2.transform(exited, region -> new WrappedRegion(from.getWorld(), (ProtectedRegion)region)));
        return this.handler.onCrossBoundary(player, from, to, this.implementation.wrapRegionSet(Objects.requireNonNull(to.getWorld()), toSet), mappedEntered, mappedExited, moveType.name());
    }

    public void tick(Player player, ApplicableRegionSet set) {
        this.handler.tick(player, this.implementation.wrapRegionSet(player.getWorld(), set));
    }

    @Nullable
    public StateFlag.State getInvincibility(Player player) {
        WrappedState state = this.handler.getInvincibility(player);
        if (state == null) {
            return null;
        }
        return state == WrappedState.ALLOW ? StateFlag.State.ALLOW : StateFlag.State.DENY;
    }
}

