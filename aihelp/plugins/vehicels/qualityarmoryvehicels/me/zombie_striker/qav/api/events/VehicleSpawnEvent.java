/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.player.PlayerEvent
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.api.events;

import me.zombie_striker.qav.VehicleEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class VehicleSpawnEvent
extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final VehicleEntity ve;

    public VehicleSpawnEvent(Player player, VehicleEntity vehicleEntity) {
        super(player);
        this.ve = vehicleEntity;
    }

    public VehicleEntity getVehicle() {
        return this.ve;
    }

    public boolean isCanceled() {
        return this.cancel;
    }

    public void setCanceled(boolean bl) {
        this.cancel = bl;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}

