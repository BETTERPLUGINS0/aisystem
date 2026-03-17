/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package me.zombie_striker.qav.hooks.implementation;

import java.util.Optional;
import me.zombie_striker.qav.hooks.ProtectionHook;
import me.zombie_striker.qav.hooks.worldguard.WorldGuardWrapper;
import me.zombie_striker.qav.hooks.worldguard.flag.IWrappedFlag;
import me.zombie_striker.qav.hooks.worldguard.flag.WrappedState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardHook
implements ProtectionHook {
    private final WorldGuardWrapper worldGuard = WorldGuardWrapper.getInstance();
    private final IWrappedFlag<WrappedState> canMove = this.worldGuard.getFlag("qav-use", WrappedState.class).orElse(null);
    private final IWrappedFlag<WrappedState> canPlace = this.worldGuard.getFlag("vehicle-place", WrappedState.class).orElse(null);
    private final IWrappedFlag<WrappedState> canRemove = this.worldGuard.getFlag("vehicle-remove", WrappedState.class).orElse(null);

    public static void register() {
        WorldGuardWrapper.getInstance().registerFlag("qav-use", WrappedState.class, WrappedState.ALLOW);
    }

    @Override
    public boolean canMove(Player player, Location location) {
        return this.checkFlag(player, location, this.canMove);
    }

    @Override
    public boolean canPlace(Player player, Location location) {
        return this.checkFlag(player, location, this.canPlace);
    }

    @Override
    public boolean canRemove(Player player, Location location) {
        return this.checkFlag(player, location, this.canRemove);
    }

    private boolean checkFlag(Player player, Location location, IWrappedFlag<WrappedState> iWrappedFlag) {
        if (iWrappedFlag == null) {
            return true;
        }
        Optional<WrappedState> optional = this.worldGuard.queryFlag(player, location, iWrappedFlag);
        return optional.orElse(WrappedState.ALLOW).equals((Object)WrappedState.ALLOW);
    }
}

