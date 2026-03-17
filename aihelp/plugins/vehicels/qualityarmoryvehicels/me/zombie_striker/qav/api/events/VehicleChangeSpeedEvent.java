/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 *  org.jetbrains.annotations.NotNull
 */
package me.zombie_striker.qav.api.events;

import me.zombie_striker.qav.VehicleEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class VehicleChangeSpeedEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private final VehicleEntity ve;
    private final double forwardBefore;
    private double forwardNow;

    public VehicleChangeSpeedEvent(VehicleEntity vehicleEntity, double d, double d2) {
        this.ve = vehicleEntity;
        this.forwardBefore = d;
        this.forwardNow = d2;
    }

    public VehicleEntity getVehicle() {
        return this.ve;
    }

    public void setNewSpeed(double d) {
        this.forwardNow = d;
    }

    public double getOldSpeed() {
        return this.forwardBefore;
    }

    public double getNewSpeed() {
        return this.forwardNow;
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

